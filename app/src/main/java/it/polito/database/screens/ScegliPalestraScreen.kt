package it.polito.database.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.polito.database.AppViewModel
import it.polito.database.R

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ScegliPalestraScreen(viewModel: AppViewModel, navController: NavController) {
    var listaPalestre = listOf<String>(
        "McFit Via San Paolo, 25 \nTorino (TO), 10141",
        "McFit Via Giuseppe Luigi Lagrange, 47 \nTorino (TO), 10123",
        "McFit C.so Giulio Cesare, 29 \nTorino (TO), 10152"
    )
    var searchText by remember { mutableStateOf("") }
    var selectedCardIndex by remember { mutableStateOf(-1) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = 90.dp, bottom = 110.dp)
    ) {

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.onBackground),
            label = {
                Text("Cerca palestra", modifier = Modifier.padding(start = 25.dp))
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            singleLine = true
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .verticalScroll(rememberScrollState())
        ) {
            val filteredPalestre = listaPalestre.filter {
                it.contains(searchText, ignoreCase = true)
            }

            filteredPalestre.forEachIndexed { index, i ->
                PalestraCard(
                    palestra = i,
                    isSelected = selectedCardIndex == index,
                    onCardClick = { selectedCardIndex = index; viewModel.lockerSelezionato=i },

                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PalestraCard(palestra: String, isSelected: Boolean, onCardClick: () -> Unit) {
    val separazione = palestra.split("\n").toTypedArray()
    val indirizzo = separazione[0]
    val città = separazione[1]

    Card(
        onClick = onCardClick,
        modifier = Modifier.padding(10.dp)
            .background(if (isSelected) Color.Gray else Color.Transparent)
    ) {
        Row(modifier = Modifier.padding(5.dp).fillMaxWidth()) {
            Column() {
                Text(text = indirizzo)
                Text(text = città)
            }
            if (isSelected) {
                Column() {
                    Image(
                        painter = painterResource(id = R.drawable.frecciasopra),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(48.dp)
                    )
                }
            }
        }
    }
}
