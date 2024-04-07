package viewmodel

import dev.icerock.moko.biometry.BiometryAuthenticator
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.launch

class BiometryViewModel (
    val biometryAuthenticator: BiometryAuthenticator
    ) : ViewModel() {

    fun tryToAuth() = viewModelScope.launch {
            try {
                val isSuccess = biometryAuthenticator.checkBiometryAuthentication(
                    requestTitle = "Biometry".desc(),
                    requestReason = "Just for test".desc(),
                    failureButtonText = "Oops".desc(),
                    allowDeviceCredentials = false
                )

                if (isSuccess) {
                    // Do something onSuccess
                }
            } catch (throwable: Throwable) {
                // Do something onFailed
            }
        }
}