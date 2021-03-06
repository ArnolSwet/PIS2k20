package com.example.appEasyHealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import appEasyHealth.Logica.FoodForTrainerAdapter
import appEasyHealth.Logica.Usuari
import com.example.easyhealth.R
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList

class ClientForTrainer : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var txtName:  TextView
    private lateinit var txtSuscription: TextView
    private lateinit var txtLocation: TextView
    private lateinit var txtWeight: TextView
    private lateinit var txtHeight: TextView
    private lateinit var txtEmpty: TextView
    private lateinit var recycler: RecyclerView
    private lateinit var clientID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_for_trainer)
        val client = getIntent().getSerializableExtra("Client").toString()
        clientID = client.subSequence(client.indexOf("#")+1 ,client.length).toString()

        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("User")

        txtName = findViewById(R.id.txtClientName)
        txtSuscription = findViewById(R.id.txtCliSubscriptionNum)
        txtLocation = findViewById(R.id.txtCliLocation)
        txtWeight = findViewById(R.id.txtCliWeightNum)
        txtHeight = findViewById(R.id.txtCliHeightNum)
        txtEmpty = findViewById(R.id.nameEmpty)
        recycler = findViewById(R.id.recycler_view_food_trainer)
        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formatted = currentDate.format(formatter)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext,"Fail to read data", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot : DataSnapshot in p0.children) {
                    val user = snapshot.getValue(Usuari::class.java)
                    if (user!!.type == "Client") {
                        val client = snapshot.getValue(Client::class.java)
                        if (client?.id == clientID) {
                            clientID = snapshot.key!!.toString()
                            if (client.name != null) {
                                txtName.text = client.name
                            }
                            if (client.weight != null) {
                                txtWeight.text = client.weight.toString()
                            }
                            if (client.height != null) {
                                txtHeight.text = client.height.toString()
                            }
                            if (client.location != null) {
                                txtLocation.text = client.location.toString()
                            }
                            if (client.foodlist.isNullOrEmpty()) {
                                recycler.setVisibility(View.GONE);
                                txtEmpty.setVisibility(View.VISIBLE);
                            } else {
                                recycler.setVisibility(View.VISIBLE);
                                txtEmpty.setVisibility(View.GONE);
                                var todayFood = client.getFoodListonDay(formatted)
                                var foodNames = ArrayList<String>()
                                var foodTypes = ArrayList<String>()
                                var foodCalories = ArrayList<String>()

                                for (food in todayFood) {
                                    foodNames.add(food.name.toString())
                                    foodTypes.add(food.tipus.toString())
                                    foodCalories.add(food.calories.toString())
                                }
                                initReyclerView(client, foodNames, foodTypes, foodCalories)
                            }
                            if (client?.suscription != null) {
                                txtSuscription.text = client.suscription
                            }
                        }
                    }
                }
            }
        })
    }

    fun initReyclerView(
        client: Client,
        foodNames: ArrayList<String>,
        foodTypes: ArrayList<String>,
        foodCalories: ArrayList<String>
    ) {
        var managerLayout = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        var recyclerView =findViewById<RecyclerView>(R.id.recycler_view_food_trainer)
        recyclerView.layoutManager = managerLayout
        var adapter = FoodForTrainerAdapter(this, foodNames, foodTypes, foodCalories)
        recyclerView.adapter = adapter
    }

    fun chatClient(view: View) {
        val intent = Intent(this, Chat::class.java)
        intent.putExtra("DestinyID",clientID)
        ContextCompat.startActivity(this,intent, Bundle())
    }

    fun goBack(view: View) {
        finish()
    }


}
