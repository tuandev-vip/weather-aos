package com.tuan.weatherworld.data.repository

import com.tuan.weatherworld.core.common.AppResult
import com.tuan.weatherworld.data.model.TemperatureUnit
import kotlinx.coroutines.flow.Flow

/**
 * Hợp đồng đọc và thay đổi cài đặt của Weather World.
 *
 * Tầng sử dụng repository không cần biết dữ liệu được lưu bằng DataStore,
 * SharedPreferences hay một phương thức lưu trữ khác.
 */
interface SettingsRepository {

    val temperatureUnit: Flow<TemperatureUnit>

    suspend fun saveTemperatureUnit( unit: TemperatureUnit ): AppResult<Unit>
}