package com.buzzy.frequencyapp.models

data class Frequency(
    val name: String,
    val freq: Float,
    val description: String
)

data class BinauralFrequency(
    val name: String,
    val left: Float,
    val right: Float,
    val description: String
)

sealed class FrequencyItem {
    data class Mono(val frequency: Frequency) : FrequencyItem()
    data class Binaural(val frequency: BinauralFrequency) : FrequencyItem()
}