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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stars
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.StringsHelper
import com.example.data.UserGameData

@Composable
fun UpgradesScreen(
    userData: UserGameData,
    onUpgrade: (type: String, cost: Int) -> Boolean,
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    val lang = userData.language

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
                            contentDescription = StringsHelper.get("back", lang),
                            tint = Color.White
                        )
                    }
                    Text(
                        text = StringsHelper.get("upgrades_title", lang),
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
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(16.dp)
                        )
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
                    text = StringsHelper.get("upgrades_subtitle", lang),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFA0AEC0)
                )
            }

            // 1. Magnet Makanan
            item {
                UpgradeItemCard(
                    icon = Icons.Default.Adjust,
                    title = StringsHelper.get("magnet", lang),
                    description = StringsHelper.get("magnet_desc", lang),
                    level = userData.magnetLevel,
                    accentColor = Color(0xFF00E5FF),
                    onUpgrade = { cost -> onUpgrade("magnet", cost) },
                    userCoins = userData.coins,
                    lang = lang
                )
            }

            // 2. Multiplier Poin
            item {
                UpgradeItemCard(
                    icon = Icons.Default.Stars,
                    title = StringsHelper.get("potion_multiplier", lang),
                    description = StringsHelper.get("turn_agility_desc", lang),
                    level = userData.multiplierLevel,
                    accentColor = Color(0xFFFFD600),
                    onUpgrade = { cost -> onUpgrade("multiplier", cost) },
                    userCoins = userData.coins,
                    lang = lang
                )
            }

            // 3. Efisiensi Dorongan
            item {
                UpgradeItemCard(
                    icon = Icons.Default.Bolt,
                    title = StringsHelper.get("boost_efficiency", lang),
                    description = StringsHelper.get("boost_efficiency_desc", lang),
                    level = userData.boostLevel,
                    accentColor = Color(0xFFFF6D00),
                    onUpgrade = { cost -> onUpgrade("boost", cost) },
                    userCoins = userData.coins,
                    lang = lang
                )
            }

            // 4. Kelincahan Lari
            item {
                UpgradeItemCard(
                    icon = Icons.Default.Speed,
                    title = StringsHelper.get("speed", lang),
                    description = StringsHelper.get("speed_desc", lang),
                    level = userData.speedLevel,
                    accentColor = Color(0xFF4ADE80),
                    onUpgrade = { cost -> onUpgrade("speed", cost) },
                    userCoins = userData.coins,
                    lang = lang
                )
            }
        }
    }
}

@Composable
private fun UpgradeItemCard(
    icon: ImageVector,
    title: String,
    description: String,
    level: Int,
    accentColor: Color,
    onUpgrade: (Int) -> Unit,
    userCoins: Int,
    lang: com.example.data.AppLanguage
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
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = accentColor.copy(alpha = 0.18f),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
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
                    text = if (isMax) StringsHelper.get("max_level", lang) else "Lv. $level / 5",
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
                        text = StringsHelper.get("max_level_reached", lang),
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
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = if (canAfford) Color.Black else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${StringsHelper.get("upgrade_to_lv", lang)} ${level + 1} ($nextCost)",
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
