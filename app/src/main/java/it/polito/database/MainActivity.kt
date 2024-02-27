package it.polito.database

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.storage.ktx.storage
import it.polito.database.ui.theme.DatabaseTheme
import it.polito.database.ui.theme.MainScreen


val database = Firebase.database.reference
val storage= Firebase.storage.reference

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<AppViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        Firebase.initialize(context = this)
        installSplashScreen()
        super.onCreate(savedInstanceState)


        setContent {
            val navController = rememberNavController()
            DatabaseTheme {
              MainScreen(viewModel)

              //AuthenticationScreen(navController = navController,context = AuthenticationActivity())
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

