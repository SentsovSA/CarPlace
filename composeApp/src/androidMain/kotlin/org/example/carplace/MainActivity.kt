package org.example.carplace

import App
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.distribute.Distribute
import dev.icerock.moko.biometry.compose.BindBiometryAuthenticatorEffect
import dev.icerock.moko.biometry.compose.BiometryAuthenticatorFactory
import dev.icerock.moko.biometry.compose.rememberBiometryAuthenticatorFactory
import dev.icerock.moko.mvvm.getViewModel
import viewmodel.BiometryViewModel

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCenter.start(
            application, "fcecb8a4-1e13-4fe0-adeb-611be1e9aafb",
            Analytics::class.java, Crashes::class.java, Distribute::class.java
        )

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