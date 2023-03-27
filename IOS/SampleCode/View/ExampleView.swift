//
//  ContentView.swift
//  ExampleFoodLens
//
//  Created by 박병호 on 2023/01/11.
//

import SwiftUI
import FoodLens2
import Combine

struct ExampleView: View {
    @ObservedObject var viewModel: ViewModel
    
    var body: some View {
        ScrollView {
            VStack(spacing: 24.0) {
                Spacer()
                    .frame(height: 24)
                
                ImageSelectButtons(viewModel: self.viewModel)
                
                ConfigSettingView(viewModel: self.viewModel)
                
                PredictResultView(viewModel: self.viewModel)
            }
            .padding(.horizontal, 20)
        }
        .sheet(isPresented: $viewModel.isShowPhotoPicker) {
            PHPicker(selectedImage: $viewModel.selectedImage)
        }
        .sheet(isPresented: $viewModel.isShowCarmera) {
            CameraView(selectedImage: $viewModel.selectedImage)
        }
        .loadingIndicator(isLoading: self.$viewModel.isLoading)
    }
}

private struct ImageSelectButtons: View {
    @ObservedObject var viewModel: ViewModel
    
    var body: some View {
        VStack(spacing: 20.0) {
            VStack(spacing: 12.0) {
                Button("Camera") {
                    viewModel.isShowCarmera = true
                }
                .buttonStyle(TitleButton())
                
                Button("Photo Library") {
                    viewModel.isShowPhotoPicker = true
                }
                .buttonStyle(TitleButton())
            }
        }
    }
}

private struct ConfigSettingView: View {
    @ObservedObject var viewModel: ViewModel
    let trueFalse: [Bool] = [true, false]
    
    var body: some View {
        Menu {
            Picker("", selection: self.$viewModel.selectedLanguage) {
                ForEach(LanguageConfig.allCases, id: \.self) {
                    Text($0.rawValue)
                }
            }
        } label: {
            HStack {
                Text("Language:")
                    .foregroundColor(.white)
                
                Spacer()
                
                Text(self.viewModel.selectedLanguage.rawValue)
                
            }
        }

        Menu {
            Picker("", selection: self.$viewModel.isAutoRotate) {
                ForEach(self.trueFalse, id: \.self) {
                    Text($0.description)
                }
            }
        } label: {
            HStack {
                Text("Rotation Auto:")
                    .foregroundColor(.white)

                Spacer()

                Text(self.viewModel.isAutoRotate.description)
            }
        }
    }
}

private struct PredictResultView: View {
    @ObservedObject var viewModel: ViewModel
        
    var body: some View {
        ForEach(viewModel.predictResponses.foodInfoList, id: \.self) {
            PredictResultElementView(image: viewModel.selectedImage.fixedOrientation() ?? UIImage(), foodInfo: $0)
        }
    }
}

private struct PredictResultElementView: View {
    var image: UIImage
    var foodInfo: FoodInfo

    var body: some View {
        HStack(spacing: 8.0) {
            if let box = foodInfo.foodPosition {
                Image(uiImage: image.cropToBox(box))
                    .resizable()
                    .scaledToFit()
                    .cornerRadius(8)
                    .frame(maxWidth: 80, maxHeight: 80)
            }
            
            VStack(spacing: 5.0) {
                Spacer()
                
                HStack(spacing: 0.0) {
                    VStack(spacing: 4.0) {
                        Text("\(foodInfo.name)")
                            .font(.headline)
                        
                        Text("(\(foodInfo.energy.decimalString) kcal)")
                            .font(.footnote)
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                    
                    Spacer()
                    
                    VStack(alignment: .leading, spacing: 4.0) {
                        Text("Carbohydrate".localized() + ": \(foodInfo.carbohydrate.decimalString)g")
                            .font(.footnote)
                        
                        Text("Protein".localized() + ": \(foodInfo.protein.decimalString)g")
                            .font(.footnote)
                        
                        Text("Fat".localized() + ": \(foodInfo.fat.decimalString)g")
                            .font(.footnote)
                    }
                    .frame(maxWidth: 110)
                }
                
                Spacer()
                
                Divider()
            }
            .frame(maxHeight: .infinity)
        }
    }
}


struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ExampleView(viewModel: ViewModel())
    }
}
