package views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

class HelpScreen : Screen {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val clipboardManager: ClipboardManager = LocalClipboardManager.current
        val helpEmail = "sentsov13@gmail.com"
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(start = 5.dp, end = 5.dp),
            shape = RoundedCornerShape(10.dp),
            color = Color.White,
            elevation = 4.dp
        ) {
            LazyColumn {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                bottomSheetNavigator.hide()
                            },
                            modifier = Modifier
                                .padding(5.dp)
                        ) {
                            Icon(
                                painterResource("icons/icons8-cross-100.png"),
                                contentDescription = "Close"
                            )
                        }
                        Text(
                            text = "Помощь",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(5.dp)
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }
                item {
                    Text(
                        text = "Приложение находится в разработке.\nПо всем вопросам обращаться на почту: ",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(5.dp),
                        textAlign = TextAlign.Center,
                    )
                }
                item {
                    Text(
                        text = helpEmail,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillParentMaxWidth(),
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline,
                    )
                }
                item{
                    Spacer(Modifier.padding(20.dp))
                }
                item {
                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString((helpEmail)))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFFC5C5C5))
                    ) {
                        Text(
                            text = "Скопировать почту в буфер обмена",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}
