package viewmodel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.logging.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import model.CarImage
import org.lighthousegames.logging.logging
import kotlin.math.log

@Serializable
data class CarImageUiState(
    val images: List<CarImage> = emptyList()
)

class CarImageViewModel : ViewModel() {
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

    init {
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
    fun refresh() {
        updateImages()
    }

    companion object {
        val log = logging()
    }

    private suspend fun getImages(): List<CarImage> {
        try {
            return httpClient
                .get("https://little-ghosts-repair.loca.lt/api/storage/CarImageViewSet/")
                .body()
        } catch (e: Exception) {
            return emptyList()
        }
    }
}