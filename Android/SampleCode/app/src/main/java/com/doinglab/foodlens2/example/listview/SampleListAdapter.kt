package com.doinglab.foodlens2.example.listview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.doinglab.foodlens2.example.R

class SampleListAdapter  : ListAdapter<ListItem, SampleListAdapter.ViewHolder>(DiffCallback())  {

    inner class ViewHolder(val layout: View) : RecyclerView.ViewHolder(layout) {
        var ivFoodIcon = layout.findViewById<ImageView>(R.id.img_food)
        var tvFoodName = layout.findViewById<TextView>(R.id.tv_foodname)
        var tvFoodPosition = layout.findViewById<TextView>(R.id.tv_food_position)
        var tvFoodNutrtionInfo = layout.findViewById<TextView>(R.id.tv_food_nutrition_info)

        fun bind(item:ListItem) {
            item.icon?.let {
                ivFoodIcon.setImageDrawable(item.icon)
            }
            tvFoodName.text = item.title
            tvFoodPosition.text = item.foodPosition
            tvFoodNutrtionInfo.text = item.foodNutrition
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = getItem(position)
        holder.bind(item)
    }

    class DiffCallback : DiffUtil.ItemCallback<ListItem>() {
        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return oldItem == newItem
        }
    }
}