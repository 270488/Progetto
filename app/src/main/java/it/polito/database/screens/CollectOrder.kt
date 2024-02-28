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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
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
import java.util.Timer
import java.util.TimerTask


@Composable
fun CollectOrder(viewModel: AppViewModel, navController: NavController){
    var sblocco by remember{ mutableStateOf(false) }
    var sbloccoPP by remember{ mutableStateOf(0L) }
    var sbloccoPG by remember{ mutableStateOf(0L) }
    var codiceTastierino by remember{ mutableStateOf("") }
    var codiceDB= database.child("ordini").child(viewModel.ordineSelezionato).child("CodiceSbloccoUtente")
    var codice by remember {
        mutableStateOf("")

    }
    var audioShouldRun by remember { mutableStateOf(false) }
    val audioPlayer = AudioPlayer(LocalContext.current)
    codiceDB.addValueEventListener(object: ValueEventListener{
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            codice=dataSnapshot.value.toString()
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
                            Icon(imageVector = Icons.Default.Warning,
                                contentDescription = "attenzione",
                                tint = MaterialTheme.colorScheme.tertiary)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = buildAnnotatedString {
                                append("Chiudi lo sportello del locker")
                            },
                            fontSize = 22.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontFamily = fontFamily,
                        )
                    }
                }
            }
        }
    }

    database.child("variabili").addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { //fa una foto al db in quel momento e la mette in dataSnapshot
            viewModel.variabili.CodeP=dataSnapshot.child("CodeP").value.toString()
            viewModel.variabili.CodeG=dataSnapshot.child("CodeG").value.toString()
            viewModel.variabili.CodiceTastierino=dataSnapshot.child("CodiceTastierino").value.toString()
            viewModel.variabili.PPAperta=dataSnapshot.child("PPAperta").value as Long
            viewModel.variabili.PGAperta=dataSnapshot.child("PGAperta").value as Long
            viewModel.variabili.Sblocco=dataSnapshot.child("Sblocco").value as Long
            viewModel.variabili.SportelloP=dataSnapshot.child("SportelloP").value as Boolean
            viewModel.variabili.SportelloG=dataSnapshot.child("SportelloG").value as Boolean

            println("CodeP: "+viewModel.variabili.CodeP)
            println("CodeG: "+viewModel.variabili.CodeG)
            println("CodiceTastierino: "+viewModel.variabili.CodiceTastierino)
            println("PPAPerta: "+viewModel.variabili.PPAperta)
            println("PGAperta: "+viewModel.variabili.PGAperta)
            println("Sblocco: "+viewModel.variabili.Sblocco)
            println("SportelloP: "+viewModel.variabili.SportelloP)
            println("SportelloG: "+viewModel.variabili.SportelloG)

            if(sblocco) {
                if (viewModel.variabili.PGAperta == 0L && sbloccoPG == 1L) { //La porta si chiude
                    sbloccoPG = 0L
                    database.child("ordini").child(viewModel.ordineSelezionato).child("stato")
                        .setValue("ritirato")
                    database.child("utenti").child(viewModel.uid).child("ordini")
                        .child(viewModel.ordineSelezionato).setValue("ritirato")
                    viewModel.variabili.SportelloG = false;
                    audioShouldRun=false
                    writeVariables(viewModel.variabili)
                    sblocco=false

                    navController.navigate(Screen.OrderDetails.route)

                } else if (viewModel.variabili.PGAperta == 1L && sbloccoPG == 0L) {
                    audioShouldRun=true
                    sbloccoPG = 1L
                    openAlertDialog = viewModel.hoChiusoLoSportello.value != true
                }
                if (viewModel.variabili.PPAperta == 0L && sbloccoPP == 1L) { //La porta si chiude
                    sbloccoPP = 0L
                    database.child("ordini").child(viewModel.ordineSelezionato).child("stato")
                        .setValue("ritirato")
                    database.child("utenti").child(viewModel.uid).child("ordini")
                        .child(viewModel.ordineSelezionato).setValue("ritirato")
                    viewModel.variabili.SportelloP = false;
                    audioShouldRun=false
                    sblocco=false

                    writeVariables(viewModel.variabili)
                    navController.navigate(Screen.OrderDetails.route)
                }
                if (viewModel.variabili.PPAperta == 1L && sbloccoPP == 0L) {
                    sbloccoPP = 1L
                    audioShouldRun=true
                    openAlertDialog = viewModel.hoChiusoLoSportello.value != true
                }
                if (codiceTastierino != viewModel.variabili.CodiceTastierino && viewModel.variabili.CodiceTastierino != "0000") {
                    confrontoCodici(viewModel.variabili)
                    codiceTastierino = viewModel.variabili.CodiceTastierino
                    Log.d("codiceTastierino", codiceTastierino)
                    Log.d("CodiceTastierino", viewModel.variabili.CodiceTastierino)

                }


            }

            //confrontoCodici(viewModel.variabili)


        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Gestisci gli errori qui
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })
    LaunchedEffect(audioShouldRun){
        audioPlayer.playAudioWithDelay(3000, 10000, viewModel, audioShouldRun)

    }
    Column (horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier= Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = 90.dp, bottom = 130.dp)) {
        Box (
            contentAlignment = Alignment.Center,
            modifier= Modifier
                .fillMaxSize()
        ){
            if(sblocco){
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = "Codice per il ritiro:",
                        fontFamily = fontFamily,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .width(150.dp)
                                .background(
                                    MaterialTheme.colorScheme.secondary,
                                    RoundedCornerShape(15.dp)
                                )
                                .border(
                                    2.dp,
                                    MaterialTheme.colorScheme.onBackground,
                                    RoundedCornerShape(15.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Text(
                                text = codice.uppercase(),
                                fontFamily = fontFamily,
                                color = Color.White,
                                fontSize = 32.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = buildAnnotatedString {
                            append("*Inserisci il codice soprastante per aprire lo sportello del locker e ritirare il tuo ordine. ")
                            withStyle(
                                style = SpanStyle(
                                    textDecoration = TextDecoration.Underline
                                )
                            ){
                                append("Chiudi lo sportello per confermare di aver completato il ritiro.")
                            }
                        },
                        fontFamily = fontFamily,
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start
                    )

                }

            }
            if (!sblocco){
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
                    onClick = {
                        sblocco = true
                        viewModel.variabili.Sblocco = 1L
                        writeVariables(viewModel.variabili)
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .scale(scale),
                    shape = RoundedCornerShape(3.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
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

    }


    //cambioVariabili(variabili = viewModel.variabili)
}