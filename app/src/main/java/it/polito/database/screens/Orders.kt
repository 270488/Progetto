package it.polito.database.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.FindUrl
import it.polito.database.database
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.fontFamily
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random



@Composable
fun Orders(viewModel: AppViewModel, navController: NavController){
    var uid=viewModel.uid
    var ordini= database.child("utenti").child(uid).child("ordini")
    var listaOrdini by remember { mutableStateOf<List<String>>(emptyList()) }
    ordini.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            var lista= mutableListOf<String>()
            for (childSnapshot in dataSnapshot.children) {
                lista.add(childSnapshot.key.toString())
            }
            listaOrdini=lista

        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })

    Column (horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier= Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .verticalScroll(rememberScrollState())
            .padding(top = 90.dp, bottom = 110.dp)) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier= Modifier
                .padding(horizontal = 20.dp)
        ){
            if(listaOrdini.isEmpty()){
                Text(
                    text = "Non sono stati effettuati ordini",
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                }
            else{
                listaOrdini.forEach{
                        o->OrdineCard(o, viewModel, navController)
                }
            }
        }



    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdineCard(ordine: String, viewModel: AppViewModel, navController: NavController) {

    var ordiniEffettuati= database.child("ordini").child(ordine)
    var dataConsegna by remember {
        mutableStateOf("")
    }
    var dataOrdine by remember {
        mutableStateOf("")
    }
    var locker by remember {
        mutableStateOf("")
    }
    var sportello by remember {
        mutableStateOf(0L)
    }
    var totale by remember {
        mutableStateOf(0.00)
    }
    var stato by remember {
        mutableStateOf("")
    }
    var uid by remember {
        mutableStateOf("")
    }
    var prodotti by remember{ mutableStateOf<Map<String, Long>>(emptyMap())}
    ordiniEffettuati.addValueEventListener(object: ValueEventListener{
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            dataConsegna=dataSnapshot.child("Data Consegna").value.toString()
            dataOrdine=dataSnapshot.child("Data Ordine").value.toString()
            locker=dataSnapshot.child("Locker").value.toString()
            sportello=dataSnapshot.child("Sportello").value as Long
            totale=dataSnapshot.child("Totale").value as Double
            stato=dataSnapshot.child("stato").value.toString()
            uid=dataSnapshot.child("uid").value.toString()
            var listaProdotti= mutableMapOf<String, Long>()
            for (childSnapshot in dataSnapshot.child("Prodotti").children) {
                listaProdotti[childSnapshot.key.toString()] = childSnapshot.value as Long
            }
            prodotti=listaProdotti
        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })

    Card(
        modifier = Modifier.height(90.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor= MaterialTheme.colorScheme.onSecondary,
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        onClick = {
            viewModel.ordineSelezionato = ordine
            navController.navigate(Screen.OrderDetails.route)
        }
    ){
        Box(modifier = Modifier.fillMaxSize())
        {
            if(stato=="ordinato"){
                //TODO icona ordinato
            }
            else if(stato=="in consegna"){
                //TODO icona in consegna
            }
            else if(stato=="consegnato"){
                //TODO icona in consegna
            }
            else if(stato=="ritirato"){
                //TODO icona in consegna
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                //horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                var url= FindUrl(fileName = prodotti.keys.firstOrNull()+".jpg")
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .width(150.dp),
                    contentScale = ContentScale.Crop
                )

                //Spacer(modifier = Modifier.width(16.dp))

                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                )
                {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        Text(
                            text = "Ordine No: "+ordine,
                            fontFamily = fontFamily,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = totale.toString() + "€",
                            fontFamily = fontFamily,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )

                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        Text(
                            text = "Stato ordine: "+stato,
                            fontFamily = fontFamily,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                    }
                }

               }


        }
    }
    Spacer(modifier = Modifier.height(16.dp))

}
@SuppressLint("NewApi")
fun aggiungiOrdine(viewModel: AppViewModel){
    var uid=viewModel.uid
    val dataAttuale = LocalDate.now()
    val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    var dataOrdine=dataAttuale.format(formato)
    var dataConsegna=dataAttuale.plusDays(1).format(formato)
    var nOrdine= Random.nextInt(10000, 100000)
    var locker=viewModel.lockerSelezionato
    var sportello=0
    var stato="ordinato"
    var totale=viewModel.tot
    var numeroProdotti=0
    var codiceSbloccoUtente= generazioneCodiceCasuale()
    var codiceSbloccoFattorino= generazioneCodiceCasuale()
    var ordini=database.child("ordini")


    ordini.child(nOrdine.toString()).child("stato").setValue(stato)
    ordini.child(nOrdine.toString()).child("uid").setValue(uid)
    ordini.child(nOrdine.toString()).child("Locker").setValue(locker)
    ordini.child(nOrdine.toString()).child("Sportello").setValue(sportello)
    ordini.child(nOrdine.toString()).child("Data Ordine").setValue(dataOrdine)
    ordini.child(nOrdine.toString()).child("Data Consegna").setValue(dataConsegna)
    ordini.child(nOrdine.toString()).child("Totale").setValue(totale)
    ordini.child(nOrdine.toString()).child("CodiceSbloccoUtente").setValue(codiceSbloccoUtente)
    ordini.child(nOrdine.toString()).child("CodiceSbloccoFattorino").setValue(codiceSbloccoFattorino)


    //prodotti e quantità
    var carrello=viewModel.carrello.value.orEmpty()

    println("carrello: "+carrello.toString())

    carrello.forEach{(item, qty)->
        numeroProdotti+=qty
        println("Prodotto: "+item+" Quantità: "+qty.toString())
        ordini.child(nOrdine.toString()).child("Prodotti").child(item).setValue(qty)
        eliminaDalCarrello(item = item, uid, viewModel)
    }


    viewModel.carrello.value= emptyMap()
    database.child("utenti").child(uid).child("ordini").child(nOrdine.toString()).setValue(stato)

    //Generazione codice
    assegnazioneSportello(numeroProdotti, viewModel, codiceF = codiceSbloccoFattorino, codiceU=codiceSbloccoUtente)
}

