package com.nova.ai.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nova.ai.ui.components.NovaBackdrop
import com.nova.ai.ui.components.NovaGlassCard
import com.nova.ai.ui.theme.LocalNovaColors
import com.nova.ai.ui.theme.NovaEmerald
import com.nova.ai.ui.theme.NovaEmeraldBright
import com.nova.ai.ui.theme.NovaObsidian
import kotlinx.coroutines.launch

private data class OnboardingPage(
    val icon: ImageVector,
    val badge: String,
    val title: String,
    val description: String
)

private val onboardingPages = listOf(
    OnboardingPage(
        icon = Icons.Rounded.AutoAwesome,
        badge = "MULTI-STEP AI AGENT",
        title = "Your Personal Digital Operator",
        description = "Give natural commands in English, Hindi, or Hinglish. NOVA breaks complex goals into clear, actionable workflows."
    ),
    OnboardingPage(
        icon = Icons.Rounded.GraphicEq,
        badge = "BILINGUAL VOICE ENGINE",
        title = "Natural Voice, Hands-Free Flow",
        description = "Speak naturally with live Hindi & English speech recognition, continuous conversation mode, and Male or Female AI voices."
    ),
    OnboardingPage(
        icon = Icons.Rounded.VerifiedUser,
        badge = "APPROVAL-FIRST SECURITY",
        title = "You Stay 100% In Control",
        description = "Every external publish, message, or device action passes through transparent permissions and your Approval Center."
    )
)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val colors = LocalNovaColors.current
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()

    NovaBackdrop {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onFinish) {
                    Text("Skip", color = colors.textSecondary)
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { pageIndex ->
                val page = onboardingPages[pageIndex]
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    NovaGlassCard(
                        shape = CircleShape,
                        contentPadding = PaddingValues(28.dp)
                    ) {
                        Icon(
                            imageVector = page.icon,
                            contentDescription = null,
                            tint = NovaEmeraldBright,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = page.badge,
                        style = MaterialTheme.typography.labelMedium,
                        color = NovaEmeraldBright
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = page.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = colors.textPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = page.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.textSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Page Indicators
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(onboardingPages.size) { index ->
                    val selected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(8.dp)
                            .width(if (selected) 28.dp else 8.dp)
                            .clip(CircleShape)
                            .background(if (selected) NovaEmeraldBright else colors.glassBorder)
                    )
                }
            }

            Button(
                onClick = {
                    if (pagerState.currentPage < onboardingPages.lastIndex) {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    } else {
                        onFinish()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NovaEmerald,
                    contentColor = NovaObsidian
                )
            ) {
                Text(
                    text = if (pagerState.currentPage == onboardingPages.lastIndex) "Launch NOVA AI" else "Continue",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
