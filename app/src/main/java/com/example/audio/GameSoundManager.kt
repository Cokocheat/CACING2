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

    private val sampleRate = 22050

    // Synthesize simple pleasant waveform PCM buffer
    private fun playTone(frequencies: FloatArray, durationMs: Int, volume: Float = 0.4f) {
        if (!soundEnabled) return
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
                    val sample = sin(2.0 * PI * i * freq / sampleRate) * envelope * volume * Short.MAX_VALUE
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
}
