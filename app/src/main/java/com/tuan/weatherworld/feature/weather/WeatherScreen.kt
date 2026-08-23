package com.tuan.weatherworld.feature.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tuan.weatherworld.R
import com.tuan.weatherworld.core.design.WeatherTheme
import com.tuan.weatherworld.data.model.DailyForecast
import com.tuan.weatherworld.data.model.HourlyForecast
import com.tuan.weatherworld.data.model.Weather
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun WeatherScreen(
    onOpenLocations: () -> Unit,
    onOpenSetting: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    WeatherContent(
        state = state,
        onOpenLocations = onOpenLocations,
        onOpenSetting = onOpenSetting,
        modifier = modifier,
    )
}

@Composable
private fun WeatherContent(
    state: WeatherUiState,
    onOpenLocations: () -> Unit,
    onOpenSetting: () -> Unit,
    modifier: Modifier,
) {
    val rainyColors = WeatherTheme.conditionColors.rainy

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        rainyColors.backgroundEnd,
                        rainyColors.backgroundStart,
                    ),
                ),
            )
            .statusBarsPadding()
            .padding(
                horizontal = WeatherTheme.spacing.screenHorizontal,
                vertical = WeatherTheme.spacing.screenVertical,
            ),
    ) {

        when (val currentState = state) {
            WeatherUiState.Loading -> {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            is WeatherUiState.Error -> {
                Text(
                    text = currentState.message,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            is WeatherUiState.Success -> {
                WWScaffold(
                    weather = currentState.weather,
                    onOpenLocations = onOpenLocations,
                    onOpenSetting = onOpenSetting,
                )
                Spacer(Modifier.size(WeatherTheme.spacing.space32))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(WeatherTheme.spacing.space16),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    item {
                        WeatherDetails(
                            weather = currentState.weather,
                            contentColor = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(Modifier.size(WeatherTheme.spacing.touchTarget))
                    }

                    item {
                        HourlyForecastCard(
                            hourlyForecasts = currentState.weather.hourlyForecast,
                        )
                    }
                    item {
                        DailyForecastCard(
                            dailyForecast = currentState.weather.dailyForecast
                        )
                        Spacer(Modifier.size(WeatherTheme.spacing.space40))
                    }
                    item {
                        Text(
                            text = stringResource(R.string.weather_brand),
                            style = WeatherTheme.textStyles.sectionTitle,

                            )
                    }

                }
            }
        }
    }
}

@Composable
fun WWScaffold(
    onOpenLocations: () -> Unit,
    onOpenSetting: () -> Unit,
    weather: Weather
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = weather.cityName,
            style = WeatherTheme.textStyles.city,
            color = MaterialTheme.colorScheme.primary,
        )

        Row {
            IconButton(
                onClick = onOpenLocations,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.List,
                    contentDescription = stringResource(
                        R.string.weather_open_locations,
                    ),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(30.dp)
                )
            }
            IconButton(
                onClick = onOpenSetting,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = stringResource(
                        R.string.weather_open_settings,
                    ),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(25.dp)
                )
            }
        }

    }
}

@Composable
fun DailyForecastCard(
    dailyForecast: List<DailyForecast>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                RoundedCornerShape(20.dp)
            )
            .padding(
                horizontal = WeatherTheme.spacing.space20,
                vertical = WeatherTheme.spacing.space32
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(WeatherTheme.spacing.space8)
    ) {

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(WeatherTheme.spacing.space12)
        ) {
            Icon(
                imageVector = Icons.Default.Radio,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.scrim,

                )
            Text(
                text = stringResource(R.string.weather_daily_forecast_title),
                color = MaterialTheme.colorScheme.scrim,
                style = WeatherTheme.textStyles.cardTitle,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

        }

        dailyForecast.forEachIndexed { index, forecast ->
            DailyForecastItem(
                dailyForecast = forecast,
                isToday = index == 0,
                contentColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun DailyForecastItem(
    dailyForecast: DailyForecast,
    isToday: Boolean,
    contentColor: Color,
) {
    val dayText = if (isToday) {
        stringResource(R.string.weather_today)
    } else {
        dailyForecast.date.format(DayFormatter)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(WeatherTheme.spacing.space8)
    ) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = dayText,
                style = WeatherTheme.textStyles.cardTitle,
                color = contentColor,
                modifier = Modifier.width(75.dp)
            )

            Column() {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = null
                )

                Text(
                    text = stringResource(
                        R.string.weather_precipitation_format,
                        dailyForecast.precipitationProbability,
                    ),
                    style = WeatherTheme.textStyles.metadata,
                    color = contentColor,
                    textAlign = TextAlign.Center,
                )
            }

            Text(
                text = stringResource(
                    R.string.weather_low_high_temperature_format,
                    dailyForecast.lowTemperature, dailyForecast.highTemperature
                ), style = WeatherTheme.textStyles.cardTitle,
                textAlign = TextAlign.Center,
                color = contentColor
            )
        }
    }
}

@Composable
private fun HourlyForecastCard(
    hourlyForecasts: List<HourlyForecast>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                RoundedCornerShape(20.dp)
            )
            .padding(
                horizontal = WeatherTheme.spacing.space20,
                vertical = WeatherTheme.spacing.space32
            ),
        horizontalArrangement = Arrangement.spacedBy(WeatherTheme.spacing.space32),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(
            items = hourlyForecasts,
            key = { _, forecast -> forecast.dateTime },

            ) { index, forecast ->
            HourlyForecastItem(
                hourlyForecast = forecast,
                isNow = index == 0,
                contentColor = MaterialTheme.colorScheme.primary,
            )
        }

    }
}

@Composable
private fun HourlyForecastItem(
    hourlyForecast: HourlyForecast,
    isNow: Boolean,
    contentColor: Color,
) {
    val time = if (isNow) {
        stringResource(R.string.weather_now)
    } else {
        hourlyForecast.dateTime.format(HourFormatter)
    }

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(WeatherTheme.spacing.space12),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = time,
            style = WeatherTheme.textStyles.cardTitle,
            textAlign = TextAlign.Center,
            color = contentColor
        )

        Icon(
            imageVector = Icons.Default.WbSunny,
            contentDescription = null
        )

        Text(
            text = stringResource(
                R.string.weather_precipitation_format,
                hourlyForecast.precipitationProbability,
            ),
            style = WeatherTheme.textStyles.metadata,
            color = contentColor,
            textAlign = TextAlign.Center,
        )

        Text(
            text = stringResource(
                R.string.weather_temperature_format,
                hourlyForecast.temperature
            ),
            style = WeatherTheme.textStyles.cardTitle,
            textAlign = TextAlign.Center,
            color = contentColor
        )
    }
}

private val HourFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val DayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.forLanguageTag("vi-VN"))

@Composable
private fun WeatherDetails(
    weather: Weather,
    contentColor: Color,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(WeatherTheme.spacing.space8),
    ) {

        Text(
            text = stringResource(R.string.weather_temperature_format, weather.temperature),
            style = WeatherTheme.textStyles.temperature,
            color = contentColor,
        )
        Text(
            text = weather.weatherCondition,
            style = WeatherTheme.textStyles.sectionTitle,
            color = contentColor,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(WeatherTheme.spacing.space8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(
                    R.string.weather_high_temperature_format,
                    weather.highTemperature,
                ),
                style = WeatherTheme.textStyles.bodyStrong,
                color = contentColor,
            )
            Text(
                text = stringResource(
                    R.string.weather_low_temperature_format,
                    weather.lowTemperature,
                ),
                style = WeatherTheme.textStyles.bodyStrong,
                color = contentColor,
            )
        }
    }
}
