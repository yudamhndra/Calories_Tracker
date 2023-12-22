package com.example.caloriestracker.ui.user.dashboard.addFood

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriestracker.R
import com.example.caloriestracker.data.model.firestore.FoodItem

class FoodAdapter (private val makananList : ArrayList<FoodItem>) : RecyclerView.Adapter<FoodAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FoodAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.food_item_user, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodAdapter.ViewHolder, position: Int) {
        val makanan: FoodItem = makananList[position]

        holder.itemName.text = makanan.nama
        holder.calories.text = "${makanan.kalori} cal/servings"
        holder.itemView.findViewById<ImageButton>(R.id.btnAddMakananUser).setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, CustomFoodActivity::class.java)

            intent.putExtra("namaMakanan", makanan.nama)
            intent.putExtra("jmlKaloriMakanan", makanan.kalori)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return makananList.size
    }

    public class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val itemName : TextView = itemView.findViewById(R.id.txt_foodname)
        val calories : TextView = itemView.findViewById(R.id.txt_Calories)
    }
}