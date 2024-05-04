package com.example.hackmatch.ui.invitation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.hackmatch.R
import com.example.hackmatch.data.model.response.User
import com.example.hackmatch.databinding.InvitationsItemViewBinding

class InvitationAdapter(
    private var list: List<User.TeamData>
): Adapter<InvitationAdapter.InvitationViewHolder>() {

    inner class InvitationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = InvitationsItemViewBinding.bind(itemView)
    }

    private var onAcceptClick : (User.TeamData) -> Unit = {}
    private var onRejectClick : (User.TeamData) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvitationViewHolder {
        val layout =LayoutInflater.from(parent.context).inflate(R.layout.invitations_item_view,parent,false)
        return InvitationViewHolder(layout)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: InvitationViewHolder, position: Int) {
        val curr = list[position]
        holder.binding.apply {
            Glide.with(root)
                .load(curr.userIconUrl)
                .into(teamImage)
            teamName.text = curr.teamName
            teamMembers.text = curr.teamSize.toString()
            btnAccept.setOnClickListener {
                onAcceptClick(curr)
            }
            btnClose.setOnClickListener {
                onRejectClick(curr)
            }
        }
    }

    fun setOnAcceptClick(action: (User.TeamData) -> Unit){
        onAcceptClick = action
    }

    fun setOnRejectClick(action: (User.TeamData) -> Unit){
        onRejectClick = action
    }

    fun setData(list: List<User.TeamData>){
        this.list = list
        notifyDataSetChanged()
    }
}