package com.nova.ai.data.catalog

import com.nova.ai.domain.model.ModulePhase
import com.nova.ai.domain.model.NovaModuleInfo

object NovaModuleCatalog {
    val allModules: List<NovaModuleInfo> = listOf(
        NovaModuleInfo(
            id = "youtube_studio",
            title = "YouTube AI Studio",
            subtitle = "AI video workflow, thumbnails, metadata & OAuth upload",
            category = "Creator Studio",
            phase = ModulePhase.PHASE_5,
            capabilities = listOf(
                "Script, title, description, tags & Shorts workflow planning",
                "Thumbnail generation and licensed media assembly pipeline",
                "YouTube Data API v3 upload with Approval Center preview",
                "YouTube Analytics API channel performance dashboard"
            ),
            requiredApisAndPermissions = listOf(
                "Google OAuth 2.0 (youtube.upload, yt-analytics.readonly)",
                "Firebase Cloud Functions backend for Gemini & media pipeline"
            )
        ),
        NovaModuleInfo(
            id = "ai_video_creator",
            title = "AI Video Creator",
            subtitle = "Script, narration, scenes, subtitles & video assembly",
            category = "Creator Studio",
            phase = ModulePhase.PHASE_5,
            capabilities = listOf(
                "Multi-scene script and visual storyboard generation",
                "Natural narration voiceover and SRT subtitle export",
                "Licensed visual & audio asset sourcing and assembly"
            ),
            requiredApisAndPermissions = listOf(
                "Server-side Gemini API + TTS/Video rendering pipeline",
                "Firebase Storage for temporary render artifacts"
            )
        ),
        NovaModuleInfo(
            id = "image_creator",
            title = "AI Image & Content Creator",
            subtitle = "Thumbnails, captions, blogs, translations & documents",
            category = "Creator Studio",
            phase = ModulePhase.PHASE_5,
            capabilities = listOf(
                "AI thumbnail and graphic generation with export",
                "Hindi, English & Hinglish blog, caption, and document drafting",
                "Rewriting, summarization, and multi-language translation"
            ),
            requiredApisAndPermissions = listOf(
                "Server-side Gemini / Imagen API via Firebase Cloud Functions",
                "Scoped Storage export permission"
            )
        ),
        NovaModuleInfo(
            id = "call_manager",
            title = "Smart Call Manager",
            subtitle = "Android CallScreeningService & unavailable call policies",
            category = "Communications",
            phase = ModulePhase.PHASE_4,
            capabilities = listOf(
                "Incoming call screening using Android CallScreeningService",
                "Configurable unavailable status and custom caller messaging",
                "Caller identification from permitted phone info & call dashboard"
            ),
            requiredApisAndPermissions = listOf(
                "Android ROLE_CALL_SCREENING via RoleManager",
                "READ_CALL_LOG / READ_PHONE_STATE with explicit user consent"
            )
        ),
        NovaModuleInfo(
            id = "whatsapp_manager",
            title = "WhatsApp AI Manager",
            subtitle = "Notification quick replies & Official Business Platform API",
            category = "Communications",
            phase = ModulePhase.PHASE_4,
            capabilities = listOf(
                "Read permitted incoming WhatsApp notifications",
                "Generate Hindi, English & Hinglish reply drafts",
                "User-approved quick replies and message templates",
                "Official WhatsApp Business Platform API for eligible accounts"
            ),
            requiredApisAndPermissions = listOf(
                "Android NotificationListenerService (user-enabled)",
                "Meta WhatsApp Business Cloud API (for business accounts)"
            )
        ),
        NovaModuleInfo(
            id = "instagram_manager",
            title = "Instagram AI Manager",
            subtitle = "Meta Graph API DMs, comments, captions & post planning",
            category = "Communications",
            phase = ModulePhase.PHASE_4,
            capabilities = listOf(
                "Eligible Professional Account connection via Meta OAuth",
                "AI-drafted DM & comment replies with approval gate",
                "Hashtag suggestions, captions, and content calendar"
            ),
            requiredApisAndPermissions = listOf(
                "Meta Graph API (instagram_manage_messages, instagram_manage_comments)",
                "Instagram Professional Account linked to Facebook Page"
            )
        ),
        NovaModuleInfo(
            id = "messenger_manager",
            title = "Facebook Messenger AI",
            subtitle = "Connected Page inbox, AI drafting & approval queue",
            category = "Communications",
            phase = ModulePhase.PHASE_4,
            capabilities = listOf(
                "Connected Facebook Page inbox management",
                "AI response generation with mandatory approval toggle",
                "Conversation organization and reply history"
            ),
            requiredApisAndPermissions = listOf(
                "Meta Messenger Platform API (pages_messaging)",
                "Verified Facebook Page admin authorization"
            )
        ),
        NovaModuleInfo(
            id = "internet_research",
            title = "Internet Research Agent",
            subtitle = "Verified web search, product comparison & cited reports",
            category = "Intelligence",
            phase = ModulePhase.PHASE_6,
            capabilities = listOf(
                "Live web search with verified clickable source links",
                "Multi-page reading, product comparisons & executive briefs",
                "Searchable research history saved to Firestore"
            ),
            requiredApisAndPermissions = listOf(
                "Server-side Search Grounding API via Cloud Functions",
                "Cloud Firestore per-user history storage"
            )
        ),
        NovaModuleInfo(
            id = "automation_center",
            title = "Automation & Android Control",
            subtitle = "Multi-step workflows, app launch, reminders & alarms",
            category = "Autonomous Engine",
            phase = ModulePhase.PHASE_3,
            capabilities = listOf(
                "Launch installed apps, open URLs, and set alarms/reminders",
                "Sequential multi-step tool execution with retry & cancellation",
                "WorkManager background tasks with progress notifications"
            ),
            requiredApisAndPermissions = listOf(
                "Android Alarm, Notification & explicit runtime permissions",
                "WorkManager foreground/background task engine"
            )
        ),
        NovaModuleInfo(
            id = "approval_center",
            title = "Approval Center",
            subtitle = "Approve, edit, or reject every sensitive AI action",
            category = "Autonomous Engine",
            phase = ModulePhase.PHASE_3,
            capabilities = listOf(
                "Shows exact action, connected account, payload & expected result",
                "One-tap Approve, Edit, or Reject controls",
                "Full audit log of authorized and declined tasks"
            ),
            requiredApisAndPermissions = listOf(
                "Cloud Firestore Approval Queue + Security Rules",
                "Firebase App Check & Auth token verification"
            )
        ),
        NovaModuleInfo(
            id = "task_history",
            title = "Task Execution History",
            subtitle = "Chronological logs, statuses, retries & audit trail",
            category = "Autonomous Engine",
            phase = ModulePhase.PHASE_3,
            capabilities = listOf(
                "Track every planned, approved, running, or failed task",
                "Inspect step-by-step error diagnostics and retry options",
                "Export or purge execution logs anytime"
            ),
            requiredApisAndPermissions = listOf(
                "Cloud Firestore per-user audit collection"
            )
        ),
        NovaModuleInfo(
            id = "connected_accounts",
            title = "Connected Accounts & Cloud Sync",
            subtitle = "Firebase Auth, Google OAuth, YouTube & Meta links",
            category = "System & Security",
            phase = ModulePhase.PHASE_2,
            capabilities = listOf(
                "Firebase Authentication & encrypted token management",
                "Connect or revoke Google, YouTube, and Meta accounts anytime",
                "Per-user Firestore Security Rules & Firebase App Check"
            ),
            requiredApisAndPermissions = listOf(
                "Firebase Project (`google-services.json`)",
                "OAuth 2.0 Web/Android Client IDs"
            )
        )
    )

    fun findById(id: String): NovaModuleInfo? = allModules.firstOrNull { it.id == id }
}
