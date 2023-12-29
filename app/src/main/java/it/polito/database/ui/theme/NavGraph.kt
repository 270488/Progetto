package it.polito.database.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import it.polito.database.AppViewModel
import it.polito.database.HomePage
import it.polito.database.screens.CartScreen
import it.polito.database.screens.CategoryScreen
import it.polito.database.screens.NotificationsScreen
import it.polito.database.screens.ProfileScreen
import it.polito.database.screens.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomePage(viewModel = AppViewModel())
        }
        composable(route = Screen.Category.route) {
           CategoryScreen()
        }
        composable(route = Screen.Cart.route) {
            CartScreen()
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen()
        }
        composable(route = Screen.Notifications.route) {
            NotificationsScreen()
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen()
        }
    }
}