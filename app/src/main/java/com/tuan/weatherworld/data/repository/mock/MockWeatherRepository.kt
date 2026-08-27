package com.tuan.weatherworld.data.repository.mock

import com.tuan.weatherworld.data.model.Weather
import com.tuan.weatherworld.data.model.WeatherLocation
import com.tuan.weatherworld.data.repository.WeatherRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class MockWeatherRepository @Inject constructor() : WeatherRepository {
    override suspend fun getWeather(location: WeatherLocation): Result<Weather> {
        val weather = MockWeatherData.locations.firstOrNull { item ->
            item.location.displayName.trim().equals(
                location.displayName.trim(),
                ignoreCase = true,
            )
        }

        return if (weather != null) {
            Result.success(
                weather.copy(
                    hourlyForecast = MockWeatherData.createHourlyForecast(),
                    dailyForecast = MockWeatherData.createDailyForecast(),
                ),
            )
        } else {
            Result.failure(
                NoSuchElementException(
                    "Không tìm thấy thời tiết của ${location.displayName}",
                ),
            )
        }
    }

    override suspend fun getLocationsWeather(
        locations: List<WeatherLocation>,
    ): Result<List<Weather>> {
        val weatherList = mutableListOf<Weather>()

        for (location in locations) {
            val weather = getWeather(
                location = location,
            ).getOrElse { throwable ->
                return Result.failure(throwable)
            }

            weatherList += weather
        }

        return Result.success(weatherList)
    }

    override suspend fun searchLocations(
        query: String,
    ): Result<List<WeatherLocation>> {
        val normalizedQuery = query.trim()

        if (normalizedQuery.length < 2) {
            return Result.success(emptyList())
        }

        val locations = MockWeatherData.locations
            .map { weather -> weather.location }
            .filter { location ->
                location.displayName.contains(
                    other = normalizedQuery,
                    ignoreCase = true,
                )
            }

        return Result.success(locations)
    }
}
