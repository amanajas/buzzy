# Frequency App - iOS

Native iOS implementation of the Frequency Generator and Binaural Beats application using SwiftUI.

## Features

- ✅ Multiple frequency categories (Therapeutic, Musical, Binaural, Brainwaves)
- ✅ Custom frequency creation and management
- ✅ Real-time audio generation with AVAudioEngine
- ✅ Independent left/right channel control for binaural beats
- ✅ Multiple waveform types (Sine, Square, Sawtooth, Triangle)
- ✅ Volume control with safety limiting (max 30%)
- ✅ Search functionality across all frequencies
- ✅ Firebase Analytics integration
- ✅ Native SwiftUI design with iOS Human Interface Guidelines

## Requirements

- Xcode 15.0 or later
- iOS 16.0 or later
- Swift 5.9 or later
- macOS Ventura 13.0 or later (for development)

## Setup Instructions

1. **Clone the repository**
   ```bash
   cd ios-app
   ```

2. **Configure Firebase**
   - Go to [Firebase Console](https://console.firebase.google.com)
   - Create/select your project (buzzy-fe536)
   - Add an iOS app with bundle ID: `com.buzzy.frequencyapp`
   - Download `GoogleService-Info.plist`
   - Replace the placeholder file in the project root

3. **Open in Xcode**
   - Open Xcode
   - Select "Open a project or file"
   - Navigate to `FrequencyApp.xcodeproj` or open the folder
   - Let Swift Package Manager resolve dependencies

4. **Configure Signing**
   - Select the project in navigator
   - Go to "Signing & Capabilities"
   - Select your development team
   - Xcode will automatically manage provisioning profiles

5. **Build and Run**
   - Select a simulator or connected device
   - Press Cmd+R or click the Run button
   - The app will build and launch

## Project Structure

```
ios-app/
├── FrequencyApp/
│   ├── FrequencyAppApp.swift          # App entry point
│   ├── ContentView.swift              # Main view
│   ├── Models/
│   │   └── Frequency.swift            # Data models
│   ├── Audio/
│   │   └── AudioEngine.swift          # AVAudioEngine implementation
│   ├── Data/
│   │   └── FrequencyDatabase.swift    # Frequency presets
│   ├── ViewModels/
│   │   └── FrequencyViewModel.swift   # Business logic & state
│   ├── Components/                    # Reusable UI components
│   │   ├── HeaderView.swift
│   │   ├── StatusDisplayView.swift
│   │   ├── MainControlsView.swift
│   │   ├── SearchBarView.swift
│   │   ├── CategoryTabsView.swift
│   │   ├── CustomFrequencyFormView.swift
│   │   ├── FrequencyListView.swift
│   │   └── FooterView.swift
│   └── Utils/
│       └── Color+Extensions.swift     # Helper extensions
├── GoogleService-Info.plist           # Firebase config
├── Info.plist                         # App configuration
└── Package.swift                      # Swift Package Manager

```

## Architecture

- **MVVM Pattern**: ObservableObject ViewModels with @Published properties
- **SwiftUI**: Declarative UI framework with reactive updates
- **AVAudioEngine**: Professional audio generation and routing
- **Combine**: For reactive programming and state management

## Audio Implementation

The app uses AVAudioEngine for professional-grade audio generation:
- Real-time waveform synthesis
- Stereo channel separation for binaural beats
- Low-latency audio playback
- Background audio support

## Testing

Run tests in Xcode:
- Cmd+U to run all tests
- Or navigate to Test navigator (Cmd+6)

## Distribution

### TestFlight Beta Testing
1. Archive the app: Product → Archive
2. Upload to App Store Connect
3. Submit for TestFlight review
4. Invite testers via email or public link

### App Store Release
1. Ensure all assets are ready (screenshots, description, etc.)
2. Archive and upload to App Store Connect
3. Submit for App Store review
4. Release manually or automatically after approval

## Troubleshooting

- **Audio not working in simulator**: Test on real device for accurate audio performance
- **Firebase crashes**: Ensure `GoogleService-Info.plist` is added to project target
- **Signing issues**: Check Apple Developer account status and certificates
- **SwiftUI preview not working**: Clean build folder (Cmd+Shift+K) and restart Xcode

## Performance Considerations

- Audio engine runs on background thread for smooth UI
- Frequency changes are applied without stopping playback when possible
- Memory-efficient buffer generation for continuous playback
- Automatic audio session management for interruptions