package com.example.model

import androidx.compose.ui.graphics.Color

data class GameParticle(
    var position: Vec2,
    var velocity: Vec2,
    val color: Color,
    val initialSize: Float,
    var currentSize: Float = initialSize,
    var alpha: Float = 1f,
    val maxLifetime: Float,
    var currentAge: Float = 0f
) {
    val isDead: Boolean get() = currentAge >= maxLifetime

    fun update(dt: Float) {
        currentAge += dt
        position = position + velocity * dt
        val progress = (currentAge / maxLifetime).coerceIn(0f, 1f)
        currentSize = initialSize * (1f - progress * 0.7f)
        alpha = (1f - progress).coerceIn(0f, 1f)
    }
}
