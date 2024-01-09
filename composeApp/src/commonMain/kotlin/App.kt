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
    MaterialTheme(colors = MaterialTheme.colors.copy(background = Color(0xE3E3E3))) {
        val carsImagesVM = getViewModel(Unit, viewModelFactory { CarImageViewModel() })
        val carsViewSetsVM = getViewModel(Unit, viewModelFactory { CarViewSetViewModel() })
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        Scaffold (
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
        AutoPartChoose()
        AnimatedVisibility(uiState.images.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
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
    Row (
        modifier = Modifier
            .background(Color(0x000))
            .padding(5.dp)
    ){
        FilterChip(
            onClick = {
                selectedAuto = !selectedAuto
                selectedParts = false
                      },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .weight(50f)
                .padding(5.dp),
            selected = selectedAuto,
            colors = ChipDefaults.filterChipColors(
                if (selectedAuto) Color(0x4f4f4f) else Color(0x000),
            )
        ) {
            Text(
                text = "Автомобили",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
        FilterChip(
            onClick = {
                selectedParts = !selectedParts
                selectedAuto = false
                      },
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .weight(50f)
                .padding(5.dp),
            selected = selectedParts,
            colors = ChipDefaults.filterChipColors(
                if (selectedParts) Color(0x4f4f4f) else Color(0x7a7a7a),
            )

        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "Запчасти",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
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
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BottomAppBarCars() {
    BottomAppBar(
        backgroundColor = Color.LightGray
    ) {
        IconButton(
            onClick = {},
            modifier = Modifier.weight(25f)
        ) {
            Icon(
                painterResource("icons8-add-new-100.png"),
                contentDescription = "Add new"
            )
        }
        IconButton(
            onClick = {},
            modifier = Modifier.weight(25f)
        ) {
            Icon(
                painterResource("icons8-message-100.png"),
                contentDescription = "Messages"
            )
        }
        IconButton(
            onClick = {},
            modifier = Modifier.weight(25f)
        ) {
            Icon(
                painterResource("icons8-heart-100.png"),
                contentDescription = "Messages"
            )
        }
        IconButton(
            onClick = {},
            modifier = Modifier.weight(25f)
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
    Row(
        modifier = Modifier
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .padding(5.dp)
    )
    {
        IconButton(
            onClick = {},
            modifier = Modifier
                .weight(10f)
                .padding(horizontal = 5.dp, vertical = 5.dp)
        ) {
            Icon(
                painterResource("icons8-menu-100.png"),
                contentDescription = "menu",
            )
        }
        TextField(
            value = "Test",
            onValueChange = {},
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .weight(80f)
                .background(Color.White, shape = RoundedCornerShape(10.dp)),
            singleLine = true,
            textStyle = MaterialTheme.typography.body1,
            maxLines = 1
        )
        IconButton(
            onClick = {},
            modifier = Modifier
                .weight(10f)
                .padding(horizontal = 5.dp, vertical = 5.dp)
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
        modifier = Modifier
            .size(width = 200.dp, height = 350.dp)
            .background(color = Color.Gray, shape = RoundedCornerShape(30.dp)),
    ) {
        Column {
            CarImageCell(carImage)
            Text(
                modifier = Modifier
                    .padding(5.dp),
                fontSize = 20.sp,
                text = carsViewSetsVM.uiState.value.info.find {it.carId == carImage.carID }?.brand ?: "No brand",
            )
            Text(
                modifier = Modifier
                    .padding(5.dp),
                fontSize = 15.sp,
                text = carsViewSetsVM.uiState.value.info.find {it.carId == carImage.carID }?.model ?: "No model",
            )
            Text(
                modifier = Modifier
                    .padding(5.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.W800,
                text = carsViewSetsVM.uiState.value.info.find {it.carId == carImage.carID }?.price.toString(),
            )
        }

    }
}