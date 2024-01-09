import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.CarImage
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun App() {
    MaterialTheme (colors = MaterialTheme.colors.copy(background = Color.LightGray)) {
        val carsViewModel = getViewModel(Unit, viewModelFactory { CarImageViewModel() })
        AppBottomAppBar()
        CarsPage(carsViewModel)

    }
}

@Composable
fun CarsPage(viewModel: CarImageViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        Modifier
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopBar()
        AnimatedVisibility(uiState.images.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.fillMaxSize().padding(5.dp),
                content = {
                    items(uiState.images) {
                        CarImageCell(it)
                    }
                }
            )
        }
    }
}

@Composable
fun CarImageCell(image: CarImage) {
    KamelImage(
        asyncPainterResource(image.file),
        image.fileName,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.0f)
    )
}



@Composable
fun AppBottomAppBar() {
    Scaffold(
        bottomBar = { BottomAppBarCars() }
    ) { padding ->
        LazyColumn {
            Modifier
                .fillMaxSize()
                .padding(padding)
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BottomAppBarCars() {
    BottomAppBar(
        backgroundColor = Color.Gray
    ) {
        IconButton(
            onClick = {},
            Modifier.weight(25f)
        ) {
            Icon(
                painterResource("icons8-add-new-100.png"),
                contentDescription = "Add new"
            )
        }
        IconButton(
            onClick = {},
            Modifier.weight(25f)
        ) {
            Icon(
                painterResource("icons8-message-100.png"),
                contentDescription = "Messages"
            )
        }
        IconButton(
            onClick = {},
            Modifier.weight(25f)
        ) {
            Icon(
                painterResource("icons8-heart-100.png"),
                contentDescription = "Messages"
            )
        }
        IconButton(
            onClick = {},
            Modifier.weight(25f)
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
        Modifier
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .padding(5.dp)
    )
    {
        IconButton(
            onClick = {},
            Modifier
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
            Modifier
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