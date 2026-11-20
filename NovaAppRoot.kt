package com.nova.ai.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Chat
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nova.ai.data.catalog.NovaModuleCatalog
import com.nova.ai.di.NovaAppContainer
import com.nova.ai.domain.model.UserPreferences
import com.nova.ai.ui.components.NovaBackdrop
import com.nova.ai.ui.screens.auth.AuthScreen
import com.nova.ai.ui.screens.chat.ChatScreen
import com.nova.ai.ui.screens.chat.ChatViewModel
import com.nova.ai.ui.screens.dashboard.DashboardScreen
import com.nova.ai.ui.screens.modules.ModuleDetailScreen
import com.nova.ai.ui.screens.modules.ModuleHubScreen
import com.nova.ai.ui.screens.onboarding.OnboardingScreen
import com.nova.ai.ui.screens.settings.SettingsScreen
import com.nova.ai.ui.screens.splash.SplashScreen
import com.nova.ai.ui.screens.voice.VoiceScreen
import com.nova.ai.ui.screens.voice.VoiceViewModel
import com.nova.ai.ui.theme.LocalNovaColors
import com.nova.ai.ui.theme.NovaEmerald
import com.nova.ai.ui.theme.NovaEmeraldBright
import com.nova.ai.ui.theme.NovaObsidian
import kotlinx.coroutines.launch

object NovaRoutes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val AUTH = "auth"
    const val DASHBOARD = "dashboard"
    const val CHAT = "chat"
    const val VOICE = "voice"
    const val HUB = "hub"
    const val MODULE_DETAIL = "module/{moduleId}"
    const val SETTINGS = "settings"

    fun moduleDetail(id: String) = "module/$id"
}

private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val bottomNavItems = listOf(
    BottomNavItem(NovaRoutes.DASHBOARD, "Home", Icons.Rounded.Home),
    BottomNavItem(NovaRoutes.CHAT, "Chat", Icons.AutoMirrored.Rounded.Chat),
    BottomNavItem(NovaRoutes.VOICE, "Voice", Icons.Rounded.Mic),
    BottomNavItem(NovaRoutes.HUB, "Studio", Icons.Rounded.Apps),
    BottomNavItem(NovaRoutes.SETTINGS, "Settings", Icons.Rounded.Settings)
)

@Composable
fun NovaAppRoot(
    container: NovaAppContainer,
    preferences: UserPreferences
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val colors = LocalNovaColors.current

    val chatViewModel: ChatViewModel = viewModel(
        factory = ChatViewModel.Factory(container.aiEngineRepository, container.voiceController)
    )
    val voiceViewModel: VoiceViewModel = viewModel(
        factory = VoiceViewModel.Factory(container.voiceController, container.aiEngineRepository)
    )

    var pendingChatPrompt by remember { mutableStateOf<String?>(null) }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val showChrome = currentRoute in setOf(
        NovaRoutes.DASHBOARD,
        NovaRoutes.CHAT,
        NovaRoutes.VOICE,
        NovaRoutes.HUB,
        NovaRoutes.SETTINGS,
        NovaRoutes.MODULE_DETAIL
    )

    fun navigateTopLevel(route: String) {
        navController.navigate(route) {
            popUpTo(NovaRoutes.DASHBOARD) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showChrome,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = if (colors.isDark) Color(0xFF06130E) else Color(0xFFF3FAF7)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Text(
                        text = "NOVA AI",
                        style = MaterialTheme.typography.headlineMedium,
                        color = NovaEmeraldBright
                    )
                    Text(
                        text = "Operator: ${preferences.userName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textSecondary
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = colors.glassBorder)
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "CORE ASSISTANT",
                        style = MaterialTheme.typography.labelSmall,
                        color = NovaEmeraldBright,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )

                    bottomNavItems.forEach { item ->
                        NavigationDrawerItem(
                            label = { Text(item.label, color = colors.textPrimary) },
                            icon = { Icon(item.icon, null, tint = NovaEmeraldBright) },
                            selected = currentRoute == item.route,
                            onClick = {
                                scope.launch { drawerState.close() }
                                navigateTopLevel(item.route)
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = NovaEmerald.copy(alpha = 0.2f),
                                unselectedContainerColor = Color.Transparent
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = colors.glassBorder)
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "MODULES (PHASES 2–6)",
                        style = MaterialTheme.typography.labelSmall,
                        color = NovaEmeraldBright,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )

                    NovaModuleCatalog.allModules.forEach { module ->
                        NavigationDrawerItem(
                            label = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = module.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = colors.textPrimary
                                    )
                                    Text(
                                        text = module.phase.badge,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = NovaEmeraldBright
                                    )
                                }
                            },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate(NovaRoutes.moduleDetail(module.id))
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) {
        NovaBackdrop {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(if (showChrome) Modifier.statusBarsPadding() else Modifier)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = NovaRoutes.SPLASH
                ) {
                    composable(NovaRoutes.SPLASH) {
                        SplashScreen(
                            onSplashComplete = {
                                val next = when {
                                    !preferences.isOnboardingCompleted -> NovaRoutes.ONBOARDING
                                    !preferences.isLoggedIn -> NovaRoutes.AUTH
                                    else -> NovaRoutes.DASHBOARD
                                }
                                navController.navigate(next) {
                                    popUpTo(NovaRoutes.SPLASH) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(NovaRoutes.ONBOARDING) {
                        OnboardingScreen(
                            onFinish = {
                                scope.launch { container.preferencesRepository.completeOnboarding() }
                                navController.navigate(NovaRoutes.AUTH) {
                                    popUpTo(NovaRoutes.ONBOARDING) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(NovaRoutes.AUTH) {
                        AuthScreen(
                            onAuthenticated = { name, email ->
                                scope.launch { container.preferencesRepository.saveLocalSession(name, email) }
                                navController.navigate(NovaRoutes.DASHBOARD) {
                                    popUpTo(NovaRoutes.AUTH) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(NovaRoutes.DASHBOARD) {
                        DashboardScreen(
                            preferences = preferences,
                            onOpenDrawer = { scope.launch { drawerState.open() } },
                            onOpenChat = { prompt ->
                                pendingChatPrompt = prompt
                                navigateTopLevel(NovaRoutes.CHAT)
                            },
                            onOpenVoice = { navigateTopLevel(NovaRoutes.VOICE) },
                            onOpenModule = { id ->
                                if (id == "hub") {
                                    navigateTopLevel(NovaRoutes.HUB)
                                } else {
                                    navController.navigate(NovaRoutes.moduleDetail(id))
                                }
                            },
                            onOpenSettings = { navigateTopLevel(NovaRoutes.SETTINGS) }
                        )
                    }

                    composable(NovaRoutes.CHAT) {
                        ChatScreen(
                            viewModel = chatViewModel,
                            preferences = preferences,
                            initialPrompt = pendingChatPrompt,
                            onInitialPromptConsumed = { pendingChatPrompt = null },
                            onOpenVoice = { navigateTopLevel(NovaRoutes.VOICE) }
                        )
                    }

                    composable(NovaRoutes.VOICE) {
                        VoiceScreen(
                            viewModel = voiceViewModel,
                            preferences = preferences,
                            onLanguageChange = { lang ->
                                scope.launch { container.preferencesRepository.setLanguage(lang) }
                            },
                            onVoiceGenderChange = { gender ->
                                scope.launch { container.preferencesRepository.setVoiceGender(gender) }
                            },
                            onContinuousModeChange = { enabled ->
                                scope.launch { container.preferencesRepository.setContinuousVoiceMode(enabled) }
                            },
                            onSwitchToChat = { navigateTopLevel(NovaRoutes.CHAT) }
                        )
                    }

                    composable(NovaRoutes.HUB) {
                        ModuleHubScreen(
                            onOpenModule = { id ->
                                navController.navigate(NovaRoutes.moduleDetail(id))
                            }
                        )
                    }

                    composable(
                        route = NovaRoutes.MODULE_DETAIL,
                        arguments = listOf(navArgument("moduleId") { type = NavType.StringType })
                    ) { entry ->
                        val moduleId = entry.arguments?.getString("moduleId").orEmpty()
                        ModuleDetailScreen(
                            moduleId = moduleId,
                            onBack = { navController.popBackStack() },
                            onPlanInChat = { prompt ->
                                pendingChatPrompt = prompt
                                navigateTopLevel(NovaRoutes.CHAT)
                            }
                        )
                    }

                    composable(NovaRoutes.SETTINGS) {
                        SettingsScreen(
                            preferences = preferences,
                            onThemeChange = { mode ->
                                scope.launch { container.preferencesRepository.setThemeMode(mode) }
                            },
                            onLanguageChange = { lang ->
                                scope.launch { container.preferencesRepository.setLanguage(lang) }
                            },
                            onVoiceGenderChange = { gender ->
                                scope.launch { container.preferencesRepository.setVoiceGender(gender) }
                            },
                            onApprovalModeChange = { mode ->
                                scope.launch { container.preferencesRepository.setApprovalMode(mode) }
                            },
                            onClearMemory = {
                                scope.launch { container.preferencesRepository.clearPersonalMemory() }
                            },
                            onSignOut = {
                                scope.launch { container.preferencesRepository.signOut() }
                                navController.navigate(NovaRoutes.AUTH) {
                                    popUpTo(NovaRoutes.DASHBOARD) { inclusive = true }
                                }
                            }
                        )
                    }
                }

                // Premium Floating Bottom Navigation Bar
                if (showChrome) {
                    NovaFloatingBottomBar(
                        currentRoute = currentRoute,
                        onSelect = { route -> navigateTopLevel(route) },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .navigationBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun NovaFloatingBottomBar(
    currentRoute: String?,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalNovaColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(if (colors.isDark) Color(0xE6081812) else Color(0xF2FFFFFF))
            .border(1.dp, colors.glassBorder, RoundedCornerShape(28.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route
            val isVoiceCenter = item.route == NovaRoutes.VOICE

            if (isVoiceCenter) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (selected) NovaEmeraldBright else NovaEmerald)
                        .clickable { onSelect(item.route) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = NovaObsidian,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelect(item.route) }
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selected) NovaEmeraldBright else colors.textSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                        ),
                        color = if (selected) NovaEmeraldBright else colors.textSecondary
                    )
                }
            }
        }
    }
}
