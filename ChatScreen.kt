package com.nova.ai.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nova.ai.domain.model.ChatMessage
import com.nova.ai.domain.model.MessageSender
import com.nova.ai.domain.model.StepStatus
import com.nova.ai.domain.model.TaskPlanPreview
import com.nova.ai.domain.model.UserPreferences
import com.nova.ai.ui.components.NovaGlassCard
import com.nova.ai.ui.theme.LocalNovaColors
import com.nova.ai.ui.theme.NovaEmerald
import com.nova.ai.ui.theme.NovaEmeraldBright
import com.nova.ai.ui.theme.NovaObsidian
import com.nova.ai.ui.theme.NovaWarning

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    preferences: UserPreferences,
    initialPrompt: String?,
    onInitialPromptConsumed: () -> Unit,
    onOpenVoice: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = LocalNovaColors.current
    val listState = rememberLazyListState()
    val clipboard = LocalClipboardManager.current
    var inputText by remember { mutableStateOf("") }

    LaunchedEffect(initialPrompt) {
        if (!initialPrompt.isNullOrBlank()) {
            viewModel.sendMessage(initialPrompt, preferences.language)
            onInitialPromptConsumed()
        }
    }

    LaunchedEffect(uiState.messages.size, uiState.isThinking) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.lastIndex)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(bottom = 86.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "NOVA AI Brain",
                    style = MaterialTheme.typography.titleLarge,
                    color = colors.textPrimary
                )
                Text(
                    text = "Language: ${preferences.language.label} • Agent Task Planner",
                    style = MaterialTheme.typography.labelSmall,
                    color = NovaEmeraldBright
                )
            }

            IconButton(
                onClick = { viewModel.clearChat() },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colors.glassSurface)
                    .border(1.dp, colors.glassBorder, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Rounded.DeleteOutline,
                    contentDescription = "Clear Chat",
                    tint = colors.textSecondary
                )
            }
        }

        // Messages
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(uiState.messages, key = { it.id }) { message ->
                ChatBubbleItem(
                    message = message,
                    onCopy = { clipboard.setText(AnnotatedString(message.text)) },
                    onSpeak = {
                        viewModel.speakMessage(
                            text = message.text,
                            language = preferences.language,
                            gender = preferences.voiceGender
                        )
                    },
                    onSuggestionClick = { suggestion ->
                        viewModel.sendMessage(suggestion, preferences.language)
                    }
                )
            }

            if (uiState.isThinking) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = NovaEmeraldBright
                        )
                        Text(
                            text = "NOVA is planning...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.textSecondary
                        )
                    }
                }
            }
        }

        // Input Composer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = {
                    Text(
                        "Ask NOVA anything...",
                        color = colors.textSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                maxLines = 4,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NovaEmeraldBright,
                    unfocusedBorderColor = colors.glassBorder,
                    focusedContainerColor = colors.glassSurface,
                    unfocusedContainerColor = colors.glassSurface,
                    focusedTextColor = colors.textPrimary,
                    unfocusedTextColor = colors.textPrimary,
                    cursorColor = NovaEmeraldBright
                ),
                trailingIcon = {
                    IconButton(onClick = onOpenVoice) {
                        Icon(
                            imageVector = Icons.Rounded.Mic,
                            contentDescription = "Voice Command",
                            tint = NovaEmeraldBright
                        )
                    }
                }
            )

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.sendMessage(inputText, preferences.language)
                        inputText = ""
                    }
                },
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(NovaEmerald)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Send,
                    contentDescription = "Send",
                    tint = NovaObsidian
                )
            }
        }
    }
}

@Composable
private fun ChatBubbleItem(
    message: ChatMessage,
    onCopy: () -> Unit,
    onSpeak: () -> Unit,
    onSuggestionClick: (String) -> Unit
) {
    val colors = LocalNovaColors.current
    val isUser = message.sender == MessageSender.USER

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        if (isUser) {
            Box(
                modifier = Modifier
                    .widthIn(max = 300.dp)
                    .clip(RoundedCornerShape(20.dp, 20.dp, 6.dp, 20.dp))
                    .background(NovaEmerald.copy(alpha = 0.24f))
                    .border(1.dp, NovaEmerald.copy(alpha = 0.5f), RoundedCornerShape(20.dp, 20.dp, 6.dp, 20.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.textPrimary
                )
            }
        } else {
            NovaGlassCard(
                modifier = Modifier.widthIn(max = 340.dp),
                shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 6.dp),
                contentPadding = PaddingValues(15.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            tint = NovaEmeraldBright,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "NOVA AI",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = NovaEmeraldBright
                        )
                    }

                    Text(
                        text = message.text,
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.textPrimary
                    )

                    if (message.taskPlan != null) {
                        TaskPlanCard(plan = message.taskPlan)
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.clickable(onClick = onSpeak),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
                                contentDescription = "Speak",
                                tint = colors.textSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text("Listen", style = MaterialTheme.typography.labelSmall, color = colors.textSecondary)
                        }

                        Row(
                            modifier = Modifier.clickable(onClick = onCopy),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ContentCopy,
                                contentDescription = "Copy",
                                tint = colors.textSecondary,
                                modifier = Modifier.size(15.dp)
                            )
                            Text("Copy", style = MaterialTheme.typography.labelSmall, color = colors.textSecondary)
                        }
                    }
                }
            }

            if (message.suggestedFollowUps.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    message.suggestedFollowUps.forEach { chip ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(colors.glassSurface)
                                .border(1.dp, colors.glassBorder, RoundedCornerShape(16.dp))
                                .clickable { onSuggestionClick(chip) }
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = chip,
                                style = MaterialTheme.typography.labelMedium,
                                color = NovaEmeraldBright
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskPlanCard(plan: TaskPlanPreview) {
    val colors = LocalNovaColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.Black.copy(alpha = if (colors.isDark) 0.35f else 0.06f))
            .border(1.dp, colors.glassBorder, RoundedCornerShape(14.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = plan.title,
                style = MaterialTheme.typography.titleMedium,
                color = colors.textPrimary
            )
            if (plan.requiresApproval) {
                Icon(
                    imageVector = Icons.Rounded.VerifiedUser,
                    contentDescription = "Approval Required",
                    tint = NovaWarning,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        HorizontalDivider(color = colors.glassBorder)

        plan.steps.forEach { step ->
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val (icon, tint) = when (step.status) {
                    StepStatus.READY -> Icons.Rounded.CheckCircle to NovaEmeraldBright
                    StepStatus.REQUIRES_APPROVAL -> Icons.Rounded.VerifiedUser to NovaWarning
                    StepStatus.SCHEDULED_NEXT_PHASE -> Icons.Rounded.Schedule to colors.textSecondary
                }
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(16.dp)
                )
                Column {
                    Text(
                        text = "${step.index}. ${step.title}",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = colors.textPrimary
                    )
                    Text(
                        text = step.detail,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textSecondary
                    )
                }
            }
        }

        Text(
            text = plan.targetPhaseNote,
            style = MaterialTheme.typography.labelSmall,
            color = NovaEmeraldBright
        )
    }
}
