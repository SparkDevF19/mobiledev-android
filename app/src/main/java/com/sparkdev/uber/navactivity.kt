package com.sparkdev.uber

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout

class navactivity : AppCompatActivity() {

    var drawer: DrawerLayout = DrawerLayout(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navactivity)

        drawer = findViewById(R.id.drawer_layout)
    }
}
