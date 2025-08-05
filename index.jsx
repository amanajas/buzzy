import React, { useState, useRef, useCallback } from 'react';
import { Play, Square, Headphones, Volume2, Settings, Music, Brain, Heart, Search, Plus, X, Trash2 } from 'lucide-react';

const FrequencyGenerator = () => {
  const [isPlaying, setIsPlaying] = useState(false);
  const [leftFreq, setLeftFreq] = useState(440);
  const [rightFreq, setRightFreq] = useState(440);
  const [leftVolume, setLeftVolume] = useState(0.1);
  const [rightVolume, setRightVolume] = useState(0.1);
  const [waveType, setWaveType] = useState('sine');
  const [activeTab, setActiveTab] = useState('therapeutic');
  const [searchTerm, setSearchTerm] = useState('');
  const [customFreq, setCustomFreq] = useState('');
  const [customFrequencies, setCustomFrequencies] = useState([]);
  const [showAddCustom, setShowAddCustom] = useState(false);
  const [newCustom, setNewCustom] = useState({
    name: '',
    freq: '',
    description: '',
    type: 'mono'
  });
  const [newBinaural, setNewBinaural] = useState({
    name: '',
    leftFreq: '',
    rightFreq: '',
    description: ''
  });
  
  const audioContextRef = useRef(null);
  const leftOscillatorRef = useRef(null);
  const rightOscillatorRef = useRef(null);
  const leftGainRef = useRef(null);
  const rightGainRef = useRef(null);
  const mergerRef = useRef(null);

  // Comprehensive frequency database
  const frequencyDatabase = {
    custom: customFrequencies,
    therapeutic: [
      { name: '7.83 Hz - Schumann', freq: 7.83, description: 'Earth\'s frequency, grounding' },
      { name: '10 Hz - Alpha', freq: 10, description: 'Relaxed awareness' },
      { name: '40 Hz - Gamma', freq: 40, description: 'Enhanced focus' },
      { name: '174 Hz - Pain Relief', freq: 174, description: 'Natural anesthetic' },
      { name: '285 Hz - Healing', freq: 285, description: 'Cellular regeneration' },
      { name: '396 Hz - Liberation', freq: 396, description: 'Root chakra, fear release' },
      { name: '417 Hz - Change', freq: 417, description: 'Sacral chakra, transformation' },
      { name: '528 Hz - Love', freq: 528, description: 'Heart chakra, DNA repair' },
      { name: '639 Hz - Relationships', freq: 639, description: 'Heart chakra, connecting' },
      { name: '741 Hz - Expression', freq: 741, description: 'Throat chakra' },
      { name: '852 Hz - Intuition', freq: 852, description: 'Third eye chakra' },
      { name: '963 Hz - Divine', freq: 963, description: 'Crown chakra' }
    ],
    musical: [
      { name: 'C4 - 261.63 Hz', freq: 261.63, description: 'Middle C' },
      { name: 'D4 - 293.66 Hz', freq: 293.66, description: 'D note' },
      { name: 'E4 - 329.63 Hz', freq: 329.63, description: 'E note' },
      { name: 'F4 - 349.23 Hz', freq: 349.23, description: 'F note' },
      { name: 'G4 - 392.00 Hz', freq: 392.00, description: 'G note' },
      { name: 'A4 - 440.00 Hz', freq: 440.00, description: 'Concert pitch' },
      { name: 'B4 - 493.88 Hz', freq: 493.88, description: 'B note' },
      { name: 'C5 - 523.25 Hz', freq: 523.25, description: 'High C' }
    ],
    binaural: [
      { name: '1 Hz - Deep Sleep', left: 100, right: 101, description: 'Delta waves' },
      { name: '4 Hz - Meditation', left: 200, right: 204, description: 'Theta waves' },
      { name: '8 Hz - Alpha Relax', left: 432, right: 440, description: 'Alpha waves' },
      { name: '15 Hz - Focus', left: 440, right: 455, description: 'Beta waves' },
      { name: '40 Hz - Cognitive', left: 400, right: 440, description: 'Gamma waves' }
    ],
    brainwaves: [
      { name: '2 Hz - Delta Sleep', freq: 2, description: 'Deep sleep, healing' },
      { name: '6 Hz - Theta Dreams', freq: 6, description: 'Creativity, meditation' },
      { name: '10 Hz - Alpha Calm', freq: 10, description: 'Relaxed awareness' },
      { name: '20 Hz - Beta Alert', freq: 20, description: 'Normal consciousness' },
      { name: '40 Hz - Gamma Focus', freq: 40, description: 'High cognition' }
    ]
  };

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

  const addCustomFrequency = () => {
    const freq = parseFloat(customFreq);
    if (freq && freq >= 20 && freq <= 20000) {
      applyFrequency(freq);
      setCustomFreq('');
    }
  };

  const saveCustomFrequency = () => {
    if (newCustom.type === 'mono') {
      const freq = parseFloat(newCustom.freq);
      if (newCustom.name && freq && freq >= 20 && freq <= 20000) {
        const customEntry = {
          name: newCustom.name,
          freq: freq,
          description: newCustom.description || 'Custom frequency'
        };
        setCustomFrequencies(prev => [...prev, customEntry]);
        setNewCustom({ name: '', freq: '', description: '', type: 'mono' });
        setShowAddCustom(false);
      }
    } else {
      const leftFreq = parseFloat(newBinaural.leftFreq);
      const rightFreq = parseFloat(newBinaural.rightFreq);
      if (newBinaural.name && leftFreq && rightFreq && 
          leftFreq >= 20 && leftFreq <= 20000 && 
          rightFreq >= 20 && rightFreq <= 20000) {
        const binauralEntry = {
          name: newBinaural.name,
          left: leftFreq,
          right: rightFreq,
          description: newBinaural.description || 'Custom binaural beat'
        };
        setCustomFrequencies(prev => [...prev, binauralEntry]);
        setNewBinaural({ name: '', leftFreq: '', rightFreq: '', description: '' });
        setNewCustom({ name: '', freq: '', description: '', type: 'mono' });
        setShowAddCustom(false);
      }
    }
  };

  const removeCustomFrequency = (index) => {
    setCustomFrequencies(prev => prev.filter((_, i) => i !== index));
  };

  const filteredFrequencies = (category) => {
    const frequencies = frequencyDatabase[category];
    if (!searchTerm) return frequencies;
    
    return frequencies.filter(item =>
      item.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      item.description.toLowerCase().includes(searchTerm.toLowerCase())
    );
  };

  const tabIcons = {
    custom: Settings,
    therapeutic: Heart,
    musical: Music,
    binaural: Brain,
    brainwaves: Brain
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-50 to-blue-50 px-4 py-6">
      <div className="max-w-md mx-auto bg-white rounded-3xl shadow-2xl overflow-hidden">
        
        {/* Mobile Header */}
        <div className="bg-gradient-to-r from-purple-600 to-blue-600 text-white p-6 text-center">
          <div className="flex items-center justify-center gap-2 mb-2">
            <Headphones className="w-7 h-7" />
            <h1 className="text-xl font-bold">Frequency App</h1>
          </div>
          <p className="text-purple-100 text-sm">Custom sound therapy & binaural beats</p>
          <div className="mt-3 p-2 bg-yellow-400 bg-opacity-20 rounded-lg">
            <p className="text-xs text-yellow-100">
              🎧 Use headphones for best experience
            </p>
          </div>
        </div>

        {/* Current Playing Status */}
        <div className="p-4 bg-gray-50 border-b">
          <div className="flex items-center justify-between mb-3">
            <div className="text-sm">
              <span className="text-blue-600 font-medium">L: {leftFreq} Hz</span>
              <span className="mx-2 text-gray-400">|</span>
              <span className="text-red-600 font-medium">R: {rightFreq} Hz</span>
            </div>
            <div className={`w-3 h-3 rounded-full ${isPlaying ? 'bg-green-500 animate-pulse' : 'bg-gray-300'}`}></div>
          </div>
          
          {/* Volume Controls */}
          <div className="grid grid-cols-2 gap-3">
            <div className="bg-blue-50 p-3 rounded-xl">
              <div className="flex items-center gap-2 mb-2">
                <Volume2 className="w-4 h-4 text-blue-600" />
                <span className="text-xs font-medium text-blue-800">Left {Math.round(leftVolume * 100)}%</span>
              </div>
              <input
                type="range"
                min="0"
                max="0.3"
                step="0.01"
                value={leftVolume}
                onChange={(e) => {
                  const vol = parseFloat(e.target.value);
                  setLeftVolume(vol);
                  updateVolume('left', vol);
                }}
                className="w-full h-2 bg-blue-200 rounded-full appearance-none slider"
              />
            </div>
            
            <div className="bg-red-50 p-3 rounded-xl">
              <div className="flex items-center gap-2 mb-2">
                <Volume2 className="w-4 h-4 text-red-600" />
                <span className="text-xs font-medium text-red-800">Right {Math.round(rightVolume * 100)}%</span>
              </div>
              <input
                type="range"
                min="0"
                max="0.3"
                step="0.01"
                value={rightVolume}
                onChange={(e) => {
                  const vol = parseFloat(e.target.value);
                  setRightVolume(vol);
                  updateVolume('right', vol);
                }}
                className="w-full h-2 bg-red-200 rounded-full appearance-none slider"
              />
            </div>
          </div>
        </div>

        {/* Main Controls */}
        <div className="p-4 border-b">
          {/* Play/Stop Button */}
          <div className="flex justify-center mb-4">
            {!isPlaying ? (
              <button
                onClick={startAudio}
                className="flex items-center gap-2 px-8 py-4 bg-green-500 hover:bg-green-600 text-white rounded-2xl font-semibold text-lg shadow-lg active:scale-95 transition-all"
              >
                <Play className="w-6 h-6" />
                Start
              </button>
            ) : (
              <button
                onClick={stopAudio}
                className="flex items-center gap-2 px-8 py-4 bg-red-500 hover:bg-red-600 text-white rounded-2xl font-semibold text-lg shadow-lg active:scale-95 transition-all"
              >
                <Square className="w-6 h-6" />
                Stop
              </button>
            )}
          </div>

          {/* Wave Type Selector */}
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-700 mb-2">Wave Type:</label>
            <div className="grid grid-cols-2 gap-2">
              {['sine', 'square', 'sawtooth', 'triangle'].map((type) => (
                <button
                  key={type}
                  onClick={() => setWaveType(type)}
                  className={`py-2 px-3 text-sm rounded-xl border-2 transition-all active:scale-95 ${
                    waveType === type
                      ? 'bg-purple-500 text-white border-purple-500 shadow-md'
                      : 'bg-white text-gray-700 border-gray-300'
                  }`}
                  disabled={isPlaying}
                >
                  {type.charAt(0).toUpperCase() + type.slice(1)}
                </button>
              ))}
            </div>
          </div>

          {/* Quick Custom Input */}
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-700 mb-2">Quick Apply:</label>
            <div className="flex gap-2">
              <input
                type="number"
                placeholder="Enter Hz (20-20000)"
                value={customFreq}
                onChange={(e) => setCustomFreq(e.target.value)}
                className="flex-1 px-3 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-purple-500 focus:border-transparent text-center"
              />
              <button
                onClick={addCustomFrequency}
                className="px-4 py-3 bg-purple-500 hover:bg-purple-600 text-white rounded-xl transition-all active:scale-95"
              >
                Apply
              </button>
            </div>
          </div>
        </div>

        {/* Search */}
        <div className="p-4 border-b">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
            <input
              type="text"
              placeholder="Search frequencies..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-purple-500 focus:border-transparent"
            />
          </div>
        </div>

        {/* Category Tabs */}
        <div className="p-4 border-b">
          <div className="grid grid-cols-2 gap-2">
            {Object.keys(frequencyDatabase).map((category) => {
              const Icon = tabIcons[category];
              return (
                <button
                  key={category}
                  onClick={() => setActiveTab(category)}
                  className={`flex items-center justify-center gap-2 py-3 px-2 rounded-xl border-2 transition-all active:scale-95 ${
                    activeTab === category
                      ? 'bg-purple-500 text-white border-purple-500 shadow-md'
                      : 'bg-white text-gray-700 border-gray-300'
                  }`}
                >
                  <Icon className="w-4 h-4" />
                  <span className="text-xs font-medium">{category.charAt(0).toUpperCase() + category.slice(1)}</span>
                </button>
              );
            })}
          </div>
        </div>

        {/* Add Custom Button */}
        {activeTab === 'custom' && (
          <div className="p-4 border-b">
            <button
              onClick={() => setShowAddCustom(!showAddCustom)}
              className={`w-full flex items-center justify-center gap-2 py-3 px-4 rounded-xl font-medium transition-all active:scale-95 ${
                showAddCustom
                  ? 'bg-red-500 text-white'
                  : 'bg-green-500 text-white'
              }`}
            >
              {showAddCustom ? <X className="w-5 h-5" /> : <Plus className="w-5 h-5" />}
              {showAddCustom ? 'Cancel' : 'Add Custom Frequency'}
            </button>
          </div>
        )}

        {/* Add Custom Form */}
        {showAddCustom && (
          <div className="p-4 border-b bg-gray-50">
            <div className="mb-4">
              <div className="grid grid-cols-2 gap-2 mb-3">
                <button
                  onClick={() => setNewCustom({...newCustom, type: 'mono'})}
                  className={`py-2 px-3 text-sm rounded-xl transition-all ${
                    newCustom.type === 'mono'
                      ? 'bg-blue-500 text-white'
                      : 'bg-white text-gray-700 border border-gray-300'
                  }`}
                >
                  Single Freq
                </button>
                <button
                  onClick={() => setNewCustom({...newCustom, type: 'binaural'})}
                  className={`py-2 px-3 text-sm rounded-xl transition-all ${
                    newCustom.type === 'binaural'
                      ? 'bg-purple-500 text-white'
                      : 'bg-white text-gray-700 border border-gray-300'
                  }`}
                >
                  Binaural
                </button>
              </div>

              {newCustom.type === 'mono' ? (
                <div className="space-y-3">
                  <input
                    type="text"
                    placeholder="Name (e.g., My Healing Tone)"
                    value={newCustom.name}
                    onChange={(e) => setNewCustom({...newCustom, name: e.target.value})}
                    className="w-full px-3 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500"
                  />
                  <input
                    type="number"
                    min="20"
                    max="20000"
                    placeholder="Frequency (Hz)"
                    value={newCustom.freq}
                    onChange={(e) => setNewCustom({...newCustom, freq: e.target.value})}
                    className="w-full px-3 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500"
                  />
                  <input
                    type="text"
                    placeholder="Description (optional)"
                    value={newCustom.description}
                    onChange={(e) => setNewCustom({...newCustom, description: e.target.value})}
                    className="w-full px-3 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500"
                  />
                </div>
              ) : (
                <div className="space-y-3">
                  <input
                    type="text"
                    placeholder="Name (e.g., My Focus Beat)"
                    value={newBinaural.name}
                    onChange={(e) => setNewBinaural({...newBinaural, name: e.target.value})}
                    className="w-full px-3 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-purple-500"
                  />
                  <div className="grid grid-cols-2 gap-2">
                    <input
                      type="number"
                      min="20"
                      max="20000"
                      placeholder="Left Hz"
                      value={newBinaural.leftFreq}
                      onChange={(e) => setNewBinaural({...newBinaural, leftFreq: e.target.value})}
                      className="px-3 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-purple-500"
                    />
                    <input
                      type="number"
                      min="20"
                      max="20000"
                      placeholder="Right Hz"
                      value={newBinaural.rightFreq}
                      onChange={(e) => setNewBinaural({...newBinaural, rightFreq: e.target.value})}
                      className="px-3 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-purple-500"
                    />
                  </div>
                  <input
                    type="text"
                    placeholder="Description (optional)"
                    value={newBinaural.description}
                    onChange={(e) => setNewBinaural({...newBinaural, description: e.target.value})}
                    className="w-full px-3 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-purple-500"
                  />
                </div>
              )}

              <button
                onClick={saveCustomFrequency}
                className="w-full mt-4 py-3 bg-green-500 hover:bg-green-600 text-white rounded-xl font-medium transition-all active:scale-95"
              >
                Save Frequency
              </button>
            </div>
          </div>
        )}

        {/* Frequency List */}
        <div className="max-h-96 overflow-y-auto">
          {activeTab === 'custom' && customFrequencies.length === 0 ? (
            <div className="p-8 text-center text-gray-500">
              <Settings className="w-12 h-12 mx-auto mb-3 text-gray-400" />
              <p className="mb-2">No custom frequencies yet</p>
              <p className="text-sm">Tap "Add Custom" to create your first frequency!</p>
            </div>
          ) : (
            <div className="p-4">
              {filteredFrequencies(activeTab).map((item, index) => (
                <div key={index} className="mb-3 p-4 border border-gray-200 rounded-2xl bg-white">
                  <div className="flex justify-between items-start mb-2">
                    <div className="flex-1 min-w-0">
                      <h4 className="font-semibold text-gray-800 text-sm truncate">{item.name}</h4>
                      <p className="text-xs text-gray-600 mt-1">{item.description}</p>
                      {((activeTab === 'binaural') || (activeTab === 'custom' && item.left && item.right)) && (
                        <p className="text-xs text-purple-600 mt-1">
                          L:{item.left}Hz R:{item.right}Hz Beat:{Math.abs(item.right - item.left)}Hz
                        </p>
                      )}
                    </div>
                    
                    {activeTab === 'custom' && (
                      <button
                        onClick={() => removeCustomFrequency(index)}
                        className="ml-2 p-1 text-red-500 hover:text-red-700 transition-colors"
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    )}
                  </div>
                  
                  <div className="flex gap-1">
                    {(activeTab === 'binaural' || (activeTab === 'custom' && item.left && item.right)) ? (
                      <button
                        onClick={() => applyBinauralBeat(item.left, item.right)}
                        className="flex-1 py-2 px-3 text-xs bg-purple-500 hover:bg-purple-600 text-white rounded-xl transition-all active:scale-95"
                      >
                        Apply Both
                      </button>
                    ) : (
                      <>
                        <button
                          onClick={() => applyFrequency(item.freq, 'left')}
                          className="flex-1 py-2 px-2 text-xs bg-blue-500 hover:bg-blue-600 text-white rounded-xl transition-all active:scale-95"
                        >
                          L
                        </button>
                        <button
                          onClick={() => applyFrequency(item.freq, 'right')}
                          className="flex-1 py-2 px-2 text-xs bg-red-500 hover:bg-red-600 text-white rounded-xl transition-all active:scale-95"
                        >
                          R
                        </button>
                        <button
                          onClick={() => applyFrequency(item.freq)}
                          className="flex-1 py-2 px-2 text-xs bg-purple-500 hover:bg-purple-600 text-white rounded-xl transition-all active:scale-95"
                        >
                          Both
                        </button>
                      </>
                    )}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Bottom Info */}
        <div className="p-4 bg-blue-50 text-center">
          <p className="text-xs text-blue-700">
            <strong>Tip:</strong> Different frequencies in each ear create binaural beats for brainwave entrainment
          </p>
        </div>
      </div>
    </div>
  );
};

export default FrequencyGenerator;
