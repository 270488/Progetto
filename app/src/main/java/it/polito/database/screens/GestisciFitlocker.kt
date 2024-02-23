package it.polito.database.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
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
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = 60.dp, bottom = 110.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
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
                    .offset(x = 0.dp, y = (-10).dp),
                textAlign = TextAlign.Center,
                fontFamily = fontFamily,
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoLocker(viewModel, navController)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.tertiary)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(3.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ),
                onClick = {
                    navController.navigate(Screen.AccountScreen.route)
                }
            ){
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

@Composable
fun InfoLocker(vm: AppViewModel, navController: NavController) {

    var selezionato by remember {
        mutableStateOf(vm.lockerSelezionato)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Locker selezionato",
            fontFamily = fontFamily,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        {
            Card(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(2.dp, Color.Black),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                )
            )
            {
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = buildAnnotatedString {
                        append(selezionato.substringBefore("/") + "\n")
                        withStyle(
                            style = SpanStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                            )
                        ){
                            append(selezionato.substringAfter("/"))
                        }
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = fontFamily,
                )
            }
        }
        //Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        )
        {
            Text(
                modifier = Modifier
                    .padding(top = 8.dp, end = 16.dp)
                    .layoutId("chooseAnotherLocker")
                    .clickable { navController.navigate(Screen.ScegliPalestraScreen.route) },
                text = "Scegli un altro locker",
                fontFamily = fontFamily,
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold
            )

        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Locker usati di recente",
            fontFamily = fontFamily,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(8.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                border = BorderStroke(2.dp, Color.Black),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                )
            ){
                var listaPalestre = listOf(
                    "McFit Via San Paolo, 25/Torino (TO), 10141",
                    "McFit Via Giuseppe Lagrange, 47/Torino (TO), 10123",
                    "McFit C.so Giulio Cesare, 29/Torino (TO), 10152",
                    "McFit Via Monginevro, 84/Torino (TO), 10138",
                )
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                ) {
                    var listaLocker = listaPalestre.filterNot { it == selezionato }
                    listaLocker.forEachIndexed { index, l ->
                        val indirizzo = l.substringBefore("/")
                        val citta = l.substringAfter("/", "")

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    vm.setLocker(l)
                                    selezionato = l
                                }
                                .padding(12.dp),
                            text = buildAnnotatedString {
                                append(indirizzo+"\n")
                                withStyle(
                                    style = SpanStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal,
                                    )
                                ){
                                    append(citta)
                                }
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White,
                            fontFamily = fontFamily,
                        )
                        if (index < listaLocker.size - 1)
                            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
                    }



                }
            }
        }
        //Spacer(modifier = Modifier.height(24.dp))
    }

}
