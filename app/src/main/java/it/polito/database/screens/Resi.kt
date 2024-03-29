package it.polito.database.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.values
import it.polito.database.AppViewModel
import it.polito.database.FindUrl
import it.polito.database.R
import it.polito.database.database
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.fontFamily
import okhttp3.internal.notify
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
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
                //FILTRO ORDINI PER STATO

                val listStates= listOf<String>("In corso", "Terminati", "Tutti")

                var selectedState by remember {
                    mutableStateOf<String>("In corso")
                }

                var expanded by remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .layoutId("menuOptions")
                            .width(165.dp)
                            .height(50.dp)
                            .border(
                                BorderStroke(2.dp, Color.Black),
                                RoundedCornerShape(3.dp)
                            )
                            .background(
                                MaterialTheme.colorScheme.secondary,
                                RoundedCornerShape(3.dp)
                            )
                    ){
                        ExposedDropdownMenuBox(
                            modifier = Modifier.fillMaxSize(),
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
                        ) {
                            TextField(
                                value = selectedState,
                                onValueChange = {},
                                readOnly = true,
                                textStyle = TextStyle(
                                    fontFamily= fontFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    textColor = Color.White),
                                leadingIcon = {
                                    Icon(
                                        modifier = Modifier.size(23.dp),
                                        painter = painterResource(id = R.drawable.filtri),
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.tertiary
                                    )
                                },
                                modifier = Modifier.menuAnchor()
                            )
                            DropdownMenu(
                                offset = DpOffset((0).dp, (4).dp),
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier
                                    .width(165.dp)
                                    .background(MaterialTheme.colorScheme.secondary)
                                    .border(
                                        BorderStroke(2.dp, Color.Black),
                                        RoundedCornerShape(3.dp)
                                    )
                            ) {

                                listStates.forEachIndexed() {index, o ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                horizontalArrangement = Arrangement.Center
                                            )
                                            {
                                                Text(
                                                    modifier = Modifier
                                                        .offset(y = if(index == 0)(-4).dp else if(index == 2) 4.dp else 0.dp),
                                                    text = o,
                                                    color = Color.White,
                                                    fontFamily= fontFamily,
                                                    fontStyle = FontStyle.Italic,
                                                    fontSize = 15.sp)
                                            }

                                        },
                                        onClick = {
                                            selectedState = o
                                            expanded =
                                                false
                                        })
                                    if (index < listStates.size - 1){
                                        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                                    }
                                }

                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                listaResi.forEach{
                        i-> resiCard(numeroReso = i, viewModel, navController, selectedState)
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
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 16.dp,
                    pressedElevation = 0.dp
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
fun resiCard(numeroReso: String, viewModel: AppViewModel, navController: NavController, filtro: String){

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

    var filteredStates by remember {
        mutableStateOf<List<String>>(emptyList())
    }

    when(filtro){
        "In corso" -> {
            filteredStates = listOf("avviato", "consegnato")
        }
        "Terminati" -> {
            filteredStates = listOf("completato","scaduto")
        }
        "Tutti" -> {
            filteredStates = listOf("avviato","consegnato", "completato", "scaduto")
        }
    }

    url= FindUrl(fileName = prodotti+".jpg")

    if(filteredStates.contains(stato)){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
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

                    var isLoading by remember { mutableStateOf(true) }

                    Box(contentAlignment = Alignment.Center,
                        modifier = Modifier.width(150.dp).height(90.dp))
                    {
                        if (isLoading)
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(32.dp),
                                color = MaterialTheme.colorScheme.tertiary,
                                strokeWidth = ProgressIndicatorDefaults.CircularStrokeWidth
                            )

                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(RoundedCornerShape(15.dp))
                                .height(90.dp)
                                .width(150.dp),
                            contentScale = ContentScale.Crop,
                            onLoading = { isLoading = true },
                            onSuccess = { isLoading = false },
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = prodotti,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = fontFamily,
                        )
                        Text(
                            text = "Ordine No. " + ordine,
                            fontSize = 14.sp,
                            fontFamily = fontFamily,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = buildAnnotatedString {
                            append("Stato: ")
                            withStyle(
                                style = SpanStyle(
                                    color =
                                    if (stato == "scaduto") MaterialTheme.colorScheme.errorContainer
                                    else if (stato == "completato") Color.White
                                    else  MaterialTheme.colorScheme.tertiary,
                                )
                            ){
                                append(stato)
                            }
                        },
                        fontFamily = fontFamily,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    /*Text(
                        text = "Stato: " + stato,
                        color =
                        if (stato == "scaduto") MaterialTheme.colorScheme.errorContainer
                        else  MaterialTheme.colorScheme.tertiary,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )*/
                    Text(
                        text = "Scadenza: " + scadenza,
                        fontFamily = fontFamily,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

            }

        }
        Spacer(modifier = Modifier.height(16.dp))
    }


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

fun eliminaReso(viewModel: AppViewModel){
    val numeroReso= viewModel.resoSelezionato
    val uid=viewModel.uid
    val resi= database.child("resi").child(numeroReso)
    val resiAccount= database.child("utenti").child(uid).child("resi").child(numeroReso)

    resi.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { //fa una foto al db in quel momento e la mette in dataSnapshot
            dataSnapshot.ref.removeValue()

        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })
    resiAccount.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { //fa una foto al db in quel momento e la mette in dataSnapshot
            dataSnapshot.ref.removeValue()

        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })



}
