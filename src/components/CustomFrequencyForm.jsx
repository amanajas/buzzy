import React, { useState } from 'react';
import { Plus, X } from 'lucide-react';

const CustomFrequencyForm = ({ 
  showAddCustom, 
  setShowAddCustom, 
  saveCustomFrequency 
}) => {
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

  const handleSave = () => {
    saveCustomFrequency(newCustom, newBinaural);
    setNewCustom({ name: '', freq: '', description: '', type: 'mono' });
    setNewBinaural({ name: '', leftFreq: '', rightFreq: '', description: '' });
  };

  return (
    <>
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
              onClick={handleSave}
              className="w-full mt-4 py-3 bg-green-500 hover:bg-green-600 text-white rounded-xl font-medium transition-all active:scale-95"
            >
              Save Frequency
            </button>
          </div>
        </div>
      )}
    </>
  );
};

export default CustomFrequencyForm;