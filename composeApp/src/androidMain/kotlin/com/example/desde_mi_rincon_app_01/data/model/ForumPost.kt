package com.example.desde_mi_rincon_app_01.data.model

data class ForumPost(
    val id: String = "",
    val emotion: String = "",      // Ej: "Frustrado", "En Paz"
    val message: String = "",      // El contenido del texto
    val author: String = "",       // Nombre opcional o "Anónimo"
    val timestamp: Long = System.currentTimeMillis(), // Para ordenar por fecha
    val drawingUrl: String? = null // Lo dejamos nulo por ahora, para el futuro
)