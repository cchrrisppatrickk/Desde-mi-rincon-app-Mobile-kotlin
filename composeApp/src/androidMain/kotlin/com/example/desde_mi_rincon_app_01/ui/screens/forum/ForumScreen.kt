package com.example.desde_mi_rincon_app_01.ui.screens.forum

// --- IMPORTS ---
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween // FIXED: Added import for tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow // FIXED: standard shadow import
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.desde_mi_rincon_app_01.data.model.ForumPost
import com.example.desde_mi_rincon_app_01.ui.components.DrawingCanvas
import com.example.desde_mi_rincon_app_01.viewmodel.ForumViewModel
import kotlin.math.absoluteValue

// --- DATA ---
data class EmotionItem(val name: String, val emoji: String, val color: Color)

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
    EmotionItem("Asombrado", "🤯", Color(0xFFFFF7ED))
)

// --- SCREEN 1: SELECTION ---
@Composable
fun ForumSelectionScreen(
    onEmotionSelected: (String) -> Unit,
    onGoToFeed: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¿Cómo está tu corazón hoy?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF334155),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(emotionsList) { emotion ->
                EmotionCard(emotion) { onEmotionSelected(emotion.name) }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onGoToFeed,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.List, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ver mensajes de la comunidad")
        }
    }
}

@Composable
fun EmotionCard(emotion: EmotionItem, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = emotion.color),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .height(120.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = emotion.emoji, fontSize = 40.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = emotion.name,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF475569)
            )
        }
    }
}

// --- SCREEN 2: FEED ---
@Composable
fun ForumFeedScreen(
    onShareFeeling: () -> Unit,
    viewModel: ForumViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.listenToAllPosts()
    }

    val posts by viewModel.posts.collectAsState()

    // Obtenemos el ID del usuario actual
    val context = LocalContext.current
    val currentUserId = remember { viewModel.getUserId(context) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onShareFeeling,
                containerColor = Color(0xFF0D9488),
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Create, null) },
                text = { Text("Comparte tu sentir") }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Text(
                text = "Comunidad",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF334155),
                modifier = Modifier.padding(24.dp)
            )

            if (posts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF0D9488))
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp, start = 16.dp, end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(posts) { post ->
                        PostItem(
                            post = post,
                            currentUserId = currentUserId, // Pasamos el ID
                            onLikeClick = {
                                viewModel.toggleLike(post, currentUserId) // Acción al hacer click
                            }
                        )
                    }
                }
            }
        }
    }
}

// --- SCREEN 3: WRITE (FORM) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumWriteScreen(
    emotionName: String,
    onBack: () -> Unit,
    viewModel: ForumViewModel = viewModel()
) {
    var messageText by remember { mutableStateOf("") }
    var authorName by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("text") }

    val status by viewModel.status.collectAsState()
    val showSuccessDialog by viewModel.showSuccessDialog.collectAsState()

    val emotionColor = emotionsList.find { it.name == emotionName }?.color ?: Color(0xFFF1F5F9)
    val accentColor = Color(0xFF0D9488)

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            icon = { Icon(Icons.Default.CheckCircle, null, tint = accentColor, modifier = Modifier.size(48.dp)) },
            title = { Text("¡Liberado!", fontWeight = FontWeight.Bold) },
            text = { Text("Tu mensaje ha sido entregado al universo (y a la comunidad). Gracias por soltarlo.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.dismissSuccessDialog()
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) { Text("Volver") }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp)
        )
    }

    Scaffold(
        containerColor = Color(0xFFF8FAFC), // FIXED: Changed backgroundColor to containerColor
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Espacio de Desahogo",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = emotionName,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ModeSelector(
                selectedMode = selectedTab,
                onModeSelected = { selectedTab = it },
                accentColor = accentColor
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(accentColor)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if(selectedTab == "text") "Escribe lo que sientes..." else "Dibuja tu emoción...",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedVisibility(
                        visible = selectedTab == "text",
                        enter = fadeIn(tween(300)),
                        exit = fadeOut(tween(300))
                    ) {
                        BasicTextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            textStyle = TextStyle(
                                fontSize = 18.sp,
                                color = Color(0xFF334155),
                                lineHeight = 28.sp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 200.dp),
                            decorationBox = { innerTextField ->
                                if (messageText.isEmpty()) {
                                    Text(
                                        text = "Hoy me siento...",
                                        color = Color.LightGray,
                                        fontSize = 18.sp
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }

                    if (selectedTab == "draw") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF1F5F9))
                        ) {
                            DrawingCanvas()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.LightGray)
                Spacer(modifier = Modifier.width(12.dp))
                BasicTextField(
                    value = authorName,
                    onValueChange = { authorName = it },
                    textStyle = TextStyle(fontSize = 16.sp, color = Color.DarkGray),
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField ->
                        if (authorName.isEmpty()) {
                            Text("Firma (Opcional)", color = Color.Gray)
                        }
                        innerTextField()
                    },
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val finalMsg = if(selectedTab == "draw") "(Ha compartido un dibujo)" else messageText
                    viewModel.sendPost(emotionName, finalMsg, authorName)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    // FIXED: Replaced custom shadow function with standard Modifier.shadow to avoid complexity errors
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Send, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Liberar Emoción",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            status?.let {
                if (it.contains("Error")) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = it, color = Color.Red, fontSize = 12.sp)
                }
            }
        }
    }
}

// --- COMPONENTS ---
@Composable
fun ModeSelector(
    selectedMode: String,
    onModeSelected: (String) -> Unit,
    accentColor: Color
) {
    Row(
        modifier = Modifier
            .background(Color(0xFFE2E8F0), RoundedCornerShape(50))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ModeButton(
            text = "Escribir",
            icon = Icons.Outlined.Edit,
            isSelected = selectedMode == "text",
            onClick = { onModeSelected("text") },
            activeColor = accentColor,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(4.dp))
        ModeButton(
            text = "Dibujar",
            icon = Icons.Outlined.Brush,
            isSelected = selectedMode == "draw",
            onClick = { onModeSelected("draw") },
            activeColor = accentColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ModeButton(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    activeColor: Color,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) Color.White else Color.Transparent
    val contentColor = if (isSelected) activeColor else Color.Gray
    val elevation = if (isSelected) 2.dp else 0.dp

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = backgroundColor,
        shadowElevation = elevation,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = contentColor,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun PostItem(
    post: ForumPost,
    currentUserId: String, // Recibimos ID usuario
    onLikeClick: () -> Unit // Recibimos la acción
) {
    // Calculamos si este usuario ya dio like
    val isLiked = post.likedBy.contains(currentUserId)
    val likeCount = post.likedBy.size

    // Colores para el estado
    val likeColor = if (isLiked) Color(0xFFE11D48) else Color(0xFF94A3B8) // Rojo vs Gris
    val icon = if (isLiked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                UserAvatar(name = post.author)
                Spacer(modifier = Modifier.width(12.dp))
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
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "Opciones",
                    tint = Color(0xFFCBD5E1)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = post.message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF334155),
                lineHeight = 20.sp,
                modifier = Modifier.padding(start = 2.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            // BOTÓN DE LIKE
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Contador (solo visible si hay likes)
                AnimatedVisibility(visible = likeCount > 0) {
                    Text(
                        text = likeCount.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        color = likeColor,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }

                IconButton(
                    onClick = onLikeClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    // Icono con animación de cambio
                    Icon(
                        imageVector = icon,
                        contentDescription = "Me gusta",
                        tint = likeColor
                    )
                }
            }

        }
    }
}

@Composable
fun UserAvatar(name: String) {
    val initials = if (name.isNotBlank()) name.take(1).uppercase() else "?"
    val colorIndex = name.hashCode().absoluteValue % 5
    val avatarColors = listOf(
        Color(0xFFE0F2FE),
        Color(0xFFDCFCE7),
        Color(0xFFFAE8FF),
        Color(0xFFFEE2E2),
        Color(0xFFFEF3C7)
    )
    val textColor = Color(0xFF475569)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(avatarColors[colorIndex])
    ) {
        Text(
            text = initials,
            fontWeight = FontWeight.Bold,
            color = textColor,
            fontSize = 16.sp
        )
    }
}

@Composable
fun EmotionChip(emotionName: String) {
    val baseColor = getEmotionColor(emotionName)
    Surface(
        color = baseColor,
        shape = RoundedCornerShape(50),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.2f))
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = emotionName,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF334155)
            )
        }
    }
}

fun getEmotionColor(emotionName: String): Color {
    return emotionsList.find { it.name == emotionName }?.color ?: Color(0xFFF1F5F9)
}

// Remove or comment out the problematic custom shadow extension function
// fun Modifier.shadow(...) // Removing this to avoid the 'toPx' error.