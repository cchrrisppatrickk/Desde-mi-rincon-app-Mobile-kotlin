import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // Necesitarás la dependencia lifecycle-viewmodel-compose
import com.example.desde_mi_rincon_app_01.ForumViewModel

@Composable
fun App() {
    MaterialTheme {
        // Instanciamos el ViewModel
        val viewModel: ForumViewModel = viewModel()

        // Estados del formulario
        var nombre by remember { mutableStateOf("") }
        var mensaje by remember { mutableStateOf("") }
        var emocionSeleccionada by remember { mutableStateOf("Agotado") }
        var estadoEnvio by remember { mutableStateOf("") } // Para mostrar mensajes de éxito/error

        val listaEmociones = listOf("Agotado", "Esperanzado", "Triste", "En Paz", "Frustrado")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Foro Abierto",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF009688) // Un tono Teal como tu web
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Selector de Emociones
            Text("¿Cómo está tu corazón hoy?", style = MaterialTheme.typography.bodyMedium)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                items(listaEmociones) { emocion ->
                    EmocionChip(
                        texto = emocion,
                        seleccionado = emocion == emocionSeleccionada,
                        alClickar = { emocionSeleccionada = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Campo Nombre (Opcional)
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Tu nombre (Opcional)") },
                placeholder = { Text("Si lo dejas vacío serás 'Invitado'") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 3. Campo Mensaje
            OutlinedTextField(
                value = mensaje,
                onValueChange = { mensaje = it },
                label = { Text("Tu mensaje") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp), // Más alto para escribir
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Botón Enviar
            Button(
                onClick = {
                    if (mensaje.isNotBlank()) {
                        viewModel.enviarMensaje(
                            nombreUsuario = nombre,
                            mensaje = mensaje,
                            emocion = emocionSeleccionada,
                            onSuccess = {
                                estadoEnvio = "¡Mensaje enviado!"
                                mensaje = "" // Limpiar campo
                            },
                            onError = { error ->
                                estadoEnvio = "Error: $error"
                            }
                        )
                    } else {
                        estadoEnvio = "Por favor escribe un mensaje"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009688))
            ) {
                Text("Compartir en el Foro")
            }

            if (estadoEnvio.isNotEmpty()) {
                Text(
                    text = estadoEnvio,
                    color = if (estadoEnvio.contains("Error")) Color.Red else Color.Green,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

// Componente pequeño para el botón de emoción
@Composable
fun EmocionChip(texto: String, seleccionado: Boolean, alClickar: (String) -> Unit) {
    Surface(
        color = if (seleccionado) Color(0xFFB2DFDB) else Color.LightGray.copy(alpha = 0.3f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.clickable { alClickar(texto) }
    ) {
        Text(
            text = texto,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            color = if (seleccionado) Color(0xFF00695C) else Color.Black,
            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal
        )
    }
}