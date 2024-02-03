package it.polito.database.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.FindUrl
import it.polito.database.LoadImageFromUrl
import it.polito.database.database
import it.polito.database.ui.theme.Screen
import java.lang.Double.sum


@Composable
    fun CartScreen(viewModel: AppViewModel, navController: NavController) {
    Cart(viewModel, navController)
    }

@Composable
fun Cart(viewModel: AppViewModel, navController: NavController, modifier: Modifier = Modifier
    .padding(top = 74.dp)
    .padding(bottom = 74.dp)){

    var listaCarrello by remember { mutableStateOf<List<String>>(emptyList()) }
    var id=viewModel.uid
    var items= database.child("utenti").child(id).child("carrello")
    items.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            var lista= mutableListOf<String>()
            for (childSnapshot in dataSnapshot.children) {
                lista.add(childSnapshot.value.toString())
            }
            listaCarrello=lista
        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })

    var totale = 0.00
    Column(modifier = modifier
        .padding(vertical = 4.dp)
        .verticalScroll(rememberScrollState())){
        listaCarrello.forEach{item ->
            val product= viewModel.products.observeAsState(emptyList()).value
                .filter { it.child("nome").value.toString() == item }
            product.forEach { p ->
                totale= sum(p.child("prezzo").value as Double, totale)
            }
            ItemCard(viewModel, item, id, navController)
        }
    Box(modifier = modifier.height(200.dp)){
        Text(text = "Totale: " + totale.toString() + "€")
    }

        Button(
            onClick = {
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .layoutId("btnCheckOut")
                .padding(top = 16.dp, start = 90.dp, bottom = 16.dp)
                .width(250.dp),
            colors = ButtonDefaults.buttonColors(Color.Yellow),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                disabledElevation = 0.dp
            )
        ){
            Text(
                text = "Procedi all'acquisto",
                fontSize = 23.sp,
                color = Color.Black
            )
        }

    }

}

fun aggiungiAlCarrello(item: String, id: String) {
    var items= database.child("utenti").child(id).child("carrello")
    items.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            var lastKey = 0
            if(dataSnapshot.exists()){
                for (childSnapshot in dataSnapshot.children) {
                    val i = childSnapshot.key?.toInt() ?: 0
                    lastKey= i+1
                }

                database.child("utenti").child(id).child("carrello").child(lastKey.toString()).setValue(item)
            }
            else{
                val itemsMap = mapOf(lastKey.toString() to item)
                database.child("utenti").child(id).child("carrello").setValue(itemsMap)
            }
        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })}

fun eliminaDalCarrello(item: String, id: String){
    var items= database.child("utenti").child(id).child("carrello")
    items.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { //fa una foto al db in quel momento e la mette in dataSnapshot
            for (childSnapshot in dataSnapshot.children) {
                if(childSnapshot.value.toString()==item){
                    childSnapshot.ref.removeValue()

                }
            }
        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })
}

@Composable
fun CartItem(nome: String, prezzo: Int, quantita: Int){

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemCard(
    viewModel: AppViewModel,
    item: String,
    id: String,
    navController: NavController
){
    val product= viewModel.products.observeAsState(emptyList()).value
        .filter { it.child("nome").value.toString() == item }

    var nome=""
    var prezzo=""
    var url=""
    var totale = 0.00
    product.forEach { p ->
        nome=p.child("nome").value.toString()
        prezzo=p.child("prezzo").value.toString()
        url= FindUrl(fileName = nome+".jpg")
        totale= sum(p.child("prezzo").value as Double, totale)
    }
    Row(modifier= Modifier
        .padding(10.dp)
        .background(Color.Gray)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Card(
            onClick = {
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
                        .weight(4f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Card(modifier = Modifier.fillMaxSize()){
                        LoadImageFromUrl(imageUrl = url)
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = nome)
                    Text(text = prezzo + "€")
                    Text(text = "quantità")
                    Text(text = "opzione")
                    Column(  modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight(),
                        verticalArrangement = Arrangement.Bottom) {
                        TextButton(onClick = { eliminaDalCarrello(item = nome, id = id) }) {
                            Text(text = "Elimina", color = MaterialTheme.colorScheme.onPrimary)
                        }
                        TextButton(onClick = { aggiungiPreferito(prod = nome, id = id)
                            eliminaDalCarrello(item = nome, id = id)
                        }) {
                            Text(text = "Salva nei preferiti", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }

            }
        }
    }
}