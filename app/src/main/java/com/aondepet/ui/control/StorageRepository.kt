package com.aondepet.ui.control

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.util.UUID

class StorageRepository {

    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    fun uploadImage(imageUri: Uri, petId: String) {
        val imageRef = storageRef.child("pets/${petId}")
        imageRef.putFile(imageUri)
    }

}