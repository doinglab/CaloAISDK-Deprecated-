//
//  Double+Extension.swift
//  ExampleFoodLens
//
//  Created by 박병호 on 2023/02/20.
//

import Foundation

extension Double {
    var decimalString: String {
        String(format: "%.2f", self)
    }
}
