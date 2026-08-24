package com.tuan.weatherworld.data.source.weather.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenMeteoForecastDto(
    val current: OpenMeteoCurrentDto,
    val hourly: OpenMeteoHourlyDto,
    val daily: OpenMeteoDailyDto,
)

@Serializable
data class OpenMeteoCurrentDto(
    @SerialName("temperature_2m")
    val temperature: Double,
    @SerialName("weather_code")
    val weatherCode: Int,
)

@Serializable
data class OpenMeteoHourlyDto(
    @SerialName("time")
    val times: List<String>,
    @SerialName("temperature_2m")
    val temperatures: List<Double>,
    @SerialName("precipitation_probability")
    val precipitationProbabilities: List<Int>,
    @SerialName("weather_code")
    val weatherCodes: List<Int>,
)

@Serializable
data class OpenMeteoDailyDto(
    @SerialName("time")
    val dates: List<String>,
    @SerialName("weather_code")
    val weatherCodes: List<Int>,
    @SerialName("temperature_2m_max")
    val maxTemperatures: List<Double>,
    @SerialName("temperature_2m_min")
    val minTemperatures: List<Double>,
    @SerialName("precipitation_probability_max")
    val maxPrecipitationProbabilities: List<Int>,
)
