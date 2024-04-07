package views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import dev.icerock.moko.biometry.compose.BindBiometryAuthenticatorEffect
import dev.icerock.moko.biometry.compose.BiometryAuthenticatorFactory
import dev.icerock.moko.biometry.compose.rememberBiometryAuthenticatorFactory
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import viewmodel.BiometryViewModel

object LikedTab : Tab {
    @Composable
    override fun Content() {
        BiometryScreen()
    }

    @OptIn(ExperimentalResourceApi::class)
    override val options: TabOptions
        @Composable
        get() {
            val title = "Test"
            val icon = painterResource("icons/icons8-heart-100.png")

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}

@Composable
fun BiometryScreen() {
    val biometryFactory: BiometryAuthenticatorFactory = rememberBiometryAuthenticatorFactory()

    BiometryScreen(
        viewModel = getViewModel(
            key = "biometry-screen",
            factory = viewModelFactory {
                BiometryViewModel(
                    biometryAuthenticator = biometryFactory.createBiometryAuthenticator()
                )
            }
        )
    )
}

@Composable
private fun BiometryScreen(
    viewModel: BiometryViewModel
) = Scaffold() { paddingValues ->
    BindBiometryAuthenticatorEffect(viewModel.biometryAuthenticator)
    val text = if (viewModel.biometryAuthenticator.isBiometricAvailable()) {
        "Biometry is available"
    } else {
        "Biometry is not available"
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text)

        Button(onClick = viewModel::tryToAuth) {
            Text(text = "Click on me")
        }
    }
}