package com.example.model

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

enum class BotPersonality {
    WANDERER,   // Wanders around eating standard food
    SCAVENGER,  // Actively seeks dead worm food feast clusters
    HUNTER,     // Tries to circle around or cut off other worms
    GREEDY      // Hunts for potions and large cakes
}

data class Worm(
    val id: String,
    var name: String,
    val isPlayer: Boolean,
    var head: Vec2,
    var angle: Float = 0f,
    var targetAngle: Float = 0f,
    var speed: Float = GameConfig.BASE_SPEED,
    var isBoosting: Boolean = false,
    val bodyPoints: MutableList<Vec2> = mutableListOf(),
    var mass: Float = GameConfig.INITIAL_MASS,
    var kills: Int = 0,
    var isAlive: Boolean = true,
    var skin: SkinId = SkinId.CLASSIC_GREEN,
    var accessory: FaceAccessory = FaceAccessory.NONE,
    val activePotions: MutableMap<PotionType, Float> = mutableMapOf(),

    // AI controls
    var aiPersonality: BotPersonality = BotPersonality.WANDERER,
    var aiTarget: Vec2 = Vec2.Zero,
    var aiDecisionCooldown: Float = 0f,
    var boostCooldown: Float = 0f,

    // Facial expressions
    var mouthOpen: Float = 0f,
    var blinkTimer: Float = 2f,
    var isBlinking: Boolean = false,
    var boostParticleTimer: Float = 0f
) {
    val segmentRadius: Float
        get() {
            // Scales dynamically with mass/points without an artificial early cap
            val extraMass = (mass - 10f).coerceAtLeast(0f)
            return (13.5f + extraMass.toDouble().pow(0.40).toFloat() * 1.05f).coerceIn(13.5f, 125f)
        }

    val targetLength: Int
        get() {
            // Scales length dynamically with mass/points
            val extraMass = (mass - 10f).coerceAtLeast(0f)
            return (15 + (extraMass.toDouble().pow(0.50) * 2.4).toInt()).coerceIn(15, 650)
        }

    fun hasPotion(type: PotionType): Boolean = (activePotions[type] ?: 0f) > 0f

    fun addPotion(type: PotionType, extraDuration: Float) {
        val current = activePotions[type] ?: 0f
        activePotions[type] = current + extraDuration
    }

    fun updatePotions(dt: Float) {
        val iterator = activePotions.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val remaining = entry.value - dt
            if (remaining <= 0f) {
                iterator.remove()
            } else {
                entry.setValue(remaining)
            }
        }
    }

    /**
     * Steer smoothly towards target angle with turning rate limit
     */
    fun steerTowards(target: Float, maxTurnRad: Float) {
        var diff = target - angle
        while (diff < -PI) diff += (2 * PI).toFloat()
        while (diff > PI) diff -= (2 * PI).toFloat()

        val turnAmount = diff.coerceIn(-maxTurnRad, maxTurnRad)
        angle += turnAmount
    }

    fun initializeBody() {
        bodyPoints.clear()
        val dir = Vec2.fromAngle(angle + PI.toFloat(), GameConfig.SEGMENT_SPACING)
        var current = head
        for (i in 0 until GameConfig.INITIAL_SEGMENTS) {
            current = current + dir
            bodyPoints.add(current)
        }
    }
}
