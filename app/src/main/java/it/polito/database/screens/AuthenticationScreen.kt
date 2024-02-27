package it.polito.database.screens

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import it.polito.database.AppViewModel
import it.polito.database.R
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.fontFamily

@OptIn(ExperimentalMaterial3Api::class)

//email: elena@gmail.com
//passwore: elena18
@Composable
fun AuthenticationScreen(navController: NavHostController,context: AuthenticationActivity, viewModel: AppViewModel) {
    val auth = Firebase.auth

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(6.dp)
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Image(
            painter = painterResource(id = R.drawable.logomcfit),
            contentDescription = "",
            modifier = Modifier
                .width(250.dp)
                .height(100.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.FillBounds
        )
        Spacer(modifier = Modifier.height(36.dp))
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = "Effettua il login per accedere al tuo profilo",
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        }
        val emailValue = remember { mutableStateOf(TextFieldValue()) }
        val passwordValue = remember { mutableStateOf(TextFieldValue()) }
        val emailDomain = emailValue.value.text.trim().substringAfterLast('@')
        Spacer(modifier = Modifier.height(32.dp))
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
                    fontSize = 18.sp)
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
            Spacer(modifier = Modifier.height(26.dp))
            OutlinedTextField(
                value = passwordValue.value,
                shape = MaterialTheme.shapes.large,
                visualTransformation = PasswordVisualTransformation(),
                placeholder =
                { Text(
                    text = "Password",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic,
                    fontSize = 18.sp)
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

            Spacer(modifier = Modifier.height(26.dp))
            var ctx = LocalContext.current
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
                    Log.d("Auth", "email: ${emailValue.value.text.trim()} ")
                    Log.d("Auth", "password: ${passwordValue.value.text.trim()} ")
                    auth.signInWithEmailAndPassword(
                        emailValue.value.text.trim(),
                        passwordValue.value.text.trim()
                    )
                        .addOnCompleteListener(context) { task ->
                            if (task.isSuccessful) {
                                if (emailDomain == "mcfit.corriere.it") {
                                    Log.d("AUTH-C", "Success")
                                    navController.navigate(Screen.CorriereHome.route)
                                } else {
                                    Log.d("AUTH-U", "Success")
                                    navController.navigate(Screen.Home.route)
                                }

                                val userId = task.result?.user?.uid
                                if (userId != null) {
                                    viewModel.uid=userId}

                            } else {
                                Toast.makeText(ctx,"Email o password errate",
                                    Toast.LENGTH_SHORT).show()
                                Log.d("AUTH", "Failed: ${task.exception}")
                                //errore login fallito
                            }
                        }
                }
            )
            {
                Text(
                    text = "Log In",
                    fontSize = 22.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.offset(x = 0.dp, y = (-2).dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom,
                //modifier = Modifier.align(Alignment.End)
            ) {
                Spacer(modifier = Modifier.height(42.dp))
                Text(
                    text = "Non hai un account?",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 14.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                    )
                Spacer(modifier = Modifier.width(4.dp)) // Aggiungi spazio tra i due testi
                Text(
                    text = "Registrati",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 18.sp,
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