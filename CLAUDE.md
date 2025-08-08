# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a React-based frequency generator and binaural beats application called "Buzzy". The application is a single-page mobile-optimized web app that generates audio frequencies for therapeutic, musical, and brainwave entrainment purposes.

## Architecture

The application has been organized into a modular structure with separate components, hooks, and data files for better maintainability and code organization.

### Core Components

- **Audio Engine** (`src/hooks/useAudioEngine.js`): Custom React hook managing Web Audio API
  - Left/right oscillators with independent frequency and volume control
  - Multiple waveform types (sine, square, sawtooth, triangle)  
  - Real-time frequency and volume updates while playing
  - Exposes audio state and control methods to components

- **State Management**: Uses React hooks for local state
  - Audio playback state managed by useAudioEngine hook
  - Custom frequency storage in main App component
  - UI state (active tabs, forms, search) distributed across components

- **UI Components** (`src/components/`):
  - `Header.jsx` - App title and headphone warning
  - `StatusDisplay.jsx` - Current frequency display and volume controls
  - `MainControls.jsx` - Play/stop button, wave type selector, quick frequency input
  - `SearchBar.jsx` - Frequency search functionality
  - `CategoryTabs.jsx` - Tabbed navigation for frequency categories
  - `CustomFrequencyForm.jsx` - Form for adding custom frequencies
  - `FrequencyList.jsx` - Display and interaction with frequency presets
  - `Footer.jsx` - Help text and tips

- **Data Layer** (`src/data/frequencyDatabase.js`): Static frequency presets
  - Therapeutic frequencies (Solfeggio, chakra tones)
  - Musical notes (standard pitch)
  - Binaural beats for brainwave entrainment
  - Brainwave frequency categories

### Key Features

- **Frequency Categories**: 
  - Therapeutic frequencies (Solfeggio, chakra tones)
  - Musical notes (standard pitch)
  - Binaural beats for brainwave entrainment
  - Custom user-defined frequencies

- **Audio Controls**:
  - Independent left/right channel control
  - Volume sliders with safety limiting (max 30%)
  - Multiple waveform selection
  - Real-time frequency adjustment

## Development Notes

- Modular React component architecture with clear separation of concerns
- Custom hook pattern for audio engine encapsulation
- No build process, testing framework, or linting configuration detected
- Uses modern React hooks and functional components
- Relies on browser Web Audio API support
- Mobile-optimized with touch-friendly controls

## File Structure

```
/
├── index.jsx                           # Entry point (imports from src/App)
├── index.html                          # HTML template for deployment
├── package.json                        # NPM dependencies and scripts
├── firebase.json                       # Firebase hosting configuration
├── .env                               # Environment variables (not committed)
├── .gitignore                         # Git ignore rules
├── .github/
│   └── workflows/
│       └── firebase-deploy.yml        # GitHub Actions CI/CD workflow
├── src/
│   ├── App.jsx                        # Main application component
│   ├── firebase/
│   │   └── config.js                  # Firebase initialization
│   ├── hooks/
│   │   └── useAudioEngine.js          # Audio engine custom hook
│   ├── components/
│   │   ├── Header.jsx                 # App header with branding
│   │   ├── StatusDisplay.jsx          # Current playing status & volume
│   │   ├── MainControls.jsx           # Play/stop & wave controls
│   │   ├── SearchBar.jsx              # Frequency search
│   │   ├── CategoryTabs.jsx           # Category navigation
│   │   ├── CustomFrequencyForm.jsx    # Add custom frequencies
│   │   ├── FrequencyList.jsx          # Display frequency presets
│   │   └── Footer.jsx                 # Help text
│   └── data/
│       └── frequencyDatabase.js       # Static frequency presets
```

## Component Dependencies

- All components are functional React components using hooks
- `App.jsx` orchestrates all other components and manages shared state
- `useAudioEngine` hook provides audio functionality to any component that needs it
- Components are designed to be reusable and have clear prop interfaces

## Firebase Setup

The project is configured to use Firebase for analytics and potential future features.

### Configuration Files
- `.env` - Contains Firebase configuration variables (not committed to git)
- `src/firebase/config.js` - Firebase initialization using environment variables

### Environment Variables
Required environment variables in `.env`:
```
REACT_APP_FIREBASE_API_KEY=your_api_key
REACT_APP_FIREBASE_AUTH_DOMAIN=your_auth_domain
REACT_APP_FIREBASE_PROJECT_ID=your_project_id
REACT_APP_FIREBASE_STORAGE_BUCKET=your_storage_bucket
REACT_APP_FIREBASE_MESSAGING_SENDER_ID=your_sender_id
REACT_APP_FIREBASE_APP_ID=your_app_id
REACT_APP_FIREBASE_MEASUREMENT_ID=your_measurement_id
```

### Usage
Import Firebase services from `src/firebase/config.js`:
```javascript
import { app, analytics } from './firebase/config';
```

## GitHub Actions CI/CD

The project includes automated testing and deployment to Firebase Hosting via GitHub Actions.

### Workflow Configuration
- **File**: `.github/workflows/firebase-deploy.yml`
- **Triggers**: Push to `main` branch, Pull Requests
- **Jobs**: 
  - `test`: Runs tests and builds the application
  - `deploy`: Deploys to Firebase Hosting (only on main branch pushes)

### Required GitHub Environment Setup
To enable deployment, configure a GitHub Environment named "FIREBASE" in your repository settings:

1. **Go to GitHub Repository Settings → Environments**
2. **Create Environment named "FIREBASE"**
3. **Add Environment Secrets:**

**Firebase Configuration Secrets:**
- `REACT_APP_FIREBASE_API_KEY`
- `REACT_APP_FIREBASE_AUTH_DOMAIN`
- `REACT_APP_FIREBASE_PROJECT_ID`
- `REACT_APP_FIREBASE_STORAGE_BUCKET`
- `REACT_APP_FIREBASE_MESSAGING_SENDER_ID`
- `REACT_APP_FIREBASE_APP_ID`
- `REACT_APP_FIREBASE_MEASUREMENT_ID`

**Firebase Service Account:**
- `FIREBASE_SERVICE_ACCOUNT_BUZZY_FE536` - JSON service account key

### Setting Up Firebase Service Account
1. Go to Firebase Console → Project Settings → Service Accounts
2. Generate new private key (downloads JSON file)
3. Copy entire JSON content to GitHub environment secret `FIREBASE_SERVICE_ACCOUNT_BUZZY_FE536`

### Environment Benefits
- Centralized secret management
- Environment-specific deployment controls
- Better security isolation
- Deployment approval workflows (optional)

### Build Process
- `npm run build` creates a `dist/` folder with static files
- Firebase Hosting serves from the `dist/` directory
- Includes HTML file with CDN dependencies for React and Firebase

## Dependencies

- `firebase` - Firebase SDK for analytics and future features
- React and Lucide React icons (loaded via CDN)
- Tailwind CSS (loaded via CDN)

## Browser Requirements

- Modern browser with Web Audio API support
- Headphones recommended for binaural beat functionality
- JavaScript enabled for Firebase analytics