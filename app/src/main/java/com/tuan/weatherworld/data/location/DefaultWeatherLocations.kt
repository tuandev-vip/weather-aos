package com.tuan.weatherworld.data.location

import com.tuan.weatherworld.data.model.WeatherLocation


object DefaultWeatherLocations {

    val haNoi = WeatherLocation(
        displayName = "Hà Nội",
        latitude = 21.0285,
        longitude = 105.8542,
    )

    val baVi = WeatherLocation(
        displayName = "Ba Vì",
        latitude = 21.1992,
        longitude = 105.4230,
    )

    val haiPhong = WeatherLocation(
        displayName = "Hải Phòng",
        latitude = 20.8449,
        longitude = 106.6881,
    )

    val hoChiMinhCity = WeatherLocation(
        displayName = "TP Hồ Chí Minh",
        latitude = 10.8231,
        longitude = 106.6297,
    )

    val caMau = WeatherLocation(
        displayName = "Cà Mau",
        latitude = 9.1769,
        longitude = 105.1524,
    )

    val thaiNguyen = WeatherLocation(
        displayName = "Thái Nguyên",
        latitude = 21.5672,
        longitude = 105.8252,
    )

    val daNang = WeatherLocation(
        displayName = "Đà Nẵng",
        latitude = 16.0544,
        longitude = 108.2022,
    )

    // Danh sách all chứa 7 location theo đúng thứ tự hiển thị
    val all: List<WeatherLocation> = listOf(
        haNoi,
        baVi,
        haiPhong,
        hoChiMinhCity,
        caMau,
        thaiNguyen,
        daNang,
    )
}