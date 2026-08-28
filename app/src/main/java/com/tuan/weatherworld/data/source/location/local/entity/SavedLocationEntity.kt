package com.tuan.weatherworld.data.source.location.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Một hàng trong bảng Room `saved_locations`.
 *
 * Chỉ một hàng được phép tồn tại cho cùng cặp vĩ độ/kinh độ; vì vậy thêm lại một
 * địa điểm sẽ bị `OnConflictStrategy.IGNORE` của DAO bỏ qua thay vì tạo bản sao.
 */
@Entity(
    tableName = "saved_locations",
    indices = [
        Index(
            value = ["latitude", "longitude"],
            unique = true,
        ),
    ],
)
data class SavedLocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "display_name")
    val displayName: String,

    val latitude: Double,

    val longitude: Double,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
)
