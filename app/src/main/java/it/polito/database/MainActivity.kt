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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.firebase.BuildConfig
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.ktx.initialize
import com.google.firebase.storage.ktx.storage
import it.polito.database.screens.AuthenticationActivity
import it.polito.database.screens.AuthenticationScreen
import it.polito.database.screens.CategoryScreen
import it.polito.database.screens.FavoritesScreen
import it.polito.database.screens.ProductListScreen
import it.polito.database.ui.theme.MainScreen
import it.polito.database.ui.theme.NavGraph
import it.polito.database.ui.theme.Screen


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

