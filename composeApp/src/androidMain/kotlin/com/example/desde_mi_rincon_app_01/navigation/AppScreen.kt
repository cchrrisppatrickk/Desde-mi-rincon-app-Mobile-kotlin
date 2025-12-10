package com.example.desde_mi_rincon_app_01.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.* // Importar todo para facilitar
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppScreen(val route: String, val title: String, val icon: ImageVector) {
    object Home : AppScreen("home", "Inicio", Icons.Default.Home)

    // Esta sigue siendo la pantalla principal del Foro (Selección de emoción)
    object Forum : AppScreen("forum", "Foro", Icons.Default.Favorite)

    // NUEVA RUTA: Para ver los mensajes de la comunidad
    object CommunityFeed : AppScreen("forum_feed", "Comunidad", Icons.Default.List)

    // Ruta para escribir (Mantenemos la lógica de pasar la emoción)
    object ForumWrite : AppScreen("forum_write/{emotion}", "Escribir", Icons.Default.Create) {
        fun createRoute(emotion: String) = "forum_write/$emotion"
    }

    object Capsules : AppScreen("capsules", "Cápsulas", Icons.Default.PlayCircle)
    object Weekly : AppScreen("weekly", "Rincón", Icons.Default.Star)
    object Challenges : AppScreen("challenges", "Retos", Icons.Default.EmojiEvents)
}