package com.fantafeat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Model.ScoreModel;
import com.fantafeat.R;

import java.util.List;

public class FallAdpter extends RecyclerView.Adapter<FallAdpter.ViewHolder>{

    private Context context;
    private List<ScoreModel.Fow> list;

    public FallAdpter(Context context, List<ScoreModel.Fow> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.fall_bat_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScoreModel.Fow modal=list.get(position);

        holder.txtBatName.setText(modal.getName());
        holder.txtBatDesc.setText(modal.getHowOut());
        holder.txtOvers.setText(modal.getOversAtDismissal());
        holder.txtRuns.setText(modal.getScoreAtDismissal()+"");


        /*if (modal.isPlayerInTeam()){
            holder.linearLayout.setBackgroundResource(R.color.selectGreen);
        }else {
            holder.linearLayout.setBackgroundResource(R.color.pureWhite);
        }*/

        if ((position % 2) == 0) {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.white_pure));
        }
        else {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.appBackGround));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtBatName,txtBatDesc,txtRuns,txtOvers;
        private LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout=itemView.findViewById(R.id.linearLayout);
            txtBatName=itemView.findViewById(R.id.txtBatName);
            txtBatDesc=itemView.findViewById(R.id.txtBatDesc);
            txtRuns=itemView.findViewById(R.id.txtRuns);
            txtOvers=itemView.findViewById(R.id.txtOvers);
        }
    }
}
