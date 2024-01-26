package views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import email
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import login
import masks.MaskVisualTransformation
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import phone
import userID
import viewmodel.CarImageViewModel

object Registration : Tab, Screen {
    private lateinit var response: Response

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    private suspend fun sendInfo(email: String, login: String, phone:String, navigator: Navigator) {
        try {
            response =
                httpClient.post("https://little-ghosts-repair.loca.lt/login/") {
                    contentType(ContentType.Application.Json)
                    setBody(buildJsonObject {
                        put("method", "confirm")
                        put("email", email)
                        put("name", login)
                        put("phone", phone)
                    })
                }.body<Response>()
            userID.value = response.response
            navigator.push(ProfileTabAfterLogin)
            CarImageViewModel.log.i { response }
        } catch (e: Exception) {
            CarImageViewModel.log.i { "Регистрация неудачна" }
            CarImageViewModel.log.e { "error: ${e.message}" }
        }
    }

    @Serializable
    data class Response(
        var response: Int
    )
    @Composable
    @OptIn(ExperimentalMaterialApi::class)
    override fun Content() {
        MaterialTheme(
            colors = MaterialTheme.colors.copy(background = Color(0xFFE3E3E3))
        ) {
            val refreshScope = rememberCoroutineScope()
            val sendScope = rememberCoroutineScope()
            var refreshing by remember { mutableStateOf(false) }
            val navigator = LocalNavigator.currentOrThrow
            val email by remember { mutableStateOf(email) }
            val login by remember { mutableStateOf(login) }
            val phone by remember { mutableStateOf(phone) }
            fun refresh() = refreshScope.launch {
                refreshing = true
                CarImageViewModel.log.i { "refreshing..." }
                delay(1000)
                refreshing = false
            }
            val state = rememberPullRefreshState(refreshing, ::refresh)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE3E3E3))
                    .padding(50.dp)
                    .pullRefresh(state),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 50.dp, start = 5.dp, end = 5.dp)
                        .pullRefresh(state),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ваш email: ${email.value}",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                    Text(
                        text = "Ваш логин: ",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                    TextField(
                        value = login.value,
                        onValueChange = { login.value = it },
                        readOnly = false,
                        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                        placeholder = { Text(text = "Логин") },
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
                    Text(
                        text = "Ваш номер телефона: ",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                    TextField(
                        value = phone.value,
                        onValueChange = { it ->
                            if (it.length <= NumberDefaults.INPUT_LENGTH) {
                                phone.value = it.filter { it.isDigit() }
                            }
                        },
                        readOnly = false,
                        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                        placeholder = { Text(text = "Номер телефона") },
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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        visualTransformation = MaskVisualTransformation(NumberDefaults.MASK),
                        singleLine = true,
                        maxLines = 1
                    )
                    Spacer(Modifier.padding(80.dp))
                    Button(
                        onClick = {
                            sendScope.launch {
                                sendInfo(email.value, login.value, phone.value, navigator)
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFFC5C5C5))
                    ) {
                        Text(
                            text = "Сохранить",
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

    object NumberDefaults {
        const val MASK = "+7(###)###-##-##"
        const val INPUT_LENGTH = 10
    }

    @OptIn(ExperimentalResourceApi::class)
    override val options: TabOptions
        @Composable
        get() {
            val title = "Test"
            val icon = painterResource("icons/icons8-home-100.png")

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}