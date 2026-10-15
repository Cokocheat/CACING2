package com.example.model

enum class GameMode {
    OPEN_ARENA,
    TIME_ATTACK,
    BATTLE_ROYALE
}

enum class GameDifficulty(
    val id: String,
    val titleResKey: String,
    val badge: String,
    val decisionCooldownMin: Float,
    val decisionCooldownMax: Float,
    val lookAheadDist: Float,
    val turnSpeedFactor: Float,
    val boostAggressionFactor: Float,
    val huntLeadingDist: Float
) {
    EASY(
        id = "easy",
        titleResKey = "diff_easy",
        badge = "🟢",
        decisionCooldownMin = 0.50f,
        decisionCooldownMax = 0.85f,
        lookAheadDist = 80f,
        turnSpeedFactor = 0.80f,
        boostAggressionFactor = 0.35f,
        huntLeadingDist = 40f
    ),
    NORMAL(
        id = "normal",
        titleResKey = "diff_normal",
        badge = "🟡",
        decisionCooldownMin = 0.22f,
        decisionCooldownMax = 0.42f,
        lookAheadDist = 130f,
        turnSpeedFactor = 1.0f,
        boostAggressionFactor = 0.85f,
        huntLeadingDist = 80f
    ),
    HARD(
        id = "hard",
        titleResKey = "diff_hard",
        badge = "🔴",
        decisionCooldownMin = 0.08f,
        decisionCooldownMax = 0.16f,
        lookAheadDist = 190f,
        turnSpeedFactor = 1.35f,
        boostAggressionFactor = 1.45f,
        huntLeadingDist = 140f
    );

    companion object {
        fun fromId(id: String): GameDifficulty =
            entries.find { it.id.equals(id, ignoreCase = true) } ?: NORMAL
    }
}

object GameConfig {
    const val ARENA_RADIUS = 3800f
    const val ARENA_CENTER_X = 3800f
    const val ARENA_CENTER_Y = 3800f
    const val ARENA_WIDTH = 7600f
    const val ARENA_HEIGHT = 7600f

    const val INITIAL_MASS = 25f
    const val BASE_SPEED = 230f
    const val BOOST_SPEED = 420f
    const val TURN_SPEED = 5.2f // radians per second

    const val BASE_SEGMENT_RADIUS = 16f
    const val SEGMENT_SPACING = 14f
    const val INITIAL_SEGMENTS = 14

    const val FOOD_COUNT_TARGET = 360
    const val POTION_COUNT_TARGET = 16
    const val BOT_COUNT_TARGET = 14

    const val MAGNET_BASE_RADIUS = 240f
    const val MAGNET_UPGRADED_MAX = 420f
}
