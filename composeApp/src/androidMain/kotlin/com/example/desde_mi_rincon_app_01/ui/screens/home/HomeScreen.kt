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
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Videocam
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.desde_mi_rincon_app_01.R
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        state = scrollState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // 1. HERO (PORTADA)
        item {
            HeroSection(
                onEnterClick = {
                    coroutineScope.launch {
                        // Scroll suave hacia la sección de herramientas
                        scrollState.animateScrollToItem(1)
                    }
                }
            )
        }

        // 2. FRASE DEL DÍA
        item {
            DailyQuoteSection()
        }

        // 3. GUÍA DE HERRAMIENTAS
        item {
            AppFeaturesSection()
        }

        // 4. MISIÓN Y VISIÓN
        item {
            MissionVisionSection()
        }

        // Espacio final
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// --- COMPONENTES ---

@Composable
fun HeroSection(onEnterClick: () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { isVisible = true }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(550.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.home_hero),
            contentDescription = "Ambiente tranquilo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.2f),
                            Color.Black.copy(alpha = 0.5f),
                            Color.Black.copy(alpha = 0.9f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TÍTULO ANIMADO
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(1000)) +
                        slideInVertically(
                            initialOffsetY = { 50 }, // CORREGIDO: Uso explícito del nombre
                            animationSpec = tween(1000)
                        )
            ) {
                Text(
                    text = "A veces, cuidar a otros\nagota tu propia luz.",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 44.sp
                    ),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SUBTÍTULO ANIMADO
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 300)) +
                        slideInVertically(
                            initialOffsetY = { 50 }, // CORREGIDO
                            animationSpec = tween(1000, delayMillis = 300)
                        )
            ) {
                Text(
                    text = "Bienvenido a \"Desde mi rincón\". Tu espacio seguro para ser vulnerable, auténtico y encontrar fuerza.",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                    color = Color(0xFFE2E8F0),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // BOTÓN ANIMADO
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 600)) +
                        slideInVertically(
                            initialOffsetY = { 50 }, // CORREGIDO
                            animationSpec = tween(1000, delayMillis = 600)
                        )
            ) {
                Button(
                    onClick = onEnterClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0D9488),
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(8.dp),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .height(56.dp)
                        .width(220.dp)
                ) {
                    Text("Entra al rincón", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowDownward, contentDescription = null)
                }
            }
        }
    }
}

// --- NUEVO COMPONENTE: FRASE DEL DÍA ---
@Composable
fun DailyQuoteSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .offset(y = (-40).dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF9C3)),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.FormatQuote,
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier
                        .size(40.dp)
                        .graphicsLayer(rotationZ = 180f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Recordatorio de hoy",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFFB45309),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "No tienes que poder con todo tú solo. Pedir ayuda también es un acto de valentía.",
                        style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
                        color = Color(0xFF78350F)
                    )
                }
            }
        }
    }
}

// --- NUEVO COMPONENTE: CARACTERÍSTICAS DE LA APP ---
@Composable
fun AppFeaturesSection() {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 0.dp)) {
        Text(
            text = "Tus Herramientas",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF334155),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        FeatureCard(
            title = "Muro de Emociones",
            desc = "Valida lo que sientes. Lee y escribe mensajes anónimos según tu emoción actual.",
            icon = Icons.Default.Forum,
            color = Color(0xFF0D9488)
        )

        Spacer(modifier = Modifier.height(12.dp))

        FeatureCard(
            title = "Lienzo de Expresión",
            desc = "Cuando no hay palabras, dibuja. Arteterapia digital para liberar tensión.",
            icon = Icons.Default.Brush,
            color = Color(0xFFDB2777)
        )

        Spacer(modifier = Modifier.height(12.dp))

        FeatureCard(
            title = "Ruleta de Retos",
            desc = "¿Atascado? Gira la ruleta para recibir un pequeño reto cultural o de bienestar.",
            icon = Icons.Default.Psychology,
            color = Color(0xFFD97706)
        )

        Spacer(modifier = Modifier.height(12.dp))

        FeatureCard(
            title = "Cápsulas de Video",
            desc = "Contenido multimedia creado por la comunidad para inspirarte.",
            icon = Icons.Default.Videocam,
            color = Color(0xFF4F46E5)
        )
    }
}

@Composable
fun FeatureCard(title: String, desc: String, icon: ImageVector, color: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                Text(text = desc, style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
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
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFE2E8F0)))
        Spacer(modifier = Modifier.height(32.dp))

        InfoCard(
            title = "Misión",
            icon = Icons.Outlined.Explore,
            colorTheme = Color(0xFF0D9488),
            backgroundColor = Color.White,
            content = "Crear un espacio íntimo y auténtico donde las y los jóvenes puedan sentirse acompañados. Buscamos conectar desde la vulnerabilidad y la creatividad para construir una juventud más consciente."
        )

        Spacer(modifier = Modifier.height(24.dp))

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
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(80.dp)
                    .background(colorTheme, RoundedCornerShape(50))
            )

            Spacer(modifier = Modifier.width(20.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = colorTheme.copy(alpha = 0.1f),
                        shape = CircleShape,
                        modifier = Modifier
                            .size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(icon, null, tint = colorTheme, modifier = Modifier.size(24.dp))
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
                    color = Color(0xFF475569),
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}