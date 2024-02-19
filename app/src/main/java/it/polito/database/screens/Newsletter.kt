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
fun Newsletter(viewModel: AppViewModel, navController: NavController){
    NewsletterScreen(viewModel,navController)
}

@Composable
fun NewsletterScreen(viewModel: AppViewModel, navController: NavController, modifier: Modifier = Modifier
    .padding(top = 74.dp)
    .padding(bottom = 74.dp)) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier= Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .verticalScroll(rememberScrollState())
            .padding(top = 90.dp, bottom = 110.dp)
    )
    {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.secondary,
                        shape = MaterialTheme.shapes.large
                    )
                    .padding(12.dp)
                    .padding(vertical = 16.dp)
            ) {
                Column(modifier = Modifier.padding(vertical = 16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Ricevi informazioni sulle prossime promozioni",
                            fontFamily = fontFamily,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier
                                .offset(x = 0.dp, y = (-2).dp)
                                .padding(horizontal = 16.dp)
                                .weight(0.7f)
                        )
                        ColoredSwitch(modifier = Modifier
                            .padding(end = 30.dp)
                            .weight(0.3f), "opzione1", viewModel)
                    }
                    Spacer(modifier = Modifier.height(16.dp) )
                    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.padding(start = 16.dp, end = 16.dp))
                    Spacer(modifier = Modifier.height(16.dp) )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Ricevi informazioni su eventi e collaborazioni",
                            fontFamily = fontFamily,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier
                                .offset(x = 0.dp, y = (-2).dp)
                                .padding(horizontal = 16.dp)
                                .weight(0.6f)
                        )
                        ColoredSwitch(modifier = Modifier
                            .padding(end = 30.dp)
                            .weight(0.4f),"opzione2",viewModel)
                    }
                }
            }
        }
    }



@Composable
fun ColoredSwitch(modifier: Modifier, opt: String, viewModel: AppViewModel) {
    var checked1 by remember { mutableStateOf(false) }
    var checked2 by remember { mutableStateOf(false) }

    var id = viewModel.uid

    var opzione1 by remember { mutableStateOf(false) }
    var opzione2 by remember { mutableStateOf(false) }
    var opz1 = database.child("utenti").child(id).child("newsletter")
    opz1.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            var o1 = dataSnapshot.child("opzione1").value as Boolean
            var o2 = dataSnapshot.child("opzione2").value as Boolean
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

    if(opt == "opzione1") {
        Switch(
            checked = checked1,
            onCheckedChange = {
                checked1 = !checked1
                if (opzione1 == false) {
                    database.child("utenti").child(id).child("newsletter").child("opzione1")
                        .setValue(true)
                } else if (opzione1 == true) {
                    database.child("utenti").child(id).child("newsletter").child("opzione1")
                        .setValue(false)
                }
                Log.d("opzioni finali: ", opzione1.toString())
                Log.d("opzioni finali: ", opzione2.toString())

            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.secondary,
                checkedTrackColor = MaterialTheme.colorScheme.outlineVariant,
                uncheckedThumbColor = MaterialTheme.colorScheme.outlineVariant,
                uncheckedTrackColor = MaterialTheme.colorScheme.secondary,
            )
        )
    } else if (opt == "opzione2") {

        Switch(
            checked = checked2,
            onCheckedChange = {
                checked2 = !checked2
                    if (opzione2 == false) {
                        database.child("utenti").child(id).child("newsletter").child("opzione2")
                            .setValue(true)
                    } else if (opzione2 == true) {
                        database.child("utenti").child(id).child("newsletter").child("opzione2")
                            .setValue(false)
                    }
                Log.d("opzioni finali: ", opzione1.toString())
                Log.d("opzioni finali: ", opzione2.toString())

            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.secondary,
                checkedTrackColor = MaterialTheme.colorScheme.outlineVariant,
                uncheckedThumbColor = MaterialTheme.colorScheme.outlineVariant,
                uncheckedTrackColor = MaterialTheme.colorScheme.secondary,
            )
        )
    }
}