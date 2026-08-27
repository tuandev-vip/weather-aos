package com.tuan.weatherworld.data.repository

import com.tuan.weatherworld.data.location.DefaultWeatherLocations
import com.tuan.weatherworld.data.mapper.toDomain
import com.tuan.weatherworld.data.model.Weather
import com.tuan.weatherworld.data.model.WeatherLocation
import com.tuan.weatherworld.data.repository.mock.MockWeatherData.locations
import com.tuan.weatherworld.data.source.weather.WeatherRemoteDataSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource,
) : WeatherRepository {

    override suspend fun getWeather(
        location: WeatherLocation,
    ): Result<Weather> {
        return try {
            val weather = fetchWeather(
                location = location,
            )

            Result.success(weather)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun getLocationsWeather(locations: List<WeatherLocation>): Result<List<Weather>> {
        return try {
            val weatherList = coroutineScope {
                        locations.map { location ->
                        async {
                            fetchWeather(
                                location = location,
                            )
                        }
                    }
                    .awaitAll()
            }

            Result.success(weatherList)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun searchLocations(
        query: String,
    ): Result<List<WeatherLocation>> {
        val normalizedQuery = query.trim()

        if (normalizedQuery.length < 2) {
            return Result.success(emptyList())
        }

        return try {
            val responseDto = remoteDataSource.searchLocations(
                query = normalizedQuery,
            )

            val locations = responseDto.toDomain()

            Result.success(locations)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }


    private suspend fun fetchWeather(
        location: WeatherLocation,
    ): Weather {
        val dto = remoteDataSource.getForecast(
            latitude = location.latitude,
            longitude = location.longitude,
        )

        return dto.toDomain(
            location = location,
        )
    }
}