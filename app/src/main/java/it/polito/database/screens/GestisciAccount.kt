package it.polito.database.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavHostController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.R
import it.polito.database.database
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.fontFamily

@Composable
fun GestisciAccountScreen(viewModel: AppViewModel, navController: NavHostController) {
    var credenziali= database.child("utenti").child(viewModel.uid)
    var nome by remember { mutableStateOf("") }

    credenziali.addValueEventListener(object: ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            nome=dataSnapshot.child("nome").value.toString()
        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = 100.dp)
    ) {
        // Icona e Scritte
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Profile icon",
            modifier = Modifier
                .size(160.dp)
                .border(1.dp, MaterialTheme.colorScheme.tertiary, CircleShape)
        )
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Icona di pedice",
            tint = MaterialTheme.colorScheme.tertiary, // Colore dell'icona
            modifier = Modifier.offset(x = (-55).dp,y = (-35).dp,)
                .size(40.dp)
        )
        //Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = nome.capitalize(),
            modifier = Modifier.fillMaxWidth().offset(x = 0.dp, y = (-14).dp),
            textAlign = TextAlign.Center,
            fontFamily = fontFamily,
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Edit(navController)


    }
}
@Composable
fun Edit(navController: NavHostController) {
    val elenco = listOf(
        "Modifica dati",
        "Gestisci FitLocker",
        "Cambia account",
        "Logout"
    )
    Column (modifier = Modifier.padding(horizontal = 24.dp)) {
        elenco.forEach { name ->
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
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = buildAnnotatedString {
                                            append("Sei sicuro di voler uscire?")
                                        },
                                        fontSize = 22.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        fontFamily = fontFamily,
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))
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
                                            }
                                        ) {
                                            Text(
                                                text = "Annulla",
                                                fontFamily = fontFamily,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        TextButton(
                                            modifier = Modifier.width(146.dp),
                                            shape = RoundedCornerShape(3.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.tertiary,
                                                contentColor = MaterialTheme.colorScheme.onTertiary
                                            ),
                                            onClick = {
                                                navController.navigate(Screen.AuthenticationScreen.route)
                                                openAlertDialog = false
                                            }
                                        ) {
                                            Text(
                                                text = "Si, voglio uscire",
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

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (name == "Logout") MaterialTheme.colorScheme.secondaryContainer
                            else MaterialTheme.colorScheme.secondary,
                            shape = MaterialTheme.shapes.large
                        )
                        .border(
                            width = 1.dp, color = MaterialTheme.colorScheme.tertiary,
                            shape = MaterialTheme.shapes.large
                        )
                        .clickable {
                            if (name == "Logout" || name == "Cambia account") {
                                openAlertDialog=true
                            } else if (name == "Modifica dati") {
                                navController.navigate(Screen.ModificaDati.route)
                            } else if (name == "Gestisci FitLocker")
                                navController.navigate(Screen.GestisciFitlockerScreen.route)
                            //TODO else if () per le altre sezioni
                        }
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = name,
                            fontFamily = fontFamily,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.offset(x = 0.dp, y = (-2).dp)
                        )

                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
/*Button(
modifier = Modifier
.fillMaxWidth()
.border(
width = 1.dp, color = MaterialTheme.colorScheme.tertiary,
shape = MaterialTheme.shapes.large
),
colors = ButtonDefaults.buttonColors(
containerColor = MaterialTheme.colorScheme.secondary
),

Text(
text = "Registrati",
fontSize = 22.sp,
fontFamily = fontFamily,
fontWeight = FontWeight.Bold,
color = MaterialTheme.colorScheme.tertiary,
modifier = Modifier.offset(x = 0.dp, y = (-2).dp)
)*/