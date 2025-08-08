package com.buzzy.frequencyapp.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.buzzy.frequencyapp.models.FrequencyItem
import com.buzzy.frequencyapp.R

@Composable
fun DeleteConfirmationDialog(
    frequencyItem: FrequencyItem,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.delete_custom_frequency),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        },
        text = {
            val frequencyName = when (frequencyItem) {
                is FrequencyItem.Mono -> frequencyItem.frequency.name
                is FrequencyItem.Binaural -> frequencyItem.frequency.name
            }
            Text(
                text = stringResource(R.string.delete_confirmation_message, frequencyName),
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEF4444) // Red for delete
                )
            ) {
                Text(
                    text = stringResource(R.string.delete),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White.copy(alpha = 0.8f)
                )
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
        containerColor = Color(0xFF1E2139),
        titleContentColor = Color.White,
        textContentColor = Color.White
    )
}