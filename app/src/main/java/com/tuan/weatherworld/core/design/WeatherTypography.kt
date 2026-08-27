package com.tuan.weatherworld.core.design

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val WeatherFontFamily = FontFamily.SansSerif

/**
 * Named text roles for Weather World.
 *
 * The current temperature is intentionally the strongest visual element,
 * followed by the city name. Smaller roles support forecast sections and
 * compact favorite-location cards without declaring font sizes in features.
 */
object WeatherTextStyles {
    val temperature = TextStyle(fontFamily = WeatherFontFamily, fontWeight = FontWeight.Light, fontSize = 64.sp, lineHeight = 72.sp, letterSpacing = (-1).sp,)
    val city = TextStyle(fontFamily = WeatherFontFamily, fontWeight = FontWeight.Medium, fontSize = 28.sp, lineHeight = 34.sp,)
    val sectionTitle = TextStyle(fontFamily = WeatherFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 26.sp,)
    val cardTemperature = TextStyle(fontFamily = WeatherFontFamily, fontWeight = FontWeight.Medium, fontSize = 24.sp, lineHeight = 30.sp,)
    val cardTitle = TextStyle(fontFamily = WeatherFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 24.sp,)
    val body = TextStyle(fontFamily = WeatherFontFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp,)
    val bodyStrong = TextStyle(fontFamily = WeatherFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 24.sp,)
    val metadata = TextStyle(fontFamily = WeatherFontFamily, fontWeight = FontWeight.Medium, fontSize = 13.sp, lineHeight = 18.sp, letterSpacing = 0.3.sp,)
    val label = TextStyle(fontFamily = WeatherFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.6.sp,)
}

/** Material 3 mapping so standard components inherit the same type scale. */
val WeatherTypography = Typography(
    displayLarge = WeatherTextStyles.temperature,
    headlineLarge = WeatherTextStyles.city,
    headlineSmall = WeatherTextStyles.cardTemperature,
    titleLarge = WeatherTextStyles.sectionTitle,
    titleMedium = WeatherTextStyles.cardTitle,
    bodyLarge = WeatherTextStyles.body,
    bodyMedium = WeatherTextStyles.metadata,
    labelLarge = WeatherTextStyles.bodyStrong,
    labelMedium = WeatherTextStyles.label,
)
