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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Activity.AfterMatchActivity;
import com.fantafeat.Activity.ContestListActivity;
import com.fantafeat.Activity.CricketSelectPlayerActivity;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.PlayerListModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.BottomSheetTeam;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.PrefConstant;
import com.google.gson.Gson;

import java.util.List;


public class MyTeamListAdapter extends RecyclerView.Adapter<MyTeamListAdapter.MyTeamListHolder> {

    private Context mContext;
    private List<PlayerListModel> playerListModelList;
    MyPreferences preferences;
    int flag = 0;

    public MyTeamListAdapter(Context mContext, List<PlayerListModel> playerListModelList, int flag) {
        this.mContext = mContext;
        this.playerListModelList = playerListModelList;
        preferences = MyApp.getMyPreferences();
        this.flag = flag;
    }

    @NonNull
    @Override
    public MyTeamListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyTeamListHolder(LayoutInflater.from(mContext).inflate(R.layout.row_my_team, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyTeamListHolder holder, int position) {
        final PlayerListModel list = playerListModelList.get(holder.getAdapterPosition());

        MatchModel matchModel=preferences.getMatchModel();
        //txtWK,txtBat,txtAr,txtBowl
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
        }
        else if (matchModel.getSportId().equals("4")){
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
        }else if (matchModel.getSportId().equals("7")){
            holder.layCr.setVisibility(View.GONE);
            holder.viewCr.setVisibility(View.GONE);
            holder.layBowl.setVisibility(View.GONE);
            holder.viewBowl.setVisibility(View.GONE);

            holder.txtWK.setText("Defender");
            holder.txtBat.setText("All Rounder");
            holder.txtAr.setText("Raider");
        }else if (matchModel.getSportId().equals("3")){
            holder.txtWK.setText("OF");
            holder.txtBat.setText("IF");
            holder.txtAr.setText("PIT");
            holder.txtBowl.setText("CAT");
        }

        if (matchModel.getSportId().equals("1")){
            if (list.getCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.ic_team1_tshirt);
            }else if (list.getCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.ic_team2_tshirt);
            }
        }
        else if (matchModel.getSportId().equals("2")){
            if (list.getCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.football_player1);
            }else if (list.getCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.football_player2);
            }
        }
        else if (matchModel.getSportId().equals("4")){
            if (list.getCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.basketball_team1);
            }else if (list.getCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.basketball_team2);
            }
        }
        else if (matchModel.getSportId().equals("3")){
            if (list.getCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.baseball_player1);
            }else if (list.getCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.baseball_player2);
            }
        }
        else if (matchModel.getSportId().equals("6")){
            if (list.getCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.handball_player1);
            }else if (list.getCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.handball_player2);
            }
        }
        else if (matchModel.getSportId().equals("7")){
            if (list.getCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.kabaddi_player1);
            }else if (list.getCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.kabaddi_player2);
            }
        }

        if (matchModel.getSportId().equals("1")){
            if (list.getVCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.ic_team1_tshirt);
            }else if (list.getVCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.ic_team2_tshirt);
            }
        }
        else if (matchModel.getSportId().equals("2")){
            if (list.getVCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.football_player1);
            }else if (list.getVCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.football_player2);
            }
        }
        else if (matchModel.getSportId().equals("4")){
            if (list.getVCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.basketball_team1);
            }else if (list.getVCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.basketball_team2);
            }
        }
        else if (matchModel.getSportId().equals("3")){
            if (list.getVCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.baseball_player1);
            }else if (list.getVCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.baseball_player2);
            }
        }
        else if (matchModel.getSportId().equals("6")){
            if (list.getVCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.handball_player1);
            }else if (list.getVCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.handball_player2);
            }
        }
        else if (matchModel.getSportId().equals("7")){
            if (list.getVCTeam_id().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.kabaddi_player1);
            }else if (list.getVCTeam_id().equals(matchModel.getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.kabaddi_player2);
            }
        }

        /*if(preferences.getMatchModel().getMatchStatus().toLowerCase().equals("pending")) {
            if (preferences.getMatchModel().getTeamAXi().toLowerCase().equals("yes") || preferences.getMatchModel().getTeamBXi().toLowerCase().equals("yes")) {
                //Log.e(TAG, "onBindViewHolder: " + list.getLineup_count());
                if (CustomUtil.convertInt(list.getLineup_count()) == 0) {
                    holder.player_count.setText("All Players");
                    holder.player_count.setTextColor(mContext.getResources().getColor(R.color.green));
                    holder.lineup_count_text_sec.setText(" are in lineup");
                } else if(CustomUtil.convertInt(list.getLineup_count()) == 1) {
                    holder.player_count.setText(list.getLineup_count() + " Player");
                    holder.player_count.setTextColor(mContext.getResources().getColor(R.color.red));
                    holder.lineup_count_text_sec.setText(" is not in lineup");
                }else{
                    holder.player_count.setText(list.getLineup_count() + " Players");
                    holder.player_count.setTextColor(mContext.getResources().getColor(R.color.red));
                    holder.lineup_count_text_sec.setText(" are not in lineup");
                }
            } else {
                holder.lineup_text.setVisibility(View.INVISIBLE);
            }
        }else if(preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("playing") || preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("In review")){
            if (CustomUtil.convertInt(list.getLineup_count()) == 0) {
                holder.player_count.setText("All Players");
                holder.player_count.setTextColor(mContext.getResources().getColor(R.color.green));
                holder.lineup_count_text_sec.setText(" are in lineup");
            } else if(CustomUtil.convertInt(list.getLineup_count()) == 1) {
                holder.player_count.setText(list.getLineup_count() + " Player");
                holder.player_count.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.lineup_count_text_sec.setText(" is not in lineup");
            }else{
                holder.player_count.setText(list.getLineup_count() + " Players");
                holder.player_count.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.lineup_count_text_sec.setText(" are not in lineup");
            }
        }else if(preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("completed")){
            if (CustomUtil.convertInt(list.getLineup_count()) == 0) {
                holder.player_count.setText("All Players");
                holder.player_count.setTextColor(mContext.getResources().getColor(R.color.green));
                holder.lineup_count_text_sec.setText(" were in lineup");
            } else if(CustomUtil.convertInt(list.getLineup_count()) == 1) {
                holder.player_count.setText(list.getLineup_count() + " Player");
                holder.player_count.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.lineup_count_text_sec.setText(" was not in lineup");
            }else{
                holder.player_count.setText(list.getLineup_count() + " Players");
                holder.player_count.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.lineup_count_text_sec.setText(" were not in lineup");
            }
        }*/

        holder.txtTeamname.setText(list.getTempTeamName());

      //  String c = list.getC_player_name();
       // String[] c_names = c.split(" ");
        //holder.c_first_name.setText(c_names[0]);
        holder.c_last_name.setText(list.getC_player_name());

       // String vc = list.getVc_player_name();
      //  String[] vc_names = vc.split(" ");
        //holder.vc_first_name.setText(vc_names[0]);
        holder.vc_last_name.setText(list.getVc_player_name());

        holder.txtTeam1Count.setText("  " + list.getTeam1_count());
        holder.txtTeam2Count.setText("  " + list.getTeam2_count());
        holder.txtTeam1Name.setText(preferences.getMatchModel().getTeam1Sname() + " :");
        holder.txtTeam2Name.setText(preferences.getMatchModel().getTeam2Sname() + " :");

        holder.txtBastman.setText("" + list.getBat_count());
        holder.txtWicketK.setText("" + list.getWk_count());
        holder.txtAllrounder.setText("" + list.getAr_count());
        holder.txtBowler.setText("" + list.getBowl_count());
        holder.txtCenter.setText("" + list.getCr_count());

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

        if (flag == 0) {
            holder.flag_0.setVisibility(View.VISIBLE);
            holder.flag_1.setVisibility(View.GONE);

            holder.imgCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MyApp.getClickStatus()) {
                        if (preferences.getPlayerModel().size() <
                                CustomUtil.convertInt(preferences.getUpdateMaster().getMaxTeamCount())) {
                        /*new FragmentUtil().addFragment((FragmentActivity) mContext,
                                R.id.fragment_container,
                                new SelectPlayerFragment(list, false),
                                ((HomeActivity) mContext).fragmentTag(8),
                                FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
                            Gson gson = new Gson();
                            Intent intent = new Intent(mContext, CricketSelectPlayerActivity.class);
                            intent.putExtra(PrefConstant.TEAMCREATETYPE, -1);
                            intent.putExtra("team", gson.toJson(list));
                            mContext.startActivity(intent);
                        } else {
                            CustomUtil.showTopSneakError(mContext, "Max "
                                    + preferences.getUpdateMaster().getMaxTeamCount()
                                    + " teams allowed to create");
                        }
                    }
                }
            });

            holder.imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MyApp.getClickStatus()) {
                     //   preferences.setMatchModel(matchModel);
                        BottomSheetTeam bottomSheetTeam = new BottomSheetTeam(mContext, "", list.getPlayerDetailList(), 1);
                        bottomSheetTeam.show(((FragmentActivity) mContext).getSupportFragmentManager(),
                                BottomSheetTeam.TAG);
                   /* new FragmentUtil().addFragment((FragmentActivity) mContext,
                            R.id.fragment_container,
                            new ShareFeedFragment(list),
                            ((HomeActivity) mContext).fragmentTag(47),
                            FragmentUtil.ANIMATION_TYPE.CUSTOM);*/

                    }
                }
            });

            holder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MyApp.getClickStatus()) {
                    /*new FragmentUtil().addFragment((FragmentActivity) mContext,
                            R.id.fragment_container,
                            new SelectPlayerFragment(list, true),
                            ((HomeActivity) mContext).fragmentTag(8),
                            FragmentUtil.ANIMATION_TYPE.CUSTOM);
*/
                        Gson gson = new Gson();
                        Intent intent = new Intent(mContext, CricketSelectPlayerActivity.class);
                        intent.putExtra(PrefConstant.TEAMCREATETYPE, 1);
                        intent.putExtra("team", gson.toJson(list));
                        mContext.startActivity(intent);
                    }
                }
            });
        } else {
            holder.imgCopy.setVisibility(View.INVISIBLE);
            //holder.imgShare.setVisibility(View.INVISIBLE);
            holder.imgEdit.setVisibility(View.INVISIBLE);
            holder.flag_0.setVisibility(View.GONE);
            holder.flag_1.setVisibility(View.VISIBLE);

           // holder.points.setText("Points: " + list.getTotal_points());
            holder.points.setText("Points: " + CustomUtil.getFormater("0.00").format(list.getTotal_points()));
        }

        holder.matchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()){
                    if(flag == 0) {
                        BottomSheetTeam bottomSheetTeam = new BottomSheetTeam(mContext, list, true);
                        bottomSheetTeam.show(((ContestListActivity) mContext).getSupportFragmentManager(),
                                BottomSheetTeam.TAG);
                    }else{
                        BottomSheetTeam bottomSheetTeam = new BottomSheetTeam(mContext, list, true);
                        bottomSheetTeam.show(((AfterMatchActivity) mContext).getSupportFragmentManager(),
                                BottomSheetTeam.TAG);
                    }
                }

            }
        });

       /* if (preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("Cancelled")) {
            holder.expand_view.setEnabled(false);
            holder.lineup_text.setVisibility(View.INVISIBLE);
            holder.flag_1.setVisibility(View.INVISIBLE);
            holder.flag_0.setVisibility(View.INVISIBLE);
        }else {
            holder.expand_view.setEnabled(true);
            holder.expand_view.setBackground(mContext.getResources().getDrawable(R.drawable.transparent_view));
        }
*/

    }

    @Override
    public int getItemCount() {
        try {
            return playerListModelList.size();
        } catch (Exception e) {
            LogUtil.e("error", "getItemCount: " + e);
        }
        return 0;

    }

    protected class MyTeamListHolder extends RecyclerView.ViewHolder {
        public CardView matchCard;
        public TextView txtTeamname;
        public ImageView imgEdit;
        public ImageView imgCopy;
        public ImageView imgShare;
        public LinearLayout cardLinear,layCr,layBowl;
        public LinearLayout flag_0;
        public LinearLayout flag_1;
        public TextView txtCaption;
        public ImageView imgTeam1;
        public TextView txtTeam1Name,txtWK,txtBat,txtAr,txtBowl,txtCr,txtCenter;
        public View viewCr,viewBowl;
        public TextView txtTeam1Count;
        public TextView txtTeam2Name;
        public TextView txtTeam2Count;
        public ImageView imgTeam2;
        public TextView txtWicketK;
        public TextView txtBastman;
        public TextView points,txtLineups;
        public TextView txtAllrounder;
        public TextView txtBowler,c_last_name,vc_last_name;

        public MyTeamListHolder(@NonNull View itemView) {
            super(itemView);

            matchCard = (CardView) itemView.findViewById(R.id.match_card);
            txtTeamname = (TextView) itemView.findViewById(R.id.txtTeamname);
            imgEdit = (ImageView) itemView.findViewById(R.id.imgEdit);
            imgCopy = (ImageView) itemView.findViewById(R.id.imgCopy);
            imgShare = (ImageView) itemView.findViewById(R.id.imgShare);
            imgShare.setVisibility(View.GONE);
            cardLinear = (LinearLayout) itemView.findViewById(R.id.card_linear);
            layCr = (LinearLayout) itemView.findViewById(R.id.layCr);
            flag_0 = (LinearLayout) itemView.findViewById(R.id.flag_0);
            flag_1 = (LinearLayout) itemView.findViewById(R.id.flag_1);
            txtCaption = (TextView) itemView.findViewById(R.id.txtCaption);
            imgTeam1 = (ImageView) itemView.findViewById(R.id.img_team1);
            txtTeam1Name = (TextView) itemView.findViewById(R.id.txt_team1_name);
            txtTeam1Count = (TextView) itemView.findViewById(R.id.txt_team1_count);
            txtTeam2Name = (TextView) itemView.findViewById(R.id.txt_team2_name);
            txtTeam2Count = (TextView) itemView.findViewById(R.id.txt_team2_count);
            imgTeam2 = (ImageView) itemView.findViewById(R.id.img_team2);
            txtWicketK = (TextView) itemView.findViewById(R.id.txtWicket_k);
            txtBastman = (TextView) itemView.findViewById(R.id.txtBastman);
            txtAllrounder = (TextView) itemView.findViewById(R.id.txtAllrounder);
            txtBowler = (TextView) itemView.findViewById(R.id.txtBowler);
            //,,,
            viewCr = itemView.findViewById(R.id.viewCr);
            txtCenter = (TextView) itemView.findViewById(R.id.txtCenter);
            txtWK = (TextView) itemView.findViewById(R.id.txtWK);
            txtBat = (TextView) itemView.findViewById(R.id.txtBat);
            txtAr = (TextView) itemView.findViewById(R.id.txtAr);
            txtBowl = (TextView) itemView.findViewById(R.id.txtBowl);
            txtCr = (TextView) itemView.findViewById(R.id.txtCr);
           // c_first_name = (TextView) itemView.findViewById(R.id.c_first_name);
            c_last_name = (TextView) itemView.findViewById(R.id.c_last_name);
           // vc_first_name = (TextView) itemView.findViewById(R.id.vc_first_name);
            vc_last_name = (TextView) itemView.findViewById(R.id.vc_last_name);
            points = (TextView) itemView.findViewById(R.id.points);
            viewBowl = itemView.findViewById(R.id.viewBowl);
            layBowl = itemView.findViewById(R.id.layBowl);
            txtLineups = itemView.findViewById(R.id.txtLineups);

        }
    }

    public void updateData(List<PlayerListModel> playerListModelList) {
        this.playerListModelList = playerListModelList;
        notifyDataSetChanged();
    }
}
