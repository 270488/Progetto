package it.polito.database.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun AccessoESicureezzaScreen(viewModel: AppViewModel, navController: NavHostController) {
    val pozioni = listOf(
        "Modifica mail",
        "Modifica password",
        "Metodo di pagamento",
        //"Preferenze cookies",
        "Logout"
    )
    var credenziali= database.child("utenti").child(viewModel.uid)
    var email by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }

    credenziali.addValueEventListener(object: ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            email=dataSnapshot.child("email").value.toString()
            pw=dataSnapshot.child("password").value.toString()
        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = 100.dp))
    {
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
        pozioni.forEach { name ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(
                        if(name == "Logout") MaterialTheme.colorScheme.secondaryContainer
                        else MaterialTheme.colorScheme.secondary
                    )
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(12.dp)
                    .animateContentSize()
            ) {
                Column {
                    Text(
                        text = name,
                        fontFamily = fontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (name == "Logout") MaterialTheme.colorScheme.tertiary
                        else MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.clickable {
                            if (name == "Preferenze cookies") {
                                navController.navigate(Screen.PreferenzeCookies.route)
                            }
                        }
                    )
                        if (name == "Modifica mail") {
                            Text(
                                text = email,
                                fontFamily = fontFamily,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.primaryContainer
                            )
                        } else if (name == "Modifica password") {
                            Row {
                                pw.forEach {
                                    Text(
                                        text = "• ",
                                        fontFamily = fontFamily,
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }
                }
                IconButton(onClick = {
                    if (name == "Logout")
                        navController.navigate(Screen.AuthenticationScreen.route)
                }) {
                    if (name != "Preferenze cookies" && name != "Logout") {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            tint = MaterialTheme.colorScheme.tertiary,
                            contentDescription = "selezione",
                            modifier = Modifier.size(32.dp)
                        )
                    } else if (name == "Logout") {
                        Icon(
                            painter = painterResource(id = R.drawable.frecciadx),
                            tint = MaterialTheme.colorScheme.tertiary,
                            contentDescription = "selezione",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
        }
    }
}
