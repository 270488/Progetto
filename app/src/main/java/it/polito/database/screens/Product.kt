package it.polito.database.screens

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import android.media.Image
import android.widget.ImageView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.widget.ConstraintLayout
import coil.decode.ImageSource
import it.polito.database.AppViewModel
import it.polito.database.FindUrl
import it.polito.database.R
import it.polito.database.ui.theme.Screen

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
                        productCategory: {
                          top: ['parent', 'top'],
                          left: ['parent', 'left']
                        },
                        productTitle: {
                          top: ['productCategory', 'bottom'],
                          left: ['parent', 'left']
                        },
                        productPrice: {
                          bottom: ['bgCard', 'top'],
                          left: ['parent', 'left']
                        },
                        productPriceLabel: {
                        bottom: ['productCategory', 'top'],
                        left: ['parent', 'left']
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
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .layoutId("productImage")
            .fillMaxSize()
            .padding(top = 80.dp, bottom = 300.dp)
    )
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
                .padding(top = 320.dp, start = 320.dp, bottom = 8.dp),
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

        Card(
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier
                .layoutId("bgCard")
                .height(380.dp)
                .fillMaxWidth()
        ) {
            ConstraintLayout(
                constraintSet = ConstraintSet(
                    content =
                    """
                    {
                        txtColor: {
                          top: ['parent', 'top'],
                          left: ['parent', 'left']
                        },
                        txtSize: {
                          top: ['txtColor', 'top'],
                          left: ['txtColor', 'right']
                        },
                        txtSizeValue: {
                          top: ['txtSize', 'bottom'],
                          left: ['txtSize', 'left']
                        },
                        txtDescription: {
                          top: ['txtSizeValue', 'top'],
                          left: ['parent', 'left'],
                          right: ['parent', 'right']
                        },
                        btnMinus: {
                          top: ['txtDescription', 'bottom'],
                          left: ['parent', 'left']
                        },
                        txtQtd: {
                          top: ['btnMinus', 'top'],
                          bottom: ['btnMinus','bottom'],
                          left: ['btnMinus', 'right']
                        },
                        btnPlus: {
                          top: ['txtDescription', 'bottom'],
                          left: ['txtQtd', 'right']
                        },
                        btnHeart: {
                          top: ['btnPlus', 'top'],
                          bottom: ['btnPlus', 'bottom'],
                          right: ['parent', 'right']
                        },
                        btnAddToCard: {
                          top: ['btnMinus', 'bottom'],
                          left: ['parent', 'left']
                        },
                        btnBuy: {
                          top: ['btnMinus', 'bottom'],
                          left: ['btnAddToCart', 'right'],
                          right: ['parent', 'right']
                        },
                        color1: {
                          top: ['txtColor', 'bottom'],
                          left: ['parent', 'left']
                        },
                        color2: {
                          top: ['txtColor', 'bottom'],
                          left: ['color1', 'right']
                        },
                        color3: {
                          top: ['txtColor', 'bottom'],
                          left: ['color2', 'right']
                        },
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
        Text(
            text = "Accessori per l'allenamento",
            modifier = Modifier
                .layoutId("productCategory")
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            fontSize = TextUnit(20F, TextUnitType.Sp),
            color = Color.White
            )

        Text(
            text = "Shaker per proteine 700ml",
            fontSize = TextUnit(30F, TextUnitType.Sp),
            color = Color.White,
            modifier = Modifier
                .layoutId("productName")
                .padding(start = 16.dp, top = 40.dp)
        )

        Text(
            text = "Price",
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier
                .layoutId("productPriceLabel")
                .padding(start = 16.dp)
        )

        Text(
            text = "36€",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .layoutId("productPrice")
                .padding(start = 16.dp, bottom = 8.dp, top = 8.dp)
        )
    }
}

@Composable
fun CardContent(){
    Text(
        text = "Color",
        color = Color.Gray,
        modifier = Modifier
            .layoutId("txtColor")
            .padding(top = 64.dp)
    )

    Button(
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .layoutId("color1")
            .padding(top = 8.dp)
            .width(30.dp)
            .height(30.dp),
        colors = ButtonDefaults.buttonColors(Color.Black),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        )
    ) {
    }

    Button(
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .layoutId("color2")
            .padding(top = 8.dp, start = 20.dp)
            .width(30.dp)
            .height(30.dp),
        colors = ButtonDefaults.buttonColors(Color.White),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        )
    ) {

    }
    OutlinedButton(
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .layoutId("btnMinus")
            .padding(top = 32.dp, end = 16.dp)
    ) {
        Text(
            text = "-",
            fontSize = 23.sp,
            color = Color.Black
        )
    }

    Text(
        text = "01",
        fontSize = 23.sp,
        color = Color.DarkGray,
        modifier = Modifier
            .layoutId("txtQtd")
            .padding(top = 32.dp, start = 80.dp)
    )
    IconButton(
        onClick = { /*TODO*/ },
        colors = IconButtonDefaults.iconButtonColors(Color.Black),
        modifier = Modifier
            .layoutId("btnPlus")
            .padding(top = 32.dp, start = 120.dp)
    ) {
       androidx.compose.material3.Icon(
           imageVector = Icons.Default.Add,
           contentDescription = "Aggiungi quantità",
           tint = Color.White
       )
    }

    OutlinedButton(
        onClick = { /*TODO*/ },
        border = BorderStroke(
            width = 1.dp,
            color = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .layoutId("btnAddtoCart")
            .padding(top = 32.dp)
    ) {
        Image(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Add Cart",
            modifier = Modifier.size(25.dp)
        )
    }
}

