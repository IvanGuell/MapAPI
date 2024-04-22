package com.example.mapapp.firebase

import MapMarkers
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class Repository {

    private val database = FirebaseFirestore.getInstance()

    fun addMarker(marker: MapMarkers) {
        database.collection("markers")
            .add(
                hashMapOf(
                    "id" to marker.id,
                    "position" to hashMapOf(
                        "latitude" to marker.position.latitude,
                        "longitude" to marker.position.longitude
                    ),
                    "title" to marker.title,
                    "snippet" to marker.snippet,
                    "photo" to marker.photo
                )
            )
    }
    fun editMarker(editMarker: MapMarkers){
        database.collection("markers")
            .document(editMarker.id)
            .set(
                hashMapOf(
                    "uid" to editMarker.id,
                    "position" to hashMapOf(
                        "latitude" to editMarker.position.latitude,
                        "longitude" to editMarker.position.longitude
                    ),
                    "title" to editMarker.title,
                    "snippet" to editMarker.snippet,
                    "photo" to editMarker.photo
                )
            )
    }

    fun deleteMarker(markerId: String){
        database.collection("markers")
            .document(markerId)
            .delete()
    }

    fun getMarkers(): CollectionReference {
        return database.collection("markers")
    }

    fun getMarker(markerId: String): DocumentReference {
        return database.collection("markers")
            .document(markerId)
    }
}

