
import android.annotation.SuppressLint
import android.icu.text.CaseMap.Title
import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.End
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Start
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import it.polito.database.screens.AuthenticationActivity
import it.polito.database.ui.theme.Screen
/*import com.hbb20.CCPDropDown
import com.hbb20.ccp.CCP
import com.hbb20.ccp.Country*/
@OptIn(ExperimentalMaterial3Api::class)

//email: elena@gmail.com
//password: elena18
@Composable
fun NewAccount(navController: NavHostController,context: AuthenticationActivity) {
    val auth = Firebase.auth

    Column(

        modifier = Modifier
            .fillMaxSize()
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
           // CountryDropdown()


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
        Row(modifier = Modifier.align(Alignment.Start))
        {
            Text(
                text = "Sesso*"
            )
        }
        //TODO checkbox per genere
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
/*
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType")
@Composable
fun CountryDropdown() {
    val context = LocalContext.current
    val countryCodePicker = remember {
        CCPDropDown(context)
    }

    TextField(
        value = "",
        onValueChange = {},
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    countryCodePicker.performClick()
                },
                indication = rememberRipple(bounded = false)
            )
    )
}

@Composable
fun CCPCountryList(
    countryList: List<Country>,
    onCountrySelected: (Country) -> Unit
) {
    // Implementazione del composable per visualizzare la lista dei paesi
    // e gestire la selezione del paese
}


@Composable
fun CCPCountryListItem(
    country: Country,
    onCountrySelected: (Country) -> Unit
) {
    // Implementazione del composable per visualizzare singoli elementi della lista dei paesi
}*/