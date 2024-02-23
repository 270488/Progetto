package it.polito.database.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import it.polito.database.AppViewModel
import it.polito.database.R
import it.polito.database.ui.theme.fontFamily

@Composable
fun PaeseELinguaScreen(viewModel: AppViewModel, navController: NavHostController) {
    var selectedCountry = remember { mutableStateOf<Paese?>(null)}
    var selectedLanguage = remember { mutableStateOf<Lingua?>(null)}

    Column (Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Paese/Territorio",
                color = Color.Black,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Start
            )
        }
        Spacer(modifier = Modifier.height(8.dp) )

        Paese( selectedCountry = selectedCountry.value){
                country -> selectedCountry.value = country
        }
    }
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Lingua:",
                color = Color.Black,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Start
            )
        }
        Spacer(modifier = Modifier.height(8.dp) )

        Lingua( selectedLanguage= selectedLanguage.value){
                language -> selectedLanguage.value = language
        }
    }
}
}

enum class Paese {
    Austria,Belgio, Bulgaria,Cipro, Croazia, Danimarca,Estonia,Finlandia,Francia, Germania, Grecia,
    Irlanda,Italia, Lettonia, Lituania, Lussemburgo,  Malta, Polonia,
    Portogallo, Romania, Slovenia, Slovacchia,Spagna,Svezia,Ungheria
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Paese(
    selectedCountry: Paese?,
    onCountrySelected: (Paese) -> Unit
){
    val countries = enumValues<Paese>().toList()
    var isExpanded by remember { mutableStateOf(false) }
    var chosenCountry = selectedCountry.toString();
    if (selectedCountry===null){
        chosenCountry="Austria";
    }

    val icon = if (isExpanded){
        painterResource(id = R.drawable.frecciasopra)
    } else painterResource(id = R.drawable.frecciasotto)

    Column(modifier = Modifier.fillMaxWidth()){
        OutlinedTextField(
            shape = MaterialTheme.shapes.large,
            value = chosenCountry,
            onValueChange = {},
            readOnly = false,
            trailingIcon = {
                Icon(icon , "", tint = Color.Black,
                    modifier = Modifier
                        .clickable { isExpanded = !isExpanded }
                        .size(16.dp))
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = if (isExpanded) MaterialTheme.colorScheme.onSecondary else Color.Black,
                textColor = MaterialTheme.colorScheme.onBackground
            ),
            textStyle = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()

        )
        DropdownMenu(
            offset = DpOffset(0.dp, (4).dp),
            modifier = Modifier
                .padding(vertical = 2.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .verticalScroll(rememberScrollState())
                .height(400.dp)
                .border(1.dp, MaterialTheme.colorScheme.onPrimary),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            for (index in countries.indices) {
                val country = countries[index]
                DropdownMenuItem(
                    text = {
                        Text(
                            text = country.toString(),
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.outline
                        ) },
                    onClick = {
                        onCountrySelected(country)
                        isExpanded = false
                    }
                )
                if (index < countries.size - 1){
                    Divider(thickness = 2.dp, color = Color(0x1A000000))
                }

            }
        }
    }
}

enum class Lingua {
    Deutsch,Français, English,Español, Italiano, Polski,Português,Slovenščina,中文,日本語,한국어
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Lingua(
    selectedLanguage: Lingua?,
    onLanguageSelected: (Lingua) -> Unit
){
    val languages = enumValues<Lingua>().toList()
    var isExpanded by remember { mutableStateOf(false) }
    var chosenLanguage = selectedLanguage.toString();
    if (selectedLanguage===null){
        chosenLanguage="Italiano";
    }

    val icon = if (isExpanded){
        painterResource(id = R.drawable.frecciasopra)
    } else painterResource(id = R.drawable.frecciasotto)

    Column(modifier = Modifier.fillMaxWidth()){
        OutlinedTextField(
            shape = MaterialTheme.shapes.large,
            value = chosenLanguage,
            onValueChange = {},
            readOnly = false,
            trailingIcon = {
                Icon(icon , "", tint = Color.Black,
                    modifier = Modifier
                        .clickable { isExpanded = !isExpanded }
                        .size(16.dp))
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = if (isExpanded) MaterialTheme.colorScheme.onSecondary else Color.Black,
                textColor = MaterialTheme.colorScheme.onBackground
            ),
            textStyle = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()

        )
        DropdownMenu(
            offset = DpOffset(0.dp, (4).dp),
            modifier = Modifier
                .padding(vertical = 2.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .verticalScroll(rememberScrollState())
                .height(400.dp)
                .border(1.dp, MaterialTheme.colorScheme.onPrimary),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            for (index in languages.indices) {
                val language = languages[index]
                DropdownMenuItem(
                    text = {
                        Text(
                            text = language.toString(),
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.outline
                        ) },
                    onClick = {
                        onLanguageSelected(language)
                        isExpanded = false
                    }
                )
                if (index < languages.size - 1){
                    Divider(thickness = 2.dp, color = Color(0x1A000000))
                }

            }
        }
    }
}
