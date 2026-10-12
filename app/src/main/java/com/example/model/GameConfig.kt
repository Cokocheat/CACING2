package com.example.model

enum class GameMode(val title: String, val subtitle: String, val icon: String) {
    OPEN_ARENA("Arena Luar", "Mode klasik santai tanpa batas waktu", "🌍"),
    TIME_ATTACK("Time Attack", "3 Menit cetak skor & kill tertinggi!", "⏱️"),
    BATTLE_ROYALE("Battle Royale", "Zona menyusut, jadilah yang terakhir bertahan!", "👑")
}

object GameConfig {
    const val ARENA_RADIUS = 2200f
    const val ARENA_CENTER_X = 2200f
    const val ARENA_CENTER_Y = 2200f
    const val ARENA_WIDTH = 4400f
    const val ARENA_HEIGHT = 4400f

    const val INITIAL_MASS = 25f
    const val BASE_SPEED = 180f
    const val BOOST_SPEED = 340f
    const val TURN_SPEED = 4.8f // radians per second

    const val BASE_SEGMENT_RADIUS = 16f
    const val SEGMENT_SPACING = 14f
    const val INITIAL_SEGMENTS = 14

    const val FOOD_COUNT_TARGET = 180
    const val POTION_COUNT_TARGET = 10
    const val BOT_COUNT_TARGET = 10

    const val MAGNET_BASE_RADIUS = 240f
    const val MAGNET_UPGRADED_MAX = 420f
}
