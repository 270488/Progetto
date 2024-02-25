package it.polito.database.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.database
import it.polito.database.ui.theme.Blue20
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.fontFamily

@Composable
fun CorriereDetails(viewModel: AppViewModel, navController: NavController){
    var ordine=viewModel.ordineSelezionato
    var ordiniEffettuati= database.child("ordini").child(ordine)

    var dataConsegna by remember {
        mutableStateOf("")
    }
    var locker by remember {
        mutableStateOf("")
    }
    var stato by remember {
        mutableStateOf("")
    }
    var uid by remember {
        mutableStateOf("")
    }
    ordiniEffettuati.addValueEventListener(object: ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            dataConsegna=dataSnapshot.child("Data Consegna").value.toString()
            locker=dataSnapshot.child("Locker").value.toString()
            stato=dataSnapshot.child("stato").value.toString()
            uid=dataSnapshot.child("uid").value.toString()
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

        DettaglioCard(viewModel, navController, stato=stato, dataConsegna = dataConsegna, id= uid, locker = locker)


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DettaglioCard(viewModel: AppViewModel,
                        navController: NavController,
                        stato: String,
                        dataConsegna: String,
                        locker: String,
                  id:String) {

    var currItem by remember {
        mutableStateOf("")
    }
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 5.dp)
            ) {
                Text(text = "Stato della spedizione:", color = Color.White, fontFamily = fontFamily)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                //horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 5.dp, end = 5.dp, bottom = 5.dp)
            ) {
                Text(
                    text = "Data consegna prevista: " + dataConsegna,
                    color = Color.White,
                    fontFamily = fontFamily
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                //horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp,)
            ) {
                Text(
                    text = "Locker di destinazione: " + locker,
                    color = Color.White,
                    fontFamily = fontFamily
                )
            }

            if (stato == "ordinato") {
                Button(
                    onClick = {
                        database.child("ordini").child(viewModel.ordineSelezionato).child("stato")
                            .setValue("spedito")
                        database.child("utenti").child(id).child("ordini")
                            .child(viewModel.ordineSelezionato).setValue("spedito")

                    }) {
                    Text(text = "spedito")
                }
            } else if (stato == "spedito") {
                Button(
                    onClick = {
                        navController.navigate(Screen.DeliverOrder.route)

                    }) {
                    Text(text = "sblocca locker")
                }
            } else if (stato == "ritirato") {
                Button(
                    onClick = {
                        database.child("ordini").child(viewModel.ordineSelezionato).child("stato")
                            .setValue("ritirato")
                        database.child("utenti").child(id).child("ordini")
                            .child(viewModel.ordineSelezionato).setValue("ritirato")
                        // TODO togliere dalla lista l'ordine

                    }) {
                    Text(text = "spedito")
                }
            }

        }
    }
}

