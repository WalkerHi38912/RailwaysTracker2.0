package com.example.russianrailways20.View

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.russianrailways20.Model.DB.PrevTripsEntity
import com.example.russianrailways20.R
import com.example.russianrailways20.ViewModel.TrainViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@ExperimentalMaterial3Api
@Composable
fun MainMenu(
    trainViewModel: TrainViewModel,
    navController: NavController
) {

    val textFrom by trainViewModel.textFrom.collectAsState()
    val textTo by trainViewModel.textTo.collectAsState()
    val codeFrom by trainViewModel.codeFrom.collectAsState()
    val codeTo by trainViewModel.codeTo.collectAsState()
    val calendarState = rememberSheetState()

    //Date
    val apiSelectedDate = remember { mutableStateOf( LocalDate.now() ) }
    val dateDay = DateTimeFormatter.ofPattern("dd", Locale("ru", "RU"))
    val dateMonth = DateTimeFormatter.ofPattern("MMM", Locale("ru", "RU"))
    val dateDayOfWeek = DateTimeFormatter.ofPattern("EE", Locale("ru", "RU"))
    val viewDateDay by remember {
        derivedStateOf {
            apiSelectedDate.value.format(dateDay)
        }
    }
    val viewDateMonth by remember {
        derivedStateOf {
            apiSelectedDate.value.format(dateMonth)
        }
    }
    val viewDateDayOfWeek by remember {
        derivedStateOf {
            apiSelectedDate.value.format(dateDayOfWeek)
        }
    }

    val newTrip = PrevTripsEntity(
        stationFromName = textFrom,
        stationFromCode = codeFrom,
        stationToName = textTo,
        stationToCode = codeTo
    )
    //Date

    //Navigation
    val currentBackStackEntry = navController.currentBackStackEntry
    currentBackStackEntry?.savedStateHandle?.getLiveData<String>("selectedStationTitle_to")
        ?.observe(LocalLifecycleOwner.current) { title ->
            trainViewModel.updateTextTo(title ?: "")
        }

    currentBackStackEntry?.savedStateHandle?.getLiveData<String>("selectedStationCode_to")
        ?.observe(LocalLifecycleOwner.current) { code ->
            trainViewModel.updateCodeTo(code ?: "")
        }

    currentBackStackEntry?.savedStateHandle?.getLiveData<String>("selectedStationTitle_from")
        ?.observe(LocalLifecycleOwner.current) { title ->
            trainViewModel.updateTextFrom(title ?: "")
        }

    currentBackStackEntry?.savedStateHandle?.getLiveData<String>("selectedStationCode_from")
        ?.observe(LocalLifecycleOwner.current) { code ->
            trainViewModel.updateCodeFrom(code ?: "")
        }
    //Navigation

    //LastFourTrips

    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(

        ),
        selection = CalendarSelection.Date{ date ->
            apiSelectedDate.value = date
        }
    )


    Box(
        Modifier
            .padding(top = 0.dp, bottom = 0.dp, start = 8.dp, end = 8.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(color = Color.Transparent.copy(alpha = 0.4f))
    ) {
        Column {
            Box {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.width(8.dp))
                        Image(
                            painter = painterResource(id = R.drawable.arrows_lzobnua03qcd),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    trainViewModel.swapData()
                                }
                        )
                        Spacer(Modifier.width(8.dp))
                        Column(
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 8.dp, end = 16.dp)
                        ) {
                            Text(
                                text = "Куда: $textTo",
                                modifier = Modifier
                                    .padding(top = 8.dp, bottom = 8.dp)
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = Color.White,
                                        shape = RoundedCornerShape(15.dp)
                                    )
                                    .padding(16.dp)
                                    .clickable {
                                        navController.navigate("stationList/to") // Переход для выбора "to"
                                    },
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Thin
                            )

                            Text(
                                text = "Откуда: $textFrom",
                                modifier = Modifier
                                    .padding(top = 8.dp, bottom = 8.dp)
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = Color.White,
                                        shape = RoundedCornerShape(15.dp)
                                    )
                                    .padding(16.dp)
                                    .clickable {
                                        navController.navigate("stationList/from") // Переход для выбора "from"
                                    },
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Thin
                            )
                        }
                    }
                }
            }
            HorizontalDivider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(start = 56.dp, end = 16.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row{
                    Box(
                        modifier = Modifier.padding(8.dp)
                    ){
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.baseline_event_24),
                                contentDescription = "Calendar",
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
                                    .clickable {
                                        calendarState.show()
                                    }
                            )
                            Row {
                                Text(
                                    text = viewDateDay,
                                    color = Color.White,
                                    fontWeight = FontWeight.Thin,
                                    fontSize = 40.sp
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(start = 4.dp),
                                    verticalArrangement = Arrangement.Top
                                ) {
                                    Text(
                                        text = viewDateMonth,
                                        color = Color.LightGray,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = viewDateDayOfWeek,
                                        color = Color.LightGray,
                                        fontSize = 12.sp,
                                        style = TextStyle(
                                            lineHeight = 14.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                    Button(
                        onClick = {
                            trainViewModel.fetchTrainData(
                                from = codeFrom,
                                to = codeTo,
                                date = apiSelectedDate.value.toString()
                            )
                            trainViewModel.saveTrip(newTrip)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Red,
                            disabledContentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                    ) {
                        Log.e("ApiDate", "${apiSelectedDate.value}")
                        Text(text = "Найти Поезд")
                    }
                }
            }
        }
    }
}