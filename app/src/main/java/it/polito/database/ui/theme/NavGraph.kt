package it.polito.database.ui.theme

import AccountScreen
import NewAccount
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import it.polito.database.AppViewModel
import it.polito.database.HomePage
import it.polito.database.screens.AiutoEContatti
import it.polito.database.screens.AuthenticationActivity
import it.polito.database.screens.AuthenticationScreen
import it.polito.database.screens.CartScreen
import it.polito.database.screens.CategoryScreen
import it.polito.database.screens.CheckoutScreen
import it.polito.database.screens.CollectOrder
import it.polito.database.screens.DettaglioResiScreen
import it.polito.database.screens.FavoritesScreen
import it.polito.database.screens.GestisciAccountScreen
import it.polito.database.screens.GestisciFitlockerScreen
import it.polito.database.screens.ModificaDati
import it.polito.database.screens.NewsletterScreen
import it.polito.database.screens.NotificationsScreen
import it.polito.database.screens.OrderDetails
import it.polito.database.screens.Orders
import it.polito.database.screens.PreferenzaNotificheScreen
import it.polito.database.screens.ProductListScreen
import it.polito.database.screens.ProductScreen
import it.polito.database.screens.ProfileScreen
import it.polito.database.screens.ResiScreen
import it.polito.database.screens.ScegliPalestraScreen
import it.polito.database.screens.SettingsScreen


@Composable
fun NavGraph(navController: NavHostController, viewModel: AppViewModel){
    NavHost(
        navController = navController,
        startDestination = Screen.AuthenticationScreen.route
    ) {
        composable(route = Screen.Home.route) {
            HomePage(viewModel, navController)
        }
        composable(route = Screen.Category.route) {
           CategoryScreen(viewModel, navController)
        }
        composable(route = Screen.Cart.route) {
            CartScreen(viewModel, navController)
        }
        composable(route = Screen.Checkout.route) {
            CheckoutScreen(viewModel, navController)
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(viewModel,navController)
        }
        composable(route = Screen.Notifications.route) {
            NotificationsScreen(viewModel)
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(viewModel)
        }
        composable(route = Screen.Product.route) {
            ProductScreen(viewModel,navController)
        }
        composable(route = Screen.AuthenticationScreen.route) {
            AuthenticationScreen(navController,AuthenticationActivity(), viewModel)
        }
        composable(route = Screen.NewAccount.route) {
           NewAccount(navController, AuthenticationActivity(), viewModel = viewModel)
        }
        composable(route = Screen.ProductList.route) {
            ProductListScreen(viewModel, navController)
        }
        composable(route = Screen.FavoritesScreen.route) {
            FavoritesScreen(viewModel, navController)
        }
        composable(route = Screen.AccountScreen.route) {
            AccountScreen(viewModel, navController)
        }
        composable(route = Screen.ResiScreen.route) {
            ResiScreen(viewModel, navController)
        }
        composable(route = Screen.GestisciAccountScreen.route) {
            GestisciAccountScreen(viewModel, navController)
        }
        composable(route = Screen.Newsletter.route) {
            NewsletterScreen(viewModel, navController)
        }
        composable(route = Screen.PreferenzaNotifiche.route) {
            PreferenzaNotificheScreen(viewModel, navController)
        }
        composable(route = Screen.DettaglioResiScreen.route) {
            DettaglioResiScreen(viewModel, navController)
        }
        composable(route = Screen.ModificaDati.route) {
            ModificaDati(navController,viewModel, AuthenticationActivity())
        }
        composable(route = Screen.ScegliPalestraScreen.route) {
            ScegliPalestraScreen(viewModel,navController)
        }
        composable(route = Screen.Orders.route) {
            Orders(viewModel,navController)
        }
        composable(route = Screen.AiutoEContatti.route) {
            AiutoEContatti(viewModel,navController)
        }
        composable(route = Screen.CollectOrder.route) {
            CollectOrder(viewModel,navController)
        }
        composable(route = Screen.OrderDetails.route) {
            OrderDetails(viewModel,navController)
        }
        composable(route = Screen.GestisciFitlockerScreen.route) {
            GestisciFitlockerScreen(viewModel, navController)
        }
    }
}