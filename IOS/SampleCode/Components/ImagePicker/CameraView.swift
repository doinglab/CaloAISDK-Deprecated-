//
//  ImagePicker.swift
//  ExampleFoodLens
//
//  Created by 박병호 on 2023/01/26.
//


import UIKit
import SwiftUI

struct CameraView: UIViewControllerRepresentable {
    
    @Binding var selectedImage: UIImage
    @Environment(\.presentationMode) var isPresented
    var sourceType: UIImagePickerController.SourceType = .camera
        
    func makeUIViewController(context: Context) -> UIImagePickerController {
        let imagePicker = UIImagePickerController()
        imagePicker.sourceType = self.sourceType
        imagePicker.delegate = context.coordinator // confirming the delegate
        return imagePicker
    }

    func updateUIViewController(_ uiViewController: UIImagePickerController, context: Context) {

    }

    // Connecting the Coordinator class with this struct
    func makeCoordinator() -> Coordinator {
        return Coordinator(picker: self)
    }
    
    class Coordinator: NSObject, UINavigationControllerDelegate, UIImagePickerControllerDelegate {
        var picker: CameraView
        
        init(picker: CameraView) {
            self.picker = picker
        }
        
        func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
            guard let selectedImage = info[UIImagePickerController.InfoKey.originalImage.rawValue] as? UIImage else { return }
            self.picker.selectedImage = selectedImage
            self.picker.isPresented.wrappedValue.dismiss()
        }
        
        func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
//            guard let selectedImage = info[UIImagePickerControllerOriginalImage] as? UIImage else { return }
//            self.picker.selectedImage = selectedImage
//            self.picker.isPresented.wrappedValue.dismiss()
        }
        
    }
}
