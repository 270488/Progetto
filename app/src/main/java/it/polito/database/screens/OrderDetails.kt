package it.polito.database.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.FindUrl
import it.polito.database.R
import it.polito.database.database
import it.polito.database.ui.theme.Blue20
import it.polito.database.ui.theme.Blue40
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.Yellow40
import it.polito.database.ui.theme.fontFamily

@Composable
fun OrderDetails(viewModel: AppViewModel, navController: NavController){

    var ordine=viewModel.ordineSelezionato
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
        mutableStateOf("")
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
    var prodotti by remember{ mutableStateOf<Map<String, Long>>(emptyMap()) }
    ordiniEffettuati.addValueEventListener(object: ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            dataConsegna=dataSnapshot.child("Data Consegna").value.toString()
            dataOrdine=dataSnapshot.child("Data Ordine").value.toString()
            locker=dataSnapshot.child("Locker").value.toString()
            sportello=dataSnapshot.child("Sportello").value.toString()
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

    Column (horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier= Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            //.verticalScroll(rememberScrollState())
            .padding(top = 80.dp, bottom = 110.dp)) {

        DettaglioOrdineCard(viewModel, navController, prodotti, stato=stato, totale=totale, dataOrdine = dataOrdine, dataConsegna = dataConsegna, locker=locker)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 8.dp)
        ){
            Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.tertiary)
            Spacer(modifier = Modifier.height(15.dp))

            if(stato=="consegnato"){
                Button(onClick = { navController.navigate(Screen.CollectOrder.route)
                    viewModel.ordineSelezionato=ordine},
                    shape = RoundedCornerShape(3.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp))
                {
                    Text(
                        text = "Ritira ordine",
                        fontFamily = fontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            else{
                Button(
                    onClick = { navController.navigate(Screen.Orders.route) },
                    shape = RoundedCornerShape(3.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp)
                ) {
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DettaglioOrdineCard(viewModel: AppViewModel,
                        navController: NavController,
                        prodotti: Map<String, Long>,
                        stato: String,
                        totale: Double,
                        dataOrdine: String,
                        dataConsegna: String,
                        locker: String) {

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier= Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    )
    {
        //Numero dell'ordine
        Text(
            text = "Ordine No: " + viewModel.ordineSelezionato,
            fontFamily = fontFamily,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(12.dp))
        //Colonna con le card dei prodotti dell'ordine scrollabile
        Column(
            modifier = Modifier
                .heightIn(min = 0.dp, max = 180.dp)
                .verticalScroll(rememberScrollState())
        ) {
            prodotti.forEach { (item, qty) ->
                //currItem = item
                dettaglioProdotto(
                    viewModel = viewModel,
                    qty = qty,
                    item = item,
                    stato = stato,
                    navController
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        //Column con info generali
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Stato della spedizione:",
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = fontFamily,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.align(Alignment.Start)
            )

            if (stato == "ordinato") {
                //TODO icona ordinato
                Column (
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(painter = painterResource(id = R.drawable.ordinato),
                        contentDescription = "",
                        modifier = Modifier
                            .size(48.dp)
                            .padding(top = 8.dp),
                        tint = Yellow40)

                    Image(
                        painter = painterResource(id = R.drawable.barrastatoordine1),
                        contentDescription = "",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    )
                }
            } else if (stato == "spedito") {
                Icon(
                    painter = painterResource(id = R.drawable.spedito),
                    contentDescription = "",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(top = 8.dp),
                    tint = Yellow40
                )
                Image(
                        painter = painterResource(id = R.drawable.barrastatoordine2),
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                )
                //TODO icona in consegna
            } else if (stato == "consegnato") {
                Icon(
                    painter = painterResource(id = R.drawable.consegnato),
                    contentDescription = "",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(top = 8.dp),
                    tint = Yellow40
                )
                Image(
                    painter = painterResource(id = R.drawable.barrastatoordine3),
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                )
                //TODO icona in consegna
            } else if (stato == "ritirato") {
                Icon(
                    painter = painterResource(id = R.drawable.reso_completato),
                    contentDescription = "",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(top = 8.dp),
                    tint = Yellow40
                )
                Image(
                    painter = painterResource(id = R.drawable.barrastatoordine4),
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                )
                //TODO icona in consegna
            }
            Text(
                text = "Riepilogo",
                fontSize = 16.sp,
                color = Color.White,
                fontFamily = fontFamily,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 4.dp)
            )
            Text(
                text = "Data ordine: " + dataOrdine,
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = fontFamily,
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "Data consegna prevista: " + dataConsegna,
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = fontFamily,
                modifier = Modifier.align(Alignment.Start)
            )

            Text(
                text = "Locker di destinazione: " + locker.replace("/"," "),
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontFamily = fontFamily,
                modifier = Modifier
                    .align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))


          /*  if (stato == "ordinato") {
                //TODO icona ordinato
            } else if (stato == "spedito") {
                //TODO icona in consegna
            } else if (stato == "consegnato") {
                //TODO icona in consegna
            } else if (stato == "ritirato") {
                //TODO icona in consegna
            }*/
        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dettaglioProdotto(viewModel: AppViewModel, qty: Long, item: String, stato: String, navController: NavController){
    var url= FindUrl(fileName = item+".jpg")
    var numeroOrdine=viewModel.ordineSelezionato

    var resi= database.child("resi")
    var resoAvviato by remember {
        mutableStateOf(false)
    }
    resi.addValueEventListener(object: ValueEventListener{
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            Log.d("DataSnapshot", dataSnapshot.toString())
            for (childSnapshot in dataSnapshot.children) {
                var r = false
                Log.d("childSnapshot", childSnapshot.toString())
                Log.d("numero ordine", numeroOrdine)
                Log.d("prodotti", item)
                Log.d("ordine", childSnapshot.child("ordine").value.toString())
                Log.d("ordine", childSnapshot.child("prodotti").value.toString())


                if (childSnapshot.child("ordine").value.toString() == numeroOrdine) {
                    if (childSnapshot.child("prodotti").value.toString() == item) {
                        r = true
                    }
                    resoAvviato = r
                }
        }
        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })

    Card(
        //modifier= Modifier.fillMaxSize(),
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(2.dp, Color.Black),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1D232C),
            contentColor = Color.White
        ),
        onClick = {viewModel.prodottoSelezionato=item;
        navController.navigate(Screen.Product.route)}
    )
    {
        Column {
            Row {
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .size(130.dp, 78.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp)
                )
                {
                    Text(
                        text = item,
                        fontFamily = fontFamily,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )

                    Text(
                        text = "Quantità: " + qty.toString(),
                        fontSize = 16.sp,
                        fontFamily = fontFamily,
                    )

                    /*Text(
                        text = "Ordine No: " + viewModel.ordineSelezionato,
                        fontSize = 16.sp,
                        fontFamily = fontFamily,
                    )*/
                }
            }
            //RIGA CON RESTITUISCI ORDINE E ORDINA DI NUOVO
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )
            {
                var openAlertDialog by remember { mutableStateOf(false) }
                var ctx = LocalContext.current

                if (openAlertDialog){
                    Popup(
                        onDismissRequest = { openAlertDialog = false },
                        //modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xCC1D232C))
                                .padding(horizontal = 20.dp)
                        ){
                            //EFFETTIVO BOX DEL POPUP
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(15.dp)
                                    )
                                    .border(
                                        2.dp,
                                        MaterialTheme.colorScheme.tertiary,
                                        RoundedCornerShape(15.dp)
                                    )
                            ){
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        //.fillMaxSize()
                                        .background(MaterialTheme.colorScheme.primary)
                                        .padding(16.dp)
                                ){
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = buildAnnotatedString {
                                            append("Vuoi restituire un articolo?\n")
                                            withStyle(
                                                style = SpanStyle(
                                                    color = MaterialTheme.colorScheme.tertiary,
                                                )
                                            ){
                                                append("Nessun problema")
                                            }
                                        },
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        fontFamily = fontFamily,
                                    )
                                    Spacer(modifier = Modifier.height(22.dp))
                                    Column(modifier = Modifier.padding(horizontal = 18.dp)) {
                                        Text(
                                            text = "Se intendi restituire il tuo articolo non devi far altro che riconsegnarlo presso la reception della palestra in cui l’hai ritirato.",
                                            fontSize = 16.sp,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            fontFamily = fontFamily,
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text(
                                            text = "Un corriere si occuperà in seguito di ritirare il tuo pacco e riceverai un rimborso completo non appena l’ordine sarà ricevuto dal magazzino.",
                                            fontSize = 16.sp,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            fontFamily = fontFamily,
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(22.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp)
                                    ) {
                                        TextButton(
                                            modifier = Modifier.width(135.dp),
                                            shape = RoundedCornerShape(3.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.tertiary,
                                                contentColor = MaterialTheme.colorScheme.onTertiary
                                            ),
                                            onClick = {
                                                if(!resoAvviato){
                                                    aggiungiReso(item, viewModel.ordineSelezionato,viewModel.uid, viewModel)
                                                }

                                                Toast.makeText(ctx, "Richiesta di reso confermata", Toast.LENGTH_SHORT).show()
                                                navController.navigate(Screen.ResiScreen.route)
                                                openAlertDialog = false
                                            }
                                        ) {
                                            Text(
                                                text = "Avvia reso",
                                                fontFamily = fontFamily,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        TextButton(
                                            modifier = Modifier.width(135.dp),
                                            shape = RoundedCornerShape(3.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                                            ),
                                            onClick = {
                                                openAlertDialog = false
                                            }
                                        ) {
                                            Text(
                                                text = "Annulla",
                                                fontFamily = fontFamily,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Text(
                    modifier = Modifier.clickable {
                        viewModel.prodottoSelezionato = item
                        navController.navigate(Screen.Product.route) },
                    text = "Ordina di nuovo",
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    textDecoration = TextDecoration.Underline,
                )
                Log.d("Reso avviato: ", resoAvviato.toString())
                Text(
                    modifier = Modifier
                        .clickable {
                            if(!resoAvviato)
                                openAlertDialog = true
                        }
                        .alpha(if (stato == "ritirato" || resoAvviato) 0.3f else 1f),
                    text = "Restituisci ordine",
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    textDecoration = TextDecoration.Underline,
                )
                
                
                
                
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    viewModel: AppViewModel,
    item: String,
    navController: NavController
) {
    var ctx = LocalContext.current
    AlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(15.dp)),
        containerColor = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(15.dp),
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    append("Vuoi restituire un articolo?\n")
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    ){
                        append("Nessun problema")
                    }
                },
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontFamily = fontFamily,
            )
        },
        text = {
            Text(
                text = dialogText,
                fontSize = 16.sp,
                color = Color.White,
                fontFamily = fontFamily,
            )
               },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    modifier = Modifier.width(125.dp),
                    shape = RoundedCornerShape(3.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    onClick = {
                        aggiungiReso(item, viewModel.ordineSelezionato,viewModel.uid, viewModel)
                        Toast.makeText(ctx, "Richiesta di reso confermata", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.ResiScreen.route)
                        onDismissRequest()
                    }
                ) {
                    Text(
                        text = "Avvia reso",
                        fontFamily = fontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                TextButton(
                    modifier = Modifier.width(125.dp),
                    shape = RoundedCornerShape(3.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text(
                        text = "Annulla",
                        fontFamily = fontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
    )
}

