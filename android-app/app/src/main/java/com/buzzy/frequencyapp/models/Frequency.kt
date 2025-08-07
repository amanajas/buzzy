package com.buzzy.frequencyapp.models

import kotlinx.serialization.Serializable

@Serializable
data class Frequency(
    val name: String,
    val freq: Float,
    val description: String
)

@Serializable
data class BinauralFrequency(
    val name: String,
    val left: Float,
    val right: Float,
    val description: String
)

@Serializable
sealed class FrequencyItem {
    @Serializable
    data class Mono(val frequency: Frequency) : FrequencyItem()
    @Serializable
    data class Binaural(val frequency: BinauralFrequency) : FrequencyItem()
}