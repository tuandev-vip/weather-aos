package com.tuan.weatherworld

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Điểm khởi tạo Hilt ở cấp application.
 * `@HiltAndroidApp` sinh container phụ thuộc sống cùng tiến trình Weather World.
 */
@HiltAndroidApp
class WeatherWorldApp : Application()
