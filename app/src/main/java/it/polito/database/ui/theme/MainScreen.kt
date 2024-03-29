package it.polito.database.ui.theme

//import com.google.android.material.bottomNavigation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        Screen.NotificationsCorriere.route,
        Screen.Product.route,
        Screen.ProductList.route,
        Screen.FavoritesScreen.route,
        Screen.AccountScreen.route,
        Screen.GestisciAccountScreen.route,
        Screen.ResiScreen.route,
        Screen.DettaglioResiScreen.route,
        Screen.ScegliPalestraScreen.route,
        Screen.Orders.route,
        Screen.ModificaDati.route,
        Screen.AiutoEContatti.route,
        Screen.CollectOrder.route,
        Screen.OrderDetails.route,
        Screen.Newsletter.route,
        Screen.PreferenzaNotifiche.route,
        Screen.AreaLegale.route,
        Screen.PreferenzeCookies.route,
        Screen.GestisciFitlockerScreen.route,
        Screen.DeliverOrder.route,
        Screen.CorriereDetails.route,
        Screen.AccessoESicurezza.route,
        Screen.PaeseELingua.route,
        Screen.Checkout.route
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
        Screen.Profile
    )
    val screensCorriere = listOf(
        Screen.CorriereHome,
        Screen.CorriereProfile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
//serve ad osservare lo stato ed essere notificati quando questo cambia
    val currentDestination = navBackStackEntry?.destination

    if(currentDestination?.route !== Screen.CorriereHome.route
        && currentDestination?.route !== Screen.CorriereProfile.route
        && currentDestination?.route !== Screen.CorriereDetails.route)
    {
        NavigationBar(
            modifier = Modifier
                .drawBehind {
                    drawLine(
                        color = Color(0xFFFFED37),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 4.dp.toPx()
                    )
                },
            containerColor = MaterialTheme.colorScheme.onBackground)
        {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }else{

        NavigationBar(
            modifier = Modifier
                .drawBehind {
                    drawLine(
                        color = Color(0xFFFFED37),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 4.dp.toPx()
                    )
                },
            containerColor = MaterialTheme.colorScheme.onBackground)
        {
            screensCorriere.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
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
        /*label = {
            Text(text = screen.title, fontFamily = fontFamily, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        },*/
        icon = {
            Icon(imageVector = screen.icon,
                contentDescription = "Navigation Icon",
                modifier = Modifier.size(40.dp) //.padding(bottom = 3.dp)
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
    val screensCorriere = listOf(
        Screen.NotificationsCorriere,
        Screen.Settings,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
//serve ad osservare lo stato ed essere notificati quando questo cambia
    val currentDestination = navBackStackEntry?.destination

    if (currentDestination?.route !== Screen.AuthenticationScreen.route &&
        currentDestination?.route !== Screen.NewAccount.route &&
        currentDestination?.route !== Screen.CorriereHome.route
        && currentDestination?.route !== Screen.CorriereProfile.route) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            screens.forEach { screen ->
                AddItem2(
                    navController = navController
                )
            }

        }
    }
    if(currentDestination?.route == Screen.CorriereHome.route
        || currentDestination?.route == Screen.CorriereProfile.route)
    {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            screensCorriere.forEach { screen ->
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    TopAppBar(
        modifier = Modifier.padding(top = 6.dp),
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center)
            {
                //Text(text = "McFIT")
                Image(
                    painter = painterResource(id = R.drawable.logomcfit),
                    contentDescription = "",
                )
                //aggiungere il logo
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor =  MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            IconButton(
                onClick = {
                    if(currentDestination?.route == Screen.CorriereHome.route
                        || currentDestination?.route == Screen.CorriereProfile.route)
                    {
                        navController.navigate(Screen.NotificationsCorriere.route)
                    } else navController.navigate(Screen.Notifications.route)


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
                        contentDescription = "Notifiche",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        },
        actions = {

            IconButton(onClick = {  navController.navigate(Screen.Settings.route) },
                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.tertiary)) {
                Icon(
                    imageVector = Screen.Settings.icon,
                    contentDescription = "Impostazioni",
                    modifier = Modifier.size(40.dp)
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
    NavigationBar(
        containerColor =
        if(currentDestination?.route == Screen.Settings.route
            || currentDestination?.route == Screen.PreferenzaNotifiche.route
            || currentDestination?.route == Screen.AreaLegale.route
            || currentDestination?.route == Screen.AiutoEContatti.route
            ||currentDestination?.route == Screen.PaeseELingua.route
        ){
            MaterialTheme.colorScheme.background
        } else MaterialTheme.colorScheme.primary
    ){
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
        modifier = Modifier
            .padding(top = 6.dp),
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center)
            {
                when (currentDestination?.route) {
                    Screen.Notifications.route -> Text(text = "Notifiche", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.NotificationsCorriere.route -> Text(text = "Notifiche", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.Settings.route -> Text(text = "Impostazioni", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.Product.route -> Text(text = "", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.ProductList.route-> Text(text = viewModel.sottocat, fontFamily = fontFamily, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Screen.FavoritesScreen.route-> Text(text = "I miei preferiti", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.AccountScreen.route-> Text(text = "Il mio account", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.GestisciAccountScreen.route-> Text(text = "Gestisci Account", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.ResiScreen.route->Text(text = "I miei resi", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.DettaglioResiScreen.route->Text(text = "Dettaglio reso", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.ScegliPalestraScreen.route->Text(text = "Seleziona palestra", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.Orders.route->Text(text = "I miei ordini", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.ModificaDati.route->Text(text = "Modifica dati", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.AiutoEContatti.route->Text(text = "Aiuto e contatti", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.OrderDetails.route->Text(text = "Dettagli ordine", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.CollectOrder.route->Text(text = "Ritiro ordine", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.GestisciFitlockerScreen.route->Text(text = "Gestisci Fitlocker", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.PreferenzaNotifiche.route->Text(text = "Preferenze Notifiche", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.Newsletter.route->Text(text = "Newsletter", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.AreaLegale.route->Text(text = "Area Legale", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.PreferenzeCookies.route->Text(text = "Preferenze Cookies", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.AccessoESicurezza.route->Text(text = "Accesso e sicurezza", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Screen.PaeseELingua.route->Text(text = "Paese e lingua", fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                //tante condizioni quante sono le schermate che hanno questa top Bar
                //( con freccia per tornare indietro, impostazioni a destra
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor =
            if(currentDestination?.route == Screen.Settings.route
                || currentDestination?.route == Screen.PreferenzaNotifiche.route
                || currentDestination?.route == Screen.AreaLegale.route
                || currentDestination?.route == Screen.AiutoEContatti.route
                ||currentDestination?.route == Screen.PaeseELingua.route
            ){
                MaterialTheme.colorScheme.background
            }else MaterialTheme.colorScheme.primary,

            navigationIconContentColor =
            if(currentDestination?.route == Screen.Settings.route
                || currentDestination?.route == Screen.PreferenzaNotifiche.route
                || currentDestination?.route == Screen.AreaLegale.route
                || currentDestination?.route == Screen.AiutoEContatti.route
                ||currentDestination?.route == Screen.PaeseELingua.route
            ){
                MaterialTheme.colorScheme.onBackground
            }else MaterialTheme.colorScheme.tertiary,

            actionIconContentColor =
            if(currentDestination?.route == Screen.Settings.route
                || currentDestination?.route == Screen.PreferenzaNotifiche.route
                || currentDestination?.route == Screen.AreaLegale.route
                || currentDestination?.route == Screen.AiutoEContatti.route
                ||currentDestination?.route == Screen.PaeseELingua.route
            ){
                MaterialTheme.colorScheme.onBackground
            }else MaterialTheme.colorScheme.tertiary,

            titleContentColor =
            if(currentDestination?.route == Screen.Settings.route
                || currentDestination?.route == Screen.PreferenzaNotifiche.route
                || currentDestination?.route == Screen.AreaLegale.route
                || currentDestination?.route == Screen.AiutoEContatti.route
                ||currentDestination?.route == Screen.PaeseELingua.route
            ){
                MaterialTheme.colorScheme.onBackground
            }else MaterialTheme.colorScheme.onPrimary,

            ),
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.navigateUp()
                },
                /*colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.tertiary
                )*/
            ){
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back" ,
                    modifier = Modifier.size(40.dp)
                )
            }
        },
        actions = {
            if(currentDestination?.route !== Screen.Settings.route
                && currentDestination?.route !== Screen.NewAccount.route){
                IconButton(
                    onClick = {
                        navController.navigate(Screen.Settings.route)
                    },
                    /*colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.tertiary
                    )*/
                ) {
                    Icon(
                        imageVector = Screen.Settings.icon,
                        contentDescription = "Impostazioni",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            if(currentDestination?.route == Screen.Settings.route){
                Box(modifier = Modifier.size(40.dp))
            }
        }
    )
}




