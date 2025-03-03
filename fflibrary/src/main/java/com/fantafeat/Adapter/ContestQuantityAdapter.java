package com.fantafeat.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.Model.ContestQuantityModel;
import com.fantafeat.R;

import java.util.ArrayList;

public class ContestQuantityAdapter extends RecyclerView.Adapter<ContestQuantityAdapter.ViewHolder>{

    private Context context;
    private ArrayList<ContestQuantityModel> contestQtyList;
    private onQtyListener listener;

    public ContestQuantityAdapter(Context context, ArrayList<ContestQuantityModel> contestQtyList, onQtyListener listener) {
        this.context = context;
        this.contestQtyList = contestQtyList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.contest_quantity_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContestQuantityModel model=contestQtyList.get(position);

        holder.txtQty.setText(model.getNoOfQty());

        if (model.isSelected()){
            holder.txtQty.setTextColor(context.getResources().getColor(R.color.green_pure));
            holder.txtQty.setBackgroundResource(R.drawable.green_border_qty);
        }else {
            holder.txtQty.setTextColor(context.getResources().getColor(R.color.blackPrimary));
            holder.txtQty.setBackgroundResource(R.drawable.gray_border_qty);
        }

        holder.txtQty.setOnClickListener(v -> {
            for (int i = 0; i < contestQtyList.size(); i++) {
                ContestQuantityModel model1=contestQtyList.get(i);
                model1.setSelected(false);
            }
            model.setSelected(true);
            listener.onClick(model);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return contestQtyList.size();
    }

    public interface onQtyListener{
         void onClick(ContestQuantityModel model);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtQty;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtQty=itemView.findViewById(R.id.txtQty);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            float widthPixels = (float) ((displayMetrics.widthPixels)/6)-30;
            //float heightPixels = (float) (widthPixels * 1.9);
            txtQty.setMinWidth((int)widthPixels);
        }
    }
}
