package it.polito.database.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Menu
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
    object GestisciFitlockerScreen: Screen(
        route = "gestisciFitlocker",
        title = "GestisciFitlocker",
        icon = Icons.Default.Edit
    )
    object Newsletter: Screen(
        route = "newsletter",
        title = "Newsletter",
        icon = Icons.Default.MailOutline
    )
    object PreferenzaNotifiche: Screen(
        route = "preferenzaNotifiche",
        title = "PreferenzaNotifiche",
        icon = Icons.Default.Notifications
    )
    object DettaglioResiScreen: Screen(
        route = "dettaglioResi",
        title = "DettaglioResi",
        icon = Icons.Default.Edit
    )
    object ModificaDati: Screen(
        route = "modificaDati",
        title = "ModificaDati",
        icon = Icons.Default.Edit
    )
    object ScegliPalestraScreen: Screen(
        route = "scegliPalestra",
        title = "ScegliPalestra",
        icon = Icons.Default.Edit
    )
    object Orders: Screen(
        route = "ordersScreen",
        title = "OrdersScreen",
        icon = Icons.Default.Edit
    )
    object AiutoEContatti: Screen(
        route = "aiutoEContatti",
        title = "AiutoEContatti",
        icon = Icons.Default.Call
    )
    object OrderDetails: Screen(
        route = "orderDetails",
        title = "OrderDetails",
        icon = Icons.Default.Call
    )
    object CollectOrder: Screen(
        route = "collectOrder",
        title = "CollectOrder",
        icon = Icons.Default.Call
    )
    object CorriereHome: Screen(
        route = "corriereHome",
        title = "CorriereHome",
        icon = Icons.Default.AccountBox
    )
    object CorriereProfile: Screen(
        route = "corriereProfile",
        title = "CorriereProfile",
        icon = Icons.Default.AccountBox
    )

    object CorriereDetails: Screen(
        route = "corriereDetails",
        title = "CorriereDetails",
        icon = Icons.Default.Lock
    )
    object DeliverOrder: Screen(
        route = "deliverOrder",
        title = "DeliverOrder",
        icon = Icons.Default.Lock
    )
    object AreaLegale: Screen(
        route = "areaLegale",
        title = "AreaLegale",
        icon = Icons.Default.Menu
    )

}