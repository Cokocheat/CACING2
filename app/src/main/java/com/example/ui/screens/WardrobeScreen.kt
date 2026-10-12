package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Visibility
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
import androidx.compose.runtime.mutableStateOf
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
import kotlin.math.PI
import kotlin.math.sin

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
    // Live preview state allows user to test/view any skin or accessory BEFORE buying
    var previewSkin by remember { mutableStateOf(userData.equippedSkin) }
    var previewAccessory by remember { mutableStateOf(userData.equippedAccessory) }

    val infiniteTransition = rememberInfiniteTransition(label = "wormWiggleWardrobe")
    val wigglePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

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
                        text = "LEMARI SKIN & AKSERORIS",
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

        // INTERACTIVE LIVE WORM PREVIEW (Updates dynamically when clicking any item!)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(115.dp)
                .background(Color(0xFF141829))
                .border(1.dp, Color(0xFF2C3454)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f

                val preview = Worm(
                    id = "wardrobe_preview",
                    name = userData.playerName,
                    isPlayer = true,
                    head = Vec2(cx + 60f, cy),
                    skin = previewSkin,
                    accessory = previewAccessory,
                    mass = 85f
                ).apply {
                    bodyPoints.clear()
                    for (i in 0 until 18) {
                        val wave = sin(wigglePhase + i * 0.45f) * 14f
                        val px = (cx + 60f) - i * 13f
                        bodyPoints.add(Vec2(px, cy + wave))
                    }
                }

                WormsRenderer.drawWorm(
                    drawScope = this,
                    worm = preview,
                    cameraOffset = Offset.Zero,
                    zoom = 1f
                )
            }

            // Preview Info Banner
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 6.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xDD0E121E),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4ADE80).copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = null, tint = Color(0xFF4ADE80), modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Pratinjau: ${previewSkin.title} ${if (previewAccessory != FaceAccessory.NONE) "+ " + previewAccessory.title else ""}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    if (previewSkin != userData.equippedSkin || previewAccessory != userData.equippedAccessory) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(Klik item lain untuk mencoba)",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFFD700),
                            fontSize = 10.sp
                        )
                    }
                }
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
                        "Skin Tubuh Cacing",
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
                columns = GridCells.Adaptive(minSize = 180.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(SkinId.entries) { skin ->
                    val isUnlocked = userData.unlockedSkins.contains(skin)
                    val isEquipped = userData.equippedSkin == skin
                    val isPreviewing = previewSkin == skin

                    Surface(
                        onClick = { previewSkin = skin },
                        shape = RoundedCornerShape(18.dp),
                        color = when {
                            isEquipped -> Color(0xFF23304E)
                            isPreviewing -> Color(0xFF241D3B)
                            else -> Color(0xFF1A1F33)
                        },
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isEquipped || isPreviewing) 2.dp else 1.dp,
                            color = when {
                                isEquipped -> Color(0xFF4ADE80)
                                isPreviewing -> Color(0xFFFFD700)
                                else -> Color(0xFF2C3454)
                            }
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

                            Spacer(modifier = Modifier.height(8.dp))
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

                            Spacer(modifier = Modifier.height(10.dp))

                            // Action buttons / status
                            when {
                                isEquipped -> {
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
                                }
                                isUnlocked -> {
                                    Button(
                                        onClick = {
                                            onEquipSkin(skin)
                                            previewSkin = skin
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C3454)),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("PAKAI", style = MaterialTheme.typography.labelSmall, color = Color.White)
                                    }
                                }
                                else -> {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = { previewSkin = skin },
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00E5FF))
                                        ) {
                                            Text("LIHAT", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Button(
                                            onClick = {
                                                if (onBuySkin(skin)) {
                                                    previewSkin = skin
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9100)),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.weight(1.2f)
                                        ) {
                                            Text("🪙 ${skin.price}", fontSize = 11.sp, color = Color.Black, fontWeight = FontWeight.Black)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 180.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(FaceAccessory.entries) { acc ->
                    val isUnlocked = userData.unlockedAccessories.contains(acc)
                    val isEquipped = userData.equippedAccessory == acc
                    val isPreviewing = previewAccessory == acc

                    Surface(
                        onClick = { previewAccessory = acc },
                        shape = RoundedCornerShape(18.dp),
                        color = when {
                            isEquipped -> Color(0xFF23304E)
                            isPreviewing -> Color(0xFF241D3B)
                            else -> Color(0xFF1A1F33)
                        },
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isEquipped || isPreviewing) 2.dp else 1.dp,
                            color = when {
                                isEquipped -> Color(0xFF4ADE80)
                                isPreviewing -> Color(0xFFFFD700)
                                else -> Color(0xFF2C3454)
                            }
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

                            Spacer(modifier = Modifier.height(10.dp))

                            when {
                                isEquipped -> {
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
                                }
                                isUnlocked -> {
                                    Button(
                                        onClick = {
                                            onEquipAccessory(acc)
                                            previewAccessory = acc
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C3454)),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("PAKAI", style = MaterialTheme.typography.labelSmall, color = Color.White)
                                    }
                                }
                                else -> {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = { previewAccessory = acc },
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00E5FF))
                                        ) {
                                            Text("LIHAT", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Button(
                                            onClick = {
                                                if (onBuyAccessory(acc)) {
                                                    previewAccessory = acc
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9100)),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.weight(1.2f)
                                        ) {
                                            Text("🪙 ${acc.price}", fontSize = 11.sp, color = Color.Black, fontWeight = FontWeight.Black)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
