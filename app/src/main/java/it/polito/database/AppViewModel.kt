package it.polito.database

import android.util.DisplayMetrics
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlin.math.round


class AppViewModel: ViewModel() {

    var products=MutableLiveData<List<DataSnapshot>>(emptyList())
    fun writeNewUser(userId: String, name: String, email: String) {
        val user = User(userId, name, email)

        database.child("users").child(userId).setValue(user)
        database.child("users").child("anna00").child("name").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

    }
    fun addProduct(prod: DataSnapshot){
        val productName = prod.child("nome").value.toString()

        // Verifica se esiste già un prodotto con lo stesso nome
        val productExists = products.value?.any { it.child("nome").value.toString() == productName } ?: false

        if (!productExists) {
            // Aggiungi il prodotto solo se non esiste già
            val currentList = products.value.orEmpty()
            val updatedList = currentList + prod
            products.value = updatedList
        }
    }
}

@Composable
fun HomePage(viewModel: AppViewModel){
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)

        )
    {
       // IntestazioneHome()
        Spacer(modifier = Modifier.height(5.dp))
        ScrollableColumn(viewModel)
        Spacer(modifier = Modifier.height(5.dp))
       // Footer()
    }

}
/*
@Composable
fun IntestazioneHome(){
    Row(
        modifier = Modifier
            .fillMaxHeight(0.1f)
            .fillMaxWidth()
            .background(Color.Gray),
        verticalAlignment = Alignment.CenterVertically,
    ){
        Row(modifier = Modifier
            .padding(5.dp)
            .weight(1f)){
            Text(text = "Campanella", color = Color.White)
        }
        Row(modifier = Modifier
            .padding(5.dp)
            .weight(1f)){
            Text(text = "MCFIT", color = Color.White)
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "Logo", color = Color.White)
        }
        
        Row(modifier = Modifier
            .padding(5.dp)
            .weight(1f)){
            Text(text = "Impostazioni", color = Color.White)
        }
    }
}
*/
@Composable
fun ScrollableColumn(viewModel: AppViewModel) {

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

    val prod by viewModel.products.observeAsState()


    Column(
        modifier = Modifier
            .fillMaxHeight(0.9f)
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
            ,
    ) {
        Spacer(modifier=Modifier.weight(0.25f))
        //prima riga "DA NON PERDERE"
        Column(modifier = Modifier){

            Row(modifier=Modifier.fillMaxWidth()){
                Text(text = "Da non perdere", color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start)
                Text(text = "Cerca", color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End)
            }
        //Contiene i bottoni con i prodotti
        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            val nonperdere= listaFiltrata(categoria = "non perdere", viewModel = viewModel)

            nonperdere?.forEach { p ->
                val fileName=p.child("nome").value.toString()+".jpg"
                val url= FindUrl(fileName = fileName)
                IconButton(onClick = { /*TODO*/ },
                    modifier=Modifier.size(200.dp, 150.dp)
                    .background(Color.Gray)) {
                    LoadImageFromUrl(imageUrl = url)
                }

            }

        }
        Text(text = "________________________________________________________________", color = Color.Yellow)
        }
        Spacer(modifier=Modifier.weight(0.25f))
        //Seconda riga OFFERTE PER TE
        Column(modifier = Modifier){

            Row(modifier=Modifier.fillMaxWidth()){
                Text(text = "Offerte per te", color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start)

            }
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())){

                val offerte= listaFiltrata(categoria = "offerte", viewModel = viewModel)

                offerte?.forEach { p ->
                    IconButton(onClick = { /*TODO*/ }, modifier=Modifier.size(200.dp, 150.dp)) {
                        val fileName=p.child("nome").value.toString()+".jpg"
                        val url= FindUrl(fileName = fileName)
                        LoadImageFromUrl(imageUrl = url)

                    }
                }

            }
            Text(text = "________________________________________________________________", color = Color.Yellow)
        }
        Spacer(modifier=Modifier.weight(0.25f))

        //Terza riga ACQUISTA DI NUOVO
        Column(modifier = Modifier){

            Row(modifier=Modifier.fillMaxWidth()){
                Text(text = "Acquista di nuovo", color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start)

            }
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())){
                val acquista= listaFiltrata(categoria = "acquista", viewModel = viewModel)

                acquista?.forEach { p ->
                    IconButton(onClick = { /*TODO*/ }, modifier=Modifier.size(200.dp, 150.dp)) {
                        val fileName=p.child("nome").value.toString()+".jpg"
                        val url= FindUrl(fileName = fileName)
                        LoadImageFromUrl(imageUrl = url)

                    }
                }
            }
            Text(text = "________________________________________________________________", color = Color.Yellow)
        }

    }
}

/*@Composable
fun Footer(){
    Row(
        modifier = Modifier

            .fillMaxWidth(),
    ){

        Button(onClick = { /*TODO*/ }, modifier = Modifier.weight(1f)) {
            Text(text = "Home")
        }
        Button(onClick = { /*TODO*/ }, modifier = Modifier.weight(1f)) {
            Text(text = "Catalogo")
        }
        Button(onClick = { /*TODO*/ }, modifier = Modifier.weight(1f)) {
            Text(text = "Carrello")
        }
        Button(onClick = { /*TODO*/ }, modifier = Modifier.weight(1f)) {
            Text(text = "Area Cliente")
        }

        
    }
}
*/


//Funzione che carica l'immagine tramite URL
@OptIn(ExperimentalCoilApi::class)
@Composable
fun LoadImageFromUrl(imageUrl: String) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier.clip(RoundedCornerShape(160.dp)).fillMaxSize(),
    )
}

//Funzione che scarica l'URL in base al nome del file
@Composable
fun FindUrl(fileName: String): String{
    var url by remember { mutableStateOf<String>("") }
    storage.child(fileName).downloadUrl.addOnSuccessListener {
        // Got the download URL for 'users/me/profile.png'
        url=it.toString()
    }.addOnFailureListener {
        // Handle any errors
        Log.e("Foto Error", "Errore nel listener "+fileName)
    }

    return url
}

@Composable
fun listaFiltrata(categoria: String, viewModel: AppViewModel): List<DataSnapshot>{
    return viewModel.products.observeAsState(emptyList()).value
        .filter { it.child("categoria").value.toString() == categoria }
}