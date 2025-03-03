package com.fantafeat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Model.PaymentSettingModel;
import com.fantafeat.R;

import java.util.ArrayList;

public class PaymentSettingAdapter extends RecyclerView.Adapter<PaymentSettingAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PaymentSettingModel> list;

    public PaymentSettingAdapter(Context context, ArrayList<PaymentSettingModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.payment_main_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PaymentSettingModel model=list.get(position);
        holder.txtTitle.setText(model.getTitle());
        holder.txtSubTitle.setText(model.getSubTitle());
        LinearLayoutManager layoutManager=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        holder.recyclerOption.setLayoutManager(layoutManager);
        PaymentOptionAdapter adapter=new PaymentOptionAdapter(model.getPaymentType(),context);
        holder.recyclerOption.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtTitle,txtSubTitle;
        private RecyclerView recyclerOption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerOption=itemView.findViewById(R.id.recyclerOption);
            txtTitle=itemView.findViewById(R.id.txtTitle);
            txtSubTitle=itemView.findViewById(R.id.txtSubTitle);
        }
    }
}
