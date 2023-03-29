//
//  ButtonStyle.swift
//  ExampleFoodLens
//
//  Created by 박병호 on 2023/01/26.
//

import SwiftUI

struct TitleButton: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .frame(maxWidth: .infinity)
            .padding(.vertical, 12)
            .background(Color.gray)
            .foregroundColor(.white)
            .clipShape(Capsule())
            .opacity(configuration.isPressed ? 0.4 : 1.0)
    }
}
