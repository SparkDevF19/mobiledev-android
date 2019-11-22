package com.sparkdev.uber

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class ReviewActivity : AppCompatActivity()
{

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var dbReference: DatabaseReference

    private val driver: Driver = Driver("3u85h2tgu3fu",
                                          "jsmit123@fiu.edu",
                                        "John",
                                        "Smith",
                                     "305-123-4567",
                                       "33174",
                                         listOf<Review>(),
                                            40,
                                       8,
                                        10)

    private val user = auth.currentUser

    private var driverRating = 0
    private var tipAmount = 0.0

    private var newDriverScore = 0.0
    private var allReviewScores: List<Int> = emptyList()

    private var isPressed: Boolean = false

    private var driverID: String = ""
    private var userID: String = ""

    private var rawDate: LocalDateTime = LocalDateTime.now()
    private var formattedDate: String = rawDate.format(DateTimeFormatter.ofPattern("M/d/y"))



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.review_activity)


        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        dbReference = database.reference.child("driver")
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

    private fun getInfo()
    {
        userID = user?.uid!!
        driverRating = 5
        tipAmount = 1.00
        newDriverScore = calculateScore(allReviewScores)
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

        dbReference.child("Review").child("Date").setValue(formattedDate)
        dbReference.child("Review").child("Driver_ID").setValue(driverID)
        dbReference.child("Review").child("Score").setValue(driverRating)
        dbReference.child("Review").child("Tip").setValue(tipAmount)
        dbReference.child("Review").child("User_ID").setValue(userID)
    }

    private fun calculateScore(allScores: List<Int>): Double
    {
        var avgScore = 0.0
        var totalScore = 0
        var numOfScores = 0

        allScores.forEach {
            numOfScores++
            totalScore += it
        }

        avgScore = (totalScore/numOfScores).toDouble()

        return avgScore
    }

}
