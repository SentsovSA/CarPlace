//
//  BiometryViewController.swift
//  iosApp
//
//  Created by SentsovSA on 21.02.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import UIKit
import SwiftUI
import ComposeApp

class BiometryViewController: UIViewController {
    
    private var viewModel: BiometryViewModel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.viewModel = BiometryViewModel(
            biometryAuthenticator: BiometryBiometryAuthenticator()
        )
    }
    
    @IBAction private func loginAction() {
        self.viewModel.tryToAuth()
    }
}
