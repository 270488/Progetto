
import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.End
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
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
    ) {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = "Crea il tuo profilo")
        }

        val emailValue = remember { mutableStateOf(TextFieldValue()) }
        val passwordValue = remember { mutableStateOf(TextFieldValue()) }

        //aggiungere altri campi se serve

        OutlinedTextField(
            value = emailValue.value,
            label = { Text("Emal") },
            onValueChange = {
                emailValue.value = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = passwordValue.value,
            label = { Text("Password") },
            onValueChange = {
                passwordValue.value = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Button(modifier = Modifier.fillMaxWidth(),
            onClick = {
                auth.createUserWithEmailAndPassword(
                    emailValue.value.text.trim(),
                    passwordValue.value.text.trim()
                ).addOnCompleteListener(context) { task ->
                    if (task.isSuccessful) {
                        Log.d("AUTH", "Registration Success")
                        auth.signInWithEmailAndPassword(
                            emailValue.value.text.trim(),
                            passwordValue.value.text.trim()
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