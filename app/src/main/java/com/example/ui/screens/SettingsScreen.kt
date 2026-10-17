package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenWith
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
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
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val lang = userData.language
    var nameInput by remember(userData.playerName) { mutableStateOf(userData.playerName) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .testTag("settings_screen")
    ) {
        // TOP APP BAR
        Surface(
            color = Color(0xFF1E293B),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = onBack,
                    shape = CircleShape,
                    color = Color(0xFF0F172A),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF475569)),
                    modifier = Modifier.size(40.dp).testTag("settings_back_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = StringsHelper.get("cancel", lang),
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = StringsHelper.get("settings", lang).uppercase(),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = StringsHelper.get("settings_subtitle", lang),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
        }

        // SCROLLABLE NEAT SETTINGS CONTENT
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 760.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            // 1. PROFIL PEMAIN
            item {
                SettingsCard(
                    title = StringsHelper.get("player_name", lang),
                    icon = Icons.Default.Person,
                    accentColor = Color(0xFFFFD700)
                ) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = {
                            nameInput = it
                            onUpdateName(it)
                        },
                        label = { Text(StringsHelper.get("player_nickname", lang)) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFD700),
                            unfocusedBorderColor = Color(0xFF334155),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color(0xFFFFD700),
                            unfocusedLabelColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("player_name_input")
                    )
                }
            }

            // 2. AUDIO & EFEK GETARAN
            item {
                SettingsCard(
                    title = "AUDIO & GETARAN",
                    icon = Icons.Default.VolumeUp,
                    accentColor = Color(0xFFFF9100)
                ) {
                    // Suara
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0x33FF9100),
                                modifier = Modifier.size(34.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Color(0xFFFF9100), modifier = Modifier.size(18.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(StringsHelper.get("sound", lang), fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
                                Text("Efek suara makan, lari & mati", fontSize = 12.sp, color = Color(0xFF94A3B8))
                            }
                        }
                        Switch(
                            checked = userData.soundEnabled,
                            onCheckedChange = onToggleSound,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF4ADE80),
                                checkedTrackColor = Color(0x664ADE80),
                                uncheckedThumbColor = Color(0xFF94A3B8),
                                uncheckedTrackColor = Color(0xFF334155)
                            ),
                            modifier = Modifier.testTag("sound_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Getaran
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0x3300E5FF),
                                modifier = Modifier.size(34.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Vibration, contentDescription = null, tint = Color(0xFF00E5FF), modifier = Modifier.size(18.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(StringsHelper.get("vibration", lang), fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
                                Text("Sensasi getar haptik saat tabrakan", fontSize = 12.sp, color = Color(0xFF94A3B8))
                            }
                        }
                        Switch(
                            checked = userData.vibrationEnabled,
                            onCheckedChange = onToggleVibration,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF00E5FF),
                                checkedTrackColor = Color(0x6600E5FF),
                                uncheckedThumbColor = Color(0xFF94A3B8),
                                uncheckedTrackColor = Color(0xFF334155)
                            ),
                            modifier = Modifier.testTag("vibration_switch")
                        )
                    }
                }
            }

            // 3. KONTROL & TATA LETAK
            item {
                SettingsCard(
                    title = "KONTROL & TATA LETAK",
                    icon = Icons.Default.Gamepad,
                    accentColor = Color(0xFF4ADE80)
                ) {
                    // Mode Kendali: Joystick vs Touch Follow
                    Text("PILIHAN MODE KENDALI", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFF94A3B8), letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val isJoy = userData.controlMode == ControlMode.JOYSTICK
                        SelectableCard(
                            title = StringsHelper.get("virtual_joystick", lang),
                            subtitle = "Analog virtual",
                            icon = Icons.Default.Gamepad,
                            selected = isJoy,
                            onClick = { onSetControlMode(ControlMode.JOYSTICK) },
                            modifier = Modifier.weight(1f)
                        )

                        val isTouch = userData.controlMode == ControlMode.TOUCH_FOLLOW
                        SelectableCard(
                            title = StringsHelper.get("touch_follow", lang),
                            subtitle = "Ikuti jari",
                            icon = Icons.Default.TouchApp,
                            selected = isTouch,
                            onClick = { onSetControlMode(ControlMode.TOUCH_FOLLOW) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Mode Joystick (Hanya relevan jika Joystick aktif)
                    if (userData.controlMode == ControlMode.JOYSTICK) {
                        Text("GAYA JOYSTICK", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFF94A3B8), letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val isStatic = userData.joystickMode == JoystickMode.STATIC
                            SelectableCard(
                                title = StringsHelper.get("joystick_mode_static", lang),
                                subtitle = "Tetap di pojok",
                                icon = Icons.Default.PinDrop,
                                selected = isStatic,
                                onClick = { onSetJoystickMode(JoystickMode.STATIC) },
                                modifier = Modifier.weight(1f)
                            )

                            val isDynamic = userData.joystickMode == JoystickMode.DYNAMIC
                            SelectableCard(
                                title = StringsHelper.get("joystick_mode_dynamic", lang),
                                subtitle = "Mengikuti sentuhan",
                                icon = Icons.Default.Tune,
                                selected = isDynamic,
                                onClick = { onSetJoystickMode(JoystickMode.DYNAMIC) },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // Button Buka Editor Tata Letak (Drag & Drop)
                    Button(
                        onClick = onOpenLayoutEditor,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4ADE80)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .shadow(6.dp, RoundedCornerShape(14.dp), spotColor = Color(0xFF4ADE80))
                            .testTag("open_layout_editor_button")
                    ) {
                        Icon(Icons.Default.OpenWith, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SESUAIKAN TATA LETAK TOMBOL (DRAG & DROP)",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Posisi: Joy (${userData.joystickOffsetX}dp, ${userData.joystickOffsetY}dp) • Boost (${userData.boostOffsetX}dp, ${userData.boostOffsetY}dp)",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )

                        OutlinedButton(
                            onClick = onResetControls,
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF9100)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF9100).copy(alpha = 0.5f))
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(StringsHelper.get("reset_layout", lang), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // 4. TEMA LATAR BELAKANG ARENA (CERAH & BERMOTIF)
            item {
                SettingsCard(
                    title = "LATAR BELAKANG ARENA",
                    icon = Icons.Default.Palette,
                    accentColor = Color(0xFF00E5FF)
                ) {
                    Text(
                        text = "Pilih tema warna dan motif catur arena untuk gameplay dan menu utama",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(MapTheme.entries) { theme ->
                            val isSelected = userData.mapTheme == theme
                            Surface(
                                onClick = { onSetMapTheme(theme) },
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) Color(0xFF1E293B) else Color(0xFF0F172A),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) Color(0xFF00E5FF) else Color(0xFF334155)
                                ),
                                modifier = Modifier.width(135.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    // Theme color swatch circle with border
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

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = theme.title,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                        color = if (isSelected) Color(0xFF00E5FF) else Color.White,
                                        maxLines = 1
                                    )

                                    if (isSelected) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = Color(0x3300E5FF)
                                        ) {
                                            Text(
                                                text = "AKTIF",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color(0xFF00E5FF),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 5. PILIHAN BAHASA
            item {
                SettingsCard(
                    title = StringsHelper.get("language", lang),
                    icon = Icons.Default.Language,
                    accentColor = Color(0xFF38BDF8)
                ) {
                    Text(
                        text = StringsHelper.get("select_language_desc", lang),
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppLanguage.entries.take(3).forEach { appLang ->
                            LanguagePill(
                                lang = appLang,
                                isSelected = userData.language == appLang,
                                onClick = { onSetLanguage(appLang) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppLanguage.entries.drop(3).forEach { appLang ->
                            LanguagePill(
                                lang = appLang,
                                isSelected = userData.language == appLang,
                                onClick = { onSetLanguage(appLang) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // 6. TENTANG GAME & INFO VERSI
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF1E293B),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0x2238BDF8),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Nana Tayantuh • Worms Zone IO Edition",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Versi 1.3.0 • Dibuat dengan Kotlin Jetpack Compose",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SettingsCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF1E293B),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = accentColor.copy(alpha = 0.2f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = accentColor,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            content()
        }
    }
}

@Composable
private fun SelectableCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = if (selected) Color(0xFF0F172A) else Color(0xFF141F33),
        border = androidx.compose.foundation.BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) Color(0xFF4ADE80) else Color(0xFF334155)
        ),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) Color(0xFF4ADE80) else Color(0xFF94A3B8),
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selected) Color(0xFF4ADE80) else Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )
            }
            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color(0xFF4ADE80),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun LanguagePill(
    lang: AppLanguage,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Color(0xFF0284C7) else Color(0xFF0F172A),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) Color(0xFF38BDF8) else Color(0xFF334155)
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = if (isSelected) Color.White.copy(alpha = 0.25f) else Color(0xFF1E293B)
            ) {
                Text(
                    text = lang.badgeCode,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isSelected) Color.White else Color(0xFF38BDF8),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = lang.displayName,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
