package com.example.desde_mi_rincon_app_01.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.desde_mi_rincon_app_01.R

@Composable
fun HomeScreen() {
    // LazyColumn permite hacer scroll verticalmente si el contenido es largo
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        // 1. SECCIÓN HERO (El Gancho)
        item {
            HeroSection()
        }

        // 2. SECCIÓN MISIÓN Y VISIÓN
        item {
            MissionVisionSection()
        }

        // Espacio extra al final para que no choque con la barra de navegación
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun HeroSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp) // Altura considerable para impactar
    ) {
        // A. La Imagen de Fondo (Local en drawable)
        Image(
            painter = painterResource(id = R.drawable.home_hero), // Asegúrate de tener la imagen aquí
            contentDescription = "Persona en un rincón con luz tenue",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // B. Capa oscura (Scrim) para que el texto se lea bien
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.3f), Color.Black.copy(alpha = 0.7f))
                    )
                )
        )

        // C. El Texto y Botón
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "A veces, cuidar a otros\nagota tu propia luz.",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = MaterialTheme.typography.headlineLarge.fontFamily, // Aquí iría tu fuente Lora si la configuras
                    fontWeight = FontWeight.Bold,
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = Color.Black,
                        blurRadius = 8f
                    )
                ),
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Bienvenido a \"Desde mi rincón\". Tu espacio seguro para ser vulnerable, auténtico y encontrar fuerza.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /* Acción para bajar o ir a Misión */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D9488)), // Teal-600
                shape = RoundedCornerShape(50),
                modifier = Modifier.height(50.dp)
            ) {
                Text(text = "Entra al rincón", color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = Color.White)
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
        // --- TARJETA MISIÓN ---
        InfoCard(
            title = "Misión",
            icon = Icons.Outlined.Explore, // Icono similar a Compass
            colorTheme = Color(0xFF0D9488), // Teal
            backgroundColor = Color(0xFFF0FDFA), // Teal-50
            content = "Desde mi rincón tiene como misión crear un espacio íntimo y auténtico donde las y los jóvenes puedan sentirse acompañados, escuchados e inspirados, a través de reflexiones sinceras, mensajes de amor propio, cultura de paz y experiencias del día a día, buscamos conectar desde la vulnerabilidad y la creatividad."
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- TARJETA VISIÓN ---
        InfoCard(
            title = "Visión",
            icon = Icons.Default.Visibility, // Icono de Ojo
            colorTheme = Color(0xFFD97706), // Amber-600
            backgroundColor = Color(0xFFFFFBEB), // Amber-50
            content = "Ser un referente juvenil en redes sociales donde la autenticidad, la paz, la expresión emocional y el activismo se unan para inspirar cambios positivos. \"Desde mi rincón\" aspira a convertirse en una comunidad latinoamericana que impulsa el crecimiento personal y promueve la no violencia."
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
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // CORRECCIÓN AQUÍ:
        Row(
            modifier = Modifier.padding(20.dp),
            // En vez de crossAxisAlignment, usamos verticalAlignment
            verticalAlignment = Alignment.Top
        ) {
            // Borde izquierdo de color
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(100.dp)
                    .background(colorTheme, RoundedCornerShape(2.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                // Icono y Título
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(colorTheme.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, contentDescription = null, tint = colorTheme)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = colorTheme,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Contenido de texto
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF475569), // Slate-600
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}