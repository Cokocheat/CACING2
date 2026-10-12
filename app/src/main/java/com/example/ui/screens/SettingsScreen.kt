package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ControlMode
import com.example.data.UserGameData

@Composable
fun SettingsScreen(
    userData: UserGameData,
    onSetControlMode: (ControlMode) -> Unit,
    onToggleSound: (Boolean) -> Unit,
    onToggleVibration: (Boolean) -> Unit,
    onUpdateName: (String) -> Unit,
    onUpdateJoystick: (offsetX: Int, offsetY: Int, scale: Float) -> Unit,
    onUpdateBoost: (offsetX: Int, offsetY: Int, scale: Float) -> Unit,
    onResetControls: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var nameInput by remember(userData.playerName) { mutableStateOf(userData.playerName) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF10131F))
            .testTag("settings_screen")
    ) {
        // TOP APP BAR
        Surface(
            color = Color(0xFF1A1F33),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3454)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 8.dp, start = 12.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("settings_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White
                    )
                }
                Text(
                    text = "PENGATURAN & KUSTOMISASI",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 320.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // JOYSTICK & BOOST POSITION CUSTOMIZER (The requested feature!)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF1A1F33),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF4ADE80)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Tune, contentDescription = null, tint = Color(0xFF4ADE80), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "EDIT POSISI JOYSTICK & BOOST",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color(0xFF4ADE80),
                                    fontWeight = FontWeight.Black
                                )
                            }
                            OutlinedButton(
                                onClick = onResetControls,
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF9100))
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reset Posisi", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Text(
                            text = "Sesuaikan jarak kontrol agar nyaman dijangkau jempol HP kamu:",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFA0AEC0),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Joystick Left Spacing
                        Text(
                            text = "Jarak Joystick dari Kiri Layar: ${userData.joystickOffsetX} dp",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                        Slider(
                            value = userData.joystickOffsetX.toFloat(),
                            onValueChange = { onUpdateJoystick(it.toInt(), userData.joystickOffsetY, userData.joystickScale) },
                            valueRange = 10f..120f,
                            colors = SliderDefaults.colors(thumbColor = Color(0xFF4ADE80), activeTrackColor = Color(0xFF4ADE80))
                        )

                        // Joystick Bottom Spacing
                        Text(
                            text = "Jarak Joystick dari Bawah Layar: ${userData.joystickOffsetY} dp",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                        Slider(
                            value = userData.joystickOffsetY.toFloat(),
                            onValueChange = { onUpdateJoystick(userData.joystickOffsetX, it.toInt(), userData.joystickScale) },
                            valueRange = 10f..120f,
                            colors = SliderDefaults.colors(thumbColor = Color(0xFF4ADE80), activeTrackColor = Color(0xFF4ADE80))
                        )

                        // Boost Right Spacing
                        Text(
                            text = "Jarak Tombol Boost dari Kanan Layar: ${userData.boostOffsetX} dp",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFF9100)
                        )
                        Slider(
                            value = userData.boostOffsetX.toFloat(),
                            onValueChange = { onUpdateBoost(it.toInt(), userData.boostOffsetY, userData.boostScale) },
                            valueRange = 10f..120f,
                            colors = SliderDefaults.colors(thumbColor = Color(0xFFFF9100), activeTrackColor = Color(0xFFFF9100))
                        )

                        // Boost Bottom Spacing
                        Text(
                            text = "Jarak Tombol Boost dari Bawah Layar: ${userData.boostOffsetY} dp",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFF9100)
                        )
                        Slider(
                            value = userData.boostOffsetY.toFloat(),
                            onValueChange = { onUpdateBoost(userData.boostOffsetX, it.toInt(), userData.boostScale) },
                            valueRange = 10f..120f,
                            colors = SliderDefaults.colors(thumbColor = Color(0xFFFF9100), activeTrackColor = Color(0xFFFF9100))
                        )
                    }
                }
            }

            // CONTROL MODE SECTION
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF1A1F33),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3454)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "MODE KENDALI",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xFF00E5FF),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            FilterChip(
                                selected = userData.controlMode == ControlMode.JOYSTICK,
                                onClick = { onSetControlMode(ControlMode.JOYSTICK) },
                                label = { Text("Joystick Virtual") },
                                leadingIcon = {
                                    Icon(Icons.Default.Gamepad, contentDescription = null, modifier = Modifier.size(18.dp))
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF4ADE80),
                                    selectedLabelColor = Color.Black,
                                    containerColor = Color(0xFF242A45),
                                    labelColor = Color.White
                                ),
                                modifier = Modifier.weight(1f)
                            )

                            FilterChip(
                                selected = userData.controlMode == ControlMode.TOUCH_FOLLOW,
                                onClick = { onSetControlMode(ControlMode.TOUCH_FOLLOW) },
                                label = { Text("Sentuh & Ikuti") },
                                leadingIcon = {
                                    Icon(Icons.Default.TouchApp, contentDescription = null, modifier = Modifier.size(18.dp))
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF4ADE80),
                                    selectedLabelColor = Color.Black,
                                    containerColor = Color(0xFF242A45),
                                    labelColor = Color.White
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = userData.controlMode.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFA0AEC0)
                        )
                    }
                }
            }

            // AUDIO & HAPTICS
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF1A1F33),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3454)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "SUARA & GETARAN",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xFFFF9100),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Efek Suara Game", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                            }
                            Switch(checked = userData.soundEnabled, onCheckedChange = onToggleSound)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Vibration, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Getaran Haptik Taktil", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                            }
                            Switch(checked = userData.vibrationEnabled, onCheckedChange = onToggleVibration)
                        }
                    }
                }
            }

            // PLAYER NAME
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF1A1F33),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3454)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "PROFIL CACING",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = nameInput,
                            onValueChange = {
                                nameInput = it
                                onUpdateName(it)
                            },
                            label = { Text("Nama Panggilan Cacing") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF4ADE80),
                                unfocusedBorderColor = Color(0xFF353D63),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // ABOUT
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF1A1F33),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3454)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFA0AEC0))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "nana tayantuh - Arena Luar Edition",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Versi 1.1.0 • Event 7 Hari • Arena Luar, Time Attack, Battle Royale",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFA0AEC0)
                            )
                        }
                    }
                }
            }
        }
    }
}
