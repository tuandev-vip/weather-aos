package com.tuan.weatherworld.core.common

import com.tuan.weatherworld.core.error.AppError

/**
 * Kết quả của một thao tác trong data layer.
 *
 * Thành công mang theo dữ liệu kiểu T.
 * Thất bại mang theo loại lỗi và Throwable gốc để log/debug.
 */
sealed interface AppResult<out T> {

    data class Success<T>( val data: T ) : AppResult<T>

    data class Error( val error: AppError, val throwable: Throwable? = null ) : AppResult<Nothing>
}