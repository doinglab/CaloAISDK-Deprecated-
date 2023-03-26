//
//  ViewModel.swift
//  ExampleFoodLens
//
//  Created by 박병호 on 2023/01/11.
//

import UIKit
import FoodLens2
import Combine

class ViewModel: ObservableObject {
    @Published var selectedImage: UIImage = .init()
    @Published var predictResponses: RecognitionResult = .init()
    
    @Published var isShowPhotoPicker: Bool = false
    @Published var isShowCarmera: Bool = false
    @Published var isShowingDetailView: Bool = false
    @Published var isLoading: Bool = false
    
    let foodlens: FoodLens = .init()
    
    private var cancellable: Set<AnyCancellable> = .init()

    init() {
        foodlens.setLanguage(.ko)
        foodlens.setAutoRotate(true)
        setupImageBinding()
    }
    
    private var selectedImagePublisher: AnyPublisher<UIImage, Never> {
        self.$selectedImage
            .dropFirst()
            .eraseToAnyPublisher()
    }
    
    private func setupImageBinding() {
        self.selectedImagePublisher
            .sink { image in
                self.predictResponses.foodInfoList.removeAll()
                self.asyncPredict(image)
            }
            .store(in: &self.cancellable)
    }
    
    private func startLoading() {
        DispatchQueue.main.async {
            self.isLoading = true
        }
    }
    
    private func stopLoading() {
        DispatchQueue.main.async {
            self.isLoading = false
        }
    }
    
    func asyncPredict(_ image: UIImage?) {
        guard let image = image else {
            return
        }
        
        self.startLoading()
        Task {
            let result = await foodlens.predict(image: image)
            switch result {
            case .success(let response):
                self.stopLoading()
                DispatchQueue.main.async {
                    self.predictResponses = response
                }
            case .failure(let error):
                self.stopLoading()
                print(error.localizedDescription)
            }
        }
    }
    
    func combinePredeict(_ image: UIImage?) {
        guard let image = image else {
            return
        }
        
        self.startLoading()
        foodlens.predictPublisher(image: image)
            .sink(receiveCompletion: { output in
                switch output {
                case .finished:
                    print("Publisher finished")
                case .failure(let error):
                    self.stopLoading()
                    print(error.localizedDescription)
                }
            }, receiveValue: { response in
                self.stopLoading()
                DispatchQueue.main.async {
                    self.predictResponses = response
                }
            })
            .store(in: &self.cancellable)
    }
    
    func closurePredict(_ image: UIImage?) {
        guard let image = image else {
            return
        }
        
        self.startLoading()
        foodlens.predict(image: image) { result in
            switch result {
            case .success(let response):
                self.stopLoading()
                DispatchQueue.main.async {
                    self.predictResponses = response
                }
            case .failure(let error):
                self.stopLoading()
                print(error.localizedDescription)
            }
        }
    }
}
