package com.tuan.weatherworld.data.model

/**
 * Đơn vị nhiệt độ mà người dùng chọn để hiển thị trong ứng dụng.
 *
 * Dữ liệu thời tiết trong domain vẫn được giữ ở Celsius.
 * Fahrenheit chỉ được chuyển đổi khi chuẩn bị hiển thị lên UI.
 */
enum class TemperatureUnit {
    CELSIUS,
    FAHRENHEIT,
}