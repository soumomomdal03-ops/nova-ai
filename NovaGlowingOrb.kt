package com.nova.ai.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nova.ai.ui.theme.LocalNovaColors
import com.nova.ai.ui.theme.NovaEmerald
import com.nova.ai.ui.theme.NovaEmeraldBright
import com.nova.ai.ui.theme.NovaMintGlow
import com.nova.ai.ui.theme.NovaPlasmaCyan
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun NovaDashboardOrb(
    greeting: String,
    subtitle: String,
    size: Dp = 220.dp,
    onClick: () -> Unit
) {
    val colors = LocalNovaColors.current
    val infiniteTransition = rememberInfiniteTransition(label = "dashboard_orb")

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave"
    )

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val maxRadius = this.size.minDimension / 2f

            // Outer ambient emerald halo
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        NovaEmerald.copy(alpha = 0.34f),
                        NovaEmerald.copy(alpha = 0.12f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = maxRadius * pulse
                ),
                radius = maxRadius * pulse,
                center = center
            )

            // Inner dark glass core
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF0E3A2B),
                        Color(0xFF061B14),
                        Color(0xFF030B08)
                    ),
                    center = center,
                    radius = maxRadius * 0.78f
                ),
                radius = maxRadius * 0.78f,
                center = center
            )

            // Orbiting neon emerald ring
            val rad = Math.toRadians(rotation.toDouble())
            val start = Offset(
                center.x + (cos(rad) * maxRadius * 0.78f).toFloat(),
                center.y + (sin(rad) * maxRadius * 0.78f).toFloat()
            )
            val end = Offset(
                center.x - (cos(rad) * maxRadius * 0.78f).toFloat(),
                center.y - (sin(rad) * maxRadius * 0.78f).toFloat()
            )
            drawCircle(
                brush = Brush.linearGradient(
                    colors = listOf(NovaEmeraldBright, NovaPlasmaCyan.copy(alpha = 0.45f), Color.Transparent),
                    start = start,
                    end = end
                ),
                radius = maxRadius * 0.78f,
                center = center,
                style = Stroke(width = 3.5f)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = greeting,
                style = MaterialTheme.typography.labelMedium,
                color = NovaMintGlow
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = if (colors.isDark) Color.White else Color(0xFFE6FFF7)
            )
            Spacer(modifier = Modifier.height(14.dp))

            // Animated voice equalizer bars
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val barMultipliers = listOf(0.45f, 0.85f, 1.0f, 0.7f, 0.9f, 0.5f)
                barMultipliers.forEachIndexed { index, base ->
                    val dynamicFactor = (0.5f + 0.5f * sin(wavePhase + index * 0.9f)).coerceIn(0.25f, 1f)
                    val barHeight = (10f + 16f * base * dynamicFactor).dp
                    Canvas(modifier = Modifier.width(3.dp).height(barHeight)) {
                        drawRoundRect(
                            color = NovaEmeraldBright,
                            cornerRadius = CornerRadius(4f, 4f),
                            size = Size(this.size.width, this.size.height)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NovaPlasmaVoiceOrb(
    isActive: Boolean,
    audioLevel: Float,
    size: Dp = 210.dp,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "plasma_orb")

    val breathe by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathe"
    )

    val swirl by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(5200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "swirl"
    )

    val dynamicScale = if (isActive) {
        (breathe + audioLevel * 0.14f).coerceAtMost(1.22f)
    } else {
        breathe
    }

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val baseRadius = this.size.minDimension / 2.4f

            // Outer glowing aura
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        if (isActive) NovaPlasmaCyan.copy(alpha = 0.55f) else NovaEmerald.copy(alpha = 0.35f),
                        NovaEmeraldBright.copy(alpha = 0.2f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = baseRadius * 1.25f * dynamicScale
                ),
                radius = baseRadius * 1.25f * dynamicScale,
                center = center
            )

            // Main luminous plasma sphere (matches reference voice orb)
            val rad = Math.toRadians(swirl.toDouble())
            val highlightCenter = Offset(
                center.x + (cos(rad) * baseRadius * 0.22f).toFloat(),
                center.y + (sin(rad) * baseRadius * 0.22f).toFloat()
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFF0FDFA),
                        Color(0xFF7DD3FC),
                        NovaEmeraldBright,
                        Color(0xFF0D9488),
                        Color(0xFF064E3B)
                    ),
                    center = highlightCenter,
                    radius = baseRadius * dynamicScale
                ),
                radius = baseRadius * 0.92f * dynamicScale,
                center = center
            )

            // Crisp specular rim
            drawCircle(
                brush = Brush.linearGradient(
                    colors = listOf(Color.White.copy(alpha = 0.85f), NovaEmeraldBright.copy(alpha = 0.4f), Color.Transparent)
                ),
                radius = baseRadius * 0.92f * dynamicScale,
                center = center,
                style = Stroke(width = 3f)
            )
        }
    }
}
