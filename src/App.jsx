import React, { useState } from 'react';
import { useAudioEngine } from './hooks/useAudioEngine';
import { frequencyDatabase } from './data/frequencyDatabase';
import Header from './components/Header';
import StatusDisplay from './components/StatusDisplay';
import MainControls from './components/MainControls';
import SearchBar from './components/SearchBar';
import CategoryTabs from './components/CategoryTabs';
import CustomFrequencyForm from './components/CustomFrequencyForm';
import FrequencyList from './components/FrequencyList';
import Footer from './components/Footer';

const App = () => {
  const audioEngine = useAudioEngine();
  const [activeTab, setActiveTab] = useState('therapeutic');
  const [searchTerm, setSearchTerm] = useState('');
  const [customFrequencies, setCustomFrequencies] = useState([]);
  const [showAddCustom, setShowAddCustom] = useState(false);

  const fullFrequencyDatabase = {
    custom: customFrequencies,
    ...frequencyDatabase
  };

  const filteredFrequencies = (category) => {
    const frequencies = fullFrequencyDatabase[category];
    if (!searchTerm) return frequencies;
    
    return frequencies.filter(item =>
      item.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      item.description.toLowerCase().includes(searchTerm.toLowerCase())
    );
  };

  const saveCustomFrequency = (newCustom, newBinaural) => {
    if (newCustom.type === 'mono') {
      const freq = parseFloat(newCustom.freq);
      if (newCustom.name && freq && freq >= 20 && freq <= 20000) {
        const customEntry = {
          name: newCustom.name,
          freq: freq,
          description: newCustom.description || 'Custom frequency'
        };
        setCustomFrequencies(prev => [...prev, customEntry]);
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
        setShowAddCustom(false);
      }
    }
  };

  const removeCustomFrequency = (index) => {
    setCustomFrequencies(prev => prev.filter((_, i) => i !== index));
  };

  const updateVolume = (channel, volume) => {
    if (channel === 'left') {
      audioEngine.setLeftVolume(volume);
    } else {
      audioEngine.setRightVolume(volume);
    }
    audioEngine.updateVolume(channel, volume);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-50 to-blue-50 px-4 py-6">
      <div className="max-w-md mx-auto bg-white rounded-3xl shadow-2xl overflow-hidden">
        
        <Header />

        <StatusDisplay 
          leftFreq={audioEngine.leftFreq}
          rightFreq={audioEngine.rightFreq}
          isPlaying={audioEngine.isPlaying}
          leftVolume={audioEngine.leftVolume}
          rightVolume={audioEngine.rightVolume}
          updateVolume={updateVolume}
        />

        <MainControls 
          isPlaying={audioEngine.isPlaying}
          startAudio={audioEngine.startAudio}
          stopAudio={audioEngine.stopAudio}
          waveType={audioEngine.waveType}
          setWaveType={audioEngine.setWaveType}
          applyFrequency={audioEngine.applyFrequency}
        />

        <SearchBar 
          searchTerm={searchTerm}
          setSearchTerm={setSearchTerm}
        />

        <CategoryTabs 
          activeTab={activeTab}
          setActiveTab={setActiveTab}
          categories={Object.keys(fullFrequencyDatabase)}
        />

        {activeTab === 'custom' && (
          <CustomFrequencyForm 
            showAddCustom={showAddCustom}
            setShowAddCustom={setShowAddCustom}
            saveCustomFrequency={saveCustomFrequency}
          />
        )}

        <FrequencyList 
          activeTab={activeTab}
          frequencies={filteredFrequencies(activeTab)}
          applyFrequency={audioEngine.applyFrequency}
          applyBinauralBeat={audioEngine.applyBinauralBeat}
          removeCustomFrequency={removeCustomFrequency}
        />

        <Footer />
      </div>
    </div>
  );
};

export default App;