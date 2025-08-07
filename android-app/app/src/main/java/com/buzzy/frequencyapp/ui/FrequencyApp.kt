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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.buzzy.frequencyapp.data.DataManager
import com.buzzy.frequencyapp.ui.components.*

@Composable
fun FrequencyApp(
    viewModel: FrequencyViewModel = viewModel(
        factory = FrequencyViewModelFactory(
            DataManager(LocalContext.current)
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1A1B2E),
                        Color(0xFF16213E)
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
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF0F1425)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header
                    Header()
                    
                    Divider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color.White.copy(alpha = 0.1f)
                    )
                    
                    // Status Display
                    StatusDisplay(
                        leftFreq = uiState.leftFreq,
                        rightFreq = uiState.rightFreq,
                        isPlaying = uiState.isPlaying,
                        leftVolume = uiState.leftVolume,
                        rightVolume = uiState.rightVolume,
                        selectedLeftFrequency = uiState.selectedLeftFrequency,
                        selectedRightFrequency = uiState.selectedRightFrequency,
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
                        selectedLeftFrequency = uiState.selectedLeftFrequency,
                        selectedRightFrequency = uiState.selectedRightFrequency,
                        onToggleFrequencyForEar = viewModel::toggleFrequencyForEar,
                        onApplyBinauralBeat = viewModel::applyBinauralBeat,
                        onRemoveCustom = viewModel::removeCustomFrequency,
                        showDeleteConfirmation = uiState.showDeleteConfirmation,
                        itemToDelete = uiState.itemToDelete,
                        onConfirmDelete = viewModel::confirmDeleteCustomFrequency,
                        onCancelDelete = viewModel::hideDeleteConfirmation
                    )
                    
                    // Footer
                    Footer()
                }
            }
        }
    }
}