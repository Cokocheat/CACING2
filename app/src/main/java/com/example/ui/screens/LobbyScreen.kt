package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserGameData
import com.example.model.GameMode
import com.example.model.Vec2
import com.example.model.Worm
import com.example.ui.game.DailyLoginDialog
import com.example.ui.game.WormsRenderer
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun LobbyScreen(
    userData: UserGameData,
    onPlayClick: (GameMode) -> Unit,
    onOpenWardrobe: () -> Unit,
    onOpenUpgrades: () -> Unit,
    onOpenRecords: () -> Unit,
    onOpenSettings: () -> Unit,
    onUpdateName: (String) -> Unit,
    onClaimDailyReward: () -> Unit
) {
    var nameInput by remember(userData.playerName) { mutableStateOf(userData.playerName) }
    var selectedMode by remember { mutableStateOf(GameMode.OPEN_ARENA) }

    // Daily Login Dialog: Automatically shows up when player can claim today!
    var showDailyLoginDialog by remember { mutableStateOf(userData.canClaimDailyToday) }

    // Floating worm wiggling animation for preview
    val infiniteTransition = rememberInfiniteTransition(label = "wormWiggle")
    val wigglePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "btnPulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF10131F), Color(0xFF181E33), Color(0xFF0D101C))
                )
            )
            .testTag("lobby_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // TOP HEADER BAR: Game Logo "nana tayantuh", "ARENA LUAR", Event 7 Hari, Coins, Settings
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Game title & "ARENA LUAR"
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "nana tayantuh",
                        style = MaterialTheme.typography.displaySmall,
                        color = Color(0xFF4ADE80),
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF242A45),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD600).copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "ARENA LUAR",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFFD600),
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Top Right: Event 7 Hari Button + Coins + Settings
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Event 7 Hari Button
                    Surface(
                        onClick = { showDailyLoginDialog = true },
                        shape = RoundedCornerShape(20.dp),
                        color = if (userData.canClaimDailyToday) Color(0xFF3B2D54) else Color(0xCC1A1F33),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.5.dp,
                            color = if (userData.canClaimDailyToday) Color(0xFFFFD700) else Color(0xFF353D63)
                        ),
                        shadowElevation = if (userData.canClaimDailyToday) 6.dp else 2.dp,
                        modifier = Modifier.testTag("btn_daily_event")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🎁", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (userData.canClaimDailyToday) "Event 7 Hari (!)" else "Event 7 Hari",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (userData.canClaimDailyToday) Color(0xFFFFD700) else Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Coins
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xCC1A1F33),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFFD700)),
                        shadowElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🪙", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${userData.coins}",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color(0xFFFFD700),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Settings
                    IconButton(
                        onClick = onOpenSettings,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xCC1A1F33))
                            .border(1.5.dp, Color(0xFF353D63), CircleShape)
                            .testTag("settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Pengaturan",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // MAIN LANDSCAPE 2-COLUMN CONTENT
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // LEFT COLUMN: Avatar Preview & Feature Navigation
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // WORM PREVIEW BOX
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x66161B2E))
                            .border(2.dp, Color(0xFF2C3454), RoundedCornerShape(20.dp))
                            .clickable { onOpenWardrobe() },
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val cx = size.width / 2f
                            val cy = size.height / 2f

                            val previewWorm = Worm(
                                id = "preview",
                                name = userData.playerName,
                                isPlayer = true,
                                head = Vec2(cx + 60f, cy),
                                angle = 0f,
                                skin = userData.equippedSkin,
                                accessory = userData.equippedAccessory,
                                mass = 90f
                            ).apply {
                                bodyPoints.clear()
                                for (i in 0 until 18) {
                                    val wave = sin(wigglePhase + i * 0.45f) * 16f
                                    val px = (cx + 60f) - i * 14f
                                    val py = cy + wave
                                    bodyPoints.add(Vec2(px, py))
                                }
                            }

                            WormsRenderer.drawWorm(
                                drawScope = this,
                                worm = previewWorm,
                                cameraOffset = Offset.Zero,
                                zoom = 1f
                            )
                        }

                        // Wardrobe tag
                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 6.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xCC0E121E)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.Checkroom, contentDescription = null, tint = Color(0xFF4ADE80), modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${userData.equippedSkin.title} (Ganti Skin)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // BOTTOM ACTION BUTTONS
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LandscapeNavButton(
                            icon = Icons.Default.Checkroom,
                            title = "Lemari Skin",
                            color = Color(0xFF00E5FF),
                            testTag = "btn_wardrobe",
                            onClick = onOpenWardrobe,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        LandscapeNavButton(
                            icon = Icons.Default.Upgrade,
                            title = "Peningkatan",
                            color = Color(0xFFFF9100),
                            testTag = "btn_upgrades",
                            onClick = onOpenUpgrades,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        LandscapeNavButton(
                            icon = Icons.Default.EmojiEvents,
                            title = "Statistik",
                            color = Color(0xFFFFD700),
                            testTag = "btn_records",
                            onClick = onOpenRecords,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // RIGHT COLUMN: Name Input, Mode Selector, High Score, Big PLAY button
                Column(
                    modifier = Modifier
                        .weight(1.15f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // NICKNAME INPUT
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = {
                            nameInput = it
                            onUpdateName(it)
                        },
                        label = { Text("Nama Cacing Kamu", color = Color(0xFFA0AEC0)) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4ADE80),
                            unfocusedBorderColor = Color(0xFF353D63),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color(0x99161B2E),
                            unfocusedContainerColor = Color(0x99161B2E)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("player_name_input")
                    )

                    // GAME MODE SELECTOR (Arena Luar, Time Attack, Battle Royale)
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF161B2E),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3454)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "PILIH MODE PERMAINAN:",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFA0AEC0),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                GameMode.entries.forEach { mode ->
                                    val isSelected = selectedMode == mode
                                    Surface(
                                        onClick = { selectedMode = mode },
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) Color(0xFF253350) else Color(0xFF1E243D),
                                        border = androidx.compose.foundation.BorderStroke(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) Color(0xFF4ADE80) else Color(0xFF353D63)
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(text = mode.icon, fontSize = 18.sp)
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = mode.title,
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) Color(0xFF4ADE80) else Color.White,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // HIGH SCORE TROPHY BANNER
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF1E243D),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF353D63)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🏆", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Rekor Terbaik",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFFA0AEC0)
                                    )
                                    Text(
                                        text = "${userData.highScore} Poin",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Color(0xFFFFD700),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Lawan Dikalahkan",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFFA0AEC0)
                                )
                                Text(
                                    text = "${userData.totalKills} Cacing",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color(0xFFFF5252),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // BIG PLAY BUTTON
                    Button(
                        onClick = { onPlayClick(selectedMode) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .scale(pulseScale)
                            .shadow(16.dp, RoundedCornerShape(18.dp), spotColor = Color(0xFF4ADE80))
                            .testTag("play_game_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4ADE80)),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "MULAI MAIN (${selectedMode.title.uppercase()})",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        }

        // Daily Login Event Modal
        if (showDailyLoginDialog) {
            DailyLoginDialog(
                userData = userData,
                onClaimReward = {
                    onClaimDailyReward()
                },
                onDismiss = {
                    showDailyLoginDialog = false
                }
            )
        }
    }
}

@Composable
private fun LandscapeNavButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    color: Color,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF1E243D),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f)),
        modifier = modifier.testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 11.sp
            )
        }
    }
}
