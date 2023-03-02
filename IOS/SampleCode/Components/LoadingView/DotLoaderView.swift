//
//  DotLoaderView.swift
//  FoodLens_UI
//
//  Created by 박병호 on 2022/10/25.
//

import SwiftUI
import Combine

struct DotLoaderView: View {
    let timer: Publishers.Autoconnect<Timer.TimerPublisher>
    let timing: Double

    let maxCounter = 4
    @State var counter = 0
    
    let frame: CGSize
    let primaryColor: Color

    public init(color: Color = .blue, size: CGFloat = 50, speed: Double = 0.5) {
        timing = speed / 2
        timer = Timer.publish(every: timing, on: .main, in: RunLoop.Mode.default).autoconnect()
        frame = CGSize(width: size, height: size)
        primaryColor = color
    }

    public var body: some View {
        HStack(spacing: 5) {
            ForEach(0..<maxCounter, id: \.self) { index in
                Circle()
                    .offset(y: counter == index ? -frame.height / 10 : frame.height / 10)
                    .fill(primaryColor)
            }
        }
        .frame(width: frame.width, height: frame.height, alignment: .center)
        .onReceive(timer, perform: { _ in
            withAnimation(.easeInOut(duration: timing * 2)) {
                counter = counter == (maxCounter - 1) ? 0 : counter + 1
            }
        })
    }
}

struct DotLoaderView_Previews: PreviewProvider {
    static var previews: some View {
        DotLoaderView()
    }
}
