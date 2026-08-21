package com.tuan.weatherworld.feature.weather

import androidx.compose.runtime.Composable

/**
 * Navigation-level entry point for the weather feature.
 *
 * The static source is temporary. A later commit will replace it with state
 * collected from WeatherViewModel without changing AppNavGraph.
 */
@Composable
fun WeatherRoute(
    onOpenLocations: () -> Unit,
) {
    WeatherScreen(
        cityName = WeatherPreviewData.cityName,
        temperature = WeatherPreviewData.temperature,
        weatherCurrent = WeatherPreviewData.weatherCurrent,
        highTemperature = WeatherPreviewData.highTemperature,
        lowTemperature = WeatherPreviewData.lowTemperature,
        onOpenLocations = onOpenLocations,
    )
}
