# 🎧 Buzzy - Frequency Generator & Binaural Beats

A modern, mobile-optimized web application for generating custom audio frequencies, binaural beats, and sound therapy tones. Built with React and Web Audio API for precise audio control and therapeutic sound experiences.

## ✨ Features

### 🎵 Audio Generation
- **Dual Channel Control**: Independent left and right ear frequency control
- **Multiple Wave Types**: Sine, Square, Sawtooth, and Triangle waves
- **Real-time Updates**: Change frequencies and wave types while playing
- **Volume Control**: Independent left/right volume sliders with safety limiting (max 30%)
- **Frequency Range**: 20Hz to 20,000Hz support

### 🧠 Frequency Categories
- **Therapeutic Frequencies**: Solfeggio frequencies, chakra tones, Schumann resonance
- **Musical Notes**: Standard musical scale (C4-C5) with concert pitch reference
- **Binaural Beats**: Pre-configured beats for different brainwave states
- **Brainwave Entrainment**: Delta, Theta, Alpha, Beta, and Gamma wave frequencies
- **Custom Frequencies**: Save and manage your own frequency presets

### 🎯 Specialized Features
- **Binaural Beat Generation**: Different frequencies in each ear for brainwave entrainment
- **Search & Filter**: Find specific frequencies quickly
- **Mobile Optimized**: Touch-friendly interface designed for mobile devices
- **Real-time Feedback**: Visual indicators for playback status and frequency display

## 🚀 Live Demo

Visit the live application: [https://buzzy-fe536.web.app](https://buzzy-fe536.web.app)

## 🛠️ Technology Stack

- **Frontend**: React 18, HTML5, CSS3
- **Audio**: Web Audio API
- **Styling**: Tailwind CSS
- **Icons**: Unicode Emoji Symbols
- **Analytics**: Firebase Analytics
- **Hosting**: Firebase Hosting
- **CI/CD**: GitHub Actions

## 📱 Usage

### Getting Started
1. **Use Headphones**: For the best binaural beat experience, headphones are recommended
2. **Start Audio**: Click the green "Start" button to begin audio generation
3. **Choose Wave Type**: Select from Sine, Square, Sawtooth, or Triangle waves
4. **Set Frequencies**: Use preset categories or enter custom frequencies

### Wave Types Explained
- **Sine Wave**: Pure, smooth tone - ideal for meditation and relaxation
- **Square Wave**: Sharp, digital sound - good for focus and alertness
- **Sawtooth Wave**: Bright, rich harmonics - energizing and stimulating
- **Triangle Wave**: Softer than square, rounder than sine - balanced tone

### Frequency Categories

#### 🩺 Therapeutic Frequencies
- **Schumann Resonance (7.83 Hz)**: Earth's natural frequency
- **Solfeggio Frequencies**: Ancient healing tones (174Hz - 963Hz)
- **Chakra Frequencies**: Frequencies associated with energy centers

#### 🎼 Musical Notes
- **Concert Pitch**: Standard musical notes (C4-C5)
- **Reference Tuning**: A4 = 440Hz standard

#### 🧠 Binaural Beats
- **Delta (1-4 Hz)**: Deep sleep, healing
- **Theta (4-8 Hz)**: Meditation, creativity
- **Alpha (8-15 Hz)**: Relaxation, focus
- **Beta (15-40 Hz)**: Alertness, concentration
- **Gamma (40+ Hz)**: High-level cognition

## 🔧 Development

### Prerequisites
- Node.js 20+
- npm or yarn
- Git

### Local Development
```bash
# Clone the repository
git clone https://github.com/amanajas/buzzy.git
cd buzzy

# Install dependencies
npm install

# Start development (serve files locally)
# The app uses a single HTML file - serve with any local server
python -m http.server 8000
# or
npx serve .
```

### Project Structure
```
/
├── index.html                          # Main application file
├── index.jsx                           # React entry point
├── package.json                        # Dependencies and scripts
├── firebase.json                       # Firebase hosting config
├── .github/workflows/
│   └── firebase-deploy.yml            # CI/CD pipeline
├── src/
│   ├── App.jsx                        # Main React component
│   ├── firebase/config.js             # Firebase configuration
│   ├── hooks/useAudioEngine.js        # Audio engine logic
│   ├── components/                    # UI components
│   └── data/frequencyDatabase.js      # Frequency presets
└── CLAUDE.md                          # Development documentation
```

### Build Process
```bash
# Build for production
npm run build

# The build creates a dist/ folder with:
# - index.html (main app)
# - index.jsx (React entry)
# - src/ (component files)
```

## 🚀 Deployment

### Firebase Hosting
The app is automatically deployed to Firebase Hosting via GitHub Actions on every push to the main branch.

#### Manual Deployment
```bash
# Install Firebase CLI
npm install -g firebase-tools

# Login to Firebase
firebase login

# Deploy to hosting
firebase deploy --only hosting
```

### CI/CD Pipeline
- **Triggers**: Push to main branch, Pull Requests
- **Environment**: Uses GitHub Environment named "FIREBASE"
- **Process**: Build → Test → Deploy to Firebase Hosting

#### Required GitHub Secrets (in FIREBASE environment):
```
REACT_APP_FIREBASE_API_KEY
REACT_APP_FIREBASE_AUTH_DOMAIN
REACT_APP_FIREBASE_PROJECT_ID
REACT_APP_FIREBASE_STORAGE_BUCKET
REACT_APP_FIREBASE_MESSAGING_SENDER_ID  
REACT_APP_FIREBASE_APP_ID
REACT_APP_FIREBASE_MEASUREMENT_ID
FIREBASE_SERVICE_ACCOUNT_BUZZY_FE536
```

## 🎯 Use Cases

### 🧘 Meditation & Relaxation
- Use low-frequency sine waves (7.83Hz Schumann, 10Hz Alpha)
- Apply binaural beats for theta states (4-6Hz difference between ears)
- Volume at comfortable levels for extended listening

### 🎓 Study & Focus
- Beta wave frequencies (15-20Hz) for alertness
- Gamma frequencies (40Hz) for enhanced cognition
- Square or sawtooth waves for stimulating effects

### 😴 Sleep & Recovery
- Delta wave binaural beats (1-4Hz)
- Low-volume sine waves
- Gradual frequency reduction over time

### 🎵 Sound Therapy
- Solfeggio frequencies for chakra work
- 528Hz "Love Frequency" for healing
- Custom frequency combinations for personal therapy

## ⚠️ Safety & Considerations

- **Volume Limits**: App limits volume to 30% to prevent hearing damage
- **Headphone Recommendation**: Essential for binaural beats effectiveness
- **Medical Disclaimer**: Not intended to treat medical conditions
- **Photosensitive Warning**: Some users may be sensitive to certain frequencies
- **Extended Use**: Take breaks during long listening sessions

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the ISC License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Web Audio API**: For precise audio generation capabilities
- **React Community**: For the excellent development framework
- **Firebase**: For reliable hosting and analytics
- **Frequency Research**: Based on established therapeutic and musical frequency research
- **Open Source Community**: For the tools and libraries that made this possible

## 📧 Support

For questions, issues, or suggestions:
- Open an issue on [GitHub](https://github.com/amanajas/buzzy/issues)
- Check the [CLAUDE.md](CLAUDE.md) file for technical documentation

---

**⚡ Built with passion for sound, therapy, and open-source technology.**

*Use responsibly and enjoy your audio journey! 🎧*