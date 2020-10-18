package com.example.rankanime.modele

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class FirebaseHelper {

    var firebaseAuth : FirebaseAuth
    var currentUser : FirebaseUser
    var reference : DatabaseReference
    var totalSize : Int = 0

    constructor() {
        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("animes"+currentUser.uid)
        getNbElementDataBase()
    }

    fun addItemToFirebase(item : RvItem) {

        if(item.rang.toInt() == 0) { item.rang = "1" }

        if(item.rang.toInt() > (totalSize)){
            item.rang = (totalSize + 1).toString();
            reference.push().setValue(item)
        }
        else if(totalSize > 0)
        {
            reorganisedRankFromPosAndInsert(item)
        }
    }

    fun reorganisedRankFromPosAndInsert(item : RvItem) {
        var increment = 1

        reference.orderByChild("rang").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (datas in dataSnapshot.children) {
                        val key = datas.key

                        if(increment >= item.rang.toInt())
                            reference.child(key!!).child("rang").setValue((increment + 1).toString())

                        increment++
                    }

                    reference.push().setValue(item)
                }
            }
        })
    }

    fun reorganisedRank() {
        var increment = 1

        reference.orderByChild("rang").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (datas in dataSnapshot.children) {
                        val key = datas.key
                        reference.child(key!!).child("rang").setValue((increment).toString())
                        increment++
                    }
                }
            }
        })
    }

    fun removeItem(position : Int)
    {
        reference.orderByChild("rang").equalTo((position+1).toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    throw error.toException()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (datas in dataSnapshot.children) {
                            val key = datas.key
                            reference.child(key!!).removeValue()
                        }

                        Log.d("Algo", "TotalSize : " + totalSize)

                        if(totalSize > 1)
                            reorganisedRank()
                    }
                }
            })


    }

    fun getNbElementDataBase() {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                totalSize = dataSnapshot.childrenCount.toInt()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException() // don't ignore errors
            }
        })
    }
}