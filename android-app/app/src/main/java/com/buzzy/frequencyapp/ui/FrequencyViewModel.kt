package com.buzzy.frequencyapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buzzy.frequencyapp.audio.AudioEngine
import com.buzzy.frequencyapp.data.DataManager
import com.buzzy.frequencyapp.data.FrequencyDatabase
import com.buzzy.frequencyapp.models.BinauralFrequency
import com.buzzy.frequencyapp.models.Frequency
import com.buzzy.frequencyapp.models.FrequencyItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class FrequencyUiState(
    val isPlaying: Boolean = false,
    val leftFreq: Float = 440f,
    val rightFreq: Float = 440f,
    val leftVolume: Float = 0.1f,
    val rightVolume: Float = 0.1f,
    val waveType: AudioEngine.WaveType = AudioEngine.WaveType.SINE,
    val selectedLeftFrequency: FrequencyItem? = null,
    val selectedRightFrequency: FrequencyItem? = null,
    val activeTab: String = "therapeutic",
    val searchTerm: String = "",
    val customFrequencies: List<FrequencyItem> = emptyList(),
    val showAddCustom: Boolean = false,
    val categories: List<String> = listOf("custom", "therapeutic", "musical", "binaural", "brainwaves"),
    val filteredFrequencies: List<FrequencyItem> = emptyList(),
    val showDeleteConfirmation: Boolean = false,
    val itemToDelete: Int? = null,
    val currentLanguage: String = "en"
)

class FrequencyViewModel(
    private val dataManager: DataManager
) : ViewModel() {
    private val audioEngine = AudioEngine()
    
    private val _uiState = MutableStateFlow(FrequencyUiState())
    val uiState: StateFlow<FrequencyUiState> = _uiState.asStateFlow()
    
    init {
        loadPreferences()
        updateFilteredFrequencies()
    }
    
    private fun loadPreferences() {
        viewModelScope.launch {
            // Load custom frequencies
            dataManager.getCustomFrequencies().collect { customFrequencies ->
                _uiState.update { it.copy(customFrequencies = customFrequencies) }
                updateFilteredFrequencies()
            }
        }
        
        viewModelScope.launch {
            // Load selected frequencies
            combine(
                dataManager.getSelectedLeftFrequency(),
                dataManager.getSelectedRightFrequency()
            ) { left, right ->
                Pair(left, right)
            }.collect { (leftFreq, rightFreq) ->
                _uiState.update { state ->
                    state.copy(
                        selectedLeftFrequency = leftFreq,
                        selectedRightFrequency = rightFreq,
                        leftFreq = when (leftFreq) {
                            is FrequencyItem.Mono -> leftFreq.frequency.freq
                            is FrequencyItem.Binaural -> leftFreq.frequency.left
                            null -> state.leftFreq
                        },
                        rightFreq = when (rightFreq) {
                            is FrequencyItem.Mono -> rightFreq.frequency.freq
                            is FrequencyItem.Binaural -> rightFreq.frequency.right
                            null -> state.rightFreq
                        }
                    )
                }
            }
        }
        
        viewModelScope.launch {
            // Load volume preferences
            combine(
                dataManager.getLeftVolume(),
                dataManager.getRightVolume()
            ) { left, right ->
                Pair(left, right)
            }.collect { (leftVolume, rightVolume) ->
                _uiState.update { it.copy(leftVolume = leftVolume, rightVolume = rightVolume) }
                audioEngine.leftVolume = leftVolume
                audioEngine.rightVolume = rightVolume
            }
        }
        
        viewModelScope.launch {
            // Load wave type preference
            dataManager.getWaveType().collect { waveType ->
                _uiState.update { it.copy(waveType = waveType) }
                audioEngine.waveType = waveType
            }
        }
        
        viewModelScope.launch {
            // Load active tab preference
            dataManager.getActiveTab().collect { activeTab ->
                _uiState.update { it.copy(activeTab = activeTab) }
                updateFilteredFrequencies()
            }
        }
        
        viewModelScope.launch {
            // Load language preference
            dataManager.getLanguage().collect { language ->
                _uiState.update { it.copy(currentLanguage = language) }
            }
        }
    }
    
    fun startAudio() {
        val currentState = _uiState.value
        
        // Don't start audio if no frequencies are selected
        if (currentState.selectedLeftFrequency == null && currentState.selectedRightFrequency == null) {
            return
        }
        
        audioEngine.leftFrequency = currentState.leftFreq
        audioEngine.rightFrequency = currentState.rightFreq
        audioEngine.leftVolume = currentState.leftVolume
        audioEngine.rightVolume = currentState.rightVolume
        audioEngine.waveType = currentState.waveType
        
        audioEngine.startAudio()
        _uiState.update { it.copy(isPlaying = true) }
    }
    
    fun stopAudio() {
        audioEngine.stopAudio()
        _uiState.update { it.copy(isPlaying = false) }
    }
    
    fun setWaveType(waveType: AudioEngine.WaveType) {
        val wasPlaying = _uiState.value.isPlaying
        
        if (wasPlaying) {
            // Stop current audio, update wave type, then restart
            audioEngine.stopAudio()
        }
        
        audioEngine.waveType = waveType
        _uiState.update { it.copy(waveType = waveType) }
        
        // Save wave type preference
        viewModelScope.launch {
            dataManager.saveWaveType(waveType)
        }
        
        if (wasPlaying) {
            // Restart audio with new wave type
            audioEngine.leftFrequency = _uiState.value.leftFreq
            audioEngine.rightFrequency = _uiState.value.rightFreq
            audioEngine.leftVolume = _uiState.value.leftVolume
            audioEngine.rightVolume = _uiState.value.rightVolume
            audioEngine.startAudio()
        }
    }

    
    fun updateVolume(channel: AudioEngine.Channel, volume: Float) {
        // Update the volume in the state first
        _uiState.update { state ->
            when (channel) {
                AudioEngine.Channel.LEFT -> state.copy(leftVolume = volume)
                AudioEngine.Channel.RIGHT -> state.copy(rightVolume = volume)
                AudioEngine.Channel.BOTH -> state.copy(leftVolume = volume, rightVolume = volume)
            }
        }
        
        // Apply volume changes directly
        audioEngine.updateVolume(channel, volume)
        
        // Save volume preferences
        viewModelScope.launch {
            val state = _uiState.value
            dataManager.saveVolume(state.leftVolume, state.rightVolume)
        }
    }
    
    fun setFrequencyForEar(frequencyItem: FrequencyItem, channel: AudioEngine.Channel) {
        val currentState = _uiState.value
        
        when (channel) {
            AudioEngine.Channel.LEFT -> {
                val leftFreq = when (frequencyItem) {
                    is FrequencyItem.Mono -> frequencyItem.frequency.freq
                    is FrequencyItem.Binaural -> frequencyItem.frequency.left
                }
                
                _uiState.update { state ->
                    state.copy(
                        selectedLeftFrequency = frequencyItem,
                        leftFreq = leftFreq
                    )
                }
                
                // Update audio engine
                audioEngine.leftFrequency = leftFreq
                if (currentState.isPlaying) {
                    // Audio is already playing, keep the volume as it was
                    audioEngine.leftVolume = currentState.leftVolume
                }
            }
            
            AudioEngine.Channel.RIGHT -> {
                val rightFreq = when (frequencyItem) {
                    is FrequencyItem.Mono -> frequencyItem.frequency.freq
                    is FrequencyItem.Binaural -> frequencyItem.frequency.right
                }
                
                _uiState.update { state ->
                    state.copy(
                        selectedRightFrequency = frequencyItem,
                        rightFreq = rightFreq
                    )
                }
                
                // Update audio engine
                audioEngine.rightFrequency = rightFreq
                if (currentState.isPlaying) {
                    // Audio is already playing, keep the volume as it was
                    audioEngine.rightVolume = currentState.rightVolume
                }
            }
            
            AudioEngine.Channel.BOTH -> {
                // Apply to both ears (traditional behavior)
                val freq = when (frequencyItem) {
                    is FrequencyItem.Mono -> frequencyItem.frequency.freq
                    is FrequencyItem.Binaural -> frequencyItem.frequency.left // Use left freq for both
                }
                
                _uiState.update { state ->
                    state.copy(
                        selectedLeftFrequency = frequencyItem,
                        selectedRightFrequency = frequencyItem,
                        leftFreq = freq,
                        rightFreq = freq
                    )
                }
                
                audioEngine.leftFrequency = freq
                audioEngine.rightFrequency = freq
                if (currentState.isPlaying) {
                    audioEngine.leftVolume = currentState.leftVolume
                    audioEngine.rightVolume = currentState.rightVolume
                }
            }
        }
        
        // Save selected frequencies
        viewModelScope.launch {
            val state = _uiState.value
            dataManager.saveSelectedFrequencies(
                state.selectedLeftFrequency,
                state.selectedRightFrequency
            )
        }
        
        // Don't auto-start audio - user must press Play button
    }
    
    fun toggleFrequencyForEar(frequencyItem: FrequencyItem, channel: AudioEngine.Channel) {
        val currentState = _uiState.value
        
        // Check if this frequency is already selected for the specified channel
        val shouldDeselect = when (channel) {
            AudioEngine.Channel.LEFT -> currentState.selectedLeftFrequency == frequencyItem
            AudioEngine.Channel.RIGHT -> currentState.selectedRightFrequency == frequencyItem
            AudioEngine.Channel.BOTH -> currentState.selectedLeftFrequency == frequencyItem && currentState.selectedRightFrequency == frequencyItem
        }
        
        if (shouldDeselect) {
            // Deselect the frequency
            deselectFrequencyForEar(channel)
        } else {
            // Select the frequency
            setFrequencyForEar(frequencyItem, channel)
        }
    }
    
    fun deselectFrequencyForEar(channel: AudioEngine.Channel) {
        when (channel) {
            AudioEngine.Channel.LEFT -> {
                _uiState.update { state ->
                    state.copy(selectedLeftFrequency = null)
                }
                // Stop left audio if playing
                if (_uiState.value.isPlaying) {
                    audioEngine.leftVolume = 0f
                }
            }
            
            AudioEngine.Channel.RIGHT -> {
                _uiState.update { state ->
                    state.copy(selectedRightFrequency = null)
                }
                // Stop right audio if playing
                if (_uiState.value.isPlaying) {
                    audioEngine.rightVolume = 0f
                }
            }
            
            AudioEngine.Channel.BOTH -> {
                _uiState.update { state ->
                    state.copy(
                        selectedLeftFrequency = null,
                        selectedRightFrequency = null
                    )
                }
                // Stop both audio channels if playing
                if (_uiState.value.isPlaying) {
                    audioEngine.leftVolume = 0f
                    audioEngine.rightVolume = 0f
                }
            }
        }
        
        // Save selected frequencies after deselection
        viewModelScope.launch {
            val state = _uiState.value
            dataManager.saveSelectedFrequencies(
                state.selectedLeftFrequency,
                state.selectedRightFrequency
            )
        }
    }
    
    fun applyFrequency(frequency: Float, channel: AudioEngine.Channel = AudioEngine.Channel.BOTH) {
        // Legacy function - create a temporary FrequencyItem for compatibility
        val tempItem = FrequencyItem.Mono(Frequency("Custom", frequency, "Direct frequency input"))
        setFrequencyForEar(tempItem, channel)
    }
    
    fun applyBinauralBeat(leftFreq: Float, rightFreq: Float) {
        // Create a binaural frequency item
        val binauralItem = FrequencyItem.Binaural(
            BinauralFrequency("Custom Binaural", leftFreq, rightFreq, "Custom binaural beat")
        )
        
        // Apply binaural beat (always uses both ears)
        _uiState.update { state ->
            state.copy(
                selectedLeftFrequency = binauralItem,
                selectedRightFrequency = binauralItem,
                leftFreq = leftFreq,
                rightFreq = rightFreq
            )
        }
        
        audioEngine.leftFrequency = leftFreq
        audioEngine.rightFrequency = rightFreq
        audioEngine.leftVolume = _uiState.value.leftVolume
        audioEngine.rightVolume = _uiState.value.rightVolume
        
        // Don't auto-start audio - user must press Play button
    }
    
    
    fun setActiveTab(tab: String) {
        _uiState.update { it.copy(activeTab = tab) }
        
        // Save active tab preference
        viewModelScope.launch {
            dataManager.saveActiveTab(tab)
        }
        
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
        
        // Save custom frequencies
        viewModelScope.launch {
            dataManager.saveCustomFrequencies(_uiState.value.customFrequencies)
        }
        
        updateFilteredFrequencies()
    }
    
    fun showDeleteConfirmation(index: Int) {
        _uiState.update { state ->
            state.copy(
                showDeleteConfirmation = true,
                itemToDelete = index
            )
        }
    }
    
    fun hideDeleteConfirmation() {
        _uiState.update { state ->
            state.copy(
                showDeleteConfirmation = false,
                itemToDelete = null
            )
        }
    }
    
    fun confirmDeleteCustomFrequency() {
        val indexToDelete = _uiState.value.itemToDelete
        if (indexToDelete != null) {
            _uiState.update { state ->
                state.copy(
                    customFrequencies = state.customFrequencies.filterIndexed { i, _ -> i != indexToDelete },
                    showDeleteConfirmation = false,
                    itemToDelete = null
                )
            }
            
            // Save custom frequencies after removal
            viewModelScope.launch {
                dataManager.saveCustomFrequencies(_uiState.value.customFrequencies)
            }
            
            updateFilteredFrequencies()
        }
    }
    
    fun removeCustomFrequency(index: Int) {
        // This function now just shows the confirmation dialog
        showDeleteConfirmation(index)
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