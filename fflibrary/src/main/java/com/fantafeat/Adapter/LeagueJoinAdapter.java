package com.fantafeat.Adapter;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Activity.TeamSelectJoinActivity;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.PlayerListModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.BottomSheetTeam;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.LogUtil;

import java.util.List;

public class LeagueJoinAdapter extends RecyclerView.Adapter<LeagueJoinAdapter.LeagueJoinHolder> {

    private Context mContext;
    private List<PlayerListModel> playerListModels;
    private TeamSelectJoinActivity teamSelectJoinActivity;
    private static final String TAG = "LeagueJoinAdapter";
    MyPreferences preferences;
    private int maxTeamJoin;

    public LeagueJoinAdapter(Context mContext, List<PlayerListModel> playerListModels, TeamSelectJoinActivity teamSelectJoinActivity,int maxTeam) {
        this.mContext = mContext;
        this.playerListModels = playerListModels;
        this.teamSelectJoinActivity = teamSelectJoinActivity;
        preferences = MyApp.getMyPreferences();
        this.maxTeamJoin = maxTeam;

    }

    @NonNull
    @Override
    public LeagueJoinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LeagueJoinHolder(LayoutInflater.from(mContext).inflate(R.layout.row_team_join_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final LeagueJoinHolder holder, int position) {
        final PlayerListModel list = playerListModels.get(position);

        MatchModel matchModel=preferences.getMatchModel();

        if (matchModel.getSportId().equals("1")){
            holder.txtWK.setText("Wicket Keeper");
            holder.txtBat.setText("Batsman");
            holder.txtAr.setText("All Rounder");
            holder.txtBowl.setText("Bowler");
        }else if (matchModel.getSportId().equals("2")){
            holder.txtWK.setText("Goal Keeper");
            holder.txtBat.setText("Defender");
            holder.txtAr.setText("Mid Fielder");
            holder.txtBowl.setText("Forward");
        }else if (matchModel.getSportId().equals("4")){
            holder.layCr.setVisibility(View.VISIBLE);
            holder.viewCr.setVisibility(View.VISIBLE);

            holder.txtWK.setText("PG");
            holder.txtBat.setText("SG");
            holder.txtAr.setText("SF");
            holder.txtBowl.setText("PF");
            holder.txtCr.setText("CR");
        }else if (matchModel.getSportId().equals("6")){
            holder.layCr.setVisibility(View.GONE);
            holder.viewCr.setVisibility(View.GONE);
            holder.layBowl.setVisibility(View.GONE);
            holder.viewBowl.setVisibility(View.GONE);

            holder.txtWK.setText("Goal Keeper");
            holder.txtBat.setText("Defender");
            holder.txtAr.setText("Forward");
        }else if (matchModel.getSportId().equals("3")){
            holder.txtWK.setText("OF");
            holder.txtBat.setText("IF");
            holder.txtAr.setText("PIT");
            holder.txtBowl.setText("CAT");
        }else if (matchModel.getSportId().equals("7")){
            holder.layCr.setVisibility(View.GONE);
            holder.viewCr.setVisibility(View.GONE);
            holder.layBowl.setVisibility(View.GONE);
            holder.viewBowl.setVisibility(View.GONE);

            holder.txtWK.setText("Defender");
            holder.txtBat.setText("All Rounder");
            holder.txtAr.setText("Raider");
        }

     /*   if (((TeamSelectJoinActivity) mContext).selectPosition == holder.getAdapterPosition()) {
            holder.team_select_img.setChecked(true);
        } else {
            holder.team_select_img.setChecked(false);
        }*/
        holder.team_select_img.setChecked(list.isChecked());

        if (list.getIsSelected().equalsIgnoreCase("Yes")) {

            holder.linear4.setEnabled(false);
            holder.linear4.setClickable(false);
            holder.team_select_img.setChecked(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.matchCard.setForeground(mContext.getResources().getDrawable(R.drawable.disable_match));
            }

        } else {
            holder.linear4.setEnabled(true);
            holder.linear4.setClickable(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.matchCard.setForeground(mContext.getResources().getDrawable(R.drawable.transparent_view));
            }
        }

        holder.txtTeamname.setText(list.getTempTeamName());

        String c = list.getC_player_name();
      //  String[] c_names = c.split(" ");
      //  holder.c_first_name.setText(c_names[0]);
        holder.c_last_name.setText(c);

        String vc = list.getVc_player_name();
       // String[] vc_names = vc.split(" ");
       // holder.vc_first_name.setText(vc_names[0]);
        holder.vc_last_name.setText(vc);

        holder.txtTeam1Count.setText("  " + list.getTeam1_count());
        holder.txtTeam2Count.setText("  " + list.getTeam2_count());
        holder.txtTeam1Name.setText(matchModel.getTeam1Sname()+" :");
        holder.txtTeam2Name.setText(matchModel.getTeam2Sname()+" :");

        holder.txtBastman.setText("" + list.getBat_count());
        holder.txtWicketK.setText("" + list.getWk_count());
        holder.txtAllrounder.setText("" + list.getAr_count());
        holder.txtBowler.setText("" + list.getBowl_count());
        holder.txtCenter.setText("" + list.getCr_count());

        if (matchModel.getSportId().equals("1")){
            if (list.getCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.ic_team1_tshirt);
            }else if (list.getCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.ic_team2_tshirt);
            }

            if (list.getVCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.ic_team1_tshirt);
            }else if (list.getVCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.ic_team2_tshirt);
            }

        }
        else if (matchModel.getSportId().equals("2")){
            if (list.getCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.football_player1);
            }else if (list.getCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.football_player2);
            }

            if (list.getVCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.football_player1);
            }else if (list.getVCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.football_player2);
            }
        }
        else if (matchModel.getSportId().equals("4")){
            if (list.getCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.basketball_team1);
            }else if (list.getCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.basketball_team2);
            }

            if (list.getVCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.basketball_team1);
            }else if (list.getVCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.basketball_team2);
            }
        }
        else if (matchModel.getSportId().equals("3")){
            if (list.getCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.baseball_player1);
            }else if (list.getCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.baseball_player2);
            }

            if (list.getVCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.baseball_player1);
            }else if (list.getVCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.baseball_player2);
            }
        }
        else if (matchModel.getSportId().equals("6")){
            if (list.getCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.handball_player1);
            }else if (list.getCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.handball_player2);
            }

            if (list.getVCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.handball_player1);
            }else if (list.getVCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.handball_player2);
            }
        }
        else if (matchModel.getSportId().equals("7")){
            if (list.getCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.kabaddi_player1);
            }else if (list.getCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.kabaddi_player2);
            }

            if (list.getVCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.kabaddi_player1);
            }else if (list.getVCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.kabaddi_player2);
            }
        }

        /*if(preferences.getMatchModel().getMatchStatus().toLowerCase().equals("pending")) {
            if (preferences.getMatchModel().getTeamAXi().toLowerCase().equals("yes") ||
                    preferences.getMatchModel().getTeamBXi().toLowerCase().equals("yes")) {
                holder.txtLineups.setVisibility(View.VISIBLE);
                if (CustomUtil.convertInt(list.getLineup_count()) == 0) {
                    holder.txtLineups.setText("All Players are in lineup");
                    holder.txtLineups.setBackgroundResource(R.drawable.curve_bottom_selected);
                }else if (CustomUtil.convertInt(list.getLineup_count()) == 1) {
                    holder.txtLineups.setText(list.getLineup_count() + " Player is not in lineup");
                    holder.txtLineups.setBackgroundResource(R.drawable.curve_bottom_unselected);
                }else  {
                    holder.txtLineups.setText(list.getLineup_count() + " Players are not in lineup");
                    holder.txtLineups.setBackgroundResource(R.drawable.curve_bottom_unselected);
                }
            }else {
                holder.txtLineups.setVisibility(View.GONE);
            }
        }
        else if(preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("playing") ||
                preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("In review")){
            holder.txtLineups.setVisibility(View.VISIBLE);
            if (CustomUtil.convertInt(list.getLineup_count()) == 0) {
                holder.txtLineups.setText("All Players are in lineup");
                holder.txtLineups.setBackgroundResource(R.drawable.curve_bottom_selected);
            }else if (CustomUtil.convertInt(list.getLineup_count()) == 1) {
                holder.txtLineups.setText(list.getLineup_count() + " Player is not in lineup");
                holder.txtLineups.setBackgroundResource(R.drawable.curve_bottom_unselected);
            }else  {
                holder.txtLineups.setText(list.getLineup_count() + " Players are not in lineup");
                holder.txtLineups.setBackgroundResource(R.drawable.curve_bottom_unselected);
            }
        }
        else if(preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("completed")){
            holder.txtLineups.setVisibility(View.VISIBLE);
            if (CustomUtil.convertInt(list.getLineup_count()) == 0) {
                holder.txtLineups.setText("All Players were in lineup");
                holder.txtLineups.setBackgroundResource(R.drawable.curve_bottom_selected);
            }else if (CustomUtil.convertInt(list.getLineup_count()) == 1) {
                holder.txtLineups.setText(list.getLineup_count() + " Player was not in lineup");
                holder.txtLineups.setBackgroundResource(R.drawable.curve_bottom_unselected);
            }else  {
                holder.txtLineups.setText(list.getLineup_count() + " Players were not in lineup");
                holder.txtLineups.setBackgroundResource(R.drawable.curve_bottom_unselected);
            }
        }*/

        holder.linear4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e(TAG, "onClick: " + holder.getAdapterPosition());
                if (ApiManager.IS_MULTIJOIN && maxTeamJoin > 1){
                    if (list.isChecked()){
                        holder.team_select_img.setChecked(false);
                        list.setChecked(false);
                        teamSelectJoinActivity.singleCheck(position,false);
                    }else {
                        holder.team_select_img.setChecked(true);
                        list.setChecked(true);
                        teamSelectJoinActivity.singleCheck(position,true);
                    }
                    teamSelectJoinActivity.team_join_confirm_btn.setVisibility(View.VISIBLE);
                }else {
                    for (int k=0;k<playerListModels.size();k++){
                        PlayerListModel modal=playerListModels.get(k);
                        modal.setChecked(false);
                    }
                    teamSelectJoinActivity.selectPosition = holder.getAdapterPosition();
                    holder.team_select_img.setChecked(true);
                    list.setChecked(true);
                    notifyDataSetChanged();
                    teamSelectJoinActivity.team_join_confirm_btn.setVisibility(View.VISIBLE);
                }
               /* holder.team_select_img.setChecked(true);
                ((TeamSelectJoinActivity) mContext).team_join_confirm_btn.setVisibility(View.VISIBLE);
                notifyItemChanged(((TeamSelectJoinActivity) mContext).selectPosition);
                ((TeamSelectJoinActivity) mContext).selectPosition = holder.getAdapterPosition();*/
            }
        });

        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()){
                    BottomSheetTeam bottomSheetTeam = new BottomSheetTeam(mContext, list.getTempTeamName(), list.getPlayerDetailList());
                    bottomSheetTeam.show(((TeamSelectJoinActivity) mContext).getSupportFragmentManager(),
                            BottomSheetTeam.TAG);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return playerListModels.size();
    }

    protected class LeagueJoinHolder extends RecyclerView.ViewHolder {
        public CardView matchCard;
        public TextView txtTeamname;

        public LinearLayout cardLinear;
        public LinearLayout linear4,layCr,layBowl;
        public View viewCr,viewBowl;
        public TextView txtCaption,txtCr,txtCenter;
        public TextView txtTeam1Name,txtWK,txtBat,txtAr,txtBowl;
        public ImageView imgTeam1;
        public TextView txtTeam1Count;
        public TextView txtTeam2Name;
        public TextView txtTeam2Count;
        public ImageView imgTeam2;
        public ImageView show;
        public TextView txtWicketK;
        public TextView txtBastman,txtLineups;
        public TextView txtAllrounder;
        public CheckBox team_select_img;
        public TextView txtBowler,c_last_name,vc_last_name;//c_first_name,vc_first_name,

        public LeagueJoinHolder(@NonNull View itemView) {
            super(itemView);

            matchCard = (CardView)itemView.findViewById( R.id.match_card );
            txtTeamname = (TextView)itemView.findViewById( R.id.txtTeamname );

            cardLinear = (LinearLayout)itemView.findViewById( R.id.card_linear );
            linear4 = (LinearLayout)itemView.findViewById( R.id.linear4 );
            txtCaption = (TextView)itemView.findViewById( R.id.txtCaption );
            imgTeam1 = (ImageView)itemView.findViewById( R.id.img_team1 );
            txtTeam1Name = (TextView)itemView.findViewById( R.id.txt_team1_name );
            txtTeam1Count = (TextView)itemView.findViewById( R.id.txt_team1_count );
            txtTeam2Name = (TextView)itemView.findViewById( R.id.txt_team2_name );
            txtTeam2Count = (TextView)itemView.findViewById( R.id.txt_team2_count );
            imgTeam2 = (ImageView)itemView.findViewById( R.id.img_team2 );
            show = (ImageView)itemView.findViewById( R.id.show );
            txtWicketK = (TextView)itemView.findViewById( R.id.txtWicket_k );
            txtBastman = (TextView)itemView.findViewById( R.id.txtBastman );
            txtAllrounder = (TextView)itemView.findViewById( R.id.txtAllrounder );
            txtBowler = (TextView)itemView.findViewById( R.id.txtBowler );
            viewCr = itemView.findViewById(R.id.viewCr);
            txtCr = (TextView) itemView.findViewById(R.id.txtCr);
            txtCenter = (TextView) itemView.findViewById(R.id.txtCenter);
            layCr = (LinearLayout) itemView.findViewById(R.id.layCr);
          //  c_first_name = (TextView)itemView.findViewById( R.id.c_first_name );
            c_last_name = (TextView)itemView.findViewById( R.id.c_last_name );
          //  vc_first_name = (TextView)itemView.findViewById( R.id.vc_first_name );
            vc_last_name = (TextView)itemView.findViewById( R.id.vc_last_name );
            team_select_img = (CheckBox) itemView.findViewById( R.id.team_select_img );

            txtWK = (TextView) itemView.findViewById(R.id.txtWK);
            txtBat = (TextView) itemView.findViewById(R.id.txtBat);
            txtAr = (TextView) itemView.findViewById(R.id.txtAr);
            txtBowl = (TextView) itemView.findViewById(R.id.txtBowl);

            viewBowl = itemView.findViewById(R.id.viewBowl);
            layBowl = itemView.findViewById(R.id.layBowl);
            txtLineups = itemView.findViewById(R.id.txtLineups);

        }
    }
}
