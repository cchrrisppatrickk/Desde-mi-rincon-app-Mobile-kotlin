package com.example.desde_mi_rincon_app_01.data.model

data class Post(
    val id: String = "",
    val autor: String = "Invitado", // Nombre que el usuario escribe (o default)
    val mensaje: String = "",
    val emocion: String = "Neutral", // Para filtrar por emociones después
    val fecha: Long = System.currentTimeMillis()
)