package com.sparkdev.uber

data class Driver(val DriverID: String,
                  val Email: String,
                  val First_Name: String,
                  val Phone_Number: String,
                  val Postal_Code: String,
                  val Reviews: List<Review>,
                  val Score: Int,
                  val Total_Reviews: Int,
                  val Total_Rides: Int)