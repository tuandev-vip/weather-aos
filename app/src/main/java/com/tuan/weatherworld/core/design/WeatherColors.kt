package com.tuan.weatherworld.core.design

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Các màu dùng chung cho component Material 3 trong toàn ứng dụng.
 *
 * UI của từng feature nên đọc các vai trò màu từ MaterialTheme, không khai báo
 * những màu rời rạc ngay trong Screen hoặc component.
 */
internal val LightWeatherColorScheme = lightColorScheme(
    primary = Color(0xFFDCD7D7),
    secondary = Color(0xFF006879),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFF3A3A46),
    onBackground = Color(0xFF57575D),
    scrim = Color(0xFFBCBCD2),
    surface = Color(0xFFABABB7),
    onSurface = Color(0xFFFDFDFF),
    error = Color(0xFFD00808),
    primaryFixed = Color(0xFF1C4BDA),
    tertiary = Color(0xFF0C0C10),

)

internal val DarkWeatherColorScheme = darkColorScheme(
    primary = Color(0xFFDCD7D7),
    secondary = Color(0xFF006879),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFF3A3A46),
    onBackground = Color(0xFF57575D),
    scrim = Color(0xFFBCBCD2),
    surface = Color(0xFFABABB7),
    onSurface = Color(0xFF43434B),
    error = Color(0xFFD00808),
    primaryFixed = Color(0xFF063E81),
    tertiary = Color(0xFFDCD7D7),
)

/**
 * Bảng màu ngữ nghĩa cho card hoặc hình minh họa của một trạng thái thời tiết.
 *
 * Hai màu nền tạo gradient, [accent] nhấn mạnh icon/giá trị chính và [content]
 * được dùng cho chữ, icon nằm trên gradient.
 */
@Immutable
data class WeatherConditionPalette(
    val backgroundStart: Color,
    val backgroundEnd: Color,
    val accent: Color,
    val content: Color,
)

/** Các vai trò màu riêng của thời tiết không có sẵn trong Material ColorScheme. */
@Immutable
data class WeatherConditionColors(
    val sunny: WeatherConditionPalette,
    val rainy: WeatherConditionPalette,
    val thunderstorm: WeatherConditionPalette,
    val cloudy: WeatherConditionPalette,
)

internal val LightWeatherConditionColors = WeatherConditionColors(
    sunny = WeatherConditionPalette(
        backgroundStart = Color(0xFF1565C0),
        backgroundEnd = Color(0xFF0D47A1),
        accent = Color(0xFFFFD54F),
        content = Color(0xFFFFFFFF),
    ),
    rainy = WeatherConditionPalette(
        backgroundStart = Color(0xFF546E7A),
        backgroundEnd = Color(0xFF263238),
        accent = Color(0xFF90CAF9),
        content = Color(0xFFFFFFFF),
    ),
    thunderstorm = WeatherConditionPalette(
        backgroundStart = Color(0xFF3949AB),
        backgroundEnd = Color(0xFF1A1B2E),
        accent = Color(0xFFFFD54F),
        content = Color(0xFFFFFFFF),
    ),
    cloudy = WeatherConditionPalette(
        backgroundStart = Color(0xFF546E7A),
        backgroundEnd = Color(0xFF455A64),
        accent = Color(0xFFCFD8DC),
        content = Color(0xFFFFFFFF),
    ),
)

internal val DarkWeatherConditionColors = WeatherConditionColors(
    sunny = WeatherConditionPalette(
        backgroundStart = Color(0xFF0D47A1),
        backgroundEnd = Color(0xFF082F6D),
        accent = Color(0xFFFFCA28),
        content = Color(0xFFFFFFFF),
    ),
    rainy = WeatherConditionPalette(
        backgroundStart = Color(0xFF263238),
        backgroundEnd = Color(0xFF102027),
        accent = Color(0xFF64B5F6),
        content = Color(0xFFEAF2FF),
    ),
    thunderstorm = WeatherConditionPalette(
        backgroundStart = Color(0xFF283593),
        backgroundEnd = Color(0xFF121326),
        accent = Color(0xFFFFD54F),
        content = Color(0xFFFFFFFF),
    ),
    cloudy = WeatherConditionPalette(
        backgroundStart = Color(0xFF455A64),
        backgroundEnd = Color(0xFF263238),
        accent = Color(0xFFB0BEC5),
        content = Color(0xFFEAF2FF),
    ),
)
