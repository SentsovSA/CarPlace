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
import model.CarImage

@Serializable
data class CarImageUiState(
    val images: List<CarImage> = emptyList()
)

class CarImageViewModel: ViewModel() {
    private val _uiState = MutableStateFlow<CarImageUiState>(CarImageUiState())
    val uiState = _uiState.asStateFlow()

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    override fun onCleared() {
        httpClient.close()
    }

    init{
        updateImages()
    }

    private fun updateImages() {
        viewModelScope.launch {
            val images = getImages()
            _uiState.update {
                it.copy(images = images)

            }
        }
    }

    private suspend fun getImages(): List<CarImage> {
        return httpClient
            .get("https://tall-ends-grab.loca.lt/api/storage/CarImageViewSet/")
            .body()
    }
}