package com.buzzy.frequencyapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoryTabs(
    activeTab: String,
    categories: List<String>,
    onTabChange: (String) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = categories.indexOf(activeTab),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        edgePadding = 16.dp,
        containerColor = Color(0xFF1E2139),
        contentColor = Color.White
    ) {
        categories.forEach { category ->
            Tab(
                selected = activeTab == category,
                onClick = { onTabChange(category) },
                text = {
                    Text(
                        text = when(category) {
                            "custom" -> "⭐ Custom"
                            "therapeutic" -> "💚 Therapeutic"
                            "musical" -> "🎵 Musical"
                            "binaural" -> "🎧 Binaural"
                            "brainwaves" -> "🧠 Brainwaves"
                            else -> category.replaceFirstChar { it.uppercase() }
                        }
                    )
                }
            )
        }
    }
}