package com.example.desde_mi_rincon_app_01.ui.components.forum

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.desde_mi_rincon_app_01.data.model.ForumPost
import com.example.desde_mi_rincon_app_01.ui.components.common.UserAvatar
import com.example.desde_mi_rincon_app_01.utils.emotionsList
import com.example.desde_mi_rincon_app_01.utils.getTimeAgo

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PostItem(
    post: ForumPost,
    currentUserId: String,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textLengthLimit = 135

    // DATOS DE LA EMOCIÓN
    val emotionData = remember(post.emotion) {
        emotionsList.find { it.name == post.emotion }
    }
    val emotionColor = emotionData?.color ?: Color(0xFFF1F5F9)
    val emotionEmoji = emotionData?.emoji ?: "💭"
    val emotionName = emotionData?.name ?: post.emotion

    // MEMOIZACIÓN DE LIKES
    val isLiked by remember(post, currentUserId) {
        derivedStateOf { post.likedBy.contains(currentUserId) }
    }
    val likeCount by remember(post) {
        derivedStateOf { post.likedBy.size }
    }

    // MEMOIZACIÓN DE TEXTO
    val isTextLong by remember(post) {
        derivedStateOf { post.message.length > textLengthLimit }
    }

    // ESTADOS UI
    var isExpanded by rememberSaveable(post.id) { mutableStateOf(false) }

    // NUEVO: Estado para controlar si el modal de imagen pantalla completa está abierto
    var showImageDialog by rememberSaveable { mutableStateOf(false) }

    val likeColor = if (isLiked) Color(0xFFE11D48) else Color(0xFF94A3B8)
    val icon = if (isLiked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder
    val likeInteractionSource = remember { MutableInteractionSource() }

    // --- DIÁLOGO DE IMAGEN CON ESTADO DE CARGA ---
    if (showImageDialog && post.drawingUrl != null) {
        Dialog(
            onDismissRequest = { showImageDialog = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f))
                    .clickable { showImageDialog = false },
                contentAlignment = Alignment.Center
            ) {
                // Usamos SubcomposeAsyncImage para controlar el estado de carga
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(post.drawingUrl)
                        .crossfade(true) // Transición suave al aparecer
                        .build(),
                    contentDescription = "Dibujo expandido",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentScale = ContentScale.Fit,
                    // ESTO ES LO NUEVO:
                    loading = {
                        Box(
                            modifier = Modifier.size(50.dp), // Tamaño del área de carga
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        }
                    },
                    error = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Image, // O un icono de "broken image"
                                contentDescription = "Error",
                                tint = Color.Gray,
                                modifier = Modifier.size(48.dp)
                            )
                            Text("No se pudo cargar", color = Color.Gray)
                        }
                    }
                )

                // BOTÓN CERRAR (Igual que antes)
                IconButton(
                    onClick = { showImageDialog = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 48.dp, end = 24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }

    // --- TARJETA DEL POST ---
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .animateContentSize(animationSpec = tween(durationMillis = 300))
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            // --- ENCABEZADO (Igual que antes) ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                UserAvatar(name = post.author, modifier = Modifier.size(40.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.author.ifBlank { "Anónimo" },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = getTimeAgo(post.timestamp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF94A3B8)
                    )
                }
                Surface(
                    color = emotionColor,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = emotionEmoji, fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = emotionName,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF475569)
                        )
                    }
                }

                // Botón Like
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(interactionSource = likeInteractionSource, indication = null, onClick = onLikeClick)
                        .padding(4.dp)
                ) {
                    AnimatedContent(
                        targetState = likeCount,
                        transitionSpec = { slideInVertically { height -> height } + fadeIn() with slideOutVertically { height -> -height } + fadeOut() },
                        label = "likeCountAnimation"
                    ) { targetCount ->
                        if (targetCount > 0) {
                            Text(text = targetCount.toString(), style = MaterialTheme.typography.labelMedium, color = likeColor, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 4.dp))
                        }
                    }
                    Icon(imageVector = icon, contentDescription = "Me gusta", tint = likeColor, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- CONTENIDO TEXTO ---
            // Solo mostramos texto si no es el mensaje automático de dibujo O si queremos mostrarlo igual
            if (post.message != "(Ha compartido un dibujo)" && post.message.isNotBlank()) {
                Text(
                    text = post.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF334155),
                    lineHeight = 20.sp,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                if (isTextLong) {
                    TextButton(
                        onClick = { isExpanded = !isExpanded },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF0D9488)),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(text = if (isExpanded) "Ver menos" else "Ver más...", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // --- NUEVO: BOTÓN "VER IMAGEN" ---
            if (post.drawingUrl != null) {
                Spacer(modifier = Modifier.height(12.dp))

                // Botón Estilizado que abre el modal
                OutlinedButton(
                    onClick = { showImageDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF0D9488)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF0D9488)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ver Dibujo Adjunto", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}