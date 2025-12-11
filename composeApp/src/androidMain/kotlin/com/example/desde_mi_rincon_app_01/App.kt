// Archivo: App.kt
import androidx.compose.foundation.layout.Box
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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.desde_mi_rincon_app_01.navigation.AppScreen
import com.example.desde_mi_rincon_app_01.ui.components.ChatBotOverlay
import com.example.desde_mi_rincon_app_01.ui.screens.capsules.CapsulesScreen
import com.example.desde_mi_rincon_app_01.ui.screens.challenges.ChallengesScreen
import com.example.desde_mi_rincon_app_01.ui.screens.forum.ForumFeedScreen
import com.example.desde_mi_rincon_app_01.ui.screens.forum.ForumSelectionScreen
import com.example.desde_mi_rincon_app_01.ui.screens.forum.ForumWriteScreen
import com.example.desde_mi_rincon_app_01.ui.screens.home.HomeScreen
import com.example.desde_mi_rincon_app_01.ui.screens.weekly.WeeklyScreen

@Composable
fun App() {
    val navController = rememberNavController()
    val screens = listOf(
        AppScreen.Home,
        AppScreen.Forum,
        AppScreen.Capsules,
        AppScreen.Weekly,
        AppScreen.Challenges
    )

    // BOX RAÍZ: Contiene la App completa y el Chatbot flotando encima
    Box(modifier = Modifier.fillMaxSize()) {

        Scaffold(
            // Barra de navegación minimalista
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp, // Sombra sutil hacia arriba
                    modifier = Modifier.shadow(16.dp) // Sombra extra para efecto flotante
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    screens.forEach { screen ->
                        val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title,
                                    modifier = Modifier.size(26.dp)
                                )
                            },
                            // Minimalismo: Ocultamos el texto si no está seleccionado (opcional)
                            // O simplemente lo dejamos pequeño. Aquí lo dejamos siempre visible pero sutil.
                            label = {
                                Text(
                                    screen.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if(isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal
                                )
                            },
                            selected = isSelected,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF0D9488), // Teal Primary
                                selectedTextColor = Color(0xFF0D9488),
                                indicatorColor = Color(0xFFE0F2F1),    // Teal muy suave
                                unselectedIconColor = Color(0xFF94A3B8), // Slate-400
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
        ) { innerPadding ->

            // CONTENEDOR DE PANTALLAS
            NavHost(
                navController = navController,
                startDestination = AppScreen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(AppScreen.Home.route) { HomeScreen() }

                // --- FLUJO FORO ---
                composable(AppScreen.Forum.route) {
                    ForumSelectionScreen(
                        onEmotionSelected = { emotion ->
                            navController.navigate(AppScreen.ForumWrite.createRoute(emotion))
                        },
                        onGoToFeed = {
                            navController.navigate(AppScreen.CommunityFeed.route)
                        }
                    )
                }

                composable(AppScreen.CommunityFeed.route) {
                    ForumFeedScreen(
                        onShareFeeling = {
                            navController.navigate(AppScreen.Forum.route) {
                                popUpTo(AppScreen.CommunityFeed.route) { saveState = true }
                            }
                        }
                    )
                }

                // CORRECCIÓN: Eliminado el bloque duplicado, dejado solo este con argumentos
                composable(
                    route = AppScreen.ForumWrite.route,
                    arguments = listOf(navArgument("emotion") { type = NavType.StringType })
                ) { backStackEntry ->
                    val emotion = backStackEntry.arguments?.getString("emotion") ?: "General"
                    ForumWriteScreen(
                        emotionName = emotion,
                        onBack = { navController.popBackStack() }
                    )
                }
                // ------------------

                composable(AppScreen.Capsules.route) { CapsulesScreen() }
                composable(AppScreen.Weekly.route) { WeeklyScreen() }
                composable(AppScreen.Challenges.route) { ChallengesScreen() }
            }
        }

        // EL CHATBOT FLOTA SOBRE EL SCAFFOLD
        // Gracias al z-index natural del Box, este componente se dibuja AL FINAL, quedando encima.
        ChatBotOverlay(bottomOffset = 135.dp)
    }
}