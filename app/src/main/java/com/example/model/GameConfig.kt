package com.example.model

enum class GameMode {
    OPEN_ARENA,
    TIME_ATTACK,
    BATTLE_ROYALE
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
