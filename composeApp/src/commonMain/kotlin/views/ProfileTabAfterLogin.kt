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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import email
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import loggedIn
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import viewmodel.CarImageViewModel
import viewmodel.UserViewModel

object ProfileTabAfterLogin : Tab, Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        MaterialTheme(
            colors = MaterialTheme.colors.copy(background = Color(0xFFE3E3E3))
        ) {
            val refreshScope = rememberCoroutineScope()
            var refreshing by remember { mutableStateOf(false) }
            val navigator = LocalNavigator.currentOrThrow
            val email by remember { mutableStateOf(email) }
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
                val userVM = getViewModel(Unit, viewModelFactory { UserViewModel() })
                val userState by userVM.uiState.collectAsState()
                Column(
                    modifier = Modifier
                        .padding(top = 50.dp, start = 5.dp, end = 5.dp)
                        .pullRefresh(state),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ваш email: " + userState.info.find { it.userEmail == email.value }?.userEmail.toString(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                    Text(
                        text = "Ваш логин: " + userState.info.find { it.userEmail == email.value }?.userName.toString(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                    Text(
                        text = "Ваш номер телефона: " + userState.info.find { it.userEmail == email.value }?.userPhone.toString(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                    Spacer(Modifier.padding(80.dp))
                    Button(
                        onClick = {
                            loggedIn.value = false
                            email.value = ""
                            navigator.push(ProfileTabBeforeLogin)
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