package it.polito.database.screens

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.TextStyle
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
import it.polito.database.LoadImageFromUrl

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ProductListScreen(viewModel: AppViewModel) {
    val sottocategoria="Proteine"
    TopAndBottom(cat = sottocategoria, viewModel) //Va sostituito con il nome della sottocategoria
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAndBottom(cat: String, viewModel: AppViewModel){

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
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = cat,
                        color = Color.Black
                    )
                },
                modifier = Modifier.background(Color.Black),
                navigationIcon = {
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Torna indietro",
                            modifier = Modifier
                                .size(40.dp),
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {  }) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = "Impostazioni",
                            modifier = Modifier
                                .size(40.dp),
                            tint = Color.Black,
                        )
                    }
                }
            )

        },
        bottomBar = { BottomBar(navController = navController) }
    ){}

    ProductList(viewModel=viewModel)


}
@Composable
fun ProductList(modifier: Modifier = Modifier
    .padding(top = 74.dp)
    .padding(bottom = 74.dp), viewModel: AppViewModel) {

    val categoria="Nutrizione Sportiva"
    val sottocategoria="Proteine"

    val products= filtroCategorieProdotti(categoria = categoria, sottocategoria = sottocategoria, viewModel = viewModel)

    Column (modifier= Modifier
        .padding(top = 74.dp)
        .padding(bottom = 74.dp)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
    ) {
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
    return viewModel.products.observeAsState(emptyList()).value
        .filter { it.child("categoria").value.toString() == categoria }
        .filter { it.child("sottocategoria").value.toString()==sottocategoria}
}

@Composable
fun contentCard(nome: String, prezzo: String, url: String){

    Column (modifier=Modifier.padding(10.dp).border(width = 1.dp, shape = RectangleShape, color = Color.Black)) {
        Row(modifier= Modifier.fillMaxSize()){
            LoadImageFromUrl(imageUrl = url)

        }
        Row (modifier=Modifier .fillMaxWidth()
            .padding(32.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = nome, modifier = Modifier.weight(1f), style = TextStyle(fontSize = 20.sp))
            Text(text = prezzo, modifier = Modifier.weight(1f),style = TextStyle(fontSize = 20.sp))
        }
    }


}
