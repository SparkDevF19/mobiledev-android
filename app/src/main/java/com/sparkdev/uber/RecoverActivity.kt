package com.sparkdev.uber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_recover.*
import kotlinx.android.synthetic.main.activity_register.*

class RecoverActivity : AppCompatActivity() {

    //If the user didn't receive the email verification, he can bypass it by going through the
    //password reset process

    private lateinit var auth: FirebaseAuth

    var isPressed: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover)

        val auth = FirebaseAuth.getInstance()

        recoverBackButton.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        recoveryButton.setOnClickListener {
            if (!isPressed) {
                Log.d("myLog", "Button is pressed")
                isPressed = true

                val email = recoveryEmail.text.toString()
                if (email.isEmpty()) {
                    recoveryEmail.error = "Please enter your email"
                    recoveryEmail.requestFocus()
                    isPressed = false
                    return@setOnClickListener
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    recoveryEmail.error = "Please enter a valid email"
                    recoveryEmail.requestFocus()
                    isPressed = false
                    return@setOnClickListener
                }
                recoverProgressBar.visibility = View.VISIBLE

                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("myLog", "success to send the recovery email")
                            Toast.makeText(baseContext, "reset email sent", Toast.LENGTH_SHORT)
                                .show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            Log.d("myLog", "failed to send recovery email")
                            Toast.makeText(
                                baseContext,
                                "Failed to send reset email",
                                Toast.LENGTH_SHORT
                            ).show()
                            recoverProgressBar.visibility = View.GONE
                            isPressed = false
                            return@addOnCompleteListener
                        }
                    }
            }
        }

    }
}
