package com.batball11.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.fantafeat.Model.TopicModel
import com.fantafeat.R

class TopicAdapter (val context:Context, val list:ArrayList<TopicModel>, val listener:onItemClick): RecyclerView.Adapter<TopicAdapter.ViewHolder>() {

    inner class ViewHolder (itemView:View):RecyclerView.ViewHolder(itemView){
        val txtTopicTitle=itemView.findViewById<TextView>(R.id.txtTopicTitle)
        val cardTopic=itemView.findViewById<LinearLayout>(R.id.cardTopic)
    }

    public interface onItemClick{
        public fun onClick(model: TopicModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.topic_item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model=list[position]

        /*if (model.isSelected){
            holder.cardTopic.setBackgroundColor(context.resources.getColor(R.color.colorSelectTransfer))
        }
        else{
            holder.cardTopic.setBackgroundColor(context.resources.getColor(R.color.white))
        }*/
        holder.txtTopicTitle.text=model.topicName

        holder.cardTopic.setOnClickListener {
           /* list.forEachIndexed { index, topicModel ->
                topicModel.isSelected=false
            }
            model.isSelected=true
            notifyDataSetChanged()*/
            listener.onClick(model)

        }
    }
}