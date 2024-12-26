package com.example.russianrailways20.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.russianrailways20.Model.DB.PrevTripsEntity
import com.example.russianrailways20.R
import com.example.russianrailways20.ViewModel.TrainViewModel

@Composable
fun PreviousTravelsScreen(viewModel: TrainViewModel){
    val savedTrips by viewModel.savedTrips.collectAsState()


    if (savedTrips.isNotEmpty()){
        Row {
            Column (
                modifier = Modifier
                    .padding(8.dp)
            ){
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
                        .background(Color.Transparent.copy(0.5f))
                        .padding(start = 12.dp, end = 12.dp, top = 8.dp)
                ){
                    Text("Поездки:", color = Color.White, fontSize = 15.sp)
                }
                Row (
                    modifier = Modifier
                        .clip(RoundedCornerShape(topEnd = 15.dp, bottomStart = 15.dp, bottomEnd = 15.dp))
                        .background(Color.Transparent.copy(0.5f))
                        .padding(8.dp)
                ){
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                        ,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(savedTrips) { trip ->
                            if(trip.stationToName != "" && trip.stationFromName != ""){
                                TripItem(trip = trip)
                            }
                        }
                    }
                }
            }
            Image(
                modifier = Modifier.padding(32.dp),
                painter = painterResource(id = R.drawable.baseline_arrow_downward_24),
                contentDescription = ""
            )
        }
    }

}

@Composable
fun TripItem(trip: PrevTripsEntity) {
    Text(text = "${trip.stationFromName} - ${trip.stationToName}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Thin)
}