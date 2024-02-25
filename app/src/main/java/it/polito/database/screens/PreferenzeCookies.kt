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
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import it.polito.database.AppViewModel
import it.polito.database.ui.theme.fontFamily

@Composable
fun PreferenzeCookies(viewModel: AppViewModel, navController: NavController){
    PreferenzeCookiesScreen(viewModel,navController)
}

@Composable
fun PreferenzeCookiesScreen(viewModel: AppViewModel, navController: NavController){


    val biscotti = listOf(
        "Archiviare informazioni su dispositivo e/o accedervi",
        "Uso di dati limitati per la selezione della pubblicità",
        "Creare profili di contenuto peprsonalizzato",
        "Uso dei profili per la selezione di contenuto personalizzato",
        "Sviluppare e migliorare i servizi",
        "Assicurare sicurezza, prevenire frodi e debug"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = 90.dp, bottom = 110.dp)
            .verticalScroll(rememberScrollState())
    )
    {
        biscotti.forEach {b ->
            Column() {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = b,
                        fontFamily = fontFamily,
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier.widthIn(0.dp, 280.dp)
                    )
                    BiscottiSwitch(
                        modifier = Modifier
                            .layoutId("switch"),
                        "b${biscotti.indexOf(b)}", viewModel
                    )
                }
                if (b != biscotti.last())
                Divider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            /*
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White,
                        shape = MaterialTheme.shapes.large
                    )
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
                                text = b,
                                fontFamily = fontFamily,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Start,
                                color = Color.Black,
                                modifier = Modifier
                                    .offset(x = 0.dp, y = (-2).dp)
                                    .padding(horizontal = 16.dp)
                            )
                        }
                        BiscottiSwitch(
                            modifier = Modifier
                                .padding(end = 30.dp)
                                .weight(0.4f), "b${biscotti.indexOf(b)}", viewModel
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    }
                }*/
            }
        }
    }


    @Composable
    fun BiscottiSwitch(modifier: Modifier, opt: String, viewModel: AppViewModel) {

        var checked1 by remember { mutableStateOf(false) }
        var checked2 by remember { mutableStateOf(false) }
        var checked3 by remember { mutableStateOf(false) }
        var checked4 by remember { mutableStateOf(false) }
        var checked5 by remember { mutableStateOf(false) }
        var checked6 by remember { mutableStateOf(false) }

        if (opt == "b1"){
            Switch(
                checked = checked1,
                onCheckedChange = {
                    checked1 = !checked1
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.secondary,
                    checkedTrackColor = MaterialTheme.colorScheme.outlineVariant,
                    uncheckedThumbColor = MaterialTheme.colorScheme.outlineVariant,
                    uncheckedTrackColor = MaterialTheme.colorScheme.secondary,
                )
            )
        } else if(opt == "b2") {
            Switch(
                checked = checked2,
                onCheckedChange = {
                    checked2 = !checked2
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.secondary,
                    checkedTrackColor = MaterialTheme.colorScheme.outlineVariant,
                    uncheckedThumbColor = MaterialTheme.colorScheme.outlineVariant,
                    uncheckedTrackColor = MaterialTheme.colorScheme.secondary,
                )
            )
        } else if (opt == "b3"){
                Switch(
                    checked = checked3,
                    onCheckedChange = {
                        checked3 = !checked3
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.secondary,
                        checkedTrackColor = MaterialTheme.colorScheme.outlineVariant,
                        uncheckedThumbColor = MaterialTheme.colorScheme.outlineVariant,
                        uncheckedTrackColor = MaterialTheme.colorScheme.secondary,
                    )
                )
            } else if(opt == "b4"){
                Switch(
                    checked = checked4,
                    onCheckedChange = {
                        checked4 = !checked4
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.secondary,
                        checkedTrackColor = MaterialTheme.colorScheme.outlineVariant,
                        uncheckedThumbColor = MaterialTheme.colorScheme.outlineVariant,
                        uncheckedTrackColor = MaterialTheme.colorScheme.secondary,
                    )
                )
        } else if (opt == "b5"){
            Switch(
                checked = checked5,
                onCheckedChange = {
                    checked5 = !checked5
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.secondary,
                    checkedTrackColor = MaterialTheme.colorScheme.outlineVariant,
                    uncheckedThumbColor = MaterialTheme.colorScheme.outlineVariant,
                    uncheckedTrackColor = MaterialTheme.colorScheme.secondary,
                )
            )
        } else if(opt == "b0"){
            Switch(
                checked = checked6,
                onCheckedChange = {
                    checked6 = !checked6
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


