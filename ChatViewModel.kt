package com.nova.ai.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nova.ai.data.voice.NovaVoiceController
import com.nova.ai.domain.model.AppLanguage
import com.nova.ai.domain.model.ChatMessage
import com.nova.ai.domain.model.MessageSender
import com.nova.ai.domain.model.VoiceGender
import com.nova.ai.domain.repository.AiEngineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatUiState(
    val messages: List<ChatMessage> = listOf(
        ChatMessage(
            sender = MessageSender.NOVA,
            text = "Hello! I am NOVA AI. Give me a command in English, Hindi, or Hinglish — I can plan YouTube workflows, messaging drafts, research tasks, and device actions.",
            suggestedFollowUps = listOf(
                "Create a YouTube video about AI",
                "Plan WhatsApp quick replies",
                "Research top smartphones under 25000"
            )
        )
    ),
    val isThinking: Boolean = false
)

class ChatViewModel(
    private val aiEngine: AiEngineRepository,
    private val voiceController: NovaVoiceController
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun sendMessage(text: String, language: AppLanguage) {
        val trimmed = text.trim()
        if (trimmed.isEmpty() || _uiState.value.isThinking) return

        val userMessage = ChatMessage(sender = MessageSender.USER, text = trimmed)
        _uiState.update {
            it.copy(
                messages = it.messages + userMessage,
                isThinking = true
            )
        }

        viewModelScope.launch {
            val response = aiEngine.processCommand(
                input = trimmed,
                language = language,
                history = _uiState.value.messages
            )
            _uiState.update {
                it.copy(
                    messages = it.messages + response,
                    isThinking = false
                )
            }
        }
    }

    fun speakMessage(text: String, language: AppLanguage, gender: VoiceGender) {
        voiceController.speak(text, language, gender)
    }

    fun clearChat() {
        voiceController.stopSpeaking()
        _uiState.value = ChatUiState()
    }

    class Factory(
        private val aiEngine: AiEngineRepository,
        private val voiceController: NovaVoiceController
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChatViewModel(aiEngine, voiceController) as T
        }
    }
}
