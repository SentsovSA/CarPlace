package views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

class AboutScreen: Screen {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
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
                            text = "О приложении",
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
                        modifier = Modifier
                            .padding(horizontal = 15.dp, vertical = 2.dp),
                        textAlign = TextAlign.Start,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W800,
                        text = "Версия: 1.0.0"
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(10.dp)
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 15.dp, vertical = 2.dp),
                        textAlign = TextAlign.Start,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W800,
                        text = "Создатель: SentsovSA",
                    )
                }
            }
        }
    }
}