package com.fantafeat.Adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.CalendarContract.Colors
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fantafeat.Model.NewOfferModel
import com.fantafeat.R
import com.fantafeat.util.LogUtil

class NewOfferAdapter(val context:Context, var list:ArrayList<NewOfferModel>, val listener:onOfferItemClick):
        RecyclerView.Adapter<NewOfferAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txtTitle=itemView.findViewById<TextView>(R.id.txtTitle)
        val txtOffer=itemView.findViewById<TextView>(R.id.txtOffer)
        val txtOfferCode=itemView.findViewById<TextView>(R.id.txtOfferCode)
        val txtApply=itemView.findViewById<TextView>(R.id.txtApply)
        val txtDetails=itemView.findViewById<TextView>(R.id.txtDetails)
    }

    interface onOfferItemClick{
        public fun onClick(model: NewOfferModel)
    }

    fun updateList(list:ArrayList<NewOfferModel>){
        this.list= ArrayList()
        this.list.addAll(list)
        LogUtil.e("resp","OfferList: "+this.list.size)
       // notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.new_offer_item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model=list[position]

        if (model.isApply){
            holder.txtApply.text = "✓✓ Applied"
            //holder.txtApply.setBackgroundColor(context.resources.getColor(R.color.lightest_green))
            holder.txtApply.setBackgroundResource(R.drawable.light_green_bottom_radius)
            holder.txtApply.setTextColor(context.resources.getColor(R.color.green_pure))
        }else{
            holder.txtApply.text = "Tap to apply"
            //holder.txtApply.setBackgroundColor(context.resources.getColor(R.color.trans_blue))
            holder.txtApply.setBackgroundResource(R.drawable.light_yellow_bottom_radius)
            holder.txtApply.setTextColor(context.resources.getColor(R.color.colorPrimary))
        }

        holder.txtTitle.text = model.offerTitle
        holder.txtOffer.text = model.offerDesc
        holder.txtOfferCode.text = model.couponCode

        holder.txtApply.setOnClickListener {
            listener.onClick(model)
        }

        holder.txtDetails.setOnClickListener {
            showDetailDialog(model)
        }

    }

    private fun showDetailDialog(model: NewOfferModel){
        val dialog= Dialog(context)
        dialog.setContentView(R.layout.new_offer_detail_dialog)

        val imgClose=dialog.findViewById<ImageView>(R.id.imgClose)
        val txtPromoCode=dialog.findViewById<TextView>(R.id.txtPromoCode)
        val txtCondition=dialog.findViewById<TextView>(R.id.txtCondition)

        txtPromoCode.text = model.couponCode
        txtCondition.text = model.offerTnc

        imgClose.setOnClickListener { dialog.dismiss() }

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

}