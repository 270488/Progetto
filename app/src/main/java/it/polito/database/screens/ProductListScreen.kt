package it.polito.database.screens

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.FindUrl
import it.polito.database.GlobalVariables
import it.polito.database.LoadImageFromUrl
import it.polito.database.database
import it.polito.database.ui.theme.Screen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ProductListScreen(viewModel: AppViewModel, navController: NavController) {
    ProductList(viewModel = viewModel, navController = navController)
}

@Composable
fun ProductList(modifier: Modifier = Modifier
    .padding(top = 74.dp)
    .padding(bottom = 74.dp), viewModel: AppViewModel, navController: NavController

) {

    val categoria=GlobalVariables.cat
    val sottocategoria=GlobalVariables.sottocat

    val children= database.child("prodotti") //prende dal db il nodo prodotti e aggiunge un listener
    children.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { //fa una foto al db in quel momento e la mette in dataSnapshot
            // Itera sui figli del nodo

            for (childSnapshot in dataSnapshot.children) { //prende i figli di prodotti, quindi 0, 1...
                // Aggiungi il prodotto alla lista
                viewModel.addProduct(childSnapshot)

            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Gestisci gli errori qui
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })



    val products= filtroCategorieProdotti(categoria = categoria, sottocategoria = sottocategoria, viewModel = viewModel)
    println("Products $products")
    Column (modifier= Modifier
        .padding(top = 74.dp)
        .padding(bottom = 74.dp)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
    ) {

        //barra superiore con filtri
        val filtri= listOf<String>("Prezzo", "Rating", "Taglia")

        var expanded by remember { mutableStateOf(false) }

        Row(modifier= Modifier
            .fillMaxWidth()
            .padding(5.dp)){
            Icon(
                imageVector = Icons.Filled.List, //andrebbe messa icona del filtro
                contentDescription = ""

            )
            Text(text = "Filtri",
                style = MaterialTheme.typography.headlineSmall
                    .copy(fontWeight = FontWeight.Medium, color = Color.Yellow))
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .animateContentSize()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Filtri",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium)
                    )
                    if(expanded){
                        filtri.forEach{f-> elementoFiltro(filtro = f)
                        }

                    }
                }
                IconButton(onClick = { expanded = !expanded }) {
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
        }
        products.forEach{ prod->
            val nome=prod.child("nome").value.toString()
            val prezzo=prod.child("prezzo").value.toString()
            val fileName=nome+".jpg"
            val url= FindUrl(fileName = fileName)
            contentCard(nome = nome, prezzo = prezzo, url=url, navController, viewModel )
        }
    }
}

@Composable
private fun filtroCategorieProdotti(categoria: String,sottocategoria: String, viewModel: AppViewModel): List<DataSnapshot>{
    println("Categoria $categoria , sottocateogria $sottocategoria")

    return viewModel.products.observeAsState(emptyList()).value
        .filter { it.child("categoria").value.toString() == categoria }
        .filter { it.child("sottocategoria").value.toString()==sottocategoria}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun contentCard(nome: String, prezzo: String, url: String, navController: NavController, viewModel: AppViewModel){
    var id="ZNa99YXVgfVWD1ioAeY9mLtQ8Dh2"
    var listaPreferiti by remember { mutableStateOf<List<String>>(emptyList()) }
    var filledHeart by remember{ mutableStateOf(false) }
    var preferiti= database.child("utenti").child(id).child("preferiti")
    preferiti.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { //fa una foto al db in quel momento e la mette in dataSnapshot
            // Itera sui figli del nodo
            var list= mutableListOf<String>()


            for (childSnapshot in dataSnapshot.children) { //prende i figli di prodotti, quindi 0, 1...
                // Aggiungi il prodotto alla lista
                list.add(childSnapshot.value.toString())
            }
            listaPreferiti=list
        }
        override fun onCancelled(databaseError: DatabaseError) {
            // Gestisci gli errori qui
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })


    if(listaPreferiti.contains(nome)){
        filledHeart=true
    }
    else
        filledHeart=false

    Row(modifier= Modifier
        .padding(10.dp)
        .background(Color.Gray)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Card(onClick = {
            viewModel.prodottoSelezionato = nome
            navController.navigate(Screen.Product.route)
        },
            modifier = Modifier.height(200.dp),
        ) {

            Row() {
                Card(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(5.dp)
                        .weight(1f),
                ) {

                    LoadImageFromUrl(imageUrl = url)

                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(), verticalArrangement = Arrangement.Center
                ) {
                    Text(text = nome)
                    Text(text = prezzo + "€")
                }
                FloatingActionButton(
                    onClick = {
                        filledHeart = !filledHeart
                        if(filledHeart){
                            aggiungiPreferito(prod = nome, id = id)
                        }
                        else
                            eliminaPreferito(prod = nome, id = id)
                    },
                    containerColor = Color.Transparent,
                    modifier = Modifier
                        .layoutId("btnHeart")
                        .padding(top = 32.dp, start = 320.dp, bottom = 8.dp, end = 16.dp),
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    )
                ) {
                    if(filledHeart==false) {
                        Image(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = "Empty Heart",
                            modifier = Modifier.size(50.dp)
                        )
                    }else{
                        Image(
                            imageVector = Icons.Outlined.Favorite,
                            contentDescription = "Filled Heart",
                            modifier = Modifier.size(50.dp)
                        )
                    }

                }



            }
        }
    }


}

@Composable
fun elementoFiltro(filtro: String){
    Column (modifier= Modifier
        .padding(2.dp)
        .fillMaxWidth()
        .border(width = 1.dp, color = Color.Black, shape = RectangleShape),
        verticalArrangement = Arrangement.Center) {
        Row(modifier= Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .padding(end = 2.dp), horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = filtro, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium) )
        }
    }
}