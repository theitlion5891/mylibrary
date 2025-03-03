package com.fantafeat.Adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.fantafeat.Model.MyTickets
import com.fantafeat.R
import com.fantafeat.util.CustomUtil

class MyTicketsAdapter (val context:Context, val list:ArrayList<MyTickets>, val listener:onItemClick):RecyclerView.Adapter<MyTicketsAdapter.ViewHolder>() {

    public interface onItemClick{
        public fun onClick(model: MyTickets)
    }

    inner class ViewHolder (itemView:View):RecyclerView.ViewHolder(itemView){
        val txtTitle=itemView.findViewById<TextView>(R.id.txtTitle)
        val txtDate=itemView.findViewById<TextView>(R.id.txtDate)
        val txtStatus=itemView.findViewById<TextView>(R.id.txtStatus)
        val txtId=itemView.findViewById<TextView>(R.id.txtId)
        val cardMain=itemView.findViewById<CardView>(R.id.cardMain)
        val imgInfo=itemView.findViewById<ImageView>(R.id.imgInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.my_tickets_item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model=list[position]

        if (TextUtils.isEmpty(model.topicName)) {
            holder.txtTitle.text = model.ticketId
            holder.txtId.visibility=View.INVISIBLE
        }
        else {
            holder.txtTitle.text = model.topicName
            holder.txtId.text="#"+model.ticketId
        }

        holder.txtDate.text= CustomUtil.dateConvertWithFormat(model.createAt,"dd MMM, yyyy hh:mm a")


        if (model.ticketStatus.equals("open",true)){
            holder.txtStatus.setBackgroundResource(R.drawable.opinio_yes)
            holder.txtStatus.setTextColor(context.resources.getColor(R.color.green_pure))
            holder.imgInfo.setImageResource(R.drawable.open_ticket)
            holder.txtStatus.text="OPEN"
        }
        else  if (model.ticketStatus.equals("close",true)){
            holder.txtStatus.setBackgroundResource(R.drawable.gray_bg_light_circular)
            holder.txtStatus.setTextColor(context.resources.getColor(R.color.gray_444444))
            holder.imgInfo.setImageResource(R.drawable.cloed_ticket)
            holder.txtStatus.text="CLOSED"
        }
        else  {
            holder.txtStatus.setBackgroundResource(R.drawable.blue_bg_circular)
            holder.txtStatus.setTextColor(context.resources.getColor(R.color.blue))
            holder.imgInfo.setImageResource(R.drawable.inreview_ticekt)
            holder.txtStatus.text="IN REVIEW"
        }

        holder.cardMain.setOnClickListener {
            listener.onClick(model)
        }

    }
}