package com.fantafeat.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.fantafeat.Activity.CricketSelectPlayerActivity;
import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.Adapter.SelectPlayerAdapter;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.PlayerListModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.Model.SportsModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.BottomSheetTeam;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.FragmentUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CricketSelectPlayerFragment extends BaseFragment {

    public List<PlayerModel> playerModelList;
    public List<PlayerModel> playerModelListTemp;
    public TabLayout select_player_tab;
    private ViewPager2 select_player_viewpager;
    private ViewPagerAdapter viewPagerAdapter;
    public TextView  team1_selected_players, team2_selected_players, credit_left, credits,lineup,txtTeamlabel,txtSelBy,
            points, desc_select_player, match_title, timer, team2_name, team1_name,team1_cnt,team2_cnt,txtClear;//total_player_selected,
    ImageView arrow_point, arrow_credit,arrow_players,arrow_selby;//,imgClearSelection;
    CircleImageView team1_img, team2_img;
    public HashMap<String, Integer> checkSelect = new HashMap<>();
    public SelectPlayerAdapter selectPlayerAdapter;
    public int wk, bat, ar, bowl,cr, team1_selected, team2_selected, totalSelected, team1_selected_dots = 0, team2_selected_dots = 0,
            maxTeamPlayer=0,eachTeamMax=0;
    public List<PlayerModel> selected_list;
    float total_points = 100;
    int credit_filter = 0, point_filter = 0,sel_by=0, total_remain = 0,lineup_filter=0;
    public TextView next_for_select_captain;
    LinearLayout  team1_dots,layXi;//team2_dots,
    WKplayerFragment wKplayerFragment;
    BatPlayerFragment batPlayerFragment;
    ARplayerFragment aRplayerFragment;
    BowlPlayerFragment bowlPlayerFragment;
    CenterPlayerFragment centerPlayerFragment;
    private BottomSheetDialog bottomSheetDialog = null;
    private TextView txtTeam1Name,txtTeam2Name,txtAnounce;

    public MatchModel selected_match;
    private long diff;
    private CountDownTimer countDownTimer;
    private Toolbar toolbar;
    public ImageView mToolBarBack,imgTeamFilter,toolbar_wallet,imgEyePreview,imgAounce;
    private TextView team_preview,txtAounce;

    public int teamCreateType;
    public String contestData="";
    public PlayerListModel playerSelectedList;
    public List<PlayerModel> selectPlayerList;
    public List<PlayerModel> selectPlayerListTemp;
    private int teamFiltered=3;
    private String sportId="";
    public int wk_min=1,wk_max=4,bat_min=3,bat_max=6,ar_min=1,ar_max=4,bowl_min=3,bowl_max=6,cr_min=1,cr_max=4;

    public static CricketSelectPlayerFragment newInstance() {//, Fragment fragment
        return new CricketSelectPlayerFragment();
    }

    public static CricketSelectPlayerFragment newInstance(String json) {//, Fragment fragment
        CricketSelectPlayerFragment myFragment = new CricketSelectPlayerFragment();//fragment
        Bundle args = new Bundle();
        args.putString("jsonData", json);
        myFragment.setArguments(args);
        return myFragment;
    }

    public static CricketSelectPlayerFragment newInstance(PlayerListModel playerSelectedList, boolean isEdit) {//, Fragment fragment
        CricketSelectPlayerFragment myFragment = new CricketSelectPlayerFragment();//fragment
        Bundle args = new Bundle();
        args.putSerializable("playerModel", playerSelectedList);
        args.putBoolean("isEdit", isEdit);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        teamCreateType=0;

        if (getArguments() != null) {

            if (getArguments().containsKey("jsonData")){
                teamCreateType=0;
                this.contestData=getArguments().getString("jsonData");
            }
            if (getArguments().containsKey("isEdit")){
                if (getArguments().getBoolean("isEdit")){
                    teamCreateType = 1;
                }
                else {
                    teamCreateType = -1;
                }
            }
            if (getArguments().containsKey("playerModel")){
                this.playerSelectedList = (PlayerListModel) getArguments().getSerializable("playerModel");
                this.selected_list = new ArrayList<>(playerSelectedList.getPlayerDetailList());
                LogUtil.e(TAG, "CricketSelectPlayerFragment: "+selected_list.size() );
                this.selectPlayerListTemp = new ArrayList<>(playerSelectedList.getPlayerDetailList());
            }
        }
    }

    public CricketSelectPlayerFragment(){
        teamCreateType=0;
    }

    public CricketSelectPlayerFragment(String json){
        teamCreateType=0;
        this.contestData=json;
       // Log.d("teamData",json+" ");
    }

    public CricketSelectPlayerFragment(PlayerListModel playerSelectedList, boolean isEdit) {
        if (isEdit) {
            teamCreateType = 1;
        } else {
            teamCreateType = -1;
        }

        this.playerSelectedList = playerSelectedList;
        this.selected_list = new ArrayList<>(playerSelectedList.getPlayerDetailList());
        Log.e(TAG, "CricketSelectPlayerFragment: "+selected_list.size() );
        this.selectPlayerListTemp = new ArrayList<>(playerSelectedList.getPlayerDetailList());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cricket_select_player, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initControl(View view) {
        bottomSheetDialog = new BottomSheetDialog(mContext);

        match_title = view.findViewById(R.id.match_title);
        match_title.setSelected(true);
        timer = view.findViewById(R.id.timer);
        mToolBarBack = view.findViewById(R.id.toolbar_back);
        toolbar = view.findViewById(R.id.contest_list_toolbar);
        txtTeam1Name = view.findViewById(R.id.txtTeam1Name);
        txtTeam2Name = view.findViewById(R.id.txtTeam2Name);
        imgTeamFilter = view.findViewById(R.id.imgTeamFilter);
        toolbar_wallet = view.findViewById(R.id.toolbar_wallet);
        txtTeamlabel = view.findViewById(R.id.txtTeamlabel);
        team1_cnt = view.findViewById(R.id.team1_cnt);
        team2_cnt = view.findViewById(R.id.team2_cnt);
        arrow_selby = view.findViewById(R.id.arrow_selby);
        txtSelBy = view.findViewById(R.id.txtSelBy);
        imgAounce = view.findViewById(R.id.imgAounce);
        layXi = view.findViewById(R.id.layXi);
        txtAounce = view.findViewById(R.id.txtAounce);
        txtAnounce = view.findViewById(R.id.txtAnounce);
        txtAnounce.setSelected(true);
        toolbar_wallet.setVisibility(View.INVISIBLE);
        ((CricketSelectPlayerActivity)mContext).setSupportActionBar(toolbar);

        //total_player_selected = view.findViewById(R.id.total_player_selected);
        /*team1_selected_players = view.findViewById(R.id.team1_selected_players);
        team2_selected_players = view.findViewById(R.id.team2_selected_players);*/
        credit_left = view.findViewById(R.id.credit_left);
        select_player_tab = view.findViewById(R.id.select_player_tab);
        select_player_viewpager = view.findViewById(R.id.select_player_viewpager);
        credits = view.findViewById(R.id.credits_select);
        lineup = view.findViewById(R.id.lineup_select);
        points = view.findViewById(R.id.points_select);
        desc_select_player = view.findViewById(R.id.desc_select_player);
        arrow_credit = view.findViewById(R.id.arrow_credit);
        arrow_point = view.findViewById(R.id.arrow_point);
        arrow_players = view.findViewById(R.id.arrow_players);
        txtClear = view.findViewById(R.id.txtClear);

        /*if (ApiManager.is_team_clear){
            txtClear.setVisibility(View.VISIBLE);
        }else {
            txtClear.setVisibility(View.INVISIBLE);
        }*/

        next_for_select_captain = view.findViewById(R.id.next_for_select_captain);
        team_preview = view.findViewById(R.id.team_preview);
        imgEyePreview = view.findViewById(R.id.imgEyePreview);
        team1_img = view.findViewById(R.id.team1_img);
        team2_img = view.findViewById(R.id.team2_img);
      //  team2_dots = view.findViewById(R.id.team2_dots);
        team1_dots = view.findViewById(R.id.team1_dots);
        team2_name = view.findViewById(R.id.team2_name);
        team2_name.setSelected(true);
        team1_name = view.findViewById(R.id.team1_name);
        team1_name.setSelected(true);
        total_points = 100;

        if (preferences.getMatchModel() != null) {
            selected_match = preferences.getMatchModel();
            sportId=selected_match.getSportId();
        }

        SportsModel sportsModel=null;
        for (int i = 0; i < preferences.getSports().size(); i++) {
            if (preferences.getSports().get(i).getId().trim().equalsIgnoreCase(sportId.trim())){
                sportsModel=preferences.getSports().get(i);
            }
        }

        if (sportId.equals("4")){
            maxTeamPlayer=8;
            eachTeamMax=5;
        }
        else if (sportId.equals("3")){
            maxTeamPlayer=9;
            eachTeamMax=6;
        }
        else if (sportId.equals("6")){
            maxTeamPlayer=7;
            eachTeamMax=5;
        }
        else if (sportId.equals("7")){//kabaddi
            maxTeamPlayer=7;
            eachTeamMax=5;
        }
        else {
            maxTeamPlayer=11;
            eachTeamMax=7;
        }

        for (int i=0;i<maxTeamPlayer;i++){
            TextView tv=new TextView(mContext);
            tv.setBackgroundResource(R.drawable.unselected_player_bubble);
            LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(40,40);
            param.setMargins(10,0,10,0);
            tv.setLayoutParams(param);
            tv.setTag(String.valueOf(i+1));
            team1_dots.addView(tv);
        }

        //if (sportId.equals("1")){
            if (sportsModel!=null){
                wk_min=CustomUtil.convertInt(sportsModel.getTab_1_min());
                wk_max=CustomUtil.convertInt(sportsModel.getTab_1_max());

                bat_min=CustomUtil.convertInt(sportsModel.getTab_2_min());
                bat_max=CustomUtil.convertInt(sportsModel.getTab_2_max());

                ar_min=CustomUtil.convertInt(sportsModel.getTab_3_min());
                ar_max=CustomUtil.convertInt(sportsModel.getTab_3_max());

                if (TextUtils.isEmpty(sportsModel.getTab_4_min())){
                    bowl_min=3;
                }else {
                    bowl_min=CustomUtil.convertInt(sportsModel.getTab_4_min());
                }

                if (TextUtils.isEmpty(sportsModel.getTab_4_max())){
                    bowl_max=6;
                }else {
                    bowl_max=CustomUtil.convertInt(sportsModel.getTab_4_max());
                }


                if (TextUtils.isEmpty(sportsModel.getTab_5_min())){
                    cr_min=1;
                }else {
                    cr_min=CustomUtil.convertInt(sportsModel.getTab_5_min());
                }
                if (TextUtils.isEmpty(sportsModel.getTab_5_mix())){
                    cr_max=4;
                }else {
                    cr_max=CustomUtil.convertInt(sportsModel.getTab_5_mix());
                }


                eachTeamMax=CustomUtil.convertInt(sportsModel.getTeam_1_max_player());

            }
            else {
                wk_min=1;
                wk_max=4;

                bat_min=3;
                bat_max=6;

                ar_min=1;
                ar_max=4;

                bowl_min=3;
                bowl_max=6;

                cr_min=1;
                cr_max=4;

            }
        //}

        /*else if (sportId.equals("7")){
            wk_min=2;
            wk_max=4;

            bat_min=1;
            bat_max=2;

            ar_min=1;
            ar_max=3;
        }
        else if (sportId.equals("2")){
            wk_min=1;
            wk_max=1;

            bat_min=3;
            bat_max=5;

            ar_min=3;
            ar_max=5;

            bowl_min=1;
            bowl_max=3;
        } else if (sportId.equals("4")){
            wk_min=1;
            wk_max=4;

            bat_min=1;
            bat_max=4;

            ar_min=1;
            ar_max=4;

            bowl_min=1;
            bowl_max=4;

            cr_min=1;
            cr_max=4;
        }
        else if (sportId.equals("6")){
            wk_min=1;
            wk_max=1;

            bat_min=2;
            bat_max=4;

            ar_min=2;
            ar_max=4;
        }else if (sportId.equals("3")){
            wk_min=2;
            wk_max=5;

            bat_min=2;
            bat_max=5;

            ar_min=1;
            ar_max=1;

            bowl_min=1;
            bowl_max=1;
        }*/

        /*
        * if (preferences.getMatchModel().getSportId().equals("1")){
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
            }*/

        txtTeamlabel.setText("Max "+eachTeamMax+" Players from Team");

        txtAounce.setTextColor(mContext.getResources().getColor(R.color.white_pure));

        if (preferences.getMatchModel()!=null && (preferences.getMatchModel().getTeamAXi().equalsIgnoreCase("yes") ||
                preferences.getMatchModel().getTeamBXi().equalsIgnoreCase("yes"))) {
            //imgAounce.setVisibility(View.VISIBLE);
            layXi.setVisibility(View.VISIBLE);
            //txtAounce.setTextColor(mContext.getResources().getColor(R.color.white));

            if (TextUtils.isEmpty(preferences.getMatchModel().getLineup_change_msg())){
                txtAnounce.setText("The 'Starting XI' is a tool to help you choose your squad, but we encourage that you conduct your own research before forming teams.");
            }else {
                txtAnounce.setText(preferences.getMatchModel().getLineup_change_msg());
            }
        }
        else {
            layXi.setVisibility(View.GONE);
            //imgAounce.setVisibility(View.GONE);
           // txtAounce.setTextColor(mContext.getResources().getColor(R.color.tab_unselected));
        }

        if (selected_match.getMatchTitle() != null) {
            match_title.setText(selected_match.getMatchTitle());
        }
        initClick();

        CustomUtil.loadImageWithGlide(mContext,team1_img,ApiManager.TEAM_IMG,selected_match.getTeam1Img(),R.drawable.team_loading);
        CustomUtil.loadImageWithGlide(mContext,team2_img,ApiManager.TEAM_IMG,selected_match.getTeam2Img(),R.drawable.team_loading);
        /*if (selected_match.getTeam1Img() != null && !selected_match.getTeam1Img().equals("")) {
            if (URLUtil.isValidUrl(selected_match.getTeam1Img())){
                Glide.with(mContext)
                        .load(selected_match.getTeam1Img())
                        .error(R.drawable.team_loading)
                        .into(team1_img);
            }else {
                Glide.with(mContext)
                        .load(ApiManager.TEAM_IMG + selected_match.getTeam1Img())
                        .placeholder(R.drawable.team_loading)
                        .error(R.drawable.team_loading)
                        .into(team1_img);
            }

        } else {
            Glide.with(mContext)
                    .load(R.drawable.ic_team1_placeholder)
                    .error(R.drawable.team_loading)
                    .into(team1_img);
        }

        if (selected_match.getTeam2Img() != null && !selected_match.getTeam2Img().equals("")) {
            if (URLUtil.isValidUrl(selected_match.getTeam2Img())){
                Glide.with(mContext)
                        .load(selected_match.getTeam2Img())
                        .error(R.drawable.team_loading)
                        .into(team2_img);
            }else {
                Glide.with(mContext)
                        .load(ApiManager.TEAM_IMG + selected_match.getTeam2Img())
                        .placeholder(R.drawable.team_loading)
                        .error(R.drawable.team_loading)
                        .into(team2_img);
            }

        } else {
            Glide.with(mContext)
                    .load(R.drawable.ic_team2_placeholder)
                    .error(R.drawable.team_loading)
                    .into(team2_img);
        }*/

        team1_name.setText(preferences.getMatchModel().getTeam1Sname());
        team2_name.setText(preferences.getMatchModel().getTeam2Sname());

        txtTeam1Name.setText(preferences.getMatchModel().getTeam1Name());
        txtTeam2Name.setText(preferences.getMatchModel().getTeam2Name());

        setTimer();
        getData();

    }

    @Override
    public void onResume() {
        super.onResume();
        ConstantUtil.isTimeOverShow=false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        selected_list = null;
        playerModelList = null;
        countDownTimer.cancel();
    }

    @Override
    public void initClick() {

        txtAounce.setOnClickListener(v -> {
            if (MyApp.getClickStatus()){
                /*if (preferences.getMatchModel()!=null && (preferences.getMatchModel().getTeamAXi().equalsIgnoreCase("yes") ||
                        preferences.getMatchModel().getTeamBXi().equalsIgnoreCase("yes"))) {*/
                    new FragmentUtil().addFragment((FragmentActivity) mContext,
                            R.id.cricket_select_frame,
                            new LineupsSelectionFragment(this),
                            fragmentTag(35),
                            FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                    /*TeamAnnounce bottomSheetTeam = new TeamAnnounce(mContext,playerModelList,preferences);
                    bottomSheetTeam.show(((CricketSelectPlayerActivity)mContext).getSupportFragmentManager(),
                            TeamAnnounce.TAG);*/
               // }
            }
        });

        imgAounce.setOnClickListener(v -> {
            if (MyApp.getClickStatus()){
                /*if (preferences.getMatchModel()!=null && (preferences.getMatchModel().getTeamAXi().equalsIgnoreCase("yes") ||
                        preferences.getMatchModel().getTeamBXi().equalsIgnoreCase("yes"))) {*/
                    new FragmentUtil().addFragment((FragmentActivity) mContext,
                            R.id.cricket_select_frame,
                            new LineupsSelectionFragment(this),
                            fragmentTag(35),
                            FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                    /*TeamAnnounce bottomSheetTeam = new TeamAnnounce(mContext,playerModelList,preferences);
                    bottomSheetTeam.show(((CricketSelectPlayerActivity)mContext).getSupportFragmentManager(),
                            TeamAnnounce.TAG);*/
               // }
            }
        });

        txtClear.setOnClickListener(v->{
            clearSelection();
        });

        team_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()){
                    if (selected_list!=null && selected_list.size() > 0) {
                        BottomSheetTeam bottomSheetTeam = new BottomSheetTeam(mContext, "",selected_list);
                        bottomSheetTeam.show(((CricketSelectPlayerActivity) mContext).getSupportFragmentManager(),
                                BottomSheetTeam.TAG);
                    }else {
                        CustomUtil.showTopSneakError(mContext,"Please Select Player First");
                    }
                }

            }
        });

        imgEyePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()){
                    if (selected_list!=null && selected_list.size() > 0) {
                        BottomSheetTeam bottomSheetTeam = new BottomSheetTeam(mContext, "",selected_list);
                        bottomSheetTeam.show(((CricketSelectPlayerActivity) mContext).getSupportFragmentManager(),
                                BottomSheetTeam.TAG);
                    }else {
                        CustomUtil.showTopSneakError(mContext,"Please Select Player First");
                    }
                }

            }
        });

        mToolBarBack.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });

        next_for_select_captain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Log.e(TAG, "onClick: "+ selected_list.size());
                if (MyApp.getClickStatus()){
                    if (selected_list!=null){
                        if (selected_list.size() != maxTeamPlayer) {
                            CustomUtil.showTopSneakError(mContext, "Please Select "+maxTeamPlayer+" Players");
                        } else {
                            selected_list = new ArrayList<>();
                            for (PlayerModel playerModel : CustomUtil.emptyIfNull(playerModelList)) {
                                if (checkSelect.containsKey(playerModel.getPlayerId()) && checkSelect.get(playerModel.getPlayerId()).equals(1)) {
                                    selected_list.add(playerModel);
                                }
                            }
                            if (teamCreateType != 0) {
                                for (PlayerModel playerModel : CustomUtil.emptyIfNull(playerSelectedList.getPlayerDetailList())) {
                                    for (PlayerModel model : CustomUtil.emptyIfNull(selected_list)) {
                                        if (playerModel.getPlayerId().equals(model.getPlayerId())) {
                                            model.setIsCaptain(playerModel.getIsCaptain());
                                            model.setIsWiseCaptain(playerModel.getIsWiseCaptain());
                                        }
                                    }
                                }
                            }
                            Type menu = new TypeToken<List<PlayerModel>>() {
                            }.getType();
                            String json = gson.toJson(selected_list, menu);

                            if (teamCreateType == 0) {
                        /*AddNewFragment(ChoiceCVCFragment.newInstance(json, teamCreateType, "",contestData),
                                fragmentTag(10));*/
                                new FragmentUtil().addFragment((FragmentActivity) mContext,
                                        R.id.cricket_select_frame,
                                        SelectCVCFragment.newInstance(json,teamCreateType,"",contestData),
                                        fragmentTag(14),
                                        FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                            } else {
                                String oldPlayerSelected = gson.toJson(selectPlayerListTemp);
                        /*AddNewFragment(ChoiceCVCFragment.newInstance(json, teamCreateType,oldPlayerSelected ,playerSelectedList.getId(),contestData),
                                fragmentTag(10));*/
                                LogUtil.e("temtmid",playerSelectedList.getPlayerDetailList().get(0).getTempTeamId()+"   ");
                                new FragmentUtil().addFragment((FragmentActivity) mContext,
                                        R.id.cricket_select_frame,
                                        SelectCVCFragment.newInstance(json, teamCreateType,oldPlayerSelected ,playerSelectedList.getPlayerDetailList().get(0).getTempTeamId(),contestData),
                                        fragmentTag(14),
                                        FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                            }
                        }
                    }
                }
            }
        });

        points.setOnClickListener(view -> {
            if (point_filter == 0) {
                point_filter = 1;
                arrow_point.setVisibility(View.VISIBLE);
                arrow_point.setImageResource(R.drawable.ic_up_green);
            } else if (point_filter == 1) {
                point_filter = -1;
                arrow_point.setImageResource(R.drawable.ic_down_green);
            } else if (point_filter == -1) {
                point_filter = 1;
                arrow_point.setImageResource(R.drawable.ic_up_green);
            }
            credit_filter = 0;
            lineup_filter = 0;
            sel_by = 0;
            arrow_credit.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_down_arrow));
            arrow_players.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_down_arrow));
            arrow_selby.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_down_arrow));
            points.setTypeface(Typeface.DEFAULT_BOLD);
            credits.setTypeface(Typeface.DEFAULT);
            lineup.setTypeface(Typeface.DEFAULT);
            txtSelBy.setTypeface(Typeface.DEFAULT);
            filter_data();
            Log.e(TAG, "onClick: " );
        });

        txtSelBy.setOnClickListener(view -> {
            if (sel_by == 0) {
                sel_by = 1;
                arrow_selby.setVisibility(View.VISIBLE);
                arrow_selby.setImageResource(R.drawable.ic_up_green);
            } else if (sel_by == 1) {
                sel_by = -1;
                arrow_selby.setImageResource(R.drawable.ic_down_green);
            } else if (sel_by == -1) {
                sel_by = 1;
                arrow_selby.setImageResource(R.drawable.ic_up_green);
            }
            credit_filter = 0;
            lineup_filter = 0;
            point_filter = 0;
            arrow_credit.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_down_arrow));
            arrow_players.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_down_arrow));
            arrow_point.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_down_arrow));
            points.setTypeface(Typeface.DEFAULT);
            txtSelBy.setTypeface(Typeface.DEFAULT_BOLD);
            credits.setTypeface(Typeface.DEFAULT);
            lineup.setTypeface(Typeface.DEFAULT);
            filter_data();
            Log.e(TAG, "onClick: " );
        });

        arrow_selby.setOnClickListener(view -> {
            if (sel_by == 0) {
                sel_by = 1;
                arrow_selby.setVisibility(View.VISIBLE);
                arrow_selby.setImageResource(R.drawable.ic_up_green);
            } else if (sel_by == 1) {
                sel_by = -1;
                arrow_selby.setImageResource(R.drawable.ic_down_green);
            } else if (sel_by == -1) {
                sel_by = 1;
                arrow_selby.setImageResource(R.drawable.ic_up_green);
            }
            credit_filter = 0;
            lineup_filter = 0;
            point_filter = 0;
            arrow_credit.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_down_arrow));
            arrow_players.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_down_arrow));
            arrow_point.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_down_arrow));

            points.setTypeface(Typeface.DEFAULT);
            txtSelBy.setTypeface(Typeface.DEFAULT_BOLD);
            credits.setTypeface(Typeface.DEFAULT);
            lineup.setTypeface(Typeface.DEFAULT);
            filter_data();
            Log.e(TAG, "onClick: " );
        });
        credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (credit_filter == 0) {
                    credit_filter = 1;
                    arrow_credit.setVisibility(View.VISIBLE);
                    arrow_credit.setImageResource(R.drawable.ic_up_green);
                }
                else if (credit_filter == 1) {
                    credit_filter = -1;
                    arrow_credit.setImageResource(R.drawable.ic_down_green);
                }
                else if (credit_filter == -1) {
                    credit_filter = 1;
                    arrow_credit.setImageResource(R.drawable.ic_up_green);
                }
                point_filter = 0;
                lineup_filter = 0;
                sel_by = 0;
                arrow_point.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_down_arrow));
                arrow_players.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_down_arrow));
                arrow_selby.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_down_arrow));
                points.setTypeface(Typeface.DEFAULT);
                credits.setTypeface(Typeface.DEFAULT_BOLD);
                txtSelBy.setTypeface(Typeface.DEFAULT);
                lineup.setTypeface(Typeface.DEFAULT);

                filter_data();
            }
        });
        lineup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preferences.getMatchModel().getTeamAXi().toLowerCase().equals("yes") || preferences.getMatchModel().getTeamBXi().toLowerCase().equals("yes")) {
                    if (lineup_filter == 0) {
                        lineup_filter = 1;
                        arrow_players.setImageResource(R.drawable.ic_up_green);
                    } else if (lineup_filter == 1) {
                        lineup_filter = -1;
                        arrow_players.setImageResource(R.drawable.ic_down_green);
                    } else if (lineup_filter == -1) {
                        lineup_filter = 1;
                        arrow_players.setImageResource(R.drawable.ic_up_green);
                    }
                    points.setTypeface(Typeface.DEFAULT);
                    credits.setTypeface(Typeface.DEFAULT);
                    txtSelBy.setTypeface(Typeface.DEFAULT);
                    lineup.setTypeface(Typeface.DEFAULT_BOLD);
                    point_filter = 0;
                    credit_filter = 0;
                    sel_by = 0;
                    arrow_point.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_down_arrow));
                    arrow_credit.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_down_arrow));
                    arrow_selby.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_down_arrow));

                    filter_data();
                } else {
                    CustomUtil.showTopSneakError(mContext, "Please stay tuned! Lineups coming soon.");
                }
            }
        });
        select_player_tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (sportId.equals("6") ){
                    if (select_player_tab.getSelectedTabPosition() == 0) {
                        if (wKplayerFragment != null) {
                            //type_dsc.setText("Wicket Keeper (Select min 1, max 4)");
                            wKplayerFragment.updateData(preferences,teamFiltered);
                        }
                    } else if (select_player_tab.getSelectedTabPosition() == 1) {
                        if (batPlayerFragment != null) {
                            //type_dsc.setText("Batsmen (Select min 3, max 6)");
                            batPlayerFragment.updateData(preferences,teamFiltered);
                        }
                    } else if (select_player_tab.getSelectedTabPosition() == 2) {
                        if (aRplayerFragment != null) {
                            //type_dsc.setText("All Rounders (Select min 1, max 4)");
                            aRplayerFragment.updateData(preferences,teamFiltered);
                        }
                    }
                }else {
                    if (select_player_tab.getSelectedTabPosition() == 0) {
                        if (wKplayerFragment != null) {
                            wKplayerFragment.updateData(preferences,teamFiltered);
                        }
                    } else if (select_player_tab.getSelectedTabPosition() == 1) {
                        if (batPlayerFragment != null) {
                            batPlayerFragment.updateData(preferences,teamFiltered);
                        }
                    } else if (select_player_tab.getSelectedTabPosition() == 2) {
                        if (aRplayerFragment != null) {
                            aRplayerFragment.updateData(preferences,teamFiltered);
                        }
                    }
                    else if (select_player_tab.getSelectedTabPosition() == 3) {
                        if (bowlPlayerFragment != null) {
                            bowlPlayerFragment.updateData(preferences,teamFiltered);
                        }
                    }
                    if (sportId.equals("4") ){
                        if (centerPlayerFragment != null && select_player_tab.getSelectedTabPosition() == 4) {
                            centerPlayerFragment.updateData(preferences,teamFiltered);
                        }
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        imgTeamFilter.setOnClickListener(v->{
            if (MyApp.getClickStatus()){
                showTeamFilter();
            }
        });

    }

    public void reloadData(){
        //getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        if (select_player_tab.getSelectedTabPosition() == 0) {
            if (wKplayerFragment != null) {
                // desc_select_player.setText("Wicket Keeper (Select min 1, max 4)");
                wKplayerFragment.updateData(preferences,teamFiltered);
            }
        }
        else if (select_player_tab.getSelectedTabPosition() == 1) {
            if (batPlayerFragment != null) {
                //desc_select_player.setText("Batsmen (Select min 3, max 6)");
                batPlayerFragment.updateData(preferences,teamFiltered);
            }
        }
        else if (select_player_tab.getSelectedTabPosition() == 2) {
            if (aRplayerFragment != null) {
                aRplayerFragment.updateData(preferences,teamFiltered);
            }
        }
        else if (select_player_tab.getSelectedTabPosition() == 3) {
            if (bowlPlayerFragment != null) {
                //desc_select_player.setText("Bowlers (Select min 3, max 6)");
                bowlPlayerFragment.updateData(preferences,teamFiltered);
            }
        }
        else if (select_player_tab.getSelectedTabPosition() == 4) {
            if (centerPlayerFragment != null) {
                //desc_select_player.setText("Bowlers (Select min 3, max 6)");
                centerPlayerFragment.updateData(preferences,teamFiltered);
            }
        }

        changeNextBg();

    }

    private void changeNextBg(){
        if(totalSelected==maxTeamPlayer){
            next_for_select_captain.setBackground(mContext.getResources().getDrawable(R.drawable.green_bg_round_fill));
        }else{
            next_for_select_captain.setBackground(mContext.getResources().getDrawable(R.drawable.gray_bg_round_fill));
        }
    }

    private void clearSelection() {

        Button btn_ok,btn_cancel;
        View view = LayoutInflater.from(mContext).inflate(R.layout.clear_player_dialog, null);
        btn_ok =  view.findViewById(R.id.btn_ok);
        btn_cancel =  view.findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                clearPlayer();
            }
        });
        btn_cancel.setOnClickListener(v->{
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);
        bottomSheetDialog.show();

    }

    private void clearPlayer(){
        select_player_tab.selectTab(select_player_tab.getTabAt(0));
        // imgClearSelection.setVisibility(View.GONE);
        team1_selected = 0;
        team2_selected = 0;
        team1_selected_dots = 0;
        team2_selected_dots = 0;
        show_team1_dots(team1_selected_dots+team2_selected_dots);
        //team1_count.setVisibility(View.INVISIBLE);
        //team2_count.setVisibility(View.INVISIBLE);
        team1_cnt.setText("" + team1_selected);
        team2_cnt.setText("" + team2_selected);

        selected_list = new ArrayList<>();

        Iterator it = checkSelect.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            checkSelect.put(pair.getKey().toString(),0);
            // System.out.println(pair.getKey() + " = " + pair.getValue());
        }

        sportId=preferences.getMatchModel().getSportId();
        wk=bat=ar=bowl=cr = 0;

        if (sportId.equalsIgnoreCase("1")){
            select_player_tab.getTabAt(0).setText("WK(0)");
            select_player_tab.getTabAt(1).setText("BAT(0)");
            select_player_tab.getTabAt(2).setText("AR(0)");
            select_player_tab.getTabAt(3).setText("BOWL(0)");
        }else  if (sportId.equalsIgnoreCase("2")){
            select_player_tab.getTabAt(0).setText("GK(0)");
            select_player_tab.getTabAt(1).setText("DEF(0)");
            select_player_tab.getTabAt(2).setText("MID(0)");
            select_player_tab.getTabAt(3).setText("FOR(0)");
        }else  if (sportId.equalsIgnoreCase("3")){
            select_player_tab.getTabAt(0).setText("OF(0)");
            select_player_tab.getTabAt(1).setText("IF(0)");
            select_player_tab.getTabAt(2).setText("PIT(0)");
            select_player_tab.getTabAt(3).setText("CAT(0)");
        }else  if (sportId.equalsIgnoreCase("4")){
            select_player_tab.getTabAt(0).setText("PG(0)");
            select_player_tab.getTabAt(1).setText("SG(0)");
            select_player_tab.getTabAt(2).setText("SF(0)");
            select_player_tab.getTabAt(3).setText("PF(0)");
            select_player_tab.getTabAt(4).setText("CR(0)");
        }else  if (sportId.equalsIgnoreCase("6")){
            select_player_tab.getTabAt(0).setText("GK(0)");
            select_player_tab.getTabAt(1).setText("DEF(0)");
            select_player_tab.getTabAt(2).setText("FWD(0)");
        }else  if (sportId.equalsIgnoreCase("7")){
            select_player_tab.getTabAt(0).setText("DEF(0)");
            select_player_tab.getTabAt(1).setText("AR(0)");
            select_player_tab.getTabAt(2).setText("RAID(0)");
        }

        totalSelected = 0;
        total_points = 100;
        total_remain = 0;

        //  team_total_points.setText("" + totalPoints);

        // team_total_player.setText("" + totalSelected);

        credit_left.setText("" + total_points);

        if (wKplayerFragment != null) {
            wKplayerFragment.adapterNotify();
        }
        if (bowlPlayerFragment!=null){
            bowlPlayerFragment.adapterNotify();
        }

        if (batPlayerFragment!=null){
            batPlayerFragment.adapterNotify();
        }
        if (aRplayerFragment!=null){
            aRplayerFragment.adapterNotify();
        }

        if (centerPlayerFragment!=null){
            centerPlayerFragment.adapterNotify();
        }

        txtClear.setVisibility(View.INVISIBLE);
    }

    private void showTeamFilter() {
        ImageView imgClose;
        View view = LayoutInflater.from(mContext).inflate(R.layout.team_show_filter_dialog, null);
        imgClose = view.findViewById(R.id.imgClose);
        TextView txtTeam1 = view.findViewById(R.id.txtTeam1);
        TextView txtTeam2 = view.findViewById(R.id.txtTeam2);
        txtTeam1.setText(preferences.getMatchModel().getTeam1Sname());
        txtTeam2.setText(preferences.getMatchModel().getTeam2Sname());
        TextView txtBoth = view.findViewById(R.id.txtBoth);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        if (teamFiltered==1){
            txtTeam1.setTextColor(mContext.getResources().getColor(R.color.green_pure));
            txtTeam2.setTextColor(mContext.getResources().getColor(R.color.black_pure));
            txtBoth.setTextColor(mContext.getResources().getColor(R.color.black_pure));
        }else if (teamFiltered==2){
            txtTeam1.setTextColor(mContext.getResources().getColor(R.color.black_pure));
            txtTeam2.setTextColor(mContext.getResources().getColor(R.color.green_pure));
            txtBoth.setTextColor(mContext.getResources().getColor(R.color.black_pure));
        }else if (teamFiltered==3){
            txtTeam1.setTextColor(mContext.getResources().getColor(R.color.black_pure));
            txtTeam2.setTextColor(mContext.getResources().getColor(R.color.black_pure));
            txtBoth.setTextColor(mContext.getResources().getColor(R.color.green_pure));
        }
        imgClose.setOnClickListener(view1 -> {
            bottomSheetDialog.dismiss();
        });
        txtTeam1.setOnClickListener(v->{
            teamFiltered=1;
            txtTeam1.setTextColor(mContext.getResources().getColor(R.color.green_pure));
            txtTeam2.setTextColor(mContext.getResources().getColor(R.color.black_pure));
            txtBoth.setTextColor(mContext.getResources().getColor(R.color.black_pure));
            bottomSheetDialog.dismiss();
            teamFilter();

        });
        txtTeam2.setOnClickListener(v->{
            teamFiltered=2;
            txtTeam1.setTextColor(mContext.getResources().getColor(R.color.black_pure));
            txtTeam2.setTextColor(mContext.getResources().getColor(R.color.green_pure));
            txtBoth.setTextColor(mContext.getResources().getColor(R.color.black_pure));
            bottomSheetDialog.dismiss();
            teamFilter();
        });
        txtBoth.setOnClickListener(v->{
            teamFiltered=3;
            txtTeam1.setTextColor(mContext.getResources().getColor(R.color.black_pure));
            txtTeam2.setTextColor(mContext.getResources().getColor(R.color.black_pure));
            txtBoth.setTextColor(mContext.getResources().getColor(R.color.green_pure));
            bottomSheetDialog.dismiss();
            teamFilter();
        });
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);
        if (!bottomSheetDialog.isShowing()) {
            bottomSheetDialog.show();
        }
    }

    private void filter_data() {
        MatchModel matchModel=preferences.getMatchModel();
        if (point_filter == 1) {
            if (matchModel!=null && (matchModel.getTeamAXi().equalsIgnoreCase("yes") ||
                    matchModel.getTeamBXi().equalsIgnoreCase("yes"))) {

                List<PlayerModel> tempList1 = new ArrayList<>(), tempList2 = new ArrayList<>();
                for (PlayerModel playerModel : CustomUtil.emptyIfNull(playerModelList)) {
                    if (playerModel.getPlayingXi().equalsIgnoreCase("Yes")) {
                        tempList1.add(playerModel);
                    } else {
                        tempList2.add(playerModel);
                    }
                }
                Collections.sort(tempList1, new avgPointUp());
                Collections.sort(tempList2, new avgPointUp());

                playerModelList = new ArrayList<>();
                playerModelList.addAll(tempList1);
                playerModelList.addAll(tempList2);
            }else {
                Collections.sort(playerModelList, new avgPointUp());
            }
        }
        else if (point_filter == -1) {
            if (matchModel!=null && (matchModel.getTeamAXi().equalsIgnoreCase("yes") ||
                    matchModel.getTeamBXi().equalsIgnoreCase("yes"))) {
                List<PlayerModel> tempList1 = new ArrayList<>(), tempList2 = new ArrayList<>();
                for (PlayerModel playerModel : CustomUtil.emptyIfNull(playerModelList)) {
                    if (playerModel.getPlayingXi().equalsIgnoreCase("Yes")) {
                        tempList1.add(playerModel);
                    } else {
                        tempList2.add(playerModel);
                    }
                }
                Collections.sort(tempList1, new avgPointDown());
                Collections.sort(tempList2, new avgPointDown());

                playerModelList = new ArrayList<>();
                playerModelList.addAll(tempList1);
                playerModelList.addAll(tempList2);
            }else {
                Collections.sort(playerModelList, new avgPointDown());
            }

        }
        else if (credit_filter == 1) {
            if (matchModel!=null && (matchModel.getTeamAXi().equalsIgnoreCase("yes") ||
                    matchModel.getTeamBXi().equalsIgnoreCase("yes"))) {
                List<PlayerModel> tempList1 = new ArrayList<>(), tempList2 = new ArrayList<>();
                for (PlayerModel playerModel : CustomUtil.emptyIfNull(playerModelList)) {
                    if (playerModel.getPlayingXi().equalsIgnoreCase("Yes")) {
                        tempList1.add(playerModel);
                    } else {
                        tempList2.add(playerModel);
                    }
                }
                Collections.sort(tempList1, new CreditUp());
                Collections.sort(tempList2, new CreditUp());

                playerModelList = new ArrayList<>();
                playerModelList.addAll(tempList1);
                playerModelList.addAll(tempList2);
            }else {
                Collections.sort(playerModelList, new CreditUp());
            }
           // Collections.sort(playerModelList, new CreditUp());
        }
        else if (credit_filter == -1) {
            if (matchModel!=null && (matchModel.getTeamAXi().equalsIgnoreCase("yes") ||
                    matchModel.getTeamBXi().equalsIgnoreCase("yes"))) {
                List<PlayerModel> tempList1 = new ArrayList<>(), tempList2 = new ArrayList<>();
                for (PlayerModel playerModel : CustomUtil.emptyIfNull(playerModelList)) {
                    if (playerModel.getPlayingXi().equalsIgnoreCase("Yes")) {
                        tempList1.add(playerModel);
                    } else {
                        tempList2.add(playerModel);
                    }
                }
                Collections.sort(tempList1, new CreditDown());
                Collections.sort(tempList2, new CreditDown());

                playerModelList = new ArrayList<>();
                playerModelList.addAll(tempList1);
                playerModelList.addAll(tempList2);
            }else {
                Collections.sort(playerModelList, new CreditDown());
            }
            //Collections.sort(playerModelList, new CreditDown());
        }
        else if (sel_by == 1) {
            if (matchModel!=null && (matchModel.getTeamAXi().equalsIgnoreCase("yes") ||
                    matchModel.getTeamBXi().equalsIgnoreCase("yes"))) {
                List<PlayerModel> tempList1 = new ArrayList<>(), tempList2 = new ArrayList<>();
                for (PlayerModel playerModel : CustomUtil.emptyIfNull(playerModelList)) {
                    if (playerModel.getPlayingXi().equalsIgnoreCase("Yes")) {
                        tempList1.add(playerModel);
                    } else {
                        tempList2.add(playerModel);
                    }
                }
                Collections.sort(tempList1, new SelUp());
                Collections.sort(tempList2, new SelUp());

                playerModelList = new ArrayList<>();
                playerModelList.addAll(tempList1);
                playerModelList.addAll(tempList2);
            }else {
                Collections.sort(playerModelList, new SelUp());
            }
            //Collections.sort(playerModelList, new SelUp());
        }
        else if (sel_by == -1) {
            if (matchModel!=null && (matchModel.getTeamAXi().equalsIgnoreCase("yes") ||
                    matchModel.getTeamBXi().equalsIgnoreCase("yes"))) {
                List<PlayerModel> tempList1 = new ArrayList<>(), tempList2 = new ArrayList<>();
                for (PlayerModel playerModel : CustomUtil.emptyIfNull(playerModelList)) {
                    if (playerModel.getPlayingXi().equalsIgnoreCase("Yes")) {
                        tempList1.add(playerModel);
                    } else {
                        tempList2.add(playerModel);
                    }
                }
                Collections.sort(tempList1, new SelDown());
                Collections.sort(tempList2, new SelDown());

                playerModelList = new ArrayList<>();
                playerModelList.addAll(tempList1);
                playerModelList.addAll(tempList2);
            }else {
                Collections.sort(playerModelList, new SelDown());
            }
            //Collections.sort(playerModelList, new SelDown());
        }
        else if (lineup_filter == 1) {
            List<PlayerModel> tempList1 = new ArrayList<>(), tempList2 = new ArrayList<>();
            for (PlayerModel playerModel : CustomUtil.emptyIfNull(playerModelList)) {
                if (playerModel.getPlayingXi().equalsIgnoreCase("Yes")) {
                    tempList1.add(playerModel);
                } else {
                    tempList2.add(playerModel);
                }
            }
            playerModelList = new ArrayList<>();
            playerModelList.addAll(tempList1);
            playerModelList.addAll(tempList2);
        }
        else if (lineup_filter == -1) {
            List<PlayerModel> tempList1 = new ArrayList<>(), tempList2 = new ArrayList<>();
            for (PlayerModel playerModel : CustomUtil.emptyIfNull(playerModelList)) {
                if (playerModel.getPlayingXi().equalsIgnoreCase("Yes")) {
                    tempList1.add(playerModel);
                } else {
                    tempList2.add(playerModel);
                }
            }
            playerModelList = new ArrayList<>();
            playerModelList.addAll(tempList2);
            playerModelList.addAll(tempList1);
        }

        if (select_player_tab.getSelectedTabPosition() == 0) {
            Log.e(TAG, "filter_data: 1" );
            if (wKplayerFragment != null) {
                Log.e(TAG, "filter_data: " );
                wKplayerFragment.updateData(preferences,teamFiltered);
            }
        }
        else if (select_player_tab.getSelectedTabPosition() == 1) {
            if (batPlayerFragment != null) {
                batPlayerFragment.updateData(preferences,teamFiltered);
            }
        }
        else if (select_player_tab.getSelectedTabPosition() == 2) {
            if (aRplayerFragment != null) {
                aRplayerFragment.updateData(preferences,teamFiltered);
            }
        }
        else if (select_player_tab.getSelectedTabPosition() == 3) {
            if (bowlPlayerFragment != null) {
                bowlPlayerFragment.updateData(preferences,teamFiltered);
            }
        }
        if (sportId.equals("4")){
            if (select_player_tab.getSelectedTabPosition() == 4) {
                if (centerPlayerFragment != null) {
                    centerPlayerFragment.updateData(preferences,teamFiltered);
                }
            }
        }

       /* if (sportId.equals("6")){
            if (select_player_tab.getSelectedTabPosition() == 0) {
                Log.e(TAG, "filter_data: 1" );
                if (wKplayerFragment != null) {
                    Log.e(TAG, "filter_data: " );
                    wKplayerFragment.updateData(preferences,teamFiltered);
                }
            } else if (select_player_tab.getSelectedTabPosition() == 1) {
                if (batPlayerFragment != null) {
                    batPlayerFragment.updateData(preferences,teamFiltered);
                }
            } else if (select_player_tab.getSelectedTabPosition() == 2) {
                if (aRplayerFragment != null) {
                    aRplayerFragment.updateData(preferences,teamFiltered);
                }
            }
        }else {

        }*/

    }

    private void teamFilter(){

        if (select_player_tab.getSelectedTabPosition() == 0) {
            Log.e(TAG, "filter_data: 1" );
            if (wKplayerFragment != null) {
                Log.e(TAG, "filter_data: " );
                wKplayerFragment.updateData(preferences,teamFiltered);
            }
        }
        else if (select_player_tab.getSelectedTabPosition() == 1) {
            if (batPlayerFragment != null) {
                batPlayerFragment.updateData(preferences,teamFiltered);
            }
        }
        else if (select_player_tab.getSelectedTabPosition() == 2) {
            if (aRplayerFragment != null) {
                aRplayerFragment.updateData(preferences,teamFiltered);
            }
        }
        else if (select_player_tab.getSelectedTabPosition() == 3) {
            if (bowlPlayerFragment != null) {
                bowlPlayerFragment.updateData(preferences,teamFiltered);
            }
        }
        if (sportId.equals("4")){
            if (select_player_tab.getSelectedTabPosition() == 4) {
                if (centerPlayerFragment != null) {
                    centerPlayerFragment.updateData(preferences,teamFiltered);
                }
            }
        }
    }

    private void setTimer() {
        Date date = null;
        String matchDate = "";

        SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat matchFormate = MyApp.changedFormat("dd MMM yyyy");
        try {
            date = format.parse(selected_match.getRegularMatchStartTime());
            matchDate = matchFormate.format(date);

            diff = date.getTime() - MyApp.getCurrentDate().getTime();

            Log.e(TAG, "onBindViewHolder: " + diff);
        } catch (ParseException e) {
            LogUtil.e(TAG, "onBindViewHolder: " + e.toString());
            e.printStackTrace();
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        /*if (matchModel.getMatchStatus().equals("Pending")) {*/
        countDownTimer = new CountDownTimer(diff, 1000) {
            public void onTick(long millisUntilFinished) {

                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;

                long elapsedDays = millisUntilFinished / daysInMilli;
                millisUntilFinished = millisUntilFinished % daysInMilli;

                long elapsedHours = millisUntilFinished / hoursInMilli;
                millisUntilFinished = millisUntilFinished % hoursInMilli;

                long elapsedMinutes = millisUntilFinished / minutesInMilli;
                millisUntilFinished = millisUntilFinished % minutesInMilli;

                long elapsedSeconds = millisUntilFinished / secondsInMilli;

                String diff = "";
                if (elapsedDays > 0) {
                    diff = CustomUtil.getFormater("0").format(elapsedDays) + "d " + CustomUtil.getFormater("0").format(elapsedHours) + "h";
                } else if (elapsedHours > 0) {
                    diff = CustomUtil.getFormater("0").format(elapsedHours) + "h " + CustomUtil.getFormater("0").format(elapsedMinutes) + "m";
                } else {
                    diff = CustomUtil.getFormater("0").format(elapsedMinutes) + "m " + CustomUtil.getFormater("0").format(elapsedSeconds) + "s";
                }
                timer.setText(diff);
            }

            public void onFinish() {

                timesOver();
                // Do something on finish
            }
        }.start();
        /*} else {
            holder.match_remain_time.setTextSize(14);
            holder.match_remain_time.setText(List.getMatchStatus());
        }*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        countDownTimer.cancel();
    }

    private void timesOver(){
        if (bottomSheetDialog==null){
            bottomSheetDialog = new BottomSheetDialog(mContext);
        }
        Button btn_ok;
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_events, null);
        btn_ok = view.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                ConstantUtil.isTimeOverShow=false;
                Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
                /*new FragmentUtil().resumeFragment((FragmentActivity) mContext,
                        R.id.fragment_container,
                        new HomeFragment(),
                        ((HomeActivity) mContext).fragmentTag(0),
                        FragmentUtil.ANIMATION_TYPE.SLIDE_LEFT_TO_RIGHT);*/
            }
        });

        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);
        if (!bottomSheetDialog.isShowing() && !ConstantUtil.isTimeOverShow){
            ConstantUtil.isTimeOverShow=true;
            bottomSheetDialog.show();
        }

    }

    private void getData() {
        playerModelList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            //jsonObject.put("user_id", preferences.getUserModel().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.MATCH_DETAIL_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "player: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {

                    JSONArray array = responseBody.optJSONArray("data");

                    for (int i = 0; i < array.length(); i++) {
                        try {
                            PlayerModel playerModel = gson.fromJson(array.getJSONObject(i).toString(), PlayerModel.class);
                            checkSelect.put(playerModel.getPlayerId(), 0);
                            playerModelList.add(playerModel);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    /*createTablayout();
                    credits.setTypeface(Typeface.DEFAULT_BOLD);
                    credit_filter = 1;
                    filter_data();*/

                    if (selected_list != null && selected_list.size() > 0) {
                        MatchModel matchModel=preferences.getMatchModel();
                        if (matchModel!=null && (matchModel.getTeamAXi().equalsIgnoreCase("yes") ||
                                matchModel.getTeamBXi().equalsIgnoreCase("yes"))) {
                            ArrayList<Integer> removedArr=new ArrayList<>();
                            for (int i=0;i<selected_list.size();i++) {
                                PlayerModel selectedModel=selected_list.get(i);
                                if (selectedModel.getPlayingXi().equalsIgnoreCase("no") &&
                                        !selectedModel.getOther_text().equalsIgnoreCase("substitute")){
                                    removedArr.add(i);
                                }
                            }
                            showXiDialog(removedArr);
                        }else {
                            initData();
                        }
                    }else {
                        initData();
                    }


                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void showXiDialog(ArrayList<Integer> removedArr){
        int pCount=removedArr.size();
        if (removedArr!=null && pCount>0){
            AlertDialog.Builder alert=new AlertDialog.Builder(mContext);
            alert.setCancelable(false);
            if (pCount>1) {
                alert.setTitle(pCount + " Players are not in lineups");
            }
            else {
                alert.setTitle(pCount + " Player is not in lineups");
            }
            alert.setMessage("Do you want to remove ?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    for (int position=0;position < removedArr.size();position++) {
                        selected_list.remove(removedArr.get(position)-position);
                    }
                    initData();
                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    initData();
                }
            });
            alert.show();

        }else {
            initData();
        }

    }
    private void initData() {
        credit_filter = 1;
        point_filter = 0;
        lineup_filter= 0;
        points.setTypeface(Typeface.DEFAULT);
        credits.setTypeface(Typeface.DEFAULT);//DEFAULT_BOLD
        lineup.setTypeface(Typeface.DEFAULT);
        arrow_credit.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_down_arrow));
        arrow_point.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_down_arrow));
        arrow_players.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_down_arrow));

        MatchModel matchModel=preferences.getMatchModel();
        if (matchModel!=null && (matchModel.getTeamAXi().equalsIgnoreCase("yes") ||
                matchModel.getTeamBXi().equalsIgnoreCase("yes"))) {
            //lineup_filter=1;
            credit_filter=1;
            //credit_filter = 0;
        }
        //filter_data();

        if (point_filter == 1) {
            if (matchModel!=null && (matchModel.getTeamAXi().equalsIgnoreCase("yes") ||
                    matchModel.getTeamBXi().equalsIgnoreCase("yes"))) {
                List<PlayerModel> tempList1 = new ArrayList<>(), tempList2 = new ArrayList<>();
                for (PlayerModel playerModel : CustomUtil.emptyIfNull(playerModelList)) {
                    if (playerModel.getPlayingXi().equalsIgnoreCase("Yes")) {
                        tempList1.add(playerModel);
                    } else {
                        tempList2.add(playerModel);
                    }
                }
                Collections.sort(tempList1, new avgPointUp());
                Collections.sort(tempList2, new avgPointUp());

                playerModelList = new ArrayList<>();
                playerModelList.addAll(tempList1);
                playerModelList.addAll(tempList2);
            }else {
                Collections.sort(playerModelList, new avgPointUp());
            }
            //Collections.sort(playerModelList, new avgPointUp());
        }
        else if (point_filter == -1) {
            if (matchModel!=null && (matchModel.getTeamAXi().equalsIgnoreCase("yes") ||
                    matchModel.getTeamBXi().equalsIgnoreCase("yes"))) {
                List<PlayerModel> tempList1 = new ArrayList<>(), tempList2 = new ArrayList<>();
                for (PlayerModel playerModel : CustomUtil.emptyIfNull(playerModelList)) {
                    if (playerModel.getPlayingXi().equalsIgnoreCase("Yes")) {
                        tempList1.add(playerModel);
                    } else {
                        tempList2.add(playerModel);
                    }
                }
                Collections.sort(tempList1, new avgPointDown());
                Collections.sort(tempList2, new avgPointDown());

                playerModelList = new ArrayList<>();
                playerModelList.addAll(tempList1);
                playerModelList.addAll(tempList2);
            }else {
                Collections.sort(playerModelList, new avgPointDown());
            }
            //Collections.sort(playerModelList, new avgPointDown());
        }
        else if (credit_filter == 1) {
            if (matchModel!=null && (matchModel.getTeamAXi().equalsIgnoreCase("yes") ||
                    matchModel.getTeamBXi().equalsIgnoreCase("yes"))) {
                List<PlayerModel> tempList1 = new ArrayList<>(), tempList2 = new ArrayList<>();
                for (PlayerModel playerModel : CustomUtil.emptyIfNull(playerModelList)) {
                    if (playerModel.getPlayingXi().equalsIgnoreCase("Yes")) {
                        tempList1.add(playerModel);
                    } else {
                        tempList2.add(playerModel);
                    }
                }
                Collections.sort(tempList1, new CreditUp());
                Collections.sort(tempList2, new CreditUp());

                playerModelList = new ArrayList<>();
                playerModelList.addAll(tempList1);
                playerModelList.addAll(tempList2);
            }else {
                Collections.sort(playerModelList, new CreditUp());
            }
            //Collections.sort(playerModelList, new CreditUp());
        }
        else if (credit_filter == -1) {
            if (matchModel!=null && (matchModel.getTeamAXi().equalsIgnoreCase("yes") ||
                    matchModel.getTeamBXi().equalsIgnoreCase("yes"))) {
                List<PlayerModel> tempList1 = new ArrayList<>(), tempList2 = new ArrayList<>();
                for (PlayerModel playerModel : CustomUtil.emptyIfNull(playerModelList)) {
                    if (playerModel.getPlayingXi().equalsIgnoreCase("Yes")) {
                        tempList1.add(playerModel);
                    } else {
                        tempList2.add(playerModel);
                    }
                }
                Collections.sort(tempList1, new CreditDown());
                Collections.sort(tempList2, new CreditDown());

                playerModelList = new ArrayList<>();
                playerModelList.addAll(tempList1);
                playerModelList.addAll(tempList2);
            }else {
                Collections.sort(playerModelList, new CreditDown());
            }
            //Collections.sort(playerModelList, new CreditDown());
        }
        else if (lineup_filter == 1) {
            List<PlayerModel> tempList1 = new ArrayList<>(), tempList2 = new ArrayList<>();
            for (PlayerModel playerModel : CustomUtil.emptyIfNull(playerModelList)) {
                if (playerModel.getPlayingXi().equalsIgnoreCase("Yes")) {
                    tempList1.add(playerModel);
                } else {
                    tempList2.add(playerModel);
                }
            }
            playerModelList = new ArrayList<>();
            playerModelList.addAll(tempList1);
            playerModelList.addAll(tempList2);
        }
        else if (lineup_filter == -1) {
            List<PlayerModel> tempList1 = new ArrayList<>(), tempList2 = new ArrayList<>();
            for (PlayerModel playerModel : CustomUtil.emptyIfNull(playerModelList)) {
                if (playerModel.getPlayingXi().equalsIgnoreCase("Yes")) {
                    tempList1.add(playerModel);
                } else {
                    tempList2.add(playerModel);
                }
            }
            playerModelList = new ArrayList<>();
            playerModelList.addAll(tempList2);
            playerModelList.addAll(tempList1);
        }

        if (selected_list != null && selected_list.size() > 0) {
            LogUtil.e(TAG, "initData: " + selected_list.size());
            /*MatchModel matchModel=preferences.getMatchModel();
            if (matchModel!=null && (matchModel.getTeamAXi().equalsIgnoreCase("yes") ||
                    matchModel.getTeamBXi().equalsIgnoreCase("yes"))) {
                ArrayList<Integer> removedArr=new ArrayList<>();
                for (int i=0;i<selected_list.size();i++) {
                    PlayerModel selectedModel=selected_list.get(i);
                    if (selectedModel.getPlayingXi().equalsIgnoreCase("no")){
                        removedArr.add(i);
                    }
                }
                for (int position=0;position<removedArr.size();position++) {
                    selected_list.remove(removedArr.get(position)-position);
                }
            }*/

            for (PlayerModel model : CustomUtil.emptyIfNull(selected_list)) {
                if (checkSelect.containsKey(model.getPlayerId())) {

                    checkSelect.put(model.getPlayerId(), 1);
                    model.setSelected(true);
                    if (sportId.equals("1")){
                        if (model.getPlayerType().equalsIgnoreCase("wk")) {
                            wk += 1;
                        } else if (model.getPlayerType().equalsIgnoreCase("bat")) {
                            bat += 1;
                        } else if (model.getPlayerType().equalsIgnoreCase("ar")) {
                            ar += 1;
                        } else if (model.getPlayerType().equalsIgnoreCase("bowl")) {
                            bowl += 1;
                        }
                    }
                    else if (sportId.equals("2")){
                        if (model.getPlayerType().equalsIgnoreCase("gk")) {
                            wk += 1;
                        } else if (model.getPlayerType().equalsIgnoreCase("def")) {
                            bat += 1;
                        } else if (model.getPlayerType().equalsIgnoreCase("mid")) {
                            ar += 1;
                        } else if (model.getPlayerType().equalsIgnoreCase("for")) {
                            bowl += 1;
                        }
                    }
                    else if (sportId.equals("3")){
                        if (model.getPlayerType().equalsIgnoreCase("of")) {
                            wk += 1;
                        } else if (model.getPlayerType().equalsIgnoreCase("if")) {
                            bat += 1;
                        } else if (model.getPlayerType().equalsIgnoreCase("pit")) {
                            ar += 1;
                        } else if (model.getPlayerType().equalsIgnoreCase("cat")) {
                            bowl += 1;
                        }
                    }
                    else if (sportId.equals("4")){
                        if (model.getPlayerType().equalsIgnoreCase("pg")) {
                            wk += 1;
                        } else if (model.getPlayerType().equalsIgnoreCase("sg")) {
                            bat += 1;
                        } else if (model.getPlayerType().equalsIgnoreCase("sf")) {
                            ar += 1;
                        } else if (model.getPlayerType().equalsIgnoreCase("pf")) {
                            bowl += 1;
                        }else if (model.getPlayerType().equalsIgnoreCase("cr")) {
                            cr += 1;
                        }
                    }
                    else if (sportId.equals("6")){
                        if (model.getPlayerType().equalsIgnoreCase("gk")) {
                            wk += 1;
                        } else if (model.getPlayerType().equalsIgnoreCase("def")) {
                            bat += 1;
                        } else if (model.getPlayerType().equalsIgnoreCase("fwd")) {
                            ar += 1;
                        }
                    }
                    else if (sportId.equals("7")){
                        if (model.getPlayerType().equalsIgnoreCase("def")) {
                            wk += 1;
                        } else if (model.getPlayerType().equalsIgnoreCase("ar")) {
                            bat += 1;
                        } else if (model.getPlayerType().equalsIgnoreCase("raid")) {
                            ar += 1;
                        }
                    }

                    if (model.getTeamId().equals(preferences.getMatchModel().getTeam1())) {
                        team1_selected += 1;
                        team1_selected_dots+=1;
                    }
                    else if (model.getTeamId().equals(preferences.getMatchModel().getTeam2())) {
                        team2_selected += 1;
                        team2_selected_dots+=1;
                    }

                    total_remain-=1;
                    totalSelected += 1;
                    total_points -= CustomUtil.convertFloat(model.getPlayerRank());

                }
            }

            for (PlayerModel selectedModel : CustomUtil.emptyIfNull(selected_list)) {
                for (PlayerModel model : CustomUtil.emptyIfNull(playerModelList)) {
                    if (selectedModel.getPlayerId().equalsIgnoreCase(model.getPlayerId())){
                        model.setSelected(true);
                    }
                }
            }
        }

        changeNextBg();

        viewPagerAdapter = new ViewPagerAdapter(this);
        select_player_viewpager.setAdapter(viewPagerAdapter);
        ConstantUtil.reduceDragSensitivity(select_player_viewpager);
        select_player_viewpager.setUserInputEnabled(ApiManager.isPagerSwipe);

       // LogUtil.d("respmId",selected_match.getSportId());

        String[] title=null;
        if (sportId.equals("1")){
            title=new String[4];
            select_player_viewpager.setOffscreenPageLimit(4);
            title[0]="WK(0)";
            title[1]="BAT(0)";
            title[2]="AR(0)";
            title[3]="BOWL(0)";
        }
        else  if (sportId.equals("2")) {
            title=new String[4];
            select_player_viewpager.setOffscreenPageLimit(4);
            title[0]="GK(0)";
            title[1]="DEF(0)";
            title[2]="MID(0)";
            title[3]="FOR(0)";
        }
        else  if (sportId.equals("3")) {
            title=new String[4];
            select_player_viewpager.setOffscreenPageLimit(4);
            title[0]="OF(0)";
            title[1]="IF(0)";
            title[2]="PIT(0)";
            title[3]="CAT(0)";
        }
        else if (sportId.equals("4")) {
            title=new String[5];
            select_player_viewpager.setOffscreenPageLimit(5);
            title[0]="PG(0)";
            title[1]="SG(0)";
            title[2]="SF(0)";
            title[3]="PF(0)";
            title[4]="CR(0)";
        }
        else if (sportId.equals("6")) {
            title=new String[3];
            select_player_viewpager.setOffscreenPageLimit(3);
            title[0]="GK(0)";
            title[1]="DEF(0)";
            title[2]="FWD(0)";
        }
        else if (sportId.equals("7")) {
            title=new String[3];
            select_player_viewpager.setOffscreenPageLimit(3);
            title[0]="DEF(0)";
            title[1]="AR(0)";
            title[2]="RAID(0)";
        }

        String[] finalTitle = title;
        new TabLayoutMediator(select_player_tab, select_player_viewpager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(finalTitle[position]);
                    }
                }).attach();

        if (selected_list != null && selected_list.size() > 0) {
            if (selected_match.getSportId().equals("1")){
                select_player_tab.getTabAt(0).setText("WK (" + wk + ")");
                select_player_tab.getTabAt(1).setText("BAT (" + bat + ")");
                select_player_tab.getTabAt(2).setText("AR (" + ar + ")");
                select_player_tab.getTabAt(3).setText("BOWL (" + bowl + ")");
            }
            else if (selected_match.getSportId().equals("2")) {
                select_player_tab.getTabAt(0).setText("GK (" + wk + ")");
                select_player_tab.getTabAt(1).setText("DEF (" + bat + ")");
                select_player_tab.getTabAt(2).setText("MID (" + ar + ")");
                select_player_tab.getTabAt(3).setText("FOR (" + bowl + ")");
            }
            else if (selected_match.getSportId().equals("3")) {
                select_player_tab.getTabAt(0).setText("OF (" + wk + ")");
                select_player_tab.getTabAt(1).setText("IF (" + bat + ")");
                select_player_tab.getTabAt(2).setText("PIT (" + ar + ")");
                select_player_tab.getTabAt(3).setText("CAT (" + bowl + ")");
            }
            else if (selected_match.getSportId().equals("4")) {
                select_player_tab.getTabAt(0).setText("PG (" + wk + ")");
                select_player_tab.getTabAt(1).setText("SG (" + bat + ")");
                select_player_tab.getTabAt(2).setText("SF (" + ar + ")");
                select_player_tab.getTabAt(3).setText("PF (" + bowl + ")");
                select_player_tab.getTabAt(4).setText("CR (" + cr + ")");
            }
            else if (selected_match.getSportId().equals("6")) {
                select_player_tab.getTabAt(0).setText("GK (" + wk + ")");
                select_player_tab.getTabAt(1).setText("DEF (" + bat + ")");
                select_player_tab.getTabAt(2).setText("FWD (" + ar + ")");
            }
            else if (selected_match.getSportId().equals("7")) {
                select_player_tab.getTabAt(0).setText("DEF (" + wk + ")");
                select_player_tab.getTabAt(1).setText("AR (" + bat + ")");
                select_player_tab.getTabAt(2).setText("RAID (" + ar + ")");
            }

            //select_player_tab.selectTab(select_player_tab.getTabAt(editedTab));
            //show_team1_dots(team1_selected_dots);
            //show_team2_dots(team2_selected_dots);
            show_team1_dots(team1_selected_dots+team2_selected_dots);

          //  total_player_selected.setText(totalSelected+"/11");
            team1_cnt.setText(team1_selected_dots+"");
            team2_cnt.setText(team2_selected_dots+"");

            credit_left.setText("" + total_points);
            txtClear.setVisibility(View.VISIBLE);
        }

        filter_data();
    }

    public void show_team1_dots(int team1_selected_dots) {

        final int childCount = team1_dots.getChildCount();
        for (int j = 0; j < team1_dots.getChildCount(); j++) {
            if(Integer.parseInt(String.valueOf(team1_dots.getChildAt(j).getTag())) <= team1_selected_dots) {
                View view = team1_dots.getChildAt(j);
                view.setBackground(mContext.getResources().getDrawable(R.drawable.selected_player_bubble));
            } else {
                View view = team1_dots.getChildAt(j);
                view.setBackground(mContext.getResources().getDrawable(R.drawable.unselected_player_bubble));
            }
        }

    }

    /*private void show_team2_dots(int team2_selected_dots) {
        final int childCount = team2_dots.getChildCount();
        for (int j = team2_dots.getChildCount()-1; j >= 0; j--) {
            if(Integer.parseInt(String.valueOf(team2_dots.getChildAt(j).getTag())) <= team2_selected_dots) {
                View view = team2_dots.getChildAt(j);
                view.setBackground(mContext.getResources().getDrawable(R.drawable.selected_player_bubble));
            } else {
                View view = team2_dots.getChildAt(j);
                view.setBackground(mContext.getResources().getDrawable(R.drawable.unselected_player_bubble));
            }
        }
    }*/

    public boolean validateCricketClick(PlayerModel playerModel) {
        if (selected_list == null) {
            selected_list = new ArrayList<>();
        }
        if (checkSelect.get(playerModel.getPlayerId()) == 1) {
//-----------------------------------------------------
            int i = -1,j=0;
            for( PlayerModel playerModel1 : selected_list){
                if(playerModel1.getPlayerId().equals(playerModel.getPlayerId())){
                    i = j;
                }
                j++;
            }
            selected_list.remove(i);
            playerModel.setSelected(false);
            ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
            //selected_list.remove(playerModel);
//--------------------------------------------------------
            checkSelect.put(playerModel.getPlayerId(), 0);

            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                team1_selected -= 1;
                team1_selected_dots -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //show_team1_dots(team1_selected_dots);
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                team2_selected -= 1;
                team2_selected_dots -= 1;
                //team2_selected_players.setText("" + team2_selected);
                //show_team2_dots(team2_selected_dots);
            }
            show_team1_dots(team1_selected_dots+team2_selected_dots);
            total_remain += 1;
            totalSelected -= 1;
            total_points += CustomUtil.convertFloat(playerModel.getPlayerRank());
            credit_left.setText("" + total_points);
            //total_player_selected.setText(totalSelected+"/11");
            team1_cnt.setText(team1_selected_dots+"");
            team2_cnt.setText(team2_selected_dots+"");

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "wk")) {
                wk -= 1;
                if (wk >= 0) {
                    select_player_tab.getTabAt(0).setText("WK(" + wk + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "bat")) {
                bat -= 1;
                if (bat >= 0) {
                    select_player_tab.getTabAt(1).setText("BAT(" + bat + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "bowl")) {
                bowl -= 1;
                if (bowl >= 0) {
                    select_player_tab.getTabAt(3).setText("BOWL(" + bowl + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "ar")) {
                ar -= 1;
                if (ar >= 0) {
                    select_player_tab.getTabAt(2).setText("AR(" + ar + ")");
                }
            }

            changeNextBg();
            if (selected_list.size()>0){
                txtClear.setVisibility(View.VISIBLE);
            }else {
                txtClear.setVisibility(View.INVISIBLE);
            }
            return true;
        }
        else {
            boolean isSelected = false;
            if (totalSelected == maxTeamPlayer) {
                CustomUtil.showTopSneakError(mContext, "Maximum "+maxTeamPlayer+" players are allowed, click NEXT to proceed");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId()) &&
                    team1_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId()) &&
                    team2_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }

            if ((total_points - Float.parseFloat(playerModel.getPlayerRank())) < 0) {
                CustomUtil.showTopSneakError(mContext, "You don't have enough credit to pick this player");
                return false;
            }

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "wk")) {
                if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (bat_min - bat)) && bowl <= bowl_min && bat <= bat_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Batsmen");
                    } else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Bowlers");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (bat_min - bat)) && ar <= ar_min && bat <= bat_min) {
                    if (bat_min > bat) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Batsmen");
                    } else if (ar_min > ar) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" All-Rounder");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (ar_min - ar)) && bowl <= bowl_min && ar <= ar_min) {
                    if (ar_min > ar) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" All-Rounder");
                    } else if (bowl_min > bowl) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Bowlers");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Batsmen");
                } else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Bowlers");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" All-Rounder");
                } else if (wk == wk_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+wk_max+" Wicket-keeper allowed");
                } else {
                    wk += 1;
                    isSelected = true;
                    select_player_tab.getTabAt(0).setText("WK(" + wk + ")");
                    /// select_tab.getTabAt(0).setText("WK(" + wk + ")");
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "bat")) {
                if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (wk_min - wk)) && bowl <= bowl_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Wicket-Keeper");
                    } else if (bowl_min >= (bowl_min - bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Bowlers");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (wk_min - wk)) && ar <= ar_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Wicket-Keeper");
                    } else if (ar_min <= (ar_min - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" All-Rounder");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (ar_min - ar)) && bowl <= bowl_min && ar <= ar_min) {
                    if (ar_min <= (ar_min - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" All-Rounder");
                    } else if (bowl_min >= (bowl_min - bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Bowlers");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Bowlers");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Wicket-Keeper");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" All-Rounder");
                } else if (bat == bat_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+bat_max+" Batsmen are allowed");
                } else {
                    bat += 1;
                    select_player_tab.getTabAt(1).setText("BAT(" + bat + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "ar")) {
                if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (wk_min - wk)) && bat <= bat_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Wicket-Keeper");
                    } else if (bat_min >= (bat_min - bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Batsmen");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (wk_min - wk)) && bowl <= bowl_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Wicket-Keeper");
                    } else if (bowl_min >= (bowl_min - bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Bowlers");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (bat_min - bat)) && bowl <= bowl_min && bat <= bat_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Batsmen");
                    } else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Bowlers");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Bowlers");
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Batsmen");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Wicket-Keeper");
                } else if (ar == ar_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+ar_max+" All-Rounders are allowed");
                } else {
                    ar += 1;
                    select_player_tab.getTabAt(2).setText("AR(" + ar + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "bowl")) {
                if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (wk_min - wk)) && bat <= bat_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Wicket-Keeper");
                    } else if (bat_min >= (bat_min - bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Batsmen");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (wk_min - wk)) && ar <= ar_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Wicket-Keeper");
                    } else if (ar_min <= (ar_min - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" All-Rounder");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (ar_min - ar)) && bat <= bat_min && ar <= ar_min) {
                    if (ar_min <= (ar_min - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" All-Rounder");
                    } else if (bat_min >= (bat_min - bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Batsmen");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Batsmen");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Wicket-Keeper");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" All-Rounder");
                } else if (bowl == bowl_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+bowl_max+" Bowlers are allowed");
                } else {
                    bowl += 1;
                    select_player_tab.getTabAt(3).setText("BOWL(" + bowl + ")");
                    isSelected = true;
                }
            }

            if (isSelected) {
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                    team1_selected += 1;
                    team1_selected_dots += 1;
                    //show_team1_dots(team1_selected_dots);
                }
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                    team2_selected += 1;
                    team2_selected_dots += 1;
                   // show_team2_dots(team2_selected_dots);
                }
                show_team1_dots(team1_selected_dots+team2_selected_dots);
                total_remain -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //team2_selected_players.setText("" + team2_selected);
                selected_list.add(playerModel);
                playerModel.setSelected(true);
                ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);

                checkSelect.put(playerModel.getPlayerId(), 1);
                totalSelected += 1;
                total_points -= CustomUtil.convertFloat(playerModel.getPlayerRank());
                credit_left.setText("" + total_points);
               // total_player_selected.setText(totalSelected+"/11");
                team1_cnt.setText(team1_selected_dots+"");
                team2_cnt.setText(team2_selected_dots+"");

                changeNextBg();

                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }

                return true;
            } else {
                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        }
    }

    public boolean validateFootballClick(PlayerModel playerModel) {
        if (selected_list == null) {
            selected_list = new ArrayList<>();
        }
        MatchModel matchModel=preferences.getMatchModel();
        if (checkSelect.get(playerModel.getPlayerId()) == 1) {

            int i = -1,j=0;
            for( PlayerModel playerModel1 : selected_list){
                if(playerModel1.getPlayerId().equals(playerModel.getPlayerId())){
                    i = j;
                }
                j++;
            }
            selected_list.remove(i);
            playerModel.setSelected(false);
            ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
            //selected_list.remove(playerModel);
//--------------------------------------------------------
            checkSelect.put(playerModel.getPlayerId(), 0);

            if (CustomUtil.stringEqualsIgnore(matchModel.getTeam1(), playerModel.getTeamId())) {
                team1_selected -= 1;
                team1_selected_dots -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //show_team1_dots(team1_selected_dots);
            }
            if (CustomUtil.stringEqualsIgnore(matchModel.getTeam2(), playerModel.getTeamId())) {
                team2_selected -= 1;
                team2_selected_dots -= 1;
                //team2_selected_players.setText("" + team2_selected);
                // show_team2_dots(team2_selected_dots);
            }
            show_team1_dots(team1_selected_dots+team2_selected_dots);
            total_remain += 1;
            totalSelected -= 1;
            total_points += CustomUtil.convertFloat(playerModel.getPlayerRank());
            credit_left.setText("" + total_points);
            //total_player_selected.setText(totalSelected+"/11");
            team1_cnt.setText(team1_selected_dots+"");
            team2_cnt.setText(team2_selected_dots+"");

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "gk")) {
                wk -= 1;
                if (wk >= 0) {
                    select_player_tab.getTabAt(0).setText("GK(" + wk + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "def")) {
                bat -= 1;
                if (bat >= 0) {
                    select_player_tab.getTabAt(1).setText("DEF(" + bat + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "for")) {
                bowl -= 1;
                if (bowl >= 0) {
                    select_player_tab.getTabAt(3).setText("FOR(" + bowl + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "mid")) {
                ar -= 1;
                if (ar >= 0) {
                    select_player_tab.getTabAt(2).setText("MID(" + ar + ")");
                }
            }

            changeNextBg();

            if (selected_list.size()>0){
                txtClear.setVisibility(View.VISIBLE);
            }else {
                txtClear.setVisibility(View.INVISIBLE);
            }
            return true;
        }
        else {

            boolean isSelected = false;
            if (totalSelected == maxTeamPlayer) {
                CustomUtil.showTopSneakError(mContext, "Maximum "+maxTeamPlayer+" players are allowed, click NEXT to proceed");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(matchModel.getTeam1(), playerModel.getTeamId()) &&
                    team1_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(matchModel.getTeam2(), playerModel.getTeamId()) &&
                    team2_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }

            if ((total_points - Float.parseFloat(playerModel.getPlayerRank())) < 0) {
                CustomUtil.showTopSneakError(mContext, "You don't have enough credit to pick this player");
                return false;
            }

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "gk")) {
                if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (bat_min - bat)) && bowl <= bowl_min && bat <= bat_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                    } else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Forward");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (bat_min - bat)) && ar <= ar_min && bat <= bat_min) {
                    if (bat_min > bat) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                    } else if (ar_min > ar) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Mid-Fielders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (ar_min - ar)) && bowl <= bowl_min && ar <= ar_min) {
                    if (ar_min > ar) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Mid-Fielders");
                    } else if (bowl_min > bowl) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Forward");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                } else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Forward");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Mid-Fielders");
                } else if (wk == wk_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+wk_max+" Goal-keeper are allowed");
                } else {
                    wk += 1;
                    isSelected = true;
                    select_player_tab.getTabAt(0).setText("GK(" + wk + ")");
                    /// select_tab.getTabAt(0).setText("WK(" + wk + ")");
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "def")) {
                if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (wk_min - wk)) && bowl <= bowl_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Goal-keeper");
                    } else if (bowl_min >= (bowl_min - bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Forward");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (wk_min - wk)) && ar <= ar_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Goal-keeper");
                    } else if (ar_min >= (ar_min - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Mid-Fielders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (ar_min - ar)) && bowl <= bowl_min && ar <= ar_min) {
                    if (ar_min <= (ar_min - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Mid-Fielders");
                    } else if (bowl_min >= (bowl_min - bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Forward");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Forward");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Goal-keeper");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Mid-Fielders");
                } else if (bat == bat_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+bat_max+" Defenders are allowed");
                } else {
                    bat += 1;
                    select_player_tab.getTabAt(1).setText("DEF(" + bat + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "mid")) {
                if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (wk_min - wk)) && bat <= bat_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Goal-keeper");
                    } else if (bat_min >= (bat_min - bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (wk_min - wk)) && bowl <= bowl_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Goal-keeper");
                    } else if (bowl_min >= (bowl_min - bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Forward");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (bat_min - bat)) && bowl <= bowl_min && bat <= bat_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                    } else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Forward");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Forward");
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Goal-keeper");
                } else if (ar == ar_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+ar_max+" Mid-Fielders are allowed");
                } else {
                    ar += 1;
                    select_player_tab.getTabAt(2).setText("MID(" + ar + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "for")) {
                if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (wk_min - wk)) && bat <= bat_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Goal-keeper");
                    } else if (bat_min >= (bat_min - bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (wk_min - wk)) && ar <= ar_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Goal-keeper");
                    } else if (ar_min >= (ar_min - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Mid-Fielders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (ar_min - ar)) && bat <= bat_min && ar <= ar_min) {
                    if (ar_min <= (ar_min - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Mid-Fielders");
                    } else if (bat_min >= (bat_min - bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Goal-keeper");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Mid-Fielders");
                } else if (bowl == bowl_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+bowl_max+" Forwards are allowed");
                } else {
                    bowl += 1;
                    select_player_tab.getTabAt(3).setText("FOR(" + bowl + ")");
                    isSelected = true;
                }
            }

            if (isSelected) {
                if (CustomUtil.stringEqualsIgnore(matchModel.getTeam1(), playerModel.getTeamId())) {
                    team1_selected += 1;
                    team1_selected_dots += 1;
                    //show_team1_dots(team1_selected_dots);
                }
                if (CustomUtil.stringEqualsIgnore(matchModel.getTeam2(), playerModel.getTeamId())) {
                    team2_selected += 1;
                    team2_selected_dots += 1;
                    // show_team2_dots(team2_selected_dots);
                }
                show_team1_dots(team1_selected_dots+team2_selected_dots);
                total_remain -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //team2_selected_players.setText("" + team2_selected);
                selected_list.add(playerModel);
                playerModel.setSelected(true);
                ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
                checkSelect.put(playerModel.getPlayerId(), 1);
                totalSelected += 1;
                total_points -= CustomUtil.convertFloat(playerModel.getPlayerRank());
                credit_left.setText("" + total_points);
                //total_player_selected.setText(totalSelected+"/11");
                team1_cnt.setText(team1_selected_dots+"");
                team2_cnt.setText(team2_selected_dots+"");

                changeNextBg();

                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }
                return true;
            } else {
                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        }
    }

    public boolean validateBasketballClick(PlayerModel playerModel) {
        if (selected_list == null) {
            selected_list = new ArrayList<>();
        }

        MatchModel matchModel=preferences.getMatchModel();

        if (checkSelect.get(playerModel.getPlayerId()) == 1) {
//-----------------------------------------------------
            int i = -1,j=0;
            for( PlayerModel playerModel1 : selected_list){
                if(playerModel1.getPlayerId().equals(playerModel.getPlayerId())){
                    i = j;
                }
                j++;
            }
            selected_list.remove(i);
            playerModel.setSelected(false);
            ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
            //selected_list.remove(playerModel);
//--------------------------------------------------------
            checkSelect.put(playerModel.getPlayerId(), 0);

            if (CustomUtil.stringEqualsIgnore(matchModel.getTeam1(), playerModel.getTeamId())) {
                team1_selected -= 1;
                team1_selected_dots -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //show_team1_dots(team1_selected_dots);
            }
            if (CustomUtil.stringEqualsIgnore(matchModel.getTeam2(), playerModel.getTeamId())) {
                team2_selected -= 1;
                team2_selected_dots -= 1;
                //team2_selected_players.setText("" + team2_selected);
                // show_team2_dots(team2_selected_dots);
            }
            show_team1_dots(team1_selected_dots+team2_selected_dots);
            total_remain += 1;
            totalSelected -= 1;
            total_points += CustomUtil.convertFloat(playerModel.getPlayerRank());
            credit_left.setText("" + total_points);
            //total_player_selected.setText(totalSelected+"/11");
            team1_cnt.setText(team1_selected_dots+"");
            team2_cnt.setText(team2_selected_dots+"");

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "pg")) {
                wk -= 1;
                if (wk >= 0) {
                    select_player_tab.getTabAt(0).setText("PG(" + wk + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "sg")) {
                bat -= 1;
                if (bat >= 0) {
                    select_player_tab.getTabAt(1).setText("SG(" + bat + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "sf")) {
                ar -= 1;
                if (ar >= 0) {
                    select_player_tab.getTabAt(2).setText("SF(" + ar + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "pf")) {
                bowl -= 1;
                if (bowl >= 0) {
                    select_player_tab.getTabAt(3).setText("PF(" + bowl + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "cr")) {
                cr -= 1;
                if (cr >= 0) {
                    select_player_tab.getTabAt(4).setText("CR(" + cr + ")");
                }
            }

            changeNextBg();

            if (selected_list.size()>0){
                txtClear.setVisibility(View.VISIBLE);
            }else {
                txtClear.setVisibility(View.INVISIBLE);
            }

            return true;
        }
        else {
            boolean isSelected = false;
            if (totalSelected == maxTeamPlayer) {
                CustomUtil.showTopSneakError(mContext, "Maximum "+maxTeamPlayer+" players are allowed, click NEXT to proceed");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(matchModel.getTeam1(), playerModel.getTeamId()) &&
                    team1_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(matchModel.getTeam2(), playerModel.getTeamId()) &&
                    team2_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }

            if ((total_points - Float.parseFloat(playerModel.getPlayerRank())) < 0) {
                CustomUtil.showTopSneakError(mContext, "You don't have enough credit to pick this player");
                return false;
            }

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "pg")) {
                //5-4-3
                if ((maxTeamPlayer - totalSelected) <= ((cr_min - cr) + (bowl_min - bowl)+ (bat_min - bat)) && cr <= cr_min && bowl <= bowl_min && bat <= bat_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }
                    else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                    else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (bowl_min - bowl)+ (cr_min - cr)) && ar <= ar_min && bowl <= bowl_min && cr <= cr_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (bowl_min - bowl)+ (bat_min - bat)) && ar <= ar_min && bowl <= bowl_min && bat <= bat_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }
                } //2-5-3
                else if ((maxTeamPlayer - totalSelected) <= ((cr_min - cr) + (ar_min - ar)+ (bat_min - bat)) && cr <= cr_min && ar <= ar_min && bat <= bat_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }
                }

                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) +(bat_min - bat)) && ar <= ar_min && bat <= bat_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    } else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl)+ (cr_min - cr)) && bowl <= bowl_min && cr <= cr_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl)+ (bat_min - bat)) && bowl <= bowl_min && bat <= bat_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar)+ (cr_min - cr)) && ar <= ar_min && cr <= cr_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat)+ (cr_min - cr)) && bat <= bat_min && cr <= cr_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar)+ (bowl_min - bowl)) && ar <= ar_min && bowl <= bowl_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }
                }

                else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guards");
                } else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                }else if ((maxTeamPlayer - totalSelected) <= (cr_min - cr) && cr <= cr_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                } else if (wk == wk_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+wk_max+" Point Guards allowed");
                } else {
                    wk += 1;
                    isSelected = true;
                    select_player_tab.getTabAt(0).setText("PG(" + wk + ")");
                    /// select_tab.getTabAt(0).setText("WK(" + wk + ")");
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "sg")) {

                if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (bowl_min - bowl)+ (cr_min - cr)) && ar <= ar_min && bowl <= bowl_min && cr <= cr_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                } //5-4-2
                else if ((maxTeamPlayer - totalSelected) <= ((cr_min - cr) + (bowl_min - bowl)+ (wk_min - wk)) && cr <= cr_min && bowl <= bowl_min && wk <= wk_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                } //3-4-2
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (bowl_min - bowl)+ (wk_min - wk)) && ar <= ar_min && bowl <= bowl_min && wk <= wk_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                } //2-5-3
                else if ((maxTeamPlayer - totalSelected) <= ((cr_min - cr) + (ar_min - ar)+ (wk_min - wk)) && cr <= cr_min && ar <= ar_min && wk <= wk_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                }

                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) +(wk_min - wk)) && ar <= ar_min && wk <= wk_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    } else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl)+ (cr_min - cr)) && bowl <= bowl_min && cr <= cr_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl)+ (wk_min - wk)) && bowl <= bowl_min && wk <= wk_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar)+ (cr_min - cr)) && ar <= ar_min && cr <= cr_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((wk_min - wk)+ (cr_min - cr)) && wk <= wk_min && cr <= cr_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar)+ (bowl_min - bowl)) && ar <= ar_min && bowl <= bowl_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }
                }

                else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guards allowed");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                } else if ((maxTeamPlayer - totalSelected) <= (cr_min - cr) && cr <= cr_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                } else if (bat == bat_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+bat_max+" Shooting Guards are allowed");
                } else {
                    bat += 1;
                    select_player_tab.getTabAt(1).setText("SG(" + bat + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "sf")) {
                if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (bowl_min - bowl)+ (cr_min - cr)) && bat <= bat_min && bowl <= bowl_min && cr <= cr_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                } //5-4-2
                else if ((maxTeamPlayer - totalSelected) <= ((cr_min - cr) + (bowl_min - bowl)+ (wk_min - wk)) && cr <= cr_min && bowl <= bowl_min && wk <= wk_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                } //3-4-2
                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (bowl_min - bowl)+ (wk_min - wk)) && bat <= bat_min && bowl <= bowl_min && wk <= wk_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                } //2-5-3
                else if ((maxTeamPlayer - totalSelected) <= ((cr_min - cr) + (bat_min - bat)+ (wk_min - wk)) && cr <= cr_min && bat <= bat_min && wk <= wk_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                }

                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) +(wk_min - wk)) && bat <= bat_min && wk <= wk_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    } else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl)+ (cr_min - cr)) && bowl <= bowl_min && cr <= cr_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl)+ (wk_min - wk)) && bowl <= bowl_min && wk <= wk_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat)+ (cr_min - cr)) && bat <= bat_min && cr <= cr_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((wk_min - wk)+ (cr_min - cr)) && wk <= wk_min && cr <= cr_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat)+ (bowl_min - bowl)) && bat <= bat_min && bowl <= bowl_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }
                }

                else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guards");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guards");
                }else if ((maxTeamPlayer - totalSelected) <= (cr_min - cr) && cr <= cr_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                } else if (ar == ar_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+ar_max+" Small Forward are allowed");
                } else {
                    ar += 1;
                    select_player_tab.getTabAt(2).setText("SF(" + ar + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "pf")) {
                if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (ar_min - ar)+ (cr_min - cr)) && bat <= bat_min && ar <= ar_min && cr <= cr_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                } //5-4-2
                else if ((maxTeamPlayer - totalSelected) <= ((cr_min - cr) + (ar_min - ar)+ (wk_min - wk)) && cr <= cr_min && ar <= ar_min && wk <= wk_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                } //3-4-2
                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (ar_min - ar)+ (wk_min - wk)) && bat <= bat_min && ar <= ar_min && wk <= wk_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                } //2-5-3
                else if ((maxTeamPlayer - totalSelected) <= ((cr_min - cr) + (bat_min - bat)+ (wk_min - wk)) && cr <= cr_min && bat <= bat_min && wk <= wk_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                }

                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) +(wk_min - wk)) && bat <= bat_min && wk <= wk_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    } else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar)+ (cr_min - cr)) && ar <= ar_min && cr <= cr_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar)+ (wk_min - wk)) && ar <= ar_min && wk <= wk_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat)+ (cr_min - cr)) && bat <= bat_min && cr <= cr_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((wk_min - wk)+ (cr_min - cr)) && wk <= wk_min && cr <= cr_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat)+ (ar_min - ar)) && bat <= bat_min && ar <= ar_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }
                }

                else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guards");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guards");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                }else if ((maxTeamPlayer - totalSelected) <= (cr_min - cr) && cr <= cr_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                } else if (bowl == bowl_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+bowl_max+" Forwards are allowed");
                } else {
                    bowl += 1;
                    select_player_tab.getTabAt(3).setText("PF(" + bowl + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "cr")) {
                if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (ar_min - ar)+ (bowl_min - bowl)) && bat <= bat_min && ar <= ar_min && bowl <= bowl_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }
                } //5-4-2
                else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (ar_min - ar)+ (wk_min - wk)) && bowl <= bowl_min && ar <= ar_min && wk <= wk_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                } //3-4-2
                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (ar_min - ar)+ (wk_min - wk)) && bat <= bat_min && ar <= ar_min && wk <= wk_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                } //2-5-3
                else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (bat_min - bat)+ (wk_min - wk)) && bowl <= bowl_min && bat <= bat_min && wk <= wk_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                }

                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) +(wk_min - wk)) && bat <= bat_min && wk <= wk_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    } else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar)+ (bowl_min - bowl)) && ar <= ar_min && bowl <= bowl_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least bowl_min Power Forward");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar)+ (wk_min - wk)) && ar <= ar_min && wk <= wk_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat)+ (bowl_min - bowl)) && bat <= bat_min && bowl <= bowl_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((wk_min - wk)+ (bowl_min - bowl)) && wk <= wk_min && bowl <= bowl_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat)+ (ar_min - ar)) && bat <= bat_min && ar <= ar_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guards");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guards");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                }else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                } else if (cr == cr_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+cr_max+" Center are allowed");
                } else {
                    cr += 1;
                    select_player_tab.getTabAt(4).setText("CR(" + cr + ")");
                    isSelected = true;
                }
            }

            if (isSelected) {
                if (CustomUtil.stringEqualsIgnore(matchModel.getTeam1(), playerModel.getTeamId())) {
                    team1_selected += 1;
                    team1_selected_dots += 1;
                    //show_team1_dots(team1_selected_dots);
                }
                if (CustomUtil.stringEqualsIgnore(matchModel.getTeam2(), playerModel.getTeamId())) {
                    team2_selected += 1;
                    team2_selected_dots += 1;
                    // show_team2_dots(team2_selected_dots);
                }
                show_team1_dots(team1_selected_dots+team2_selected_dots);
                total_remain -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //team2_selected_players.setText("" + team2_selected);
                selected_list.add(playerModel);
                playerModel.setSelected(true);
                ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
                checkSelect.put(playerModel.getPlayerId(), 1);
                totalSelected += 1;
                total_points -= CustomUtil.convertFloat(playerModel.getPlayerRank());
                credit_left.setText("" + total_points);
                //total_player_selected.setText(totalSelected+"/maxTeamPlayer");
                team1_cnt.setText(team1_selected_dots+"");
                team2_cnt.setText(team2_selected_dots+"");

                changeNextBg();

                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }
                return true;
            } else {
                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        }
    }

    public boolean validateHandballClick(PlayerModel playerModel) {
        if (selected_list == null) {
            selected_list = new ArrayList<>();
        }
        MatchModel matchModel=preferences.getMatchModel();
        if (checkSelect.get(playerModel.getPlayerId()) == 1) {

            int i = -1,j=0;
            for( PlayerModel playerModel1 : selected_list){
                if(playerModel1.getPlayerId().equals(playerModel.getPlayerId())){
                    i = j;
                }
                j++;
            }
            selected_list.remove(i);
            playerModel.setSelected(false);
            ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
            //selected_list.remove(playerModel);

            checkSelect.put(playerModel.getPlayerId(), 0);

            if (CustomUtil.stringEqualsIgnore(matchModel.getTeam1(), playerModel.getTeamId())) {
                team1_selected -= 1;
                team1_selected_dots -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //show_team1_dots(team1_selected_dots);
            }
            if (CustomUtil.stringEqualsIgnore(matchModel.getTeam2(), playerModel.getTeamId())) {
                team2_selected -= 1;
                team2_selected_dots -= 1;
                //team2_selected_players.setText("" + team2_selected);
                // show_team2_dots(team2_selected_dots);
            }
            show_team1_dots(team1_selected_dots+team2_selected_dots);
            total_remain += 1;
            totalSelected -= 1;
            total_points += CustomUtil.convertFloat(playerModel.getPlayerRank());
            credit_left.setText("" + total_points);
            //total_player_selected.setText(totalSelected+"/11");
            team1_cnt.setText(team1_selected_dots+"");
            team2_cnt.setText(team2_selected_dots+"");

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "gk")) {
                wk -= 1;
                if (wk >= 0) {
                    select_player_tab.getTabAt(0).setText("GK(" + wk + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "def")) {
                bat -= 1;
                if (bat >= 0) {
                    select_player_tab.getTabAt(1).setText("DEF(" + bat + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "fwd")) {
                ar -= 1;
                if (ar >= 0) {
                    select_player_tab.getTabAt(3).setText("FWD(" + ar + ")");
                }
            }

            changeNextBg();

            if (selected_list.size()>0){
                txtClear.setVisibility(View.VISIBLE);
            }else {
                txtClear.setVisibility(View.INVISIBLE);
            }
            return true;
        }
        else {
            boolean isSelected = false;
            if (totalSelected == maxTeamPlayer) {
                CustomUtil.showTopSneakError(mContext, "Maximum "+maxTeamPlayer+" players are allowed, click NEXT to proceed");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(matchModel.getTeam1(), playerModel.getTeamId()) &&
                    team1_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(matchModel.getTeam2(), playerModel.getTeamId()) &&
                    team2_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }

            if ((total_points - Float.parseFloat(playerModel.getPlayerRank())) < 0) {
                CustomUtil.showTopSneakError(mContext, "You don't have enough credit to pick this player");
                return false;
            }

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "gk")) {
                if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (bat_min - bat)) && ar <= ar_min && bat <= bat_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                    } else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Forward");
                    }
                }  else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Forward");
                } else if (wk == wk_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+wk_max+" Goal-keeper are allowed");
                } else {
                    wk += 1;
                    isSelected = true;
                    select_player_tab.getTabAt(0).setText("GK(" + wk + ")");
                    /// select_tab.getTabAt(0).setText("WK(" + wk + ")");
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "def")) {
                if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (wk_min - wk)) && ar <= ar_min && wk <= wk_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick only "+wk_min+" Goal-keeper");
                    } else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Forward");
                    }
                } /*else if ((maxTeamPlayer - totalSelected) <= (1 - bowl) && bowl <= 1) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least 1 Forward");
                }*/ else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Goal-keeper");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Forward");
                } else if (bat == bat_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+bat_max+" Defenders are allowed");
                } else {
                    bat += 1;
                    select_player_tab.getTabAt(1).setText("DEF(" + bat + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "fwd")) {
                if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (wk_min - wk)) && bat <= bat_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Goal-keeper");
                    } else if (bat_min >= (bat_min - bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Goal-keeper");
                } else if (ar == ar_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+ar_max+" Forward are allowed");
                } else {
                    ar += 1;
                    select_player_tab.getTabAt(2).setText("FWD(" + ar + ")");
                    isSelected = true;
                }
            }
            /*else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "for")) {
                if ((maxTeamPlayer - totalSelected) <= ((3 - bat) + (1 - wk)) && bat <= 3 && wk <= 1) {
                    if (1 <= (1 - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Only 1 Goal-keeper allowed");
                    } else if (3 >= (3 - bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least 3 Defenders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((3 - ar) + (1 - wk)) && ar <= 3 && wk <= 1) {
                    if (1 <= (1 - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Only 1 Goal-keeper allowed");
                    } else if (3 >= (3 - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least 3 Mid-Fielders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((3 - bat) + (3 - ar)) && bat <= 3 && ar <= 3) {
                    if (3 <= (3 - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least 3 Mid-Fielders");
                    } else if (1 >= (1 - bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least 3 Defenders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (3 - bat) && bat <= 3) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least 3 Defenders");
                } else if ((maxTeamPlayer - totalSelected) <= (1 - wk) && wk <= 1) {
                    CustomUtil.showTopSneakError(mContext, "Only 1 Goal-keeper allowed");
                } else if ((maxTeamPlayer - totalSelected) <= (3 - ar) && ar <= 3) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least 3 Mid-Fielders");
                } else if (bowl == 3) {
                    CustomUtil.showTopSneakError(mContext, "Max 3 Forwards are allowed");
                } else {
                    bowl += 1;
                    select_player_tab.getTabAt(3).setText("FOR(" + bowl + ")");
                    isSelected = true;
                }
            }*/

            if (isSelected) {
                if (CustomUtil.stringEqualsIgnore(matchModel.getTeam1(), playerModel.getTeamId())) {
                    team1_selected += 1;
                    team1_selected_dots += 1;
                    //show_team1_dots(team1_selected_dots);
                }
                if (CustomUtil.stringEqualsIgnore(matchModel.getTeam2(), playerModel.getTeamId())) {
                    team2_selected += 1;
                    team2_selected_dots += 1;
                    // show_team2_dots(team2_selected_dots);
                }
                show_team1_dots(team1_selected_dots+team2_selected_dots);
                total_remain -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //team2_selected_players.setText("" + team2_selected);
                selected_list.add(playerModel);
                playerModel.setSelected(true);
                ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
                checkSelect.put(playerModel.getPlayerId(), 1);
                totalSelected += 1;
                total_points -= CustomUtil.convertFloat(playerModel.getPlayerRank());
                credit_left.setText("" + total_points);
                //total_player_selected.setText(totalSelected+"/11");
                team1_cnt.setText(team1_selected_dots+"");
                team2_cnt.setText(team2_selected_dots+"");

                changeNextBg();

                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }
                return true;
            } else {
                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        }
    }

    public boolean validateKabaddiClick(PlayerModel playerModel) {
        if (selected_list == null) {
            selected_list = new ArrayList<>();
        }
        MatchModel matchModel=preferences.getMatchModel();
        if (checkSelect.get(playerModel.getPlayerId()) == 1) {
//-----------------------------------------------------
            int i = -1,j=0;
            for( PlayerModel playerModel1 : selected_list){
                if(playerModel1.getPlayerId().equals(playerModel.getPlayerId())){
                    i = j;
                }
                j++;
            }
            selected_list.remove(i);
            playerModel.setSelected(false);
            ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
            //selected_list.remove(playerModel);
//--------------------------------------------------------
            checkSelect.put(playerModel.getPlayerId(), 0);

            if (CustomUtil.stringEqualsIgnore(matchModel.getTeam1(), playerModel.getTeamId())) {
                team1_selected -= 1;
                team1_selected_dots -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //show_team1_dots(team1_selected_dots);
            }
            if (CustomUtil.stringEqualsIgnore(matchModel.getTeam2(), playerModel.getTeamId())) {
                team2_selected -= 1;
                team2_selected_dots -= 1;
                //team2_selected_players.setText("" + team2_selected);
                // show_team2_dots(team2_selected_dots);
            }
            show_team1_dots(team1_selected_dots+team2_selected_dots);
            total_remain += 1;
            totalSelected -= 1;
            total_points += CustomUtil.convertFloat(playerModel.getPlayerRank());
            credit_left.setText("" + total_points);
            //total_player_selected.setText(totalSelected+"/11");
            team1_cnt.setText(team1_selected_dots+"");
            team2_cnt.setText(team2_selected_dots+"");

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "def")) {
                wk -= 1;
                if (wk >= 0) {
                    select_player_tab.getTabAt(0).setText("DEF(" + wk + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "ar")) {
                bat -= 1;
                if (bat >= 0) {
                    select_player_tab.getTabAt(1).setText("AR(" + bat + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "raid")) {
                ar -= 1;
                if (ar >= 0) {
                    select_player_tab.getTabAt(2).setText("RAID(" + ar + ")");
                }
            }
            changeNextBg();
            if (selected_list.size()>0){
                txtClear.setVisibility(View.VISIBLE);
            }else {
                txtClear.setVisibility(View.INVISIBLE);
            }
            return true;
        }
        else {
            boolean isSelected = false;
            if (totalSelected == maxTeamPlayer) {
                CustomUtil.showTopSneakError(mContext, "Maximum "+maxTeamPlayer+" players are allowed, click NEXT to proceed");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(matchModel.getTeam1(), playerModel.getTeamId()) &&
                    team1_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(matchModel.getTeam2(), playerModel.getTeamId()) &&
                    team2_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }

            if ((total_points - Float.parseFloat(playerModel.getPlayerRank())) < 0) {
                CustomUtil.showTopSneakError(mContext, "You don't have enough credit to pick this player");
                return false;
            }

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "def")) {
                if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (bat_min - bat)) && ar <= ar_min && bat <= bat_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" All-Rounder");
                    } else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Raider");
                    }
                }  else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" All-Rounder");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Raider");
                } else if (wk == wk_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+wk_max+" Defenders are allowed");
                } else {
                    wk += 1;
                    isSelected = true;
                    select_player_tab.getTabAt(0).setText("DEF(" + wk + ")");
                    /// select_tab.getTabAt(0).setText("WK(" + wk + ")");
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "ar")) {
                if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (wk_min - wk)) && ar <= ar_min && wk <= wk_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Defenders");
                    } else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Raider");
                    }
                } /*else if ((maxTeamPlayer - totalSelected) <= (1 - bowl) && bowl <= 1) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least 1 Forward");
                }*/ else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Defenders");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Raider");
                } else if (bat == bat_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+bat_max+" All-Rounders are allowed");
                } else {
                    bat += 1;
                    select_player_tab.getTabAt(1).setText("AR(" + bat + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "raid")) {
                if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (wk_min - wk)) && bat <= bat_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Defenders");
                    } else if (bat_min >= (bat_min - bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" All-Rounder");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" All-Rounder");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Defenders");
                } else if (ar == ar_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+ar_max+" Raiders are allowed");
                } else {
                    ar += 1;
                    select_player_tab.getTabAt(2).setText("RAID(" + ar + ")");
                    isSelected = true;
                }
            }
            if (isSelected) {
                if (CustomUtil.stringEqualsIgnore(matchModel.getTeam1(), playerModel.getTeamId())) {
                    team1_selected += 1;
                    team1_selected_dots += 1;
                    //show_team1_dots(team1_selected_dots);
                }
                if (CustomUtil.stringEqualsIgnore(matchModel.getTeam2(), playerModel.getTeamId())) {
                    team2_selected += 1;
                    team2_selected_dots += 1;
                    // show_team2_dots(team2_selected_dots);
                }
                show_team1_dots(team1_selected_dots+team2_selected_dots);
                total_remain -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //team2_selected_players.setText("" + team2_selected);
                selected_list.add(playerModel);
                playerModel.setSelected(true);
                ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
                checkSelect.put(playerModel.getPlayerId(), 1);
                totalSelected += 1;
                total_points -= CustomUtil.convertFloat(playerModel.getPlayerRank());
                credit_left.setText("" + total_points);
                //total_player_selected.setText(totalSelected+"/11");
                team1_cnt.setText(team1_selected_dots+"");
                team2_cnt.setText(team2_selected_dots+"");

                changeNextBg();

                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }
                return true;
            } else {
                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        }
    }

    public boolean validateBaseballClick(PlayerModel playerModel) {
        if (selected_list == null) {
            selected_list = new ArrayList<>();
        }
        MatchModel matchModel=preferences.getMatchModel();
        if (checkSelect.get(playerModel.getPlayerId()) == 1) {
//-----------------------------------------------------
            int i = -1,j=0;
            for( PlayerModel playerModel1 : selected_list){
                if(playerModel1.getPlayerId().equals(playerModel.getPlayerId())){
                    i = j;
                }
                j++;
            }
            selected_list.remove(i);
            playerModel.setSelected(false);
            ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
            //selected_list.remove(playerModel);
//--------------------------------------------------------
            checkSelect.put(playerModel.getPlayerId(), 0);

            if (CustomUtil.stringEqualsIgnore(matchModel.getTeam1(), playerModel.getTeamId())) {
                team1_selected -= 1;
                team1_selected_dots -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //show_team1_dots(team1_selected_dots);
            }
            if (CustomUtil.stringEqualsIgnore(matchModel.getTeam2(), playerModel.getTeamId())) {
                team2_selected -= 1;
                team2_selected_dots -= 1;
                //team2_selected_players.setText("" + team2_selected);
                //show_team2_dots(team2_selected_dots);
            }
            show_team1_dots(team1_selected_dots+team2_selected_dots);
            total_remain += 1;
            totalSelected -= 1;
            total_points += CustomUtil.convertFloat(playerModel.getPlayerRank());
            credit_left.setText("" + total_points);
            //total_player_selected.setText(totalSelected+"/11");
            team1_cnt.setText(team1_selected_dots+"");
            team2_cnt.setText(team2_selected_dots+"");

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "of")) {
                wk -= 1;
                if (wk >= 0) {
                    select_player_tab.getTabAt(0).setText("OF(" + wk + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "if")) {
                bat -= 1;
                if (bat >= 0) {
                    select_player_tab.getTabAt(1).setText("IF(" + bat + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "cat")) {
                bowl -= 1;
                if (bowl >= 0) {
                    select_player_tab.getTabAt(3).setText("CAT(" + bowl + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "pit")) {
                ar -= 1;
                if (ar >= 0) {
                    select_player_tab.getTabAt(2).setText("PIT(" + ar + ")");
                }
            }

            changeNextBg();

            if (selected_list.size()>0){
                txtClear.setVisibility(View.VISIBLE);
            }else {
                txtClear.setVisibility(View.INVISIBLE);
            }
            return true;
        }
        else {
            boolean isSelected = false;
            if (totalSelected == maxTeamPlayer) {
                CustomUtil.showTopSneakError(mContext, "Maximum "+maxTeamPlayer+" players are allowed, click NEXT to proceed");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(matchModel.getTeam1(), playerModel.getTeamId()) &&
                    team1_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(matchModel.getTeam2(), playerModel.getTeamId()) &&
                    team2_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }

            if ((total_points - Float.parseFloat(playerModel.getPlayerRank())) < 0) {
                CustomUtil.showTopSneakError(mContext, "You don't have enough credit to pick this player");
                return false;
            }

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "of")) {
                if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (bat_min - bat)) && bowl <= bowl_min && bat <= bat_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Infielders");
                    } else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Catcher");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (bat_min - bat)) && ar <= ar_min && bat <= bat_min) {
                    if (bat_min > bat) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Infielders");
                    } else if (ar_min > ar) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Pitcher");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (ar_min - ar)) && bowl <= bowl_min && ar <= ar_min) {
                    if (ar_min > ar) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Pitcher");
                    } else if (bowl_min > bowl) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Catcher");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Infielders");
                } else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Catcher");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Pitcher");
                } else if (wk == wk_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+wk_max+" Outfielders allowed");
                } else {
                    wk += 1;
                    isSelected = true;
                    select_player_tab.getTabAt(0).setText("OF(" + wk + ")");
                    /// select_tab.getTabAt(0).setText("WK(" + wk + ")");
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "if")) {
                if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (wk_min - wk)) && bowl <= bowl_min && wk <= wk_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Outfielders");
                    } else if (bowl_min >= (bowl_min - bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Catcher");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (wk_min - wk)) && ar <= ar_min && wk <= wk_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Outfielders");
                    } else if (ar_min <= (ar_min - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Pitcher");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (ar_min - ar)) && bowl <= bowl_min && ar <= ar_min) {
                    if (ar_min <= (ar_min - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Pitcher");
                    } else if (bowl_min >= (bowl_min - bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Catcher");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Catcher");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Outfielders");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Pitcher");
                } else if (bat == bat_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+bat_max+" Infielders are allowed");
                } else {
                    bat += 1;
                    select_player_tab.getTabAt(1).setText("IF(" + bat + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "pit")) {
                if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (wk_min - wk)) && bat <= bat_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Outfielders");
                    } else if (bat_min >= (bat_min - bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Infielders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (wk_min - wk)) && bowl <= bowl_min && wk <= wk_min) {
                    if (wk_min > ( wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Outfielders");
                    } else if (bowl_min >= (bowl_min - bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Catcher");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (bat_min - bat)) && bowl <= bowl_min && bat <= bat_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Infielders");
                    } else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Catcher");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Catcher");
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Infielders");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Outfielders");
                } else if (ar == ar_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+ar_max+" Pitchers are allowed");
                } else {
                    ar += 1;
                    select_player_tab.getTabAt(2).setText("PIT(" + ar + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "cat")) {
                if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (wk_min - wk)) && bat <= bat_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Outfielders");
                    } else if (bat_min >= (bat_min - bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Infielders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (wk_min - wk)) && ar <= ar_min && wk <= wk_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Outfielders");
                    } else if (ar_min <= (ar_min - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Pitcher");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (ar_min - ar)) && bat <= bat_min && ar <= ar_min) {
                    if (ar_min <= (ar_min - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Pitcher");
                    } else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Infielders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Infielders");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Outfielders");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Pitcher");
                } else if (bowl == bowl_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+bowl_max+" Catcher are allowed");
                } else {
                    bowl += 1;
                    select_player_tab.getTabAt(3).setText("CAT(" + bowl + ")");
                    isSelected = true;
                }
            }

            if (isSelected) {
                if (CustomUtil.stringEqualsIgnore(matchModel.getTeam1(), playerModel.getTeamId())) {
                    team1_selected += 1;
                    team1_selected_dots += 1;
                    //show_team1_dots(team1_selected_dots);
                }
                if (CustomUtil.stringEqualsIgnore(matchModel.getTeam2(), playerModel.getTeamId())) {
                    team2_selected += 1;
                    team2_selected_dots += 1;
                    // show_team2_dots(team2_selected_dots);
                }
                show_team1_dots(team1_selected_dots+team2_selected_dots);
                total_remain -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //team2_selected_players.setText("" + team2_selected);
                selected_list.add(playerModel);
                playerModel.setSelected(true);
                ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
                checkSelect.put(playerModel.getPlayerId(), 1);
                totalSelected += 1;
                total_points -= CustomUtil.convertFloat(playerModel.getPlayerRank());
                credit_left.setText("" + total_points);
                // total_player_selected.setText(totalSelected+"/11");
                team1_cnt.setText(team1_selected_dots+"");
                team2_cnt.setText(team2_selected_dots+"");

                changeNextBg();

                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }

                return true;
            } else {
                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        }
    }

    /*public boolean validateFootballClick(PlayerModel playerModel) {
        if (selected_list == null) {
            selected_list = new ArrayList<>();
        }
        if (checkSelect.get(playerModel.getPlayerId()) == 1) {
//-----------------------------------------------------
            int i = -1,j=0;
            for( PlayerModel playerModel1 : selected_list){
                if(playerModel1.getPlayerId().equals(playerModel.getPlayerId())){
                    i = j;
                }
                j++;
            }
            selected_list.remove(i);
            playerModel.setSelected(false);
            ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
            //selected_list.remove(playerModel);
//--------------------------------------------------------
            checkSelect.put(playerModel.getPlayerId(), 0);

            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                team1_selected -= 1;
                team1_selected_dots -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //show_team1_dots(team1_selected_dots);
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                team2_selected -= 1;
                team2_selected_dots -= 1;
                //team2_selected_players.setText("" + team2_selected);
               // show_team2_dots(team2_selected_dots);
            }
            show_team1_dots(team1_selected_dots+team2_selected_dots);
            total_remain += 1;
            totalSelected -= 1;
            total_points += CustomUtil.convertFloat(playerModel.getPlayerRank());
            credit_left.setText("" + total_points);
            //total_player_selected.setText(totalSelected+"/11");
            team1_cnt.setText(team1_selected_dots+"");
            team2_cnt.setText(team2_selected_dots+"");

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "gk")) {
                wk -= 1;
                if (wk >= 0) {
                    select_player_tab.getTabAt(0).setText("GK(" + wk + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "def")) {
                bat -= 1;
                if (bat >= 0) {
                    select_player_tab.getTabAt(1).setText("DEF(" + bat + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "for")) {
                bowl -= 1;
                if (bowl >= 0) {
                    select_player_tab.getTabAt(3).setText("FOR(" + bowl + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "mid")) {
                ar -= 1;
                if (ar >= 0) {
                    select_player_tab.getTabAt(2).setText("MID(" + ar + ")");
                }
            }

            changeNextBg();

            if (selected_list.size()>0){
                txtClear.setVisibility(View.VISIBLE);
            }else {
                txtClear.setVisibility(View.INVISIBLE);
            }
            return true;
        }
        else {
            boolean isSelected = false;
            if (totalSelected == maxTeamPlayer) {
                CustomUtil.showTopSneakError(mContext, "Maximum "+maxTeamPlayer+" players are allowed, click NEXT to proceed");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId()) &&
                    team1_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId()) &&
                    team2_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }

            if ((total_points - Float.parseFloat(playerModel.getPlayerRank())) < 0) {
                CustomUtil.showTopSneakError(mContext, "You don't have enough credit to pick this player");
                return false;
            }

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "gk")) {
                if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (bat_min - bat)) && bowl <= bowl_min && bat <= bat_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                    } else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Forward");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (bat_min - bat)) && ar <= ar_min && bat <= bat_min) {
                    if (bat_min > bat) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                    } else if (ar_min > ar) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Mid-Fielders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (ar_min - ar)) && bowl <= bowl_min && ar <= ar_min) {
                    if (ar_min > ar) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Mid-Fielders");
                    } else if (bowl_min > bowl) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Forward");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                } else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Forward");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Mid-Fielders");
                } else if (wk == wk_max) {
                    CustomUtil.showTopSneakError(mContext, "Only "+wk_max+" Goal-keeper allowed");
                } else {
                    wk += 1;
                    isSelected = true;
                    select_player_tab.getTabAt(0).setText("GK(" + wk + ")");
                    /// select_tab.getTabAt(0).setText("WK(" + wk + ")");
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "def")) {
                if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (wk_min - wk)) && bowl <= bowl_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Only "+wk_min+" Goal-keeper allowed");
                    } else if (bowl_min >= (bowl_min - bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Forward");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (wk_min - wk)) && ar <= ar_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Only "+wk_min+" Goal-keeper allowed");
                    } else if (ar_min >= (ar_min - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Mid-Fielders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (ar_min - ar)) && bowl <= bowl_min && ar <= ar_min) {
                    if (ar_min <= (ar_min - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Mid-Fielders");
                    } else if (bowl_min >= (bowl_min - bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Forward");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Forward");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Only "+wk_min+" Goal-keeper allowed");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Mid-Fielders");
                } else if (bat == bat_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+bat_max+" Defenders are allowed");
                } else {
                    bat += 1;
                    select_player_tab.getTabAt(1).setText("DEF(" + bat + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "mid")) {
                if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (wk_min - wk)) && bat <= bat_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Only "+wk_min+" Goal-keeper allowed");
                    } else if (bat_min >= (bat_min - bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (wk_min - wk)) && bowl <= bowl_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Only "+wk_min+" Goal-keeper allowed");
                    } else if (bowl_min >= (bowl_min - bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Forward");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (bat_min - bat)) && bowl <= bowl_min && bat <= bat_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                    } else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Forward");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Forward");
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Only "+wk_min+" Goal-keeper allowed");
                } else if (ar == ar_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+ar_max+" Mid-Fielders are allowed");
                } else {
                    ar += 1;
                    select_player_tab.getTabAt(2).setText("MID(" + ar + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "for")) {
                if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (wk_min - wk)) && bat <= bat_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Only "+wk_min+" Goal-keeper allowed");
                    } else if (bat_min >= (bat_min - bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (wk_min - wk)) && ar <= ar_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Only "+wk_min+" Goal-keeper allowed");
                    } else if (ar_min >= (ar_min - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Mid-Fielders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (ar_min - ar)) && bat <= bat_min && ar <= ar_min) {
                    if (ar_min <= (ar_min - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Mid-Fielders");
                    } else if (bat_min >= (bat_min - bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Only "+wk_min+" Goal-keeper allowed");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Mid-Fielders");
                } else if (bowl == bowl_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+bowl_max+" Forwards are allowed");
                } else {
                    bowl += 1;
                    select_player_tab.getTabAt(3).setText("FOR(" + bowl + ")");
                    isSelected = true;
                }
            }

            if (isSelected) {
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                    team1_selected += 1;
                    team1_selected_dots += 1;
                    //show_team1_dots(team1_selected_dots);
                }
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                    team2_selected += 1;
                    team2_selected_dots += 1;
                   // show_team2_dots(team2_selected_dots);
                }
                show_team1_dots(team1_selected_dots+team2_selected_dots);
                total_remain -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //team2_selected_players.setText("" + team2_selected);
                selected_list.add(playerModel);
                playerModel.setSelected(true);
                ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
                checkSelect.put(playerModel.getPlayerId(), 1);
                totalSelected += 1;
                total_points -= CustomUtil.convertFloat(playerModel.getPlayerRank());
                credit_left.setText("" + total_points);
                //total_player_selected.setText(totalSelected+"/11");
                team1_cnt.setText(team1_selected_dots+"");
                team2_cnt.setText(team2_selected_dots+"");

                changeNextBg();

                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }
                return true;
            } else {
                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        }
    }

    public boolean validateBasketballClick(PlayerModel playerModel) {
        if (selected_list == null) {
            selected_list = new ArrayList<>();
        }
        if (checkSelect.get(playerModel.getPlayerId()) == 1) {
//-----------------------------------------------------
            int i = -1,j=0;
            for( PlayerModel playerModel1 : selected_list){
                if(playerModel1.getPlayerId().equals(playerModel.getPlayerId())){
                    i = j;
                }
                j++;
            }
            selected_list.remove(i);
            playerModel.setSelected(false);
            ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
            //selected_list.remove(playerModel);
//--------------------------------------------------------
            checkSelect.put(playerModel.getPlayerId(), 0);

            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                team1_selected -= 1;
                team1_selected_dots -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //show_team1_dots(team1_selected_dots);
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                team2_selected -= 1;
                team2_selected_dots -= 1;
                //team2_selected_players.setText("" + team2_selected);
               // show_team2_dots(team2_selected_dots);
            }
            show_team1_dots(team1_selected_dots+team2_selected_dots);
            total_remain += 1;
            totalSelected -= 1;
            total_points += CustomUtil.convertFloat(playerModel.getPlayerRank());
            credit_left.setText("" + total_points);
            //total_player_selected.setText(totalSelected+"/11");
            team1_cnt.setText(team1_selected_dots+"");
            team2_cnt.setText(team2_selected_dots+"");

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "pg")) {
                wk -= 1;
                if (wk >= 0) {
                    select_player_tab.getTabAt(0).setText("PG(" + wk + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "sg")) {
                bat -= 1;
                if (bat >= 0) {
                    select_player_tab.getTabAt(1).setText("SG(" + bat + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "sf")) {
                ar -= 1;
                if (ar >= 0) {
                    select_player_tab.getTabAt(2).setText("SF(" + ar + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "pf")) {
                bowl -= 1;
                if (bowl >= 0) {
                    select_player_tab.getTabAt(3).setText("PF(" + bowl + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "cr")) {
                cr -= 1;
                if (cr >= 0) {
                    select_player_tab.getTabAt(4).setText("CR(" + cr + ")");
                }
            }

            changeNextBg();

            if (selected_list.size()>0){
                txtClear.setVisibility(View.VISIBLE);
            }else {
                txtClear.setVisibility(View.INVISIBLE);
            }

            return true;
        }
        else {
            boolean isSelected = false;
            if (totalSelected == maxTeamPlayer) {
                CustomUtil.showTopSneakError(mContext, "Maximum "+maxTeamPlayer+" players are allowed, click NEXT to proceed");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId()) &&
                    team1_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId()) &&
                    team2_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }

            if ((total_points - Float.parseFloat(playerModel.getPlayerRank())) < 0) {
                CustomUtil.showTopSneakError(mContext, "You don't have enough credit to pick this player");
                return false;
            }

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "pg")) {
                //5-4-3
                if ((maxTeamPlayer - totalSelected) <= ((cr_min - cr) + (bowl_min - bowl)+ (bat_min - bat)) && cr <= cr_min &&
                        bowl <= bowl_min && bat <= bat_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }
                    else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                    else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (bowl_min - bowl)+ (cr_min - cr)) && ar <= ar_min && bowl <= bowl_min && cr <= cr_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (bowl_min - bowl)+ (bat_min - bat)) && ar <= ar_min && bowl <= bowl_min && bat <= bat_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }
                } //2-5-3
                else if ((maxTeamPlayer - totalSelected) <= ((cr_min - cr) + (ar_min - ar)+ (bat_min - bat)) && cr <= cr_min && ar <= ar_min && bat <= bat_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }
                }

                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) +(bat_min - bat)) && ar <= ar_min && bat <= bat_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    } else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl)+ (cr_min - cr)) && bowl <= bowl_min && cr <= cr_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl)+ (bat_min - bat)) && bowl <= bowl_min && bat <= bat_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar)+ (cr_min - cr)) && ar <= ar_min && cr <= cr_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat)+ (cr_min - cr)) && bat <= bat_min && cr <= cr_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar)+ (bowl_min - bowl)) && ar <= ar_min && bowl <= bowl_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }
                }

                else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guards");
                } else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                }else if ((maxTeamPlayer - totalSelected) <= (cr_min - cr) && cr <= cr_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                } else if (wk == wk_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+wk_max+" Point Guards allowed");
                } else {
                    wk += 1;
                    isSelected = true;
                    select_player_tab.getTabAt(0).setText("PG(" + wk + ")");
                    /// select_tab.getTabAt(0).setText("WK(" + wk + ")");
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "sg")) {

                if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (bowl_min - bowl)+ (cr_min - cr)) && ar <= ar_min && bowl <= bowl_min && cr <= cr_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                } //5-4-2
                else if ((maxTeamPlayer - totalSelected) <= ((cr_min - cr) + (bowl_min - bowl)+ (wk_min - wk)) && cr <= cr_min && bowl <= bowl_min && wk <= wk_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                } //3-4-2
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (bowl_min - bowl)+ (wk_min - wk)) && ar <= ar_min && bowl <= bowl_min && wk <= wk_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                } //2-5-3
                else if ((maxTeamPlayer - totalSelected) <= ((cr_min - cr) + (ar_min - ar)+ (wk_min - wk)) && cr <= cr_min && ar <= ar_min && wk <= wk_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                }

                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) +(wk_min - wk)) && ar <= ar_min && wk <= wk_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    } else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl)+ (cr_min - cr)) && bowl <= bowl_min && cr <= cr_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl)+ (wk_min - wk)) && bowl <= bowl_min && wk <= wk_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar)+ (cr_min - cr)) && ar <= ar_min && cr <= cr_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((wk_min - wk)+ (cr_min - cr)) && wk <= wk_min && cr <= cr_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar)+ (bowl_min - bowl)) && ar <= ar_min && bowl <= bowl_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }
                }

                else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guards allowed");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                } else if ((maxTeamPlayer - totalSelected) <= (cr_min - cr) && cr <= cr_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                } else if (bat == bat_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+bat_max+" Shooting Guards are allowed");
                } else {
                    bat += 1;
                    select_player_tab.getTabAt(1).setText("SG(" + bat + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "sf")) {
                if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (bowl_min - bowl)+ (cr_min - cr)) && bat <= bat_min && bowl <= bowl_min && cr <= cr_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                } //5-4-2
                else if ((maxTeamPlayer - totalSelected) <= ((cr_min - cr) + (bowl_min - bowl)+ (wk_min - wk)) && cr <= cr_min && bowl <= bowl_min && wk <= wk_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                } //3-4-2
                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (bowl_min - bowl)+ (wk_min - wk)) && bat <= bat_min && bowl <= bowl_min && wk <= wk_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                } //2-5-3
                else if ((maxTeamPlayer - totalSelected) <= ((cr_min - cr) + (bat_min - bat)+ (wk_min - wk)) && cr <= cr_min && bat <= bat_min && wk <= wk_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                }

                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) +(wk_min - wk)) && bat <= bat_min && wk <= wk_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    } else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl)+ (cr_min - cr)) && bowl <= bowl_min && cr <= cr_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl)+ (wk_min - wk)) && bowl <= bowl_min && wk <= wk_min) {
                    if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat)+ (cr_min - cr)) && bat <= bat_min && cr <= cr_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((wk_min - wk)+ (cr_min - cr)) && wk <= wk_min && cr <= cr_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat)+ (bowl_min - bowl)) && bat <= bat_min && bowl <= bowl_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }
                }

                else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guards");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Only "+wk_min+" Point Guards allowed");
                }else if ((maxTeamPlayer - totalSelected) <= (cr_min - cr) && cr <= cr_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                } else if (ar == ar_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+ar_max+" Small Forward are allowed");
                } else {
                    ar += 1;
                    select_player_tab.getTabAt(2).setText("SF(" + ar + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "pf")) {
                if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (ar_min - ar)+ (cr_min - cr)) && bat <= bat_min && ar <= ar_min && cr <= cr_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                } //5-4-2
                else if ((maxTeamPlayer - totalSelected) <= ((cr_min - cr) + (ar_min - ar)+ (wk_min - wk)) && cr <= cr_min && ar <= ar_min && wk <= wk_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                } //3-4-2
                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (ar_min - ar)+ (wk_min - wk)) && bat <= bat_min && ar <= ar_min && wk <= wk_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                } //2-5-3
                else if ((maxTeamPlayer - totalSelected) <= ((cr_min - cr) + (bat_min - bat)+ (wk_min - wk)) && cr <= cr_min && bat <= bat_min && wk <= wk_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                }

                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) +(wk_min - wk)) && bat <= bat_min && wk <= wk_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    } else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar)+ (cr_min - cr)) && ar <= ar_min && cr <= cr_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar)+ (wk_min - wk)) && ar <= ar_min && wk <= wk_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat)+ (cr_min - cr)) && bat <= bat_min && cr <= cr_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((wk_min - wk)+ (cr_min - cr)) && wk <= wk_min && cr <= cr_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }else if (cr_min > (cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat)+ (ar_min - ar)) && bat <= bat_min && ar <= ar_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }
                }

                else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guards");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Only "+wk_min+" Point Guards allowed");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                }else if ((maxTeamPlayer - totalSelected) <= (cr_min - cr) && cr <= cr_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+cr_min+" Center");
                } else if (bowl == bowl_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+bowl_max+" Forwards are allowed");
                } else {
                    bowl += 1;
                    select_player_tab.getTabAt(3).setText("PF(" + bowl + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "cr")) {
                if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (ar_min - ar)+ (bowl_min - bowl)) && bat <= bat_min && ar <= ar_min && bowl <= bowl_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }
                } //5-4-2
                else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (ar_min - ar)+ (wk_min - wk)) && bowl <= bowl_min && ar <= ar_min && wk <= wk_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                } //3-4-2
                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (ar_min - ar)+ (wk_min - wk)) && bat <= bat_min && ar <= ar_min && wk <= wk_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                } //2-5-3
                else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (bat_min - bat)+ (wk_min - wk)) && bowl <= bowl_min && bat <= bat_min && wk <= wk_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                }

                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) +(wk_min - wk)) && bat <= bat_min && wk <= wk_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    } else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar)+ (bowl_min - bowl)) && ar <= ar_min && bowl <= bowl_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar)+ (wk_min - wk)) && ar <= ar_min && wk <= wk_min) {
                    if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }else if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat)+ (bowl_min - bowl)) && bat <= bat_min && bowl <= bowl_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((wk_min - wk)+ (bowl_min - bowl)) && wk <= wk_min && bowl <= bowl_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Point Guard");
                    }else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat)+ (ar_min - ar)) && bat <= bat_min && ar <= ar_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guard");
                    }else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                    }
                }
                else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Shooting Guards");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Only "+wk_min+" Point Guards allowed");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Small Forward");
                }else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Power Forward");
                } else if (cr == cr_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+cr_max+" Center are allowed");
                } else {
                    cr += 1;
                    select_player_tab.getTabAt(4).setText("CR(" + cr + ")");
                    isSelected = true;
                }
            }

            if (isSelected) {
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                    team1_selected += 1;
                    team1_selected_dots += 1;
                    //show_team1_dots(team1_selected_dots);
                }
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                    team2_selected += 1;
                    team2_selected_dots += 1;
                   // show_team2_dots(team2_selected_dots);
                }
                show_team1_dots(team1_selected_dots+team2_selected_dots);
                total_remain -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //team2_selected_players.setText("" + team2_selected);
                selected_list.add(playerModel);
                playerModel.setSelected(true);
                ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
                checkSelect.put(playerModel.getPlayerId(), 1);
                totalSelected += 1;
                total_points -= CustomUtil.convertFloat(playerModel.getPlayerRank());
                credit_left.setText("" + total_points);
                //total_player_selected.setText(totalSelected+"/maxTeamPlayer");
                team1_cnt.setText(team1_selected_dots+"");
                team2_cnt.setText(team2_selected_dots+"");

                changeNextBg();

                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }
                return true;
            } else {
                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        }
    }

    public boolean validateHandballClick(PlayerModel playerModel) {
        if (selected_list == null) {
            selected_list = new ArrayList<>();
        }
        if (checkSelect.get(playerModel.getPlayerId()) == 1) {
//-----------------------------------------------------
            int i = -1,j=0;
            for( PlayerModel playerModel1 : selected_list){
                if(playerModel1.getPlayerId().equals(playerModel.getPlayerId())){
                    i = j;
                }
                j++;
            }
            selected_list.remove(i);
            playerModel.setSelected(false);
            ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
            //selected_list.remove(playerModel);
//--------------------------------------------------------
            checkSelect.put(playerModel.getPlayerId(), 0);

            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                team1_selected -= 1;
                team1_selected_dots -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //show_team1_dots(team1_selected_dots);
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                team2_selected -= 1;
                team2_selected_dots -= 1;
                //team2_selected_players.setText("" + team2_selected);
                // show_team2_dots(team2_selected_dots);
            }
            show_team1_dots(team1_selected_dots+team2_selected_dots);
            total_remain += 1;
            totalSelected -= 1;
            total_points += CustomUtil.convertFloat(playerModel.getPlayerRank());
            credit_left.setText("" + total_points);
            //total_player_selected.setText(totalSelected+"/11");
            team1_cnt.setText(team1_selected_dots+"");
            team2_cnt.setText(team2_selected_dots+"");

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "gk")) {
                wk -= 1;
                if (wk >= 0) {
                    select_player_tab.getTabAt(0).setText("GK(" + wk + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "def")) {
                bat -= 1;
                if (bat >= 0) {
                    select_player_tab.getTabAt(1).setText("DEF(" + bat + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "fwd")) {
                ar -= 1;
                if (ar >= 0) {
                    select_player_tab.getTabAt(3).setText("FWD(" + ar + ")");
                }
            }

            changeNextBg();

            if (selected_list.size()>0){
                txtClear.setVisibility(View.VISIBLE);
            }else {
                txtClear.setVisibility(View.INVISIBLE);
            }
            return true;
        }
        else {
            boolean isSelected = false;
            if (totalSelected == maxTeamPlayer) {
                CustomUtil.showTopSneakError(mContext, "Maximum "+maxTeamPlayer+" players are allowed, click NEXT to proceed");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId()) &&
                    team1_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId()) &&
                    team2_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }

            if ((total_points - Float.parseFloat(playerModel.getPlayerRank())) < 0) {
                CustomUtil.showTopSneakError(mContext, "You don't have enough credit to pick this player");
                return false;
            }

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "gk")) {
                if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (bat_min - bat)) && ar <= ar_min && bat <= bat_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                    } else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Forward");
                    }
                }  else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Forward");
                } else if (wk == wk_max) {
                    CustomUtil.showTopSneakError(mContext, "Only "+wk_max+" Goal-keeper allowed");
                } else {
                    wk += 1;
                    isSelected = true;
                    select_player_tab.getTabAt(0).setText("GK(" + wk + ")");
                    /// select_tab.getTabAt(0).setText("WK(" + wk + ")");
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "def")) {
                if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (wk_min - wk)) && ar <= ar_min && wk <= wk_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick only "+wk_min+" Goal-keeper");
                    } else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Forward");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Only "+wk_min+" Goal-keeper allowed");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Forward");
                } else if (bat == bat_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+bat_max+" Defenders are allowed");
                } else {
                    bat += 1;
                    select_player_tab.getTabAt(1).setText("DEF(" + bat + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "fwd")) {
                if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (wk_min - wk)) && bat <= bat_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Only "+wk_min+" Goal-keeper allowed");
                    } else if (bat_min >= (bat_min - bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Defenders");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Only "+wk_min+" Goal-keeper allowed");
                } else if (ar == ar_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+ar_max+" Forward are allowed");
                } else {
                    ar += 1;
                    select_player_tab.getTabAt(2).setText("FWD(" + ar + ")");
                    isSelected = true;
                }
            }

            if (isSelected) {
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                    team1_selected += 1;
                    team1_selected_dots += 1;
                    //show_team1_dots(team1_selected_dots);
                }
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                    team2_selected += 1;
                    team2_selected_dots += 1;
                    // show_team2_dots(team2_selected_dots);
                }
                show_team1_dots(team1_selected_dots+team2_selected_dots);
                total_remain -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //team2_selected_players.setText("" + team2_selected);
                selected_list.add(playerModel);
                playerModel.setSelected(true);
                ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
                checkSelect.put(playerModel.getPlayerId(), 1);
                totalSelected += 1;
                total_points -= CustomUtil.convertFloat(playerModel.getPlayerRank());
                credit_left.setText("" + total_points);
                //total_player_selected.setText(totalSelected+"/11");
                team1_cnt.setText(team1_selected_dots+"");
                team2_cnt.setText(team2_selected_dots+"");

                changeNextBg();

                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }
                return true;
            } else {
                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        }
    }

    public boolean validateKabaddiClick(PlayerModel playerModel) {
        if (selected_list == null) {
            selected_list = new ArrayList<>();
        }
        if (checkSelect.get(playerModel.getPlayerId()) == 1) {
//-----------------------------------------------------
            int i = -1,j=0;
            for( PlayerModel playerModel1 : selected_list){
                if(playerModel1.getPlayerId().equals(playerModel.getPlayerId())){
                    i = j;
                }
                j++;
            }
            selected_list.remove(i);
            playerModel.setSelected(false);
            ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
            //selected_list.remove(playerModel);
//--------------------------------------------------------
            checkSelect.put(playerModel.getPlayerId(), 0);

            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                team1_selected -= 1;
                team1_selected_dots -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //show_team1_dots(team1_selected_dots);
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                team2_selected -= 1;
                team2_selected_dots -= 1;
                //team2_selected_players.setText("" + team2_selected);
                // show_team2_dots(team2_selected_dots);
            }
            show_team1_dots(team1_selected_dots+team2_selected_dots);
            total_remain += 1;
            totalSelected -= 1;
            total_points += CustomUtil.convertFloat(playerModel.getPlayerRank());
            credit_left.setText("" + total_points);
            //total_player_selected.setText(totalSelected+"/11");
            team1_cnt.setText(team1_selected_dots+"");
            team2_cnt.setText(team2_selected_dots+"");

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "def")) {
                wk -= 1;
                if (wk >= 0) {
                    select_player_tab.getTabAt(0).setText("DEF(" + wk + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "ar")) {
                bat -= 1;
                if (bat >= 0) {
                    select_player_tab.getTabAt(1).setText("AR(" + bat + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "raid")) {
                ar -= 1;
                if (ar >= 0) {
                    select_player_tab.getTabAt(2).setText("RAID(" + ar + ")");
                }
            }
            changeNextBg();
            if (selected_list.size()>0){
                txtClear.setVisibility(View.VISIBLE);
            }else {
                txtClear.setVisibility(View.INVISIBLE);
            }
            return true;
        }
        else {
            boolean isSelected = false;
            if (totalSelected == maxTeamPlayer) {
                CustomUtil.showTopSneakError(mContext, "Maximum "+maxTeamPlayer+" players are allowed, click NEXT to proceed");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId()) &&
                    team1_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId()) &&
                    team2_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }

            if ((total_points - Float.parseFloat(playerModel.getPlayerRank())) < 0) {
                CustomUtil.showTopSneakError(mContext, "You don't have enough credit to pick this player");
                return false;
            }

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "def")) {
                if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (bat_min - bat)) && ar <= ar_min && bat <= bat_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" All-Rounder");
                    } else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Raider");
                    }
                }  else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" All-Rounder");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Raider");
                } else if (wk == wk_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+wk_max+" Defenders are allowed");
                } else {
                    wk += 1;
                    isSelected = true;
                    select_player_tab.getTabAt(0).setText("DEF(" + wk + ")");
                    /// select_tab.getTabAt(0).setText("WK(" + wk + ")");
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "ar")) {
                if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (wk_min - wk)) && ar <= ar_min && wk <= wk_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Defenders");
                    } else if (ar_min > (ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Raider");
                    }
                }  else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Defenders");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Raider");
                } else if (bat == bat_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+bat_max+" All-Rounders are allowed");
                } else {
                    bat += 1;
                    select_player_tab.getTabAt(1).setText("AR(" + bat + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "raid")) {
                if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (wk_min - wk)) && bat <= bat_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Defenders");
                    } else if (bat_min >= (bat_min - bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" All-Rounder");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Defenders");
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" All-Rounder");
                } else if (ar == ar_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+ar_max+" Raiders are allowed");
                } else {
                    ar += 1;
                    select_player_tab.getTabAt(2).setText("RAID(" + ar + ")");
                    isSelected = true;
                }
            }
            if (isSelected) {
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                    team1_selected += 1;
                    team1_selected_dots += 1;
                    //show_team1_dots(team1_selected_dots);
                }
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                    team2_selected += 1;
                    team2_selected_dots += 1;
                    // show_team2_dots(team2_selected_dots);
                }
                show_team1_dots(team1_selected_dots+team2_selected_dots);
                total_remain -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //team2_selected_players.setText("" + team2_selected);
                selected_list.add(playerModel);
                playerModel.setSelected(true);
                ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
                checkSelect.put(playerModel.getPlayerId(), 1);
                totalSelected += 1;
                total_points -= CustomUtil.convertFloat(playerModel.getPlayerRank());
                credit_left.setText("" + total_points);
                //total_player_selected.setText(totalSelected+"/11");
                team1_cnt.setText(team1_selected_dots+"");
                team2_cnt.setText(team2_selected_dots+"");

                changeNextBg();

                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }
                return true;
            } else {
                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        }
    }

    public boolean validateBaseballClick(PlayerModel playerModel) {
        if (selected_list == null) {
            selected_list = new ArrayList<>();
        }
        if (checkSelect.get(playerModel.getPlayerId()) == 1) {
//-----------------------------------------------------
            int i = -1,j=0;
            for( PlayerModel playerModel1 : selected_list){
                if(playerModel1.getPlayerId().equals(playerModel.getPlayerId())){
                    i = j;
                }
                j++;
            }
            selected_list.remove(i);
            playerModel.setSelected(false);
            ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
            //selected_list.remove(playerModel);
//--------------------------------------------------------
            checkSelect.put(playerModel.getPlayerId(), 0);

            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                team1_selected -= 1;
                team1_selected_dots -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //show_team1_dots(team1_selected_dots);
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                team2_selected -= 1;
                team2_selected_dots -= 1;
                //team2_selected_players.setText("" + team2_selected);
                //show_team2_dots(team2_selected_dots);
            }
            show_team1_dots(team1_selected_dots+team2_selected_dots);
            total_remain += 1;
            totalSelected -= 1;
            total_points += CustomUtil.convertFloat(playerModel.getPlayerRank());
            credit_left.setText("" + total_points);
            //total_player_selected.setText(totalSelected+"/11");
            team1_cnt.setText(team1_selected_dots+"");
            team2_cnt.setText(team2_selected_dots+"");

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "of")) {
                wk -= 1;
                if (wk >= 0) {
                    select_player_tab.getTabAt(0).setText("OF(" + wk + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "if")) {
                bat -= 1;
                if (bat >= 0) {
                    select_player_tab.getTabAt(1).setText("IF(" + bat + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "cat")) {
                bowl -= 1;
                if (bowl >= 0) {
                    select_player_tab.getTabAt(3).setText("CAT(" + bowl + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "pit")) {
                ar -= 1;
                if (ar >= 0) {
                    select_player_tab.getTabAt(2).setText("PIT(" + ar + ")");
                }
            }

            changeNextBg();

            if (selected_list.size()>0){
                txtClear.setVisibility(View.VISIBLE);
            }else {
                txtClear.setVisibility(View.INVISIBLE);
            }
            return true;
        }
        else {
            boolean isSelected = false;
            if (totalSelected == maxTeamPlayer) {
                CustomUtil.showTopSneakError(mContext, "Maximum "+maxTeamPlayer+" players are allowed, click NEXT to proceed");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId()) &&
                    team1_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId()) &&
                    team2_selected >= eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+eachTeamMax+" players are allowed from a team");
                return false;
            }

            if ((total_points - Float.parseFloat(playerModel.getPlayerRank())) < 0) {
                CustomUtil.showTopSneakError(mContext, "You don't have enough credit to pick this player");
                return false;
            }

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "of")) {
                if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (bat_min - bat)) && bowl <= bowl_min && bat <= bat_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Infielders");
                    } else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Catcher");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (bat_min - bat)) && ar <= ar_min && bat <= bat_min) {
                    if (bat_min > bat) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Infielders");
                    } else if (ar_min > ar) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Pitcher");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (ar_min - ar)) && bowl <= bowl_min && ar <= ar_min) {
                    if (ar_min > ar) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Pitcher");
                    } else if (bowl_min > bowl) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Catcher");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Infielders");
                } else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Catcher");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Pitcher");
                } else if (wk == wk_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+wk_max+" Outfielders allowed");
                } else {
                    wk += 1;
                    isSelected = true;
                    select_player_tab.getTabAt(0).setText("OF(" + wk + ")");
                    /// select_tab.getTabAt(0).setText("WK(" + wk + ")");
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "if")) {
                if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (wk_min - wk)) && bowl <= bowl_min && wk <= wk_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Outfielders");
                    } else if (bowl_min >= (bowl_min - bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Catcher");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (wk_min - wk)) && ar <= ar_min && wk <= wk_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Outfielders");
                    } else if (ar_min <= (ar_min - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Pitcher");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (ar_min - ar)) && bowl <= bowl_min && ar <= ar_min) {
                    if (ar_min <= (ar_min - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Pitcher");
                    } else if (bowl_min >= (bowl_min - bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Catcher");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Catcher");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Outfielders");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Pitcher");
                } else if (bat == bat_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+bat_max+" Infielders are allowed");
                } else {
                    bat += 1;
                    select_player_tab.getTabAt(1).setText("IF(" + bat + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "pit")) {
                if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (wk_min - wk)) && bat <= bat_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Outfielders");
                    } else if (bat_min >= (bat_min - bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Infielders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (wk_min - wk)) && bowl <= bowl_min && wk <= wk_min) {
                    if (wk_min > ( wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Outfielders");
                    } else if (bowl_min >= (bowl_min - bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Catcher");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bowl_min - bowl) + (bat_min - bat)) && bowl <= bowl_min && bat <= bat_min) {
                    if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Infielders");
                    } else if (bowl_min > (bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Catcher");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bowl_min - bowl) && bowl <= bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bowl_min+" Catcher");
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Infielders");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Outfielders");
                } else if (ar == ar_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+ar_max+" Pitchers are allowed");
                } else {
                    ar += 1;
                    select_player_tab.getTabAt(2).setText("PIT(" + ar + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "cat")) {
                if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (wk_min - wk)) && bat <= bat_min && wk <= wk_min) {
                    if (wk_min <= (wk_min - wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Outfielders");
                    } else if (bat_min >= (bat_min - bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Infielders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((ar_min - ar) + (wk_min - wk)) && ar <= ar_min && wk <= wk_min) {
                    if (wk_min > (wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Outfielders");
                    } else if (ar_min <= (ar_min - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Pitcher");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= ((bat_min - bat) + (ar_min - ar)) && bat <= bat_min && ar <= ar_min) {
                    if (ar_min <= (ar_min - ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Pitcher");
                    } else if (bat_min > (bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Infielders");
                    }
                } else if ((maxTeamPlayer - totalSelected) <= (bat_min - bat) && bat <= bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+bat_min+" Infielders");
                } else if ((maxTeamPlayer - totalSelected) <= (wk_min - wk) && wk <= wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+wk_min+" Outfielders");
                } else if ((maxTeamPlayer - totalSelected) <= (ar_min - ar) && ar <= ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+ar_min+" Pitcher");
                } else if (bowl == bowl_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+bowl_max+" Catcher are allowed");
                } else {
                    bowl += 1;
                    select_player_tab.getTabAt(3).setText("CAT(" + bowl + ")");
                    isSelected = true;
                }
            }

            if (isSelected) {
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                    team1_selected += 1;
                    team1_selected_dots += 1;
                    //show_team1_dots(team1_selected_dots);
                }
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                    team2_selected += 1;
                    team2_selected_dots += 1;
                    // show_team2_dots(team2_selected_dots);
                }
                show_team1_dots(team1_selected_dots+team2_selected_dots);
                total_remain -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //team2_selected_players.setText("" + team2_selected);
                selected_list.add(playerModel);
                playerModel.setSelected(true);
                ((CricketSelectPlayerActivity)mContext).updateSelectedList(selected_list);
                checkSelect.put(playerModel.getPlayerId(), 1);
                totalSelected += 1;
                total_points -= CustomUtil.convertFloat(playerModel.getPlayerRank());
                credit_left.setText("" + total_points);
                // total_player_selected.setText(totalSelected+"/11");
                team1_cnt.setText(team1_selected_dots+"");
                team2_cnt.setText(team2_selected_dots+"");

                changeNextBg();

                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }

                return true;
            } else {
                if (selected_list.size()>0){
                    txtClear.setVisibility(View.VISIBLE);
                }else {
                    txtClear.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        }
    }*/

    public class ViewPagerAdapter extends FragmentStateAdapter {

        Fragment fragment;

        public ViewPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
            this.fragment = fragment;
        }


        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (selected_match.getSportId().equals("4")) {
                if (position == 0) {
                    return wKplayerFragment =  WKplayerFragment.newInstance(fragment,sportId);
                } else if (position == 1) {
                    return batPlayerFragment = BatPlayerFragment.newInstance(fragment,sportId);
                } else if (position == 2) {
                    return aRplayerFragment =  ARplayerFragment.newInstance(fragment,sportId);
                } else if (position == 3) {
                    return bowlPlayerFragment =  BowlPlayerFragment.newInstance(fragment,sportId);
                }else if (position == 4) {
                    return centerPlayerFragment =  CenterPlayerFragment.newInstance(fragment,sportId);
                }
            }else if (selected_match.getSportId().equals("6")) {
                if (position == 0) {
                    return wKplayerFragment =  WKplayerFragment.newInstance(fragment,sportId);
                } else if (position == 1) {
                    return batPlayerFragment = BatPlayerFragment.newInstance(fragment,sportId);
                } else if (position == 2) {
                    return aRplayerFragment =  ARplayerFragment.newInstance(fragment,sportId);
                }
            }else if (selected_match.getSportId().equals("7")) {
                if (position == 0) {
                    return wKplayerFragment = WKplayerFragment.newInstance(fragment,sportId);
                } else if (position == 1) {
                    return batPlayerFragment = BatPlayerFragment.newInstance(fragment,sportId);
                } else if (position == 2) {
                    return aRplayerFragment =  ARplayerFragment.newInstance(fragment,sportId);
                }
            }else {
                if (position == 0) {
                    return wKplayerFragment = WKplayerFragment.newInstance(fragment,sportId);
                } else if (position == 1) {
                    return batPlayerFragment = BatPlayerFragment.newInstance(fragment,sportId);
                } else if (position == 2) {
                    return aRplayerFragment =  ARplayerFragment.newInstance(fragment,sportId);
                } else if (position == 3) {
                    return bowlPlayerFragment =  BowlPlayerFragment.newInstance(fragment,sportId);
                }
            }
            return null;
        }

        @Override
        public int getItemCount() {
            if (selected_match.getSportId().equals("4")) {
                return 5;
            }else if (selected_match.getSportId().equals("6")) {
                return 3;
            }else if (selected_match.getSportId().equals("7")) {
                return 3;
            }else {
                return 4;
            }
        }

    }

    public class CreditUp implements Comparator<PlayerModel> {
        @Override
        public int compare(PlayerModel o1, PlayerModel o2) {
            return Float.compare(CustomUtil.convertFloat(o2.getPlayerRank()), CustomUtil.convertFloat(o1.getPlayerRank()));
        }
    }

    public class CreditDown implements Comparator<PlayerModel> {
        @Override
        public int compare(PlayerModel o1, PlayerModel o2) {
            return Float.compare(CustomUtil.convertFloat(o1.getPlayerRank()), CustomUtil.convertFloat(o2.getPlayerRank()));
        }
    }

    public class SelUp implements Comparator<PlayerModel> {
        @Override
        public int compare(PlayerModel o1, PlayerModel o2) {
            return Float.compare(CustomUtil.convertFloat(o2.getPlayer_percent()), CustomUtil.convertFloat(o1.getPlayer_percent()));
        }
    }

    public class SelDown implements Comparator<PlayerModel> {
        @Override
        public int compare(PlayerModel o1, PlayerModel o2) {
            return Float.compare(CustomUtil.convertFloat(o1.getPlayer_percent()), CustomUtil.convertFloat(o2.getPlayer_percent()));
        }
    }

    public class avgPointUp implements Comparator<PlayerModel> {
        @Override
        public int compare(PlayerModel o1, PlayerModel o2) {
            return Float.compare(CustomUtil.convertFloat(o2.getPlayerAvgPoint()), CustomUtil.convertFloat(o1.getPlayerAvgPoint()));
        }
    }

    public class avgPointDown implements Comparator<PlayerModel> {
        @Override
        public int compare(PlayerModel o1, PlayerModel o2) {
            return Float.compare(CustomUtil.convertFloat(o1.getPlayerAvgPoint()), CustomUtil.convertFloat(o2.getPlayerAvgPoint()));
        }
    }

}

   /*public class XiUp implements Comparator<PlayerModel> {
        @Override
        public int compare(PlayerModel o1, PlayerModel o2) {
            //return  Float.compare(CustomUtil.convertFloat(o2.getPlayerRank()), CustomUtil.convertFloat(o1.getPlayerRank()));
        }
    }
    public class XiDown implements Comparator<PlayerModel> {
        @Override
        public int compare(PlayerModel o1, PlayerModel o2) {
            //return Float.compare(CustomUtil.convertFloat(o1.getPlayerRank()), CustomUtil.convertFloat(o2.getPlayerRank()));
        }
    }*/
