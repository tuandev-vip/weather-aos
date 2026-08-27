package com.tuan.weatherworld.core.design

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tuan.weatherworld.core.ui.rememberSafeClick


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WWScreenScaffold(
    title: String? = null,
    onIconBack: (() -> Unit)? = null,
    onContentBack: (@Composable () -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.background,
    action: @Composable RowScope.() -> Unit = {},
    snackBarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        contentColor = containerColor,
        snackbarHost = snackBarHost,
        topBar = {
            if (title != null || onIconBack != null) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = title.orEmpty(),
                            style = WeatherTheme.textStyles.sectionTitle,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    },
                    navigationIcon = {
                        val safeBack = rememberSafeClick(onIconBack ?: {})
                        if (onContentBack != null) {
                           IconButton(
                               onClick = safeBack
                           ) {
                               onContentBack()
                           }

                        } else if (onIconBack != null) {
                            IconButton(
                                onClick = safeBack
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBackIos,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = containerColor,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                    actions = action

                )

            }
        },
        content = content

    )
}