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
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
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

// --- COLORES TEMÁTICOS ---
private val AmberBackground = Color(0xFFFFFBEB)
private val AmberPrimary = Color(0xFFF59E0B)
private val AmberDark = Color(0xFF78350F)
private val SlateText = Color(0xFF475569)

@Composable
fun ChallengesScreen() {
    val scrollState = rememberScrollState()

    // DATOS (Sin cambios en lógica)
    val traditions = remember {
        listOf(
            Tradition("Egipto", "🇪🇬", "Maat: El antiguo concepto de verdad, equilibrio y justicia cósmica.", Color(0xFFFEF3C7)),
            Tradition("Grecia", "🇬🇷", "Xenia: La sagrada ley de la hospitalidad hacia el extranjero.", Color(0xFFCCFBF1)),
            Tradition("México", "🇲🇽", "Tequio: Trabajo comunitario no remunerado en beneficio del pueblo.", Color(0xFFFEE2E2)),
            Tradition("India", "🇮🇳", "Ahimsa: La no violencia hacia todos los seres vivos.", Color(0xFFE0E7FF)),
            Tradition("China", "🇨🇳", "Tao: El fluir natural de la vida, actuar sin forzar.", Color(0xFFF3E8FF)),
            Tradition("Perú", "🇵🇪", "Ayni: Reciprocidad andina, hoy por ti, mañana por mí.", Color(0xFFFFEDD5))
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AmberBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 24.dp) // Más aire en los bordes
    ) {
        // --- HEADER ---
        HeaderSection()

        Spacer(modifier = Modifier.height(40.dp))

        // --- RULETA (UI MEJORADA) ---
        RouletteSection(traditions)

        Spacer(modifier = Modifier.height(40.dp))

        // --- UPLOAD ---
        UploadSection()

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun HeaderSection() {
    Column {
        // Título con Icono destacado
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                color = AmberPrimary.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.EmojiEvents, null, tint = AmberPrimary, modifier = Modifier.size(28.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Reto Cultural",
                    style = MaterialTheme.typography.labelLarge,
                    color = AmberPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Alianzas",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = AmberDark
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tarjeta de Instrucciones
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Tu misión de hoy:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SlateText
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Únete con otro voluntario, giren la ruleta e investiguen una tradición antigua.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlateText.copy(alpha = 0.8f),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(20.dp))
                Divider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(20.dp))

                // Pasos visuales
                StepItem(1, "El Origen", "Compartan su motivación.")
                StepItem(2, "La Investigación", "Giren la ruleta.")
                StepItem(3, "La Reflexión", "Apliquen la sabiduría.")
                StepItem(4, "La Presentación", "¡Creen algo juntos!")
            }
        }
    }
}

@Composable
fun StepItem(number: Int, title: String, desc: String) {
    Row(modifier = Modifier.padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(AmberPrimary.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                color = AmberDark,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, color = AmberDark, style = MaterialTheme.typography.bodyMedium)
            Text(text = desc, style = MaterialTheme.typography.bodySmall, color = SlateText)
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
        // Título de sección centrado
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "✨ Sabiduría Antigua",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = AmberDark
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- CONTENEDOR DE LA RULETA (UI MEJORADA) ---
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(340.dp) // Espacio total
        ) {
            // Sombra decorativa detrás de la ruleta
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .offset(y = 10.dp)
                    .background(Color.Black.copy(alpha = 0.1f), CircleShape)
            )

            // 1. FLECHA INDICADORA (Ahora es más estilizada)
            Canvas(modifier = Modifier
                .size(50.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-15).dp)
                .zIndex(10f)
            ) {
                val path = Path().apply {
                    moveTo(size.width / 2, size.height) // Punta
                    lineTo(size.width * 0.2f, 0f)
                    lineTo(size.width * 0.8f, 0f)
                    close()
                }
                // Sombra de la flecha
                drawPath(path, Color.Black.copy(alpha = 0.2f))
                // Cuerpo de la flecha
                drawPath(path, Color(0xFF1F2937))
            }

            // 2. LA RULETA GIRATORIA
            Canvas(
                modifier = Modifier
                    .size(310.dp)
                    .rotate(rotation.value)
            ) {
                val radius = size.minDimension / 2
                val anglePerSlice = 360f / traditions.size

                // Borde exterior grueso (Marco de madera/dorado)
                drawCircle(
                    color = Color(0xFFFDE68A), // Amber-200
                    radius = radius + 10f
                )

                traditions.forEachIndexed { index, tradition ->
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

                    // Línea divisoria más sutil
                    drawArc(
                        color = Color.White.copy(alpha = 0.5f),
                        startAngle = startAngle,
                        sweepAngle = anglePerSlice,
                        useCenter = true,
                        style = Stroke(width = 2f),
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
                            textSize = 60f
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                        // Emoji bandera
                        drawText(tradition.flag, x, y + 20, paint)
                    }
                }
            }

            // 3. Botón Central (Ahora parece un botón físico)
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .shadow(8.dp, CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Color.White, Color(0xFFFDE68A))
                        ),
                        shape = CircleShape
                    )
                    .border(4.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("GIRA", fontWeight = FontWeight.Black, fontSize = 16.sp, color = AmberDark)
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // BOTÓN PRINCIPAL GRANDE
        Button(
            onClick = {
                scope.launch {
                    val randomAngle = (1800..3600).random().toFloat()
                    val targetRotation = rotation.value + randomAngle

                    rotation.animateTo(
                        targetValue = targetRotation,
                        animationSpec = tween(durationMillis = 4000, easing = FastOutSlowInEasing)
                    )

                    // LÓGICA ORIGINAL (NO CAMBIADA)
                    val normalizedAngle = targetRotation % 360
                    val angleFromTop = (360 - normalizedAngle) % 360
                    val index = (angleFromTop / 60).toInt() % traditions.size
                    result = traditions[index]
                    showDialog = true
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F2937)),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp).shadow(8.dp, RoundedCornerShape(16.dp))
        ) {
            Text("¡Probar Suerte!", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }

    // --- MODAL RESULTADO (ESTILO TARJETA PREMIUM) ---
    if (showDialog && result != null) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Bandera gigante con fondo circular
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(result!!.color.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = result!!.flag, fontSize = 64.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = result!!.country,
                        style = MaterialTheme.typography.headlineMedium,
                        color = AmberDark,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = "TRADICIÓN ANTIGUA",
                        style = MaterialTheme.typography.labelSmall,
                        color = AmberPrimary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = result!!.description,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = SlateText,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Caja de Misión estilizada
                    Surface(
                        color = AmberBackground,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AmberPrimary.copy(alpha = 0.3f))
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lightbulb, null, tint = AmberPrimary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Tu misión: Investigar y crear un recurso sobre esto.",
                                style = MaterialTheme.typography.bodySmall,
                                color = AmberDark,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary),
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("¡Acepto el reto!", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun UploadSection() {
    val context = LocalContext.current

    // Botón de subida minimalista pero claro
    OutlinedButton(
        onClick = {
            Toast.makeText(context, "¡Excelente trabajo! Su evidencia ha sido enviada.", Toast.LENGTH_LONG).show()
        },
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFE2E8F0)),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = SlateText)
    ) {
        Icon(Icons.Default.UploadFile, contentDescription = null, tint = SlateText)
        Spacer(modifier = Modifier.width(12.dp))
        Text("Ya tengo mi evidencia, subir ahora", fontWeight = FontWeight.SemiBold)
    }
}