package com.example.hackmatch.ui.team.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.hackmatch.R
import com.example.hackmatch.data.model.response.User
import com.example.hackmatch.databinding.TeamMemberItemViewBinding

class TeamMemberAdapter(
    private var list : List<User.TeamData>
): Adapter<TeamMemberAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = TeamMemberItemViewBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.team_member_item_view, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curr = list[position]
        holder.binding.apply {
            teamMemberRole.text = if(curr.isLeader) "Leader" else "Member"
            teamMemberName.text = curr.userName
            Glide.with(root).load(curr.userIconUrl).into(teamMemberAvatar)
        }
    }

    fun setData(newList: List<User.TeamData>){
        list = newList
        notifyDataSetChanged()
    }
}