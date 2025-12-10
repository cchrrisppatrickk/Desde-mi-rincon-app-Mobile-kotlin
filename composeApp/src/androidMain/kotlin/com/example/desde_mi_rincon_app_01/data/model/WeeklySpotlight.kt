package com.example.desde_mi_rincon_app_01.data.model

data class WeeklySpotlight(
    val id: String = "",
    val name: String = "",        // Ej: "Sofía M. - Voluntaria..."
    val photoUrl: String = "",    // URL de la foto (puedes usar una de prueba al inicio)
    val quote: String = "",       // La cita sobre su rincón seguro
    val message: String = "",     // El mensaje a los compañeros
    val timestamp: Long = System.currentTimeMillis() // Para saber cuál es el último
)