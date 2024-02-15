package it.polito.database.screens

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.FindUrl

import it.polito.database.LoadImageFromUrl
import it.polito.database.database
import it.polito.database.ui.theme.Blue20
import it.polito.database.ui.theme.Blue40
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.fontFamily

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

    val categoria=viewModel.cat
    val sottocategoria=viewModel.sottocat

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
        .background(Blue40)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
    ) {

        //barra superiore con filtri
        val filtri= listOf<String>("Prezzo", "Rating", "Taglia")

        var expanded by remember { mutableStateOf(false) }

        Row(modifier= Modifier
            .fillMaxWidth()
            .background(Blue40)
            .padding(5.dp)){
            Icon(
                imageVector = Icons.Filled.List, //andrebbe messa icona del filtro
                contentDescription = ""

            )
            Text(text = "Filtri",
                style = MaterialTheme.typography.headlineSmall
                    .copy(fontWeight = FontWeight.Medium, color = Color.Yellow, fontFamily = fontFamily))
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .background(Blue40)
                    .animateContentSize()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Blue40)
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Filtri",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium, fontFamily = fontFamily)
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
                        },
                        tint = Color.White
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
    var id=viewModel.uid
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
        .background(Blue40)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Card(onClick = {
            viewModel.prodottoSelezionato = nome
            navController.navigate(Screen.Product.route)
        },
            modifier = Modifier
                .size(width = Dp.Unspecified, height = 150.dp)
                .fillMaxWidth()
                .background(Blue40),
        )  { Box (modifier = Modifier.fillMaxSize())
                {
                    AsyncImage(
                        model = url,
                        contentDescription = "Product Description",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            compositingStrategy = CompositingStrategy.ModulateAlpha
                            alpha = 0.75f
                        }
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.Black
                                )
                            )
                        )
                    )
                    Text(
                        text = nome,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontFamily = fontFamily,
                            fontSize = 24.sp,
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset.Zero,
                                blurRadius = 5f
                            )
                        ) ,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = 3.dp, start = 6.dp)
                    )
                    Text(
                        text = prezzo + "€",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontFamily = fontFamily,
                            fontSize = 32.sp,
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset.Zero,
                                blurRadius = 5f
                            )
                        ),
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 3.dp, end = 6.dp)
                    )
                    FloatingActionButton(
                        onClick = {
                            filledHeart = !filledHeart
                            if (filledHeart) {
                                aggiungiPreferito(prod = nome, id = id)
                            } else
                                eliminaPreferito(prod = nome, id = id)
                        },
                        containerColor = Color.Transparent,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .layoutId("btnHeart"),
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp
                        )
                    ) {
                        if (filledHeart == false) {
                            Image(
                                imageVector = Icons.Outlined.FavoriteBorder,
                                contentDescription = "Empty Heart",
                                modifier = Modifier
                                    .size(50.dp, 50.dp)
                            )
                        } else {
                            Image(
                                imageVector = Icons.Outlined.Favorite,
                                contentDescription = "Filled Heart",
                                modifier = Modifier.size(50.dp, 50.dp)
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
        .border(width = 1.dp, color = Color.White, shape = RectangleShape),
        verticalArrangement = Arrangement.Center) {
        Row(modifier= Modifier
            .padding(all = 2.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = filtro, color = Color.White, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium, fontFamily = fontFamily) )
        }
    }
}