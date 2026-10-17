package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.OpenWith
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.ControlMode
import com.example.data.JoystickMode
import com.example.data.StringsHelper
import com.example.data.UserGameData
import com.example.model.MapTheme

@Composable
fun SettingsScreen(
    userData: UserGameData,
    onSetControlMode: (ControlMode) -> Unit,
    onSetJoystickMode: (JoystickMode) -> Unit,
    onToggleSound: (Boolean) -> Unit,
    onToggleVibration: (Boolean) -> Unit,
    onUpdateName: (String) -> Unit,
    onOpenLayoutEditor: () -> Unit,
    onResetControls: () -> Unit,
    onSetLanguage: (AppLanguage) -> Unit,
    onSetMapTheme: (MapTheme) -> Unit = {},
    onSetSoundVolume: (Int) -> Unit = {},
    onSetMusicVolume: (Int) -> Unit = {},
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val lang = userData.language
    var selectedTab by remember { mutableIntStateOf(0) }
    var nameInput by remember(userData.playerName) { mutableStateOf(userData.playerName) }

    val tabs = remember {
        listOf(
            SettingsTabItem("Opsi", Icons.Default.Settings),
            SettingsTabItem("Kontrol", Icons.Default.Gamepad),
            SettingsTabItem("Arena", Icons.Default.Image),
            SettingsTabItem("Makanan", Icons.Default.Fastfood),
            SettingsTabItem("Profil", Icons.Default.Person),
            SettingsTabItem("Bahasa", Icons.Default.Language)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B4282)) // Royal Blue Game Backdrop like screenshot
            .testTag("settings_screen")
    ) {
        // Decorative background worm silhouette motifs
        Canvas(modifier = Modifier.fillMaxSize()) {
            val step = 140.dp.toPx()
            var x = 0f
            while (x < size.width) {
                var y = 0f
                while (y < size.height) {
                    drawCircle(
                        color = Color(0x12FFFFFF),
                        radius = 18.dp.toPx(),
                        center = Offset(x + 20f, y + 20f)
                    )
                    drawCircle(
                        color = Color(0x15FFFFFF),
                        radius = 12.dp.toPx(),
                        center = Offset(x + 46f, y + 20f)
                    )
                    y += step
                }
                x += step
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // TOP HEADER: "Pengaturan" title (Yellow cartoon) + Coins pill + Orange Close [X] Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pengaturan",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFFDE047), // Vibrant cartoon yellow
                    letterSpacing = 0.5.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Coins Pill (Smiling Coin face + amount + blue plus button)
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFF1E293B),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFACC15))
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 8.dp, end = 4.dp, top = 3.dp, bottom = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("😊", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${userData.coins}",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF38BDF8),
                                modifier = Modifier.size(24.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }

                    // Orange Square Close Button [X] (Identical to screenshot)
                    Surface(
                        onClick = onBack,
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF97316), // Vivid orange
                        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFED7AA)),
                        shadowElevation = 6.dp,
                        modifier = Modifier.size(40.dp).testTag("settings_back_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Tutup",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            // HORIZONTAL TABS ROW (Smooth scrollable row so all functional tabs fit perfectly)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.Bottom,
                contentPadding = PaddingValues(horizontal = 2.dp)
            ) {
                items(tabs.size) { index ->
                    val tab = tabs[index]
                    val isSelected = selectedTab == index
                    Surface(
                        onClick = { selectedTab = index },
                        shape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
                        color = if (isSelected) Color(0xFF3B5DA8) else Color(0xFF24366A),
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp,
                            if (isSelected) Color(0xFF60A5FA) else Color(0xFF1D2D5B)
                        ),
                        modifier = Modifier
                            .height(if (isSelected) 46.dp else 40.dp)
                            .testTag("settings_tab_$index")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                tint = if (isSelected) Color(0xFFFDE047) else Color(0xFF94A3B8),
                                modifier = Modifier.size(20.dp)
                            )
                            if (isSelected) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = tab.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFFDE047)
                                )
                            }
                        }
                    }
                }
            }

            // MAIN SETTINGS CARD CONTAINER (With Smooth Animated Tab Transitions)
            Surface(
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp, topEnd = 20.dp),
                color = Color(0xFF3B5DA8), // Connected tab body color
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF60A5FA)),
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally(animationSpec = tween(280)) { fullWidth -> fullWidth } + fadeIn(tween(200)))
                                .togetherWith(slideOutHorizontally(animationSpec = tween(280)) { fullWidth -> -fullWidth } + fadeOut(tween(200)))
                        } else {
                            (slideInHorizontally(animationSpec = tween(280)) { fullWidth -> -fullWidth } + fadeIn(tween(200)))
                                .togetherWith(slideOutHorizontally(animationSpec = tween(280)) { fullWidth -> fullWidth } + fadeOut(tween(200)))
                        }
                    },
                    label = "SettingsTabTransition"
                ) { targetIndex ->
                    when (targetIndex) {
                        0 -> OpsiTabContent(
                            userData = userData,
                            onSetSoundVolume = onSetSoundVolume,
                            onSetMusicVolume = onSetMusicVolume,
                            onToggleSound = onToggleSound
                        )
                        1 -> KontrolTabContent(
                            userData = userData,
                            lang = lang,
                            onSetControlMode = onSetControlMode,
                            onSetJoystickMode = onSetJoystickMode,
                            onOpenLayoutEditor = onOpenLayoutEditor,
                            onResetControls = onResetControls
                        )
                        2 -> ArenaTabContent(
                            userData = userData,
                            onSetMapTheme = onSetMapTheme
                        )
                        3 -> MakananTabContent()
                        4 -> ProfilTabContent(
                            userData = userData,
                            nameInput = nameInput,
                            onNameChange = {
                                nameInput = it
                                onUpdateName(it)
                            }
                        )
                        5 -> BahasaTabContent(
                            userData = userData,
                            onSetLanguage = onSetLanguage
                        )
                    }
                }
            }
        }
    }
}

private data class SettingsTabItem(
    val title: String,
    val icon: ImageVector
)

// TAB 0: OPSI (EXACTLY AS IN SCREENSHOT: SEGMENTED VOLUME BARS, SOCIALS, PRIVACY, TERMS)
@Composable
private fun OpsiTabContent(
    userData: UserGameData,
    onSetSoundVolume: (Int) -> Unit,
    onSetMusicVolume: (Int) -> Unit,
    onToggleSound: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 18.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // 1. Musik Volume Bar
            SegmentedVolumeRow(
                label = "Musik",
                volume = userData.musicVolume,
                icon = Icons.Default.MusicNote,
                onDecrease = { onSetMusicVolume((userData.musicVolume - 1).coerceAtLeast(0)) },
                onIncrease = { onSetMusicVolume((userData.musicVolume + 1).coerceAtMost(10)) }
            )

            // 2. Suara Volume Bar
            SegmentedVolumeRow(
                label = "Suara",
                volume = userData.soundVolume,
                icon = Icons.Default.VolumeUp,
                onDecrease = { onSetSoundVolume((userData.soundVolume - 1).coerceAtLeast(0)) },
                onIncrease = { onSetSoundVolume((userData.soundVolume + 1).coerceAtMost(10)) }
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle
            Text(
                text = "Jangan lewatkan acara dan hadiah spesial!",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            // Social Buttons Row (Facebook, TikTok, YouTube like screenshot)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Facebook
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF1877F2),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("f", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
                    }
                }
                // TikTok
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF0F172A),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF00F2FE)),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🎵", fontSize = 22.sp)
                    }
                }
                // YouTube
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFFF0000),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Privacy & Terms of Service Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF4C6BB5),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF8BB4F8)),
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Kebijakan Privasi",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF4C6BB5),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF8BB4F8)),
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Ketentuan Layanan",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "v1.3.0, id, Nana Tayantuh • Worms Zone IO Edition",
            fontSize = 11.sp,
            color = Color(0xBBFFFFFF),
            textAlign = TextAlign.Center
        )
    }
}

// 10-SEGMENTED COLOR VOLUME ROW (Red -> Orange -> Yellow like screenshot)
@Composable
private fun SegmentedVolumeRow(
    label: String,
    volume: Int, // 0 to 10
    icon: ImageVector,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Label
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            color = Color.White,
            modifier = Modifier.width(80.dp)
        )

        // Minus Button
        Surface(
            onClick = onDecrease,
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF294079),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF60A5FA)),
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Remove, contentDescription = "Kurang", tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // 10 Segmented Meter in dark track
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF1B2A52),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF2D4685)),
            modifier = Modifier
                .width(220.dp)
                .height(28.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp, vertical = 3.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 1..10) {
                    val isActive = i <= volume
                    val segColor = when {
                        !isActive -> Color(0xFF2A3A63) // Inactive dark gray-blue
                        i <= 3 -> Color(0xFFEF4444)    // Red
                        i <= 6 -> Color(0xFFF97316)    // Orange
                        else -> Color(0xFFEAB308)      // Yellow/Gold
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(2.dp))
                            .background(segColor)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Plus Button
        Surface(
            onClick = onIncrease,
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF294079),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF60A5FA)),
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Add, contentDescription = "Tambah", tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Volume Icon (Note or Speaker)
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

// TAB 1: KONTROL (PILIHAN MODE JOYSTICK & TOUCH, BUTTON EDITOR)
@Composable
private fun KontrolTabContent(
    userData: UserGameData,
    lang: AppLanguage,
    onSetControlMode: (ControlMode) -> Unit,
    onSetJoystickMode: (JoystickMode) -> Unit,
    onOpenLayoutEditor: () -> Unit,
    onResetControls: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("PILIHAN MODE KENDALI", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFFFDE047))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val isJoy = userData.controlMode == ControlMode.JOYSTICK
            Surface(
                onClick = { onSetControlMode(ControlMode.JOYSTICK) },
                shape = RoundedCornerShape(12.dp),
                color = if (isJoy) Color(0xFF223668) else Color(0xFF1E2A4F),
                border = androidx.compose.foundation.BorderStroke(2.dp, if (isJoy) Color(0xFFFDE047) else Color(0xFF334B85)),
                modifier = Modifier.weight(1f)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Gamepad, contentDescription = null, tint = if (isJoy) Color(0xFFFDE047) else Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(StringsHelper.get("virtual_joystick", lang), fontWeight = FontWeight.Bold, color = if (isJoy) Color(0xFFFDE047) else Color.White, fontSize = 13.sp)
                        Text("Analog virtual", fontSize = 11.sp, color = Color(0xBBFFFFFF))
                    }
                }
            }

            val isTouch = userData.controlMode == ControlMode.TOUCH_FOLLOW
            Surface(
                onClick = { onSetControlMode(ControlMode.TOUCH_FOLLOW) },
                shape = RoundedCornerShape(12.dp),
                color = if (isTouch) Color(0xFF223668) else Color(0xFF1E2A4F),
                border = androidx.compose.foundation.BorderStroke(2.dp, if (isTouch) Color(0xFFFDE047) else Color(0xFF334B85)),
                modifier = Modifier.weight(1f)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.TouchApp, contentDescription = null, tint = if (isTouch) Color(0xFFFDE047) else Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(StringsHelper.get("touch_follow", lang), fontWeight = FontWeight.Bold, color = if (isTouch) Color(0xFFFDE047) else Color.White, fontSize = 13.sp)
                        Text("Ikuti sentuhan", fontSize = 11.sp, color = Color(0xBBFFFFFF))
                    }
                }
            }
        }

        if (userData.controlMode == ControlMode.JOYSTICK) {
            Text("GAYA JOYSTICK", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFFFDE047))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val isStatic = userData.joystickMode == JoystickMode.STATIC
                Surface(
                    onClick = { onSetJoystickMode(JoystickMode.STATIC) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isStatic) Color(0xFF223668) else Color(0xFF1E2A4F),
                    border = androidx.compose.foundation.BorderStroke(2.dp, if (isStatic) Color(0xFF4ADE80) else Color(0xFF334B85)),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PinDrop, contentDescription = null, tint = if (isStatic) Color(0xFF4ADE80) else Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Mode Diam", fontWeight = FontWeight.Bold, color = if (isStatic) Color(0xFF4ADE80) else Color.White, fontSize = 13.sp)
                            Text("Tetap di pojok kiri", fontSize = 11.sp, color = Color(0xBBFFFFFF))
                        }
                    }
                }

                val isDynamic = userData.joystickMode == JoystickMode.DYNAMIC
                Surface(
                    onClick = { onSetJoystickMode(JoystickMode.DYNAMIC) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isDynamic) Color(0xFF223668) else Color(0xFF1E2A4F),
                    border = androidx.compose.foundation.BorderStroke(2.dp, if (isDynamic) Color(0xFF4ADE80) else Color(0xFF334B85)),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Tune, contentDescription = null, tint = if (isDynamic) Color(0xFF4ADE80) else Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Mode Dinamis", fontWeight = FontWeight.Bold, color = if (isDynamic) Color(0xFF4ADE80) else Color.White, fontSize = 13.sp)
                            Text("Mengikuti jari Anda", fontSize = 11.sp, color = Color(0xBBFFFFFF))
                        }
                    }
                }
            }
        }

        Button(
            onClick = onOpenLayoutEditor,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4ADE80)),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Icon(Icons.Default.OpenWith, contentDescription = null, tint = Color.Black)
            Spacer(modifier = Modifier.width(8.dp))
            Text("SESUAIKAN TATA LETAK TOMBOL (DRAG & DROP)", fontWeight = FontWeight.Black, color = Color.Black)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Joy (${userData.joystickOffsetX}dp, ${userData.joystickOffsetY}dp) • Boost (${userData.boostOffsetX}dp, ${userData.boostOffsetY}dp)",
                color = Color(0xCCFFFFFF),
                fontSize = 12.sp
            )
            OutlinedButton(
                onClick = onResetControls,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFF97316)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF97316))
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Reset Posisi", fontSize = 12.sp)
            }
        }
    }
}

// TAB 2: ARENA (PILIHAN TEMA LATAR BELAKANG)
@Composable
private fun ArenaTabContent(
    userData: UserGameData,
    onSetMapTheme: (MapTheme) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("PILIHAN TEMA LATAR BELAKANG ARENA", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFFFDE047))
        Text("Warna dan motif catur arena berlaku untuk menu utama dan saat bermain di game.", fontSize = 12.sp, color = Color.White)

        MapTheme.entries.forEach { theme ->
            val isSelected = userData.mapTheme == theme
            Surface(
                onClick = { onSetMapTheme(theme) },
                shape = RoundedCornerShape(14.dp),
                color = if (isSelected) Color(0xFF223668) else Color(0xFF1E2A4F),
                border = androidx.compose.foundation.BorderStroke(2.dp, if (isSelected) Color(0xFFFDE047) else Color(0xFF334B85)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(theme.bgColor)
                                .border(2.dp, theme.borderColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(theme.icon, fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(theme.title, fontWeight = FontWeight.Black, color = if (isSelected) Color(0xFFFDE047) else Color.White, fontSize = 14.sp)
                            Text(theme.description, fontSize = 11.sp, color = Color(0xBBFFFFFF))
                        }
                    }
                    if (isSelected) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFFDE047),
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// TAB 3: MAKANAN (INFO SNACKS & SPELLS)
@Composable
private fun MakananTabContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("PANDUAN MAKANAN & SPELL", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFFFDE047))

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFF24366A),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3B5DA8)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("🍔 Makanan Lezat Arena:", fontWeight = FontWeight.Black, color = Color.White)
                Text("• Burger, Keju, Roti, Jamur, Sosis, dan Daging Ayam memberikan massa untuk cacingmu bertambah panjang.", fontSize = 12.sp, color = Color(0xDDFFFFFF))
                Text("• Daging Korban Cacing Mati: Menghasilkan santapan berenergi tinggi dengan aura kilau biru terang di tepinya!", fontSize = 12.sp, color = Color(0xFF38BDF8))
            }
        }

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFF24366A),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3B5DA8)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("🧲 Spell & Ramuan Ajaib:", fontWeight = FontWeight.Black, color = Color(0xFFFDE047))
                Text("• 🧲 Magnet: Menyedot seluruh makanan di sekitar tanpa harus disentuh.", fontSize = 12.sp, color = Color.White)
                Text("• ⚡ Kilat Lari: Lari kencang tanpa mengurangi panjang badan cacing.", fontSize = 12.sp, color = Color.White)
                Text("• ✖️2 Poin Ganda: Menggandakan skor setiap makanan yang dimakan.", fontSize = 12.sp, color = Color.White)
                Text("• 🔍 Mata Elang / Zoom: Memperluas jarak pandang kamera di arena.", fontSize = 12.sp, color = Color.White)
            }
        }
    }
}

// TAB 4: PROFIL
@Composable
private fun ProfilTabContent(
    userData: UserGameData,
    nameInput: String,
    onNameChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("PROFIL PEMAIN", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFFFDE047))

        OutlinedTextField(
            value = nameInput,
            onValueChange = onNameChange,
            label = { Text("Nama Panggilan") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFDE047),
                unfocusedBorderColor = Color(0xFF60A5FA),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = Color(0xFFFDE047),
                unfocusedLabelColor = Color(0xBBFFFFFF)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFF24366A),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Statistik Permainan:", fontWeight = FontWeight.Black, color = Color.White)
                Text("• Skor Tertinggi: ${userData.highScore}", fontSize = 13.sp, color = Color(0xFFFFD700))
                Text("• Total Mangsa Dikalahkan: ${userData.totalKills}", fontSize = 13.sp, color = Color(0xFF4ADE80))
                Text("• Total Pertandingan: ${userData.totalGames}", fontSize = 13.sp, color = Color(0xFF00E5FF))
            }
        }
    }
}

// TAB 5: BAHASA
@Composable
private fun BahasaTabContent(
    userData: UserGameData,
    onSetLanguage: (AppLanguage) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("PILIH BAHASA APLIKASI", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFFFDE047))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppLanguage.entries.take(3).forEach { appLang ->
                val isSelected = userData.language == appLang
                Surface(
                    onClick = { onSetLanguage(appLang) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) Color(0xFFFDE047) else Color(0xFF24366A),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, if (isSelected) Color(0xFFFACC15) else Color(0xFF3B5DA8)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(appLang.badgeCode, fontWeight = FontWeight.Black, color = if (isSelected) Color.Black else Color(0xFF38BDF8), fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(appLang.displayName, fontWeight = FontWeight.Bold, color = if (isSelected) Color.Black else Color.White, fontSize = 12.sp)
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppLanguage.entries.drop(3).forEach { appLang ->
                val isSelected = userData.language == appLang
                Surface(
                    onClick = { onSetLanguage(appLang) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) Color(0xFFFDE047) else Color(0xFF24366A),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, if (isSelected) Color(0xFFFACC15) else Color(0xFF3B5DA8)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(appLang.badgeCode, fontWeight = FontWeight.Black, color = if (isSelected) Color.Black else Color(0xFF38BDF8), fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(appLang.displayName, fontWeight = FontWeight.Bold, color = if (isSelected) Color.Black else Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
