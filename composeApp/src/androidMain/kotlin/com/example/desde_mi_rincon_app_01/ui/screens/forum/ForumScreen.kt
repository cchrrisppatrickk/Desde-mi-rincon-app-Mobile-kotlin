package com.example.desde_mi_rincon_app_01.ui.screens.forum

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.desde_mi_rincon_app_01.viewmodel.ForumViewModel

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import com.example.desde_mi_rincon_app_01.data.model.ForumPost
import com.example.desde_mi_rincon_app_01.ui.components.DrawingCanvas

// --- DATOS DE LAS EMOCIONES (Configuración visual) ---
data class EmotionItem(val name: String, val emoji: String, val color: Color)

val emotionsList = listOf(
    EmotionItem("Agotado", "😫", Color(0xFFE2E8F0)),     // Slate-200
    EmotionItem("Esperanzado", "🌻", Color(0xFFFEF9C3)), // Yellow-100
    EmotionItem("Frustrado", "😤", Color(0xFFFEE2E2)),   // Red-100
    EmotionItem("En Paz", "🕊️", Color(0xFFDBEAFE)),      // Blue-100
    EmotionItem("Triste", "🌧️", Color(0xFFE0E7FF)),      // Indigo-100
    // NUEVA EMOCIÓN AGREGADA
    EmotionItem("Confundido", "🌀", Color(0xFFF3E8FF)), // Purple-100
    // NUEVAS EMOCIONES
    EmotionItem("Eufórico", "🎉", Color(0xFFFCE7F3)),     // Pink-100
    EmotionItem("Nostálgico", "📜", Color(0xFFFEF3C7)),   // Amber-100
    EmotionItem("Determinado", "💪", Color(0xFFD1FAE5)),  // Emerald-100
    EmotionItem("Asombrado", "🤯", Color(0xFFFEF3C7))     // Yellow-100 (alternativa: 0xFFFFF7ED para Orange-50)

)

// --- PANTALLA 1: SELECCIÓN DE EMOCIÓN ---
@Composable
fun ForumSelectionScreen(
    onEmotionSelected: (String) -> Unit,
    onGoToFeed: () -> Unit // Nuevo callback
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

        // ... (Texto auxiliar) ...
        Spacer(modifier = Modifier.height(24.dp))

        // Grid de emociones (ocupa el peso disponible pero deja espacio al botón final)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f) // Importante para que el botón quede abajo
        ) {
            items(emotionsList) { emotion ->
                EmotionCard(emotion) { onEmotionSelected(emotion.name) }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- NUEVO BOTÓN: VER MENSAJES ---
        Button(
            onClick = onGoToFeed,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)), // Slate Dark
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
                color = Color(0xFF475569) // Slate-600
            )
        }
    }
}

// --- PANTALLA 2: FORMULARIO DE ESCRITURA ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumWriteScreen(
    emotionName: String,
    onBack: () -> Unit, // Esta función es la que nos devuelve a la pantalla anterior
    viewModel: ForumViewModel = viewModel()
) {
    // ELIMINADO: LaunchedEffect(Unit) { viewModel.listenToAllPosts() }

    // Estados del formulario
    var messageText by remember { mutableStateOf("") }
    var authorName by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("text") }

    // Observamos los estados del ViewModel
    val status by viewModel.status.collectAsState() // Para errores
    val showSuccessDialog by viewModel.showSuccessDialog.collectAsState() // Para éxito

    val emotionColor = emotionsList.find { it.name == emotionName }?.color ?: Color.White

    // --- LÓGICA DEL DIÁLOGO (POP-UP) ---
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                // Opcional: Qué pasa si tocan fuera (lo dejamos vacío para obligar a usar el botón)
            },
            icon = {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF0D9488))
            },
            title = {
                Text(text = "¡Emoción Liberada!", fontWeight = FontWeight.Bold)
            },
            text = {
                Text("Gracias por compartir tu sentir. Tu mensaje ha sido entregado a la comunidad y ahora pesa un poco menos.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.dismissSuccessDialog() // 1. Reseteamos el estado
                        onBack() // 2. REDIRECCIÓN: Volvemos a la pantalla anterior
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D9488))
                ) {
                    Text("Aceptar")
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
    // ------------------------------------

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Desahogo: $emotionName") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = emotionColor)
            )
        }
    ) { padding ->
        // Usamos Column con verticalScroll en lugar de LazyColumn porque ya no es una lista infinita
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()), // Habilita scroll si el teclado tapa
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Este es tu espacio seguro.",
                style = MaterialTheme.typography.titleMedium,
                color = Color.DarkGray
            )

            // TABS (Igual que antes)
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                TabButton("Escribir Mensaje", selectedTab == "text") { selectedTab = "text" }
                Spacer(modifier = Modifier.width(16.dp))
                TabButton("Dibujar", selectedTab == "draw") { selectedTab = "draw" }
            }

            // INPUT AREA (Igual que antes)
            if (selectedTab == "text") {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    label = { Text("Escribe tu mensaje aquí...") },
                    modifier = Modifier.fillMaxWidth().height(200.dp), // Un poco más alto ahora que hay espacio
                    shape = RoundedCornerShape(12.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    DrawingCanvas()
                }
            }

            // CAMPO NOMBRE (Igual)
            OutlinedTextField(
                value = authorName,
                onValueChange = { authorName = it },
                label = { Text("Tu nombre (Opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // BOTÓN ENVIAR
            Button(
                onClick = {
                    val finalMsg = if(selectedTab == "draw") "(Ha compartido un dibujo)" else messageText
                    viewModel.sendPost(emotionName, finalMsg, authorName)
                    // Nota: Ya no limpiamos el texto aquí manualmente,
                    // porque al salir de la pantalla se destruye el estado.
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D9488)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Send, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Liberar Emoción")
            }

            // SOLO MOSTRAR STATUS SI ES ERROR (Texto rojo)
            status?.let {
                Text(
                    text = it,
                    color = Color.Red, // Solo para errores
                    modifier = Modifier.padding(top=8.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // ELIMINADO: Todo el bloque de "Mensajes de la Comunidad"
        }
    }
}

@Composable
fun ForumFeedScreen(
    onShareFeeling: () -> Unit, // Callback para volver a los botones
    viewModel: ForumViewModel = viewModel()
) {
    // Cargamos todos los mensajes
    LaunchedEffect(Unit) {
        viewModel.listenToAllPosts()
    }

    val posts by viewModel.posts.collectAsState()

    Scaffold(
        floatingActionButton = {
            // Botón flotante para incitar a escribir
            ExtendedFloatingActionButton(
                onClick = onShareFeeling,
                containerColor = Color(0xFF0D9488), // Teal
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Create, null) },
                text = { Text("Comparte tu sentir") }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            // Header
            Text(
                text = "Comunidad",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF334155),
                modifier = Modifier.padding(24.dp)
            )

            // Lista de Mensajes
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
                        PostItem(post) // Usamos el componente que ya creaste
                    }
                }
            }
        }
    }
}

// Componente auxiliar para estilizar los botones de las pestañas
@Composable
fun TabButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(bottom = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color(0xFF0D9488) else Color.Gray
        )
        // La línea inferior animada (simulada)
        if (isSelected) {
            Box(
                modifier = Modifier
                    .height(2.dp)
                    .width(40.dp) // Ancho fijo o dinámico
                    .background(Color(0xFF0D9488))
            )
        } else {
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

// COMPONENTE PARA CADA MENSAJE (TARJETA)
@Composable
fun PostItem(post: ForumPost) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // --- NUEVO: Etiqueta de la Emoción ---
            Surface(
                color = getEmotionColor(post.emotion), // Función auxiliar abajo
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = post.emotion,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = Color.DarkGray
                )
            }
            // --------------------------------------

            // Contenido del mensaje
            Text(
                text = post.message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Pie de tarjeta: Autor y Fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = post.author,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D9488)
                )

                Text(
                    text = "Hace un momento", // Aquí podrías formatear post.timestamp
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}

// Función auxiliar simple para dar color al badge según el texto de la emoción
// Puedes ponerla al final de tu archivo ForumScreen.kt
fun getEmotionColor(emotionName: String): Color {
    return emotionsList.find { it.name == emotionName }?.color ?: Color(0xFFF1F5F9)
}