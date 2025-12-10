// Archivo: App.kt
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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

    // Lista de pantallas para la barra inferior
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
                NavigationBar(
                    containerColor = Color.White, // Fondo blanco como en el Navbar web
                    contentColor = Color(0xFF0D9488) // El color Teal-600 de tu CSS
                ) {
                    // Obtenemos la ruta actual para saber cuál ícono pintar
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    screens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title, style = MaterialTheme.typography.labelSmall) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF0D9488), // Teal seleccionado
                                selectedTextColor = Color(0xFF0D9488),
                                indicatorColor = Color(0xFFCCFBF1), // Teal-100 para el fondo del ícono activo
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            ),
                            onClick = {
                                navController.navigate(screen.route) {
                                    // Lógica para evitar múltiples copias de la misma pantalla en la pila
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            // Aquí ocurre la "magia" de cambiar pantallas
            // En App.kt

            NavHost(
                navController = navController,
                startDestination = AppScreen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                // --- ESTO ES LO QUE FALTABA ---
                composable(AppScreen.Home.route) {
                    HomeScreen() // Asegúrate de importar tu HomeScreen creado anteriormente
                }
                // -----------------------------

                // 1. PANTALLA DE SELECCIÓN (FORO)
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

                // 2. NUEVA PANTALLA: FEED DE COMUNIDAD
                composable(AppScreen.CommunityFeed.route) {
                    ForumFeedScreen(
                        onShareFeeling = {
                            // Volvemos atrás (a la selección) para que elija emoción
                            navController.popBackStack()
                        }
                    )
                }

                // 3. PANTALLA DE ESCRITURA (FORMULARIO)
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

                composable(AppScreen.Capsules.route) {
                    CapsulesScreen()
                }
                composable(AppScreen.Weekly.route) {
                    WeeklyScreen()
                }

                composable(AppScreen.Challenges.route) {
                    ChallengesScreen()
                }
            }
        }

        // El Chatbot va FUERA del Scaffold, pero DENTRO del Box
        ChatBotOverlay()

    }


}

// Pantalla temporal para ver que la navegación funciona
@Composable
fun PlaceholderScreen(text: String) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(text = text, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A)) // Slate-900
    }
}