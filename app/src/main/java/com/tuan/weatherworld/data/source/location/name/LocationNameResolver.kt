package com.tuan.weatherworld.data.source.location.name

import com.tuan.weatherworld.data.source.location.device.DeviceCoordinates

/**
 * Hợp đồng reverse geocoding: đổi tọa độ GPS thành tên tỉnh/thành phố hiển thị.
 * Tách khỏi [com.tuan.weatherworld.data.source.location.device.DeviceLocationProvider]
 * để việc lấy tọa độ và việc diễn giải tọa độ có thể thay thế/test độc lập.
 */
interface LocationNameResolver {

    suspend fun resolveLocationName(
        coordinates: DeviceCoordinates,
    ): Result<String>
}
