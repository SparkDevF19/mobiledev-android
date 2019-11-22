package com.sparkdev.uber

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sparkdev.uber.DashboardActivity

class SettingsActivity : AppCompatActivity() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val user: FirebaseUser? = auth.currentUser
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val dbReference: DatabaseReference = database.reference.child("User")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val userName: TextView = findViewById(R.id.userName)
        val userEmail: TextView = findViewById(R.id.userEmail)
//        val userNumber: TextView = findViewById(R.id.userPhoneNumber)
        val signOutButton: Button = findViewById(R.id.SignOutButton)
        val editProfileButton: Button = findViewById(R.id.EditProfButton)
        val homeAddress: EditText = findViewById(R.id.HomeAddress)
        val workAddress: EditText = findViewById(R.id.WorkAddress)
        val avatar = findViewById<ImageView>(R.id.ProfileAvatar)


        signOutButton.setOnClickListener {
            signOut()
        }
        editProfileButton.setOnClickListener {
            editProfile()
        }
    }

    private fun signOut() {
        Toast.makeText(this, "Signed Out!", Toast.LENGTH_LONG)
        auth.signOut()
        val intent: Intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun editProfile() {
        val intent: Intent = Intent(this, EditProfileActivity::class.java)
        startActivity(intent)
        finish()
    }
}