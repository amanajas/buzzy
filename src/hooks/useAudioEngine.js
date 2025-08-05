import { useRef, useCallback, useState } from 'react';

export const useAudioEngine = () => {
  const [isPlaying, setIsPlaying] = useState(false);
  const [leftFreq, setLeftFreq] = useState(440);
  const [rightFreq, setRightFreq] = useState(440);
  const [leftVolume, setLeftVolume] = useState(0.1);
  const [rightVolume, setRightVolume] = useState(0.1);
  const [waveType, setWaveType] = useState('sine');
  
  const audioContextRef = useRef(null);
  const leftOscillatorRef = useRef(null);
  const rightOscillatorRef = useRef(null);
  const leftGainRef = useRef(null);
  const rightGainRef = useRef(null);
  const mergerRef = useRef(null);

  const initializeAudio = useCallback(() => {
    if (!audioContextRef.current) {
      audioContextRef.current = new (window.AudioContext || window.webkitAudioContext)();
    }
    return audioContextRef.current;
  }, []);

  const startAudio = useCallback(async () => {
    const audioContext = initializeAudio();
    
    if (audioContext.state === 'suspended') {
      await audioContext.resume();
    }

    leftOscillatorRef.current = audioContext.createOscillator();
    rightOscillatorRef.current = audioContext.createOscillator();
    leftGainRef.current = audioContext.createGain();
    rightGainRef.current = audioContext.createGain();
    mergerRef.current = audioContext.createChannelMerger(2);
    
    leftOscillatorRef.current.type = waveType;
    leftOscillatorRef.current.frequency.setValueAtTime(leftFreq, audioContext.currentTime);
    leftGainRef.current.gain.setValueAtTime(leftVolume, audioContext.currentTime);
    
    rightOscillatorRef.current.type = waveType;
    rightOscillatorRef.current.frequency.setValueAtTime(rightFreq, audioContext.currentTime);
    rightGainRef.current.gain.setValueAtTime(rightVolume, audioContext.currentTime);
    
    leftOscillatorRef.current.connect(leftGainRef.current);
    leftGainRef.current.connect(mergerRef.current, 0, 0);
    rightOscillatorRef.current.connect(rightGainRef.current);
    rightGainRef.current.connect(mergerRef.current, 0, 1);
    mergerRef.current.connect(audioContext.destination);
    
    leftOscillatorRef.current.start();
    rightOscillatorRef.current.start();
    
    setIsPlaying(true);
  }, [leftFreq, rightFreq, leftVolume, rightVolume, waveType]);

  const stopAudio = useCallback(() => {
    if (leftOscillatorRef.current) {
      leftOscillatorRef.current.stop();
      leftOscillatorRef.current.disconnect();
      leftOscillatorRef.current = null;
    }
    
    if (rightOscillatorRef.current) {
      rightOscillatorRef.current.stop();
      rightOscillatorRef.current.disconnect();
      rightOscillatorRef.current = null;
    }
    
    if (leftGainRef.current) {
      leftGainRef.current.disconnect();
      leftGainRef.current = null;
    }
    
    if (rightGainRef.current) {
      rightGainRef.current.disconnect();
      rightGainRef.current = null;
    }
    
    if (mergerRef.current) {
      mergerRef.current.disconnect();
      mergerRef.current = null;
    }
    
    setIsPlaying(false);
  }, []);

  const updateFrequency = useCallback((channel, frequency) => {
    if (channel === 'left' && leftOscillatorRef.current) {
      leftOscillatorRef.current.frequency.setValueAtTime(
        frequency, 
        audioContextRef.current.currentTime
      );
    } else if (channel === 'right' && rightOscillatorRef.current) {
      rightOscillatorRef.current.frequency.setValueAtTime(
        frequency, 
        audioContextRef.current.currentTime
      );
    }
  }, []);

  const updateVolume = useCallback((channel, volume) => {
    if (channel === 'left' && leftGainRef.current) {
      leftGainRef.current.gain.setValueAtTime(
        volume, 
        audioContextRef.current.currentTime
      );
    } else if (channel === 'right' && rightGainRef.current) {
      rightGainRef.current.gain.setValueAtTime(
        volume, 
        audioContextRef.current.currentTime
      );
    }
  }, []);

  const applyFrequency = (freq, channel = 'both') => {
    if (channel === 'both' || channel === 'left') {
      setLeftFreq(freq);
      updateFrequency('left', freq);
    }
    if (channel === 'both' || channel === 'right') {
      setRightFreq(freq);
      updateFrequency('right', freq);
    }
  };

  const applyBinauralBeat = (leftFreq, rightFreq) => {
    setLeftFreq(leftFreq);
    setRightFreq(rightFreq);
    updateFrequency('left', leftFreq);
    updateFrequency('right', rightFreq);
  };

  return {
    // State
    isPlaying,
    leftFreq,
    rightFreq,
    leftVolume,
    rightVolume,
    waveType,
    
    // Setters
    setLeftFreq,
    setRightFreq,
    setLeftVolume,
    setRightVolume,
    setWaveType,
    
    // Methods
    startAudio,
    stopAudio,
    updateFrequency,
    updateVolume,
    applyFrequency,
    applyBinauralBeat
  };
};