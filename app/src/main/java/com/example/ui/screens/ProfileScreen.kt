package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StringsHelper
import com.example.data.UserGameData
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProfileScreen(
    userData: UserGameData,
    onUpdateName: (String) -> Unit,
    onResetData: () -> Unit,
    onClaimAchievement: (String, Int) -> Unit,
    onOpenCoinShop: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val lang = userData.language
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Berkas, 1: Prestasi
    var showNameDialog by remember { mutableStateOf(false) }
    var tempName by remember(userData.playerName) { mutableStateOf(userData.playerName) }
    var showResetConfirmDialog by remember { mutableStateOf(false) }

    val hasUnclaimedAchievements = remember(userData.claimedAchievements) {
        !userData.claimedAchievements.contains("gold_rush") || !userData.claimedAchievements.contains("predator_500")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF273C75)) // Royal Blue Game Backdrop like screenshot
            .testTag("profile_screen")
    ) {
        // Decorative background check / motifs
        Canvas(modifier = Modifier.fillMaxSize()) {
            val step = 140.dp.toPx()
            var x = 0f
            while (x < size.width) {
                var y = 0f
                while (y < size.height) {
                    drawCircle(
                        color = Color(0x12FFFFFF),
                        radius = 18.dp.toPx(),
                        center = Offset(x + 20f, y + 20f)
                    )
                    y += step
                }
                x += step
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // TOP HEADER: "Profil" (Yellow Cartoon font) + Coin pill + Orange [X]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = StringsHelper.get("tab_profile", lang),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFFDE047),
                    letterSpacing = 0.5.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Coin pill with Plus button
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFF1E293B),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFACC15))
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 8.dp, end = 4.dp, top = 3.dp, bottom = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = NumberFormat.getNumberInstance(Locale.US).format(userData.coins),
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                onClick = onOpenCoinShop,
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF38BDF8),
                                modifier = Modifier.size(24.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Add, contentDescription = "Tambah Koin", tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }

                    // Orange Square Close Button [X]
                    Surface(
                        onClick = onBack,
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF97316),
                        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFED7AA)),
                        shadowElevation = 6.dp,
                        modifier = Modifier.size(40.dp).testTag("profile_back_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Tutup",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            // HORIZONTAL TABS: [Berkas] & [Prestasi]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                // Tab 0: Berkas
                val isTab0 = selectedTab == 0
                Surface(
                    onClick = { selectedTab = 0 },
                    shape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
                    color = if (isTab0) Color(0xFF3B5DA8) else Color(0xFF24366A),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        if (isTab0) Color(0xFF60A5FA) else Color(0xFF1D2D5B)
                    ),
                    modifier = Modifier.height(if (isTab0) 46.dp else 40.dp).testTag("tab_berkas")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Badge,
                            contentDescription = null,
                            tint = if (isTab0) Color(0xFFFDE047) else Color(0xFF94A3B8),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = StringsHelper.get("tab_archive", lang),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isTab0) Color(0xFFFDE047) else Color(0xFF94A3B8)
                        )
                    }
                }

                // Tab 1: Prestasi
                val isTab1 = selectedTab == 1
                Surface(
                    onClick = { selectedTab = 1 },
                    shape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
                    color = if (isTab1) Color(0xFF3B5DA8) else Color(0xFF24366A),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        if (isTab1) Color(0xFF60A5FA) else Color(0xFF1D2D5B)
                    ),
                    modifier = Modifier.height(if (isTab1) 46.dp else 40.dp).testTag("tab_prestasi")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(contentAlignment = Alignment.TopEnd) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = if (isTab1) Color(0xFFFDE047) else Color(0xFF94A3B8),
                                modifier = Modifier.size(18.dp)
                            )
                            if (hasUnclaimedAchievements) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEF4444))
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = StringsHelper.get("tab_achievements", lang),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isTab1) Color(0xFFFDE047) else Color(0xFF94A3B8)
                        )
                    }
                }
            }

            // MAIN TAB CONTENT BODY
            Surface(
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp, topEnd = 20.dp),
                color = Color(0xFF3B5DA8),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF60A5FA)),
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally(animationSpec = tween(280)) { fullWidth -> fullWidth } + fadeIn(tween(200)))
                                .togetherWith(slideOutHorizontally(animationSpec = tween(280)) { fullWidth -> -fullWidth } + fadeOut(tween(200)))
                        } else {
                            (slideInHorizontally(animationSpec = tween(280)) { fullWidth -> -fullWidth } + fadeIn(tween(200)))
                                .togetherWith(slideOutHorizontally(animationSpec = tween(280)) { fullWidth -> fullWidth } + fadeOut(tween(200)))
                        }
                    },
                    label = "ProfileTabTransition"
                ) { targetTab ->
                    if (targetTab == 0) {
                        // TAB 0: BERKAS (Screenshot_20261018-054741.jpg)
                        BerkasTabContent(
                            userData = userData,
                            onOpenEditName = { showNameDialog = true },
                            onOpenResetConfirm = { showResetConfirmDialog = true },
                            onShareProfile = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Ayo tanding di Nana Tayantuh Worms Zone IO! Skor Terbaikku: ${userData.highScore} dengan ${userData.totalKills} cacing dikalahkan!"
                                    )
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Bagikan Profil"))
                            }
                        )
                    } else {
                        // TAB 1: PRESTASI (Screenshot_20261018-054746.jpg)
                        PrestasiTabContent(
                            userData = userData,
                            onClaim = { id, reward -> onClaimAchievement(id, reward) }
                        )
                    }
                }
            }
        }

        // Edit Name Dialog
        if (showNameDialog) {
            AlertDialog(
                onDismissRequest = { showNameDialog = false },
                title = { Text("Ganti Nama Cacing", fontWeight = FontWeight.Black) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Masukkan nama baru Anda:", fontSize = 13.sp)
                        OutlinedTextField(
                            value = tempName,
                            onValueChange = { if (it.length <= 16) tempName = it },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFFACC15),
                                unfocusedBorderColor = Color(0xFF94A3B8)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (tempName.isNotBlank()) {
                                onUpdateName(tempName)
                            }
                            showNameDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E))
                    ) {
                        Text("Simpan", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showNameDialog = false }) {
                        Text("Batal")
                    }
                }
            )
        }

        // Confirm Reset Data Dialog
        if (showResetConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showResetConfirmDialog = false },
                title = { Text("Hapus Semua Data Permainan?", fontWeight = FontWeight.Black, color = Color(0xFFDC2626)) },
                text = {
                    Text(
                        "Apakah Anda yakin ingin mengatur ulang semua skor, koin, dan data cacing ke awal? Tindakan ini tidak dapat dibatalkan.",
                        fontSize = 13.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onResetData()
                            showResetConfirmDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                    ) {
                        Text("Reset Sekarang", fontWeight = FontWeight.Black)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetConfirmDialog = false }) {
                        Text("Batal")
                    }
                }
            )
        }
    }
}

// TAB 0: BERKAS
@Composable
private fun BerkasTabContent(
    userData: UserGameData,
    onOpenEditName: () -> Unit,
    onOpenResetConfirm: () -> Unit,
    onShareProfile: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // LEFT COLUMN: Avatar & Level Card (Screenshot_20261018-054741.jpg)
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF274288),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF4D74C9)),
            modifier = Modifier
                .width(260.dp)
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Orange Avatar Card with Level XP Bar
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFEA580C),
                        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFDBA74)),
                        shadowElevation = 6.dp,
                        modifier = Modifier.size(90.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Canvas(modifier = Modifier.size(56.dp)) {
                                val cx = size.width / 2f
                                val cy = size.height / 2f
                                val r = size.minDimension / 2f - 2f
                                // Head base circle
                                drawCircle(color = Color(0xFF475569), radius = r, center = Offset(cx, cy))
                                drawCircle(color = Color(0xFFFDE047), radius = r, center = Offset(cx, cy), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5f))
                                // Left eye white
                                drawCircle(color = Color.White, radius = r * 0.38f, center = Offset(cx - r * 0.32f, cy))
                                // Right eye white
                                drawCircle(color = Color.White, radius = r * 0.38f, center = Offset(cx + r * 0.32f, cy))
                                // Left pupil
                                drawCircle(color = Color.Black, radius = r * 0.22f, center = Offset(cx - r * 0.30f, cy))
                                // Right pupil
                                drawCircle(color = Color.Black, radius = r * 0.22f, center = Offset(cx + r * 0.34f, cy))
                                // Eye shines
                                drawCircle(color = Color.White, radius = r * 0.08f, center = Offset(cx - r * 0.35f, cy - r * 0.08f))
                                drawCircle(color = Color.White, radius = r * 0.08f, center = Offset(cx + r * 0.29f, cy - r * 0.08f))
                            }
                        }
                    }

                    // Level XP Progress Bar
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF1E293B),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF59E0B)),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(14.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.65f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFFF97316), Color(0xFFFBBF24))
                                        )
                                    )
                            )
                        }
                    }

                    // Player Name Row with Edit Pen
                    Surface(
                        onClick = onOpenEditName,
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF1E3269),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4C6FB8)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Language, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = userData.playerName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                            }
                            Icon(Icons.Default.Edit, contentDescription = "Edit Nama", tint = Color(0xFFFDE047), modifier = Modifier.size(14.dp))
                        }
                    }

                    // Level Shield Badge
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF1E3269),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4C6FB8)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFF0284C7),
                                    modifier = Modifier.size(22.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("16", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Level Anda",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                            }
                            Text("16", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFFFDE047))
                        }
                    }
                }

                // BOTTOM ACTION BUTTONS: Red Trash [🗑️] + Blue Share [🔗]
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Red Trash Button
                        Surface(
                            onClick = onOpenResetConfirm,
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFEF4444),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFCA5A5)),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Delete, contentDescription = "Reset Data", tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }

                        // Blue Share Button
                        Surface(
                            onClick = onShareProfile,
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF0284C7),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF7DD3FC)),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Share, contentDescription = "Bagikan", tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    Text(
                        text = "Nana Tayantuh • Worms Zone IO Edition",
                        fontSize = 9.sp,
                        color = Color(0x99FFFFFF),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // RIGHT GRID: 6 STATS CARDS (Screenshot_20261018-054741.jpg)
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f).fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Card 1: Skor Terbaik
            item {
                StatCard(
                    icon = Icons.Default.EmojiEvents,
                    title = "Skor Terbaik",
                    value = NumberFormat.getNumberInstance(Locale.US).format(userData.highScore),
                    accentColor = Color(0xFFF59E0B)
                )
            }
            // Card 2: Waktu Bertahan Hidup Terbaik
            item {
                val mins = userData.bestSurvivalSeconds / 60
                val secs = userData.bestSurvivalSeconds % 60
                StatCard(
                    icon = Icons.Default.Timer,
                    title = "Waktu Bertahan Hidup",
                    value = "${mins}m ${secs}d",
                    accentColor = Color(0xFF38BDF8)
                )
            }
            // Card 3: Cacing dikalahkan
            item {
                StatCard(
                    icon = Icons.Default.LocalFireDepartment,
                    title = "Cacing dikalahkan",
                    value = "${userData.totalKills}",
                    accentColor = Color(0xFFEF4444)
                )
            }
            // Card 4: Game yang Dimainkan
            item {
                StatCard(
                    icon = Icons.Default.SportsEsports,
                    title = "Game Dimainkan",
                    value = "${userData.totalGames}",
                    accentColor = Color(0xFFA855F7)
                )
            }
            // Card 5: Total Waktu Bermain
            item {
                val hours = userData.totalPlayTimeSeconds / 3600
                val mins = (userData.totalPlayTimeSeconds % 3600) / 60
                val secs = userData.totalPlayTimeSeconds % 60
                StatCard(
                    icon = Icons.Default.History,
                    title = "Total Waktu Bermain",
                    value = "${hours}j ${mins}m ${secs}d",
                    accentColor = Color(0xFF10B981)
                )
            }
            // Card 6: Cacing dikumpulkan
            item {
                StatCard(
                    icon = Icons.Default.Collections,
                    title = "Cacing dikumpulkan",
                    value = "${userData.unlockedSkins.size}",
                    accentColor = Color(0xFFEAB308)
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    icon: ImageVector,
    title: String,
    value: String,
    accentColor: Color
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFE0F2FE),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFBAE6FD)),
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth().height(105.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0369A1),
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            Surface(
                shape = CircleShape,
                color = accentColor.copy(alpha = 0.18f),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF0F172A),
                textAlign = TextAlign.Center
            )
        }
    }
}

// TAB 1: PRESTASI (Screenshot_20261018-054746.jpg)
@Composable
private fun PrestasiTabContent(
    userData: UserGameData,
    onClaim: (String, Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // LEFT COLUMN: Greek Temple Pillars with Bronze, Silver, Gold Winged Statues
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1E356C),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF436AB5)),
            modifier = Modifier
                .width(260.dp)
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Header Pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0x99000000)
                ) {
                    Text(
                        text = "Mainkan game dan dapatkan hadiah!",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        textAlign = TextAlign.Center
                    )
                }

                // 3 Winged Statues on Pillars
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Bronze Statue (22/42)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.MilitaryTech,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(32.dp)
                        )
                        Surface(
                            shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp),
                            color = Color(0xFFFED7AA),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF97316)),
                            modifier = Modifier.width(42.dp).height(50.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("22/42", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF9A3412))
                            }
                        }
                    }

                    // Silver Statue (10/42)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.MilitaryTech,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(34.dp)
                        )
                        Surface(
                            shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp),
                            color = Color(0xFFE2E8F0),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF94A3B8)),
                            modifier = Modifier.width(44.dp).height(75.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("10/42", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF334155))
                            }
                        }
                    }

                    // Gold Statue (4/47)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(38.dp)
                        )
                        Surface(
                            shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp),
                            color = Color(0xFFFEF08A),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEAB308)),
                            modifier = Modifier.width(46.dp).height(100.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("4/47", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF854D0E))
                            }
                        }
                    }
                }

                Text(
                    text = "Capai rekor lebih tinggi untuk membuka patung emas legendaris!",
                    fontSize = 10.sp,
                    color = Color(0xCCFFFFFF),
                    textAlign = TextAlign.Center
                )
            }
        }

        // RIGHT GRID: 6 ACHIEVEMENT CARDS (Screenshot_20261018-054746.jpg)
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f).fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Achievement 1: Demam Emas (Claimable reward!)
            item {
                val isClaimed = userData.claimedAchievements.contains("gold_rush")
                AchievementCard(
                    title = "Demam Emas",
                    icon = Icons.Default.Stars,
                    iconTint = Color(0xFFEAB308),
                    isClaimable = !isClaimed,
                    isClaimed = isClaimed,
                    rewardCoins = 250,
                    progressText = if (isClaimed) "Selesai" else "5000/5000",
                    onClaim = { onClaim("gold_rush", 250) }
                )
            }

            // Achievement 2: Rutin Tanpa Henti
            item {
                AchievementCard(
                    title = "Rutin Tanpa Henti",
                    icon = Icons.Default.CalendarToday,
                    iconTint = Color(0xFF0284C7),
                    isClaimable = false,
                    isClaimed = false,
                    rewardCoins = 250,
                    progressText = "17/20",
                    onClaim = {}
                )
            }

            // Achievement 3: Peningkatan
            item {
                AchievementCard(
                    title = "Peningkatan",
                    icon = Icons.Default.FlashOn,
                    iconTint = Color(0xFFF59E0B),
                    isClaimable = false,
                    isClaimed = false,
                    rewardCoins = 500,
                    progressText = "24/30",
                    onClaim = {}
                )
            }

            // Achievement 4: Predator Arena (Claimable reward!)
            item {
                val isClaimed = userData.claimedAchievements.contains("predator_500")
                val kills = userData.totalKills
                val canClaim = kills >= 400 && !isClaimed
                AchievementCard(
                    title = "Predator Arena",
                    icon = Icons.Default.LocalFireDepartment,
                    iconTint = Color(0xFFEF4444),
                    isClaimable = canClaim,
                    isClaimed = isClaimed,
                    rewardCoins = 500,
                    progressText = if (isClaimed) "Selesai" else "$kills/500",
                    onClaim = { onClaim("predator_500", 500) }
                )
            }

            // Achievement 5: Kelas Berat
            item {
                AchievementCard(
                    title = "Kelas Berat",
                    icon = Icons.Default.FitnessCenter,
                    iconTint = Color(0xFFA855F7),
                    isClaimable = false,
                    isClaimed = false,
                    rewardCoins = 100,
                    progressText = "${userData.highScore}/500000",
                    onClaim = {}
                )
            }

            // Achievement 6: Blitz - Kecepatan Tanpa Batas!
            item {
                AchievementCard(
                    title = "Blitz - Kecepatan",
                    icon = Icons.Default.RocketLaunch,
                    iconTint = Color(0xFF06B6D4),
                    isClaimable = false,
                    isClaimed = false,
                    rewardCoins = 500,
                    progressText = "3595/6000",
                    onClaim = {}
                )
            }
        }
    }
}

@Composable
private fun AchievementCard(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    isClaimable: Boolean,
    isClaimed: Boolean,
    rewardCoins: Int,
    progressText: String,
    onClaim: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFE0F2FE),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFBAE6FD)),
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth().height(115.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0369A1),
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            Surface(
                shape = CircleShape,
                color = iconTint.copy(alpha = 0.16f),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
                }
            }

            if (isClaimed) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFE2E8F0)
                ) {
                    Text(
                        text = "✓ Selesai",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            } else if (isClaimable) {
                var isClaiming by remember { mutableStateOf(false) }
                Surface(
                    onClick = {
                        if (!isClaiming) {
                            isClaiming = true
                            onClaim()
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    color = if (isClaiming) Color(0xFF16A34A) else Color(0xFF22C55E),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isClaiming) "Mengklaim..." else "Klaim (+$rewardCoins)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF0284C7),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(progressText, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("+$rewardCoins Koin", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFDE047))
                    }
                }
            }
        }
    }
}
