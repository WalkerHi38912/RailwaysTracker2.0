package com.example.russianrailways20.View

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.russianrailways20.Model.Repository
import com.example.russianrailways20.Model.StationApiInterface
import com.example.russianrailways20.Model.TrainApiInterface
import com.example.russianrailways20.Model.TrainViewModelFactory
import com.example.russianrailways20.R
import com.example.russianrailways20.ViewModel.TrainViewModel
import com.example.russianrailways20.ui.theme.RussianRailways20Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RussianRailways20Theme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(){
    val repository = Repository(TrainApi = TrainApiInterface.create(), StationApi = StationApiInterface.create())
    val factory = TrainViewModelFactory(repository)
    val trainViewModel: TrainViewModel = viewModel(factory = factory)
    TrainScreen(trainViewModel)
}

@Composable
fun TrainScreen(trainViewModel: TrainViewModel){
    val trainData by trainViewModel.trainData.collectAsState()
    val stationData by trainViewModel.stationsData.collectAsState()
    val isDataLoaded by trainViewModel.dataLoaded.collectAsState()
    MainMenu()
}


@Composable
fun MainMenu(){
    var textFrom by remember { mutableStateOf("") }
    var textTo by remember { mutableStateOf("") }

    Column {
        Box (
            modifier = Modifier
                .padding(top = 54.dp, start = 8.dp, end = 8.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .background(Color.LightGray)
        ){
            Column {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Image(
                        painter = painterResource(id = R.drawable.arrows_lzobnua03qcd),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable {
                                val tmp = textFrom
                                textFrom = textTo
                                textTo = tmp
                            }
                    )
                    Column (
                        modifier = Modifier
                            .padding(16.dp)
                    ){
                        OutlinedTextField(
                            value = textFrom,
                            onValueChange = { textFrom = it},
                            label = { Text(text = "Куда:")},
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            shape = RoundedCornerShape(15.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                unfocusedIndicatorColor = Color.White,
                                focusedIndicatorColor = Color.White,
                                focusedLabelColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                disabledTextColor = Color.Gray
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                        )
                        OutlinedTextField(
                            value = textTo,
                            onValueChange = { textTo = it},
                            label = { Text(text = "Откуда:")},
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            shape = RoundedCornerShape(15.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                unfocusedIndicatorColor = Color.White,
                                focusedIndicatorColor = Color.White,
                                focusedLabelColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                disabledTextColor = Color.Gray
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                        )
                    }
                }

            }

        }
        Box(
            modifier = Modifier
                .padding(start = 8.dp, top = 0.dp, end =  8.dp, bottom = 8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(Color.LightGray)
        ){
            Row {
                Button(onClick = {

                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White, // Основной цвет кнопки
                        contentColor = Color.Black, // Цвет текста внутри кнопки
                        disabledContainerColor = Color.White, // Цвет кнопки в отключённом состоянии
                        disabledContentColor = Color.Black // Цвет текста в
                    ),
                    modifier = Modifier
                    .padding(16.dp)
                ) {
                    Text(text = "Найти Поезд")
                }
            }
        }
    }

}



