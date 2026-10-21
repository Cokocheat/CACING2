package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
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
    onClaimLuckyBonus: (Int) -> Boolean = { false },
    onBreakPiggyBank: () -> Unit = {},
    onClaimDailyReward: () -> Any? = {},
    onUpdateName: (String) -> Unit = {},
    onSetMapTheme: (MapTheme) -> Unit = {},
    onSetGameDifficulty: (GameDifficulty) -> Unit = {}
) {
    val context = LocalContext.current
    val lang = userData.language

    var selectedMode by remember { mutableStateOf(GameMode.OPEN_ARENA) }
    var showDailyLoginDialog by remember(userData.canClaimDailyToday) { mutableStateOf(userData.canClaimDailyToday) }
    var showBonusDialog by remember { mutableStateOf(false) }
    var showPiggyDialog by remember { mutableStateOf(false) }

    // Pulsing animation for main battle button
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

    // Full-Screen In-Game Physics Worm wandering in lobby (Preserved background canvas)
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
        // 1. In-Game Arena Background with terracotta checkerboard & dynamic wandering worm (PRESERVED)
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

        // 2. TOTAL REDESIGNED MODERN ARCADE FOREGROUND UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // TOP HEADER: Profile Card (Left) | Title Badge (Center) | Pure Coins Pill (Right - NON-CLICKABLE)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TOP-LEFT: Polished Player Profile Pill
                Surface(
                    onClick = onOpenProfile,
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xEB25160A),
                    border = androidx.compose.foundation.BorderStroke(1.8.dp, Color(0xFFF59E0B)),
                    shadowElevation = 6.dp
                ) {
                    Row(
                        modifier = Modifier.padding(start = 6.dp, end = 12.dp, top = 5.dp, bottom = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(contentAlignment = Alignment.BottomCenter) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF3B200E),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFDE047)),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = null,
                                        tint = Color(0xFFFDE047),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFF59E0B)
                            ) {
                                Text(
                                    text = "16",
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Text(
                                text = userData.playerName.ifBlank { StringsHelper.get("tab_profile", lang) },
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            // Energy / XP Progress Bar
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFF140B04),
                                border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFFF59E0B).copy(alpha = 0.7f)),
                                modifier = Modifier.width(95.dp).height(8.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(0.68f)
                                            .clip(RoundedCornerShape(4.dp))
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

                // TOP-CENTER: Brand Banner Logo "CACING BESAR BATTLE"
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xF0201207),
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFACC15)),
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CACING BESAR",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFFDE047),
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "BATTLE",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF38BDF8),
                            letterSpacing = 1.sp
                        )
                    }
                }

                // TOP-RIGHT: Pure Gold Coins Pill (NON-CLICKABLE per user request!)
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xEB25160A),
                    border = androidx.compose.foundation.BorderStroke(1.8.dp, Color(0xFFFACC15)),
                    shadowElevation = 6.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(19.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = java.text.NumberFormat.getNumberInstance(java.util.Locale.US).format(userData.coins),
                            color = Color(0xFFFEF08A),
                            fontWeight = FontWeight.Black,
                            fontSize = 13.5.sp
                        )
                    }
                }
            }

            // MIDDLE AREA: Left Quick Events | Center Battle Hub | Right Game Features
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // LEFT SIDEBAR: Events & Free Bonuses (Daily Event, Lucky Bonus, Piggy Bank)
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 1. Calendar / 15-Hari Daily Login Gift ("Event 15 Hari")
                    EventPillButton(
                        icon = Icons.Default.CardGiftcard,
                        iconTint = Color(0xFF38BDF8),
                        title = "Event 15 Hari",
                        badge = if (userData.canClaimDailyToday) "KLAIM!" else "Harian",
                        isHighlight = userData.canClaimDailyToday,
                        onClick = { showDailyLoginDialog = true }
                    )

                    // 2. Lucky Bonus Button
                    val canClaimBonus = userData.canClaimLuckyBonus
                    val bonusHours = (userData.millisUntilNextBonus / (1000 * 60 * 60)).toInt()
                    val bonusMins = ((userData.millisUntilNextBonus / (1000 * 60)) % 60).toInt()
                    EventPillButton(
                        icon = Icons.Default.Star,
                        iconTint = Color(0xFFFDE047),
                        title = "Bonus Hoki",
                        badge = if (canClaimBonus) "KLAIM!" else "${bonusHours}j ${bonusMins}m",
                        isHighlight = canClaimBonus,
                        onClick = { showBonusDialog = true }
                    )

                    // 3. Piggy Bank Button
                    EventPillButton(
                        icon = Icons.Default.MonetizationOn,
                        iconTint = Color(0xFFFFD700),
                        title = "Celengan",
                        badge = "${userData.piggyBankCoins}",
                        isHighlight = userData.piggyBankCoins >= 1000,
                        onClick = { showPiggyDialog = true }
                    )
                }

                // CENTER BATTLE HUB: Mode Selector, Difficulty Selector, Play Button & Wardrobe
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 1. Game Mode Selector Row
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xEB201207),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFF59E0B).copy(alpha = 0.7f)),
                        shadowElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            ModeChip(
                                title = "Arena Luar",
                                isSelected = selectedMode == GameMode.OPEN_ARENA,
                                onClick = { selectedMode = GameMode.OPEN_ARENA }
                            )
                            ModeChip(
                                title = "Time Attack",
                                isSelected = selectedMode == GameMode.TIME_ATTACK,
                                onClick = { selectedMode = GameMode.TIME_ATTACK }
                            )
                            ModeChip(
                                title = "Battle Royale",
                                isSelected = selectedMode == GameMode.BATTLE_ROYALE,
                                onClick = { selectedMode = GameMode.BATTLE_ROYALE }
                            )
                        }
                    }

                    // 2. Difficulty Level Selector Row (Mudah, Normal, Sulit - requested by user)
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xEB201207),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFFACC15).copy(alpha = 0.8f)),
                        shadowElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Tingkat AI:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFCBD5E1)
                            )

                            DifficultyChip(
                                label = "MUDAH",
                                badgeColor = Color(0xFF22C55E),
                                isSelected = userData.gameDifficulty == GameDifficulty.EASY,
                                onClick = { onSetGameDifficulty(GameDifficulty.EASY) }
                            )
                            DifficultyChip(
                                label = "NORMAL",
                                badgeColor = Color(0xFFFACC15),
                                isSelected = userData.gameDifficulty == GameDifficulty.NORMAL,
                                onClick = { onSetGameDifficulty(GameDifficulty.NORMAL) }
                            )
                            DifficultyChip(
                                label = "SULIT",
                                badgeColor = Color(0xFFEF4444),
                                isSelected = userData.gameDifficulty == GameDifficulty.HARD,
                                onClick = { onSetGameDifficulty(GameDifficulty.HARD) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // 3. GIANT "BERTEMPUR!" PLAY BUTTON
                    val playInteractionSource = remember { MutableInteractionSource() }
                    val isPlayPressed by playInteractionSource.collectIsPressedAsState()
                    val playScale by animateFloatAsState(
                        targetValue = if (isPlayPressed) 0.92f else pulseScale,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "playScale"
                    )

                    Surface(
                        onClick = { onPlayClick(selectedMode) },
                        interactionSource = playInteractionSource,
                        shape = RoundedCornerShape(26.dp),
                        color = Color(0xFF16A34A),
                        border = androidx.compose.foundation.BorderStroke(3.5.dp, Color(0xFFFACC15)),
                        shadowElevation = if (isPlayPressed) 4.dp else 10.dp,
                        modifier = Modifier
                            .scale(playScale)
                            .width(225.dp)
                            .height(54.dp)
                            .testTag("play_button")
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = StringsHelper.get("battle_btn", lang),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    // 4. "Lemari Skin Cacing" Button
                    Surface(
                        onClick = onOpenWardrobe,
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFF0284C7),
                        border = androidx.compose.foundation.BorderStroke(1.8.dp, Color(0xFFF59E0B)),
                        shadowElevation = 5.dp,
                        modifier = Modifier
                            .width(175.dp)
                            .height(36.dp)
                            .testTag("btn_wardrobe")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(Icons.Default.Palette, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = StringsHelper.get("wardrobe_btn", lang),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }

                // RIGHT SIDEBAR: Records, Upgrades, Settings, Share
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Leaderboard / Rekor
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
                    // Settings / Pengaturan
                    CartoonSquareButton(
                        icon = Icons.Default.Settings,
                        testTag = "settings_button",
                        onClick = onOpenSettings
                    )
                    // Share Profil
                    CartoonSquareButton(
                        icon = Icons.Default.Share,
                        testTag = "btn_share",
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Ayo tanding di Cacing Besar Battle IO! Skor Tertinggi: ${userData.highScore} dengan ${userData.totalKills} cacing dikalahkan!"
                                )
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, StringsHelper.get("share_profile", lang)))
                        }
                    )
                }
            }

            // BOTTOM BAR: Footer note (Crisp, clean and minimal)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0x991E1208),
                    modifier = Modifier.padding(bottom = 2.dp)
                ) {
                    Text(
                        text = "Cacing Besar Battle • Edisi Arena Luar Bebas Lag",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xBBFDE047),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
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
                    showDailyLoginDialog = false
                },
                onDismiss = {
                    showDailyLoginDialog = false
                }
            )
        }

        // Lucky Bonus Dialog
        if (showBonusDialog) {
            LuckyBonusDialog(
                userData = userData,
                onClaimBonus = {
                    onClaimLuckyBonus(750)
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

// MODERN EVENT PILL BUTTON FOR SIDEBAR
@Composable
private fun EventPillButton(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    badge: String,
    isHighlight: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "eventPillScale"
    )

    Surface(
        onClick = onClick,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(14.dp),
        color = if (isHighlight) Color(0xEB3F1A08) else Color(0xEB25160A),
        border = androidx.compose.foundation.BorderStroke(
            1.8.dp,
            if (isHighlight) Color(0xFFFACC15) else Color(0xFF78350F)
        ),
        shadowElevation = if (isHighlight) 6.dp else 3.dp,
        modifier = Modifier
            .width(82.dp)
            .scale(scale)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 5.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1
            )
            Text(
                text = badge,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Black,
                color = if (isHighlight) Color(0xFF4ADE80) else Color(0xFFFEF08A)
            )
        }
    }
}

// GAME MODE CHIP
@Composable
private fun ModeChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) Color(0xFFF59E0B) else Color.Transparent,
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFFDE047)) else null
    ) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
            color = if (isSelected) Color.Black else Color(0xFFCBD5E1),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

// DIFFICULTY CHIP
@Composable
private fun DifficultyChip(
    label: String,
    badgeColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "diffScale"
    )

    Surface(
        onClick = onClick,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) badgeColor.copy(alpha = 0.28f) else Color(0x33000000),
        border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 1.8.dp else 1.dp,
            if (isSelected) badgeColor else Color(0x5564748B)
        ),
        modifier = Modifier.scale(scale)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(badgeColor)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                color = if (isSelected) Color.White else Color(0xFFA0AEC0)
            )
        }
    }
}

// WARM CARTOON SQUARE BUTTON WITH ANIMATED TACTILE BOUNCE & GLOW
@Composable
private fun CartoonSquareButton(
    icon: ImageVector,
    testTag: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "buttonScale"
    )

    Surface(
        onClick = onClick,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(14.dp),
        color = if (isPressed) Color(0xFF3E2211) else Color(0xEB28170B),
        border = androidx.compose.foundation.BorderStroke(
            2.dp,
            if (isPressed) Color(0xFFFDE047) else Color(0xFFF59E0B)
        ),
        shadowElevation = if (isPressed) 1.dp else 6.dp,
        modifier = Modifier
            .size(44.dp)
            .scale(scale)
            .testTag(testTag)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isPressed) Color(0xFFFFF9C4) else Color(0xFFFDE047),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
