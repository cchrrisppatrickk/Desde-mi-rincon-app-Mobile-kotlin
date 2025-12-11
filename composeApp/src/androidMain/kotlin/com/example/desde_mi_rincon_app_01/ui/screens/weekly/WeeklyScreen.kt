package com.example.desde_mi_rincon_app_01.ui.screens.weekly

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.desde_mi_rincon_app_01.viewmodel.WeeklyViewModel

// Colores específicos para esta pantalla (Atmósfera Nocturna/Íntima)
private val DeepTeal = Color(0xFF0F172A) // Slate 900 (Casi negro)
private val RichTeal = Color(0xFF115E59) // Teal 800
private val AccentGold = Color(0xFFFCD34D) // Amber 300
private val TextWhite = Color(0xFFF1F5F9) // Slate 100
private val TextMuted = Color(0xFF94A3B8) // Slate 400

@Composable
fun WeeklyScreen(viewModel: WeeklyViewModel = viewModel()) {
    val spotlight by viewModel.spotlight.collectAsState()

    // 1. FONDO CON GRADIENTE RADIAL (Efecto Spotlight)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(RichTeal, DeepTeal),
                    center = androidx.compose.ui.geometry.Offset.Unspecified,
                    radius = 1500f
                )
            )
    ) {
        if (spotlight != null) {
            // Elemento Decorativo de Fondo (Comilla Gigante)
            Icon(
                imageVector = Icons.Default.FormatQuote,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 40.dp, y = 100.dp)
                    .size(200.dp)
                    .alpha(0.05f) // Muy sutil
                    .scale(scaleX = -1f, scaleY = 1f) // Invertida
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- CABECERA ---
                Text(
                    text = "INVITADA SEMANAL",
                    style = MaterialTheme.typography.labelMedium,
                    color = AccentGold,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "El Rincón Compartido",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = FontFamily.Serif, // Toque editorial
                        fontWeight = FontWeight.Medium
                    ),
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(40.dp))

                // --- FOTO DE PERFIL "HERO" ---
                Box(contentAlignment = Alignment.Center) {
                    // Anillo decorativo sutil detrás
                    Box(
                        modifier = Modifier
                            .size(170.dp)
                            .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape)
                    )

                    // Imagen Principal
                    AsyncImage(
                        model = spotlight!!.photoUrl,
                        contentDescription = spotlight!!.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .border(3.dp, Color.White.copy(alpha = 0.8f), CircleShape)
                    )

                    // Pequeña estrella flotante
                    Surface(
                        color = AccentGold,
                        shape = CircleShape,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = (-10).dp, y = (-10).dp)
                            .size(32.dp),
                        shadowElevation = 4.dp
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = null,
                            tint = DeepTeal,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- NOMBRE ---
                Text(
                    text = spotlight!!.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- CITA (Feature principal) ---
                Text(
                    text = "\"${spotlight!!.quote}\"",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 32.sp
                    ),
                    color = Color(0xFFCCFBF1), // Teal muy claro
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // --- TARJETA DE MENSAJE (Estilo Glassmorphism Minimalista) ---
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White.copy(alpha = 0.08f), // Fondo translúcido
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Divider(
                            color = AccentGold,
                            modifier = Modifier
                                .width(24.dp)
                                .height(2.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "MENSAJE A LA COMUNIDAD",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = spotlight!!.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextWhite.copy(alpha = 0.9f),
                        lineHeight = 26.sp,
                        textAlign = TextAlign.Left // Alineación izquierda es más legible en bloques
                    )
                }

                // Espacio inferior para navegación
                Spacer(modifier = Modifier.height(80.dp))
            }
        } else {
            // Loader elegante
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AccentGold)
            }
        }
    }
}