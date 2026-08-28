package com.tuan.weatherworld.data.model

/**
 * Địa điểm ở dạng domain mà các tầng của ứng dụng cùng sử dụng.
 *
 * [displayName] chỉ phục vụ hiển thị; cặp [latitude] và [longitude] mới là dữ liệu
 * được dùng để gọi API dự báo và nhận diện một địa điểm trong cơ sở dữ liệu Room.
 * Model này không tự tìm tên hay tự tải thời tiết.
 */
data class WeatherLocation(
    val displayName: String,
    val latitude: Double,
    val longitude: Double,
)
