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
import androidx.compose.foundation.layout.widthIn
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
    var dotCount by remember { mutableStateOf(1) }

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

    LaunchedEffect(Unit) {
        while (true) {
            delay(350)
            dotCount = (dotCount % 3) + 1
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFBBF24), // Bright golden amber yellow
                        Color(0xFFF59E0B), // Warm honey yellow
                        Color(0xFFD97706)  // Deep golden motif
                    )
                )
            )
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Yellow Geometric Motif Canvas (Grid, Diamonds, Honeycomb Rings)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Soft radial sunlight glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x60FFFBEB),
                        Color(0x25FEF3C7),
                        Color.Transparent
                    ),
                    center = Offset(w * 0.5f, h * 0.5f),
                    radius = w * 0.65f
                ),
                radius = w * 0.65f,
                center = Offset(w * 0.5f, h * 0.5f)
            )

            // 2. Geometric motif grid pattern in warm amber tone
            val step = 48.dp.toPx()
            var x = 0f
            while (x < w) {
                drawLine(
                    color = Color(0x2078350F),
                    start = Offset(x, 0f),
                    end = Offset(x, h),
                    strokeWidth = 1f
                )
                x += step
            }
            var y = 0f
            while (y < h) {
                drawLine(
                    color = Color(0x2078350F),
                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = 1f
                )
                y += step
            }

            // 3. Motif diamond markers and circles at grid intersections
            var ix = 0f
            while (ix < w + step) {
                var iy = 0f
                while (iy < h + step) {
                    drawCircle(
                        color = Color(0x2892400E),
                        radius = 2.5.dp.toPx(),
                        center = Offset(ix, iy)
                    )
                    drawCircle(
                        color = Color(0x50FFFBEB),
                        radius = 1.2.dp.toPx(),
                        center = Offset(ix, iy)
                    )
                    iy += step
                }
                ix += step
            }
        }

        // Center Content Card - Seamless warm golden amber glass matching the yellow background
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color(0x2E78350F), // Warm transparent amber matching the background
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0x60FFFBEB)),
            modifier = Modifier
                .padding(24.dp)
                .widthIn(max = 440.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 26.dp)
            ) {
                // Animated Worm Mascot Showcase (Continuous, seamless, no flicker)
                Box(
                    modifier = Modifier
                        .size(240.dp, 90.dp)
                        .scale(pulseScale),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val cx = size.width / 2f
                        val cy = size.height / 2f

                        val headY = cy + sin(wigglePhase) * 16f
                        val headX = cx + 55f
                        val vy = cos(wigglePhase) * 16f
                        val vx = 16f
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
                                val segPhase = wigglePhase - i * 0.38f
                                val px = (cx + 55f) - i * 14f
                                val py = cy + sin(segPhase) * 16f
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

                Spacer(modifier = Modifier.height(14.dp))

                // Game Title Logo - Rich dark amber / brown for high legibility on yellow
                Text(
                    text = "Cacing Besar Battle",
                    style = MaterialTheme.typography.displayMedium,
                    color = Color(0xFF451A03),
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0x3578350F),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF92400E).copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "${StringsHelper.get("outer_arena", language).uppercase()} • WIDE OPEN WORLD",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF78350F),
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Progress Bar Container & Single Animated Loading Text
                Column(
                    modifier = Modifier.width(320.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Custom Gradient Progress Bar
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0x3578350F),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x5078350F))
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progressAnim.value)
                                    .height(12.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(
                                                Color(0xFF15803D),
                                                Color(0xFF22C55E),
                                                Color(0xFFFDE047)
                                            )
                                        )
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Single Loading Text with moving dots (e.g. Loading ., Loading .., Loading ...)
                    Text(
                        text = "Loading" + ".".repeat(dotCount),
                        fontSize = 16.sp,
                        color = Color(0xFF451A03),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
