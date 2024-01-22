
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.google.firebase.database.FirebaseDatabase
import it.polito.database.User
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
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    )


    {
        Text(text = "Crea account", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp) )

        var selectedCity = remember { mutableStateOf<City?>(null)}
        val nome = remember { mutableStateOf(TextFieldValue())}
        val cognome = remember { mutableStateOf(TextFieldValue())}
        var selectedGender = remember { mutableStateOf<Gender?>(null)}
        //val dataDiNascita = remember { mutableStateOf(TextFieldValue())} per ora non la includo
        val email = remember { mutableStateOf(TextFieldValue()) }
        val username = remember { mutableStateOf(TextFieldValue()) }
        val password = remember { mutableStateOf(TextFieldValue())}
        val confermaPassword = remember { mutableStateOf(TextFieldValue())}


        CitySelection( selectedCity = selectedCity.value){
            city -> selectedCity.value = city
        }
        Spacer(modifier = Modifier.height(16.dp) )
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

        GenderSelection(selectedGender = selectedGender.value){
            gen -> selectedGender.value = gen
        }

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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = SpaceEvenly
        ){
            OutlinedTextField(
                value = password.value,
                label = { Text("Password*") },
                onValueChange = {
                    password.value = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(2.dp) )
            OutlinedTextField(
                value = confermaPassword.value,
                label = { Text("Conferma Password*") },
                onValueChange = {
                    confermaPassword.value = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }
       Row(modifier = Modifier.align(CenterHorizontally))
       {
           Text(
               text = "Tutti i campi contrassegnati da * sono obbligatori"
           )
       }
        Spacer(modifier = Modifier.padding(8.dp))
        Button(modifier = Modifier.fillMaxWidth(),
            onClick = {
                val user = User(
                    nome = nome.value.text.trim(),
                    cognome = cognome.value.text.trim(),
                    city = selectedCity.value?.name ?: "",
                    gender = selectedGender.value?.name ?: "",
                    email = email.value.text.trim(),
                    username = username.value.text.trim(),
                    password = password.value.text.trim()
                )

                auth.createUserWithEmailAndPassword(
                    user.email,
                    user.password
                ).addOnCompleteListener(context) { task ->
                    if (task.isSuccessful) {
                        //TODO finire il controllo sul match delle password
                        Log.d("AUTH", "Registration Success")
                        val userId = task.result?.user?.uid
                        if (userId != null) {
                            val databaseReference = FirebaseDatabase.getInstance().getReference("utenti")
                            val userReference = databaseReference.child(userId)

                            userReference.child("nome").setValue(user.nome)
                            userReference.child("cognome").setValue(user.cognome)
                            userReference.child("città").setValue(user.city)
                            userReference.child("genere").setValue(user.gender)
                            userReference.child("email").setValue(user.email)
                            userReference.child("username").setValue(user.username)
                            userReference.child("password").setValue(user.password)

                            auth.signInWithEmailAndPassword(
                                user.email,
                                user.password
                            ).addOnCompleteListener(context) { signInTask ->
                                if (signInTask.isSuccessful) {
                                    Log.d("AUTH", "Login Success")
                                    navController.navigate(Screen.Home.route)
                                } else {
                                    Log.d("AUTH", "Login Failed: ${signInTask.exception}")
                                }
                            }
                        }
                    } else {
                        Log.e("AUTH", "Registration Failed: ${task.exception?.message}")
                    }
                }
            })


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySelection(
    selectedCity: City?,
    onCitySelected: (City) -> Unit
) {

    var isExpanded by remember {
        mutableStateOf(false) //default: menù chiuso
    }
    val cities = enumValues<City>().toList()

    Column(
        Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
        ) {
            Text("Città*")
        }

        // Dropdown Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()

        ){
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it }
            ) {
                TextField(
                    value = selectedCity.toString(),
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    }, modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                    modifier = Modifier
                        .height(200.dp)
                ) {
                    cities.forEach { city ->
                        DropdownMenuItem(
                            text = {
                                Text(text = city.toString())
                            },
                            onClick = {
                                onCitySelected(city)
                                isExpanded = false
                            })
                    }
                }
            }
        }
    }
}


@Composable
fun GenderSelection(
    selectedGender : Gender?,
    onGenderSelected: (Gender?) ->Unit) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Sesso*")

        GenderRadioButton(
            text = "Uomo",
            isSelected = selectedGender == Gender.MALE
        ) {
            onGenderSelected(Gender.MALE)
        }

        GenderRadioButton(
            text = "Donna",
            isSelected = selectedGender == Gender.FEMALE
        ) {
            onGenderSelected(Gender.FEMALE)
        }

        GenderRadioButton(
            text = "Altro",
            isSelected = selectedGender == Gender.OTHER
        ) {
            onGenderSelected(Gender.OTHER)
        }
    }
}

@Composable
fun GenderRadioButton(
    text: String,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelected
        )
        Text(text = text)
    }
}

enum class Gender {
    MALE, FEMALE, OTHER
}

enum class City  {
    Bari, Bergamo, Bologna, Brescia, Como, Cremona, Catania, Ferrara,
    Milano, Napoli, Piacenza, Padova, Perugia, Parma, Pavia, Roma,
    Torino, Treviso, Udine, Varese, Venezia, Vicenza, Verona
}
