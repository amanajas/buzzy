import SwiftUI

struct CategoryTabsView: View {
    @Binding var activeTab: String
    let categories: [String]
    
    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 12) {
                ForEach(categories, id: \.self) { category in
                    Button(action: { activeTab = category }) {
                        Text(categoryLabel(for: category))
                            .font(.system(size: 14, weight: .medium))
                            .foregroundColor(activeTab == category ? .white : .primary)
                            .padding(.horizontal, 16)
                            .padding(.vertical, 8)
                            .background(
                                activeTab == category ?
                                Color(hex: "667EEA") : Color.gray.opacity(0.2)
                            )
                            .cornerRadius(20)
                    }
                }
            }
            .padding(.horizontal)
        }
        .padding(.vertical, 8)
    }
    
    private func categoryLabel(for category: String) -> String {
        switch category {
        case "custom": return "⭐ Custom"
        case "therapeutic": return "💚 Therapeutic"
        case "musical": return "🎵 Musical"
        case "binaural": return "🎧 Binaural"
        case "brainwaves": return "🧠 Brainwaves"
        default: return category.capitalized
        }
    }
}