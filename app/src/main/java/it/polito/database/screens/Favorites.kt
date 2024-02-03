 package it.polito.database.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.FindUrl
import it.polito.database.LoadImageFromUrl
import it.polito.database.database
import it.polito.database.ui.theme.Screen


@Composable
fun FavoritesScreen(viewModel: AppViewModel, navController: NavController){
    var id=viewModel.uid

    var listaPreferiti by remember { mutableStateOf<List<String>>(emptyList()) }

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
    println(listaPreferiti)


    Column (modifier= Modifier
        .padding(top = 74.dp)
        .padding(bottom = 74.dp)
        .verticalScroll(rememberScrollState())){
        listaPreferiti.forEach{preferito->
            FavoriteCard(preferito, viewModel, id, navController)
        }
        Box(modifier = Modifier
            .padding(30.dp)
            .background(Color.Yellow)
            .clickable { navController.navigate(Screen.Home.route) }){//Va cambiato da HOME a I MIEI ORDINI
            Text(text = "I miei ordini")
        }
    }
    
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteCard(preferito: String, viewModel: AppViewModel, id:String, navController: NavController){
    val product= viewModel.products.observeAsState(emptyList()).value
        .filter { it.child("nome").value.toString() == preferito }

    println("prodotto: $product")

    var nome=""
    var prezzo=""
    var url=""
    product.forEach { p ->
        nome=p.child("nome").value.toString()
        prezzo=p.child("prezzo").value.toString()
        url= FindUrl(fileName = nome+".jpg")

    }

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


            var clicked by remember {
                mutableStateOf(true)
            }

            Row() {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(5.dp)
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center
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
                        eliminaPreferito(prod = nome, id = id)
                    },
                    containerColor = Color.Transparent,
                    modifier = Modifier
                        .layoutId("btnHeart")
                        .padding(top = 32.dp, end = 16.dp),
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    )
                ) {

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


fun eliminaPreferito(prod: String, id: String){
    var preferiti= database.child("utenti").child(id).child("preferiti")
    preferiti.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { //fa una foto al db in quel momento e la mette in dataSnapshot
            for (childSnapshot in dataSnapshot.children) {
                if(childSnapshot.value.toString()==prod){
                    childSnapshot.ref.removeValue()

                }
            }
        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })


}


fun aggiungiPreferito(prod: String, id: String){

    //Verifica esistenza nodo "Preferiti"
    var preferiti= database.child("utenti").child(id).child("preferiti")
    preferiti.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { //fa una foto al db in quel momento e la mette in dataSnapshot
            var lastKey = 0
            println("UID: "+id)
            if(dataSnapshot.exists()){
                //Aggiunge preferito
                for (childSnapshot in dataSnapshot.children) {
                    val i = childSnapshot.key?.toInt() ?: 0
                    lastKey= i+1
                    println("LastKey: "+lastKey)
                }

                database.child("utenti").child(id).child("preferiti").child(lastKey.toString()).setValue(prod)
            }
            else{
                val preferitiMap = mapOf(lastKey.toString() to prod)
                database.child("utenti").child(id).child("preferiti").setValue(preferitiMap)
            }
        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })



}

