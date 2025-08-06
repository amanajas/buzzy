import Foundation

struct Frequency: Identifiable, Codable {
    let id = UUID()
    let name: String
    let freq: Float
    let description: String
}

struct BinauralFrequency: Identifiable, Codable {
    let id = UUID()
    let name: String
    let left: Float
    let right: Float
    let description: String
}

enum FrequencyItem: Identifiable {
    case mono(Frequency)
    case binaural(BinauralFrequency)
    
    var id: UUID {
        switch self {
        case .mono(let freq):
            return freq.id
        case .binaural(let freq):
            return freq.id
        }
    }
    
    var name: String {
        switch self {
        case .mono(let freq):
            return freq.name
        case .binaural(let freq):
            return freq.name
        }
    }
    
    var description: String {
        switch self {
        case .mono(let freq):
            return freq.description
        case .binaural(let freq):
            return freq.description
        }
    }
}