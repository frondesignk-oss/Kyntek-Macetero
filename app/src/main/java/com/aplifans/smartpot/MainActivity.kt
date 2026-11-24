package com.aplifans.smartpot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Forest = Color(0xFF8BCF9B)
private val Background = Color(0xFF101713)
private val Panel = Color(0xFF1B2820)
private val Muted = Color(0xFFA8B8AC)

data class Pot(val name: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Forest,
                    background = Background,
                    surface = Panel,
                    onPrimary = Color(0xFF102016),
                    onBackground = Color(0xFFF1F5F1),
                    onSurface = Color(0xFFF1F5F1)
                )
            ) {
                KyntekApp()
            }
        }
    }
}

@Composable
private fun KyntekApp() {
    val pots = remember { mutableStateListOf<Pot>() }
    var showAddDialog by remember { mutableStateOf(false) }
    var newPotName by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 22.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item {
                    Spacer(Modifier.height(30.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .background(Panel, CircleShape)
                                .padding(12.dp)
                        ) {
                            Icon(Icons.Default.Eco, contentDescription = null, tint = Forest)
                        }
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text("KYNTEK", fontSize = 12.sp, color = Forest, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                            Text("Macetero inteligente", fontSize = 21.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(Modifier.height(28.dp))
                    Text("Tu jardín", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Text("Controlá tus macetas desde un solo lugar", color = Muted, fontSize = 14.sp)
                }

                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Panel),
                        shape = RoundedCornerShape(22.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(20.dp)) {
                            Text("MIS MACETAS", color = Forest, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                            Spacer(Modifier.height(10.dp))
                            Text("${pots.size}", fontSize = 34.sp, fontWeight = FontWeight.Bold)
                            Text(if (pots.size == 1) "maceta registrada" else "macetas registradas", color = Muted, fontSize = 13.sp)
                        }
                    }
                }

                if (pots.isEmpty()) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Panel),
                            shape = RoundedCornerShape(22.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.WaterDrop, contentDescription = null, tint = Forest, modifier = Modifier.height(42.dp))
                                Spacer(Modifier.height(12.dp))
                                Text("Todavía no hay macetas", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    "Agregá tu primera maceta para empezar a organizar tu jardín inteligente.",
                                    color = Muted,
                                    fontSize = 13.sp
                                )
                                Spacer(Modifier.height(16.dp))
                                Button(onClick = { showAddDialog = true }) {
                                    Icon(Icons.Default.Add, contentDescription = null)
                                    Text(" Agregar maceta")
                                }
                            }
                        }
                    }
                } else {
                    items(pots.size) { index ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Panel),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(18.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Eco, contentDescription = null, tint = Forest)
                                Column(Modifier.padding(start = 14.dp)) {
                                    Text(pots[index].name, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
                                    Text("Pendiente de vincular dispositivo", color = Muted, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        "Versión inicial · Los datos todavía no provienen de sensores",
                        color = Muted,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(bottom = 90.dp)
                    )
                }
            }

            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Forest,
                contentColor = Color(0xFF102016),
                modifier = Modifier.align(Alignment.BottomEnd).padding(22.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar maceta")
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Nueva maceta") },
            text = {
                OutlinedTextField(
                    value = newPotName,
                    onValueChange = { newPotName = it },
                    label = { Text("Nombre (ej. Monstera)") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val name = newPotName.trim()
                        if (name.isNotEmpty()) {
                            pots.add(Pot(name))
                            newPotName = ""
                            showAddDialog = false
                        }
                    },
                    enabled = newPotName.isNotBlank()
                ) { Text("Agregar") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancelar") }
            }
        )
    }
}
