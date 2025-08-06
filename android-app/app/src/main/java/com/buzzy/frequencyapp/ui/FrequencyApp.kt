package com.buzzy.frequencyapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.buzzy.frequencyapp.ui.components.*

@Composable
fun FrequencyApp(
    viewModel: FrequencyViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFEDE7F6),
                        Color(0xFFE3F2FD)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header
                    Header()
                    
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                    
                    // Status Display
                    StatusDisplay(
                        leftFreq = uiState.leftFreq,
                        rightFreq = uiState.rightFreq,
                        isPlaying = uiState.isPlaying,
                        leftVolume = uiState.leftVolume,
                        rightVolume = uiState.rightVolume,
                        onVolumeChange = viewModel::updateVolume
                    )
                    
                    // Main Controls
                    MainControls(
                        isPlaying = uiState.isPlaying,
                        waveType = uiState.waveType,
                        onPlayPause = {
                            if (uiState.isPlaying) {
                                viewModel.stopAudio()
                            } else {
                                viewModel.startAudio()
                            }
                        },
                        onWaveTypeChange = viewModel::setWaveType,
                        onApplyFrequency = viewModel::applyFrequency
                    )
                    
                    // Search Bar
                    SearchBar(
                        searchTerm = uiState.searchTerm,
                        onSearchTermChange = viewModel::setSearchTerm
                    )
                    
                    // Category Tabs
                    CategoryTabs(
                        activeTab = uiState.activeTab,
                        categories = uiState.categories,
                        onTabChange = viewModel::setActiveTab
                    )
                    
                    // Custom Frequency Form (if custom tab is active)
                    if (uiState.activeTab == "custom") {
                        CustomFrequencyForm(
                            showAddCustom = uiState.showAddCustom,
                            onToggleForm = viewModel::toggleAddCustomForm,
                            onSaveCustom = viewModel::saveCustomFrequency
                        )
                    }
                    
                    // Frequency List
                    FrequencyList(
                        frequencies = uiState.filteredFrequencies,
                        activeTab = uiState.activeTab,
                        onApplyFrequency = viewModel::applyFrequency,
                        onApplyBinauralBeat = viewModel::applyBinauralBeat,
                        onRemoveCustom = viewModel::removeCustomFrequency
                    )
                    
                    // Footer
                    Footer()
                }
            }
        }
    }
}