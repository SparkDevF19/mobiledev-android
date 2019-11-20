package com.sparkdev.uber

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

class NavigationActivity : AppCompatActivity() {

    private lateinit var mDrawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)

        mDrawerLayout = findViewById(R.id.drawer_layout)
        mDrawerLayout.openDrawer(GravityCompat.START)
    }
}
