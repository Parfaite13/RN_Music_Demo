//
//  MDModule.swift
//  MDModule
//
//  Copyright © 2021 Alex Demchenko. All rights reserved.
//

import Foundation

@objc(MDModule)
class MDModule: NSObject {
  @objc
  func constantsToExport() -> [AnyHashable : Any]! {
    return ["count": 1]
  }

  @objc
  static func requiresMainQueueSetup() -> Bool {
    return true
  }
}
