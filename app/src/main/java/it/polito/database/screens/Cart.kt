package it.polito.database.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.FindUrl
import it.polito.database.database
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.fontFamily
import java.lang.Double.sum
import kotlin.math.roundToInt


@Composable
    fun CartScreen(viewModel: AppViewModel, navController: NavController) {
    Cart(viewModel, navController)
    }

@Composable
fun Cart(viewModel: AppViewModel, navController: NavController, modifier: Modifier = Modifier
    .padding(top = 74.dp)
    .padding(bottom = 74.dp)){

    var credenziali= database.child("utenti").child(viewModel.uid)
    var nome by remember { mutableStateOf("") }

    credenziali.addValueEventListener(object: ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            nome=dataSnapshot.child("nome").value.toString()
        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })

    var listaCarrello by remember { mutableStateOf<List<String>>(emptyList()) }
    var id=viewModel.uid
    var items= database.child("utenti").child(id).child("carrello")
    items.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            var lista= mutableListOf<String>()
            for (childSnapshot in dataSnapshot.children) {
                lista.add(childSnapshot.child("nome").value.toString())
            }
            listaCarrello=lista
        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })

    //var totale = viewModel.tot
    var totale =0.00

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier= Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .verticalScroll(rememberScrollState())
            .padding(top = 90.dp, bottom = 110.dp)
    )
    {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier= Modifier
                .padding(horizontal = 20.dp)
        ){
            var numItem = 0
            if (listaCarrello.isEmpty()){
                totale=0.00
                viewModel.tot=0.00
                Text(
                    text = "Il tuo carrello [$numItem]",
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Il tuo carrello è vuoto.",
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(top = 130.dp, bottom = 16.dp)
                )
            }else {
                listaCarrello.forEach { item ->
                    numItem++
                }
                Text(
                text = "Il tuo carrello [$numItem]",
                fontFamily = fontFamily,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                listaCarrello.forEach { item ->
                    var originalQty by remember {
                        mutableStateOf(0L)
                    }
                    var qty by remember { mutableStateOf(0L)}
                    var items= database.child("utenti").child(id).child("carrello")
                    items.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) { //fa una foto al db in quel momento e la mette in dataSnapshot
                            for (childSnapshot in dataSnapshot.children) {

                                if (childSnapshot.child("nome").value.toString() == item) {

                                    try {
                                        val newQty =
                                            (childSnapshot.child("qty").value as Long).toLong()

                                        // Controlla se la quantità è cambiata rispetto all'originale
                                        if (newQty != originalQty) {
                                            // Aggiorna l'originale solo se la quantità è cambiata
                                            originalQty = newQty
                                        }
                                        // Aggiorna qty sempre con il valore originale
                                        qty = originalQty

                                    } catch (e: ClassCastException) {
                                        Log.e("CastingError", "Errore durante il casting di qty", e)
                                    }
                                }
                             }
                            }
                        override fun onCancelled(databaseError: DatabaseError) {
                            println("Errore nel leggere i dati dal database: ${databaseError.message}")
                        }
                    })
                    Log.d("quantità prodotto: ", qty.toString())
                    val product = viewModel.products.observeAsState(emptyList()).value
                        .filter { it.child("nome").value.toString() == item }
                    product.forEach { p ->
                        var prezzo=p.child("prezzo").value as Double
                        Log.d("quantità prodotto: ", qty.toString())
                        totale = sum(qty.toDouble()*prezzo, totale)
                    }

                    ItemCard(viewModel, item, id, navController)
                }
            }
        }
        Log.d("totale: ", totale.toString())
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp))
            {
                Text(
                    text = "Riepilogo",
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = "Totale ordine:",
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    text = "%.2f".format(totale) + "€",
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal,
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp))
            {
                Text(
                    text = buildAnnotatedString {
                        append("In consegna a ")
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        ){
                            append(nome.capitalize())
                        }
                    },
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 22.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Normal,
                )

            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp))
            {
                Text(
                    text = viewModel.lockerSelezionato.replace("/"," "),
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                )
            }

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            )
            {
                Button(
                    onClick = {
                        if(!listaCarrello.isEmpty()){
                            navController.navigate(Screen.Checkout.route)
                            viewModel.tot=totale
                        }

                    },
                    modifier = Modifier
                        .layoutId("btnCheckOut")
                        .padding(top = 20.dp, end = 8.dp),
                    shape = RoundedCornerShape(3.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 16.dp,
                        pressedElevation = 0.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                ){
                    Text(
                        text = "Vai al checkout",
                        fontFamily = fontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

        }

    }

}

fun aggiungiAlCarrello(item: String, id: String, qty: Int, viewModel: AppViewModel) {
    var items= database.child("utenti").child(id).child("carrello")
    val currentMap = viewModel.carrello.value.orEmpty()
    if(!currentMap.containsKey(item)){
        val updatedMap = currentMap + (item to qty )
        viewModel.carrello.value = updatedMap

        items.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var lastKey = 0
                if(dataSnapshot.exists()){
                    for (childSnapshot in dataSnapshot.children) {
                        val i = childSnapshot.key?.toInt() ?: 0
                        lastKey= i+1
                    }


                    database.child("utenti").child(id).child("carrello").child(lastKey.toString()).child("nome").setValue(item)
                }
                else{
                    //val itemsMap = mapOf(lastKey.toString() to item)
                    database.child("utenti").child(id).child("carrello").child(0.toString()).child("nome").setValue(item)
                }
                database.child("utenti").child(id).child("carrello").child(lastKey.toString()).child("qty").setValue(qty)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("Errore nel leggere i dati dal database: ${databaseError.message}")
            }
        }


    )
    }
}

fun eliminaDalCarrello(item: String, id: String, viewModel: AppViewModel){
    var items= database.child("utenti").child(id).child("carrello")
    val currentMap = viewModel.carrello.value.orEmpty()
    val updatedMap = currentMap - item
    viewModel.carrello.value = updatedMap

    items.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { //fa una foto al db in quel momento e la mette in dataSnapshot
            for (childSnapshot in dataSnapshot.children) {
                if(childSnapshot.child("nome").value.toString()==item){
                    childSnapshot.ref.removeValue()
                }
            }
        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ItemCard(
    viewModel: AppViewModel,
    item: String,
    id: String,
    navController: NavController
) {
    val product = viewModel.products.observeAsState(emptyList()).value
        .filter { it.child("nome").value.toString() == item }

    var nome = ""
    var prezzo = ""
    var url = ""
    var originalQty by remember {
        mutableStateOf(0L)
    }
    var qty by remember {
        mutableStateOf(0L)
    }
    product.forEach { p ->
        nome = p.child("nome").value.toString()
        prezzo = p.child("prezzo").value.toString()
        url = FindUrl(fileName = nome + ".jpg")

    }
    val prodCarrello = database.child("utenti").child(id).child("carrello")
    prodCarrello.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { //fa una foto al db in quel momento e la mette in dataSnapshot
            for (childSnapshot in dataSnapshot.children) {

                if (childSnapshot.child("nome").value.toString() == item) {

                    try {
                        val newQty = (childSnapshot.child("qty").value as Long).toLong()

                        // Controlla se la quantità è cambiata rispetto all'originale
                        if (newQty != originalQty) {
                            // Aggiorna l'originale solo se la quantità è cambiata
                            originalQty = newQty
                        }
                        // Aggiorna qty sempre con il valore originale
                        qty = originalQty

                    } catch (e: ClassCastException) {
                        Log.e("CastingError", "Errore durante il casting di qty", e)
                    }
                }

            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })

    val squareSize = 80.dp
    val swipeAbleState = rememberSwipeableState(initialValue = 0)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, -sizePx to 1)


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.tertiary)
            .swipeable(
                state = swipeAbleState,
                anchors = anchors,
                thresholds = { _, _ ->
                    androidx.compose.material.FractionalThreshold(0.3f)
                },
                orientation = Orientation.Horizontal
            )
    ) {
        //ICONE CHE COMPAIONO
        Column(
            modifier = Modifier.padding(24.dp).align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                    eliminaDalCarrello(
                        item = nome,
                        id = id,
                        viewModel
                    )
                },
                contentDescription = "Delete",
                tint = Color.Black
            )
        }
        Card(
            modifier = Modifier
                .height(90.dp)
                .offset {
                    IntOffset(
                        swipeAbleState.offset.value.roundToInt(), 0
                    )
                },
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
            ),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
            onClick = {
                viewModel.prodottoSelezionato = nome
                navController.navigate(Screen.Product.route)
            }
        ) {

            var clicked by remember {
                mutableStateOf(true)
            }

            Box(modifier = Modifier.fillMaxSize())
            {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    //horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .width(150.dp),
                        contentScale = ContentScale.Crop
                    )

                    //Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = 10.dp, vertical = 12.dp)
                    )
                    {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            val nomeTroncato = if (nome.length > 16) {
                                nome.take(16) + ".."
                            } else {
                                nome
                            }
                            Text(
                                text = nomeTroncato.replaceFirstChar { it.uppercase() },
                                fontFamily = fontFamily,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = prezzo + "€",
                                fontFamily = fontFamily,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start
                            )
                            SelettoreQuantita(viewModel, qty)

                        }
                    }

                    //Text(text = nome)
                    //Text(text = prezzo + "€")

                    //Non metterei la possibilità di aggiungere ai preferiti anche dal carrello
                    /*TextButton(onClick = { aggiungiPreferito(prod = nome, id = id)
                eliminaDalCarrello(item = nome, id = id)}) {
                    Text(text = "Salva nei preferiti", color = MaterialTheme.colorScheme.onPrimary)}*/
                }


            }
        }
        //Spacer(modifier = Modifier.height(16.dp))
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun SelettoreQuantita(viewModel: AppViewModel, quantita: Long) {
    var count = quantita
    //var count by remember { mutableIntStateOf(quantita.toInt()) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.offset(y = (2).dp)
    )
    {
        Text(
            text = "-",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = fontFamily,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.clickable {
                if(count>1){
                    count--;
                }
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = count.toString(),
            fontFamily = fontFamily,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "+",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = fontFamily,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.clickable {
                if(count<4){
                count++;
                }
            }
        )
    }
}