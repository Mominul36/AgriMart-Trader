package com.example.agrimarttrader.Class

import com.example.agrimarttrader.Model.Trader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MyClass {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()



    fun getKey(): String {
        val uniqueKey = database.reference.push().key ?: return "0"

        var key = uniqueKey.hashCode().toString()
        key = key.replace("-","")

        return key
    }


    fun getCurrentTrader(onComplete: (Trader?) -> Unit) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            var email = currentUser.email.toString()

            email = email.replace("+88","")
            email = email.replace("@gmail.com","")


            val traderRef: DatabaseReference = database.getReference("Trader").child(email!!)

            traderRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot!=null){
                        val trader = snapshot.getValue(Trader::class.java)
                        onComplete(trader)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    onComplete(null)
                }
            })
        } else {
            onComplete(null)
        }
    }





    fun fetchAllData(tableName: String, onDataChange: (DataSnapshot?) -> Unit, onCancelled: (DatabaseError) -> Unit) {

        val tableReference: DatabaseReference = database.getReference(tableName)
        tableReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    onDataChange(snapshot)
                } else {
                    onDataChange(null)
                }            }

            override fun onCancelled(error: DatabaseError) {
                onCancelled(error)
            }
        })
    }








    fun fetchOrderByChild(
        tableName: String,
        columnName: String,
        columnValue: String,
        onDataChange: (DataSnapshot?) -> Unit,
        onCancelled: (DatabaseError) -> Unit) {

        val tableReference: DatabaseReference = database.getReference(tableName)
        tableReference.orderByChild(columnName).equalTo(columnValue)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        onDataChange(snapshot)
                    } else {
                        onDataChange(null)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    onCancelled(error)
                }
            })
    }





    fun <T> fetchSingleData(
        tableName: String,
        id: String,
        modelClass: Class<T>,
        onDataChange: (T?) -> Unit,
        onCancelled: (DatabaseError) -> Unit) {

        val tableReference: DatabaseReference = database.getReference(tableName)

        tableReference.child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val data: T? = snapshot.getValue(modelClass)
                    onDataChange(data)
                } else {
                    onDataChange(null)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                onCancelled(error)
            }
        })
    }




    fun <T> addData(tableName: String, id: String, model: T, onComplete: (Boolean) -> Unit) {
        val tableReference: DatabaseReference = database.getReference(tableName)

        tableReference.child(id).setValue(model)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { error ->
                onComplete(false)
            }
    }




    fun deleteDataById(tableName: String, id: String, onComplete: (Boolean) -> Unit) {
        val tableReference: DatabaseReference = database.getReference(tableName)

        tableReference.child(id).removeValue()
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { error ->
                onComplete(false)
            }
    }




    fun updateDataById(tableName: String, id: String, updates: Map<String, Any>, onComplete: (Boolean) -> Unit) {
        val tableReference: DatabaseReference = database.getReference(tableName)

        tableReference.child(id).updateChildren(updates)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { error ->
                onComplete(false)
            }
    }














}


