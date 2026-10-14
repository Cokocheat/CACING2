package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.StringsHelper
import com.example.data.UserGameData

@Composable
fun RecordsScreen(
    userData: UserGameData,
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    val lang = userData.language

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF10131F))
            .testTag("records_screen")
    ) {
        // TOP APP BAR
        Surface(
            color = Color(0xFF1A1F33),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3454)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 8.dp, start = 12.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("records_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = StringsHelper.get("back", lang),
                        tint = Color.White
                    )
                }
                Text(
                    text = StringsHelper.get("records_title", lang),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
            }
        }

        androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
            columns = androidx.compose.foundation.lazy.grid.GridCells.Adaptive(minSize = 300.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // STATS SUMMARY CARD
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF1A1F33),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3454)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = StringsHelper.get("career_stats", lang),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF4ADE80),
                            fontWeight = FontWeight.Bold
                        )

                        RecordRow(
                            label = StringsHelper.get("high_score", lang),
                            value = "${userData.highScore} ${StringsHelper.get("points", lang)}",
                            icon = Icons.Default.EmojiEvents,
                            iconColor = Color(0xFFFFD700),
                            valueColor = Color(0xFFFFD700)
                        )
                        RecordRow(
                            label = StringsHelper.get("total_kills", lang),
                            value = "${userData.totalKills} ${StringsHelper.get("worms_count", lang)}",
                            icon = Icons.Default.LocalFireDepartment,
                            iconColor = Color(0xFFFF5252),
                            valueColor = Color(0xFFFF5252)
                        )
                        RecordRow(
                            label = StringsHelper.get("max_length", lang),
                            value = "${userData.longestLength} ${StringsHelper.get("segments", lang)}",
                            icon = Icons.Default.Timeline,
                            iconColor = Color(0xFF00E5FF),
                            valueColor = Color(0xFF00E5FF)
                        )
                        RecordRow(
                            label = StringsHelper.get("total_games", lang),
                            value = "${userData.totalGames}",
                            icon = Icons.Default.SportsEsports,
                            iconColor = Color.White,
                            valueColor = Color.White
                        )
                        RecordRow(
                            label = StringsHelper.get("coins_available", lang),
                            value = "${userData.coins}",
                            icon = Icons.Default.MonetizationOn,
                            iconColor = Color(0xFFFFD700),
                            valueColor = Color(0xFFFFD700)
                        )
                    }
                }
            }

            // ACHIEVEMENTS SECTION
            item {
                Text(
                    text = StringsHelper.get("achievements", lang),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFFF9100),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Achievement 1: First Kill
            item {
                AchievementCard(
                    title = StringsHelper.get("first_hunter", lang),
                    desc = StringsHelper.get("first_hunter_desc", lang),
                    icon = Icons.Default.LocalFireDepartment,
                    iconColor = Color(0xFFFF5252),
                    isUnlocked = userData.totalKills >= 1
                )
            }

            // Achievement 2: Centurion
            item {
                AchievementCard(
                    title = StringsHelper.get("arena_ruler", lang),
                    desc = StringsHelper.get("arena_ruler_desc", lang),
                    icon = Icons.Default.EmojiEvents,
                    iconColor = Color(0xFFFFD700),
                    isUnlocked = userData.highScore >= 300
                )
            }

            // Achievement 3: Long snake
            item {
                AchievementCard(
                    title = StringsHelper.get("giant_dragon", lang),
                    desc = StringsHelper.get("giant_dragon_desc", lang),
                    icon = Icons.Default.MilitaryTech,
                    iconColor = Color(0xFF00E5FF),
                    isUnlocked = userData.longestLength >= 40
                )
            }

            // Achievement 4: Veteran
            item {
                AchievementCard(
                    title = StringsHelper.get("veteran", lang),
                    desc = StringsHelper.get("veteran_desc", lang),
                    icon = Icons.Default.Star,
                    iconColor = Color(0xFFFFB300),
                    isUnlocked = userData.totalGames >= 10
                )
            }
        }
    }
}

@Composable
private fun RecordRow(
    label: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    valueColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = label, style = MaterialTheme.typography.bodyLarge, color = Color(0xFFA0AEC0))
        }
        Text(text = value, style = MaterialTheme.typography.titleMedium, color = valueColor, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun AchievementCard(
    title: String,
    desc: String,
    icon: ImageVector,
    iconColor: Color,
    isUnlocked: Boolean
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isUnlocked) Color(0xFF1E263D) else Color(0xFF141828),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (isUnlocked) Color(0xFF4ADE80) else Color(0xFF262E48)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isUnlocked) iconColor.copy(alpha = 0.18f) else Color(0xFF1E2436),
                    modifier = Modifier.size(42.dp)
                ) {
                    androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isUnlocked) iconColor else Color(0xFF556080),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        color = if (isUnlocked) Color.White else Color(0xFF718096),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFA0AEC0)
                    )
                }
            }

            Icon(
                imageVector = if (isUnlocked) Icons.Default.CheckCircle else Icons.Default.Lock,
                contentDescription = null,
                tint = if (isUnlocked) Color(0xFF4ADE80) else Color(0xFF4A5568),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
