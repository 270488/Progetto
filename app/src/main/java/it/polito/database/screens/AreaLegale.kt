package it.polito.database.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import it.polito.database.AppViewModel
import it.polito.database.ui.theme.fontFamily

@Composable
fun AreaLegaleScreen(viewModel: AppViewModel, navController: NavHostController) {

 Column(modifier = Modifier
     .verticalScroll(rememberScrollState())
     .fillMaxSize()
     .padding(top = 100.dp, bottom = 100.dp)
     .padding(horizontal = 16.dp)
 ) {
     Text(
         text = "Condizioni generali d'utilizzo",
         color = Color.Black,
         fontSize = 22.sp,
         fontFamily = fontFamily,
         fontWeight = FontWeight.Bold
         )
     Text(text =
             "Salvo ove diversamente specificato, le condizioni d’uso di questa Applicazione esposte in questa sezione hanno validità generale.\n" +
             "\n" +
             "Ulteriori condizioni d’uso o d’accesso applicabili in particolari situazioni sono espressamente indicate in questo documento.\n" +
             "\n" +
             "Utilizzando questa Applicazione l’Utente dichiara di soddisfare i seguenti requisiti:\n" +
             "\n" +
             "Non ci sono restrizioni riferite agli Utenti rispetto al fatto che essi siano Consumatori o Utenti Professionisti;\n" +
             "Registrazione\n" +
             "Per usufruire del Servizio l’Utente può aprire un account indicando tutti i dati e le informazioni richieste in maniera completa e veritiera.\n" +
             "Non è possibile usufruire del Servizio senza aprire un account Utente.\n" +
             "\n" +
             "È responsabilità degli Utenti conservare le proprie credenziali d’accesso in modo sicuro e preservarne la confidenzialità. A tal fine, gli Utenti devono scegliere una password che corrisponda al più alto livello di sicurezza disponibile su questa Applicazione.\n" +
             "\n" +
             "Creando un account l’Utente accetta di essere pienamente responsabile di ogni attività posta in atto con le sue credenziali d’accesso. Gli Utenti sono tenuti a informare il Titolare immediatamente e univocamente tramite i recapiti indicati in questo documento qualora ritengano che le proprie informazioni personali, quali ad esempio l’account Utente, le credenziali d’accesso o dati personali, siano state violate, illecitamente diffuse o sottratte.\n" +
             "\n" +
             "Chiusura account\n" +
             "L’Utente è libero di chiudere il proprio account e cessare l’utilizzo del Servizio in qualsiasi momento, seguendo questa procedura:\n" +
             "\n" +
             "Contattando il Titolare ai recapiti in questo documento.\n" +
             "Sospensione e cancellazione account\n" +
             "Il Titolare si riserva il diritto di sospendere o cancellare l’account di un Utente in qualsiasi momento a propria discrezione e senza preavviso, qualora lo ritenga inopportuno, offensivo o contrario a questi Termini.\n" +
             "\n" +
             "La sospensione o cancellazione dell’account non da all’Utente alcun diritto di risarcimento, rimborso o indennizzo.\n" +
             "\n" +
             "La sospensione o cancellazione di un account per cause addebitabili all’Utente non esonera l’Utente dal pagamento dei compensi o prezzi eventualmente applicabili.",
         color = Color.Black,
         fontSize = 14.sp,
         fontFamily = fontFamily,
         fontWeight = FontWeight.Normal
     )
 }
}
