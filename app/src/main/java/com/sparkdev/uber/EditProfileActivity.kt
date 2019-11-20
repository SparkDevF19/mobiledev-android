package com.sparkdev.uber

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        val changeEmailField: EditText = findViewById(R.id.ChangeEmailField)
        changeEmailField.setText("Placeholder Text")
    }
}
