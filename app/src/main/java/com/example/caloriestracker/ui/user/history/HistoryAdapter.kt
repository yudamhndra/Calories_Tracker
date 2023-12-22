package com.example.caloriestracker.ui.user.history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriestracker.R
import com.example.caloriestracker.data.model.room.UserMenu

class HistoryAdapter(context: Context) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var foodList = emptyList<UserMenu>()
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.food_item_history, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = foodList[position]
        holder.itemView.findViewById<TextView>(R.id.txt_foodname).text = currentItem.foodName
        holder.itemView.findViewById<TextView>(R.id.txt_Calories).text = currentItem.foodCalorie.toString()
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    fun setData(makananUser: List<UserMenu>){
        this.foodList = makananUser
        notifyDataSetChanged()

    }


}