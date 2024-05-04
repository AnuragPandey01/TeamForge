package com.example.hackmatch.ui.team.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.hackmatch.R
import com.example.hackmatch.data.model.response.User
import com.example.hackmatch.databinding.TeamItemViewBinding

class TeamAdapter(
    private var teams: List<User.TeamData>
) : Adapter<TeamAdapter.ViewHolder>(){

    private var onItemClick: ((User.TeamData) -> Unit) = {}

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = TeamItemViewBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.team_item_view, parent, false))
    }

    override fun getItemCount() = teams.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            teamName.text = teams[position].teamName
            teamMembers.text = "${teams[position].teamSize} members"
            Glide.with(root)
                .load("https://api.dicebear.com/8.x/shapes/jpeg?seed=${teams[position].teamName}")
                .into(teamImage)

            root.setOnClickListener {
                onItemClick(teams[position])
            }
        }
    }

    fun setData(teams: List<User.TeamData>){
        this.teams = teams
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClick: (User.TeamData) -> Unit){
        this.onItemClick = onItemClick
    }
}