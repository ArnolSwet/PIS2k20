package com.example.appEasyHealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.easyhealth.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
class MainTrainer : AppCompatActivity() {
    //atributs
    /* txtname, llistaclients, txtnumclass, txtdisponib*/
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var txtName:  TextView
    private lateinit var txtmssgs:  TextView
    private lateinit var txtnumclas: TextView
    private lateinit var txtnumclients: TextView
    private val clientList: ArrayList<Client> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("User")
        auth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = auth.currentUser
        val userDB = databaseReference.child(user?.uid!!)
        setContentView(R.layout.activity_main_trainer)

        txtmssgs = findViewById(R.id.txtCliHeightNum)
        txtnumclas = findViewById(R.id.txtCliSubscriptionNum)
        txtnumclients = findViewById(R.id.txtCliWeightNum)
        var client1 = Client("erni", "theboss" ,"ernietheboss@gmail.com", "322", "Premium", "", "Client.", "", "", "")
        var client2 = Client("viki", "elvikingo" ,"veikingo@gmail.com", "32322", "Standard", "", "Client.", "", "", "")
        clientList.plusAssign(client1)
        clientList.plusAssign(client2)

        userDB.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext,"Fail to read data", Toast.LENGTH_SHORT).show()

            }

            override fun onDataChange(p0: DataSnapshot) {
                val client  = p0.getValue(Client::class.java)!!
                userDB.child("foodlist").setValue(foodList)

                if (client?.name != null) {
                    txtName.text = client.name
                }
                if (client?.weight != null) {
                    txtWeight.text = client.weight.toString()
                }
                if (client?.height != null) {
                    txtHeight.text = client.height.toString()
                }
                if (client?.suscription != null) {
                    txtSuscription.text = client.suscription
                }
            }
        })
    }








































    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_trainer)
    }

    fun goClient(view: View){
        val intent = Intent(this, ClientForTrainer::class.java)
        startActivity(intent)
    }

    fun yourClasses(view: View){
        val intent = Intent(this, YourClasses::class.java)
        startActivity(intent)
    }

    fun goBack(view: View){
        finish()
    }

    fun goSettings(view: View) {
        val intent = Intent(this, SettingsTrainer::class.java)
        startActivity(intent)
    }
}
