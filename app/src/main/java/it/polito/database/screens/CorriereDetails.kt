package it.polito.database.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.R
import it.polito.database.database
import it.polito.database.ui.theme.Blue20
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.Yellow40
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

    var currState = viewModel.corriereState.observeAsState()

    Column (
        horizontalAlignment = Alignment.Start,
        modifier= Modifier
            .fillMaxWidth().padding(horizontal = 12.dp)
    )
    {
        Text(
            text = "Stato della spedizione:",
            fontFamily = fontFamily,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.height(20.dp))
        if (currState.value == "In attesa di ritiro") {
            Icon(painter = painterResource(id = R.drawable.in_attesa_di_ritiro),
                contentDescription = "in attesa di ritiro",
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally),
                tint = Yellow40
            )
        } else if (currState.value == "In consegna") {
            Icon(
                painter = painterResource(id = R.drawable.in_consegna),
                contentDescription = "in consegna",
                modifier = Modifier
                    .size(75.dp)
                    .align(Alignment.CenterHorizontally),
                tint = Yellow40
            )
        } else if (currState.value == "Consegnato") {
            Icon(
                painter = painterResource(id = R.drawable.consegnato),
                contentDescription = "consegnato",
                modifier = Modifier
                    .size(55.dp)
                    .align(Alignment.CenterHorizontally),
                tint = Yellow40
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Locker di destinazione:",
            fontFamily = fontFamily,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .width(220.dp)
                .align(Alignment.CenterHorizontally)
        )
        {
            Text(
                modifier = Modifier.padding(8.dp),
                text = locker.replace("/","\n"),
                fontSize = 16.sp,
                fontFamily = fontFamily,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row {
            Text(
                text = "Data consegna prevista:  ",
                color = Color.White,
                fontFamily = fontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold

            )
            Text(
                text = dataConsegna,
                color = Color.White,
                fontFamily = fontFamily,
                fontSize = 16.sp
            )
        }
    }

    Column {
        Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.tertiary)
        Spacer(modifier = Modifier.height(28.dp))
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ){
            if (stato == "ordinato") {
                viewModel.corriereState.value = "In attesa di ritiro"
                Button(
                    onClick = {
                        database.child("ordini").child(viewModel.ordineSelezionato).child("stato")
                            .setValue("spedito")
                        database.child("utenti").child(id).child("ordini")
                            .child(viewModel.ordineSelezionato).setValue("spedito")
                        viewModel.corriereState.value = "In consegna"
                    },
                    modifier = Modifier
                        .layoutId("btnCheckOut"),
                    shape = RoundedCornerShape(3.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 16.dp,
                        pressedElevation = 0.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                )
                {
                    Text(
                        text = "Conferma ritiro",
                        fontFamily = fontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else if (stato == "spedito") {
                viewModel.corriereState.value = "In consegna"
                Button(
                    onClick = {
                        navController.navigate(Screen.DeliverOrder.route)

                    },
                    modifier = Modifier
                        .layoutId("btnCheckOut"),
                    shape = RoundedCornerShape(3.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 16.dp,
                        pressedElevation = 0.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                )
                {
                    Text(
                        text = "Sblocca locker",
                        fontFamily = fontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }


}