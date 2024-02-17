package it.polito.database.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.values
import it.polito.database.AppViewModel
import it.polito.database.FindUrl
import it.polito.database.database
import it.polito.database.ui.theme.Screen
import okhttp3.internal.notify
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random


@Composable
fun ResiScreen(viewModel: AppViewModel, navController: NavController){

    var id=viewModel.uid

    var listaResi by remember { mutableStateOf<List<String>>(emptyList()) }

    var resi= database.child("utenti").child(id).child("resi")
    resi.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { //fa una foto al db in quel momento e la mette in dataSnapshot
            // Itera sui figli del nodo
            var list= mutableListOf<String>()


            for (childSnapshot in dataSnapshot.children) { //prende i figli di prodotti, quindi 0, 1...
                // Aggiungi il prodotto alla lista
                list.add(childSnapshot.key.toString())


            }
            listaResi=list
        }
        override fun onCancelled(databaseError: DatabaseError) {
            // Gestisci gli errori qui
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })
    println(listaResi)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier= Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .verticalScroll(rememberScrollState())
            .padding(top = 90.dp, bottom = 110.dp)
    ){

        Button(onClick = { aggiungiReso("Pink Leggins", "1542", id) }, 
            modifier = Modifier.background(Color.Yellow)) {
            Text(text = "Aggiungi Reso")

        }
        
        listaResi.forEach{
            i-> resiCard(numeroReso = i, viewModel, navController)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun resiCard(numeroReso: String, viewModel: AppViewModel, navController: NavController){

    var reso=viewModel.resi.observeAsState(emptyList()).value
        .filter { it.key.toString() == numeroReso }

    var ordine=""
    var stato=""
    var prodotti=""
    var url=""
    var scadenza=""

    reso.forEach{
        r->
       ordine=r.child("ordine").value.toString()
       stato=r.child("stato").value.toString()
       prodotti=r.child("prodotti").value.toString()
        url= FindUrl(fileName = prodotti+".jpg")

        scadenza= r.child("scadenza").value.toString()
    }

    Card(modifier = Modifier.padding(5.dp),
        onClick = {navController.navigate(Screen.DettaglioResiScreen.route)
            viewModel.resoSelezionato=numeroReso
    } ){
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
                    Text(text = prodotti)
                    Text(text = "Ordine No. " + ordine)
                }
            }
            Row(){
                Text(text = "Stato reso: " + stato)
                Text(text = "Scadenza reso: "+scadenza)
            }


        }

    }
}

@SuppressLint("NewApi")
fun aggiungiReso(prodotto: String, ordine: String, uid: String){
    var resi= database.child("resi")
    val numeroReso= Random.nextInt(10000, 100000)
    val dataAttuale = LocalDate.now()
    val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val scadenza = dataAttuale.plusDays(30).format(formato) //aggiunge 30 giorni

    resi.child(numeroReso.toString()).child("ordine").setValue(ordine)
    resi.child(numeroReso.toString()).child("stato").setValue("avviato")
    resi.child(numeroReso.toString()).child("uid").setValue(uid)
    resi.child(numeroReso.toString()).child("prodotti").setValue(prodotto)
    resi.child(numeroReso.toString()).child("scadenza").setValue(scadenza)

    database.child("utenti").child(uid).child("resi").child(numeroReso.toString()).setValue(prodotto)
}
