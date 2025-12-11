package com.example.desde_mi_rincon_app_01.ui.components.forum

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.desde_mi_rincon_app_01.data.model.ForumPost
import com.example.desde_mi_rincon_app_01.ui.components.common.UserAvatar // (Ver paso 3)


@Composable
fun PostItem(
    post: ForumPost,
    currentUserId: String,
    onLikeClick: () -> Unit
) {
    // Calculamos si este usuario ya dio like
    val isLiked = post.likedBy.contains(currentUserId)
    val likeCount = post.likedBy.size

    // Colores e iconos para el estado
    val likeColor = if (isLiked) Color(0xFFE11D48) else Color(0xFF94A3B8) // Rojo vs Gris
    val icon = if (isLiked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder

    // Lógica para "Ver más"
    var isExpanded by remember { mutableStateOf(false) }
    val textLengthLimit = 150 // Caracteres aproximados para considerar un texto "largo"
    val isTextLong = post.message.length > textLengthLimit

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            // Animación suave al expandir/colapsar la tarjeta
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // --- ROW 1: ENCABEZADO (Avatar + Info + LIKE) ---
            Row(verticalAlignment = Alignment.Top) {
                UserAvatar(name = post.author)

                Spacer(modifier = Modifier.width(12.dp))

                // Columna central (Nombre y fecha)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.author.ifBlank { "Anónimo" },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = "Hace un momento",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )
                }

                // --- ZONA DEL CORAZÓN (Movido aquí) ---
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    // Contador (solo visible si hay likes)
                    AnimatedVisibility(visible = likeCount > 0) {
                        Text(
                            text = likeCount.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            color = likeColor,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }

                    // Icono Clickable sin el padding extra del IconButton por defecto para que quede más compacto
                    Icon(
                        imageVector = icon,
                        contentDescription = "Me gusta",
                        tint = likeColor,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null // Quitamos el efecto ripple para que sea más limpio
                            ) { onLikeClick() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- ROW 2: MENSAJE CON "VER MÁS" ---
            Column {
                Text(
                    text = post.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF334155),
                    lineHeight = 20.sp,
                    // Si no está expandido y es largo, limitar a 3 líneas
                    maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 2.dp)
                )

                // Botón de "Ver más / Ver menos" solo si el texto es largo
                if (isTextLong) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isExpanded) "Ver menos" else "Ver más...",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF0D9488), // Color Teal (Acentuado)
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { isExpanded = !isExpanded }
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}