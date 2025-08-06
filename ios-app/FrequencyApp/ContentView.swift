import SwiftUI

struct ContentView: View {
    @StateObject private var viewModel = FrequencyViewModel()
    
    var body: some View {
        ZStack {
            LinearGradient(
                gradient: Gradient(colors: [Color(hex: "EDE7F6"), Color(hex: "E3F2FD")]),
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
            .ignoresSafeArea()
            
            ScrollView {
                VStack(spacing: 0) {
                    // Header
                    HeaderView()
                    
                    Divider()
                        .padding(.horizontal)
                    
                    // Status Display
                    StatusDisplayView(viewModel: viewModel)
                    
                    // Main Controls
                    MainControlsView(viewModel: viewModel)
                    
                    // Search Bar
                    SearchBarView(searchTerm: $viewModel.searchTerm)
                    
                    // Category Tabs
                    CategoryTabsView(
                        activeTab: $viewModel.activeTab,
                        categories: viewModel.categories
                    )
                    
                    // Custom Frequency Form
                    if viewModel.activeTab == "custom" {
                        CustomFrequencyFormView(viewModel: viewModel)
                    }
                    
                    // Frequency List
                    FrequencyListView(viewModel: viewModel)
                    
                    // Footer
                    FooterView()
                }
                .background(Color.white)
                .cornerRadius(24)
                .shadow(radius: 8)
                .padding()
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}