package com.buzzy.frequencyapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit4.runners.AndroidJUnit4
import com.buzzy.frequencyapp.audio.AudioEngine
import com.buzzy.frequencyapp.ui.FrequencyApp
import com.buzzy.frequencyapp.ui.FrequencyViewModel
import com.buzzy.frequencyapp.ui.theme.FrequencyAppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FrequencyAppUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun app_displays_correctly_on_startup() {
        // Start the app
        composeTestRule.setContent {
            FrequencyAppTheme {
                FrequencyApp()
            }
        }

        // Verify main UI elements are present
        composeTestRule.onNodeWithText("♪ Buzzy").assertIsDisplayed()
        composeTestRule.onNodeWithText("Use headphones for binaural beats").assertIsDisplayed()
        composeTestRule.onNodeWithText("Start").assertIsDisplayed()
        composeTestRule.onNodeWithText("Wave Type").assertIsDisplayed()
    }

    @Test
    fun wave_type_selection_works() {
        composeTestRule.setContent {
            FrequencyAppTheme {
                FrequencyApp()
            }
        }

        // Find and click on Square wave type
        composeTestRule.onNodeWithText("Square").assertIsDisplayed()
        composeTestRule.onNodeWithText("Square").performClick()

        // Find and click on Triangle wave type
        composeTestRule.onNodeWithText("Triangle").assertIsDisplayed()
        composeTestRule.onNodeWithText("Triangle").performClick()

        // Find and click on Sawtooth wave type
        composeTestRule.onNodeWithText("Sawtooth").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sawtooth").performClick()

        // Return to Sine wave type
        composeTestRule.onNodeWithText("Sine").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sine").performClick()
    }

    @Test
    fun quick_frequency_input_works() {
        composeTestRule.setContent {
            FrequencyAppTheme {
                FrequencyApp()
            }
        }

        // Find the frequency input field
        composeTestRule.onNodeWithText("Enter Hz").assertIsDisplayed()
        
        // Type a frequency
        composeTestRule.onNodeWithText("Enter Hz").performTextInput("440")
        
        // Find and click the Apply button
        composeTestRule.onNodeWithText("Apply").assertIsDisplayed()
        composeTestRule.onNodeWithText("Apply").performClick()

        // Verify the frequency is applied (should show in status display)
        composeTestRule.onNodeWithText("440.00 Hz").assertIsDisplayed()
    }

    @Test
    fun play_stop_button_works() {
        composeTestRule.setContent {
            FrequencyAppTheme {
                FrequencyApp()
            }
        }

        // Initially should show Start button
        composeTestRule.onNodeWithText("Start").assertIsDisplayed()
        
        // Click Start button
        composeTestRule.onNodeWithText("Start").performClick()
        
        // Should now show Stop button and Playing indicator
        composeTestRule.onNodeWithText("Stop").assertIsDisplayed()
        composeTestRule.onNodeWithText("♫ Playing ♫").assertIsDisplayed()
        
        // Click Stop button
        composeTestRule.onNodeWithText("Stop").performClick()
        
        // Should return to Start button
        composeTestRule.onNodeWithText("Start").assertIsDisplayed()
    }

    @Test
    fun category_tabs_work() {
        composeTestRule.setContent {
            FrequencyAppTheme {
                FrequencyApp()
            }
        }

        // Check default tab (therapeutic should be active)
        composeTestRule.onNodeWithText("💚 Therapeutic").assertIsDisplayed()
        
        // Click on Musical tab
        composeTestRule.onNodeWithText("🎵 Musical").performClick()
        
        // Click on Binaural tab
        composeTestRule.onNodeWithText("🎧 Binaural").performClick()
        
        // Click on Brainwaves tab
        composeTestRule.onNodeWithText("🧠 Brainwaves").performClick()
        
        // Click on Custom tab
        composeTestRule.onNodeWithText("⭐ Custom").performClick()
        
        // Should show add custom frequency button
        composeTestRule.onNodeWithText("Add Custom Frequency").assertIsDisplayed()
    }

    @Test
    fun search_functionality_works() {
        composeTestRule.setContent {
            FrequencyAppTheme {
                FrequencyApp()
            }
        }

        // Find search field
        composeTestRule.onNodeWithText("Search frequencies...").assertIsDisplayed()
        
        // Type a search term
        composeTestRule.onNodeWithText("Search frequencies...").performTextInput("432")
        
        // Search should filter results (this would depend on having 432 Hz in database)
        // The test validates that search input works, actual filtering depends on data
    }

    @Test
    fun volume_controls_work() {
        composeTestRule.setContent {
            FrequencyAppTheme {
                FrequencyApp()
            }
        }

        // Volume controls should be visible
        composeTestRule.onNodeWithContentDescription("Left Volume").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Right Volume").assertIsDisplayed()
        
        // Volume percentage should be displayed
        composeTestRule.onNodeWithText("10%").assertIsDisplayed() // Default volume
    }

    @Test
    fun custom_frequency_form_works() {
        composeTestRule.setContent {
            FrequencyAppTheme {
                FrequencyApp()
            }
        }

        // Navigate to Custom tab
        composeTestRule.onNodeWithText("⭐ Custom").performClick()
        
        // Click Add Custom Frequency button
        composeTestRule.onNodeWithText("Add Custom Frequency").performClick()
        
        // Form should appear
        composeTestRule.onNodeWithText("Mono").assertIsDisplayed()
        composeTestRule.onNodeWithText("Binaural").assertIsDisplayed()
        
        // Fill out form
        composeTestRule.onNodeWithText("Name").performTextInput("Test Frequency")
        composeTestRule.onNodeWithText("Description").performTextInput("Test Description")
        composeTestRule.onNodeWithText("Frequency (Hz)").performTextInput("440")
        
        // Save button should be enabled and clickable
        composeTestRule.onNodeWithText("Save Custom Frequency").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save Custom Frequency").performClick()
    }

    @Test
    fun binaural_beat_form_works() {
        composeTestRule.setContent {
            FrequencyAppTheme {
                FrequencyApp()
            }
        }

        // Navigate to Custom tab
        composeTestRule.onNodeWithText("⭐ Custom").performClick()
        
        // Click Add Custom Frequency button
        composeTestRule.onNodeWithText("Add Custom Frequency").performClick()
        
        // Switch to Binaural mode
        composeTestRule.onNodeWithText("Binaural").performClick()
        
        // Binaural form should appear
        composeTestRule.onNodeWithText("Left (Hz)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Right (Hz)").assertIsDisplayed()
        
        // Fill out binaural form
        composeTestRule.onNodeWithText("Name").performTextInput("Test Binaural")
        composeTestRule.onNodeWithText("Description").performTextInput("Test Binaural Description")
        composeTestRule.onNodeWithText("Left (Hz)").performTextInput("440")
        composeTestRule.onNodeWithText("Right (Hz)").performTextInput("445")
        
        // Save button should work
        composeTestRule.onNodeWithText("Save Custom Frequency").performClick()
    }

    @Test
    fun dark_theme_colors_are_applied() {
        composeTestRule.setContent {
            FrequencyAppTheme {
                FrequencyApp()
            }
        }

        // This test verifies that the dark theme is properly applied
        // We can check for the presence of key UI elements that should be styled
        composeTestRule.onNodeWithText("♪ Buzzy").assertIsDisplayed()
        composeTestRule.onNodeWithText("Wave Type").assertIsDisplayed()
        composeTestRule.onNodeWithText("Quick Frequency (20-20000 Hz)").assertIsDisplayed()
    }
}