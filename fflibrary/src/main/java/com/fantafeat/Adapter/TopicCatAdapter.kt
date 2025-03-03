package com.batball11.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fantafeat.Model.TopicCatModel
import com.fantafeat.Model.TopicModel
import com.fantafeat.R

class TopicCatAdapter (val context: Context, val list:ArrayList<TopicCatModel>, val listener: onItemClick) :
        RecyclerView.Adapter<TopicCatAdapter.ViewHolder>(){

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val txtCat=itemView.findViewById<TextView>(R.id.txtCat)
        val recyclerGames=itemView.findViewById<RecyclerView>(R.id.recyclerGames)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.games_main_item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.txtCat.setText(list[position].title)
        holder.txtCat.setTextColor(context.resources.getColor(R.color.black_pure))
        holder.txtCat.textSize = 16f
        val typeface: Typeface? = ResourcesCompat.getFont(context, R.font.roboto_bold)
        holder.txtCat.typeface = typeface

        holder.recyclerGames.layoutManager= GridLayoutManager(context,3)
        val topicAdapter= TopicAdapter(context,list[position].list,object :TopicAdapter.onItemClick{
            override fun onClick(model: TopicModel) {
                listener.onClick(model)
            }
        })
        holder.recyclerGames.adapter=topicAdapter
    }

    public interface onItemClick{
        public fun onClick(model: TopicModel)
    }
}