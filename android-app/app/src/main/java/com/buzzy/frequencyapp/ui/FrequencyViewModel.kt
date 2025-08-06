package com.buzzy.frequencyapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buzzy.frequencyapp.audio.AudioEngine
import com.buzzy.frequencyapp.data.FrequencyDatabase
import com.buzzy.frequencyapp.models.BinauralFrequency
import com.buzzy.frequencyapp.models.Frequency
import com.buzzy.frequencyapp.models.FrequencyItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FrequencyUiState(
    val isPlaying: Boolean = false,
    val leftFreq: Float = 440f,
    val rightFreq: Float = 440f,
    val leftVolume: Float = 0.1f,
    val rightVolume: Float = 0.1f,
    val waveType: AudioEngine.WaveType = AudioEngine.WaveType.SINE,
    val activeTab: String = "therapeutic",
    val searchTerm: String = "",
    val customFrequencies: List<FrequencyItem> = emptyList(),
    val showAddCustom: Boolean = false,
    val categories: List<String> = listOf("custom", "therapeutic", "musical", "binaural", "brainwaves"),
    val filteredFrequencies: List<FrequencyItem> = emptyList()
)

class FrequencyViewModel : ViewModel() {
    private val audioEngine = AudioEngine()
    
    private val _uiState = MutableStateFlow(FrequencyUiState())
    val uiState: StateFlow<FrequencyUiState> = _uiState.asStateFlow()
    
    init {
        updateFilteredFrequencies()
    }
    
    fun startAudio() {
        audioEngine.leftFrequency = _uiState.value.leftFreq
        audioEngine.rightFrequency = _uiState.value.rightFreq
        audioEngine.leftVolume = _uiState.value.leftVolume
        audioEngine.rightVolume = _uiState.value.rightVolume
        audioEngine.waveType = _uiState.value.waveType
        audioEngine.startAudio()
        
        _uiState.update { it.copy(isPlaying = true) }
    }
    
    fun stopAudio() {
        audioEngine.stopAudio()
        _uiState.update { it.copy(isPlaying = false) }
    }
    
    fun setWaveType(waveType: AudioEngine.WaveType) {
        audioEngine.waveType = waveType
        _uiState.update { it.copy(waveType = waveType) }
    }
    
    fun updateVolume(channel: AudioEngine.Channel, volume: Float) {
        audioEngine.updateVolume(channel, volume)
        _uiState.update { state ->
            when (channel) {
                AudioEngine.Channel.LEFT -> state.copy(leftVolume = volume)
                AudioEngine.Channel.RIGHT -> state.copy(rightVolume = volume)
                AudioEngine.Channel.BOTH -> state.copy(leftVolume = volume, rightVolume = volume)
            }
        }
    }
    
    fun applyFrequency(frequency: Float, channel: AudioEngine.Channel = AudioEngine.Channel.BOTH) {
        audioEngine.applyFrequency(frequency, channel)
        _uiState.update { state ->
            when (channel) {
                AudioEngine.Channel.LEFT -> state.copy(leftFreq = frequency)
                AudioEngine.Channel.RIGHT -> state.copy(rightFreq = frequency)
                AudioEngine.Channel.BOTH -> state.copy(leftFreq = frequency, rightFreq = frequency)
            }
        }
    }
    
    fun applyBinauralBeat(leftFreq: Float, rightFreq: Float) {
        audioEngine.applyBinauralBeat(leftFreq, rightFreq)
        _uiState.update { state ->
            state.copy(leftFreq = leftFreq, rightFreq = rightFreq)
        }
    }
    
    fun setActiveTab(tab: String) {
        _uiState.update { it.copy(activeTab = tab) }
        updateFilteredFrequencies()
    }
    
    fun setSearchTerm(term: String) {
        _uiState.update { it.copy(searchTerm = term) }
        updateFilteredFrequencies()
    }
    
    fun toggleAddCustomForm() {
        _uiState.update { it.copy(showAddCustom = !it.showAddCustom) }
    }
    
    fun saveCustomFrequency(name: String, description: String, leftFreq: Float?, rightFreq: Float?) {
        val newItem = if (rightFreq != null && leftFreq != null && leftFreq != rightFreq) {
            FrequencyItem.Binaural(
                BinauralFrequency(name, leftFreq, rightFreq, description)
            )
        } else {
            FrequencyItem.Mono(
                Frequency(name, leftFreq ?: 440f, description)
            )
        }
        
        _uiState.update { state ->
            state.copy(
                customFrequencies = state.customFrequencies + newItem,
                showAddCustom = false
            )
        }
        updateFilteredFrequencies()
    }
    
    fun removeCustomFrequency(index: Int) {
        _uiState.update { state ->
            state.copy(
                customFrequencies = state.customFrequencies.filterIndexed { i, _ -> i != index }
            )
        }
        updateFilteredFrequencies()
    }
    
    private fun updateFilteredFrequencies() {
        viewModelScope.launch {
            val state = _uiState.value
            val allFrequencies = when (state.activeTab) {
                "custom" -> state.customFrequencies
                "therapeutic" -> FrequencyDatabase.therapeutic.map { FrequencyItem.Mono(it) }
                "musical" -> FrequencyDatabase.musical.map { FrequencyItem.Mono(it) }
                "binaural" -> FrequencyDatabase.binaural.map { FrequencyItem.Binaural(it) }
                "brainwaves" -> FrequencyDatabase.brainwaves.map { FrequencyItem.Mono(it) }
                else -> emptyList()
            }
            
            val filtered = if (state.searchTerm.isEmpty()) {
                allFrequencies
            } else {
                allFrequencies.filter { item ->
                    when (item) {
                        is FrequencyItem.Mono -> {
                            item.frequency.name.contains(state.searchTerm, ignoreCase = true) ||
                            item.frequency.description.contains(state.searchTerm, ignoreCase = true)
                        }
                        is FrequencyItem.Binaural -> {
                            item.frequency.name.contains(state.searchTerm, ignoreCase = true) ||
                            item.frequency.description.contains(state.searchTerm, ignoreCase = true)
                        }
                    }
                }
            }
            
            _uiState.update { it.copy(filteredFrequencies = filtered) }
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        audioEngine.stopAudio()
    }
}