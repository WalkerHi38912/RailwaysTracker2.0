package com.example.russianrailways20.View

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.russianrailways20.ViewModel.TrainViewModel

@ExperimentalMaterial3Api
@Composable
fun NavigationGraph(trainViewModel: TrainViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController, trainViewModel)
        }
        composable(
            route = "stationList/{field}",
            arguments = listOf(navArgument("field") { type = NavType.StringType })
        ) { backStackEntry ->
            val field = backStackEntry.arguments?.getString("field") ?: "to" // Default "to"
            StationsListScreen(navController, field, trainViewModel)
        }
    }
}