// Archivo: App.kt
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.desde_mi_rincon_app_01.navigation.AppScreen
import com.example.desde_mi_rincon_app_01.ui.screens.home.HomeScreen

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
        NavHost(
            navController = navController,
            startDestination = AppScreen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppScreen.Home.route) { HomeScreen() } // <--- Llamada a tu nueva pantalla real}
            composable(AppScreen.Forum.route) { PlaceholderScreen("Foro: ¿Cómo está tu corazón?") }
            composable(AppScreen.Capsules.route) { PlaceholderScreen("Cápsulas de Bienestar") }
            composable(AppScreen.Weekly.route) { PlaceholderScreen("Rincón Semanal") }
            composable(AppScreen.Challenges.route) { PlaceholderScreen("Retos Culturales y Ruleta") }
        }
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