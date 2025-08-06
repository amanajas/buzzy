import Foundation
import SwiftUI
import Combine

class FrequencyViewModel: ObservableObject {
    @Published var audioEngine = AudioEngine()
    @Published var activeTab = "therapeutic"
    @Published var searchTerm = ""
    @Published var customFrequencies: [FrequencyItem] = []
    @Published var showAddCustom = false
    
    let categories = ["custom", "therapeutic", "musical", "binaural", "brainwaves"]
    
    var filteredFrequencies: [FrequencyItem] {
        var frequencies: [FrequencyItem] = []
        
        if activeTab == "custom" {
            frequencies = customFrequencies
        } else {
            frequencies = FrequencyDatabase.getFrequencies(for: activeTab)
        }
        
        if searchTerm.isEmpty {
            return frequencies
        }
        
        return frequencies.filter { item in
            item.name.localizedCaseInsensitiveContains(searchTerm) ||
            item.description.localizedCaseInsensitiveContains(searchTerm)
        }
    }
    
    func togglePlayPause() {
        if audioEngine.isPlaying {
            audioEngine.stopAudio()
        } else {
            audioEngine.startAudio()
        }
    }
    
    func setWaveType(_ waveType: AudioEngine.WaveType) {
        audioEngine.waveType = waveType
        if audioEngine.isPlaying {
            audioEngine.stopAudio()
            audioEngine.startAudio()
        }
    }
    
    func applyFrequency(_ frequency: Float, channel: AudioEngine.Channel = .both) {
        audioEngine.applyFrequency(frequency, channel: channel)
    }
    
    func applyBinauralBeat(leftFreq: Float, rightFreq: Float) {
        audioEngine.applyBinauralBeat(leftFreq: leftFreq, rightFreq: rightFreq)
    }
    
    func updateVolume(channel: AudioEngine.Channel, volume: Float) {
        audioEngine.updateVolume(channel: channel, volume: volume)
    }
    
    func saveCustomFrequency(name: String, description: String, frequency: Float?, leftFreq: Float?, rightFreq: Float?) {
        let newItem: FrequencyItem
        
        if let left = leftFreq, let right = rightFreq, left != right {
            newItem = .binaural(BinauralFrequency(
                name: name,
                left: left,
                right: right,
                description: description.isEmpty ? "Custom binaural beat" : description
            ))
        } else if let freq = frequency ?? leftFreq {
            newItem = .mono(Frequency(
                name: name,
                freq: freq,
                description: description.isEmpty ? "Custom frequency" : description
            ))
        } else {
            return
        }
        
        customFrequencies.append(newItem)
        showAddCustom = false
    }
    
    func removeCustomFrequency(at index: Int) {
        guard index < customFrequencies.count else { return }
        customFrequencies.remove(at: index)
    }
    
    func applyQuickFrequency(_ frequency: String) {
        guard let freq = Float(frequency),
              freq >= 20, freq <= 20000 else { return }
        applyFrequency(freq)
    }
}