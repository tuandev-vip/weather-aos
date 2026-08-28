package com.tuan.weatherworld.feature.locations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tuan.weatherworld.R
import com.tuan.weatherworld.core.design.WWScreenScaffold
import com.tuan.weatherworld.core.design.WeatherTheme
import com.tuan.weatherworld.core.ui.asString
import com.tuan.weatherworld.data.model.WeatherLocation

/**
 * Entry point của màn tìm kiếm: collect hai state độc lập từ ViewModel.
 *
 * Search state dựng danh sách kết quả; save state điều khiển khóa thao tác,
 * Snackbar và callback [onLocationAdded]. Navigation vẫn thuộc AppNavGraph.
 */
@Composable
fun LocationSearchScreen(
    onBack: (() -> Unit)?,
    viewModel: LocationSearchViewModel = hiltViewModel(),
    onLocationAdded: (WeatherLocation) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()
    val savedLocationUiState by viewModel.savedLocationState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val alreadyExistsMessage = stringResource(
        R.string.locations_search_already_exists,
    )
    val isAdding = savedLocationUiState is SavedLocationUiState.Adding
    val savedLocationErrorMessage =
        when (val currentState = savedLocationUiState) {
            is SavedLocationUiState.Error -> currentState.message.asString()
            else -> null
        }


    LaunchedEffect(savedLocationUiState) {
        when (val currentState = savedLocationUiState) {
            is SavedLocationUiState.Added -> {
                onLocationAdded( currentState.location)
            }

            is SavedLocationUiState.AlreadyExists -> {
                snackbarHostState.showSnackbar(
                    message = alreadyExistsMessage,
                )
                viewModel.onSavedLocationResultHandled()
            }

            is SavedLocationUiState.Error -> {
                snackbarHostState.showSnackbar(
                    message = savedLocationErrorMessage.orEmpty()
                )
                viewModel.onSavedLocationResultHandled()
            }

            is SavedLocationUiState.Idle -> Unit
            is SavedLocationUiState.Adding -> Unit
        }
    }

    WWScreenScaffold(
        title = stringResource(R.string.locations_search_title),
        onIconBack = onBack,
        snackBarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            )
        },
    ) { paddingValues ->
        LocationSearchContent(
            query = query,
            onLocationSelected = viewModel::onLocationSelected,
            onQueryChange = viewModel::onSearchQueryChange,
            isAdding = isAdding,
            uiState = uiState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = WeatherTheme.spacing.space16),
        )
    }
}
@Composable
private fun LocationSearchContent(
    query: String,
    uiState: LocationSearchUiState,
    onQueryChange: (String) -> Unit,
    onLocationSelected: (WeatherLocation) -> Unit,
    isAdding: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,

        ) {

        Spacer(Modifier.size(WeatherTheme.spacing.space32))

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isAdding,
            placeholder = {
                Text(
                    text = stringResource(R.string.locations_search_hint),
                    color = MaterialTheme.colorScheme.surface,
                    maxLines = 1
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surface,
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onQueryChange("")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surface,
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                MaterialTheme.colorScheme.surface,
                unfocusedTextColor = MaterialTheme.colorScheme.surface
            ),
            singleLine = true
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is LocationSearchUiState.NoResults -> {
                    Text(
                        text = stringResource(R.string.locations_search_no_result),
                        style = WeatherTheme.textStyles.body,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                is LocationSearchUiState.Idle -> {
                    Text(
                        text = stringResource(R.string.locations_search_empty_idle),
                        style = WeatherTheme.textStyles.body,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }

                is LocationSearchUiState.Loading -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                is LocationSearchUiState.Error -> {
                    Text(
                        text = uiState.message.asString(),
                        style = WeatherTheme.textStyles.body,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is LocationSearchUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = uiState.locations,
                            key = { location ->
                                "${location.latitude},${location.longitude}"
                            },
                        ) { location ->
                            LocationSearchResultItem(
                                location = location,
                                onSelected = {
                                    onLocationSelected(location)
                                },
                                isAdding = isAdding,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationSearchResultItem(
    location: WeatherLocation,
    onSelected: () -> Unit,
    isAdding: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSelected, enabled = !isAdding),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = WeatherTheme.spacing.space16),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(
                modifier = Modifier.width(
                    WeatherTheme.spacing.space12,
                ),
            )
            Text(
                text = location.displayName,
                modifier = Modifier.weight(1f),
                style = WeatherTheme.textStyles.bodyStrong,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.surface)
    }
}
