package com.sparkdev.uber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
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


        loginSend.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        loginButton.setOnClickListener{
            doLogin()
        }

        loginRecover.setOnClickListener {
            startActivity(Intent(this, RecoverActivity::class.java))
            finish()
        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }

    }

    private fun doLogin(){
        //checks for empty or invalid input
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

        //Signs the user in
        loginProgressBar.visibility= View.VISIBLE

        auth.signInWithEmailAndPassword(loginEmail.text.toString(), loginPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Log.d("MyLog", "success with log in")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    loginPassword.error = "Wrong password/email"
                    loginPassword.requestFocus()
                    loginRecover.visibility = View.VISIBLE
                    loginProgressBar.visibility= View.GONE
                }
            }
    }

    private fun updateUI (currentUser : FirebaseUser?){
        if (currentUser != null){
            if (currentUser.isEmailVerified){
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            } else {
                Toast.makeText(baseContext, "Please verify your email", Toast.LENGTH_LONG).show()
                loginRecover.visibility = View.VISIBLE
            }
        } else {
            Toast.makeText(baseContext, "Login failed.", Toast.LENGTH_SHORT).show()
            loginProgressBar.visibility= View.GONE
        }
    }

}
