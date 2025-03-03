package com.fantafeat.Fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantafeat.Activity.CricketSelectPlayerActivity;
import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.Adapter.SelectCVCAdapter;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.PlayerListModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.BottomSheetTeam;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SelectCVCFragment extends BaseFragment {

    public List<PlayerModel> playerModelList = new ArrayList<>();
    TextView points, type, match_title, timer,txtCap,txtWise;
    TextView team_preview, save_team;
    RecyclerView select_cvc_list;
    SelectCVCAdapter selectCVCAdapter;
    public int c, vc;
    public String cid="", vcid="";

    private MatchModel selected_match;
    private long diff;
    private CountDownTimer countDownTimer;
    private Toolbar toolbar;
    public ImageView mToolBarBack,arrow_players,arrow_points,imgPreview;

    private int teamCreateType;
    private String temp_team_id;
    public int avg_point_selected, type_selected,cap_selected,wise_selected;
    private List<PlayerModel> oldPlayerList;
    private String contestData = "",typeFilter="";
    private BottomSheetDialog bottomSheetDialog = null;

    public static SelectCVCFragment newInstance(String param1, int teamCreateType, String temp_team_id, String contestData) {
        SelectCVCFragment fragment = new SelectCVCFragment();
        Bundle args = new Bundle();
        args.putString("select_player", param1);
        args.putInt("teamCreateType", teamCreateType);
        args.putString("temp_team_id", temp_team_id);
        args.putString("contest_data", contestData);
        fragment.setArguments(args);
        return fragment;
    }

    public static SelectCVCFragment newInstance(String param1, int teamCreateType, String oldSelectedPlayer, String temp_team_id, String contestData) {
        SelectCVCFragment fragment = new SelectCVCFragment();
        Bundle args = new Bundle();
        args.putString("select_player", param1);
        args.putInt("teamCreateType", teamCreateType);
        args.putString("temp_team_id", temp_team_id);
        args.putString("oldPlayerList", oldSelectedPlayer);
        args.putString("contest_data", contestData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gson = new Gson();
            contestData = getArguments().getString("contest_data");
            LogUtil.d("resp","contestData: "+contestData);
            String player_list = getArguments().getString("select_player");
            temp_team_id = getArguments().getString("temp_team_id");
            LogUtil.e("resp", "temp_team_id " + temp_team_id);
            Type player = new TypeToken<List<PlayerModel>>() {
            }.getType();
            playerModelList = gson.fromJson(player_list, player);
            teamCreateType = getArguments().getInt("teamCreateType");
            LogUtil.e(TAG, "teamCreateType " + teamCreateType);
            String oldPlayer = getArguments().getString("oldPlayerList");
            Type playerModel = new TypeToken<List<PlayerModel>>() {
            }.getType();
            oldPlayerList = gson.fromJson(oldPlayer, playerModel);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_c_v_c, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        countDownTimer.cancel();
    }

    @Override
    public void initControl(View view) {
        c = vc = -1;
        avg_point_selected = 0;
        cap_selected=0;
        wise_selected=0;
        type_selected = 1;

        toolbar = view.findViewById(R.id.contest_list_toolbar);
        ((CricketSelectPlayerActivity) mContext).setSupportActionBar(mToolbar);
        match_title = view.findViewById(R.id.match_title);
        timer = view.findViewById(R.id.timer);
        mToolBarBack = view.findViewById(R.id.toolbar_back);
        arrow_players = view.findViewById(R.id.arrow_players);
        arrow_points = view.findViewById(R.id.arrow_points);
        txtCap = view.findViewById(R.id.txtCap);
        txtWise = view.findViewById(R.id.txtWise);

        points = view.findViewById(R.id.points);

        type = view.findViewById(R.id.type);
        team_preview = view.findViewById(R.id.team_preview);
        imgPreview = view.findViewById(R.id.imgPreview);
        save_team = view.findViewById(R.id.save_team);
        select_cvc_list = view.findViewById(R.id.select_cvc_list);

        if (preferences.getMatchModel() != null) {
            selected_match = preferences.getMatchModel();
        }

        if (selected_match.getMatchTitle() != null) {
            match_title.setText(selected_match.getTeam1Sname() + " vs " + selected_match.getTeam2Sname());
        }
        setTimer();


        Map<String, ArrayList<PlayerModel>> hash = new HashMap<>();
        for (PlayerModel mp : CustomUtil.emptyIfNull(playerModelList)) {
            if (hash.containsKey(mp.getPlayerType())) {
                hash.get(mp.getPlayerType()).add(mp);
            } else {
                ArrayList<PlayerModel> matchPlayers = new ArrayList<>();
                matchPlayers.add(mp);
                hash.put(mp.getPlayerType(), matchPlayers);
            }
        }

        playerModelList = new ArrayList<>();
        if (selected_match.getSportId().equals("1")){
            playerModelList.addAll(Objects.requireNonNull(hash.get("WK")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("BAT")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("AR")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("BOWL")));
        }else if (selected_match.getSportId().equals("2")){
            playerModelList.addAll(Objects.requireNonNull(hash.get("GK")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("DEF")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("MID")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("FOR")));
        }else if (selected_match.getSportId().equals("3")){
            playerModelList.addAll(Objects.requireNonNull(hash.get("OF")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("IF")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("PIT")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("CAT")));
        }
        else if (selected_match.getSportId().equals("4")){
            playerModelList.addAll(Objects.requireNonNull(hash.get("PG")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("SG")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("SF")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("PF")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("CR")));
        }else if (selected_match.getSportId().equals("6")){
            playerModelList.addAll(Objects.requireNonNull(hash.get("GK")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("DEF")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("FWD")));
        }else if (selected_match.getSportId().equals("7")){
            playerModelList.addAll(Objects.requireNonNull(hash.get("DEF")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("AR")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("RAID")));
        }

        int i = 0;
        for (PlayerModel mp : CustomUtil.emptyIfNull(playerModelList)) {

            if (mp.getIsCaptain().equals("Yes")) {
                c = i;
                cid=mp.getId();
            } else if (mp.getIsWiseCaptain().equals("Yes")) {
                vc = i;
                vcid=mp.getId();
            }
            i++;
        }

        playerModelList = new ArrayList<>();
        if (selected_match.getSportId().equals("1")){
            playerModelList.addAll(Objects.requireNonNull(hash.get("WK")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("BAT")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("AR")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("BOWL")));
        }else if (selected_match.getSportId().equals("2")){
            playerModelList.addAll(Objects.requireNonNull(hash.get("GK")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("DEF")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("MID")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("FOR")));
        }else if (selected_match.getSportId().equals("3")){
            playerModelList.addAll(Objects.requireNonNull(hash.get("OF")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("IF")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("PIT")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("CAT")));
        }else if (selected_match.getSportId().equals("4")){
            playerModelList.addAll(Objects.requireNonNull(hash.get("PG")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("SG")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("SF")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("PF")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("CR")));
        }else if (selected_match.getSportId().equals("6")){
            playerModelList.addAll(Objects.requireNonNull(hash.get("GK")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("DEF")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("FWD")));
        }else if (selected_match.getSportId().equals("7")){
            playerModelList.addAll(Objects.requireNonNull(hash.get("DEF")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("AR")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("RAID")));
        }

        selectCVCAdapter = new SelectCVCAdapter(mContext, playerModelList, preferences, SelectCVCFragment.this);
        select_cvc_list.setLayoutManager(new LinearLayoutManager(mContext));
        select_cvc_list.setAdapter(selectCVCAdapter);
        select_cvc_list.setHasFixedSize(true);

        changeNextBg();
    }

    public void changeNextBg(){
        if (!cid.equals("") && !vcid.equals("")) {
            save_team.setBackground(mContext.getResources().getDrawable(R.drawable.green_bg_round_fill));
        }else{
            save_team.setBackground(mContext.getResources().getDrawable(R.drawable.gray_bg_round_fill));
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
                    diff = new DecimalFormat("0").format(elapsedDays) + "d " + new DecimalFormat("0").format(elapsedHours) + "h";
                } else if (elapsedHours > 0) {
                    diff = new DecimalFormat("0").format(elapsedHours) + "h " + new DecimalFormat("0").format(elapsedMinutes) + "m";
                } else {
                    diff = new DecimalFormat("0").format(elapsedMinutes) + "m " + new DecimalFormat("0").format(elapsedSeconds) + "s";
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
    public void onResume() {
        super.onResume();
        ConstantUtil.isTimeOverShow=false;
    }

    private void timesOver() {
        TextView btn_ok;
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_events, null);
        btn_ok = (TextView) view.findViewById(R.id.btn_ok);
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
        bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);
        if (!bottomSheetDialog.isShowing() && !ConstantUtil.isTimeOverShow){
            ConstantUtil.isTimeOverShow=true;
            bottomSheetDialog.show();
        }

    }

    @Override
    public void initClick() {

        mToolBarBack.setOnClickListener(v->{
            RemoveFragment();
        });

        team_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()){
                    int i = 0;
                    for (PlayerModel mp : CustomUtil.emptyIfNull(playerModelList)) {
                        if (mp.getId().equals(cid)){
                            mp.setIsCaptain("Yes");
                            mp.setIsWiseCaptain("No");
                        }else if (mp.getId().equals(vcid)){
                            mp.setIsCaptain("No");
                            mp.setIsWiseCaptain("Yes");
                        }else {
                            mp.setIsCaptain("No");
                            mp.setIsWiseCaptain("No");
                        }
                  /*  if (i == c) {
                        mp.setIsCaptain("Yes");
                        mp.setIsWiseCaptain("No");
                    } else if (i == vc) {
                        mp.setIsCaptain("No");
                        mp.setIsWiseCaptain("Yes");
                    } else {
                        mp.setIsCaptain("No");
                        mp.setIsWiseCaptain("No");
                    }*/
                        i++;
                    }

                    BottomSheetTeam bottomSheetTeam = new BottomSheetTeam(mContext, "", playerModelList);
                    bottomSheetTeam.show(((CricketSelectPlayerActivity) mContext).getSupportFragmentManager(),
                            BottomSheetTeam.TAG);
                }

            }
        });

        imgPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                team_preview.performClick();
            }
        });

        save_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()){
                    save_team.setEnabled(false);
                    if (!cid.equals("") && !vcid.equals("")) {
                        int i = 0;
                        for (PlayerModel mp : CustomUtil.emptyIfNull(playerModelList)) {
                            if (mp.getId().equals(cid)){
                                mp.setIsCaptain("Yes");
                                mp.setIsWiseCaptain("No");
                            }else if (mp.getId().equals(vcid)){
                                mp.setIsCaptain("No");
                                mp.setIsWiseCaptain("Yes");
                            }else {
                                mp.setIsCaptain("No");
                                mp.setIsWiseCaptain("No");
                            }
                        /*if (i == c) {
                            mp.setIsCaptain("Yes");
                            mp.setIsWiseCaptain("No");
                        } else if (i == vc) {
                            mp.setIsCaptain("No");
                            mp.setIsWiseCaptain("Yes");
                        } else {
                            mp.setIsCaptain("No");
                            mp.setIsWiseCaptain("No");
                        }*/
                            i++;
                        }
                        //sendTeam(playerModelList);

                        boolean isMatch = false, teamMatch;
                        for (PlayerListModel plm : CustomUtil.emptyIfNull(preferences.getPlayerModel())) {
                            teamMatch = false;
                            for (PlayerModel pm : CustomUtil.emptyIfNull(plm.getPlayerDetailList())) {
                                isMatch = false;
                                for (PlayerModel spm : CustomUtil.emptyIfNull(playerModelList)) {
                                    if (spm.getPlayerId().equalsIgnoreCase(pm.getPlayerId()) &&
                                            spm.getIsCaptain().equalsIgnoreCase(pm.getIsCaptain()) &&
                                            spm.getIsWiseCaptain().equalsIgnoreCase(pm.getIsWiseCaptain())) {
                                        isMatch = true;
                                        break;
                                    }
                                }
                                if (!isMatch) {
                                    break;

                                }
                            }
                            if (isMatch) {

                                LogUtil.e(TAG, "onClick: for if");
                            }
                        }
                        if (isMatch) {
                            save_team.setEnabled(true);
                            CustomUtil.showTopSneakError(mContext, "Team Already Created");
                        } else {
                            if (teamCreateType == 0) {
                                sendTeam(playerModelList);
                            } else if (teamCreateType == 1) {
                                editTeam(playerModelList);
                            } else if (teamCreateType == -1) {
                                sendTeam(playerModelList);
                            }
                        }

                    /*JSONArray team_details= new JSONArray();
                    for(PlayerModel model:playerModelList){
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("is_captain",model.getIsCaptain());
                            jsonObject.put("is_wise_captain",model.getIsWiseCaptain());
                            jsonObject.put("is_man_of_match","");
                            jsonObject.put("player_type",model.getPlayerType());
                            jsonObject.put("player_id",model.getPlayerId());
                            jsonObject.put("player_name",model.getPlayerName());
                            jsonObject.put("player_image",model.getPlayerImage());
                            jsonObject.put("player_sname",model.getPlayerSname());
                            jsonObject.put("player_avg_point",model.getPlayerAvgPoint());
                            jsonObject.put("player_rank",model.getPlayerRank());
                            jsonObject.put("team_id",model.getTeamId());
                            jsonObject.put("playing_xi",model.getPlayingXi());

                            team_details.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }*/

                        //makeAPIcall(team_details);
                    } else {
                        save_team.setEnabled(true);
                        CustomUtil.showTopSneakError(mContext, "Please select both Captain and Vice Captain");
                    }
                }

            }
        });

        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type_selected == 0) {
                    type_selected = 1;
                    arrow_players.setImageResource(R.drawable.ic_down_green);
                }else {
                    arrow_players.setImageResource(R.drawable.ic_up_green);
                    type_selected = 0;
                }

                if(avg_point_selected==1 || avg_point_selected==-1){
                    avg_point_selected = 0;
                    arrow_points.setImageResource(R.drawable.ic_up_down_arrow);
                    points.setTypeface(Typeface.DEFAULT);
                }

                type.setTypeface(Typeface.DEFAULT_BOLD);
                points.setTypeface(Typeface.DEFAULT);

               // avg_point_selected = 0;
               // arrow_points.setImageResource(R.drawable.ic_up_green);

                filterData();
            }
        });

        points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeFilter="point";
                if (avg_point_selected == 0) {
                    avg_point_selected = 1;
                    arrow_points.setImageResource( R.drawable.ic_up_green);
                } else if (avg_point_selected == 1) {
                    avg_point_selected = -1;
                    arrow_points.setImageResource( R.drawable.ic_down_green);
                } else if (avg_point_selected == -1) {
                    avg_point_selected = 1;
                    arrow_points.setImageResource( R.drawable.ic_up_green);
                }
                if(type_selected == 1){
                    type_selected = 0;
                    arrow_players.setImageResource(R.drawable.ic_up_down_arrow);
                    type.setTypeface(Typeface.DEFAULT);

                }
                points.setTypeface(Typeface.DEFAULT_BOLD);
                type.setTypeface(Typeface.DEFAULT);

                //type_selected = 0;
                //arrow_players.setImageResource(R.drawable.ic_up_down_arrow);

                filterData();
            }
        });

        txtCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeFilter="cap";
                if (cap_selected == 0) {
                    cap_selected = 1;
                   // arrow_points.setImageResource( R.drawable.ic_up_green);
                } else if (cap_selected == 1) {
                    cap_selected = -1;
                    //arrow_points.setImageResource( R.drawable.ic_down_green);
                } else if (cap_selected == -1) {
                    cap_selected = 1;
                   // arrow_points.setImageResource( R.drawable.ic_up_green);
                }
                if(type_selected == 1){
                    type_selected = 0;
                    arrow_players.setImageResource(R.drawable.ic_up_down_arrow);
                    type.setTypeface(Typeface.DEFAULT);

                }
                points.setTypeface(Typeface.DEFAULT);
                type.setTypeface(Typeface.DEFAULT);
                txtCap.setTypeface(Typeface.DEFAULT_BOLD);
                txtWise.setTypeface(Typeface.DEFAULT);

                //type_selected = 0;
                //arrow_players.setImageResource(R.drawable.ic_up_down_arrow);

                filterData();
            }
        });

        txtWise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeFilter="wise";
                if (wise_selected == 0) {
                    wise_selected = 1;
                   // arrow_points.setImageResource( R.drawable.ic_up_green);
                } else if (wise_selected == 1) {
                    wise_selected = -1;
                    //arrow_points.setImageResource( R.drawable.ic_down_green);
                } else if (wise_selected == -1) {
                    wise_selected = 1;
                   // arrow_points.setImageResource( R.drawable.ic_up_green);
                }
                if(type_selected == 1){
                    type_selected = 0;
                    arrow_players.setImageResource(R.drawable.ic_up_down_arrow);
                    type.setTypeface(Typeface.DEFAULT);

                }
                points.setTypeface(Typeface.DEFAULT);
                type.setTypeface(Typeface.DEFAULT);
                txtCap.setTypeface(Typeface.DEFAULT);
                txtWise.setTypeface(Typeface.DEFAULT_BOLD);

                //type_selected = 0;
                //arrow_players.setImageResource(R.drawable.ic_up_down_arrow);

                filterData();
            }
        });
    }

    private void filterData() {
        if (typeFilter.equalsIgnoreCase("point")){
            if (avg_point_selected == 1) {
                Collections.sort(playerModelList, new avgPointUp());
            } else if (avg_point_selected == -1) {
                Collections.sort(playerModelList, new avgPointDown());
            }
        }

        if (typeFilter.equalsIgnoreCase("cap")){
            if (cap_selected == 1) {
                Collections.sort(playerModelList, new capUp());
            } else if (cap_selected == -1) {
                Collections.sort(playerModelList, new capDown());
            }
        }

        if (typeFilter.equalsIgnoreCase("wise")){
            if (wise_selected == 1) {
                Collections.sort(playerModelList, new wiseUp());
            } else if (wise_selected == -1) {
                Collections.sort(playerModelList, new wiseDown());
            }
        }

        if (type_selected == 1) {
            Map<String, ArrayList<PlayerModel>> hash = new HashMap<>();
            for (PlayerModel mp : CustomUtil.emptyIfNull(playerModelList)) {
                if (hash.containsKey(mp.getPlayerType())) {
                    hash.get(mp.getPlayerType()).add(mp);
                } else {
                    ArrayList<PlayerModel> matchPlayers = new ArrayList<>();
                    matchPlayers.add(mp);
                    hash.put(mp.getPlayerType(), matchPlayers);
                }
            }

            playerModelList = new ArrayList<>();
            if (selected_match.getSportId().equals("1")){
                playerModelList.addAll(Objects.requireNonNull(hash.get("WK")));
                playerModelList.addAll(Objects.requireNonNull(hash.get("BAT")));
                playerModelList.addAll(Objects.requireNonNull(hash.get("AR")));
                playerModelList.addAll(Objects.requireNonNull(hash.get("BOWL")));
            }else if (selected_match.getSportId().equals("2")){
                playerModelList.addAll(Objects.requireNonNull(hash.get("GK")));
                playerModelList.addAll(Objects.requireNonNull(hash.get("DEF")));
                playerModelList.addAll(Objects.requireNonNull(hash.get("MID")));
                playerModelList.addAll(Objects.requireNonNull(hash.get("FOR")));
            }else if (selected_match.getSportId().equals("3")){
                playerModelList.addAll(Objects.requireNonNull(hash.get("OF")));
                playerModelList.addAll(Objects.requireNonNull(hash.get("IF")));
                playerModelList.addAll(Objects.requireNonNull(hash.get("PIT")));
                playerModelList.addAll(Objects.requireNonNull(hash.get("CAT")));
            }
            else if (selected_match.getSportId().equals("4")){
                playerModelList.addAll(Objects.requireNonNull(hash.get("PG")));
                playerModelList.addAll(Objects.requireNonNull(hash.get("SG")));
                playerModelList.addAll(Objects.requireNonNull(hash.get("SF")));
                playerModelList.addAll(Objects.requireNonNull(hash.get("PF")));
                playerModelList.addAll(Objects.requireNonNull(hash.get("CR")));
            }else if (selected_match.getSportId().equals("6")){
                playerModelList.addAll(Objects.requireNonNull(hash.get("GK")));
                playerModelList.addAll(Objects.requireNonNull(hash.get("DEF")));
                playerModelList.addAll(Objects.requireNonNull(hash.get("FWD")));
            }else if (selected_match.getSportId().equals("7")){
                playerModelList.addAll(Objects.requireNonNull(hash.get("DEF")));
                playerModelList.addAll(Objects.requireNonNull(hash.get("AR")));
                playerModelList.addAll(Objects.requireNonNull(hash.get("RAID")));
            }
       /*     playerModelList.addAll(Objects.requireNonNull(hash.get("WK")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("BAT")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("AR")));
            playerModelList.addAll(Objects.requireNonNull(hash.get("BOWL")));*/
        }

        selectCVCAdapter.updateSelectCVC(playerModelList);
    }

    public void editTeam(List<PlayerModel> players) {

        JSONArray oldTeamDiff = new JSONArray();
        JSONArray newTeamDiff = new JSONArray();

        for (PlayerModel old : oldPlayerList) {
            boolean isMatch = false;
            for (PlayerModel newList : players) {
                if (old.getPlayerId().equalsIgnoreCase(newList.getPlayerId())) {
                    if (old.getIsCaptain().equalsIgnoreCase(newList.getIsCaptain()) &&
                            old.getIsWiseCaptain().equalsIgnoreCase(newList.getIsWiseCaptain())) {
                        isMatch = true;
                    }
                    break;
                }
            }
            if (!isMatch) {
                JSONObject jo = new JSONObject();
                try {
                    jo.put("id", old.getId());

                    jo.put("player_id", old.getPlayerId());
                    jo.put("player_name", old.getPlayerName());
                    jo.put("player_image", old.getPlayerImage());
                    jo.put("player_sname", old.getPlayerSname());
                    jo.put("player_avg_point", old.getPlayerAvgPoint());
                    jo.put("player_rank", old.getPlayerRank());
                    jo.put("playing_xi", old.getPlayingXi());
                    jo.put("team_id", old.getTeamId());
                    jo.put("player_type", old.getPlayerType());
                    jo.put("is_captain", old.getIsCaptain());
                    jo.put("is_wise_captain", old.getIsWiseCaptain());
                    jo.put("is_man_of_match", old.getIsManOfMatch());
                    oldTeamDiff.put(jo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //oldPlayerArray.add(old);
            }
        }
        LogUtil.e(TAG, "Edit oldTeamDiff1 " + oldTeamDiff.toString());
        for (PlayerModel newList : players) {
            boolean isMatch = false;
            for (PlayerModel old : oldPlayerList) {
                if (old.getPlayerId().equalsIgnoreCase(newList.getPlayerId())) {
                    if (old.getIsCaptain().equalsIgnoreCase(newList.getIsCaptain()) &&
                            old.getIsWiseCaptain().equalsIgnoreCase(newList.getIsWiseCaptain())) {
                        isMatch = true;
                    }
                    break;
                }
            }
            if (!isMatch) {
                JSONObject jo = new JSONObject();
                try {
                    jo.put("player_id", newList.getPlayerId());
                    jo.put("player_name", newList.getPlayerName());
                    jo.put("player_image", newList.getPlayerImage());
                    jo.put("player_sname", newList.getPlayerSname());
                    jo.put("player_avg_point", newList.getPlayerAvgPoint());
                    jo.put("player_rank", newList.getPlayerRank());
                    jo.put("playing_xi", newList.getPlayingXi());
                    jo.put("team_id", newList.getTeamId());
                    jo.put("player_type", newList.getPlayerType());
                    jo.put("is_captain", newList.getIsCaptain());
                    jo.put("is_wise_captain", newList.getIsWiseCaptain());
                    jo.put("is_man_of_match", newList.getIsManOfMatch());
                    newTeamDiff.put(jo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //newPlayerArray.add(newList);
            }
        }
        LogUtil.e(TAG, "Edit newTeamDiff " + newTeamDiff.toString());
        JSONObject PlayerData = null;
        try {
            PlayerData = new JSONObject();

            PlayerData.put("match_id", preferences.getMatchModel().getId());
            PlayerData.put("con_display_type", preferences.getMatchModel().getConDisplayType());
            PlayerData.put("user_id", preferences.getUserModel().getId());
            PlayerData.put("team_name", preferences.getUserModel().getUserTeamName());
            PlayerData.put("temp_team_id", temp_team_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "Edit PlayerData " + PlayerData.toString());
        JSONArray ja = new JSONArray();
        for (PlayerModel mp : CustomUtil.emptyIfNull(players)) {
            JSONObject jo = new JSONObject();
            try {
                jo.put("player_id", mp.getPlayerId());
                jo.put("player_name", mp.getPlayerName());
                jo.put("player_image", mp.getPlayerImage());
                jo.put("player_sname", mp.getPlayerSname());
                jo.put("player_avg_point", mp.getPlayerAvgPoint());
                jo.put("player_rank", mp.getPlayerRank());
                jo.put("playing_xi", mp.getPlayingXi());
                jo.put("team_id", mp.getTeamId());
                jo.put("player_type", mp.getPlayerType());
                jo.put("is_captain", mp.getIsCaptain());
                jo.put("is_wise_captain", mp.getIsWiseCaptain());
                jo.put("is_man_of_match", mp.getIsManOfMatch());
                ja.put(jo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //LogUtil.e(TAG, "onCreate: NAme :  " + mp.getPlayer_name() + "  id: " + mp.getPlayer_id() + "  is c : " + mp.getIs_c() + "  is vc : " + mp.getIs_vc() + "  is mm : "+ mp.getIs_mm() );
        }
        LogUtil.e(TAG, "Edit ja " + ja.toString());
        try {
            PlayerData.put("team_details", ja);
            PlayerData.put("oldTeamDiff", oldTeamDiff);
            PlayerData.put("newTeamDiff", newTeamDiff);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.e(TAG, "Edit ja " + PlayerData.toString());

        HttpRestClient.postJSON(mContext, true, ApiManager.EDIT_TEMP_TEAM, PlayerData, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    save_team.setEnabled(true);
                    preferences.setPlayerModel(null);
                    ((CricketSelectPlayerActivity) mContext).finish();
                } else {
                    save_team.setEnabled(true);
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                save_team.setEnabled(true);

            }
        });
    }

    public void sendTeam(List<PlayerModel> players) {
        JSONObject PlayerData = null;
        try {
            PlayerData = new JSONObject();

            PlayerData.put("match_id", preferences.getMatchModel().getId());
            PlayerData.put("con_display_type", preferences.getMatchModel().getConDisplayType());
            PlayerData.put("user_id", preferences.getUserModel().getId());
            PlayerData.put("team_name", preferences.getUserModel().getUserTeamName());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray ja = new JSONArray();
        for (PlayerModel mp : CustomUtil.emptyIfNull(players)) {
            JSONObject jo = new JSONObject();
            try {
                jo.put("player_id", mp.getPlayerId());
                jo.put("player_name", mp.getPlayerName());
                jo.put("player_image", mp.getPlayerImage());
                jo.put("player_sname", mp.getPlayerSname());
                jo.put("player_avg_point", mp.getPlayerAvgPoint());
                jo.put("player_rank", mp.getPlayerRank());
                jo.put("playing_xi", mp.getPlayingXi());
                jo.put("team_id", mp.getTeamId());
                jo.put("player_type", mp.getPlayerType());
                jo.put("is_captain", mp.getIsCaptain());
                jo.put("is_wise_captain", mp.getIsWiseCaptain());
                jo.put("is_man_of_match", mp.getIsManOfMatch());
                ja.put(jo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //LogUtil.e(TAG, "onCreate: NAme :  " + mp.getPlayer_name() + "  id: " + mp.getPlayer_id() + "  is c : " + mp.getIs_c() + "  is vc : " + mp.getIs_vc() + "  is mm : "+ mp.getIs_mm() );
        }
        try {
            PlayerData.put("team_details", ja);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.e(TAG, "sendTeam: " + PlayerData.toString());

        HttpRestClient.postJSON(mContext, true, ApiManager.CREATE_TEMP_TEAM, PlayerData, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    save_team.setEnabled(true);
                    preferences.setPlayerModel(null);
                    /*try {
                        for (Iterator<Fragment> iterator = FragmentUtil.fragmentStack.iterator(); iterator.hasNext(); ) {
                            Fragment fragment = iterator.next();
                            LogUtil.e(TAG, "onSuccessResult: fragment"+fragment.getTag() );
                            if (fragment.getTag().equals(fragmentTag(2))) {
                                new FragmentUtil().resumeFragment((FragmentActivity) mContext,
                                        R.id.fragment_container,
                                        new FeedFragment(),
                                        ((HomeActivity) mContext).fragmentTag(2),
                                        FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                                break;
                            }
                        }
                    }catch(Exception e){
                        LogUtil.e(TAG,"error "+e.toString());
                    }*/

                    if (teamCreateType == -1) {
                        /*new FragmentUtil().resumeFragment((FragmentActivity) mContext,
                                R.id.fragment_container,
                                new MatchTeamListFragment(),
                                ((HomeActivity) mContext).fragmentTag(12),
                                FragmentUtil.ANIMATION_TYPE.SLIDE_DOWN_TO_UP);*/
                        ((CricketSelectPlayerActivity) mContext).finish();

                    }
                    else if(teamCreateType==0) {
                        if(!contestData.equals("")) {
                            Log.e(TAG, "onSuccessResult: 2");
                            preferences.setPref("join_contest", true);
                            preferences.setPref("contest_pass_data", contestData);
                            ((CricketSelectPlayerActivity) mContext).finish();
                        }else{
                            ((CricketSelectPlayerActivity) mContext).finish();
                        }
                    }
                    else{
                        ((CricketSelectPlayerActivity) mContext).finish();
                    }
                } else {
                    save_team.setEnabled(true);
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                save_team.setEnabled(true);
            }
        });
    }

    /*public boolean check_captain(PlayerModel model) {
        if (is_captain == 0) {
            if (model.getIsWiseCaptain().equals("yes")) {
                model.setIsWiseCaptain("no");
                is_vise_captain = 0;
                model.setIsCaptain("yes");
                is_captain = 1;
                return true;
                //selectCVCAdapter.updateList(playerModelList);
            } else {
                model.setIsCaptain("yes");
                is_captain = 1;
                return true;
                //selectCVCAdapter.updateList(playerModelList);
            }
        } else {
            if (model.getIsWiseCaptain().equals("yes")) {
                model.setIsWiseCaptain("no");
                is_vise_captain = 0;
                for (int i = 0; i < playerModelList.size(); i++) {
                    if (playerModelList.get(i).getIsCaptain().equals("yes")) {
                        playerModelList.get(i).setIsCaptain("no");
                        selectCVCAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                model.setIsCaptain("yes");
                is_captain = 1;
                return true;
//                selectCVCAdapter.notifyItemChanged();
            } else {
                for (int i = 0; i < playerModelList.size(); i++) {
                    if (playerModelList.get(i).getIsCaptain().equals("yes")) {
                        playerModelList.get(i).setIsCaptain("no");
                        selectCVCAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                model.setIsCaptain("yes");
                is_captain = 1;
                return true;
                //selectCVCAdapter.updateList(playerModelList);
            }
        }
    }

    public boolean check_vise_captain(PlayerModel model) {

        if (is_vise_captain == 0) {
            if (model.getIsCaptain().equals("yes")) {
                model.setIsCaptain("no");
                is_captain = 0;
                model.setIsWiseCaptain("yes");
                is_vise_captain = 1;
                return true;
                //selectCVCAdapter.updateList(playerModelList);
            } else {
                model.setIsWiseCaptain("yes");
                is_vise_captain = 1;
                return true;
                //selectCVCAdapter.updateList(playerModelList);
            }
        } else {
            if (model.getIsCaptain().equals("yes")) {
                model.setIsCaptain("no");
                is_captain = 0;
                for (int i = 0; i < playerModelList.size(); i++) {
                    if (playerModelList.get(i).getIsWiseCaptain().equals("yes")) {
                        playerModelList.get(i).setIsWiseCaptain("no");
                        selectCVCAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                model.setIsWiseCaptain("yes");
                is_vise_captain = 1;
                return true;
//                selectCVCAdapter.notifyItemChanged();
            } else {
                for (int i = 0; i < playerModelList.size(); i++) {
                    if (playerModelList.get(i).getIsWiseCaptain().equals("yes")) {
                        playerModelList.get(i).setIsWiseCaptain("no");
                        selectCVCAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                model.setIsWiseCaptain("yes");
                is_vise_captain = 1;
                return true;
                //selectCVCAdapter.updateList(playerModelList);
            }
        }
    }*/

    /*  private void makeAPIcall(JSONArray team_details) {
        JSONObject jsonObject = new JSONObject();
        try {
            //Log.e(TAG, "makeAPIcall: "+preferences.getMatchModel()().getId()+"      "+preferences.getUserModel().getUserTeamName()+"      "+preferences.getUserModel().getId()+"    " );
            jsonObject.put("match_id",preferences.getMatchModel().getId());
            jsonObject.put("con_display_type","1");
            jsonObject.put("team_name",preferences.getUserModel().getUserTeamName());
            jsonObject.put("user_id",preferences.getUserModel().getId());
            jsonObject.put("team_details",team_details);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.e(TAG, "makeAPIcall: "+jsonObject.toString() );

        HttpRestClient.postJSON(mContext, true, ApiManager.CREATE_TEMP_TEAM, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if(responseBody.optBoolean("status")){
                    Log.e(TAG, "onSuccessResult: "+responseBody.optString("data") );
                    */
    /*new FragmentUtil().resumeFragment((FragmentActivity) mContext,
                            R.id.home_fragment_container,
                            new ContestListFragment(),
                            ((HomeActivity) mContext).fragmentTag(12),
                            FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT
                    );*/
    /*
                    ((CricketSelectPlayerActivity)mContext).finish();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                CustomUtil.showSnackBarShort(mContext,responseBody);
            }
        });
    }*/

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

    public class capUp implements Comparator<PlayerModel> {
        @Override
        public int compare(PlayerModel o1, PlayerModel o2) {
            return Float.compare(CustomUtil.convertFloat(o2.getCap_percent()), CustomUtil.convertFloat(o1.getCap_percent()));
        }
    }

    public class capDown implements Comparator<PlayerModel> {
        @Override
        public int compare(PlayerModel o1, PlayerModel o2) {
            return Float.compare(CustomUtil.convertFloat(o1.getCap_percent()), CustomUtil.convertFloat(o2.getCap_percent()));
        }
    }

    public class wiseUp implements Comparator<PlayerModel> {
        @Override
        public int compare(PlayerModel o1, PlayerModel o2) {
            return Float.compare(CustomUtil.convertFloat(o2.getWise_cap_percent()), CustomUtil.convertFloat(o1.getWise_cap_percent()));
        }
    }

    public class wiseDown implements Comparator<PlayerModel> {
        @Override
        public int compare(PlayerModel o1, PlayerModel o2) {
            return Float.compare(CustomUtil.convertFloat(o1.getWise_cap_percent()), CustomUtil.convertFloat(o2.getWise_cap_percent()));
        }
    }

}


   /* public boolean check_captain(PlayerModel model){
        if(is_captain==0) {
            if(model.getIs_vice_captain().equals("yes")){
                model.setIs_vice_captain("no");
                is_vise_captain=0;
                model.setIs_captain("yes");
                is_captain = 1;
                return true;
                //selectCVCAdapter.updateList(playerModelList);
            }else{
                model.setIs_captain("yes");
                is_captain = 1;
                return true;
                //selectCVCAdapter.updateList(playerModelList);
            }
        }else{
            if(model.getIs_vice_captain().equals("yes")){
                model.setIs_vice_captain("no");
                is_vise_captain=0;
                for (int i = 0; i < playerModelList.size(); i++) {
                    if (playerModelList.get(i).getIs_captain().equals("yes")) {
                        playerModelList.get(i).setIs_captain("no");
                        selectCVCAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                model.setIs_captain("yes");
                is_captain = 1;
                return true;
//                selectCVCAdapter.notifyItemChanged();
            }else{
                for (int i = 0; i < playerModelList.size(); i++) {
                    if (playerModelList.get(i).getIs_captain().equals("yes")) {
                        playerModelList.get(i).setIs_captain("no");
                        selectCVCAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                model.setIs_captain("yes");
                is_captain = 1;
                return true;
                //selectCVCAdapter.updateList(playerModelList);
            }
        }
        */
/*if(model.getIs_captain().equals("0")) {
            if (model.getIs_vice_captain().equals("1")) {
                model.setIs_vice_captain("0");
                is_vise_captain = 0;
                model.setIs_captain("1");
                is_captain = 1;
            } else {
                if (is_captain == 1) {
                    for (int i = 0; i < playerModelList.size(); i++) {
                        if (playerModelList.get(i).getIs_captain().equals("1")) {
                            playerModelList.get(i).setIs_captain("0");
                        }
                    }
                    model.setIs_captain("1");
                } else {
                    model.setIs_captain("1");
                    is_captain = 1;
                }
            }*//*

    }

    public boolean check_vise_captain(PlayerModel model){

        if(is_vise_captain==0) {
            if(model.getIs_captain().equals("yes")){
                model.setIs_captain("no");
                is_captain=0;
                model.setIs_vice_captain("yes");
                is_vise_captain = 1;
                return true;
                //selectCVCAdapter.updateList(playerModelList);
            }else{
                model.setIs_vice_captain("yes");
                is_vise_captain = 1;
                return true;
                //selectCVCAdapter.updateList(playerModelList);
            }
        }else{
            if(model.getIs_captain().equals("yes")){
                model.setIs_captain("no");
                is_captain=0;
                for (int i = 0; i < playerModelList.size(); i++) {
                    if (playerModelList.get(i).getIs_vice_captain().equals("yes")) {
                        playerModelList.get(i).setIs_vice_captain("no");
                        selectCVCAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                model.setIs_vice_captain("yes");
                is_vise_captain = 1;
                return true;
//                selectCVCAdapter.notifyItemChanged();
            }else{
                for (int i = 0; i < playerModelList.size(); i++) {
                    if (playerModelList.get(i).getIs_vice_captain().equals("yes")) {
                        playerModelList.get(i).setIs_vice_captain("no");
                        selectCVCAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                model.setIs_vice_captain("yes");
                is_vise_captain = 1;
                return true;
                //selectCVCAdapter.updateList(playerModelList);
            }
        }
        *//*if(model.getIs_vice_captain().equals("0")){
            if(model.getIs_captain().equals("1")){
                model.setIs_captain("0");
                is_captain =0;
                model.setIs_vice_captain("1");
                is_vise_captain = 1;
            }else{
                if(is_vise_captain == 1){
                    for(int i=0;i<playerModelList.size();i++){
                        if(playerModelList.get(i).getIs_vice_captain().equals("1")){
                            playerModelList.get(i).setIs_vice_captain("0");
                        }
                    }
                    model.setIs_vice_captain("1");
                }else{
                    model.setIs_vice_captain("1");
                    is_vise_captain = 1;
                }
            }*//*
    }*/

   /* public boolean check_captain(PlayerModel model){
        if(is_captain==0) {
            if(model.getIsWiseCaptain().equals("yes")){
                model.setIsWiseCaptain("no");
                is_vise_captain=0;
                model.setIsCaptain("yes");
                is_captain = 1;
                return true;
                //selectCVCAdapter.updateList(playerModelList);
            }else{
                model.setIsCaptain("yes");
                is_captain = 1;
                return true;
                //selectCVCAdapter.updateList(playerModelList);
            }
        }else{
            if(model.getIsWiseCaptain().equals("yes")){
                model.setIsWiseCaptain("no");
                is_vise_captain=0;
                for (int i = 0; i < playerModelList.size(); i++) {
                    if (playerModelList.get(i).getIsCaptain().equals("yes")) {
                        playerModelList.get(i).setIsCaptain("no");
                        selectCVCAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                model.setIsCaptain("yes");
                is_captain = 1;
                return true;
//                selectCVCAdapter.notifyItemChanged();
            }else{
                for (int i = 0; i < playerModelList.size(); i++) {
                    if (playerModelList.get(i).getIsCaptain().equals("yes")) {
                        playerModelList.get(i).setIsCaptain("no");
                        selectCVCAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                model.setIsCaptain("yes");
                is_captain = 1;
                return true;
                //selectCVCAdapter.updateList(playerModelList);
            }
        }
        *//*if(model.getIs_captain().equals("0")) {
            if (model.getIs_vice_captain().equals("1")) {
                model.setIs_vice_captain("0");
                is_vise_captain = 0;
                model.setIs_captain("1");
                is_captain = 1;
            } else {
                if (is_captain == 1) {
                    for (int i = 0; i < playerModelList.size(); i++) {
                        if (playerModelList.get(i).getIs_captain().equals("1")) {
                            playerModelList.get(i).setIs_captain("0");
                        }
                    }
                    model.setIs_captain("1");
                } else {
                    model.setIs_captain("1");
                    is_captain = 1;
                }
            }*//*

    }

    public boolean check_vise_captain(PlayerModel model){

        if(is_vise_captain==0) {
            if(model.getIsCaptain().equals("yes")){
                model.setIsCaptain("no");
                is_captain=0;
                model.setIsWiseCaptain("yes");
                is_vise_captain = 1;
                return true;
                //selectCVCAdapter.updateList(playerModelList);
            }else{
                model.setIsWiseCaptain("yes");
                is_vise_captain = 1;
                return true;
                //selectCVCAdapter.updateList(playerModelList);
            }
        }else{
            if(model.getIsCaptain().equals("yes")){
                model.setIsCaptain("no");
                is_captain=0;
                for (int i = 0; i < playerModelList.size(); i++) {
                    if (playerModelList.get(i).getIsWiseCaptain().equals("yes")) {
                        playerModelList.get(i).setIsWiseCaptain("no");
                        selectCVCAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                model.setIsWiseCaptain("yes");
                is_vise_captain = 1;
                return true;
//                selectCVCAdapter.notifyItemChanged();
            }else{
                for (int i = 0; i < playerModelList.size(); i++) {
                    if (playerModelList.get(i).getIsWiseCaptain().equals("yes")) {
                        playerModelList.get(i).setIsWiseCaptain("no");
                        selectCVCAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                model.setIsWiseCaptain("yes");
                is_vise_captain = 1;
                return true;
                //selectCVCAdapter.updateList(playerModelList);
            }
        }
        *//*if(model.getIs_vice_captain().equals("0")){
            if(model.getIs_captain().equals("1")){
                model.setIs_captain("0");
                is_captain =0;
                model.setIs_vice_captain("1");
                is_vise_captain = 1;
            }else{
                if(is_vise_captain == 1){
                    for(int i=0;i<playerModelList.size();i++){
                        if(playerModelList.get(i).getIs_vice_captain().equals("1")){
                            playerModelList.get(i).setIs_vice_captain("0");
                        }
                    }
                    model.setIs_vice_captain("1");
                }else{
                    model.setIs_vice_captain("1");
                    is_vise_captain = 1;
                }
            }*//*
    }*/
