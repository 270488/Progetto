package it.polito.database.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.database
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@Composable
fun Orders(viewModel: AppViewModel, navController: NavController){
    var uid=viewModel.uid
}

@SuppressLint("NewApi")
fun aggiungiOrdine(viewModel: AppViewModel){
    var uid=viewModel.uid
    val dataAttuale = LocalDate.now()
    val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    var dataOrdine=dataAttuale.format(formato)
    var dataConsegna=dataAttuale.plusDays(1).format(formato)
    var nOrdine= Random.nextInt(10000, 100000)
    var locker=viewModel.lockerSelezionato
    var sportello=0
    var stato="ordinato"
    var totale=0.00

    var ordini=database.child("ordini")
    ordini.child(nOrdine.toString()).child("stato").setValue(stato)
    ordini.child(nOrdine.toString()).child("uid").setValue(uid)
    ordini.child(nOrdine.toString()).child("Locker").setValue(locker)
    ordini.child(nOrdine.toString()).child("Sportello").setValue(sportello)
    ordini.child(nOrdine.toString()).child("Data Ordine").setValue(dataOrdine)
    ordini.child(nOrdine.toString()).child("Data Consegna").setValue(dataConsegna)
    ordini.child(nOrdine.toString()).child("Totale").setValue(totale)


    //prodotti e quantità
    var carrello=viewModel.carrello.value.orEmpty()

    println("carrello: "+carrello.toString())

    carrello.forEach{(item, qty)->
        println("Prodotto: "+item+" Quantità: "+qty.toString())
        ordini.child(nOrdine.toString()).child("Prodotti").child(item).setValue(qty)
        eliminaDalCarrello(item = item, uid, viewModel)
    }


    viewModel.carrello.value= emptyMap()
    database.child("utenti").child(uid).child("ordini").child(nOrdine.toString()).setValue(stato)
}