package com.example.mapapp.model

data class User (

    var userId: String? = null,
    var userName: String,
    var email: String?,
    var password: String



){
    constructor(): this(null, "", "", "")
}