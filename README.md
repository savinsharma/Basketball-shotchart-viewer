NBA 3D Shot Chart (Android MVVM)

An interactive 3D shot chart experience for NBA games built with MVVM Clean Architecture.
This feature allows fans to view real-time animated shots on a 3D basketball court, with immersive camera controls.

✨ Features

📊 3D Shot Chart Rendering

Real-time animation of shots with trajectory arcs.

Made/Missed visualization.

Player-specific shot filtering.

🎥 Camera Controls

Default: Mid-row sideline seat (fan’s-eye view).

Rotate, zoom, and orbit freely around the arena.

⚡ Live Game Integration

Shots updated dynamically from live game feed.

Optimized for smooth 60fps rendering.

🏗️ Clean Architecture (MVVM)

Separation of concerns with layers: UI, ViewModel, Domain, Data.

Testable and scalable.

🛠️ Tech Stack

Language: Kotlin

Architecture: MVVM + Clean Architecture

DI: Hilt

Networking: Retrofit + OkHttp

Coroutines & Flow: Async & reactive data flow

Rendering: OpenGL ES 3.0 (custom GLSurfaceView + Renderer)

LiveData / StateFlow: Reactive UI updates

📂 Project Structure
com.app.basketballshotviewer
│
├── home/     
│   ├── data/ 
│   ├── domain/
│   ├── view/│
├── shotchart/
│   ├── constant/ 
│   ├── data/  
│   │   ├── core/
│   │   ├── datasource/
│   │   ├── repo/
│   ├── domain/
│   │   ├── model/
│   │   ├── usecase/
│   ├── view/
│   │   ├── di/
│   │   ├── state/
│   │   ├── ui/
│   ├── render/
│   │   ├── GlView/
│   │   ├── CameraController/
│   │   ├── Animator/
│   │   ├── Renderer/

🚀 Getting Started
Prerequisites

Android Studio Ladybug or later

Minimum SDK: 24

OpenGL ES 3.0 supported device/emulator

Setup

Clone the repo

Open in Android Studio.

Sync Gradle dependencies.

Run on device/emulator with OpenGL ES 3.0 support.

🤝 Contributing

Contributions are welcome! Please fork the repo and submit a pull request.

📜 License

This project is licensed under the MIT License.
