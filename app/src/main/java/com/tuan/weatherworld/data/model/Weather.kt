package com.tuan.weatherworld.data.model

data class Weather(
    val cityName: String,
    val temperature: Int,
    val weatherCondition: String,
    val highTemperature: Int,
    val lowTemperature: Int,
)