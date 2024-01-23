package views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
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
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import loggedIn

class CodeConfirmation(private var code: String) : Screen {
    @Composable
    override fun Content() {
        var text by remember { mutableStateOf("") }
        var codeText by remember { mutableStateOf("") }
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val navigator = LocalNavigator.currentOrThrow
        val coroutineScope = rememberCoroutineScope()
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(start = 5.dp, end = 5.dp),
            shape = RoundedCornerShape(10.dp),
            color = Color.White,
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
                            loggedIn.value = true
                            bottomSheetNavigator.hide()
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
                        text = "Продолжить",
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

}