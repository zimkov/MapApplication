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
import com.example.retrofitproject.R

class CommentAdapter : ListAdapter<Comment, CommentAdapter.Holder>(Comparator()){
    class Holder(view: View) : RecyclerView.ViewHolder(view){
        private val listItem = view.findViewById<LinearLayout>(R.id.layoutCommentParent)
        fun bind(comment: Comment){
            listItem.findViewById<TextView>(R.id.usernameComment).text = comment.user.name
            listItem.findViewById<TextView>(R.id.textComment).text = comment.text
            listItem.findViewById<RatingBar>(R.id.ratingComment).rating = comment.rate.toFloat()
            listItem.findViewById<RatingBar>(R.id.ratingComment).invalidate()
            listItem.findViewById<TextView>(R.id.textComment).invalidate()
            listItem.findViewById<TextView>(R.id.usernameComment).invalidate()
        }
    }

    class Comparator : DiffUtil.ItemCallback<Comment>(){
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false)
        return  Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}