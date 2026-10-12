package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.FaceAccessory
import com.example.model.SkinId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ControlMode(val title: String, val description: String) {
    JOYSTICK("Joystick Virtual", "Kemudikan cacing menggunakan joystick virtual"),
    TOUCH_FOLLOW("Sentuh & Ikuti", "Cacing berbelok mengikuti posisi jari di layar")
}

data class UserGameData(
    val playerName: String = "Nana Tayantuh",
    val coins: Int = 150,
    val highScore: Int = 0,
    val totalKills: Int = 0,
    val totalGames: Int = 0,
    val longestLength: Int = 0,
    val equippedSkin: SkinId = SkinId.CLASSIC_GREEN,
    val equippedAccessory: FaceAccessory = FaceAccessory.NONE,
    val unlockedSkins: Set<SkinId> = setOf(SkinId.CLASSIC_GREEN),
    val unlockedAccessories: Set<FaceAccessory> = setOf(FaceAccessory.NONE),
    val magnetLevel: Int = 1,
    val multiplierLevel: Int = 1,
    val boostLevel: Int = 1,
    val speedLevel: Int = 1,
    val controlMode: ControlMode = ControlMode.JOYSTICK,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true
)

class UserPreferencesRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("worms_zone_prefs", Context.MODE_PRIVATE)

    private val _userData = MutableStateFlow(loadData())
    val userData: StateFlow<UserGameData> = _userData.asStateFlow()

    private fun loadData(): UserGameData {
        val name = prefs.getString("player_name", "Nana Tayantuh") ?: "Nana Tayantuh"
        val coins = prefs.getInt("coins", 150)
        val highScore = prefs.getInt("high_score", 0)
        val totalKills = prefs.getInt("total_kills", 0)
        val totalGames = prefs.getInt("total_games", 0)
        val longestLength = prefs.getInt("longest_length", 0)

        val skinStr = prefs.getString("equipped_skin", SkinId.CLASSIC_GREEN.name) ?: SkinId.CLASSIC_GREEN.name
        val equippedSkin = SkinId.fromId(skinStr)

        val accStr = prefs.getString("equipped_accessory", FaceAccessory.NONE.name) ?: FaceAccessory.NONE.name
        val equippedAccessory = FaceAccessory.fromId(accStr)

        val unlockedSkinsSet = prefs.getStringSet("unlocked_skins", setOf(SkinId.CLASSIC_GREEN.name))
            ?.mapNotNull { runCatching { SkinId.valueOf(it) }.getOrNull() }
            ?.toSet() ?: setOf(SkinId.CLASSIC_GREEN)

        val unlockedAccSet = prefs.getStringSet("unlocked_acc", setOf(FaceAccessory.NONE.name))
            ?.mapNotNull { runCatching { FaceAccessory.valueOf(it) }.getOrNull() }
            ?.toSet() ?: setOf(FaceAccessory.NONE)

        val magnetLvl = prefs.getInt("magnet_lvl", 1).coerceIn(1, 5)
        val multiLvl = prefs.getInt("multi_lvl", 1).coerceIn(1, 5)
        val boostLvl = prefs.getInt("boost_lvl", 1).coerceIn(1, 5)
        val speedLvl = prefs.getInt("speed_lvl", 1).coerceIn(1, 5)

        val ctrlStr = prefs.getString("control_mode", ControlMode.JOYSTICK.name) ?: ControlMode.JOYSTICK.name
        val controlMode = runCatching { ControlMode.valueOf(ctrlStr) }.getOrDefault(ControlMode.JOYSTICK)

        val sound = prefs.getBoolean("sound_enabled", true)
        val vibration = prefs.getBoolean("vibration_enabled", true)

        return UserGameData(
            playerName = name,
            coins = coins,
            highScore = highScore,
            totalKills = totalKills,
            totalGames = totalGames,
            longestLength = longestLength,
            equippedSkin = equippedSkin,
            equippedAccessory = equippedAccessory,
            unlockedSkins = unlockedSkinsSet,
            unlockedAccessories = unlockedAccSet,
            magnetLevel = magnetLvl,
            multiplierLevel = multiLvl,
            boostLevel = boostLvl,
            speedLevel = speedLvl,
            controlMode = controlMode,
            soundEnabled = sound,
            vibrationEnabled = vibration
        )
    }

    fun updatePlayerName(newName: String) {
        val trimmed = newName.trim().take(16).ifEmpty { "Nana Tayantuh" }
        prefs.edit().putString("player_name", trimmed).apply()
        _userData.value = _userData.value.copy(playerName = trimmed)
    }

    fun recordGameResult(score: Int, kills: Int, length: Int, coinsEarned: Int) {
        val current = _userData.value
        val newHigh = maxOf(current.highScore, score)
        val newKills = current.totalKills + kills
        val newGames = current.totalGames + 1
        val newCoins = current.coins + coinsEarned
        val newLongest = maxOf(current.longestLength, length)

        prefs.edit()
            .putInt("high_score", newHigh)
            .putInt("total_kills", newKills)
            .putInt("total_games", newGames)
            .putInt("coins", newCoins)
            .putInt("longest_length", newLongest)
            .apply()

        _userData.value = current.copy(
            highScore = newHigh,
            totalKills = newKills,
            totalGames = newGames,
            coins = newCoins,
            longestLength = newLongest
        )
    }

    fun equipSkin(skin: SkinId) {
        if (_userData.value.unlockedSkins.contains(skin)) {
            prefs.edit().putString("equipped_skin", skin.name).apply()
            _userData.value = _userData.value.copy(equippedSkin = skin)
        }
    }

    fun buySkin(skin: SkinId): Boolean {
        val current = _userData.value
        if (current.unlockedSkins.contains(skin)) {
            equipSkin(skin)
            return true
        }
        if (current.coins >= skin.price) {
            val newCoins = current.coins - skin.price
            val newUnlocked = current.unlockedSkins + skin
            prefs.edit()
                .putInt("coins", newCoins)
                .putStringSet("unlocked_skins", newUnlocked.map { it.name }.toSet())
                .putString("equipped_skin", skin.name)
                .apply()
            _userData.value = current.copy(
                coins = newCoins,
                unlockedSkins = newUnlocked,
                equippedSkin = skin
            )
            return true
        }
        return false
    }

    fun equipAccessory(acc: FaceAccessory) {
        if (_userData.value.unlockedAccessories.contains(acc)) {
            prefs.edit().putString("equipped_accessory", acc.name).apply()
            _userData.value = _userData.value.copy(equippedAccessory = acc)
        }
    }

    fun buyAccessory(acc: FaceAccessory): Boolean {
        val current = _userData.value
        if (current.unlockedAccessories.contains(acc)) {
            equipAccessory(acc)
            return true
        }
        if (current.coins >= acc.price) {
            val newCoins = current.coins - acc.price
            val newUnlocked = current.unlockedAccessories + acc
            prefs.edit()
                .putInt("coins", newCoins)
                .putStringSet("unlocked_acc", newUnlocked.map { it.name }.toSet())
                .putString("equipped_accessory", acc.name)
                .apply()
            _userData.value = current.copy(
                coins = newCoins,
                unlockedAccessories = newUnlocked,
                equippedAccessory = acc
            )
            return true
        }
        return false
    }

    fun upgradeUpgrade(type: String, cost: Int): Boolean {
        val current = _userData.value
        if (current.coins < cost) return false
        val newCoins = current.coins - cost
        val editor = prefs.edit().putInt("coins", newCoins)

        val updated = when (type) {
            "magnet" -> {
                val next = (current.magnetLevel + 1).coerceAtMost(5)
                editor.putInt("magnet_lvl", next)
                current.copy(coins = newCoins, magnetLevel = next)
            }
            "multiplier" -> {
                val next = (current.multiplierLevel + 1).coerceAtMost(5)
                editor.putInt("multi_lvl", next)
                current.copy(coins = newCoins, multiplierLevel = next)
            }
            "boost" -> {
                val next = (current.boostLevel + 1).coerceAtMost(5)
                editor.putInt("boost_lvl", next)
                current.copy(coins = newCoins, boostLevel = next)
            }
            "speed" -> {
                val next = (current.speedLevel + 1).coerceAtMost(5)
                editor.putInt("speed_lvl", next)
                current.copy(coins = newCoins, speedLevel = next)
            }
            else -> current
        }
        editor.apply()
        _userData.value = updated
        return true
    }

    fun setControlMode(mode: ControlMode) {
        prefs.edit().putString("control_mode", mode.name).apply()
        _userData.value = _userData.value.copy(controlMode = mode)
    }

    fun toggleSound(enabled: Boolean) {
        prefs.edit().putBoolean("sound_enabled", enabled).apply()
        _userData.value = _userData.value.copy(soundEnabled = enabled)
    }

    fun toggleVibration(enabled: Boolean) {
        prefs.edit().putBoolean("vibration_enabled", enabled).apply()
        _userData.value = _userData.value.copy(vibrationEnabled = enabled)
    }
}
