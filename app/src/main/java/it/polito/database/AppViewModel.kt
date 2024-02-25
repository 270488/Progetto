package it.polito.database

//import androidx.compose.foundation.layout.RowScopeInstance.weight
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import it.polito.database.screens.cambioVariabili
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.screens.GestioneArduino
import it.polito.database.ui.theme.Blue40
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.Yellow40
import it.polito.database.ui.theme.fontFamily


class AppViewModel: ViewModel() {

    var products=MutableLiveData<List<DataSnapshot>>(emptyList())
    var resi=MutableLiveData<List<DataSnapshot>>(emptyList())
    var prodottoSelezionato="shaker"
    var resoSelezionato=""
    var ordineSelezionato=""
    var lockerSelezionato="McFit Via San Paolo, 25/Torino (TO), 10141"
    var cat=""
    var sottocat=""
    var uid=""
    var cId=""
    var quantita = 1
    var tot = 0.00
    var carrello=MutableLiveData<Map<String, Int>>(emptyMap())

    var variabili= GestioneArduino("", "","0000",0L,0L,0L,false, false, 0L)

    var listaPalestre = listOf<String>(
        "McFit Via San Paolo, 25/Torino (TO), 10141",
        "McFit Via Giuseppe Lagrange, 47/Torino (TO), 10123",
        "McFit C.so Giulio Cesare, 29/Torino (TO), 10152",
        "McFit Via Bertola, 31/Torino (TO), 10122",
        "McFit C.so Principe Oddone, 92/Torino (TO), 10158",
        "McFit Via Giuseppe Gaibaldi, 72/Torino (TO), 10135",
        "McFit Via Orietta Berti, 58/Torino (TO), 10155",
        "McFit Via Monginevro, 84/Torino (TO), 10138",
        "McFit Giorgio Mastrota, 22/Torino (TO), 10118",
    )

    fun setLocker(value: String){
        lockerSelezionato = value
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
    fun addResi(reso: DataSnapshot){
        val numeroReso = reso.key.toString()
        // Verifica se esiste già un reso con lo stesso codice
        val resoExists = resi.value?.any { it.key.toString() == numeroReso } ?: false
        if (!resoExists) {
            // Aggiungi il prodotto solo se non esiste già
            val currentList = resi.value.orEmpty()
            val updatedList = currentList + reso
            resi.value = updatedList
        }

    }

    fun addCarrello(item:String, qty: Int){
        val currentMap = carrello.value.orEmpty()
        val updatedMap = currentMap + (item to qty)
        carrello.value = updatedMap

    }


}



@Composable
fun HomePage(viewModel: AppViewModel, navController: NavController){
    val prod by viewModel.products.observeAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary)
        )
    {
        Spacer(modifier = Modifier.height(5.dp))
        ScrollableColumn(viewModel, navController)
        Spacer(modifier = Modifier.height(5.dp))


    }
    cambioVariabili(viewModel.variabili)


}

@Composable
fun ScrollableColumn(viewModel: AppViewModel, navController: NavController) {

    val children =
        database.child("prodotti") //prende dal db il nodo prodotti e aggiunge un listener
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

    val resi =
        database.child("resi") //prende dal db il nodo prodotti e aggiunge un listener
    resi.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { //fa una foto al db in quel momento e la mette in dataSnapshot
            // Itera sui figli del nodo
            for (childSnapshot in dataSnapshot.children) { //prende i figli di prodotti, quindi 0, 1...
                // Aggiungi il prodotto alla lista
                viewModel.addResi(childSnapshot)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Gestisci gli errori qui
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })

    val carrello =
        database.child("utenti").child(viewModel.uid).child("carrello") //prende dal db il nodo prodotti e aggiunge un listener
    carrello.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { //fa una foto al db in quel momento e la mette in dataSnapshot

            for (childSnapshot in dataSnapshot.children) {
                if(childSnapshot.child("qty").value != null){
                    println("longvalue: "+childSnapshot.child("qty").value.toString())
                    println("child: "+childSnapshot.toString())
                    val longValue = childSnapshot.child("qty").value as Long
                    var intValue=0
                    try {
                        intValue = Math.toIntExact(longValue) // Converte Long in Int
                        // Usa intValue come un intero
                    } catch (e: ArithmeticException) {
                        // Gestisci l'overflow o fai qualcos'altro in caso di eccezione
                        e.printStackTrace()
                    }
                    viewModel.addCarrello(childSnapshot.child("nome").value.toString(),  intValue)


                }
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
            .padding(10.dp, 74.dp, 10.dp, 10.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        //Spacer(modifier = Modifier.weight(0.01f))
        //prima riga "DA NON PERDERE"
        Column(modifier = Modifier) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Da non perdere", color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = fontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Cerca", color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = fontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate(Screen.Category.route) }
                )
            }
            //Contiene i bottoni con i prodotti
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
            ) {
                val nonperdere = listaFiltrata(categoria = "non perdere", viewModel = viewModel)
                var isLoading by remember { mutableStateOf(true) }


                if(isLoading){
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .width(380.dp)
                            .height(120.dp))
                    {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = MaterialTheme.colorScheme.tertiary,
                            strokeWidth = ProgressIndicatorDefaults.CircularStrokeWidth
                        )
                    }
                }
                nonperdere?.forEach { p ->
                    val fileName = p.child("nome").value.toString() + ".jpg"
                    val url = FindUrl(fileName = fileName)
                    /*IconButton(onClick = { viewModel.prodottoSelezionato=p.child("nome").value.toString();
                    navController.navigate(Screen.Product.route)}, //CAMBIARE CON DELLE CARDS
                    modifier= Modifier.size(200.dp, 150.dp)){
                        //.background(Color.Gray)) {
                    LoadImageFromUrl(imageUrl = url)
                }*/
                    Card(
                        modifier = Modifier
                            .background(Blue40)
                            .size(170.dp, 102.dp)
                            .fillMaxWidth()
                            .padding(all = 4.dp)
                            .fillMaxHeight()
                            .clickable {
                                run {
                                    viewModel.prodottoSelezionato =
                                        p.child("nome").value.toString();
                                    navController.navigate(Screen.Product.route)
                                }
                            },
                       // border = BorderStroke(2.dp, Yellow40),

                        )
                    {
                        Box (

                        ){
                            AsyncImage(model = url,
                                contentDescription = "Product Description",
                                contentScale = ContentScale.Crop,
                                onLoading = { isLoading = true },
                                onSuccess = { isLoading = false },
                                modifier = Modifier.fillMaxWidth())
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    compositingStrategy = CompositingStrategy.ModulateAlpha
                                    alpha = 0.7f
                                }
                                .background(
                                    brush = Brush.verticalGradient(
                                        listOf(
                                            Color.Transparent,
                                            Color.Black
                                        )
                                    )
                                ))
                            Text(
                                text = p.child("nome").value.toString(),
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 3.dp),
                                fontFamily = fontFamily,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(
                                    shadow = Shadow(
                                        color = Color.Black,
                                        offset = Offset.Zero,
                                        blurRadius = 5f
                                    )
                                )
                            )
                        }
                    }

                }

            }
            //Text(text = "__________________________________________________", color = MaterialTheme.colorScheme.tertiary)
            /*Box(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .background(Yellow40)
                    .size(700.dp, 1.dp)
            )*/
        }
        Spacer(modifier = Modifier.height(22.dp))
        Divider(thickness = 1.dp, color = Yellow40, modifier = Modifier.padding(top = 4.dp, bottom = 16.dp))
        //Spacer(modifier = Modifier.weight(0.01f))
        //Seconda riga OFFERTE PER TE
        Column(modifier = Modifier) {

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)) {

                Text(
                    text = "Offerte per te", color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = fontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.weight(0.01f))
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {

                val offerte = listaFiltrata(categoria = "offerte", viewModel = viewModel)
                var isLoading by remember { mutableStateOf(true) }

                if(isLoading){
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .width(380.dp)
                            .height(120.dp))
                    {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = MaterialTheme.colorScheme.tertiary,
                            strokeWidth = ProgressIndicatorDefaults.CircularStrokeWidth
                        )
                    }
                }

                offerte?.forEach { p ->
                    /*IconButton(onClick = { viewModel.prodottoSelezionato=p.child("nome").value.toString()
                        navController.navigate(Screen.Product.route)  }, modifier=Modifier.size(200.dp, 150.dp)) {

                        LoadImageFromUrl(imageUrl = url)*/
                    val fileName = p.child("nome").value.toString() + ".jpg"
                    val url = FindUrl(fileName = fileName)
                    Card(
                        modifier = Modifier
                            .background(Blue40)
                            .size(170.dp, 102.dp)
                            .fillMaxWidth()
                            .padding(all = 4.dp)
                            .fillMaxHeight()
                            .clickable {
                                run {
                                    viewModel.prodottoSelezionato =
                                        p.child("nome").value.toString();
                                    navController.navigate(Screen.Product.route)
                                }
                            },
                        //border = BorderStroke(2.dp, Yellow40),

                        )
                    {
                        Box (modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)){
                            //LoadImageFromUrl(imageUrl = url)
                            AsyncImage(model = url,
                                contentDescription = "Product Description",
                                contentScale = ContentScale.Crop,
                                onLoading = { isLoading = true },
                                onSuccess = { isLoading = false },
                                modifier = Modifier
                                    .fillMaxSize())
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    compositingStrategy = CompositingStrategy.ModulateAlpha
                                    alpha = 0.7f
                                }
                                .background(
                                    brush = Brush.verticalGradient(
                                        listOf(
                                            Color.Transparent,
                                            Color.Black
                                        )
                                    )
                                ))
                            Text(
                                text = p.child("nome").value.toString(),
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 3.dp),
                                fontFamily = fontFamily,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(
                                    shadow = Shadow(
                                        color = Color.Black,
                                        offset = Offset.Zero,
                                        blurRadius = 5f
                                    )
                                )
                            )
                        }
                    }

                }
            }

        }
        //Text(text = "__________________________________________________", color = Color.Yellow)
       /*( Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .background(Yellow40)
                .size(700.dp, 1.dp)
        )*/
        Spacer(modifier = Modifier.height(22.dp))
        Divider(thickness = 1.dp, color = Yellow40, modifier = Modifier.padding(top = 4.dp, bottom = 16.dp))
        //Spacer(modifier = Modifier.weight(0.01f))
        //Terza riga ACQUISTA DI NUOVO
        Column(modifier = Modifier) {

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)) {
                Text(
                    text = "Acquista di nuovo", color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = fontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.weight(0.01f))
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                val acquista = listaFiltrata(categoria = "acquista", viewModel = viewModel)
                var isLoading by remember { mutableStateOf(true) }

                if(isLoading){
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .width(380.dp)
                            .height(120.dp))
                    {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = MaterialTheme.colorScheme.tertiary,
                            strokeWidth = ProgressIndicatorDefaults.CircularStrokeWidth
                        )
                    }
                }

                acquista?.forEach { p ->
                    /*  IconButton(onClick = {
                        viewModel.prodottoSelezionato = p.child("nome").value.toString();
                        navController.navigate(Screen.Product.route)
                    }, modifier = Modifier.size(200.dp, 150.dp)) {*/
                    val fileName = p.child("nome").value.toString() + ".jpg"
                    val url = FindUrl(fileName = fileName)
                    Card(
                        modifier = Modifier
                            .background(Blue40)
                            .size(170.dp, 102.dp)
                            .fillMaxWidth()
                            .padding(all = 4.dp)
                            .fillMaxHeight()
                            .clickable {
                                run {
                                    viewModel.prodottoSelezionato =
                                        p.child("nome").value.toString();
                                    navController.navigate(Screen.Product.route)
                                }
                            },
                        //border = BorderStroke(2.dp, Yellow40),

                        )
                    {
                        Box{
                            //LoadImageFromUrl(imageUrl = url)
                            AsyncImage(model = url,
                                contentDescription = "Product Description",
                                contentScale = ContentScale.Crop,
                                onLoading = { isLoading = true },
                                onSuccess = { isLoading = false },
                                modifier = Modifier.fillMaxWidth())
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    compositingStrategy = CompositingStrategy.ModulateAlpha
                                    alpha = 0.7f
                                }
                                .background(
                                    brush = Brush.verticalGradient(
                                        listOf(
                                            Color.Transparent,
                                            Color.Black
                                        )
                                    )
                                ))
                            Text(
                                text = p.child("nome").value.toString(),
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 3.dp),
                                fontFamily = fontFamily,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(
                                    shadow = Shadow(
                                        color = Color.Black,
                                        offset = Offset.Zero,
                                        blurRadius = 5f
                                    )
                                )
                            )
                        }

                    }
                }
            }

        }
    }
}


//Funzione che carica l'immagine tramite URL
@OptIn(ExperimentalCoilApi::class)
@Composable
fun LoadImageFromUrl(imageUrl: String) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp)),
        contentScale = ContentScale.FillWidth
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
    }/*
    if (url == ""){
        var fileNameNew = fileName.substring(0, fileName.length - 3) +
                fileName.substring(fileName.length - 3).uppercase()
        storage.child(fileNameNew).downloadUrl.addOnSuccessListener {
            // Got the download URL for 'users/me/profile.png'
            url=it.toString()
        }.addOnFailureListener {
            // Handle any errors
            Log.e("Foto Error", "Errore nel listener "+fileName)
        }
    }*/
    return url
}

@Composable
fun listaFiltrata(categoria: String, viewModel: AppViewModel): List<DataSnapshot>{
    return viewModel.products.observeAsState(emptyList()).value
        .filter { it.child("sezione").value.toString() == categoria }
}