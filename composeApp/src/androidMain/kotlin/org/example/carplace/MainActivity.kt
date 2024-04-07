package org.example.carplace

import App
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import dev.icerock.moko.biometry.compose.BindBiometryAuthenticatorEffect
import dev.icerock.moko.biometry.compose.BiometryAuthenticatorFactory
import dev.icerock.moko.biometry.compose.rememberBiometryAuthenticatorFactory
import dev.icerock.moko.mvvm.getViewModel
import viewmodel.BiometryViewModel

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val biometryFactory: BiometryAuthenticatorFactory = rememberBiometryAuthenticatorFactory()

            // Create viewModel from common code
            val viewModel = getViewModel {
                BiometryViewModel(
                    // Pass platform implementation of the Biometry Authenticator
                    // to a common code
                    biometryAuthenticator = biometryFactory.createBiometryAuthenticator()
                )
            }

            // Binds the Biometry Authenticator to the view lifecycle
            BindBiometryAuthenticatorEffect(viewModel.biometryAuthenticator)

            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}