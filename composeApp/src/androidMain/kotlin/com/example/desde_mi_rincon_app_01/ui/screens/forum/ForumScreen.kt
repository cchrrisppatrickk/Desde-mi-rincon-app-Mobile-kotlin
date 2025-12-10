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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
    EmotionItem("Triste", "🌧️", Color(0xFFE0E7FF))       // Indigo-100
)

// --- PANTALLA 1: SELECCIÓN DE EMOCIÓN ---
@Composable
fun ForumSelectionScreen(onEmotionSelected: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¿Cómo está tu corazón hoy?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF334155), // Slate-700
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Selecciona una emoción para entrar al foro.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Grid de Cajetillas
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 columnas
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(emotionsList) { emotion ->
                EmotionCard(emotion) {
                    onEmotionSelected(emotion.name)
                }
            }
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
@OptIn(ExperimentalMaterial3Api::class) // <--- AGREGA ESTA LÍNEA AQUÍ
@Composable
fun ForumWriteScreen(
    emotionName: String,
    onBack: () -> Unit,
    viewModel: ForumViewModel = viewModel()
) {
    // 1. Cargamos los posts al entrar a la pantalla
    //Update:
    // MODIFICACIÓN: Cambiamos 'emotionName' por 'Unit' para que cargue solo una vez
    // y llamamos a la nueva función sin parámetros.
    LaunchedEffect(Unit) {
        viewModel.listenToAllPosts()
    }

    // Estados del formulario
    var messageText by remember { mutableStateOf("") }
    var authorName by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("text") }

    val status by viewModel.status.collectAsState()
    val posts by viewModel.posts.collectAsState() // <--- Lista de mensajes en vivo

    val emotionColor = emotionsList.find { it.name == emotionName }?.color ?: Color.White

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Foro: $emotionName") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = emotionColor)
            )
        }
    ) { padding ->
        // USAMOS LAZYCOLUMN PARA TODA LA PANTALLA
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(24.dp), // Margen general
            verticalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre elementos
        ) {

            // --- BLOQUE 1: EL FORMULARIO (Header) ---
            item {
                Text(
                    text = "Comparte tu sentir con la comunidad",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(16.dp))

                // TABS
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    TabButton("Escribir Mensaje", selectedTab == "text") { selectedTab = "text" }
                    Spacer(modifier = Modifier.width(16.dp))
                    TabButton("Dibujar", selectedTab == "draw") { selectedTab = "draw" }
                }

                // INPUT AREA
                if (selectedTab == "text") {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        label = { Text("Escribe tu mensaje aquí...") },
                        modifier = Modifier.fillMaxWidth().height(150.dp),
                        shape = RoundedCornerShape(12.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        DrawingCanvas() // Tu componente optimizado
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // CAMPO NOMBRE
                OutlinedTextField(
                    value = authorName,
                    onValueChange = { authorName = it },
                    label = { Text("Tu nombre (Opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // BOTÓN ENVIAR
                Button(
                    onClick = {
                        val finalMsg = if(selectedTab == "draw") "(Ha compartido un dibujo)" else messageText
                        viewModel.sendPost(emotionName, finalMsg, authorName)
                        messageText = "" // Limpiamos el campo tras enviar
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D9488)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Compartir")
                }

                status?.let {
                    Text(text = it, color = Color(0xFF0D9488), modifier = Modifier.padding(top=8.dp))
                }

                // SEPARADOR VISUAL
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Mensajes de la Comunidad",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334155)
                )
            }

            // --- BLOQUE 2: LA LISTA DE MENSAJES (Feed) ---
            if (posts.isEmpty()) {
                item {
                    Text(
                        text = "Sé el primero en compartir algo aquí...",
                        fontStyle = FontStyle.Italic,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            } else {
                items(posts) { post ->
                    PostItem(post)
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