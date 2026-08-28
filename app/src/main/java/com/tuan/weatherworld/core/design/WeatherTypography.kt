package com.tuan.weatherworld.core.design

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val WeatherFontFamily = FontFamily.SansSerif

/**
 * Các vai trò chữ có tên dùng chung trong Weather World.
 *
 * Nhiệt độ hiện tại là điểm nhấn mạnh nhất, sau đó là tên thành phố. Các vai trò
 * nhỏ hơn phục vụ phần dự báo và card yêu thích mà feature không phải tự đặt cỡ chữ.
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

/** Ánh xạ sang Material 3 để component chuẩn kế thừa cùng thang chữ. */
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
