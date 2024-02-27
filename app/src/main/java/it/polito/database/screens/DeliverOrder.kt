package it.polito.database.screens

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.database
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.fontFamily

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
    var openAlertDialog by remember { mutableStateOf(false) }
    var ctx = LocalContext.current

    if (openAlertDialog) {
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
            ) {
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
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            //.fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "success",
                                tint = Color.Green
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = buildAnnotatedString {
                                append("Ordine consegnato correttamente")
                            },
                            fontSize = 22.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontFamily = fontFamily,
                        )
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            TextButton(
                                modifier = Modifier.width(146.dp),
                                shape = RoundedCornerShape(3.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                                ),
                                onClick = {
                                    openAlertDialog = false
                                    navController.navigate(Screen.CorriereHome.route)
                                }
                            ) {
                                Text(
                                    text = "Okay",
                                    fontFamily = fontFamily,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = 90.dp, bottom = 130.dp, start = 20.dp, end = 20.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            val scaleAnimation = rememberInfiniteTransition(label = "")
            val scale by scaleAnimation.animateFloat(
                initialValue = 1.15f,
                targetValue = 0.95f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1800,
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ), label = ""
            )
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .scale(scale),
                shape = RoundedCornerShape(3.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ),
                onClick = {
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

                        openAlertDialog=true

                    }
                }
                else if(sportello =="G" && !viewModel.variabili.SportelloG)
                {
                    //viewModel.variabili.PGAperta=1L //apre la porta
                    database.child("variabili").child("PGAperta").setValue(1L)
                    if(viewModel.variabili.PGAperta==0L){ //se la chiude

                        database.child("ordini").child(viewModel.ordineSelezionato).child("stato").setValue("consegnato")
                        database.child("utenti").child(viewModel.uid).child("ordini").child(viewModel.ordineSelezionato).setValue("consegnato")
                        viewModel.corriereState.value="consegnato"
                        database.child("variabili").child("SportelloG").setValue(true)
                        openAlertDialog=true

                        //viewModel.variabili.SportelloG=true; //è occupato
                        //writeVariables(viewModel.variabili)
                    }
                }

              //writeVariables(viewModel.variabili)


            }) {
                Text(
                    text = "SBLOCCA LOCKER",
                    fontFamily = fontFamily,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(x = 0.dp, y = (-2).dp)
                )
            }
        }

    }
   // cambioVariabili(variabili = viewModel.variabili)
}