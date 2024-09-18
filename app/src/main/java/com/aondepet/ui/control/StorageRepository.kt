package com.aondepet.ui.control

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage

class StorageRepository {

    private val storage = FirebaseStorage.getInstance()

    fun uploadImage(imageUri: Uri, petId: String): Task<Uri> {
        val storageRef = storage.reference
        val imageRef = storageRef.child("images/$petId")
        return imageRef.putFile(imageUri).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            imageRef.downloadUrl
        }
    }

    fun getImageUri(petId: String): Task<Uri> {
        val storageRef = storage.reference
        val imageRef = storageRef.child("images/$petId")
        return imageRef.downloadUrl
    }

    fun updateImage(imageUri: Uri, petId: String): Task<Uri> {
        val storageRef = storage.reference
        val imageRef = storageRef.child("images/$petId")
        return imageRef.putFile(imageUri).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            imageRef.downloadUrl


        }
    }

}