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
import com.example.model.MapTheme
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
        arenaRadius: Float,
        mapTheme: MapTheme = MapTheme.GREEN_MEADOW
    ) {
        with(drawScope) {
            // Draw background fill
            drawRect(color = mapTheme.bgColor)

            // Draw grid lines
            val gridSize = 100f * zoom
            val startX = (cameraOffset.x % gridSize)
            val startY = (cameraOffset.y % gridSize)

            val gridColor = mapTheme.gridColor
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
                color = mapTheme.glowColor.copy(alpha = 0.25f),
                radius = radiusOnScreen + 18f * zoom,
                center = centerOnScreen,
                style = Stroke(width = 28f * zoom)
            )

            // Outer danger border line
            drawCircle(
                color = mapTheme.borderColor,
                radius = radiusOnScreen,
                center = centerOnScreen,
                style = Stroke(width = 7f * zoom)
            )
        }
    }

    fun drawFood(
        drawScope: DrawScope,
        food: FoodItem,
        screenPos: Offset,
        zoom: Float,
        mapTheme: MapTheme = MapTheme.CYBER_FUNK
    ) {
        with(drawScope) {
            val r = food.radius * zoom
            val x = screenPos.x
            val y = screenPos.y

            // Theme-specific ambient food aura
            when (mapTheme) {
                MapTheme.CYBER_FUNK -> {
                    drawCircle(
                        color = Color(0x3300E5FF),
                        radius = r * 1.35f,
                        center = screenPos
                    )
                    drawCircle(
                        color = Color(0x6600E5FF),
                        radius = r * 1.2f,
                        center = screenPos,
                        style = Stroke(width = 1.2f * zoom)
                    )
                }
                MapTheme.COSMIC_SPACE -> {
                    drawCircle(
                        color = Color(0x337C4DFF),
                        radius = r * 1.35f,
                        center = screenPos
                    )
                    drawCircle(
                        color = Color(0xAAFFFFFF),
                        radius = 1.4f * zoom,
                        center = Offset(x - r * 0.9f, y - r * 0.8f)
                    )
                }
                MapTheme.SWEET_CANDY -> {
                    drawCircle(
                        color = Color(0x33FF4081),
                        radius = r * 1.3f,
                        center = screenPos
                    )
                }
                MapTheme.LAVA_VOLCANO -> {
                    drawCircle(
                        color = Color(0x44FF5722),
                        radius = r * 1.35f,
                        center = screenPos
                    )
                    drawCircle(
                        color = Color(0xFFFF9100),
                        radius = 1.4f * zoom,
                        center = Offset(x + r * 0.9f, y - r * 0.8f)
                    )
                }
                MapTheme.GREEN_MEADOW -> {
                    drawCircle(
                        color = Color(0x224CAF50),
                        radius = r * 1.25f,
                        center = screenPos
                    )
                }
            }

            // Ambient ground contact shadow for all foods
            drawOval(
                color = Color(0x38000000),
                topLeft = Offset(x - r * 0.95f, y + r * 0.45f),
                size = Size(r * 1.9f, r * 0.55f)
            )

            when (food.type) {
                FoodType.BURGER -> {
                    // 1. Bottom toasted golden bun
                    drawRoundRect(
                        color = Color(0xFFC78338),
                        topLeft = Offset(x - r * 1.05f, y + r * 0.25f),
                        size = Size(r * 2.1f, r * 0.65f),
                        cornerRadius = CornerRadius(r * 0.35f, r * 0.35f)
                    )
                    // 2. Juicy grilled dark beef patty
                    drawRoundRect(
                        color = Color(0xFF3E2319),
                        topLeft = Offset(x - r * 1.0f, y - r * 0.02f),
                        size = Size(r * 2.0f, r * 0.45f),
                        cornerRadius = CornerRadius(r * 0.2f, r * 0.2f)
                    )
                    // Grill line accents
                    drawLine(
                        color = Color(0xFF26140D),
                        start = Offset(x - r * 0.6f, y + r * 0.05f),
                        end = Offset(x + r * 0.6f, y + r * 0.25f),
                        strokeWidth = 1.5f * zoom
                    )
                    // 3. Melted cheddar cheese triangle corner
                    val cheesePath = Path().apply {
                        moveTo(x - r * 0.9f, y + r * 0.05f)
                        lineTo(x + r * 0.9f, y + r * 0.05f)
                        lineTo(x + r * 0.2f, y + r * 0.45f)
                        lineTo(x - r * 0.3f, y + r * 0.15f)
                        close()
                    }
                    drawPath(cheesePath, color = Color(0xFFFFC107), style = Fill)
                    // 4. Fresh ruffled green lettuce
                    drawRoundRect(
                        color = Color(0xFF00E676),
                        topLeft = Offset(x - r * 1.1f, y - r * 0.32f),
                        size = Size(r * 2.2f, r * 0.36f),
                        cornerRadius = CornerRadius(r * 0.25f, r * 0.25f)
                    )
                    // Lettuce leaf ripples
                    drawCircle(color = Color(0xFF69F0AE), radius = r * 0.22f, center = Offset(x - r * 0.8f, y - r * 0.2f))
                    drawCircle(color = Color(0xFF69F0AE), radius = r * 0.24f, center = Offset(x + r * 0.8f, y - r * 0.2f))
                    // 5. Ripe sliced red tomato
                    drawRoundRect(
                        color = Color(0xFFFF1744),
                        topLeft = Offset(x - r * 0.85f, y - r * 0.45f),
                        size = Size(r * 1.7f, r * 0.22f),
                        cornerRadius = CornerRadius(r * 0.1f, r * 0.1f)
                    )
                    // 6. Top plump golden bun
                    drawRoundRect(
                        color = Color(0xFFFFA726),
                        topLeft = Offset(x - r * 1.05f, y - r * 1.05f),
                        size = Size(r * 2.1f, r * 0.95f),
                        cornerRadius = CornerRadius(r * 0.7f, r * 0.7f)
                    )
                    // Bun top gloss shine
                    drawOval(
                        color = Color(0x66FFFFFF),
                        topLeft = Offset(x - r * 0.7f, y - r * 0.95f),
                        size = Size(r * 1.4f, r * 0.35f)
                    )
                    // Crispy ivory sesame seeds
                    drawOval(color = Color(0xFFFFF9C4), topLeft = Offset(x - r * 0.45f, y - r * 0.65f), size = Size(r * 0.2f, r * 0.1f))
                    drawOval(color = Color(0xFFFFF9C4), topLeft = Offset(x + r * 0.3f, y - r * 0.68f), size = Size(r * 0.2f, r * 0.1f))
                    drawOval(color = Color(0xFFFFF9C4), topLeft = Offset(x - r * 0.1f, y - r * 0.8f), size = Size(r * 0.2f, r * 0.1f))
                    drawOval(color = Color(0xFFFFF9C4), topLeft = Offset(x + r * 0.55f, y - r * 0.45f), size = Size(r * 0.18f, r * 0.09f))
                }

                FoodType.DONUT -> {
                    // 1. Golden baked dough base
                    drawCircle(color = Color(0xFFE0A96D), radius = r, center = screenPos)
                    drawCircle(color = Color(0xFFC68B4E), radius = r * 0.95f, center = screenPos, style = Stroke(width = 2.5f * zoom))
                    // 2. Thick glossy strawberry frosting with drips
                    drawCircle(color = Color(0xFFFF4081), radius = r * 0.88f, center = screenPos)
                    // Icing drip droplets
                    drawCircle(color = Color(0xFFFF4081), radius = r * 0.24f, center = Offset(x - r * 0.6f, y + r * 0.4f))
                    drawCircle(color = Color(0xFFFF4081), radius = r * 0.26f, center = Offset(x + r * 0.55f, y + r * 0.45f))
                    drawCircle(color = Color(0xFFFF4081), radius = r * 0.22f, center = Offset(x + r * 0.1f, y + r * 0.7f))
                    // 3. Donut center hole
                    drawCircle(color = Color(0xFF101424), radius = r * 0.36f, center = screenPos)
                    drawCircle(color = Color(0xFFB07234), radius = r * 0.42f, center = screenPos, style = Stroke(width = 1.5f * zoom))
                    // 4. White glossy crescent highlight
                    drawArc(
                        color = Color(0x99FFFFFF),
                        startAngle = 190f,
                        sweepAngle = 100f,
                        useCenter = false,
                        topLeft = Offset(x - r * 0.75f, y - r * 0.75f),
                        size = Size(r * 1.5f, r * 1.5f),
                        style = Stroke(width = 3.5f * zoom, cap = StrokeCap.Round)
                    )
                    // 5. Colorful crisp sprinkles
                    drawRoundRect(color = Color(0xFFFFEB3B), topLeft = Offset(x - r * 0.55f, y - r * 0.3f), size = Size(r * 0.3f, r * 0.12f), cornerRadius = CornerRadius(2f, 2f))
                    drawRoundRect(color = Color(0xFF00E5FF), topLeft = Offset(x + r * 0.35f, y - r * 0.45f), size = Size(r * 0.12f, r * 0.3f), cornerRadius = CornerRadius(2f, 2f))
                    drawRoundRect(color = Color(0xFF76FF03), topLeft = Offset(x + r * 0.4f, y + r * 0.25f), size = Size(r * 0.28f, r * 0.12f), cornerRadius = CornerRadius(2f, 2f))
                    drawRoundRect(color = Color(0xFFFFFFFF), topLeft = Offset(x - r * 0.4f, y + r * 0.35f), size = Size(r * 0.12f, r * 0.28f), cornerRadius = CornerRadius(2f, 2f))
                    drawRoundRect(color = Color(0xFFFF9100), topLeft = Offset(x - r * 0.1f, y - r * 0.65f), size = Size(r * 0.28f, r * 0.11f), cornerRadius = CornerRadius(2f, 2f))
                }

                FoodType.WATERMELON_SLICE -> {
                    // 1. Dark green outer rind
                    drawArc(
                        color = Color(0xFF1B5E20),
                        startAngle = 0f,
                        sweepAngle = 180f,
                        useCenter = true,
                        topLeft = Offset(x - r, y - r),
                        size = Size(r * 2f, r * 2f)
                    )
                    // 2. Light lime inner rind rim
                    drawArc(
                        color = Color(0xFF81C784),
                        startAngle = 0f,
                        sweepAngle = 180f,
                        useCenter = true,
                        topLeft = Offset(x - r * 0.9f, y - r * 0.9f),
                        size = Size(r * 1.8f, r * 1.8f)
                    )
                    // 3. Vibrant sweet red flesh
                    drawArc(
                        color = Color(0xFFFF1744),
                        startAngle = 0f,
                        sweepAngle = 180f,
                        useCenter = true,
                        topLeft = Offset(x - r * 0.78f, y - r * 0.78f),
                        size = Size(r * 1.56f, r * 1.56f)
                    )
                    // White gloss shine arc
                    drawArc(
                        color = Color(0x66FFFFFF),
                        startAngle = 20f,
                        sweepAngle = 60f,
                        useCenter = false,
                        topLeft = Offset(x - r * 0.65f, y - r * 0.65f),
                        size = Size(r * 1.3f, r * 1.3f),
                        style = Stroke(width = 2.5f * zoom, cap = StrokeCap.Round)
                    )
                    // 4. Black teardrop seeds with white glint
                    fun drawSeed(sx: Float, sy: Float) {
                        drawOval(color = Color(0xFF1A1A1A), topLeft = Offset(sx - r * 0.08f, sy - r * 0.14f), size = Size(r * 0.16f, r * 0.26f))
                        drawCircle(color = Color.White, radius = r * 0.04f, center = Offset(sx - r * 0.02f, sy - r * 0.06f))
                    }
                    drawSeed(x - r * 0.42f, y + r * 0.28f)
                    drawSeed(x, y + r * 0.48f)
                    drawSeed(x + r * 0.42f, y + r * 0.28f)
                    drawSeed(x - r * 0.2f, y + r * 0.38f)
                    drawSeed(x + r * 0.2f, y + r * 0.38f)
                }

                FoodType.CHEESE -> {
                    // 3D Isometric Golden Cheese Wedge
                    val sidePath = Path().apply {
                        moveTo(x - r * 0.85f, y + r * 0.8f)
                        lineTo(x + r * 0.85f, y + r * 0.8f)
                        lineTo(x + r * 0.95f, y + r * 0.5f)
                        lineTo(x - r * 0.75f, y + r * 0.5f)
                        close()
                    }
                    drawPath(sidePath, color = Color(0xFFE69A00), style = Fill)

                    val topPath = Path().apply {
                        moveTo(x - r * 0.85f, y + r * 0.5f)
                        lineTo(x + r * 0.85f, y + r * 0.5f)
                        lineTo(x, y - r * 0.9f)
                        close()
                    }
                    drawPath(topPath, color = Color(0xFFFFD54F), style = Fill)

                    // Swiss cheese holes with inner shadow
                    fun drawHole(hx: Float, hy: Float, hr: Float) {
                        drawCircle(color = Color(0xFFC77C00), radius = hr, center = Offset(hx, hy))
                        drawCircle(color = Color(0xFFFFE082), radius = hr * 0.85f, center = Offset(hx, hy + hr * 0.2f))
                    }
                    drawHole(x - r * 0.25f, y + r * 0.15f, r * 0.2f)
                    drawHole(x + r * 0.32f, y + r * 0.05f, r * 0.15f)
                    drawHole(x, y + r * 0.32f, r * 0.16f)
                }

                FoodType.PIZZA -> {
                    // Crust arc
                    val crustPath = Path().apply {
                        moveTo(x - r * 0.85f, y - r * 0.8f)
                        lineTo(x + r * 0.85f, y - r * 0.8f)
                        lineTo(x + r * 0.95f, y - r * 0.55f)
                        lineTo(x - r * 0.95f, y - r * 0.55f)
                        close()
                    }
                    drawPath(crustPath, color = Color(0xFFC6843D), style = Fill)

                    // Cheese slice body
                    val cheeseSlice = Path().apply {
                        moveTo(x - r * 0.85f, y - r * 0.6f)
                        lineTo(x + r * 0.85f, y - r * 0.6f)
                        lineTo(x, y + r * 0.9f)
                        close()
                    }
                    drawPath(cheeseSlice, color = Color(0xFFFFD54F), style = Fill)

                    // Pepperoni slices
                    fun drawPepperoni(px: Float, py: Float) {
                        drawCircle(color = Color(0xFFC62828), radius = r * 0.22f, center = Offset(px, py))
                        drawCircle(color = Color(0xFF8E0000), radius = r * 0.06f, center = Offset(px - r * 0.05f, py - r * 0.05f))
                    }
                    drawPepperoni(x - r * 0.3f, y - r * 0.2f)
                    drawPepperoni(x + r * 0.25f, y - r * 0.15f)
                    drawPepperoni(x, y + r * 0.28f)
                    // Green herb speckles
                    drawCircle(color = Color(0xFF2E7D32), radius = r * 0.04f, center = Offset(x - r * 0.1f, y - r * 0.05f))
                    drawCircle(color = Color(0xFF2E7D32), radius = r * 0.04f, center = Offset(x + r * 0.1f, y + r * 0.08f))
                }

                FoodType.CUPCAKE -> {
                    // Ribbed baking cup
                    val cupPath = Path().apply {
                        moveTo(x - r * 0.7f, y)
                        lineTo(x + r * 0.7f, y)
                        lineTo(x + r * 0.5f, y + r * 0.8f)
                        lineTo(x - r * 0.5f, y + r * 0.8f)
                        close()
                    }
                    drawPath(cupPath, color = Color(0xFF90CAF9), style = Fill)
                    // Pleats
                    drawLine(color = Color(0xFF42A5F5), start = Offset(x - r * 0.2f, y), end = Offset(x - r * 0.15f, y + r * 0.8f), strokeWidth = 1.5f)
                    drawLine(color = Color(0xFF42A5F5), start = Offset(x + r * 0.2f, y), end = Offset(x + r * 0.15f, y + r * 0.8f), strokeWidth = 1.5f)

                    // Fluffy swirled frosting
                    drawCircle(color = Color(0xFFFF80AB), radius = r * 0.55f, center = Offset(x, y - r * 0.15f))
                    drawCircle(color = Color(0xFFF48FB1), radius = r * 0.42f, center = Offset(x, y - r * 0.45f))
                    drawCircle(color = Color(0xFFFF4081), radius = r * 0.28f, center = Offset(x, y - r * 0.7f))

                    // Glossy red cherry with stem
                    drawCircle(color = Color(0xFFFF1744), radius = r * 0.2f, center = Offset(x, y - r * 0.95f))
                    drawCircle(color = Color.White, radius = r * 0.06f, center = Offset(x - r * 0.06f, y - r * 1.0f))
                    drawLine(color = Color(0xFF5D4037), start = Offset(x, y - r * 1.05f), end = Offset(x + r * 0.2f, y - r * 1.35f), strokeWidth = 2f)
                }

                FoodType.APPLE -> {
                    // Red glossy apple
                    drawCircle(color = Color(0xFFFF1744), radius = r * 0.95f, center = Offset(x, y + r * 0.05f))
                    // Specular radial reflection
                    drawCircle(color = Color(0x66FFFFFF), radius = r * 0.35f, center = Offset(x - r * 0.35f, y - r * 0.25f))
                    // Woody stem
                    drawLine(color = Color(0xFF4E342E), start = Offset(x, y - r * 0.85f), end = Offset(x + r * 0.1f, y - r * 1.25f), strokeWidth = 2.5f * zoom)
                    // Crisp green leaf
                    drawOval(color = Color(0xFF4CAF50), topLeft = Offset(x + r * 0.05f, y - r * 1.35f), size = Size(r * 0.6f, r * 0.35f))
                }

                FoodType.STRAWBERRY -> {
                    // Red berry body
                    val berryPath = Path().apply {
                        moveTo(x - r * 0.7f, y - r * 0.4f)
                        lineTo(x + r * 0.7f, y - r * 0.4f)
                        lineTo(x, y + r * 0.95f)
                        close()
                    }
                    drawPath(berryPath, color = Color(0xFFFF1744), style = Fill)
                    drawCircle(color = Color(0xFFFF1744), radius = r * 0.7f, center = Offset(x, y - r * 0.3f))
                    // Yellow seeds
                    drawCircle(color = Color(0xFFFFF59D), radius = r * 0.06f, center = Offset(x - r * 0.25f, y - r * 0.1f))
                    drawCircle(color = Color(0xFFFFF59D), radius = r * 0.06f, center = Offset(x + r * 0.25f, y - r * 0.1f))
                    drawCircle(color = Color(0xFFFFF59D), radius = r * 0.06f, center = Offset(x, y + r * 0.3f))
                    // Green calyx leaves
                    drawCircle(color = Color(0xFF2E7D32), radius = r * 0.25f, center = Offset(x - r * 0.35f, y - r * 0.75f))
                    drawCircle(color = Color(0xFF2E7D32), radius = r * 0.25f, center = Offset(x + r * 0.35f, y - r * 0.75f))
                    drawCircle(color = Color(0xFF4CAF50), radius = r * 0.25f, center = Offset(x, y - r * 0.8f))
                }

                FoodType.GOLDEN_COIN -> {
                    // 3D Golden Coin
                    drawCircle(color = Color(0xFFFFA000), radius = r, center = screenPos)
                    drawCircle(color = Color(0xFFFFD700), radius = r * 0.85f, center = screenPos)
                    drawCircle(color = Color(0xFFFFE082), radius = r * 0.6f, center = screenPos)
                    // Embossed star
                    drawCircle(color = Color.White, radius = r * 0.2f, center = screenPos)
                }

                FoodType.BOOST_DROP -> {
                    // Pulsing electric energetic spark
                    drawCircle(color = Color(0x55FFEB3B), radius = r * 1.5f, center = screenPos)
                    drawCircle(color = Color(0xFFFFEB3B), radius = r, center = screenPos)
                    drawCircle(color = Color.White, radius = r * 0.45f, center = screenPos)
                }

                FoodType.DEATH_FEAST -> {
                    // Glowing Banquet Deluxe
                    drawCircle(color = Color(0x44FF1744), radius = r * 1.6f, center = screenPos)
                    drawCircle(color = Color(0xFFFF5252), radius = r * 1.1f, center = screenPos)
                    drawCircle(color = Color(0xFFFFD54F), radius = r * 0.65f, center = screenPos)
                    drawCircle(color = Color.White, radius = r * 0.3f, center = screenPos)
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
