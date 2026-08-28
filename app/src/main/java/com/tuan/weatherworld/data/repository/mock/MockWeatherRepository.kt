package com.tuan.weatherworld.data.repository.mock

import com.tuan.weatherworld.core.common.AppResult
import com.tuan.weatherworld.core.error.AppError
import com.tuan.weatherworld.data.model.Weather
import com.tuan.weatherworld.data.model.WeatherLocation
import com.tuan.weatherworld.data.repository.WeatherRepository
import javax.inject.Inject

/**
 * Test double chạy hoàn toàn trong bộ nhớ cho [WeatherRepository].
 *
 * Lớp được giữ lại để demo hoặc test mà không cần mạng; production hiện được Hilt
 * bind với [com.tuan.weatherworld.data.repository.WeatherRepositoryImpl].
 */
class MockWeatherRepository @Inject constructor() : WeatherRepository {
    override suspend fun getWeather(location: WeatherLocation): AppResult<Weather> {
        val weather = MockWeatherData.locations.firstOrNull { item ->
            item.location.displayName.trim().equals(
                location.displayName.trim(),
                ignoreCase = true,
            )
        }

        return if (weather != null) {
            AppResult.Success(
                data = weather.copy(
                    hourlyForecast = MockWeatherData.createHourlyForecast(),
                    dailyForecast = MockWeatherData.createDailyForecast(),
                ),
            )
        } else {
            val throwable = NoSuchElementException(
                "Không tìm thấy thời tiết của ${location.displayName}",
            )

            AppResult.Error(
                error = AppError.Unknown,
                throwable = throwable
            )
        }
    }

    override suspend fun getLocationsWeather(
        locations: List<WeatherLocation>,
    ): AppResult<List<Weather>> {
        val weatherList = mutableListOf<Weather>()

        for (location in locations) {
            when (val result = getWeather(location)) {
                is AppResult.Success -> {
                    weatherList += result.data
                }

                is AppResult.Error -> {
                    return result
                }
            }
        }

        return AppResult.Success(
            data = weatherList,
        )
    }

    override suspend fun searchLocations(
        query: String,
    ): AppResult<List<WeatherLocation>> {
        val normalizedQuery = query.trim()

        if (normalizedQuery.length < 2) {
            return AppResult.Success(
                data = emptyList(),
            )
        }

        val locations = MockWeatherData.locations
            .map { weather -> weather.location }
            .filter { location ->
                location.displayName.contains(
                    other = normalizedQuery,
                    ignoreCase = true,
                )
            }

        return AppResult.Success(
            data = locations,
        )
    }
}
