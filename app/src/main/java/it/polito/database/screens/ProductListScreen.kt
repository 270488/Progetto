package it.polito.database.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
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
import it.polito.database.R
import it.polito.database.database
import it.polito.database.ui.theme.Blue20
import it.polito.database.ui.theme.Blue40
import it.polito.database.ui.theme.Red20
import it.polito.database.ui.theme.Red40
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.Yellow40
import it.polito.database.ui.theme.fontFamily

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ProductListScreen(viewModel: AppViewModel, navController: NavController) {
    ProductList(viewModel = viewModel, navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
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

    //Prezzo crescente
    val productsSortedByPrice = products.sortedBy { dataSnapshot ->
        dataSnapshot.child("prezzo").value.toString().toDouble()
    }
    //Prezzo decrescente
    val productsSortedByPriceDescending = products.sortedByDescending { dataSnapshot ->
        dataSnapshot.child("prezzo").value.toString().toDouble()
    }
    //Nome crescente
    val productsSortedByName = products.sortedBy { dataSnapshot ->
        dataSnapshot.child("nome").value.toString()
    }
    //Nome decrescente
    val productsSortedByNameDescending = products.sortedByDescending { dataSnapshot ->
        dataSnapshot.child("nome").value.toString()
    }
    //Ordinata casualmente ratings
    var productsSortedByRating by remember { mutableStateOf(products.shuffled()) }

    //Ordinata casualmente venduti
    var productsSortedBySales by remember { mutableStateOf(products.shuffled()) }

    Column (modifier= Modifier
        .padding(top = 74.dp)
        .padding(bottom = 74.dp)
        .background(Blue40)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
    ) {

        //barra superiore con filtri
        val filtri= listOf<String>("Prezzo ↑", "Prezzo ↓", "Recensioni", "I più venduti")

        var selectedOption by remember {
            mutableStateOf<String>("")
        }

        var expanded by remember { mutableStateOf(false) }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .layoutId("menuOptions")
                    .width(180.dp)
                    .height(52.dp)
                    .border(
                        BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                        RoundedCornerShape(3.dp)
                    )
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(3.dp))
                    .padding(start = 12.dp)
            ){
                ExposedDropdownMenuBox(
                    modifier = Modifier.fillMaxSize(),
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                ) {
                    TextField(
                        value = "Ordina per :",
                        onValueChange = {},
                        readOnly = true,
                        textStyle = TextStyle(
                            fontFamily= fontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            textColor = Color.White),
                        leadingIcon = {
                            Icon(
                                modifier = Modifier.size(25.dp),
                                painter = painterResource(id = R.drawable.sort),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        },
                        modifier = Modifier.menuAnchor()
                    )
                    DropdownMenu(
                        offset = DpOffset(0.dp, (4).dp),
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .width(150.dp)
                            .background(MaterialTheme.colorScheme.primary)
                            .border(
                                BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
                                RoundedCornerShape(3.dp)
                            )
                    ) {
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxSize())
                        {
                            filtri.forEachIndexed() {index, o ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            modifier = Modifier.height(30.dp),
                                            text = o,
                                            color = Color.White,
                                            fontFamily= fontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp)
                                    },
                                    onClick = {
                                        selectedOption = o
                                        expanded =
                                            false
                                    })
                                if (index < filtri.size - 1){
                                    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
                                }
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(15.dp))
            ) {
                if (selectedOption != "")
                    Row {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            text = selectedOption,
                            fontFamily = fontFamily,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier
                                .clickable { selectedOption = "" }
                                .padding(end = 16.dp, top = 8.dp, bottom = 8.dp),
                            text = "X",
                            fontFamily = fontFamily,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

            }
        }

        when (selectedOption){
            "" -> {
                products.forEach{ prod->
                    val nome=prod.child("nome").value.toString()
                    val prezzo=prod.child("prezzo").value.toString()
                    val fileName=nome+".jpg"
                    val url= FindUrl(fileName = fileName)
                    contentCard(nome = nome, prezzo = prezzo, url=url, navController, viewModel )
                }
            }
            "Prezzo ↑" -> {
                productsSortedByPrice.forEach{ prod->
                    val nome=prod.child("nome").value.toString()
                    val prezzo=prod.child("prezzo").value.toString()
                    val fileName=nome+".jpg"
                    val url= FindUrl(fileName = fileName)
                    contentCard(nome = nome, prezzo = prezzo, url=url, navController, viewModel )
                }
            }
            "Prezzo ↓" -> {
                productsSortedByPriceDescending.forEach{ prod->
                    val nome=prod.child("nome").value.toString()
                    val prezzo=prod.child("prezzo").value.toString()
                    val fileName=nome+".jpg"
                    val url= FindUrl(fileName = fileName)
                    contentCard(nome = nome, prezzo = prezzo, url=url, navController, viewModel )
                }
            }
            "Recensioni" -> {
                productsSortedByRating.forEach{ prod->
                    val nome=prod.child("nome").value.toString()
                    val prezzo=prod.child("prezzo").value.toString()
                    val fileName=nome+".jpg"
                    val url= FindUrl(fileName = fileName)
                    contentCard(nome = nome, prezzo = prezzo, url=url, navController, viewModel )
                }
            }
            "I più venduti" -> {
                productsSortedBySales.forEach{ prod->
                    val nome=prod.child("nome").value.toString()
                    val prezzo=prod.child("prezzo").value.toString()
                    val fileName=nome+".jpg"
                    val url= FindUrl(fileName = fileName)
                    contentCard(nome = nome, prezzo = prezzo, url=url, navController, viewModel )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
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
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Card(onClick = {
            viewModel.prodottoSelezionato = nome
            navController.navigate(Screen.Product.route)
        },
            modifier = Modifier
                .height(200.dp)
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
                        colorFilter = ColorFilter.tint(Red20),
                        contentDescription = "Empty Heart",
                        modifier = Modifier
                            .size(40.dp)
                    )
                } else {
                    Image(
                        imageVector = Icons.Outlined.Favorite,
                        colorFilter = ColorFilter.tint(Red40),
                        contentDescription = "Filled Heart",
                        modifier = Modifier.size(40.dp)
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
        .border(width = 1.dp, color = Yellow40, shape = RoundedCornerShape(15)),
        verticalArrangement = Arrangement.Center) {
        Row(modifier= Modifier
            .padding(all = 2.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = filtro, color = Color.White, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium, fontFamily = fontFamily, fontStyle = FontStyle.Italic) )
        }
    }
}