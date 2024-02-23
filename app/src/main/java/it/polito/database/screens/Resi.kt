package it.polito.database.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import it.polito.database.ui.theme.fontFamily
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
                if(!list.contains(childSnapshot.key.toString())){
                    list.add(childSnapshot.key.toString())
                }

            }
            listaResi=list
        }
        override fun onCancelled(databaseError: DatabaseError) {
            // Gestisci gli errori qui
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })


    Column (horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier= Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .verticalScroll(rememberScrollState())
            .padding(top = 90.dp, bottom = 110.dp))
    {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            if(listaResi.isEmpty()){
                Text(
                    text = "Non sono presenti resi",
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(top = 150.dp, bottom = 16.dp)
                )
            }
            else{
                listaResi.forEach{
                        i-> resiCard(numeroReso = i, viewModel, navController)
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.tertiary)
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                shape = RoundedCornerShape(3.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ),
                onClick = {
                    navController.navigate(Screen.Orders.route)
                }
            ){
                Text(
                    text = "I miei ordini",
                    fontFamily = fontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun resiCard(numeroReso: String, viewModel: AppViewModel, navController: NavController){

    var reso= database.child("resi").child(numeroReso)

    var ordine by remember { mutableStateOf("") }
    var stato by remember { mutableStateOf("") }
    var prodotti by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var scadenza by remember { mutableStateOf("") }

    reso.addValueEventListener(object: ValueEventListener{
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

    url= FindUrl(fileName = prodotti+".jpg")

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor= MaterialTheme.colorScheme.onSecondary,
        ),
        border = BorderStroke(2.dp, Color.Black),
        onClick = {
            navController.navigate(Screen.DettaglioResiScreen.route)
            viewModel.resoSelezionato=numeroReso
        }
    )
    {
        Column(){
            Row(){
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .height(90.dp)
                        .width(150.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = prodotti,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily,
                    )
                    Text(
                        text = "Ordine No. " + ordine,
                        fontSize = 16.sp,
                        fontFamily = fontFamily,
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Stato: " + stato,
                    color =
                    if (stato == "scaduto") MaterialTheme.colorScheme.errorContainer
                    else  MaterialTheme.colorScheme.tertiary,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Scadenza: " + scadenza,
                    fontFamily = fontFamily,
                    fontSize = 16.sp
                )
            }


        }

    }
    Spacer(modifier = Modifier.height(16.dp))
}

@SuppressLint("NewApi")
fun aggiungiReso(prodotto: String, ordine: String, uid: String, viewModel: AppViewModel){
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
