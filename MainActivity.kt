package com.nova.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nova.ai.domain.model.ThemeMode
import com.nova.ai.domain.model.UserPreferences
import com.nova.ai.ui.navigation.NovaAppRoot
import com.nova.ai.ui.theme.NovaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer = (application as NovaApplication).container

        setContent {
            val preferences by appContainer.preferencesRepository.userPreferencesFlow
                .collectAsStateWithLifecycle(initialValue = UserPreferences())

            NovaTheme(darkTheme = preferences.themeMode == ThemeMode.DARK) {
                NovaAppRoot(
                    container = appContainer,
                    preferences = preferences
                )
            }
        }
    }

    override fun onDestroy() {
        if (isFinishing) {
            (application as NovaApplication).container.voiceController.release()
        }
        super.onDestroy()
    }
}
