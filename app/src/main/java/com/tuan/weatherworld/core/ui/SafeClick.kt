package com.tuan.weatherworld.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue

/**
 * Bọc callback để bỏ những lần bấm liên tiếp trong [interval] mili giây.
 * `rememberUpdatedState` luôn gọi callback mới nhất mà không tạo lại bộ đếm click.
 */
@Composable
fun rememberSafeClick(
    onClick: () -> Unit,
    interval: Long = 1000L,
): () -> Unit {
    val currentOnClick by rememberUpdatedState(onClick)
    var lastClickTime by remember { mutableLongStateOf(0L) }

    return remember {
        {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > interval) {
                lastClickTime = currentTime
                currentOnClick()
            }
        }
    }
}
