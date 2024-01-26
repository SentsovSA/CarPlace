package views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import viewmodel.CarImageViewModel
import viewmodel.CarViewSetViewModel
import viewmodel.PartImageViewModel
import viewmodel.PartViewSetViewModel

class Details(
    private val imageIDs: List<Int>,
    private val partImageIDs: List<Int>,
    private val isPart: Boolean
) : Screen, Tab {

    @Composable
    @OptIn(ExperimentalMaterialApi::class)
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        Scaffold(
            scaffoldState = scaffoldState,
            content = { padding ->
                BottomSheetNavigator(
                    modifier = Modifier.padding(padding),
                    hideOnBackPress = true,
                    sheetBackgroundColor = Color.Transparent
                ) {
                    DetailsContent(bottomSheetNavigator)
                }
            }
        )
    }

    @OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
    @Composable
    fun DetailsContent(bottomSheetNavigator: BottomSheetNavigator) {
        val carsViewSetsVM = getViewModel(Unit, viewModelFactory { CarViewSetViewModel() })
        val carVSVMState by carsViewSetsVM.uiState.collectAsState()
        val carImagesVM = getViewModel(Unit, viewModelFactory { CarImageViewModel() })
        val partViewSetVM = getViewModel(Unit, viewModelFactory { PartViewSetViewModel() })
        val partVSVMState by partViewSetVM.uiState.collectAsState()
        val partImagesVM = getViewModel(Unit, viewModelFactory { PartImageViewModel() })
        var price by remember { mutableStateOf("") }
        var nameTag by remember { mutableStateOf("") }
        var condition by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var stock by remember { mutableStateOf("") }
        var vin by remember { mutableStateOf("") }
        var year by remember { mutableStateOf("") }
        val pagerState = rememberPagerState(
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
        if (!isPart) {
            if (carVSVMState.info.isNotEmpty() && imageIDs.isNotEmpty()) {
                price =
                    carsViewSetsVM.uiState.value.info.find { it.imageID == imageIDs }?.price.toString()
                nameTag =
                    carsViewSetsVM.uiState.value.info.find { it.imageID == imageIDs }?.brand + " " +
                            carsViewSetsVM.uiState.value.info.find { it.imageID == imageIDs }?.model
                condition =
                    carsViewSetsVM.uiState.value.info.find { it.imageID == imageIDs }?.condition.toString()
                description =
                    carsViewSetsVM.uiState.value.info.find { it.imageID == imageIDs }?.description.toString()
                stock =
                    carsViewSetsVM.uiState.value.info.find { it.imageID == imageIDs }?.stock.toString()
                vin =
                    carsViewSetsVM.uiState.value.info.find { it.imageID == imageIDs }?.vin.toString()
                year =
                    carsViewSetsVM.uiState.value.info.find { it.imageID == imageIDs }?.year.toString()

            } else {
                price = "0"
                nameTag = "Pusto"
            }
        } else {
            if(partVSVMState.info.isNotEmpty() && partImageIDs.isNotEmpty()) {
                price =
                    partViewSetVM.uiState.value.info.find { it.partImageID == partImageIDs }?.partPrice.toString()
                nameTag =
                    partViewSetVM.uiState.value.info.find { it.partImageID == partImageIDs }?.partName.toString()
                condition =
                    partViewSetVM.uiState.value.info.find { it.partImageID == partImageIDs }?.condition.toString()
                description =
                    partViewSetVM.uiState.value.info.find { it.partImageID == partImageIDs }?.description.toString()
                stock =
                    partViewSetVM.uiState.value.info.find { it.partImageID == partImageIDs }?.stock.toString()
            }
        }
        LazyColumn {
            item {
                Column(
                    modifier = Modifier
                        .padding(5.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .padding(5.dp)
                    ) {
                        IconButton(
                            modifier = Modifier
                                .weight(10f),
                            onClick = {
                                bottomSheetNavigator.hide()
                            },
                            content = {
                                Icon(
                                    painterResource("icons/icons8-back-100.png"),
                                    contentDescription = "Close"
                                )
                            }
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 5.dp, vertical = 7.dp)
                                .weight(90f),
                            textAlign = TextAlign.Start,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            text = nameTag
                        )
                    }
                    if(!isPart) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(10.dp)
                                )
                        ) { index ->
                            carImagesVM.uiState.value.images.find {
                                it.imageID == (imageIDs[index])
                            }?.file?.let {
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
                    } else {
                        HorizontalPager(
                            state = partPagerState,
                            modifier = Modifier
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(10.dp)
                                )
                        ) { index ->
                            partImagesVM.uiState.value.images.find {it.imageID == partImageIDs[index]}?.file?.let {
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
                    }

                    Text(
                        modifier = Modifier
                            .padding(horizontal = 15.dp, vertical = 2.dp)
                            .align(Alignment.Start),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W800,
                        text = "$price ₽"
                    )
                    Row(
                        modifier = Modifier
                            .padding(5.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 2.dp)
                                .weight(40f),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            text = "Состояние: "
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 2.dp)
                                .weight(60f),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            text = condition
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(5.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 2.dp)
                                .weight(40f),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            text = "Количество в наличии: "
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 2.dp)
                                .weight(60f),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            text = stock
                        )
                    }
                    if(!isPart) {
                        Row(
                            modifier = Modifier
                                .padding(5.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 2.dp)
                                    .weight(40f),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal,
                                text = "VIN-номер: "
                            )
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 2.dp)
                                    .weight(60f),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal,
                                text = vin
                            )
                        }
                        Row(
                            modifier = Modifier
                                .padding(5.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 2.dp)
                                    .weight(40f),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal,
                                text = "Год выпуска: "
                            )
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 2.dp)
                                    .weight(60f),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal,
                                text = year
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .padding(5.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 2.dp)
                                .weight(40f),
                            textAlign = TextAlign.Start,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            text = "Описание: "
                        )
                    }
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 15.dp, vertical = 2.dp),
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        text = description
                    )
                }
                Spacer(Modifier.padding(5.dp))
            }
            item {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                ) {
                    IconButton(
                        modifier = Modifier
                            .weight(10f)
                            .padding(start = 10.dp),
                        onClick = {
                            bottomSheetNavigator.hide()
                        },
                        content = {
                            Icon(
                                painterResource("icons/icons8-message-100.png"),
                                contentDescription = "Close"
                            )
                        }
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 5.dp, end = 5.dp, top = 7.dp)
                            .weight(80f),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W800,
                        text = "Связаться",
                        textAlign = TextAlign.Center
                    )
                    IconButton(
                        modifier = Modifier
                            .weight(10f)
                            .padding(end = 10.dp),
                        onClick = {
                            bottomSheetNavigator.hide()
                        },
                        content = {
                            Icon(
                                painterResource("icons/icons8-call-100.png"),
                                contentDescription = "Close"
                            )
                        }
                    )
                }
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