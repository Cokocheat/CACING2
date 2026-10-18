package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.FaceAccessory
import com.example.model.GameDifficulty
import com.example.model.MapTheme
import com.example.model.SkinId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ControlMode {
    JOYSTICK,
    TOUCH_FOLLOW
}

enum class JoystickMode {
    STATIC,
    DYNAMIC
}

data class DailyRewardInfo(
    val day: Int,
    val coins: Int,
    val title: String = "",
    val unlockSkin: SkinId? = null,
    val unlockAccessory: FaceAccessory? = null,
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
    val soundVolume: Int = 8, // 0 to 10
    val musicVolume: Int = 7, // 0 to 10
    val vibrationEnabled: Boolean = true,
    // 15 Days Daily Login Event
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
    // Language & Map Theme & Enemy Difficulty Customization
    val language: AppLanguage = AppLanguage.INDONESIAN,
    val mapTheme: MapTheme = MapTheme.CYBER_FUNK,
    val gameDifficulty: GameDifficulty = GameDifficulty.NORMAL,
    // Detailed Profile Stats & Achievements (from screenshots)
    val bestSurvivalSeconds: Int = 1658, // 27m 38d
    val totalPlayTimeSeconds: Long = 12175L, // 3j 22m 55d
    val piggyBankCoins: Int = 1500,
    val claimedAchievements: Set<String> = emptySet(),
    val showAnalogArrow: Boolean = true,
    val foodSkinPack: String = "FAST_FOOD",
    val highFpsEnabled: Boolean = true
) {
    companion object {
        val DAILY_REWARDS = listOf(
            DailyRewardInfo(1, 300, "Paket Pemula"),
            DailyRewardInfo(2, 500, "Bonus Koin"),
            DailyRewardInfo(3, 750, "Kacamata Hitam Keren", unlockAccessory = FaceAccessory.SUNGLASSES, isSpecial = true),
            DailyRewardInfo(4, 1000, "Ekstra Koin"),
            DailyRewardInfo(5, 1400, "Skin Pelangi Berkilau", unlockSkin = SkinId.RAINBOW, isSpecial = true),
            DailyRewardInfo(6, 1800, "Kantong Koin"),
            DailyRewardInfo(7, 2500, "Peti Emas Minggu 1", isSpecial = true),
            DailyRewardInfo(8, 2800, "Topeng Bajak Laut", unlockAccessory = FaceAccessory.PIRATE_PATCH, isSpecial = true),
            DailyRewardInfo(9, 3200, "Tumpukan Koin"),
            DailyRewardInfo(10, 3800, "Skin Neon Cyber", unlockSkin = SkinId.BUMBLEBEE, isSpecial = true),
            DailyRewardInfo(11, 4200, "Koin Melimpah"),
            DailyRewardInfo(12, 4800, "Kilau Mata Anime", unlockAccessory = FaceAccessory.ANIME_SPARKLE, isSpecial = true),
            DailyRewardInfo(13, 5500, "Harta Karun"),
            DailyRewardInfo(14, 6500, "Skin Kosmik Galaksi", unlockSkin = SkinId.GALAXY, isSpecial = true),
            DailyRewardInfo(15, 12000, "KAISAR EMAS & MAHKOTA!", unlockSkin = SkinId.GOLDEN_EMPEROR, unlockAccessory = FaceAccessory.ROYAL_CROWN, isSpecial = true)
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
            return if (lastClaimedDay >= 15) {
                // If finished 15 days and 1 day passed, loop back to 1
                if (canClaimDailyToday) 1 else 15
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
        val soundVol = prefs.getInt("sound_volume", 8)
        val musicVol = prefs.getInt("music_volume", 7)
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

        val themeStr = prefs.getString("map_theme", MapTheme.CYBER_FUNK.id) ?: MapTheme.CYBER_FUNK.id
        val mapTheme = MapTheme.fromId(themeStr)

        val diffStr = prefs.getString("game_difficulty", GameDifficulty.NORMAL.id) ?: GameDifficulty.NORMAL.id
        val gameDifficulty = GameDifficulty.fromId(diffStr)

        val joyModeStr = prefs.getString("joystick_mode", JoystickMode.STATIC.name) ?: JoystickMode.STATIC.name
        val joystickMode = try { JoystickMode.valueOf(joyModeStr) } catch (_: Exception) { JoystickMode.STATIC }

        val bestSurvival = prefs.getInt("best_survival", 1658)
        val totalPlay = prefs.getLong("total_play_time", 12175L)
        val piggyCoins = prefs.getInt("piggy_bank_coins", 1500)
        val claimedAch = prefs.getStringSet("claimed_achievements", emptySet()) ?: emptySet()

        val showArrow = prefs.getBoolean("show_analog_arrow", true)
        val foodPack = prefs.getString("food_skin_pack", "FAST_FOOD") ?: "FAST_FOOD"
        val highFps = prefs.getBoolean("high_fps_enabled", true)

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
            soundVolume = soundVol,
            musicVolume = musicVol,
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
            mapTheme = mapTheme,
            gameDifficulty = gameDifficulty,
            bestSurvivalSeconds = bestSurvival,
            totalPlayTimeSeconds = totalPlay,
            piggyBankCoins = piggyCoins,
            claimedAchievements = claimedAch,
            showAnalogArrow = showArrow,
            foodSkinPack = foodPack,
            highFpsEnabled = highFps
        )
    }

    fun claimAchievement(achievementId: String, rewardCoins: Int) {
        val current = _userData.value
        val updatedClaimed = current.claimedAchievements + achievementId
        val updatedCoins = current.coins + rewardCoins
        prefs.edit()
            .putStringSet("claimed_achievements", updatedClaimed)
            .putInt("coins", updatedCoins)
            .apply()
        _userData.value = current.copy(
            coins = updatedCoins,
            claimedAchievements = updatedClaimed
        )
    }

    fun breakPiggyBank(): Int {
        val current = _userData.value
        val reward = current.piggyBankCoins
        val updatedCoins = current.coins + reward
        prefs.edit()
            .putInt("piggy_bank_coins", 0)
            .putInt("coins", updatedCoins)
            .apply()
        _userData.value = current.copy(
            coins = updatedCoins,
            piggyBankCoins = 0
        )
        return reward
    }

    fun resetAllUserData() {
        prefs.edit().clear().apply()
        _userData.value = UserGameData(
            playerName = "Nana Tayantuh",
            coins = 1500,
            highScore = 0,
            totalKills = 0,
            totalGames = 0,
            unlockedSkins = setOf(SkinId.CLASSIC_GREEN),
            unlockedAccessories = setOf(FaceAccessory.NONE),
            claimedAchievements = emptySet(),
            piggyBankCoins = 500
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
        var newUnlockedAccessories = current.unlockedAccessories

        rewardInfo.unlockSkin?.let { skin ->
            if (!newUnlockedSkins.contains(skin)) {
                newUnlockedSkins = newUnlockedSkins + skin
                prefs.edit().putStringSet("unlocked_skins", newUnlockedSkins.map { it.name }.toSet()).apply()
            }
        }

        rewardInfo.unlockAccessory?.let { acc ->
            if (!newUnlockedAccessories.contains(acc)) {
                newUnlockedAccessories = newUnlockedAccessories + acc
                prefs.edit().putStringSet("unlocked_accessories", newUnlockedAccessories.map { it.name }.toSet()).apply()
            }
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
            unlockedSkins = newUnlockedSkins,
            unlockedAccessories = newUnlockedAccessories
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

    fun setSoundVolume(volume: Int) {
        val v = volume.coerceIn(0, 10)
        prefs.edit().putInt("sound_volume", v).putBoolean("sound_enabled", v > 0).apply()
        _userData.value = _userData.value.copy(soundVolume = v, soundEnabled = v > 0)
    }

    fun setMusicVolume(volume: Int) {
        val v = volume.coerceIn(0, 10)
        prefs.edit().putInt("music_volume", v).apply()
        _userData.value = _userData.value.copy(musicVolume = v)
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

    fun setGameDifficulty(diff: GameDifficulty) {
        prefs.edit().putString("game_difficulty", diff.id).apply()
        _userData.value = _userData.value.copy(gameDifficulty = diff)
    }

    fun toggleAnalogArrow(enabled: Boolean) {
        prefs.edit().putBoolean("show_analog_arrow", enabled).apply()
        _userData.value = _userData.value.copy(showAnalogArrow = enabled)
    }

    fun setFoodSkinPack(pack: String) {
        prefs.edit().putString("food_skin_pack", pack).apply()
        _userData.value = _userData.value.copy(foodSkinPack = pack)
    }

    fun toggleHighFps(enabled: Boolean) {
        prefs.edit().putBoolean("high_fps_enabled", enabled).apply()
        _userData.value = _userData.value.copy(highFpsEnabled = enabled)
    }
}
