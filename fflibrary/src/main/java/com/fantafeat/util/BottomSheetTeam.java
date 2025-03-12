package com.fantafeat.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;


import com.fantafeat.Activity.AfterMatchPlayerStatesActivity;
import com.fantafeat.Activity.CricketSelectPlayerActivity;
import com.fantafeat.Activity.PlayerStatsActivity;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.PlayerListModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.Session.MyPreferences;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class BottomSheetTeam extends BottomSheetDialogFragment {
    List<PlayerModel> playerModelList;
    PlayerListModel playerListModel;
    Context mContext;
    MyPreferences preferences;
    private String team_name,temp_team_id,temp_team_name;
    private Boolean isMatchStarted;
    private float totalPoints;
    private float mTotalPoints, mTotalCredit;
    int screenshot;
    int ss = 0;
    public View view;
    public RelativeLayout constraintLayout;

    public static final String TAG = "BottomSheetTeam";

    LinearLayout wicketLayout, batsmanLayout, batsmanLayout2, allRounderLayout,allRounderLayout2, bowlerLayout, bowlerLayout2,layMainCr,crLayout,layMainBowl;
    TextView prv_team1_name, prv_team2_name, prv_team1_count, prv_team2_count, prv_total_points,
            prv_total_point_text, prv_team_name,txtWK,txtBat,txtAR,txtBowl,txtCr;
    BottomSheetBehavior bottomSheetBehavior;
    BottomSheetDialog bottomSheet,bottomSheetDialog_temp;
    ImageView team_preview_edit, dialog_team_share,wc_edit,bat_edit,al_edit,bw_edit,imgTeamBg;
    boolean showShare = false;
    MatchModel matchModel;

    public BottomSheetTeam(@NonNull Context mContext, String team_name, List<PlayerModel> playerModelList) {
        this.playerModelList = playerModelList;
        this.mContext = mContext;
        this.team_name = team_name;
        preferences = MyApp.getMyPreferences();
        playerListModel = new PlayerListModel();
        totalPoints = 0;
        matchModel=preferences.getMatchModel();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(preferences.getMatchModel().getRegularMatchStartTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (date != null && date.after(MyApp.getCurrentDate())) {
            isMatchStarted = false;
        } else {
            isMatchStarted = true;
        }
    }

    public BottomSheetTeam(@NonNull Context mContext, String team_name,String temp_team_id, List<PlayerModel> playerModelList, boolean showShare) {
        this.playerModelList = playerModelList;
        this.mContext = mContext;
        this.team_name = team_name;
        preferences = MyApp.getMyPreferences();
        playerListModel = new PlayerListModel();
        playerListModel.setId(temp_team_id);
        matchModel=preferences.getMatchModel();

        totalPoints = 0;
        this.showShare = showShare;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(preferences.getMatchModel().getRegularMatchStartTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (date != null && date.after(MyApp.getCurrentDate())) {
            isMatchStarted = false;
        } else {
            isMatchStarted = true;
        }
    }

    public BottomSheetTeam(@NonNull Context mContext, String team_name, String temp_team_id, List<PlayerModel> playerModelList, boolean showShare, MatchModel matchModel) {
        this.playerModelList = playerModelList;
        this.mContext = mContext;
        this.team_name = team_name;
        this.matchModel = matchModel;
        preferences = MyApp.getMyPreferences();
        preferences.setMatchModel(matchModel);

        playerListModel = new PlayerListModel();
        playerListModel.setId(temp_team_id);
        totalPoints = 0;
        this.showShare = showShare;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(matchModel.getRegularMatchStartTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (date != null && date.after(MyApp.getCurrentDate())) {
            isMatchStarted = false;
        } else {
            isMatchStarted = true;
        }
    }

    public BottomSheetTeam(@NonNull Context mContext, PlayerListModel playerListModel, boolean showShare) {
        this.playerListModel = playerListModel;
        this.playerModelList = playerListModel.getPlayerDetailList();
        this.mContext = mContext;
        this.team_name = playerListModel.getTempTeamName();
        this.showShare = showShare;
        preferences = MyApp.getMyPreferences();
        matchModel=preferences.getMatchModel();
        //playerListModel = new PlayerListModel();

        totalPoints = 0;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(preferences.getMatchModel().getRegularMatchStartTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (date != null && date.after(MyApp.getCurrentDate())) {
            isMatchStarted = false;
        } else {
            isMatchStarted = true;
        }
    }

    public BottomSheetTeam(@NonNull Context mContext, String team_name, List<PlayerModel> playerModelList, int ss) {
        this.playerModelList = playerModelList;
        this.mContext = mContext;
        this.team_name = team_name;
        this.ss = ss;
        preferences = MyApp.getMyPreferences();
        playerListModel = new PlayerListModel();
        matchModel=preferences.getMatchModel();

        totalPoints = 0;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(preferences.getMatchModel().getRegularMatchStartTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (date != null && date.after(MyApp.getCurrentDate())) {
            isMatchStarted = false;
        } else {
            isMatchStarted = true;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        view = View.inflate(getContext(), R.layout.dialog_team_priview, null);
        bottomSheet.setContentView(view);
        constraintLayout = (RelativeLayout) view.findViewById(R.id.team_preview_root);
        ViewGroup.LayoutParams params = constraintLayout.getLayoutParams();
        //params.height = getWindowHeight();
        constraintLayout.setLayoutParams(params);
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setPeekHeight(getWindowHeight());
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.white));
        }*/

        setViewCreated(view);
        bottomSheetDialog_temp = bottomSheet;
        new Handler().postDelayed(() -> {
            if(ss==1){
                takeScreenshot();
            }
        }, 200);

        return bottomSheet;

    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        /*DisplayMetrics displayMetrics = new DisplayMetrics();
        ((HomeActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //return displayMetrics.heightPixels - getStatusBarHeight();
        return displayMetrics.heightPixels;*/
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void setViewCreated(View view) {

        prv_team1_name = (TextView) view.findViewById(R.id.prv_team1_name);
        prv_team2_name = (TextView) view.findViewById(R.id.prv_team2_name);

        wicketLayout = (LinearLayout) view.findViewById(R.id.wicketLayout);
        batsmanLayout = (LinearLayout) view.findViewById(R.id.batsmanLayout);
        batsmanLayout2 = (LinearLayout) view.findViewById(R.id.batsmanLayout2);
        allRounderLayout = (LinearLayout) view.findViewById(R.id.allRounderLayout);
        allRounderLayout2 = (LinearLayout) view.findViewById(R.id.allRounderLayout2);
        layMainBowl = (LinearLayout) view.findViewById(R.id.layMainBowl);
        bowlerLayout = (LinearLayout) view.findViewById(R.id.bowlerLayout);
        bowlerLayout2 = (LinearLayout) view.findViewById(R.id.bowlerLayout2);
        crLayout = view.findViewById(R.id.crLayout);
        layMainCr = view.findViewById(R.id.layMainCr);
        txtCr = view.findViewById(R.id.txtCr);
        imgTeamBg = view.findViewById(R.id.imgTeamBg);

        team_preview_edit = view.findViewById(R.id.team_preview_edit);

        txtWK = view.findViewById(R.id.txtWK);
        txtBat = view.findViewById(R.id.txtBat);
        txtAR = view.findViewById(R.id.txtAR);
        txtBowl = view.findViewById(R.id.txtBowl);

        prv_team1_count = view.findViewById(R.id.prv_team1_count);
        prv_team2_count = view.findViewById(R.id.prv_team2_count);
        prv_total_points = view.findViewById(R.id.prv_total_points);
        prv_total_point_text = view.findViewById(R.id.prv_total_point_text);
        prv_team_name = view.findViewById(R.id.prv_team_name);
        prv_team_name.setSelected(true);

        dialog_team_share = view.findViewById(R.id.dialog_team_share);
        dialog_team_share.setVisibility(View.GONE);
        if (matchModel.getMatchStatus().equalsIgnoreCase("Pending") && showShare) {
           // dialog_team_share.setVisibility(View.VISIBLE);
            team_preview_edit.setVisibility(View.VISIBLE);
        }
        else if (matchModel.getMatchStatus().equalsIgnoreCase("Completed") && showShare) {
            //dialog_team_share.setVisibility(View.VISIBLE);
            team_preview_edit.setVisibility(View.GONE);
        }
        else {
           // dialog_team_share.setVisibility(View.GONE);
            team_preview_edit.setVisibility(View.GONE);
        }

        Map<String, ArrayList<PlayerModel>> hash = new HashMap<>();
        for (PlayerModel mp : CustomUtil.emptyIfNull(playerModelList)) {
            if (hash.containsKey(mp.getTeamId())) {
                hash.get(mp.getTeamId()).add(mp);
            } else {
                ArrayList<PlayerModel> matchPlayers = new ArrayList<>();
                matchPlayers.add(mp);
                hash.put(mp.getTeamId(), matchPlayers);
            }
        }

        playerModelList = new ArrayList<>();
        if (hash.containsKey(matchModel.getTeam1())) {
            playerModelList.addAll(Objects.requireNonNull(hash.get(matchModel.getTeam1())));
        }
        if (hash.containsKey(matchModel.getTeam2())) {
            playerModelList.addAll(Objects.requireNonNull(hash.get(matchModel.getTeam2())));
        }

        String sportId=matchModel.getSportId();

        switch (sportId) {
            case "1":
                layMainCr.setVisibility(View.GONE);
                imgTeamBg.setImageResource(R.drawable.select_player_bg);
                txtWK.setText("Wicket Keeper");
                txtBat.setText("Batsman");
                txtAR.setText("All-Rounder");
                txtBowl.setText("Bowler");
                break;
            case "2":
                layMainCr.setVisibility(View.GONE);
                imgTeamBg.setImageResource(R.drawable.football_ground);
                txtWK.setText("Goal Keeper");
                txtBat.setText("Defender");
                txtAR.setText("Mid Fielder");
                txtBowl.setText("Forward");
                break;
            case "3":
                layMainCr.setVisibility(View.GONE);
                imgTeamBg.setImageResource(R.drawable.baseball_ground);
                txtWK.setText("Outfielders");
                txtBat.setText("Infielders");
                txtAR.setText("Pitcher");
                txtBowl.setText("Catcher");
                break;
            case "6":
                layMainCr.setVisibility(View.GONE);
                layMainBowl.setVisibility(View.GONE);
                imgTeamBg.setImageResource(R.drawable.handball_ground);
                txtWK.setText("Goal Keeper");
                txtBat.setText("Defender");
                txtAR.setText("Forward");
                break;
            case "7":
                layMainCr.setVisibility(View.GONE);
                layMainBowl.setVisibility(View.GONE);
                imgTeamBg.setImageResource(R.drawable.kabaddi_ground);
                txtWK.setText("Defenders");
                txtBat.setText("All-Rounders");
                txtAR.setText("Raiders");
                break;
            case "4":
                layMainCr.setVisibility(View.VISIBLE);
                imgTeamBg.setImageResource(R.drawable.basketball_bg);
                txtWK.setText("Point Guard");
                txtBat.setText("Shooting Guard");
                txtAR.setText("Small Forward");
                txtBowl.setText("Power Forward");
                txtCr.setText("Center");
                break;
        }

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int Team1Count = 0, Team2Count = 0;
        int wk = 0, bat = 0, ball = 0, allRounder = 0,cr=0;
        for (PlayerModel mpm : CustomUtil.emptyIfNull(playerModelList)) {
            if (sportId.equals("1")){
                switch (mpm.getPlayerType()) {
                    case "WK":
                        wk += 1;
                        break;
                    case "BAT":
                        bat += 1;
                        break;
                    case "AR":
                        allRounder += 1;
                        break;
                    case "BOWL":
                        ball += 1;
                        break;
                }
            }
            else if (sportId.equals("2")){
                switch (mpm.getPlayerType()) {
                    case "GK":
                        wk += 1;
                        break;
                    case "DEF":
                        bat += 1;
                        break;
                    case "MID":
                        allRounder += 1;
                        break;
                    case "FOR":
                        ball += 1;
                        break;
                }
            }
            else if (sportId.equals("3")){
                switch (mpm.getPlayerType()) {
                    case "OF":
                        wk += 1;
                        break;
                    case "IF":
                        bat += 1;
                        break;
                    case "PIT":
                        allRounder += 1;
                        break;
                    case "CAT":
                        ball += 1;
                        break;
                }
            }
            else if (sportId.equals("6")){
                switch (mpm.getPlayerType()) {
                    case "GK":
                        wk += 1;
                        break;
                    case "DEF":
                        bat += 1;
                        break;
                    case "FWD":
                        allRounder += 1;
                        break;
                }
            }
            else if (sportId.equals("7")){
                switch (mpm.getPlayerType()) {
                    case "DEF":
                        wk += 1;
                        break;
                    case "AR":
                        bat += 1;
                        break;
                    case "RAID":
                        allRounder += 1;
                        break;
                }
            }
            else if (sportId.equals("4")){
                switch (mpm.getPlayerType()) {
                    case "PG":
                        wk += 1;
                        break;
                    case "SG":
                        bat += 1;
                        break;
                    case "SF":
                        allRounder += 1;
                        break;
                    case "PF":
                        ball += 1;
                        break;
                    case "CR":
                        cr += 1;
                        break;
                }
            }

        }
        int wkTemp = 0, batTemp = 0, ballTemp = 0, allRounderTemp = 0;
        for (final PlayerModel mpm : CustomUtil.emptyIfNull(playerModelList)) {
          //  Log.e(TAG, "onSuccessResult: ABC "+mpm.getPlayerName() );
            if (preferences.getPlayerList() != null) {

                for (PlayerModel playerModel : preferences.getPlayerList()) {

                    if (playerModel.getPlayerId().equalsIgnoreCase(mpm.getPlayerId())) {
                        LogUtil.e(TAG, "onSuccessResult:  "+playerModel.getPlayerName()+"  "+playerModel.getPlayingXi() );
                        mpm.setPlayingXi(playerModel.getPlayingXi());
                        mpm.setPlayer_percent(playerModel.getPlayer_percent());
                        mpm.setOther_text(playerModel.getOther_text());
                        mpm.setOther_text2(playerModel.getOther_text2());
                        //mpm.setTeamId(playerModel.getTeamId());
                        break;
                    }
                }
            }

            final View addView = layoutInflater.inflate(R.layout.row_team_preview, null);
            addView.setLayoutParams(param);

            TextView playerName = (TextView) addView.findViewById(R.id.player_name);
            TextView playerPoint = (TextView) addView.findViewById(R.id.player_point);
            TextView txtCVC = (TextView) addView.findViewById(R.id.txtCVC);
            ImageView imageView = (ImageView) addView.findViewById(R.id.player_img);
           // ImageView isLeader = (ImageView) addView.findViewById(R.id.isLeader);
            ImageView inPlaying = addView.findViewById(R.id.inPlaying);

            addView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MyApp.getClickStatus()) {
                        if(matchModel.getMatchStatus().equalsIgnoreCase("pending")) {
                            //bottomSheet.dismiss();
                            /*new FragmentUtil().addFragment((FragmentActivity) mContext,
                                    R.id.fragment_container,
                                    new PlayerStatesFragment(mpm, matchModel.getLeagueId()),
                                    ((HomeActivity) mContext).fragmentTag(40),
                                    FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
                            Intent intent = new Intent(mContext, PlayerStatsActivity.class);
                            intent.putExtra("league_id",matchModel.getLeagueId());
                            Gson gson = new Gson();
                            intent.putExtra("playerModel",gson.toJson(mpm));
                            mContext.startActivity(intent);
                        }
                        else{
                            //bottomSheet.dismiss();
                            /*new FragmentUtil().addFragment((FragmentActivity) mContext,
                                    R.id.fragment_container,
                                    new AfterMatachPlayerStatesFragment(mpm),
                                    ((HomeActivity) mContext).fragmentTag(48),
                                    FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
                            Intent intent = new Intent(mContext, AfterMatchPlayerStatesActivity.class);
                            Gson gson = new Gson();
                            intent.putExtra("type",2);
                            intent.putExtra("model",gson.toJson(mpm));
                            mContext.startActivity(intent);
                        }
                    }
                }
            });

            if (mpm.getTeamId().equals(matchModel.getTeam1())) {
                Team1Count += 1;
                playerName.setBackgroundResource(R.drawable.white_round_fill);
                txtCVC.setBackgroundResource(R.drawable.white_circle);
                playerName.setTextColor(getResources().getColor(R.color.font_color));
                txtCVC.setTextColor(getResources().getColor(R.color.font_color));

                switch (sportId) {
                    case "1":
                        CustomUtil.loadImageWithGlide(mContext, imageView, ApiManager.TEAM_IMG, mpm.getPlayerImage(), R.drawable.ic_team1_tshirt);
                        break;
                    case "2":
                        CustomUtil.loadImageWithGlide(mContext, imageView, ApiManager.TEAM_IMG, mpm.getPlayerImage(), R.drawable.football_player1);
                        break;
                    case "4":
                        CustomUtil.loadImageWithGlide(mContext, imageView, ApiManager.TEAM_IMG, mpm.getPlayerImage(), R.drawable.basketball_team1);
                        break;
                    case "3":
                        CustomUtil.loadImageWithGlide(mContext, imageView, ApiManager.TEAM_IMG, mpm.getPlayerImage(), R.drawable.baseball_player1);
                        break;
                    case "6":
                        CustomUtil.loadImageWithGlide(mContext, imageView, ApiManager.TEAM_IMG, mpm.getPlayerImage(), R.drawable.handball_player1);
                        break;
                    case "7":
                        CustomUtil.loadImageWithGlide(mContext, imageView, ApiManager.TEAM_IMG, mpm.getPlayerImage(), R.drawable.kabaddi_player1);
                        break;
                }
            }
            if (mpm.getTeamId().equals(matchModel.getTeam2())) {
                Team2Count += 1;
                playerName.setBackgroundResource(R.drawable.black_round_fill);
                txtCVC.setBackgroundResource(R.drawable.black_circle);
                playerName.setTextColor(getResources().getColor(R.color.pureWhite));
                txtCVC.setTextColor(getResources().getColor(R.color.pureWhite));
                switch (sportId) {
                    case "1":
                        CustomUtil.loadImageWithGlide(mContext, imageView, ApiManager.TEAM_IMG, mpm.getPlayerImage(), R.drawable.ic_team2_tshirt);
                        break;
                    case "2":
                        CustomUtil.loadImageWithGlide(mContext, imageView, ApiManager.TEAM_IMG, mpm.getPlayerImage(), R.drawable.football_player2);
                        break;
                    case "4":
                        CustomUtil.loadImageWithGlide(mContext, imageView, ApiManager.TEAM_IMG, mpm.getPlayerImage(), R.drawable.basketball_team2);
                        break;
                    case "3":
                        CustomUtil.loadImageWithGlide(mContext, imageView, ApiManager.TEAM_IMG, mpm.getPlayerImage(), R.drawable.baseball_player2);
                        break;
                    case "6":
                        CustomUtil.loadImageWithGlide(mContext, imageView, ApiManager.TEAM_IMG, mpm.getPlayerImage(), R.drawable.handball_player2);
                        break;
                    case "7":
                        CustomUtil.loadImageWithGlide(mContext, imageView, ApiManager.TEAM_IMG, mpm.getPlayerImage(), R.drawable.kabaddi_player2);
                        break;
                }
            }

            if (matchModel.getTeamAXi().equalsIgnoreCase("Yes") ||
                    matchModel.getTeamBXi().equalsIgnoreCase("Yes")) {
                if (!mpm.getPlayingXi().equalsIgnoreCase("Yes")) {
                    inPlaying.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(mpm.getOther_text()) && mpm.getOther_text().equalsIgnoreCase("substitute"))
                        inPlaying.setImageResource(R.drawable.substitute_dot);
                    else
                        inPlaying.setImageResource(R.drawable.nonplay);
                } else {
                    inPlaying.setVisibility(View.GONE);
                }
            } else {
                inPlaying.setVisibility(View.GONE);
            }

            if (mpm.getIsCaptain().equals("Yes")) {
                txtCVC.setVisibility(View.VISIBLE);
                //isLeader.setImageResource(R.drawable.c);
                txtCVC.setText("C");
                playerListModel.setC_player_name(mpm.getPlayerName());
                playerListModel.setC_player_sname(mpm.getPlayerSname());
                playerListModel.setC_player_avg_point(mpm.getPlayerAvgPoint());
                playerListModel.setC_player_rank(mpm.getPlayerRank());
                playerListModel.setC_player_img(mpm.getPlayerImage());
                playerListModel.setC_player_xi(mpm.getPlayingXi());
                playerListModel.setC_player_type(mpm.getPlayerType());
            }
            else if (mpm.getIsWiseCaptain().equals("Yes")) {
                txtCVC.setVisibility(View.VISIBLE);
                //isLeader.setImageResource(R.drawable.vc);
                txtCVC.setText("VC");
                playerListModel.setVc_player_name(mpm.getPlayerName());
                playerListModel.setVc_player_sname(mpm.getPlayerSname());
                playerListModel.setVc_player_avg_point(mpm.getPlayerAvgPoint());
                playerListModel.setVc_player_rank(mpm.getPlayerRank());
                playerListModel.setVc_player_img(mpm.getPlayerImage());
                playerListModel.setVc_player_xi(mpm.getPlayingXi());
                playerListModel.setVc_player_type(mpm.getPlayerType());

            }
            else {
                txtCVC.setVisibility(View.GONE);
            }

            playerName.setText(mpm.getPlayerSname());

            if (isMatchStarted) {
                totalPoints += CustomUtil.convertFloat(mpm.getTotalPoints());
                playerPoint.setText(mpm.getTotalPoints() + " Pts");
                mTotalPoints += CustomUtil.convertFloat(mpm.getTotalPoints());
            } else {
                LogUtil.e(TAG, "onSuccessResult: " + mpm.getOther_text()+"  "+mpm.getOther_text2());
                totalPoints += CustomUtil.convertFloat(mpm.getPlayerRank());
                mTotalCredit += CustomUtil.convertFloat(mpm.getPlayerRank());
                if (TextUtils.isEmpty(mpm.getOther_text2())){
                    playerPoint.setText( mpm.getPlayerRank()+" Cr" );
                }else {
                    playerPoint.setText(mpm.getPlayerRank()+" Cr "+"\n◉ "+mpm.getOther_text2());
                }
            }

            /*try {
                if (!FragmentUtil.fragmentStack.peek().getTag().equalsIgnoreCase("Feed")) {
//Log.e(TAG, "setViewCreated: "+ FragmentUtil.fragmentStack.peek().getTag());
                    if (isMatchStarted) {
                        totalPoints += CustomUtil.convertFloat(mpm.getTotalPoints());
                        playerPoint.setText(mpm.getTotalPoints() + " Pts");
                        mTotalPoints += CustomUtil.convertFloat(mpm.getTotalPoints());
                    } else {
                        totalPoints += CustomUtil.convertFloat(mpm.getPlayerRank());
                        mTotalCredit += CustomUtil.convertFloat(mpm.getPlayerRank());
                        playerPoint.setText("Cr : " + mpm.getPlayerRank());
                    }
                } else {
                    totalPoints += CustomUtil.convertFloat(mpm.getPlayerRank());
                    mTotalCredit += CustomUtil.convertFloat(mpm.getPlayerRank());
                    playerPoint.setText("Cr : " + mpm.getPlayerRank());
                }
            }catch(Exception e){
                if (isMatchStarted) {
                    totalPoints += CustomUtil.convertFloat(mpm.getTotalPoints());
                    playerPoint.setText(mpm.getTotalPoints() + " Pts");
                    mTotalPoints += CustomUtil.convertFloat(mpm.getTotalPoints());
                } else {
                    totalPoints += CustomUtil.convertFloat(mpm.getPlayerRank());
                    mTotalCredit += CustomUtil.convertFloat(mpm.getPlayerRank());
                    playerPoint.setText("Cr : " + mpm.getPlayerRank());
                }
            }*/

            if (sportId.equals("1")) {
                switch (mpm.getPlayerType().toLowerCase()) {
                    case "wk":
                        wicketLayout.addView(addView);
                        break;
                    case "bat":
                        batTemp += 1;
                        if (bat >= 5) {
                            if (bat == 5) {
                                if (batTemp <= 3) {
                                    batsmanLayout.addView(addView);
                                } else {
                                    batsmanLayout2.addView(addView);
                                }
                            } else if (bat == 6) {
                                if (batTemp <= 4) {
                                    batsmanLayout.addView(addView);
                                } else {
                                    batsmanLayout2.addView(addView);
                                }
                            } else {
                                batsmanLayout.addView(addView);
                            }
                        } else {
                            batsmanLayout.addView(addView);
                        }
                        break;
                    case "ar":
                        allRounderLayout.addView(addView);
                        break;
                    case "bowl":
                        ballTemp += 1;
                        if (ball >= 5) {
                            if (ball == 5) {
                                if (ballTemp <= 3) {
                                    bowlerLayout.addView(addView);
                                } else {
                                    bowlerLayout2.addView(addView);
                                }
                            } else if (ball == 6) {
                                if (ballTemp <= 4) {
                                    bowlerLayout.addView(addView);
                                } else {
                                    bowlerLayout2.addView(addView);
                                }
                            } else {
                                bowlerLayout.addView(addView);
                            }
                        } else {
                            bowlerLayout.addView(addView);
                        }
                        break;
                }
            }
            else if (sportId.equals("2")) {
                switch (mpm.getPlayerType().toLowerCase()) {
                    case "gk":
                        wicketLayout.addView(addView);
                        break;
                    case "def":
                        batTemp += 1;
                        if (bat >= 4) {
                            if (bat == 4) {
                                batsmanLayout.addView(addView);

                            } else if (bat == 5) {
                                if (batTemp <= 3) {
                                    batsmanLayout.addView(addView);
                                } else {
                                    batsmanLayout2.addView(addView);
                                }
                            } else {
                                batsmanLayout.addView(addView);
                            }
                        } else {
                            batsmanLayout.addView(addView);
                        }
                        break;
                    case "mid":
                        allRounderTemp += 1;
                        if (allRounder >= 4) {
                            if (allRounder == 4) {
                                allRounderLayout.addView(addView);
                            } else if (allRounder == 5) {
                                if (allRounderTemp <= 3) {
                                    allRounderLayout.addView(addView);
                                } else {
                                    allRounderLayout2.addView(addView);
                                }
                            } else {
                                allRounderLayout.addView(addView);
                            }
                        } else {
                            allRounderLayout.addView(addView);
                        }
                        break;
                    case "for":
                        ballTemp += 1;
                        bowlerLayout.addView(addView);
                        break;
                }
            }
            else if (sportId.equals("3")) {
                switch (mpm.getPlayerType().toLowerCase()) {
                    case "of":
                        wicketLayout.addView(addView);
                        break;
                    case "if":
                        batTemp += 1;
                        if (bat >= 4) {
                            if (bat == 4) {
                                batsmanLayout.addView(addView);

                            } else if (bat == 5) {
                                if (batTemp <= 3) {
                                    batsmanLayout.addView(addView);
                                } else {
                                    batsmanLayout2.addView(addView);
                                }
                            } else {
                                batsmanLayout.addView(addView);
                            }
                        } else {
                            batsmanLayout.addView(addView);
                        }
                        break;
                    case "pit":
                        allRounderTemp += 1;
                        if (allRounder >= 4) {
                            if (allRounder == 4) {
                                allRounderLayout.addView(addView);
                            } else if (allRounder == 5) {
                                if (allRounderTemp <= 3) {
                                    allRounderLayout.addView(addView);
                                } else {
                                    allRounderLayout2.addView(addView);
                                }
                            } else {
                                allRounderLayout.addView(addView);
                            }
                        } else {
                            allRounderLayout.addView(addView);
                        }
                        break;
                    case "cat":
                        ballTemp += 1;
                        bowlerLayout.addView(addView);
                        break;
                }
            }
            else if (sportId.equals("6")) {
                switch (mpm.getPlayerType().toLowerCase()) {
                    case "gk":
                        wicketLayout.addView(addView);
                        break;
                    case "def":
                        batTemp += 1;
                        if (bat >= 4) {
                            if (bat == 4) {
                                batsmanLayout.addView(addView);

                            } else if (bat == 5) {
                                if (batTemp <= 3) {
                                    batsmanLayout.addView(addView);
                                } else {
                                    batsmanLayout2.addView(addView);
                                }
                            } else {
                                batsmanLayout.addView(addView);
                            }
                        } else {
                            batsmanLayout.addView(addView);
                        }
                        break;
                    case "fwd":
                        allRounderTemp += 1;
                        if (allRounder >= 4) {
                            if (allRounder == 4) {
                                allRounderLayout.addView(addView);
                            } else if (allRounder == 5) {
                                if (allRounderTemp <= 3) {
                                    allRounderLayout.addView(addView);
                                } else {
                                    allRounderLayout2.addView(addView);
                                }
                            } else {
                                allRounderLayout.addView(addView);
                            }
                        } else {
                            allRounderLayout.addView(addView);
                        }
                        break;
                }
            }
            else if (sportId.equals("7")) {
                switch (mpm.getPlayerType().toLowerCase()) {
                    case "def":
                        wicketLayout.addView(addView);
                        break;
                    case "ar":
                        batTemp += 1;
                        if (bat >= 4) {
                            if (bat == 4) {
                                batsmanLayout.addView(addView);

                            } else if (bat == 5) {
                                if (batTemp <= 3) {
                                    batsmanLayout.addView(addView);
                                } else {
                                    batsmanLayout2.addView(addView);
                                }
                            } else {
                                batsmanLayout.addView(addView);
                            }
                        } else {
                            batsmanLayout.addView(addView);
                        }
                        break;
                    case "raid":
                        allRounderTemp += 1;
                        if (allRounder >= 4) {
                            if (allRounder == 4) {
                                allRounderLayout.addView(addView);
                            } else if (allRounder == 5) {
                                if (allRounderTemp <= 3) {
                                    allRounderLayout.addView(addView);
                                } else {
                                    allRounderLayout2.addView(addView);
                                }
                            } else {
                                allRounderLayout.addView(addView);
                            }
                        } else {
                            allRounderLayout.addView(addView);
                        }
                        break;
                }
            }
            else if (sportId.equals("4")) {
                switch (mpm.getPlayerType().toLowerCase()) {
                    case "pg":
                        wicketLayout.addView(addView);
                        break;
                    case "sg":
                        batTemp += 1;
                        batsmanLayout.addView(addView);
                        break;
                    case "sf":
                        allRounderTemp += 1;
                        allRounderLayout.addView(addView);
                        break;
                    case "pf":
                        ballTemp += 1;
                        bowlerLayout.addView(addView);
                        break;
                    case "cr":
                        cr += 1;
                        crLayout.addView(addView);
                        break;
                }
            }
        }

        if (isMatchStarted) {
            prv_total_point_text.setText("Total Points");
        }
        else {
            prv_total_point_text.setText("Total Credit");
        }

        prv_total_points.setText("" + totalPoints);
        prv_team_name.setText("" + team_name);
        prv_team1_count.setText("" + Team1Count);
        prv_team2_count.setText("" + Team2Count);
        prv_team1_name.setText(matchModel.getTeam1Sname());
        prv_team2_name.setText(matchModel.getTeam2Sname());


        playerListModel.setMatchId(matchModel.getId());
        playerListModel.setContestDisplayTypeId(matchModel.getConDisplayType());
        playerListModel.setUserId(preferences.getUserModel().getId());
        playerListModel.setTotalPoint("" + mTotalPoints);
       // playerListModel.setId();

        playerListModel.setTeam1_count("" + Team1Count);
        playerListModel.setTeam2_count("" + Team2Count);
        playerListModel.setCreditTotal("" + mTotalCredit);
        playerListModel.setWk_count("" + wk);
        playerListModel.setBat_count("" + bat);
        playerListModel.setAr_count("" + allRounder);
        playerListModel.setBowl_count("" + ball);
        playerListModel.setIsJoined("No");
        playerListModel.setIsSelected("No");
        playerListModel.setTeam1_sname(matchModel.getTeam1Sname());
        playerListModel.setTeam2_sname(matchModel.getTeam2Sname());
        playerListModel.setPlayerDetailList(playerModelList);

        team_preview_edit.setOnClickListener(v->{
            if (MyApp.getClickStatus()) {
                bottomSheet.dismiss();
                Gson gson = new Gson();

                LogUtil.e("teamedt",gson.toJson(playerListModel));
                Intent intent = new Intent(mContext, CricketSelectPlayerActivity.class);
                intent.putExtra(PrefConstant.TEAMCREATETYPE, 1);
                intent.putExtra("team", gson.toJson(playerListModel));
                mContext.startActivity(intent);
            }
        });

        dialog_team_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    bottomSheet.dismiss();
                    takeScreenshot();
                }
            }
        });
    }

    public void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        try {

            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture

            View v1 = bottomSheet.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mContext.getCacheDir(), "images");
            imageFile.mkdirs();

            FileOutputStream outputStream = new FileOutputStream(imageFile+ "/image.jpg");
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            shareScreenshot();

            //openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void shareScreenshot() {
        //Uri uri = Uri.fromFile(file);//Convert file path into Uri for sharing
        File imagePath = new File(mContext.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.jpg");
        Uri uri = FileProvider.getUriForFile(mContext,"com.fantafeat.provider",newFile);//Convert file path into Uri for sharing
        if(uri !=null) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
           // intent.putExtra(Intent.EXTRA_TEXT, "Hurry! Make team and win upto 1 Lakh rupees. Join Fantafeat.");
            intent.putExtra(Intent.EXTRA_STREAM, uri);//pass uri here
            mContext.startActivity(Intent.createChooser(intent, "Share your Team"));
        }
    }

}