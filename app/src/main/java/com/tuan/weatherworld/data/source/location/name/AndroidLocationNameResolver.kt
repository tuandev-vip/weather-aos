package com.tuan.weatherworld.data.source.location.name

import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import com.tuan.weatherworld.data.source.location.device.DeviceCoordinates
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Reverse geocoding production sử dụng [Geocoder] của Android.
 *
 * API 33 trở lên trả kết quả bằng callback nên được chuyển thành hàm `suspend`;
 * Android cũ dùng lời gọi đồng bộ trên [Dispatchers.IO]. Kết quả ưu tiên tên
 * tỉnh/thành phố ngắn gọn và không trả cả chuỗi địa chỉ dài cho UI.
 */
@Singleton
class AndroidLocationNameResolver @Inject constructor(
    @ApplicationContext context: Context,
) : LocationNameResolver {

    private val geocoder = Geocoder(
        context,
        Locale.forLanguageTag("vi-VN"),
    )

    override suspend fun resolveLocationName(
        coordinates: DeviceCoordinates,
    ): Result<String> {
        if (!Geocoder.isPresent()) {
            return Result.failure(
                IllegalStateException(
                    "Thiết bị không hỗ trợ dịch vụ tìm tên địa điểm",
                ),
            )
        }

        return try {
            val address =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getAddressApi33OrAbove(coordinates)
                } else {
                    getAddressBelowApi33(coordinates)
                }

            val displayName = address?.toDisplayName()

            if (displayName == null) {
                Result.failure(
                    IllegalStateException(
                        "Không tìm thấy tên địa điểm từ tọa độ hiện tại",
                    ),
                )
            } else {
                Result.success(displayName)
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private suspend fun getAddressApi33OrAbove(
        coordinates: DeviceCoordinates,
    ): Address? {
        return suspendCancellableCoroutine { continuation ->
            geocoder.getFromLocation(
                coordinates.latitude,
                coordinates.longitude,
                1,
                object : Geocoder.GeocodeListener {

                    override fun onGeocode(
                        addresses: MutableList<Address>,
                    ) {
                        if (continuation.isActive) {
                            continuation.resume(
                                addresses.firstOrNull(),
                            )
                        }
                    }

                    override fun onError(
                        errorMessage: String?,
                    ) {
                        if (continuation.isActive) {
                            continuation.resumeWithException(
                                IOException(
                                    errorMessage
                                        ?: "Không thể tìm tên địa điểm",
                                ),
                            )
                        }
                    }
                },
            )
        }
    }

    @Suppress("DEPRECATION")
    private suspend fun getAddressBelowApi33(
        coordinates: DeviceCoordinates,
    ): Address? {
        return withContext(Dispatchers.IO) {
            geocoder.getFromLocation(
                coordinates.latitude,
                coordinates.longitude,
                1,
            )?.firstOrNull()
        }
    }

    private fun Address.toDisplayName(): String? {
        return listOf(
            adminArea,
            locality,
            subAdminArea,
            countryName,
        ).firstOrNull { name ->
            !name.isNullOrBlank()
        }?.trim()
    }
}
