package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserGameData
import com.example.model.FaceAccessory
import com.example.model.SkinId
import com.example.model.Vec2
import com.example.model.Worm
import com.example.ui.game.WormsRenderer

@Composable
fun WardrobeScreen(
    userData: UserGameData,
    onEquipSkin: (SkinId) -> Unit,
    onBuySkin: (SkinId) -> Boolean,
    onEquipAccessory: (FaceAccessory) -> Unit,
    onBuyAccessory: (FaceAccessory) -> Boolean,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF10131F))
            .testTag("wardrobe_screen")
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
                        modifier = Modifier.testTag("wardrobe_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "LEMARI CACING",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Black
                    )
                }

                // Coins Badge
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

        // LIVE WORM PREVIEW
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color(0xFF141829)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f

                val preview = Worm(
                    id = "wardrobe_preview",
                    name = userData.playerName,
                    isPlayer = true,
                    head = Vec2(cx + 40f, cy),
                    skin = userData.equippedSkin,
                    accessory = userData.equippedAccessory,
                    mass = 70f
                ).apply {
                    bodyPoints.clear()
                    for (i in 0 until 16) {
                        val px = (cx + 40f) - i * 13f
                        bodyPoints.add(Vec2(px, cy))
                    }
                }

                WormsRenderer.drawWorm(
                    drawScope = this,
                    worm = preview,
                    cameraOffset = Offset.Zero,
                    zoom = 1f
                )
            }
        }

        // TABS: SKINS vs ACCESSORIES
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFF1A1F33),
            contentColor = Color(0xFF4ADE80),
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = Color(0xFF4ADE80)
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        "Skin Tubuh",
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 0) Color(0xFF4ADE80) else Color(0xFFA0AEC0)
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        "Aksesoris Wajah",
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 1) Color(0xFF4ADE80) else Color(0xFFA0AEC0)
                    )
                }
            )
        }

        // ITEMS GRID
        if (selectedTab == 0) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 170.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(SkinId.entries) { skin ->
                    val isUnlocked = userData.unlockedSkins.contains(skin)
                    val isEquipped = userData.equippedSkin == skin

                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (isEquipped) Color(0xFF23304E) else Color(0xFF1A1F33),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isEquipped) 2.dp else 1.dp,
                            color = if (isEquipped) Color(0xFF4ADE80) else Color(0xFF2C3454)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Color orb preview
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(skin.headColor)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(skin.primaryColor)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(skin.secondaryColor)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = skin.title,
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = skin.description,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFA0AEC0),
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            if (isEquipped) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0x334ADE80),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4ADE80))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color(0xFF4ADE80), modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("DIPAKAI", style = MaterialTheme.typography.labelSmall, color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold)
                                    }
                                }
                            } else if (isUnlocked) {
                                Button(
                                    onClick = { onEquipSkin(skin) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C3454)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("PAKAI", style = MaterialTheme.typography.labelSmall, color = Color.White)
                                }
                            } else {
                                Button(
                                    onClick = { onBuySkin(skin) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9100)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("🪙 ${skin.price}", style = MaterialTheme.typography.labelSmall, color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 170.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(FaceAccessory.entries) { acc ->
                    val isUnlocked = userData.unlockedAccessories.contains(acc)
                    val isEquipped = userData.equippedAccessory == acc

                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (isEquipped) Color(0xFF23304E) else Color(0xFF1A1F33),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isEquipped) 2.dp else 1.dp,
                            color = if (isEquipped) Color(0xFF4ADE80) else Color(0xFF2C3454)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = when (acc) {
                                    FaceAccessory.ROYAL_CROWN -> "👑"
                                    FaceAccessory.SUNGLASSES -> "🕶️"
                                    FaceAccessory.PIRATE_PATCH -> "🏴‍☠️"
                                    FaceAccessory.ANIME_SPARKLE -> "✨"
                                    FaceAccessory.NONE -> "🙂"
                                },
                                fontSize = 32.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = acc.title,
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = acc.description,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFA0AEC0),
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            if (isEquipped) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0x334ADE80),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4ADE80))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color(0xFF4ADE80), modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("DIPAKAI", style = MaterialTheme.typography.labelSmall, color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold)
                                    }
                                }
                            } else if (isUnlocked) {
                                Button(
                                    onClick = { onEquipAccessory(acc) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C3454)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("PAKAI", style = MaterialTheme.typography.labelSmall, color = Color.White)
                                }
                            } else {
                                Button(
                                    onClick = { onBuyAccessory(acc) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9100)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("🪙 ${acc.price}", style = MaterialTheme.typography.labelSmall, color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
