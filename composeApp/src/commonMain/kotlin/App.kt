import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import views.AddNewTab
import views.HomeTab
import views.LikedTab
import views.MessagesTab
import views.ProfileTabAfterLogin
import views.ProfileTabBeforeLogin

lateinit var loggedIn: MutableState<Boolean>
lateinit var userID: MutableState<Int>
lateinit var email: MutableState<String>
lateinit var login: MutableState<String>
lateinit var phone: MutableState<String>

@Composable
fun App() {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    loggedIn = rememberSaveable { mutableStateOf(false) }
    userID = rememberSaveable{ mutableStateOf(-1) }
    email = rememberSaveable { mutableStateOf("") }
    login = rememberSaveable { mutableStateOf("") }
    phone = rememberSaveable { mutableStateOf("") }
    TabNavigator(HomeTab) {
        Scaffold(
            scaffoldState = scaffoldState,
            content = { padding ->
                Box(Modifier.padding(padding)) {
                    CurrentTab()
                }
            },
            bottomBar = {
                BottomNavigation {
                    TabNavigationItem(HomeTab)
                    TabNavigationItem(AddNewTab)
                    TabNavigationItem(MessagesTab)
                    TabNavigationItem(LikedTab)
                    if(!loggedIn.value) {
                        TabNavigationItem(ProfileTabBeforeLogin)
                    } else {
                        TabNavigationItem(ProfileTabAfterLogin)
                    }
                }
            }
        )
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    BottomNavigationItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = {
            tab.options.icon?.let {
                Icon(
                    painter = it,
                    contentDescription = tab.options.title
                )
            }
        },
        modifier = Modifier
            .weight(20f)
            .background(color = Color(0xFFE3E3E3))
            .padding(5.dp),
        unselectedContentColor = Color(0xFF5c5c5c),
        selectedContentColor = Color.Black
    )
}


