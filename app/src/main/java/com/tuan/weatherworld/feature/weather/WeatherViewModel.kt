package com.tuan.weatherworld.feature.weather

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuan.weatherworld.core.navigation.Routes
import com.tuan.weatherworld.data.location.DefaultWeatherLocations
import com.tuan.weatherworld.data.model.Weather
import com.tuan.weatherworld.data.model.WeatherLocation
import com.tuan.weatherworld.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Trạng thái tải dự báo của đúng một địa điểm trên WeatherScreen. */
sealed interface WeatherUiState {
    data object Loading : WeatherUiState
    data class Success(val weather: Weather) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}

/**
 * Đọc địa điểm từ navigation argument và tải dự báo qua [WeatherRepository].
 *
 * [SavedStateHandle] giữ tên, vĩ độ và kinh độ do `Routes.weather(...)` truyền
 * sang. Đà Nẵng chỉ là fallback bảo vệ khi argument thiếu hoặc tọa độ không parse
 * được; location đã chọn bình thường luôn đi theo route.
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow<WeatherUiState>(
        WeatherUiState.Loading,
    )

    val state: StateFlow<WeatherUiState> = _state.asStateFlow()

    // ---------- Đọc navigation arguments ----------
    private val defaultLocation = DefaultWeatherLocations.daNang

    private val locationName: String =
        savedStateHandle
            .get<String>(Routes.ARG_LOCATION_NAME)
            ?.takeIf { it.isNotBlank() }
            ?: defaultLocation.displayName

    private val latitude: Double =
        savedStateHandle
            .get<String>(Routes.ARG_LOCATION_LATITUDE)
            ?.toDoubleOrNull()
            ?: defaultLocation.latitude

    private val longitude: Double =
        savedStateHandle
            .get<String>(Routes.ARG_LOCATION_LONGITUDE)
            ?.toDoubleOrNull()
            ?: defaultLocation.longitude

    private val location = WeatherLocation(
        displayName = locationName,
        latitude = latitude,
        longitude = longitude,
    )

    init {
        loadWeather(location)
    }

    // ---------- Tải dữ liệu và cập nhật UiState ----------
    fun loadWeather(location: WeatherLocation) {
        viewModelScope.launch {
            _state.value = WeatherUiState.Loading

            repository.getWeather(location)
                .onSuccess { weather ->
                    _state.value = WeatherUiState.Success(
                        weather = weather,
                    )
                }
                .onFailure { throwable ->
                    _state.value = WeatherUiState.Error(
                        message = throwable.message
                            ?: "Không thể tải dữ liệu thời tiết",
                    )
                }
        }
    }


}
