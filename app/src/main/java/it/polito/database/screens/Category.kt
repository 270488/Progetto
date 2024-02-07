package it.polito.database.screens

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.database
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.polito.database.R
import it.polito.database.ui.theme.Blue40
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.Yellow40
import it.polito.database.ui.theme.fontFamily


@Composable
fun CategoryScreen(viewModel: AppViewModel, navController: NavController) {
    Greetings(viewModel, navController)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable

// list of items
private fun Greetings(viewModel: AppViewModel,navController: NavController,
    modifier: Modifier = Modifier
        .padding(top = 76.dp)
        .padding(bottom = 74.dp) //per non far coprire l'inizio da top e bottom baR
        .background(Blue40)
        .fillMaxHeight()
) {
    var listaCategorie by remember { mutableStateOf<List<String>>(emptyList()) }

    database.child("categorie").get().addOnSuccessListener { dataSnapshot -> //prende tutti i figli del nodo categorie
        if (dataSnapshot.exists()) {
            val categorie = mutableListOf<String>()
            for (childSnapshot in dataSnapshot.children) { //prende ogni figlio del nodo categorie
                val categoria = childSnapshot.key.toString() //prende la chiave, quindi abbigliamento donna ecc...
                categoria?.let {
                    categorie.add(categoria) //aggiunge la chiave alla lista categoria, locale del for
                }
            }
            listaCategorie = categorie //copia la lista locale del for nella listaCategorie fuori dalla funzione
        }
    }.addOnFailureListener { e ->
        // Gestisci eventuali errori durante il recupero dei dati dal database
        println("Errore durante il recupero delle categorie: ${e.message}")
    }
    Divider(thickness = 4.dp, color = Color.Red)
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = listaCategorie) { name ->
            Greeting(name = name, viewModel, navController)
        }
    }
}

// ui for card
@Composable
private fun Greeting(name: String, viewModel: AppViewModel, navController: NavController) {
    Card(colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primary
    ), modifier = Modifier,
        //.padding(vertical = 4.dp, horizontal = 4.dp),
        shape = RectangleShape
    ) {
        CardContent(name = name, viewModel, navController)
    }
    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
}

// ui for card content
@Composable
private fun CardContent(name: String, viewModel: AppViewModel,navController: NavController) {

    var listaSottoCategorie by remember { mutableStateOf<Map<String, List<String>>>(emptyMap()) }

    database.child("categorie").get().addOnSuccessListener { dataSnapshot ->
        if (dataSnapshot.exists()) {
            val sottoCategorie = mutableMapOf<String, List<String>>()

            for (childSnapshot in dataSnapshot.children) { //prende ogni figlio di categorie, quindi abbigliamento donna ecc...
                val list= mutableListOf<String>() //List value
                val categoria = childSnapshot.key.toString() //chiave=abbigliamento donna

                for(item in childSnapshot.children){ //prende ogni figlio di abbigliamento donna
                    list.add(item.value.toString()) //aggiunge il valore del figlio alla list
                }

                sottoCategorie[categoria]=list //riempie mappa locale della funzione

            }
            listaSottoCategorie = sottoCategorie //copia mappa locale nella mappa esterna alla funzione

        }
    }.addOnFailureListener { e ->
        // Gestisci eventuali errori durante il recupero dei dati dal database
        println("Errore durante il recupero delle categorie: ${e.message}")
    }
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .padding(8.dp)
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium, fontSize = 20.sp)
            )

            val sottoCategorie=listaSottoCategorie.get(name) // prende la lista associata al nome della categoria
            if(expanded){
                viewModel.cat=name
                Box(modifier = Modifier.height(14.dp))
                sottoCategorie?.forEach{sottocategoria->
                    sottoCategoriaCard(sottocategoria = sottocategoria, viewModel=viewModel, categoria = name,navController)
                }
            }
        }
        IconButton(onClick = { expanded = !expanded }) {
            if(expanded) {
                Icon(
                    painter =  painterResource(id = R.drawable.frecciadx),
                    tint = MaterialTheme.colorScheme.tertiary,
                    contentDescription = "show less",
                    modifier = Modifier
                        .size(32.dp)
                        .rotate(90f)
            )}
            else {
                Icon(
                    painter =  painterResource(id = R.drawable.frecciadx),
                    tint = MaterialTheme.colorScheme.tertiary,
                    contentDescription = "show more",
                    modifier = Modifier
                        .size(32.dp)
                )}
        }
    }
}

@Composable
fun sottoCategoriaCard(sottocategoria: String, viewModel: AppViewModel, categoria: String,navController: NavController){
    var expanded by remember { mutableStateOf(false) }
    Column (modifier= Modifier
        .padding(2.dp)
        .fillMaxWidth(),
        //.border(width = 1.dp, color = Color.Black, shape = RectangleShape),
        verticalArrangement = Arrangement.Center) {
        Row(modifier= Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .padding(end = 2.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
            Text(text = sottocategoria, style = MaterialTheme.typography.headlineSmall.copy(fontFamily = fontFamily, fontWeight = FontWeight.Medium, fontSize = 15.sp, fontStyle = FontStyle.Italic) )
            IconButton(onClick = { expanded = !expanded;
                if (expanded) {
                    viewModel.sottocat=sottocategoria


                    navController.navigate(Screen.ProductList.route)
            } }) {

                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowRight,
                    contentDescription =
                    if (expanded) {
                        "show less"
                    } else {
                        "show more"
                    }
                )
            }
        }
        //Divider(thickness = 1.dp, color = Color.Black)
    }
}