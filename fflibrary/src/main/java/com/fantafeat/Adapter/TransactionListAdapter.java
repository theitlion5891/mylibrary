package com.fantafeat.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Model.TransactionModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.CustomUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.TransactionHolder> {
    Context mContext;
    List<TransactionModel> transactionModelList = new ArrayList<>();

    public TransactionListAdapter(Context mContext, List<TransactionModel> transactionModelList) {
        this.mContext = mContext;
        this.transactionModelList = transactionModelList;
    }

    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionHolder(LayoutInflater.from(mContext).inflate(R.layout.row_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final TransactionHolder holder, int position) {
        final TransactionModel transactionModel = transactionModelList.get(position);

        if (transactionModel.getTransType().equals("Debit")) {
            holder.txtAmt.setTextColor(Color.RED);
            holder.imgStatus.setImageResource(R.drawable.ic_deposit);
            holder.txtTitle.setText(transactionModel.getTransName());
        }
        else if (transactionModel.getTransType().equals("Credit")) {
            holder.txtAmt.setTextColor(mContext.getResources().getColor(R.color.green_pure));
            holder.imgStatus.setImageResource(R.drawable.ic_withdraw_green);
            holder.txtTitle.setText(transactionModel.getTransName());
        }
        DecimalFormat decimalFormat=CustomUtil.getFormater("00.00");

        holder.txtWalletBalance.setText(mContext.getResources().getString(R.string.rs) +
                decimalFormat.format(CustomUtil.convertFloat(transactionModel.getTotalBal())));
        holder.tran_description.setText(transactionModel.getTransDesc());
        holder.txtTraID.setText("#" + transactionModel.getTransId());
        holder.txtAmt.setText(mContext.getResources().getString(R.string.rs) +
                decimalFormat.format(CustomUtil.convertFloat(transactionModel.getAmount())));

        SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat TransactionFormat = MyApp.changedFormat("dd MMM, yyyy hh:mma");

        try {
            Date date = format.parse(transactionModel.getCreateAt());

            String transactionDate = TransactionFormat.format(date);

            holder.tran_time.setText(transactionDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

      /*  if (transactionModel.isOpen()) {
            holder.tran_sub_layout.setVisibility(View.VISIBLE);
        } else {
            holder.tran_sub_layout.setVisibility(View.GONE);
        }

        if (holder.getAdapterPosition() % 2 == 0) {
            holder.row_transaction_back.setBackgroundColor(mContext.getResources().getColor(R.color.appBackground));
        } else {
            holder.row_transaction_back.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat TransactionFormat = new SimpleDateFormat("dd MMM yyyy");

        try {
            Date date = format.parse(transactionModel.getCreateAt());

            String transactionDate = TransactionFormat.format(date);

            holder.tran_time.setText(transactionDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (transactionModel.getTransType().equals("Debit")) {
            holder.tran_type.setTextColor(Color.RED);
        } else if (transactionModel.getTransType().equals("Credit")) {
            holder.tran_type.setTextColor(mContext.getResources().getColor(R.color.green));
        }
        holder.tran_type.setText(transactionModel.getTransType());
        holder.tran_id.setText("#" + transactionModel.getTransId());
        holder.tran_name.setText(transactionModel.getTransName());
        holder.tran_description.setText(transactionModel.getTransDesc());
        holder.tran_amount.setText(mContext.getResources().getString(R.string.rs) +
                new DecimalFormat("00.00").format(CustomUtil.convertFloat(transactionModel.getAmount())));


        holder.tran_sub_layout.setVisibility(View.GONE);
        holder.transaction_row.setClickable(true);
        holder.transaction_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    if (transactionModel.isOpen()) {
                        holder.tran_sub_layout.setVisibility(View.GONE);
                        transactionModel.setOpen(false);
                    } else {
                        holder.tran_sub_layout.setVisibility(View.VISIBLE);
                        transactionModel.setOpen(true);
                    }
                }
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return transactionModelList.size();
    }

    public class TransactionHolder extends RecyclerView.ViewHolder {

        //LinearLayout transaction_row, tran_sub_layout, row_transaction_back;
        ImageView imgStatus;
        TextView tran_time, txtTitle, txtAmt, txtTraID, tran_name, tran_description,txtWalletBalance;

        public TransactionHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtAmt = itemView.findViewById(R.id.txtAmt);
            tran_time = itemView.findViewById(R.id.tran_time);
            txtTraID = itemView.findViewById(R.id.txtTraID);
            tran_description = itemView.findViewById(R.id.tran_description);
            txtWalletBalance = itemView.findViewById(R.id.txtWalletBalance);
            imgStatus = itemView.findViewById(R.id.imgStatus);

        }
    }

    public void updateData(List<TransactionModel> transactionModelList) {
        this.transactionModelList = transactionModelList;
        notifyDataSetChanged();
    }
}
