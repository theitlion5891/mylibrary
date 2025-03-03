package com.fantafeat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Activity.PaymentActivity;
import com.fantafeat.Model.PaymentSettingModel;
import com.fantafeat.R;

import java.util.List;

public class PaymentOptionAdapter extends RecyclerView.Adapter<PaymentOptionAdapter.ViewHolder> {

    private List<PaymentSettingModel.PaymentType> list;
    private Context context;

    public PaymentOptionAdapter(List<PaymentSettingModel.PaymentType> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.payment_option_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PaymentSettingModel.PaymentType model=list.get(position);

        if (model.getIs_enable_android().equalsIgnoreCase("no")){
            holder.layOption.setVisibility(View.GONE);
        }else {
            holder.layOption.setVisibility(View.VISIBLE);
        }

        holder.txtTitle.setText(model.getTitle());
        holder.txtSubTitle.setText(model.getSubTitle());

        holder.layOption.setOnClickListener(view -> {
            if (context instanceof PaymentActivity){
                ((PaymentActivity) context).choosePaymentOption(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtTitle,txtSubTitle;
        private LinearLayout layOption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle=itemView.findViewById(R.id.txtTitle);
            txtSubTitle=itemView.findViewById(R.id.txtSubTitle);
            layOption=itemView.findViewById(R.id.layOption);
            //layMain=itemView.findViewById(R.id.layMain);
        }
    }
}
