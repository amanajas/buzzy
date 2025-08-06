import SwiftUI

struct MainControlsView: View {
    @ObservedObject var viewModel: FrequencyViewModel
    @State private var quickFrequency = ""
    
    var body: some View {
        VStack(spacing: 16) {
            // Play/Stop Button
            Button(action: { viewModel.togglePlayPause() }) {
                HStack {
                    Image(systemName: viewModel.audioEngine.isPlaying ? "stop.fill" : "play.fill")
                        .font(.system(size: 20))
                    Text(viewModel.audioEngine.isPlaying ? "Stop" : "Play")
                        .font(.system(size: 18, weight: .medium))
                }
                .foregroundColor(.white)
                .frame(maxWidth: .infinity)
                .padding(.vertical, 16)
                .background(viewModel.audioEngine.isPlaying ? Color.red : Color.green)
                .cornerRadius(12)
            }
            
            // Wave Type Selector
            VStack(alignment: .leading, spacing: 8) {
                Text("Wave Type")
                    .font(.caption)
                    .foregroundColor(.gray)
                
                HStack(spacing: 8) {
                    ForEach(AudioEngine.WaveType.allCases, id: \.self) { waveType in
                        Button(action: { viewModel.setWaveType(waveType) }) {
                            VStack(spacing: 4) {
                                Text(waveIcon(for: waveType))
                                    .font(.system(size: 16))
                                Text(waveType.rawValue)
                                    .font(.caption)
                            }
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, 8)
                            .background(
                                viewModel.audioEngine.waveType == waveType ?
                                Color(hex: "667EEA") : Color.gray.opacity(0.2)
                            )
                            .foregroundColor(
                                viewModel.audioEngine.waveType == waveType ?
                                .white : .primary
                            )
                            .cornerRadius(8)
                        }
                    }
                }
            }
            .padding()
            .background(Color.gray.opacity(0.1))
            .cornerRadius(12)
            
            // Quick Frequency Input
            VStack(alignment: .leading, spacing: 8) {
                Text("Quick Frequency (20-20000 Hz)")
                    .font(.caption)
                    .foregroundColor(.gray)
                
                HStack {
                    TextField("Enter Hz", text: $quickFrequency)
                        .keyboardType(.decimalPad)
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                    
                    Button("Apply") {
                        viewModel.applyQuickFrequency(quickFrequency)
                        quickFrequency = ""
                    }
                    .disabled(quickFrequency.isEmpty)
                    .padding(.horizontal)
                    .padding(.vertical, 8)
                    .background(
                        quickFrequency.isEmpty ? Color.gray : Color(hex: "667EEA")
                    )
                    .foregroundColor(.white)
                    .cornerRadius(8)
                }
            }
            .padding()
            .background(Color.gray.opacity(0.1))
            .cornerRadius(12)
        }
        .padding(.horizontal)
    }
    
    private func waveIcon(for waveType: AudioEngine.WaveType) -> String {
        switch waveType {
        case .sine: return "∿"
        case .square: return "⊓"
        case .sawtooth: return "⋰"
        case .triangle: return "△"
        }
    }
}