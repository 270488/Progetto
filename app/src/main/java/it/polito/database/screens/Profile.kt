package it.polito.database.screens

import androidx.compose.animation.animateContentSize
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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
fun ProfileScreen(viewModel: AppViewModel, navController: NavHostController) {
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


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = 100.dp)
    ) {
        // Icona e Scritte
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Icona
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(120.dp)
                    .weight(0.35f)
                    .border(1.dp, MaterialTheme.colorScheme.tertiary, CircleShape)
            ){
                Image(
                    painter = painterResource(id = R.drawable.profile_picture),
                    contentDescription = "Profile icon",
                    contentScale = ContentScale.Crop,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            // Scritte
            Box(
                modifier = Modifier
                    .height(90.dp)
                    .weight(0.65f)
            ){
                Text(
                    text = buildAnnotatedString {
                        append("Ciao,\n")
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.tertiary,
                                fontSize = 34.sp
                            )
                        ){
                            append(nome.capitalize())
                        }
                    },
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    lineHeight = 40.sp,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.TopStart)
                )
                Text(
                    text = "Cambia account/Esci",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 14.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .clickable {
                            openAlertDialog=true
                        }
                        .align(Alignment.BottomEnd)
                        .offset(x = 0.dp, y = 6.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(60.dp))
        // Card con opzioni
        Opzioni(navController)
    }
}

@Composable
fun Opzioni(navController: NavHostController) {
    val elenco = listOf(
        "I miei ordini",
        "I miei preferiti",
        "I miei resi",
        "Il mio account",
        "Gestisci FitLocker"
    )
    Column {
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
        elenco.forEach { name ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(12.dp)
                    .fillMaxWidth()
                    .animateContentSize()
            )  {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = name,
                        fontFamily = fontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    if(name == "Gestisci FitLocker"){
                        Spacer(modifier = Modifier.width(30.dp))
                        Image(
                            painter = painterResource(id = R.drawable.fitlocker_logo),
                            contentDescription = "Profile icon",
                            modifier = Modifier.size(60.dp))
                    }
                }

                IconButton(onClick = {
                    if (name =="I miei preferiti" )
                        navController.navigate(Screen.FavoritesScreen.route)
                    else if (name =="I miei resi" )
                        navController.navigate(Screen.ResiScreen.route)
                    else if (name == "Il mio account")
                        navController.navigate(Screen.AccountScreen.route)
                    else if(name=="I miei ordini")
                        navController.navigate(Screen.Orders.route)
                    else if (name =="Gestisci FitLocker" )
                        navController.navigate(Screen.GestisciFitlockerScreen.route)

                }) {
                    Icon(
                        painter =  painterResource(id = R.drawable.frecciadx),
                        tint = MaterialTheme.colorScheme.tertiary,
                        contentDescription = "selezione",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
        }
    }

}
