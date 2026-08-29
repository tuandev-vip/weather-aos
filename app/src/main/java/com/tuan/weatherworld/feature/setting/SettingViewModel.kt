package com.tuan.weatherworld.feature.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuan.weatherworld.core.common.AppResult
import com.tuan.weatherworld.core.ui.UiText
import com.tuan.weatherworld.core.ui.toUiText
import com.tuan.weatherworld.data.model.TemperatureUnit
import com.tuan.weatherworld.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingUiState(
    val isLoading: Boolean = true,
    val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val isSaving: Boolean = false,
    val errorMessage: UiText? = null
)

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingUiState())

    val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()

    init {
        observeTemperatureUnit()
    }

    private fun observeTemperatureUnit() {
        viewModelScope.launch {
            settingsRepository.temperatureUnit.collect { savedState ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        temperatureUnit = savedState
                    )
                }
            }
        }
    }


    fun selectTemperatureUnit(temperature: TemperatureUnit) {
        val currentState = _uiState.value

        if (currentState.isSaving || currentState.temperatureUnit == temperature) { return }

        _uiState.update { oldState -> oldState.copy(isSaving = true, errorMessage = null) }

        viewModelScope.launch {
            val result = settingsRepository.saveTemperatureUnit(temperature)

            when (result) {
                is AppResult.Success -> {
                    _uiState.update { oldState ->
                        oldState.copy(
                            isSaving = false
                        )
                    }
                }

                is AppResult.Error -> {
                    _uiState.update { oldState ->
                        oldState.copy(
                            isSaving = false,
                            errorMessage = result.error.toUiText()
                        )
                    }
                }
            }
        }


    }


}