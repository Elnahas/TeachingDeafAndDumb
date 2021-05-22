package com.teachingthedeafanddumb.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.teachingthedeafanddumb.data.model.UserModel
import com.teachingthedeafanddumb.other.Constants.FIELD_PHONE
import com.teachingthedeafanddumb.other.Constants.FOLDER_PROFILE_IMAGE
import com.teachingthedeafanddumb.other.Constants.REF_USERS
import com.teachingthedeafanddumb.other.await
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.ArrayList
import javax.inject.Inject

class UserRepository @Inject constructor() {

    var refUser = FirebaseDatabase.getInstance().getReference(REF_USERS)

    suspend fun registerUser(userModel: UserModel, uri: Uri?) =
        run {

            uri?.let {
                val path = FirebaseStorage.getInstance().reference.child(FOLDER_PROFILE_IMAGE)
                    .child(userModel.id!!)
                val downloadUrl = path
                    .putFile(uri).addOnCanceledListener {  }
                    .await() // await() instead of snapshot
                    .storage
                    .downloadUrl.addOnCanceledListener { }
                    .await()// await the url
                    .toString()

                userModel.photoUrl = downloadUrl
            }


            refUser.child(
                userModel.id!!
            )
                .setValue(userModel)
                .addOnSuccessListener { }
                .addOnFailureListener {
                }.await()


            userModel
        }


    suspend fun isUserExists(phone :String):DataSnapshot =run{

        refUser.orderByChild(FIELD_PHONE).equalTo(phone).await()

    }

}