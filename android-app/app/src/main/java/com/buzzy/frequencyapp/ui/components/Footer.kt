package com.buzzy.frequencyapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Footer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        
        Text(
            text = "💡 Tip: Start with low volume and adjust gradually",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = "⚠️ Not for medical use. Consult professionals for health concerns.",
            fontSize = 11.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}