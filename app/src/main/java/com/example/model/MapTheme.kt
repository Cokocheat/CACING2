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
        title = "Cyber Funk",
        icon = "⚡",
        bgColor = Color(0xFF070B18),
        gridColor = Color(0xFF132A4A),
        borderColor = Color(0xFF00E5FF),
        glowColor = Color(0xFFE040FB),
        description = "Gaya futuristik Cyber Funk dengan grid laser neon & warna synthwave"
    ),
    GREEN_MEADOW(
        id = "green_meadow",
        title = "Green Meadow",
        icon = "🌿",
        bgColor = Color(0xFF122415),
        gridColor = Color(0xFF1F4225),
        borderColor = Color(0xFF4CAF50),
        glowColor = Color(0xFF81C784),
        description = "Padang rumput hijau segar nan asri dengan nuansa alam terbuka luas"
    ),
    SWEET_CANDY(
        id = "sweet_candy",
        title = "Sweet Candy",
        icon = "🍭",
        bgColor = Color(0xFF2E1331),
        gridColor = Color(0xFF55225A),
        borderColor = Color(0xFFFF4081),
        glowColor = Color(0xFFFF80AB),
        description = "Dunia permen manis gula-gula penuh warna ceria dan cemilan lezat"
    ),
    COSMIC_SPACE(
        id = "cosmic_space",
        title = "Cosmic Space",
        icon = "🌌",
        bgColor = Color(0xFF080C1D),
        gridColor = Color(0xFF19234A),
        borderColor = Color(0xFF7C4DFF),
        glowColor = Color(0xFFB388FF),
        description = "Galaksi antariksa luas dengan taburan bintang dan nebula bercahaya"
    ),
    LAVA_VOLCANO(
        id = "lava_volcano",
        title = "Lava Volcano",
        icon = "🌋",
        bgColor = Color(0xFF260D0D),
        gridColor = Color(0xFF4A1818),
        borderColor = Color(0xFFFF5722),
        glowColor = Color(0xFFFFAB40),
        description = "Kawah gunung berapi panas dengan retakan lahar magma membara"
    );

    companion object {
        fun fromId(id: String): MapTheme {
            return entries.find { it.id == id || it.id.startsWith(id) || id.startsWith(it.id) }
                ?: if (id == "cyber") CYBER_FUNK
                else if (id == "meadow") GREEN_MEADOW
                else if (id == "candy") SWEET_CANDY
                else if (id == "space") COSMIC_SPACE
                else if (id == "volcano") LAVA_VOLCANO
                else CYBER_FUNK
        }
    }
}
