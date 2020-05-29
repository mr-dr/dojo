package com.dojo.lit.util

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import android.util.Log
import com.dojo.lit.lit.model.TransactionResponse
import com.google.firebase.database.*

/**
 * This class is responsible with -
 * 1. firebase realtime db
 * 2. firebase APIs
 * **/
object FirebaseUtils {
    private val TAG = "FirebaseUtils"
    private val database: FirebaseDatabase
    private val myRef: DatabaseReference
    private val realtimeDbListeners = ArrayList<FirebaseRealtimeDbListener>()

    init {
        database = Firebase.database
        // fixme rishabh - needs to be set on join room
        myRef = database.getReference("r64GfCEqv8ThuMay28202017:10:061590685806445")
        addListenersToRealtimeDb()
    }

    private fun addListenersToRealtimeDb() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue()
                Log.d(TAG, "Value is: $value")
                realtimeDbListeners.forEach {
                    it.onDataChange(
                        GsonUtil.fromJson(GsonUtil.toJson(value), TransactionResponse::class.java)) // Gson().fromJson(Gson().toJson(value), TransactionResponse::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                val exception = error.toException()
                Log.w(TAG, "Failed to read value.", exception)
                realtimeDbListeners.forEach {
                    it.onCancelled(exception)
                }
            }
        })
    }

    // each game service (eg. Lit) will implement FirebaseRealtimeDbListener & subscribe to this func
    fun subscribeToFirebaseRealtimeDb(listener: FirebaseRealtimeDbListener) {
        if (!realtimeDbListeners.contains(listener)) {
            realtimeDbListeners.add(listener)
        }
    }

    fun unsubscribeToFirebaseRealtimeDb(listener: FirebaseRealtimeDbListener) {
        if (realtimeDbListeners.contains(listener)) {
            realtimeDbListeners.remove(listener)
        }
    }

}

abstract class FirebaseRealtimeDbListener {
    abstract fun onDataChange(value: TransactionResponse?)
    abstract fun onCancelled(exception: DatabaseException)
}
