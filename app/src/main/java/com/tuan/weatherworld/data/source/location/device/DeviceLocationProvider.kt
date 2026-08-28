package com.tuan.weatherworld.data.source.location.device

/** Tọa độ thô do dịch vụ vị trí của thiết bị trả về; chưa chứa tên địa điểm. */
data class DeviceCoordinates(
    val latitude: Double,
    val longitude: Double,
)

/**
 * Ranh giới lấy tọa độ hiện tại của thiết bị.
 * ViewModel phụ thuộc interface này nên không cần biết Google Play Services.
 */
interface DeviceLocationProvider {

    suspend fun getCurrentCoordinates(): Result<DeviceCoordinates>
}
