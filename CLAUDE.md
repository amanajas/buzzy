# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a React-based frequency generator and binaural beats application called "Frequency App". The application is a single-page mobile-optimized web app that generates audio frequencies for therapeutic, musical, and brainwave entrainment purposes.

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
├── src/
│   ├── App.jsx                        # Main application component
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

## Browser Requirements

- Modern browser with Web Audio API support
- Headphones recommended for binaural beat functionality
- No external dependencies beyond React and Lucide React icons