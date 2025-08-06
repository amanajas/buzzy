package com.buzzy.frequencyapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
    onApplyFrequency: (Float, AudioEngine.Channel) -> Unit,
    onApplyBinauralBeat: (Float, Float) -> Unit,
    onRemoveCustom: (Int) -> Unit
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
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = if (activeTab == "custom") 
                            "No custom frequencies yet" 
                        else 
                            "No frequencies found",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Gray
                    )
                }
            }
        } else {
            itemsIndexed(frequencies) { index, item ->
                FrequencyCard(
                    item = item,
                    isCustom = activeTab == "custom",
                    onApply = {
                        when (item) {
                            is FrequencyItem.Mono -> onApplyFrequency(item.frequency.freq, AudioEngine.Channel.BOTH)
                            is FrequencyItem.Binaural -> onApplyBinauralBeat(item.frequency.left, item.frequency.right)
                        }
                    },
                    onRemove = if (activeTab == "custom") {
                        { onRemoveCustom(index) }
                    } else null
                )
            }
        }
    }
}

@Composable
private fun FrequencyCard(
    item: FrequencyItem,
    isCustom: Boolean,
    onApply: () -> Unit,
    onRemove: (() -> Unit)?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onApply() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onApply,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = when (item) {
                        is FrequencyItem.Mono -> item.frequency.name
                        is FrequencyItem.Binaural -> item.frequency.name
                    },
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
                
                Text(
                    text = when (item) {
                        is FrequencyItem.Mono -> "${item.frequency.freq} Hz"
                        is FrequencyItem.Binaural -> "L: ${item.frequency.left} Hz | R: ${item.frequency.right} Hz"
                    },
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = when (item) {
                        is FrequencyItem.Mono -> item.frequency.description
                        is FrequencyItem.Binaural -> item.frequency.description
                    },
                    fontSize = 11.sp,
                    color = Color.Gray
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
    }
}