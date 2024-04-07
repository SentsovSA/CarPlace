package views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

class MenuScreen : Screen {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
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
                            text = "Меню",
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
                    Button(
                        onClick = {

                        },
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.elevation(defaultElevation = 5.dp),
                        border = ButtonDefaults.outlinedBorder,
                    ) {
                        Icon(
                            painterResource("icons/icons8-notification-100.png"),
                            contentDescription = "Notifications",
                            modifier = Modifier
                                .weight(10f)
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(
                            text = "Уведомления",
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(5.dp)
                                .weight(90f)
                        )
                    }
                }
                item {
                    Button(
                        onClick = {
                            bottomSheetNavigator.show(HelpScreen())
                        },
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.elevation(defaultElevation = 5.dp),
                        border = ButtonDefaults.outlinedBorder,
                    ) {
                        Icon(
                            painterResource("icons/icons8-help-100.png"),
                            contentDescription = "Help",
                            modifier = Modifier
                                .weight(10f)
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(
                            text = "Помощь",
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(5.dp)
                                .weight(90f)
                        )
                    }
                }
                item {
                    Button(
                        onClick = {
                            bottomSheetNavigator.show(TermsScreen())
                        },
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.elevation(defaultElevation = 5.dp),
                        border = ButtonDefaults.outlinedBorder,
                    ) {
                        Icon(
                            painterResource("icons/icons8-terms-and-conditions-100.png"),
                            contentDescription = "Terms and conditions",
                            modifier = Modifier
                                .weight(10f)
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(
                            text = "Лицензионное соглашение",
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(5.dp)
                                .weight(90f)
                        )
                    }
                }
                item {
                    Button(
                        onClick = {

                        },
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.elevation(defaultElevation = 5.dp),
                        border = ButtonDefaults.outlinedBorder,
                    ) {
                        Icon(
                            painterResource("icons/icons8-settings-100.png"),
                            contentDescription = "Settings",
                            modifier = Modifier
                                .weight(10f)
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(
                            text = "Настройки",
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(5.dp)
                                .weight(90f)
                        )
                    }
                }
                item {
                    Button(
                        onClick = {
                            bottomSheetNavigator.show(AboutScreen())
                        },
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.elevation(defaultElevation = 5.dp),
                        border = ButtonDefaults.outlinedBorder,
                    ) {
                        Icon(
                            painterResource("icons/icons8-about-100.png"),
                            contentDescription = "About",
                            modifier = Modifier
                                .weight(10f)
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(
                            text = "О приложении",
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(5.dp)
                                .weight(90f)
                        )
                    }
                }
            }
        }
    }
}