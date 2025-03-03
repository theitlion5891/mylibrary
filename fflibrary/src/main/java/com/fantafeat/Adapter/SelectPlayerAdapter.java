package com.fantafeat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fantafeat.Activity.PlayerStatsActivity;
import com.fantafeat.Fragment.CricketSelectPlayerFragment;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

import static com.fantafeat.Session.BaseFragment.TAG;

public class SelectPlayerAdapter extends RecyclerView.Adapter<SelectPlayerAdapter.SelectPlayerHolder> {

    public List<PlayerModel> player_list;
    public Context context;
    public Fragment fragment;
    public MyPreferences preferences;

    public SelectPlayerAdapter(List<PlayerModel> player_list, Context context, Fragment fragment, MyPreferences preferences){
        this.player_list = player_list;
        this.context = context;
        this.fragment = fragment;
        this.preferences = preferences;
    }

    @NonNull
    @Override
    public SelectPlayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SelectPlayerHolder(LayoutInflater.from(context).inflate(R.layout.row_select_player,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectPlayerHolder holder, int position) {
        final PlayerModel model = player_list.get(position);

        if (((CricketSelectPlayerFragment)fragment).checkSelect.get(model.getPlayerId()) == 0) {
            holder.plus_minus_sign.setImageResource(R.drawable.ic_plus);
            holder.select_linear_layout.setBackgroundResource(R.color.transparent);
        } else {
            holder.plus_minus_sign.setImageResource(R.drawable.ic_minus);
            if (preferences.getMatchModel().getTeamAXi().equalsIgnoreCase("yes") ||
                    preferences.getMatchModel().getTeamBXi().equalsIgnoreCase("yes")) {
                if (model.getPlayingXi().equals("Yes")) {
                    holder.select_linear_layout.setBackgroundResource(R.color.selectGreen);
                }else {
                    //holder.select_linear_layout.setBackgroundResource(R.color.selectRed);
                    if (model.getOther_text().equalsIgnoreCase("substitute")){
                        holder.select_linear_layout.setBackgroundResource(R.color.lighest_blue);
                    }else
                        holder.select_linear_layout.setBackgroundResource(R.color.selectRed);
                }
            }else {
                holder.select_linear_layout.setBackgroundResource(R.color.selectGreen);
            }
            //holder.select_card.setBackgroundColor(context.getResources().getColor(R.color.selectGreen));
        }

        if (preferences.getMatchModel().getTeamAXi().equalsIgnoreCase("yes") ||
                preferences.getMatchModel().getTeamBXi().equalsIgnoreCase("yes")) {

            /*if ((holder.getAdapterPosition() > 0 &&
                    player_list.get(holder.getAdapterPosition() - 1).getPlayingXi().equalsIgnoreCase(model.getPlayingXi()))) {
                holder.layAnnounce.setVisibility(View.GONE);
            }else {
                holder.layAnnounce.setVisibility(View.VISIBLE);
            }*/

            if (model.getPlayingXi().equals("Yes")) {
                holder.team_xi_sign.setVisibility(View.VISIBLE);
                holder.team_xi_sign.setImageResource(R.drawable.play);

                if (TextUtils.isEmpty(model.getOther_text2())){
                    holder.team_xi_text.setVisibility(View.GONE);
                }
                else {
                    holder.team_xi_text.setVisibility(View.VISIBLE);
                    holder.team_xi_text.setText(model.getOther_text2());
                    if (model.getOther_text2().contains("Unavailable")) {
                        holder.team_xi_text.setTextColor(context.getResources().getColor(R.color.red));
                    } else {
                        holder.team_xi_text.setTextColor(context.getResources().getColor(R.color.green_pure));
                    }
                }
                /*if (model.getOther_text().equalsIgnoreCase("substitute")){
                    holder.imgAnnounceBottom.setImageResource(R.drawable.purple_announce_bottom);
                    holder.imgAnnounceLine.setImageResource(R.drawable.purple_line);
                    holder.txtAnnounce.setText("Substitute");
                }
                else {
                    holder.imgAnnounceBottom.setImageResource(R.drawable.green_announce_bottom);
                    holder.imgAnnounceLine.setImageResource(R.drawable.green_line);
                    holder.txtAnnounce.setText("Announced");
                }*/

                /*if (TextUtils.isEmpty(model.getOther_text2())){
                    holder.team_xi_text.setText("Playing");
                }else {
                    holder.team_xi_text.setText(model.getOther_text2().trim());
                }*/
            }
            else if (model.getPlayingXi().equals("No")) {

                holder.team_xi_sign.setVisibility(View.VISIBLE);
                if (model.getOther_text().equalsIgnoreCase("substitute")){
                    holder.team_xi_sign.setImageResource(R.drawable.substitute_dot);
                }else
                    holder.team_xi_sign.setImageResource(R.drawable.nonplay);

                holder.team_xi_text.setVisibility(View.GONE);

                /*holder.imgAnnounceBottom.setImageResource(R.drawable.orange_announce_bottom);
                holder.imgAnnounceLine.setImageResource(R.drawable.orange_line);
                holder.txtAnnounce.setText("Unannounced");*/

            }
            else {
                //holder.layAnnounce.setVisibility(View.GONE);

                holder.team_xi_sign.setVisibility(View.GONE);
                holder.team_xi_text.setVisibility(View.GONE);
            }
        }
        else {
            //holder.layAnnounce.setVisibility(View.GONE);

            holder.team_xi_sign.setVisibility(View.GONE);
           // holder.team_xi_text.setVisibility(View.GONE);
            holder.team_xi_text.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(model.getOther_text().trim())){
                holder.team_xi_text.setText("◉ "+model.getOther_text().trim());
            }else {
                holder.team_xi_text.setText(model.getOther_text().trim());
            }

            holder.team_xi_text.setTextColor(context.getResources().getColor(R.color.orange_selection));

            if (!TextUtils.isEmpty(model.getOther_text2())){
                holder.team_xi_text.setVisibility(View.VISIBLE);
                holder.team_xi_text.setText(model.getOther_text2());
                if (model.getOther_text2().contains("Unavailable")) {
                    holder.team_xi_text.setTextColor(context.getResources().getColor(R.color.red));
                }
            }


        }

        holder.avg_points.setText(model.getPlayerAvgPoint());
        DecimalFormat precision = new DecimalFormat("0.0");
        holder.credit_points.setText(precision.format(Double.parseDouble(model.getPlayerRank())));
        holder.player_name.setText(model.getPlayerSname());
       // Log.e("plyrname",model.getPlayerType()+"   "+model.getPlayerSname());
        //holder.plus_minus_sign.setImageResource(R.drawable.ic_plus);
        if(model.getTeamId().equals(preferences.getMatchModel().getTeam1())){
            holder.player_team.setBackground(context.getResources().getDrawable(R.drawable.team2_name_select_player));
            holder.player_team.setTextColor(context.getResources().getColor(R.color.font_color));
            holder.player_team.setText(preferences.getMatchModel().getTeam1Sname());
        }else{
            holder.player_team.setBackground(context.getResources().getDrawable(R.drawable.team1_name_select_player));
            holder.player_team.setTextColor(context.getResources().getColor(R.color.pureWhite));
            holder.player_team.setText(preferences.getMatchModel().getTeam2Sname());
        }

        if (preferences.getUpdateMaster().getIs_enable_selc_per().equalsIgnoreCase("yes") &&
                !TextUtils.isEmpty(model.getPlayer_percent())){
            holder.selected_by.setVisibility(View.VISIBLE);
            if (preferences.getMatchModel().getTeamAXi().equalsIgnoreCase("yes") ||
                    preferences.getMatchModel().getTeamBXi().equalsIgnoreCase("yes")) {
                holder.selected_by.setText(" | Sel By "+model.getPlayer_percent()+"%");
            }else {
                holder.selected_by.setText("Sel By "+model.getPlayer_percent()+"%");
            }

        }else {
            holder.selected_by.setVisibility(View.GONE);
        }

        String sportId=preferences.getMatchModel().getSportId();

        if (sportId.equals("1")){
            if(model.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.ic_team1_tshirt);
            }else if(model.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.ic_team2_tshirt);
            }
        }
        else if (sportId.equals("2")){
            if(model.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.football_player1);
            }else if(model.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.football_player2);
            }
        }
        else if (sportId.equals("4")){
            if(model.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.basketball_team1);
            }else if(model.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.basketball_team2);
            }
        }
        else if (sportId.equals("3")){
            if(model.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),
                        R.drawable.baseball_player1);
            }else if(model.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.baseball_player2);
            }
        }
        else if (sportId.equals("6")){
            if(model.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),
                        R.drawable.handball_player1);
            }else if(model.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.handball_player2);
            }
        }
        else if (sportId.equals("7")){
            if(model.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),
                        R.drawable.kabaddi_player1);
            }else if(model.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),
                        R.drawable.kabaddi_player2);
            }
        }


        holder.player_image.setOnClickListener(view -> {
            Intent intent = new Intent(context, PlayerStatsActivity.class);
            intent.putExtra("league_id",preferences.getMatchModel().getLeagueId());
            Gson gson = new Gson();
            intent.putExtra("playerModel",gson.toJson(model));
            context.startActivity(intent);
        });

        holder.select_card.setClickable(true);
        holder.select_card.setOnClickListener(view -> {
            if (preferences.getMatchModel().getSportId().equals("1")){
                if(((CricketSelectPlayerFragment)fragment).validateCricketClick(model)){
                    //Log.e(TAG, "onClick: IF");
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }else if (preferences.getMatchModel().getSportId().equals("2")){
                if(((CricketSelectPlayerFragment)fragment).validateFootballClick(model)){
                    //Log.e(TAG, "onClick: IF");
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }else if (preferences.getMatchModel().getSportId().equals("4")){
                if(((CricketSelectPlayerFragment)fragment).validateBasketballClick(model)){
                    //Log.e(TAG, "onClick: IF");
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }else if (preferences.getMatchModel().getSportId().equals("6")){
                if(((CricketSelectPlayerFragment)fragment).validateHandballClick(model)){
                    //Log.e(TAG, "onClick: IF");
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }else if (preferences.getMatchModel().getSportId().equals("7")){
                if(((CricketSelectPlayerFragment)fragment).validateKabaddiClick(model)){
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }else if (preferences.getMatchModel().getSportId().equals("3")){
                if(((CricketSelectPlayerFragment)fragment).validateBaseballClick(model)){
                    //Log.e(TAG, "onClick: IF");
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return player_list.size();
    }

    public class SelectPlayerHolder extends RecyclerView.ViewHolder{

        ImageView player_image,plus_minus_sign,team_xi_sign/*,imgAnnounceLine,imgAnnounceBottom*/;
        TextView player_name,avg_points,credit_points,player_team,team_xi_text,selected_by/*,txtAnnounce*/;
        CardView select_card;
        LinearLayout select_linear_layout;
        //RelativeLayout layAnnounce;
        private boolean isAnnounced=true,isUnannounced=true;

        public SelectPlayerHolder(@NonNull View itemView) {
            super(itemView);

            player_image = itemView.findViewById(R.id.player_image);
            plus_minus_sign = itemView.findViewById(R.id.plus_minus_sign);
            player_name = itemView.findViewById(R.id.player_name);
            player_team = itemView.findViewById(R.id.player_team);
            avg_points = itemView.findViewById(R.id.avg_points);
            credit_points = itemView.findViewById(R.id.credit_points);
            select_card = itemView.findViewById(R.id.select_card);
            select_linear_layout = itemView.findViewById(R.id.select_linear_layout);
            team_xi_sign = itemView.findViewById(R.id.team_xi_sign);
            team_xi_text = itemView.findViewById(R.id.team_xi_text);
            selected_by = itemView.findViewById(R.id.selected_by);
           // imgAnnounceLine = itemView.findViewById(R.id.imgAnnounceLine);
            //imgAnnounceBottom = itemView.findViewById(R.id.imgAnnounceBottom);
            //txtAnnounce = itemView.findViewById(R.id.txtAnnounce);
            //layAnnounce = itemView.findViewById(R.id.layAnnounce);
        }
    }

    public void updateList(List<PlayerModel> playerModelList) {
        Log.e("plyrname", "updateList: " );
        /*for (PlayerModel model:playerModelList){
            Log.e("plyrname",model.getPlayerType()+"   "+model.getPlayerName()+"   "+model.getPlayerSname());
        }*/
        this.player_list.clear();
        this.player_list.addAll(playerModelList);
        notifyDataSetChanged();
    }

}
