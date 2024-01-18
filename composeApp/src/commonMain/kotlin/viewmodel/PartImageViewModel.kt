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
import model.PartImage
import org.lighthousegames.logging.logging

@Serializable
data class PartImageUiState(
    val images: List<PartImage> = emptyList()
)

class PartImageViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<PartImageUiState>(PartImageUiState())
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

    private suspend fun getImages(): List<PartImage> {
        return try {
            httpClient
                .get("https://little-ghosts-repair.loca.lt/api/storage/PartImageViewSet/")
                .body()
        } catch (e: Exception) {
            log.e {"error: ${e.message}"}
            emptyList()
        }
    }
}