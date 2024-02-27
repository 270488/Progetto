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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import it.polito.database.AppViewModel
import it.polito.database.R
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.fontFamily

@Composable
fun Checkout(viewModel: AppViewModel, navController: NavController){
    CheckoutScreen(viewModel,navController)
}

@Composable
fun CheckoutScreen(viewModel: AppViewModel, navController: NavController, modifier: Modifier = Modifier
    .padding(top = 74.dp)
    .padding(bottom = 74.dp)) {

    val totale = viewModel.tot
    var isDialogOpen by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier= Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .verticalScroll(rememberScrollState())
            .padding(top = 90.dp, bottom = 110.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )
            {
                Text(
                    text = "Spedizione",
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 24.sp,
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
                    text = viewModel.lockerSelezionato.replace("/"," "),
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
                Button(
                    onClick = {
                        navController.navigate(Screen.ScegliPalestraScreen.route)
                    },
                    modifier = Modifier
                        .layoutId("chooseAnotherLocker")
                        .width(350.dp)
                        .padding(top = 12.dp, start = 6.dp, end = 6.dp)
                        .border(
                            width = 2.dp, color = MaterialTheme.colorScheme.tertiary,
                            shape = RoundedCornerShape(3.dp)
                        ),
                    shape = RoundedCornerShape(3.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.tertiary
                    ),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Scegli un altro locker",
                            fontFamily = fontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Image(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(id = R.drawable.fitlocker_logo),
                            contentDescription = "logo"
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(22.dp))
            Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.tertiary)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )
            {
                Text(
                    text = "Pagamento",
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = "Totale ordine:",
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    text = "%.2f".format(totale) + "€",
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )
            {
                Text(
                    text = "Seleziona metodo di pagamento:",
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                var icon1Selected by remember { mutableStateOf(true) }
                var icon2Selected by remember { mutableStateOf(false) }
                Icon(
                    painter = painterResource(id = R.drawable.paypal),
                    contentDescription = "logo PayPal",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            icon1Selected = true
                            icon2Selected = false
                        }
                        .alpha(if (icon1Selected) 1f else 0.4f),
                    tint = Color(0xFF14E3FF)

                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    painter = painterResource(id = R.drawable.carta),
                    contentDescription = "logo carta",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            icon2Selected = true
                            icon1Selected = false
                        }
                        .alpha(if (icon2Selected) 1f else 0.4f),
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth()
            )
            {
                Button(
                    onClick = {
                        if(!aggiungiOrdine(viewModel)){ //ritorna false se non ci sono sportelli disponibili
                            isDialogOpen=true

                        }
                        else
                            navController.navigate(Screen.Orders.route)
                    },
                    modifier = Modifier
                        .layoutId("payment")
                        .padding(top = 12.dp, end = 8.dp),
                    shape = RoundedCornerShape(3.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 16.dp,
                        pressedElevation = 0.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                ) {
                    Text(
                        text = "Vai al pagamento",
                        fontFamily = fontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                if(isDialogOpen){
                    Popup(
                        onDismissRequest = { isDialogOpen = false
                            navController.navigate(Screen.Cart.route)},
                        //modifier = Modifier.fillMaxSize()
                    ) { Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(30.dp)
                                .clickable { isDialogOpen = false },
                            painter = painterResource(id = R.drawable.back_arrow),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xCC1D232C))
                                .padding(horizontal = 20.dp)
                        ) {
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
                                Text(
                                    text = "Siamo spiacenti: \nil locker selezionato è al momento pieno.\nSi prega di riprovare più tardi. ",
                                    fontSize = 24.sp,
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
        }
    }

}
}
