# Weather World — Android

Weather World là dự án Android học tập về ứng dụng thời tiết, được xây dựng từng bước từ giao diện Jetpack Compose cơ bản đến dữ liệu thời tiết thật.

> Trạng thái hiện tại: **Design system ready** — foundation và bộ token light/dark đã hoàn thành; chưa có màn hình thời tiết và chưa kết nối API.

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
- Gradle Kotlin DSL
- minSdk 24
- targetSdk 36
- compileSdk 36.1

Các công nghệ như Retrofit, Coroutines, Hilt, Room và DataStore chưa được thêm ở giai đoạn này.

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
Compose UI tĩnh
    → component dùng lại
    → model và fake data
    → ViewModel + StateFlow
    → MVVM + Repository
    → Weather API thật
    → loading/error/retry
    → Hilt
    → DataStore + Room
    → testing
```

Kế hoạch chi tiết theo từng commit nằm trong [`LEARNING_PLAN.md`](LEARNING_PLAN.md).

## Nguyên tắc kiến trúc

UI không biết dữ liệu đến từ fake data hay Internet. Khi dự án phát triển, luồng dữ liệu mục tiêu là:

```text
WeatherScreen
    → WeatherViewModel
    → WeatherRepository
    → WeatherRemoteDataSource
    → Weather API
```

Ở những commit đầu, dự án chỉ tập trung vào UI và dữ liệu giả. API, dependency injection và database chỉ được thêm khi đã hiểu tầng đứng trước chúng.
