package it.polito.database.screens

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.End
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.fontFamily

@OptIn(ExperimentalMaterial3Api::class)

//email: elena@gmail.com
//passwore: elena18
@Composable
fun AuthenticationScreen(navController: NavHostController,context: AuthenticationActivity) {
    val auth = Firebase.auth

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = 74.dp)
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = "Effettua il login per accedere al tuo profilo",
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            )
        }

        val emailValue = remember { mutableStateOf(TextFieldValue()) }
        val passwordValue = remember { mutableStateOf(TextFieldValue()) }
        Spacer(modifier = Modifier.padding(14.dp))
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(start = 50.dp, end = 50.dp)
        ) {
            OutlinedTextField(
                value = emailValue.value,
                shape = MaterialTheme.shapes.large,
                placeholder =
                { Text(
                    text = "Email",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp)
                },
                onValueChange = {
                    emailValue.value = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    textColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .fillMaxWidth()
                //Con questo si aggiunge un bordo nero fisso ma si copre il bordo bianco quando il text field è focused
                    /*.border(BorderStroke(width = 3.dp, MaterialTheme.colorScheme.onBackground),
                            shape = MaterialTheme.shapes.large)*/
            )
            Spacer(modifier = Modifier.padding(10.dp))
            OutlinedTextField(
                value = passwordValue.value,
                shape = MaterialTheme.shapes.large,
                placeholder =
                { Text(
                    text = "Password",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp)
                },
                onValueChange = {
                    passwordValue.value = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    textColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(8.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.tertiary,
                        shape = MaterialTheme.shapes.large)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                onClick = {
                    auth.signInWithEmailAndPassword(
                        emailValue.value.text.trim(),
                        passwordValue.value.text.trim()
                    )
                        .addOnCompleteListener(context) { task ->
                            if (task.isSuccessful) {
                                Log.d("AUTH", "Success")
                                navController.navigate(Screen.Home.route)
                            } else {
                                Log.d("AUTH", "Failed: ${task.exception}")
                                //errore login fallito
                            }
                        }
                }
            )
            {
                Text(
                    text = "Log In",
                    fontSize = 20.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.End).padding(top = 10.dp)
            ) {
                Text(
                    text = "Non hai un account?",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 15.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                    )
                Spacer(modifier = Modifier.width(3.dp)) // Aggiungi spazio tra i due testi
                Text(
                    text = "Registrati",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 15.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.clickable(onClick = {
                        navController.navigate(Screen.NewAccount.route)
                    })
                )

            }
        }

    }
}