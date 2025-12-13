import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.desde_mi_rincon_app_01.navigation.AppScreen
import com.example.desde_mi_rincon_app_01.ui.components.ChatBotOverlay
import com.example.desde_mi_rincon_app_01.ui.screens.SplashScreen
import com.example.desde_mi_rincon_app_01.ui.screens.capsules.CapsulesScreen
import com.example.desde_mi_rincon_app_01.ui.screens.challenges.ChallengesScreen
import com.example.desde_mi_rincon_app_01.ui.screens.forum.ForumFeedScreen
import com.example.desde_mi_rincon_app_01.ui.screens.forum.ForumSelectionScreen
import com.example.desde_mi_rincon_app_01.ui.screens.forum.ForumWriteScreen
import com.example.desde_mi_rincon_app_01.ui.screens.home.HomeScreen
import com.example.desde_mi_rincon_app_01.ui.screens.weekly.WeeklyScreen
import androidx.annotation.Keep

@Keep
@Composable
fun App() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val screens = listOf(
        AppScreen.Home,
        AppScreen.Forum,
        AppScreen.Capsules,
        AppScreen.Weekly,
        AppScreen.Challenges
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                // La barra solo se renderiza si no estamos en Splash
                if (currentRoute != AppScreen.Splash.route) {
                    NavigationBar(
                        containerColor = Color.White,
                        tonalElevation = 8.dp,
                        modifier = Modifier.shadow(16.dp)
                    ) {
                        screens.forEach { screen ->
                            val isSelected = currentRoute == screen.route
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        imageVector = screen.icon,
                                        contentDescription = screen.title,
                                        modifier = Modifier.size(26.dp)
                                    )
                                },
                                label = {
                                    if (isSelected) {
                                        Text(
                                            screen.title,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                },
                                selected = isSelected,
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color(0xFF0D9488),
                                    selectedTextColor = Color(0xFF0D9488),
                                    indicatorColor = Color(0xFFE0F2F1),
                                    unselectedIconColor = Color(0xFF94A3B8),
                                    unselectedTextColor = Color(0xFF94A3B8)
                                ),
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->

            // --- CORRECCIÓN IMPORTANTE ---
            // Quitamos el modifier.padding(innerPadding) del NavHost.
            // Esto evita que el Splash se encoja cuando aparece la barra.
            NavHost(
                navController = navController,
                startDestination = AppScreen.Splash.route
            ) {

                // 1. PANTALLA SPLASH (Configuramos su SALIDA)
                composable(
                    route = AppScreen.Splash.route,
                    exitTransition = {
                        // Cuando el Splash se vaya, que haga un Fade Out (se vuelva transparente)
                        // tween(1000) significa "hazlo en 1000 milisegundos (1 segundo)"
                        fadeOut(animationSpec = tween(1000))
                    }
                ) {
                    SplashScreen(
                        onNavigateToHome = {
                            navController.navigate(AppScreen.Home.route) {
                                popUpTo(AppScreen.Splash.route) { inclusive = true }
                            }
                        }
                    )
                }
                // 2. PANTALLA HOME (Con Animación de Entrada y Padding Manual)
                composable(
                    route = AppScreen.Home.route,
                    enterTransition = {
                        // Fade In suave de 1 segundo
                        fadeIn(animationSpec = tween(1000))
                    }
                ) {
                    // APLICAMOS EL PADDING AQUÍ, DENTRO DE LA PANTALLA
                    Box(modifier = Modifier.padding(innerPadding)) {
                        HomeScreen()
                    }
                }

                // --- RESTO DE PANTALLAS (Aplicamos padding igual que en Home) ---

                composable(AppScreen.Forum.route) {
                    Box(modifier = Modifier.padding(innerPadding)) {
                        ForumSelectionScreen(
                            onEmotionSelected = { emotion ->
                                navController.navigate(AppScreen.ForumWrite.createRoute(emotion))
                            },
                            onGoToFeed = {
                                navController.navigate(AppScreen.CommunityFeed.route)
                            }
                        )
                    }
                }

                composable(AppScreen.CommunityFeed.route) {
                    Box(modifier = Modifier.padding(innerPadding)) {
                        ForumFeedScreen(
                            onShareFeeling = {
                                navController.navigate(AppScreen.Forum.route) {
                                    popUpTo(AppScreen.CommunityFeed.route) { saveState = true }
                                }
                            }
                        )
                    }
                }

                composable(
                    route = AppScreen.ForumWrite.route,
                    arguments = listOf(navArgument("emotion") { type = NavType.StringType })
                ) { backStackEntry ->
                    val emotion = backStackEntry.arguments?.getString("emotion") ?: "General"
                    // Nota: Quizás ForumWrite no necesita bottomBar o padding si es pantalla completa
                    // Si lo necesita, envuélvelo en Box(padding) también.
                    Box(modifier = Modifier.padding(innerPadding)) {
                        ForumWriteScreen(
                            emotionName = emotion,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }

                composable(AppScreen.Capsules.route) {
                    Box(modifier = Modifier.padding(innerPadding)) {
                        CapsulesScreen()
                    }
                }

                composable(AppScreen.Weekly.route) {
                    Box(modifier = Modifier.padding(innerPadding)) {
                        WeeklyScreen()
                    }
                }

                composable(AppScreen.Challenges.route) {
                    Box(modifier = Modifier.padding(innerPadding)) {
                        ChallengesScreen()
                    }
                }
            }
        }

        // Chatbot Overlay
        if (currentRoute != AppScreen.Splash.route) {
            ChatBotOverlay(bottomOffset = 135.dp)
        }
    }
}