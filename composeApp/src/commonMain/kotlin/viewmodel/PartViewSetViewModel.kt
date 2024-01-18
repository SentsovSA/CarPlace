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
import model.PartViewSet
import viewmodel.CarImageViewModel.Companion.log

@Serializable
data class PartViewSetUiState(
    val info: List<PartViewSet> = emptyList()
)

class PartViewSetViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<PartViewSetUiState>(PartViewSetUiState())
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

    fun refresh() = updateInfo()

    private suspend fun getInfo(): List<PartViewSet> {
        return try {
            httpClient
                .get("https://little-ghosts-repair.loca.lt/api/storage/PartViewSet/")
                .body()
        } catch (e: Exception) {
            log.e {"error: ${e.message}"}
            emptyList()
        }
    }
}