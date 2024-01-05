package it.polito.database.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.components.Lazy
import it.polito.database.AppViewModel


@Composable
    fun CartScreen(viewModel: AppViewModel) {
        Cart(viewModel)
    }
@Composable
fun Cart(viewModel: AppViewModel, modifier: Modifier = Modifier
    .padding(top = 74.dp)
    .padding(bottom = 74.dp)){

    LazyColumn(modifier = modifier.padding(vertical = 4.dp)){

    }

}

@Composable
fun CartItem(nome: String, prezzo: Int, quantita: Int){

}
