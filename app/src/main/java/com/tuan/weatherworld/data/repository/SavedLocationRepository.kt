package com.tuan.weatherworld.data.repository

import com.tuan.weatherworld.data.model.WeatherLocation
import kotlinx.coroutines.flow.Flow

sealed interface SaveLocationResult {
    data object Added : SaveLocationResult
    data object AlreadyExists : SaveLocationResult
}

interface SavedLocationRepository {

    fun observeSavedLocations(): Flow<List<WeatherLocation>>

    suspend fun saveLocation(
        location: WeatherLocation,
    ): Result<SaveLocationResult>

    suspend fun deleteLocation(
        location: WeatherLocation,
    ): Result<Boolean>
}