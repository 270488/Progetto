package it.polito.database.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import it.polito.database.AppViewModel
import it.polito.database.R

@Composable
fun ProductScreen(viewModel: AppViewModel) {
    ProductDetail()
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ProductDetail() {
Scaffold (
   /* topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Pagina prodotto",
                    color = Color.Transparent
                )
            },
            modifier = Modifier.background(Color.Black),
            navigationIcon = {
               IconButton(
                   onClick = {  }
               ) {
                   Icon(
                       imageVector = Icons.Rounded.ArrowBack,
                       contentDescription = "Torna indietro",
                       modifier = Modifier
                           .size(40.dp),
                       tint = Color.Black
                   )
               }
            },
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Impostazioni",
                        modifier = Modifier
                            .size(40.dp),
                        tint = Color.Black,
                    )
                }
            }
        )
    } */
){
    ProductContent()
}
}

@Composable
fun ProductContent(
    // product: Screen.Product = mProduct
) {
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
        Image(
        painter = painterResource(id = R.drawable.shaker700ml),
        contentDescription = "Product Image",
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .layoutId("productImage")
            .fillMaxSize()
            .padding(bottom = 300.dp)
    )

        Card(
            shape = RoundedCornerShape(
                topStart = 8.dp,
                topEnd = 8.dp
            ),
            colors = CardDefaults.cardColors(Color.DarkGray),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier
                .layoutId("bgCard")
                .height(400.dp)
                .fillMaxWidth()
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
              CardContent()
            }
        }
        var filledHeart by remember{ mutableStateOf(false) }
        FloatingActionButton(
            onClick = {
                if(filledHeart==false) {
                    filledHeart = true
                }else{
                    filledHeart = false
                }
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
                    contentDescription = "Empty Heart",
                    modifier = Modifier.size(50.dp)
                )}else{
                Image(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = "Filled Heart",
                    modifier = Modifier.size(50.dp)
                )
            }

        }

    }
}

@Composable
fun CardContent(){
    Text(
        text = "Accessori per l'allenamento",
        modifier = Modifier
            .layoutId("productCategory")
            .padding(top = 10.dp, bottom = 16.dp),
        fontSize = 14.sp,
        color = Color.White
    )

    Text(
        text = "Shaker",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .layoutId("productName")
            .padding(top = 30.dp)
    )

    Text(
        text = "36€",
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .layoutId("productPrice")
            .padding(end = 16.dp, top = 25.dp)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 120.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
       // RatingBar()
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 140.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        OptionSelection()
        QtySelection()
    }

    Text(
        text = "Descrizione",
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .layoutId("txtDescriptionTitle")
            .padding(top = 160.dp),
        color = Color.White
    )

    Text(
        text = "Lo shaker proteico permette di mescolare rapidamente e facilmente le proteine in polvere; versare il latte o l'acqua, aggiungere la polvere, chiudere, agitare e la bevanda proteica è subito pronta.\n" +
                "\n" +
                "L'agitatore con setaccio previene la formazione di grumi e residui; lo shaker da palestra presenta inoltre una pratica scala a livelli di 50 ml fino a 700 ml\n" +
                "\n" +
                "Lo shaker proteico IronMaxx con filtro e tappo a vite è particolarmente adatto per i viaggi ed è pratico da portare nella borsa sportiva\n" +
                "\n" +
                "È possibile riempire e pulire con facilità lo shaker per frullati di proteine rimuovendo il grande tappo a vite: l'apertura per bere si trova sotto il tappo a vite più piccolo\n" +
                "\n" +
                "Cos'è incluso: 1 x IronMaxx Protein Shaker da 700 ml - Shaker Stabile con Setaccio per Frullati Proteici - Facile da Pulire - Color Nero Notte",
        modifier = Modifier
            .layoutId("txtDescription")
            .padding(top = 8.dp),
        color = Color.White
    )

    Button(
        onClick = {/*TODO*/},
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .layoutId("btnBuy")
            .padding(top = 32.dp, start = 90.dp, bottom = 16.dp)
            .width(250.dp),
        colors = ButtonDefaults.buttonColors(Color.Yellow),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        )
    ){
        Text(
            text = "Aggiungi al carrello",
            fontSize = 23.sp,
            color = Color.Black
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

    var isHalfStar = (rating % 1) != 0.0
    Row {
        for(index in 1 .. stars){
          /*  Icon(
                imageVector = if(index<= rating){
                        Icons.Rounded.Star
                    } else {
                           if(isHalfStar){
                               Icons.Rounded
                           } else {

                           }
                          },
                modifier = Modifier.clickable { onRatingChange(index.toDouble()) },
                contentDescription = null,
                tint = starsColor,

            ) */
        }
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

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .layoutId("menuOptions")
            .width(160.dp)
            .height(48.dp)
    ){
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it }
        ) {
            TextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.Gray, textColor = Color.White),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                }, modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier
                    .height(100.dp)
                    .background(Color.Gray)
            ) {
                options.forEach { o ->
                    DropdownMenuItem(
                        text = {
                            Text(text = o, color = Color.White)
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
fun QtySelection() {
    var selectedQty by remember {
        mutableStateOf<String>("Quantità")
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
            onExpandedChange = { isExpanded = it }
        ) {
            TextField(
                value = selectedQty,
                onValueChange = {},
                readOnly = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.Gray, textColor = Color.White),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                }, modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier
                    .height(100.dp)
                    .background(Color.Gray)
            ) {
                qty.forEach { q ->
                    DropdownMenuItem(
                        text = {
                            Text(text = q.toString(), color = Color.White)
                        },
                        onClick = {
                            selectedQty = q.toString()
                            isExpanded =
                                false
                        })
                }
            }
        }
    }
}
