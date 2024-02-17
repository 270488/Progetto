package it.polito.database.screens

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
import it.polito.database.AppViewModel
import it.polito.database.ui.theme.fontFamily

@Composable
fun PreferenzaNotifiche(viewModel: AppViewModel, navController: NavController){
    PreferenzaNotificheScreen(viewModel,navController)
}

@Composable
fun PreferenzaNotificheScreen(viewModel: AppViewModel, navController: NavController, modifier: Modifier = Modifier
    .padding(top = 74.dp)
    .padding(bottom = 74.dp)) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier= Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(top = 90.dp, bottom = 110.dp)
    )
    {
        Column (
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier= Modifier
                .padding(horizontal = 20.dp)
        ){
                Text(
                    text = "Preferenze sulle notifiche",
                    fontFamily = fontFamily,
                    color = Color.Black,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White,
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
                            fontWeight = FontWeight.SemiBold,
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
                    NotificheSwitch(modifier = Modifier
                        .padding(end = 30.dp)
                        .weight(0.4f), "opzione1", viewModel)
                }
                Spacer(modifier = Modifier.height(16.dp) )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.padding(start = 16.dp, end = 16.dp))
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
                            fontWeight = FontWeight.SemiBold,
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
                    NotificheSwitch(modifier = Modifier
                        .padding(end = 30.dp)
                        .weight(0.4f),"opzione2",viewModel)
                }
            }
        }
    }
}



@Composable
fun NotificheSwitch(modifier: Modifier, opt: String, viewModel: AppViewModel) {
    var checked by remember { mutableStateOf(false) }

    Switch(
        checked = checked,
        onCheckedChange = {
            checked = !checked
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = Color.Black,
            uncheckedThumbColor = Color.Black,
            uncheckedTrackColor = Color.White,
        )
    )
}