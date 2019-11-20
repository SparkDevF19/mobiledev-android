package com.sparkdev.uber

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ReviewActivity : AppCompatActivity()
{

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var dbReference: DatabaseReference


    private var driverRating: Int
        get()
        {
            return driverRating
        }
        set(newDriverRating)
        {
            driverRating = newDriverRating
        }

    private var tipAmount: Double
        get()
        {
            return tipAmount
        }
        set(newTipAmount)
        {
            tipAmount = newTipAmount
        }

    private var isPressed: Boolean = false
    private var driverID: String = ""
    private var userID: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.review_activity)


        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
    }


    private fun verifyCustomTip()
    {
        //val customTip: String = regCustomTip.text.toString()

        //The following check for each box to see if empty and return if so.
        //if (regCustomTip.isEmpty()) {
        //    regCustomTip.error = "Please enter your custom tip amount."
        //    regCustomTip.requestFocus()
        //    isPressed = false
        //}
        //else
        //{
        //    //tipAmount = regCustomTip.text.toString().toDouble()
        //}
    }


    private fun uploadReview()
    {
        //verify that no values are null
        //get the user inputted driverRating and tipAmount values (Note: the rider(s) do not HAVE to review a driver and/or give them a tip)
        //get the driverID, userID, and the date of the current ride
        //confirm with user the rating and tip amount they want to give the driver
        //begin establishing FireBase database connection
        //make a new review in the database for the driver with the above mentioned values
        //verify review data and tip amount were successfully sent
        //output to the user a message thanking them for their review or let them know something went wrong and to try again now or later
    }

}
