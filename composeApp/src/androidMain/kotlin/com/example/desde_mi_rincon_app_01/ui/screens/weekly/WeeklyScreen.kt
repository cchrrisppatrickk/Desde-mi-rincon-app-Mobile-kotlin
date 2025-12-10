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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

import com.example.desde_mi_rincon_app_01.viewmodel.WeeklyViewModel

@Composable
fun WeeklyScreen(viewModel: WeeklyViewModel = viewModel()) {
    val spotlight by viewModel.spotlight.collectAsState()

    // Fondo degradado Teal Oscuro (replicando bg-teal-900)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF134E4A), Color(0xFF115E59)) // Teal 900 -> 800
                )
            )
    ) {
        if (spotlight != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Scroll si el texto es largo
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFEF08A), // Amarillo suave
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "El Rincón Compartido",
                    style = MaterialTheme.typography.headlineMedium,
                    fontFamily = MaterialTheme.typography.headlineMedium.fontFamily, // Idealmente Lora Serif
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF0FDFA) // Teal 50
                )
                Text(
                    text = "Invitada de la Semana",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF99F6E4) // Teal 200
                )

                Spacer(modifier = Modifier.height(32.dp))

                // FOTO CIRCULAR
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .border(4.dp, Color(0xFF14B8A6), CircleShape) // Borde Teal 500
                        .padding(4.dp) // Espacio entre borde y foto
                        .clip(CircleShape)
                        .background(Color.Gray) // Fondo mientras carga
                ) {
                    AsyncImage(
                        model = spotlight!!.photoUrl,
                        contentDescription = "Foto de invitado",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // NOMBRE
                Text(
                    text = spotlight!!.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // CITA 1 (El Rincón)
                Icon(Icons.Default.FormatQuote, contentDescription = null, tint = Color(0xFF5EEAD4))
                Text(
                    text = spotlight!!.quote,
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFFCCFBF1), // Teal 100
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // CAJA MENSAJE A COMPAÑEROS
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F766E).copy(alpha = 0.6f)), // Fondo semitransparente
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2DD4BF))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "MENSAJE A LA COMUNIDAD:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5EEAD4),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = spotlight!!.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            textAlign = TextAlign.Justify,
                            lineHeight = 24.sp
                        )
                    }
                }

                // Espacio final para no chocar con el navbar
                Spacer(modifier = Modifier.height(80.dp))
            }
        } else {
            // Cargando...
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}