package com.nova.ai.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nova.ai.ui.components.NovaBackdrop
import com.nova.ai.ui.components.NovaPlasmaVoiceOrb
import com.nova.ai.ui.theme.LocalNovaColors
import com.nova.ai.ui.theme.NovaEmeraldBright
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    val colors = LocalNovaColors.current
    val latestOnComplete by rememberUpdatedState(onSplashComplete)
    val scale = remember { Animatable(0.75f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(1f, animationSpec = tween(750, easing = FastOutSlowInEasing))
        alpha.animateTo(1f, animationSpec = tween(550))
        delay(900)
        latestOnComplete()
    }

    NovaBackdrop {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            NovaPlasmaVoiceOrb(
                isActive = true,
                audioLevel = 0.25f,
                size = 165.dp,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "NOVA AI",
                style = MaterialTheme.typography.displaySmall.copy(
                    letterSpacing = 6.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = colors.textPrimary,
                modifier = Modifier
                    .scale(scale.value)
                    .alpha(alpha.value)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "AUTONOMOUS PERSONAL AI OPERATOR",
                style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 2.sp),
                color = NovaEmeraldBright,
                modifier = Modifier.alpha(alpha.value)
            )
        }
    }
}
