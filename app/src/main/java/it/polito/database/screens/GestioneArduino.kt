package it.polito.database.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.database

fun writeVariables(variabili: GestioneArduino) {
    variabili.variabiliPath.child("CodeP").setValue(variabili.CodeP)
    variabili.variabiliPath.child("CodeG").setValue(variabili.CodeG)
    variabili.variabiliPath.child("CodiceTastierino").setValue(variabili.CodiceTastierino)
    variabili.variabiliPath.child("PPAperta").setValue(variabili.PPAperta)
    variabili.variabiliPath.child("PGAperta").setValue(variabili.PGAperta)
    variabili.variabiliPath.child("Sblocco").setValue(variabili.Sblocco)
}

class GestioneArduino {
    var variabiliPath= database.child("variabili")

    var CodeP=""
    var CodeG=""
    var CodiceTastierino=""
    var PPAperta=0L
    var PGAperta=0L
    var Sblocco=0L

}


fun onDataChange(variabili: GestioneArduino){
    
    variabili.variabiliPath.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { //fa una foto al db in quel momento e la mette in dataSnapshot
            variabili.CodeP=dataSnapshot.child("CodeP").value.toString()
            variabili.CodeG=dataSnapshot.child("CodeG").value.toString()
            variabili.CodiceTastierino=dataSnapshot.child("CodiceTastierino").value.toString()
            variabili.PPAperta=dataSnapshot.child("PPAperta").value as Long
            variabili.PGAperta=dataSnapshot.child("PGAperta").value as Long
            variabili.Sblocco=dataSnapshot.child("Sblocco").value as Long

            println("CodeP: "+variabili.CodeP)
            println("CodeG: "+variabili.CodeG)
            println("CodiceTastierino: "+variabili.CodiceTastierino)
            println("PPAPerta: "+variabili.PPAperta)
            println("PGAperta: "+variabili.PGAperta)
            println("Sblocco: "+variabili.Sblocco)

            confrontoCodici(variabili)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Gestisci gli errori qui
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })
}

fun confrontoCodici(variabili: GestioneArduino){
    if(variabili.CodiceTastierino==variabili.CodeP){
        variabili.Sblocco=0L
        writeVariables(variabili)
        variabili.PPAperta=1L
        variabili.CodiceTastierino="0000"
        writeVariables(variabili)

    }
    else if(variabili.CodiceTastierino==variabili.CodeG){
        variabili.Sblocco=0L
        writeVariables(variabili)
        variabili.PGAperta=1L
        variabili.CodiceTastierino="0000"

        writeVariables(variabili)
    }
}