package com.fantafeat.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Model.UpdateModel;
import com.fantafeat.Model.WithdrawListModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.CustomUtil;

import java.util.ArrayList;

public class WithdrawListAdapter extends RecyclerView.Adapter<WithdrawListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<WithdrawListModel> list;

    public WithdrawListAdapter(Context context, ArrayList<WithdrawListModel> list) {
        this.context = context;
        this.list = list;
    }

    public void updateData(ArrayList<WithdrawListModel> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.withdraw_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WithdrawListModel model=list.get(position);

        String rs=context.getResources().getString(R.string.rs);
        holder.txtWithdrawCharge.setText(rs+model.getChargAmt());
        holder.txtWithdrawReqAmt.setText(rs+model.getAmount());
        holder.txtCreditAmt.setText(rs+model.getWdFinalAmt());
        holder.txtAmtTds.setText(rs+model.getWdBfrTdsAmt());
        holder.txtTds.setText(rs+model.getTdsAmt());
        holder.txtTransactionId.setText("#"+model.getTransId());
        holder.txtCreateAt.setText(CustomUtil.dateTimeConvert(model.getCreateAt()));
        //holder.txtMsg.setText(model.getDesc());

        holder.layExSetDate.setVisibility(View.GONE);

        if (TextUtils.isEmpty(model.getDesc())){
            holder.txtMsg.setVisibility(View.GONE);
        }else {
            holder.txtMsg.setVisibility(View.VISIBLE);
            holder.txtMsg.setText(model.getDesc());
        }

        if (model.getStatus().equalsIgnoreCase("pending")){
            holder.txtStatus.setBackgroundResource(R.drawable.opinion_blue);
            holder.txtStatus.setTextColor(context.getResources().getColor(R.color.blue1));
            holder.txtStatus.setText("In-Process");

            //if (!model.getTransId().contains("WDICF")){
            UpdateModel updateModel= MyApp.getMyPreferences().getUpdateMaster();
            if (!TextUtils.isEmpty(updateModel.getIs_wd_expected_date())){
                holder.layExSetDate.setVisibility(View.VISIBLE);
                holder.txtExSetDate.setText(CustomUtil.dateConvertWithFormat(updateModel.getIs_wd_expected_date(),
                        "MMM dd, yyyy hh:mm a","yyyy-MM-dd HH:mm:ss"));
            }
            //}
        }
        else if (model.getStatus().equalsIgnoreCase("success")){
            holder.txtStatus.setBackgroundResource(R.drawable.opinio_yes);
            holder.txtStatus.setTextColor(context.getResources().getColor(R.color.green_pure));
            holder.txtStatus.setText("Success");

            holder.txtMsg.setVisibility(View.VISIBLE);
            holder.txtMsg.setText("Your withdrawal has been processed successfully.");

        }
        else if (model.getStatus().equalsIgnoreCase("reject")) {
            holder.txtStatus.setBackgroundResource(R.drawable.opinio_no);
            holder.txtStatus.setTextColor(context.getResources().getColor(R.color.red));
            holder.txtStatus.setText("Failed");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtWithdrawReqAmt,txtCreditAmt,txtAmtTds,txtTds,txtWithdrawCharge,txtTransactionId,txtCreateAt,txtMsg,txtStatus,txtExSetDate;
        private LinearLayout layExSetDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtWithdrawReqAmt=itemView.findViewById(R.id.txtWithdrawReqAmt);
            txtCreditAmt=itemView.findViewById(R.id.txtCreditAmt);
            txtAmtTds=itemView.findViewById(R.id.txtAmtTds);
            txtTds=itemView.findViewById(R.id.txtTds);
            txtWithdrawCharge=itemView.findViewById(R.id.txtWithdrawCharge);
            txtTransactionId=itemView.findViewById(R.id.txtTransactionId);
            txtCreateAt=itemView.findViewById(R.id.txtCreateAt);
            txtMsg=itemView.findViewById(R.id.txtMsg);
            txtStatus=itemView.findViewById(R.id.txtStatus);
            txtExSetDate=itemView.findViewById(R.id.txtExSetDate);
            layExSetDate=itemView.findViewById(R.id.layExSetDate);
        }
    }
}
