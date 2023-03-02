//
//  UIImage+Extension.swift
//  ExampleFoodLens
//
//  Created by 박병호 on 2023/01/25.
//

import UIKit
//import FoodLens2

extension UIImage {
    func cropToBox(_ box: Box) -> UIImage {
        let width = box.xmax - box.xmin
        let height = box.ymax - box.ymin
        return self.cropToBounds(posX: box.xmin, posY: box.ymin, width: width, height: height)
    }
    
    func cropToBounds(posX: CGFloat, posY : CGFloat, width: CGFloat, height: CGFloat) -> UIImage {
        let cgwidth: CGFloat = CGFloat(width)
        let cgheight: CGFloat = CGFloat(height)
        let rect: CGRect = CGRect(x: posX, y: posY, width: cgwidth, height: cgheight)
        guard let cgImage = self.cgImage ,
              let imageRef = cgImage.cropping(to: rect) else {
            return UIImage()
        }
        let ret_image: UIImage = UIImage(cgImage: imageRef, scale: self.scale, orientation: self.imageOrientation)

        return ret_image
    }
}
