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

public class BowlerAdapter extends RecyclerView.Adapter<BowlerAdapter.ViewHolder>{

    private Context context;
    private List<ScoreModel.Bowler> list;

    public BowlerAdapter(Context context, List<ScoreModel.Bowler> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.bowlerr_item,parent,false) );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScoreModel.Bowler bowler=list.get(position);

        if (bowler.getCorvc().equalsIgnoreCase("c")){
            holder.txtBowlName.setText(bowler.getName()+" (C)");
        }
        else if (bowler.getCorvc().equalsIgnoreCase("vc")){
            holder.txtBowlName.setText(bowler.getName()+" (VC)");
        }
        else {
            holder.txtBowlName.setText(bowler.getName());
        }

       // holder.txtBowlName.setText(bowler.getName());
        holder.txtMaiden.setText(bowler.getMaidens());
        holder.txtZero.setText(bowler.getOvers());
        holder.txtWickets.setText(bowler.getWickets());
        holder.txtRun.setText(bowler.getRunsConceded());
        holder.txtECO.setText(bowler.getEcon());

        if (bowler.isPlayerInTeam()){
            holder.linearLayout.setBackgroundResource(R.color.selectGreen);
        }else {
            holder.linearLayout.setBackgroundResource(R.color.pureWhite);
        }

        /*if ((position % 2) == 0) {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        else {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.appBackGround));
        }*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtBowlName,txtZero,txtMaiden,txtRun,txtWickets,txtECO;
        private LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtBowlName=itemView.findViewById(R.id.txtBowlName);
            txtZero=itemView.findViewById(R.id.txtZero);
            txtMaiden=itemView.findViewById(R.id.txtMaiden);
            txtRun=itemView.findViewById(R.id.txtRun);
            txtWickets=itemView.findViewById(R.id.txtWickets);
            txtECO=itemView.findViewById(R.id.txtECO);
            linearLayout=itemView.findViewById(R.id.linearLayout);
        }
    }
}
