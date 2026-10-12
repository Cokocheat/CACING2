package com.example.model

import androidx.compose.ui.graphics.Color

enum class SkinId(
    val title: String,
    val description: String,
    val price: Int,
    val headColor: Color,
    val primaryColor: Color,
    val secondaryColor: Color,
    val patternType: PatternType
) {
    CLASSIC_GREEN(
        title = "Hijau Klasik",
        description = "Cacing hijau legendaris Worms Zone",
        price = 0,
        headColor = Color(0xFF4ADE80),
        primaryColor = Color(0xFF22C55E),
        secondaryColor = Color(0xFF16A34A),
        patternType = PatternType.STRIPES
    ),
    RAINBOW(
        title = "Pelangi Ceria",
        description = "Warna-warni pelangi berkilau",
        price = 250,
        headColor = Color(0xFFFF5252),
        primaryColor = Color(0xFFFFEB3B),
        secondaryColor = Color(0xFF2196F3),
        patternType = PatternType.RAINBOW_CYCLE
    ),
    BUMBLEBEE(
        title = "Lebah Menyengat",
        description = "Kuning cerah dan hitam belang",
        price = 400,
        headColor = Color(0xFFFFD600),
        primaryColor = Color(0xFFFFEA00),
        secondaryColor = Color(0xFF212121),
        patternType = PatternType.STRIPES
    ),
    WATERMELON(
        title = "Semangka Segar",
        description = "Kulit hijau dengan daging merah berbiji",
        price = 600,
        headColor = Color(0xFFE91E63),
        primaryColor = Color(0xFFFF5252),
        secondaryColor = Color(0xFF2E7D32),
        patternType = PatternType.WATERMELON_SEGMENTS
    ),
    TIGER(
        title = "Macan Api",
        description = "Belang oranye menyala siap berburu",
        price = 850,
        headColor = Color(0xFFFF6D00),
        primaryColor = Color(0xFFFF9100),
        secondaryColor = Color(0xFF263238),
        patternType = PatternType.STRIPES
    ),
    GALAXY(
        title = "Galaksi Kosmik",
        description = "Bintang ungu dan nebula neon",
        price = 1200,
        headColor = Color(0xFF7C4DFF),
        primaryColor = Color(0xFF651FFF),
        secondaryColor = Color(0xFF00E5FF),
        patternType = PatternType.NEON_PULSE
    ),
    CANDY_GUMMY(
        title = "Permen Gummy",
        description = "Manis ceria warna pastel buah",
        price = 1500,
        headColor = Color(0xFFFF4081),
        primaryColor = Color(0xFF00E676),
        secondaryColor = Color(0xFFFFD54F),
        patternType = PatternType.RAINBOW_CYCLE
    ),
    GOLDEN_EMPEROR(
        title = "Kaisar Emas",
        description = "Kemewahan emas murni sang jawara",
        price = 2500,
        headColor = Color(0xFFFFD700),
        primaryColor = Color(0xFFFFC107),
        secondaryColor = Color(0xFFFFA000),
        patternType = PatternType.GOLDEN_SHINE
    );

    companion object {
        fun fromId(id: String): SkinId {
            return entries.find { it.name == id } ?: CLASSIC_GREEN
        }
    }
}

enum class PatternType {
    STRIPES,
    RAINBOW_CYCLE,
    WATERMELON_SEGMENTS,
    NEON_PULSE,
    GOLDEN_SHINE
}

enum class FaceAccessory(
    val title: String,
    val description: String,
    val price: Int
) {
    NONE("Biasa", "Mata bulat ramah ekspresif", 0),
    ROYAL_CROWN("Mahkota Emas", "Mahkota raja pemburu arena", 350),
    SUNGLASSES("Kacamata Keren", "Tampil modis dan misterius", 500),
    PIRATE_PATCH("Bajak Laut", "Penutup mata petualang samudra", 700),
    ANIME_SPARKLE("Mata Bintang", "Mata kawaii berbinar imut", 900);

    companion object {
        fun fromId(id: String): FaceAccessory {
            return entries.find { it.name == id } ?: NONE
        }
    }
}
