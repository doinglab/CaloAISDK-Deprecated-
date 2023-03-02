//
//  String+Extension.swift
//  ExampleFoodLens
//
//  Created by 박병호 on 2023/02/20.
//

import Foundation

extension String {
    func localized(comment: String = "") -> String {
        return NSLocalizedString(self, comment: comment)
    }
}
