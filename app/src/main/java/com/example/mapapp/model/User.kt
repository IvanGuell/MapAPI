package com.example.mapapp.model

data class User (

    var userId: String? = null,
    var userName: String,
    var age: Int,
    var profilePicture: String? = null


){
    constructor(): this(null, "", 0, null)
}