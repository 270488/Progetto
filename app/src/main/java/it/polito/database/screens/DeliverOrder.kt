package it.polito.database.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.database
import it.polito.database.ui.theme.Screen

@Composable
fun DeliverOrder(viewModel: AppViewModel, navController: NavController) {
    var sportello by remember { mutableStateOf("") }
    var ordine=database.child("ordini").child(viewModel.ordineSelezionato).child("Sportello")
    ordine.addValueEventListener(object: ValueEventListener {
        var sp=""
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            sp=dataSnapshot.value.toString()
            sportello = sp
            Log.d("Verify", "Sportello : $sportello")
        }

        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }

    })

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .verticalScroll(rememberScrollState())
            .padding(top = 90.dp, bottom = 110.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) {

            Button(onClick = { //TODO cambiare valori variabili di sblocco nel DB
                // controlla quale porta aprire, se è chiusa la apre ma sblocco resta a zero->
                //non inserisce nessun codice
                // se la porta da aperta passa a chiusa, cambio lo stato dell'ordine->
                //sia per nodo ordini e utenti che corriere
                // aggiorna variabili ( solo PGaperta e PPaperta)
                if(sportello =="P" && !viewModel.variabili.SportelloP)
                // sportello assegnato per l'ordine selezionato è P ed è libero
                {
                   //viewModel.variabili.PPAperta=1L //apre la porta
                    database.child("variabili").child("PPAperta").setValue(1L)
                    if(viewModel.variabili.PPAperta==0L){ //se la chiude
                        database.child("ordini").child(viewModel.ordineSelezionato).child("stato").setValue("consegnato")
                        database.child("utenti").child(viewModel.uid).child("ordini").child(viewModel.ordineSelezionato).setValue("consegnato")
                        viewModel.corriereState.value="consegnato"
                        database.child("variabili").child("SportelloP").setValue(true)

                        //viewModel.variabili.SportelloP=true; //è occupato
                        //writeVariables(viewModel.variabili)
                        // pop up conferma consegna e rimuovere ordine dalla lista del corriere
                        navController.navigate(Screen.CorriereHome.route)
                    }
                }
                else if(sportello =="G" && !viewModel.variabili.SportelloG)
                {
                    //viewModel.variabili.PGAperta=1L //apre la porta
                    database.child("variabili").child("PGAperta").setValue(1L)
                    if(viewModel.variabili.PGAperta==0L){ //se la chiude
                        navController.navigate(Screen.CorriereHome.route)
                        database.child("ordini").child(viewModel.ordineSelezionato).child("stato").setValue("consegnato")
                        database.child("utenti").child(viewModel.uid).child("ordini").child(viewModel.ordineSelezionato).setValue("consegnato")
                        viewModel.corriereState.value="consegnato"
                        database.child("variabili").child("SportelloG").setValue(true)
                        //viewModel.variabili.SportelloG=true; //è occupato
                        //writeVariables(viewModel.variabili)
                        // TODO pop up conferma consegna e rimuovere ordine dalla lista del corriere
                    }
                }

              //writeVariables(viewModel.variabili)


            }) {
                Text(text = "Sblocca Locker")
            }
        }

    }
   // cambioVariabili(variabili = viewModel.variabili)
}