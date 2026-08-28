package com.tuan.weatherworld.data.mapper

import com.tuan.weatherworld.core.error.AppError
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Phân loại lỗi của luồng gọi Open-Meteo REST API thành [AppError] của ứng dụng.
 *
 * Luồng dữ liệu và nơi phát sinh lỗi:
 * 1. Open-Meteo REST API là server cung cấp dữ liệu. Server nhận HTTP request rồi
 *    trả HTTP status code và JSON; REST API không trực tiếp ném exception Kotlin.
 * 2. Retrofit là thư viện Android/Kotlin dùng để tạo và gửi HTTP request tới API.
 *    Khi server trả mã HTTP không thành công, Retrofit phát ra [HttpException].
 * 3. Tầng kết nối mạng bên dưới Retrofit phát ra [UnknownHostException],
 *    [ConnectException], [SocketTimeoutException] hoặc [IOException] khi không thể
 *    kết nối, mất mạng hay chờ phản hồi quá lâu.
 * 4. Retrofit Kotlinx Serialization converter chuyển JSON server trả về thành DTO.
 *    [SerializationException] xuất hiện khi JSON không khớp cấu trúc hoặc kiểu dữ
 *    liệu đã khai báo trong DTO.
 * 5. Mapper này đổi các exception kỹ thuật trên thành [AppError] để Repository và
 *    ViewModel không phải phụ thuộc vào message kỹ thuật của thư viện.
 *
 * Nếu sau này đổi Kotlinx Serialization sang Gson hoặc Moshi thì nhóm lỗi parse
 * JSON cũng phải đổi sang exception tương ứng của thư viện mới.
 */
object NetworkErrorMapper {

    fun from(throwable: Throwable): AppError {
        return when (throwable) {
            // Request chờ phản hồi quá thời gian cho phép.
            is SocketTimeoutException -> AppError.RequestTimeout

            // Không tìm thấy máy chủ hoặc không thể thiết lập kết nối mạng.
            is UnknownHostException, is ConnectException -> AppError.NoInternet

            // JSON trả về không thể parse đúng sang DTO bằng Kotlinx Serialization.
            is SerializationException -> AppError.InvalidResponse

            // Server đã phản hồi bằng mã lỗi HTTP nên cần phân loại tiếp theo status code.
            is HttpException -> mapHttpError(code = throwable.code())

            // Lỗi đọc/ghi trong luồng HTTP chưa thuộc các trường hợp cụ thể phía trên.
            is IOException -> AppError.NoInternet

            // Lỗi chưa được ứng dụng nhận diện.
            else -> AppError.Unknown
        }
    }

    private fun mapHttpError(code: Int): AppError {
        return when (code) {
            // API từ chối vì ứng dụng gửi quá nhiều request trong thời gian ngắn.
            429 -> AppError.TooManyRequests

            // Nhóm lỗi phía server: lỗi nội bộ, quá tải hoặc tạm ngừng hoạt động.
            in 500..599 -> AppError.ServerUnavailable

            // Các mã HTTP khác hiện chưa có loại AppError riêng.
            else -> AppError.Unknown
        }
    }
}
