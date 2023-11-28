package it.polito.database

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.polito.database.ui.theme.DatabaseTheme
class MainActivity : ComponentActivity() {
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {

        FirebaseApp.initializeApp(this.baseContext)



        super.onCreate(savedInstanceState)
        setContent {
            DatabaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    writeNewUser("elsa96", "Elsa", "elsa@gmail.com")
                    Greeting("Android")
                }
            }
        }
    }
}

val database = Firebase.database.reference

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var text=""
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
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DatabaseTheme {
        Greeting("Android")
    }
}
fun writeNewUser(userId: String, name: String, email: String) {
    val user = User(userId, name, email)

    database.child("users").child(userId).setValue(user)
    database.child("users").child("anna00").child("name").get().addOnSuccessListener {
        Log.i("firebase", "Got value ${it.value}")
    }.addOnFailureListener{
        Log.e("firebase", "Error getting data", it)
    }
}