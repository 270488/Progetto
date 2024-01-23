package it.polito.database.ui.theme

import NewAccount
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import it.polito.database.AppViewModel
import it.polito.database.HomePage
import it.polito.database.screens.AuthenticationActivity
import it.polito.database.screens.AuthenticationScreen
import it.polito.database.screens.CartScreen
import it.polito.database.screens.CategoryScreen
import it.polito.database.screens.NotificationsScreen
import it.polito.database.screens.ProductScreen
import it.polito.database.screens.ProfileScreen
import it.polito.database.screens.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController, viewModel: AppViewModel){
    NavHost(
        navController = navController,
        startDestination = Screen.AuthenticationScreen.route
    ) {
        composable(route = Screen.Home.route) {
            HomePage(viewModel)
        }
        composable(route = Screen.Category.route) {
           CategoryScreen()
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
        composable(route = Screen.Product.route) {
            ProductScreen(viewModel)
        }
        composable(route = Screen.AuthenticationScreen.route) {
            AuthenticationScreen(navController,AuthenticationActivity())
        }
        composable(route = Screen.NewAccount.route) {
           NewAccount(navController, AuthenticationActivity())
        }
    }
}