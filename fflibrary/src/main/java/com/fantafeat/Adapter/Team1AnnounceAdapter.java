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

import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Team1AnnounceAdapter extends RecyclerView.Adapter<Team1AnnounceAdapter.ViewHolder> {

    private List<PlayerModel> playerModelList;
    private Context context;
    String sportId="";

    public Team1AnnounceAdapter(List<PlayerModel> playerModelList, Context context,String sportId) {
        this.playerModelList = playerModelList;
        this.context = context;
        this.sportId = sportId;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.team1_announce,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        PlayerModel model=playerModelList.get(position);

       // CustomUtil.loadImageWithGlide(context,holder.imgPlayer, ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.team_loading);

        if (model.isSelected()){
            holder.select_linear_layout.setBackgroundResource(R.color.selectGreen);
        }else {
            holder.select_linear_layout.setBackgroundResource(R.color.white_pure);
        }

        if (!TextUtils.isEmpty(model.getPlayerImage())){
            if (sportId.equals("1")){
                CustomUtil.loadImageWithGlide(context,holder.imgPlayer,ApiManager.TEAM_IMG,model.getPlayerImage(),
                        R.drawable.ic_team1_tshirt);
            }
            else if (sportId.equals("2")){
                CustomUtil.loadImageWithGlide(context,holder.imgPlayer,ApiManager.TEAM_IMG,model.getPlayerImage(),
                        R.drawable.football_player1);
            }
            else if (sportId.equals("4")){
                CustomUtil.loadImageWithGlide(context,holder.imgPlayer,ApiManager.TEAM_IMG,model.getPlayerImage(),
                        R.drawable.basketball_team1);
            }
            else if (sportId.equals("3")){
                CustomUtil.loadImageWithGlide(context,holder.imgPlayer,ApiManager.TEAM_IMG,model.getPlayerImage(),
                        R.drawable.baseball_player1);
            }
            else if (sportId.equals("6")){
                CustomUtil.loadImageWithGlide(context,holder.imgPlayer,ApiManager.TEAM_IMG,model.getPlayerImage(),
                        R.drawable.handball_player1);
            }
            else if (sportId.equals("7")){
                CustomUtil.loadImageWithGlide(context,holder.imgPlayer,ApiManager.TEAM_IMG,model.getPlayerImage(),
                        R.drawable.kabaddi_player1);
            }
        }
        else {
            if (sportId.equals("1")){
                holder.imgPlayer.setImageResource(R.drawable.ic_team1_tshirt);
            }
            else if (sportId.equals("2")){
                holder.imgPlayer.setImageResource(R.drawable.football_player1);
            }
            else if (sportId.equals("4")){
                holder.imgPlayer.setImageResource(R.drawable.basketball_team1);
            }
            else if (sportId.equals("3")){
                holder.imgPlayer.setImageResource(R.drawable.baseball_player1);
            }
            else if (sportId.equals("6")){
                holder.imgPlayer.setImageResource(R.drawable.handball_player1);
            }
            else if (sportId.equals("7")){
                holder.imgPlayer.setImageResource(R.drawable.kabaddi_player1);
            }
        }

        // CustomUtil.loadImageWithGlide(context,holder.imgPlayer, ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.team_loading);

        if (TextUtils.isEmpty(model.getPlayerName())){
            holder.txtPlayer.setText("-");
        }else {
            holder.txtPlayer.setText(model.getPlayerName());
        }

        if (TextUtils.isEmpty(model.getPlayerType())){
            holder.txtType.setText("-");
        }else {
            holder.txtType.setText(model.getPlayerType());
        }

    }

    @Override
    public int getItemCount() {
        return playerModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout select_linear_layout;
        private ImageView imgPlayer;
        private TextView txtPlayer,txtType;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgPlayer=itemView.findViewById(R.id.imgPlayer);
            txtType=itemView.findViewById(R.id.txtType);
            txtPlayer=itemView.findViewById(R.id.txtPlayer);
            select_linear_layout=itemView.findViewById(R.id.select_linear_layout);
        }
    }
}
