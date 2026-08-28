package com.tuan.weatherworld.data.repository

import com.tuan.weatherworld.data.model.Weather
import com.tuan.weatherworld.data.model.WeatherLocation

/**
 * Hợp đồng dữ liệu thời tiết dành cho ViewModel.
 *
 * Các hàm chỉ nhận và trả domain model; chi tiết Retrofit, DTO và Open-Meteo được
 * che bên dưới implementation. [Result] giúp lỗi mạng hoặc lỗi ánh xạ được chuyển
 * về ViewModel mà không để exception làm hỏng coroutine của màn hình.
 */
interface WeatherRepository {
    suspend fun getWeather(location: WeatherLocation): Result<Weather>
    suspend fun getLocationsWeather(locations: List<WeatherLocation>): Result<List<Weather>>

    suspend fun searchLocations(query: String):  Result<List<WeatherLocation>>
}
