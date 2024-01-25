package it.polito.database.screens

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.database
import it.polito.database.ui.theme.BottomBar
import it.polito.database.AppViewModel
import it.polito.database.FindUrl
import it.polito.database.GlobalVariables
import it.polito.database.LoadImageFromUrl
import okhttp3.internal.notify

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ProductListScreen(viewModel: AppViewModel) {
    ProductList(viewModel = viewModel)
}

@Composable
fun ProductList(modifier: Modifier = Modifier
    .padding(top = 74.dp)
    .padding(bottom = 74.dp), viewModel: AppViewModel) {

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

        Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){
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
            contentCard(nome = nome, prezzo = prezzo, url=url)
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

@Composable
fun contentCard(nome: String, prezzo: String, url: String){

    Column (modifier= Modifier
        .padding(10.dp)
        .border(width = 1.dp, shape = RectangleShape, color = Color.Black)) {
        Row(modifier= Modifier.fillMaxSize()){
            LoadImageFromUrl(imageUrl = url)

        }
        Row (modifier= Modifier
            .fillMaxWidth()
            .padding(32.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = nome, modifier = Modifier.weight(1f), style = TextStyle(fontSize = 20.sp))
            Text(text = prezzo+"€", modifier = Modifier.weight(1f),style = TextStyle(fontSize = 20.sp))
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