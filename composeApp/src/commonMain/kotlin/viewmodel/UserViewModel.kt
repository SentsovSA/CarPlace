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
import model.User
import viewmodel.CarImageViewModel.Companion.log

@Serializable
data class UserUiState(
    val info: List<User> = emptyList()
)

class UserViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UserUiState>(UserUiState())
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

    private suspend fun getInfo(): List<User> {
        return try {
            httpClient
                .get("https://little-ghosts-repair.loca.lt/api/storage/UserViewSet/")
                .body()
        } catch (e: Exception) {
            CarImageViewModel.log.e {"error: ${e.message}"}
            emptyList()
        }
    }
}