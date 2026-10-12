package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.UserPreferencesRepository
import com.example.model.GameConfig
import com.example.model.GameMode
import com.example.model.SkinId
import com.example.model.Vec2
import com.example.model.Worm
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context verifies nana tayantuh app name`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("nana tayantuh", appName)
    }

    @Test
    fun `verify vec2 math operations`() {
        val v1 = Vec2(3f, 4f)
        assertEquals(5f, v1.length(), 0.001f)

        val v2 = Vec2(1f, 2f)
        val vSum = v1 + v2
        assertEquals(4f, vSum.x, 0.001f)
        assertEquals(6f, vSum.y, 0.001f)

        val norm = v1.normalized()
        assertEquals(1f, norm.length(), 0.001f)
    }

    @Test
    fun `verify worm initialization and growth`() {
        val worm = Worm(
            id = "test_worm",
            name = "TestPlayer",
            isPlayer = true,
            head = Vec2(100f, 100f),
            mass = GameConfig.INITIAL_MASS,
            skin = SkinId.CLASSIC_GREEN
        )
        worm.initializeBody()

        assertEquals(GameConfig.INITIAL_SEGMENTS, worm.bodyPoints.size)
        assertTrue(worm.segmentRadius > 0f)

        // Simulate eating food
        worm.mass += 50f
        assertTrue(worm.targetLength > GameConfig.INITIAL_SEGMENTS)
    }

    @Test
    fun `verify user preferences repository saves game progress`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = UserPreferencesRepository(context)

        val initialData = repo.userData.value
        assertNotNull(initialData)

        repo.recordGameResult(score = 250, kills = 3, length = 35, coinsEarned = 20)
        val updated = repo.userData.value

        assertTrue(updated.highScore >= 250)
        assertTrue(updated.totalKills >= 3)
        assertTrue(updated.longestLength >= 35)
    }

    @Test
    fun `verify daily 7 days login claim mechanism`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = UserPreferencesRepository(context)

        // On first install, player can claim day 1 immediately
        assertTrue(repo.userData.value.canClaimDailyToday)
        assertEquals(1, repo.userData.value.nextDailyDay)

        val coinsBefore = repo.userData.value.coins
        val claimResult = repo.claimDailyReward()

        assertNotNull(claimResult)
        assertEquals(1, claimResult?.first)
        assertTrue((claimResult?.second ?: 0) > 0)
        assertEquals(coinsBefore + (claimResult?.second ?: 0), repo.userData.value.coins)

        // Cannot claim again immediately (must wait 1 day)
        assertFalse(repo.userData.value.canClaimDailyToday)
        assertTrue(repo.userData.value.millisUntilNextClaim > 0)
    }

    @Test
    fun `verify customizable joystick and boost positions`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = UserPreferencesRepository(context)

        repo.updateJoystickSettings(offsetX = 55, offsetY = 60, scale = 1.2f)
        repo.updateBoostSettings(offsetX = 50, offsetY = 65, scale = 1.15f)

        assertEquals(55, repo.userData.value.joystickOffsetX)
        assertEquals(60, repo.userData.value.joystickOffsetY)
        assertEquals(1.2f, repo.userData.value.joystickScale, 0.01f)
        assertEquals(50, repo.userData.value.boostOffsetX)
        assertEquals(65, repo.userData.value.boostOffsetY)
        assertEquals(1.15f, repo.userData.value.boostScale, 0.01f)

        // Reset
        repo.resetControlSettings()
        assertEquals(36, repo.userData.value.joystickOffsetX)
        assertEquals(36, repo.userData.value.joystickOffsetY)
    }

    @Test
    fun `verify game modes definitions`() {
        assertEquals("Arena Luar", GameMode.OPEN_ARENA.title)
        assertEquals("Time Attack", GameMode.TIME_ATTACK.title)
        assertEquals("Battle Royale", GameMode.BATTLE_ROYALE.title)
    }
}
