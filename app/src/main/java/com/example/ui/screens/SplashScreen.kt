package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.StringsHelper
import com.example.model.SkinId
import com.example.model.Vec2
import com.example.model.Worm
import com.example.ui.game.WormsRenderer
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SplashScreen(
    language: AppLanguage,
    onLoadingComplete: () -> Unit
) {
    val progressAnim = remember { Animatable(0f) }
    var currentTipIndex by remember { mutableStateOf(0) }

    val tips = remember(language) {
        listOf(
            StringsHelper.get("tip1", language),
            StringsHelper.get("tip2", language),
            StringsHelper.get("tip3", language),
            StringsHelper.get("tip4", language)
        )
    }

    val infiniteTransition = rememberInfiniteTransition(label = "splashAnim")
    val wigglePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    LaunchedEffect(Unit) {
        progressAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2200, easing = LinearEasing)
        )
        delay(250)
        onLoadingComplete()
    }

    LaunchedEffect(tips.size) {
        var idx = 0
        while (true) {
            delay(800)
            idx = (idx + 1) % tips.size
            currentTipIndex = idx
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1B284D),
                        Color(0xFF10172E),
                        Color(0xFF080C18)
                    )
                )
            )
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Animated Worm Mascot Showcase
            Box(
                modifier = Modifier
                    .size(240.dp, 90.dp)
                    .scale(pulseScale),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cx = size.width / 2f
                    val cy = size.height / 2f

                    val headY = cy + sin(wigglePhase) * 18f
                    val headX = cx + 55f + cos(wigglePhase * 0.5f) * 10f
                    val vy = cos(wigglePhase) * 18f
                    val vx = 14f - sin(wigglePhase * 0.5f) * 5f
                    val headAngle = kotlin.math.atan2(vy, vx)

                    val splashWorm = Worm(
                        id = "splash",
                        name = "nana",
                        isPlayer = true,
                        head = Vec2(headX, headY),
                        angle = headAngle,
                        skin = SkinId.RAINBOW,
                        mass = 85f
                    ).apply {
                        bodyPoints.clear()
                        for (i in 0 until 16) {
                            val segPhase = wigglePhase - i * 0.45f
                            val wave = sin(segPhase) * 18f
                            val px = (cx + 55f) - i * 14f + cos(segPhase * 0.5f) * 8f
                            val py = cy + wave
                            bodyPoints.add(Vec2(px, py))
                        }
                    }

                    WormsRenderer.drawWorm(
                        drawScope = this,
                        worm = splashWorm,
                        cameraOffset = Offset.Zero,
                        zoom = 1.05f
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Game Title Logo
            Text(
                text = "nana tayantuh",
                style = MaterialTheme.typography.displayMedium,
                color = Color(0xFF4ADE80),
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF263254),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFFD700))
            ) {
                Text(
                    text = "${StringsHelper.get("outer_arena", language).uppercase()} • WIDE OPEN WORLD",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFFFD700),
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Progress Bar Container
            Column(
                modifier = Modifier.width(360.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Percentage Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = StringsHelper.get("loading", language),
                        fontSize = 11.sp,
                        color = Color(0xFFA0AEC0),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${(progressAnim.value * 100).toInt()}%",
                        fontSize = 13.sp,
                        color = Color(0xFF4ADE80),
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Custom Gradient Progress Bar
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF14192B),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E3858))
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progressAnim.value)
                                .height(14.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(
                                            Color(0xFF00E5FF),
                                            Color(0xFF4ADE80),
                                            Color(0xFFFFD700)
                                        )
                                    )
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Animated Tip Message (Fixed height prevents any layout jump)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tips[currentTipIndex],
                        fontSize = 12.sp,
                        color = Color(0xFFCBD5E1),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
