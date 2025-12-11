package com.example.desde_mi_rincon_app_01.ui.components.common

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.desde_mi_rincon_app_01.data.model.EmotionItem
import kotlin.math.absoluteValue

@Composable
fun UserAvatar(name: String) {
    val initials = if (name.isNotBlank()) name.take(1).uppercase() else "?"
    val colorIndex = name.hashCode().absoluteValue % 5
    val avatarColors = listOf(Color(0xFFE0F2FE), Color(0xFFDCFCE7), Color(0xFFFAE8FF), Color(0xFFFEE2E2), Color(0xFFFEF3C7))
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(40.dp).clip(CircleShape).background(avatarColors[colorIndex])) {
        Text(text = initials, fontWeight = FontWeight.Bold, color = Color(0xFF475569), fontSize = 16.sp)
    }
}

@Composable
fun EmotionCard(emotion: EmotionItem, onClick: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = emotion.color), shape = RoundedCornerShape(16.dp), modifier = Modifier.height(120.dp).clickable { onClick() }, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
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
    Row(modifier = Modifier.background(Color(0xFFE2E8F0), RoundedCornerShape(50)).padding(4.dp)) {
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