package com.example.model

import androidx.compose.ui.graphics.Color

enum class FoodType(
    val title: String,
    val massValue: Float,
    val baseRadius: Float,
    val primaryColor: Color
) {
    APPLE("Apel", 1.0f, 10f, Color(0xFFFF1744)),
    STRAWBERRY("Stroberi", 1.2f, 11f, Color(0xFFFF4081)),
    CHEESE("Keju", 2.0f, 13f, Color(0xFFFFD54F)),
    WATERMELON_SLICE("Semangka", 2.5f, 14f, Color(0xFF00E676)),
    DONUT("Donat", 3.5f, 16f, Color(0xFFFF80AB)),
    CUPCAKE("Kue Muffin", 4.0f, 17f, Color(0xFFBA68C8)),
    SANDWICH("Sandwich", 5.0f, 19f, Color(0xFFFFB74D)),
    BURGER("Burger Lezat", 6.5f, 21f, Color(0xFFFF8F00)),
    PIZZA("Pizza Gurih", 7.0f, 22f, Color(0xFFFF5722)),
    GOLDEN_COIN("Koin Emas", 4.0f, 16f, Color(0xFFFFD700)),
    BOOST_DROP("Percikan Energi", 0.5f, 7f, Color(0xFFFFEB3B)),
    DEATH_FEAST("Pesta Makanan", 8.0f, 24f, Color(0xFFFF1744));

    companion object {
        private val standardFood = listOf(
            APPLE, STRAWBERRY, CHEESE, WATERMELON_SLICE,
            DONUT, CUPCAKE, SANDWICH, BURGER, PIZZA
        )

        fun randomStandard(randomSeed: Double): FoodType {
            val idx = (randomSeed * standardFood.size).toInt().coerceIn(0, standardFood.lastIndex)
            return standardFood[idx]
        }
    }
}

data class FoodItem(
    val id: Long,
    var position: Vec2,
    val type: FoodType,
    val mass: Float = type.massValue,
    val radius: Float = type.baseRadius,
    var velocity: Vec2 = Vec2.Zero,
    var rotation: Float = 0f,
    var isDeathDrop: Boolean = false
)
