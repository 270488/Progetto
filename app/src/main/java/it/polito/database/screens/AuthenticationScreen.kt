package it.polito.database.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationScreen(context: AuthenticationActivity){
    val auth=Firebase.auth

    Column(modifier= Modifier
        .fillMaxSize()
        .padding(8.dp)) {
        val emailValue= remember{ mutableStateOf(TextFieldValue()) }
        val passwordValue= remember{ mutableStateOf(TextFieldValue()) }

        OutlinedTextField(value = emailValue.value,
            label = { Text("Emal")},
            onValueChange = {
                            emailValue.value=it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(value = passwordValue.value,
            label = { Text("Password")},
            onValueChange = {
                passwordValue.value=it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Button(modifier= Modifier.fillMaxWidth(),
            onClick = {
                auth.signInWithEmailAndPassword(emailValue.value.text.trim(), passwordValue.value.text.trim())
                    .addOnCompleteListener(context){
                            task->
                        if(task.isSuccessful) {
                                Log.d("AUTH", "Success")
                        }
                        else {Log.d("AUTH", "Failed: ${task.exception}")
                        //errore login fallito
                    }}
            }
        )
        {
            Text(text = "Log In")
        }

    }
}