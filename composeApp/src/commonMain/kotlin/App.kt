import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.CarImage
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import viewmodel.CarImageViewModel
import viewmodel.CarViewSetViewModel

@Composable
fun App() {
    MaterialTheme(colors = MaterialTheme.colors.copy(background = Color(0xFFE3E3E3))) {
        val carsImagesVM = getViewModel(Unit, viewModelFactory { CarImageViewModel() })
        val carsViewSetsVM = getViewModel(Unit, viewModelFactory { CarViewSetViewModel() })
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        Scaffold(
            scaffoldState = scaffoldState,
            content = {
                CarsPage(carsImagesVM, carsViewSetsVM)
            },
            bottomBar = { BottomAppBarCars() },
        )
    }
}

@Composable
fun CarsPage(viewModel: CarImageViewModel, carsViewSetsVM: CarViewSetViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopBar()
        Spacer(
            modifier = Modifier.padding(bottom = 15.dp)
        )
        AutoPartChoose()
        Spacer(
            modifier = Modifier.padding(bottom = 10.dp)
        )
        FilterButton()

        Text(
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 5.dp)
                .align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            text = "Рекомендуем",
        )
        AnimatedVisibility(uiState.images.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 5.dp)
                    .background(Color.White, shape = RoundedCornerShape(10.dp)),
                content = {
                    items(uiState.images) {
                        CarCard(it, carsViewSetsVM)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AutoPartChoose() {
    var selectedAuto by remember { mutableStateOf(false) }
    var selectedParts by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .background(color = Color(0xFFC5C5C5), shape = RoundedCornerShape(10.dp))
            .padding(5.dp)
    ) {
        FilterChip(
            onClick = {
                selectedAuto = !selectedAuto
                selectedParts = false
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .weight(50f)
                .padding(start = 2.dp, end = 2.dp),
            selected = selectedAuto,
            colors = ChipDefaults.filterChipColors(
                if (selectedAuto) Color(0xFFFFFFFF) else Color(0xFFC5C5C5),
            )
        ) {
            Text(
                text = "Автомобили",
                modifier = Modifier
                    .weight(50f)
                    .padding(start = 20.dp, end = 10.dp),
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
                .weight(50f)
                .padding(start = 2.dp, end = 2.dp),
            selected = selectedParts,
            colors = ChipDefaults.filterChipColors(
                if (selectedParts) Color(0xFFFFFFFF) else Color(0xFFC5C5C5),
            )

        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "Запчасти",
                fontSize = 20.sp,
                modifier = Modifier
                    .weight(50f)
                    .padding(start = 2.dp, end = 10.dp),
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
            painterResource("icons8-filter-100.png"),
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
            .background(Color.Transparent, shape = RoundedCornerShape(10.dp))
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BottomAppBarCars() {
    BottomAppBar(
        backgroundColor = Color(0xFFE3E3E3)
    ) {
        IconButton(
            onClick = {},
            modifier = Modifier.weight(20f)
        ) {
            Icon(
                painterResource("icons8-home-100.png"),
                contentDescription = "Add new"
            )
        }
        IconButton(
            onClick = {},
            modifier = Modifier.weight(20f)
        ) {
            Icon(
                painterResource("icons8-add-new-100.png"),
                contentDescription = "Add new"
            )
        }
        IconButton(
            onClick = {},
            modifier = Modifier.weight(20f)
        ) {
            Icon(
                painterResource("icons8-message-100.png"),
                contentDescription = "Messages"
            )
        }
        IconButton(
            onClick = {},
            modifier = Modifier.weight(20f)
        ) {
            Icon(
                painterResource("icons8-heart-100.png"),
                contentDescription = "Messages"
            )
        }
        IconButton(
            onClick = {},
            modifier = Modifier.weight(20f)
        ) {
            Icon(
                painterResource("icons8-person-100.png"),
                contentDescription = "Messages"
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TopBar() {
    var text by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(10.dp))
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
    )
    {
        IconButton(
            onClick = {},
            modifier = Modifier
                .weight(10f)
                .padding(horizontal = 5.dp)
                .align(Alignment.CenterVertically),
        ) {
            Icon(
                painterResource("icons8-menu-100.png"),
                contentDescription = "menu",
            )
        }
        TextField(
            value = text,
            onValueChange = { text = it},
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
                painterResource("icons8-search-100.png"),
                contentDescription = "search"
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CarCard(carImage: CarImage, carsViewSetsVM: CarViewSetViewModel) {
    val viewSetsUIState by carsViewSetsVM.uiState.collectAsState()
    Card(
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(7.dp),
        backgroundColor = Color(0xFFE3E3E3),
    ) {
        Column {
            CarImageCell(carImage)
            Text(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 2.dp)
                    .align(Alignment.CenterHorizontally),
                fontSize = 20.sp,
                fontWeight = FontWeight.W800,
                text = carsViewSetsVM.uiState.value.info.find { it.carId == carImage.carID }?.price.toString(),
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 2.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                text = carsViewSetsVM.uiState.value.info.find { it.carId == carImage.carID }?.brand + " " +
                    carsViewSetsVM.uiState.value.info.find { it.carId == carImage.carID }?.model
            )
        }

    }
}