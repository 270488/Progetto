
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun AccountScreen(viewModel: AppViewModel, navController: NavHostController) {
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
                .size(140.dp)
                .border(1.dp, MaterialTheme.colorScheme.tertiary, CircleShape)
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

        Opzioni(navController)


    }
}

@Composable
fun Opzioni(navController: NavHostController) {
    val elenco = listOf(
        "Accesso e sicurezza",
        "Gestisci account",
        "Iscriviti alla newsletter",
        "Preferenze cookies",
        "Logout"
    )
    Column {
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
        elenco.forEach { name ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .background(
                        if(name == "Logout") MaterialTheme.colorScheme.secondaryContainer
                        else MaterialTheme.colorScheme.secondary
                    )
                    .padding(12.dp)
                    .fillMaxWidth()
                    .animateContentSize()
            )  {
                Text(
                    text = name,
                    fontFamily = fontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if(name == "Logout") MaterialTheme.colorScheme.tertiary
                            else MaterialTheme.colorScheme.onPrimary
                )
                IconButton(onClick = {
                    if (name =="Logout" ) {
                        //TODO inserire alert
                        navController.navigate(Screen.AuthenticationScreen.route)
                    } else if (name =="Gestisci account" ) {
                        navController.navigate(Screen.GestisciAccountScreen.route)
                    } else if (name =="Iscriviti alla newsletter" ) {
                        //TODO inserire alert
                        navController.navigate(Screen.Newsletter.route)
                    }
                    //TODO else if () per le altre sezioni
                }) {
                    Icon(
                        painter =  painterResource(id = R.drawable.frecciadx),
                        tint = MaterialTheme.colorScheme.tertiary,
                        contentDescription = "selezione",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
        }
    }

}
