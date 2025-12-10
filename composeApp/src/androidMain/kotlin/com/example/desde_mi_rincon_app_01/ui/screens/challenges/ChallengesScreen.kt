package com.example.desde_mi_rincon_app_01.ui.screens.challenges

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.example.desde_mi_rincon_app_01.data.model.Tradition
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ChallengesScreen() {
    val scrollState = rememberScrollState()

    // DATOS EXACTOS DE TU HTML (Main.html)
    val traditions = remember {
        listOf(
            Tradition("Egipto", "🇪🇬", "Maat: El antiguo concepto de verdad, equilibrio y justicia cósmica.", Color(0xFFFEF3C7)),
            Tradition("Grecia", "🇬🇷", "Xenia: La sagrada ley de la hospitalidad hacia el extranjero.", Color(0xFFCCFBF1)),
            Tradition("México", "🇲🇽", "Tequio: Trabajo comunitario no remunerado en beneficio del pueblo (Zapoteca).", Color(0xFFFEE2E2)),
            Tradition("India", "🇮🇳", "Ahimsa: La no violencia hacia todos los seres vivos.", Color(0xFFE0E7FF)),
            Tradition("China", "🇨🇳", "Tao: El fluir natural de la vida, actuar sin forzar (Wu Wei).", Color(0xFFF3E8FF)),
            Tradition("Perú", "🇵🇪", "Ayni: Reciprocidad andina, hoy por ti, mañana por mí.", Color(0xFFFFEDD5))
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBEB)) // Amber-50
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // --- HEADER ---
        HeaderSection()

        Spacer(modifier = Modifier.height(32.dp))

        // --- RULETA CORREGIDA ---
        RouletteSection(traditions)

        Spacer(modifier = Modifier.height(32.dp))

        // --- UPLOAD ---
        UploadSection()

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun HeaderSection() {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Reto Cultural: Alianzas",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF78350F) // Amber-900
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Únete con otro voluntario, giren la ruleta e investiguen una tradición antigua.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF92400E) // Amber-800
        )

        Spacer(modifier = Modifier.height(24.dp))

        // PASOS DEL RETO (Texto idéntico al HTML)
        StepItem(1, "El Origen", "Compartan qué los motivó a empezar el voluntariado.", Color(0xFFF59E0B))
        StepItem(2, "La Investigación", "Giren la ruleta e investiguen la tradición asignada.", Color(0xFFFBBF24))
        StepItem(3, "La Reflexión", "¿Cómo se aplica esa sabiduría al voluntariado actual?", Color(0xFFFCD34D))
        StepItem(4, "La Presentación", "Elijan formato: Vídeo, Artículo o Infografía.", Color(0xFFD97706))
    }
}

@Composable
fun StepItem(number: Int, title: String, desc: String, color: Color) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = number.toString(), color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, color = Color(0xFF78350F))
            Text(text = desc, style = MaterialTheme.typography.bodySmall, color = Color(0xFF92400E))
        }
    }
}

@Composable
fun RouletteSection(traditions: List<Tradition>) {
    val scope = rememberCoroutineScope()
    val rotation = remember { Animatable(0f) }
    var result by remember { mutableStateOf<Tradition?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sabiduría Antigua",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF78350F)
        )
        Text("Descubre tu raíz cultural", fontSize = 12.sp, color = Color(0xFF92400E))

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(320.dp) // Un poco más grande
        ) {
            // 1. FLECHA INDICADORA (DIBUJADA MANUALMENTE PARA MAYOR PRECISIÓN)
            // La colocamos arriba al centro, apuntando hacia abajo
            Canvas(modifier = Modifier
                .size(40.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-10).dp)
                .zIndex(10f) // Asegura que esté encima de la ruleta
            ) {
                val path = Path().apply {
                    moveTo(size.width / 2, size.height) // Punta abajo
                    lineTo(0f, 0f) // Esquina izq arriba
                    lineTo(size.width, 0f) // Esquina der arriba
                    close()
                }
                drawPath(path, Color(0xFF1F2937)) // Slate-800
            }

            // 2. LA RULETA GIRATORIA
            Canvas(
                modifier = Modifier
                    .size(300.dp)
                    .shadow(12.dp, CircleShape)
                    .rotate(rotation.value)
            ) {
                val radius = size.minDimension / 2
                // Cada rebanada mide 60 grados (360 / 6)
                val anglePerSlice = 360f / traditions.size

                traditions.forEachIndexed { index, tradition ->
                    // IMPORTANTE: Empezamos en -90 grados para que el índice 0 esté ARRIBA (las 12 del reloj)
                    // Esto alinea la lógica visual con la lógica matemática.
                    val startAngle = (index * anglePerSlice) - 90f

                    // Rebanada
                    drawArc(
                        color = tradition.color,
                        startAngle = startAngle,
                        sweepAngle = anglePerSlice,
                        useCenter = true,
                        size = Size(size.width, size.height),
                        topLeft = Offset(0f, 0f)
                    )

                    // Borde Blanco
                    drawArc(
                        color = Color.White,
                        startAngle = startAngle,
                        sweepAngle = anglePerSlice,
                        useCenter = true,
                        style = Stroke(width = 6f),
                        size = Size(size.width, size.height),
                        topLeft = Offset(0f, 0f)
                    )

                    // Texto y Bandera
                    val midAngleRad = (startAngle + anglePerSlice / 2) * (PI / 180f)
                    val textRadius = radius * 0.75f
                    val x = center.x + textRadius * cos(midAngleRad).toFloat()
                    val y = center.y + textRadius * sin(midAngleRad).toFloat()

                    drawContext.canvas.nativeCanvas.apply {
                        val paint = android.graphics.Paint().apply {
                            textSize = 64f
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                        // +24 para ajustar verticalmente el emoji
                        drawText(tradition.flag, x, y + 24, paint)
                    }
                }
            }

            // 3. Botón Central (Decorativo)
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(Color.White, CircleShape)
                    .border(6.dp, Color(0xFFFEF3C7), CircleShape) // Amber-100
                    .shadow(4.dp, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("GIRA", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF78350F))
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                scope.launch {
                    val randomAngle = (1800..3600).random().toFloat() // Entre 5 y 10 vueltas
                    val targetRotation = rotation.value + randomAngle

                    rotation.animateTo(
                        targetValue = targetRotation,
                        animationSpec = tween(durationMillis = 4000, easing = FastOutSlowInEasing)
                    )

                    // --- LÓGICA MATEMÁTICA CORREGIDA ---
                    // 1. Normalizamos el ángulo final a 0-360
                    val normalizedAngle = targetRotation % 360

                    // 2. Como la ruleta gira a la derecha (horario), el índice que queda ARRIBA
                    // se calcula restando el giro a 360.
                    // Ejemplo: Si giras 60 grados, el índice 0 se mueve a la derecha, y el índice 5 (el último) sube.
                    val angleFromTop = (360 - normalizedAngle) % 360

                    // 3. Dividimos por 60 grados para obtener el índice
                    val index = (angleFromTop / 60).toInt() % traditions.size

                    result = traditions[index]
                    showDialog = true
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F2937)),
            shape = RoundedCornerShape(50),
            modifier = Modifier.height(50.dp).padding(horizontal = 32.dp)
        ) {
            Text("¡Girar Ruleta!", fontSize = 16.sp)
        }
    }

    // --- MODAL DE RESULTADO ENRIQUECIDO ---
    if (showDialog && result != null) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = result!!.flag, fontSize = 80.sp)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = result!!.country,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF78350F), // Amber-900
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = result!!.description,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF475569) // Slate-600
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Caja de "Tu Misión" (Igual que en HTML)
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFFBEB), RoundedCornerShape(12.dp)) // Amber-50
                            .border(1.dp, Color(0xFFFCD34D), RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "Tu misión:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                            Text(
                                text = "Investiga esta sabiduría antigua y crea un recurso para compartirlo.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF78350F)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)), // Amber-500
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("¡Acepto el reto!", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun UploadSection() {
    val context = LocalContext.current

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Button(
                onClick = {
                    Toast.makeText(context, "¡Excelente trabajo! Su evidencia ha sido enviada.", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)), // Slate-800
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.UploadFile, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Subir Trabajo Compartido", color = Color.White)
            }
        }
    }
}