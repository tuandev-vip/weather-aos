package com.tuan.weatherworld.data.repository.mock

import com.tuan.weatherworld.data.model.DailyForecast
import com.tuan.weatherworld.data.model.HourlyForecast
import com.tuan.weatherworld.data.model.Weather
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object MockWeatherData {
    val locations: List<Weather> = listOf(
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
        Weather(
            cityName = "Thái Nguyên",
            temperature = 36,
            weatherCondition = "Trời nắng",
            highTemperature = 38,
            lowTemperature = 33,
        ),
        Weather(
            cityName = "Đà Nẵng",
            temperature = 36,
            weatherCondition = "Trời nắng",
            highTemperature = 38,
            lowTemperature = 33,
        ),
    )

    private val hourlyTemperatures = listOf(
        30, 30, 31, 32, 33, 33,
        31, 30, 31, 32, 33, 33,
        31, 30, 31, 32, 33, 33,
        31, 30, 31, 32, 31, 30,
    )

    private val hourlyPrecipitationProbabilities = listOf(
        10, 10, 5, 0, 0, 20,
        40, 10, 5, 0, 0, 20,
        40, 10, 5, 0, 0, 20,
        40, 10, 5, 0, 10, 20,
    )

    fun createHourlyForecast(
        now: LocalDateTime = LocalDateTime.now(),
    ): List<HourlyForecast> {
        val currentHour = now.truncatedTo(ChronoUnit.HOURS)

        return List(24) { index ->
            HourlyForecast(
                dateTime = currentHour.plusHours(index.toLong()),
                temperature = hourlyTemperatures[index],
                precipitationProbability =
                    hourlyPrecipitationProbabilities[index],
            )
        }
    }

    private val dailyLowTemperatures = listOf(
        24, 24, 23, 24, 25,
        24, 23, 24, 25, 24,
    )

    private val dailyHighTemperatures = listOf(
        30, 29, 31, 30, 32,
        31, 30, 32, 33, 31,
    )

    private val dailyPrecipitationProbabilities = listOf(
        75, 80, 60, 40, 20,
        30, 50, 70, 40, 25,
    )

    fun createDailyForecast(
        today: LocalDate = LocalDate.now(),
    ): List<DailyForecast> {
        return List(10) { index ->
            DailyForecast(
                date = today.plusDays(index.toLong()),
                lowTemperature = dailyLowTemperatures[index],
                highTemperature = dailyHighTemperatures[index],
                precipitationProbability =
                    dailyPrecipitationProbabilities[index],
            )
        }
    }
}
