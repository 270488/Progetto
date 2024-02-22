package it.polito.database.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
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
    var prodotti by remember{ mutableStateOf<Map<String, Long>>(emptyMap()) }
    ordiniEffettuati.addValueEventListener(object: ValueEventListener {
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

    Column (horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier= Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .verticalScroll(rememberScrollState())
            .padding(top = 90.dp, bottom = 110.dp)) {
        DettaglioOrdineCard(viewModel, navController, prodotti, stato=stato, totale=totale, dataOrdine = dataOrdine, dataConsegna = dataConsegna, locker=locker)
        Divider(thickness = 1.dp, color = Yellow40, modifier = Modifier.padding(top = 4.dp, bottom = 16.dp, start = 10.dp, end= 10.dp))
        if(stato=="consegnato"){
            Button(onClick = { navController.navigate(Screen.CollectOrder.route)
            viewModel.ordineSelezionato=ordine},
                    shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp)
                    .width(250.dp),
                colors = ButtonDefaults.buttonColors(Yellow40),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    disabledElevation = 0.dp)){
                Text(text = "Ritira ordine", fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Blue40, fontSize = 23.sp)
            }
        }
        else{
            Button(
                onClick = { navController.navigate(Screen.Orders.route) },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp)
                    .width(250.dp),
                colors = ButtonDefaults.buttonColors(Yellow40),
            ) {
                Text(text = "I miei ordini", fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Blue40, fontSize = 23.sp)

            }
        }

    }
}

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
            .padding(horizontal = 20.dp)
    )
    {
        Card(modifier = Modifier
            .fillMaxSize()
            .border(2.dp, Color.Black, RoundedCornerShape(4)),
            colors = CardDefaults.cardColors(Blue20),
            )
        {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                //horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                prodotti.forEach { (item, qty) ->
                    dettaglioProdotto(
                        viewModel = viewModel,
                        qty = qty,
                        item = item,
                        navController
                    )
                }

            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                //horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
                    .padding(start = 5.dp)
            ) {
                Text(text = "Stato della spedizione:", color = Color.White, fontFamily = fontFamily)
            }
            if (stato == "ordinato") {
                //TODO icona ordinato
            } else if (stato == "in consegna") {
                //TODO icona in consegna
            } else if (stato == "consegnato") {
                //TODO icona in consegna
            } else if (stato == "ritirato") {
                //TODO icona in consegna
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                //horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
                    .padding(start=5.dp, end=5.dp, top=5.dp)
            ) {
                Text(text = "Data ordine: " + dataOrdine, color = Color.White, fontFamily = fontFamily)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                //horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
                    .padding(start=5.dp,end=5.dp, bottom = 5.dp)
            ) {
                Text(text = "Data consegna prevista: " + dataConsegna,color = Color.White, fontFamily = fontFamily)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                //horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
                    .padding( 5.dp, )
            ) {
                Text(text = "Locker di destinazione: " + locker,color = Color.White, fontFamily = fontFamily)
            }

            if (stato == "ordinato") {
                //TODO icona ordinato
            } else if (stato == "in consegna") {
                //TODO icona in consegna
            } else if (stato == "consegnato") {
                //TODO icona in consegna
            } else if (stato == "ritirato") {
                //TODO icona in consegna
            }


        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dettaglioProdotto(viewModel: AppViewModel, qty: Long, item: String, navController: NavController){
    var url= FindUrl(fileName = item+".jpg")
    Card(modifier= Modifier
        .fillMaxSize(),
        colors = CardDefaults.cardColors(Blue20),
        onClick = {viewModel.prodottoSelezionato=item;
        navController.navigate(Screen.Product.route)}) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
        )

        {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            )
            {
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .size(150.dp, 100.dp)
                        .border(2.dp,Color.White, RoundedCornerShape(5)),
                    contentScale = ContentScale.Crop
                )
            }
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
                        text = item,
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
                        .padding(top = 5.dp)
                ) {
                    Text(
                        text = "Quantità: " + qty.toString(),
                        fontFamily = fontFamily,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp,
                        //fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Start
                    )

                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp)
                ) {

                    Text(
                        text = "Ordine No: " + viewModel.ordineSelezionato,
                        fontFamily = fontFamily,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp,
                        //fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
        )

        {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                var openAlertDialog = remember { mutableStateOf(false) }

                // ...
                when {
                    // ...
                    openAlertDialog.value -> {
                        AlertDialog(
                            item = item,
                            navController = navController,
                            viewModel = viewModel,
                            onDismissRequest = { openAlertDialog.value = false },
                            onConfirmation = {
                                openAlertDialog.value = false
                                println("Confirmation registered") // Add logic here to handle confirmation.
                            },
                            dialogTitle = "Vuoi restituire un articolo? \n" +
                                    "Nessun problema!",
                            dialogText = "Se intendi restituire il tuo articolo non devi far altro che riconsegnarlo presso la reception della palestra in cui l’hai ritirato.\n" +
                                    "\n" +
                                    "Un corriere si occuperà in seguito di ritirare il tuo pacco e riceverai un rimborso completo non appena l’ordine sarà ricevuto dal magazzino."
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                {
                    Card(onClick = { openAlertDialog.value = true },
                        colors = CardDefaults.cardColors(Color.Transparent),
                        modifier = Modifier.height(30.dp))
                    {
                        Text(
                            text = "Restituisci ordine",
                            fontFamily = fontFamily,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Start
                        )

                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                {
                    Card(onClick = {
                        viewModel.prodottoSelezionato = item
                        navController.navigate(Screen.Product.route)
                    },
                        colors = CardDefaults.cardColors(Color.Transparent),
                        modifier = Modifier.height(30.dp)) {
                        Text(
                            text = "Ordina di nuovo",
                            fontFamily = fontFamily,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Start,

                            )
                    }
                }
            }
        }
    }
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
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
               },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    aggiungiReso(item, viewModel.ordineSelezionato,viewModel.uid, viewModel)
                    Toast.makeText(ctx, "Richiesta di reso confermata", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.ResiScreen.route)
                    onDismissRequest()
                }
            ) {
                Text("Avvia reso")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Annulla")
            }
        }
    )
}

