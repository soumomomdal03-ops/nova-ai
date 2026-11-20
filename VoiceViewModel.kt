package com.nova.ai.ui.screens.voice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nova.ai.data.voice.NovaVoiceController
import com.nova.ai.domain.model.AppLanguage
import com.nova.ai.domain.model.UserPreferences
import com.nova.ai.domain.model.VoiceSessionState
import com.nova.ai.domain.repository.AiEngineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class VoiceUiState(
    val state: VoiceSessionState = VoiceSessionState.IDLE,
    val transcript: String = "",
    val aiReply: String = "Tap the glowing microphone and speak in English, Hindi, or Hinglish.",
    val audioLevel: Float = 0f,
    val errorText: String? = null
)

class VoiceViewModel(
    private val voiceController: NovaVoiceController,
    private val aiEngine: AiEngineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VoiceUiState())
    val uiState: StateFlow<VoiceUiState> = _uiState.asStateFlow()

    fun startListening(preferences: UserPreferences) {
        _uiState.update {
            it.copy(
                state = VoiceSessionState.LISTENING,
                transcript = "",
                errorText = null,
                audioLevel = 0.1f
            )
        }

        voiceController.startListening(
            language = preferences.language,
            onReady = {
                _uiState.update { it.copy(state = VoiceSessionState.LISTENING) }
            },
            onRmsChanged = { level ->
                _uiState.update { it.copy(audioLevel = level) }
            },
            onPartialResult = { partial ->
                _uiState.update { it.copy(transcript = partial) }
            },
            onFinalResult = { spoken ->
                _uiState.update {
                    it.copy(
                        transcript = spoken,
                        state = VoiceSessionState.PROCESSING,
                        audioLevel = 0.25f
                    )
                }
                handleSpokenCommand(spoken, preferences)
            },
            onError = { error ->
                _uiState.update {
                    it.copy(
                        state = VoiceSessionState.ERROR,
                        errorText = error,
                        audioLevel = 0f
                    )
                }
            }
        )
    }

    private fun handleSpokenCommand(spoken: String, preferences: UserPreferences) {
        viewModelScope.launch {
            val response = aiEngine.processCommand(
                input = spoken,
                language = preferences.language,
                history = emptyList()
            )
            _uiState.update {
                it.copy(
                    aiReply = response.text,
                    state = VoiceSessionState.SPEAKING,
                    audioLevel = 0.45f
                )
            }
            voiceController.speak(
                text = response.text,
                language = preferences.language,
                gender = preferences.voiceGender,
                onComplete = {
                    if (preferences.continuousVoiceMode) {
                        startListening(preferences)
                    } else {
                        _uiState.update {
                            it.copy(
                                state = VoiceSessionState.IDLE,
                                audioLevel = 0f
                            )
                        }
                    }
                }
            )
        }
    }

    fun stopAll() {
        voiceController.stopListening()
        voiceController.stopSpeaking()
        _uiState.update {
            it.copy(
                state = VoiceSessionState.IDLE,
                audioLevel = 0f
            )
        }
    }

    class Factory(
        private val voiceController: NovaVoiceController,
        private val aiEngine: AiEngineRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VoiceViewModel(voiceController, aiEngine) as T
        }
    }
}
