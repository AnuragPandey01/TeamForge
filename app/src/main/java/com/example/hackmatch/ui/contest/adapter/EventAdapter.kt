package com.example.hackmatch.ui.contest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hackmatch.R
import com.example.hackmatch.data.model.response.EventData
import com.example.hackmatch.databinding.EventItemViewBinding

class EventAdapter(
    private var list: List<EventData>
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.event_item_view,parent,false)
        return EventViewHolder(layout)
    }
    private var onShare : (EventData) -> Unit = {}
    private var onMore : (EventData) -> Unit = {}
    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val curr = list[position]
        holder.binding.apply {
            eventTitle.text = curr.title
            location.text = curr.location
            startsChip.text = "Starts ${curr.startDate}"
            modeChip.text = if(curr.isOnline) "ONLINE" else "OFFLINE"
            eventType.text = curr.eventType
            btnShare.setOnClickListener {
                onShare(curr)
            }
            btnMore.setOnClickListener {
                onMore(curr)
            }

        }
    }

    inner class EventViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = EventItemViewBinding.bind(itemView)
    }

    fun addList(
        newList:List<EventData>
    ){
        list = newList;
        notifyDataSetChanged()
    }

    fun addOnShare(onShare: (EventData) -> Unit){
        this.onShare = onShare
    }

    fun addOnMore(onMore: (EventData) -> Unit){
        this.onMore = onMore
    }

}