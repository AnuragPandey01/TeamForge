package com.example.hackmatch.ui.user_search.adapter

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hackmatch.R
import com.example.hackmatch.data.model.response.User
import com.example.hackmatch.databinding.UserSearchItemViewBinding

class UserSearchAdapter (
    private var users: List<User>
): RecyclerView.Adapter<UserSearchAdapter.ViewHolder>(){

    private var onItemClick: ((User) -> Unit) = {}

    inner class ViewHolder(itemview: View): RecyclerView.ViewHolder(itemview) {
        val binding = UserSearchItemViewBinding.bind(itemview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_search_item_view,parent,false)

        return ViewHolder(layout)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curr= users[position]
        holder.binding.apply{

            Glide.with(root)
                .load(curr.iconUrl)
                .into(userIcon)

            userName.text = curr.name
            userTitle.text = curr.role
            userSkills.text = "Sills: ${curr.tags.map{it.name}.joinToString(", ",limit = 3)}"
            root.setOnClickListener {
                onItemClick(curr)
            }
        }
    }

    fun addSearchResult(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (User) -> Unit) {
        onItemClick = listener
    }
}