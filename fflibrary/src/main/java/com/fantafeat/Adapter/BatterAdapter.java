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

public class BatterAdapter extends RecyclerView.Adapter<BatterAdapter.ViewHolder>{

    private Context context;
    private List<ScoreModel.Batsman> list;

    public BatterAdapter(Context context, List<ScoreModel.Batsman> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.batter_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ScoreModel.Batsman commentary=list.get(position);

        if (commentary.getCorvc().equalsIgnoreCase("c")){
            holder.txtBatName.setText(commentary.getName()+" (C)");
        }
        else if (commentary.getCorvc().equalsIgnoreCase("vc")){
            holder.txtBatName.setText(commentary.getName()+" (VC)");
        }else {
            holder.txtBatName.setText(commentary.getName());
        }

        holder.txtBatDesc.setText(commentary.getHowOut());
        holder.txtBall.setText(commentary.getBallsFaced());
        holder.txtRuns.setText(commentary.getRuns());
        holder.txtFour.setText(commentary.getFours());
        holder.txtSix.setText(commentary.getSixes());
        holder.txtSr.setText(commentary.getStrikeRate());

       // LogUtil.e("resp","Batter: "+commentary.isPlayerInTeam());
        if (commentary.isPlayerInTeam()){
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

        private TextView txtBatName,txtBatDesc,txtRuns,txtBall,txtFour,txtSix,txtSr;
        private LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtBatName=itemView.findViewById(R.id.txtBatName);
            txtBatDesc=itemView.findViewById(R.id.txtBatDesc);
            txtRuns=itemView.findViewById(R.id.txtRuns);
            txtBall=itemView.findViewById(R.id.txtBall);
            txtFour=itemView.findViewById(R.id.txtFour);
            txtSix=itemView.findViewById(R.id.txtSix);
            txtSr=itemView.findViewById(R.id.txtSr);
            linearLayout=itemView.findViewById(R.id.linearLayout);


        }
    }
}
