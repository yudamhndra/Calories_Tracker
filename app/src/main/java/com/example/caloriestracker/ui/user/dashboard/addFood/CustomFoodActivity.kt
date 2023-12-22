package com.example.caloriestracker.ui.user.dashboard.addFood

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.caloriestracker.databinding.ActivityCustomFoodBinding
import com.example.caloriestracker.data.model.room.UserMenu
import com.example.caloriestracker.MainActivity
import com.google.firebase.auth.FirebaseAuth

class CustomFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomFoodBinding
    private lateinit var makananViewModel: FoodViewModel

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userId = firebaseAuth.currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealTypes = listOf("Makan Pagi", "Makan Siang", "Makan Malam")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mealTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMealType.adapter = adapter

        val itemName = intent.getStringExtra("namaMakanan")
        val calorie = intent.getStringExtra("jmlKaloriMakanan")

        binding.etNamaMakananUser.setText(itemName)
        binding.etJumlahKaloriUser.setText(calorie)

        makananViewModel = ViewModelProvider(this).get(FoodViewModel::class.java)

        binding.btnSaveCustom.setOnClickListener {
            saveMakananToDatabase()
        }
    }

    private fun saveMakananToDatabase() {
        val userID = userId
        val foodName = binding.etNamaMakananUser.text.toString()
        val type = binding.spinnerMealType.selectedItem.toString()
        val serving = binding.etJumlahKaloriUser.text.toString().toInt()
        val foodCaloriePer100g = binding.etJumlahKaloriUser.text.toString().toInt()
        val foodCalorie = (serving / 100) * foodCaloriePer100g

        val makanan = userId?.let {
            UserMenu(
                userId = it,
                foodName = foodName,
                foodCalorie = foodCalorie,
                serving = serving,
                type = type
            )
        }

        if (makanan != null) {
            makananViewModel.addMakanan(makanan)
        }

        Toast.makeText(this, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
    }
}
