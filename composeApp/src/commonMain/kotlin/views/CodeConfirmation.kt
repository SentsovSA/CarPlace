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
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
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
import userID
import viewmodel.UserViewModel

class CodeConfirmation(private var code: String) : Screen, Tab {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        MaterialTheme(
            colors = MaterialTheme.colors.copy(background = Color(0xFFE3E3E3))
        ) {
            val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
            val navigator = LocalNavigator.currentOrThrow
            Scaffold(
                scaffoldState = scaffoldState,
                backgroundColor = Color(0xFFE3E3E3),
                content = { padding ->
                    BottomSheetNavigator(
                        modifier = Modifier.padding(padding),
                        hideOnBackPress = true,
                        sheetBackgroundColor = Color.Transparent
                    ) {
                        CodeConfContent(navigator)
                    }
                }
            )
        }
    }

    @Composable
    fun CodeConfContent(navigator: Navigator) {
        var text by remember { mutableStateOf("") }
        var codeText by remember { mutableStateOf("") }
        val scope = rememberCoroutineScope()
        val userVM = getViewModel(Unit, viewModelFactory { UserViewModel() })
        val userState by userVM.uiState.collectAsState()
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 5.dp, end = 5.dp),
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFE3E3E3),
            elevation = 4.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, top = 20.dp)
            ) {
                Text(text = "Подтверждение Email", fontSize = 30.sp, textAlign = TextAlign.Center)
            }
            Spacer(Modifier.fillMaxWidth().padding(30.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    readOnly = false,
                    textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                    placeholder = { Text(text = "Код из Email") },
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
                        if (text == code) {
                            scope.launch {
                                delay(2000)
                                if (userState.info.isNotEmpty()) {
                                    loggedIn.value = true
                                    userID.value =
                                        userState.info.find { it.userEmail == email.value }?.userID
                                            ?: -1
                                    if (userID.value != -1) {
                                        navigator.push(ProfileTabAfterLogin)
                                    } else {
                                        navigator.push(Registration)
                                    }
                                }
                            }
                        } else {
                            codeText = "Неверный код"
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFFC5C5C5))
                ) {
                    Text(
                        text = "Подтвердить",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                    )
                }
                Spacer(Modifier.padding(30.dp))
                Text(text = codeText)
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

