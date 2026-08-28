package com.tuan.weatherworld.data.source.location.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tuan.weatherworld.data.source.location.local.dao.SavedLocationDao
import com.tuan.weatherworld.data.source.location.local.entity.SavedLocationEntity

/**
 * Điểm cấu hình Room của Weather World.
 *
 * [entities] khai báo các bảng, [version] là phiên bản schema và mỗi DAO được lộ
 * ra bằng một hàm abstract để Room sinh implementation khi biên dịch.
 */
@Database(
    entities = [
        SavedLocationEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun savedLocationDao(): SavedLocationDao

    companion object {
        const val DATABASE_NAME = "weather_world.db"
    }
}
