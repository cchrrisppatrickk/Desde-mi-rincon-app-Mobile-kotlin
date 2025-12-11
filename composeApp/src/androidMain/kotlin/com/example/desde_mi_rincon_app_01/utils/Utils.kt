package com.example.desde_mi_rincon_app_01.utils

fun getTimeAgo(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60_000 -> "Hace un momento" // Menos de 1 minuto
        diff < 3600_000 -> "Hace ${diff / 60_000} min" // Minutos
        diff < 86400_000 -> "Hace ${diff / 3600_000} h" // Horas
        diff < 172800_000 -> "Ayer" // Menos de 48 horas (aprox)
        else -> "Hace ${diff / 86400_000} días" // Días
    }
}
