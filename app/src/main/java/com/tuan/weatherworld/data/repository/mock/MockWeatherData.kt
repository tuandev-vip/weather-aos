package com.tuan.weatherworld.data.repository.mock

import com.tuan.weatherworld.data.model.Weather

object MockWeatherData {
    val listWeather: List<Weather> = listOf(
        Weather(
            cityName = "Hà Nội",
            temperature = 27,
            weatherCondition = "Trời mưa",
            highTemperature = 30,
            lowTemperature = 24,
        ),
        Weather(
            cityName = "Ba Vì",
            temperature = 24,
            weatherCondition = "Trời âm u",
            highTemperature = 30,
            lowTemperature = 24,
        ),
        Weather(
            cityName = "Hải Phòng",
            temperature = 30,
            weatherCondition = "Trời hửng nắng",
            highTemperature = 33,
            lowTemperature = 25,
        ),
        Weather(
            cityName = "TP Hồ Chí Minh",
            temperature = 27,
            weatherCondition = "Trời mưa",
            highTemperature = 30,
            lowTemperature = 24,
        ),
        Weather(
            cityName = "Cà Mau",
            temperature = 36,
            weatherCondition = "Trời nắng",
            highTemperature = 38,
            lowTemperature = 33,
        ),
    )
}
