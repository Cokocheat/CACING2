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
        title = "Kuning Cerah (Sunny Arena)",
        icon = "☀️",
        bgColor = Color(0xFFFDE047), // Bright sunny golden yellow arena (like icon & splash)
        gridColor = Color(0x35B45309), // Warm amber motif grid
        borderColor = Color(0xFFE11D48), // Vibrant crimson danger boundary
        glowColor = Color(0xFFF97316), // Warm golden sunshine glow
        description = "Arena kuning cerah ceria penuh motif geometris berenergi tinggi"
    ),
    RAINBOW_CARNIVAL(
        id = "rainbow_carnival",
        title = "Langit Pelangi (Sky Carnival)",
        icon = "🎪",
        bgColor = Color(0xFFBAE6FD), // Bright vibrant sky cyan
        gridColor = Color(0x350369A1), // Deep sky cyan grid
        borderColor = Color(0xFF8B5CF6), // Neon purple boundary
        glowColor = Color(0xFF06B6D4), // Cyan energetic glow
        description = "Karnaval langit pelangi cerah penuh warna ceria dan motif pesta"
    ),
    SWEET_CANDY(
        id = "sweet_candy",
        title = "Permen Manis (Sweet Candy)",
        icon = "🍭",
        bgColor = Color(0xFFFCE7F3), // Bright sweet strawberry cream pink
        gridColor = Color(0x35BE185D), // Berry grid lines
        borderColor = Color(0xFFEC4899), // Deep pink danger border
        glowColor = Color(0xFFA855F7), // Sweet sugar glow
        description = "Dunia permen manis gula-gula cerah penuh warna ceria"
    ),
    COSMIC_SPACE(
        id = "green_meadow",
        title = "Padang Rumput (Green Meadow)",
        icon = "🌱",
        bgColor = Color(0xFF86EFAC), // Bright lush emerald pasture
        gridColor = Color(0x35047857), // Forest green lawn grid
        borderColor = Color(0xFFFFD700), // Golden border
        glowColor = Color(0xFF10B981), // Fresh emerald glow
        description = "Padang rumput hijau cerah berseri penuh kesegaran"
    ),
    LAVA_VOLCANO(
        id = "lava_volcano",
        title = "Koral Magma (Coral Sunburst)",
        icon = "🌋",
        bgColor = Color(0xFFFED7AA), // Bright warm coral peach sunburst
        gridColor = Color(0x35EA580C), // Warm coral grid lines
        borderColor = Color(0xFFDC2626), // Deep crimson danger border
        glowColor = Color(0xFFF59E0B), // Warm flame glow
        description = "Arena koral hangat terang dengan motif cerah berenergi"
    );

    companion object {
        fun fromId(id: String): MapTheme {
            if (id == "green_meadow" || id == "meadow") return COSMIC_SPACE
            return entries.find { it.id == id || it.id.startsWith(id) || id.startsWith(it.id) }
                ?: CYBER_FUNK
        }
    }
}
