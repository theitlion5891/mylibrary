package com.fantafeat.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fantafeat.Model.QuestionModel
import com.fantafeat.R

class HelpdeskQuestionAdapter (val context: Context, val list:ArrayList<QuestionModel>, val listener:onItemClick):RecyclerView.Adapter<HelpdeskQuestionAdapter.ViewHolder>() {


    public interface onItemClick{
        public fun onClick(model: QuestionModel)
    }

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val txtQue=itemView.findViewById<TextView>(R.id.txtQue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.question_item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model=list[position]

        holder.txtQue.text=model.faqQuestion

        holder.txtQue.setOnClickListener { listener.onClick(model) }
    }
}