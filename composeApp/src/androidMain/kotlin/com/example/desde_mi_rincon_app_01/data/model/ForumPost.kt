package com.example.desde_mi_rincon_app_01.data.model


data class ForumPost(
    val id: String = "",
    val emotion: String = "",
    val message: String = "",
    val author: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val drawingUrl: String? = null,
    // NUEVO: Lista de IDs de dispositivos/usuarios que dieron like
    val likedBy: List<String> = emptyList()
)