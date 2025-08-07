package com.buzzy.frequencyapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buzzy.frequencyapp.audio.AudioEngine
import com.buzzy.frequencyapp.models.FrequencyItem

@Composable
fun FrequencyList(
    frequencies: List<FrequencyItem>,
    activeTab: String,
    selectedLeftFrequency: FrequencyItem?,
    selectedRightFrequency: FrequencyItem?,
    onToggleFrequencyForEar: (FrequencyItem, AudioEngine.Channel) -> Unit,
    onApplyBinauralBeat: (Float, Float) -> Unit,
    onRemoveCustom: (Int) -> Unit,
    showDeleteConfirmation: Boolean = false,
    itemToDelete: Int? = null,
    onConfirmDelete: () -> Unit = {},
    onCancelDelete: () -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (frequencies.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1E2139)
                    )
                ) {
                    Text(
                        text = if (activeTab == "custom") 
                            "No custom frequencies yet" 
                        else 
                            "No frequencies found",
                        modifier = Modifier.padding(16.dp),
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            itemsIndexed(frequencies) { index, item ->
                FrequencyCard(
                    item = item,
                    isCustom = activeTab == "custom",
                    selectedLeftFrequency = selectedLeftFrequency,
                    selectedRightFrequency = selectedRightFrequency,
                    onToggleFrequencyForEar = onToggleFrequencyForEar,
                    onApplyBinauralBeat = onApplyBinauralBeat,
                    onRemove = if (activeTab == "custom") {
                        { onRemoveCustom(index) }
                    } else null
                )
            }
        }
    }
    
    // Show delete confirmation dialog
    if (showDeleteConfirmation && itemToDelete != null && itemToDelete < frequencies.size) {
        DeleteConfirmationDialog(
            frequencyItem = frequencies[itemToDelete],
            onConfirm = onConfirmDelete,
            onDismiss = onCancelDelete
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FrequencyCard(
    item: FrequencyItem,
    isCustom: Boolean,
    selectedLeftFrequency: FrequencyItem?,
    selectedRightFrequency: FrequencyItem?,
    onToggleFrequencyForEar: (FrequencyItem, AudioEngine.Channel) -> Unit,
    onApplyBinauralBeat: (Float, Float) -> Unit,
    onRemove: (() -> Unit)?
) {
    // Check if this item is selected for any ear
    val isSelectedLeft = selectedLeftFrequency == item
    val isSelectedRight = selectedRightFrequency == item
    val isSelectedBoth = isSelectedLeft && isSelectedRight
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelectedBoth -> Color(0xFF8B5CF6) // Both ears selected - purple
                isSelectedLeft -> Color(0xFF60A5FA) // Left ear only - blue
                isSelectedRight -> Color(0xFFFB7185) // Right ear only - pink/rose
                else -> Color(0xFF1E2139) // Default background
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelectedLeft || isSelectedRight) 4.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Main content row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (item) {
                                is FrequencyItem.Mono -> item.frequency.name
                                is FrequencyItem.Binaural -> item.frequency.name
                            },
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        
                        // Selection indicators
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            if (isSelectedLeft) {
                                Text(
                                    text = "🎧L",
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .background(
                                            if (isSelectedBoth) Color.White.copy(alpha = 0.2f) 
                                            else Color(0xFF1E40AF).copy(alpha = 0.8f), // Blue badge for left ear
                                            RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                            if (isSelectedRight && !isSelectedBoth) {
                                Text(
                                    text = "🎧R",
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .background(
                                            Color(0xFFBE185D).copy(alpha = 0.8f), // Pink badge for right ear
                                            RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                            if (isSelectedBoth) {
                                Text(
                                    text = "🎧Both",
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .background(
                                            Color.White.copy(alpha = 0.2f),
                                            RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    
                    Text(
                        text = when (item) {
                            is FrequencyItem.Mono -> "${item.frequency.freq} Hz"
                            is FrequencyItem.Binaural -> "L: ${item.frequency.left} Hz | R: ${item.frequency.right} Hz"
                        },
                        fontSize = 12.sp,
                        color = Color(0xFF4F46E5)
                    )
                    
                    Text(
                        text = when (item) {
                            is FrequencyItem.Mono -> item.frequency.description
                            is FrequencyItem.Binaural -> item.frequency.description
                        },
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
                
                if (isCustom && onRemove != null) {
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFFEF4444)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Ear selection controls
            if (item is FrequencyItem.Mono) {
                // Mono frequencies can be assigned to individual ears
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { onToggleFrequencyForEar(item, AudioEngine.Channel.LEFT) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF60A5FA) // Blue to match left ear background
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(32.dp)
                    ) {
                        Text(
                            text = "🎧 Left Ear",
                            fontSize = 10.sp,
                            color = Color.White
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = { onToggleFrequencyForEar(item, AudioEngine.Channel.RIGHT) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFB7185) // Pink to match right ear background
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(32.dp)
                    ) {
                        Text(
                            text = "🎧 Right Ear",
                            fontSize = 10.sp,
                            color = Color.White
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Both ears option
                Button(
                    onClick = { onToggleFrequencyForEar(item, AudioEngine.Channel.BOTH) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B5CF6) // Purple to match both ears background
                    ),
                    modifier = Modifier.fillMaxWidth().height(28.dp)
                ) {
                    Text(
                        text = "🎧 Both Ears",
                        fontSize = 10.sp,
                        color = Color.White
                    )
                }
            } else {
                // Binaural frequencies always use both ears
                Column {
                    Text(
                        text = "Binaural Beat - Uses Both Ears",
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Button(
                        onClick = { onApplyBinauralBeat(
                            (item as FrequencyItem.Binaural).frequency.left,
                            item.frequency.right
                        ) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF10B981)
                        ),
                        modifier = Modifier.fillMaxWidth().height(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play Binaural Beat",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Play Binaural Beat",
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}