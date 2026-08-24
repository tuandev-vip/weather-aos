package com.tuan.weatherworld.data.repository

import com.tuan.weatherworld.data.location.DefaultWeatherLocations
import com.tuan.weatherworld.data.mapper.toDomain
import com.tuan.weatherworld.data.model.Weather
import com.tuan.weatherworld.data.model.WeatherLocation
import com.tuan.weatherworld.data.source.weather.WeatherRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

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

    override suspend fun getLocationsWeather(): Result<List<Weather>> {
        return try {
            val weatherList = coroutineScope {
                DefaultWeatherLocations.all
                    .map { location ->
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