package it.polito.database.screens

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults


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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.database
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun CategoryScreen() {
Greetings()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable

// list of items
private fun Greetings(
    modifier: Modifier = Modifier
        .padding(top = 74.dp)
        .padding(bottom = 74.dp) //per non far coprire l'inizio da top e bottom bar
) {
    var listaCategorie by remember { mutableStateOf<List<String>>(emptyList()) }

    database.child("categorie").get().addOnSuccessListener { dataSnapshot ->
        if (dataSnapshot.exists()) {
            val categorie = mutableListOf<String>()
            for (childSnapshot in dataSnapshot.children) {
                val categoria = childSnapshot.key.toString()
                categoria?.let {
                    categorie.add(categoria)
                }
            }
            listaCategorie = categorie
        }
    }.addOnFailureListener { e ->
        // Gestisci eventuali errori durante il recupero dei dati dal database
        println("Errore durante il recupero delle categorie: ${e.message}")
    }

    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        /*val names = listOf(
            "Alimentazione salutare",
            "Nutrizione sportiva",
            "Salute e dimagrimento",
            "Cosmetica e cura personale",
            "Abbigliamento da donna",
            "Abbigliamento da uomo"
        )*/
        items(items = listaCategorie) { name ->
            Greeting(name = name)
        }

    }
}

// ui for card
@Composable
private fun Greeting(name: String) {
    Card(colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primary
    ), modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp)) {
        CardContent(name = name)
    }
}

// ui for card content
@Composable
private fun CardContent(name: String) {

    var listaSottoCategorie by remember { mutableStateOf<Map<String, List<String>>>(emptyMap()) }

    database.child("categorie").get().addOnSuccessListener { dataSnapshot ->
        if (dataSnapshot.exists()) {
            val sottoCategorie = mutableMapOf<String, List<String>>()


            for (childSnapshot in dataSnapshot.children) {
                val list= mutableListOf<String>() //List value
                val categoria = childSnapshot.key.toString() //key

                for(item in childSnapshot.children){

                    list.add(item.value.toString())
                }

                sottoCategorie[categoria]=list

            }
            listaSottoCategorie = sottoCategorie
            println("Lista sotto categorie: ${listaSottoCategorie}")
        }
    }.addOnFailureListener { e ->
        // Gestisci eventuali errori durante il recupero dei dati dal database
        println("Errore durante il recupero delle categorie: ${e.message}")
    }
    var expanded by remember { mutableStateOf(false) }
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
                text = name,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium)
            )

            val sottoCategorie=listaSottoCategorie.get(name)
            if(expanded){
                sottoCategorie?.forEach{sottocategoria->
                    Column{
                        Text(text = sottocategoria )
                    }
                }
            }




            /*if(name == "Nutrizione sportiva"){
            if (expanded) {
                Column{
                    Text(text = "Proteine" )
                }
                Column {
                    Text(text = "Aumento della massa" )
                }
                Column {
                    Text(text = "Energia e resistenza")
                }
                Column {
                    Text(text = "Bruciagrassi e definizione")
                }
                Column {
                    Text(text = "Salute dell'atleta")
                }
            }
        }*/
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