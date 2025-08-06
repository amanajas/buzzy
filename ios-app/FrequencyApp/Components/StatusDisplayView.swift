import SwiftUI

struct StatusDisplayView: View {
    @ObservedObject var viewModel: FrequencyViewModel
    
    var body: some View {
        VStack(spacing: 16) {
            // Frequency Display
            HStack(spacing: 40) {
                VStack {
                    Text("Left")
                        .font(.caption)
                        .foregroundColor(.gray)
                    Text("\(String(format: "%.2f", viewModel.audioEngine.leftFrequency)) Hz")
                        .font(.system(size: 24, weight: .bold))
                        .foregroundColor(viewModel.audioEngine.isPlaying ? Color(hex: "667EEA") : .gray)
                }
                
                VStack {
                    Text("Right")
                        .font(.caption)
                        .foregroundColor(.gray)
                    Text("\(String(format: "%.2f", viewModel.audioEngine.rightFrequency)) Hz")
                        .font(.system(size: 24, weight: .bold))
                        .foregroundColor(viewModel.audioEngine.isPlaying ? Color(hex: "667EEA") : .gray)
                }
            }
            
            if viewModel.audioEngine.isPlaying {
                Text("♫ Playing ♫")
                    .font(.system(size: 14))
                    .foregroundColor(Color(hex: "667EEA"))
            }
            
            // Volume Controls
            VStack(spacing: 12) {
                // Left Volume
                HStack {
                    Image(systemName: "speaker.wave.2.fill")
                        .foregroundColor(Color(hex: "667EEA"))
                        .frame(width: 20)
                    Text("L")
                        .frame(width: 20)
                    Slider(
                        value: Binding(
                            get: { viewModel.audioEngine.leftVolume },
                            set: { viewModel.updateVolume(channel: .left, volume: $0) }
                        ),
                        in: 0...0.3
                    )
                    Text("\(Int(viewModel.audioEngine.leftVolume * 100))%")
                        .font(.caption)
                        .frame(width: 40, alignment: .trailing)
                }
                
                // Right Volume
                HStack {
                    Image(systemName: "speaker.wave.2.fill")
                        .foregroundColor(Color(hex: "667EEA"))
                        .frame(width: 20)
                    Text("R")
                        .frame(width: 20)
                    Slider(
                        value: Binding(
                            get: { viewModel.audioEngine.rightVolume },
                            set: { viewModel.updateVolume(channel: .right, volume: $0) }
                        ),
                        in: 0...0.3
                    )
                    Text("\(Int(viewModel.audioEngine.rightVolume * 100))%")
                        .font(.caption)
                        .frame(width: 40, alignment: .trailing)
                }
            }
        }
        .padding()
    }
}