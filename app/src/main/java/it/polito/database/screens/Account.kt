
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.navigation.NavHostController
import it.polito.database.AppViewModel
import it.polito.database.R
import it.polito.database.ui.theme.Screen


@Composable
fun AccountScreen(viewModel: AppViewModel, navController: NavHostController) {
    Row(
        modifier = Modifier
            .padding(top = 86.dp)
    ) {
        // Icona e Scritte
        Column(
            modifier = Modifier
            .padding(vertical = 16.dp))
            {
                val profileIcon = R.drawable.profile
                Image(
                    painter = painterResource(id = profileIcon),
                    contentDescription = "Profile icon",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Pippo", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)

                Spacer(modifier = Modifier.height(16.dp))

                Opzioni(navController)

        }
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
                if (name =="Logout" ) {
                    //TODO inserire alert
                    navController.navigate(Screen.AuthenticationScreen.route)
                }
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
