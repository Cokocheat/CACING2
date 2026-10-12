package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserGameData

@Composable
fun RecordsScreen(
    userData: UserGameData,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

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
                        contentDescription = "Kembali",
                        tint = Color.White
                    )
                }
                Text(
                    text = "REKOR & PRESTASI",
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
                            text = "STATISTIK KARIER",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF4ADE80),
                            fontWeight = FontWeight.Bold
                        )

                        RecordRow(label = "Skor Tertinggi", value = "${userData.highScore}", icon = "🏆", valueColor = Color(0xFFFFD700))
                        RecordRow(label = "Total Musuh Dieliminasi", value = "${userData.totalKills} Cacing", icon = "⚔️", valueColor = Color(0xFFFF5252))
                        RecordRow(label = "Panjang Cacing Maksimal", value = "${userData.longestLength} Segmen", icon = "🐍", valueColor = Color(0xFF00E5FF))
                        RecordRow(label = "Total Pertandingan", value = "${userData.totalGames} Game", icon = "🎮", valueColor = Color.White)
                        RecordRow(label = "Saldo Koin Tersedia", value = "${userData.coins} Koin", icon = "🪙", valueColor = Color(0xFFFFD700))
                    }
                }
            }

            // ACHIEVEMENTS SECTION
            item {
                Text(
                    text = "PENCAPAIAN ARENA",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFFF9100),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Achievement 1: First Kill
            item {
                AchievementCard(
                    title = "Pemangsa Pertama",
                    desc = "Kalahkan setidaknya 1 lawan di arena",
                    icon = "🗡️",
                    isUnlocked = userData.totalKills >= 1
                )
            }

            // Achievement 2: Centurion
            item {
                AchievementCard(
                    title = "Penguasa Arena",
                    desc = "Capai rekor skor 300 poin atau lebih",
                    icon = "👑",
                    isUnlocked = userData.highScore >= 300
                )
            }

            // Achievement 3: Long snake
            item {
                AchievementCard(
                    title = "Naga Raksasa",
                    desc = "Tumbuhkan cacing hingga panjang 40 segmen",
                    icon = "🐉",
                    isUnlocked = userData.longestLength >= 40
                )
            }

            // Achievement 4: Veteran
            item {
                AchievementCard(
                    title = "Pemain Sejati",
                    desc = "Mainkan 10 pertandingan",
                    icon = "⭐",
                    isUnlocked = userData.totalGames >= 10
                )
            }
        }
    }
}

@Composable
private fun RecordRow(label: String, value: String, icon: String, valueColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 18.sp)
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
    icon: String,
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
                Text(text = icon, fontSize = 28.sp)
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
