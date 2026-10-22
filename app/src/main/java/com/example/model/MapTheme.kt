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
        title = "Terracotta Klasik (Classic Arena)",
        icon = "",
        bgColor = Color(0xFF6B383D), // Warm terracotta / cocoa maroon exactly like the screenshot
        gridColor = Color(0xFF55252A), // Checkerboard tile line tone
        borderColor = Color(0xFFFF5722), // Vibrant danger boundary
        glowColor = Color(0xFFFF8A65), // Warm boundary glow
        description = "Arena terracotta bermotif catur hangat dengan efek bayangan gelap elegan"
    ),
    RAINBOW_CARNIVAL(
        id = "rainbow_carnival",
        title = "Langit Malam (Midnight Slate)",
        icon = "",
        bgColor = Color(0xFF26334D), // Deep slate blue checkered
        gridColor = Color(0xFF1B2538),
        borderColor = Color(0xFF8B5CF6),
        glowColor = Color(0xFF06B6D4),
        description = "Karnaval malam bermotif catur dengan kilau warna elegan"
    ),
    SWEET_CANDY(
        id = "sweet_candy",
        title = "Permen Berry (Berry Maroon)",
        icon = "",
        bgColor = Color(0xFF4D243B), // Deep berry maroon checkered
        gridColor = Color(0xFF3B182B),
        borderColor = Color(0xFFEC4899),
        glowColor = Color(0xFFA855F7),
        description = "Dunia permen manis berry bermotif catur lembut"
    ),
    COSMIC_SPACE(
        id = "green_meadow",
        title = "Hutan Lumut (Deep Forest)",
        icon = "",
        bgColor = Color(0xFF263A29), // Deep forest moss checkered
        gridColor = Color(0xFF1B2B1D),
        borderColor = Color(0xFF4ADE80),
        glowColor = Color(0xFF10B981),
        description = "Padang rumput hutan hijau teduh bermotif catur nyaman di mata"
    ),
    LAVA_VOLCANO(
        id = "lava_volcano",
        title = "Batu Magma (Ember Stone)",
        icon = "",
        bgColor = Color(0xFF3D2321), // Deep volcanic chocolate checkered
        gridColor = Color(0xFF2E1715),
        borderColor = Color(0xFFDC2626),
        glowColor = Color(0xFFF59E0B),
        description = "Kawah batu magma hangat dengan efek bayangan gelap dramatis"
    );

    companion object {
        fun fromId(id: String): MapTheme {
            if (id == "green_meadow" || id == "meadow") return COSMIC_SPACE
            return entries.find { it.id == id || it.id.startsWith(id) || id.startsWith(it.id) }
                ?: CYBER_FUNK
        }
    }
}
