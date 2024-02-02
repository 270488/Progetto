package it.polito.database.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.polito.database.AppViewModel
import it.polito.database.R
import it.polito.database.ui.theme.Screen


@Composable
fun ProfileScreen(viewModel: AppViewModel, navController: NavHostController) {
    Column(
        modifier = Modifier
            .padding(top = 86.dp)
    ) {
        // Icona e Scritte
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Icona
            Column(
                modifier = Modifier
                    .weight(.5F)
            ) {
                val profileIcon = R.drawable.profile
                Image(
                    painter = painterResource(id = profileIcon),
                    contentDescription = "Profile icon",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(MaterialTheme.shapes.medium)
                )
            }

            // Scritte
            Column(
                modifier = Modifier
                    .weight(.5F)
                    .padding(top = 36.dp)
            ) {
                Text(text = "Ciao,", modifier = Modifier.fillMaxWidth())
                Text(text = "Pippo", modifier = Modifier.fillMaxWidth())
                Text(
                    text = "Cambia account/Esci",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Screen.AuthenticationScreen.route)

                            //TODO aggiungere un alert per chiedere conferma
                        }
                        .fillMaxWidth()
                )
            }
        }

        // Card con opzioni
        Opzioni(navController)
    }
}

@Composable
fun Opzioni(navController: NavHostController) {
    val elenco = listOf(
        "I miei ordini",
        "I miei preferiti",
        "I miei resi",
        "Il mio account",
        "Gestisci FitLocker"
    )
    elenco.forEach { name ->
        Row(
            modifier = Modifier
                .padding(12.dp)
                .animateContentSize()
        )  {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium)
                )
            }

            IconButton(onClick = {
                if (name =="I miei preferiti" )
                    navController.navigate(Screen.FavoritesScreen.route)
                else if (name =="I miei resi" )
                    navController.navigate(Screen.ResiScreen.route)
                else if (name == "Il mio account")
                    navController.navigate(Screen.AccountScreen.route)
                //TODO else if () per le altre sezioni
            }) {
                Icon(
                    imageVector =  Icons.Filled.KeyboardArrowRight,
                    contentDescription = "selezione"
                )
            }
        }
    }
}
