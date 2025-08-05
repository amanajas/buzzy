import React from 'react';
import { Volume2 } from 'lucide-react';

const StatusDisplay = ({ 
  leftFreq, 
  rightFreq, 
  isPlaying, 
  leftVolume, 
  rightVolume, 
  updateVolume 
}) => {
  return (
    <div className="p-4 bg-gray-50 border-b">
      <div className="flex items-center justify-between mb-3">
        <div className="text-sm">
          <span className="text-blue-600 font-medium">L: {leftFreq} Hz</span>
          <span className="mx-2 text-gray-400">|</span>
          <span className="text-red-600 font-medium">R: {rightFreq} Hz</span>
        </div>
        <div className={`w-3 h-3 rounded-full ${isPlaying ? 'bg-green-500 animate-pulse' : 'bg-gray-300'}`}></div>
      </div>
      
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
              updateVolume('right', vol);
            }}
            className="w-full h-2 bg-red-200 rounded-full appearance-none slider"
          />
        </div>
      </div>
    </div>
  );
};

export default StatusDisplay;