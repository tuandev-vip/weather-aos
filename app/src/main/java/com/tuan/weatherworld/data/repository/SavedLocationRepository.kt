package com.tuan.weatherworld.data.repository

import com.tuan.weatherworld.data.model.WeatherLocation
import kotlinx.coroutines.flow.Flow

/** Kết quả nghiệp vụ khi thêm một địa điểm vào danh sách yêu thích trong Room. */
sealed interface SaveLocationResult {
    data object Added : SaveLocationResult
    data object AlreadyExists : SaveLocationResult
}

/**
 * Hợp đồng quản lý nhiều địa điểm yêu thích đã lưu.
 *
 * Room là nguồn sự thật của danh sách này. [observeSavedLocations] trả [Flow] để
 * màn hình tự nhận danh sách mới mỗi khi bảng `saved_locations` thay đổi.
 */
interface SavedLocationRepository {

    fun observeSavedLocations(): Flow<List<WeatherLocation>>

    suspend fun saveLocation(
        location: WeatherLocation,
    ): Result<SaveLocationResult>

    suspend fun deleteLocation(
        location: WeatherLocation,
    ): Result<Boolean>
}
