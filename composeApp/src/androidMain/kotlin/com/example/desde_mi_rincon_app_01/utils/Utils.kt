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

val randomNames = listOf(
    "Viajero Silencioso", "Alma Valiente", "Estrella Fugaz",
    "Caminante", "Luz de Luna", "Brote Nuevo", "Corazón Valiente",
    "Eco del Bosque", "Susurro del Viento", "Guardian del Alba",
    "Sombra Serena", "Río Tranquilo", "Fuego Calmo", "Nómada Sin Rostro",
    "Atardecer Lejano", "Piedra Ancestral", "Lluvia de Estrellas",
    "Espíritu Libre", "Mar de Cristal", "Verdad Oculta",
    "Relámpago Blanco", "Memoria Perdida", "Horizonte Eterno",
    "Raíz Profunda", "Vuelo Nocturno", "Esperanza Errante",
    "Canción Antigua", "Puente de Niebla", "Rumor de Hojas",
    "Silencio Hablado", "Rastro de Luz", "Flor de Escarcha",
    "Tiempo Suspendido", "Misterio Andante", "Reflejo del Agua",
    "Gema del Amanecer", "Latido del Tiempo", "Ceniza Voladora",
    "Suspiro de la Montaña", "Cazador de Silencios", "Ruta de Plata",
    "Pájaro de Obsidiana", "Voluntad de Hierba", "Espejo Roto",
    "Camino de Sal", "Lamento de Cristal", "Vigía del Ocaso",
    "Fragua de Sueños", "Huella de Sombra", "Barca del Vacío",
    "Perla del Norte", "Cicatriz del Viento", "Aldea Flotante",
    "Secreto de Arena", "Arpa Invisible", "Polvo de Luna",
    "Última Ola", "Jardín Suspendido", "Faro de Bruma",
    "Eterno Retorno", "Grito de Piedra", "Nido de Neblina",
    "Manto Estelar", "Sendero de Lágrimas", "Cáliz del Sur",
    "Réquiem del Desierto", "Alas de Granito", "Puerta del Eco",
    "Frontera de Nada", "Balada del Invierno", "Rey sin Reino",
    "Murmullo Subterráneo", "Savia de la Noche", "Trino de Acero",
    "Naufragio Celeste", "Luz que se Apaga", "Vereda de Musgo"
)