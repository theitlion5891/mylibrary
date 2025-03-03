package com.fantafeat.Adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fantafeat.Model.ChatModel
import com.fantafeat.R
import com.fantafeat.util.ConstantUtil
import com.fantafeat.util.CustomUtil

class CallbackChatAdapter (val context:Context, val list:ArrayList<ChatModel>, val listener:onClickAction, var status:String):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class MyHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val txtDate=itemView.findViewById<TextView>(R.id.txtDate)
        val txtMsg=itemView.findViewById<TextView>(R.id.txtMsg)
        val txtViewBtnLbl=itemView.findViewById<TextView>(R.id.txtViewBtnLbl)
        val layViewMedia=itemView.findViewById<LinearLayout>(R.id.layViewMedia)
    }

    inner class OtherHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val txtDate=itemView.findViewById<TextView>(R.id.txtDate)
        val txtMsg=itemView.findViewById<TextView>(R.id.txtMsg)
        val txtUploadBtnLbl=itemView.findViewById<TextView>(R.id.txtUploadBtnLbl)
        val layUploadMedia=itemView.findViewById<LinearLayout>(R.id.layUploadMedia)
    }

    public interface onClickAction{
        public fun onUploadMedia(model: ChatModel)
        public fun onShowMedia(model: ChatModel)
    }

    public fun changeStatus(status: String){
        this.status=status
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (list[position].isMe){
            return 1
        }
        return 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==1){
            return MyHolder(LayoutInflater.from(context).inflate(R.layout.my_chat_item,parent,false))
        }
        return OtherHolder(LayoutInflater.from(context).inflate(R.layout.other_chat_item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model=list[position]
        if (holder is MyHolder){
            holder.txtDate.text = CustomUtil.printDifferenceAgo(model.createAt)
            if (TextUtils.isEmpty(model.message)){
                holder.txtMsg.visibility=View.GONE
            }
           else{
                holder.txtMsg.visibility=View.VISIBLE
                holder.txtMsg.text = model.message
            }

            if (!TextUtils.isEmpty(model.documentsName)){
                holder.layViewMedia.visibility=View.VISIBLE
                val arr=model.documentsName.split(".")
                if (arr.size>=2){
                    if (arr[1].equals("jpg",true) ||
                        arr[1].equals("jpeg",true) ||
                        arr[1].equals("png",true)){
                        holder.txtViewBtnLbl.text="View Photo"
                    }
                    else if (arr[1].equals("mp4",true)){
                        holder.txtViewBtnLbl.text="View Video"
                    }
                }
            }else{
                holder.layViewMedia.visibility=View.GONE
            }

            holder.layViewMedia.setOnClickListener {
                listener.onShowMedia(model)
            }
        }
        else if (holder is OtherHolder){
            holder.txtDate.text = CustomUtil.printDifferenceAgo(model.createAt)
            if (TextUtils.isEmpty(model.message)){
                holder.txtMsg.visibility=View.GONE
            }
            else{
                holder.txtMsg.visibility=View.VISIBLE
                holder.txtMsg.text = model.message
            }
            holder.txtUploadBtnLbl.setText("Upload Media (Up to "+ ConstantUtil.MAX_MEDIA_SIZE_MB+" MB)")
            if (model.allow_doc_upload.equals("yes",true) && !status.equals("close",true)){
                holder.layUploadMedia.visibility=View.VISIBLE
            }else{
                holder.layUploadMedia.visibility=View.GONE
            }

            holder.layUploadMedia.setOnClickListener {
                listener.onUploadMedia(model)
            }
        }
    }
}