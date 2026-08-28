package com.tuan.weatherworld.data.mapper

import com.tuan.weatherworld.data.model.DailyForecast
import com.tuan.weatherworld.data.model.HourlyForecast
import com.tuan.weatherworld.data.model.Weather
import com.tuan.weatherworld.data.model.WeatherLocation
import com.tuan.weatherworld.data.source.weather.dto.OpenMeteoDailyDto
import com.tuan.weatherworld.data.source.weather.dto.OpenMeteoForecastDto
import com.tuan.weatherworld.data.source.weather.dto.OpenMeteoHourlyDto
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.roundToInt

/**
 * Ánh xạ DTO dự báo của Open-Meteo sang [Weather] mà ứng dụng sử dụng.
 *
 * [location] do phía gọi cung cấp vì endpoint dự báo chỉ nhận tọa độ và không trả
 * về tên địa điểm. Các danh sách lệch độ dài được bỏ qua an toàn bằng `getOrNull`.
 */
fun OpenMeteoForecastDto.toDomain(
    location: WeatherLocation,
): Weather {
    val currentTemperature = current.temperature.roundToInt()

    return Weather(
        location = location,
        temperature = currentTemperature,
        weatherCondition = weatherConditionFromCode(
            code = current.weatherCode,
        ),
        highTemperature = daily.maxTemperatures
            .firstOrNull()
            ?.roundToInt()
            ?: currentTemperature,
        lowTemperature = daily.minTemperatures
            .firstOrNull()
            ?.roundToInt()
            ?: currentTemperature,
        hourlyForecast = hourly.toDomain(),
        dailyForecast = daily.toDomain(),
    )
}

private fun OpenMeteoHourlyDto.toDomain(): List<HourlyForecast> {
    return times.mapIndexedNotNull { index, time ->
        val temperature = temperatures.getOrNull(index)
            ?: return@mapIndexedNotNull null

        val precipitationProbability =
            precipitationProbabilities.getOrNull(index)
                ?: return@mapIndexedNotNull null

        HourlyForecast(
            dateTime = LocalDateTime.parse(time),
            temperature = temperature.roundToInt(),
            precipitationProbability = precipitationProbability,
        )
    }
}

private fun OpenMeteoDailyDto.toDomain(): List<DailyForecast> {
    return dates.mapIndexedNotNull { index, date ->
        val lowTemperature = minTemperatures.getOrNull(index)
            ?: return@mapIndexedNotNull null

        val highTemperature = maxTemperatures.getOrNull(index)
            ?: return@mapIndexedNotNull null

        val precipitationProbability =
            maxPrecipitationProbabilities.getOrNull(index)
                ?: return@mapIndexedNotNull null

        DailyForecast(
            date = LocalDate.parse(date),
            lowTemperature = lowTemperature.roundToInt(),
            highTemperature = highTemperature.roundToInt(),
            precipitationProbability = precipitationProbability,
        )
    }
}

private fun weatherConditionFromCode(
    code: Int,
): String {
    return when (code) {
        0 -> "Trời quang"

        1, 2 -> "Trời hửng nắng"

        3 -> "Trời âm u"

        45, 48 -> "Có sương mù"

        51, 53, 55,
        56, 57,
            -> "Mưa phùn"

        61, 63, 65,
        66, 67,
            -> "Trời mưa"

        71, 73, 75,
        77,
            -> "Có tuyết"

        80, 81, 82 -> "Mưa rào"

        85, 86 -> "Mưa tuyết"

        95, 96, 99 -> "Mưa giông"

        else -> "Không xác định"
    }
}
