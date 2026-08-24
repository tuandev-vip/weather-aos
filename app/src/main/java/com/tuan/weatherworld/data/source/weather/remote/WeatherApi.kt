package com.tuan.weatherworld.data.source.weather.remote

import com.tuan.weatherworld.data.source.weather.dto.OpenMeteoForecastDto
import retrofit2.http.Query
import retrofit2.http.GET

interface WeatherApi {
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude")
        latitude: Double,

        @Query("longitude")
        longitude: Double,

        @Query("current")
        current: String = "temperature_2m,weather_code",

        @Query("hourly")
        hourly: String =
            "temperature_2m,precipitation_probability,weather_code",

        @Query("daily")
        daily: String =
            "weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max",

        @Query("forecast_hours")
        forecastHours: Int = 24,

        @Query("forecast_days")
        forecastDays: Int = 10,

        @Query("timezone")
        timezone: String = "auto",

        @Query("temperature_unit")
        temperatureUnit: String = "celsius",
    ): OpenMeteoForecastDto
}