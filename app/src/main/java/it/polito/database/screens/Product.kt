package it.polito.database.screens

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import it.polito.database.AppViewModel
import it.polito.database.FindUrl
import it.polito.database.database
import it.polito.database.ui.theme.Blue20
import it.polito.database.ui.theme.Red20
import it.polito.database.ui.theme.Red40
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.Yellow40
import it.polito.database.ui.theme.Yellow80
import it.polito.database.ui.theme.fontFamily
import kotlinx.coroutines.delay


@SuppressLint("RememberReturnType")

@Composable
fun ProductScreen(viewModel: AppViewModel, navController: NavController) {
    ProductDetail(viewModel, navController)
}
@Composable
fun ProductDetail(viewModel: AppViewModel, navController: NavController) {

    var id = viewModel.uid
    var quantita = viewModel.quantita
    var prod=viewModel.prodottoSelezionato

    var clicked by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(clicked) {
        if (clicked) {
            delay(3000)
            clicked = false
        }
    }

    val product= viewModel.products.observeAsState(emptyList()).value
        .filter { it.child("nome").value.toString() == prod }
    Log.d("product: ", product.toString())

    var nome=""
    var prezzo=0.00
    var url=""
    var descrizione=""
    var categoria=""
    var sottocategoria=""

    product.forEach { p ->
        nome=p.child("nome").value.toString()
        prezzo= p.child("prezzo").getValue() as Double
        url= FindUrl(fileName = nome+".jpg")
        categoria=p.child("categoria").value.toString()
        sottocategoria=p.child("sottocategoria").value.toString()
        descrizione=p.child("descrizione").value.toString()
    }

    var listaPreferiti by remember { mutableStateOf<List<String>>(emptyList()) }

    var preferiti= database.child("utenti").child(id).child("preferiti")
    preferiti.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { //fa una foto al db in quel momento e la mette in dataSnapshot
            // Itera sui figli del nodo
            var list= mutableListOf<String>()


            for (childSnapshot in dataSnapshot.children) { //prende i figli di prodotti, quindi 0, 1...
                // Aggiungi il prodotto alla lista
                list.add(childSnapshot.value.toString())
            }
            listaPreferiti=list
        }
        override fun onCancelled(databaseError: DatabaseError) {
            // Gestisci gli errori qui
            println("Errore nel leggere i dati dal database: ${databaseError.message}")
        }
    })

    var filledHeart by remember{ mutableStateOf(false) }
    if(listaPreferiti.contains(nome)){
        filledHeart=true
    }
    else
        filledHeart=false

    var rating by remember { mutableStateOf(4f) }

    Box {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(top = 90.dp, bottom = 76.dp)
                .verticalScroll(rememberScrollState())
        ){
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(205.dp))
                {
                    var isLoading by remember { mutableStateOf(true) }

                    if(isLoading){
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(50.dp)
                                .align(Alignment.Center),
                            color = MaterialTheme.colorScheme.tertiary,
                            strokeWidth = ProgressIndicatorDefaults.CircularStrokeWidth
                        )
                    }
                    AsyncImage(
                        model=url,
                        contentDescription = "Product Image",
                        contentScale = ContentScale.Crop,
                        onLoading = { isLoading = true },
                        onSuccess = { isLoading = false },
                        modifier = Modifier.fillMaxSize(),
                    )

                    if(!filledHeart && !isLoading) {
                        Image(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            colorFilter = ColorFilter.tint(Red20),
                            contentDescription = "Empty Heart",
                            modifier = Modifier
                                .size(60.dp)
                                .align(Alignment.BottomEnd)
                                .padding(12.dp)
                                .clickable {
                                    filledHeart = !filledHeart
                                    if (filledHeart) {
                                        aggiungiPreferito(prod = nome, id = id)
                                    } else
                                        eliminaPreferito(prod = nome, id = id)
                                },
                        )}else{
                        if(!isLoading){
                            Image(
                                imageVector = Icons.Outlined.Favorite,
                                contentDescription = "Filled Heart",
                                colorFilter = ColorFilter.tint(Red40),
                                modifier = Modifier
                                    .size(60.dp)
                                    .align(Alignment.BottomEnd)
                                    .padding(12.dp)
                                    .clickable {
                                        filledHeart = !filledHeart
                                        if (filledHeart) {
                                            aggiungiPreferito(prod = nome, id = id)
                                        } else
                                            eliminaPreferito(prod = nome, id = id)
                                    },
                            )
                        }
                    }
                }
                //Spacer(modifier = Modifier.height(16.dp))
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = categoria+"/ "+sottocategoria,
                        modifier = Modifier
                            .layoutId("productCategory")
                            .padding(top = 8.dp),
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        fontFamily= fontFamily,
                        color = MaterialTheme.colorScheme.background
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = nome,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = fontFamily,
                            color = Color.White,
                            modifier = Modifier
                                .layoutId("productName")
                        )
                        Text(
                            text = "%.2f".format(prezzo)+"€",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = fontFamily,
                            color = Color.White,
                            modifier = Modifier
                                .layoutId("productPrice")
                        )
                    }

                    /* RATING BAR CON PALLINE
                    RatingBar(
                        value = rating,
                        size = 26.dp,
                        stepSize = StepSize.HALF,
                        painterEmpty = painterResource(id = R.drawable.dot_empty),
                        painterFilled = painterResource(id = R.drawable.dot_filled),
                        onValueChange = {
                            rating = it
                        },
                        onRatingChanged = {
                            Log.d("TAG", "onRatingChanged: $it")
                        }
                    )
                    */
                    RatingBar(
                        modifier = Modifier.offset(y = (-8.dp)),
                        value = rating,
                        size = 28.dp,
                        stepSize = StepSize.HALF,
                        style = RatingBarStyle.Fill(
                            inActiveColor = MaterialTheme.colorScheme.tertiaryContainer,
                            activeColor = MaterialTheme.colorScheme.tertiary
                        ),
                        onValueChange = {
                            rating = it
                        },
                        onRatingChanged = {
                            Log.d("TAG", "onRatingChanged: $it")
                        }
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        OptionSelection()
                        QtySelection(viewModel)
                    }

                    Text(
                        text = "Descrizione",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        fontFamily = fontFamily,
                        modifier = Modifier
                            .layoutId("txtDescriptionTitle")
                            .padding(top = 8.dp),
                        color = Color.White,
                    )

                    Card(
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth()
                            .layoutId("txtDescription")
                            .padding(top = 8.dp),
                        colors = CardDefaults.cardColors(Blue20),
                        border = BorderStroke(2.dp, Color.Black)
                    ) {
                        Text(
                            text = descrizione,
                            color = Color.White,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(8.dp)
                                .verticalScroll(rememberScrollState())
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ){

                        Button(
                            onClick = {
                                Log.d("quantità selezionata: ", viewModel.quantita.toString())
                                aggiungiAlCarrello(item = nome, id = id, qty = viewModel.quantita, viewModel)
                                clicked = true
                            },
                            shape = RoundedCornerShape(3.dp),
                            modifier = Modifier
                                .layoutId("btnBuy"),
                            colors = ButtonDefaults.buttonColors(Yellow40),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 16.dp,
                                pressedElevation = 0.dp
                            ),
                        ){
                            Text(
                                text = "Aggiungi al carrello",
                                fontFamily = fontFamily,
                                fontSize = 18.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.offset(x = 0.dp, y = (-2).dp),
                            )

                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

        }
        if(clicked)  MySnackBar(navController = navController, viewModel = viewModel, nome, id)
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionSelection() {
    var selectedOption by remember {
        mutableStateOf<String>("Opzioni")
    }
    var isExpanded by remember {
        mutableStateOf(false) //default: menù chiuso
    }
    val options = listOf("Opzione 1","Opzione 2","Opzione 3", "Opzione 4")

    val icon = if (isExpanded){
        Icons.Outlined.KeyboardArrowUp
    } else Icons.Outlined.KeyboardArrowDown


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .layoutId("menuOptions")
            .width(160.dp)
            .height(50.dp)
            .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(3.dp))
            .background(Blue20, RoundedCornerShape(3.dp))
    ){
        ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxSize(),
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it },
        ) {
            TextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                textStyle = TextStyle(
                    fontFamily= fontFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp),
                colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Blue20, textColor = Color.White),
                trailingIcon = {
                    Icon(icon , "", tint = Color.White,
                        modifier = Modifier.size(20.dp))
                }, modifier = Modifier.menuAnchor()
            )
            DropdownMenu(
                offset = DpOffset(0.dp, (4).dp),
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier
                    .width(160.dp)
                    .background(Blue20)
                    .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(3.dp))
                    .height(120.dp)
            ) {
                options.forEachIndexed() {index, o ->
                    DropdownMenuItem(
                        modifier = Modifier.padding(vertical = 0.dp),
                        text = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ){
                                Text(
                                    text = o,
                                    color = Color.White,
                                    fontFamily= fontFamily,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 14.sp)
                            }
                        },
                        onClick = {
                            selectedOption = o
                            isExpanded =
                                false
                        })
                    if (index < options.size - 1){
                        Divider(thickness = 1.dp, color = Yellow40)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QtySelection(viewModel: AppViewModel) {
    var selectedQty by remember {
        mutableStateOf<Int>(1)
    }
    var isExpanded by remember {
        mutableStateOf(false) //default: menù chiuso
    }
    val qty = listOf(1,2,3,4)

    val icon = if (isExpanded){
        Icons.Outlined.KeyboardArrowUp
    } else Icons.Outlined.KeyboardArrowDown

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .layoutId("menuQty")
            .width(160.dp)
            .height(50.dp)
            .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(3.dp))
            .background(Blue20, RoundedCornerShape(3.dp))
    ){
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it },
            modifier = Modifier.fillMaxSize()
        ) {
            TextField(
                value = selectedQty.toString(),
                onValueChange = {},
                readOnly = true,
                textStyle = TextStyle(
                    fontFamily= fontFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp),
                colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Blue20, textColor = Color.White),
                trailingIcon = {
                    Icon(icon , "", tint = Color.White,
                        modifier = Modifier.size(20.dp))
                }, modifier = Modifier.menuAnchor()
            )
            DropdownMenu(
                offset = DpOffset(0.dp, (4).dp),
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier
                    .width(160.dp)
                    .background(Blue20, RoundedCornerShape(3.dp))
                    .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(3.dp))
                    .height(120.dp) )
            {
                qty.forEachIndexed() {index, q ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ){
                                Text(
                                    text = q.toString(),
                                    color = Color.White,
                                    fontFamily= fontFamily,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 14.sp)
                            }
                        },
                        onClick = {
                            selectedQty = q
                            viewModel.quantita = q
                            isExpanded = false

                        })
                    if (index < qty.size - 1){
                        Divider(thickness = 1.dp, color = Yellow40)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySnackBar(navController: NavController, viewModel: AppViewModel, nome: String, id: String) {

    val message = "Aggiunto al carrello"
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = message)
    {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Long
            )
        }
    }

    SnackbarHost(hostState = snackbarHostState) {

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize().padding(start = 8.dp, end = 8.dp, top = 90.dp, bottom = 100.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Yellow80, RoundedCornerShape(3.dp))
                    .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(3.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Text(
                    text = message,
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                /*Button(
                    shape = RoundedCornerShape(15.dp),
                    onClick = { navController.navigate(Screen.Cart.route) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                )*/

                    Text(
                        modifier = Modifier.clickable { navController.navigate(Screen.Cart.route) },
                        text = "Vai al carrello",
                        fontFamily = fontFamily,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textDecoration = TextDecoration.Underline
                    )


            }
        }

    }
}

/*
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun DemoSnackBar() {
        val snackBarHostState = remember {
            SnackbarHostState()
        }
        val coroutineScope = rememberCoroutineScope()
        Scaffold(content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    coroutineScope.launch {

                        val snackBarResult = snackBarHostState.showSnackbar(
                            message = "Snackbar is here",
                            actionLabel = "Undo",
                            duration = SnackbarDuration.Short
                        )
                        when (snackBarResult) {
                            SnackbarResult.ActionPerformed -> {
                                Log.d("Snackbar", "Action Performed")
                            }
                            else -> {
                                Log.d("Snackbar", "Snackbar dismissed")
                            }
                        }
                    }

                }) {
                    Text(text = "Show Snack Bar", color = Color.White)
                }
            }
        }, snackbarHost = { SnackbarHost(hostState = snackBarHostState) })
    }
*/
