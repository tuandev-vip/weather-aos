package com.tuan.weatherworld.core.navigation

import android.net.Uri

/** Central route table for Weather World's destinations. */
object Routes {
    const val SPLASH = "splash"
    const val WEATHER = "weather"

    // Location Screen
    const val LOCATIONS = "locations"

    // Weather setting
    const val SETTING = "setting"

    // location search Screen
    const val LOCATION_SEARCH = "location_search"

    const val ARG_LOCATION_NAME = "locationName"
    const val ARG_LOCATION_LATITUDE = "latitude"
    const val ARG_LOCATION_LONGITUDE = "longitude"

    const val WEATHER_ROUTE =
        "$WEATHER/{$ARG_LOCATION_NAME}/{$ARG_LOCATION_LATITUDE}/{$ARG_LOCATION_LONGITUDE}"

    fun weather(
        displayName: String,
        latitude: Double,
        longitude: Double,
    ) = "$WEATHER/${Uri.encode(displayName)}/$latitude/$longitude"
}
