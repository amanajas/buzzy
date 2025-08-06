import SwiftUI

struct FooterView: View {
    var body: some View {
        VStack(spacing: 4) {
            Divider()
                .padding(.horizontal)
            
            Text("💡 Tip: Start with low volume and adjust gradually")
                .font(.system(size: 12))
                .foregroundColor(.gray)
                .multilineTextAlignment(.center)
            
            Text("⚠️ Not for medical use. Consult professionals for health concerns.")
                .font(.system(size: 11))
                .foregroundColor(.gray)
                .multilineTextAlignment(.center)
        }
        .padding()
    }
}