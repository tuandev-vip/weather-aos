package com.tuan.weatherworld.data.repository

import com.tuan.weatherworld.data.mapper.toDomain
import com.tuan.weatherworld.data.mapper.toEntity
import com.tuan.weatherworld.data.model.WeatherLocation
import com.tuan.weatherworld.data.source.location.local.dao.SavedLocationDao
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedLocationRepositoryImpl @Inject constructor(
private val savedLocationDao: SavedLocationDao,
) : SavedLocationRepository{
    override fun observeSavedLocations(): Flow<List<WeatherLocation>> {
        return savedLocationDao.observeAll().map { entities ->
            entities.map {
                entity -> entity.toDomain()
            }
        }
    }

    override suspend fun saveLocation(location: WeatherLocation): Result<SaveLocationResult> {
        return try {
            val rowId = savedLocationDao.insert(
                location = location.toEntity(),
            )

            val saveResult = if (rowId == -1L){
                SaveLocationResult.AlreadyExists
            }else {
                SaveLocationResult.Added
            }

            Result.success(saveResult)
        } catch (exception : CancellationException){
            throw exception
        }catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun deleteLocation(location: WeatherLocation): Result<Boolean> {
        return try {
            val deletedRows = savedLocationDao.deleteByCoordinates(
                latitude = location.latitude,
                longitude = location.longitude,
            )

            Result.success(deletedRows > 0)

        }catch (exception : CancellationException){
            throw exception
        }catch (exception : Exception){
            Result.failure(exception)
        }
    }

}