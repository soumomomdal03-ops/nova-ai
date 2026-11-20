package com.nova.ai.di

import android.content.Context
import com.nova.ai.data.ai.LocalPhase1AiEngine
import com.nova.ai.data.preferences.UserPreferencesRepository
import com.nova.ai.data.voice.NovaVoiceController
import com.nova.ai.domain.repository.AiEngineRepository

class NovaAppContainer(context: Context) {
    val preferencesRepository: UserPreferencesRepository =
        UserPreferencesRepository(context.applicationContext)

    val aiEngineRepository: AiEngineRepository =
        LocalPhase1AiEngine()

    val voiceController: NovaVoiceController =
        NovaVoiceController(context.applicationContext)
}
