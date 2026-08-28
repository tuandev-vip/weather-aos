package com.tuan.weatherworld.core.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.tuan.weatherworld.R
import com.tuan.weatherworld.core.error.AppError

/**
 * Nội dung mà UI có thể hiển thị.
 *
 * Resource lấy câu chữ từ strings.xml.
 * Dynamic dùng cho nội dung phát sinh trong lúc chạy.
 */
sealed interface UiText {
    data class Resource(@StringRes val resId: Int, val args: List<Any> = emptyList()) : UiText
    data class Dynamic(val value: String) : UiText
}

/** Tạo UiText.Resource ngắn gọn từ một string resource. */
fun uiText(@StringRes resId: Int, vararg args: Any): UiText =
    UiText.Resource(resId = resId, args = args.toList())

/** Chuyển loại lỗi của app thành câu thông báo dành cho người dùng. */
fun AppError.toUiText(): UiText {
    return when (this) {
        AppError.NoInternet -> uiText(R.string.error_no_internet)

        AppError.RequestTimeout -> uiText(R.string.error_request_timeout)

        AppError.ServerUnavailable -> uiText(R.string.error_server_unavailable)

        AppError.TooManyRequests -> uiText(R.string.error_too_many_requests)

        AppError.InvalidResponse -> uiText(R.string.error_invalid_response)

        AppError.LocationPermissionDenied -> uiText(R.string.error_location_permission_denied)

        AppError.LocationUnavailable -> uiText(R.string.error_location_unavailable)

        AppError.StorageFailure -> uiText(R.string.error_storage_failure)

        AppError.Unknown -> uiText(R.string.error_unknown)
    }
}


/** Chuyển UiText thành String thật tại Compose UI. */
@Composable
fun UiText.asString(): String {
    val context = LocalContext.current
    return when (this) {
        is UiText.Dynamic -> value
        is UiText.Resource -> context.getString(resId, *args.toTypedArray())
    }
}