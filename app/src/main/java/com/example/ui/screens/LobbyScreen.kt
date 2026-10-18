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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.StringsHelper
import com.example.data.UserGameData
import com.example.model.GameConfig
import com.example.model.GameDifficulty
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import com.example.model.GameMode
import com.example.model.MapTheme
import com.example.model.Vec2
import com.example.model.Worm
import com.example.ui.game.DailyLoginDialog
import com.example.ui.game.LuckyBonusDialog
import com.example.ui.game.PiggyBankDialog
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
    onOpenProfile: () -> Unit,
    onOpenCoinShop: () -> Unit,
    onAddCoins: (Int) -> Unit = {},
    onBreakPiggyBank: () -> Unit = {},
    onClaimDailyReward: () -> Any? = {},
    onUpdateName: (String) -> Unit = {},
    onSetMapTheme: (MapTheme) -> Unit = {},
    onSetGameDifficulty: (GameDifficulty) -> Unit = {}
) {
    val context = LocalContext.current
    val lang = userData.language

    val selectedMode by remember { mutableStateOf(GameMode.OPEN_ARENA) }
    var showDailyLoginDialog by remember(userData.canClaimDailyToday) { mutableStateOf(userData.canClaimDailyToday) }
    var showBonusDialog by remember { mutableStateOf(false) }
    var showPiggyDialog by remember { mutableStateOf(false) }

    // Pulsing and slithering animations
    val infiniteTransition = rememberInfiniteTransition(label = "lobbyAnimations")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "btnPulse"
    )

    // Full-Screen In-Game Physics Worm wandering in lobby
    var screenWidth by remember { mutableFloatStateOf(1000f) }
    var screenHeight by remember { mutableFloatStateOf(700f) }
    var frameTick by remember { mutableLongStateOf(0L) }

    val lobbyWorm = remember {
        Worm(
            id = "lobby_player",
            name = userData.playerName,
            isPlayer = true,
            head = Vec2(350f, 400f),
            angle = 0.3f,
            targetAngle = 0.3f,
            speed = 250f,
            skin = userData.equippedSkin,
            accessory = userData.equippedAccessory,
            mass = 85f
        ).apply {
            initializeBody()
        }
    }

    LaunchedEffect(Unit) {
        var lastTime = System.nanoTime()
        var wanderNoise = 0f
        var cruiseHeading = 0.35f

        while (true) {
            withFrameNanos { now ->
                val dt = ((now - lastTime) / 1_000_000_000f).coerceIn(0.005f, 0.033f)
                lastTime = now

                wanderNoise += dt * 5.0f

                val margin = 75f
                val head = lobbyWorm.head
                val w = screenWidth.coerceAtLeast(400f)
                val h = screenHeight.coerceAtLeast(300f)

                if (head.x > w - margin && cos(cruiseHeading) > 0) {
                    cruiseHeading = PI.toFloat() - (cruiseHeading * 0.4f)
                } else if (head.x < margin && cos(cruiseHeading) < 0) {
                    cruiseHeading = 0f + ((PI.toFloat() - cruiseHeading) * 0.4f)
                }

                if (head.y > h - margin && sin(cruiseHeading) > 0) {
                    cruiseHeading = -kotlin.math.abs(cruiseHeading)
                } else if (head.y < margin && sin(cruiseHeading) < 0) {
                    cruiseHeading = kotlin.math.abs(cruiseHeading)
                }

                val slitherOffset = sin(wanderNoise) * 0.15f
                lobbyWorm.targetAngle = cruiseHeading + slitherOffset

                val maxTurn = GameConfig.TURN_SPEED * 2.2f * dt
                lobbyWorm.steerTowards(lobbyWorm.targetAngle, maxTurn)

                val velocity = Vec2.fromAngle(lobbyWorm.angle, lobbyWorm.speed * dt)
                lobbyWorm.head = lobbyWorm.head + velocity

                lobbyWorm.head = Vec2(
                    lobbyWorm.head.x.coerceIn(20f, w - 20f),
                    lobbyWorm.head.y.coerceIn(20f, h - 20f)
                )

                var prevPoint = lobbyWorm.head
                val desiredDist = GameConfig.SEGMENT_SPACING
                for (i in lobbyWorm.bodyPoints.indices) {
                    val current = lobbyWorm.bodyPoints[i]
                    val diff = current - prevPoint
                    val dist = diff.length()
                    if (dist > 0.001f) {
                        lobbyWorm.bodyPoints[i] = prevPoint + diff.normalized() * desiredDist
                    }
                    prevPoint = lobbyWorm.bodyPoints[i]
                }

                frameTick = now
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { size ->
                screenWidth = size.width.toFloat()
                screenHeight = size.height.toFloat()
            }
            .testTag("lobby_screen")
    ) {
        // 1. In-Game Arena Background with warm terracotta checkerboard & dark vignette (matches screenshot!)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            WormsRenderer.drawArenaBackground(
                drawScope = this,
                cameraOffset = Offset.Zero,
                zoom = 1.0f,
                arenaCenter = Vec2(w / 2f, h / 2f),
                arenaRadius = maxOf(w, h) * 2f,
                mapTheme = userData.mapTheme
            )

            // Scattered glowing food treats on the ground
            val foodPellets = listOf(
                Offset(w * 0.14f, h * 0.22f) to Color(0xFFFF5252),
                Offset(w * 0.28f, h * 0.82f) to Color(0xFFFFD700),
                Offset(w * 0.72f, h * 0.18f) to Color(0xFF00E5FF),
                Offset(w * 0.85f, h * 0.78f) to Color(0xFF4ADE80),
                Offset(w * 0.44f, h * 0.14f) to Color(0xFFFF4081),
                Offset(w * 0.62f, h * 0.88f) to Color(0xFFFFB300)
            )
            for ((pos, color) in foodPellets) {
                drawCircle(color = color.copy(alpha = 0.35f), radius = 6.dp.toPx(), center = pos)
                drawCircle(color = color, radius = 3.dp.toPx(), center = pos)
                drawCircle(color = Color.White, radius = 1.2.dp.toPx(), center = pos)
            }

            // Draw full-screen wandering worm
            val tick = frameTick
            lobbyWorm.name = userData.playerName
            lobbyWorm.skin = userData.equippedSkin
            lobbyWorm.accessory = userData.equippedAccessory

            WormsRenderer.drawWorm(
                drawScope = this,
                worm = lobbyWorm,
                cameraOffset = Offset.Zero,
                zoom = 1.0f
            )
        }

        // 2. FOREGROUND UI LAYOUT (EXACTLY MATCHING Screenshot_20261017-092349.jpg)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // TOP BAR: Player Profile (Left) & Energy + Coins (Right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TOP-LEFT: Warm Amber-Chocolate Profile Card (Blends seamlessly with the arena theme)
                Surface(
                    onClick = onOpenProfile,
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xEB28170B), // Warm chocolate amber matching theme
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFF59E0B)),
                    shadowElevation = 6.dp
                ) {
                    Row(
                        modifier = Modifier.padding(start = 6.dp, end = 14.dp, top = 6.dp, bottom = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar Circle with Level 16 badge
                        Box(contentAlignment = Alignment.BottomCenter) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF332012),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFDE047)),
                                modifier = Modifier.size(46.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("😊", fontSize = 24.sp)
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFF59E0B),
                                modifier = Modifier.padding(bottom = 0.dp)
                            ) {
                                Text(
                                    text = "16",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Profil label & Orange Energy Bar
                        Column {
                            Text(
                                text = userData.playerName.ifBlank { StringsHelper.get("tab_profile", lang) },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            // Energy / XP Progress Bar
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF190E06),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.7f)),
                                modifier = Modifier.width(110.dp).height(12.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(0.65f)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(
                                                Brush.horizontalGradient(
                                                    listOf(Color(0xFFF97316), Color(0xFFFBBF24))
                                                )
                                            )
                                    )
                                }
                            }
                        }
                    }
                }

                // TOP-RIGHT: Apples (Energy) + Gold Coins
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Apple Stamina Pill
                    Surface(
                        onClick = onOpenCoinShop,
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xEB28170B),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFF97316))
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 8.dp, end = 4.dp, top = 3.dp, bottom = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🍎", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "85/20",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF38BDF8),
                                modifier = Modifier.size(24.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Add, contentDescription = "Tambah Stamina", tint = Color.White, modifier = Modifier.size(15.dp))
                                }
                            }
                        }
                    }

                    // Gold Coin Pill
                    Surface(
                        onClick = onOpenCoinShop,
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xEB28170B),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFACC15))
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 8.dp, end = 4.dp, top = 3.dp, bottom = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("😊", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${userData.coins}",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF38BDF8),
                                modifier = Modifier.size(24.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Add, contentDescription = "Tambah Koin", tint = Color.White, modifier = Modifier.size(15.dp))
                                }
                            }
                        }
                    }
                }
            }

            // MIDDLE AREA: Left Event Badges + Center Cartoon Logo & Action Buttons + Right Mascots
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // LEFT STACK: Event / Bonus Badges (Warm theme harmony)
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Bonus Button
                    Surface(
                        onClick = { showBonusDialog = true },
                        shape = CircleShape,
                        color = Color(0xFFDC2626),
                        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFDE047)),
                        shadowElevation = 6.dp,
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("BONUS", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.White)
                                Text("⭐", fontSize = 14.sp)
                            }
                        }
                    }

                    // Trophy Ribbon Badge ("2h 2j")
                    Surface(
                        onClick = onOpenRecords,
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xEB28170B),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFACC15)),
                        modifier = Modifier.width(64.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFFFDE047), modifier = Modifier.size(26.dp))
                            Text("2h 2j", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                    }

                    // Calendar / 15-Hari Daily Login Gift Clipboard ("7j 50m")
                    Surface(
                        onClick = { showDailyLoginDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xEB28170B),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFACC15)),
                        modifier = Modifier.width(64.dp).testTag("btn_daily_event")
                    ) {
                        Column(
                            modifier = Modifier.padding(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(contentAlignment = Alignment.TopEnd) {
                                Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(26.dp))
                                if (userData.canClaimDailyToday) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFEF4444))
                                    )
                                }
                            }
                            Text("7j 50m", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                    }
                }

                // CENTER COLUMN: Big Cartoon 3D Logo + Center Mascot + "Bertempur!" + "Lemari Cacing"
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Big Cartoon 3D Game Logo
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFA2C170B), // Warm chocolate base banner
                            border = androidx.compose.foundation.BorderStroke(2.5.dp, Color(0xFFFACC15)),
                            shadowElevation = 8.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "WORMS",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFFDE047),
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ZONE",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF38BDF8),
                                    letterSpacing = 1.sp
                                )
                            }
                        }
                    }

                    // Center Mascot Worm with "▶ Video" Pill
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Text("🐛", fontSize = 38.sp)
                        Surface(
                            onClick = onOpenCoinShop,
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xEB28170B),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFACC15)),
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFFFDE047), modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text("Video", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // GIANT "Bertempur!" PLAY BUTTON (Glossy Green with Golden Border like screenshot!)
                    Surface(
                        onClick = { onPlayClick(selectedMode) },
                        shape = RoundedCornerShape(26.dp),
                        color = Color(0xFF22C55E), // Vibrant glossy green
                        border = androidx.compose.foundation.BorderStroke(3.5.dp, Color(0xFFFACC15)), // Thick gold stroke
                        shadowElevation = 10.dp,
                        modifier = Modifier
                            .scale(pulseScale)
                            .width(230.dp)
                            .height(58.dp)
                            .testTag("play_button")
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = StringsHelper.get("battle_btn", lang),
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // BLUE PILL BUTTON: "Lemari Cacing" with red notification dot
                    Surface(
                        onClick = onOpenWardrobe,
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF0284C7), // Glossy cyan-blue
                        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFF59E0B)),
                        shadowElevation = 6.dp,
                        modifier = Modifier.width(180.dp).height(38.dp).testTag("btn_wardrobe")
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = StringsHelper.get("wardrobe_btn", lang),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEF4444))
                                )
                            }
                        }
                    }

                    // Subtitle below button with cozy translucent pill
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xCC1E1208),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = StringsHelper.get("choose_theme_sub", lang),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFDE047),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }

                // RIGHT STACK: Piggy bank & Mascot
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🐍", fontSize = 34.sp)

                    Surface(
                        onClick = { showPiggyDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xEB28170B),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFACC15)),
                        modifier = Modifier.width(64.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🐷", fontSize = 22.sp)
                            Text("23j 55m", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                    }
                }
            }

            // BOTTOM BAR: Left (Leaderboard, Upgrades) & Right (Share, Settings)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Bottom-Left Buttons
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Leaderboard / Records
                    CartoonSquareButton(
                        icon = Icons.Default.Leaderboard,
                        testTag = "btn_records",
                        onClick = onOpenRecords
                    )
                    // Upgrades / Peningkatan
                    CartoonSquareButton(
                        icon = Icons.Default.Upgrade,
                        testTag = "btn_upgrades",
                        onClick = onOpenUpgrades
                    )
                }

                // Bottom-Right Buttons (Share & Settings)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Share
                    CartoonSquareButton(
                        icon = Icons.Default.Share,
                        testTag = "btn_share",
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Ayo tanding di Nana Tayantuh Worms Zone IO! Skor Tertinggi: ${userData.highScore} dengan ${userData.totalKills} cacing dikalahkan!"
                                )
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, StringsHelper.get("share_profile", lang)))
                        }
                    )
                    // Settings
                    CartoonSquareButton(
                        icon = Icons.Default.Settings,
                        testTag = "settings_button",
                        onClick = onOpenSettings
                    )
                }
            }
        }

        // Daily Login Dialog Popup
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

        // Lucky Bonus Dialog
        if (showBonusDialog) {
            LuckyBonusDialog(
                onClaimBonus = {
                    onAddCoins(750)
                    showBonusDialog = false
                },
                onDismiss = {
                    showBonusDialog = false
                }
            )
        }

        // Piggy Bank Dialog
        if (showPiggyDialog) {
            PiggyBankDialog(
                piggyCoins = userData.piggyBankCoins,
                onBreak = {
                    onBreakPiggyBank()
                    showPiggyDialog = false
                },
                onDismiss = {
                    showPiggyDialog = false
                }
            )
        }
    }
}

// WARM CARTOON SQUARE BUTTON WITH GOLD BORDER (Signature style matching theme)
@Composable
private fun CartoonSquareButton(
    icon: ImageVector,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = Color(0xEB28170B), // Warm theme chocolate amber
        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFF59E0B)), // Warm orange-gold border
        shadowElevation = 5.dp,
        modifier = Modifier.size(46.dp).testTag(testTag)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFFDE047), // Vibrant cartoon golden icon
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
