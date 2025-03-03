package com.fantafeat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Model.AfterMatchPlayerStatesModel;
import com.fantafeat.R;

import java.util.List;

public class AfterMatchPlayerStatesAdapter extends RecyclerView.Adapter<AfterMatchPlayerStatesAdapter.Holder>{

    Context mContext;
    List<AfterMatchPlayerStatesModel> list;

    public AfterMatchPlayerStatesAdapter(Context mContext, List<AfterMatchPlayerStatesModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.row_live_plater_states,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        AfterMatchPlayerStatesModel model = list.get(holder.getAdapterPosition());

        holder.title.setText(model.getTitle());
        holder.points.setText(model.getPoint());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView title,points;
        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            points = itemView.findViewById(R.id.points);
        }
    }
}
