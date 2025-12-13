package com.example.desde_mi_rincon_app_01.data.model
import androidx.annotation.Keep

@Keep
data class VideoCapsule(
    val id: String = "",
    val title: String = "",       // Ej: "Soltar para avanzar"
    val description: String = "", // Ej: "Un mensaje corto sobre cómo dejar ir..."
    val category: String = "",    // Ej: "Reflexión", "Microcuento", "Amor Propio"
    val videoUrl: String = "",    // Ej: "https://www.youtube.com/watch?v=XYZ"
    val duration: String = ""     // Ej: "3 min" (Opcional)
)