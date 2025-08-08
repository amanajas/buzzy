package com.buzzy.frequencyapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.res.stringResource
import com.buzzy.frequencyapp.audio.AudioEngine
import com.buzzy.frequencyapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainControls(
    isPlaying: Boolean,
    waveType: AudioEngine.WaveType,
    hasSelectedFrequencies: Boolean,
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
            enabled = hasSelectedFrequencies || isPlaying,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isPlaying) Color(0xFFEF4444) else Color(0xFF10B981),
                disabledContainerColor = Color(0xFF6B7280),
                disabledContentColor = Color(0xFF9CA3AF)
            )
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Stop" else "Play",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = when {
                    isPlaying -> stringResource(R.string.stop)
                    hasSelectedFrequencies -> stringResource(R.string.play)
                    else -> stringResource(R.string.no_frequency_selected)
                },
                color = if (hasSelectedFrequencies || isPlaying) Color.White else Color(0xFF9CA3AF)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Wave Type Selector
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E2139)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.wave_type),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(120.dp)
                ) {
                    items(AudioEngine.WaveType.values().toList()) { type ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (waveType == type) Color(0xFF4F46E5) else Color(0xFF374151)
                            ),
                            onClick = { onWaveTypeChange(type) }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = when(type) {
                                        AudioEngine.WaveType.SINE -> "∿"
                                        AudioEngine.WaveType.SQUARE -> "⊓"
                                        AudioEngine.WaveType.SAWTOOTH -> "⋰"
                                        AudioEngine.WaveType.TRIANGLE -> "△"
                                    },
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = when(type) {
                                        AudioEngine.WaveType.SINE -> stringResource(R.string.wave_sine)
                                        AudioEngine.WaveType.SQUARE -> stringResource(R.string.wave_square)
                                        AudioEngine.WaveType.SAWTOOTH -> stringResource(R.string.wave_sawtooth)
                                        AudioEngine.WaveType.TRIANGLE -> stringResource(R.string.wave_triangle)
                                    },
                                    color = Color.White.copy(alpha = 0.8f),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Quick Frequency Input
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E2139)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.quick_frequency_range),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
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
                        placeholder = { Text(stringResource(R.string.enter_hz), color = Color.White.copy(alpha = 0.6f)) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4F46E5),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
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
                        enabled = quickFrequency.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4F46E5),
                            contentColor = Color.White
                        )
                    ) {
                        Text(stringResource(R.string.apply))
                    }
                }
            }
        }
    }
}