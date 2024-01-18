package it.polito.database.screens

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.decode.ImageSource
import it.polito.database.AppViewModel

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
    topBar = {
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
    }
){
    ProductContent()
}
}

@Composable
fun ProductContent() {
}
