package com.buzzy.frequencyapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buzzy.frequencyapp.audio.AudioEngine

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun StatusDisplay(
    leftFreq: Float,
    rightFreq: Float,
    isPlaying: Boolean,
    leftVolume: Float,
    rightVolume: Float,
    selectedLeftFrequency: com.buzzy.frequencyapp.models.FrequencyItem?,
    selectedRightFrequency: com.buzzy.frequencyapp.models.FrequencyItem?,
    onVolumeChange: (AudioEngine.Channel, Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Selected Frequencies Display
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E2139)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = "Selected Frequencies",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Left Ear Selection
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "🎧 Left Ear",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        
                        if (selectedLeftFrequency != null) {
                            Text(
                                text = when (selectedLeftFrequency) {
                                    is com.buzzy.frequencyapp.models.FrequencyItem.Mono -> selectedLeftFrequency.frequency.name
                                    is com.buzzy.frequencyapp.models.FrequencyItem.Binaural -> "${selectedLeftFrequency.frequency.name} (L)"
                                },
                                fontSize = 10.sp,
                                color = Color(0xFF4F46E5),
                                maxLines = 2
                            )
                            Text(
                                text = "${String.format("%.1f", leftFreq)} Hz",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isPlaying) Color(0xFF4F46E5) else Color.White.copy(alpha = 0.8f)
                            )
                            
                        } else {
                            Text(
                                text = "No frequency selected",
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.5f)
                            )
                            Text(
                                text = "Silent",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.3f)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Right Ear Selection
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "🎧 Right Ear",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        
                        if (selectedRightFrequency != null) {
                            Text(
                                text = when (selectedRightFrequency) {
                                    is com.buzzy.frequencyapp.models.FrequencyItem.Mono -> selectedRightFrequency.frequency.name
                                    is com.buzzy.frequencyapp.models.FrequencyItem.Binaural -> "${selectedRightFrequency.frequency.name} (R)"
                                },
                                fontSize = 10.sp,
                                color = Color(0xFF4F46E5),
                                maxLines = 2
                            )
                            Text(
                                text = "${String.format("%.1f", rightFreq)} Hz",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isPlaying) Color(0xFF4F46E5) else Color.White.copy(alpha = 0.8f)
                            )
                            
                        } else {
                            Text(
                                text = "No frequency selected",
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.5f)
                            )
                            Text(
                                text = "Silent",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }
        }
        
        if (isPlaying) {
            Text(
                text = "♫ Playing ♫",
                fontSize = 14.sp,
                color = Color(0xFF4F46E5),
                modifier = Modifier.padding(top = 8.dp)
            )
        } else if (selectedLeftFrequency != null || selectedRightFrequency != null) {
            Text(
                text = "● Ready to Play ●",
                fontSize = 14.sp,
                color = Color(0xFF10B981),
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            Text(
                text = "⚪ Select frequencies to play",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Volume Controls
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Left Volume
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Left Volume",
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
                Text(
                    text = "L",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontSize = 14.sp,
                    color = Color.White
                )
                Slider(
                    value = leftVolume,
                    onValueChange = { onVolumeChange(AudioEngine.Channel.LEFT, it) },
                    valueRange = 0f..0.3f,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${(leftVolume * 100).toInt()}%",
                    modifier = Modifier.width(40.dp),
                    textAlign = TextAlign.End,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
            
            // Right Volume
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Right Volume",
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
                Text(
                    text = "R",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontSize = 14.sp,
                    color = Color.White
                )
                Slider(
                    value = rightVolume,
                    onValueChange = { onVolumeChange(AudioEngine.Channel.RIGHT, it) },
                    valueRange = 0f..0.3f,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${(rightVolume * 100).toInt()}%",
                    modifier = Modifier.width(40.dp),
                    textAlign = TextAlign.End,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }
}