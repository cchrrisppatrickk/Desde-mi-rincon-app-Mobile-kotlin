package com.example.desde_mi_rincon_app_01.utils

fun extractVideoId(url: String): String? {
    // Busca el patrón de ID de video en URLs de YouTube (cortas y largas)
    val regex = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*".toRegex()
    return regex.find(url)?.value
}