package com.tuan.weatherworld.core.design

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/** Các mức bo góc dùng chung cho component của Weather World. */
object WeatherShapes {
    /** Small icon backgrounds and compact controls. */
    val small = RoundedCornerShape(8.dp)

    /** Forecast chips and compact cards. */
    val medium = RoundedCornerShape(12.dp)

    /** Buttons and text fields. */
    val button = RoundedCornerShape(14.dp)

    /** Favorite-location and forecast cards. */
    val card = RoundedCornerShape(20.dp)

    /** Large current-weather card. */
    val heroCard = RoundedCornerShape(28.dp)

    /** Fully rounded chips such as unit and day selectors. */
    val pill = RoundedCornerShape(percent = 50)
}

/** Ánh xạ sang Material 3 để component chuẩn dùng cùng hệ bo góc của ứng dụng. */
val WeatherMaterialShapes = Shapes(
    extraSmall = WeatherShapes.small,
    small = WeatherShapes.medium,
    medium = RoundedCornerShape(16.dp),
    large = WeatherShapes.card,
    extraLarge = WeatherShapes.heroCard,
)
