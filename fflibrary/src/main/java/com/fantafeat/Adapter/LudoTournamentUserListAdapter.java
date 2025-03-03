package com.fantafeat.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.R;
import com.fantafeat.Model.LudoTournamentUserModel;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.CustomUtil;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LudoTournamentUserListAdapter extends RecyclerView.Adapter<LudoTournamentUserListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<LudoTournamentUserModel> list;
    private MyPreferences preferences;
    private String winning_dec;

    public LudoTournamentUserListAdapter(Context context, ArrayList<LudoTournamentUserModel> list, String winning_dec) {
        this.context = context;
        this.list = list;
        preferences = MyApp.getMyPreferences();
        this.winning_dec = winning_dec;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.tournament_user_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LudoTournamentUserModel item=list.get(position);

        if (winning_dec.equalsIgnoreCase("yes")){
            holder.txtWinAmt.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(item.getTotalWinAmt())){
                holder.txtWinAmt.setVisibility(View.GONE);
            }else {
                if (item.getTotalWinAmt().equalsIgnoreCase("0")){
                    holder.txtWinAmt.setVisibility(View.GONE);
                }else {
                    if (item.getUserId().equals(preferences.getUserModel().getId())) {
                        holder.txtWinAmt.setText("You Won: "+ CustomUtil.displayAmountFloat( context, item.getTotalWinAmt()));
                    }else {
                        holder.txtWinAmt.setText("Win: "+CustomUtil.displayAmountFloat( context, item.getTotalWinAmt()));
                    }
                }
            }

        }else {
            holder.txtWinAmt.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(item.getTotalPoint())){
            holder.txtPoints.setText("-");
        }else {
            holder.txtPoints.setText(item.getTotalPoint());
        }

        if (TextUtils.isEmpty(item.getTotalRank())){
            holder.txtRank.setText("-");
        }else {
            holder.txtRank.setText("#"+item.getTotalRank());
        }

        switch (item.getTotalRank()) {
            case "1":
                holder.txtRank.setVisibility(View.GONE);
                holder.imgRank.setVisibility(View.VISIBLE);
                holder.imgRank.setImageResource(R.drawable.ic_rank_one);
                break;
            case "2":
                holder.txtRank.setVisibility(View.GONE);
                holder.imgRank.setVisibility(View.VISIBLE);
                holder.imgRank.setImageResource(R.drawable.ic_rank_two);
                break;
            case "3":
                holder.txtRank.setVisibility(View.GONE);
                holder.imgRank.setVisibility(View.VISIBLE);
                holder.imgRank.setImageResource(R.drawable.ic_rank_three);
                break;
            default:
                holder.imgRank.setVisibility(View.GONE);
                holder.txtRank.setVisibility(View.VISIBLE);
                break;
        }

        switch (item.getTotalRank()) {
            case "1":
                holder.leader_layout.setBackgroundResource(R.drawable.tournament_rank_1);
                break;
            case "2":
                holder.leader_layout.setBackgroundResource(R.drawable.tournament_rank_2);
                break;
            case "3":
                holder.leader_layout.setBackgroundResource(R.drawable.tournament_rank_3);
                break;
            default:
                if (item.getUserId().equals(preferences.getUserModel().getId())) {
                    holder.leader_layout.setBackgroundResource(R.drawable.tournament_rank_my);
                }else {
                    holder.leader_layout.setBackgroundResource(R.drawable.tournament_rank_other);
                }
                break;
        }

        holder.txtTeamName.setText("@"+item.getTeamName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtUserName,txtPoints,txtRank,txtTeamName,txtWinAmt;

        ImageView imgRank;
        LinearLayout leader_layout/*,layRank*/;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtUserName=itemView.findViewById(R.id.txtUserName);
            txtPoints=itemView.findViewById(R.id.txtPoints);
            txtRank=itemView.findViewById(R.id.txtRank);
            txtTeamName=itemView.findViewById(R.id.txtTeamName);
            txtUserName.setVisibility(View.GONE);
            leader_layout=itemView.findViewById(R.id.leader_layout);
            //layRank=itemView.findViewById(R.id.layRank);
            imgRank=itemView.findViewById(R.id.imgRank);
            txtWinAmt=itemView.findViewById(R.id.txtWinAmt);

        }
    }
}
