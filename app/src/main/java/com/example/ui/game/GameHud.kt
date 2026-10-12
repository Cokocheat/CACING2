package com.example.ui.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ControlMode
import com.example.data.UserGameData
import com.example.engine.EngineHudState
import com.example.engine.KillEvent
import com.example.engine.LeaderboardEntry
import com.example.model.GameConfig
import com.example.model.GameMode
import com.example.model.Vec2
import kotlin.math.atan2
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Composable
fun GameHud(
    hudState: EngineHudState,
    leaderboard: List<LeaderboardEntry>,
    userData: UserGameData,
    onDirectionChange: (Float) -> Unit,
    onBoostChange: (Boolean) -> Unit,
    onPauseClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("game_hud_root")
    ) {
        // TOP BAR HUD: Score, Coins, Protection Shield, Rank, Pause, Mode Badge
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 14.dp, end = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Left Column: Score, Coins, Kills, Shield Status, Mode Banner
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Score Badge
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xCC1A1F33),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF353D63)),
                        shadowElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🍩", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "SKOR: ${hudState.score}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Panjang: ${(12 + sqrt(hudState.score.toFloat()) * 1.8f).toInt()}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF4ADE80)
                                )
                            }
                        }
                    }

                    // Coins Bar
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xCC1A1F33),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🪙", fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "+${hudState.coins}",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFFFFD700)
                            )
                        }
                    }

                    // Kills Bar
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xCC1A1F33),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF5252).copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "⚔️", fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${hudState.kills}",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFFFF5252)
                            )
                        }
                    }

                    // Mode Specific Info Badge
                    when (hudState.gameMode) {
                        GameMode.OPEN_ARENA -> {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xCC162238),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4ADE80).copy(alpha = 0.6f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "🌍", fontSize = 13.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Arena Luar",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color(0xFF4ADE80),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        GameMode.TIME_ATTACK -> {
                            val minutes = (hudState.timeAttackRemaining / 60).toInt()
                            val seconds = (hudState.timeAttackRemaining % 60).toInt()
                            val formattedTime = "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xCC3D1515),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF5252))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "⏱️", fontSize = 13.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = formattedTime,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color(0xFFFFD700),
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }
                        GameMode.BATTLE_ROYALE -> {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xCC2A1B3D),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBB86FC))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "👑", fontSize = 13.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Sisa: ${hudState.aliveBotsCount + 1}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color(0xFFFFD600),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Spawn Protection Shield Pill
                    if (hudState.spawnProtectionRemaining > 0f) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xEE092C3E),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF00E5FF)),
                            shadowElevation = 6.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "🛡️", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "KEBAL: ${(hudState.spawnProtectionRemaining + 0.9f).toInt()}s",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color(0xFF00E5FF),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Active Potions Pill list
                if (hudState.activePotions.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        hudState.activePotions.forEach { (type, remaining) ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = type.color.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, type.color)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(type.color)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${type.title} (${remaining.toInt()}s)",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Right Row: Leaderboard + Minimap + Pause Button
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Leaderboard Box
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xB3121626),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3454)),
                    modifier = Modifier.width(142.dp)
                ) {
                    Column(modifier = Modifier.padding(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Peringkat",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFA0AEC0),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "#${hudState.playerRank}",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFFFFD700)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        leaderboard.take(5).forEach { entry ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 1.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val textColor = if (entry.isPlayer) Color(0xFFFFD700) else Color(0xFFE2E8F0)
                                Text(
                                    text = "${entry.rank}. ${entry.name.take(8)}",
                                    fontSize = 11.sp,
                                    color = textColor,
                                    fontWeight = if (entry.isPlayer) FontWeight.Bold else FontWeight.Normal
                                )
                                Text(
                                    text = "${entry.score}",
                                    fontSize = 11.sp,
                                    color = textColor,
                                    fontWeight = if (entry.isPlayer) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Pause button
                    IconButton(
                        onClick = onPauseClick,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xCC1A1F33))
                            .border(1.5.dp, Color(0xFF353D63), CircleShape)
                            .testTag("pause_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Pause,
                            contentDescription = "Jeda Game",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Minimap Radar
                    MinimapRadar(
                        playerPos = hudState.playerPos,
                        botPositions = hudState.botPositions,
                        arenaRadius = hudState.currentArenaRadius
                    )
                }
            }
        }

        // KILL NOTIFICATION BANNER (Top-Center Popping Notification)
        AnimatedVisibility(
            visible = hudState.recentKillEvent != null,
            enter = fadeIn() + scaleIn(initialScale = 0.8f),
            exit = fadeOut() + scaleOut(targetScale = 0.8f),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 68.dp)
        ) {
            hudState.recentKillEvent?.let { kill ->
                KillBanner(killEvent = kill, playerName = userData.playerName)
            }
        }

        // BOTTOM CONTROLS: Ergonomic & Configurable Joystick (Left), Center Hint, and Boost Button (Right)
        // 1. Virtual Joystick
        if (userData.controlMode == ControlMode.JOYSTICK) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(
                        start = userData.joystickOffsetX.dp,
                        bottom = userData.joystickOffsetY.dp
                    )
            ) {
                val joySize = (126 * userData.joystickScale).dp
                VirtualJoystick(
                    onMove = onDirectionChange,
                    modifier = Modifier.size(joySize)
                )
            }
        }

        // 2. Center Hint
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 14.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xAA1A1F33),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF353D63))
            ) {
                Text(
                    text = "👆 Geser jempol atau sentuh layar untuk mengarahkan",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFCBD5E1),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
        }

        // 3. Turbo Boost Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = userData.boostOffsetX.dp,
                    bottom = userData.boostOffsetY.dp
                )
        ) {
            val boostSize = (82 * userData.boostScale).dp
            TurboBoostButton(
                onBoostChange = onBoostChange,
                modifier = Modifier.size(boostSize)
            )
        }
    }
}

@Composable
fun KillBanner(killEvent: KillEvent, playerName: String = "Kamu") {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xDD12172A),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFF5252)),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "⚔️", fontSize = 16.sp)
            Text(
                text = "$playerName  ➜  ${killEvent.victimName}",
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0x33FFD700)
            ) {
                Text(
                    text = "+${killEvent.coinsEarned} 🪙",
                    fontSize = 12.sp,
                    color = Color(0xFFFFD700),
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            if (killEvent.streak > 1) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFF5252)
                ) {
                    Text(
                        text = "x${killEvent.streak}",
                        fontSize = 11.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun VirtualJoystick(
    onMove: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var thumbOffset by remember { mutableStateOf(Offset.Zero) }
    val maxRadius = 48f

    Box(
        modifier = modifier
            .testTag("virtual_joystick")
            .clip(CircleShape)
            .background(Color(0x77161B2E))
            .border(2.5.dp, Color(0xAA4ADE80), CircleShape)
            .shadow(8.dp, CircleShape)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val drag = offset - center
                        val dist = drag.getDistance()
                        val clamped = if (dist > maxRadius) drag * (maxRadius / dist) else drag
                        thumbOffset = clamped
                        val angle = atan2(clamped.y, clamped.x)
                        onMove(angle)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val newOffset = thumbOffset + dragAmount
                        val dist = newOffset.getDistance()
                        val clamped = if (dist > maxRadius) newOffset * (maxRadius / dist) else newOffset
                        thumbOffset = clamped
                        val angle = atan2(clamped.y, clamped.x)
                        onMove(angle)
                    },
                    onDragEnd = {
                        thumbOffset = Offset.Zero
                    },
                    onDragCancel = {
                        thumbOffset = Offset.Zero
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // Inner thumb knob
        Box(
            modifier = Modifier
                .offset { IntOffset(thumbOffset.x.roundToInt(), thumbOffset.y.roundToInt()) }
                .size(46.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF4ADE80), Color(0xFF16A34A))
                    )
                )
                .border(2.dp, Color.White.copy(alpha = 0.85f), CircleShape)
                .shadow(6.dp, CircleShape)
        )
    }
}

@Composable
fun TurboBoostButton(
    onBoostChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .testTag("turbo_boost_button")
            .clip(CircleShape)
            .background(
                Brush.verticalGradient(
                    colors = if (isPressed) {
                        listOf(Color(0xFFFF3D00), Color(0xFFDD2C00))
                    } else {
                        listOf(Color(0xFFFF9100), Color(0xFFFF6D00))
                    }
                )
            )
            .border(3.dp, if (isPressed) Color(0xFFFFD600) else Color.White, CircleShape)
            .shadow(if (isPressed) 14.dp else 8.dp, CircleShape)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        onBoostChange(true)
                        tryAwaitRelease()
                        isPressed = false
                        onBoostChange(false)
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = "Turbo Dorongan",
                tint = Color.White,
                modifier = Modifier.size(34.dp)
            )
            Text(
                text = "BOOST",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun MinimapRadar(
    playerPos: Vec2,
    botPositions: List<Vec2>,
    arenaRadius: Float,
    modifier: Modifier = Modifier
) {
    val radarSize = 68.dp
    val arenaCenter = Vec2(GameConfig.ARENA_CENTER_X, GameConfig.ARENA_CENTER_Y)

    Surface(
        modifier = modifier.size(radarSize),
        shape = CircleShape,
        color = Color(0xCC0E121E),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF2C3454)),
        shadowElevation = 4.dp
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val r = size.width / 2f
            val cx = size.width / 2f
            val cy = size.height / 2f

            // Radar background grid ring
            drawCircle(
                color = Color(0x334ADE80),
                radius = r * 0.5f,
                center = Offset(cx, cy),
                style = Stroke(width = 1f)
            )

            // Safe zone border ring
            val safeZoneScale = (arenaRadius / GameConfig.ARENA_RADIUS) * r * 0.9f
            drawCircle(
                color = Color(0x66BB86FC),
                radius = safeZoneScale,
                center = Offset(cx, cy),
                style = Stroke(width = 1.5f)
            )

            // Draw other bots dots
            val scale = (r * 0.88f) / GameConfig.ARENA_RADIUS
            for (i in botPositions.indices) {
                val bHead = botPositions[i]
                val dx = (bHead.x - arenaCenter.x) * scale
                val dy = (bHead.y - arenaCenter.y) * scale
                drawCircle(
                    color = Color(0xFFFF5252),
                    radius = 2.2f,
                    center = Offset(cx + dx, cy + dy)
                )
            }

            // Draw Player glowing green dot
            val px = (playerPos.x - arenaCenter.x) * scale
            val py = (playerPos.y - arenaCenter.y) * scale
            drawCircle(
                color = Color(0xFF4ADE80),
                radius = 3.8f,
                center = Offset(cx + px, cy + py)
            )
            drawCircle(
                color = Color.White,
                radius = 1.8f,
                center = Offset(cx + px, cy + py)
            )
        }
    }
}
