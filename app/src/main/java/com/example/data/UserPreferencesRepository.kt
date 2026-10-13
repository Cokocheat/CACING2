package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.FaceAccessory
import com.example.model.MapTheme
import com.example.model.SkinId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ControlMode(val title: String, val description: String) {
    JOYSTICK("Joystick Virtual", "Kemudikan cacing menggunakan joystick virtual"),
    TOUCH_FOLLOW("Sentuh & Ikuti", "Cacing berbelok mengikuti posisi jari di layar")
}

enum class JoystickMode(val title: String, val description: String) {
    STATIC("Mode Diam", "Joystick tetap berada di posisi sudut layar yang telah diatur"),
    DYNAMIC("Mode Bergerak", "Joystick otomatis muncul di mana pun jempolmu menyentuh layar")
}

data class DailyRewardInfo(
    val day: Int,
    val coins: Int,
    val bonusDescription: String,
    val isSpecial: Boolean = false
)

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
    val vibrationEnabled: Boolean = true,
    // 7 Days Daily Login Event
    val lastClaimedDay: Int = 0, // 0 means haven't claimed day 1 yet
    val lastClaimTimestamp: Long = 0L,
    val firstInstallTimestamp: Long = 0L,
    // Customizable Control Positions
    val joystickOffsetX: Int = 36, // dp from left edge (default more inset for thumb)
    val joystickOffsetY: Int = 36, // dp from bottom edge
    val joystickScale: Float = 1.05f,
    val joystickMode: JoystickMode = JoystickMode.STATIC,
    val boostOffsetX: Int = 36, // dp from right edge
    val boostOffsetY: Int = 36, // dp from bottom edge
    val boostScale: Float = 1.05f,
    // Language & Map Theme Customization
    val language: AppLanguage = AppLanguage.INDONESIAN,
    val mapTheme: MapTheme = MapTheme.CYBER_FUNK
) {
    companion object {
        val DAILY_REWARDS = listOf(
            DailyRewardInfo(1, 200, "Hadiah Hari Pertama!"),
            DailyRewardInfo(2, 400, "Koin Bertambah!"),
            DailyRewardInfo(3, 650, "Bonus Ramuan & Koin!"),
            DailyRewardInfo(4, 900, "Koin Sultan Cacing!"),
            DailyRewardInfo(5, 1200, "Peti Emas Hadiah!"),
            DailyRewardInfo(6, 1600, "Hadiah Legendaris!"),
            DailyRewardInfo(7, 3000, "Jackpot 7 Hari + Skin Emas!", isSpecial = true)
        )
        const val ONE_DAY_MILLIS = 24 * 60 * 60 * 1000L
    }

    val canClaimDailyToday: Boolean
        get() {
            if (lastClaimedDay == 0) return true // First login ever, can claim day 1 immediately
            val elapsed = System.currentTimeMillis() - lastClaimTimestamp
            return elapsed >= ONE_DAY_MILLIS
        }

    val nextDailyDay: Int
        get() {
            return if (lastClaimedDay >= 7) {
                // If finished 7 days and 1 day passed, loop back to 1
                if (canClaimDailyToday) 1 else 7
            } else {
                lastClaimedDay + 1
            }
        }

    val millisUntilNextClaim: Long
        get() {
            if (canClaimDailyToday) return 0L
            val elapsed = System.currentTimeMillis() - lastClaimTimestamp
            return (ONE_DAY_MILLIS - elapsed).coerceAtLeast(0L)
        }
}

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

        var firstInstall = prefs.getLong("first_install_time", 0L)
        if (firstInstall == 0L) {
            firstInstall = System.currentTimeMillis()
            prefs.edit().putLong("first_install_time", firstInstall).apply()
        }

        val lastClaimDay = prefs.getInt("daily_claim_day", 0)
        val lastClaimTime = prefs.getLong("daily_claim_time", 0L)

        val joyX = prefs.getInt("joy_offset_x", 36)
        val joyY = prefs.getInt("joy_offset_y", 36)
        val joyScale = prefs.getFloat("joy_scale", 1.05f)

        val boostX = prefs.getInt("boost_offset_x", 36)
        val boostY = prefs.getInt("boost_offset_y", 36)
        val boostScale = prefs.getFloat("boost_scale", 1.05f)

        val langStr = prefs.getString("app_language", AppLanguage.INDONESIAN.code) ?: AppLanguage.INDONESIAN.code
        val language = AppLanguage.fromCode(langStr)

        val themeStr = prefs.getString("map_theme", MapTheme.GREEN_MEADOW.id) ?: MapTheme.GREEN_MEADOW.id
        val mapTheme = MapTheme.fromId(themeStr)

        val joyModeStr = prefs.getString("joystick_mode", JoystickMode.STATIC.name) ?: JoystickMode.STATIC.name
        val joystickMode = try { JoystickMode.valueOf(joyModeStr) } catch (_: Exception) { JoystickMode.STATIC }

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
            vibrationEnabled = vibration,
            lastClaimedDay = lastClaimDay,
            lastClaimTimestamp = lastClaimTime,
            firstInstallTimestamp = firstInstall,
            joystickOffsetX = joyX,
            joystickOffsetY = joyY,
            joystickScale = joyScale,
            joystickMode = joystickMode,
            boostOffsetX = boostX,
            boostOffsetY = boostY,
            boostScale = boostScale,
            language = language,
            mapTheme = mapTheme
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

    fun claimDailyReward(): Pair<Int, Int>? {
        val current = _userData.value
        if (!current.canClaimDailyToday) return null

        val targetDay = current.nextDailyDay
        val rewardInfo = UserGameData.DAILY_REWARDS.getOrNull(targetDay - 1) 
            ?: UserGameData.DAILY_REWARDS.last()
        val rewardCoins = rewardInfo.coins

        val now = System.currentTimeMillis()
        val newCoins = current.coins + rewardCoins
        var newUnlockedSkins = current.unlockedSkins

        // Special Day 7 reward: Unlock Golden Skin if not unlocked!
        if (targetDay == 7 && !newUnlockedSkins.contains(SkinId.GOLDEN_EMPEROR)) {
            newUnlockedSkins = newUnlockedSkins + SkinId.GOLDEN_EMPEROR
            prefs.edit().putStringSet("unlocked_skins", newUnlockedSkins.map { it.name }.toSet()).apply()
        }

        prefs.edit()
            .putInt("daily_claim_day", targetDay)
            .putLong("daily_claim_time", now)
            .putInt("coins", newCoins)
            .apply()

        _userData.value = current.copy(
            lastClaimedDay = targetDay,
            lastClaimTimestamp = now,
            coins = newCoins,
            unlockedSkins = newUnlockedSkins
        )

        return Pair(targetDay, rewardCoins)
    }

    fun spendCoins(amount: Int): Boolean {
        val current = _userData.value
        if (current.coins < amount) return false
        val newCoins = current.coins - amount
        prefs.edit().putInt("coins", newCoins).apply()
        _userData.value = current.copy(coins = newCoins)
        return true
    }

    fun addCoins(amount: Int) {
        val current = _userData.value
        val newCoins = current.coins + amount
        prefs.edit().putInt("coins", newCoins).apply()
        _userData.value = current.copy(coins = newCoins)
    }

    fun updateJoystickSettings(offsetX: Int, offsetY: Int, scale: Float) {
        val safeX = offsetX.coerceIn(10, 140)
        val safeY = offsetY.coerceIn(10, 140)
        val safeScale = scale.coerceIn(0.75f, 1.45f)
        prefs.edit()
            .putInt("joy_offset_x", safeX)
            .putInt("joy_offset_y", safeY)
            .putFloat("joy_scale", safeScale)
            .apply()
        _userData.value = _userData.value.copy(
            joystickOffsetX = safeX,
            joystickOffsetY = safeY,
            joystickScale = safeScale
        )
    }

    fun updateBoostSettings(offsetX: Int, offsetY: Int, scale: Float) {
        val safeX = offsetX.coerceIn(10, 140)
        val safeY = offsetY.coerceIn(10, 140)
        val safeScale = scale.coerceIn(0.75f, 1.45f)
        prefs.edit()
            .putInt("boost_offset_x", safeX)
            .putInt("boost_offset_y", safeY)
            .putFloat("boost_scale", safeScale)
            .apply()
        _userData.value = _userData.value.copy(
            boostOffsetX = safeX,
            boostOffsetY = safeY,
            boostScale = safeScale
        )
    }

    fun resetControlSettings() {
        updateJoystickSettings(36, 36, 1.05f)
        updateBoostSettings(36, 36, 1.05f)
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

    fun setJoystickMode(mode: JoystickMode) {
        prefs.edit().putString("joystick_mode", mode.name).apply()
        _userData.value = _userData.value.copy(joystickMode = mode)
    }

    fun toggleSound(enabled: Boolean) {
        prefs.edit().putBoolean("sound_enabled", enabled).apply()
        _userData.value = _userData.value.copy(soundEnabled = enabled)
    }

    fun toggleVibration(enabled: Boolean) {
        prefs.edit().putBoolean("vibration_enabled", enabled).apply()
        _userData.value = _userData.value.copy(vibrationEnabled = enabled)
    }

    fun setLanguage(lang: AppLanguage) {
        prefs.edit().putString("app_language", lang.code).apply()
        _userData.value = _userData.value.copy(language = lang)
    }

    fun setMapTheme(theme: MapTheme) {
        prefs.edit().putString("map_theme", theme.id).apply()
        _userData.value = _userData.value.copy(mapTheme = theme)
    }
}
