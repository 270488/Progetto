package it.polito.database.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.R
import it.polito.database.database
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.fontFamily

@Composable
fun CorriereProfile(viewModel: AppViewModel,navController: NavController){

    var credenziali= database.child("corrieri").child("jSJNjHS9PENBjyBSz0NYOT3zz173")
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var tel by remember { mutableStateOf("") }

    credenziali.addValueEventListener(object: ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            nome=dataSnapshot.child("nome").value.toString()
            email=dataSnapshot.child("email").value.toString()
            tel=dataSnapshot.child("telefono").value.toString()
        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = 100.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(120.dp)
                    .weight(0.35f)
                    .border(1.dp, MaterialTheme.colorScheme.tertiary, CircleShape)
            ){
                Image(
                    painter = painterResource(id = R.drawable.corriere_foto),
                    contentDescription = "Profile icon",
                    contentScale = ContentScale.Crop,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .height(90.dp)
                    .weight(0.65f)
            ){
                Text(
                    text = buildAnnotatedString {
                        append("Buon lavoro,\n")
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.tertiary,
                                fontSize = 32.sp
                            )
                        ){
                            append(nome)
                        }
                    },
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    lineHeight = 40.sp,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.TopStart)
                )
            }
        }
        Spacer(modifier = Modifier.height(60.dp))
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(16.dp)) {
            Row {
                Text(
                    text = "E-mail: ",
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = email,
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                )
            }
            Row {
                Text(
                    text = "Tel: ",
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = tel,
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp
                )
            }


        }
        Spacer(modifier = Modifier.height(60.dp))
        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.padding(bottom = 30.dp)) {
            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(12.dp)
                        .fillMaxWidth()
                        .animateContentSize()
                )  {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Logout",
                            fontFamily = fontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }

                    IconButton(onClick = {
                            navController.navigate(Screen.AuthenticationScreen.route)

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
