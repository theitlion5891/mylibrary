package com.fantafeat.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Model.PokerTableModel;
import com.fantafeat.R;
import com.fantafeat.util.CustomUtil;

import java.util.ArrayList;

public class PokerTableAdapter extends RecyclerView.Adapter<PokerTableAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<PokerTableModel> list;
    private onTableClick listener;

    public PokerTableAdapter(Context mContext, ArrayList<PokerTableModel> list, onTableClick listener) {
        this.mContext = mContext;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.poker_table_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PokerTableModel model=list.get(position);

        float minbuy= CustomUtil.convertFloat(model.getMin_buy_in());
        float avgstack= CustomUtil.convertFloat(model.getAvgStack());

        holder.txtAvgStack.setText(mContext.getResources().getString(R.string.rs)+(int)Math.round(avgstack));
        holder.txtPlayers.setText(model.getWaiting_player()+"/"+model.getTotal_player());
       /* if (TextUtils.isEmpty(model.getWaiting_player())){
            model.setWaiting_player("0");
        }
        if (CustomUtil.convertInt(model.getWaiting_player())>0){
            holder.txtPlayers.setText(model.getWaiting_player()+"/"+model.getTotal_player());
            holder.txtPlayers.setVisibility(View.VISIBLE);
            holder.txtLblPlayer.setVisibility(View.VISIBLE);
        }else {
            holder.txtPlayers.setVisibility(View.GONE);
            holder.txtLblPlayer.setVisibility(View.GONE);
        }*/

        holder.txtBuyIn.setText(mContext.getResources().getString(R.string.rs)+((int)Math.round(minbuy)));

        holder.layContest.setOnClickListener(view -> {
            listener.onClick(model);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface onTableClick{
        public void onClick(PokerTableModel model);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtAvgStack,txtPlayers,txtBuyIn,txtLblPlayer;
        private LinearLayout layContest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtAvgStack=itemView.findViewById(R.id.txtAvgStack);
            txtPlayers=itemView.findViewById(R.id.txtPlayers);
            txtBuyIn=itemView.findViewById(R.id.txtBuyIn);
            layContest=itemView.findViewById(R.id.layContest);
            txtLblPlayer=itemView.findViewById(R.id.txtLblPlayer);
        }
    }

}
