package com.tuan.weatherworld.data.repository.mock

import com.tuan.weatherworld.data.model.Weather
import com.tuan.weatherworld.data.model.WeatherLocation
import com.tuan.weatherworld.data.repository.WeatherRepository
import javax.inject.Inject

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

    override suspend fun getLocationsWeather(): Result<List<Weather>> {
        return Result.success(MockWeatherData.locations)
    }
}
