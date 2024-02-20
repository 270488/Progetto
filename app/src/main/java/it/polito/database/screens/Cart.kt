package it.polito.database.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
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
                lista.add(childSnapshot.child("nome").value.toString())
            }
            listaCarrello=lista
        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })

    var totale = viewModel.tot

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
                Text(
                    text = "Il tuo carrello è vuoto.",
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }else {
                listaCarrello.forEach { item ->
                    numItem++
                }
                Text(
                text = "Il tuo carrello [$numItem]",
                fontFamily = fontFamily,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold
                )
                listaCarrello.forEach { item ->
                    var prezzo= 0.00
                    var qty=item
                    val product = viewModel.products.observeAsState(emptyList()).value
                        .filter { it.child("nome").value.toString() == item }
                    product.forEach { p ->
                        totale = sum(p.child("prezzo").value as Double, totale)
                    }
                    ItemCard(viewModel, item, id, navController)
                }
            }
        }

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
                            append("Pippo")
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
                    text = "FitLocker Via San Paolo, 25, Torino (TO), 10138",
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
                              navController.navigate(Screen.Checkout.route)
                    },
                    modifier = Modifier
                        .layoutId("btnCheckOut")
                        .padding(top = 20.dp, end = 8.dp),
                    shape = RoundedCornerShape(3.dp),
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

fun aggiungiAlCarrello(item: String, id: String, qty: Int) {
    var items= database.child("utenti").child(id).child("carrello")
    var prodotti= database.child("prodotti")

    var quantita=0
    var prezzo=0.00
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
                database.child("utenti").child(id).child("carrello").child(lastKey.toString()).child("nome").setValue(item)
            }
            database.child("utenti").child(id).child("carrello").child(lastKey.toString()).child("qty").setValue(qty)
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
    var totCard=0.00
    var prezzoDouble=0.00
    var url=""
    var originalQty by remember {
        mutableStateOf(0L)
    }
    var qty by remember {
        mutableStateOf(0L)
    }
    product.forEach { p ->
        nome=p.child("nome").value.toString()
        prezzo=p.child("prezzo").value.toString()
        prezzoDouble=p.child("prezzo").value as Double
        url= FindUrl(fileName = nome+".jpg")

    }
    val prodCarrello= database.child("utenti").child(id).child("carrello")
    prodCarrello.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { //fa una foto al db in quel momento e la mette in dataSnapshot
            for (childSnapshot in dataSnapshot.children) {

                if(childSnapshot.child("nome").value.toString()==item){

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


    totCard+=qty*prezzoDouble


    Card(
        modifier = Modifier.height(90.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor= MaterialTheme.colorScheme.onSecondary,
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
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                )
                {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                        .fillMaxWidth()
                    ) {
                        val nomeTroncato = if (nome.length > 11) {
                            nome.take(12) + ".."
                        } else {
                            nome
                        }
                        Text(
                            text = nomeTroncato.replaceFirstChar{ it.uppercase() },
                            fontFamily = fontFamily,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                        SelettoreQuantita(viewModel,qty)
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
                        Text(
                            text = "Rimuovi",
                            fontFamily = fontFamily,
                            fontSize = 16.sp,
                            textDecoration = TextDecoration.Underline,
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.clickable { eliminaDalCarrello(item = nome, id = id) }
                        )

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
    Spacer(modifier = Modifier.height(16.dp))

}

@Composable
fun SelettoreQuantita(viewModel: AppViewModel, quantita: Long) {
    //var qty = viewModel.quantita
    var count = quantita // TODO Cambiare valore iniziale con valore della quantità inserita e aggiornare vm e db di conseguenza (possiamo anche lasciarlo finto)

    Row(
        modifier = Modifier.offset(y = (-5).dp),
        verticalAlignment = Alignment.CenterVertically)
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
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = count.toString(),
            fontFamily = fontFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.width(6.dp))
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