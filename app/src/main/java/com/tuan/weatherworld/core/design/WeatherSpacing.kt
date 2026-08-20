package com.tuan.weatherworld.core.design

import androidx.compose.ui.unit.dp

/**
 * A 4dp spacing scale shared by every Weather World feature.
 *
 * Features should prefer these tokens over declaring unrelated padding and
 * gap values. Semantic aliases document why a particular distance is used.
 */
object WeatherSpacing {
    val space4 = 4.dp
    val space8 = 8.dp
    val space12 = 12.dp
    val space16 = 16.dp
    val space20 = 20.dp
    val space24 = 24.dp
    val space32 = 32.dp
    val space40 = 40.dp
    val space48 = 48.dp

    /** Horizontal breathing room at the edge of a phone screen. */
    val screenHorizontal = space20

    /** Default vertical padding for the screen content. */
    val screenVertical = space24

    /** Inner padding shared by weather and favorite-location cards. */
    val cardPadding = space16

    /** Gap between closely related items inside a card or list row. */
    val itemGap = space12

    /** Gap between independent forecast sections. */
    val sectionGap = space32

    /** Minimum accessible size for buttons and other interactive controls. */
    val touchTarget = 48.dp
}
