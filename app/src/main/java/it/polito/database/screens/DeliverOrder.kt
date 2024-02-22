package it.polito.database.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.database
import it.polito.database.ui.theme.fontFamily

@Composable
fun DeliverOrder(viewModel: AppViewModel, navController: NavController) {
    var sblocco by remember { mutableStateOf(false) }
    var sbloccoPP by remember { mutableStateOf(0L) }
    var sbloccoPG by remember { mutableStateOf(0L) }
    var codiceDB =
        database.child("ordini").child(viewModel.ordineSelezionato).child("CodiceSbloccoFattorino")
    var codice by remember {
        mutableStateOf("")
    }
    codiceDB.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            codice = dataSnapshot.value.toString()
        }

        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })

    viewModel.variabili.variabiliPath.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { //fa una foto al db in quel momento e la mette in dataSnapshot
            viewModel.variabili.CodeP = dataSnapshot.child("CodeP").value.toString()
            viewModel.variabili.CodeG = dataSnapshot.child("CodeG").value.toString()
            viewModel.variabili.CodiceTastierino =
                dataSnapshot.child("CodiceTastierino").value.toString()
            viewModel.variabili.PPAperta = dataSnapshot.child("PPAperta").value as Long
            viewModel.variabili.PGAperta = dataSnapshot.child("PGAperta").value as Long
            viewModel.variabili.Sblocco = dataSnapshot.child("Sblocco").value as Long
            viewModel.variabili.SportelloP = dataSnapshot.child("SportelloP").value as Boolean
            viewModel.variabili.SportelloG = dataSnapshot.child("SportelloG").value as Boolean
            viewModel.variabili.CodiceErrato = dataSnapshot.child("CodiceErrato").value as Long

            println("CodeP: " + viewModel.variabili.CodeP)
            println("CodeG: " + viewModel.variabili.CodeG)
            println("CodiceTastierino: " + viewModel.variabili.CodiceTastierino)
            println("PPAPerta: " + viewModel.variabili.PPAperta)
            println("PGAperta: " + viewModel.variabili.PGAperta)
            println("Sblocco: " + viewModel.variabili.Sblocco)
            println("SportelloP: " + viewModel.variabili.SportelloP)
            println("SportelloG: " + viewModel.variabili.SportelloG)

            if (viewModel.variabili.PGAperta == 0L && sbloccoPG == 1L) { //La porta si chiude
                sbloccoPG = 0L
                database.child("ordini").child(viewModel.ordineSelezionato).child("stato")
                    .setValue("consegnato")
                database.child("utenti").child(viewModel.uid).child("ordini")
                    .child(viewModel.ordineSelezionato).setValue("consegnato")

            } else if (viewModel.variabili.PGAperta == 1L && sbloccoPG == 0L) {
                sbloccoPG = 1L
            }
            if (viewModel.variabili.PPAperta == 0L && sbloccoPP == 1L) { //La porta si chiude
                sbloccoPP = 0L
                database.child("ordini").child(viewModel.ordineSelezionato).child("stato")
                    .setValue("consegnato")
                database.child("utenti").child(viewModel.uid).child("ordini")
                    .child(viewModel.ordineSelezionato).setValue("consegnato")
            }
            if (viewModel.variabili.PPAperta == 1L && sbloccoPP == 0L) {
                sbloccoPP = 1L
            }


        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Gestisci gli errori qui
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
            if (sblocco) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                ) {

                    Text(
                        text = "Il codice per il ritiro è:\n" + codice,
                        fontFamily = fontFamily,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                }


            }

            Button(onClick = {
                sblocco = true;
                viewModel.variabili.Sblocco = 1L;
                writeVariables(viewModel.variabili)
            }) {
                Text(text = "Sblocca Locker")
            }
        }

    }
    cambioVariabili(variabili = viewModel.variabili)
}