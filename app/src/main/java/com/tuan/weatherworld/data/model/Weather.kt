package com.tuan.weatherworld.data.model

import java.time.LocalDate
import java.time.LocalDateTime

data class Weather(
    val location: WeatherLocation,
    val temperature: Int,
    val weatherCondition: String,
    val highTemperature: Int,
    val lowTemperature: Int,

    val hourlyForecast: List<HourlyForecast> = emptyList(),
    val dailyForecast: List<DailyForecast> = emptyList(),
)

data class HourlyForecast(
    val dateTime: LocalDateTime,
    val temperature: Int,
    val precipitationProbability: Int,
)

data class DailyForecast(
    val date: LocalDate,
    val lowTemperature: Int,
    val highTemperature: Int,
    val precipitationProbability: Int,
)
