package com.example.ui.game

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.DialogProperties
import com.example.data.AppLanguage
import com.example.data.StringsHelper
import com.example.data.UserGameData

@Composable
fun NeonCompactToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    activeColor: Color = Color(0xFF4ADE80),
    inactiveColor: Color = Color(0xFF242A45)
) {
    val transition = androidx.compose.animation.core.updateTransition(targetState = checked, label = "toggleTrans")
    val thumbOffset by transition.animateDp(
        transitionSpec = { tween(durationMillis = 180) },
        label = "thumbOffset"
    ) { isChecked ->
        if (isChecked) 18.dp else 2.dp
    }

    Box(
        modifier = Modifier
            .size(width = 40.dp, height = 24.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (checked) activeColor.copy(alpha = 0.35f) else inactiveColor)
            .border(
                width = 1.5.dp,
                color = if (checked) activeColor else Color(0xFF4A5578),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 2.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .padding(start = thumbOffset)
                .size(18.dp)
                .clip(CircleShape)
                .background(if (checked) activeColor else Color(0xFF94A3B8))
        )
    }
}

@Composable
fun PauseDialog(
    soundEnabled: Boolean,
    vibrationEnabled: Boolean,
    lang: AppLanguage = AppLanguage.INDONESIAN,
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
                    text = StringsHelper.get("pause_title", lang),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )

                // Sound & Vibration Row with sleek NeonCompactToggle (never clipped or cut off!)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFF202740),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF353D63)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f, fill = false),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (soundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                                    contentDescription = null,
                                    tint = Color(0xFF4ADE80),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = StringsHelper.get("sound", lang),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                            }
                            NeonCompactToggle(
                                checked = soundEnabled,
                                onCheckedChange = onToggleSound,
                                activeColor = Color(0xFF4ADE80)
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFF202740),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF353D63)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f, fill = false),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Vibration,
                                    contentDescription = null,
                                    tint = Color(0xFFFF9100),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = StringsHelper.get("vibration", lang),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                            }
                            NeonCompactToggle(
                                checked = vibrationEnabled,
                                onCheckedChange = onToggleVibration,
                                activeColor = Color(0xFFFF9100)
                            )
                        }
                    }
                }

                // Primary Hero Action: Resume / Lanjutkan (Full Width, never wraps, perfectly centered and balanced!)
                Button(
                    onClick = onResume,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("resume_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4ADE80)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = StringsHelper.get("resume", lang).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black,
                        fontWeight = FontWeight.Black,
                        maxLines = 1,
                        softWrap = false
                    )
                }

                // Secondary Actions Row: Restart & Back to Lobby
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onRestart,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("restart_button"),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFF9100))
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color(0xFFFF9100), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = StringsHelper.get("restart", lang),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFFFF9100),
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }

                    FilledTonalButton(
                        onClick = onQuitToLobby,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("quit_lobby_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(0xFF2C3454))
                    ) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = StringsHelper.get("back_to_menu", lang),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReviveDialog(
    reviveCost: Int,
    playerCoins: Int,
    wormLength: Int,
    lang: AppLanguage = AppLanguage.INDONESIAN,
    onRevive: () -> Unit,
    onGiveUp: () -> Unit
) {
    val canAfford = playerCoins >= reviveCost

    val infiniteTransition = rememberInfiniteTransition(label = "revivePulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Dialog(
        onDismissRequest = onGiveUp,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF141829),
            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFF9100)),
            shadowElevation = 20.dp,
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("revive_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header (NO EMOJI)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color(0xFFFF5252),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = StringsHelper.get("revive_title", lang),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFFFF9100),
                        fontWeight = FontWeight.Black
                    )
                }

                Text(
                    text = StringsHelper.get("revive_desc", lang),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFCBD5E1),
                    textAlign = TextAlign.Center
                )

                // Cost Breakdown Box
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF1E243D),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF353D63)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(StringsHelper.get("worm_length", lang) + ":", style = MaterialTheme.typography.bodySmall, color = Color(0xFFA0AEC0))
                            Text("$wormLength ${StringsHelper.get("segments", lang)}", style = MaterialTheme.typography.titleSmall, color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(StringsHelper.get("your_coins", lang) + ":", style = MaterialTheme.typography.bodySmall, color = Color(0xFFA0AEC0))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("$playerCoins", style = MaterialTheme.typography.titleSmall, color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)
                            }
                        }
                        androidx.compose.material3.HorizontalDivider(color = Color(0xFF353D63), thickness = 1.dp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(StringsHelper.get("revive_cost", lang) + ":", style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = if (canAfford) Color(0xFFFF9100) else Color(0xFFFF5252), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$reviveCost",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (canAfford) Color(0xFFFF9100) else Color(0xFFFF5252),
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }

                if (!canAfford) {
                    Text(
                        text = StringsHelper.get("not_enough_coins", lang),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFFF5252),
                        fontWeight = FontWeight.Bold
                    )
                }

                // Buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onRevive,
                        enabled = canAfford,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .scale(if (canAfford) pulseScale else 1.0f)
                            .testTag("revive_confirm_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF9100),
                            disabledContainerColor = Color(0xFF2C3454)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = if (canAfford) Color.Black else Color.Gray)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${StringsHelper.get("revive_btn", lang)} ($reviveCost)",
                            style = MaterialTheme.typography.labelLarge,
                            color = if (canAfford) Color.Black else Color.Gray,
                            fontWeight = FontWeight.Black
                        )
                    }

                    OutlinedButton(
                        onClick = onGiveUp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("revive_give_up_button"),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF353D63))
                    ) {
                        Text(StringsHelper.get("give_up", lang), style = MaterialTheme.typography.bodyMedium, color = Color(0xFFA0AEC0))
                    }
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
    isVictory: Boolean = false,
    lang: AppLanguage = AppLanguage.INDONESIAN,
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
            border = androidx.compose.foundation.BorderStroke(2.dp, if (isVictory) Color(0xFFFFD700) else Color(0xFFFF3366)),
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
                // Header Loss or Victory Banner (NO EMOJI)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (isVictory) Icons.Default.EmojiEvents else Icons.Default.SentimentVeryDissatisfied,
                        contentDescription = null,
                        tint = if (isVictory) Color(0xFFFFD700) else Color(0xFFFF3366),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isVictory) StringsHelper.get("arena_winner", lang) else StringsHelper.get("game_over", lang),
                        style = MaterialTheme.typography.headlineMedium,
                        color = if (isVictory) Color(0xFFFFD700) else Color(0xFFFF3366),
                        fontWeight = FontWeight.Black
                    )
                }

                Text(
                    text = StringsHelper.get("good_job", lang),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (score >= highScore && score > 0 || isVictory) Color(0xFFFFD700) else Color(0xFFA0AEC0),
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
                            StatRow(title = StringsHelper.get("final_score", lang), value = "$score", icon = Icons.Default.Stars, highlight = true, lang = lang)
                            StatRow(title = StringsHelper.get("high_score", lang), value = "$highScore", icon = Icons.Default.EmojiEvents, highlight = false, lang = lang)
                            StatRow(title = StringsHelper.get("enemies_slain", lang), value = "$kills", icon = Icons.Default.LocalFireDepartment, highlight = false, lang = lang)
                            StatRow(title = StringsHelper.get("final_length", lang), value = "$length ${StringsHelper.get("segments", lang)}", icon = Icons.Default.Timeline, highlight = false, lang = lang)
                            StatRow(title = StringsHelper.get("coins_earned", lang), value = "+$coinsEarned", icon = Icons.Default.MonetizationOn, highlight = true, valueColor = Color(0xFFFFD700), lang = lang)
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
                            Text(StringsHelper.get("play_again", lang), style = MaterialTheme.typography.labelLarge, color = Color.Black, fontWeight = FontWeight.Black)
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
                            Text(StringsHelper.get("back_to_menu", lang), style = MaterialTheme.typography.labelMedium, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DailyLoginDialog(
    userData: UserGameData,
    lang: AppLanguage = userData.language,
    onClaimReward: () -> Unit,
    onDismiss: () -> Unit
) {
    val canClaim = userData.canClaimDailyToday
    val nextDay = userData.nextDailyDay

    val infiniteTransition = rememberInfiniteTransition(label = "loginClaimGlow")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "btnPulse"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF141829),
            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFFD700)),
            shadowElevation = 24.dp,
            modifier = Modifier
                .widthIn(max = 620.dp)
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("daily_login_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header Bar (NO EMOJI)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CardGiftcard,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = StringsHelper.get("daily_title", lang),
                                style = MaterialTheme.typography.titleLarge,
                                color = Color(0xFFFFD700),
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = StringsHelper.get("daily_banner_info", lang),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFA0AEC0)
                            )
                        }
                    }

                    Surface(
                        onClick = onDismiss,
                        shape = CircleShape,
                        color = Color(0xFF2C1E34),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFF5252)),
                        shadowElevation = 6.dp,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = StringsHelper.get("close", lang),
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // 7 Days Horizontal Scrollable Cards
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(UserGameData.DAILY_REWARDS) { reward ->
                        val isClaimed = reward.day <= userData.lastClaimedDay && userData.lastClaimedDay > 0
                        val isCurrentClaimable = reward.day == nextDay && canClaim
                        val isCurrentWaiting = reward.day == nextDay && !canClaim

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = when {
                                isCurrentClaimable -> Color(0xFF2A2345)
                                isClaimed -> Color(0xFF1A2633)
                                else -> Color(0xFF1A1F33)
                            },
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (isCurrentClaimable) 2.dp else 1.dp,
                                color = when {
                                    isCurrentClaimable -> Color(0xFFFFD700)
                                    isClaimed -> Color(0xFF4ADE80)
                                    reward.day == 15 -> Color(0xFFFFD700)
                                    else -> Color(0xFF2C3454)
                                }
                            ),
                            modifier = Modifier.width(125.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                // Day Header
                                Text(
                                    text = if (reward.day == 15) "HARI 15 (FINAL)" else "${StringsHelper.get("day", lang)} ${reward.day}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (reward.day == 15) Color(0xFFFFD700) else if (isCurrentClaimable) Color(0xFFFFD700) else Color(0xFFA0AEC0),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 10.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // Reward Icon
                                val (iconVector, iconTint) = when {
                                    reward.day == 15 -> Pair(Icons.Default.MilitaryTech, Color(0xFFFFD700))
                                    reward.unlockSkin != null -> Pair(Icons.Default.EmojiEvents, Color(0xFF00E5FF))
                                    reward.unlockAccessory != null -> Pair(Icons.Default.Stars, Color(0xFFFF80AB))
                                    else -> Pair(Icons.Default.MonetizationOn, Color(0xFFFFD700))
                                }

                                Icon(
                                    imageVector = iconVector,
                                    contentDescription = null,
                                    tint = iconTint,
                                    modifier = Modifier.size(26.dp)
                                )

                                Spacer(modifier = Modifier.height(3.dp))

                                // Coins Amount
                                Text(
                                    text = "+${reward.coins}",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color(0xFFFFD700),
                                    fontWeight = FontWeight.Black
                                )

                                // Item reward title
                                if (reward.title.isNotEmpty()) {
                                    Text(
                                        text = reward.title,
                                        fontSize = 8.5.sp,
                                        color = if (reward.day == 15) Color(0xFFFFD700) else Color(0xFFE2E8F0),
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        textAlign = TextAlign.Center
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // Status Badge
                                when {
                                    isClaimed -> {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0x334ADE80)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF4ADE80), modifier = Modifier.size(11.dp))
                                                Spacer(modifier = Modifier.width(2.dp))
                                                Text(StringsHelper.get("claimed", lang), fontSize = 10.sp, color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                    isCurrentClaimable -> {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFFFFD700)
                                        ) {
                                            Text(
                                                text = StringsHelper.get("claim", lang),
                                                fontSize = 10.sp,
                                                color = Color.Black,
                                                fontWeight = FontWeight.Black,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    isCurrentWaiting -> {
                                        Icon(Icons.Default.HourglassTop, contentDescription = null, tint = Color(0xFFFF9100), modifier = Modifier.size(14.dp))
                                    }
                                    else -> {
                                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF556080), modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                // Status message & Claim button
                if (canClaim) {
                    val rewardCoins = UserGameData.DAILY_REWARDS.getOrNull(nextDay - 1)?.coins ?: 200
                    Button(
                        onClick = onClaimReward,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .scale(pulseScale)
                            .shadow(14.dp, RoundedCornerShape(16.dp), spotColor = Color(0xFFFFD700))
                            .testTag("claim_daily_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "${StringsHelper.get("claim", lang)} (+ $rewardCoins ${StringsHelper.get("coins", lang)})",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Black,
                            fontWeight = FontWeight.Black
                        )
                    }
                } else {
                    val remainingMillis = userData.millisUntilNextClaim
                    val hours = (remainingMillis / (1000 * 60 * 60)).toInt()
                    val minutes = ((remainingMillis / (1000 * 60)) % 60).toInt()
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFF1E243D),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF353D63)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.HourglassTop, contentDescription = null, tint = Color(0xFFFF9100), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${StringsHelper.get("claimed_today", lang)} ${hours}h ${minutes}m",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFA0AEC0),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Explicit Bottom Close Button
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFA0AEC0)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF353D63))
                ) {
                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(StringsHelper.get("close", lang), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
private fun StatRow(
    title: String,
    value: String,
    icon: ImageVector,
    highlight: Boolean,
    valueColor: Color = if (highlight) Color(0xFF4ADE80) else Color.White,
    lang: AppLanguage = AppLanguage.INDONESIAN
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = if (highlight) Color(0xFFFFD700) else Color(0xFFA0AEC0), modifier = Modifier.size(16.dp))
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

// COIN SHOP & FREE REWARDS DIALOG
@Composable
fun CoinShopDialog(
    userData: UserGameData,
    lang: AppLanguage,
    onAddCoins: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF1E293B),
            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFACC15)),
            shadowElevation = 12.dp,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🪙", fontSize = 26.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = StringsHelper.get("coin_shop_title", lang),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFFDE047)
                        )
                    }
                    Surface(
                        onClick = onDismiss,
                        shape = CircleShape,
                        color = Color(0xFF334155),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                Text(
                    text = "Koin saat ini: ${userData.coins} 🪙",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Package 1: Watch Video +500 Coins
                CoinPackageItem(
                    icon = "🎬",
                    title = "Tonton Video Singkat",
                    reward = "+500 Koin Gratis",
                    badge = "GRATIS",
                    badgeColor = Color(0xFF22C55E),
                    onClick = {
                        onAddCoins(500)
                        onDismiss()
                    }
                )

                // Package 2: Daily Mystery Chest +1000 Coins
                CoinPackageItem(
                    icon = "🎁",
                    title = "Peti Emas Misteri",
                    reward = "+1,000 Koin Emas",
                    badge = "HARIAN",
                    badgeColor = Color(0xFFEAB308),
                    onClick = {
                        onAddCoins(1000)
                        onDismiss()
                    }
                )

                // Package 3: Starter Pack +5000 Coins
                CoinPackageItem(
                    icon = "💎",
                    title = "Paket Pemula",
                    reward = "+5,000 Koin",
                    badge = "POPULER",
                    badgeColor = Color(0xFF38BDF8),
                    onClick = {
                        onAddCoins(5000)
                        onDismiss()
                    }
                )

                // Package 4: Sultan Pack +25000 Coins
                CoinPackageItem(
                    icon = "👑",
                    title = "Paket Sultan",
                    reward = "+25,000 Koin",
                    badge = "HEMAT",
                    badgeColor = Color(0xFFA855F7),
                    onClick = {
                        onAddCoins(25000)
                        onDismiss()
                    }
                )

                // Package 5: Mega King Pack +100000 Coins
                CoinPackageItem(
                    icon = "🌟",
                    title = "Paket Raja Cacing",
                    reward = "+100,000 Koin",
                    badge = "MEGA BONUS",
                    badgeColor = Color(0xFFEF4444),
                    onClick = {
                        onAddCoins(100000)
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
private fun CoinPackageItem(
    icon: String,
    title: String,
    reward: String,
    badge: String,
    badgeColor: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF0F172A),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, badgeColor.copy(alpha = 0.7f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(icon, fontSize = 28.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(title, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                    Text(reward, fontWeight = FontWeight.Black, color = Color(0xFFFDE047), fontSize = 13.sp)
                }
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = badgeColor
            ) {
                Text(
                    text = badge,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

// PIGGY BANK DIALOG
@Composable
fun PiggyBankDialog(
    piggyCoins: Int,
    onBreak: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF1E293B),
            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFACC15)),
            shadowElevation = 12.dp,
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("🐷", fontSize = 54.sp)
                Text(
                    text = "Celengan Babi Nana",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFFDE047)
                )
                Text(
                    text = "Celenganmu telah mengumpulkan koin dari pertandingan!",
                    fontSize = 13.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF0F172A),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFF59E0B)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Total Tabungan Koin:", fontSize = 12.sp, color = Color(0xFFA0AEC0))
                        Text(
                            text = "$piggyCoins 🪙",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFFACC15)
                        )
                    }
                }

                Button(
                    onClick = onBreak,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E)),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Pecahkan Celengan & Ambil Koin!", fontWeight = FontWeight.Black, fontSize = 14.sp)
                }

                TextButton(onClick = onDismiss) {
                    Text("Nanti Saja", color = Color(0xFFA0AEC0))
                }
            }
        }
    }
}

// LUCKY BONUS DIALOG
@Composable
fun LuckyBonusDialog(
    onClaimBonus: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF1E293B),
            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFEF4444)),
            shadowElevation = 12.dp,
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("⭐", fontSize = 52.sp)
                Text(
                    text = "Bonus Harian Spesial!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFFDE047)
                )
                Text(
                    text = "Selamat! Anda mendapatkan hadiah koin kejutan harian sebesar:",
                    fontSize = 13.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF450A0A),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFEF4444)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("+750 Koin Emas", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color(0xFFFDE047))
                    }
                }

                Button(
                    onClick = onClaimBonus,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("KLAIM SEKARANG!", fontWeight = FontWeight.Black, fontSize = 15.sp)
                }

                TextButton(onClick = onDismiss) {
                    Text("Tutup", color = Color(0xFFA0AEC0))
                }
            }
        }
    }
}

