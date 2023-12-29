package it.polito.database.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.annotations.concurrent.Background
import it.polito.database.HomePage



   @SuppressLint("NotConstructor")
   @OptIn(ExperimentalMaterial3Api::class)
   @Composable
    fun NotificationsScreen(){
       Box(
           modifier = Modifier.fillMaxSize(),
           contentAlignment = Alignment.Center
       ){
           Text(text = "Notifiche",
               color = MaterialTheme.colorScheme.primary,
               fontSize = MaterialTheme.typography.headlineLarge.fontSize,
               fontWeight = FontWeight.Bold
               )


       }


}
