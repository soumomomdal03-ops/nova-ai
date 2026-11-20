package com.nova.ai.domain.model

import java.util.UUID

enum class ThemeMode {
    DARK,
    LIGHT
}

enum class AppLanguage(val label: String, val speechLocale: String) {
    ENGLISH("English", "en-IN"),
    HINDI("Hindi", "hi-IN"),
    HINGLISH("Hinglish", "en-IN")
}

enum class VoiceGender(val label: String, val pitch: Float, val speechRate: Float) {
    FEMALE("Female (Nova Aria)", 1.08f, 1.0f),
    MALE("Male (Nova Atlas)", 0.84f, 0.98f)
}

enum class ApprovalMode(val title: String, val subtitle: String) {
    ALWAYS_ASK("Always Ask", "Require explicit approval before every external action"),
    SENSITIVE_ONLY("Sensitive Actions Only", "Approve publishing, messaging, calls, and account changes"),
    AUTO_LOW_RISK("Auto Low-Risk", "Run approved local read-only tasks automatically")
}

data class UserPreferences(
    val isOnboardingCompleted: Boolean = false,
    val isLoggedIn: Boolean = false,
    val userName: String = "Operator",
    val userEmail: String = "",
    val themeMode: ThemeMode = ThemeMode.DARK,
    val language: AppLanguage = AppLanguage.HINGLISH,
    val voiceGender: VoiceGender = VoiceGender.FEMALE,
    val continuousVoiceMode: Boolean = false,
    val approvalMode: ApprovalMode = ApprovalMode.ALWAYS_ASK
)

enum class MessageSender {
    USER,
    NOVA
}

enum class StepStatus {
    READY,
    REQUIRES_APPROVAL,
    SCHEDULED_NEXT_PHASE
}

data class TaskStepPreview(
    val index: Int,
    val title: String,
    val detail: String,
    val status: StepStatus
)

data class TaskPlanPreview(
    val title: String,
    val category: String,
    val requiresApproval: Boolean,
    val targetPhaseNote: String,
    val steps: List<TaskStepPreview>
)

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestampMillis: Long = System.currentTimeMillis(),
    val taskPlan: TaskPlanPreview? = null,
    val suggestedFollowUps: List<String> = emptyList()
)

enum class VoiceSessionState {
    IDLE,
    LISTENING,
    PROCESSING,
    SPEAKING,
    ERROR
}

enum class ModulePhase(val badge: String) {
    PHASE_1("Active"),
    PHASE_2("Phase 2"),
    PHASE_3("Phase 3"),
    PHASE_4("Phase 4"),
    PHASE_5("Phase 5"),
    PHASE_6("Phase 6")
}

data class NovaModuleInfo(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: String,
    val phase: ModulePhase,
    val capabilities: List<String>,
    val requiredApisAndPermissions: List<String>
)
