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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserGameData

@Composable
fun UpgradesScreen(
    userData: UserGameData,
    onUpgrade: (type: String, cost: Int) -> Boolean,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF10131F))
            .testTag("upgrades_screen")
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("upgrades_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "PENINGKATAN RAMUAN",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Black
                    )
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF242A45),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD700))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🪙", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${userData.coins}",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
            columns = androidx.compose.foundation.lazy.grid.GridCells.Adaptive(minSize = 280.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "Tingkatkan kekuatan ramuan dan kemampuan cacing untuk memenangkan arena!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFA0AEC0)
                )
            }

            // 1. Magnet Makanan
            item {
                UpgradeItemCard(
                    icon = "🧲",
                    title = "Radius Magnet Makanan",
                    description = "Menarik makanan dan koin dari jarak yang lebih jauh",
                    level = userData.magnetLevel,
                    accentColor = Color(0xFF00E5FF),
                    onUpgrade = { cost -> onUpgrade("magnet", cost) },
                    userCoins = userData.coins
                )
            }

            // 2. Multiplier Poin
            item {
                UpgradeItemCard(
                    icon = "🟡",
                    title = "Efektivitas Poin Ganda",
                    description = "Memperbanyak perolehan poin dari makanan saat aktif",
                    level = userData.multiplierLevel,
                    accentColor = Color(0xFFFFD600),
                    onUpgrade = { cost -> onUpgrade("multiplier", cost) },
                    userCoins = userData.coins
                )
            }

            // 3. Efisiensi Dorongan
            item {
                UpgradeItemCard(
                    icon = "⚡",
                    title = "Efisiensi Turbo Boost",
                    description = "Mengurangi hilangnya massa tubuh saat berlari kencang",
                    level = userData.boostLevel,
                    accentColor = Color(0xFFFF6D00),
                    onUpgrade = { cost -> onUpgrade("boost", cost) },
                    userCoins = userData.coins
                )
            }

            // 4. Kelincahan Lari
            item {
                UpgradeItemCard(
                    icon = "🏃",
                    title = "Kelincahan Dasar Cacing",
                    description = "Meningkatkan kecepatan jalan dasar di arena",
                    level = userData.speedLevel,
                    accentColor = Color(0xFF4ADE80),
                    onUpgrade = { cost -> onUpgrade("speed", cost) },
                    userCoins = userData.coins
                )
            }
        }
    }
}

@Composable
private fun UpgradeItemCard(
    icon: String,
    title: String,
    description: String,
    level: Int,
    accentColor: Color,
    onUpgrade: (Int) -> Unit,
    userCoins: Int
) {
    val upgradeCosts = listOf(0, 120, 260, 480, 800)
    val nextCost = if (level < 5) upgradeCosts[level] else 0
    val isMax = level >= 5
    val canAfford = userCoins >= nextCost && !isMax

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF1A1F33),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3454)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = icon, fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFA0AEC0)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Level Dots / Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    for (i in 1..5) {
                        Box(
                            modifier = Modifier
                                .size(width = 24.dp, height = 8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (i <= level) accentColor else Color(0xFF2C3454))
                        )
                    }
                }

                Text(
                    text = if (isMax) "MAX LEVEL" else "Lv. $level / 5",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isMax) Color(0xFFFFD700) else accentColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Upgrade Button
            if (isMax) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0x33FFD700),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD700)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "TINGKAT MAKSIMAL",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFFFFD700),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 10.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                Button(
                    onClick = { onUpgrade(nextCost) },
                    enabled = canAfford,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        disabledContainerColor = Color(0xFF2C3454)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "TINGKATKAN KE LV. ${level + 1} (🪙 $nextCost)",
                            style = MaterialTheme.typography.labelLarge,
                            color = if (canAfford) Color.Black else Color(0xFFA0AEC0),
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }
    }
}
