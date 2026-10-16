package com.example.model

import androidx.compose.ui.graphics.Color

enum class PotionType(
    val title: String,
    val description: String,
    val defaultDuration: Float,
    val color: Color
) {
    SPEED_BOOST(
        title = "Kilat Cepat",
        description = "Lari sangat kencang tanpa menghabiskan badan",
        defaultDuration = 12f,
        color = Color(0xFF00E676)
    ),
    MULTIPLIER(
        title = "Poin Ganda (2x)",
        description = "Nilai makanan menjadi dua kali lipat",
        defaultDuration = 15f,
        color = Color(0xFFFFD600)
    ),
    MAGNET(
        title = "Magnet Makanan",
        description = "Menarik seluruh makanan lezat di sekitar",
        defaultDuration = 14f,
        color = Color(0xFF00E5FF)
    ),
    RADAR(
        title = "Radar Pemburu",
        description = "Mendeteksi posisi santapan cacing besar di peta",
        defaultDuration = 18f,
        color = Color(0xFFA855F7)
    ),
    ZOOM(
        title = "Mata Elang (Zoom)",
        description = "Sudut pandang lebih luas untuk memantau lawan",
        defaultDuration = 20f,
        color = Color(0xFFFF6D00)
    )
}

data class PotionItem(
    val id: Long,
    val position: Vec2,
    val type: PotionType,
    val radius: Float = 36f,
    var pulsePhase: Float = 0f
)
