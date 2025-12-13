package com.example.desde_mi_rincon_app_01.utils

import androidx.compose.ui.graphics.Color
import com.example.desde_mi_rincon_app_01.data.model.EmotionItem
import androidx.annotation.Keep

@Keep
val emotionsList = listOf(
    EmotionItem("Agotado", "😫", Color(0xFFE2E8F0)),
    EmotionItem("Esperanzado", "🌻", Color(0xFFFEF9C3)),
    EmotionItem("Frustrado", "😤", Color(0xFFFEE2E2)),
    EmotionItem("En Paz", "🕊️", Color(0xFFDBEAFE)),
    EmotionItem("Triste", "🌧️", Color(0xFFE0E7FF)),
    EmotionItem("Confundido", "🌀", Color(0xFFF3E8FF)),
    EmotionItem("Eufórico", "🎉", Color(0xFFFCE7F3)),
    EmotionItem("Nostálgico", "📜", Color(0xFFFEF3C7)),
    EmotionItem("Determinado", "💪", Color(0xFFD1FAE5)),
    EmotionItem("Asombrado", "🤯", Color(0xFFFFF7ED)),
            // --- NUEVAS EMOCIONES ---
    EmotionItem("Ansioso", "😰", Color(0xFFFEEBC8)),   // Naranja pálido (Alerta suave)
    EmotionItem("Agradecido", "🙏", Color(0xFFF0FDFA)), // Teal muy suave (Calma/Gratitud)
    EmotionItem("Enojado", "😡", Color(0xFFFFD1D1)),    // Rojo suave (Diferente a frustrado)
    EmotionItem("Inspirado", "✨", Color(0xFFECFCCB)),   // Lima suave (Creatividad/Idea)
    EmotionItem("Solo", "🥀", Color(0xFFF1F5F9)),       // Gris frío (Soledad/Melancolía)
    EmotionItem("Valiente", "🦁", Color(0xFFFFE4C4))    // Beige/Dorado (Fortaleza)
)

fun getEmotionColor(emotionName: String): Color {
    return emotionsList.find { it.name == emotionName }?.color ?: Color(0xFFF1F5F9)
}