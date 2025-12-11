package com.example.desde_mi_rincon_app_01.ui.components.forum

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.desde_mi_rincon_app_01.data.model.ForumPost
import com.example.desde_mi_rincon_app_01.ui.components.common.UserAvatar // (Ver paso 3)
import com.example.desde_mi_rincon_app_01.utils.getTimeAgo


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PostItem(
    post: ForumPost,
    currentUserId: String,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val textLengthLimit = 100 // ← CAMBIA ESTE NÚMERO

    // Memoizar cálculos costosos
    val isLiked by remember(post, currentUserId) {
        derivedStateOf { post.likedBy.contains(currentUserId) }
    }

    val likeCount by remember(post) {
        derivedStateOf { post.likedBy.size }
    }

    val isTextLong by remember(post) {
        derivedStateOf { post.message.length > textLengthLimit }
    }

    // Estado local con rememberSaveable para sobrevivir a recomposiciones
    var isExpanded by rememberSaveable(post.id) { mutableStateOf(false) }

    // Memoizar colores e iconos
    val likeColor = if (isLiked) Color(0xFFE11D48) else Color(0xFF94A3B8)
    val icon = if (isLiked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder

    // InteractionSource para el like (mejor manejo de estados)
    val likeInteractionSource = remember { MutableInteractionSource() }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .animateContentSize(
                animationSpec = tween(durationMillis = 300)
            )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp) // Reducir padding interno
                .fillMaxWidth()
        ) {
            // --- ENCABEZADO ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Avatar optimizado
                UserAvatar(
                    name = post.author,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
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

                // Like button optimizado
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(
                            interactionSource = likeInteractionSource,
                            indication = null,
                            onClick = onLikeClick
                        )
                        .padding(4.dp)
                ) {
                    AnimatedContent(
                        targetState = likeCount,
                        transitionSpec = {
                            slideInVertically { height -> height } +
                                    fadeIn() with
                                    slideOutVertically { height -> -height } +
                                    fadeOut()
                        },
                        label = "likeCountAnimation"
                    ) { targetCount ->
                        if (targetCount > 0) {
                            Text(
                                text = targetCount.toString(),
                                style = MaterialTheme.typography.labelMedium,
                                color = likeColor,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }
                    }

                    Icon(
                        imageVector = icon,
                        contentDescription = "Me gusta",
                        tint = likeColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- CONTENIDO ---
            Column {
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
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFF0D9488)
                        ),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = if (isExpanded) "Ver menos" else "Ver más...",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}