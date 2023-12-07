package it.polito.database

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.google.firebase.database.values

class AppViewModel: ViewModel() {
    fun writeNewUser(userId: String, name: String, email: String) {
        val user = User(userId, name, email)

        database.child("users").child(userId).setValue(user)
        database.child("users").child("anna00").child("name").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

    }
}

@Composable
fun HomePage(){
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .height(600.dp)
        )
    {
        IntestazioneHome()
        Spacer(modifier = Modifier.height(5.dp))
        ScrollableColumn()
        Spacer(modifier = Modifier
            .height(5.dp)
            .weight(1f))
        Footer()

    }

}

@Composable
fun IntestazioneHome(){
    Row(modifier = Modifier
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
@Composable
fun ScrollableColumn() {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier=Modifier.weight(0.25f))
        //Prima riga da non perdere
        Column(modifier = Modifier){

            Row(modifier=Modifier.fillMaxWidth()){
                Text(text = "Da non perdere", color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start)
                Text(text = "Cerca", color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End)
            }
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())){
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Figura 1")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Figura 2")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Figura 3")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Figura 3")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Figura 4")
                }
            }
            Text(text = "________________________________________________________________", color = Color.Yellow)
        }
        Spacer(modifier=Modifier.weight(0.25f))
        //Seconda riga offerte per te
        Column(modifier = Modifier){

            Row(modifier=Modifier.fillMaxWidth()){
                Text(text = "Offerte per te", color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start)

            }
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())){
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Figura 1")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Figura 2")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Figura 3")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Figura 3")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Figura 4")
                }
            }
            Text(text = "________________________________________________________________", color = Color.Yellow)
        }
        Spacer(modifier=Modifier.weight(0.25f))

        //Terza riga acquista di nuovo
        Column(modifier = Modifier){

            Row(modifier=Modifier.fillMaxWidth()){
                Text(text = "Acquista di nuovo", color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start)

            }
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())){
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Figura 1")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Figura 2")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Figura 3")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Figura 3")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Figura 4")
                }
            }
            Text(text = "________________________________________________________________", color = Color.Yellow)
        }
        Spacer(modifier=Modifier.weight(0.25f))
    }
}

@Composable
fun Footer(){
    Row(modifier = Modifier.fillMaxWidth()){
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
