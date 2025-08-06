package com.buzzy.frequencyapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun CustomFrequencyForm(
    showAddCustom: Boolean,
    onToggleForm: () -> Unit,
    onSaveCustom: (String, String, Float?, Float?) -> Unit
) {
    var freqType by remember { mutableStateOf("mono") }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var leftFreq by remember { mutableStateOf("") }
    var rightFreq by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            onClick = onToggleForm,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add custom"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (showAddCustom) "Cancel" else "Add Custom Frequency")
        }
        
        AnimatedVisibility(visible = showAddCustom) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Frequency Type Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        FilterChip(
                            selected = freqType == "mono",
                            onClick = { freqType = "mono" },
                            label = { Text("Mono") }
                        )
                        FilterChip(
                            selected = freqType == "binaural",
                            onClick = { freqType = "binaural" },
                            label = { Text("Binaural") }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Name Input
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Description Input
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Frequency Inputs
                    if (freqType == "mono") {
                        OutlinedTextField(
                            value = frequency,
                            onValueChange = { frequency = it },
                            label = { Text("Frequency (Hz)") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = leftFreq,
                                onValueChange = { leftFreq = it },
                                label = { Text("Left (Hz)") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = rightFreq,
                                onValueChange = { rightFreq = it },
                                label = { Text("Right (Hz)") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Save Button
                    Button(
                        onClick = {
                            if (name.isNotEmpty()) {
                                if (freqType == "mono") {
                                    frequency.toFloatOrNull()?.let { freq ->
                                        if (freq in 20f..20000f) {
                                            onSaveCustom(name, description, freq, null)
                                            // Reset form
                                            name = ""
                                            description = ""
                                            frequency = ""
                                        }
                                    }
                                } else {
                                    val left = leftFreq.toFloatOrNull()
                                    val right = rightFreq.toFloatOrNull()
                                    if (left != null && right != null && 
                                        left in 20f..20000f && right in 20f..20000f) {
                                        onSaveCustom(name, description, left, right)
                                        // Reset form
                                        name = ""
                                        description = ""
                                        leftFreq = ""
                                        rightFreq = ""
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = name.isNotEmpty()
                    ) {
                        Text("Save Custom Frequency")
                    }
                }
            }
        }
    }
}