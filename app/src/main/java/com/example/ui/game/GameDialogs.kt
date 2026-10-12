package com.example.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun PauseDialog(
    soundEnabled: Boolean,
    vibrationEnabled: Boolean,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onToggleSound: (Boolean) -> Unit,
    onToggleVibration: (Boolean) -> Unit,
    onQuitToLobby: () -> Unit
) {
    Dialog(
        onDismissRequest = onResume,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    ) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = Color(0xFF161B2E),
            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF353D63)),
            shadowElevation = 16.dp,
            modifier = Modifier
                .widthIn(max = 500.dp)
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("pause_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "GAME DIJEDA",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )

                // Sound & Vibration Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF202740),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (soundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                                    contentDescription = null,
                                    tint = Color(0xFF4ADE80),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Suara", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                            }
                            Switch(checked = soundEnabled, onCheckedChange = onToggleSound)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF202740),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Vibration,
                                    contentDescription = null,
                                    tint = Color(0xFFFF9100),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Getar", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                            }
                            Switch(checked = vibrationEnabled, onCheckedChange = onToggleVibration)
                        }
                    }
                }

                // Action buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onResume,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("resume_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4ADE80)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("LANJUTKAN", style = MaterialTheme.typography.labelLarge, color = Color.Black, fontWeight = FontWeight.Black)
                    }

                    OutlinedButton(
                        onClick = onRestart,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("restart_button"),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFF9100))
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color(0xFFFF9100), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ULANG", style = MaterialTheme.typography.labelLarge, color = Color(0xFFFF9100), fontWeight = FontWeight.Bold)
                    }
                }

                FilledTonalButton(
                    onClick = onQuitToLobby,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("quit_lobby_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(0xFF2C3454))
                ) {
                    Icon(imageVector = Icons.Default.Home, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("KEMBALI KE MENU", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun GameOverDialog(
    score: Int,
    highScore: Int,
    kills: Int,
    length: Int,
    coinsEarned: Int,
    onPlayAgain: () -> Unit,
    onBackToLobby: () -> Unit
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF141829),
            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFF3366)),
            shadowElevation = 20.dp,
            modifier = Modifier
                .widthIn(max = 560.dp)
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("game_over_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header Loss Banner
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "💥", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "KAMU KALAH!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFFFF3366),
                        fontWeight = FontWeight.Black
                    )
                }

                Text(
                    text = if (score >= highScore && score > 0) "🎉 REKOR SKOR BARU DI ARENA! 🎉" else "Coba lagi dan jadilah cacing terpanjang!",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (score >= highScore && score > 0) Color(0xFFFFD700) else Color(0xFFA0AEC0),
                    textAlign = TextAlign.Center
                )

                // Landscape 2-column or Stats Summary
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Left Stats Box
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFF1E243D),
                        modifier = Modifier.weight(1.2f)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            StatRow(title = "Skor Akhir", value = "$score", icon = "🍩", highlight = true)
                            StatRow(title = "Rekor Terbaik", value = "$highScore", icon = "🏆", highlight = false)
                            StatRow(title = "Lawan Dikalahkan", value = "$kills", icon = "⚔️", highlight = false)
                            StatRow(title = "Panjang Akhir", value = "$length", icon = "🐍", highlight = false)
                            StatRow(title = "Koin Diperoleh", value = "+$coinsEarned", icon = "🪙", highlight = true, valueColor = Color(0xFFFFD700))
                        }
                    }

                    // Right Actions Box
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = onPlayAgain,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("game_over_play_again_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4ADE80)),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("MAIN LAGI", style = MaterialTheme.typography.labelLarge, color = Color.Black, fontWeight = FontWeight.Black)
                        }

                        FilledTonalButton(
                            onClick = onBackToLobby,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("game_over_lobby_button"),
                            colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(0xFF2C3454)),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Home, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("MENU UTAMA", style = MaterialTheme.typography.labelMedium, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatRow(
    title: String,
    value: String,
    icon: String,
    highlight: Boolean,
    valueColor: Color = if (highlight) Color(0xFF4ADE80) else Color.White
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = title, style = MaterialTheme.typography.bodySmall, color = Color(0xFFA0AEC0))
        }
        Text(
            text = value,
            style = if (highlight) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodySmall,
            color = valueColor,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Medium
        )
    }
}
