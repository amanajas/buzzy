package com.buzzy.frequencyapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.buzzy.frequencyapp.audio.AudioEngine
import com.buzzy.frequencyapp.models.FrequencyItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Locale

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "frequency_app_prefs")

class DataManager(private val context: Context) {
    
    companion object {
        private val CUSTOM_FREQUENCIES_KEY = stringPreferencesKey("custom_frequencies")
        private val SELECTED_LEFT_FREQUENCY_KEY = stringPreferencesKey("selected_left_frequency")
        private val SELECTED_RIGHT_FREQUENCY_KEY = stringPreferencesKey("selected_right_frequency")
        private val LEFT_VOLUME_KEY = floatPreferencesKey("left_volume")
        private val RIGHT_VOLUME_KEY = floatPreferencesKey("right_volume")
        private val WAVE_TYPE_KEY = stringPreferencesKey("wave_type")
        private val ACTIVE_TAB_KEY = stringPreferencesKey("active_tab")
        private val LANGUAGE_KEY = stringPreferencesKey("language")
        
        private val json = Json { 
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }
    
    // Custom frequencies
    suspend fun saveCustomFrequencies(frequencies: List<FrequencyItem>) {
        context.dataStore.edit { preferences ->
            val jsonString = json.encodeToString(frequencies)
            preferences[CUSTOM_FREQUENCIES_KEY] = jsonString
        }
    }
    
    fun getCustomFrequencies(): Flow<List<FrequencyItem>> {
        return context.dataStore.data.map { preferences ->
            val jsonString = preferences[CUSTOM_FREQUENCIES_KEY] ?: return@map emptyList()
            try {
                json.decodeFromString<List<FrequencyItem>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
    
    // Selected frequencies
    suspend fun saveSelectedFrequencies(
        leftFrequency: FrequencyItem?,
        rightFrequency: FrequencyItem?
    ) {
        context.dataStore.edit { preferences ->
            if (leftFrequency != null) {
                preferences[SELECTED_LEFT_FREQUENCY_KEY] = json.encodeToString(leftFrequency)
            } else {
                preferences.remove(SELECTED_LEFT_FREQUENCY_KEY)
            }
            
            if (rightFrequency != null) {
                preferences[SELECTED_RIGHT_FREQUENCY_KEY] = json.encodeToString(rightFrequency)
            } else {
                preferences.remove(SELECTED_RIGHT_FREQUENCY_KEY)
            }
        }
    }
    
    fun getSelectedLeftFrequency(): Flow<FrequencyItem?> {
        return context.dataStore.data.map { preferences ->
            val jsonString = preferences[SELECTED_LEFT_FREQUENCY_KEY] ?: return@map null
            try {
                json.decodeFromString<FrequencyItem>(jsonString)
            } catch (e: Exception) {
                null
            }
        }
    }
    
    fun getSelectedRightFrequency(): Flow<FrequencyItem?> {
        return context.dataStore.data.map { preferences ->
            val jsonString = preferences[SELECTED_RIGHT_FREQUENCY_KEY] ?: return@map null
            try {
                json.decodeFromString<FrequencyItem>(jsonString)
            } catch (e: Exception) {
                null
            }
        }
    }
    
    // Volume preferences
    suspend fun saveVolume(leftVolume: Float, rightVolume: Float) {
        context.dataStore.edit { preferences ->
            preferences[LEFT_VOLUME_KEY] = leftVolume
            preferences[RIGHT_VOLUME_KEY] = rightVolume
        }
    }
    
    fun getLeftVolume(): Flow<Float> {
        return context.dataStore.data.map { preferences ->
            preferences[LEFT_VOLUME_KEY] ?: 0.1f
        }
    }
    
    fun getRightVolume(): Flow<Float> {
        return context.dataStore.data.map { preferences ->
            preferences[RIGHT_VOLUME_KEY] ?: 0.1f
        }
    }
    
    // Wave type preference
    suspend fun saveWaveType(waveType: AudioEngine.WaveType) {
        context.dataStore.edit { preferences ->
            preferences[WAVE_TYPE_KEY] = waveType.name
        }
    }
    
    fun getWaveType(): Flow<AudioEngine.WaveType> {
        return context.dataStore.data.map { preferences ->
            val waveTypeName = preferences[WAVE_TYPE_KEY] ?: AudioEngine.WaveType.SINE.name
            try {
                AudioEngine.WaveType.valueOf(waveTypeName)
            } catch (e: Exception) {
                AudioEngine.WaveType.SINE
            }
        }
    }
    
    // Active tab preference
    suspend fun saveActiveTab(activeTab: String) {
        context.dataStore.edit { preferences ->
            preferences[ACTIVE_TAB_KEY] = activeTab
        }
    }
    
    fun getActiveTab(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[ACTIVE_TAB_KEY] ?: "therapeutic"
        }
    }
    
    // Language preference
    suspend fun saveLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }
    
    fun getLanguage(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[LANGUAGE_KEY] ?: getDeviceLanguage()
        }
    }
    
    private fun getDeviceLanguage(): String {
        val deviceLocale = Locale.getDefault()
        val language = deviceLocale.language
        val country = deviceLocale.country
        
        // Map device locale to supported languages
        return when {
            language == "en" -> "en"
            language == "es" -> "es"
            language == "fr" -> "fr"
            language == "de" -> "de"
            language == "pt" && country == "BR" -> "pt-rBR"
            language == "pt" -> "pt"
            language == "ja" -> "ja"
            language == "zh" -> "zh-rCN"
            language == "ko" -> "ko"
            language == "ar" -> "ar"
            language == "hi" -> "hi"
            else -> "en" // Default to English if no match
        }
    }
}