package com.sparkdev.uber

import java.util.*

data class Review(val Date: Date,
                  val Driver_ID: String,
                  val Score: Int,
                  val Tip: Double,
                  val User_ID: String)