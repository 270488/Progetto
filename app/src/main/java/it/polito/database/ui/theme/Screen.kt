package it.polito.database.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen (
    val route: String,
    val title: String,
    val icon: ImageVector
 ) {
    object Home: Screen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    object Category: Screen(
        route = "category",
        title = "Category",
        icon = Icons.Default.List
    )
    object Cart: Screen(
        route = "cart",
        title = "Cart",
        icon = Icons.Default.ShoppingCart
    )
    object Checkout: Screen(
        route = "checkout",
        title = "Checkout",
        icon = Icons.Default.ShoppingCart
    )
    object Profile: Screen(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )
    object Notifications: Screen(
        route = "notifications",
        title = "Notifications",
        icon = Icons.Default.Notifications
    )
    object Settings: Screen(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
    object AuthenticationScreen: Screen(
        route = "authenticationScreen",
        title = "AuthenticationScreen",
        icon = Icons.Default.Person
    )
    object NewAccount: Screen(
        route = "newAccount",
        title = "NewAccount",
        icon = Icons.Default.Person
    )
    object Product: Screen(
        route = "product",
        title = "Product",
        icon = Icons.Default.Info
    )
    object ProductList: Screen(
        route = "productList",
        title = "ProductList",
        icon = Icons.Default.Info
    )
    object FavoritesScreen: Screen(
        route = "favoritesScreen",
        title = "Favorites",
        icon = Icons.Default.FavoriteBorder
    )
    object AccountScreen: Screen(
        route = "accountScreen",
        title = "AccountScreen",
        icon = Icons.Default.AccountBox
    )
    object ResiScreen: Screen(
        route = "resiScreen",
        title = "ResiScreen",
        icon = Icons.Default.AccountBox
    )
    object GestisciAccountScreen: Screen(
        route = "gestisciAccount",
        title = "GestisciAccount",
        icon = Icons.Default.Edit
    )
}