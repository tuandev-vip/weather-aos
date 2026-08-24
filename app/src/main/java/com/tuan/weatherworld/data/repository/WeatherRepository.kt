package com.tuan.weatherworld.data.repository

import com.tuan.weatherworld.data.model.Weather
import com.tuan.weatherworld.data.model.WeatherLocation

interface WeatherRepository {
    suspend fun getWeather(location: WeatherLocation): Result<Weather>
    suspend fun getLocationsWeather(): Result<List<Weather>>
}
