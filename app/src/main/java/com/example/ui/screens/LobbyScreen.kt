package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.StringsHelper
import com.example.data.UserGameData
import com.example.model.GameMode
import com.example.model.MapTheme
import com.example.model.Vec2
import com.example.model.Worm
import com.example.ui.game.DailyLoginDialog
import com.example.ui.game.WormsRenderer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LobbyScreen(
    userData: UserGameData,
    onPlayClick: (GameMode) -> Unit,
    onOpenWardrobe: () -> Unit,
    onOpenUpgrades: () -> Unit,
    onOpenRecords: () -> Unit,
    onOpenSettings: () -> Unit,
    onClaimDailyReward: () -> Any? = {},
    onUpdateName: (String) -> Unit,
    onSetMapTheme: (MapTheme) -> Unit = {}
) {
    val lang = userData.language

    var selectedMode by remember { mutableStateOf(GameMode.OPEN_ARENA) }
    var nameInput by remember { mutableStateOf(userData.playerName) }
    var showDailyLoginDialog by remember { mutableStateOf(false) }

    // Pulsing and slithering animations
    val infiniteTransition = rememberInfiniteTransition(label = "lobbyAnimations")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "playPulse"
    )

    val wigglePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(22000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "slitherWiggle"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowPulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF091428), // Deep cyber sapphire
                        Color(0xFF0D2240), // Royal azure
                        Color(0xFF081830)  // Deep navy
                    )
                )
            )
            .testTag("lobby_screen")
    ) {
        // Modern Realistic 3D Background Lighting & Cyber Arena Grid
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Radial ambient spotlight shining down
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x3338BDF8),
                        Color(0x1800E5FF),
                        Color.Transparent
                    ),
                    center = Offset(w * 0.35f, h * 0.45f),
                    radius = w * 0.45f
                ),
                topLeft = Offset(w * 0.05f, 0f),
                size = Size(w * 0.6f, h)
            )

            // 2. Futuristic cyber grid lines with subtle vibrant teal/cyan color
            val gridStep = 60.dp.toPx()
            var gx = 0f
            while (gx < w) {
                drawLine(
                    color = Color(0x1200E5FF),
                    start = Offset(gx, 0f),
                    end = Offset(gx, h),
                    strokeWidth = 1f
                )
                gx += gridStep
            }
            var gy = 0f
            while (gy < h) {
                drawLine(
                    color = Color(0x1200E5FF),
                    start = Offset(0f, gy),
                    end = Offset(w, gy),
                    strokeWidth = 1f
                )
                gy += gridStep
            }

            // 3. Floating glowing energy bokeh orbs for realistic depth
            val orbs = listOf(
                Triple(w * 0.12f, h * 0.25f, 32f),
                Triple(w * 0.28f, h * 0.78f, 24f),
                Triple(w * 0.75f, h * 0.18f, 28f),
                Triple(w * 0.88f, h * 0.82f, 36f),
                Triple(w * 0.52f, h * 0.30f, 20f)
            )
            orbs.forEach { (ox, oy, r) ->
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x3500E5FF), Color(0x104ADE80), Color.Transparent),
                        center = Offset(ox, oy),
                        radius = r * 2.5f
                    ),
                    radius = r * 2.5f,
                    center = Offset(ox, oy)
                )
            }
        }

        // Foreground UI Elements
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // TOP HEADER BAR: Game Logo "nana tayantuh", "ARENA LUAR", Event 7 Hari, Coins, Settings
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Game title & "ARENA LUAR"
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "nana tayantuh",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF4ADE80),
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF1A2A48),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00E5FF).copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = StringsHelper.get("outer_arena", lang).uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF00E5FF),
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Top Right: Event 7 Hari Button + Coins + Settings (NO EMOJIS)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Event 7 Hari Button (Vector Icon)
                    Surface(
                        onClick = { showDailyLoginDialog = true },
                        shape = RoundedCornerShape(20.dp),
                        color = if (userData.canClaimDailyToday) Color(0xFF3A2B55) else Color(0xCC1A2342),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.5.dp,
                            color = if (userData.canClaimDailyToday) Color(0xFFFFD700) else Color(0xFF35446E)
                        ),
                        shadowElevation = if (userData.canClaimDailyToday) 6.dp else 2.dp,
                        modifier = Modifier.testTag("btn_daily_event")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CardGiftcard,
                                contentDescription = null,
                                tint = if (userData.canClaimDailyToday) Color(0xFFFFD700) else Color(0xFF00E5FF),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (userData.canClaimDailyToday) "${StringsHelper.get("daily_event", lang)} (!)" else StringsHelper.get("daily_event", lang),
                                style = MaterialTheme.typography.labelMedium,
                                color = if (userData.canClaimDailyToday) Color(0xFFFFD700) else Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Coins Badge (Vector Icon)
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xCC16223D),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFFD700)),
                        shadowElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(17.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${userData.coins}",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color(0xFFFFD700),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Settings Button
                    IconButton(
                        onClick = onOpenSettings,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xCC1A2342))
                            .border(1.5.dp, Color(0xFF35446E), CircleShape)
                            .testTag("settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = StringsHelper.get("settings", lang),
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
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // LEFT COLUMN: Interactive Stage with Live Worm & Quick Navigation Actions
                Column(
                    modifier = Modifier
                        .weight(1.15f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Worm Stage Visual Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val cx = size.width * 0.5f
                            val cy = size.height * 0.50f
                            val t = wigglePhase

                            val marginX = 28.dp.toPx()
                            val marginY = 22.dp.toPx()
                            val halfW = (size.width / 2f) - marginX
                            val halfH = (size.height / 2f) - marginY

                            // Decorative scattered bright food orbs across screen sides and corners
                            val foodPellets = listOf(
                                Offset(cx - halfW * 0.88f, cy - halfH * 0.80f) to Color(0xFFFF5252),
                                Offset(cx + halfW * 0.85f, cy - halfH * 0.75f) to Color(0xFFFFD700),
                                Offset(cx - halfW * 0.80f, cy + halfH * 0.82f) to Color(0xFF00E5FF),
                                Offset(cx + halfW * 0.88f, cy + halfH * 0.78f) to Color(0xFF4ADE80),
                                Offset(cx - halfW * 0.40f, cy - halfH * 0.85f) to Color(0xFFFF4081),
                                Offset(cx + halfW * 0.45f, cy + halfH * 0.85f) to Color(0xFFFFB300),
                                Offset(cx - halfW * 0.92f, cy) to Color(0xFFE040FB),
                                Offset(cx + halfW * 0.92f, cy) to Color(0xFF00E5FF)
                            )
                            for ((pos, color) in foodPellets) {
                                drawCircle(color = color.copy(alpha = 0.35f), radius = 6.dp.toPx(), center = pos)
                                drawCircle(color = color, radius = 3.dp.toPx(), center = pos)
                                drawCircle(color = Color.White, radius = 1.2.dp.toPx(), center = pos)
                            }

                            // Dynamic wandering path reaching every side and corner of the screen randomly (non-circular)
                            val headX = cx + sin(t) * (halfW * 0.90f) + sin(2.7f * t + 1.2f) * (halfW * 0.32f) - cos(0.8f * t) * (halfW * 0.22f)
                            val headY = cy + cos(1.4f * t) * (halfH * 0.88f) + sin(3.3f * t + 0.6f) * (halfH * 0.30f) + cos(2.1f * t) * (halfH * 0.18f)
                            val vx = cos(t) * (halfW * 0.90f) + 2.7f * cos(2.7f * t + 1.2f) * (halfW * 0.32f) + 0.8f * sin(0.8f * t) * (halfW * 0.22f)
                            val vy = -1.4f * sin(1.4f * t) * (halfH * 0.88f) + 3.3f * cos(3.3f * t + 0.6f) * (halfH * 0.30f) - 2.1f * sin(2.1f * t) * (halfH * 0.18f)
                            val headAngle = kotlin.math.atan2(vy, vx)

                            val dummyWorm = Worm(
                                id = "preview_player",
                                name = nameInput.ifBlank { "Nana" },
                                isPlayer = true,
                                head = Vec2(headX, headY),
                                angle = headAngle,
                                targetAngle = headAngle,
                                skin = userData.equippedSkin,
                                accessory = userData.equippedAccessory,
                                mass = 85f
                            )

                            // Dynamic body slithering wave along the free roaming path
                            dummyWorm.bodyPoints.clear()
                            val numSegments = 18
                            for (i in 0 until numSegments) {
                                val segT = t - (i * 0.038f)
                                var sx = cx + sin(segT) * (halfW * 0.90f) + sin(2.7f * segT + 1.2f) * (halfW * 0.32f) - cos(0.8f * segT) * (halfW * 0.22f)
                                var sy = cy + cos(1.4f * segT) * (halfH * 0.88f) + sin(3.3f * segT + 0.6f) * (halfH * 0.30f) + cos(2.1f * segT) * (halfH * 0.18f)

                                val segVx = cos(segT) * (halfW * 0.90f) + 2.7f * cos(2.7f * segT + 1.2f) * (halfW * 0.32f) + 0.8f * sin(0.8f * segT) * (halfW * 0.22f)
                                val segVy = -1.4f * sin(1.4f * segT) * (halfH * 0.88f) + 3.3f * cos(3.3f * segT + 0.6f) * (halfH * 0.30f) - 2.1f * sin(2.1f * segT) * (halfH * 0.18f)
                                val segAngle = kotlin.math.atan2(segVy, segVx)

                                val lateralWave = sin(t * 3.5f - i * 0.32f) * 2.8f
                                val normAngle = segAngle - (PI / 2).toFloat()
                                sx += cos(normAngle) * lateralWave
                                sy += sin(normAngle) * lateralWave

                                dummyWorm.bodyPoints.add(Vec2(sx, sy))
                            }

                            WormsRenderer.drawWorm(
                                drawScope = this,
                                worm = dummyWorm,
                                cameraOffset = Offset(0f, 0f),
                                zoom = 1.0f
                            )
                        }
                    }

                    // Action Buttons: Wardrobe, Upgrades, Records (NO EMOJIS)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LandscapeNavButton(
                            icon = Icons.Default.Checkroom,
                            title = StringsHelper.get("wardrobe", lang),
                            color = Color(0xFF00E5FF),
                            testTag = "btn_wardrobe",
                            onClick = onOpenWardrobe,
                            modifier = Modifier.weight(1f)
                        )
                        LandscapeNavButton(
                            icon = Icons.Default.Upgrade,
                            title = StringsHelper.get("upgrades", lang),
                            color = Color(0xFFFF9100),
                            testTag = "btn_upgrades",
                            onClick = onOpenUpgrades,
                            modifier = Modifier.weight(1f)
                        )
                        LandscapeNavButton(
                            icon = Icons.Default.EmojiEvents,
                            title = StringsHelper.get("records", lang),
                            color = Color(0xFF4ADE80),
                            testTag = "btn_records",
                            onClick = onOpenRecords,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // RIGHT COLUMN: Nickname Input, Game Mode Picker, High Score, Big Play Button
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Player Nickname Input
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = {
                            if (it.length <= 16) {
                                nameInput = it
                                onUpdateName(it)
                            }
                        },
                        label = { Text(StringsHelper.get("player_name", lang)) },
                        leadingIcon = {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF4ADE80))
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4ADE80),
                            unfocusedBorderColor = Color(0xFF35446E),
                            focusedLabelColor = Color(0xFF4ADE80),
                            unfocusedLabelColor = Color(0xFFA0AEC0),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("player_name_input")
                    )

                    // Mode Selection Row (NO EMOJIS, Vector Icons)
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xDD121B33),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E3D66)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = StringsHelper.get("choose_mode", lang),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF4ADE80),
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                GameMode.entries.forEach { mode ->
                                    val isSelected = selectedMode == mode
                                    val (modeIcon, modeColor) = when (mode) {
                                        GameMode.OPEN_ARENA -> Pair(Icons.Default.Public, Color(0xFF4ADE80))
                                        GameMode.TIME_ATTACK -> Pair(Icons.Default.Timer, Color(0xFFFF9100))
                                        GameMode.BATTLE_ROYALE -> Pair(Icons.Default.MilitaryTech, Color(0xFFC084FC))
                                    }

                                    Surface(
                                        onClick = { selectedMode = mode },
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isSelected) Color(0xFF223558) else Color(0xFF141E34),
                                        border = androidx.compose.foundation.BorderStroke(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) modeColor else Color(0xFF2C3C64)
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                imageVector = modeIcon,
                                                contentDescription = null,
                                                tint = if (isSelected) modeColor else Color(0xFFA0AEC0),
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = when (mode) {
                                                    GameMode.OPEN_ARENA -> StringsHelper.get("outer_arena", lang)
                                                    GameMode.TIME_ATTACK -> StringsHelper.get("time_attack", lang)
                                                    GameMode.BATTLE_ROYALE -> StringsHelper.get("battle_royale", lang)
                                                },
                                                fontSize = 10.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) modeColor else Color.White,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // HIGH SCORE TROPHY BANNER (NO EMOJIS)
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xDD121B33),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E3D66)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = StringsHelper.get("high_score", lang),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFFA0AEC0),
                                        fontSize = 10.sp
                                    )
                                    Text(
                                        text = "${userData.highScore} ${StringsHelper.get("points", lang)}",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Color(0xFFFFD700),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = StringsHelper.get("kills", lang),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFFA0AEC0),
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = "${userData.totalKills} ${StringsHelper.get("worms_count", lang)}",
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
                            .height(50.dp)
                            .scale(pulseScale)
                            .shadow(16.dp, RoundedCornerShape(16.dp), spotColor = Color(0xFF4ADE80))
                            .testTag("play_game_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4ADE80)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black, modifier = Modifier.size(26.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = StringsHelper.get("play", lang),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }

        // Daily Login Event Modal
        if (showDailyLoginDialog) {
            DailyLoginDialog(
                userData = userData,
                lang = lang,
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
    icon: ImageVector,
    title: String,
    color: Color,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = Color(0xCC141E36),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, color.copy(alpha = 0.7f)),
        shadowElevation = 4.dp,
        modifier = modifier
            .height(52.dp)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}
