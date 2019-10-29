package com.sparkdev.uber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        rRegButton.setOnClickListener {
            registerUser()
        }
        rLoginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    private fun registerUser(){
        if(rLoginEmail.text.toString().isEmpty()){        //if email is empty
            rLoginEmail.error = "Please enter your email"
            rLoginEmail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(rLoginEmail.text.toString()).matches()) {       //if the input doesn't follow an email pattern
            rLoginEmail.error = "Please enter a valid email"
            rLoginEmail.requestFocus()
            return
        }
        if (rLoginPassword.text.toString().isEmpty()){      //if password is empty
            rLoginPassword.error = "Please enter you password"
            rLoginPassword.requestFocus()
            return
        }

        //I want to work on creating a more advanced password pattern detection than just checking if empty

        auth.createUserWithEmailAndPassword(rLoginEmail.text.toString(), rLoginPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val user = auth.currentUser
                    user?.sendEmailVerification()     //this function sends a verification email
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this, LoginActivity::class.java))    //redirects the user to the login page
                                finish()                                                                //only after the verification email was sent
                            }
                        }
                } else {
                    Toast.makeText(baseContext, "Registration failed. Try again later",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

}

