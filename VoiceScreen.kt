package com.nova.ai.ui.screens.voice

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Keyboard
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nova.ai.domain.model.AppLanguage
import com.nova.ai.domain.model.UserPreferences
import com.nova.ai.domain.model.VoiceGender
import com.nova.ai.domain.model.VoiceSessionState
import com.nova.ai.ui.components.NovaGlassCard
import com.nova.ai.ui.components.NovaPlasmaVoiceOrb
import com.nova.ai.ui.theme.LocalNovaColors
import com.nova.ai.ui.theme.NovaDanger
import com.nova.ai.ui.theme.NovaEmerald
import com.nova.ai.ui.theme.NovaEmeraldBright
import com.nova.ai.ui.theme.NovaObsidian

@Composable
fun VoiceScreen(
    viewModel: VoiceViewModel,
    preferences: UserPreferences,
    onLanguageChange: (AppLanguage) -> Unit,
    onVoiceGenderChange: (VoiceGender) -> Unit,
    onContinuousModeChange: (Boolean) -> Unit,
    onSwitchToChat: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = LocalNovaColors.current
    val context = LocalContext.current
    var permissionDeniedNote by remember { mutableStateOf(false) }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            permissionDeniedNote = false
            viewModel.startListening(preferences)
        } else {
            permissionDeniedNote = true
        }
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.stopAll() }
    }

    fun triggerMic() {
        val active = uiState.state == VoiceSessionState.LISTENING ||
            uiState.state == VoiceSessionState.SPEAKING
        if (active) {
            viewModel.stopAll()
            return
        }
        val hasMic = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (hasMic) {
            permissionDeniedNote = false
            viewModel.startListening(preferences)
        } else {
            micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    val statusLabel = when (uiState.state) {
        VoiceSessionState.IDLE -> "Ready to listen"
        VoiceSessionState.LISTENING -> "Listening..."
        VoiceSessionState.PROCESSING -> "Thinking..."
        VoiceSessionState.SPEAKING -> "NOVA Speaking..."
        VoiceSessionState.ERROR -> "Tap microphone to retry"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 104.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header (Matches reference screen 3)
        Text(
            text = "Voice Analysis",
            style = MaterialTheme.typography.titleLarge,
            color = colors.textPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = statusLabel,
            style = MaterialTheme.typography.bodyMedium,
            color = NovaEmeraldBright
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Luminous Plasma Orb
        NovaPlasmaVoiceOrb(
            isActive = uiState.state == VoiceSessionState.LISTENING || uiState.state == VoiceSessionState.SPEAKING,
            audioLevel = uiState.audioLevel,
            size = 215.dp,
            onClick = { triggerMic() }
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Live Transcript / AI Response
        if (uiState.transcript.isNotBlank()) {
            Text(
                text = ""${uiState.transcript}"",
                style = MaterialTheme.typography.titleMedium,
                color = colors.textPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        NovaGlassCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(
                text = when {
                    permissionDeniedNote -> "Microphone permission is needed for voice commands. Please allow RECORD_AUDIO."
                    uiState.errorText != null -> uiState.errorText.orEmpty()
                    else -> uiState.aiReply
                },
                style = MaterialTheme.typography.bodyMedium,
                color = if (permissionDeniedNote || uiState.errorText != null) NovaDanger else colors.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Voice & Language Controls
        NovaGlassCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AppLanguage.entries.forEach { lang ->
                        val selected = preferences.language == lang
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (selected) NovaEmerald.copy(alpha = 0.24f) else colors.glassSurface)
                                .border(
                                    1.dp,
                                    if (selected) NovaEmeraldBright else colors.glassBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onLanguageChange(lang) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = lang.label,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (selected) NovaEmeraldBright else colors.textSecondary
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VoiceGender.entries.forEach { gender ->
                        val selected = preferences.voiceGender == gender
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (selected) NovaEmerald.copy(alpha = 0.24f) else colors.glassSurface)
                                .border(
                                    1.dp,
                                    if (selected) NovaEmeraldBright else colors.glassBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onVoiceGenderChange(gender) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (gender == VoiceGender.FEMALE) "Female Voice" else "Male Voice",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (selected) NovaEmeraldBright else colors.textSecondary
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Continuous Conversation",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = colors.textPrimary
                        )
                        Text(
                            text = "Auto-listen again after NOVA finishes speaking",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.textSecondary
                        )
                    }
                    Switch(
                        checked = preferences.continuousVoiceMode,
                        onCheckedChange = onContinuousModeChange,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NovaObsidian,
                            checkedTrackColor = NovaEmeraldBright
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Bottom Controls Row (Matches reference screen 3)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onSwitchToChat,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(colors.glassSurface)
                    .border(1.dp, colors.glassBorder, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Keyboard,
                    contentDescription = "Switch to Text Chat",
                    tint = colors.textPrimary
                )
            }

            val isRunning = uiState.state == VoiceSessionState.LISTENING ||
                uiState.state == VoiceSessionState.SPEAKING

            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(if (isRunning) NovaEmeraldBright else NovaEmerald)
                    .border(3.dp, NovaEmeraldBright.copy(alpha = 0.45f), CircleShape)
                    .clickable { triggerMic() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Rounded.Stop else Icons.Rounded.Mic,
                    contentDescription = "Microphone",
                    tint = NovaObsidian,
                    modifier = Modifier.size(34.dp)
                )
            }

            IconButton(
                onClick = { viewModel.stopAll() },
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(colors.glassSurface)
                    .border(1.dp, colors.glassBorder, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Stop Voice",
                    tint = colors.textPrimary
                )
            }
        }
    }
}
