import React, { useState } from 'react';
import { Play, Square } from 'lucide-react';

const MainControls = ({ 
  isPlaying, 
  startAudio, 
  stopAudio, 
  waveType, 
  setWaveType, 
  applyFrequency 
}) => {
  const [customFreq, setCustomFreq] = useState('');

  const addCustomFrequency = () => {
    const freq = parseFloat(customFreq);
    if (freq && freq >= 20 && freq <= 20000) {
      applyFrequency(freq);
      setCustomFreq('');
    }
  };

  return (
    <div className="p-4 border-b">
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
  );
};

export default MainControls;