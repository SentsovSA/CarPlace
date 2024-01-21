import AutoPartHelper.selectedAuto
import AutoPartHelper.selectedParts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.CarImage
import model.PartImage
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import viewmodel.CarImageViewModel
import viewmodel.CarImageViewModel.Companion.log
import viewmodel.CarViewSetViewModel
import viewmodel.PartImageViewModel
import viewmodel.PartViewSetViewModel
import views.AddNewTab
import views.HomeTab
import views.LikedTab
import views.MenuScreen
import views.MessagesTab
import views.ProfileTab

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun App() {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    TabNavigator(HomeTab) {
        Scaffold(
            scaffoldState = scaffoldState,
            content = { padding ->
                BottomSheetNavigator(
                    modifier = Modifier.padding(padding),
                    hideOnBackPress = true,
                    sheetBackgroundColor = Color.Transparent
                ) {
                    CurrentTab()
                }
            },
            bottomBar = {
                BottomNavigation {
                    TabNavigationItem(HomeTab)
                    TabNavigationItem(AddNewTab)
                    TabNavigationItem(MessagesTab)
                    TabNavigationItem(LikedTab)
                    TabNavigationItem(ProfileTab)
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
    val scaffoldState = rememberScaffoldState()
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
                TopBar()
            }
            item(span = { GridItemSpan(2) }) {
                Spacer(
                    modifier = Modifier
                        .padding(bottom = 15.dp)
                )
            }
            item(span = { GridItemSpan(2) }) {
                AutoPartChoose(
                    carImagesVM, carsViewSetsVM,
                    partImagesVM, partsViewSetsVM
                )
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
                            for (i in carImagesState.images) {
                                MultiCard(
                                    i,
                                    carsViewSetsVM,
                                    partsViewSetsVM,
                                    partImagesState.images[0],
                                    false
                                )
                            }
                        } else if (selectedAuto && !selectedParts) {
                            log.i { "1st column else if 1: selectedAuto = $selectedAuto, selectedParts = $selectedParts" }
                            for (i in carImagesState.images.indices) {
                                if(i % 2 == 0)
                                    MultiCard(
                                        carImagesState.images[i],
                                        carsViewSetsVM,
                                        partsViewSetsVM,
                                        partImagesState.images[0],
                                        false
                                    )
                            }
                        } else if (!selectedAuto && selectedParts) {
                            log.i { "1st column else if 2: selectedAuto = $selectedAuto, selectedParts = $selectedParts" }
                            for (i in partImagesState.images.indices) {
                                if(i % 2 == 0)
                                    MultiCard(
                                        carImagesState.images[0],
                                        carsViewSetsVM,
                                        partsViewSetsVM,
                                        partImagesState.images[i],
                                        true
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
                            for (i in partImagesState.images) {
                                MultiCard(
                                    carImagesState.images[0],
                                    carsViewSetsVM,
                                    partsViewSetsVM,
                                    i,
                                    true
                                )
                            }
                        } else if (selectedAuto && !selectedParts) {
                            log.i { "2nd column else if 1: selectedAuto = $selectedAuto, selectedParts = $selectedParts" }
                            for (i in carImagesState.images.indices) {
                                if(i % 2 == 1)
                                MultiCard(
                                    carImagesState.images[i],
                                    carsViewSetsVM,
                                    partsViewSetsVM,
                                    partImagesState.images[0],
                                    false
                                )
                            }
                        } else if (!selectedAuto && selectedParts) {
                            log.i { "2nd column else if 2: selectedAuto = $selectedAuto, selectedParts = $selectedParts" }
                            for (i in partImagesState.images.indices) {
                                if(i % 2 == 1)
                                MultiCard(
                                    carImagesState.images[0],
                                    carsViewSetsVM,
                                    partsViewSetsVM,
                                    partImagesState.images[i],
                                    true
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
    carImagesVM: CarImageViewModel, carsViewSetsVM: CarViewSetViewModel,
    partImagesVM: PartImageViewModel, partsViewSetsVM: PartViewSetViewModel
) {
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
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(50f),
            selected = selectedAuto,
            colors = ChipDefaults.filterChipColors(
                if (selectedAuto) Color(0xFFFFFFFF) else Color(0xFFC5C5C5),
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
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(end = 10.dp)
                .weight(50f),
            selected = selectedParts,
            colors = ChipDefaults.filterChipColors(
                if (selectedParts) Color(0xFFFFFFFF) else Color(0xFFC5C5C5),
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
fun CarImageCell(image: CarImage) {
    KamelImage(
        asyncPainterResource(image.file),
        image.fileName,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.0f)
            .padding(5.dp)
    )
}

@Composable
fun PartImageCell(image: PartImage) {
    KamelImage(
        asyncPainterResource(image.file),
        image.fileName,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.0f)
            .padding(5.dp)
    )
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

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TopBar() {
    var text by remember { mutableStateOf("") }
    val bottomSheetNavigator = LocalBottomSheetNavigator.current
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

@Composable
fun MultiCard(
    carImage: CarImage,
    carsViewSetsVM: CarViewSetViewModel,
    partsViewSetsVM: PartViewSetViewModel,
    partImage: PartImage,
    isPart: Boolean
) {
    val carViewSetsUIState by carsViewSetsVM.uiState.collectAsState()
    val partViewSetsUIState by partsViewSetsVM.uiState.collectAsState()
    val price: String
    val nameTag: String
    if (!isPart) {
        if (carViewSetsUIState.info.isNotEmpty() && carImage.carID.isNotEmpty()) {
            price =
                carsViewSetsVM.uiState.value.info.find { it.carId == carImage.carID[0] }?.price.toString()
            nameTag =
                carsViewSetsVM.uiState.value.info.find { it.carId == carImage.carID[0] }?.brand + " " +
                        carsViewSetsVM.uiState.value.info.find { it.carId == carImage.carID[0] }?.model
        } else {
            price = "0"
            nameTag = "Pusto"
            log.i { "Car huinya pustaya" }
        }
    } else {
        if (partViewSetsUIState.info.isNotEmpty() && partImage.partID.isNotEmpty()) {
            price =
                partsViewSetsVM.uiState.value.info.find { it.partID == partImage.partID[0] }?.partPrice.toString()
            nameTag =
                partsViewSetsVM.uiState.value.info.find { it.partID == partImage.partID[0] }?.partName.toString()
        } else {
            price = "0"
            nameTag = "Pusto"
            log.i { "Part huinya pustaya" }
        }
    }

    Card(
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(7.dp),
        backgroundColor = Color(0xFFE3E3E3),
    ) {
        Column {
            if (!isPart) {
                CarImageCell(carImage)
            } else {
                PartImageCell(partImage)
            }
            Text(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 2.dp)
                    .align(Alignment.CenterHorizontally),
                fontSize = 16.sp,
                fontWeight = FontWeight.W800,
                text = if (price != null) "$price ₽" else ""
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 2.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                text = if (nameTag != null) nameTag else ""
            )
        }
    }
}

