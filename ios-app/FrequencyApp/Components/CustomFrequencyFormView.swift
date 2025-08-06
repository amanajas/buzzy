import SwiftUI

struct CustomFrequencyFormView: View {
    @ObservedObject var viewModel: FrequencyViewModel
    @State private var freqType = "mono"
    @State private var name = ""
    @State private var description = ""
    @State private var frequency = ""
    @State private var leftFreq = ""
    @State private var rightFreq = ""
    
    var body: some View {
        VStack(spacing: 16) {
            Button(action: { viewModel.showAddCustom.toggle() }) {
                HStack {
                    Image(systemName: "plus.circle.fill")
                    Text(viewModel.showAddCustom ? "Cancel" : "Add Custom Frequency")
                }
                .frame(maxWidth: .infinity)
                .padding()
                .background(Color(hex: "667EEA"))
                .foregroundColor(.white)
                .cornerRadius(12)
            }
            
            if viewModel.showAddCustom {
                VStack(spacing: 12) {
                    // Frequency Type Toggle
                    Picker("Type", selection: $freqType) {
                        Text("Mono").tag("mono")
                        Text("Binaural").tag("binaural")
                    }
                    .pickerStyle(SegmentedPickerStyle())
                    
                    // Name Input
                    TextField("Name", text: $name)
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                    
                    // Description Input
                    TextField("Description", text: $description)
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                    
                    // Frequency Inputs
                    if freqType == "mono" {
                        TextField("Frequency (Hz)", text: $frequency)
                            .keyboardType(.decimalPad)
                            .textFieldStyle(RoundedBorderTextFieldStyle())
                    } else {
                        HStack {
                            TextField("Left (Hz)", text: $leftFreq)
                                .keyboardType(.decimalPad)
                                .textFieldStyle(RoundedBorderTextFieldStyle())
                            
                            TextField("Right (Hz)", text: $rightFreq)
                                .keyboardType(.decimalPad)
                                .textFieldStyle(RoundedBorderTextFieldStyle())
                        }
                    }
                    
                    // Save Button
                    Button(action: saveCustomFrequency) {
                        Text("Save Custom Frequency")
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(name.isEmpty ? Color.gray : Color.green)
                            .foregroundColor(.white)
                            .cornerRadius(12)
                    }
                    .disabled(name.isEmpty)
                }
                .padding()
                .background(Color.gray.opacity(0.1))
                .cornerRadius(12)
            }
        }
        .padding(.horizontal)
    }
    
    private func saveCustomFrequency() {
        guard !name.isEmpty else { return }
        
        if freqType == "mono" {
            if let freq = Float(frequency), freq >= 20, freq <= 20000 {
                viewModel.saveCustomFrequency(
                    name: name,
                    description: description,
                    frequency: freq,
                    leftFreq: nil,
                    rightFreq: nil
                )
                resetForm()
            }
        } else {
            if let left = Float(leftFreq), let right = Float(rightFreq),
               left >= 20, left <= 20000, right >= 20, right <= 20000 {
                viewModel.saveCustomFrequency(
                    name: name,
                    description: description,
                    frequency: nil,
                    leftFreq: left,
                    rightFreq: right
                )
                resetForm()
            }
        }
    }
    
    private func resetForm() {
        name = ""
        description = ""
        frequency = ""
        leftFreq = ""
        rightFreq = ""
    }
}