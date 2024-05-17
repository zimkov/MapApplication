package com.example.retrofitproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitproject.DataClasses.Comment
import com.example.retrofitproject.DataClasses.SocialMapObject
import com.example.retrofitproject.R

class MapObjectAdapter: ListAdapter<SocialMapObject, MapObjectAdapter.Holder>(Comparator()) {
    class Holder(view: View) : RecyclerView.ViewHolder(view){
        private val listItem = view.findViewById<LinearLayout>(R.id.layoutMapObjectParent)
        fun bind(socialMapObject: SocialMapObject){
            listItem.findViewById<TextView>(R.id.nameMapObject).text = socialMapObject.display_name
            listItem.findViewById<TextView>(R.id.adressMapObject).text = socialMapObject.adress
            listItem.findViewById<TextView>(R.id.nameMapObject).invalidate()
            listItem.findViewById<TextView>(R.id.adressMapObject).invalidate()
        }
    }

    class Comparator : DiffUtil.ItemCallback<SocialMapObject>(){
        override fun areItemsTheSame(oldItem: SocialMapObject, newItem: SocialMapObject): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SocialMapObject, newItem: SocialMapObject): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mapobject_item, parent, false)
        return  Holder(view)
    }

    override fun onBindViewHolder(holder: MapObjectAdapter.Holder, position: Int) {
        holder.bind(getItem(position))
    }



}