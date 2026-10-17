package com.example.engine

import androidx.compose.ui.graphics.Color
import com.example.audio.GameSoundManager
import com.example.data.UserGameData
import com.example.model.BotPersonality
import com.example.model.FaceAccessory
import com.example.model.FoodItem
import com.example.model.FoodType
import com.example.model.GameConfig
import com.example.model.GameDifficulty
import com.example.model.GameMode
import com.example.model.GameParticle
import com.example.model.PotionItem
import com.example.model.PotionType
import com.example.model.SkinId
import com.example.model.Vec2
import com.example.model.Worm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

data class LeaderboardEntry(
    val name: String,
    val score: Int,
    val isPlayer: Boolean,
    val rank: Int
)

data class KillEvent(
    val victimName: String,
    val streak: Int,
    val coinsEarned: Int,
    val timestamp: Long = System.currentTimeMillis()
)

data class EngineHudState(
    val score: Int = 25,
    val coins: Int = 0,
    val kills: Int = 0,
    val playerRank: Int = 1,
    val spawnProtectionRemaining: Float = 6.0f,
    val activePotions: Map<PotionType, Float> = emptyMap(),
    val playerPos: Vec2 = Vec2.Zero,
    val botPositions: List<Vec2> = emptyList(),
    val gameMode: GameMode = GameMode.OPEN_ARENA,
    val difficulty: GameDifficulty = GameDifficulty.NORMAL,
    val timeAttackRemaining: Float = 180f,
    val aliveBotsCount: Int = 0,
    val currentArenaRadius: Float = GameConfig.ARENA_RADIUS,
    val recentKillEvent: KillEvent? = null
)

class WormsGameEngine(
    private val soundManager: GameSoundManager,
    private val userData: UserGameData,
    var gameMode: GameMode = GameMode.OPEN_ARENA,
    var difficulty: GameDifficulty = userData.gameDifficulty
) {
    private val idGen = AtomicLong(1)

    companion object {
        private val BOT_NAMES = listOf(
            "ViperStrike", "MegaSlither", "DonutKing", "NoodleGod",
            "TurboChonk", "PixelSnek", "ShadowCrawl", "SpicyBite",
            "DragonScale", "NeonGhost", "SnackMonster", "GoldenViper",
            "Wormzilla", "SpeedyGons", "CandyCrush", "TitanBoa"
        )
    }

    val arenaCenter = Vec2(GameConfig.ARENA_CENTER_X, GameConfig.ARENA_CENTER_Y)
    var currentArenaRadius: Float = GameConfig.ARENA_RADIUS
        private set

    // Game Entities
    val bots = mutableListOf<Worm>()
    val foods = mutableListOf<FoodItem>()
    val potions = mutableListOf<PotionItem>()
    val particles = mutableListOf<GameParticle>()

    // Game loop state
    var isGameOver = false
        private set
    var isVictory = false
        private set
    var isPaused = false
    var coinsCollectedInSession = 0
        private set
    var gameTimeSeconds = 0f
        private set

    // Time Attack countdown
    var timeAttackRemaining: Float = 180f
        private set

    // Kill banner tracking
    var playerKillStreak = 0
        private set
    var recentKillEvent: KillEvent? = null
        private set
    private var killEventTimer = 0f

    // Extended 6-second invulnerability on spawn
    var spawnProtectionTimer: Float = 6.0f
        private set
    val isPlayerProtected: Boolean get() = spawnProtectionTimer > 0f

    // Smooth camera zoom (zoomed closer for clear visibility)
    var cameraZoom: Float = 1.30f
        private set

    private var hudUpdateTimer = 0f
    private var leaderboardTimer = 0f

    private val _leaderboard = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val leaderboard: StateFlow<List<LeaderboardEntry>> = _leaderboard.asStateFlow()

    private val _hudState = MutableStateFlow(EngineHudState())
    val hudState: StateFlow<EngineHudState> = _hudState.asStateFlow()

    private fun createInitialPlayer(): Worm {
        val baseSpeed = GameConfig.BASE_SPEED + (userData.speedLevel - 1) * 12f
        return Worm(
            id = "player",
            name = userData.playerName,
            isPlayer = true,
            head = arenaCenter,
            angle = 0f,
            targetAngle = 0f,
            speed = baseSpeed,
            skin = userData.equippedSkin,
            accessory = userData.equippedAccessory,
            mass = GameConfig.INITIAL_MASS
        ).apply {
            initializeBody()
        }
    }

    var player: Worm = createInitialPlayer()

    init {
        startNewGame(gameMode)
    }

    fun startNewGame(mode: GameMode = gameMode) {
        this.gameMode = mode
        isGameOver = false
        isVictory = false
        isPaused = false
        coinsCollectedInSession = 0
        gameTimeSeconds = 0f
        timeAttackRemaining = 180f
        playerKillStreak = 0
        recentKillEvent = null
        killEventTimer = 0f
        currentArenaRadius = GameConfig.ARENA_RADIUS
        spawnProtectionTimer = 6.0f
        cameraZoom = 1.0f
        foods.clear()
        potions.clear()
        bots.clear()
        particles.clear()

        // Create player
        player = createInitialPlayer()

        // Starting food clusters all around player
        repeat(80) {
            val offsetAngle = Random.nextFloat() * 2 * PI.toFloat()
            val dist = Random.nextFloat() * 450f + 50f
            val foodPos = player.head + Vec2(cos(offsetAngle) * dist, sin(offsetAngle) * dist)
            val type = FoodType.entries[it % FoodType.entries.size]
            foods.add(
                FoodItem(
                    id = idGen.incrementAndGet(),
                    position = foodPos,
                    type = type,
                    mass = type.massValue,
                    radius = type.baseRadius
                )
            )
        }

        // Spawn arena-wide food
        repeat(GameConfig.FOOD_COUNT_TARGET - 80) {
            spawnRandomFood()
        }

        // Spawn initial potions
        repeat(GameConfig.POTION_COUNT_TARGET) {
            spawnRandomPotion()
        }

        // Spawn AI Bots (in Battle Royale, spawn 24 initial bots, otherwise full BOT_COUNT_TARGET)
        val initialBotCount = if (gameMode == GameMode.BATTLE_ROYALE) 24 else GameConfig.BOT_COUNT_TARGET
        repeat(initialBotCount) {
            spawnBot()
        }

        updateLeaderboard()
        emitHudState()
    }

    fun calculateReviveCost(): Int {
        val base = Random.nextInt(15, 30)
        val sizeCost = (player.bodyPoints.size / 5) * Random.nextInt(4, 7)
        return (base + sizeCost).coerceIn(20, 250)
    }

    fun revivePlayer() {
        player.isAlive = true
        isGameOver = false
        spawnProtectionTimer = 6.0f
        // Safe placement near arena center
        val safeOffset = Vec2(Random.nextFloat() * 100f - 50f, Random.nextFloat() * 100f - 50f)
        player.head = arenaCenter + safeOffset
        player.bodyPoints.clear()
        player.initializeBody()
        soundManager.playReviveSound()
        emitHudState()
    }

    private fun randomArenaPoint(margin: Float = 150f): Vec2 {
        val angle = Random.nextFloat() * 2 * PI.toFloat()
        val r = sqrt(Random.nextFloat()) * (currentArenaRadius - margin).coerceAtLeast(100f)
        return Vec2(
            arenaCenter.x + cos(angle) * r,
            arenaCenter.y + sin(angle) * r
        )
    }

    private fun spawnRandomFood(nearPos: Vec2? = null, customType: FoodType? = null) {
        val pos = if (nearPos != null) {
            val offsetAngle = Random.nextFloat() * 2 * PI.toFloat()
            val dist = Random.nextFloat() * 80f
            nearPos + Vec2(cos(offsetAngle) * dist, sin(offsetAngle) * dist)
        } else {
            randomArenaPoint(80f)
        }

        val type = customType ?: if (Random.nextFloat() < 0.08f) {
            FoodType.GOLDEN_COIN
        } else {
            val allTypes = FoodType.entries.filter { it != FoodType.DEATH_FEAST && it != FoodType.GOLDEN_COIN }
            allTypes[Random.nextInt(allTypes.size)]
        }

        foods.add(
            FoodItem(
                id = idGen.incrementAndGet(),
                position = pos,
                type = type,
                mass = type.massValue,
                radius = type.baseRadius
            )
        )
    }

    private fun spawnDeadWormFood(exactPos: Vec2, customType: FoodType) {
        foods.add(
            FoodItem(
                id = idGen.incrementAndGet(),
                position = exactPos,
                type = customType,
                mass = customType.massValue,
                radius = customType.baseRadius,
                isDeathDrop = true
            )
        )
    }

    private fun spawnRandomPotion() {
        val pos = randomArenaPoint(200f)
        val allTypes = PotionType.entries
        val type = allTypes[Random.nextInt(allTypes.size)]
        potions.add(
            PotionItem(
                id = idGen.incrementAndGet(),
                position = pos,
                type = type
            )
        )
    }

    private fun spawnBot() {
        var spawnPos = randomArenaPoint(300f)
        var tries = 0
        while (tries < 30 && spawnPos.distanceSquaredTo(player.head) < 1300f * 1300f) {
            spawnPos = randomArenaPoint(300f)
            tries++
        }
        val botSkin = SkinId.entries[Random.nextInt(SkinId.entries.size)]
        val botAcc = FaceAccessory.entries[Random.nextInt(FaceAccessory.entries.size)]
        val botName = BOT_NAMES[Random.nextInt(BOT_NAMES.size)]
        val personality = BotPersonality.entries[Random.nextInt(BotPersonality.entries.size)]
        val startingMass = Random.nextFloat() * 30f + 20f

        val bot = Worm(
            id = "bot_${idGen.incrementAndGet()}",
            name = botName,
            isPlayer = false,
            head = spawnPos,
            angle = Random.nextFloat() * 2 * PI.toFloat(),
            targetAngle = Random.nextFloat() * 2 * PI.toFloat(),
            speed = GameConfig.BASE_SPEED,
            skin = botSkin,
            accessory = botAcc,
            mass = startingMass,
            aiPersonality = personality,
            aiTarget = randomArenaPoint(200f)
        ).apply {
            initializeBody()
        }
        bots.add(bot)
    }

    fun setPlayerTargetAngle(angle: Float) {
        if (!isGameOver) {
            player.targetAngle = angle
        }
    }

    fun setPlayerBoosting(boosting: Boolean) {
        if (!isGameOver) {
            player.isBoosting = boosting
        }
    }

    fun update(dt: Float) {
        if (isPaused || isGameOver) return
        gameTimeSeconds += dt

        // Kill Banner timer
        if (recentKillEvent != null) {
            killEventTimer -= dt
            if (killEventTimer <= 0f) {
                recentKillEvent = null
            }
        }

        // Mode: Time Attack handling
        if (gameMode == GameMode.TIME_ATTACK) {
            timeAttackRemaining = (timeAttackRemaining - dt).coerceAtLeast(0f)
            if (timeAttackRemaining <= 0f) {
                isGameOver = true
                isVictory = true
                soundManager.playVictorySound()
                emitHudState()
                return
            }
        }

        // Mode: Battle Royale shrinking safe zone
        if (gameMode == GameMode.BATTLE_ROYALE) {
            currentArenaRadius = maxOf(600f, GameConfig.ARENA_RADIUS - gameTimeSeconds * 12.5f)
            val aliveCount = bots.count { it.isAlive }
            if (aliveCount == 0 && player.isAlive) {
                isGameOver = true
                isVictory = true
                soundManager.playVictorySound()
                emitHudState()
                return
            }
        }

        if (spawnProtectionTimer > 0f) {
            spawnProtectionTimer = (spawnProtectionTimer - dt).coerceAtLeast(0f)
        }

        // Smooth camera zoom (brought closer to worm so it is not too far away)
        val hasZoomPotion = player.hasPotion(PotionType.ZOOM)
        val targetZoom = if (hasZoomPotion) 0.90f else (1.30f - (player.bodyPoints.size / 360f) * 0.35f).coerceIn(0.95f, 1.35f)
        cameraZoom += (targetZoom - cameraZoom) * (dt * 3.5f).coerceAtMost(1f)

        // 1. Update player
        updateWorm(player, dt)

        // 2. Update AI bots
        val botIterator = bots.iterator()
        while (botIterator.hasNext()) {
            val bot = botIterator.next()
            if (!bot.isAlive) {
                botIterator.remove()
                continue
            }
            updateAiDecision(bot, dt)
            updateWorm(bot, dt)
        }

        // Replenish bots (only in Open Arena & Time Attack, NOT in Battle Royale)
        if (gameMode != GameMode.BATTLE_ROYALE) {
            while (bots.size < GameConfig.BOT_COUNT_TARGET) {
                spawnBot()
            }
        }

        // 3. Collision with Arena Boundary
        checkBoundaryCollision(player)
        for (i in bots.indices) {
            checkBoundaryCollision(bots[i])
        }

        // 4. Worm-to-Worm Collision Check
        checkWormCollisions()

        // 5. Food & Potion Interactions
        updateFoodAndPotions(dt)

        // 6. Food & Potion Replenishment
        while (foods.size < GameConfig.FOOD_COUNT_TARGET) {
            spawnRandomFood()
        }
        while (potions.size < GameConfig.POTION_COUNT_TARGET) {
            spawnRandomPotion()
        }

        // 7. Update Particles
        val particleIterator = particles.iterator()
        while (particleIterator.hasNext()) {
            val p = particleIterator.next()
            p.update(dt)
            if (p.isDead) {
                particleIterator.remove()
            }
        }

        // 8. Timed updates
        hudUpdateTimer += dt
        if (hudUpdateTimer >= 0.1f) {
            hudUpdateTimer = 0f
            emitHudState()
        }

        leaderboardTimer += dt
        if (leaderboardTimer >= 0.5f) {
            leaderboardTimer = 0f
            updateLeaderboard()
        }
    }

    private fun emitHudState() {
        val botPositions = ArrayList<Vec2>(bots.size)
        for (i in bots.indices) {
            val b = bots[i]
            if (b.isAlive) botPositions.add(b.head)
        }
        val aliveCount = bots.count { it.isAlive }
        _hudState.value = EngineHudState(
            score = player.mass.toInt(),
            coins = coinsCollectedInSession,
            kills = player.kills,
            playerRank = getPlayerRank(),
            spawnProtectionRemaining = spawnProtectionTimer,
            activePotions = player.activePotions.toMap(),
            playerPos = player.head,
            botPositions = botPositions,
            gameMode = gameMode,
            difficulty = difficulty,
            timeAttackRemaining = timeAttackRemaining,
            aliveBotsCount = aliveCount,
            currentArenaRadius = currentArenaRadius,
            recentKillEvent = recentKillEvent
        )
    }

    private fun updateWorm(worm: Worm, dt: Float) {
        if (!worm.isAlive) return

        worm.updatePotions(dt)

        // Blinking
        worm.blinkTimer -= dt
        if (worm.blinkTimer <= 0f) {
            worm.isBlinking = !worm.isBlinking
            worm.blinkTimer = if (worm.isBlinking) 0.15f else Random.nextFloat() * 3f + 1.5f
        }

        // Speed calculation
        val hasSpeedPotion = worm.hasPotion(PotionType.SPEED_BOOST)
        var targetSpeed = if (hasSpeedPotion) {
            GameConfig.BOOST_SPEED * 1.15f
        } else if (worm.isBoosting && worm.mass > 20f) {
            GameConfig.BOOST_SPEED
        } else {
            GameConfig.BASE_SPEED
        }

        // Apply player speed level upgrade
        if (worm.isPlayer) {
            targetSpeed += (userData.speedLevel - 1) * 12f
        }

        worm.speed += (targetSpeed - worm.speed) * (dt * 10f).coerceAtMost(1f)

        // Boost mass consumption and trail drop
        if (worm.isBoosting && worm.mass > 25f) {
            val boostEfficiency = if (worm.isPlayer) (1.0f - (userData.boostLevel - 1) * 0.12f) else 1.0f
            val massLoss = 4f * dt * boostEfficiency
            worm.mass = (worm.mass - massLoss).coerceAtLeast(20f)

            worm.boostParticleTimer -= dt
            if (worm.boostParticleTimer <= 0f) {
                worm.boostParticleTimer = 0.08f
                val tailPos = worm.bodyPoints.lastOrNull() ?: worm.head
                if (Random.nextFloat() < 0.35f) {
                    spawnRandomFood(tailPos, FoodType.APPLE)
                }
                particles.add(
                    GameParticle(
                        position = tailPos,
                        velocity = Vec2(Random.nextFloat() * 40f - 20f, Random.nextFloat() * 40f - 20f),
                        color = worm.skin.primaryColor,
                        initialSize = 10f,
                        maxLifetime = 0.4f
                    )
                )
            }
        }

        // Steer smoothly towards target angle with turn speed influenced by difficulty
        val turnFactor = if (worm.isPlayer) 1.0f else difficulty.turnSpeedFactor
        val maxTurn = GameConfig.TURN_SPEED * turnFactor * dt
        worm.steerTowards(worm.targetAngle, maxTurn)

        // Move head
        val velocity = Vec2.fromAngle(worm.angle, worm.speed * dt)
        worm.head = worm.head + velocity

        // Follow body segments
        var prevPoint = worm.head
        for (i in worm.bodyPoints.indices) {
            val current = worm.bodyPoints[i]
            val diff = current - prevPoint
            val dist = diff.length()
            val desiredDist = GameConfig.SEGMENT_SPACING

            if (dist > 0.001f) {
                worm.bodyPoints[i] = prevPoint + diff.normalized() * desiredDist
            }
            prevPoint = worm.bodyPoints[i]
        }

        // Adjust length according to mass
        val desiredLength = worm.targetLength
        if (worm.bodyPoints.size < desiredLength) {
            val last = worm.bodyPoints.lastOrNull() ?: worm.head
            val dir = Vec2.fromAngle(worm.angle + PI.toFloat(), GameConfig.SEGMENT_SPACING)
            worm.bodyPoints.add(last + dir)
        } else if (worm.bodyPoints.size > desiredLength && worm.bodyPoints.size > 10) {
            worm.bodyPoints.removeAt(worm.bodyPoints.lastIndex)
        }
    }

    private fun updateAiDecision(bot: Worm, dt: Float) {
        bot.aiDecisionCooldown -= dt
        if (bot.aiDecisionCooldown <= 0f) {
            // Decision reaction cooldown based on difficulty
            val cdSpan = (difficulty.decisionCooldownMax - difficulty.decisionCooldownMin).coerceAtLeast(0.04f)
            bot.aiDecisionCooldown = difficulty.decisionCooldownMin + Random.nextFloat() * cdSpan

            // Danger detection: avoid arena border
            val distToCenterSq = bot.head.distanceSquaredTo(arenaCenter)
            val borderDangerDist = currentArenaRadius - (if (difficulty == GameDifficulty.HARD) 360f else 280f)
            if (distToCenterSq > borderDangerDist * borderDangerDist) {
                val toCenter = (arenaCenter - bot.head).angle()
                bot.targetAngle = toCenter
                bot.isBoosting = difficulty == GameDifficulty.HARD
                return
            }

            // Danger detection: avoid shielded/invulnerable player
            if (player.isAlive && isPlayerProtected && bot.head.distanceSquaredTo(player.head) < 450f * 450f) {
                bot.targetAngle = (bot.head - player.head).angle()
                bot.isBoosting = false
                return
            }

            // Danger detection: avoid nearby worm bodies
            var avoidAngle: Float? = null
            val lookDist = difficulty.lookAheadDist
            val lookAhead = bot.head + Vec2.fromAngle(bot.angle, lookDist)
            val avoidDist = bot.segmentRadius + (if (difficulty == GameDifficulty.HARD) 55f else 38f)
            val avoidDistSq = avoidDist * avoidDist

            // Multi-ray lookahead for Hard difficulty to avoid getting trapped
            val checkPoints = if (difficulty == GameDifficulty.HARD) {
                listOf(
                    lookAhead,
                    bot.head + Vec2.fromAngle(bot.angle - 0.45f, lookDist * 0.85f),
                    bot.head + Vec2.fromAngle(bot.angle + 0.45f, lookDist * 0.85f)
                )
            } else {
                listOf(lookAhead)
            }

            // Check against player body
            if (player.isAlive && player.id != bot.id) {
                for (pt in checkPoints) {
                    for (seg in player.bodyPoints) {
                        if (pt.distanceSquaredTo(seg) < avoidDistSq) {
                            avoidAngle = (bot.head - seg).angle()
                            break
                        }
                    }
                    if (avoidAngle != null) break
                }
            }

            // Check against other bots
            if (avoidAngle == null) {
                for (i in bots.indices) {
                    val other = bots[i]
                    if (other.id == bot.id || !other.isAlive) continue
                    for (pt in checkPoints) {
                        for (seg in other.bodyPoints) {
                            if (pt.distanceSquaredTo(seg) < avoidDistSq) {
                                avoidAngle = (bot.head - seg).angle()
                                break
                            }
                        }
                        if (avoidAngle != null) break
                    }
                    if (avoidAngle != null) break
                }
            }

            if (avoidAngle != null) {
                bot.targetAngle = avoidAngle
                bot.isBoosting = difficulty != GameDifficulty.EASY // Easy bots don't emergency boost away
                return
            }

            // Behavior goal selection
            when (bot.aiPersonality) {
                BotPersonality.HUNTER -> {
                    val minGameTime = if (difficulty == GameDifficulty.HARD) 3.0f else 8.0f
                    val massThreshold = if (difficulty == GameDifficulty.HARD) 1.1f else (if (difficulty == GameDifficulty.NORMAL) 1.3f else 1.8f)
                    val huntDistSq = if (difficulty == GameDifficulty.HARD) 550f * 550f else 400f * 400f
                    val canHuntPlayer = player.isAlive && !isPlayerProtected && gameTimeSeconds > minGameTime &&
                            player.mass < bot.mass * massThreshold && bot.head.distanceSquaredTo(player.head) < huntDistSq

                    if (canHuntPlayer) {
                        // Encirclement tactic for Hard mode when bot has superior mass
                        if (difficulty == GameDifficulty.HARD && bot.mass > player.mass * 1.3f && bot.head.distanceSquaredTo(player.head) < 320f * 320f) {
                            val toPlayer = player.head - bot.head
                            val tangent = Vec2(-toPlayer.y, toPlayer.x).normalized()
                            val coilTarget = player.head + tangent * (player.segmentRadius * 3.5f)
                            bot.targetAngle = (coilTarget - bot.head).angle()
                            bot.isBoosting = true
                            return
                        }

                        // Predictive intercept cutting off player path
                        val leadDist = difficulty.huntLeadingDist
                        val intercept = player.head + Vec2.fromAngle(player.angle, leadDist)
                        bot.targetAngle = (intercept - bot.head).angle()
                        bot.isBoosting = (difficulty != GameDifficulty.EASY) && bot.head.distanceSquaredTo(player.head) < 240f * 240f
                        return
                    } else {
                        var bestBot: Worm? = null
                        var bestDistSq = 450f * 450f
                        for (i in bots.indices) {
                            val other = bots[i]
                            if (other.id != bot.id && other.isAlive && other.mass < bot.mass) {
                                val dSq = other.head.distanceSquaredTo(bot.head)
                                if (dSq < bestDistSq) {
                                    bestDistSq = dSq
                                    bestBot = other
                                }
                            }
                        }
                        if (bestBot != null) {
                            val intercept = bestBot.head + Vec2.fromAngle(bestBot.angle, difficulty.huntLeadingDist * 0.8f)
                            bot.targetAngle = (intercept - bot.head).angle()
                            bot.isBoosting = (difficulty != GameDifficulty.EASY) && bestDistSq < 200f * 200f
                            return
                        }
                    }
                }
                BotPersonality.SCAVENGER -> {
                    var feastPos: Vec2? = null
                    var feastDistSq = 600f * 600f
                    for (i in foods.indices) {
                        val f = foods[i]
                        if (f.type == FoodType.DEATH_FEAST || f.type == FoodType.BURGER) {
                            val dSq = bot.head.distanceSquaredTo(f.position)
                            if (dSq < feastDistSq) {
                                feastDistSq = dSq
                                feastPos = f.position
                            }
                        }
                    }
                    if (feastPos != null) {
                        bot.targetAngle = (feastPos - bot.head).angle()
                        bot.isBoosting = difficulty != GameDifficulty.EASY
                        return
                    }
                }
                BotPersonality.GREEDY -> {
                    var potionPos: Vec2? = null
                    var potionDistSq = 500f * 500f
                    for (i in potions.indices) {
                        val p = potions[i]
                        val dSq = bot.head.distanceSquaredTo(p.position)
                        if (dSq < potionDistSq) {
                            potionDistSq = dSq
                            potionPos = p.position
                        }
                    }
                    if (potionPos != null) {
                        bot.targetAngle = (potionPos - bot.head).angle()
                        return
                    }
                }
                BotPersonality.WANDERER -> {}
            }

            // Default: find closest food
            var closestFoodPos: Vec2? = null
            var closestDistSq = 350f * 350f
            for (i in foods.indices) {
                val f = foods[i]
                val dSq = bot.head.distanceSquaredTo(f.position)
                if (dSq < closestDistSq) {
                    closestDistSq = dSq
                    closestFoodPos = f.position
                }
            }

            if (closestFoodPos != null) {
                bot.targetAngle = (closestFoodPos - bot.head).angle()
                bot.isBoosting = false
            } else {
                bot.targetAngle += (Random.nextFloat() * 1.2f - 0.6f)
                bot.isBoosting = false
            }
        }
    }

    private fun checkBoundaryCollision(worm: Worm) {
        if (!worm.isAlive) return
        val distToCenterSq = worm.head.distanceSquaredTo(arenaCenter)
        val maxDist = currentArenaRadius - worm.segmentRadius * 0.5f
        if (distToCenterSq >= maxDist * maxDist) {
            if (worm.isPlayer && isPlayerProtected) {
                val toCenter = (arenaCenter - worm.head).normalized()
                worm.head = arenaCenter + (worm.head - arenaCenter).normalized() * (maxDist - 20f)
                worm.angle = toCenter.angle()
                worm.targetAngle = worm.angle
                return
            }
            killWorm(worm, killer = null)
        }
    }

    private fun checkWormCollisions() {
        val totalBots = bots.size

        // 1. Check Player against all bots (Only if player is NOT protected)
        if (player.isAlive && !isPlayerProtected) {
            val headP = player.head
            val radiusP = player.segmentRadius

            for (bIdx in 0 until totalBots) {
                val bot = bots[bIdx]
                if (!bot.isAlive) continue

                val radiusB = bot.segmentRadius
                val hitDistance = (radiusP + radiusB) * 0.72f
                val hitDistanceSq = hitDistance * hitDistance

                val bSegments = bot.bodyPoints
                val count = bSegments.size
                for (sIdx in 1 until count) {
                    if (headP.distanceSquaredTo(bSegments[sIdx]) < hitDistanceSq) {
                        killWorm(player, killer = bot)
                        break
                    }
                }
                if (!player.isAlive) break
            }
        }

        // 2. Check each bot against player and against other bots
        for (aIdx in 0 until totalBots) {
            val botA = bots[aIdx]
            if (!botA.isAlive) continue

            val headA = botA.head
            val radiusA = botA.segmentRadius

            // Check Bot A hitting Player's body
            if (player.isAlive && !isPlayerProtected) {
                val radiusP = player.segmentRadius
                val hitDist = (radiusA + radiusP) * 0.72f
                val hitDistSq = hitDist * hitDist
                val pSegments = player.bodyPoints
                for (sIdx in 1 until pSegments.size) {
                    if (headA.distanceSquaredTo(pSegments[sIdx]) < hitDistSq) {
                        killWorm(botA, killer = player)
                        break
                    }
                }
            }

            if (!botA.isAlive) continue

            // Check Bot A hitting other Bots' bodies
            for (bIdx in 0 until totalBots) {
                if (aIdx == bIdx) continue
                val botB = bots[bIdx]
                if (!botB.isAlive) continue

                val radiusB = botB.segmentRadius
                val hitDist = (radiusA + radiusB) * 0.72f
                val hitDistSq = hitDist * hitDist
                val bSegments = botB.bodyPoints
                for (sIdx in 1 until bSegments.size) {
                    if (headA.distanceSquaredTo(bSegments[sIdx]) < hitDistSq) {
                        killWorm(botA, killer = botB)
                        break
                    }
                }
                if (!botA.isAlive) break
            }
        }
    }

    private fun killWorm(victim: Worm, killer: Worm?) {
        if (!victim.isAlive) return
        if (victim.isPlayer && (isPlayerProtected || gameTimeSeconds < 5.0f)) return

        victim.isAlive = false

        if (killer != null && killer.isAlive) {
            killer.kills++
            killer.mass += 15f
            if (killer.isPlayer) {
                soundManager.playKillSound()
                playerKillStreak++
                val bonusCoins = Random.nextInt(15, 35)
                coinsCollectedInSession += bonusCoins
                recentKillEvent = KillEvent(
                    victimName = victim.name,
                    streak = playerKillStreak,
                    coinsEarned = bonusCoins
                )
                killEventTimer = 2.8f
            }
        }

        // Drop food feast precisely along former body points without scattering (gak buyar)
        val step = maxOf(1, victim.bodyPoints.size / 40)
        // Also spawn at head position
        spawnDeadWormFood(victim.head, FoodType.DEATH_FEAST)

        for (i in victim.bodyPoints.indices step step) {
            val pt = victim.bodyPoints[i]
            val foodType = when {
                i % 3 == 0 -> FoodType.DEATH_FEAST
                Random.nextFloat() < 0.25f -> FoodType.GOLDEN_COIN
                Random.nextFloat() < 0.55f -> FoodType.BURGER
                else -> FoodType.DONUT
            }
            // Spawn EXACTLY on the segment point so the dead worm's shape is preserved (gak buyar)
            spawnDeadWormFood(pt, foodType)

            // Radiant shimmering light particles floating softly up from the food drops
            particles.add(
                GameParticle(
                    position = pt,
                    velocity = Vec2(Random.nextFloat() * 20f - 10f, -Random.nextFloat() * 45f - 15f),
                    color = Color(0xFFFFF59D), // Warm glowing sunlight / sparkle
                    initialSize = 10f,
                    maxLifetime = 0.9f
                )
            )
        }

        if (victim.isPlayer) {
            isGameOver = true
            soundManager.playGameOverSound()
        }

        updateLeaderboard()
        emitHudState()
    }

    private fun updateFoodAndPotions(dt: Float) {
        val playerHead = player.head
        val hasMagnet = player.hasPotion(PotionType.MAGNET)
        val magnetRadius = if (hasMagnet) {
            GameConfig.MAGNET_BASE_RADIUS + (userData.magnetLevel - 1) * 35f
        } else {
            50f + (userData.magnetLevel - 1) * 12f
        }

        val multiplierActive = player.hasPotion(PotionType.MULTIPLIER)
        var scoreMultiplier = if (multiplierActive) (2.0f + (userData.multiplierLevel - 1) * 0.25f) else 1.0f
        // Bonus 2x score in Time Attack!
        if (gameMode == GameMode.TIME_ATTACK) {
            scoreMultiplier *= 2.0f
        }

        val foodIterator = foods.iterator()
        while (foodIterator.hasNext()) {
            val food = foodIterator.next()

            if (player.isAlive) {
                val dist = playerHead.distanceTo(food.position)
                if (dist < magnetRadius) {
                    val dir = (playerHead - food.position).normalized()
                    val pullSpeed = (1f - dist / magnetRadius) * 450f + 120f
                    food.position = food.position + dir * (pullSpeed * dt)
                }

                if (dist < player.segmentRadius + food.radius + 6f) {
                    foodIterator.remove()
                    val massGained = food.mass * scoreMultiplier
                    player.mass += massGained
                    player.mouthOpen = 1f

                    if (food.type == FoodType.GOLDEN_COIN) {
                        coinsCollectedInSession += 2
                    }

                    soundManager.playEatSound()

                    particles.add(
                        GameParticle(
                            position = food.position,
                            velocity = Vec2(Random.nextFloat() * 50f - 25f, Random.nextFloat() * 50f - 25f),
                            color = food.type.primaryColor,
                            initialSize = 8f,
                            maxLifetime = 0.3f
                        )
                    )
                    continue
                }
            }

            for (i in bots.indices) {
                val bot = bots[i]
                if (!bot.isAlive) continue
                val dist = bot.head.distanceTo(food.position)
                if (dist < bot.segmentRadius + food.radius + 6f) {
                    foodIterator.remove()
                    bot.mass += food.mass
                    bot.mouthOpen = 1f
                    break
                }
            }
        }

        // Potion pickups
        val potionIterator = potions.iterator()
        while (potionIterator.hasNext()) {
            val potion = potionIterator.next()
            potion.pulsePhase += dt * 3f

            if (player.isAlive && playerHead.distanceTo(potion.position) < player.segmentRadius + potion.radius + 10f) {
                potionIterator.remove()
                val duration = potion.type.defaultDuration
                player.addPotion(potion.type, duration)
                soundManager.playPotionSound()

                repeat(12) {
                    particles.add(
                        GameParticle(
                            position = potion.position,
                            velocity = Vec2(Random.nextFloat() * 140f - 70f, Random.nextFloat() * 140f - 70f),
                            color = potion.type.color,
                            initialSize = 12f,
                            maxLifetime = 0.6f
                        )
                    )
                }
                continue
            }

            for (i in bots.indices) {
                val bot = bots[i]
                if (!bot.isAlive) continue
                if (bot.head.distanceTo(potion.position) < bot.segmentRadius + potion.radius + 10f) {
                    potionIterator.remove()
                    bot.addPotion(potion.type, potion.type.defaultDuration)
                    break
                }
            }
        }

        if (player.mouthOpen > 0f) {
            player.mouthOpen = (player.mouthOpen - dt * 2.5f).coerceAtLeast(0f)
        }
        for (i in bots.indices) {
            val bot = bots[i]
            if (bot.mouthOpen > 0f) {
                bot.mouthOpen = (bot.mouthOpen - dt * 2.5f).coerceAtLeast(0f)
            }
        }
    }

    private fun updateLeaderboard() {
        val list = mutableListOf<Worm>()
        if (player.isAlive || isGameOver) list.add(player)
        for (i in bots.indices) {
            val b = bots[i]
            if (b.isAlive) list.add(b)
        }
        list.sortByDescending { it.mass }

        val entries = list.take(10).mapIndexed { idx, worm ->
            LeaderboardEntry(
                name = worm.name,
                score = worm.mass.toInt(),
                isPlayer = worm.isPlayer,
                rank = idx + 1
            )
        }
        _leaderboard.value = entries
    }

    fun getPlayerRank(): Int {
        val list = mutableListOf<Worm>()
        list.add(player)
        for (i in bots.indices) {
            val b = bots[i]
            if (b.isAlive) list.add(b)
        }
        list.sortByDescending { it.mass }
        val idx = list.indexOfFirst { it.isPlayer }
        return if (idx >= 0) idx + 1 else list.size
    }
}
