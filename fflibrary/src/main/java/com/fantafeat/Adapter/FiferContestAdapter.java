package com.fantafeat.Adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.fantafeat.Activity.AfterMatchPlayerStatesActivity;
import com.fantafeat.Activity.FiferContestActivity;
import com.fantafeat.Activity.PlayerStatsActivity;
import com.fantafeat.Model.GroupContestModel;
import com.fantafeat.Model.GroupModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FiferContestAdapter extends RecyclerView.Adapter<FiferContestAdapter.ViewHolder> {

    private Context mContext;
    private List<GroupContestModel.ContestDatum> contestDatumList;
    private Gson gson;
    private ObjectAnimator anim;
    private Animation animation;
    private int mainPosition = 0;
    private boolean isMyJoined = false;
    private boolean is_match_after = false;
    LayoutInflater layoutInflater;
    private String tag;

    public FiferContestAdapter(Context mContext, List<GroupContestModel.ContestDatum> contestDatumList, Gson gson,int mainPosition,boolean isMyJoined,boolean is_match_after) {
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.fifer_contest_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final GroupContestModel.ContestDatum list = contestDatumList.get(holder.getAdapterPosition());

        holder.contest_price_pool.setText(CustomUtil.displayAmountFloat(mContext, list.getDistributeAmount()));
        holder.contest_joined.setText(list.getJoinedTeams());

        if (is_match_after){
            list.setDisable(true);
        }

        if (list.getEntryFee().equalsIgnoreCase("0") || list.getEntryFee().equalsIgnoreCase("0.0") ||
                list.getEntryFee().equalsIgnoreCase("0.00")){
            holder.txtLblEntry.setText("FREE");
        }
        else {
            holder.txtLblEntry.setText(mContext.getResources().getString(R.string.rs) + list.getEntryFee().replace(".00",""));
        }

        /*if (TextUtils.isEmpty(list.getDefaultBonus()) || CustomUtil.convertFloat(list.getDefaultBonus())<=0){
            holder.contest_offer_price.setVisibility(View.GONE);
        }else {
            holder.contest_offer_price.setVisibility(View.VISIBLE);
            holder.contest_offer_price.setText("Use "+list.getDefaultBonus()+"% Bonus");
        }

        if (TextUtils.isEmpty(list.getDefault_ff_coins()) || CustomUtil.convertFloat(list.getDefault_ff_coins())<=0){
            holder.txtFFGems.setVisibility(View.GONE);
        }else {
            holder.txtFFGems.setVisibility(View.VISIBLE);
            holder.txtFFGems.setText("Use "+list.getDefault_ff_coins()+"% "+mContext.getResources().getString(R.string.fanta_gems));
        }*/

        if(list.isDisable()){
            holder.txtLblEntry.setEnabled(false);
            holder.txtLblEntry.setBackground(mContext.getResources().getDrawable(R.drawable.btn_grey));
        }else {
            holder.txtLblEntry.setEnabled(true);
            holder.txtLblEntry.setBackground(mContext.getResources().getDrawable(R.drawable.btn_green));
        }

        ArrayList<GroupModel.PlayersDatum> playersData=new ArrayList<>(list.getPlayers());
        if (is_match_after){
            Collections.sort(playersData, new joinCntUp());

        }

        if (isMyJoined){
            if (MyApp.getMyPreferences().getMatchModel().getMatchStatus().equalsIgnoreCase("completed")){
                holder.txtLblWin.setText("Winning");
            }else
                holder.txtLblWin.setText("Approx Winning");
        }else {
            holder.txtLblWin.setText("Winning/Entry");
        }

        if (MyApp.getMyPreferences().getMatchModel().getMatchStatus().equalsIgnoreCase("playing")) {
            if (!TextUtils.isEmpty(list.getCon_win_amount_per_spot_my())){
                float amt=CustomUtil.convertFloat(list.getCon_win_amount_per_spot_my());
                if (amt>0){
                    holder.txtWonLbl.setVisibility(View.VISIBLE);
                    holder.txtWonLbl.setText("In Winning");
                }
            }
        }else if (MyApp.getMyPreferences().getMatchModel().getMatchStatus().equalsIgnoreCase("completed")) {
            if (!TextUtils.isEmpty(list.getCon_win_amount_per_spot_my())){
                float amt=CustomUtil.convertFloat(list.getCon_win_amount_per_spot_my());
                if (amt>0){
                    holder.txtWonLbl.setVisibility(View.VISIBLE);
                    holder.txtWonLbl.setText("You Won");
                }
            }
        }
        else {
            holder.txtWonLbl.setVisibility(View.GONE);
        }

       /* if (isMyJoined){
            playersData=new ArrayList<>(list.getPlayers());
            Collections.sort(playersData, new joinCntUp());
        }else {
            playersData=new ArrayList<>(list.getPlayers());
            Collections.sort(playersData, new prizePoolUp());
        }*/

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );

        holder.layPlayers.removeAllViews();
        holder.layPlayers.invalidate();

        for (int i=0;i<playersData.size();i++  ) {
            GroupModel.PlayersDatum player=playersData.get(i);
            View addView = layoutInflater.inflate(R.layout.fifer_player_row, null);

            addView.setLayoutParams(param);

            TextView playerName =  addView.findViewById(R.id.player_name);
            playerName.setSelected(true);
            TextView txtJoining =  addView.findViewById(R.id.txtJoining);
            TextView txtWinEntry =  addView.findViewById(R.id.txtWinEntry);
            txtWinEntry.setSelected(true);
            TextView player_type = addView.findViewById(R.id.txtPlayerType);
            //TextView player_team = addView.findViewById(R.id.player_team);
            TextView team_xi_text = addView.findViewById(R.id.team_xi_text);
            ImageView team_xi_sign = addView.findViewById(R.id.team_xi_sign);
            ImageView player_image = addView.findViewById(R.id.player_image);
            LinearLayout mainLay = addView.findViewById(R.id.select_linear_layout);

            CustomUtil.loadImageWithGlide(mContext,player_image, ApiManager.TEAM_IMG,player.getPlayerImage(),R.drawable.ic_team1_tshirt);

            if(i % 2 == 0){
                mainLay.setBackgroundColor(mContext.getResources().getColor( R.color.appBackground));
            }else {
                mainLay.setBackgroundColor(mContext.getResources().getColor(R.color.white_pure));
            }

            playerName.setText(player.getPlayerName());//.replace(" ","\n")
            player_type.setText(player.getPlayerType());

           // player_team.setVisibility(View.GONE);

            if (isMyJoined){
                if (player.getTotalRank().equalsIgnoreCase("1")){
                    if (!MyApp.getMyPreferences().getMatchModel().getMatchStatus().equalsIgnoreCase("pending")){
                        mainLay.setBackgroundColor(mContext.getResources().getColor( R.color.lightYellow));
                        txtWinEntry.setTextSize(18);
                        txtWinEntry.setTypeface(Typeface.DEFAULT_BOLD);
                       // player_team.setVisibility(View.VISIBLE);
                       // player_team.setText("Rank: "+player.getTotalRank());
                    }

                }else {
                    txtWinEntry.setTextColor(mContext.getResources().getColor(R.color.gray_686868));
                }
                if (TextUtils.isEmpty(player.getJoiningCnt())){
                    player.setJoiningCnt("0");
                }
                if (TextUtils.isEmpty(player.getJoined_spot()) || player.getJoined_spot().equalsIgnoreCase("0")){
                    txtJoining.setText("0/"+player.getJoiningCnt());
                    txtWinEntry.setText(mContext.getResources().getString(R.string.rs)+"0.0");
                }
                else {
                    if (TextUtils.isEmpty(player.getApx_win_amt())){
                        txtWinEntry.setText(mContext.getResources().getString(R.string.rs)+"0.0");
                    }else {
                        float totWin;
                        if (is_match_after){

                            if (player.getTotalRank().equalsIgnoreCase("1")){
                                totWin = Float.parseFloat(list.getCon_win_amount_per_spot()) * CustomUtil.convertFloat(player.getJoined_spot());
                                txtWinEntry.setText(mContext.getResources().getString(R.string.rs)+CustomUtil.getFormater("00.00").format(totWin));
                            }else {
                                txtWinEntry.setText(mContext.getResources().getString(R.string.rs)+"0.0");
                            }
                        }else {

                            totWin = Float.parseFloat(player.getApx_win_amt()) * CustomUtil.convertFloat(player.getJoined_spot());
                            txtWinEntry.setText(mContext.getResources().getString(R.string.rs)+CustomUtil.getFormater("00.00").format(totWin));
                        }

                    }
                    String spot="<b>"+player.getJoined_spot()+"</b>"+"/"+player.getJoiningCnt();
                    txtJoining.setText(Html.fromHtml(spot));
                }
            }
            else {
                if (TextUtils.isEmpty(player.getJoiningCnt()) || player.getJoiningCnt().equalsIgnoreCase("0")){
                    txtJoining.setText("0");
                }else {
                    txtJoining.setText(player.getJoiningCnt());
                }

                if (TextUtils.isEmpty(player.getApx_win_amt()) || player.getApx_win_amt().equalsIgnoreCase("0")){
                    txtWinEntry.setText(mContext.getResources().getString(R.string.rs)+"0.0");
                }else {
                    txtWinEntry.setText(mContext.getResources().getString(R.string.rs)+CustomUtil.getFormater("00.00").format(Float.parseFloat(player.getApx_win_amt())));
                }

            }

            if (is_match_after){
                team_xi_sign.setVisibility(View.GONE);
                team_xi_text.setVisibility(View.VISIBLE);

                team_xi_text.setText("Points: "+player.getTotalPoints());
                //team_xi_text.setTextColor(mContext.getResources().getColor(R.color.blackPrimary));

            }
            else {
                if (MyApp.getMyPreferences().getMatchModel().getTeamAXi().equalsIgnoreCase("Yes") ||
                        MyApp.getMyPreferences().getMatchModel().getTeamBXi().equalsIgnoreCase("Yes")) {
                    if (player.getPlayingXi().equalsIgnoreCase("Yes")) {
                        team_xi_sign.setVisibility(View.VISIBLE);
                        team_xi_sign.setImageResource(R.drawable.play);
                        team_xi_text.setText("Playing");
                    } else  if (player.getPlayingXi().equalsIgnoreCase("No")) {
                        team_xi_sign.setVisibility(View.VISIBLE);
                        team_xi_sign.setImageResource(R.drawable.nonplay);
                        team_xi_text.setVisibility(View.GONE);
                    }else {
                        team_xi_sign.setVisibility(View.GONE);
                        team_xi_text.setVisibility(View.GONE);
                    }
                }else {
                    team_xi_sign.setVisibility(View.GONE);
                    team_xi_text.setVisibility(View.GONE);
                }
            }



            addView.setOnClickListener(view -> {

                if(MyApp.getClickStatus()){
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
                        intent.putExtra("joining",txtJoining.getText().toString());
                        intent.putExtra("model",gson.toJson(mpm));
                        mContext.startActivity(intent);

                    }else {
                        for (int j=0;j< list.getPlayers().size();j++){
                            GroupModel.PlayersDatum player1= list.getPlayers().get(j);
                            player1.setChecked(false);
                        }
                        player.setChecked(true);
                        ((FiferContestActivity)mContext).confirmTeam(list,mainPosition);
                    }
                }
            });

            holder.layPlayers.addView(addView);
        }

        holder.txtLblEntry.setOnClickListener(view -> {
            if(MyApp.getClickStatus()){
                for (int j=0;j< list.getPlayers().size();j++){
                    GroupModel.PlayersDatum player1= list.getPlayers().get(j);
                    player1.setChecked(false);
                }
                ((FiferContestActivity)mContext).confirmTeam(list,mainPosition);
            }
        });
    }

    public class prizePoolUp implements Comparator<GroupModel.PlayersDatum> {
        @Override
        public int compare(GroupModel.PlayersDatum o1, GroupModel.PlayersDatum o2) {
            return Float.compare(CustomUtil.convertFloat(o2.getApx_win_amt()), CustomUtil.convertFloat(o1.getApx_win_amt()));
        }
    }

    public class joinCntUp implements Comparator<GroupModel.PlayersDatum> {
        @Override
        public int compare(GroupModel.PlayersDatum o1, GroupModel.PlayersDatum o2) {
            return Float.compare(CustomUtil.convertFloat(o2.getTotalPoints()), CustomUtil.convertFloat(o1.getTotalPoints()));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<GroupContestModel.ContestDatum> contestModelList){
        this.contestDatumList = contestModelList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return contestDatumList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView contest_price_pool,contest_joined,txtLblEntry,txtLblWin,txtWonLbl;
        LinearLayout layPlayers;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contest_price_pool = itemView.findViewById(R.id.contest_price_pool);
            contest_joined = itemView.findViewById(R.id.contest_joined);
            txtLblEntry = itemView.findViewById(R.id.txtLblEntry);
            layPlayers = itemView.findViewById(R.id.layPlayers);
            txtLblWin = itemView.findViewById(R.id.txtLblWin);
            txtLblWin.setSelected(true);
            txtWonLbl = itemView.findViewById(R.id.txtWonLbl);


        }
    }
}
