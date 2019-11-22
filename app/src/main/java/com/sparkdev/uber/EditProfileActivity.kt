package com.sparkdev.uber

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditProfileActivity : AppCompatActivity() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val user: FirebaseUser? = auth.currentUser
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val dbReference: DatabaseReference = database.reference.child("User")

    override fun onCreate(savedInstanceState: Bundle?) {
        val userBD = dbReference.child(user?.uid!!)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        val changeEmailField: EditText = findViewById(R.id.ChangeEmailField)
        changeEmailField.setText(userBD.child("Email").toString())
        val changeNameField: EditText = findViewById(R.id.changeNameField)
        changeNameField.setText(userBD.child("First_Name").toString() + userBD.child("Last_Name").toString())
    }
}
