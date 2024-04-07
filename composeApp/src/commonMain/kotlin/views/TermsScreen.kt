package views

import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
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
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

class TermsScreen : Screen {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val state =
            rememberWebViewState("https://docs.google.com/document/d/1YoHeZajFQu6yUYx7IvGfYvulW0GtGsEjd8bQ3WkVF-A/edit?usp=sharing")
        val scrollableState = rememberScrollableState { 1f }
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            shape = RoundedCornerShape(10.dp),
            color = Color.White,
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier
            ) {
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
                        text = "Лицензионное соглашение",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .padding(10.dp)
                )
                val loadingState = state.loadingState
                if (loadingState is LoadingState.Loading) {
                    LinearProgressIndicator(
                        progress = loadingState.progress,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFE3E3E3),
                    )
                }
                WebView(
                    state = state,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                )
            }
        }
    }
}