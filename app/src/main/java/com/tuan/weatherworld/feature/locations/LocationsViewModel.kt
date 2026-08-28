package com.tuan.weatherworld.feature.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuan.weatherworld.data.model.Weather
import com.tuan.weatherworld.data.model.WeatherLocation
import com.tuan.weatherworld.data.repository.SavedLocationRepository
import com.tuan.weatherworld.data.repository.SelectedLocationRepository
import com.tuan.weatherworld.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Trạng thái danh sách favorites sau khi kết hợp địa điểm Room với Forecast API. */
sealed interface LocationsUiState {
    data object Loading : LocationsUiState

    data object Empty : LocationsUiState
    data class Success(val locations: List<Weather>) : LocationsUiState
    data class Error(val message: String) : LocationsUiState
}

/**
 * Quan sát danh sách địa điểm yêu thích và tải thời tiết tương ứng.
 *
 * Room là nguồn sự thật cho danh sách. Mỗi emission được chuyển thành yêu cầu
 * Forecast; `collectLatest` hủy lượt tải cũ nếu danh sách thay đổi. Khi người dùng
 * chọn card, ViewModel lưu location vào DataStore trước rồi mới cho phép điều hướng.
 */
@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val savedLocationRepository: SavedLocationRepository,
    private val selectedLocationRepository: SelectedLocationRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<LocationsUiState>(
        LocationsUiState.Loading,
    )
    val uiState: StateFlow<LocationsUiState> = _state.asStateFlow()

    // ---------- Quan sát Room và tải Forecast ----------
    init {
        observeSavedLocations()
    }

    private fun observeSavedLocations() {
        viewModelScope.launch {
            savedLocationRepository.observeSavedLocations()
                .catch { throwable ->
                    _state.value = LocationsUiState.Error(
                        message = throwable.message ?: "Không thể đọc danh sách địa điểm",
                    )
                }
                .collectLatest { locations ->
                    if (locations.isEmpty()) {
                        _state.value = LocationsUiState.Empty
                    } else {
                        loadLocations(locations = locations)
                    }
                }
        }
    }

    private suspend fun loadLocations(locations: List<WeatherLocation>) {
            _state.value = LocationsUiState.Loading

            weatherRepository.getLocationsWeather(locations = locations)
                .onSuccess { weatherList ->
                    _state.value = LocationsUiState.Success(
                        locations = weatherList,
                    )

                }
                .onFailure { throwable ->
                    _state.value = LocationsUiState.Error(
                        message = throwable.message
                            ?: "Không thể tải danh sách thành phố",
                    )
                }
    }

    // ---------- Chọn địa điểm mặc định ----------
    fun selectLocation(
        location: WeatherLocation,
        onSelected: (WeatherLocation) -> Unit
    ){
        viewModelScope.launch {
            selectedLocationRepository.saveSelectedLocation(location)
                .onSuccess {
                    onSelected(location)
                }
                .onFailure { throwable ->
                    _state.value = LocationsUiState.Error(
                        message = throwable.message  ?: "Không thể lưu địa điểm đang chọn",
                    )
                }
        }
    }

}
