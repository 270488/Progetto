package it.polito.database.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
//import com.google.android.material.bottomNavigation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MainScreen(){
    val navController = rememberNavController()
   Scaffold(
       topBar = {
           TopAppBar(
                   title = {
               Box(
                   modifier = Modifier.fillMaxWidth(),
                   contentAlignment = Alignment.Center)
               {
                   Text(text = "McFIT")
                   //aggiungere il logo
               }
           },
               navigationIcon = {
                   IconButton(
                       onClick = {
                       //TODO
                       }
                   ){
                       BadgedBox( //è il pallino per le notifiche
                           //capire come abilitarlo quando il databse la manda
                           badge = {
                               Badge(modifier = Modifier.size(10.dp)) {

                               }
                           }) {
                           Icon(
                               imageVector = Icons.Default.Notifications ,
                               contentDescription = "Notifiche"
                           )
                       }

                   }
               },
               actions = {
                   IconButton(onClick = { /*TODO*/ }) {
                       Icon(
                           imageVector = Icons.Default.Settings,
                           contentDescription = "Impostazioni"
                       )
                   }
               }
               )
       },

       bottomBar = { BottomBar(navController = navController) }
   ) {
       BottomNavGraph(navController = navController)
   }
}

@Composable
fun BottomBar(navController: NavHostController){
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Category,
        BottomBarScreen.Cart,
        BottomBarScreen.Profile,

    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
//serve ad osservare lo stato ed essere notificati quando questo cambia
    val currentDestination = navBackStackEntry?.destination
    NavigationBar{
        screens.forEach {screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
                )
        }

        }
    }


@Composable
fun RowScope.AddItem(
        screen: BottomBarScreen,
        currentDestination: NavDestination?,
        navController: NavHostController
    ){
        NavigationBarItem(
            label = {
                Text(text = screen.title)
            },
            icon = {
                Icon(imageVector = screen.icon,
                    contentDescription = "Navigation Icon"
                )
            },
            selected = currentDestination?.hierarchy?.any(){
                it.route == screen.route
            } == true,
            onClick = {
                navController.navigate(screen.route)
            }
        )
    }
