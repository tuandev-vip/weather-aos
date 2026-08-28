package com.tuan.weatherworld.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuan.weatherworld.data.model.WeatherLocation
import com.tuan.weatherworld.data.repository.SavedLocationRepository
import com.tuan.weatherworld.data.repository.SelectedLocationRepository
import com.tuan.weatherworld.data.source.location.device.DeviceLocationProvider
import com.tuan.weatherworld.data.source.location.name.LocationNameResolver
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/** Trạng thái lấy, đặt tên và lưu vị trí thiết bị trong lần khởi động đầu tiên. */
sealed interface CurrentLocationUiState {

    data object Idle : CurrentLocationUiState

    data object Loading : CurrentLocationUiState

    data class Success(
        val location: WeatherLocation,
    ) : CurrentLocationUiState

    data class Error(
        val message: String,
    ) : CurrentLocationUiState
}

/**
 * Quyết định địa điểm khởi động của ứng dụng.
 *
 * Nếu DataStore đã có địa điểm được chọn, app dùng lại ngay. Nếu chưa có,
 * ViewModel lấy GPS, reverse geocode thành tên tỉnh/thành phố, lưu địa điểm vào
 * Room và DataStore rồi mới phát [CurrentLocationUiState.Success]. Lỗi đặt tên
 * không chặn tải thời tiết: app dùng tên dự phòng `Vị trí hiện tại`.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val selectedLocationRepository: SelectedLocationRepository,
    private val deviceLocationProvider: DeviceLocationProvider,
    private val savedLocationRepository: SavedLocationRepository,
    private val locationNameResolver: LocationNameResolver,
) : ViewModel() {

    // ---------- Trạng thái lấy vị trí lần đầu ----------
    private val _currentLocationState =
        MutableStateFlow<CurrentLocationUiState>(CurrentLocationUiState.Idle)

    val currentLocationState: StateFlow<CurrentLocationUiState> =
        _currentLocationState.asStateFlow()

    // ---------- Đọc location mặc định của người dùng quay lại ----------
    suspend fun getStartLocation(): WeatherLocation? {
        return selectedLocationRepository.selectedLocation.first()
    }

    // ---------- Lần đầu: GPS -> tên địa điểm -> lưu local ----------
    fun loadCurrentLocation() {
        if (_currentLocationState.value == CurrentLocationUiState.Loading) {
            return
        }

        viewModelScope.launch {
            _currentLocationState.value =
                CurrentLocationUiState.Loading

            val coordinates = deviceLocationProvider
                .getCurrentCoordinates()
                .getOrElse { throwable ->
                    _currentLocationState.value =
                        CurrentLocationUiState.Error(
                            message = throwable.message
                                ?: "Không thể lấy vị trí hiện tại",
                        )

                    return@launch
                }

            val displayName = locationNameResolver
                .resolveLocationName(coordinates)
                .getOrElse {
                    "Vị trí hiện tại"
                }

            val currentLocation = WeatherLocation(
                displayName = displayName,
                latitude = coordinates.latitude,
                longitude = coordinates.longitude,
            )

            saveInitialLocation(currentLocation)
                .onSuccess {
                    _currentLocationState.value =
                        CurrentLocationUiState.Success(
                            location = currentLocation,
                        )
                }
                .onFailure { throwable ->
                    _currentLocationState.value =
                        CurrentLocationUiState.Error(
                            message = throwable.message
                                ?: "Không thể lưu vị trí hiện tại",
                        )
                }
        }
    }

    // Room lưu favorite; DataStore lưu đúng một location đang được chọn.
    private suspend fun saveInitialLocation(
        location: WeatherLocation,
    ): Result<Unit> {
        savedLocationRepository
            .saveLocation(location)
            .getOrElse { throwable ->
                return Result.failure(throwable)
            }

        return selectedLocationRepository
            .saveSelectedLocation(location)
    }

    fun onLocationPermissionDenied() {
        _currentLocationState.value =
            CurrentLocationUiState.Error(
                message = "Ứng dụng chưa được cấp quyền vị trí",
            )
    }
}
