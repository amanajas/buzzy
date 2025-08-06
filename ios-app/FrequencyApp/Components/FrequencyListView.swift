import SwiftUI

struct FrequencyListView: View {
    @ObservedObject var viewModel: FrequencyViewModel
    
    var body: some View {
        VStack {
            if viewModel.filteredFrequencies.isEmpty {
                Text(viewModel.activeTab == "custom" ?
                     "No custom frequencies yet" : "No frequencies found")
                    .foregroundColor(.gray)
                    .padding()
                    .frame(maxWidth: .infinity)
                    .background(Color.gray.opacity(0.1))
                    .cornerRadius(12)
                    .padding(.horizontal)
            } else {
                ForEach(Array(viewModel.filteredFrequencies.enumerated()), id: \.element.id) { index, item in
                    FrequencyCardView(
                        item: item,
                        isCustom: viewModel.activeTab == "custom",
                        onApply: {
                            switch item {
                            case .mono(let freq):
                                viewModel.applyFrequency(freq.freq)
                            case .binaural(let freq):
                                viewModel.applyBinauralBeat(leftFreq: freq.left, rightFreq: freq.right)
                            }
                        },
                        onRemove: viewModel.activeTab == "custom" ? {
                            viewModel.removeCustomFrequency(at: index)
                        } : nil
                    )
                }
            }
        }
    }
}

struct FrequencyCardView: View {
    let item: FrequencyItem
    let isCustom: Bool
    let onApply: () -> Void
    let onRemove: (() -> Void)?
    
    var body: some View {
        HStack {
            Button(action: onApply) {
                Image(systemName: "play.circle.fill")
                    .font(.system(size: 32))
                    .foregroundColor(Color(hex: "667EEA"))
            }
            
            VStack(alignment: .leading, spacing: 4) {
                Text(item.name)
                    .font(.system(size: 14, weight: .medium))
                
                Text(frequencyText)
                    .font(.system(size: 12))
                    .foregroundColor(Color(hex: "667EEA"))
                
                Text(item.description)
                    .font(.system(size: 11))
                    .foregroundColor(.gray)
            }
            
            Spacer()
            
            if isCustom, let onRemove = onRemove {
                Button(action: onRemove) {
                    Image(systemName: "trash.fill")
                        .foregroundColor(.red)
                }
            }
        }
        .padding()
        .background(Color.white)
        .cornerRadius(12)
        .shadow(radius: 2)
        .padding(.horizontal)
        .padding(.vertical, 4)
        .onTapGesture {
            onApply()
        }
    }
    
    private var frequencyText: String {
        switch item {
        case .mono(let freq):
            return "\(String(format: "%.2f", freq.freq)) Hz"
        case .binaural(let freq):
            return "L: \(String(format: "%.2f", freq.left)) Hz | R: \(String(format: "%.2f", freq.right)) Hz"
        }
    }
}