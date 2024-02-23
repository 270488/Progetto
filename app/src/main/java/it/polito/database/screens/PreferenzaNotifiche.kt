package it.polito.database.screens

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
fun PreferenzaNotifiche(viewModel: AppViewModel, navController: NavController){
    PreferenzaNotificheScreen(viewModel,navController)
}

@Composable
fun PreferenzaNotificheScreen(viewModel: AppViewModel, navController: NavController)
{
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier= Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 70.dp, bottom = 250.dp)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(0.6f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Account",
                            fontFamily = fontFamily,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .offset(x = 0.dp, y = (-2).dp)
                                .padding(horizontal = 16.dp)
                        )
                        Text(
                            text = "Ricevi notifiche sulla sicurezza dell'account, sui pagamenti e sugli ordini.",
                            fontFamily = fontFamily,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start,
                            color = Color.Black,
                            modifier = Modifier
                                .offset(x = 0.dp, y = (-2).dp)
                                .padding(horizontal = 16.dp)
                        )
                    }
                    NotificheSwitch(modifier = Modifier, "account", viewModel)
                }
                Spacer(modifier = Modifier.height(16.dp) )
                Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(start = 16.dp, end = 4.dp))
                Spacer(modifier = Modifier.height(16.dp) )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier
                        .weight(0.6f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Spedizioni e consegne",
                            fontFamily = fontFamily,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .offset(x = 0.dp, y = (-2).dp)
                                .padding(horizontal = 16.dp)
                        )
                        Text(
                            text = "Scopri quando i tuoi pacchi vengono spediti e quando verranno consegnati.",
                            fontFamily = fontFamily,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start,
                            color = Color.Black,
                            modifier = Modifier
                                .offset(x = 0.dp, y = (-2).dp)
                                .padding(horizontal = 16.dp)
                        )
                    }
                    NotificheSwitch(modifier = Modifier,"spedizioni",viewModel)
                }
            }
        }
    }
}



@Composable
fun NotificheSwitch(modifier: Modifier, opt: String, viewModel: AppViewModel) {

    var checked1 by remember { mutableStateOf(false) }
    var checked2 by remember { mutableStateOf(false) }

    var id = viewModel.uid

    var opzione1 by remember { mutableStateOf(false) }
    var opzione2 by remember { mutableStateOf(false) }
    var opz1 = database.child("utenti").child(id).child("preferenzeNotifiche")
    opz1.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            var o1 = dataSnapshot.child("account").value as Boolean
            var o2 = dataSnapshot.child("spedizioni").value as Boolean
            opzione1 = o1
            opzione2 = o2
            if(opzione1==true){
                checked1=true
            }else{
                checked1=false
            }
            if(opzione2==true){
                checked2=true
            }else{
                checked2=false
            }
        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })

    Log.d("opzioni iniziali: ", opzione1.toString())
    Log.d("opzioni iniziali: ", opzione2.toString())

    if(opt == "account") {
        Switch(
            checked = checked1,
            onCheckedChange = {
                checked1 = !checked1
                if (opzione1 == false) {
                    database.child("utenti").child(id).child("preferenzeNotifiche").child("account")
                        .setValue(true)
                } else if (opzione1 == true) {
                    database.child("utenti").child(id).child("preferenzeNotifiche").child("account")
                        .setValue(false)
                }
                Log.d("opzioni finali: ", opzione1.toString())
                Log.d("opzioni finali: ", opzione2.toString())

            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color.Black,
                uncheckedThumbColor = Color.Black,
                uncheckedTrackColor = Color.White,
            )
        )
    } else if (opt == "spedizioni") {

        Switch(
            checked = checked2,
            onCheckedChange = {
                checked2 = !checked2
                if (opzione2 == false) {
                    database.child("utenti").child(id).child("preferenzeNotifiche").child("spedizioni")
                        .setValue(true)
                } else if (opzione2 == true) {
                    database.child("utenti").child(id).child("preferenzeNotifiche").child("spedizioni")
                        .setValue(false)
                }
                Log.d("opzioni finali: ", opzione1.toString())
                Log.d("opzioni finali: ", opzione2.toString())

            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color.Black,
                uncheckedThumbColor = Color.Black,
                uncheckedTrackColor = Color.White,
            )
        )
    }

}