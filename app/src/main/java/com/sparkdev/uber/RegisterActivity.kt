package com.sparkdev.uber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
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
        val password: String = regPassword.text.toString().trim()
        val name: String = regName.text.toString()
        val lastName: String = regLast.text.toString()
        val phone: String = regPhone.text.toString()
        val postal: String = regPostal.text.toString()


        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || lastName.isEmpty() || phone.isEmpty() || postal.isEmpty()){  //checks for empty input
            Toast.makeText(baseContext, "Please, Fill in all the boxes", Toast.LENGTH_SHORT).show()
            isPressed = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {      //checks if the email is valid
            regEmail.error = "Please enter a valid email"
            regEmail.requestFocus()
            isPressed = false
        } else if (password.length < 8 || password.length > 16) {       //checks for password length
            regPassword.error = "Please, enter 8 to 16 characters"
            regPassword.requestFocus()
            isPressed = false
        } else if (!isPasswordValid(password)){
            regPassword.error = "At least: 1 capital letter, 1 number and one symbol"
            regPassword.requestFocus()
            isPressed = false
        }
        else {

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
                    userBD.child("First_Name").setValue(name)
                    userBD.child("Last_Name").setValue(lastName)
                    userBD.child("Phone_Number").setValue(phone)
                    userBD.child("Postal_Code").setValue(postal)


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

    private fun isPasswordValid(password: String?) : Boolean {
        password?.let {
            val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
            val passwordMatcher = Regex(passwordPattern)

            return passwordMatcher.find(password) != null
        } ?: return false
    }


}
