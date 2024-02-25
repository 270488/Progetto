package it.polito.database.screens

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.database
import kotlin.random.Random

fun writeVariables(variabili: GestioneArduino) {
    variabili.variabiliPath.child("CodeP").setValue(variabili.CodeP)
    variabili.variabiliPath.child("CodeG").setValue(variabili.CodeG)
    variabili.variabiliPath.child("CodiceTastierino").setValue(variabili.CodiceTastierino)
    variabili.variabiliPath.child("PPAperta").setValue(variabili.PPAperta)
    variabili.variabiliPath.child("PGAperta").setValue(variabili.PGAperta)
    variabili.variabiliPath.child("Sblocco").setValue(variabili.Sblocco)
    variabili.variabiliPath.child("SportelloG").setValue(variabili.SportelloG)
    variabili.variabiliPath.child("SportelloP").setValue(variabili.SportelloP)
    variabili.variabiliPath.child("CodiceErrato").setValue(variabili.CodiceErrato)
}

data class GestioneArduino(
                           var CodeP :String,
                           var CodeG :String,
                           var CodiceTastierino:String,
                           var PGAperta:Long,
                           var PPAperta:Long,
                           var Sblocco :Long,
                           var SportelloP :Boolean,
                           var SportelloG :Boolean,
    var CodiceErrato: Long) {
    var variabiliPath = database.child("variabili")



}

fun confrontoCodici(variabili: GestioneArduino){
    if(variabili.CodiceTastierino==variabili.CodeP){
        variabili.Sblocco=0L
        variabili.PPAperta=1L
        variabili.CodeP=""
        variabili.CodiceTastierino="0000"
        variabili.CodiceErrato=0L
        writeVariables(variabili)

    }
    else if(variabili.CodiceTastierino==variabili.CodeG){
        variabili.Sblocco=0L
        variabili.CodeG=""
        variabili.PGAperta=1L
        variabili.CodiceTastierino="0000"
        variabili.CodiceErrato=0L
        writeVariables(variabili)
    }
    if(variabili.CodiceTastierino!="0000" ){
        if(variabili.CodiceTastierino!= variabili.CodeG || variabili.CodiceTastierino!= variabili.CodeP){
            variabili.CodiceErrato=1L
            writeVariables(variabili)
        }

    }

}

fun assegnazioneSportello(numeroProdotti: Int, viewModel: AppViewModel, codiceF: String, codiceU: String){


    var codiceCasualeU= codiceU

    if(!viewModel.variabili.SportelloG && numeroProdotti>2){
        viewModel.variabili.SportelloG=true
        viewModel.variabili.CodeG=codiceCasualeU
        database.child("ordini").child(viewModel.ordineSelezionato).child("Sportello").setValue("G")
        writeVariables(variabili = viewModel.variabili)
    }
    if(numeroProdotti<=2 && !viewModel.variabili.SportelloP){
        viewModel.variabili.SportelloP=true
        viewModel.variabili.CodeP=codiceCasualeU
        database.child("ordini").child(viewModel.ordineSelezionato).child("Sportello").setValue("P")

        writeVariables(variabili = viewModel.variabili)
    }
    else if(numeroProdotti<=2 && !viewModel.variabili.SportelloG){
        viewModel.variabili.SportelloG=true
        viewModel.variabili.CodeP=codiceCasualeU
        database.child("ordini").child(viewModel.ordineSelezionato).child("Sportello").setValue("G")

        writeVariables(variabili = viewModel.variabili)
    }
}

fun generazioneCodiceCasuale(): String{
    val caratteriPossibili = "ABCD123456789"
    val lunghezzaStringa = 4

    val stringaCasuale = (1..lunghezzaStringa)
        .map { caratteriPossibili[Random.nextInt(caratteriPossibili.length)] }
        .joinToString("")
    return stringaCasuale
}