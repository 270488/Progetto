package it.polito.database.ui.theme

import android.provider.ContactsContract.CommonDataKinds.Im
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen (
    val route: String,
    val title: String,
    val icon: ImageVector
 ) {
    object Home: BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    object Category: BottomBarScreen(
        route = "categoria",
        title = "Categoria",
        icon = Icons.Default.List
    )
    object Cart: BottomBarScreen(
        route = "carrello",
        title = "Carrello",
        icon = Icons.Default.ShoppingCart
    )
    object Profile: BottomBarScreen(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )

}