# Frequency App - Android

Native Android implementation of the Frequency Generator and Binaural Beats application.

## Features

- ✅ Multiple frequency categories (Therapeutic, Musical, Binaural, Brainwaves)
- ✅ Custom frequency creation and management
- ✅ Real-time audio generation with Web Audio API equivalent
- ✅ Independent left/right channel control for binaural beats
- ✅ Multiple waveform types (Sine, Square, Sawtooth, Triangle)
- ✅ Volume control with safety limiting (max 30%)
- ✅ Search functionality across all frequencies
- ✅ Firebase Analytics integration
- ✅ Material Design 3 with Jetpack Compose

## Requirements

- Android Studio Arctic Fox or later
- Android SDK 34
- Minimum Android API 24 (Android 7.0)
- Kotlin 1.9.20 or later

## Setup Instructions

1. **Clone the repository**
   ```bash
   cd android-app
   ```

2. **Configure Firebase**
   - Go to [Firebase Console](https://console.firebase.google.com)
   - Create/select your project (buzzy-fe536)
   - Add an Android app with package name: `com.buzzy.frequencyapp`
   - Download `google-services.json`
   - Replace the placeholder file at `app/google-services.json`

3. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the `android-app` folder
   - Let Gradle sync complete

4. **Build and Run**
   - Connect an Android device or start an emulator
   - Click "Run" or press Shift+F10
   - The app will build and install on your device

## Project Structure

```
android-app/
├── app/
│   ├── src/main/java/com/buzzy/frequencyapp/
│   │   ├── MainActivity.kt              # Entry point
│   │   ├── audio/
│   │   │   └── AudioEngine.kt          # Audio generation engine
│   │   ├── data/
│   │   │   └── FrequencyDatabase.kt    # Frequency presets
│   │   ├── models/
│   │   │   └── Frequency.kt            # Data models
│   │   ├── ui/
│   │   │   ├── FrequencyApp.kt         # Main composable
│   │   │   ├── FrequencyViewModel.kt   # State management
│   │   │   ├── components/             # UI components
│   │   │   └── theme/                  # Material theme
│   │   └── utils/                      # Utility functions
│   └── google-services.json            # Firebase config
└── build.gradle                        # Build configuration
```

## Architecture

- **MVVM Pattern**: ViewModel manages UI state and business logic
- **Jetpack Compose**: Modern declarative UI framework
- **Coroutines**: For asynchronous audio generation
- **AudioTrack API**: Low-level audio playback for precise frequency control

## Testing

Run tests with:
```bash
./gradlew test
./gradlew connectedAndroidTest
```

## Release Build

1. Generate a signed APK/AAB:
   - Build → Generate Signed Bundle/APK
   - Follow the wizard to create or use existing keystore

2. Or use command line:
   ```bash
   ./gradlew assembleRelease
   ```

## Troubleshooting

- **Audio not playing**: Ensure device volume is up and not in silent mode
- **Firebase errors**: Verify `google-services.json` is correctly configured
- **Build failures**: Try "File → Invalidate Caches and Restart" in Android Studio