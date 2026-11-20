package com.nova.ai.domain.repository

import com.nova.ai.domain.model.AppLanguage
import com.nova.ai.domain.model.ChatMessage

interface AiEngineRepository {
    suspend fun processCommand(
        input: String,
        language: AppLanguage,
        history: List<ChatMessage>
    ): ChatMessage
}
