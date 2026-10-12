package com.example.model

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class Vec2(val x: Float = 0f, val y: Float = 0f) {
    operator fun plus(other: Vec2): Vec2 = Vec2(x + other.x, y + other.y)
    operator fun minus(other: Vec2): Vec2 = Vec2(x - other.x, y - other.y)
    operator fun times(scalar: Float): Vec2 = Vec2(x * scalar, y * scalar)
    operator fun div(scalar: Float): Vec2 = if (scalar != 0f) Vec2(x / scalar, y / scalar) else Vec2(0f, 0f)

    fun length(): Float = sqrt(x * x + y * y)
    fun lengthSquared(): Float = x * x + y * y

    fun distanceTo(other: Vec2): Float {
        val dx = x - other.x
        val dy = y - other.y
        return sqrt(dx * dx + dy * dy)
    }

    fun distanceSquaredTo(other: Vec2): Float {
        val dx = x - other.x
        val dy = y - other.y
        return dx * dx + dy * dy
    }

    fun normalized(): Vec2 {
        val l = length()
        return if (l > 0.0001f) Vec2(x / l, y / l) else Vec2(1f, 0f)
    }

    fun angle(): Float = atan2(y, x)

    companion object {
        val Zero = Vec2(0f, 0f)
        fun fromAngle(angleRad: Float, length: Float = 1f): Vec2 {
            return Vec2(cos(angleRad) * length, sin(angleRad) * length)
        }
        fun lerp(start: Vec2, stop: Vec2, fraction: Float): Vec2 {
            return Vec2(
                start.x + (stop.x - start.x) * fraction,
                start.y + (stop.y - start.y) * fraction
            )
        }
    }
}
