import Foundation

struct FrequencyDatabase {
    static let therapeutic = [
        Frequency(name: "7.83 Hz - Schumann", freq: 7.83, description: "Earth's frequency, grounding"),
        Frequency(name: "10 Hz - Alpha", freq: 10, description: "Relaxed awareness"),
        Frequency(name: "40 Hz - Gamma", freq: 40, description: "Enhanced focus"),
        Frequency(name: "174 Hz - Pain Relief", freq: 174, description: "Natural anesthetic"),
        Frequency(name: "285 Hz - Healing", freq: 285, description: "Cellular regeneration"),
        Frequency(name: "396 Hz - Liberation", freq: 396, description: "Root chakra, fear release"),
        Frequency(name: "417 Hz - Change", freq: 417, description: "Sacral chakra, transformation"),
        Frequency(name: "528 Hz - Love", freq: 528, description: "Heart chakra, DNA repair"),
        Frequency(name: "639 Hz - Relationships", freq: 639, description: "Heart chakra, connecting"),
        Frequency(name: "741 Hz - Expression", freq: 741, description: "Throat chakra"),
        Frequency(name: "852 Hz - Intuition", freq: 852, description: "Third eye chakra"),
        Frequency(name: "963 Hz - Divine", freq: 963, description: "Crown chakra")
    ]
    
    static let musical = [
        Frequency(name: "C4 - 261.63 Hz", freq: 261.63, description: "Middle C"),
        Frequency(name: "D4 - 293.66 Hz", freq: 293.66, description: "D note"),
        Frequency(name: "E4 - 329.63 Hz", freq: 329.63, description: "E note"),
        Frequency(name: "F4 - 349.23 Hz", freq: 349.23, description: "F note"),
        Frequency(name: "G4 - 392.00 Hz", freq: 392.00, description: "G note"),
        Frequency(name: "A4 - 440.00 Hz", freq: 440.00, description: "Concert pitch"),
        Frequency(name: "B4 - 493.88 Hz", freq: 493.88, description: "B note"),
        Frequency(name: "C5 - 523.25 Hz", freq: 523.25, description: "High C")
    ]
    
    static let binaural = [
        BinauralFrequency(name: "1 Hz - Deep Sleep", left: 100, right: 101, description: "Delta waves"),
        BinauralFrequency(name: "4 Hz - Meditation", left: 200, right: 204, description: "Theta waves"),
        BinauralFrequency(name: "8 Hz - Alpha Relax", left: 432, right: 440, description: "Alpha waves"),
        BinauralFrequency(name: "15 Hz - Focus", left: 440, right: 455, description: "Beta waves"),
        BinauralFrequency(name: "40 Hz - Cognitive", left: 400, right: 440, description: "Gamma waves")
    ]
    
    static let brainwaves = [
        Frequency(name: "2 Hz - Delta Sleep", freq: 2, description: "Deep sleep, healing"),
        Frequency(name: "6 Hz - Theta Dreams", freq: 6, description: "Creativity, meditation"),
        Frequency(name: "10 Hz - Alpha Calm", freq: 10, description: "Relaxed awareness"),
        Frequency(name: "20 Hz - Beta Alert", freq: 20, description: "Normal consciousness"),
        Frequency(name: "40 Hz - Gamma Focus", freq: 40, description: "High cognition")
    ]
    
    static func getFrequencies(for category: String) -> [FrequencyItem] {
        switch category {
        case "therapeutic":
            return therapeutic.map { FrequencyItem.mono($0) }
        case "musical":
            return musical.map { FrequencyItem.mono($0) }
        case "binaural":
            return binaural.map { FrequencyItem.binaural($0) }
        case "brainwaves":
            return brainwaves.map { FrequencyItem.mono($0) }
        default:
            return []
        }
    }
}