package it.polito.database.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import it.polito.database.AppViewModel
import it.polito.database.R
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.fontFamily


@Composable
    fun SettingsScreen(viewModel: AppViewModel, navController: NavController) {
    SettingsListScreen(viewModel,navController)
    }

@Composable
fun SettingsListScreen(viewModel: AppViewModel, navController: NavController){

    val listaImpostazioni = listOf(
        "Preferenze sulle notifiche",
        "Paese/Lingua",
        "Area Legale",
        "Aiuto e contatti"
    )
    Column(verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(vertical = 100.dp)
    ) {
        listaImpostazioni.forEach { name ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .background(Color.White)
                    .padding(12.dp)
                    .fillMaxWidth()
                    .animateContentSize()
                    .clickable {
                        if (name =="Preferenze sulle notifiche" )
                            navController.navigate(Screen.PreferenzaNotifiche.route)
                        else if (name =="Paese/Lingua" )
                                //TODO
                        else if (name == "Area Legale")
                            navController.navigate(Screen.AreaLegale.route)
                        else if(name=="Aiuto e contatti")
                            navController.navigate(Screen.AiutoEContatti.route)
                    }
            )  {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = name,
                        fontFamily = fontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    if(name == "Paese/Lingua"){
                        Spacer(modifier = Modifier.width(30.dp))
                        Image(
                            painter = painterResource(id = R.drawable.italy_flag),
                            contentDescription = "Profile icon",
                            modifier = Modifier.size(60.dp))
                    }
                }
            }
            if(name != "Aiuto e contatti")
            Divider(thickness = 1.dp, color = Color.Black)
        }
    }

}


