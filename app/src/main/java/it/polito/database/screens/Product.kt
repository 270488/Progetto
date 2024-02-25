package it.polito.database.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.database.AppViewModel
import it.polito.database.FindUrl
import it.polito.database.database
import it.polito.database.ui.theme.Blue20
import it.polito.database.ui.theme.Blue40
import it.polito.database.ui.theme.Red20
import it.polito.database.ui.theme.Red40
import it.polito.database.ui.theme.Screen
import it.polito.database.ui.theme.Yellow40
import it.polito.database.ui.theme.fontFamily

@SuppressLint("RememberReturnType")

@Composable
fun ProductScreen(viewModel: AppViewModel, navController: NavController) {

    ProductDetail(viewModel, navController)
}

//@Preview
@Composable
fun ProductDetail(viewModel: AppViewModel, navController: NavController) {
    var id = viewModel.uid
    var quantita = viewModel.quantita
    var prod=viewModel.prodottoSelezionato

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
    Log.d("nome: ",nome)
    Log.d("prezzo: ",prezzo.toString())
    Log.d("url: ",url)
    ConstraintLayout(
        constraintSet = ConstraintSet(
            content =
                """
                    {
                        bgCard: {
                          bottom: ['parent', 'bottom']
                        },
                        productImage: {
                          top: ['bgCard', 'top'],
                          bottom: ['bgCard', 'top'],
                          right: ['bgCard', 'right']
                        },
                        btnHeart: {
                          bottom: ['bgCard', 'top'],
                          right: ['parent', 'right'],
                        }
                    }
                """.trimIndent()
        ),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AsyncImage(
            model=url,
        contentDescription = "Product Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .layoutId("productImage")
            .fillMaxWidth()
            .padding(bottom = 150.dp)
            .verticalScroll(rememberScrollState())

        )
        /*Image(
        painter = painterResource(id = R.drawable.shaker700ml),
        contentDescription = "Product Image",
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .layoutId("productImage")
            .fillMaxSize()
            .padding(bottom = 300.dp)
    )*/

        Card(
            shape = RoundedCornerShape(
                topStart = 8.dp,
                topEnd = 8.dp
            ),
            colors = CardDefaults.cardColors(Blue40),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier
                .layoutId("bgCard")
                .height(500.dp)
                .fillMaxWidth()
                .background(Blue40)
                .verticalScroll(rememberScrollState())
        ) {
            ConstraintLayout(
                constraintSet = ConstraintSet(
                    content =
                    """
                    {
                       productCategory: {
                          top: ['parent', 'top'],
                          left: ['parent', 'left']
                        },
                        productTitle: {
                          top: ['productCategory', 'bottom'],
                          left: ['parent', 'left']
                        },
                        productPrice: {
                          top: ['parent', 'top'],
                          right: ['parent', 'right'],
                        },
                        ratings: {
                          top: ['productTitle', 'top'],
                          left: ['parent', 'left']
                        },
                        menuOptions: {
                          top: ['ratings', 'top'],
                          left: ['parent', 'left']
                        },
                        menuQty: {
                          top: ['menuOptions', 'top'],
                          left: ['menuOptions', 'right']
                        }, 
                        txtDescriptionTitle: {
                          top: ['menuOptions', 'bottom'],
                          left: ['parent', 'left'],
                        },
                        txtDescription: {
                          top: ['txtDescriptionTitle', 'bottom'],
                          left: ['parent', 'left'],
                          right: ['parent', 'right']
                        },
                        btnBuy: {
                          top: ['txtDescription', 'bottom'],
                          right: ['parent', 'right']
                        }
                    }
                """.trimIndent()
                ),
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                        end = 10.dp
                    )
            ) {
              CardContent(nome=nome, prezzo = prezzo, categoria=categoria, sottocategoria=sottocategoria, descrizione=descrizione, id=id, viewModel = viewModel, navController = navController)
            }
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

        FloatingActionButton(
            onClick = {
                filledHeart = !filledHeart
                if(filledHeart){
                    aggiungiPreferito(prod = nome, id = id)
                }
                else
                    eliminaPreferito(prod = nome, id = id)
            },
            containerColor = Color.Transparent,
            modifier = Modifier
                .layoutId("btnHeart")
                .padding(top = 32.dp, start = 320.dp, bottom = 8.dp, end = 16.dp),
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp
            )
        ) {
            if(filledHeart==false) {
                Image(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    colorFilter = ColorFilter.tint(Red20),
                    contentDescription = "Empty Heart",
                    modifier = Modifier.size(45.dp)
                )}else{
                Image(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = "Filled Heart",
                    colorFilter = ColorFilter.tint(Red40),
                    modifier = Modifier.size(45.dp)
                )
            }
        }
    }
}

@Composable
fun CardContent(nome: String, prezzo: Double, descrizione:String, categoria: String, sottocategoria: String, id: String, viewModel: AppViewModel, navController: NavController){
    Text(
        text = categoria+"/ "+sottocategoria,
        modifier = Modifier
            .layoutId("productCategory")
            .padding(top = 10.dp, bottom = 16.dp),
        fontSize = 14.sp,
        fontStyle = FontStyle.Italic,
        fontFamily= fontFamily,
        color = Color.White
    )

    Text(
        text = nome,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamily,
        color = Color.White,
        modifier = Modifier
            .layoutId("productName")
            .padding(top = 30.dp)
            .width(210.dp)
    )

    Text(
        text = "%.2f".format(prezzo)+"€",
        fontSize = 42.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamily,
        color = Color.White,
        modifier = Modifier
            .layoutId("productPrice")
            .padding(end = 8.dp, top = 25.dp)
    )
    //val quantita = 0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 35.dp)
            .layoutId("ratings"),
        verticalArrangement = Arrangement.SpaceBetween
    ){

        Text(text = "★★★★★", fontSize = 32.sp) //temporaneo
      // RatingBar()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            OptionSelection()
            QtySelection(viewModel)
        }
    }
    //Spacer(modifier = Modifier.height(15.dp))
    Text(
        text = "Descrizione",
        fontWeight = FontWeight.SemiBold,
        fontFamily = fontFamily,
        modifier = Modifier
            .layoutId("txtDescriptionTitle")
            .padding(top = 140.dp),
        color = Color.White
    )

    Card(
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .layoutId("txtDescription")
            .padding(top = 8.dp),
        colors = CardDefaults.cardColors(Blue20),
        border = BorderStroke(1.dp, Yellow40)
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
    Button(
        onClick = {
            Log.d("quantità selezionata: ", viewModel.quantita.toString())
            aggiungiAlCarrello(item = nome, id = id, qty = viewModel.quantita, viewModel)
            navController.navigate(Screen.Cart.route)
        },
        shape = RoundedCornerShape(3.dp),
        modifier = Modifier
            .layoutId("btnBuy"),
            //.padding(top = 16.dp, bottom = 16.dp),
        colors = ButtonDefaults.buttonColors(Yellow40),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        )
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

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    stars: Int = 5,
    starsColor: Color = Color.Yellow,
    onRatingChange: (Double) -> Unit
    ) {
     /*TODO*/
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

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .layoutId("menuOptions")
            .width(160.dp)
            .height(48.dp)
    ){
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it },
            modifier = Modifier
                .border(BorderStroke(1.dp, Yellow40), RoundedCornerShape(4.dp))
                .background(Blue20)
        ) {
            TextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                textStyle = TextStyle(fontFamily= fontFamily, fontWeight = FontWeight.SemiBold),
                colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Blue20, textColor = Color.White),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                }, modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier
                    .height(100.dp)
                    .background(Blue20)
                    .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(4.dp))
            ) {
                options.forEach { o ->
                    DropdownMenuItem(
                        text = {
                            Text(text = o, color = Color.White, fontFamily= fontFamily, fontWeight = FontWeight.SemiBold)
                        },
                        onClick = {
                            selectedOption = o
                            isExpanded =
                                false
                        })
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
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .layoutId("menuQty")
            .width(160.dp)
            .height(48.dp)
    ){
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it },
            modifier = Modifier
                .border(BorderStroke(1.dp, Yellow40), RoundedCornerShape(4.dp))
                .background(Blue20)
        ) {
            TextField(
                value = selectedQty.toString(),
                onValueChange = {},
                readOnly = true,
                textStyle = TextStyle(fontFamily= fontFamily, fontWeight = FontWeight.SemiBold),
                colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Blue20, textColor = Color.White),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                }, modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier
                    .height(100.dp)
                    .background(Blue20)
                    .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(4.dp))

            ) {
                qty.forEach { q ->
                    DropdownMenuItem(
                        text = {
                            Text(text = q.toString(), color = Color.White, fontFamily= fontFamily, fontWeight = FontWeight.SemiBold)
                        },
                        onClick = {
                            selectedQty = q
                            viewModel.quantita = q
                            isExpanded = false

                        })
                }
            }
        }
    }
}

