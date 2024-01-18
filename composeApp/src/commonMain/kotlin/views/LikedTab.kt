package views

import CarsPage
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import viewmodel.CarImageViewModel
import viewmodel.CarViewSetViewModel
import viewmodel.PartImageViewModel
import viewmodel.PartViewSetViewModel

object LikedTab : Tab {
    @Composable
    override fun Content() {
        MaterialTheme(colors = MaterialTheme.colors.copy(background = Color(0xFFE3E3E3))) {
            val carsImagesVM = getViewModel(Unit, viewModelFactory { CarImageViewModel() })
            val carsViewSetsVM = getViewModel(Unit, viewModelFactory { CarViewSetViewModel() })
            val partsViewSetsVM = getViewModel(Unit, viewModelFactory { PartViewSetViewModel() })
            val partImagesVM = getViewModel(Unit, viewModelFactory { PartImageViewModel() })
            CarsPage(carsImagesVM, carsViewSetsVM, partsViewSetsVM, partImagesVM)
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    override val options: TabOptions
        @Composable
        get() {
            val title = "Test"
            val icon = painterResource("icons/icons8-heart-100.png")

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}