package com.example.desde_mi_rincon_app_01.ui.components.common

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.desde_mi_rincon_app_01.data.model.EmotionItem
import kotlin.math.absoluteValue

//@Composable
//fun UserAvatar(
//    name: String,
//    modifier: Modifier = Modifier,
//    useGradient: Boolean = false
//) {
//    // Pares de colores complementarios
//    val colorPairs = listOf(
//        Pair(Color(0xFF0D9488), Color(0xFF14B8A6)), // Teal
//        Pair(Color(0xFF3B82F6), Color(0xFF60A5FA)), // Blue
//        Pair(Color(0xFF8B5CF6), Color(0xFFA78BFA)), // Violet
//        Pair(Color(0xFFEF4444), Color(0xFFF87171)), // Red
//        Pair(Color(0xFFF59E0B), Color(0xFFFBBF24)), // Amber
//        Pair(Color(0xFF10B981), Color(0xFF34D399)), // Emerald
//        Pair(Color(0xFFEC4899), Color(0xFFF472B6)), // Pink
//        Pair(Color(0xFF6366F1), Color(0xFF818CF8)), // Indigo
//        Pair(Color(0xFF8B5CF6), Color(0xFFC4B5FD)), // Light Violet
//        Pair(Color(0xFF059669), Color(0xFF10B981))  // Green
//    )
//
//    val colorPair by remember(name) {
//        derivedStateOf {
//            colorPairs[(name.hashCode() % colorPairs.size).absoluteValue]
//        }
//    }
//
//    Box(
//        modifier = modifier
//            .background(
//                brush = if (useGradient) {
//                    Brush.linearGradient(
//                        colors = listOf(colorPair.first, colorPair.second),
//                        start = Offset(0f, 0f),
//                        end = Offset.Infinite
//                    )
//                } else {
//                    SolidColor(colorPair.first)
//                },
//                shape = CircleShape
//            )
//            .clip(CircleShape),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
//            color = Color.White,
//            fontWeight = FontWeight.Bold
//        )
//    }
//}

//@Composable
//fun UserAvatar(name: String, modifier: Modifier) {
//    val initials = if (name.isNotBlank()) name.take(1).uppercase() else "?"
//    val colorIndex = name.hashCode().absoluteValue % 5
//    val avatarColors = listOf(Color(0xFFE0F2FE), Color(0xFFDCFCE7), Color(0xFFFAE8FF), Color(0xFFFEE2E2), Color(0xFFFEF3C7))
//    Box(contentAlignment = Alignment.Center, modifier = modifier
//        .clip(CircleShape)
//        .background(avatarColors[colorIndex])) {
//        Text(text = initials, fontWeight = FontWeight.Bold, color = Color(0xFF475569), fontSize = 16.sp)
//    }
//}

//////////////////////////////
//UserAvatar optimizado
@Composable
fun UserAvatar(
    name: String,
    modifier: Modifier = Modifier
) {
    val backgroundColor by remember(name) {
        derivedStateOf {
            // Lista de 15 colores bien contrastados
            val colors = listOf(
                Color(0xFF0D9488), // Teal
                Color(0xFF3B82F6), // Blue
                Color(0xFF8B5CF6), // Violet
                Color(0xFFEF4444), // Red
                Color(0xFFF59E0B), // Amber
                Color(0xFF10B981), // Emerald
                Color(0xFFEC4899), // Pink
                Color(0xFF6366F1), // Indigo
                Color(0xFF84CC16), // Lime
                Color(0xFF06B6D4), // Cyan
                Color(0xFF8B5CF6), // Purple
                Color(0xFFF97316), // Orange
                Color(0xFFA855F7), // Fuchsia
                Color(0xFF64748B), // Slate
                Color(0xFF14B8A6)  // Teal-500
            )
            colors[(name.hashCode() % colors.size).absoluteValue]
        }
    }

    // Determinar si el color es oscuro para usar texto blanco o negro
    val textColor = remember(backgroundColor) {
        val luminance = 0.299 * backgroundColor.red +
                0.587 * backgroundColor.green +
                0.114 * backgroundColor.blue
        if (luminance > 0.5) Color.Black else Color.White
    }

    Box(
        modifier = modifier
            .background(backgroundColor, CircleShape)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun EmotionCard(emotion: EmotionItem, onClick: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = emotion.color), shape = RoundedCornerShape(16.dp), modifier = Modifier
        .height(120.dp)
        .clickable { onClick() }, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = emotion.emoji, fontSize = 40.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = emotion.name, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
        }
    }
}

// Selector de modo (Escribir/Dibujar)
@Composable
fun ModeSelector(selectedMode: String, onModeSelected: (String) -> Unit, accentColor: Color) {
    Row(modifier = Modifier
        .background(Color(0xFFE2E8F0), RoundedCornerShape(50))
        .padding(4.dp)) {
        ModeButton("Escribir", Icons.Outlined.Edit, selectedMode == "text", { onModeSelected("text") }, accentColor, Modifier.weight(1f))
        Spacer(modifier = Modifier.width(4.dp))
        ModeButton("Dibujar", Icons.Outlined.Brush, selectedMode == "draw", { onModeSelected("draw") }, accentColor, Modifier.weight(1f))
    }
}

@Composable
fun ModeButton(text: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit, activeColor: Color, modifier: Modifier = Modifier) {
    val backgroundColor = if (isSelected) Color.White else Color.Transparent
    val contentColor = if (isSelected) activeColor else Color.Gray
    val elevation = if (isSelected) 2.dp else 0.dp
    Surface(onClick = onClick, shape = RoundedCornerShape(50), color = backgroundColor, shadowElevation = elevation, modifier = modifier) {
        Row(modifier = Modifier.padding(vertical = 10.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = contentColor, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, color = contentColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

// 1. EXTENSIÓN PARA EL EFECTO BRILLO (SHIMMER)
fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ), label = "shimmer_float"
    )

    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation, y = translateAnimation)
    )

    this.background(brush)
}

// 2. EL ITEM ESQUELETO (Simula la forma de tu PostItem)
@Composable
fun PostItemSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Cabecera (Avatar + Nombre)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .shimmerEffect() // Aplicamos el efecto
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(10.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Cuerpo del mensaje (Simulación de 3 líneas de texto)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect()
        )
    }
}


// 2. EL ITEM ESQUELETO (Simula la forma de tu PostItem Cápsulas de Video)
@Composable
fun CapsuleSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f) // Misma proporción que tu tarjeta real
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        // 1. Área del Thumbnail (Ocupa la mayor parte)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .shimmerEffect() // Efecto de brillo gris
        )

        // 2. Área de Información
        Column(modifier = Modifier.padding(12.dp)) {
            // Título falso
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Descripción falsa (2 líneas)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
        }
    }
}