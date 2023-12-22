package com.example.caloriestracker.ui.user.dashboard.addFood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriestracker.data.model.firestore.FoodItem
import com.example.caloriestracker.databinding.ActivityAddFoodUserBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class AddFoodActivityUser : AppCompatActivity() {

    private lateinit var binding: ActivityAddFoodUserBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var foodArrayList: ArrayList<FoodItem>
    private lateinit var foodAdapterUser: FoodAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFoodUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerMakananUser
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        foodArrayList = arrayListOf()
        foodAdapterUser = FoodAdapter(foodArrayList)

        recyclerView.adapter = foodAdapterUser

        eventChangeListener()

        with(binding) {
            btnCustomMakanan.setOnClickListener {
                val intent = Intent(this@AddFoodActivityUser, CustomFoodActivity::class.java)
                startActivity(intent)
            }

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0 || dy < 0) {
                        btnCustomMakanan.visibility = View.GONE
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        btnCustomMakanan.visibility = View.VISIBLE
                    }
                }
            })
        }
    }

    private fun eventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("makanan")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return@addSnapshotListener
                }

                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        foodArrayList.add(dc.document.toObject(FoodItem::class.java))
                    }
                }

                foodAdapterUser.notifyDataSetChanged()
            }
    }

}
