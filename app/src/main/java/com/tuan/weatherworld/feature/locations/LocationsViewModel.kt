package com.tuan.weatherworld.feature.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuan.weatherworld.data.model.Weather
import com.tuan.weatherworld.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LocationsUiState {
    data object Loading : LocationsUiState
    data class Success(val locations: List<Weather>) : LocationsUiState
    data class Error(val message: String) : LocationsUiState
}

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<LocationsUiState>(
        LocationsUiState.Loading,
    )

    val uiState: StateFlow<LocationsUiState> = _state.asStateFlow()

    init {
        loadLocations()
    }

    fun loadLocations() {
        viewModelScope.launch {
            _state.value = LocationsUiState.Loading

            repository.getLocationsWeather()
                .onSuccess { locations ->
                    _state.value = LocationsUiState.Success(
                        locations = locations,
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
}
