package com.tuan.weatherworld.data.repository.mock

import com.tuan.weatherworld.data.model.Weather
import com.tuan.weatherworld.data.repository.WeatherRepository

class MockWeatherRepository : WeatherRepository {
    override suspend fun getWeather(cityName: String): Result<Weather> {
        val weather = MockWeatherData.listWeather.firstOrNull { item ->
            item.cityName.trim().equals(
                cityName.trim(),
                ignoreCase = true,
            )
        }

        return if (weather != null) {
            Result.success(weather)
        } else {
            Result.failure(
                NoSuchElementException(
                    "Không tìm thấy thời tiết của $cityName",
                ),
            )
        }
    }
}
