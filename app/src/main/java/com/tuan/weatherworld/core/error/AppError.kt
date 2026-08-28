package com.tuan.weatherworld.core.error

/**
 * Các loại lỗi mà ứng dụng Weather World hiểu được.
 *
 * App không đưa trực tiếp message của Retrofit, Room hoặc Android SDK lên UI.
 * Lỗi kỹ thuật sẽ được phân loại thành một trong các loại lỗi bên dưới.
 */
sealed interface AppError {

    /** Thiết bị không có mạng hoặc không kết nối được tới máy chủ. */
    data object NoInternet : AppError

    /** Request mất quá nhiều thời gian và bị timeout. */
    data object RequestTimeout : AppError

    /** Máy chủ trả lỗi 5xx hoặc tạm thời không hoạt động. */
    data object ServerUnavailable : AppError

    /** API giới hạn số lượng request trong một khoảng thời gian. */
    data object TooManyRequests : AppError

    /** JSON trả về sai hoặc không chuyển được thành DTO. */
    data object InvalidResponse : AppError

    /** Người dùng chưa cấp quyền truy cập vị trí. */
    data object LocationPermissionDenied : AppError

    /** Đã có quyền nhưng thiết bị không lấy được tọa độ. */
    data object LocationUnavailable : AppError

    /** Không đọc hoặc ghi được Room/DataStore. */
    data object StorageFailure : AppError

    /** Lỗi chưa được ứng dụng phân loại. */
    data object Unknown : AppError
}