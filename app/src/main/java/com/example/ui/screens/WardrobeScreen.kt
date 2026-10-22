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
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stars
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.StringsHelper
import com.example.data.UserGameData
import com.example.model.FaceAccessory
import com.example.model.SkinId
import com.example.model.Vec2
import com.example.model.Worm
import com.example.ui.game.WormsRenderer
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

private fun getSkinLocalizedTitle(skin: SkinId, lang: AppLanguage): String {
    val key = when (skin) {
        SkinId.CLASSIC_GREEN -> "classic_green"
        SkinId.RAINBOW -> "rainbow_dash"
        SkinId.BUMBLEBEE -> "neon_glow"
        SkinId.WATERMELON -> "candy_swirl"
        SkinId.TIGER -> "tiger_stripes"
        SkinId.GALAXY -> "cosmic_galaxy"
        SkinId.CANDY_GUMMY -> "candy_swirl"
        SkinId.GOLDEN_EMPEROR -> "golden_king"
    }
    return StringsHelper.get(key, lang)
}

private fun getAccessoryLocalizedTitle(acc: FaceAccessory, lang: AppLanguage): String {
    val key = when (acc) {
        FaceAccessory.NONE -> "none_acc"
        FaceAccessory.ROYAL_CROWN -> "crown_acc"
        FaceAccessory.SUNGLASSES -> "sunglasses_acc"
        FaceAccessory.PIRATE_PATCH -> "pirate_patch_acc"
        FaceAccessory.ANIME_SPARKLE -> "anime_sparkle_acc"
    }
    return StringsHelper.get(key, lang)
}

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
    val lang = userData.language

    var selectedTab by remember { mutableIntStateOf(0) }
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
                            contentDescription = StringsHelper.get("back", lang),
                            tint = Color.White
                        )
                    }
                    Text(
                        text = StringsHelper.get("wardrobe_title", lang),
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

        // LIVE PREVIEW PODIUM (STAGE)
        Surface(
            color = Color(0xFF141828),
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cx = size.width / 2f
                    val cy = size.height / 2f

                    val headY = cy + sin(wigglePhase) * 16f
                    val headX = cx + 55f
                    val vy = cos(wigglePhase) * 16f
                    val vx = 16f
                    val headAngle = atan2(vy, vx)

                    val previewWorm = Worm(
                        id = "wardrobe_preview",
                        name = userData.playerName,
                        isPlayer = true,
                        head = Vec2(headX, headY),
                        angle = headAngle,
                        skin = previewSkin,
                        accessory = previewAccessory,
                        mass = 85f
                    ).apply {
                        bodyPoints.clear()
                        for (i in 0 until 16) {
                            val segPhase = wigglePhase - i * 0.38f
                            val px = (cx + 55f) - i * 14f
                            val py = cy + sin(segPhase) * 16f
                            bodyPoints.add(Vec2(px, py))
                        }
                    }

                    WormsRenderer.drawWorm(
                        drawScope = this,
                        worm = previewWorm,
                        cameraOffset = Offset(0f, 0f),
                        zoom = 1.05f
                    )
                }

                Text(
                    text = "${getSkinLocalizedTitle(previewSkin, lang)}  •  ${getAccessoryLocalizedTitle(previewAccessory, lang)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFA0AEC0),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 6.dp)
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
                        text = StringsHelper.get("worm_skins", lang),
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
                        text = StringsHelper.get("face_accessories", lang),
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
                            // Mini Color Palette Indicators
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(skin.headColor)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
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
                                text = getSkinLocalizedTitle(skin, lang),
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
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
                                            Text(StringsHelper.get("equipped_item", lang), style = MaterialTheme.typography.labelSmall, color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold)
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
                                        Text(StringsHelper.get("equip_item", lang), style = MaterialTheme.typography.labelSmall, color = Color.White)
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
                                            Text(StringsHelper.get("view_preview", lang), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Button(
                                            onClick = {
                                                if (onBuySkin(skin)) {
                                                    previewSkin = skin
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9100)),
                                            shape = RoundedCornerShape(10.dp),
                                            ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.MonetizationOn,
                                                    contentDescription = null,
                                                    tint = Color.Black,
                                                    modifier = Modifier.size(13.dp)
                                                )
                                                Spacer(modifier = Modifier.width(3.dp))
                                                Text(
                                                    text = "${skin.price}",
                                                    fontSize = 11.sp,
                                                    color = Color.Black,
                                                    fontWeight = FontWeight.Black
                                                )
                                            }
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
                            val accIcon: ImageVector = when (acc) {
                                FaceAccessory.ROYAL_CROWN -> Icons.Default.MilitaryTech
                                FaceAccessory.SUNGLASSES -> Icons.Default.Visibility
                                FaceAccessory.PIRATE_PATCH -> Icons.Default.Shield
                                FaceAccessory.ANIME_SPARKLE -> Icons.Default.Stars
                                FaceAccessory.NONE -> Icons.Default.Person
                            }
                            val accColor: Color = when (acc) {
                                FaceAccessory.ROYAL_CROWN -> Color(0xFFFFD700)
                                FaceAccessory.SUNGLASSES -> Color(0xFF00E5FF)
                                FaceAccessory.PIRATE_PATCH -> Color(0xFFFF5252)
                                FaceAccessory.ANIME_SPARKLE -> Color(0xFFFFB300)
                                FaceAccessory.NONE -> Color(0xFF4ADE80)
                            }

                            Surface(
                                shape = CircleShape,
                                color = accColor.copy(alpha = 0.15f),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = accIcon,
                                        contentDescription = null,
                                        tint = accColor,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = getAccessoryLocalizedTitle(acc, lang),
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
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
                                            Text(StringsHelper.get("equipped_item", lang), style = MaterialTheme.typography.labelSmall, color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold)
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
                                        Text(StringsHelper.get("equip_item", lang), style = MaterialTheme.typography.labelSmall, color = Color.White)
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
                                            Text(StringsHelper.get("view_preview", lang), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Button(
                                            onClick = {
                                                if (onBuyAccessory(acc)) {
                                                    previewAccessory = acc
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9100)),
                                            shape = RoundedCornerShape(10.dp),
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.MonetizationOn,
                                                    contentDescription = null,
                                                    tint = Color.Black,
                                                    modifier = Modifier.size(13.dp)
                                                )
                                                Spacer(modifier = Modifier.width(3.dp))
                                                Text(
                                                    text = "${acc.price}",
                                                    fontSize = 11.sp,
                                                    color = Color.Black,
                                                    fontWeight = FontWeight.Black
                                                )
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
}
