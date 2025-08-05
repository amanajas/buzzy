import React from 'react';
import { Settings, Trash2 } from 'lucide-react';

const FrequencyList = ({ 
  activeTab, 
  frequencies, 
  applyFrequency, 
  applyBinauralBeat, 
  removeCustomFrequency 
}) => {
  if (activeTab === 'custom' && frequencies.length === 0) {
    return (
      <div className="p-8 text-center text-gray-500">
        <Settings className="w-12 h-12 mx-auto mb-3 text-gray-400" />
        <p className="mb-2">No custom frequencies yet</p>
        <p className="text-sm">Tap "Add Custom" to create your first frequency!</p>
      </div>
    );
  }

  return (
    <div className="max-h-96 overflow-y-auto">
      <div className="p-4">
        {frequencies.map((item, index) => (
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
    </div>
  );
};

export default FrequencyList;