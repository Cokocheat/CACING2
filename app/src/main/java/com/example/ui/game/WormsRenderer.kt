package com.example.ui.game

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.model.FaceAccessory
import com.example.model.FoodItem
import com.example.model.FoodType
import com.example.model.GameConfig
import com.example.model.GameParticle
import com.example.model.PatternType
import com.example.model.PotionItem
import com.example.model.PotionType
import com.example.model.SkinId
import com.example.model.Vec2
import com.example.model.Worm
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object WormsRenderer {

    fun drawArenaBackground(
        drawScope: DrawScope,
        cameraOffset: Offset,
        zoom: Float,
        arenaCenter: Vec2,
        arenaRadius: Float
    ) {
        with(drawScope) {
            // Draw grid lines
            val gridSize = 100f * zoom
            val startX = (cameraOffset.x % gridSize)
            val startY = (cameraOffset.y % gridSize)

            val gridColor = Color(0xFF1E243D)
            var x = startX
            while (x < size.width) {
                drawLine(
                    color = gridColor,
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = 1.2f
                )
                x += gridSize
            }
            var y = startY
            while (y < size.height) {
                drawLine(
                    color = gridColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.2f
                )
                y += gridSize
            }

            // Draw Arena Boundary Danger Zone
            val centerOnScreen = Offset(
                (arenaCenter.x) * zoom + cameraOffset.x,
                (arenaCenter.y) * zoom + cameraOffset.y
            )
            val radiusOnScreen = arenaRadius * zoom

            // Danger outer glow
            drawCircle(
                color = Color(0x33FF1744),
                radius = radiusOnScreen + 16f * zoom,
                center = centerOnScreen,
                style = Stroke(width = 24f * zoom)
            )

            // Outer danger border line
            drawCircle(
                color = Color(0xFFFF1744),
                radius = radiusOnScreen,
                center = centerOnScreen,
                style = Stroke(width = 6f * zoom)
            )
        }
    }

    fun drawFood(drawScope: DrawScope, food: FoodItem, screenPos: Offset, zoom: Float) {
        with(drawScope) {
            val r = food.radius * zoom
            val x = screenPos.x
            val y = screenPos.y

            when (food.type) {
                FoodType.DONUT -> {
                    // Golden pastry base
                    drawCircle(color = Color(0xFFE0A96D), radius = r, center = screenPos)
                    // Pink frosting
                    drawCircle(color = Color(0xFFFF4081), radius = r * 0.85f, center = screenPos)
                    // Donut hole
                    drawCircle(color = Color(0xFF10131F), radius = r * 0.35f, center = screenPos)
                    // Sprinkles
                    drawCircle(color = Color(0xFFFFEB3B), radius = r * 0.12f, center = Offset(x - r * 0.45f, y - r * 0.3f))
                    drawCircle(color = Color(0xFF00E5FF), radius = r * 0.12f, center = Offset(x + r * 0.4f, y - r * 0.4f))
                    drawCircle(color = Color(0xFF76FF03), radius = r * 0.12f, center = Offset(x + r * 0.35f, y + r * 0.35f))
                    drawCircle(color = Color(0xFFFFFFFF), radius = r * 0.12f, center = Offset(x - r * 0.35f, y + r * 0.4f))
                }
                FoodType.BURGER -> {
                    // Bun bottom
                    drawRoundRect(
                        color = Color(0xFFD49B4F),
                        topLeft = Offset(x - r, y + r * 0.2f),
                        size = Size(r * 2f, r * 0.7f),
                        cornerRadius = CornerRadius(r * 0.3f, r * 0.3f)
                    )
                    // Patty & lettuce
                    drawRoundRect(
                        color = Color(0xFF4E342E),
                        topLeft = Offset(x - r * 0.95f, y - r * 0.05f),
                        size = Size(r * 1.9f, r * 0.4f),
                        cornerRadius = CornerRadius(r * 0.2f, r * 0.2f)
                    )
                    drawRoundRect(
                        color = Color(0xFF00E676),
                        topLeft = Offset(x - r * 0.9f, y - r * 0.3f),
                        size = Size(r * 1.8f, r * 0.3f),
                        cornerRadius = CornerRadius(r * 0.15f, r * 0.15f)
                    )
                    // Top bun
                    drawRoundRect(
                        color = Color(0xFFFFB74D),
                        topLeft = Offset(x - r, y - r),
                        size = Size(r * 2f, r * 0.85f),
                        cornerRadius = CornerRadius(r * 0.6f, r * 0.6f)
                    )
                    // Sesame seeds
                    drawCircle(color = Color(0xFFFFF9C4), radius = r * 0.08f, center = Offset(x - r * 0.35f, y - r * 0.5f))
                    drawCircle(color = Color(0xFFFFF9C4), radius = r * 0.08f, center = Offset(x + r * 0.3f, y - r * 0.55f))
                    drawCircle(color = Color(0xFFFFF9C4), radius = r * 0.08f, center = Offset(x, y - r * 0.7f))
                }
                FoodType.CHEESE -> {
                    val path = Path().apply {
                        moveTo(x - r * 0.9f, y + r * 0.8f)
                        lineTo(x + r * 0.9f, y + r * 0.8f)
                        lineTo(x, y - r * 0.9f)
                        close()
                    }
                    drawPath(path, color = Color(0xFFFFD54F), style = Fill)
                    // Holes
                    drawCircle(color = Color(0xFFFFB300), radius = r * 0.2f, center = Offset(x - r * 0.2f, y + r * 0.3f))
                    drawCircle(color = Color(0xFFFFB300), radius = r * 0.14f, center = Offset(x + r * 0.3f, y + r * 0.1f))
                }
                FoodType.WATERMELON_SLICE -> {
                    // Green rind
                    drawArc(
                        color = Color(0xFF2E7D32),
                        startAngle = 0f,
                        sweepAngle = 180f,
                        useCenter = true,
                        topLeft = Offset(x - r, y - r),
                        size = Size(r * 2, r * 2)
                    )
                    // Red flesh
                    drawArc(
                        color = Color(0xFFFF1744),
                        startAngle = 0f,
                        sweepAngle = 180f,
                        useCenter = true,
                        topLeft = Offset(x - r * 0.82f, y - r * 0.82f),
                        size = Size(r * 1.64f, r * 1.64f)
                    )
                    // Seeds
                    drawCircle(color = Color(0xFF212121), radius = r * 0.09f, center = Offset(x - r * 0.35f, y + r * 0.3f))
                    drawCircle(color = Color(0xFF212121), radius = r * 0.09f, center = Offset(x, y + r * 0.45f))
                    drawCircle(color = Color(0xFF212121), radius = r * 0.09f, center = Offset(x + r * 0.35f, y + r * 0.3f))
                }
                FoodType.GOLDEN_COIN -> {
                    drawCircle(color = Color(0xFFFFD700), radius = r, center = screenPos)
                    drawCircle(color = Color(0xFFFFA000), radius = r * 0.85f, center = screenPos, style = Stroke(width = 2f * zoom))
                    drawCircle(color = Color(0xFFFFF59D), radius = r * 0.3f, center = screenPos)
                }
                FoodType.APPLE -> {
                    drawCircle(color = Color(0xFFFF1744), radius = r, center = screenPos)
                    // Leaf
                    drawOval(
                        color = Color(0xFF4CAF50),
                        topLeft = Offset(x - r * 0.2f, y - r * 1.3f),
                        size = Size(r * 0.6f, r * 0.4f)
                    )
                }
                FoodType.DEATH_FEAST -> {
                    // Glowing Feast Bonanza
                    drawCircle(color = Color(0x44FF1744), radius = r * 1.5f, center = screenPos)
                    drawCircle(color = Color(0xFFFF5252), radius = r, center = screenPos)
                    drawCircle(color = Color(0xFFFFD54F), radius = r * 0.6f, center = screenPos)
                    drawCircle(color = Color.White, radius = r * 0.25f, center = screenPos)
                }
                else -> {
                    // Standard treats
                    drawCircle(color = food.type.primaryColor, radius = r, center = screenPos)
                    drawCircle(color = Color.White.copy(alpha = 0.6f), radius = r * 0.4f, center = Offset(x - r * 0.3f, y - r * 0.3f))
                }
            }
        }
    }

    fun drawPotion(drawScope: DrawScope, potion: PotionItem, screenPos: Offset, zoom: Float) {
        with(drawScope) {
            val r = potion.radius * zoom
            val x = screenPos.x
            val y = screenPos.y

            // Pulsing aura
            val pulse = (sin(potion.pulsePhase) * 0.15f + 0.85f)
            drawCircle(
                color = potion.type.color.copy(alpha = 0.35f * pulse),
                radius = r * 1.6f * pulse,
                center = screenPos
            )

            // Flask base
            drawCircle(color = potion.type.color, radius = r, center = screenPos)

            // Neck of potion flask
            drawRoundRect(
                color = Color.White.copy(alpha = 0.8f),
                topLeft = Offset(x - r * 0.3f, y - r * 1.4f),
                size = Size(r * 0.6f, r * 0.5f),
                cornerRadius = CornerRadius(2f, 2f)
            )

            // Shiny specular highlight
            drawCircle(
                color = Color.White.copy(alpha = 0.75f),
                radius = r * 0.35f,
                center = Offset(x - r * 0.35f, y - r * 0.35f)
            )

            // Flask stroke outline
            drawCircle(
                color = Color.White,
                radius = r,
                center = screenPos,
                style = Stroke(width = 2.5f * zoom)
            )
        }
    }

    fun drawWorm(
        drawScope: DrawScope,
        worm: Worm,
        cameraOffset: Offset,
        zoom: Float,
        isProtected: Boolean = false,
        gameTime: Float = 0f
    ) {
        if (!worm.isAlive) return
        with(drawScope) {
            val baseR = worm.segmentRadius * zoom

            // 1. Draw Body Segments from tail to neck
            val totalSegs = worm.bodyPoints.size
            for (i in totalSegs - 1 downTo 0) {
                val pt = worm.bodyPoints[i]
                val screenX = pt.x * zoom + cameraOffset.x
                val screenY = pt.y * zoom + cameraOffset.y

                // Taper tail slightly
                val taper = if (i > totalSegs - 6) {
                    0.55f + ((totalSegs - 1 - i).toFloat() / 5f) * 0.45f
                } else {
                    1f
                }
                val segR = baseR * taper

                // Determine segment color based on skin pattern
                val segColor = getSegmentColor(worm.skin, i, totalSegs)

                // Draw base segment
                drawCircle(color = segColor, radius = segR, center = Offset(screenX, screenY))

                // Draw 3D soft highlight shine on top edge for plumpness
                drawCircle(
                    color = Color.White.copy(alpha = 0.28f),
                    radius = segR * 0.5f,
                    center = Offset(screenX - segR * 0.28f, screenY - segR * 0.28f)
                )

                // Distinctive skin decoration
                when (worm.skin.patternType) {
                    PatternType.WATERMELON_SEGMENTS -> {
                        // Green rind edge + seeds
                        if (i % 3 == 0) {
                            drawCircle(color = Color(0xFF1B5E20), radius = segR, center = Offset(screenX, screenY), style = Stroke(width = 3f * zoom))
                            drawCircle(color = Color(0xFF212121), radius = segR * 0.15f, center = Offset(screenX, screenY))
                        }
                    }
                    PatternType.GOLDEN_SHINE -> {
                        if (i % 4 == 0) {
                            drawCircle(color = Color(0xFFFFF9C4), radius = segR * 0.35f, center = Offset(screenX, screenY))
                        }
                    }
                    PatternType.NEON_PULSE -> {
                        drawCircle(color = Color(0xFF00E5FF).copy(alpha = 0.5f), radius = segR, center = Offset(screenX, screenY), style = Stroke(width = 2f * zoom))
                    }
                    else -> {}
                }
            }

            // 2. Draw Expressive Head
            val headScreenX = worm.head.x * zoom + cameraOffset.x
            val headScreenY = worm.head.y * zoom + cameraOffset.y
            val headPos = Offset(headScreenX, headScreenY)
            val headR = baseR * 1.12f

            // Head circle
            drawCircle(color = worm.skin.headColor, radius = headR, center = headPos)
            drawCircle(
                color = Color.White.copy(alpha = 0.35f),
                radius = headR * 0.55f,
                center = Offset(headScreenX - headR * 0.3f, headScreenY - headR * 0.3f)
            )

            // Rotate for facial features based on worm angle
            val angleDeg = Math.toDegrees(worm.angle.toDouble()).toFloat()
            rotate(degrees = angleDeg, pivot = headPos) {
                // Cheeks (blush)
                drawCircle(
                    color = Color(0xFFFF4081).copy(alpha = 0.45f),
                    radius = headR * 0.22f,
                    center = Offset(headScreenX + headR * 0.2f, headScreenY - headR * 0.75f)
                )
                drawCircle(
                    color = Color(0xFFFF4081).copy(alpha = 0.45f),
                    radius = headR * 0.22f,
                    center = Offset(headScreenX + headR * 0.2f, headScreenY + headR * 0.75f)
                )

                // Open mouth / tongue when eating or boosting
                if (worm.mouthOpen > 0.05f || worm.isBoosting) {
                    val mouthWidth = headR * 0.65f
                    val mouthHeight = headR * (0.35f + worm.mouthOpen * 0.3f)
                    drawArc(
                        color = Color(0xFFB71C1C),
                        startAngle = -80f,
                        sweepAngle = 160f,
                        useCenter = true,
                        topLeft = Offset(headScreenX + headR * 0.45f, headScreenY - mouthHeight * 0.5f),
                        size = Size(mouthWidth, mouthHeight)
                    )
                    // Cute pink tongue
                    drawCircle(
                        color = Color(0xFFFF80AB),
                        radius = mouthHeight * 0.35f,
                        center = Offset(headScreenX + headR * 0.75f, headScreenY)
                    )
                } else {
                    // Happy closed smile curve
                    drawArc(
                        color = Color(0xFF263238),
                        startAngle = -45f,
                        sweepAngle = 90f,
                        useCenter = false,
                        topLeft = Offset(headScreenX + headR * 0.4f, headScreenY - headR * 0.35f),
                        size = Size(headR * 0.5f, headR * 0.7f),
                        style = Stroke(width = 3.5f * zoom, cap = StrokeCap.Round)
                    )
                }

                // Eyes
                val eyeDistY = headR * 0.48f
                val eyeX = headScreenX + headR * 0.28f
                val eyeR = headR * 0.34f

                if (worm.isBlinking) {
                    // Closed eye slit
                    drawLine(
                        color = Color(0xFF212121),
                        start = Offset(eyeX - eyeR * 0.8f, headScreenY - eyeDistY),
                        end = Offset(eyeX + eyeR * 0.8f, headScreenY - eyeDistY),
                        strokeWidth = 3f * zoom
                    )
                    drawLine(
                        color = Color(0xFF212121),
                        start = Offset(eyeX - eyeR * 0.8f, headScreenY + eyeDistY),
                        end = Offset(eyeX + eyeR * 0.8f, headScreenY + eyeDistY),
                        strokeWidth = 3f * zoom
                    )
                } else {
                    // Big white sclera
                    drawCircle(color = Color.White, radius = eyeR, center = Offset(eyeX, headScreenY - eyeDistY))
                    drawCircle(color = Color.White, radius = eyeR, center = Offset(eyeX, headScreenY + eyeDistY))

                    // Pupil (looking forward)
                    val pupilR = eyeR * 0.55f
                    val pupilOffset = eyeR * 0.32f
                    drawCircle(
                        color = Color(0xFF1A1C24),
                        radius = pupilR,
                        center = Offset(eyeX + pupilOffset, headScreenY - eyeDistY)
                    )
                    drawCircle(
                        color = Color(0xFF1A1C24),
                        radius = pupilR,
                        center = Offset(eyeX + pupilOffset, headScreenY + eyeDistY)
                    )

                    // Cute specular gleam in pupil
                    drawCircle(
                        color = Color.White,
                        radius = pupilR * 0.35f,
                        center = Offset(eyeX + pupilOffset + pupilR * 0.25f, headScreenY - eyeDistY - pupilR * 0.25f)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = pupilR * 0.35f,
                        center = Offset(eyeX + pupilOffset + pupilR * 0.25f, headScreenY + eyeDistY - pupilR * 0.25f)
                    )
                }

                // Face Accessories
                drawAccessory(drawScope, worm.accessory, headScreenX, headScreenY, headR, zoom)
            }

            // 3. Draw Invulnerability Shield Bubble (if protected)
            if (isProtected) {
                val pulse = (sin(gameTime * 7f) * 0.12f + 0.88f)
                val shieldR = headR * 1.65f * pulse

                // Outer cyan glow
                drawCircle(
                    color = Color(0x3300E5FF),
                    radius = shieldR + 8f * zoom,
                    center = headPos
                )
                // Translucent bubble
                drawCircle(
                    color = Color(0x2800E5FF),
                    radius = shieldR,
                    center = headPos
                )
                // Glowing cyan border
                drawCircle(
                    color = Color(0xFF00E5FF),
                    radius = shieldR,
                    center = headPos,
                    style = Stroke(width = 3.5f * zoom)
                )
                // Inner golden energy ring
                drawCircle(
                    color = Color(0xFFFFD700),
                    radius = shieldR * 0.82f,
                    center = headPos,
                    style = Stroke(width = 2f * zoom)
                )
            }
        }
    }

    private fun DrawScope.drawAccessory(
        drawScope: DrawScope,
        acc: FaceAccessory,
        hx: Float,
        hy: Float,
        hr: Float,
        zoom: Float
    ) {
        when (acc) {
            FaceAccessory.ROYAL_CROWN -> {
                // Golden crown on head
                val crownPath = Path().apply {
                    val cx = hx - hr * 0.2f
                    val cy = hy
                    val cw = hr * 0.7f
                    val ch = hr * 0.9f
                    moveTo(cx - ch * 0.6f, cy - cw * 0.5f)
                    lineTo(cx, cy - cw * 0.6f)
                    lineTo(cx - ch * 0.2f, cy - cw * 0.2f)
                    lineTo(cx, cy)
                    lineTo(cx - ch * 0.2f, cy + cw * 0.2f)
                    lineTo(cx, cy + cw * 0.6f)
                    lineTo(cx - ch * 0.6f, cy + cw * 0.5f)
                    close()
                }
                drawPath(crownPath, color = Color(0xFFFFD700), style = Fill)
                drawPath(crownPath, color = Color(0xFFFFA000), style = Stroke(width = 2f * zoom))
                // Red jewel in crown
                drawCircle(color = Color(0xFFFF1744), radius = hr * 0.12f, center = Offset(hx - hr * 0.35f, hy))
            }
            FaceAccessory.SUNGLASSES -> {
                // Cool sunglasses across eyes
                val sx = hx + hr * 0.28f
                val sw = hr * 0.8f
                val sh = hr * 1.3f
                drawRoundRect(
                    color = Color(0xFF1E1E24),
                    topLeft = Offset(sx - hr * 0.15f, hy - sh * 0.5f),
                    size = Size(sw, sh),
                    cornerRadius = CornerRadius(8f * zoom, 8f * zoom)
                )
                // Bridge
                drawLine(
                    color = Color(0xFF424242),
                    start = Offset(sx, hy - hr * 0.2f),
                    end = Offset(sx, hy + hr * 0.2f),
                    strokeWidth = 3f * zoom
                )
                // Lens glare reflection
                drawLine(
                    color = Color.White.copy(alpha = 0.5f),
                    start = Offset(sx - hr * 0.05f, hy - hr * 0.45f),
                    end = Offset(sx + hr * 0.25f, hy - hr * 0.25f),
                    strokeWidth = 2.5f * zoom
                )
            }
            FaceAccessory.PIRATE_PATCH -> {
                // Black eye patch on right eye
                drawCircle(
                    color = Color(0xFF212121),
                    radius = hr * 0.4f,
                    center = Offset(hx + hr * 0.28f, hy + hr * 0.48f)
                )
                // Strap
                drawLine(
                    color = Color(0xFF212121),
                    start = Offset(hx - hr * 0.8f, hy - hr * 0.4f),
                    end = Offset(hx + hr * 0.8f, hy + hr * 0.8f),
                    strokeWidth = 2.5f * zoom
                )
            }
            FaceAccessory.ANIME_SPARKLE -> {
                // Cute star shine near cheek
                val stX = hx + hr * 0.6f
                val stY = hy - hr * 0.7f
                drawCircle(color = Color(0xFFFFEB3B), radius = hr * 0.15f, center = Offset(stX, stY))
                drawCircle(color = Color.White, radius = hr * 0.08f, center = Offset(stX, stY))
            }
            FaceAccessory.NONE -> {}
        }
    }

    private fun getSegmentColor(skin: SkinId, index: Int, total: Int): Color {
        return when (skin.patternType) {
            PatternType.STRIPES -> {
                if ((index / 2) % 2 == 0) skin.primaryColor else skin.secondaryColor
            }
            PatternType.RAINBOW_CYCLE -> {
                val rainbow = listOf(
                    Color(0xFFFF1744), Color(0xFFFF9100), Color(0xFFFFEA00),
                    Color(0xFF00E676), Color(0xFF00E5FF), Color(0xFF651FFF), Color(0xFFFF4081)
                )
                rainbow[(index / 2) % rainbow.size]
            }
            PatternType.WATERMELON_SEGMENTS -> {
                if (index % 4 == 0) skin.secondaryColor else skin.primaryColor
            }
            PatternType.NEON_PULSE -> {
                if ((index / 3) % 2 == 0) skin.primaryColor else skin.secondaryColor
            }
            PatternType.GOLDEN_SHINE -> {
                if ((index / 2) % 2 == 0) Color(0xFFFFD700) else Color(0xFFFFC107)
            }
        }
    }

    fun drawParticles(drawScope: DrawScope, particles: List<GameParticle>, cameraOffset: Offset, zoom: Float) {
        with(drawScope) {
            for (p in particles) {
                val sx = p.position.x * zoom + cameraOffset.x
                val sy = p.position.y * zoom + cameraOffset.y
                val r = p.currentSize * zoom
                drawCircle(
                    color = p.color.copy(alpha = p.alpha),
                    radius = r,
                    center = Offset(sx, sy)
                )
            }
        }
    }
}
