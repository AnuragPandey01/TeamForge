package com.example.hackmatch.ui.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hackmatch.R
import com.example.hackmatch.data.model.response.User
import com.example.hackmatch.databinding.ExperienceCardBinding

class ExperienceAdapter(
    private var experienceList : List<User.ExperienceData>
) : Adapter<ExperienceAdapter.ExperienceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.experience_card,parent,false)
        return ExperienceViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {
        holder.binding.apply {
            experienceTitle.text = experienceList[position].title
            experienceDate.text = "${experienceList[position].startDate}-${experienceList[position].endDate}"
            experienceLink.text= experienceList[position].link
            experienceDescription.text = experienceList[position].description
        }
    }

    override fun getItemCount() = experienceList.size

    inner class ExperienceViewHolder(itemview: View): ViewHolder(itemview){
        val binding = ExperienceCardBinding.bind(itemview)
    }

    fun addData(
        data: User.ExperienceData
    ){
        experienceList = experienceList.toMutableList().apply { add(data) }
        this.notifyItemInserted(experienceList.lastIndex)
    }

    fun setData(
        data: List<User.ExperienceData>
    ){
        experienceList = data
        this.notifyDataSetChanged()
    }
}