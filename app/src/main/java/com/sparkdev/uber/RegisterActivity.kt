package com.sparkdev.uber

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var dbReference: DatabaseReference

    var isPressed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        dbReference = database.reference.child("User")

        regSend.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        regButton.setOnClickListener {
            if (!isPressed) {
                Log.d("myLog", "Button is pressed")
                isPressed = true

                createNewAccount()
            }
        }
        registerBackButton.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    //password needs to be larger than 6 characters
    private fun createNewAccount() {
        val email: String = regEmail.text.toString()
        val password: String = regPassword.text.toString()
        val name: String = regName.text.toString()
        val lastName: String = regLast.text.toString()
        val postal: String = regPostal.text.toString()

        //The following check for each box to see if empty and return if so.
        if (email.isEmpty()) {
            regEmail.error = "Please enter your Email"
            regEmail.requestFocus()
            isPressed = false
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {       //if the input doesn't follow an email pattern
            regEmail.error = "Please enter a valid email"
            regEmail.requestFocus()
            isPressed = false
        }else if (password.isEmpty()) {
            regPassword.error = "Please enter your Password"
            regPassword.requestFocus()
            isPressed = false
        }else if (name.isEmpty()) {
            regEmail.error = "Please enter your Name"
            regEmail.requestFocus()
            isPressed = false
        }else if (lastName.isEmpty()) {
            regName.error = "Please enter your Last Name"
            regName.requestFocus()
            isPressed = false
        } else if (postal.isEmpty()) {
            regPostal.error = "Please enter your Postal Code"
            regPostal.requestFocus()
            isPressed = false
        } else {

        regProgressBar.visibility= View.VISIBLE

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val user = auth.currentUser

                    //send a verification email
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Toast.makeText(baseContext, "Email Verification failed.", Toast.LENGTH_SHORT).show()
                                isPressed = false
                            }
                        }

                    //send the user input to the database
                    val userBD = dbReference.child(user?.uid!!)

                    userBD.child("Email").setValue(email)
                    userBD.child("Name").setValue(name)
                    userBD.child("LastName").setValue(lastName)
                    userBD.child("PostalCode").setValue(postal)


                    //start Login activity
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()

                } else {
                    Toast.makeText(baseContext, "Registration failed. Try again later", Toast.LENGTH_SHORT).show()
                    regProgressBar.visibility = View.GONE
                    isPressed = false
                }
            }
        }
    }
}
