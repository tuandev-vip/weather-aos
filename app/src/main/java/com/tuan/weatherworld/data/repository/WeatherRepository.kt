package com.tuan.weatherworld.data.repository

import com.tuan.weatherworld.data.model.Weather

interface WeatherRepository {
    suspend fun getWeather(cityName: String): Result<Weather>
    suspend fun getLocationsWeather(): Result<List<Weather>>
}
