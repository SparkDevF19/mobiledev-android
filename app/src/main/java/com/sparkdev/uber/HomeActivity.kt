package com.sparkdev.uber

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.decodeStream
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import kotlinx.android.synthetic.main.car_item.view.*
import java.io.IOException


class HomeActivity : AppCompatActivity() {

    val carList : ArrayList<String> = arrayListOf<String>()
    val carListImg : ArrayList<Drawable> = arrayListOf<Drawable>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private lateinit var recyclerViewAdapter : RecyclerView.Adapter<*>



    override fun onCreate(savedInstanceState: Bundle?) {

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://androidium.org"))
        startActivity(intent)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        carList.add("Carpool")
        carList.add("Economy")
        carList.add("Large Vehicle")
        carList.add("Premium")

        val resources = this.resources
        carListImg.add(resources.getDrawable(R.drawable.pool))
        carListImg.add(resources.getDrawable(R.drawable.sedan))
        carListImg.add(resources.getDrawable(R.drawable.suv))
        carListImg.add(resources.getDrawable(R.drawable.premium))


        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewAdapter = CarRecyclerViewAdapter(carListImg = this.carListImg, carList = this.carList)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {

            layoutManager = linearLayoutManager
            adapter = recyclerViewAdapter

        }
    }

}
