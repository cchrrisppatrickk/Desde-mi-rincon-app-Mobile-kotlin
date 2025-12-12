package com.example.desde_mi_rincon_app_01.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Colores personalizados
private val TealPrimary = Color(0xFF0D9488)
private val SlateLight = Color(0xFFF8FAFC)
private val SlateDark = Color(0xFF1E293B)

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@Composable
fun ChatBotOverlay(
    bottomOffset: Dp = 100.dp
) {
    var isOpen by remember { mutableStateOf(false) }

    // Estado de los mensajes
    val messages = remember { mutableStateListOf(
        ChatMessage("¡Hola! Soy Brote 🌱. ¿En qué te puedo orientar hoy?", false)
    )}
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Lógica del cerebro de Brote
    fun getBotResponse(userText: String): String {
        val lower = userText.lowercase()
        return when {
            lower.contains("hola") -> "¡Hola! ¿Cómo está tu corazón hoy?"
            lower.contains("foro") -> "El foro es un espacio seguro. Toca el ícono del ❤️ abajo para entrar."
            lower.contains("triste") || lower.contains("ayuda") -> "Siento que te sientas así. Recuerda que no estás solo/a. En la sección de Cápsulas hay ejercicios de respiración."
            else -> "Aún estoy aprendiendo, pero si necesitas ayuda urgente, escribe a desdemirincon@gmail.com"
        }
    }

    fun sendMessage() {
        if (inputText.isBlank()) return
        messages.add(ChatMessage(inputText, true))
        val userMsg = inputText
        inputText = ""

        scope.launch {
            listState.animateScrollToItem(messages.size - 1)
            delay(1000)
            messages.add(ChatMessage(getBotResponse(userMsg), false))
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // --- UI ESTRUCTURA ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            // El padding asegura que el chat no choque con la barra de navegación
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = bottomOffset),
        contentAlignment = Alignment.BottomEnd
    ) {

        // 1. VENTANA DEL CHAT (Se muestra solo si isOpen es true)
        AnimatedVisibility(
            visible = isOpen,
            enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut(),
            modifier = Modifier.padding(bottom = 80.dp) // Espacio para no tapar el botón flotante
        ) {
            Surface(
                modifier = Modifier
                    .width(320.dp)
                    .height(450.dp),
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 12.dp,
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    // HEADER
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(TealPrimary, Color(0xFF14B8A6))
                                )
                            )
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("🌱", fontSize = 16.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Brote", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                            Text("Asistente Virtual", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                        }
                        IconButton(onClick = { isOpen = false }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                        }
                    }

                    // LISTA DE MENSAJES
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .background(SlateLight)
                            .padding(horizontal = 12.dp),
                        contentPadding = PaddingValues(vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(messages) { msg ->
                            ChatBubble(msg)
                        }
                    }

                    // ÁREA DE INPUT
                    Surface(
                        shadowElevation = 8.dp,
                        color = Color.White
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = inputText,
                                onValueChange = { inputText = it },
                                placeholder = { Text("Escribe aquí...", color = Color.Gray, fontSize = 13.sp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = SlateLight,
                                    unfocusedContainerColor = SlateLight,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(25.dp),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                                keyboardActions = KeyboardActions(onSend = { sendMessage() })
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            FilledIconButton(
                                onClick = { sendMessage() },
                                colors = IconButtonDefaults.filledIconButtonColors(containerColor = TealPrimary),
                                modifier = Modifier.size(46.dp)
                            ) {
                                Icon(Icons.Default.Send, contentDescription = "Enviar", modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }

        // 2. BOTÓN FLOTANTE (FAB) CON LA PLANTITA
        FloatingActionButton(
            onClick = { isOpen = !isOpen },
            containerColor = if(isOpen) Color.White else TealPrimary,
            contentColor = if(isOpen) TealPrimary else Color.White,
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            shape = CircleShape,
            modifier = Modifier.size(60.dp)
        ) {
            AnimatedContent(targetState = isOpen, label = "fab_anim") { open ->
                if (open) {
                    // Si está abierto, mostramos la X para cerrar
                    Icon(Icons.Default.Close, contentDescription = "Cerrar")
                } else {
                    // Si está cerrado, mostramos a Brote (la plantita)
                    Text("🌱", fontSize = 28.sp)
                }
            }
        }
    }
}

@Composable
fun ChatBubble(msg: ChatMessage) {
    val align = if (msg.isUser) Alignment.End else Alignment.Start
    val bgColor = if (msg.isUser) TealPrimary else Color.White
    val textColor = if (msg.isUser) Color.White else SlateDark

    val shape = if (msg.isUser) {
        RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
    }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = align) {
        Surface(
            color = bgColor,
            shape = shape,
            shadowElevation = if(msg.isUser) 2.dp else 1.dp,
            modifier = Modifier.widthIn(max = 260.dp)
        ) {
            Text(
                text = msg.text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                color = textColor,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}