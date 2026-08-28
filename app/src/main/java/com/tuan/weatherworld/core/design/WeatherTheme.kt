package com.tuan.weatherworld.core.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalWeatherConditionColors = staticCompositionLocalOf {
    LightWeatherConditionColors
}

private val LocalWeatherSpacing = staticCompositionLocalOf {
    WeatherSpacing
}

/**
 * Theme gốc của Weather World.
 *
 * Material 3 nhận color, typography và shape dùng chung. CompositionLocal cung
 * cấp bảng màu trạng thái thời tiết và spacing vì MaterialTheme không có sẵn
 * những vai trò riêng này.
 */
@Composable
fun WeatherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) {
        DarkWeatherColorScheme
    } else {
        LightWeatherColorScheme
    }

    val conditionColors = if (darkTheme) {
        DarkWeatherConditionColors
    } else {
        LightWeatherConditionColors
    }

    CompositionLocalProvider(
        LocalWeatherConditionColors provides conditionColors,
        LocalWeatherSpacing provides WeatherSpacing,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = WeatherTypography,
            shapes = WeatherMaterialShapes,
            content = content,
        )
    }
}

/**
 * Cổng truy cập token riêng của Weather World nằm ngoài MaterialTheme.
 *
 * Ví dụ: `WeatherTheme.conditionColors.sunny` hoặc
 * `WeatherTheme.spacing.cardPadding`.
 */
object WeatherTheme {
    val conditionColors: WeatherConditionColors
        @Composable
        @ReadOnlyComposable
        get() = LocalWeatherConditionColors.current

    val spacing: WeatherSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalWeatherSpacing.current

    val textStyles: WeatherTextStyles
        get() = WeatherTextStyles

    val shapes: WeatherShapes
        get() = WeatherShapes
}
