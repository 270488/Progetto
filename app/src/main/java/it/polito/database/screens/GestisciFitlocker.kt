package it.polito.database.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
fun GestisciFitlockerScreen(viewModel: AppViewModel, navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = 80.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.fitlocker_logo),
            contentDescription = "Logo fitlocker",
            modifier = Modifier
                .size(140.dp)
        )
        Text(
            text = "FitLocker",
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 0.dp, y = (-14).dp),
            textAlign = TextAlign.Center,
            fontFamily = fontFamily,
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        InfoLocker(navController)
    }
}

@Composable
fun InfoLocker(navController: NavController) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )
        {
            Text(
                text = "Locker predefinito",
                fontFamily = fontFamily,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )
        {
            Text(
                text = "FitLocker Via San Paolo, 25, Torino (TO), 10138",
                fontFamily = fontFamily,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        )
        {
            TextButton(
                onClick = {
                          //TODO
                },
                modifier = Modifier
                    .layoutId("chooseAnotherLocker")
                    .padding(top = 20.dp, end = 8.dp),
                shape = RoundedCornerShape(3.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ),
            ) {
                Text(
                    text = "Scegli un altro locker",
                    fontFamily = fontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Image(
                    painter = painterResource(id = R.drawable.fitlocker_logo),
                    contentDescription = "logo"
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )
        {
            Text(
                text = "Locker usati di recente",
                fontFamily = fontFamily,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )
        {
            var listaLocker by remember { mutableStateOf<List<String>>(emptyList()) }

            var locker = database.child("locker")
            locker.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) { //fa una foto al db in quel momento e la mette in dataSnapshot
                    // Itera sui figli del nodo
                    var list = mutableListOf<String>()


                    for (childSnapshot in dataSnapshot.children) { //prende i figli di prodotti, quindi 0, 1...
                        // Aggiungi il prodotto alla lista
                        list.add(childSnapshot.value.toString())


                    }
                    listaLocker = list
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Gestisci gli errori qui
                    println("Errore nel leggere i dati dal database: ${databaseError.message}")
                }
            })
            Card {
                listaLocker.forEach { l ->
                    Text(
                        text = l,
                        fontFamily = fontFamily,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
            Spacer(modifier = Modifier.height(16.dp))

            Row(){
                TextButton(
                    onClick = {
                        navController.navigate(Screen.AccountScreen.route)
                    },
                    modifier = Modifier
                        .layoutId("il mio account")
                        .padding(top = 20.dp, end = 8.dp),
                    shape = RoundedCornerShape(3.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                ) {
                    Text(
                        text = "Il mio account",
                        fontFamily = fontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


    }

}
