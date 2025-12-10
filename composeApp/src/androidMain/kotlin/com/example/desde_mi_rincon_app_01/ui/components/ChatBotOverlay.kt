package com.example.desde_mi_rincon_app_01.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.desde_mi_rincon_app_01.R // Asegúrate de importar tu R para los iconos si usas drawables

// Modelo de datos simple para los mensajes
data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@Composable
fun ChatBotOverlay() {
    var isOpen by remember { mutableStateOf(false) }

    // Estado de los mensajes (Inicia con el saludo de Brote)
    val messages = remember { mutableStateListOf(
        ChatMessage("¡Hola! Soy Brote 🌱. Estoy aquí para acompañarte en el rincón. ¿En qué te puedo orientar?", false)
    )}

    // Control del texto y scroll
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // --- LÓGICA DEL CEREBRO DE BROTE ---
    fun getBotResponse(userText: String): String {
        val lower = userText.lowercase()
        return when {
            lower.contains("hola") -> "¡Hola! ¿En qué puedo ayudarte hoy?"
            lower.contains("foro") || lower.contains("escribir") -> "El foro está en la sección de 'Foro Abierto' (el icono del corazón). Elige una emoción y comparte tu sentir."
            lower.contains("video") || lower.contains("subir") || lower.contains("capsula") -> "Puedes ver y subir videos en la sección 'Cápsulas' (el icono de Play). Usa el botón '+' para subir el tuyo."
            lower.contains("reto") || lower.contains("ruleta") -> "Los retos culturales están en la última pestaña (la copa). ¡Gira la ruleta y aprende!"
            // --- LA RESPUESTA DE FALLBACK QUE PEDISTE ---
            else -> "No estoy seguro de entender esa función, pero si no entiendes la función de la página puedes contactarte con desdemirincon@gmail.com"
        }
    }

    fun sendMessage() {
        if (inputText.isBlank()) return

        // 1. Agregar mensaje del usuario
        messages.add(ChatMessage(inputText, true))
        val userMsg = inputText
        inputText = ""

        // 2. Simular "Escribiendo..." y responder
        scope.launch {
            // Scroll al fondo
            listState.animateScrollToItem(messages.size - 1)

            delay(800) // Pequeño retraso para naturalidad

            val response = getBotResponse(userMsg)
            messages.add(ChatMessage(response, false))

            // Scroll al fondo de nuevo
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // --- INTERFAZ UI ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Margen general de la pantalla
        contentAlignment = Alignment.BottomEnd // Ubicado abajo a la derecha
    ) {

        // 1. LA VENTANA DEL CHAT (Se muestra solo si isOpen es true)
        AnimatedVisibility(
            visible = isOpen,
            enter = slideInVertically { it }, // Entra desde abajo
            exit = slideOutVertically { it },
            modifier = Modifier.padding(bottom = 80.dp) // Espacio para no tapar el botón flotante
        ) {
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .height(400.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCCFBF1)) // Teal-100
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Encabezado Verde
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF0D9488)) // Teal-600
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🌱", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Brote", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        IconButton(
                            onClick = { isOpen = false },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                        }
                    }

                    // Lista de Mensajes
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF8FAFC)) // Slate-50
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(messages) { msg ->
                            ChatBubble(msg)
                        }
                    }

                    // Input (Campo de texto)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text("Escribe tu duda...", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(20.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(onSend = { sendMessage() })
                        )
                        IconButton(onClick = { sendMessage() }) {
                            Icon(Icons.Default.Send, contentDescription = "Enviar", tint = Color(0xFF0D9488))
                        }
                    }
                }
            }
        }

        // 2. EL BOTÓN FLOTANTE (FAB)
        FloatingActionButton(
            onClick = { isOpen = !isOpen },
            containerColor = Color.White,
            contentColor = Color(0xFF0D9488),
            shape = CircleShape,
            modifier = Modifier
                .size(64.dp)
                .border(2.dp, Color(0xFF0D9488), CircleShape)
        ) {
            // Icono: Si está abierto muestra X, si no, muestra la planta o un ícono de chat
            if (isOpen) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar chat")
            } else {
                // Puedes usar un icono de hoja o chat. Aquí uso texto emoji por simplicidad
                Text("🌱", fontSize = 28.sp)
            }
        }
    }
}

// Componente visual de la burbuja de chat
@Composable
fun ChatBubble(msg: ChatMessage) {
    val align = if (msg.isUser) Alignment.End else Alignment.Start
    val bgColor = if (msg.isUser) Color(0xFF0D9488) else Color.White
    val textColor = if (msg.isUser) Color.White else Color(0xFF334155)
    val shape = if (msg.isUser) {
        RoundedCornerShape(topStart = 12.dp, topEnd = 0.dp, bottomStart = 12.dp, bottomEnd = 12.dp)
    } else {
        RoundedCornerShape(topStart = 0.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp)
    }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = align) {
        Row(verticalAlignment = Alignment.Top) {
            if (!msg.isUser) {
                Text("🌱", modifier = Modifier.padding(end = 4.dp, top = 4.dp))
            }
            Surface(
                color = bgColor,
                shape = shape,
                shadowElevation = 1.dp,
                border = if(!msg.isUser) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)) else null
            ) {
                Text(
                    text = msg.text,
                    modifier = Modifier.padding(10.dp),
                    color = textColor,
                    fontSize = 14.sp
                )
            }
        }
    }
}