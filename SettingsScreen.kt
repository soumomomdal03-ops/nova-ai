package com.nova.ai.ui.screens.settings

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nova.ai.domain.model.AppLanguage
import com.nova.ai.domain.model.ApprovalMode
import com.nova.ai.domain.model.ThemeMode
import com.nova.ai.domain.model.UserPreferences
import com.nova.ai.domain.model.VoiceGender
import com.nova.ai.ui.components.NovaGlassCard
import com.nova.ai.ui.theme.LocalNovaColors
import com.nova.ai.ui.theme.NovaDanger
import com.nova.ai.ui.theme.NovaEmerald
import com.nova.ai.ui.theme.NovaEmeraldBright
import com.nova.ai.ui.theme.NovaObsidian

@Composable
fun SettingsScreen(
    preferences: UserPreferences,
    onThemeChange: (ThemeMode) -> Unit,
    onLanguageChange: (AppLanguage) -> Unit,
    onVoiceGenderChange: (VoiceGender) -> Unit,
    onApprovalModeChange: (ApprovalMode) -> Unit,
    onClearMemory: () -> Unit,
    onSignOut: () -> Unit
) {
    val colors = LocalNovaColors.current
    var memoryClearedMessage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 110.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "AI Settings & Privacy",
            style = MaterialTheme.typography.headlineMedium,
            color = colors.textPrimary
        )

        // Profile Summary
        NovaGlassCard(contentPadding = PaddingValues(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = preferences.userName,
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.textPrimary
                )
                Text(
                    text = preferences.userEmail.ifBlank { "Phase 1 Local Operator Session" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.textSecondary
                )
            }
        }

        // Theme Toggle
        NovaGlassCard(contentPadding = PaddingValues(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Dark Obsidian Emerald Theme",
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.textPrimary
                    )
                    Text(
                        text = "Switch between Dark Mode and Light Mode",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textSecondary
                    )
                }
                Switch(
                    checked = preferences.themeMode == ThemeMode.DARK,
                    onCheckedChange = { isDark ->
                        onThemeChange(if (isDark) ThemeMode.DARK else ThemeMode.LIGHT)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = NovaObsidian,
                        checkedTrackColor = NovaEmeraldBright
                    )
                )
            }
        }

        // Language Selection
        NovaGlassCard(contentPadding = PaddingValues(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Preferred AI Language",
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.textPrimary
                )
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
                                .border(1.dp, if (selected) NovaEmeraldBright else colors.glassBorder, RoundedCornerShape(12.dp))
                                .clickable { onLanguageChange(lang) }
                                .padding(vertical = 10.dp),
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
            }
        }

        // Voice Selection
        NovaGlassCard(contentPadding = PaddingValues(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "AI Assistant Voice",
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.textPrimary
                )
                VoiceGender.entries.forEach { gender ->
                    val selected = preferences.voiceGender == gender
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) NovaEmerald.copy(alpha = 0.22f) else colors.glassSurface)
                            .border(1.dp, if (selected) NovaEmeraldBright else colors.glassBorder, RoundedCornerShape(12.dp))
                            .clickable { onVoiceGenderChange(gender) }
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = gender.label,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = if (selected) NovaEmeraldBright else colors.textPrimary
                        )
                    }
                }
            }
        }

        // Approval Center Policy
        NovaGlassCard(contentPadding = PaddingValues(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Approval Center Policy",
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.textPrimary
                )
                ApprovalMode.entries.forEach { mode ->
                    val selected = preferences.approvalMode == mode
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) NovaEmerald.copy(alpha = 0.22f) else colors.glassSurface)
                            .border(1.dp, if (selected) NovaEmeraldBright else colors.glassBorder, RoundedCornerShape(12.dp))
                            .clickable { onApprovalModeChange(mode) }
                            .padding(12.dp)
                    ) {
                        Text(
                            text = mode.title,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = if (selected) NovaEmeraldBright else colors.textPrimary
                        )
                        Text(
                            text = mode.subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.textSecondary
                        )
                    }
                }
            }
        }

        // Privacy & Personal Memory Controls
        NovaGlassCard(contentPadding = PaddingValues(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Privacy & Security",
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.textPrimary
                )
                Text(
                    text = "• Zero secret API keys stored inside this APK.
• All external integrations use official OAuth 2.0 & server-side Firebase Cloud Functions.
• You can wipe personal preferences and memory anytime.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.textSecondary
                )
                if (memoryClearedMessage) {
                    Text(
                        text = "Local personal memory preferences reset.",
                        style = MaterialTheme.typography.labelMedium,
                        color = NovaEmeraldBright
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedButton(
                    onClick = {
                        onClearMemory()
                        memoryClearedMessage = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Reset Personal Memory Preferences", color = NovaEmeraldBright)
                }
                OutlinedButton(
                    onClick = onSignOut,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NovaDanger)
                ) {
                    Text("Sign Out", color = NovaDanger)
                }
            }
        }
    }
}
