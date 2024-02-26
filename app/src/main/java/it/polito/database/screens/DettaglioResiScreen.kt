package it.polito.database.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.FindUrl
import it.polito.database.R
import it.polito.database.database
import it.polito.database.ui.theme.Blue20
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.fontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DettaglioResiScreen (viewModel: AppViewModel, navController: NavController) {
    val codiceReso=viewModel.resoSelezionato

    var reso= database.child("resi").child(codiceReso)

    var ordine by remember { mutableStateOf("") }
    var stato by remember { mutableStateOf("") }
    var prodotti by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var scadenza by remember { mutableStateOf("") }

    reso.addValueEventListener(object: ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            ordine=dataSnapshot.child("ordine").value.toString()
            stato=dataSnapshot.child("stato").value.toString()
            prodotti=dataSnapshot.child("prodotti").value.toString()
            scadenza=dataSnapshot.child("scadenza").value.toString()

        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })

    url= FindUrl(fileName = prodotti+".jpg")

    val prod=viewModel.products.observeAsState(emptyList()).value
        .filter{it.child("nome").value.toString()==prodotti}
    var prezzo=""
    prod.forEach{
        p-> prezzo=p.child("prezzo").value.toString()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier= Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = 86.dp, bottom = 90.dp)
            .verticalScroll(rememberScrollState())
    ){
        DettaglioResoCard(navController,viewModel= viewModel, prezzo= prezzo,prodotto = prodotti, scadenza = scadenza, numeroOrdine = ordine, stato = stato, url = url)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            istruzioniConsegna()
            if(stato=="avviato"){
                Button(
                    shape = RoundedCornerShape(3.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    onClick = { eliminaReso(viewModel = viewModel)
                        navController.navigate(Screen.ResiScreen.route)} //TODO Aggiungere eliminazione reso
                ){
                    Text(
                        text = "Annulla reso",
                        fontFamily = fontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

            }

        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DettaglioResoCard(navController: NavController,viewModel: AppViewModel,prezzo: String, prodotto: String, scadenza: String, numeroOrdine: String, stato: String, url:String){

    Card(
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(2.dp, Color.Black),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1D232C),
            contentColor = Color.White
        ),
        modifier = Modifier
            .padding(horizontal = 20.dp))
    {
        Column(){
            Row(){
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .width(150.dp)
                        .height(90.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = prodotto,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily,
                    )
                    Text(
                        text = "Ordine No. " + numeroOrdine,
                        fontSize = 14.sp,
                        fontFamily = fontFamily,
                    )
                    Text(
                        text = "Scadenza: " + scadenza,
                        fontFamily = fontFamily,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.padding(8.dp),
                text = "Stato della spedizione",
                fontSize = 16.sp,
                fontFamily = fontFamily,
                fontStyle = FontStyle.Italic
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                if(stato=="avviato"){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Image(
                            modifier = Modifier.size(48.dp),
                            painter = painterResource(id = R.drawable.reso_avviato),
                            contentDescription = ""
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Reso avviato",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontFamily = fontFamily,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
                else if(stato=="consegnato"){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Image(
                            modifier = Modifier.size(48.dp),
                            painter = painterResource(id = R.drawable.pacco_consegnato),
                            contentDescription = ""
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Pacco consegnato",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontFamily = fontFamily,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
                else if(stato=="scaduto"){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Image(
                            modifier = Modifier.size(48.dp),
                            painter = painterResource(id = R.drawable.reso_scaduto),
                            contentDescription = ""
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Reso scaduto",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontFamily = fontFamily,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
                else if(stato=="completato"){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Image(
                            modifier = Modifier.size(48.dp),
                            painter = painterResource(id = R.drawable.reso_completato),
                            contentDescription = ""
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Reso completato",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontFamily = fontFamily,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Pacco da riconsegnare presso:",
                fontSize = 16.sp,
                fontFamily = fontFamily,
                fontStyle = FontStyle.Italic
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ){
                Card(
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                    shape = RoundedCornerShape(15.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.width(185.dp)
                )
                {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = viewModel.lockerSelezionato.replace("/","\n"),
                        fontSize = 14.sp,
                        fontFamily = fontFamily,
                    )
                }
                if(stato!="scaduto"){
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clickable { navController.navigate(Screen.ScegliPalestraScreen.route) },
                        text = "Scegli un' altra\npalestra",
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.tertiary,
                        textDecoration = TextDecoration.Underline,
                        textAlign = TextAlign.Center,
                        fontFamily = fontFamily,
                    )
                }

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if(stato=="avviato") {
                    Image(
                        //modifier = Modifier.size(50.dp),
                        painter = painterResource(id = R.drawable.barra_stato1),
                        contentDescription = ""
                    )
                }
                else if(stato=="consegnato") {
                    Image(
                        //modifier = Modifier.size(50.dp),
                        painter = painterResource(id = R.drawable.barra_stato2),
                        contentDescription = ""
                    )
                }
                else if(stato=="scaduto") {
                    Image(
                        //modifier = Modifier.size(50.dp),
                        painter = painterResource(id = R.drawable.barra_stato4),
                        contentDescription = "",
                    )
                }
                else if(stato=="completato") {
                    Image(
                        //modifier = Modifier.size(50.dp),
                        painter = painterResource(id = R.drawable.barra_stato3),
                        contentDescription = "",
                    )
                }
            }
            Text(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp),
                text = "Riepilogo",
                fontSize = 16.sp,
                fontFamily = fontFamily,
                fontStyle = FontStyle.Italic
            )
            Column(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
            ){
                Text(
                    text = "Il reso stimato è di euro "+prezzo,
                    fontSize = 14.sp,
                    fontFamily = fontFamily,
                )
                Text(
                    text = "Il reso sarà accreditato sul tuo metodo di pagamento predefinito",
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = fontFamily,
                )

            }


        }

    }

}
@Composable
private fun istruzioniConsegna() {
    var isDialogOpen by remember { mutableStateOf(false) }


    Text(
        modifier = Modifier
            .clickable { isDialogOpen = true },
        text = "Istruzioni per\nla consegna",
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = MaterialTheme.colorScheme.tertiary,
        textDecoration = TextDecoration.Underline,
        //textAlign = TextAlign.Center,
        fontFamily = fontFamily,
    )

    if (isDialogOpen) {

        Popup(
            onDismissRequest = { isDialogOpen = false },
            //modifier = Modifier.fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xCC1D232C))
                    .padding(horizontal = 20.dp)
            ){
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(15.dp))
                        .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(15.dp))
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            //.fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable { isDialogOpen = false },
                                painter = painterResource(id = R.drawable.back_arrow),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                            Text(
                                text = "Istruzioni per la\nconsegna",
                                fontSize = 24.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                fontFamily = fontFamily,
                            )
                            Box(modifier = Modifier.size(30.dp))
                        }
                        Column(
                            modifier = Modifier
                                .height(360.dp)
                                .padding(vertical = 8.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ){
                            Column {
                                Text(
                                    text = "Consegna",
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = fontFamily,
                                )
                                Text(
                                    text = "Consegna il prodotto presso la reception della palestra in cui l’hai ritirato, se vuoi cambiare palestra ti basterà cliccare sulla scritta “Scegli un’altra palestra”",
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    fontFamily = fontFamily,
                                )
                            }
                            Column {
                                Text(
                                    text = "Imballaggio",
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontFamily = fontFamily,
                                )
                                Text(
                                    text = "Tutto quello che devi fare per preparare il pacco è inserire il prodotto nella scatola originale. Non dovrai stampare alcuna etichetta!",
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    fontFamily = fontFamily,
                                )
                            }
                            Column {
                                Text(
                                    text = "Rimborso",
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = fontFamily,
                                )
                                Text(
                                    text = "Non appena il tuo pacco verrà ricevuto dal magazzino riceverai un rimborso completo in seguito ad una verifica.",
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    fontFamily = fontFamily,
                                )
                            }
                        }
                    }
                }
            }


        }

    }
}
/*@Composable
fun Popup(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(15.dp),
    content: @Composable (Modifier) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = Color.Black.copy(alpha = 0.5f),
                shape = shape
            )
            .clip(shape)
            .clickable(onClick = onDismissRequest)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = shape
                )
                .padding(16.dp)
                .clip(shape)
        ) {
            content(Modifier)
        }
    }
}*/
