package com.nova.ai.ui.screens.dashboard

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.Chat
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.TravelExplore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nova.ai.domain.model.UserPreferences
import com.nova.ai.ui.components.NovaDashboardOrb
import com.nova.ai.ui.components.NovaGlassCard
import com.nova.ai.ui.theme.LocalNovaColors
import com.nova.ai.ui.theme.NovaEmerald
import com.nova.ai.ui.theme.NovaEmeraldBright
import com.nova.ai.ui.theme.NovaObsidian

private data class QuickCapabilityCard(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val badge: String,
    val action: () -> Unit
)

@Composable
fun DashboardScreen(
    preferences: UserPreferences,
    onOpenDrawer: () -> Unit,
    onOpenChat: (initialPrompt: String?) -> Unit,
    onOpenVoice: () -> Unit,
    onOpenModule: (moduleId: String) -> Unit,
    onOpenSettings: () -> Unit
) {
    val colors = LocalNovaColors.current

    val cards = listOf(
        QuickCapabilityCard(
            title = "YouTube AI Studio",
            subtitle = "Script, thumbnail, metadata & OAuth upload workflow",
            icon = Icons.Rounded.PlayCircle,
            badge = "Plan Now",
            action = { onOpenChat("Nova, create a YouTube video about artificial intelligence, generate its title, description, thumbnail, and upload it to my channel.") }
        ),
        QuickCapabilityCard(
            title = "Voice & Hands-Free Control",
            subtitle = "Live Hindi, English & Hinglish speech recognition",
            icon = Icons.Rounded.Mic,
            badge = "Active",
            action = onOpenVoice
        ),
        QuickCapabilityCard(
            title = "Smart Call & Messaging Hub",
            subtitle = "CallScreeningService, WhatsApp, Instagram & Messenger",
            icon = Icons.Rounded.Call,
            badge = "Phase 4",
            action = { onOpenModule("call_manager") }
        ),
        QuickCapabilityCard(
            title = "Research & Approval Engine",
            subtitle = "Verified web research, Android tools & Approval Center",
            icon = Icons.Rounded.TravelExplore,
            badge = "Phase 3/6",
            action = { onOpenModule("approval_center") }
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 110.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Header Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(colors.glassSurface)
                            .border(1.dp, colors.glassBorder, CircleShape)
                            .clickable(onClick = onOpenDrawer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Menu,
                            contentDescription = "Menu",
                            tint = colors.textPrimary
                        )
                    }

                    Column {
                        Text(
                            text = "NOVA AI",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = colors.textPrimary
                        )
                        Text(
                            text = "${preferences.language.label} • ${preferences.approvalMode.title}",
                            style = MaterialTheme.typography.labelSmall,
                            color = NovaEmeraldBright
                        )
                    }
                }

                IconButton(
                    onClick = onOpenSettings,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(colors.glassSurface)
                        .border(1.dp, colors.glassBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Settings",
                        tint = colors.textPrimary
                    )
                }
            }
        }

        // Center Glowing AI Assistant Orb (Inspired by reference screen 1)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NovaDashboardOrb(
                    greeting = "Hi ${preferences.userName}",
                    subtitle = "Tap to speak",
                    size = 224.dp,
                    onClick = onOpenVoice
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusPill(icon = Icons.Rounded.AutoAwesome, text = "Phase 1 Core Ready")
                    StatusPill(icon = Icons.Rounded.Security, text = "Approval Guard ON")
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Help that feels human",
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.textPrimary
                )
                Text(
                    text = "All Modules",
                    style = MaterialTheme.typography.labelMedium,
                    color = NovaEmeraldBright,
                    modifier = Modifier.clickable { onOpenModule("hub") }
                )
            }
        }

        // Glassmorphism Feature Cards
        items(cards) { card ->
            NovaGlassCard(
                onClick = card.action,
                contentPadding = PaddingValues(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(NovaEmerald.copy(alpha = 0.16f))
                            .border(1.dp, NovaEmerald.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = card.icon,
                            contentDescription = null,
                            tint = NovaEmeraldBright,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = card.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = colors.textPrimary
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NovaEmerald.copy(alpha = 0.2f))
                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = card.badge,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NovaEmeraldBright
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = card.subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.textSecondary
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        tint = colors.textSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Bottom Glass Prompt Bar (matches reference screen 1)
        item {
            Spacer(modifier = Modifier.height(4.dp))
            NovaGlassCard(
                shape = RoundedCornerShape(28.dp),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp),
                onClick = { onOpenChat(null) }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Ask NOVA anything in English, Hindi or Hinglish...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textSecondary,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(NovaEmerald),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.Chat,
                            contentDescription = "Open Chat",
                            tint = NovaObsidian,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusPill(icon: ImageVector, text: String) {
    val colors = LocalNovaColors.current
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(colors.glassSurface)
            .border(1.dp, colors.glassBorder, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = NovaEmeraldBright,
            modifier = Modifier.size(13.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = colors.textSecondary
        )
    }
}
