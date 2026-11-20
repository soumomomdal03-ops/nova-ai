package com.nova.ai.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nova.ai.domain.model.AppLanguage
import com.nova.ai.domain.model.ApprovalMode
import com.nova.ai.domain.model.ThemeMode
import com.nova.ai.domain.model.UserPreferences
import com.nova.ai.domain.model.VoiceGender
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.novaDataStore: DataStore<Preferences> by preferencesDataStore(name = "nova_ai_prefs")

class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
        val LOGGED_IN = booleanPreferencesKey("logged_in")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val LANGUAGE = stringPreferencesKey("language")
        val VOICE_GENDER = stringPreferencesKey("voice_gender")
        val CONTINUOUS_VOICE = booleanPreferencesKey("continuous_voice")
        val APPROVAL_MODE = stringPreferencesKey("approval_mode")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.novaDataStore.data.map { prefs ->
        UserPreferences(
            isOnboardingCompleted = prefs[Keys.ONBOARDING_DONE] ?: false,
            isLoggedIn = prefs[Keys.LOGGED_IN] ?: false,
            userName = prefs[Keys.USER_NAME] ?: "Operator",
            userEmail = prefs[Keys.USER_EMAIL] ?: "",
            themeMode = runCatching {
                ThemeMode.valueOf(prefs[Keys.THEME_MODE] ?: ThemeMode.DARK.name)
            }.getOrDefault(ThemeMode.DARK),
            language = runCatching {
                AppLanguage.valueOf(prefs[Keys.LANGUAGE] ?: AppLanguage.HINGLISH.name)
            }.getOrDefault(AppLanguage.HINGLISH),
            voiceGender = runCatching {
                VoiceGender.valueOf(prefs[Keys.VOICE_GENDER] ?: VoiceGender.FEMALE.name)
            }.getOrDefault(VoiceGender.FEMALE),
            continuousVoiceMode = prefs[Keys.CONTINUOUS_VOICE] ?: false,
            approvalMode = runCatching {
                ApprovalMode.valueOf(prefs[Keys.APPROVAL_MODE] ?: ApprovalMode.ALWAYS_ASK.name)
            }.getOrDefault(ApprovalMode.ALWAYS_ASK)
        )
    }

    suspend fun completeOnboarding() {
        context.novaDataStore.edit { it[Keys.ONBOARDING_DONE] = true }
    }

    suspend fun saveLocalSession(name: String, email: String) {
        context.novaDataStore.edit {
            it[Keys.LOGGED_IN] = true
            it[Keys.USER_NAME] = name.ifBlank { "Operator" }
            it[Keys.USER_EMAIL] = email.trim()
        }
    }

    suspend fun signOut() {
        context.novaDataStore.edit {
            it[Keys.LOGGED_IN] = false
        }
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.novaDataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.novaDataStore.edit { it[Keys.LANGUAGE] = language.name }
    }

    suspend fun setVoiceGender(gender: VoiceGender) {
        context.novaDataStore.edit { it[Keys.VOICE_GENDER] = gender.name }
    }

    suspend fun setContinuousVoiceMode(enabled: Boolean) {
        context.novaDataStore.edit { it[Keys.CONTINUOUS_VOICE] = enabled }
    }

    suspend fun setApprovalMode(mode: ApprovalMode) {
        context.novaDataStore.edit { it[Keys.APPROVAL_MODE] = mode.name }
    }

    suspend fun clearPersonalMemory() {
        context.novaDataStore.edit {
            it[Keys.CONTINUOUS_VOICE] = false
            it[Keys.APPROVAL_MODE] = ApprovalMode.ALWAYS_ASK.name
            it[Keys.LANGUAGE] = AppLanguage.HINGLISH.name
            it[Keys.VOICE_GENDER] = VoiceGender.FEMALE.name
        }
    }
}
