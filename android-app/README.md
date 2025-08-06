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

- Android Studio Flamingo or later (2022.2.1+)
- Java Development Kit (JDK) 11 or later
- Android SDK with API 33
- Minimum Android API 24 (Android 7.0)
- Gradle 7.6 (automatically handled by Android Studio)

## Setup Instructions

1. **Prerequisites**
   - Install [Android Studio](https://developer.android.com/studio) (latest stable version)
   - Install Java 11+ if not included with Android Studio
   - Configure Android SDK (API 33 recommended)

2. **Clone the repository**
   ```bash
   cd android-app
   ```

3. **Configure Firebase**
   - Go to [Firebase Console](https://console.firebase.google.com)
   - Create/select your project (buzzy-fe536)
   - Add an Android app with package name: `com.buzzy.frequencyapp`
   - Download `google-services.json`
   - Replace the placeholder file at `app/google-services.json`

4. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open" (not "Import")
   - Navigate to and select the `android-app` folder
   - Android Studio will automatically:
     - Download the correct Gradle version (7.6)
     - Sync all dependencies
     - Set up the build environment
   - Wait for initial sync to complete (may take 5-10 minutes)

5. **Build and Run**
   - Ensure Gradle sync is complete (check status bar)
   - Connect an Android device with USB debugging enabled, or
   - Start an Android Virtual Device (AVD) from AVD Manager
   - Click the "Run" button (▷) or press Shift+F10
   - Select your target device
   - The app will build, install, and launch automatically

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

### Common Build Issues
- **Gradle sync failed**: 
  - File → Invalidate Caches and Restart
  - Check internet connection (Gradle downloads dependencies)
  - Ensure Java 11+ is installed and configured
  - Try Build → Clean Project, then Build → Rebuild Project

- **SDK/API version issues**:
  - Open SDK Manager (Tools → SDK Manager)
  - Install Android API 33 and latest build tools
  - Accept all license agreements

- **Firebase build errors**: 
  - Verify `google-services.json` is in `app/` folder (not root)
  - Check that package name matches Firebase project configuration
  - Ensure Firebase plugin is applied in `app/build.gradle`

### Runtime Issues
- **Audio not playing**: 
  - Test on physical device (emulator audio can be unreliable)
  - Check device volume and ensure not in silent mode
  - Grant audio permissions if prompted

- **App crashes on startup**:
  - Check Logcat for error details
  - Verify Firebase configuration is correct
  - Test on different Android API levels

### Performance Tips
- **Slow build times**: 
  - Enable Gradle daemon in gradle.properties
  - Increase Android Studio memory allocation
  - Use SSD storage for project files

- **Audio latency**: 
  - Test on physical device for best audio performance
  - Lower buffer sizes may improve latency but increase CPU usage