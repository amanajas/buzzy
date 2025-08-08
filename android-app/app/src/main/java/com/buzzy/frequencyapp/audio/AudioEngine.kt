package com.buzzy.frequencyapp.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.*
import kotlin.math.PI
import kotlin.math.sin

class AudioEngine {
    private var audioTrack: AudioTrack? = null
    private var isPlaying = false
    private var playbackJob: Job? = null
    private val audioScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    var leftFrequency = 440f
    var rightFrequency = 440f
    var leftVolume = 0.1f
    var rightVolume = 0.1f
    var waveType = WaveType.SINE
    
    private val sampleRate = 44100
    private val bufferSize = AudioTrack.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_OUT_STEREO,
        AudioFormat.ENCODING_PCM_16BIT
    )
    
    enum class WaveType {
        SINE, SQUARE, SAWTOOTH, TRIANGLE
    }
    
    fun startAudio() {
        if (isPlaying) return
        
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
            
        val format = AudioFormat.Builder()
            .setSampleRate(sampleRate)
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
            .build()
            
        audioTrack = AudioTrack(
            attributes,
            format,
            bufferSize * 2,
            AudioTrack.MODE_STREAM,
            0
        )
        
        audioTrack?.play()
        isPlaying = true
        
        playbackJob = audioScope.launch {
            generateAndPlayAudio()
        }
    }
    
    fun stopAudio() {
        isPlaying = false
        
        // Cancel the job and wait for it to complete
        runBlocking {
            playbackJob?.cancelAndJoin()
        }
        playbackJob = null
        
        audioTrack?.apply {
            try {
                stop()
                flush()
                release()
            } catch (e: Exception) {
                // Ignore exceptions during cleanup
            }
        }
        audioTrack = null
    }
    
    private suspend fun generateAndPlayAudio() {
        val buffer = ShortArray(bufferSize)
        var leftPhase = 0.0
        var rightPhase = 0.0
        
        while (isPlaying && audioTrack != null) {
            for (i in buffer.indices step 2) {
                // Left channel
                val leftSample = generateWave(leftPhase, waveType) * leftVolume * Short.MAX_VALUE
                buffer[i] = leftSample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                leftPhase += 2.0 * PI * leftFrequency / sampleRate
                if (leftPhase > 2.0 * PI) leftPhase -= 2.0 * PI
                
                // Right channel
                val rightSample = generateWave(rightPhase, waveType) * rightVolume * Short.MAX_VALUE
                buffer[i + 1] = rightSample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                rightPhase += 2.0 * PI * rightFrequency / sampleRate
                if (rightPhase > 2.0 * PI) rightPhase -= 2.0 * PI
            }
            
            // Safety check before writing
            val track = audioTrack
            if (track != null && isPlaying) {
                try {
                    track.write(buffer, 0, buffer.size)
                } catch (e: IllegalStateException) {
                    // AudioTrack has been released, stop playback
                    break
                }
            }
            yield()
        }
    }
    
    private fun generateWave(phase: Double, type: WaveType): Double {
        return when (type) {
            WaveType.SINE -> sin(phase)
            WaveType.SQUARE -> if (phase < PI) 1.0 else -1.0
            WaveType.SAWTOOTH -> 2.0 * (phase / (2.0 * PI)) - 1.0
            WaveType.TRIANGLE -> {
                val normalized = phase / (2.0 * PI)
                if (normalized < 0.5) {
                    4.0 * normalized - 1.0
                } else {
                    -4.0 * normalized + 3.0
                }
            }
        }
    }
    
    fun updateFrequency(channel: Channel, frequency: Float) {
        when (channel) {
            Channel.LEFT -> leftFrequency = frequency
            Channel.RIGHT -> rightFrequency = frequency
            Channel.BOTH -> {
                leftFrequency = frequency
                rightFrequency = frequency
            }
        }
    }
    
    fun updateVolume(channel: Channel, volume: Float) {
        val clampedVolume = volume.coerceIn(0f, 0.3f)
        when (channel) {
            Channel.LEFT -> leftVolume = clampedVolume
            Channel.RIGHT -> rightVolume = clampedVolume
            Channel.BOTH -> {
                leftVolume = clampedVolume
                rightVolume = clampedVolume
            }
        }
    }
    
    fun updateWaveType(newWaveType: WaveType) {
        // Update wave type without stopping audio
        // The audio generation loop will pick up the new wave type automatically
        waveType = newWaveType
    }
    
    fun applyFrequency(frequency: Float, channel: Channel = Channel.BOTH) {
        updateFrequency(channel, frequency)
    }
    
    fun applyBinauralBeat(leftFreq: Float, rightFreq: Float) {
        leftFrequency = leftFreq
        rightFrequency = rightFreq
    }
    
    enum class Channel {
        LEFT, RIGHT, BOTH
    }
    
    fun release() {
        stopAudio()
        audioScope.cancel()
    }
}