package com.buzzy.frequencyapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.buzzy.frequencyapp.audio.AudioEngine

@Composable
fun MainControls(
    isPlaying: Boolean,
    waveType: AudioEngine.WaveType,
    onPlayPause: () -> Unit,
    onWaveTypeChange: (AudioEngine.WaveType) -> Unit,
    onApplyFrequency: (Float, AudioEngine.Channel) -> Unit
) {
    var quickFrequency by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Play/Stop Button
        Button(
            onClick = onPlayPause,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isPlaying) Color(0xFFEF4444) else Color(0xFF10B981)
            )
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Stop" else "Play",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isPlaying) "Stop" else "Play",
                color = Color.White
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Wave Type Selector
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = "Wave Type",
                    style = MaterialTheme.typography.labelMedium
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    AudioEngine.WaveType.values().forEach { type ->
                        FilterChip(
                            selected = waveType == type,
                            onClick = { onWaveTypeChange(type) },
                            label = {
                                Text(
                                    text = when(type) {
                                        AudioEngine.WaveType.SINE -> "∿"
                                        AudioEngine.WaveType.SQUARE -> "⊓"
                                        AudioEngine.WaveType.SAWTOOTH -> "⋰"
                                        AudioEngine.WaveType.TRIANGLE -> "△"
                                    } + " " + type.name.lowercase().capitalize()
                                )
                            },
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Quick Frequency Input
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = "Quick Frequency (20-20000 Hz)",
                    style = MaterialTheme.typography.labelMedium
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = quickFrequency,
                        onValueChange = { quickFrequency = it },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = { Text("Enter Hz") },
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = {
                            quickFrequency.toFloatOrNull()?.let { freq ->
                                if (freq in 20f..20000f) {
                                    onApplyFrequency(freq, AudioEngine.Channel.BOTH)
                                    quickFrequency = ""
                                }
                            }
                        },
                        enabled = quickFrequency.isNotEmpty()
                    ) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}