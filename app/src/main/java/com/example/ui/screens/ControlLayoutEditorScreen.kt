package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.OpenWith
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.StringsHelper
import com.example.data.UserGameData

@Composable
fun ControlLayoutEditorScreen(
    userData: UserGameData,
    onSaveLayout: (joyX: Int, joyY: Int, joyScale: Float, boostX: Int, boostY: Int, boostScale: Float) -> Unit,
    onResetDefaults: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val lang = userData.language
    val density = LocalDensity.current

    var joyOffsetX by remember { mutableIntStateOf(userData.joystickOffsetX) }
    var joyOffsetY by remember { mutableIntStateOf(userData.joystickOffsetY) }
    var joyScale by remember { mutableFloatStateOf(userData.joystickScale) }

    var boostOffsetX by remember { mutableIntStateOf(userData.boostOffsetX) }
    var boostOffsetY by remember { mutableIntStateOf(userData.boostOffsetY) }
    var boostScale by remember { mutableFloatStateOf(userData.boostScale) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D111E))
            .testTag("control_layout_editor_screen")
    ) {
        val screenWidthDp = maxWidth.value
        val screenHeightDp = maxHeight.value

        // Background decorative arena grid to simulate in-game feel
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridStep = 50.dp.toPx()
            var x = 0f
            while (x < size.width) {
                drawLine(
                    color = Color(0xFF1E263D).copy(alpha = 0.6f),
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = 1f
                )
                x += gridStep
            }
            var y = 0f
            while (y < size.height) {
                drawLine(
                    color = Color(0xFF1E263D).copy(alpha = 0.6f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1f
                )
                y += gridStep
            }
        }

        // TOP FLOATING CONTROL BAR: Title, Save, Reset, Cancel, and Size Buttons
        Surface(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            shape = RoundedCornerShape(18.dp),
            color = Color(0xEE161B2E),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF353D63)),
            shadowElevation = 12.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back & Title
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = StringsHelper.get("cancel", lang),
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = StringsHelper.get("edit_layout", lang),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF4ADE80),
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = StringsHelper.get("edit_layout_subtitle", lang),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFA0AEC0)
                        )
                    }
                }

                // Size Controls and Action Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Joystick Size Scaler
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF202740),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4ADE80).copy(alpha = 0.4f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("🎮 Ukuran:", fontSize = 11.sp, color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold)
                            IconButton(
                                onClick = { joyScale = (joyScale - 0.1f).coerceIn(0.75f, 1.5f) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            }
                            Text("${(joyScale * 100).toInt()}%", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            IconButton(
                                onClick = { joyScale = (joyScale + 0.1f).coerceIn(0.75f, 1.5f) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            }
                        }
                    }

                    // Boost Size Scaler
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF202740),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF9100).copy(alpha = 0.4f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("⚡ Boost:", fontSize = 11.sp, color = Color(0xFFFF9100), fontWeight = FontWeight.Bold)
                            IconButton(
                                onClick = { boostScale = (boostScale - 0.1f).coerceIn(0.75f, 1.5f) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            }
                            Text("${(boostScale * 100).toInt()}%", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            IconButton(
                                onClick = { boostScale = (boostScale + 0.1f).coerceIn(0.75f, 1.5f) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            }
                        }
                    }

                    // Reset Button
                    OutlinedButton(
                        onClick = {
                            joyOffsetX = 36
                            joyOffsetY = 36
                            joyScale = 1.05f
                            boostOffsetX = 36
                            boostOffsetY = 36
                            boostScale = 1.05f
                            onResetDefaults()
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFFB74D))
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(StringsHelper.get("reset_layout", lang), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    // Save Button
                    Button(
                        onClick = {
                            onSaveLayout(joyOffsetX, joyOffsetY, joyScale, boostOffsetX, boostOffsetY, boostScale)
                            onBack()
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4ADE80))
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            StringsHelper.get("save_layout", lang),
                            fontSize = 12.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }

        // DRAGGABLE VIRTUAL JOYSTICK
        val joySizeDp = (126 * joyScale).dp
        val maxJoyXDp = (screenWidthDp - joySizeDp.value - 20f).coerceAtLeast(10f).toInt()
        val maxJoyYDp = (screenHeightDp - joySizeDp.value - 80f).coerceAtLeast(10f).toInt()

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = joyOffsetX.dp, y = (-joyOffsetY).dp)
                .size(joySizeDp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val deltaXDp = with(density) { dragAmount.x.toDp().value.toInt() }
                        val deltaYDp = with(density) { dragAmount.y.toDp().value.toInt() }
                        // X increases to right, Y increases upward from bottom so minus deltaY
                        joyOffsetX = (joyOffsetX + deltaXDp).coerceIn(10, maxJoyXDp)
                        joyOffsetY = (joyOffsetY - deltaYDp).coerceIn(10, maxJoyYDp)
                    }
                }
        ) {
            // Visual Drag Target for Joystick
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, Color(0xFF4ADE80), CircleShape)
                    .shadow(16.dp, CircleShape, spotColor = Color(0xFF4ADE80)),
                shape = CircleShape,
                color = Color(0x334ADE80)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    // Outer ring
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            color = Color(0xFF4ADE80),
                            radius = size.width * 0.46f,
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
                    // Center handle
                    Surface(
                        modifier = Modifier.size((52 * joyScale).dp),
                        shape = CircleShape,
                        color = Color(0xFF4ADE80),
                        shadowElevation = 8.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.OpenWith,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            // Tag badge
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-24).dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xEE161B2E),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4ADE80))
            ) {
                Text(
                    text = "🎮 JOYSTICK (Geser Saya!)",
                    fontSize = 10.sp,
                    color = Color(0xFF4ADE80),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        // DRAGGABLE TURBO BOOST BUTTON
        val boostSizeDp = (86 * boostScale).dp
        val maxBoostXDp = (screenWidthDp - boostSizeDp.value - 20f).coerceAtLeast(10f).toInt()
        val maxBoostYDp = (screenHeightDp - boostSizeDp.value - 80f).coerceAtLeast(10f).toInt()

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-boostOffsetX).dp, y = (-boostOffsetY).dp)
                .size(boostSizeDp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val deltaXDp = with(density) { dragAmount.x.toDp().value.toInt() }
                        val deltaYDp = with(density) { dragAmount.y.toDp().value.toInt() }
                        // X increases to left from right edge so minus deltaX, Y increases upward so minus deltaY
                        boostOffsetX = (boostOffsetX - deltaXDp).coerceIn(10, maxBoostXDp)
                        boostOffsetY = (boostOffsetY - deltaYDp).coerceIn(10, maxBoostYDp)
                    }
                }
        ) {
            // Visual Drag Target for Boost Button
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.5.dp, Color(0xFFFF9100), CircleShape)
                    .shadow(16.dp, CircleShape, spotColor = Color(0xFFFF9100)),
                shape = CircleShape,
                color = Color(0x44FF9100)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    // Inner glowing circle
                    Surface(
                        modifier = Modifier.size((64 * boostScale).dp),
                        shape = CircleShape,
                        color = Brush.radialGradient(
                            listOf(Color(0xFFFFB74D), Color(0xFFFF9100), Color(0xFFFF6D00))
                        ).let { Color(0xFFFF9100) }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size((28 * boostScale).dp)
                            )
                            Text(
                                text = "BOOST",
                                fontSize = (10 * boostScale).sp,
                                color = Color.White,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }

            // Tag badge
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-24).dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xEE161B2E),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF9100))
            ) {
                Text(
                    text = "⚡ TOMBOL BOOST (Geser Saya!)",
                    fontSize = 10.sp,
                    color = Color(0xFFFF9100),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        // CENTER HUD HELPER OVERLAY
        Surface(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0x88161B2E),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3454))
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎯 Sentuh & Geser Bebas dengan Jari",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Joystick: X=$joyOffsetX dp, Y=$joyOffsetY dp | Boost: X=$boostOffsetX dp, Y=$boostOffsetY dp",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF81C784)
                )
            }
        }
    }
}
