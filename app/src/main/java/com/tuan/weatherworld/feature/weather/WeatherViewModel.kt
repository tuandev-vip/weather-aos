package com.tuan.weatherworld.feature.weather

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

sealed interface WeatherUiState {
    data object Loading : WeatherUiState
    data class Success(val weather: Weather) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<WeatherUiState>(
        WeatherUiState.Loading,
    )

    val state: StateFlow<WeatherUiState> = _state.asStateFlow()

    init {
        loadWeather(DEFAULT_CITY)
    }

    fun loadWeather(cityName: String) {
        viewModelScope.launch {
            _state.value = WeatherUiState.Loading

            repository.getWeather(cityName)
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

    private companion object {
        const val DEFAULT_CITY = "Hải Phòng"
    }
}
