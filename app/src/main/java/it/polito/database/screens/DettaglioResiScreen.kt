package it.polito.database.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.FindUrl
import it.polito.database.R
import it.polito.database.database
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
        verticalArrangement = Arrangement.SpaceBetween,
        modifier= Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .verticalScroll(rememberScrollState())
            .padding(top = 90.dp, bottom = 110.dp)
    ){
        DettaglioResoCard(navController,viewModel= viewModel, prezzo= prezzo,prodotto = prodotti, scadenza = scadenza, numeroOrdine = ordine, stato = stato, url = url)
        Row {
            istruzioniConsegna()
            Button(onClick = { }) {
                Text(text = "Annulla reso")
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
            .padding(horizontal = 10.dp))
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
                Spacer(modifier = Modifier.width(10.dp))
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
                        fontSize = 16.sp,
                        fontFamily = fontFamily,
                    )
                    Text(
                        text = "Scadenza reso: " + scadenza,
                        fontFamily = fontFamily,
                        fontSize = 16.sp
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
                        modifier = Modifier.padding(vertical = 12.dp)
                    ) {
                        Image(
                            modifier = Modifier.size(50.dp),
                            painter = painterResource(id = R.drawable.reso_avviato),
                            contentDescription = ""
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Reso avviato",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontFamily = fontFamily,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
                else if(stato=="consegnato"){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 12.dp)
                    ) {
                        Image(
                            modifier = Modifier.size(50.dp),
                            painter = painterResource(id = R.drawable.pacco_consegnato),
                            contentDescription = ""
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Pacco consegnato",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontFamily = fontFamily,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
                else if(stato=="scaduto"){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 12.dp)
                    ) {
                        Image(
                            modifier = Modifier.size(50.dp),
                            painter = painterResource(id = R.drawable.reso_scaduto),
                            contentDescription = ""
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Reso scaduto",
                            fontSize = 16.sp,
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
                            modifier = Modifier.size(50.dp),
                            painter = painterResource(id = R.drawable.reso_completato),
                            contentDescription = ""
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Reso completato",
                            fontSize = 16.sp,
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
                    )
                )
                {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = viewModel.lockerSelezionato,
                        fontSize = 16.sp,
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
                    .padding(horizontal = 8.dp, vertical = 16.dp),
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
                fontSize = 18.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold
            )
            Column(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
            ){
                Text(
                    text = "Il reso stimato è di euro "+prezzo,
                    fontSize = 16.sp,
                    fontFamily = fontFamily,
                )
                Text(
                    text = "Il reso sarà accreditato sul tuo metodo di pagamento predefinito",
                    fontSize = 16.sp,
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

    Button(
        onClick = { isDialogOpen = true },
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Istruzioni per la consegna")
    }


    if (isDialogOpen) {

        Popup(
            onDismissRequest = { isDialogOpen = false },
            modifier = Modifier.fillMaxSize()
        ) {
            // Contenuto del tuo popup personalizzato
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Column(){
                    Text("Istruzioni per la consegna")
                    Text("Consegna")
                    Text("Consegna il prodotto presso la reception della palestra in cui l’hai ritirato, se vuoi cambiare palestra ti basterà cliccare sulla scritta “Scegli un’altra palestra”")
                    Text("Imballaggio")
                    Text("Tutto quello che devi fare per preparare il pacco è inserire il prodotto nella scatola originale. Non dovrai stampare alcuna etichetta!")
                    Text("Rimborso")
                    Text("Non appena il tuo pacco verrà ricevuto dal magazzino riceverai un rimborso completo in seguito ad una verifica.")

                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { isDialogOpen = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Chiudi")
                }
            }
        }

    }
}
@Composable
fun Popup(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = CircleShape,
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
}
