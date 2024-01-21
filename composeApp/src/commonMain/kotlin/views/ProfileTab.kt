package views

import CarsPage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.HttpMethod
import io.ktor.http.append
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import viewmodel.CarImageViewModel.Companion.log
import viewmodel.UserViewModel

var loggedIn = false
var email: String = ""

object ProfileTab : Tab {

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    private suspend fun sendInfo(email: String) {
        try {
            httpClient.post("https://little-ghosts-repair.loca.lt/login/") {
                paramBuilding()
                log.i{paramBuilding().toString()}
            }
        } catch (e: Exception) {
            log.i { "Регистрация неудачна" }
        }
    }

    private fun HttpRequestBuilder.paramBuilding() {
        url.parameters.append("method", "register")
        url.parameters.append("email", email)
    }


    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        MaterialTheme(
            colors = MaterialTheme.colors.copy(background = Color(0xFFE3E3E3))
        ) {
            val refreshScope = rememberCoroutineScope()
            var refreshing by remember { mutableStateOf(false) }
            fun refresh() = refreshScope.launch {
                refreshing = true
                log.i { "refreshing..." }
                delay(1000)
                refreshing = false
            }

            val state = rememberPullRefreshState(refreshing, ::refresh)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(50.dp)
                    .pullRefresh(state),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!loggedIn) {
                    var text by remember { mutableStateOf("") }
                    val sendScope = rememberCoroutineScope()
                    email = text
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Авторизация",
                        fontSize = 30.sp,
                        modifier = Modifier
                            .fillMaxWidth(),
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.padding(10.dp))
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Введите электронную почту для регистрации",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                    )
                    Spacer(Modifier.padding(10.dp))
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        readOnly = false,
                        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                        placeholder = { Text(text = "Email") },
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(10.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        maxLines = 1
                    )
                    Spacer(Modifier.padding(10.dp))
                    Button(
                        onClick = {
                            sendScope.launch {
                                sendInfo(text)
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFFC5C5C5))
                    ) {
                        Text(
                            text = "Продолжить",
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        PullRefreshIndicator(
                            refreshing = refreshing,
                            state = state,
                            backgroundColor = Color.White,
                            modifier = Modifier
                                .align(Alignment.TopCenter),
                            scale = true
                        )
                    }
                } else {
                    val userVM = getViewModel(Unit, viewModelFactory { UserViewModel() })
                    val userState by userVM.uiState.collectAsState()
                    Column(
                        modifier = Modifier
                            .padding(50.dp)
                            .pullRefresh(state),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        log.i { userState.info.find { it.userEmail == email }?.userEmail.toString() }
                        Text(
                            text = "Ваш email: " + userState.info.find { it.userEmail == email }?.userEmail.toString(),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .padding(10.dp)
                        )
                        Text(
                            text = "Ваш логин: " + userState.info.find { it.userEmail == email }?.userName.toString(),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .padding(10.dp)
                        )
                        Text(
                            text = "Ваш номер телефона: " + userState.info.find { it.userEmail == email }?.userPhone.toString(),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .padding(10.dp)
                        )
                        Spacer(Modifier.padding(80.dp))
                        Button(
                            onClick = {
                                loggedIn = false
                                email = ""
                                ProfileTab
                            },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFFC5C5C5))
                        ) {
                            Text(
                                text = "Выйти",
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal,
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    PullRefreshIndicator(
                        refreshing = refreshing,
                        state = state,
                        backgroundColor = Color.White,
                        modifier = Modifier
                            .align(Alignment.TopCenter),
                        scale = true
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    override val options: TabOptions
        @Composable
        get() {
            val title = "Test"
            val icon = painterResource("icons/icons8-person-100.png")

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}