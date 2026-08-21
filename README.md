# Weather World — Android

Weather World là dự án Android học tập về ứng dụng thời tiết, được xây dựng từng bước từ giao diện Jetpack Compose cơ bản đến dữ liệu thời tiết thật.

> Trạng thái hiện tại: **Commit 05 hoàn thành** — app đã có Navigation, Splash, Hilt, Repository mock, WeatherViewModel và StateFlow; chưa kết nối Weather API thật.

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
- Gradle Kotlin DSL
- minSdk 24
- targetSdk 36
- compileSdk 36.1

Weather World hiện lấy dữ liệu từ `MockWeatherRepository`. HTTP client, JSON parser, Room và DataStore chưa được thêm ở giai đoạn này.

## Chạy project

### Android Studio

1. Mở thư mục `weather-aos` bằng Android Studio.
2. Chờ Gradle Sync hoàn thành.
3. Chọn emulator hoặc thiết bị Android API 24 trở lên.
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

UI không biết dữ liệu đến từ fake data hay Internet. Luồng hiện tại là:

```text
AppNavGraph
    → WeatherScreen
    → WeatherViewModel
    → WeatherRepository
    → MockWeatherRepository
    → MockWeatherData
```

Khi kết nối API thật, phần sau `WeatherRepository` sẽ được thay bằng:

```text
WeatherRepositoryImpl
    → WeatherRemoteDataSource
    → Weather API
```

Hilt chịu trách nhiệm tạo và nối dependency. ViewModel quản lý `WeatherUiState` gồm Loading, Success và Error; Compose thu thập state theo lifecycle rồi tự cập nhật UI.
