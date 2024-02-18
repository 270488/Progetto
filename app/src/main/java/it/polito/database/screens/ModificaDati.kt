package it.polito.database.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.R
import it.polito.database.User
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.fontFamily

@OptIn(ExperimentalMaterial3Api::class)

//email: elena@gmail.com
//password: elena18
@Composable
fun ModificaDati(navController: NavHostController, viewModel: AppViewModel) {

    //per i valori da Firebase e i valori modificati dall'utente
    val (currentEmail, setCurrentEmail) = remember { mutableStateOf("") }
    val (currentUsername, setCurrentUsername) = remember { mutableStateOf("") }
    val (currentCity, setCurrentCity) = remember { mutableStateOf<City?>(null) }

    // per memorizzare i valori modificati dall'utente
    var (editedEmail, setEditedEmail) = remember { mutableStateOf("") }
    var (editedUsername, setEditedUsername) = remember { mutableStateOf("") }
    var (editedCity, setEditedCity) = remember { mutableStateOf<City?>(null) }
    val auth = Firebase.auth
    val currentUser = auth.currentUser
    val userReference = FirebaseDatabase.getInstance().getReference("utenti").child(currentUser?.uid ?: "")

    LaunchedEffect(Unit) {
        val auth = Firebase.auth
        val currentUser = auth.currentUser
        val userReference =
            FirebaseDatabase.getInstance().getReference("utenti").child(currentUser?.uid ?: "")

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = User.fromMap(snapshot.value as Map<String, Any>)
                if (user != null) {
                    setCurrentEmail(user.email)
                    setCurrentUsername(user.username)
                    val cityString = user.city
                    if (cityString != null) {
                        setCurrentCity(City.fromString(cityString))
                    }
                }
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

            CitySelection( selectedCity = editedCity){
                    city -> setEditedCity(city)
            }

            Spacer(modifier = Modifier.height(16.dp) )


            OutlinedTextField(
                value = editedEmail,
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
                    setEditedEmail(it)
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
                value = editedUsername,
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
                    setEditedUsername(it)
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
                      val cityString = editedCity?.toString() ?: ""

                    val updates = hashMapOf<String, Any>(
                        "email" to editedEmail,
                        "username" to editedUsername,
                        "city" to cityString
                    ).toMap()

                    userReference.updateChildren(updates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                setCurrentEmail(editedEmail)
                                setCurrentUsername(editedUsername)
                                setCurrentCity(editedCity)
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySelection(
    selectedCity: City?,
    onCitySelected: (City) -> Unit
){
    val cities = enumValues<City>().toList()
    var isExpanded by remember { mutableStateOf(false) }
    var chosenCity = selectedCity ?: City.Bari

    var textFiledSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (isExpanded){
        painterResource(id = R.drawable.frecciasopra)
    } else painterResource(id = R.drawable.frecciasotto)

    Column(modifier = Modifier.fillMaxWidth()){
        OutlinedTextField(
            shape = MaterialTheme.shapes.large,
            value = chosenCity.toString(), // Mostra il valore selezionato come stringa
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
                .onGloballyPositioned { coordinates ->
                    textFiledSize = coordinates.size.toSize()
                }

        )
        DropdownMenu(
            offset = DpOffset(0.dp, (4).dp),
            modifier = Modifier
                .width(with(LocalDensity.current) { textFiledSize.width.toDp() })
                .padding(vertical = 2.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .verticalScroll(rememberScrollState())
                .height(400.dp)
                .border(1.dp, MaterialTheme.colorScheme.onPrimary),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            for (index in cities.indices) {
                val city = cities[index]
                DropdownMenuItem(
                    text = {
                        Text(
                            text = city.toString(),
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.outline
                        ) },
                    onClick = {
                        onCitySelected(city)
                        isExpanded = false
                    }
                )
                if (index < cities.size - 1){
                    Divider(thickness = 2.dp, color = Color(0x1A000000))
                }

            }
        }
    }
}