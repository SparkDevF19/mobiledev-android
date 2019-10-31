package com.sparkdev.uber

import android.media.Image
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val userName: TextView = findViewById(R.id.userName)
        val userEmail: TextView = findViewById(R.id.userEmail)
        val userNumber: TextView = findViewById(R.id.userPhoneNumber)
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
        // do sign out stuff
    }

    private fun editProfile() {
        /*
        @TODO Make new activity for editing profile.
         */
    }

}