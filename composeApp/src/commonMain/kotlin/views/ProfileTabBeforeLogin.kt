package views

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
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
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
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import viewmodel.CarImageViewModel.Companion.log

private lateinit var response: ProfileTabBeforeLogin.Response

object ProfileTabBeforeLogin : Tab, Screen {

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    private suspend fun sendInfo(email: String, navigator: Navigator) {
        try {
            response =
                httpClient.post("https://little-ghosts-repair.loca.lt/login/") {
                    contentType(ContentType.Application.Json)
                    setBody(buildJsonObject {
                        put("method", "register")
                        put("email", email)
                    })
                }.body<Response>()
            navigator.push(CodeConfirmation(response.code))
            log.i { response }
        } catch (e: Exception) {
            log.i { "Регистрация неудачна" }
            log.e { "error: ${e.message}" }
        }
    }

    @Serializable
    data class Response(
        var response: String,
        var code: String
    )


    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        val navigator = LocalNavigator.currentOrThrow
        MaterialTheme(
            colors = MaterialTheme.colors.copy(background = Color(0xFFE3E3E3))
        ) {
            Scaffold(
                scaffoldState = scaffoldState,
                content = { padding ->
                    BottomSheetNavigator(
                        modifier = Modifier.padding(padding),
                        hideOnBackPress = true,
                        sheetBackgroundColor = Color.Transparent
                    ) {
                        MainContent(navigator)
                    }
                }
            )
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun MainContent(navigator: Navigator) {
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
            var text by remember { mutableStateOf("") }
            val sendScope = rememberCoroutineScope()
            email.value = text
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
                        sendInfo(text, navigator)
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