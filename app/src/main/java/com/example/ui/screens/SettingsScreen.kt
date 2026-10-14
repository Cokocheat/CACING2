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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.OpenWith
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.ControlMode
import com.example.data.StringsHelper
import com.example.data.UserGameData
import com.example.model.MapTheme

import com.example.data.JoystickMode

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
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val lang = userData.language
    var nameInput by remember(userData.playerName) { mutableStateOf(userData.playerName) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1322))
            .testTag("settings_screen")
    ) {
        // TOP APP BAR
        Surface(
            color = Color(0xFF161B2E),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3454)),
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp, start = 12.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("settings_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = StringsHelper.get("cancel", lang),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = StringsHelper.get("settings", lang).uppercase(),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = StringsHelper.get("settings_subtitle", lang),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFA0AEC0)
                    )
                }
            }
        }

        // SINGLE VERTICAL SCROLLING COLUMN (No 2 columns / no 2 rows grid!)
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 760.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            // 1. TATA LETAK KONTROL (Direct Visual Drag & Drop Layout Editor)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF171D33),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF4ADE80)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0x334ADE80),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Tune, contentDescription = null, tint = Color(0xFF4ADE80), modifier = Modifier.size(20.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = StringsHelper.get("edit_layout", lang),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color(0xFF4ADE80),
                                        fontWeight = FontWeight.Black
                                    )
                                    Text(
                                        text = "Atur langsung tombol joystick & boost dengan menyentuh layar",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFA0AEC0)
                                    )
                                }
                            }

                            OutlinedButton(
                                onClick = onResetControls,
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF9100))
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(StringsHelper.get("reset_layout", lang), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Big Button to open Direct Drag & Drop Editor
                        Button(
                            onClick = onOpenLayoutEditor,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4ADE80)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .shadow(8.dp, RoundedCornerShape(14.dp), spotColor = Color(0xFF4ADE80))
                        ) {
                            Icon(Icons.Default.OpenWith, contentDescription = null, tint = Color.Black, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "BUKA EDITOR TATA LETAK TOMBOL (GESER LANGSUNG)",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.Black,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Current Position Stats Pill
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF101424),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF283252)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Posisi Joystick: ${userData.joystickOffsetX}dp kiri, ${userData.joystickOffsetY}dp bawah (${(userData.joystickScale * 100).toInt()}%)",
                                    fontSize = 11.sp,
                                    color = Color(0xFF81C784)
                                )
                                Text(
                                    text = "Tombol Boost: ${userData.boostOffsetX}dp kanan, ${userData.boostOffsetY}dp bawah (${(userData.boostScale * 100).toInt()}%)",
                                    fontSize = 11.sp,
                                    color = Color(0xFFFFB74D)
                                )
                            }
                        }
                    }
                }
            }

            // 2. PENGATURAN BAHASA (Indonesia, English, Korean, Turkish, Arabic, Vietnamese, Taiwan)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF171D33),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF353D63)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0x3300E5FF),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Language, contentDescription = null, tint = Color(0xFF00E5FF), modifier = Modifier.size(20.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = StringsHelper.get("language", lang),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFF00E5FF),
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = StringsHelper.get("select_language_desc", lang),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFA0AEC0)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Languages Grid (6 languages: 3 per row)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AppLanguage.entries.take(3).forEach { appLang ->
                                val isSelected = userData.language == appLang
                                Surface(
                                    onClick = { onSetLanguage(appLang) },
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (isSelected) Color(0xFF00E5FF) else Color(0xFF222944),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) Color(0xFF00E5FF) else Color(0xFF353D63)
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (isSelected) Color.Black.copy(alpha = 0.25f) else Color(0xFF141A30),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Color.Black.copy(alpha = 0.35f) else Color(0xFF00E5FF).copy(alpha = 0.5f))
                                        ) {
                                            Text(
                                                text = appLang.badgeCode,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Black,
                                                color = if (isSelected) Color.Black else Color(0xFF00E5FF),
                                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(5.dp))
                                        Text(
                                            text = appLang.displayName,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                            color = if (isSelected) Color.Black else Color.White
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AppLanguage.entries.drop(3).forEach { appLang ->
                                val isSelected = userData.language == appLang
                                Surface(
                                    onClick = { onSetLanguage(appLang) },
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (isSelected) Color(0xFF00E5FF) else Color(0xFF222944),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) Color(0xFF00E5FF) else Color(0xFF353D63)
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (isSelected) Color.Black.copy(alpha = 0.25f) else Color(0xFF141A30),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Color.Black.copy(alpha = 0.35f) else Color(0xFF00E5FF).copy(alpha = 0.5f))
                                        ) {
                                            Text(
                                                text = appLang.badgeCode,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Black,
                                                color = if (isSelected) Color.Black else Color(0xFF00E5FF),
                                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(5.dp))
                                        Text(
                                            text = appLang.displayName,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                            color = if (isSelected) Color.Black else Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 3. PENGATURAN MODE JOYSTICK (Mode Diam vs Mode Bergerak)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF171D33),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF353D63)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0x334ADE80),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Tune, contentDescription = null, tint = Color(0xFF4ADE80), modifier = Modifier.size(20.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = StringsHelper.get("joystick_settings", lang),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFF4ADE80),
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = StringsHelper.get("joystick_mode_dynamic_desc", lang),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFA0AEC0)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            // Mode Diam (Static/Fixed)
                            val isStatic = userData.joystickMode == JoystickMode.STATIC
                            Surface(
                                onClick = { onSetJoystickMode(JoystickMode.STATIC) },
                                shape = RoundedCornerShape(14.dp),
                                color = if (isStatic) Color(0xFF26354D) else Color(0xFF1B2138),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (isStatic) 2.dp else 1.dp,
                                    color = if (isStatic) Color(0xFF4ADE80) else Color(0xFF2C3454)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                        Icon(
                                            imageVector = Icons.Default.PinDrop,
                                            contentDescription = null,
                                            tint = if (isStatic) Color(0xFF4ADE80) else Color(0xFFA0AEC0),
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = StringsHelper.get("joystick_mode_static", lang),
                                                style = MaterialTheme.typography.titleSmall,
                                                color = if (isStatic) Color(0xFF4ADE80) else Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = StringsHelper.get("joystick_mode_static_desc", lang),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color(0xFFA0AEC0),
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                    if (isStatic) {
                                        Surface(
                                            shape = CircleShape,
                                            color = Color(0xFF4ADE80),
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }
                                }
                            }

                            // Mode Bergerak (Dynamic/Floating - NO EMOJI)
                            val isDynamic = userData.joystickMode == JoystickMode.DYNAMIC
                            Surface(
                                onClick = { onSetJoystickMode(JoystickMode.DYNAMIC) },
                                shape = RoundedCornerShape(14.dp),
                                color = if (isDynamic) Color(0xFF26354D) else Color(0xFF1B2138),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (isDynamic) 2.dp else 1.dp,
                                    color = if (isDynamic) Color(0xFF4ADE80) else Color(0xFF2C3454)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                        Icon(
                                            imageVector = Icons.Default.TouchApp,
                                            contentDescription = null,
                                            tint = if (isDynamic) Color(0xFF4ADE80) else Color(0xFFA0AEC0),
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = StringsHelper.get("joystick_mode_dynamic", lang),
                                                style = MaterialTheme.typography.titleSmall,
                                                color = if (isDynamic) Color(0xFF4ADE80) else Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = StringsHelper.get("joystick_mode_dynamic_desc", lang),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color(0xFFA0AEC0),
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                    if (isDynamic) {
                                        Surface(
                                            shape = CircleShape,
                                            color = Color(0xFF4ADE80),
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
            }

            // 4. MODE KENDALI (Joystick vs Touch Follow)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF171D33),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF353D63)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0x334ADE80),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Gamepad, contentDescription = null, tint = Color(0xFF4ADE80), modifier = Modifier.size(20.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = StringsHelper.get("control_mode", lang),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFF4ADE80),
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            FilterChip(
                                selected = userData.controlMode == ControlMode.JOYSTICK,
                                onClick = { onSetControlMode(ControlMode.JOYSTICK) },
                                label = { Text(StringsHelper.get("virtual_joystick", lang)) },
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
                                label = { Text(StringsHelper.get("touch_follow", lang)) },
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
                            text = StringsHelper.get(if (userData.controlMode == ControlMode.JOYSTICK) "virtual_joystick_desc" else "touch_follow_desc", lang),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFA0AEC0)
                        )
                    }
                }
            }

            // 5. AUDIO & HAPTICS
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF171D33),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF353D63)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0x33FF9100),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Color(0xFFFF9100), modifier = Modifier.size(20.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "AUDIO & EFEK GETARAN",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFFFF9100),
                                fontWeight = FontWeight.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(StringsHelper.get("sound", lang), style = MaterialTheme.typography.bodyLarge, color = Color.White)
                            }
                            Switch(
                                checked = userData.soundEnabled,
                                onCheckedChange = onToggleSound,
                                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF4ADE80), checkedTrackColor = Color(0x664ADE80))
                            )
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
                                Text(StringsHelper.get("vibration", lang), style = MaterialTheme.typography.bodyLarge, color = Color.White)
                            }
                            Switch(
                                checked = userData.vibrationEnabled,
                                onCheckedChange = onToggleVibration,
                                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFFF9100), checkedTrackColor = Color(0x66FF9100))
                            )
                        }
                    }
                }
            }

            // 6. PROFIL PEMAIN (Name)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF171D33),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF353D63)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0x33FFD700),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(20.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = StringsHelper.get("player_name", lang),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFFFFD700),
                                fontWeight = FontWeight.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = nameInput,
                            onValueChange = {
                                nameInput = it
                                onUpdateName(it)
                            },
                            label = { Text(StringsHelper.get("player_nickname", lang)) },
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

            // 7. TENTANG GAME & ARENA LUAR INFO
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF171D33),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3454)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFA0AEC0))
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = StringsHelper.get("version_title", lang),
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = StringsHelper.get("version_info", lang),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFA0AEC0)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
