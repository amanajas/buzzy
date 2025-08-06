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

@Composable
fun StatusDisplay(
    leftFreq: Float,
    rightFreq: Float,
    isPlaying: Boolean,
    leftVolume: Float,
    rightVolume: Float,
    onVolumeChange: (AudioEngine.Channel, Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Frequency Display
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Left",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "${String.format("%.2f", leftFreq)} Hz",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPlaying) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Right",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "${String.format("%.2f", rightFreq)} Hz",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPlaying) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }
        
        if (isPlaying) {
            Text(
                text = "♫ Playing ♫",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
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
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "L",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontSize = 14.sp
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
                    fontSize = 12.sp
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
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "R",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontSize = 14.sp
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
                    fontSize = 12.sp
                )
            }
        }
    }
}