package it.polito.database.screens

import CitySelection
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.User
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.fontFamily

@OptIn(ExperimentalMaterial3Api::class)

//email: elena@gmail.com
//password: elena18
@Composable
fun ModificaDati(navController: NavHostController, viewModel: AppViewModel, context: AuthenticationActivity) {

    var (currentEmail, setCurrentEmail) = remember { mutableStateOf("") }
    var (currentUsername, setCurrentUsername) = remember { mutableStateOf("") }
    val (currentCity, setCurrentCity) = remember { mutableStateOf<City?>(null) }

    val auth = Firebase.auth
    val currentUser = auth.currentUser
    val userReference = FirebaseDatabase.getInstance().getReference("utenti").child(currentUser?.uid ?: "")

    LaunchedEffect(Unit) {
        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = User.fromMap(snapshot.value as Map<String, Any>)
                setCurrentEmail(user.email)
                setCurrentUsername(user.username)
                val cityString = user.city
                setCurrentCity(City.fromString(cityString))
            }

            override fun onCancelled(error: DatabaseError) {
                // Gestisci eventuali errori di recupero dati
            }
        })
    }
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


            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Città",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start
                )
            }
            Spacer(modifier = Modifier.height(8.dp) )

            CitySelection( selectedCity = currentCity){
                    city -> setCurrentCity(city)
            }

            Spacer(modifier = Modifier.height(16.dp) )

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Email",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start
                )
            }
            Spacer(modifier = Modifier.height(8.dp) )
            OutlinedTextField(
                value = currentEmail,
                shape = MaterialTheme.shapes.large,
                placeholder = { Text(
                    text = "Email",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic,
                    fontSize = 18.sp)
                },
                onValueChange = {
                    setCurrentEmail(it)
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
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Username",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start
                )
            }
            Spacer(modifier = Modifier.height(8.dp) )
            OutlinedTextField(
                value = currentUsername,
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
                    setCurrentUsername(it)
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


            Text(
                text = "\nTutti i campi contrassegnati da * sono obbligatori.",
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 14.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.align(Alignment.CenterHorizontally)
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
                      val cityString = currentCity?.toString() ?: ""

                    val updates = hashMapOf<String, Any>(
                        "email" to currentEmail,
                        "username" to currentUsername,
                        "city" to cityString
                    ).toMap()

                    userReference.updateChildren(updates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                setCurrentEmail(currentEmail)
                                setCurrentUsername(currentUsername)
                                setCurrentCity(currentCity)
                                navController.navigate(Screen.GestisciAccountScreen.route)

                            } else {
                                Log.e("FirebaseUpdate", "Failed to update user data: ${task.exception?.message}")
                            }
                        }
                }
            )
            {
                Text(
                    text = "Modifica",
                    fontSize = 22.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.offset(x = 0.dp, y = (-2).dp)
                )
            }

        }
    }

}