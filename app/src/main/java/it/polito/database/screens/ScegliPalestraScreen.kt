package it.polito.database.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import it.polito.database.AppViewModel
import it.polito.database.R
import it.polito.database.ui.theme.Grey40
import it.polito.database.ui.theme.fontFamily

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ScegliPalestraScreen(viewModel: AppViewModel, navController: NavController) {
    var listaPalestre = viewModel.listaPalestre
    var searchText by remember { mutableStateOf("") }
    var selectedCardIndex by remember { mutableStateOf(-1) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = 90.dp, bottom = 100.dp)
    ) {

        OutlinedTextField(
            shape = RoundedCornerShape(8.dp),
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .padding(16.dp),
            placeholder =
            { Text(
                    text = "Inserisci CAP, indirizzo...",
                    fontFamily = fontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.primaryContainer
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primaryContainer
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.onPrimary,
                focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                focusedLeadingIconColor = MaterialTheme.colorScheme.tertiary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.primaryContainer,
                selectionColors = TextSelectionColors(
                    handleColor = MaterialTheme.colorScheme.tertiary,
                    backgroundColor = Color(0x4DFFED37)
                ),
            ),
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .verticalScroll(rememberScrollState())
        ) {
            val filteredPalestre = listaPalestre.filter {
                it.contains(searchText, ignoreCase = true)
            }

            filteredPalestre.forEachIndexed { index, i ->
                Column() {//modifier = Modifier.height(40.dp)
                    PalestraCard(
                        vm = viewModel,
                        palestra = i,
                        navController = navController,
                    )
                    if (index < filteredPalestre.size - 1)
                        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PalestraCard(vm: AppViewModel, navController: NavController, palestra: String) {
    val separazione = palestra.split("/").toTypedArray()
    val indirizzo = separazione[0]
    val citta = separazione[1]

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable {
                vm.setLocker(palestra)
                navController.navigateUp()
            },
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

    /*
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
    }*/
}
