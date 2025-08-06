import AVFoundation
import Foundation

class AudioEngine: ObservableObject {
    private var audioEngine = AVAudioEngine()
    private var leftToneNode: AVAudioPlayerNode?
    private var rightToneNode: AVAudioPlayerNode?
    private var leftBuffer: AVAudioPCMBuffer?
    private var rightBuffer: AVAudioPCMBuffer?
    
    @Published var isPlaying = false
    @Published var leftFrequency: Float = 440.0
    @Published var rightFrequency: Float = 440.0
    @Published var leftVolume: Float = 0.1
    @Published var rightVolume: Float = 0.1
    @Published var waveType: WaveType = .sine
    
    enum WaveType: String, CaseIterable {
        case sine = "Sine"
        case square = "Square"
        case sawtooth = "Sawtooth"
        case triangle = "Triangle"
    }
    
    enum Channel {
        case left, right, both
    }
    
    init() {
        setupAudioSession()
    }
    
    private func setupAudioSession() {
        do {
            let audioSession = AVAudioSession.sharedInstance()
            try audioSession.setCategory(.playback, mode: .default)
            try audioSession.setActive(true)
        } catch {
            print("Failed to setup audio session: \(error)")
        }
    }
    
    func startAudio() {
        guard !isPlaying else { return }
        
        leftToneNode = AVAudioPlayerNode()
        rightToneNode = AVAudioPlayerNode()
        
        guard let leftNode = leftToneNode,
              let rightNode = rightToneNode else { return }
        
        audioEngine.attach(leftNode)
        audioEngine.attach(rightNode)
        
        let format = audioEngine.mainMixerNode.outputFormat(forBus: 0)
        
        // Connect nodes with stereo panning
        let leftMixer = AVAudioMixerNode()
        let rightMixer = AVAudioMixerNode()
        
        audioEngine.attach(leftMixer)
        audioEngine.attach(rightMixer)
        
        audioEngine.connect(leftNode, to: leftMixer, format: format)
        audioEngine.connect(rightNode, to: rightMixer, format: format)
        
        audioEngine.connect(leftMixer, to: audioEngine.mainMixerNode, format: format)
        audioEngine.connect(rightMixer, to: audioEngine.mainMixerNode, format: format)
        
        // Set pan and volume
        leftMixer.pan = -1.0
        rightMixer.pan = 1.0
        leftMixer.volume = leftVolume
        rightMixer.volume = rightVolume
        
        // Generate and schedule buffers
        leftBuffer = generateToneBuffer(frequency: leftFrequency, format: format)
        rightBuffer = generateToneBuffer(frequency: rightFrequency, format: format)
        
        if let leftBuffer = leftBuffer, let rightBuffer = rightBuffer {
            leftNode.scheduleBuffer(leftBuffer, at: nil, options: .loops, completionHandler: nil)
            rightNode.scheduleBuffer(rightBuffer, at: nil, options: .loops, completionHandler: nil)
        }
        
        do {
            try audioEngine.start()
            leftNode.play()
            rightNode.play()
            isPlaying = true
        } catch {
            print("Failed to start audio engine: \(error)")
        }
    }
    
    func stopAudio() {
        guard isPlaying else { return }
        
        leftToneNode?.stop()
        rightToneNode?.stop()
        audioEngine.stop()
        audioEngine.reset()
        
        isPlaying = false
    }
    
    private func generateToneBuffer(frequency: Float, format: AVAudioFormat) -> AVAudioPCMBuffer? {
        let sampleRate = Float(format.sampleRate)
        let frameCount = AVAudioFrameCount(sampleRate)
        
        guard let buffer = AVAudioPCMBuffer(pcmFormat: format, frameCapacity: frameCount) else {
            return nil
        }
        
        buffer.frameLength = frameCount
        
        let channelCount = Int(format.channelCount)
        let frameLength = Int(buffer.frameLength)
        
        for channel in 0..<channelCount {
            guard let floatData = buffer.floatChannelData?[channel] else { continue }
            
            for frame in 0..<frameLength {
                let phase = 2.0 * Float.pi * frequency * Float(frame) / sampleRate
                floatData[frame] = generateWaveSample(phase: phase, type: waveType)
            }
        }
        
        return buffer
    }
    
    private func generateWaveSample(phase: Float, type: WaveType) -> Float {
        switch type {
        case .sine:
            return sin(phase)
        case .square:
            return phase.truncatingRemainder(dividingBy: 2.0 * Float.pi) < Float.pi ? 1.0 : -1.0
        case .sawtooth:
            let normalized = phase.truncatingRemainder(dividingBy: 2.0 * Float.pi) / (2.0 * Float.pi)
            return 2.0 * normalized - 1.0
        case .triangle:
            let normalized = phase.truncatingRemainder(dividingBy: 2.0 * Float.pi) / (2.0 * Float.pi)
            return normalized < 0.5 ? 4.0 * normalized - 1.0 : -4.0 * normalized + 3.0
        }
    }
    
    func updateFrequency(channel: Channel, frequency: Float) {
        switch channel {
        case .left:
            leftFrequency = frequency
        case .right:
            rightFrequency = frequency
        case .both:
            leftFrequency = frequency
            rightFrequency = frequency
        }
        
        if isPlaying {
            stopAudio()
            startAudio()
        }
    }
    
    func updateVolume(channel: Channel, volume: Float) {
        let clampedVolume = min(max(volume, 0), 0.3)
        switch channel {
        case .left:
            leftVolume = clampedVolume
        case .right:
            rightVolume = clampedVolume
        case .both:
            leftVolume = clampedVolume
            rightVolume = clampedVolume
        }
        
        if isPlaying {
            // Update volume without restarting
            if let leftMixer = audioEngine.outputNode.engine?.mainMixerNode {
                leftMixer.volume = leftVolume
            }
        }
    }
    
    func applyFrequency(_ frequency: Float, channel: Channel = .both) {
        updateFrequency(channel: channel, frequency: frequency)
    }
    
    func applyBinauralBeat(leftFreq: Float, rightFreq: Float) {
        leftFrequency = leftFreq
        rightFrequency = rightFreq
        
        if isPlaying {
            stopAudio()
            startAudio()
        }
    }
}