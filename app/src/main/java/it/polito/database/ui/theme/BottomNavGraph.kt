package it.polito.database.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import it.polito.database.AppViewModel
import it.polito.database.HomePage
import it.polito.database.screens.Cart
import it.polito.database.screens.Category
import it.polito.database.screens.Profile

@Composable
fun BottomNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ){
        composable( route = BottomBarScreen.Home.route){
            HomePage(viewModel = AppViewModel())
        }
        composable( route = BottomBarScreen.Category.route){
            Category()
        }
        composable( route = BottomBarScreen.Cart.route){
            Cart()
        }
        composable( route = BottomBarScreen.Profile.route){
            Profile()
        }

    }
}