package com.tuan.weatherworld.feature.locations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tuan.weatherworld.R
import com.tuan.weatherworld.core.design.WeatherTheme
import com.tuan.weatherworld.core.ui.asString
import com.tuan.weatherworld.data.model.Weather
import com.tuan.weatherworld.data.model.WeatherLocation

/**
 * Hiển thị danh sách favorites từ [LocationsViewModel].
 * Screen chỉ render UiState và phát sự kiện thêm/chọn địa điểm; việc lưu DataStore
 * và tải thời tiết nằm trong ViewModel/Repository.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    modifier: Modifier = Modifier,
    onAddLocation: () -> Unit,
    onLocationSelected: (WeatherLocation) -> Unit,
    viewModel: LocationsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = WeatherTheme.spacing.space16),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.locations_title),
                style = WeatherTheme.textStyles.city,
                color = MaterialTheme.colorScheme.primary,
            )

            IconButton(
                onClick = onAddLocation,
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = stringResource(R.string.locations_add),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }

        LocationContent(
            uiState = uiState,
            onLocationSelected = { location ->
                viewModel.selectLocation(
                    location = location,
                    onSelected = onLocationSelected
                )
            },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun LocationContent(
    uiState: LocationsUiState,
    onLocationSelected: (WeatherLocation) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(WeatherTheme.spacing.space16),
    ) {
        when (uiState) {
            is LocationsUiState.Loading ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

            is LocationsUiState.Empty -> Text(
                text = stringResource(R.string.locations_empty),
                color = MaterialTheme.colorScheme.surface,
                textAlign = TextAlign.Center
            )

            is LocationsUiState.Error ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = uiState.message.asString(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

            is LocationsUiState.Success ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(
                        WeatherTheme.spacing.space16,
                    ),
                ) {
                    items(
                        items = uiState.locations,
                        key = { weather -> weather.location.displayName },
                    ) { weather ->
                        LocationCard(
                            weather = weather,
                            onclick = {
                                onLocationSelected(weather.location)
                            }
                        )
                    }
                    item {
                        Spacer(Modifier.size(WeatherTheme.spacing.space48))
                    }
                }
        }
    }
}

@Composable
private fun LocationCard(
    weather: Weather,
    onclick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onclick)
            .background(
                color = MaterialTheme.colorScheme.onBackground,
                shape = WeatherTheme.shapes.card,
            )
            .padding(WeatherTheme.spacing.space16),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier.weight(1f),

        ) {
            Text(
                text = weather.location.displayName,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.size(WeatherTheme.spacing.space48))
            Text(
                text = weather.weatherCondition,
                color = MaterialTheme.colorScheme.primary,
            )
        }
            Spacer(Modifier.size(WeatherTheme.spacing.space16))

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.weather_temperature_format, weather.temperature),
                style = WeatherTheme.textStyles.temperature,
                color = MaterialTheme.colorScheme.primary,
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
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = stringResource(
                        R.string.weather_low_temperature_format,
                        weather.lowTemperature,
                    ),
                    style = WeatherTheme.textStyles.bodyStrong,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}
