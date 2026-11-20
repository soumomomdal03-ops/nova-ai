package com.nova.ai.data.ai

import com.nova.ai.domain.model.AppLanguage
import com.nova.ai.domain.model.ChatMessage
import com.nova.ai.domain.model.MessageSender
import com.nova.ai.domain.model.StepStatus
import com.nova.ai.domain.model.TaskPlanPreview
import com.nova.ai.domain.model.TaskStepPreview
import com.nova.ai.domain.repository.AiEngineRepository
import kotlinx.coroutines.delay

class LocalPhase1AiEngine : AiEngineRepository {

    override suspend fun processCommand(
        input: String,
        language: AppLanguage,
        history: List<ChatMessage>
    ): ChatMessage {
        delay(550)
        val normalized = input.trim().lowercase()
        val isHindiScript = input.any { it in '\u0900'..'\u097F' }
        val isHinglish = language == AppLanguage.HINGLISH ||
            listOf("banao", "karo", "kholo", "batao", "mera", "meri", "video", "kaise", "hai", "kya")
                .any { normalized.contains(it) }

        return when {
            normalized.contains("youtube") || normalized.contains("video") || normalized.contains("short") -> {
                buildYouTubePlanResponse(input, isHindiScript, isHinglish)
            }
            normalized.contains("whatsapp") || normalized.contains("instagram") || normalized.contains("messenger") -> {
                buildMessagingPlanResponse(input, isHindiScript, isHinglish)
            }
            normalized.contains("call") || normalized.contains("phone") || normalized.contains("unavailable") -> {
                buildCallManagerResponse(isHindiScript, isHinglish)
            }
            normalized.contains("open ") || normalized.contains("kholo") || normalized.contains("alarm") || normalized.contains("reminder") -> {
                buildDeviceControlResponse(input, isHindiScript, isHinglish)
            }
            normalized.contains("research") || normalized.contains("search") || normalized.contains("compare") -> {
                buildResearchPlanResponse(input, isHindiScript, isHinglish)
            }
            else -> {
                buildGeneralAssistantResponse(input, isHindiScript, isHinglish, history.size)
            }
        }
    }

    private fun buildYouTubePlanResponse(
        input: String,
        isHindi: Boolean,
        isHinglish: Boolean
    ): ChatMessage {
        val text = when {
            isHindi -> "मैंने आपके YouTube कमांड के लिए एक मल्टी-स्टेप प्लान तैयार किया है। पब्लिश करने से पहले हर एक्शन के लिए Approval Center में आपकी अनुमति ली जाएगी।"
            isHinglish -> "Maine aapke YouTube workflow ka complete execution plan bana diya hai. Publish karne se pehle har sensitive step ke liye Approval Center me aapki permission li jayegi."
            else -> "I have structured a multi-step YouTube workflow for your request. Nothing will be uploaded or published without your final review in the Approval Center."
        }

        val plan = TaskPlanPreview(
            title = "YouTube AI Studio Workflow",
            category = "Creator Studio",
            requiresApproval = true,
            targetPhaseNote = "Live Gemini script generation connects in Phase 2; YouTube Data API v3 upload connects in Phase 5.",
            steps = listOf(
                TaskStepPreview(1, "Topic & Script Drafting", "Research angle, hook, scenes, and narration for: "$input"", StepStatus.READY),
                TaskStepPreview(2, "Voiceover & Thumbnail Prep", "Generate narration audio, captions, and 16:9 thumbnail concept", StepStatus.SCHEDULED_NEXT_PHASE),
                TaskStepPreview(3, "Metadata Optimization", "Prepare SEO title, description, tags, and visibility schedule", StepStatus.READY),
                TaskStepPreview(4, "Approval & Channel Upload", "Preview final package and upload via authorized YouTube OAuth", StepStatus.REQUIRES_APPROVAL)
            )
        )

        return ChatMessage(
            sender = MessageSender.NOVA,
            text = text,
            taskPlan = plan,
            suggestedFollowUps = listOf("Plan 3 YouTube Shorts", "Draft video script outline", "Open Voice Assistant")
        )
    }

    private fun buildMessagingPlanResponse(
        input: String,
        isHindi: Boolean,
        isHinglish: Boolean
    ): ChatMessage {
        val text = when {
            isHindi -> "मैसेजिंग वर्कफ़्लो प्लान तैयार है। NOVA केवल अनुमति प्राप्त नोटिफिकेशन और आधिकारिक APIs का उपयोग करेगा और आपकी स्वीकृति के बिना कोई संदेश नहीं भेजेगा।"
            isHinglish -> "Messaging workflow plan ready hai. NOVA sirf permitted notifications aur official APIs use karega, aur aapke approval ke bina koi reply send nahi hoga."
            else -> "Messaging assistance plan created. NOVA uses only permitted notification actions and official Meta APIs, always requiring approval before sending."
        }

        val plan = TaskPlanPreview(
            title = "Approved Messaging Workflow",
            category = "Communications",
            requiresApproval = true,
            targetPhaseNote = "Notification Listener & Official WhatsApp/Meta API connectors activate in Phase 4.",
            steps = listOf(
                TaskStepPreview(1, "Detect Permitted Message", "Read incoming message from authorized notification or official inbox", StepStatus.SCHEDULED_NEXT_PHASE),
                TaskStepPreview(2, "Draft Bilingual Reply", "Generate polite English, Hindi, or Hinglish reply options", StepStatus.READY),
                TaskStepPreview(3, "User Approval Gate", "Review, edit, or approve before dispatch", StepStatus.REQUIRES_APPROVAL)
            )
        )

        return ChatMessage(
            sender = MessageSender.NOVA,
            text = text,
            taskPlan = plan,
            suggestedFollowUps = listOf("Draft a polite busy reply", "Create Hinglish business template")
        )
    }

    private fun buildCallManagerResponse(isHindi: Boolean, isHinglish: Boolean): ChatMessage {
        val text = when {
            isHindi -> "स्मार्ट कॉल मैनेजर Android के आधिकारिक CallScreeningService का उपयोग करेगा ताकि व्यस्त होने पर कॉलर की पहचान और स्वीकृत एक्शन सुरक्षित रूप से चल सकें।"
            isHinglish -> "Smart Call Manager Android ke official CallScreeningService aur system role par work karega, taaki unavailable hone par safe call screening aur custom alerts handle ho sakein."
            else -> "Smart Call Manager is architected around Android's official CallScreeningService role to handle unavailable status, caller screening, and approved call actions safely."
        }

        val plan = TaskPlanPreview(
            title = "Smart Call Screening Setup",
            category = "Call Manager",
            requiresApproval = true,
            targetPhaseNote = "Android CallScreeningService & Call Log dashboard activate in Phase 4.",
            steps = listOf(
                TaskStepPreview(1, "Request Call Screening Role", "Prompt user via official Android RoleManager", StepStatus.REQUIRES_APPROVAL),
                TaskStepPreview(2, "Configure Unavailable Policy", "Set custom busy status and permitted call screening behavior", StepStatus.READY),
                TaskStepPreview(3, "Log Activity Dashboard", "Record screened call summaries locally with privacy controls", StepStatus.SCHEDULED_NEXT_PHASE)
            )
        )

        return ChatMessage(
            sender = MessageSender.NOVA,
            text = text,
            taskPlan = plan,
            suggestedFollowUps = listOf("Set approval mode to Always Ask", "Plan a YouTube video")
        )
    }

    private fun buildDeviceControlResponse(
        input: String,
        isHindi: Boolean,
        isHinglish: Boolean
    ): ChatMessage {
        val text = when {
            isHindi -> "डिवाइस ऑटोमेशन प्लान तैयार है। Android सुरक्षा नियमों के अंतर्गत प्रत्येक एक्शन के लिए आवश्यक अनुमति जांची जाएगी।"
            isHinglish -> "Device automation plan ready hai. Android permissions verify karne ke baad har supported action safe tarike se execute hoga."
            else -> "Device automation workflow drafted. Each step verifies required Android permissions and logs execution transparently."
        }

        val plan = TaskPlanPreview(
            title = "Android Device Action Plan",
            category = "Phone Control",
            requiresApproval = true,
            targetPhaseNote = "Live Android Intents, Alarm/Reminder tools & Automation Engine connect in Phase 3.",
            steps = listOf(
                TaskStepPreview(1, "Parse Target Action", "Identify app, reminder, or setting in: "$input"", StepStatus.READY),
                TaskStepPreview(2, "Verify Android Permission", "Check runtime consent before invoking supported Android Intent", StepStatus.REQUIRES_APPROVAL),
                TaskStepPreview(3, "Execute & Log", "Run supported system action and record in Task History", StepStatus.SCHEDULED_NEXT_PHASE)
            )
        )

        return ChatMessage(
            sender = MessageSender.NOVA,
            text = text,
            taskPlan = plan,
            suggestedFollowUps = listOf("Plan a YouTube video", "Research best AI tools")
        )
    }

    private fun buildResearchPlanResponse(
        input: String,
        isHindi: Boolean,
        isHinglish: Boolean
    ): ChatMessage {
        val text = when {
            isHindi -> "रिसर्च एजेंट का प्लान तैयार है। Phase 2 और Phase 6 में लाइव वेब सर्च और वेरिफाइड सोर्स लिंक सक्रिय होंगे — NOVA कभी भी नकली परिणाम नहीं दिखाएगा।"
            isHinglish -> "Research Agent workflow ready hai. Live web search aur verified source links Phase 2/6 me connect honge — NOVA kabhi fake search results generate nahi karta."
            else -> "Research workflow structured. Verified live web retrieval connects via secure backend tools in Phases 2 and 6; NOVA never fabricates search results."
        }

        val plan = TaskPlanPreview(
            title = "Verified Web Research Plan",
            category = "Research Agent",
            requiresApproval = false,
            targetPhaseNote = "Server-side search & citation summarization connect in Phases 2 & 6.",
            steps = listOf(
                TaskStepPreview(1, "Query Formulation", "Extract key comparisons and source criteria for: "$input"", StepStatus.READY),
                TaskStepPreview(2, "Fetch & Summarize Sources", "Retrieve verified pages and extract structured insights with links", StepStatus.SCHEDULED_NEXT_PHASE),
                TaskStepPreview(3, "Save Research Report", "Store structured brief in searchable local/cloud history", StepStatus.SCHEDULED_NEXT_PHASE)
            )
        )

        return ChatMessage(
            sender = MessageSender.NOVA,
            text = text,
            taskPlan = plan,
            suggestedFollowUps = listOf("Plan a YouTube video", "Test Voice Assistant")
        )
    }

    private fun buildGeneralAssistantResponse(
        input: String,
        isHindi: Boolean,
        isHinglish: Boolean,
        messageCount: Int
    ): ChatMessage {
        val text = when {
            isHindi -> "नमस्ते! मैं NOVA AI (Phase 1 Core) हूँ। मैंने आपका संदेश समझा: "$input"। अभी आप प्रीमियम डैशबोर्ड, वॉइस असिस्टेंट (हिंदी/अंग्रेज़ी), थीम और टास्क प्लानिंग टेस्ट कर सकते हैं। Phase 2 में सुरक्षित Firebase + Gemini बैकएंड जुड़ते ही लाइव AI उत्तर शुरू हो जाएंगे।"
            isHinglish -> "Hello! Main NOVA AI (Phase 1 Core) hoon. Aapka command mila: "$input". Abhi aap Voice Assistant (Hindi/English), Task Planner, aur Dark/Light UI use kar sakte hain. Phase 2 me Firebase + Gemini API backend connect hote hi full live AI brain activate ho jayega."
            else -> "NOVA AI Phase 1 Core is active. I received: "$input" (Session turn ${messageCount + 1}). Try asking me to plan a YouTube video, draft a messaging workflow, or tap the Voice Assistant orb for live Hindi & English speech recognition."
        }

        return ChatMessage(
            sender = MessageSender.NOVA,
            text = text,
            suggestedFollowUps = listOf(
                "Create a YouTube video about AI",
                "Set up Smart Call Manager",
                "Plan a WhatsApp reply workflow"
            )
        )
    }
}
