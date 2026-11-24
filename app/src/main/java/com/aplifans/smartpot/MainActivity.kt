package com.aplifans.smartpot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Green = Color(0xFF8DD6A5)
private val Background = Color(0xFF0D1512)
private val Panel = Color(0xFF19251F)
private val PanelSoft = Color(0xFF26362D)
private val Muted = Color(0xFFA6B7AC)
private val Blue = Color(0xFF54B9FF)

private data class PlantGuide(val name: String, val range: String, val advice: String)
private val plantGuides = listOf(
    PlantGuide("Monstera", "35–60%", "Dejá secar la capa superior antes de volver a regar."),
    PlantGuide("Potus", "30–55%", "Tolera períodos algo más secos; evitá el sustrato empapado."),
    PlantGuide("Sansevieria", "15–35%", "Necesita secados marcados entre riegos. Regá con moderación."),
    PlantGuide("Cactus y suculentas", "10–25%", "Usá sustrato muy drenante y esperá a que se seque bien."),
    PlantGuide("Helechos", "45–70%", "Prefieren humedad más constante, sin agua acumulada."),
    PlantGuide("Calathea", "45–65%", "Mantené humedad moderada y estable; evitá encharcar."),
    PlantGuide("Lirio de paz", "40–65%", "Regá cuando el sustrato empiece a perder humedad."),
    PlantGuide("Ficus", "30–55%", "Dejá secar parcialmente el sustrato entre riegos.")
)
private data class Pot(val id: Int, val name: String, val humidity: Int, val battery: Int)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(
                primary = Green, background = Background, surface = Panel,
                onPrimary = Color(0xFF102016), onBackground = Color(0xFFF1F5F1),
                onSurface = Color(0xFFF1F5F1)
            )) { KyntekApp() }
        }
    }
}

@Composable
private fun KyntekApp() {
    val pots = remember { mutableStateListOf(
        Pot(1, "Monstera", 48, 86),
        Pot(2, "Sansevieria", 24, 93)
    ) }
    var showAdd by remember { mutableStateOf(false) }
    var showGuide by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }

    Surface(Modifier.fillMaxSize(), color = Background) {
        Box(Modifier.fillMaxSize()) {
            LazyColumn(
                Modifier.fillMaxSize().padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(Modifier.height(24.dp))
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            modifier = Modifier.size(width = 104.dp, height = 76.dp),
                            shape = RoundedCornerShape(13.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Image(
                                painterResource(R.drawable.kyntek_logo),
                                contentDescription = "Logo Kyntek",
                                modifier = Modifier.fillMaxSize().padding(3.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Column(Modifier.weight(1f).padding(start = 13.dp)) {
                            Text("KYNTEK", color = Green, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                            Text("MACETERO INTELIGENTE", color = Muted, fontSize = 10.sp, letterSpacing = 1.sp)
                            Spacer(Modifier.height(5.dp))
                            Text("Tu jardín, conectado", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                        IconButton(onClick = { showGuide = true }) {
                            Icon(Icons.Default.Book, "Guía de plantas", tint = Green, modifier = Modifier.size(27.dp))
                        }
                    }
                    Spacer(Modifier.height(26.dp))
                    Text("Mis macetas", fontSize = 27.sp, fontWeight = FontWeight.Bold)
                    Text("Estado general de tus plantas", color = Muted, fontSize = 13.sp)
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SummaryCard("MACETAS", pots.size.toString(), "registradas", Modifier.weight(1f))
                        SummaryCard("DISPOSITIVOS", "Demo", "modo visual", Modifier.weight(1f))
                    }
                }
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Tus plantas", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                        TextButton(onClick = { showGuide = true }) {
                            Icon(Icons.Default.Book, null, modifier = Modifier.size(17.dp))
                            Text(" Guía")
                        }
                    }
                }
                items(pots, key = { it.id }) { pot -> PotCard(pot) }
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = PanelSoft),
                        shape = RoundedCornerShape(17.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WaterDrop, null, tint = Blue)
                            Column(Modifier.padding(start = 10.dp)) {
                                Text("Lecturas de demostración", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text("Los porcentajes son ficticios hasta vincular el ESP32.", color = Muted, fontSize = 11.sp)
                            }
                        }
                    }
                    Spacer(Modifier.height(86.dp))
                }
            }
            FloatingActionButton(
                onClick = { showAdd = true },
                containerColor = Green, contentColor = Color(0xFF102016),
                modifier = Modifier.align(Alignment.BottomEnd).padding(22.dp)
            ) { Icon(Icons.Default.Add, "Agregar maceta") }
        }
    }

    if (showAdd) {
        AlertDialog(
            onDismissRequest = { showAdd = false },
            title = { Text("Nueva maceta") },
            text = {
                OutlinedTextField(
                    value = newName, onValueChange = { newName = it },
                    label = { Text("Nombre de la planta") }, singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    enabled = newName.isNotBlank(),
                    onClick = {
                        val id = (pots.maxOfOrNull { it.id } ?: 0) + 1
                        val humidity = listOf(42, 56, 31, 68, 47)[(id - 1) % 5]
                        val battery = listOf(91, 84, 76, 98, 88)[(id - 1) % 5]
                        pots.add(Pot(id, newName.trim(), humidity, battery))
                        newName = ""
                        showAdd = false
                    }
                ) { Text("Agregar") }
            },
            dismissButton = { TextButton(onClick = { showAdd = false }) { Text("Cancelar") } }
        )
    }

    if (showGuide) {
        AlertDialog(
            onDismissRequest = { showGuide = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Book, null, tint = Green)
                    Text("  Guía de humedad")
                }
            },
            text = {
                LazyColumn(Modifier.height(430.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                    item {
                        Text(
                            "Referencias orientativas del indicador Kyntek. No son porcentajes universales de agua en el suelo.",
                            color = Muted, fontSize = 12.sp
                        )
                    }
                    items(plantGuides) { plant ->
                        Column(
                            Modifier.fillMaxWidth().background(PanelSoft, RoundedCornerShape(12.dp)).padding(11.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(plant.name, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                                Text(plant.range, color = Green, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(plant.advice, color = Muted, fontSize = 11.sp)
                        }
                    }
                    item {
                        Text(
                            "Importante: el sensor capacitivo debe calibrarse con el sustrato de cada maceta. La mezcla, compactación y ubicación de la sonda pueden cambiar la lectura.",
                            color = Muted, fontSize = 11.sp
                        )
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showGuide = false }) { Text("Cerrar") } }
        )
    }
}

@Composable
private fun SummaryCard(title: String, value: String, detail: String, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text(title, color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(Modifier.height(7.dp))
            Text(value, fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Text(detail, color = Muted, fontSize = 11.sp)
        }
    }
}

@Composable
private fun PotCard(pot: Pot) {
    val color = when {
        pot.humidity < 30 -> Color(0xFFFFC36A)
        pot.humidity > 70 -> Blue
        else -> Green
    }
    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(17.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(42.dp).background(PanelSoft, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Eco, null, tint = Green)
                }
                Column(Modifier.weight(1f).padding(start = 12.dp)) {
                    Text(pot.name, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                    Text("Dispositivo de demostración", color = Muted, fontSize = 11.sp)
                }
                Text("DEMO", color = Blue, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(17.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.WaterDrop, null, tint = color, modifier = Modifier.size(19.dp))
                Text(" Humedad del sustrato", color = Muted, fontSize = 12.sp, modifier = Modifier.weight(1f))
                Text("${pot.humidity}%", color = color, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            Box(Modifier.fillMaxWidth().height(8.dp).background(PanelSoft, CircleShape)) {
                Box(Modifier.fillMaxWidth(pot.humidity.coerceIn(0, 100) / 100f).height(8.dp).background(color, CircleShape))
            }
            Spacer(Modifier.height(15.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.BatteryFull, null, tint = Green, modifier = Modifier.size(17.dp))
                Text(" Batería", color = Muted, fontSize = 12.sp, modifier = Modifier.weight(1f))
                Text("${pot.battery}%", color = Green, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
        }
    }
}
