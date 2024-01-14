package viewmodel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import model.CarViewSet

@Serializable
data class CarViewSetUiState(
    val info: List<CarViewSet> = emptyList()
)

class CarViewSetViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<CarViewSetUiState>(CarViewSetUiState())
    val uiState = _uiState.asStateFlow()

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    override fun onCleared() {
        httpClient.close()
    }

    init {
        updateInfo()
    }

    private fun updateInfo() {
        viewModelScope.launch {
            val info = getInfo()
            _uiState.update {
                it.copy(info = info)
            }
        }
    }

    private suspend fun getInfo(): List<CarViewSet> {
        try {
            return httpClient
                .get("https://evil-paths-refuse.loca.lt/api/storage/CarViewSet/")
                .body()
        } catch (e: Exception) {
            return emptyList()
        }

    }
}