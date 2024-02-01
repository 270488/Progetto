package it.polito.database.ui.theme

//import com.google.android.material.bottomNavigation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import it.polito.database.AppViewModel
import it.polito.database.R



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MainScreen(viewModel: AppViewModel){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val schermateSmallTopBar = listOf(
        Screen.Settings.route,
        Screen.Notifications.route,
        Screen.Product.route,
        Screen.ProductList.route,
        Screen.FavoritesScreen.route,
        Screen.AccountScreen.route
    )
        Scaffold(

                topBar = {
                    if(currentDestination?.route !== Screen.AuthenticationScreen.route
                        && currentDestination?.route !== Screen.NewAccount.route) {
                    if (currentDestination?.route in schermateSmallTopBar)
                        SmallTopAppBar(navController = navController, viewModel = viewModel)
                    else
                        TopBar(navController = navController)

                }},
                bottomBar = {
                    if(currentDestination?.route !== Screen.AuthenticationScreen.route
                        && currentDestination?.route !== Screen.NewAccount.route) {
                        BottomBar(navController = navController)
                    }
                }

   )
   {
       NavGraph(navController = navController,viewModel)
   }
    }


@SuppressLint("SuspiciousIndentation")
@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        Screen.Home,
        Screen.Category,
        Screen.Cart,
        Screen.Profile,
        //Screen.Product

    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
//serve ad osservare lo stato ed essere notificati quando questo cambia
    val currentDestination = navBackStackEntry?.destination

        NavigationBar(
            containerColor = MaterialTheme.colorScheme.onBackground) {
            screens.forEach { screen ->
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
        screen: Screen,
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
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent,
                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                unselectedIconColor = MaterialTheme.colorScheme.tertiary,
            ),
            selected = currentDestination?.hierarchy?.any(){
                it.route == screen.route
            } == true,
            onClick = {
                navController.navigate(screen.route)
            }
        )
    }
@Composable
fun TopBar(navController: NavHostController) {
    val screens = listOf(
        Screen.Notifications,
        Screen.Settings,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
//serve ad osservare lo stato ed essere notificati quando questo cambia
    val currentDestination = navBackStackEntry?.destination

    if (currentDestination?.route !== Screen.AuthenticationScreen.route ) {
        NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
            screens.forEach { screen ->
                AddItem2(
                    navController = navController
                )
            }

        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItem2(navController: NavHostController){
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center)
            {
                //Text(text = "McFIT")
                Image(painter = painterResource(id = R.drawable.logomcfit), contentDescription = "")
            //aggiungere il logo
            }
                },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor =  MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.navigate(Screen.Notifications.route)
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.tertiary
                ),
            ){
                BadgedBox( //è il pallino per le notifiche
                    //capire come abilitarlo quando il databse la manda
                        badge = {
                            Badge(modifier = Modifier.size(10.dp)) {
                            }
                        })
                {
                    Icon(
                        imageVector = Screen.Notifications.icon,
                        contentDescription = "Notifiche"
                    )
                }
            }
                         },
        actions = {
            IconButton(onClick = {  navController.navigate(Screen.Settings.route) },
                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.tertiary)) {
                Icon(
                    imageVector = Screen.Settings.icon,
                    contentDescription = "Impostazioni"
                )
            }
        }
    )
}
@SuppressLint("SuspiciousIndentation")
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SmallTopAppBar(navController: NavHostController, viewModel: AppViewModel) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
        NavigationBar(containerColor = MaterialTheme.colorScheme.primary){
                AddItem3(
                    navController = navController,
                    currentDestination = currentDestination, viewModel
                    )
            }
        }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItem3(
    navController: NavHostController,
    currentDestination: NavDestination?, viewModel: AppViewModel
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center)
            {
                when (currentDestination?.route) {
                    Screen.Notifications.route -> Text(text = "Notifiche")
                    Screen.Settings.route -> Text(text = "Impostazioni")
                    Screen.Product.route -> Text(text = "")
                    Screen.ProductList.route-> Text(text = viewModel.sottocat)
                    Screen.FavoritesScreen.route-> Text(text = "I miei preferiti")
                    Screen.AccountScreen.route-> Text(text = "")
                }
                //tante condizioni quante sono le schermate che hanno questa top Bar
                //( con freccia per tornare indietro, impostazioni a destra
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor =  MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.navigateUp()
                }
            ){
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back" ,
                    )
                }
        },
        actions = {
            if(currentDestination?.route !== Screen.Settings.route
                && currentDestination?.route !== Screen.NewAccount.route){
            IconButton(
                onClick = {
                    navController.navigate(Screen.Settings.route)
                }
            ) {
                Icon(
                    imageVector = Screen.Settings.icon,
                    contentDescription = "Impostazioni"
                )
            }
            }
        }
    )
}





