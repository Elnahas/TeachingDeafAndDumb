package com.teachingthedeafanddumb.other


import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


suspend fun Query.await(): DataSnapshot = suspendCoroutine { cont ->
    addListenerForSingleValueEvent(object : ValueEventListener {

        override fun onCancelled(error: DatabaseError) {
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot != null) {
                cont.resume(snapshot)
            } else {
                cont.resumeWithException(Exception("Null data"))
            }
        }
    })
}