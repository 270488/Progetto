package it.polito.database.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.FindUrl
import it.polito.database.database
import it.polito.database.ui.theme.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DettaglioResiScreen (viewModel: AppViewModel, navController: NavController) {
    val codiceReso=viewModel.resoSelezionato

    var reso= database.child("resi").child(codiceReso)

    var ordine by remember { mutableStateOf("") }
    var stato by remember { mutableStateOf("") }
    var prodotti by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var scadenza by remember { mutableStateOf("") }

    reso.addValueEventListener(object: ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            ordine=dataSnapshot.child("ordine").value.toString()
            stato=dataSnapshot.child("stato").value.toString()
            prodotti=dataSnapshot.child("prodotti").value.toString()
            scadenza=dataSnapshot.child("scadenza").value.toString()

        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })


    val prod=viewModel.products.observeAsState(emptyList()).value
        .filter{it.child("nome").value.toString()==prodotti}
    var prezzo=""
    prod.forEach{
        p-> prezzo=p.child("prezzo").value.toString()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier= Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .verticalScroll(rememberScrollState())
            .padding(top = 90.dp, bottom = 110.dp)
    ){
        DettaglioResoCard(navController,viewModel= viewModel, prezzo= prezzo,prodotto = prodotti, scadenza = scadenza, numeroOrdine = ordine, stato = stato, url = url)
        Row {
            istruzioniConsegna()
            Button(onClick = { }) {
                Text(text = "Annulla reso")
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DettaglioResoCard(navController: NavController,viewModel: AppViewModel,prezzo: String, prodotto: String, scadenza: String, numeroOrdine: String, stato: String, url:String){



    Card(modifier = Modifier.padding(5.dp)){
        Column(){
            Row(){
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .width(150.dp),
                    contentScale = ContentScale.Crop
                )
                Column {
                    Text(text = prodotto)
                    Text(text = "Ordine No. " + numeroOrdine)
                    Text(text = "Scadenza reso: " + scadenza)
                }
            }
            Row(){
                Text(text = "Stato della spedizione")
                if(stato=="avviato"){
                    //TODO Immagine/icona del reso avviato
                }
                else if(stato=="scaduto"){
                    //TODO Immagine/icona del reso scaduto
                }
                else if(stato=="completato"){
                    //TODO Immagine/icona del reso completato
                }
            }
            Row() {
                Text(text = "Pacco da riconsegnare presso: ")
            }
            Row(){
                Card(){
                    Text(text = viewModel.lockerSelezionato)
                }
                if(stato!="scaduto"){
                    Card(onClick = {navController.navigate(Screen.ScegliPalestraScreen.route)}){
                        Text(text = "Scegli un'altra palestra")
                    }
                }

            }
            Row(){
                //TODO icona dello stato del reso

            }
            Column(){
                Text(text = "Riepilogo")
                Text(text = "Il reso stimato è di euro "+prezzo)
                Text(text = "Il reso sarà accreditato sul tuo metodo di pagamento predefinito")

            }


        }

    }

}
@Composable
private fun istruzioniConsegna() {
    var isDialogOpen by remember { mutableStateOf(false) }

    Button(
        onClick = { isDialogOpen = true },
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Istruzioni per la consegna")
    }


    if (isDialogOpen) {

        Popup(
            onDismissRequest = { isDialogOpen = false },
            modifier = Modifier.fillMaxSize()
        ) {
            // Contenuto del tuo popup personalizzato
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Column(){
                    Text("Istruzioni per la consegna")
                    Text("Consegna")
                    Text("Consegna il prodotto presso la reception della palestra in cui l’hai ritirato, se vuoi cambiare palestra ti basterà cliccare sulla scritta “Scegli un’altra palestra”")
                    Text("Imballaggio")
                    Text("Tutto quello che devi fare per preparare il pacco è inserire il prodotto nella scatola originale. Non dovrai stampare alcuna etichetta!")
                    Text("Rimborso")
                    Text("Non appena il tuo pacco verrà ricevuto dal magazzino riceverai un rimborso completo in seguito ad una verifica.")

                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { isDialogOpen = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Chiudi")
                }
            }
        }

    }
}
@Composable
fun Popup(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = CircleShape,
    content: @Composable (Modifier) -> Unit
) {


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = Color.Black.copy(alpha = 0.5f),
                shape = shape
            )
            .clip(shape)
            .clickable(onClick = onDismissRequest)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = shape
                )
                .padding(16.dp)
                .clip(shape)
        ) {
            content(Modifier)
        }
    }
}
