package com.tuan.weatherworld.feature.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuan.weatherworld.data.model.Weather
import com.tuan.weatherworld.data.model.WeatherLocation
import com.tuan.weatherworld.data.repository.SavedLocationRepository
import com.tuan.weatherworld.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LocationsUiState {
    data object Loading : LocationsUiState

    data object Empty : LocationsUiState
    data class Success(val locations: List<Weather>) : LocationsUiState
    data class Error(val message: String) : LocationsUiState
}

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val savedLocationRepository: SavedLocationRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<LocationsUiState>(
        LocationsUiState.Loading,
    )
    val uiState: StateFlow<LocationsUiState> = _state.asStateFlow()

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


}
