package com.tuan.weatherworld.data.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Dữ liệu thời tiết ở dạng domain đã sẵn sàng cho ViewModel và UI sử dụng.
 *
 * Repository tạo model này bằng cách ánh xạ DTO của Open-Meteo. UI không phụ thuộc
 * vào Retrofit, JSON hoặc tên trường của API.
 */
data class Weather(
    val location: WeatherLocation,
    val temperature: Int,
    val weatherCondition: String,
    val highTemperature: Int,
    val lowTemperature: Int,

    val hourlyForecast: List<HourlyForecast> = emptyList(),
    val dailyForecast: List<DailyForecast> = emptyList(),
)

/** Một mốc dự báo theo giờ, bắt đầu từ giờ hiện tại do API trả về. */
data class HourlyForecast(
    val dateTime: LocalDateTime,
    val temperature: Int,
    val precipitationProbability: Int,
)

/** Một ngày dự báo gồm nhiệt độ thấp nhất, cao nhất và xác suất mưa lớn nhất. */
data class DailyForecast(
    val date: LocalDate,
    val lowTemperature: Int,
    val highTemperature: Int,
    val precipitationProbability: Int,
)
