package com.fantafeat.Adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Activity.AfterMatchPlayerStatesActivity;
import com.fantafeat.Activity.ContestListActivity;
import com.fantafeat.Activity.CricketSelectPlayerActivity;
import com.fantafeat.Activity.LeaderBoardActivity;
import com.fantafeat.Activity.SinglesContestActivity;
import com.fantafeat.Activity.TeamSelectJoinActivity;
import com.fantafeat.Activity.UserLeaderboardActivity;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.Model.GroupContestModel;
import com.fantafeat.Model.GroupModel;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.PlayerListModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.BottomSheetTeam;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.PrefConstant;
import com.fantafeat.util.WinningTreeSheet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GroupContestListAdapter extends RecyclerView.Adapter<GroupContestListAdapter.ContestListHolder> {

    private Context mContext;
    private List<GroupContestModel.ContestDatum> contestDatumList;
    private Gson gson;
    private ObjectAnimator anim;
    private Animation animation;
    private int mainPosition = 0;
    private boolean isMyJoined = false;
    private boolean is_match_after = false;

    LayoutInflater layoutInflater;

    public GroupContestListAdapter(Context mContext, List<GroupContestModel.ContestDatum> contestDatumList, Gson gson,int mainPosition,boolean isMyJoined,boolean is_match_after) {
        this.mContext = mContext;
        this.contestDatumList = contestDatumList;
        this.gson = gson;
        this.mainPosition = mainPosition;
        this.isMyJoined = isMyJoined;
        this.is_match_after = is_match_after;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @NonNull
    @Override
    public ContestListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContestListHolder(LayoutInflater.from(mContext).inflate(R.layout.singles_contest_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContestListHolder holder, int position) {
        final GroupContestModel.ContestDatum list = contestDatumList.get(holder.getAdapterPosition());

        holder.contest_price_pool.setText(CustomUtil.displayAmountFloat(mContext, list.getDistributeAmount()));//CustomUtil.displayAmount(mContext, list.getDistributeAmount())
        holder.contest_winner.setText(list.getTotalWinner());

        if (list.getEntryFee().equalsIgnoreCase("0") || list.getEntryFee().equalsIgnoreCase("0.0")||
                list.getEntryFee().equalsIgnoreCase("0.00")){
            holder.txtLblEntry.setText("FREE");
        }else {
            holder.txtLblEntry.setText(mContext.getResources().getString(R.string.rs) + list.getEntryFee().replace(".00",""));
        }

        /*if (TextUtils.isEmpty(list.getDefaultBonus()) || CustomUtil.convertFloat(list.getDefaultBonus())<=0){
            holder.contest_offer_price.setVisibility(View.GONE);
        }else {
            holder.contest_offer_price.setVisibility(View.VISIBLE);
            holder.contest_offer_price.setText("Use "+list.getDefaultBonus()+"% Bonus");
        }

        if (TextUtils.isEmpty(list.getDefault_ff_coins()) || CustomUtil.convertFloat(list.getDefault_ff_coins())<=0){
            holder.txtFantaGems.setVisibility(View.GONE);
        }else {
            holder.txtFantaGems.setVisibility(View.VISIBLE);
            holder.txtFantaGems.setText("Use "+list.getDefault_ff_coins()+"% "+mContext.getResources().getString(R.string.fanta_gems));
        }*/

        //LogUtil.e("condata","pool: "+list.getDistributeAmount()+"   "+list.getCon_win_amount_per_spot_my());

        holder.txtWonLbl.setVisibility(View.GONE);

        if (MyApp.getMyPreferences().getMatchModel().getMatchStatus().equalsIgnoreCase("playing")) {
            if (!TextUtils.isEmpty(list.getCon_win_amount_per_spot_my())){
                float amt=CustomUtil.convertFloat(list.getCon_win_amount_per_spot_my());
                if (amt>0){
                    holder.txtWonLbl.setVisibility(View.VISIBLE);
                    holder.txtWonLbl.setText("In Winning");
                }
            }
        }
        else if (MyApp.getMyPreferences().getMatchModel().getMatchStatus().equalsIgnoreCase("completed")) {
            if (!TextUtils.isEmpty(list.getCon_win_amount_per_spot_my())){
                float amt=CustomUtil.convertFloat(list.getCon_win_amount_per_spot_my());
                if (amt>0){
                    holder.txtWonLbl.setVisibility(View.VISIBLE);
                    holder.txtWonLbl.setText("You Won "+mContext.getResources().getString(R.string.rs)+list.getCon_win_amount_per_spot());
                }
            }
        }
        else {
            holder.txtWonLbl.setVisibility(View.GONE);
        }

        if(list.isDisable()){
            holder.txtLblEntry.setEnabled(false);
            holder.txtLblEntry.setBackground(mContext.getResources().getDrawable(R.drawable.btn_grey));
        }else {
            holder.txtLblEntry.setEnabled(true);
            holder.txtLblEntry.setBackground(mContext.getResources().getDrawable(R.drawable.btn_green));
        }

        int totalSpots = CustomUtil.convertInt(list.getNoTeamJoin());
        int jointeam = CustomUtil.convertInt(list.getJoinedTeams());

        int progress = (jointeam * 100) / totalSpots;
        holder.contest_progress.setProgress(progress);

        if (list.getIsUnlimited().equalsIgnoreCase("Yes")) {

            holder.contest_total_team.setText("Unlimited");

            holder.teams_left_linear.setVisibility(View.VISIBLE);
            holder.contest_full_linear.setVisibility(View.GONE);

        }
        else {
            holder.contest_total_team.setText(list.getNoTeamJoin());

            int spotsLeft = totalSpots - jointeam;
            if(spotsLeft <= 0){
                holder.teams_left_linear.setVisibility(View.GONE);
                holder.contest_full_linear.setVisibility(View.VISIBLE);
            }else {
                holder.teams_left_linear.setVisibility(View.VISIBLE);
                holder.contest_full_linear.setVisibility(View.GONE);
            }
            holder.contest_left_team.setText(spotsLeft+"");
        }

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        param.setMargins(5,0,5,0);
        holder.layPlayers.removeAllViews();

        holder.playersData=new ArrayList<>(list.getPlayersContest());
        if (is_match_after){
            Collections.sort(holder.playersData, new joinCntUp());
        }

        for (GroupContestModel.ContestDatum.PlayersDatum player : holder.playersData)
        {
            View addView;
            if (MyApp.getMyPreferences().getMatchModel().getMatchStatus().equalsIgnoreCase("Pending")) {
                if (player.isDisable()){
                    addView = layoutInflater.inflate(R.layout.selected_player_group, null);
                }else {
                    addView = layoutInflater.inflate(R.layout.group_player_item, null);
                }
            }else {
                addView = layoutInflater.inflate(R.layout.group_player_item, null);
            }


            addView.setLayoutParams(param);

            RelativeLayout teamReviewLayout =  addView.findViewById(R.id.teamReviewLayout);
            ImageView imgEdit =  addView.findViewById(R.id.imgEdit);
            imgEdit.setVisibility(View.GONE);
            TextView playerName =  addView.findViewById(R.id.player_name);
            TextView player_type = addView.findViewById(R.id.player_type);
            player_type.setSelected(true);
            ImageView player_img = addView.findViewById(R.id.player_img);
            ImageView inPlaying = addView.findViewById(R.id.inPlaying);
            //LinearLayout mainLay = addView.findViewById(R.id.linearLayout3);

            if (!player.getPlayerName().equalsIgnoreCase("Choose Player")) {
                CustomUtil.loadImageWithGlide(mContext, player_img, ApiManager.TEAM_IMG, player.getPlayerImage(), R.drawable.ic_team1_tshirt);
            }

            playerName.setText(player.getPlayerName().replace(" ","\n"));

            if (!player.isDisable()){
                if (isMyJoined) {
                    if (!player.getPlayerName().equalsIgnoreCase("Choose Player")){
                        imgEdit.setVisibility(View.VISIBLE);
                    }else {
                        imgEdit.setVisibility(View.GONE);
                    }
                    if (list.isDisable()) {
                        teamReviewLayout.setBackgroundResource(R.drawable.gray_border_alpha);
                        teamReviewLayout.setAlpha(0.2f);
                    }else {
                        teamReviewLayout.setBackgroundResource(R.drawable.gray_border);
                    }
                }else {
                    imgEdit.setVisibility(View.GONE);
                    teamReviewLayout.setBackgroundResource(R.drawable.gray_border);
                }
            }
            else if (player.isJoinMe()){
                imgEdit.setVisibility(View.VISIBLE);
                teamReviewLayout.setBackgroundResource(R.drawable.selected_green_player);
            }else {
                imgEdit.setVisibility(View.GONE);
                teamReviewLayout.setBackgroundResource(R.drawable.gray_border_selected);
            }

            if (is_match_after){
                imgEdit.setVisibility(View.GONE);
                inPlaying.setVisibility(View.GONE);
                if (!player.getPlayerName().equalsIgnoreCase("Choose Player")){
                    player_type.setText("P: "+player.getTotalPoints());
                }
                //player_type.setText("P: "+player.getTotalPoints());
                player_type.setTextColor(mContext.getResources().getColor(R.color.green_pure));

            }else {
                //player_type.setText(player.getPlayerType());
                if (!player.getPlayerName().equalsIgnoreCase("Choose Player")){
                    player_type.setText(player.getPlayerType());
                }else {
                    player_type.setText("+");
                }
                if (!player.getPlayerName().equalsIgnoreCase("Choose Player")){
                    if (MyApp.getMyPreferences().getMatchModel().getTeamAXi().equalsIgnoreCase("Yes") ||
                            MyApp.getMyPreferences().getMatchModel().getTeamBXi().equalsIgnoreCase("Yes")) {
                        LogUtil.e("resp",player.getPlayingXi()+"   key"+player.getPlayerName());
                        if (!TextUtils.isEmpty(player.getPlayingXi()) && !player.getPlayingXi().equalsIgnoreCase("Yes")) {
                            inPlaying.setVisibility(View.VISIBLE);
                        } else {
                            inPlaying.setVisibility(View.GONE);
                        }
                    }else {
                        inPlaying.setVisibility(View.GONE);
                    }
                }else {
                    inPlaying.setVisibility(View.GONE);
                }

            }

            imgEdit.setOnClickListener(v -> {
                if (player.isJoinMe()){
                    for (int j=0;j< list.getPlayers().size();j++){
                        GroupContestModel.ContestDatum.PlayersDatum player1= list.getPlayersContest().get(j);
                        player1.setChecked(false);
                    }
                    player.setChecked(true);
                    ((SinglesContestActivity)mContext).confirmTeam(list,mainPosition,true);
                }
            });

            addView.setOnClickListener(view -> {
                if (is_match_after){

                    PlayerModel mpm=new PlayerModel();
                    mpm.setCap_percent("0");
                    mpm.setId(player.getId());
                    mpm.setMatchId(player.getMatchId());
                    mpm.setPlayerId(player.getPlayerId());
                    mpm.setPlayerImage(player.getPlayerImage());
                    mpm.setPlayerName(player.getPlayerName());
                    mpm.setPlayerSname(player.getPlayerSname());
                    mpm.setPlayerType(player.getPlayerType());
                    mpm.setPlayerRank(player.getPlayerRank());
                    mpm.setPlayingXi(player.getPlayingXi());
                    mpm.setTotalPoints(player.getTotalPoints());
                    mpm.setTeamId(MyApp.getMyPreferences().getMatchModel().getTeam1());


                    Intent intent = new Intent(mContext, AfterMatchPlayerStatesActivity.class);
                    Gson gson = new Gson();
                    intent.putExtra("type",2);
                    if (player.isJoinMe()){
                        intent.putExtra("joining","Yes");
                    }else {
                        intent.putExtra("joining","No");
                    }

                    intent.putExtra("model",gson.toJson(mpm));
                    mContext.startActivity(intent);
                }else {
                    if (!player.isDisable()){
                        if (!list.isDisable()){
                            for (int j=0;j< list.getPlayers().size();j++){
                                GroupModel.PlayersDatum player1= list.getPlayers().get(j);
                                player1.setChecked(false);
                            }
                            player.setChecked(true);
                            ((SinglesContestActivity)mContext).confirmTeam(list,mainPosition,false);
                        }
                    }
                }

            });

            holder.layPlayers.addView(addView);
        }

        holder.txtLblEntry.setOnClickListener(view -> {
            if(!list.isDisable()){
                ((SinglesContestActivity)mContext).confirmTeam(list,mainPosition,false);
            }
        });

       /* holder.contest_bonus_b.setOnClickListener(v->{
            ViewTooltip
                    .on((Activity)  mContext, holder.contest_bonus_b)
                    .autoHide(true, 1000)
                    .align(CENTER)
                    .position(RIGHT)
                    .text("Bonus Contest")
                    .textColor(Color.WHITE)
                    .color( mContext.getResources().getColor(R.color.blackPrimary))
                    .show();
        });*/
    }



    public void updateList(List<GroupContestModel.ContestDatum> contestModelList){
        this.contestDatumList = contestModelList;
        notifyDataSetChanged();
    }

    public class joinCntUp implements Comparator<GroupContestModel.ContestDatum.PlayersDatum> {
        @Override
        public int compare(GroupContestModel.ContestDatum.PlayersDatum o1, GroupContestModel.ContestDatum.PlayersDatum o2) {
            return Float.compare(CustomUtil.convertFloat(o2.getTotalPoints()), CustomUtil.convertFloat(o1.getTotalPoints()));
        }
    }

    @Override
    public int getItemCount() {
        return contestDatumList.size();
    }

    public class ContestListHolder extends RecyclerView.ViewHolder {
        TextView contest_price_pool,contest_winner,txtLblEntry,contest_left_team,contest_total_team,txtWonLbl;
        ProgressBar contest_progress;
        LinearLayout teams_left_linear,contest_full_linear,layPlayers;

        ArrayList<GroupContestModel.ContestDatum.PlayersDatum> playersData=new ArrayList<>();

        public ContestListHolder(@NonNull View itemView) {
            super(itemView);

            contest_price_pool = itemView.findViewById(R.id.contest_price_pool);
            contest_winner = itemView.findViewById(R.id.contest_winner);
            contest_progress = itemView.findViewById(R.id.contest_progress);
            txtLblEntry = itemView.findViewById(R.id.txtLblEntry);
            contest_left_team = itemView.findViewById(R.id.contest_left_team);
            contest_total_team = itemView.findViewById(R.id.contest_total_team);
            contest_full_linear = itemView.findViewById(R.id.contest_full_linear);
            teams_left_linear = itemView.findViewById(R.id.teams_left_linear);
            layPlayers = itemView.findViewById(R.id.layPlayers);
            txtWonLbl = itemView.findViewById(R.id.txtWonLbl);


        }
    }
}
