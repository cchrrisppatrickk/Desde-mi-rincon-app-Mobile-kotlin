// Archivo: navigation/AppScreens.kt
package com.example.desde_mi_rincon_app_01.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

// Definimos las 5 secciones basadas en tu HTML
sealed class AppScreen(val route: String, val title: String, val icon: ImageVector) {
    object Home : AppScreen("home", "Inicio", Icons.Default.Home)
    object Forum : AppScreen("forum", "Foro", Icons.Default.Favorite) // Corazón por las emociones

    object ForumWrite : AppScreen("forum_write/{emotion}", "Escribir", Icons.Default.Create) {
        fun createRoute(emotion: String) = "forum_write/$emotion"
    }
    object Capsules : AppScreen("capsules", "Cápsulas", Icons.Default.PlayCircle)
    object Weekly : AppScreen("weekly", "Rincón", Icons.Default.Star)
    object Challenges : AppScreen("challenges", "Retos", Icons.Default.EmojiEvents) // Trofeo para retos
}