
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import it.polito.database.screens.AuthenticationActivity
import it.polito.database.ui.theme.Screen


@OptIn(ExperimentalMaterial3Api::class)

//email: elena@gmail.com
//password: elena18
@Composable
fun NewAccount(navController: NavHostController,context: AuthenticationActivity) {
    val auth = Firebase.auth

    Column(
        modifier = Modifier
            .padding(top = 74.dp)
            .padding(8.dp)

    )
    {
        Text(text = "Crea account", style = MaterialTheme.typography.headlineSmall)

        val paese = remember { mutableStateOf(TextFieldValue())}
        val nome = remember { mutableStateOf(TextFieldValue())}
        val cognome = remember { mutableStateOf(TextFieldValue())}
        val genere = remember { mutableStateOf(TextFieldValue())}
        val dataDiNascita = remember { mutableStateOf(TextFieldValue())}
        val email = remember { mutableStateOf(TextFieldValue()) }
        val username = remember { mutableStateOf(TextFieldValue()) }
        val password = remember { mutableStateOf(TextFieldValue())}
        val confermaPassword = remember { mutableStateOf(TextFieldValue())}

            Text("Paese*")

       // CountryListScreen(navController)
        // TODO modificare le funzioni a fondo pagina affinchè i paesi compaiano in una lista drop down


        OutlinedTextField(
            value = nome.value,
            label = { Text("Nome*") },
            onValueChange = {
                nome.value = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = cognome.value,
            label = { Text("Cognome*") },
            onValueChange = {
                cognome.value = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        GenderSelection()

        OutlinedTextField(
            value = email.value,
            label = { Text("Email*") },
            onValueChange = {
                email.value = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = username.value,
            label = { Text("Username*") },
            onValueChange = {
                username.value = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password.value,
            label = { Text("Password*") },
            onValueChange = {
                password.value = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = confermaPassword.value,
            label = { Text("Conferma Password*") },
            onValueChange = {
                confermaPassword.value = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
       Row(modifier = Modifier.align(CenterHorizontally))
       {
           Text(
               text = "Tutti i campi contrassegnati da * sono obbligatori"
           )
       }
        Spacer(modifier = Modifier.padding(8.dp))
        Button(modifier = Modifier.fillMaxWidth(),
            onClick = {
                auth.createUserWithEmailAndPassword(
                    email.value.text.trim(),
                    password.value.text.trim()
                ).addOnCompleteListener(context) { task ->
                    if (task.isSuccessful && password == confermaPassword) {
                        //TODO finire il controllo sul match delle password
                        Log.d("AUTH", "Registration Success")
                        auth.signInWithEmailAndPassword(
                            email.value.text.trim(),
                            password.value.text.trim()
                        ).addOnCompleteListener(context) { signInTask ->
                            if (signInTask.isSuccessful) {
                                Log.d("AUTH", "Login Success")
                                navController.navigate(Screen.Home.route)
                            } else {
                                Log.d("AUTH", "Login Failed: ${signInTask.exception}")
                            }
                        }
                    }
                }
            }
        )
        {
            Text(text = "Registrati")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = "Hai già un account?",

                )
            Spacer(modifier = Modifier.width(8.dp)) // Aggiungi spazio tra i due testi
            Text(
                text = "Log in",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = {
                    navController.navigate(Screen.AuthenticationScreen.route)
                })
            )



        }
    }
}

@Composable
fun GenderSelection() {
    var selectedGender by remember { mutableStateOf<Gender?>(null) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically)
    {
        Text (text = "Sesso*")

        GenderCheckbox(
            text = "Uomo",
            isChecked = selectedGender == Gender.MALE,
            onCheckedChange = {
                selectedGender =
                    if (it)
                        Gender.MALE
                    else null
            })
        GenderCheckbox(
            text = "Donna",
            isChecked = selectedGender == Gender.FEMALE,
            onCheckedChange = {
                selectedGender =
                if (it)
                    Gender.FEMALE
                else null
            })

        GenderCheckbox(
            text = "Altro",
            isChecked = selectedGender == Gender.OTHER,
            onCheckedChange = {
                selectedGender =
                    if (it)
                        Gender.OTHER
                    else null
            })
    }
}


@Composable
fun GenderCheckbox(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .toggleable(
                    value = isChecked,
                    onValueChange = { onCheckedChange(!isChecked) }
                )

        )
        Text(text = text)
    }
}

enum class Gender {
    MALE, FEMALE, OTHER
}
/*
@Composable
fun CountryListScreen(navController: NavHostController) {
    val textVal = remember { mutableStateOf(TextFieldValue("")) }
    Column {
        SearchCountryList(textVal)
        CountryList(textVal)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCountryList(textVal: MutableState<TextFieldValue>) {
    TextField(
        value = textVal.value,
        onValueChange = { textVal.value = it },
        placeholder = { Text(text = "Search Country Name") },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = TextStyle(Color.Black, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (textVal.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        textVal.value = TextFieldValue("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun CountryList(textVal: MutableState<TextFieldValue>) {
    val context = LocalContext.current
    val countries = getListOfCountries()
    var filteredCountries: ArrayList<String>
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        val searchText = textVal.value.text
        filteredCountries = if (searchText.isEmpty()) {
            countries
        } else {
            val resultList = ArrayList<String>()
            for (country in countries) {
                if (country.lowercase(Locale.getDefault()).contains(searchText.lowercase(Locale.getDefault()))) {
                    resultList.add(country)
                }
            }
            resultList
        }
        items(filteredCountries) { filteredCountries ->
            CountryListItem(
                countryText = filteredCountries,
                onItemClick = { selectedCountry ->
                    Toast.makeText(context, selectedCountry, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
fun CountryListItem(
    countryText: String,
    onItemClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable {
                onItemClick(countryText)
            }
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = countryText,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}

@Composable
fun getListOfCountries(): ArrayList<String> {
    val isoCountryCodes = Locale.getISOCountries()
    val countryListWithEmojis = ArrayList<String>()
    for (countryCode in isoCountryCodes) {
        val locale = Locale("", countryCode)
        val countryName = locale.displayCountry
        val flagOffset = 0x1F1E6
        val asciiOffset = 0x41
        val firstChar = Character.codePointAt(countryCode, 0) - asciiOffset + flagOffset
        val secondChar = Character.codePointAt(countryCode, 1) - asciiOffset + flagOffset
        val flag = (String(Character.toChars(firstChar)) + String(Character.toChars(secondChar)))
        countryListWithEmojis.add("$countryName (${locale.country}) $flag")
    }
    return countryListWithEmojis
}
*/