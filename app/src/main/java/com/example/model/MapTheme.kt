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
    CYBER_FUNK(
        id = "cyber_funk",
        title = "Cyber Neon",
        icon = "⚡",
        bgColor = Color(0xFF0C162E),
        gridColor = Color(0xFF1E386B),
        borderColor = Color(0xFF00E5FF),
        glowColor = Color(0xFFE040FB),
        description = "Gaya futuristik Cyber Neon dengan warna cerah bermotif laser & synthwave"
    ),
    RAINBOW_CARNIVAL(
        id = "green_meadow",
        title = "Rainbow Carnival",
        icon = "🎪",
        bgColor = Color(0xFF161F38),
        gridColor = Color(0xFF2C4175),
        borderColor = Color(0xFFFFD700),
        glowColor = Color(0xFFFF4081),
        description = "Karnaval pelangi cerah ceria penuh motif warna-warni berenergi"
    ),
    SWEET_CANDY(
        id = "sweet_candy",
        title = "Sweet Candy",
        icon = "🍭",
        bgColor = Color(0xFF2B1333),
        gridColor = Color(0xFF5D2469),
        borderColor = Color(0xFFFF4081),
        glowColor = Color(0xFFFF80AB),
        description = "Dunia permen manis gula-gula penuh warna ceria dan motif lezat"
    ),
    COSMIC_SPACE(
        id = "cosmic_space",
        title = "Cosmic Space",
        icon = "🌌",
        bgColor = Color(0xFF0C1029),
        gridColor = Color(0xFF24336B),
        borderColor = Color(0xFF7C4DFF),
        glowColor = Color(0xFF00E5FF),
        description = "Galaksi antariksa luas dengan taburan bintang cerah dan nebula bercahaya"
    ),
    LAVA_VOLCANO(
        id = "lava_volcano",
        title = "Lava Volcano",
        icon = "🌋",
        bgColor = Color(0xFF290E0E),
        gridColor = Color(0xFF571C1C),
        borderColor = Color(0xFFFF5722),
        glowColor = Color(0xFFFFAB40),
        description = "Kawah api menyala dengan motif retakan lahar magma terang membara"
    );

    companion object {
        fun fromId(id: String): MapTheme {
            if (id == "green_meadow" || id == "meadow") return CYBER_FUNK
            return entries.find { it.id == id || it.id.startsWith(id) || id.startsWith(it.id) }
                ?: CYBER_FUNK
        }
    }
}
