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
fun NavGraph(navController: NavHostController, viewModel: AppViewModel){
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomePage(viewModel)
        }
        composable(route = Screen.Category.route) {
           CategoryScreen(viewModel)
        }
        composable(route = Screen.Cart.route) {
            CartScreen(viewModel)
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(viewModel)
        }
        composable(route = Screen.Notifications.route) {
            NotificationsScreen(viewModel)
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(viewModel)
        }
    }
}