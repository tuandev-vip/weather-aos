package com.tuan.weatherworld.core.navigation

import android.net.Uri

/** Bảng route tập trung, tránh rải chuỗi địa chỉ màn hình khắp ứng dụng. */
object Routes {
    // Destination không cần tham số
    const val SPLASH = "splash"
    const val LOCATIONS = "locations"
    const val SETTING = "setting"

    // Route Weather có tham số
    const val WEATHER = "weather"
    const val ARG_LOCATION_NAME = "locationName"
    const val ARG_LOCATION_LATITUDE = "latitude"
    const val ARG_LOCATION_LONGITUDE = "longitude"

    // Mẫu route dùng khi đăng ký destination trong NavHost
    const val WEATHER_ROUTE =
        "$WEATHER/{$ARG_LOCATION_NAME}/{$ARG_LOCATION_LATITUDE}/{$ARG_LOCATION_LONGITUDE}"

    // Route thật dùng khi điều hướng; tên phải encode để dấu cách/ký tự đặc biệt an toàn
    fun weather(
        displayName: String,
        latitude: Double,
        longitude: Double,
    ) = "$WEATHER/${Uri.encode(displayName)}/$latitude/$longitude"

    // Route tìm kiếm có chế độ thường hoặc bắt buộc chọn địa điểm
    const val LOCATION_SEARCH = "location_search"
    const val ARG_LOCATION_REQUIRED = "locationRequired"
    const val LOCATION_SEARCH_ROUTE =
        "$LOCATION_SEARCH?$ARG_LOCATION_REQUIRED={$ARG_LOCATION_REQUIRED}"

    fun locationSearch(
        isRequired: Boolean = false,
    ): String {
        return "$LOCATION_SEARCH?$ARG_LOCATION_REQUIRED=$isRequired"
    }
}
