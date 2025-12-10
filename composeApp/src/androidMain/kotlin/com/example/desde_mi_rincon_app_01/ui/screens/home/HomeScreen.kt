package com.example.desde_mi_rincon_app_01.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.desde_mi_rincon_app_01.R
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    // Estado del scroll para efectos y navegación
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        state = scrollState,
        modifier = Modifier.fillMaxSize().background(Color(0xFFF8FAFC)) // Fondo Slate-50 más suave
    ) {
        // 1. SECCIÓN HERO (Con Animaciones)
        item {
            HeroSection(
                onEnterClick = {
                    // Scroll suave hacia la misión (ítem índice 1)
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(1)
                    }
                }
            )
        }

        // 2. SECCIÓN MISIÓN Y VISIÓN
        item {
            MissionVisionSection()
        }

        // Espacio final
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun HeroSection(onEnterClick: () -> Unit) {
    // Estado para disparar la animación al iniciar
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(550.dp) // Un poco más alto para mayor impacto
    ) {
        // A. Imagen de Fondo
        Image(
            painter = painterResource(id = R.drawable.home_hero),
            contentDescription = "Ambiente tranquilo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // B. Capa oscura (Scrim) con degradado mejorado
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.2f),
                            Color.Black.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.8f) // Más oscuro abajo para leer mejor
                        )
                    )
                )
        )

        // C. Contenido Animado
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animación del Título: Aparece desde abajo
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(1000)) +
                        slideInVertically(initialOffsetY = { 50 }, animationSpec = tween(1000))
            ) {
                Text(
                    text = "A veces, cuidar a otros\nagota tu propia luz.",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 44.sp // Mejor interlineado
                    ),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Animación del Subtítulo: Aparece un poco después (delay)
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 300)) +
                        slideInVertically(initialOffsetY = { 50 }, animationSpec = tween(1000, delayMillis = 300))
            ) {
                Text(
                    text = "Bienvenido a \"Desde mi rincón\". Tu espacio seguro para ser vulnerable, auténtico y encontrar fuerza.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp
                    ),
                    color = Color(0xFFE2E8F0), // Slate-200 para mejor contraste
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Animación del Botón
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 600)) +
                        slideInVertically(initialOffsetY = { 50 }, animationSpec = tween(1000, delayMillis = 600))
            ) {
                Button(
                    onClick = onEnterClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0D9488), // Teal-600
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 2.dp
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .height(56.dp)
                        .width(220.dp) // Ancho fijo para elegancia
                ) {
                    Text(
                        text = "Entra al rincón",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowDownward, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun MissionVisionSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .offset(y = (-40).dp) // Efecto visual: Las tarjetas "suben" sobre el hero
    ) {
        // --- TARJETA MISIÓN ---
        InfoCard(
            title = "Misión",
            icon = Icons.Outlined.Explore,
            colorTheme = Color(0xFF0D9488),
            backgroundColor = Color.White, // Blanco puro para resaltar sobre el fondo gris
            content = "Crear un espacio íntimo y auténtico donde las y los jóvenes puedan sentirse acompañados. Buscamos conectar desde la vulnerabilidad y la creatividad para construir una juventud más consciente."
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- TARJETA VISIÓN ---
        InfoCard(
            title = "Visión",
            icon = Icons.Default.Visibility,
            colorTheme = Color(0xFFD97706),
            backgroundColor = Color.White,
            content = "Ser un referente donde la autenticidad, la paz y el activismo se unan. Aspiramos a ser una comunidad que impulsa el crecimiento personal y promueve la no violencia desde el interior."
        )
    }
}

@Composable
fun InfoCard(
    title: String,
    icon: ImageVector,
    colorTheme: Color,
    backgroundColor: Color,
    content: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(24.dp), // Más redondeado
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Más sombra
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Barra lateral decorativa
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(80.dp) // Altura fija estética
                    .background(colorTheme, RoundedCornerShape(50))
            )

            Spacer(modifier = Modifier.width(20.dp))

            Column {
                // Encabezado con Icono
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = colorTheme.copy(alpha = 0.1f),
                        shape = CircleShape,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(icon, contentDescription = null, tint = colorTheme, modifier = Modifier.size(24.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = colorTheme,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 24.sp,
                        letterSpacing = 0.5.sp
                    ),
                    color = Color(0xFF475569), // Slate-600
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}