package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

class GameSoundManager(private val context: Context) {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    var soundEnabled: Boolean = true
    var vibrationEnabled: Boolean = true
    var soundVolumeFactor: Float = 0.8f

    fun setSoundVolume(volumeInt: Int) {
        soundVolumeFactor = (volumeInt / 10f).coerceIn(0f, 1f)
        soundEnabled = volumeInt > 0
    }

    private val sampleRate = 22050

    // Synthesize simple pleasant waveform PCM buffer
    private fun playTone(frequencies: FloatArray, durationMs: Int, volume: Float = 0.4f) {
        if (!soundEnabled || soundVolumeFactor <= 0f) return
        val finalVol = volume * soundVolumeFactor
        scope.launch {
            try {
                val totalSamples = (sampleRate * (durationMs / 1000f)).toInt().coerceAtLeast(1)
                val buffer = ShortArray(totalSamples)
                val freqSteps = frequencies.size

                for (i in 0 until totalSamples) {
                    val progress = i.toFloat() / totalSamples
                    val freqIdx = (progress * freqSteps).toInt().coerceIn(0, freqSteps - 1)
                    val freq = frequencies[freqIdx]

                    // Envelope: attack and smooth decay
                    val envelope = (1f - progress) * (progress * 8f).coerceAtMost(1f)
                    val sample = sin(2.0 * PI * i * freq / sampleRate) * envelope * finalVol * Short.MAX_VALUE
                    buffer[i] = sample.toInt().toShort()
                }

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(buffer, 0, buffer.size)
                audioTrack.play()
                // Release after played
                audioTrack.setNotificationMarkerPosition(totalSamples)
                audioTrack.setPlaybackPositionUpdateListener(object : AudioTrack.OnPlaybackPositionUpdateListener {
                    override fun onMarkerReached(track: AudioTrack?) {
                        track?.release()
                    }
                    override fun onPeriodicNotification(track: AudioTrack?) {}
                })
            } catch (_: Exception) {
                // Audio synthesis fallback
            }
        }
    }

    fun playEatSound() {
        playTone(floatArrayOf(587.33f, 880.0f), 70, 0.25f)
        vibrate(15)
    }

    fun playPotionSound() {
        playTone(floatArrayOf(440f, 554.37f, 659.25f, 880f), 220, 0.35f)
        vibrate(40)
    }

    fun playKillSound() {
        playTone(floatArrayOf(300f, 450f, 600f, 900f), 280, 0.45f)
        vibrate(75)
    }

    fun playGameOverSound() {
        playTone(floatArrayOf(440f, 370f, 311f, 220f), 450, 0.45f)
        vibrate(120)
    }

    fun playReviveSound() {
        playTone(floatArrayOf(330f, 440f, 550f, 660f, 880f), 350, 0.45f)
        vibrate(90)
    }

    fun playVictorySound() {
        playTone(floatArrayOf(523f, 659f, 784f, 1046f), 500, 0.5f)
        vibrate(100)
    }

    fun playClickSound() {
        playTone(floatArrayOf(800f, 1200f), 45, 0.25f)
        vibrate(10)
    }

    fun playCoinSound() {
        playTone(floatArrayOf(987.77f, 1318.51f), 120, 0.40f)
        vibrate(25)
    }

    fun vibrate(durationMs: Long) {
        if (!vibrationEnabled || vibrator == null || !vibrator.hasVibrator()) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(durationMs)
            }
        } catch (_: Exception) {}
    }

    // Upbeat In-Game Background Music Player
    private var bgmJob: kotlinx.coroutines.Job? = null
    @Volatile
    private var isBgmRunning = false
    private var bgmAudioTrack: AudioTrack? = null

    fun startBgm() {
        if (isBgmRunning || !soundEnabled || soundVolumeFactor <= 0f) return
        isBgmRunning = true
        bgmJob = scope.launch {
            try {
                val bgmSampleRate = 22050
                val minBufSize = AudioTrack.getMinBufferSize(
                    bgmSampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(bgmSampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(minBufSize.coerceAtLeast(4096) * 2)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                bgmAudioTrack = track
                track.play()

                // Joyful retro-arcade worm IO game tune in C major & A minor pentatonic
                val melody = floatArrayOf(
                    261.63f, 329.63f, 392.00f, 523.25f, 392.00f, 523.25f, 659.25f, 523.25f,
                    220.00f, 261.63f, 329.63f, 440.00f, 329.63f, 440.00f, 523.25f, 440.00f,
                    174.61f, 220.00f, 261.63f, 349.23f, 261.63f, 349.23f, 440.00f, 349.23f,
                    196.00f, 246.94f, 293.66f, 392.00f, 293.66f, 392.00f, 493.88f, 392.00f
                )
                val bass = floatArrayOf(
                    130.81f, 130.81f, 130.81f, 130.81f,
                    110.00f, 110.00f, 110.00f, 110.00f,
                    87.31f, 87.31f, 87.31f, 87.31f,
                    98.00f, 98.00f, 98.00f, 98.00f
                )
                val stepDurationMs = 145
                val samplesPerStep = (bgmSampleRate * (stepDurationMs / 1000f)).toInt().coerceAtLeast(100)
                val stepBuf = ShortArray(samplesPerStep)
                var stepIndex = 0

                while (isBgmRunning) {
                    val mFreq = melody[stepIndex % melody.size]
                    val bFreq = bass[(stepIndex / 2) % bass.size]
                    val vol = 0.16f * soundVolumeFactor

                    for (i in 0 until samplesPerStep) {
                        val t = i.toFloat() / samplesPerStep
                        // Smooth envelope to avoid clicks
                        val env = (sin(PI * t)).toFloat()
                        val mWave = sin(2.0 * PI * i * mFreq / bgmSampleRate) * 0.65
                        val bWave = sin(2.0 * PI * i * bFreq / bgmSampleRate) * 0.45
                        val sample = (mWave + bWave) * env * vol * Short.MAX_VALUE
                        stepBuf[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                    }

                    track.write(stepBuf, 0, stepBuf.size)
                    stepIndex++
                }

                track.stop()
                track.release()
                bgmAudioTrack = null
            } catch (_: Exception) {
                try {
                    bgmAudioTrack?.release()
                } catch (_: Exception) {}
                bgmAudioTrack = null
            }
        }
    }

    fun pauseBgm() {
        isBgmRunning = false
        bgmJob?.cancel()
        bgmJob = null
        try {
            bgmAudioTrack?.pause()
            bgmAudioTrack?.flush()
        } catch (_: Exception) {}
    }

    fun stopBgm() {
        isBgmRunning = false
        bgmJob?.cancel()
        bgmJob = null
        try {
            bgmAudioTrack?.stop()
            bgmAudioTrack?.release()
            bgmAudioTrack = null
        } catch (_: Exception) {}
    }
}
