package com.example.mapapp.firebase

import com.google.firebase.firestore.CollectionReference

class Repository {

    fun addUser(user: User) {
        database.collection("users")
            .add(
                hasMapOf(
                    "userName" to user.UserName,
                    "age" to user.age,
                    "profilePicture" to user.picture
                )

            )
    }
    fun editUser(editUser: User){
            database.collection("users")
            .document(editUser.id)
            .set(
                hashMapOf(
                    "userName" to editUser.UserName,
                    "age" to editUser.age,
                    "profilePicture" to editUser.picture
                )
            )

    }


    fun deleteUser(userId: String){
        database.collection("users")
            .document(userId)
            .delete()
    }

    fun getUsers(): CollectionReference {
        return database.collection("users")
    }

    fun getUser(userId: String): DocumentReference {
        return database.collection("users")
            .document(userId)
    }

}