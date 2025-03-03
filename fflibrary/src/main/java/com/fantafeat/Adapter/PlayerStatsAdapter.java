package com.fantafeat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fantafeat.Model.PlayerStatsModel;
import com.fantafeat.R;

import java.util.List;

public class PlayerStatsAdapter extends RecyclerView.Adapter<PlayerStatsAdapter.PlayerHandler> {

    Context mContext;
    List<PlayerStatsModel> playerStatsModelList;

    public PlayerStatsAdapter(Context mContext, List<PlayerStatsModel> playerStatsModelList) {
        this.mContext = mContext;
        this.playerStatsModelList = playerStatsModelList;
    }

    @NonNull
    @Override
    public PlayerHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlayerHandler(LayoutInflater.from(mContext).inflate(R.layout.row_player_stats,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerHandler holder, int position) {
        PlayerStatsModel model = playerStatsModelList.get(position);

        holder.match_teams.setText(model.getTeam1Sname()+" vs "+model.getTeam2Sname());
        holder.player_type.setText(model.getPlayerType());
        holder.player_credit.setText(model.getPlayerRank());
        holder.player_points.setText(model.getPlayerAvgPoint());
        holder.match_date.setText(model.getMatchStartDate());
    }

    @Override
    public int getItemCount() {
        return playerStatsModelList.size();
    }

    public class PlayerHandler extends RecyclerView.ViewHolder{

        TextView match_teams,match_date,player_type,player_credit,player_points;

        public PlayerHandler(@NonNull View itemView) {
            super(itemView);
            match_teams = itemView.findViewById(R.id.match_teams);
            match_date = itemView.findViewById(R.id.match_date);
            player_type = itemView.findViewById(R.id.player_type);
            player_credit = itemView.findViewById(R.id.player_credit);
            player_points = itemView.findViewById(R.id.player_points);
        }
    }
}
