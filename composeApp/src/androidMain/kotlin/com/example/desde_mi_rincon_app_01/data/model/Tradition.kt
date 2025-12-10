package com.example.desde_mi_rincon_app_01.data.model

import androidx.compose.ui.graphics.Color

data class Tradition(
    val country: String,
    val flag: String,       // Emoji de la bandera
    val description: String, // El texto de sabiduría (Maat, Xenia, etc.)
    val color: Color        // Color de fondo para la rebanada de la ruleta
)