package com.example.desde_mi_rincon_app_01.ui.screens.forum

// --- IMPORTS ---
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween // FIXED: Added import for tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.desde_mi_rincon_app_01.data.model.EmotionItem
import com.example.desde_mi_rincon_app_01.data.model.ForumPost
import com.example.desde_mi_rincon_app_01.ui.components.DrawingCanvas
import com.example.desde_mi_rincon_app_01.ui.components.common.EmotionCard
import com.example.desde_mi_rincon_app_01.ui.components.common.ModeSelector
import com.example.desde_mi_rincon_app_01.ui.components.common.PostItemSkeleton
import com.example.desde_mi_rincon_app_01.ui.components.forum.PostItem
import com.example.desde_mi_rincon_app_01.utils.emotionsList
import com.example.desde_mi_rincon_app_01.utils.getEmotionColor
import com.example.desde_mi_rincon_app_01.viewmodel.ForumViewModel
import kotlin.math.absoluteValue





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


// --- SCREEN 2: FEED ---
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ForumFeedScreen(
    onShareFeeling: () -> Unit,
    viewModel: ForumViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.listenToAllPosts()
    }

    val posts by viewModel.posts.collectAsState()
    val context = LocalContext.current
    val currentUserId = remember { viewModel.getUserId(context) }

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
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

        // --- OPTIMIZACIÓN SKELETON LOADING ---
        // En lugar de if/else con Box, usamos LazyColumn para ambos estados
        // para mantener la estructura visual.

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // CABECERA (Siempre visible)
            item {
                Text(
                    text = "Comunidad",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334155),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            if (posts.isEmpty()) {
                // ESTADO DE CARGA: Mostramos 6 esqueletos
                items(6) {
                    PostItemSkeleton()
                }
            } else {
                // ESTADO CON DATOS
                items(
                    items = posts,
                    key = { post -> post.id }
                ) { post ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            // Usa .animateItem() si tienes Compose 1.7+, sino .animateItemPlacement()
                            .animateItem()
                    ) {
                        PostItem(
                            post = post,
                            currentUserId = currentUserId,
                            onLikeClick = {
                                viewModel.toggleLike(post, currentUserId)
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




