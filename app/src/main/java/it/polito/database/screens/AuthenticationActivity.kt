package it.polito.database.screens

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.browser.trusted.Token
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth


class AuthenticationActivity: Activity(){
    private lateinit var auth: FirebaseAuth
    public override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize Firebase Auth

        super.onCreate(savedInstanceState)
        // Check if user is signed in (non-null) and update UI accordingly.
        auth = Firebase.auth
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    // [END on_start_check_user]
    private fun startSignIn() {
        val customToken: Token? = Token.create(packageName, packageManager)!!
        // Initiate sign in with custom token
        // [START sign_in_custom]
        customToken?.let {
            auth.signInWithCustomToken(it.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCustomToken:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCustomToken:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        updateUI(null)
                    }
                }
        }
        // [END sign_in_custom]
    }

    private fun updateUI(user: FirebaseUser?) {
    }

    companion object {
        private const val TAG = "AuthenticationActivity"
    }


}