package it.polito.database.screens

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
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
        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 70.dp, bottom = 100.dp)
        )
        {
            SettingsListScreen(viewModel,navController)
        }

    }

@SuppressLint("SuspiciousIndentation")
@Composable
fun SettingsListScreen(viewModel: AppViewModel, navController: NavController){

    val listaImpostazioni = listOf(
        "Preferenze sulle notifiche",
        "Paese/Lingua",
        "Area Legale",
        "Aiuto e contatti"
    )
    Column(
        modifier = Modifier.padding(top = 80.dp, start = 16.dp, end = 16.dp),
    ) {
        listaImpostazioni.forEach { name ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                //horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    //.padding(16.dp)
                    .height(70.dp)
                    .fillMaxWidth()
                    .animateContentSize()
                    .clickable {
                        if (name == "Preferenze sulle notifiche")
                            navController.navigate(Screen.PreferenzaNotifiche.route)
                        else if (name == "Paese/Lingua")
                        //TODO
                        else if (name == "Area Legale")
                            navController.navigate(Screen.AreaLegale.route)
                        else if (name == "Aiuto e contatti")
                            navController.navigate(Screen.AiutoEContatti.route)
                    }
            )  {
                Text(
                    text = name,
                    fontFamily = fontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if(name == "Paese/Lingua"){
                    Spacer(modifier = Modifier.width(30.dp))
                    Image(
                        painter = painterResource(id = R.drawable.italy_flag),
                        contentDescription = "Profile icon",
                        modifier = Modifier.size(25.dp))
                }

            }
            if(name != "Aiuto e contatti")
            Divider(thickness = 2.dp, color = Color.Black)
        }
    }

}


