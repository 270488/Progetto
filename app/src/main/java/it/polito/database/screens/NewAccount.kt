
import android.util.Log
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import it.polito.database.AppViewModel
import it.polito.database.City
import it.polito.database.CitySelection
import it.polito.database.User
import it.polito.database.screens.AuthenticationActivity
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.fontFamily


@OptIn(ExperimentalMaterial3Api::class)

//email: elena@gmail.com
//password: elena18
@Composable
fun NewAccount(navController: NavHostController,context: AuthenticationActivity, viewModel: AppViewModel) {
    val auth = Firebase.auth

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary))
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.primary)
        )
        {
            Spacer(modifier = Modifier.height(16.dp) )
            Text(
                text = "Crea account",
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(16.dp) )

            var selectedCity = remember { mutableStateOf<City?>(null)}
            val nome = remember { mutableStateOf(TextFieldValue())}
            val cognome = remember { mutableStateOf(TextFieldValue())}
            var selectedGender = remember { mutableStateOf<Gender?>(null)}
            //val dataDiNascita = remember { mutableStateOf(TextFieldValue())} per ora non la includo
            val email = remember { mutableStateOf(TextFieldValue()) }
            val username = remember { mutableStateOf(TextFieldValue()) }
            val password = remember { mutableStateOf(TextFieldValue())}
            val confermaPassword = remember { mutableStateOf(TextFieldValue())}

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Città*",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start
                )
            }
            Spacer(modifier = Modifier.height(8.dp) )

            CitySelection( selectedCity = selectedCity.value){
                    city -> selectedCity.value = city
            }

            Spacer(modifier = Modifier.height(16.dp) )

            Row (modifier = Modifier.fillMaxWidth())
            {
                OutlinedTextField(
                    value = nome.value,
                    shape = MaterialTheme.shapes.large,
                    placeholder = { Text(
                        text = "Nome*",
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Light,
                        fontStyle = FontStyle.Italic,
                        fontSize = 18.sp)
                    },
                    onValueChange = {
                        nome.value = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                        textColor = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.weight(1F),
                    textStyle = TextStyle(
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        color = Color.Black)
                )

                Spacer(modifier = Modifier.width(8.dp) )

                OutlinedTextField(
                    value = cognome.value,
                    shape = MaterialTheme.shapes.large,
                    placeholder = { Text(
                        text = "Cognome*",
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Light,
                        fontStyle = FontStyle.Italic,
                        fontSize = 18.sp)
                    },
                    onValueChange = {
                        cognome.value = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                        textColor = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.weight(1F),
                    textStyle = TextStyle(
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        color = Color.Black)
                )
            }

            GenderSelection(selectedGender = selectedGender.value){
                    gen -> selectedGender.value = gen
            }

            OutlinedTextField(
                value = email.value,
                shape = MaterialTheme.shapes.large,
                placeholder = { Text(
                    text = "Email*",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic,
                    fontSize = 18.sp)
                },
                onValueChange = {
                    email.value = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    textColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = Color.Black)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username.value,
                shape = MaterialTheme.shapes.large,
                placeholder = { Text(
                    text = "Username*",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic,
                    fontSize = 18.sp)
                },
                onValueChange = {
                    username.value = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    textColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = Color.Black)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password.value,
                shape = MaterialTheme.shapes.large,
                placeholder = { Text(
                    text = "Password*",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic,
                    fontSize = 18.sp)
                },
                onValueChange = {
                    password.value = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    textColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.fillMaxWidth() ,
                visualTransformation = PasswordVisualTransformation(),
                textStyle = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = Color.Black)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confermaPassword.value,
                shape = MaterialTheme.shapes.large,
                placeholder = { Text(
                    text = "Conferma password*",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic,
                    fontSize = 18.sp)
                },
                onValueChange = {
                    confermaPassword.value = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    textColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                textStyle = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = Color.Black)
            )

                Text(
                    text = "\nTutti i campi contrassegnati da * sono obbligatori.",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 14.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.align(CenterHorizontally)
                )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp, color = MaterialTheme.colorScheme.tertiary,
                    shape = MaterialTheme.shapes.large
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
            onClick = {
                if(password.value == confermaPassword.value) {
                    val user = User(
                        nome = nome.value.text.trim(),
                        cognome = cognome.value.text.trim(),
                        city = selectedCity.value?.name ?: "",
                        gender = selectedGender.value?.name ?: "",
                        email = email.value.text.trim(),
                        username = username.value.text.trim(),
                        password = password.value.text.trim(),
                        preferiti = emptyList(),
                        resi = emptyList(),
                        ordini = emptyList()
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

                                userReference.child("id").setValue(userId)
                                userReference.child("nome").setValue(user.nome)
                                userReference.child("cognome").setValue(user.cognome)
                                userReference.child("city").setValue(user.city)
                                userReference.child("gender").setValue(user.gender)
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
                }
                else{
                    // Alert pop-up
                    Log.e("AUTH", "Passwords don't match")
                }

            })
        {
            Text(
                text = "Registrati",
                fontSize = 22.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.offset(x = 0.dp, y = (-2).dp)
            )
        }

            Spacer(modifier = Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.Bottom,
                modifier = Modifier.align(Alignment.End)) {
                Text(
                    text = "Hai già un account?",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 14.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Login",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 18.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.clickable(onClick = {
                        navController.navigate(Screen.AuthenticationScreen.route)
                    })
                )

            }

        }
    }

}


@Composable
fun GenderSelection(
    selectedGender : Gender?,
    onGenderSelected: (Gender?) ->Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Sesso*",
            color = MaterialTheme.colorScheme.onSecondary,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
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
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.onPrimary
            ),
            selected = isSelected,
            onClick = onSelected
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSecondary,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Light,
            fontSize = 18.sp
        )
    }
}

enum class Gender {
    MALE, FEMALE, OTHER
}


