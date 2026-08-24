# Weather World — Android

Weather World là dự án Android học tập về ứng dụng thời tiết, được xây dựng từng bước từ giao diện Jetpack Compose cơ bản đến dữ liệu thời tiết thật.

> Trạng thái hiện tại: **Commit 07 hoàn thành** — Weather và Locations đã lấy current, dự báo 24 giờ và dự báo 10 ngày thật từ Open-Meteo; tìm kiếm địa điểm thật thuộc Commit 08.

## Mục tiêu học tập

- Luyện xây dựng giao diện bằng Jetpack Compose và Material 3.
- Biết tách màn hình, component, màu sắc, typography và spacing thành các file phù hợp.
- Hiểu state, StateFlow và luồng dữ liệu một chiều trong Compose.
- Học MVVM, Repository và cách thay dữ liệu giả bằng API thật mà không viết lại UI.
- Làm quen dần với Coroutines, Hilt, DataStore, Room và testing.
- Tạo lịch sử Git gồm các commit nhỏ, rõ mục tiêu và dễ review.

## Package name

Package được chốt cho dự án:

```text
com.tuan.weatherworld
```

`namespace`, `applicationId` và package source hiện đều sử dụng tên này.

## Tech stack hiện tại

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- AndroidX Core SplashScreen
- Hilt + KSP
- ViewModel + Coroutines + StateFlow
- Lifecycle-aware state collection
- Retrofit + Kotlinx Serialization
- Open-Meteo Forecast API
- Gradle Kotlin DSL
- minSdk 26
- targetSdk 36
- compileSdk 36.1

Weather World hiện lấy dữ liệu thời tiết từ Open-Meteo qua `WeatherRepositoryImpl`. Tọa độ vẫn lấy từ `DefaultWeatherLocations`; Geocoding, Room và DataStore chưa được thêm ở giai đoạn này.

## Chạy project

### Android Studio

1. Mở thư mục `weather-aos` bằng Android Studio.
2. Chờ Gradle Sync hoàn thành.
3. Chọn emulator hoặc thiết bị Android API 26 trở lên.
4. Chọn cấu hình `app` và nhấn Run.

### Build bằng terminal trên Windows

```powershell
.\gradlew.bat :app:assembleDebug
```

APK debug được tạo trong:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Lộ trình ngắn

```text
Project foundation
    → design system
    → Navigation + Splash
    → model + mock repository
    → Hilt + ViewModel + StateFlow
    → hoàn thiện UI bằng mock data
    → Weather API thật
    → tìm kiếm và thêm địa điểm
    → DataStore + Room
    → testing
```

Kế hoạch triển khai chính thức theo từng commit nằm trong [`ROADMAP.md`](ROADMAP.md). Nội dung học mở rộng nằm trong [`LEARNING_PLAN.md`](LEARNING_PLAN.md).

## Nguyên tắc kiến trúc

UI không biết chi tiết Retrofit hoặc Open-Meteo. Luồng hiện tại là:

```text
AppNavGraph
    ├── WeatherScreen → WeatherViewModel
    └── LocationsScreen → LocationsViewModel
                              ↓
                    WeatherRepository
                              ↓
                    WeatherRepositoryImpl
                              ↓
                    WeatherRemoteDataSource
                              ↓
                OpenMeteoWeatherRemoteDataSource
                              ↓
                         WeatherApi
                              ↓
             Retrofit + Kotlinx Serialization
                              ↓
                  OpenMeteoForecastDto
                              ↓
                         toDomain()
                              ↓
                           Weather
```

`MockWeatherRepository` và `MockWeatherData` vẫn được giữ trong source để học/test, nhưng `RepositoryModule` runtime đã bind `WeatherRepository` sang `WeatherRepositoryImpl`.

Hilt chịu trách nhiệm tạo và nối dependency. Mapper giữ DTO của Open-Meteo ngoài UI và chuyển chúng thành domain model. Mỗi ViewModel quản lý `UiState` gồm Loading, Success và Error; Compose thu thập state theo lifecycle rồi tự cập nhật UI.

Route `SETTING` và `SettingScreen` hiện mới là khung điều hướng tạo sớm. Phần cài đặt thật vẫn thuộc Commit 10 trong roadmap.
