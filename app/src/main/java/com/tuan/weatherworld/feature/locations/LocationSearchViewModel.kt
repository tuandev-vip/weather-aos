package com.tuan.weatherworld.feature.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuan.weatherworld.data.model.WeatherLocation
import com.tuan.weatherworld.data.repository.SaveLocationResult
import com.tuan.weatherworld.data.repository.SavedLocationRepository
import com.tuan.weatherworld.data.repository.SelectedLocationRepository
import com.tuan.weatherworld.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Trạng thái riêng của request tìm tên địa điểm qua Open-Meteo Geocoding. */
sealed interface LocationSearchUiState {
    data object Idle : LocationSearchUiState

    data object Loading : LocationSearchUiState

    data class Success(val locations: List<WeatherLocation>) : LocationSearchUiState

    data class Error(val message: String) : LocationSearchUiState

    data object NoResults : LocationSearchUiState
}

/**
 * Trạng thái của thao tác lưu item đã chọn.
 * Tách khỏi [LocationSearchUiState] để request tìm kiếm và ghi Room/DataStore
 * không dùng chung một cờ Loading hoặc ghi đè kết quả của nhau.
 */
sealed class SavedLocationUiState {
    data object Idle : SavedLocationUiState()
    data object Adding : SavedLocationUiState()
    data object AlreadyExists : SavedLocationUiState()
    data class Added(val location: WeatherLocation) : SavedLocationUiState()
    data class Error(val message: String) : SavedLocationUiState()
}

/**
 * Điều phối hai luồng của màn tìm kiếm địa điểm.
 *
 * Luồng tìm kiếm debounce 500 ms rồi gọi Geocoding API. Luồng chọn item lưu vào
 * Room trước, sau đó lưu cùng địa điểm vào DataStore làm location mặc định; chỉ
 * khi cả chuỗi thành công Screen mới nhận [SavedLocationUiState.Added] để điều hướng.
 */
@HiltViewModel
class LocationSearchViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val savedLocationRepository: SavedLocationRepository,
    private val selectedLocationRepository: SelectedLocationRepository,
) : ViewModel() {

    // ---------- Trạng thái tìm kiếm ----------
    private val _searchQuery = MutableStateFlow("")

    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    private val _state = MutableStateFlow<LocationSearchUiState>(LocationSearchUiState.Idle)

    val state: StateFlow<LocationSearchUiState> = _state.asStateFlow()

    // ---------- Trạng thái lưu và chọn địa điểm ----------
    private val _savedLocationState =
        MutableStateFlow<SavedLocationUiState>(SavedLocationUiState.Idle)
    val savedLocationState: StateFlow<SavedLocationUiState> = _savedLocationState.asStateFlow()

    init {
        observeSearchQuery()
    }

    // ---------- Luồng tìm kiếm có debounce ----------
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchQuery.map { query -> query.trim() }
                .distinctUntilChanged()
                .debounce(500L)
                .collectLatest { query ->
                    searchLocations(query = query)
                }
        }
    }

    private suspend fun searchLocations(query: String) {
        if (query.length < 2) {
            _state.value = LocationSearchUiState.Idle
            return
        }

        _state.value = LocationSearchUiState.Loading

        weatherRepository.searchLocations(query = query)
            .onSuccess { locations ->
                _state.value =
                    if (locations.isEmpty()) {
                        LocationSearchUiState.NoResults
                    } else {
                        LocationSearchUiState.Success(
                            locations = locations
                        )
                    }
            }
            .onFailure { throwable ->
                _state.value =
                    LocationSearchUiState.Error(
                        message = throwable.message ?: "Không thể tìm địa điểm",
                    )
            }
    }

    // ---------- Luồng Room -> DataStore -> điều hướng ----------
    fun onLocationSelected(location: WeatherLocation) {
        if (_savedLocationState.value == SavedLocationUiState.Adding) {
            return
        }

        viewModelScope.launch {
            addLocationToDatabase(location)
        }
    }

    private suspend fun addLocationToDatabase(location: WeatherLocation) {
        _savedLocationState.value = SavedLocationUiState.Adding
        savedLocationRepository.saveLocation(location)
            .onSuccess { result ->
                when (result) {

                    is SaveLocationResult.Added -> { saveAsSelectedLocation(location) }

                    is SaveLocationResult.AlreadyExists -> { saveAsSelectedLocation(location) }
                }
            }
            .onFailure { throwable ->
                _savedLocationState.value = SavedLocationUiState.Error(
                    message = throwable.message ?: "Không thể thêm địa chỉ yêu thích"
                )
            }
    }

    fun onSavedLocationResultHandled() {
        if (_savedLocationState.value != SavedLocationUiState.Adding) {
            _savedLocationState.value = SavedLocationUiState.Idle
        }
    }

    private suspend fun saveAsSelectedLocation(
        location: WeatherLocation,
    ) {
        selectedLocationRepository.saveSelectedLocation(location)
            .onSuccess {
                _savedLocationState.value = SavedLocationUiState.Added(location)
            }
            .onFailure { throwable ->
                _savedLocationState.value = SavedLocationUiState.Error(
                    message = throwable.message
                        ?: "Không thể lưu địa điểm đang chọn",
                )
            }
    }
}
