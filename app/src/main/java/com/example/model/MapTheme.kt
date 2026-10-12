package com.example.model

import androidx.compose.ui.graphics.Color

enum class MapTheme(
    val id: String,
    val title: String,
    val icon: String,
    val bgColor: Color,
    val gridColor: Color,
    val borderColor: Color,
    val glowColor: Color,
    val description: String
) {
    GREEN_MEADOW(
        id = "meadow",
        title = "Padang Rumput",
        icon = "🌿",
        bgColor = Color(0xFF142416),
        gridColor = Color(0xFF223E25),
        borderColor = Color(0xFF4CAF50),
        glowColor = Color(0xFF81C784),
        description = "Padang rumput segar berembun dengan sentuhan alam luas"
    ),
    COSMIC_SPACE(
        id = "space",
        title = "Galaksi Kosmik",
        icon = "🌌",
        bgColor = Color(0xFF0D1122),
        gridColor = Color(0xFF1B2344),
        borderColor = Color(0xFF7C4DFF),
        glowColor = Color(0xFFB388FF),
        description = "Gugusan bintang dan nebula luar angkasa penuh misteri"
    ),
    SWEET_CANDY(
        id = "candy",
        title = "Negeri Permen",
        icon = "🍭",
        bgColor = Color(0xFF29142A),
        gridColor = Color(0xFF48234B),
        borderColor = Color(0xFFFF4081),
        glowColor = Color(0xFFFF80AB),
        description = "Dunia gula manis warna-warni penuh cemilan lezat"
    ),
    CYBER_NEON(
        id = "cyber",
        title = "Cyberpunk Neon",
        icon = "⚡",
        bgColor = Color(0xFF0F1726),
        gridColor = Color(0xFF162D4A),
        borderColor = Color(0xFF00E5FF),
        glowColor = Color(0xFF18FFFF),
        description = "Grid berteknologi tinggi dengan garis laser neon futuristik"
    ),
    LAVA_VOLCANO(
        id = "volcano",
        title = "Kawah Magma Lava",
        icon = "🌋",
        bgColor = Color(0xFF241010),
        gridColor = Color(0xFF421C1C),
        borderColor = Color(0xFFFF5722),
        glowColor = Color(0xFFFF8A65),
        description = "Kerak bumi berapi dengan lava pijar menyala hangat"
    );

    companion object {
        fun fromId(id: String): MapTheme = entries.find { it.id == id } ?: GREEN_MEADOW
    }
}
