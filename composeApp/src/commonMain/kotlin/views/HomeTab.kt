package views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ChipDefaults
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import viewmodel.CarImageViewModel
import viewmodel.CarImageViewModel.Companion.log
import viewmodel.CarViewSetViewModel
import viewmodel.PartImageViewModel
import viewmodel.PartViewSetViewModel
import views.HomeTab.AutoPartHelper.selectedAuto
import views.HomeTab.AutoPartHelper.selectedParts

object HomeTab : Tab, Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        MaterialTheme(
            colors = MaterialTheme.colors.copy(background = Color(0xFFE3E3E3)),
        ) {
            Scaffold(scaffoldState = scaffoldState,
                content = { padding ->
                    BottomSheetNavigator(
                        modifier = Modifier.padding(padding),
                        hideOnBackPress = true,
                        sheetBackgroundColor = Color.Transparent
                    ) {
                        val carsImagesVM =
                            getViewModel(Unit, viewModelFactory { CarImageViewModel() })
                        val carsViewSetsVM =
                            getViewModel(Unit, viewModelFactory { CarViewSetViewModel() })
                        val partsViewSetsVM =
                            getViewModel(Unit, viewModelFactory { PartViewSetViewModel() })
                        val partImagesVM =
                            getViewModel(Unit, viewModelFactory { PartImageViewModel() })
                        CarsPage(
                            carsImagesVM,
                            carsViewSetsVM,
                            partsViewSetsVM,
                            partImagesVM
                        )
                    }
                }
            )
        }
    }

    object AutoPartHelper {
        var selectedAuto = false
        var selectedParts = false
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun CarsPage(
        carImagesVM: CarImageViewModel,
        carsViewSetsVM: CarViewSetViewModel,
        partsViewSetsVM: PartViewSetViewModel,
        partImagesVM: PartImageViewModel
    ) {
        val carImagesState by carImagesVM.uiState.collectAsState()
        val carInfoState by carsViewSetsVM.uiState.collectAsState()
        val partImagesState by partImagesVM.uiState.collectAsState()
        val partInfoState by partsViewSetsVM.uiState.collectAsState()
        val refreshScope = rememberCoroutineScope()
        var refreshing by remember { mutableStateOf(false) }
        val snackbarHostState = remember { SnackbarHostState() }
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val webViewState = rememberWebViewState("https://docs.google.com/document/d/1YoHeZajFQu6yUYx7IvGfYvulW0GtGsEjd8bQ3WkVF-A/edit?usp=sharing")
        fun refresh() = refreshScope.launch {
            refreshing = true
            log.i { "refreshing..." }
            carImagesVM.refresh()
            carsViewSetsVM.refresh()
            partImagesVM.refresh()
            partsViewSetsVM.refresh()
            delay(1000)
            refreshing = false
        }

        val state = rememberPullRefreshState(refreshing, ::refresh)
        Scaffold(
            drawerBackgroundColor = Color.White,
            drawerShape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp),
            modifier = Modifier,
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) {
            LazyVerticalGrid(
                userScrollEnabled = true,
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .pullRefresh(state)
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 5.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                    TopBar(bottomSheetNavigator)
                }
                item(span = { GridItemSpan(2) }) {
                    Spacer(
                        modifier = Modifier
                            .padding(bottom = 15.dp)
                    )
                }
                item(span = { GridItemSpan(2) }) {
                    AutoPartChoose()
                    Spacer(
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                    )
                }
                item(span = { GridItemSpan(2) }) {
                    FilterButton()
                }
                item(span = { GridItemSpan(2) }) {
                    Text(
                        modifier = Modifier
                            .padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 5.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.SansSerif,
                        text = "Рекомендуем",
                    )
                }
                item {
                    Column {
                        if (carInfoState.info.isEmpty() || carImagesState.images.isEmpty() ||
                            partInfoState.info.isEmpty() || partImagesState.images.isEmpty()
                        ) {
                            val scope = rememberCoroutineScope()
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Проверьте подключение к интернету",
                                    actionLabel = "Обновить",
                                    duration = SnackbarDuration.Indefinite
                                )
                                when (result) {
                                    SnackbarResult.ActionPerformed -> {
                                        log.i { "Action performed" }
                                        refresh()
                                    }

                                    SnackbarResult.Dismissed -> {
                                        log.i { "Dismissed" }
                                    }
                                }
                            }
                        } else {
                            if (!selectedAuto && !selectedParts) {
                                log.i { "1st column if: selectedAuto = $selectedAuto, selectedParts = $selectedParts" }
                                for (i in carInfoState.info) {
                                    MultiCard(
                                        i.imageID,
                                        carsViewSetsVM,
                                        partsViewSetsVM,
                                        carImagesVM,
                                        partImagesVM,
                                        listOf(0),
                                        false,
                                        bottomSheetNavigator
                                    )
                                }
                            } else if (selectedAuto && !selectedParts) {
                                log.i { "1st column else if 1: selectedAuto = $selectedAuto, selectedParts = $selectedParts" }
                                for (i in carInfoState.info.indices) {
                                    if (i % 2 == 0)
                                        MultiCard(
                                            carInfoState.info[i].imageID,
                                            carsViewSetsVM,
                                            partsViewSetsVM,
                                            carImagesVM,
                                            partImagesVM,
                                            listOf(0),
                                            false,
                                            bottomSheetNavigator
                                        )
                                }
                            } else if (!selectedAuto && selectedParts) {
                                log.i { "1st column else if 2: selectedAuto = $selectedAuto, selectedParts = $selectedParts" }
                                for (i in partInfoState.info.indices) {
                                    if (i % 2 == 0)
                                        MultiCard(
                                            listOf(0),
                                            carsViewSetsVM,
                                            partsViewSetsVM,
                                            carImagesVM,
                                            partImagesVM,
                                            partInfoState.info[i].partImageID,
                                            true,
                                            bottomSheetNavigator
                                        )
                                }
                            }
                        }
                    }
                }
                item {
                    Column {
                        if (partInfoState.info.isEmpty() || partImagesState.images.isEmpty() ||
                            carInfoState.info.isEmpty() || carImagesState.images.isEmpty()
                        ) {
                            val scope = rememberCoroutineScope()
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Проверьте подключение к интернету",
                                    actionLabel = "Обновить",
                                    duration = SnackbarDuration.Indefinite
                                )
                                when (result) {
                                    SnackbarResult.ActionPerformed -> {
                                        refresh()
                                    }

                                    SnackbarResult.Dismissed -> {
                                        log.i { "Dismissed" }
                                    }
                                }
                            }
                        } else {
                            if (!selectedAuto && !selectedParts) {
                                log.i { "2nd column if: selectedAuto = $selectedAuto, selectedParts = $selectedParts" }
                                for (i in partInfoState.info) {
                                    MultiCard(
                                        listOf(0),
                                        carsViewSetsVM,
                                        partsViewSetsVM,
                                        carImagesVM,
                                        partImagesVM,
                                        i.partImageID,
                                        true,
                                        bottomSheetNavigator
                                    )
                                }
                            } else if (selectedAuto && !selectedParts) {
                                log.i { "2nd column else if 1: selectedAuto = $selectedAuto, selectedParts = $selectedParts" }
                                for (i in carInfoState.info.indices) {
                                    if (i % 2 == 1)
                                        MultiCard(
                                            carInfoState.info[i].imageID,
                                            carsViewSetsVM,
                                            partsViewSetsVM,
                                            carImagesVM,
                                            partImagesVM,
                                            listOf(0),
                                            false,
                                            bottomSheetNavigator
                                        )
                                }
                            } else if (!selectedAuto && selectedParts) {
                                log.i { "2nd column else if 2: selectedAuto = $selectedAuto, selectedParts = $selectedParts" }
                                for (i in partInfoState.info.indices) {
                                    if (i % 2 == 1)
                                        MultiCard(
                                            listOf(0),
                                            carsViewSetsVM,
                                            partsViewSetsVM,
                                            carImagesVM,
                                            partImagesVM,
                                            partInfoState.info[i].partImageID,
                                            true,
                                            bottomSheetNavigator
                                        )
                                }
                            }

                        }
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

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun AutoPartChoose(
    ) {
        val autoColor = false
        val partColor = false
        Row(
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp, top = 15.dp, bottom = 15.dp)
                .background(color = Color(0xFFC5C5C5), shape = RoundedCornerShape(10.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                onClick = {
                    selectedAuto = !selectedAuto
                    selectedParts = false
                    !autoColor
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(50f),
                selected = selectedAuto,
                colors = ChipDefaults.filterChipColors(
                    if (autoColor) Color(0xFFFFFFFF) else Color(0xFFC5C5C5),
                )
            ) {
                Text(
                    text = "Автомобили",
                    modifier = Modifier,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            FilterChip(
                onClick = {
                    selectedParts = !selectedParts
                    selectedAuto = false
                    !partColor
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(end = 10.dp)
                    .weight(50f),
                selected = selectedParts,
                colors = ChipDefaults.filterChipColors(
                    if (partColor) Color(0xFFFFFFFF) else Color(0xFFC5C5C5),
                )

            ) {
                Text(
                    textAlign = TextAlign.End,
                    text = "Запчасти",
                    fontSize = 20.sp,
                    modifier = Modifier,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun FilterButton() {
        Button(
            onClick = {},
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFC5C5C5)),
        ) {
            Text(
                text = "Марка и модель",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(90f)
            )
            Icon(
                painterResource("icons/icons8-filter-100.png"),
                contentDescription = "Filter",
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .weight(10f)
            )
        }
    }

    @Composable
    fun CarImageCell(imageID: Int, carImagesVM: CarImageViewModel) {
        carImagesVM.uiState.value.images.find {it.imageID == imageID}?.file?.let {
            asyncPainterResource(
                it
            )
        }?.let {
            KamelImage(
                it,
                "test",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.0f)
                    .padding(5.dp)
            )
        }
    }

    @Composable
    fun PartImageCell(partImageID: Int, partImagesVM: PartImageViewModel) {
        partImagesVM.uiState.value.images.find {it.imageID == partImageID}?.file?.let {
            asyncPainterResource(
                it
            )
        }?.let {
            KamelImage(
                it,
                "test",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.0f)
                    .padding(5.dp)
            )
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun TopBar(bottomSheetNavigator: BottomSheetNavigator) {
        var text by remember { mutableStateOf("") }

        Row(
            modifier = Modifier
                .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(10.dp))
                .padding(5.dp)
        )
        {
            IconButton(
                onClick = {
                    bottomSheetNavigator.show(MenuScreen())
                },
                modifier = Modifier
                    .weight(10f)
                    .padding(horizontal = 5.dp)
                    .align(Alignment.CenterVertically),
            ) {
                Icon(
                    painterResource("icons/icons8-menu-100.png"),
                    contentDescription = "menu",
                )
            }
            TextField(
                value = text,
                onValueChange = { text = it },
                readOnly = false,
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                placeholder = { Text(text = "Поиск") },
                modifier = Modifier
                    .weight(80f)
                    .padding(horizontal = 5.dp)
                    .align(Alignment.Top),
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
            IconButton(
                onClick = {},
                modifier = Modifier
                    .weight(10f)
                    .padding(horizontal = 5.dp)
                    .align(Alignment.CenterVertically),
            ) {
                Icon(
                    painterResource("icons/icons8-search-100.png"),
                    contentDescription = "search"
                )
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun MultiCard(
        imageIDs: List<Int>,
        carsViewSetsVM: CarViewSetViewModel,
        partsViewSetsVM: PartViewSetViewModel,
        carImagesVM: CarImageViewModel,
        partImagesVM: PartImageViewModel,
        partImageIDs: List<Int>,
        isPart: Boolean,
        bottomSheetNavigator: BottomSheetNavigator
    ) {
        val carViewSetsUIState by carsViewSetsVM.uiState.collectAsState()
        val partViewSetsUIState by partsViewSetsVM.uiState.collectAsState()
        val carPagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f
        ) {
            imageIDs.size
        }
        val partPagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f
        ) {
            partImageIDs.size
        }
        val price: String
        val nameTag: String
        if (!isPart) {
            log.i{ "imageIDs $imageIDs" }
            if (carViewSetsUIState.info.isNotEmpty() && imageIDs.isNotEmpty()) {
                price =
                    carsViewSetsVM.uiState.value.info.find { it.imageID == imageIDs }?.price.toString()
                nameTag =
                    carsViewSetsVM.uiState.value.info.find { it.imageID == imageIDs }?.brand + " " +
                            carsViewSetsVM.uiState.value.info.find { it.imageID == imageIDs }?.model
            } else {
                price = "0"
                nameTag = "Pusto"
            }
        } else {
            log.i{ "partimageIDs $partImageIDs" }
            if (partViewSetsUIState.info.isNotEmpty() && partImageIDs.isNotEmpty()) {
                price =
                    partsViewSetsVM.uiState.value.info.find { it.partImageID == partImageIDs }?.partPrice.toString()
                nameTag =
                    partsViewSetsVM.uiState.value.info.find { it.partImageID == partImageIDs }?.partName.toString()
            } else {
                price = "0"
                nameTag = "Pusto"
            }
        }

        Card(
            elevation = 10.dp,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(7.dp)
                .clickable {
                    bottomSheetNavigator.show(Details(imageIDs, partImageIDs, isPart))
                },
            backgroundColor = Color(0xFFE3E3E3),
        ) {
            Column {
                if (!isPart) {
                    HorizontalPager(
                        state = carPagerState,
                        modifier = Modifier
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) { index ->
                        CarImageCell(imageIDs[index], carImagesVM)
                    }
                } else {
                    HorizontalPager(
                        state = partPagerState,
                        modifier = Modifier
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) { index ->
                        PartImageCell(partImageIDs[index], partImagesVM)
                    }
                }
                Text(
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                        .align(Alignment.CenterHorizontally),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W800,
                    text = "$price ₽"
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    text = nameTag
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