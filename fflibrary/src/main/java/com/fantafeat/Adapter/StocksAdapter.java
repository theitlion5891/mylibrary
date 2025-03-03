package com.fantafeat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Model.StocksLiveMatchModel;
import com.fantafeat.Model.StocksModel;
import com.fantafeat.R;

import java.util.ArrayList;

public class StocksAdapter extends RecyclerView.Adapter<StocksAdapter.MyViewHolder> {
    private Context context;
    private StocksModel[] list;

    public StocksAdapter(Context context, StocksModel[] list1) {
        this.context = context;

        this.list = list1;
    }

    @NonNull
    @Override
    public StocksAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_stocks,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull StocksAdapter.MyViewHolder holder, int position) {
        StocksModel model = list[position];
        holder.player_name.setText(model.getpName());
    }
    public void updateData(StocksModel[] model){
        this.list=model;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView player_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            player_name = itemView.findViewById(R.id.player_name);
        }
    }
}
