package com.example.ui.game

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.UserGameData

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
fun ReviveDialog(
    reviveCost: Int,
    playerCoins: Int,
    wormLength: Int,
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
                // Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "❤️‍🩹", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LANJUTKAN CACING?",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFFFF9100),
                        fontWeight = FontWeight.Black
                    )
                }

                Text(
                    text = "Cacingmu tumbang! Bangkitkan kembali dengan perlindungan kebal 6 detik!",
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
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Panjang Cacing:", style = MaterialTheme.typography.bodySmall, color = Color(0xFFA0AEC0))
                            Text("$wormLength Segmen", style = MaterialTheme.typography.titleSmall, color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Koin Kamu:", style = MaterialTheme.typography.bodySmall, color = Color(0xFFA0AEC0))
                            Text("🪙 $playerCoins Koin", style = MaterialTheme.typography.titleSmall, color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)
                        }
                        androidx.compose.material3.HorizontalDivider(color = Color(0xFF353D63), thickness = 1.dp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Biaya Lanjutkan:", style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Bold)
                            Text(
                                text = "🪙 $reviveCost Koin",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (canAfford) Color(0xFFFF9100) else Color(0xFFFF5252),
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }

                if (!canAfford) {
                    Text(
                        text = "⚠️ Koin kamu kurang ${reviveCost - playerCoins} untuk melanjutkan.",
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
                            text = "HIDUPKAN LAGI (🪙 $reviveCost)",
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
                        Text("Selesai & Lihat Skor", style = MaterialTheme.typography.bodyMedium, color = Color(0xFFA0AEC0))
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
                // Header Loss or Victory Banner
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = if (isVictory) "🏆" else "💥", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isVictory) "JUARA ARENA!" else "KAMU KALAH!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = if (isVictory) Color(0xFFFFD700) else Color(0xFFFF3366),
                        fontWeight = FontWeight.Black
                    )
                }

                Text(
                    text = if (isVictory) "Hebat! Kamu bertahan dan mendominasi arena!" else if (score >= highScore && score > 0) "🎉 REKOR SKOR BARU DI ARENA! 🎉" else "Coba lagi dan jadilah cacing terpanjang!",
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
fun DailyLoginDialog(
    userData: UserGameData,
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
                // Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🎁", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "EVENT 7 HARI LOGIN",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color(0xFFFFD700),
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Login setiap hari dan klaim koin serta skin hadiah gratis!",
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
                                contentDescription = "Tutup Event",
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
                        val isLocked = reward.day > nextDay

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
                                    else -> Color(0xFF2C3454)
                                }
                            ),
                            modifier = Modifier.width(110.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                // Day Header
                                Text(
                                    text = "Hari ${reward.day}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isCurrentClaimable) Color(0xFFFFD700) else Color(0xFFA0AEC0),
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // Reward Icon
                                Text(
                                    text = if (reward.isSpecial) "👑" else "🪙",
                                    fontSize = 28.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // Amount
                                Text(
                                    text = "+${reward.coins}",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color(0xFFFFD700),
                                    fontWeight = FontWeight.Black
                                )

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
                                                Text("Klaim", fontSize = 10.sp, color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                    isCurrentClaimable -> {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFFFFD700)
                                        ) {
                                            Text(
                                                text = "SIAP!",
                                                fontSize = 10.sp,
                                                color = Color.Black,
                                                fontWeight = FontWeight.Black,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    isCurrentWaiting -> {
                                        Text(
                                            text = "Besok",
                                            fontSize = 10.sp,
                                            color = Color(0xFFFF9100),
                                            fontWeight = FontWeight.Bold
                                        )
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
                            text = "KLAIM HADIAH HARI KE-$nextDay (+🪙 $rewardCoins)",
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
                            Text("⏳", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Sudah diklaim hari ini! Hadiah berikutnya tersedia dalam ${hours}j ${minutes}m",
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
                    Text("Tutup", fontWeight = FontWeight.Bold, fontSize = 14.sp)
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
