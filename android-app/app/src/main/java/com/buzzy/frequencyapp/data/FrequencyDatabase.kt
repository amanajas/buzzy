package com.buzzy.frequencyapp.data

import com.buzzy.frequencyapp.models.BinauralFrequency
import com.buzzy.frequencyapp.models.Frequency
import com.buzzy.frequencyapp.models.FrequencyItem

object FrequencyDatabase {
    val therapeutic = listOf(
        Frequency("7.83 Hz - Schumann", 7.83f, "Earth's frequency, grounding"),
        Frequency("10 Hz - Alpha", 10f, "Relaxed awareness"),
        Frequency("40 Hz - Gamma", 40f, "Enhanced focus"),
        Frequency("174 Hz - Pain Relief", 174f, "Natural anesthetic"),
        Frequency("285 Hz - Healing", 285f, "Cellular regeneration"),
        Frequency("396 Hz - Liberation", 396f, "Root chakra, fear release"),
        Frequency("417 Hz - Change", 417f, "Sacral chakra, transformation"),
        Frequency("528 Hz - Love", 528f, "Heart chakra, DNA repair"),
        Frequency("639 Hz - Relationships", 639f, "Heart chakra, connecting"),
        Frequency("741 Hz - Expression", 741f, "Throat chakra"),
        Frequency("852 Hz - Intuition", 852f, "Third eye chakra"),
        Frequency("963 Hz - Divine", 963f, "Crown chakra")
    )
    
    val musical = listOf(
        Frequency("C4 - 261.63 Hz", 261.63f, "Middle C"),
        Frequency("D4 - 293.66 Hz", 293.66f, "D note"),
        Frequency("E4 - 329.63 Hz", 329.63f, "E note"),
        Frequency("F4 - 349.23 Hz", 349.23f, "F note"),
        Frequency("G4 - 392.00 Hz", 392.00f, "G note"),
        Frequency("A4 - 440.00 Hz", 440.00f, "Concert pitch"),
        Frequency("B4 - 493.88 Hz", 493.88f, "B note"),
        Frequency("C5 - 523.25 Hz", 523.25f, "High C")
    )
    
    val binaural = listOf(
        BinauralFrequency("1 Hz - Deep Sleep", 100f, 101f, "Delta waves"),
        BinauralFrequency("4 Hz - Meditation", 200f, 204f, "Theta waves"),
        BinauralFrequency("8 Hz - Alpha Relax", 432f, 440f, "Alpha waves"),
        BinauralFrequency("15 Hz - Focus", 440f, 455f, "Beta waves"),
        BinauralFrequency("40 Hz - Cognitive", 400f, 440f, "Gamma waves")
    )
    
    val brainwaves = listOf(
        Frequency("2 Hz - Delta Sleep", 2f, "Deep sleep, healing"),
        Frequency("6 Hz - Theta Dreams", 6f, "Creativity, meditation"),
        Frequency("10 Hz - Alpha Calm", 10f, "Relaxed awareness"),
        Frequency("20 Hz - Beta Alert", 20f, "Normal consciousness"),
        Frequency("40 Hz - Gamma Focus", 40f, "High cognition")
    )
    
    fun getAllCategories(): Map<String, List<FrequencyItem>> {
        return mapOf(
            "therapeutic" to therapeutic.map { FrequencyItem.Mono(it) },
            "musical" to musical.map { FrequencyItem.Mono(it) },
            "binaural" to binaural.map { FrequencyItem.Binaural(it) },
            "brainwaves" to brainwaves.map { FrequencyItem.Mono(it) }
        )
    }
}