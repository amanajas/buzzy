import SwiftUI

struct SearchBarView: View {
    @Binding var searchTerm: String
    
    var body: some View {
        HStack {
            Image(systemName: "magnifyingglass")
                .foregroundColor(.gray)
            
            TextField("Search frequencies...", text: $searchTerm)
                .textFieldStyle(PlainTextFieldStyle())
            
            if !searchTerm.isEmpty {
                Button(action: { searchTerm = "" }) {
                    Image(systemName: "xmark.circle.fill")
                        .foregroundColor(.gray)
                }
            }
        }
        .padding()
        .background(Color.gray.opacity(0.1))
        .cornerRadius(12)
        .padding(.horizontal)
    }
}