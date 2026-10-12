package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.audio.GameSoundManager
import com.example.data.UserPreferencesRepository
import com.example.model.GameMode
import com.example.ui.game.MainGameScreen
import com.example.ui.screens.LobbyScreen
import com.example.ui.screens.RecordsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.UpgradesScreen
import com.example.ui.screens.WardrobeScreen
import com.example.ui.theme.GameDarkBg

enum class AppScreen {
    LOBBY,
    GAMEPLAY,
    WARDROBE,
    UPGRADES,
    RECORDS,
    SETTINGS
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val userPrefs = remember { UserPreferencesRepository(context) }
    val soundManager = remember { GameSoundManager(context) }
    val userData by userPrefs.userData.collectAsState()

    // Sync sound/vibration flags
    soundManager.soundEnabled = userData.soundEnabled
    soundManager.vibrationEnabled = userData.vibrationEnabled

    var currentScreen by remember { mutableStateOf(AppScreen.LOBBY) }
    var selectedGameMode by remember { mutableStateOf(GameMode.OPEN_ARENA) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = GameDarkBg
    ) {
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "ScreenTransition"
        ) { screen ->
            when (screen) {
                AppScreen.LOBBY -> {
                    LobbyScreen(
                        userData = userData,
                        onPlayClick = { mode ->
                            selectedGameMode = mode
                            currentScreen = AppScreen.GAMEPLAY
                        },
                        onOpenWardrobe = { currentScreen = AppScreen.WARDROBE },
                        onOpenUpgrades = { currentScreen = AppScreen.UPGRADES },
                        onOpenRecords = { currentScreen = AppScreen.RECORDS },
                        onOpenSettings = { currentScreen = AppScreen.SETTINGS },
                        onUpdateName = { userPrefs.updatePlayerName(it) },
                        onClaimDailyReward = { userPrefs.claimDailyReward() }
                    )
                }
                AppScreen.GAMEPLAY -> {
                    MainGameScreen(
                        userPrefs = userPrefs,
                        soundManager = soundManager,
                        gameMode = selectedGameMode,
                        onBackToLobby = { currentScreen = AppScreen.LOBBY }
                    )
                }
                AppScreen.WARDROBE -> {
                    WardrobeScreen(
                        userData = userData,
                        onEquipSkin = { userPrefs.equipSkin(it) },
                        onBuySkin = { userPrefs.buySkin(it) },
                        onEquipAccessory = { userPrefs.equipAccessory(it) },
                        onBuyAccessory = { userPrefs.buyAccessory(it) },
                        onBack = { currentScreen = AppScreen.LOBBY }
                    )
                }
                AppScreen.UPGRADES -> {
                    UpgradesScreen(
                        userData = userData,
                        onUpgrade = { type, cost -> userPrefs.upgradeUpgrade(type, cost) },
                        onBack = { currentScreen = AppScreen.LOBBY }
                    )
                }
                AppScreen.RECORDS -> {
                    RecordsScreen(
                        userData = userData,
                        onBack = { currentScreen = AppScreen.LOBBY }
                    )
                }
                AppScreen.SETTINGS -> {
                    SettingsScreen(
                        userData = userData,
                        onSetControlMode = { userPrefs.setControlMode(it) },
                        onToggleSound = { userPrefs.toggleSound(it) },
                        onToggleVibration = { userPrefs.toggleVibration(it) },
                        onUpdateName = { userPrefs.updatePlayerName(it) },
                        onUpdateJoystick = { x, y, scale -> userPrefs.updateJoystickSettings(x, y, scale) },
                        onUpdateBoost = { x, y, scale -> userPrefs.updateBoostSettings(x, y, scale) },
                        onResetControls = { userPrefs.resetControlSettings() },
                        onBack = { currentScreen = AppScreen.LOBBY }
                    )
                }
            }
        }
    }
}
