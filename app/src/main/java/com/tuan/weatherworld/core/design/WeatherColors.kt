package com.tuan.weatherworld.core.design

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * General app colors used by Material 3 components.
 *
 * Feature UI should read these roles from MaterialTheme instead of declaring
 * one-off color values inside screens or components.
 */
internal val LightWeatherColorScheme = lightColorScheme(
    primary = Color(0xFFDCD7D7),
    secondary = Color(0xFF006879),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFF3A3A46),
    onBackground = Color(0xFF57575D),
    scrim = Color(0xFFBCBCD2),
    surface = Color(0xFFABABB7),
    error = Color(0xFFD00808),

)

internal val DarkWeatherColorScheme = darkColorScheme(
    primary = Color(0xFFDCD7D7),
    secondary = Color(0xFF006879),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFF30303A),
    scrim = Color(0xFFBCBCD2),
    surface = Color(0xFFABABB7),
    error = Color(0xFFD00808),
)

/**
 * Colors for a weather-condition card or illustration.
 *
 * The two background colors form a gradient, [accent] highlights the weather
 * icon or key value, and [content] is used for text and icons on the gradient.
 */
@Immutable
data class WeatherConditionPalette(
    val backgroundStart: Color,
    val backgroundEnd: Color,
    val accent: Color,
    val content: Color,
)

/** Weather-specific semantic roles that do not belong to Material ColorScheme. */
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
