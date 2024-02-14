package it.polito.database.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import it.polito.database.AppViewModel
import it.polito.database.R
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.fontFamily

@Composable
fun GestisciAccountScreen(viewModel: AppViewModel, navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = 80.dp)
    ) {
        // Icona e Scritte
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Profile icon",
            modifier = Modifier
                .size(160.dp)
                .border(1.dp, MaterialTheme.colorScheme.tertiary, CircleShape)
        )
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Icona di pedice",
            tint = Color.Yellow, // Colore dell'icona
            modifier = Modifier.offset(x = (-43).dp,y = (-25).dp,)
                .size(40.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Pippo",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontFamily = fontFamily,
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Edit(navController)


    }
}
@Composable
fun Edit(navController: NavHostController) {
    val elenco = listOf(
        "Modifica dati",
        "Gestisci FitLocker",
        "Cambia account",
        "Logout"
    )
    Column {
        elenco.forEach { name ->
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .background(
                        if (name == "Logout") MaterialTheme.colorScheme.secondaryContainer
                        else MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        if (name == "Logout" || name == "Cambia Account") {
                            //TODO inserire alert
                            navController.navigate(Screen.AuthenticationScreen.route)
                        }

                        //TODO else if () per le altre sezioni
                    }
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = name,
                        fontFamily = fontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (name == "Logout") MaterialTheme.colorScheme.tertiary
                        else MaterialTheme.colorScheme.onPrimary
                    )

                }
            }
        }
    }
}