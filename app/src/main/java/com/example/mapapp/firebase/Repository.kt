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
                    "mid" to marker.id,
                    "uid" to marker.uid,
                    "lat" to marker.lat,
                    "lng" to marker.lng,
                    "title" to marker.title,
                    "snippet" to marker.snippet,
                    "photo" to marker.photo
                )
            )
    }
    fun editMarker(editMarker: MapMarkers){
        database.collection("markers")
            .document(editMarker.uid)
            .set(
                hashMapOf(
                    "uid" to editMarker.uid,
                    "lat" to editMarker.lat,
                    "lng" to editMarker.lng,
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

