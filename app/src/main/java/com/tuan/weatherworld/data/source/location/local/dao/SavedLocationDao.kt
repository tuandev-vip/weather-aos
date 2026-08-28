package com.tuan.weatherworld.data.source.location.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tuan.weatherworld.data.source.location.local.entity.SavedLocationEntity
import kotlinx.coroutines.flow.Flow

/**
 * Các thao tác SQL được phép thực hiện trên danh sách địa điểm yêu thích.
 * Room tự sinh implementation; [observeAll] phát danh sách mới mỗi khi bảng đổi.
 */
@Dao
interface SavedLocationDao {

    @Query(
        """
        SELECT *
        FROM saved_locations
        ORDER BY created_at ASC, id ASC
        """,
    )
    fun observeAll(): Flow<List<SavedLocationEntity>>

    @Insert(
        onConflict = OnConflictStrategy.IGNORE,
    )
    suspend fun insert(
        location: SavedLocationEntity,
    ): Long

    @Query(
        """
        DELETE FROM saved_locations
        WHERE latitude = :latitude
        AND longitude = :longitude
        """,
    )
    suspend fun deleteByCoordinates(
        latitude: Double,
        longitude: Double,
    ): Int
}
