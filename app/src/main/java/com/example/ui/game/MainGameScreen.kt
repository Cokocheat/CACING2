package com.example.ui.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import com.example.audio.GameSoundManager
import com.example.data.UserPreferencesRepository
import com.example.engine.WormsGameEngine
import com.example.model.GameMode
import kotlin.math.atan2

@Composable
fun MainGameScreen(
    userPrefs: UserPreferencesRepository,
    soundManager: GameSoundManager,
    gameMode: GameMode = GameMode.OPEN_ARENA,
    onBackToLobby: () -> Unit
) {
    val userData by userPrefs.userData.collectAsState()
    val engine = remember(gameMode, userData.gameDifficulty) {
        WormsGameEngine(soundManager, userData, gameMode, userData.gameDifficulty)
    }

    val hudState by engine.hudState.collectAsState()
    val leaderboard by engine.leaderboard.collectAsState()

    var isPaused by remember { mutableStateOf(false) }
    var showReviveDialog by remember { mutableStateOf(false) }
    var showGameOver by remember { mutableStateOf(false) }
    var currentReviveCost by remember { mutableIntStateOf(30) }

    // Intercept hardware back button
    BackHandler {
        if (!isPaused && !engine.isGameOver) {
            isPaused = true
            engine.isPaused = true
        } else if (isPaused) {
            isPaused = false
            engine.isPaused = false
        } else {
            onBackToLobby()
        }
    }

    // In-game background music control
    DisposableEffect(Unit) {
        engine.soundManager.startBgm()
        onDispose {
            engine.soundManager.stopBgm()
        }
    }

    LaunchedEffect(isPaused, engine.isGameOver) {
        if (isPaused || engine.isGameOver) {
            engine.soundManager.pauseBgm()
        } else {
            engine.soundManager.startBgm()
        }
    }

    // High performance 60 FPS Game Loop
    var renderTick by remember { mutableLongStateOf(0L) }
    var lastFrameTimeNanos by remember { mutableLongStateOf(0L) }

    LaunchedEffect(isPaused, engine.isGameOver) {
        lastFrameTimeNanos = 0L
        while (true) {
            withFrameNanos { frameTimeNanos ->
                if (lastFrameTimeNanos == 0L) {
                    lastFrameTimeNanos = frameTimeNanos
                } else {
                    val dt = ((frameTimeNanos - lastFrameTimeNanos) / 1_000_000_000f).coerceIn(0.005f, 0.04f)
                    lastFrameTimeNanos = frameTimeNanos
                    if (!isPaused && !engine.isGameOver) {
                        engine.update(dt)
                        renderTick++
                    }
                    if (engine.isGameOver && !showGameOver && !showReviveDialog) {
                        if (engine.isVictory) {
                            showGameOver = true
                            userPrefs.recordGameResult(
                                score = engine.player.mass.toInt(),
                                kills = engine.player.kills,
                                length = engine.player.bodyPoints.size,
                                coinsEarned = engine.coinsCollectedInSession
                            )
                        } else {
                            currentReviveCost = engine.calculateReviveCost()
                            showReviveDialog = true
                        }
                    }
                }
            }
        }
    }

    // Full-screen touch control: Touching or dragging ANYWHERE steers the worm immediately
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("main_game_screen")
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val diff = offset - center
                        val angle = atan2(diff.y, diff.x)
                        engine.setPlayerTargetAngle(angle)
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val diff = change.position - center
                        val angle = atan2(diff.y, diff.x)
                        engine.setPlayerTargetAngle(angle)
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val diff = offset - center
                        val angle = atan2(diff.y, diff.x)
                        engine.setPlayerTargetAngle(angle)
                    }
                )
            }
    ) {
        // High performance Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val _tick = renderTick
            val screenCenterX = size.width / 2f
            val screenCenterY = size.height / 2f
            val zoom = engine.cameraZoom

            // Center camera on Player head
            val playerHead = engine.player.head
            val cameraOffset = Offset(
                screenCenterX - playerHead.x * zoom,
                screenCenterY - playerHead.y * zoom
            )

            // 1. Draw arena background and borders
            WormsRenderer.drawArenaBackground(
                drawScope = this,
                cameraOffset = cameraOffset,
                zoom = zoom,
                arenaCenter = engine.arenaCenter,
                arenaRadius = engine.currentArenaRadius,
                mapTheme = userData.mapTheme
            )

            // 2. Draw Food items (culled against viewport)
            val viewMargin = 90f
            val w = size.width
            val h = size.height
            for (i in engine.foods.indices) {
                val food = engine.foods[i]
                val sx = food.position.x * zoom + cameraOffset.x
                val sy = food.position.y * zoom + cameraOffset.y
                if (sx >= -viewMargin && sx <= w + viewMargin &&
                    sy >= -viewMargin && sy <= h + viewMargin
                ) {
                    WormsRenderer.drawFood(this, food, Offset(sx, sy), zoom, userData.mapTheme, userData.foodSkinPack)
                }
            }

            // 3. Draw Potions (culled)
            for (i in engine.potions.indices) {
                val potion = engine.potions[i]
                val sx = potion.position.x * zoom + cameraOffset.x
                val sy = potion.position.y * zoom + cameraOffset.y
                if (sx >= -viewMargin && sx <= w + viewMargin &&
                    sy >= -viewMargin && sy <= h + viewMargin
                ) {
                    WormsRenderer.drawPotion(this, potion, Offset(sx, sy), zoom)
                }
            }

            // 4. Draw AI Bots (viewport culled for silky smooth 60-120 FPS performance)
            val botCullMargin = 180f
            for (i in engine.bots.indices) {
                val bot = engine.bots[i]
                if (!bot.isAlive) continue
                val headSx = bot.head.x * zoom + cameraOffset.x
                val headSy = bot.head.y * zoom + cameraOffset.y
                val botSpan = (bot.bodyPoints.size * bot.segmentRadius * 0.9f * zoom) + botCullMargin
                if (headSx >= -botSpan && headSx <= w + botSpan &&
                    headSy >= -botSpan && headSy <= h + botSpan
                ) {
                    WormsRenderer.drawWorm(
                        drawScope = this,
                        worm = bot,
                        cameraOffset = cameraOffset,
                        zoom = zoom,
                        isProtected = false,
                        gameTime = engine.gameTimeSeconds
                    )
                }
            }

            // 5. Draw Player Worm
            WormsRenderer.drawWorm(
                drawScope = this,
                worm = engine.player,
                cameraOffset = cameraOffset,
                zoom = zoom,
                isProtected = engine.isPlayerProtected,
                gameTime = engine.gameTimeSeconds,
                showAnalogArrow = userData.showAnalogArrow
            )

            // 6. Draw Particles
            WormsRenderer.drawParticles(this, engine.particles, cameraOffset, zoom)
        }

        // HUD Overlay
        GameHud(
            hudState = hudState,
            leaderboard = leaderboard,
            userData = userData,
            onDirectionChange = { angle ->
                engine.setPlayerTargetAngle(angle)
            },
            onBoostChange = { boosting ->
                engine.setPlayerBoosting(boosting)
            },
            onPauseClick = {
                isPaused = true
                engine.isPaused = true
            }
        )

        // Pause Modal
        if (isPaused && !engine.isGameOver) {
            PauseDialog(
                soundEnabled = userData.soundEnabled,
                vibrationEnabled = userData.vibrationEnabled,
                lang = userData.language,
                onResume = {
                    isPaused = false
                    engine.isPaused = false
                },
                onRestart = {
                    isPaused = false
                    showGameOver = false
                    showReviveDialog = false
                    engine.startNewGame(gameMode)
                },
                onToggleSound = { userPrefs.toggleSound(it) },
                onToggleVibration = { userPrefs.toggleVibration(it) },
                onQuitToLobby = onBackToLobby
            )
        }

        // Revive Modal (Offer to continue using coins)
        if (showReviveDialog && !showGameOver) {
            ReviveDialog(
                reviveCost = currentReviveCost,
                playerCoins = userData.coins,
                wormLength = engine.player.bodyPoints.size,
                lang = userData.language,
                onRevive = {
                    if (userPrefs.spendCoins(currentReviveCost)) {
                        showReviveDialog = false
                        engine.revivePlayer()
                    }
                },
                onGiveUp = {
                    showReviveDialog = false
                    showGameOver = true
                    userPrefs.recordGameResult(
                        score = engine.player.mass.toInt(),
                        kills = engine.player.kills,
                        length = engine.player.bodyPoints.size,
                        coinsEarned = engine.coinsCollectedInSession
                    )
                }
            )
        }

        // Game Over Modal
        if (showGameOver) {
            GameOverDialog(
                score = engine.player.mass.toInt(),
                highScore = userData.highScore,
                kills = engine.player.kills,
                length = engine.player.bodyPoints.size,
                coinsEarned = engine.coinsCollectedInSession,
                isVictory = engine.isVictory,
                lang = userData.language,
                onPlayAgain = {
                    showGameOver = false
                    showReviveDialog = false
                    isPaused = false
                    engine.startNewGame(gameMode)
                },
                onBackToLobby = onBackToLobby
            )
        }
    }
}
