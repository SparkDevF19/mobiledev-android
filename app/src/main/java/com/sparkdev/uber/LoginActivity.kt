package com.sparkdev.uber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        regButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        loginButton.setOnClickListener {
            doLogin()
        }
    }

    private fun doLogin(){
        if(loginEmail.text.toString().isEmpty()){        //if email is empty
            loginEmail.error = "Please enter your email"
            loginEmail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(loginEmail.text.toString()).matches()) {    //if the email entered doesn't follow an email pattern
            loginEmail.error = "Please enter a valid email"
            loginEmail.requestFocus()
            return
        }
        if (loginPassword.text.toString().isEmpty()){       //if password is empty
            loginPassword.error = "Please enter you password"
            loginPassword.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(loginEmail.text.toString(), loginPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user
                    updateUI(null)
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI (currentUser : FirebaseUser?){
        if (currentUser != null){
            if (currentUser.isEmailVerified){
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            } else {
                Toast.makeText(
                    baseContext, "Please verify your email", Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(baseContext, "Login failed.",
                Toast.LENGTH_SHORT).show()

        }
    }


}
