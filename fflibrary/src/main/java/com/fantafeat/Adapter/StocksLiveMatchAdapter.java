package com.fantafeat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Model.StocksLiveMatchModel;
import com.fantafeat.R;

public class StocksLiveMatchAdapter extends RecyclerView.Adapter<StocksLiveMatchAdapter.MyViewHolder> {
    private Context context;
    private StocksLiveMatchModel[] list;
    private String selectedTag;

    public StocksLiveMatchAdapter(Context context, StocksLiveMatchModel[] list, String selectedTag) {
        this.context = context;
        this.list = list;
        this.selectedTag = selectedTag;
    }

    @NonNull
    @Override
    public StocksLiveMatchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_active,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull StocksLiveMatchAdapter.MyViewHolder holder, int position) {
        StocksLiveMatchModel model = list[position];
        holder.team1_full_name.setText(model.getTitle());
    }

    public void updateTag(String selectedTag){
        this.selectedTag=selectedTag;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView team1_full_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            team1_full_name=itemView.findViewById(R.id.team1_full_name);
        }
    }
}
