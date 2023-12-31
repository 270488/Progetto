package it.polito.database

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.polito.database.ui.theme.DatabaseTheme
import androidx.compose.material3.Button
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.ktx.storage
import it.polito.database.ui.theme.MainScreen


val database = Firebase.database.reference
val storage= Firebase.storage.reference

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<AppViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this.baseContext)

        super.onCreate(savedInstanceState)
        setContent {
            DatabaseTheme {
                MainScreen(viewModel)
            }
        }
    }
}



/*@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var text=name
    /*myRef.setValue("Hello, World!")
    myRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            val value = dataSnapshot.getValue<String>()
            Log.d(TAG, "Value is: $value")
        }

        override fun onCancelled(error: DatabaseError) {
            // Failed to read value
            Log.w(TAG, "Failed to read value.", error.toException())
        }
    })*/
    Text(
        text = text,
        modifier = modifier
    )
}*/

