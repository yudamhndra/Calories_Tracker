package com.example.caloriestracker.ui.admin.addFood

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.caloriestracker.R
import com.example.caloriestracker.data.model.FoodItem



class FoodAdapter : ListAdapter<FoodItem, FoodAdapter.ViewHolder>(FoodItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_item_admin, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodItem = getItem(position)
        holder.bind(foodItem)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val foodNameTextView: TextView = itemView.findViewById(R.id.txt_foodname)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.txt_DeskripsiMakanan)

        fun bind(foodItem: FoodItem) {
            foodNameTextView.text = foodItem.nama
            descriptionTextView.text = "${foodItem.kalori} cal/servings"
        }
    }

    private class FoodItemDiffCallback : DiffUtil.ItemCallback<FoodItem>() {
        override fun areItemsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
            return oldItem.nama == newItem.nama
        }

        override fun areContentsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
            return oldItem == newItem
        }
    }
}

