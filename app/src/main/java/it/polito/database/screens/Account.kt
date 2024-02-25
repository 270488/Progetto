
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.R
import it.polito.database.database
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.fontFamily


@Composable
fun AccountScreen(viewModel: AppViewModel, navController: NavHostController) {

    var credenziali= database.child("utenti").child(viewModel.uid)
    var nome by remember { mutableStateOf("") }

    credenziali.addValueEventListener(object: ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            nome=dataSnapshot.child("nome").value.toString()
        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = 80.dp)
    ) {
        // Icona e Scritte
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(CircleShape)
                .size(140.dp)
                .border(1.dp, MaterialTheme.colorScheme.tertiary, CircleShape)
        ){
            Image(
                painter = painterResource(id = R.drawable.profile_picture),
                contentDescription = "Profile icon",
                contentScale = ContentScale.Crop,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = nome.capitalize(),
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
                    } else if (name =="Preferenze cookies" ) {
                //TODO inserire alert
                navController.navigate(Screen.PreferenzeCookies.route)
            } else if (name =="Accesso e sicurezza" ) {
                //TODO inserire alert
                navController.navigate(Screen.AccessoESicurezza.route)
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
