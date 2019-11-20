package com.sparkdev.uber

data class UserModel(var email: String?,
                     var password: String,
                     var phoneNumber: String,
                     var homeAddress: Address?)