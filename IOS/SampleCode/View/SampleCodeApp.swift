//
//  SampleCodeApp.swift
//  SampleCode
//
//  Created by 박병호 on 2023/03/02.
//

import SwiftUI

@main
struct SampleCodeApp: App {
    var body: some Scene {
        WindowGroup {
            ExampleView(viewModel: ViewModel())
        }
    }
}
