//
//  LoadingView.swift
//  ExampleFoodLens
//
//  Created by 박병호 on 2023/02/17.
//

import SwiftUI

struct LoadingView: ViewModifier {
    @Binding var isLoading: Bool
    
    func body(content: Content) -> some View {
        ZStack {
            content
                
            if self.isLoading {
                Color.black.opacity(0.7)
                    .ignoresSafeArea(.all)
                
                DotLoaderView()
            }
        }
    }
}

extension View {
    func loadingIndicator(isLoading: Binding<Bool>) -> some View {
        modifier(LoadingView(isLoading: isLoading))
    }
}

struct LoadingView_Previews: PreviewProvider {
    static var previews: some View {
        Text("sadsa")
            .loadingIndicator(isLoading: .constant(true))
    }
}
