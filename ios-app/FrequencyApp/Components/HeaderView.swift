import SwiftUI

struct HeaderView: View {
    var body: some View {
        VStack(spacing: 8) {
            Text("♪ Frequency App")
                .font(.system(size: 28, weight: .bold))
                .foregroundColor(.white)
            
            HStack {
                Image(systemName: "headphones")
                    .foregroundColor(.white)
                Text("Use headphones for binaural beats")
                    .font(.system(size: 12))
                    .foregroundColor(.white)
            }
            .padding(.horizontal, 12)
            .padding(.vertical, 6)
            .background(Color.white.opacity(0.2))
            .cornerRadius(8)
        }
        .frame(maxWidth: .infinity)
        .padding(.vertical, 20)
        .background(
            LinearGradient(
                gradient: Gradient(colors: [Color(hex: "667EEA"), Color(hex: "764BA2")]),
                startPoint: .leading,
                endPoint: .trailing
            )
        )
    }
}