package com.fantafeat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Activity.AfterMatchPlayerStatesActivity;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.MatchPlayerStateModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.google.gson.Gson;

import java.util.List;

public class MatchPlayerStatsAdapter extends RecyclerView.Adapter<MatchPlayerStatsAdapter.MatchPlayerHolder> {

    Context context;
    List<MatchPlayerStateModel> matchPlayerStateModelList;
    MyPreferences preferences;

    public MatchPlayerStatsAdapter(Context context, List<MatchPlayerStateModel> matchPlayerStateModelList, MyPreferences preferences) {
        this.context = context;
        this.matchPlayerStateModelList = matchPlayerStateModelList;
        this.preferences = preferences;
    }

    @NonNull
    @Override
    public MatchPlayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MatchPlayerHolder(LayoutInflater.from(context).inflate(R.layout.row_player_state,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MatchPlayerHolder holder, int position) {
        MatchPlayerStateModel model = matchPlayerStateModelList.get(position);

        if (model.isCheck()){
            holder.linear.setBackgroundResource(R.color.selectGreen);
        }else {
            holder.linear.setBackgroundResource(R.color.pureWhite);
        }

        holder.player_name.setText(model.getPlayerSname());
        holder.player_points.setText(model.getTotalPoint());
        holder.txtType.setText(model.getPlayerType());

        MatchModel matchModel=preferences.getMatchModel();

        if(model.getTeamId().equals(matchModel.getTeam1())){
            holder.player_team.setBackground(context.getResources().getDrawable(R.drawable.team2_name_select_player));
            holder.player_team.setTextColor(context.getResources().getColor(R.color.font_color));
            holder.player_team.setText(matchModel.getTeam1Sname());
        }else{
            holder.player_team.setBackground(context.getResources().getDrawable(R.drawable.team1_name_select_player));
            holder.player_team.setTextColor(context.getResources().getColor(R.color.pureWhite));
            holder.player_team.setText(matchModel.getTeam2Sname());
        }

        if (TextUtils.isEmpty(model.getCorvc())){
            holder.imgCVC.setVisibility(View.GONE);
        }else {
            // LogUtil.e("resp",model.getPlayerSname());
            holder.imgCVC.setVisibility(View.VISIBLE);
            if (model.getCorvc().equalsIgnoreCase("c")){
                holder.imgCVC.setImageResource(R.drawable.c);
            }else if (model.getCorvc().equalsIgnoreCase("vc")){
                holder.imgCVC.setImageResource(R.drawable.vc);
            }
        }

        if (TextUtils.isEmpty(model.getPlayer_percent())){
            holder.txtSelBy.setText("-");
        }else {
            holder.txtSelBy.setText("Sel By "+model.getPlayer_percent()+"%");
        }


        /*if(model.getTeamId().equals(matchModel.getTeam1()))
        {
            holder.player_team.setText(matchModel.getTeam1Sname());
        }else if(model.getTeamId().equals(matchModel.getTeam2())){
            holder.player_team.setText(matchModel.getTeam2Sname());
        }*/
     //   LogUtil.d("resp",ApiManager.DOCUMENTS +model.getPlayerImage());
        if (matchModel.getSportId().equals("1")){
            if (model.getTeamId().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.player_img,ApiManager.DOCUMENTS,model.getPlayerImage(),R.drawable.ic_team1_tshirt);
            }else if (model.getTeamId().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(context,holder.player_img,ApiManager.DOCUMENTS,model.getPlayerImage(),R.drawable.ic_team2_tshirt);
            }
        }
        else if (matchModel.getSportId().equals("2")){
            if (model.getTeamId().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.player_img,ApiManager.DOCUMENTS,model.getPlayerImage(),R.drawable.football_player1);
            }else if (model.getTeamId().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(context,holder.player_img,ApiManager.DOCUMENTS,model.getPlayerImage(),R.drawable.football_player2);
            }
        }
        else if (matchModel.getSportId().equals("4")){
            if (model.getTeamId().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.player_img,ApiManager.DOCUMENTS,model.getPlayerImage(),R.drawable.basketball_team1);
            }else if (model.getTeamId().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(context,holder.player_img,ApiManager.DOCUMENTS,model.getPlayerImage(),R.drawable.basketball_team2);
            }
        }
        else if (matchModel.getSportId().equals("3")){
            if (model.getTeamId().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.player_img,ApiManager.DOCUMENTS,model.getPlayerImage(),R.drawable.baseball_player1);
            }else if (model.getTeamId().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(context,holder.player_img,ApiManager.DOCUMENTS,model.getPlayerImage(),R.drawable.baseball_player2);
            }
        }
        else if (matchModel.getSportId().equals("6")){
            if (model.getTeamId().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.player_img,ApiManager.DOCUMENTS,model.getPlayerImage(),R.drawable.handball_player1);
            }else if (model.getTeamId().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(context,holder.player_img,ApiManager.DOCUMENTS,model.getPlayerImage(),R.drawable.handball_player2);
            }
        }
        else if (matchModel.getSportId().equals("7")){
            if (model.getTeamId().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.player_img,ApiManager.DOCUMENTS,model.getPlayerImage(),R.drawable.kabaddi_player1);
            }else if (model.getTeamId().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(context,holder.player_img,ApiManager.DOCUMENTS,model.getPlayerImage(),R.drawable.kabaddi_player2);
            }
        }

        holder.linear.setOnClickListener(view -> {
            /*new FragmentUtil().addFragment((FragmentActivity) context,
                    R.id.fragment_container,
                    new AfterMatachPlayerStatesFragment(model),
                    ((HomeActivity) context).fragmentTag(48),
                    FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
            Intent intent = new Intent(context, AfterMatchPlayerStatesActivity.class);
            Gson gson = new Gson();
            intent.putExtra("type",1);
            intent.putExtra("model",gson.toJson(model));
            context.startActivity(intent);
        });

    }

    public void updateData(List<MatchPlayerStateModel> matchPlayerStateModelList){
        this.matchPlayerStateModelList=matchPlayerStateModelList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return matchPlayerStateModelList.size();
    }

    public class MatchPlayerHolder extends RecyclerView.ViewHolder{

        ImageView player_img,imgCVC;
        TextView player_name,player_type,player_team,player_points,txtType,txtSelBy;
        LinearLayout linear;

        public MatchPlayerHolder(@NonNull View itemView) {
            super(itemView);
            player_img = itemView.findViewById(R.id.imgProfile);
            player_name = itemView.findViewById(R.id.txtName);
            player_team = itemView.findViewById(R.id.player_team);
            player_points = itemView.findViewById(R.id.txtPoint);
            linear = itemView.findViewById(R.id.linear5);
            txtType = itemView.findViewById(R.id.txtType);
            txtSelBy = itemView.findViewById(R.id.txtSelBy);
            imgCVC = itemView.findViewById(R.id.imgCVC);
        }
    }
}
