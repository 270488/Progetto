package it.polito.database.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
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
import androidx.constraintlayout.compose.HorizontalAlign
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.City
import it.polito.database.CitySelection
import it.polito.database.R
import it.polito.database.User
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.fontFamily

@Composable
fun AiutoEContatti(viewModel: AppViewModel, navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 120.dp)
            .verticalScroll(rememberScrollState())
    ){
        Info()
        Request(navController)
    }
}

@Composable
fun Info(){
    Row {
        Modifier.align(CenterVertically)
        HorizontalAlign.Center
        Icon(
            painter = painterResource(R.drawable.cutomservice),
            contentDescription = "call center",
            Modifier
                .padding(3.dp)
                .size(38.dp)
        )
        Text(
             text = "Assistenza clienti" ,
             fontFamily = fontFamily,
             fontWeight = FontWeight.Bold,
             fontSize = 26.sp,
            )
    }
     Spacer(modifier = Modifier.height(16.dp) )
     Row {
         Icon(
             imageVector = Icons.Default.Call,
             contentDescription = "Call",
             Modifier
                 .padding(3.dp)
                 .size(38.dp)
         )
         Text(
             text = "+39 314 159 2653",
             fontFamily = fontFamily,
             fontWeight = FontWeight.Bold,
             fontSize = 26.sp,
         )
     }
     Text(
         text = "Dal lunedì al venerdì dalle ore 13 alle ore 17",
         fontFamily = fontFamily,
         fontWeight = FontWeight.Normal,
         fontSize = 22.sp,
         modifier= Modifier.padding(40.dp,20.dp,40.dp,20.dp),
         textAlign = TextAlign.Center
     )
     Divider(color = Color.Black, thickness = 2.dp, modifier = Modifier.padding(20.dp))

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Request(navController: NavController){
    var (currentEmail, setCurrentEmail) = remember { mutableStateOf("") }
    var (currentName, setCurrentName) = remember { mutableStateOf("") }
    var (currentSurname, setCurrentSurname) = remember { mutableStateOf("") }
    var (description, setDescription) = remember { mutableStateOf("") }
    val (currentCity, setCurrentCity) = remember { mutableStateOf<City?>(null) }

    val auth = Firebase.auth
    val currentUser = auth.currentUser
    val userReference = FirebaseDatabase.getInstance().getReference("utenti").child(currentUser?.uid ?: "")

    LaunchedEffect(Unit) {
        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = User.fromMap(snapshot.value as Map<String, Any>)
                setCurrentEmail(user.email)
                setCurrentName(user.nome)
                setCurrentSurname(user.cognome)
                val cityString = user.city
                setCurrentCity(City.fromString(cityString))
            }

            override fun onCancelled(error: DatabaseError) {
                // Gestisci eventuali errori di recupero dati
            }
        })
    }
    Row{
        HorizontalAlign.Center
        Modifier
            .align(CenterVertically)
            .background(MaterialTheme.colorScheme.primary)
        Text(
            text = "Invia la tua richiesta" ,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
        )
    }
    Spacer(modifier = Modifier.height(16.dp) )
    Row {
        HorizontalAlign.Center
        Modifier
            .align(CenterVertically)
        CitySelection(
            selectedCity = currentCity,
            onCitySelected = { city -> setCurrentCity(city) })
    }
    Spacer(modifier = Modifier.height(16.dp) )
    Row {
        HorizontalAlign.Center
        Modifier
            .align(CenterVertically)

        OutlinedTextField(
            value = currentEmail,
            placeholder = {
                Text(
                    text = "Email",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic,
                    fontSize = 18.sp
                )
            },
            onValueChange = {
                setCurrentEmail(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Color.Black
            )
        )
    }
    Spacer(modifier = Modifier.height(16.dp) )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = currentName,
            placeholder = {
                Text(
                    text = "Nome*",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic,
                    fontSize = 18.sp
                )
            },
            onValueChange = {
                setCurrentName(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            modifier = Modifier
                .weight(1F)
                .absolutePadding(left = 55.dp), // Aggiungi i margini sinistro e destro
            textStyle = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.width(8.dp))

        OutlinedTextField(
            value = currentSurname,
            placeholder = {
                Text(
                    text = "Cognome*",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic,
                    fontSize = 18.sp
                )
            },
            onValueChange = {
                setCurrentSurname(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            modifier = Modifier
                .weight(1F)
                .absolutePadding(right = 55.dp), // Aggiungi i margini sinistro e destro
            textStyle = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Color.Black
            )
        )
    }
    Spacer(modifier = Modifier.height(16.dp))

    val argomenti = listOf(
        "Questo locker è di plastica",
        "Mi sono arrivati gli portelli in faccia",
        "Belli i lego e le molle colorate",
        "Oh ma il reso è finto!",
        "Oh ma questa sezione è finta!",
        "Synca Gradle e riprova, grazie",
        "I motori sono impazziti",
    )
    var isExpanded by remember { mutableStateOf(false) }
    var (arg, setArg) =  remember { mutableStateOf("") }
    var textFiledSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (isExpanded){
        painterResource(id = R.drawable.frecciasopra)
    } else painterResource(id = R.drawable.frecciasotto)

    Column{
        OutlinedTextField(

            value = arg,
            onValueChange = {},
            readOnly = false,
            trailingIcon = {
                Icon(icon , "", tint = Color.Black,
                    modifier = Modifier
                        .clickable { isExpanded = !isExpanded }
                        .size(16.dp))
            },
            textStyle = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 55.dp)
                .onGloballyPositioned { coordinates ->
                    textFiledSize = coordinates.size.toSize()
                }

        )
        DropdownMenu(
            offset = DpOffset(50.dp, (-10).dp),
            modifier = Modifier
                .width(with(LocalDensity.current) { textFiledSize.width.toDp() })
                .background(MaterialTheme.colorScheme.primaryContainer)
                .verticalScroll(rememberScrollState())
                .height(200.dp)
                .border(1.dp, MaterialTheme.colorScheme.onPrimary),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            for (arg in argomenti) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = arg,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.outline
                        ) },
                    onClick = {
                        setArg(arg)
                        isExpanded = false
                    }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = description,
            modifier = Modifier.height(80.dp),
            placeholder = { Text(
                text = "Desrizione*",
                fontFamily = fontFamily,
                fontWeight = FontWeight.Light,
                color = Color.Black,
                fontSize = 18.sp)
            },
            onValueChange = {
                setDescription(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Color.Black)
        )
    }

    Spacer(modifier = Modifier.height(16.dp))
    Button(
        modifier = Modifier
            .border(
                width = 1.dp, color = Color.Black,
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        onClick = {
            navController.navigate(Screen.Home.route)
                }

    )
    {
        Text(
            text = "Invia",
            fontSize = 22.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.offset(x = 0.dp, y = (-2).dp)
        )
    }

}
