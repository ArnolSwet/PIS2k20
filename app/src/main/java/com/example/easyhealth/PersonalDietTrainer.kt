package com.example.easyhealth
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class PersonalDietTrainer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_diet_trainer)
    }

    fun goBack(view: View) {
        finish()
    }
    fun addDiet(view: View) {
        val intent = Intent(this, MakeADiet::class.java)
        startActivity(intent)
    }
}
