package com.tuan.weatherworld.data.source.location.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tuan.weatherworld.data.source.location.local.entity.SavedLocationEntity
import kotlinx.coroutines.flow.Flow

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