package it.polito.database.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.database
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.fontFamily

@Composable
fun CorriereHome(viewModel: AppViewModel, navController: NavController) {
    /*val auth = Firebase.auth
    val currentCorriere = auth.currentUser
    cambiare in uid sotto*/
    val corriereReference = FirebaseDatabase.getInstance().getReference("corrieri").child("jSJNjHS9PENBjyBSz0NYOT3zz173")
    var ordini= corriereReference.child("ordini")
    var listaOrdini by remember { mutableStateOf<List<String>>(emptyList()) }


    ordini.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            var lista= mutableListOf<String>()
            for (childSnapshot in dataSnapshot.children)  { //per ogni ordine
                lista.add(childSnapshot.key.toString())
                Log.d("Child","qualcosa" + childSnapshot.toString())

            }
            Log.d("Snapshot","qualcosa" + dataSnapshot.toString())
            Log.d("Lista","qualcosa" + lista)
            listaOrdini = lista
        }

        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    }
    )
    Log.d("ListaOdrini","qualcosa" + listaOrdini)

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
                    text = "Non ci sono ordini da consegnare",
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
                        o->Ordine(o, viewModel, navController)
                }
            }
        }



    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ordine(ordine: String, viewModel: AppViewModel, navController: NavController) {

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
            sportello= dataSnapshot.child("Sportello").value.toString()
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
    if (stato != "null") {
        Card(
            modifier = Modifier.height(90.dp),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
            ),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
            onClick = {
                viewModel.ordineSelezionato = ordine
                navController.navigate(Screen.CorriereDetails.route)
            }
        ) {

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
                        text = "Ordine No: $ordine",
                        fontFamily = fontFamily,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 18.sp,
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
                        text = "Stato ordine: $stato",
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




