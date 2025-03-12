package com.fantafeat.Activity;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantafeat.Fragment.CommentaryFragment;
import com.fantafeat.Fragment.MatchMyContestFragment;
import com.fantafeat.Fragment.MatchMyTeamsFragment;
import com.fantafeat.Fragment.MatchPlayerStatsFragment;
import com.fantafeat.Fragment.ScorecardFragment;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.Model.ScoreModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.OnFragmentInteractionListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class AfterMatchActivity extends BaseActivity implements OnFragmentInteractionListener {

    public TabLayout joined_team_tab;
    LinearLayout layDesc,layIn2te1,layIn2te2,layBat,layBowl;
    ViewPager2 viewPager;
    private final List<String> mFragmentTitleList = new ArrayList<>();
    MatchMyContestFragment matchMyContestFragment;
    MatchPlayerStatsFragment matchPlayerStatsFragment;
    MatchMyTeamsFragment matchMyTeamsFragment;
    ScorecardFragment scoreCardFragment;
    CommentaryFragment commentaryFragment;
    TextView match_title,match_status,team1_name,team2_name,inning1_score_team1,inning1_over_team1
            ,inning1_score_team2,inning1_over_team2,inning2_score_team1,inning2_over_team1,
            inning2_score_team2,team2_full_name,inning2_over_team2,team1_full_name,match_desc,txt_batsman1,txt_batsman2,
            txt_batsman1_score,txt_batsman2_score,txtBowlerName,txtBowlerDesc,txtDate;

    ImageView toolbar_back;
    View viewBat;
    CircleImageView team1_img,team2_img;
    long diff;
    Timer timer;
   // private Socket mSocket= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_match);
        Window window = AfterMatchActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.blackPrimary));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
    }

    public void initClick() {
        joined_team_tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (preferences.getMatchModel().getSportId().equals("1") &&
                        preferences.getMatchModel().getShowScorecard().equalsIgnoreCase("yes")){
                    if (tab.getPosition() == 0) {
                        /*if (matchMyContestFragment != null) {
                            if (matchMyContestFragment.selectedTag.equalsIgnoreCase("1")){
                                matchMyContestFragment.getData();
                            }else {
                                matchMyContestFragment.getSinglesData();
                            }


                        }*/
                    } else if (tab.getPosition() == 1) {
                      /*  if (matchMyTeamsFragment != null) {
                            matchMyTeamsFragment.getTempTeamData();
                        }*/
                    }else if (tab.getPosition() == 2) {
                        if (MyApp.getClickStatus()){
                           /* if (commentaryFragment != null) {
                                commentaryFragment.setComData();
                            }*/
                        }

                    }else if (tab.getPosition() == 3) {
                        if (MyApp.getClickStatus()){
                            /*if (scoreCardFragment != null) {
                                scoreCardFragment.setScoreData();
                            }*/
                        }

                    } else if (tab.getPosition() == 4) {
                       /* if (matchPlayerStatsFragment != null) {
                            matchPlayerStatsFragment.getTeamData();
                        }*/
                    }
                }else {
                    if (tab.getPosition() == 0) {
                        //if (matchMyContestFragment != null) {
                            /*if (matchMyContestFragment.contestModelList==null){
                                matchMyContestFragment.contestModelList=new ArrayList<>();
                            }
                            matchMyContestFragment.contestModelList.clear();*/
                            //matchMyContestFragment.getData();
                       // }
                    } else if (tab.getPosition() == 1) {
                      /*  if (matchMyTeamsFragment != null) {
                            matchMyTeamsFragment.getTempTeamData();
                        }*/
                    } else if (tab.getPosition() == 2) {
                      /*  if (matchPlayerStatsFragment != null) {
                            matchPlayerStatsFragment.getTeamData();
                        }*/
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
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        joined_team_tab = findViewById(R.id.joined_team_tab);
        txtDate = findViewById(R.id.txtDate);
        viewPager = findViewById(R.id.viewPager);
        match_title = findViewById(R.id.match_title);
        match_title.setSelected(true);
        match_status = findViewById(R.id.match_status);
        toolbar_back = findViewById(R.id.toolbar_back);
        layDesc = findViewById(R.id.layDesc);

        team1_name = findViewById(R.id.team1_name);
        team2_name = findViewById(R.id.team2_name);
        viewBat = findViewById(R.id.viewBat);
        inning1_score_team1 = findViewById(R.id.inning1_score_team1);
        inning1_over_team1 = findViewById(R.id.inning1_over_team1);
        inning1_score_team2 = findViewById(R.id.inning1_score_team2);
        inning1_over_team2 = findViewById(R.id.inning1_over_team2);
        inning2_score_team1 = findViewById(R.id.inning2_score_team1);
        inning2_over_team1 = findViewById(R.id.inning2_over_team1);
        inning2_score_team2 = findViewById(R.id.inning2_score_team2);
        inning2_over_team2 = findViewById(R.id.inning2_over_team2);
        layIn2te1 = findViewById(R.id.layIn2te1);
        layIn2te2 = findViewById(R.id.layIn2te2);



        team2_full_name = findViewById(R.id.team2_full_name);
        team2_full_name.setSelected(true);
        team1_full_name = findViewById(R.id.team1_full_name);
        team1_full_name.setSelected(true);
        team1_img = findViewById(R.id.team1_img);
        team2_img = findViewById(R.id.team2_img);
        match_desc = findViewById(R.id.match_desc);
        match_desc.setSelected(true);
        layBat = findViewById(R.id.layBat);
        txt_batsman1 = findViewById(R.id.txt_batsman1);
        txt_batsman1.setSelected(true);
        txt_batsman2 = findViewById(R.id.txt_batsman2);
        txt_batsman2.setSelected(true);
        txt_batsman1_score = findViewById(R.id.txt_batsman1_score);
        txt_batsman1_score.setSelected(true);
        txt_batsman2_score = findViewById(R.id.txt_batsman2_score);
        txt_batsman2_score.setSelected(true);
        txtBowlerName = findViewById(R.id.txtBowlerName);
        txtBowlerName.setSelected(true);
        txtBowlerDesc = findViewById(R.id.txtBowlerDesc);
        txtBowlerDesc.setSelected(true);
        layBowl = findViewById(R.id.layBowl);

        if (preferences.getMatchModel().getSportId().equals("1") &&
                preferences.getMatchModel().getShowScorecard().equalsIgnoreCase("yes")){

            mFragmentTitleList.add("My Contests");
            mFragmentTitleList.add("My Teams");
            mFragmentTitleList.add("Commentary");
            mFragmentTitleList.add("Scorecard");
            mFragmentTitleList.add("Stats");

            layBat.setVisibility(View.VISIBLE);
            viewBat.setVisibility(View.VISIBLE);


        }else {
            mFragmentTitleList.add("My Contests");
            mFragmentTitleList.add("My Teams");
            mFragmentTitleList.add("Player Stats");

            layBat.setVisibility(View.GONE);
            viewBat.setVisibility(View.GONE);

        }
        if (preferences.getMatchModel().getSportId().equals("1") &&
                preferences.getMatchModel().getShowScorecard().equalsIgnoreCase("yes")){
           // viewPager.setOffscreenPageLimit(5);
            joined_team_tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        }else {
            joined_team_tab.setTabMode(TabLayout.MODE_FIXED);
           // viewPager.setOffscreenPageLimit(3);
        }

        viewPager.setAdapter(createAdapter());
        viewPager.setOffscreenPageLimit(3);
        new TabLayoutMediator(joined_team_tab, viewPager,
                (tab, position) -> tab.setText(mFragmentTitleList.get(position))).attach();
        viewPager.setUserInputEnabled(false);

        initClick();

        getTeamData();

        String matchDate = "";
        String matchTime = "";
        Date date = null;

        SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat matchFormate = MyApp.changedFormat("dd-MMM-yy");
        SimpleDateFormat matchTimeFormate = MyApp.changedFormat("hh:mm a");
        try {
            date = format.parse(preferences.getMatchModel().getRegularMatchStartTime());

            matchTime = matchTimeFormate.format(date);
            matchDate = matchFormate.format(date);

            diff = date.getTime() - MyApp.getCurrentDate().getTime();

        }
        catch (ParseException e) {
            LogUtil.e(TAG, "onBindViewHolder: " + e.toString());
            e.printStackTrace();
        }
        String strDate=matchDate + " " + matchTime+"";

        match_title.setText(preferences.getMatchModel().getMatchTitle());//preferences.getMatchModel().getTeam1Sname()+" vs "+preferences.getMatchModel().getTeam2Sname()
        match_status.setText(preferences.getMatchModel().getMatchStatus());
        txtDate.setText(strDate);

        if(preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("live")) {
            match_status.setCompoundDrawablesRelativeWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_red_dot),null,null,null);
        }
        else if(preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("in review")){
            match_status.setCompoundDrawablesRelativeWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_orange_dot),null,null,null);
        }
        else if(preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("completed")){
            match_status.setCompoundDrawablesRelativeWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_green_dot),null,null,null);
        }
        else if(preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("Cancelled")) {
            match_status.setCompoundDrawablesRelativeWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_red_dot),null,null,null);
        }
        team1_full_name.setText(preferences.getMatchModel().getTeam1Name());
        team2_full_name.setText(preferences.getMatchModel().getTeam2Name());
        team1_name.setText(preferences.getMatchModel().getTeam1Sname());
        team2_name.setText(preferences.getMatchModel().getTeam2Sname());

        CustomUtil.loadImageWithGlide(mContext,team1_img,ApiManager.TEAM_IMG,preferences.getMatchModel().getTeam1Img(),R.drawable.team_loading);
        CustomUtil.loadImageWithGlide(mContext,team2_img,ApiManager.TEAM_IMG,preferences.getMatchModel().getTeam2Img(),R.drawable.team_loading);
       /* if (preferences.getMatchModel().getTeam1Img().equals("")) {
            Glide.with(mContext)
                    .load(R.drawable.team_loading)
                    .placeholder(R.drawable.team_loading)
                    .error(R.drawable.team_loading)
                    .into(team1_img);
        } else if (URLUtil.isValidUrl(preferences.getMatchModel().getTeam1Img())) {
            Glide.with(mContext)
                    .load(preferences.getMatchModel().getTeam1Img())
                    .placeholder(R.drawable.team_loading)
                    .error(R.drawable.team_loading)
                    .into(team1_img);
        } else {
            Glide.with(mContext)
                    .load(ApiManager.TEAM_IMG + preferences.getMatchModel().getTeam1Img())
                    .placeholder(R.drawable.team_loading)
                    .error(R.drawable.team_loading)
                    .into(team1_img);
        }

        if (preferences.getMatchModel().getTeam2Img().equals("")) {
            Glide.with(mContext)
                    .load(R.drawable.team_loading)
                    .placeholder(R.drawable.team_loading)
                    .error(R.drawable.team_loading)
                    .into(team2_img);
        } else if (URLUtil.isValidUrl(preferences.getMatchModel().getTeam2Img())) {
            Glide.with(mContext)
                    .load(preferences.getMatchModel().getTeam2Img())
                    .placeholder(R.drawable.team_loading)
                    .error(R.drawable.team_loading)
                    .into(team2_img);
        } else {
            Glide.with(mContext)
                    .load(ApiManager.TEAM_IMG + preferences.getMatchModel().getTeam2Img())
                    .placeholder(R.drawable.team_loading)
                    .error(R.drawable.team_loading)
                    .into(team2_img);
        }*/
        if (TextUtils.isEmpty(preferences.getMatchModel().getMatchDesc())){
            layDesc.setVisibility(View.GONE);
        }else {
            layDesc.setVisibility(View.VISIBLE);
            match_desc.setText(preferences.getMatchModel().getMatchDesc());
        }

        if (preferences.getMatchModel().getSportId().equals("1") &&
                preferences.getMatchModel().getShowScorecard().equalsIgnoreCase("yes")){
            if(preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("playing") &&
                    preferences.getUpdateMaster().getIs_score_card_refresh().equalsIgnoreCase("yes")) {
                if (timer!=null){
                    timer.cancel();
                }
                final Handler handler = new Handler();
                timer = new Timer();
                TimerTask doAsynchronousTask = new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(() -> {
                            try {
                                getScore();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                };
                timer.schedule(doAsynchronousTask, 0, 10000);

            }else {
                getScore();
            }
        }
        else {
            layBat.setVisibility(View.GONE);
            viewBat.setVisibility(View.GONE);

            updateScoreApi();
        }
        updateScore();

    }

    public void updateScore(){
        if (preferences.getMatchModel().getMatchType().equalsIgnoreCase("test")) {
            layIn2te1.setVisibility(View.VISIBLE);
            layIn2te2.setVisibility(View.VISIBLE);
            String t1i1s=preferences.getMatchModel().getTeam1Inn1Score().replace(" ","\n");
            String t2i1s=preferences.getMatchModel().getTeam2Inn1Score().replace(" ","\n");
            String t1i2s=preferences.getMatchModel().getTeam1Inn2Score().replace(" ","\n");
            String t2i2s=preferences.getMatchModel().getTeam2Inn2Score().replace(" ","\n");

            if (TextUtils.isEmpty(t1i1s)){
                inning1_score_team1.setText("0");
            }else {
                inning1_score_team1.setText(t1i1s);
            }
            if (TextUtils.isEmpty(t2i1s)){
                inning1_score_team2.setText("0");
            }else {
                inning1_score_team2.setText(t2i1s);
            }
            //inning1_score_team1.setText(t1i1s);
            //inning1_score_team2.setText(t2i1s);
            if (TextUtils.isEmpty(t1i2s)){
                inning2_score_team1.setText("0");
            }else {
                inning2_score_team1.setText(t1i2s);
            }
            if (TextUtils.isEmpty(t2i2s)){
                inning2_score_team2.setText("0");
            }else {
                inning2_score_team2.setText(t2i2s);
            }
            //inning2_score_team1.setText(t1i2s);
            //inning2_score_team2.setText(t2i2s);
        }
        else {
            String t1i1s=preferences.getMatchModel().getTeam1Inn1Score().replace(" ","\n");
            String t2i1s=preferences.getMatchModel().getTeam2Inn1Score().replace(" ","\n");

            if(preferences.getMatchModel().getSportId().equals("1")){
                if (TextUtils.isEmpty(t1i1s)){
                    inning1_score_team1.setText("0");
                }else {
                    inning1_score_team1.setText(t1i1s);
                }
                if (TextUtils.isEmpty(t2i1s)){
                    inning1_score_team2.setText("0");
                }else {
                    inning1_score_team2.setText(t2i1s);
                }
                //inning1_score_team2.setText(t2i1s); android:background="@drawable/white_round_shape"
                //                            android:textColor="@color/textPrimary"
            }else {//if(preferences.getMatchModel().getSportId().equals("2"))
                inning1_score_team1.setBackgroundResource(R.drawable.white_round_shape);
                inning1_score_team1.setTextColor(getResources().getColor(R.color.textPrimary));
                inning1_score_team2.setBackgroundResource(R.drawable.white_round_shape);
                inning1_score_team2.setTextColor(getResources().getColor(R.color.textPrimary));
                inning1_score_team1.setHeight(50);
                inning1_score_team1.setWidth(80);
                inning1_score_team2.setHeight(50);
                inning1_score_team2.setWidth(80);
                if (TextUtils.isEmpty(t1i1s)){
                    inning1_score_team1.setText("0");
                }else {
                    inning1_score_team1.setText(t1i1s);
                }
                if (TextUtils.isEmpty(t2i1s)){
                    inning1_score_team2.setText("0");
                }else {
                    inning1_score_team2.setText(t2i1s);
                }
            }

            layIn2te1.setVisibility(View.GONE);
            layIn2te2.setVisibility(View.GONE);

        }
    }

    private void getScore() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id",preferences.getMatchModel().getId());
            jsonObject.put("user_id",preferences.getUserModel().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.e(TAG, "onSuccessResult param: " + jsonObject.toString() );

        HttpRestClient.postJSONWithParam(mContext, false, ApiManager.SCORE_CARD, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                try {
                    LogUtil.e(TAG, "onSuccessResult Score: " + responseBody.toString() );
                    if(responseBody.optBoolean("status")){

                        JSONObject data=responseBody.optJSONObject("data");
                        if (data!=null){
                            JSONArray innings=data.optJSONArray("innings");
                            if (innings!=null && innings.length()>0){
                                ConstantUtil.score_list = new ArrayList<>();

                                for (int i=0;i<innings.length();i++){
                                    ScoreModel modal= gson.fromJson(innings.optJSONObject(i).toString(),ScoreModel.class);
                                    ConstantUtil.score_list.add(modal);
                                }

                                updateMainScore(ConstantUtil.score_list);

                               /* if (commentaryFragment != null) {
                                    commentaryFragment.setComData();
                                }

                                if (scoreCardFragment != null) {
                                    scoreCardFragment.setScoreData();
                                }*/

                            }else {
                                ConstantUtil.score_list = new ArrayList<>();
                            }
                        }else {
                            ConstantUtil.score_list = new ArrayList<>();
                        }
                    }else {
                        ConstantUtil.score_list = new ArrayList<>();
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer!=null)
            timer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*mSocket = MyApp.getInstance().getSocketInstance();
        if (mSocket!=null){
            if (!mSocket.connected())
                mSocket.connect();
            try {
                JSONObject obj=new JSONObject();
                if (preferences.getUserModel()!=null){
                    obj.put("user_id",preferences.getUserModel().getId());
                }
                obj.put("title","scorecard");
                mSocket.emit("connect_user",obj);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }*/
    }

    private void updateScoreApi() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id",preferences.getMatchModel().getId());
            jsonObject.put("user_id",preferences.getUserModel().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, false, ApiManager.GET_MATCH_SCORE, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "matchScore: " + responseBody.toString() );
                if(responseBody.optBoolean("status")){
                    MatchModel matchModel = preferences.getMatchModel();
                    JSONObject data = responseBody.optJSONObject("data");
                    matchModel.setTeam1Inn1Score(data.optString("team_1_inn_1_score"));
                    matchModel.setTeam1Inn2Score(data.optString("team_1_inn_2_score"));
                    matchModel.setTeam2Inn1Score(data.optString("team_2_inn_1_score"));
                    matchModel.setTeam2Inn2Score(data.optString("team_2_inn_2_score"));
                    MyApp.getMyPreferences().setMatchModel(matchModel);
                    //((AfterMatchActivity)mContext).updateScore();

                    updateScore();

                   /* if (preferences.getMatchModel().getMatchType().equalsIgnoreCase("test")) {
                        inning2_score_team1.setVisibility(View.VISIBLE);
                        inning2_score_team2.setVisibility(View.VISIBLE);

                        inning1_score_team1.setText(preferences.getMatchModel().getTeam1Inn1Score());
                        inning2_score_team1.setText(preferences.getMatchModel().getTeam1Inn2Score());
                        inning1_score_team2.setText(preferences.getMatchModel().getTeam2Inn1Score());
                        inning2_score_team2.setText(preferences.getMatchModel().getTeam2Inn2Score());
                    } else {
                        inning1_score_team1.setText(preferences.getMatchModel().getTeam1Inn1Score());
                        inning2_score_team1.setText(preferences.getMatchModel().getTeam1Inn2Score());
                        inning1_score_team2.setText(preferences.getMatchModel().getTeam2Inn1Score());
                        inning2_score_team2.setText(preferences.getMatchModel().getTeam2Inn2Score());

                        inning2_score_layout.setVisibility(View.GONE);
                        inning4_score_layout.setVisibility(View.GONE);
                    }*/
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    public void updateMainScore(List<ScoreModel> scoreModelList) {

        if (scoreModelList.size()>0 &&
                !preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("Cancelled")){
            String batMan1="-",batMan2="-",strBowler="-",strBowlerDesc="-";
            String batMan1Score="-",batMan2Score="-";
            int idx = 0;

            List<ScoreModel> list=new ArrayList<>(scoreModelList);
            Collections.reverse(list);

            ScoreModel scoreModel=list.get(0);

            for (int j = 0; j < scoreModel.getBatsmen().size(); j++) {
                ScoreModel.Batsman batsman =scoreModel.getBatsmen().get(j);
                if (batsman.getHowOut().equalsIgnoreCase("Not out")){
                    if (idx==0){
                        batMan1=batsman.getName();
                        batMan1Score=batsman.getRuns() + " (" + batsman.getBallsFaced() + ")";
                        idx++;
                    }else if (idx==1){
                        batMan2=batsman.getName();
                        batMan2Score=batsman.getRuns() + " (" + batsman.getBallsFaced() + ")";
                    }
                }
            }

            List<ScoreModel.Commentary> commentaries=new ArrayList<>(scoreModel.getCommentaries());

            if (commentaries.size()>0){
                Collections.reverse(commentaries);

                ScoreModel.Commentary modelCom = commentaries.get(0);
                ScoreModel.Commentary.BallScore modelOver = modelCom.getBallScore().get(0) ;

                for (int j = 0; j < scoreModel.getBowlers().size(); j++) {
                    ScoreModel.Bowler bowler=scoreModel.getBowlers().get(j);
                    if (modelOver.getBowlerId().equalsIgnoreCase(bowler.getBowlerId())){
                        strBowler = bowler.getName();
                        strBowlerDesc =  bowler.getWickets()+"/"+
                                bowler.getRunsConceded()+" ("+bowler.getOvers()+")";

                    }
                }

                displayBowl(modelCom);

                txtBowlerDesc.setText(strBowlerDesc);
                txtBowlerName.setText(strBowler);

                txt_batsman1.setText(batMan1);
                txt_batsman2.setText(batMan2);

                txt_batsman1_score.setText(batMan1Score);
                txt_batsman2_score.setText(batMan2Score);

                if (batMan1.equalsIgnoreCase("-") && batMan2.equalsIgnoreCase("-") &&
                        batMan1Score.equalsIgnoreCase("-") && batMan2Score.equalsIgnoreCase("-")){
                    layBat.setVisibility(View.GONE);
                }else {
                    layBat.setVisibility(View.VISIBLE);
                }
            }else {
                layBat.setVisibility(View.GONE);
            }
        }
        else {
            txtBowlerDesc.setText("-");
            txtBowlerName.setText("-");

            txt_batsman1.setText("-");
            txt_batsman2.setText("-");

            txt_batsman1_score.setText("-");
            txt_batsman2_score.setText("-");

            layBat.setVisibility(View.GONE);
        }

        updateScoreApi();
    }

    private void displayBowl( ScoreModel.Commentary bowlCntModel) {

        LinearLayout.LayoutParams paramstv=new LinearLayout.LayoutParams(50,50);

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(55,55);
        params.setMargins(5,0,5,5);

        layBowl.removeAllViews();

        for (int i=0;i<bowlCntModel.getBallScore().size();i++){
            ScoreModel.Commentary.BallScore ball=bowlCntModel.getBallScore().get(i);

            LinearLayout lv=new LinearLayout(mContext);
            lv.setLayoutParams(params);
            lv.setGravity(Gravity.CENTER);

            TextView tv=new TextView(mContext);
            tv.setLayoutParams(paramstv);
            tv.setText(ball.getScore());
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(mContext.getResources().getColor(R.color.white_pure));
            tv.setTextSize(12f);
            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.roboto_regular);
            tv.setTypeface(typeface, Typeface.BOLD);

          /*  anim = ObjectAnimator.ofPropertyValuesHolder(
                    tv,
                    PropertyValuesHolder.ofFloat("scaleX", 1.6f),
                    PropertyValuesHolder.ofFloat("scaleY", 1.6f));
            anim.setDuration(310);
            anim.setRepeatCount(ObjectAnimator.INFINITE);
            anim.setRepeatMode(ObjectAnimator.REVERSE);

            if (ball.getScore().equalsIgnoreCase("4")){
                anim.start();
            }else if (ball.getScore().equalsIgnoreCase("6")){
                anim.start();
            } else if (ball.getScore().equalsIgnoreCase("w")){
                anim.start();
            } else if (Integer.parseInt(ball.getRun())>6){
                anim.start();
            }*/

            lv.setBackgroundResource(ConstantUtil.getColorCode(ball.getScore()));

            lv.addView(tv);
            layBowl.addView(lv);

        }

        /*for (int i=0;i<bowlCntModel.getBallScore().size();i++){

            ScoreModel.Commentary.BallScore ball = bowlCntModel.getBallScore().get(i);

            TextView tv=new TextView(mContext);
            tv.setLayoutParams(params);

            tv.setText(ball.getScore());

            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(mContext.getResources().getColor(R.color.white));
            tv.setTextSize(13f);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setBackgroundResource(R.drawable.black_circul_fill);

            layBowl.addView(tv);
        }*/
    }

    private void getTeamData() {
        final ArrayList<PlayerModel> playerModels = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("user_id", preferences.getUserModel().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, false, ApiManager.MATCH_DETAIL_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "TEMP_TEAM_LIST: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {

                    JSONArray array = responseBody.optJSONArray("data");
                    int i = 0;
                    for (i = 0; i < array.length(); i++) {
                        try {
                            PlayerModel playerModel = gson.fromJson(array.getJSONObject(i).toString(), PlayerModel.class);
                            playerModels.add(playerModel);
                            preferences.setPlayerList(playerModels);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
               // getTempTeamDetailData();
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private ViewPagerAdapter createAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        return adapter;
    }

    @Override
    public void onFragmentAction(List<ScoreModel> scoreModelList) {
        LogUtil.e("resp","onFragmentAction: "+scoreModelList.size());
        updateMainScore(scoreModelList);
    }

    public class ViewPagerAdapter extends FragmentStateAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        /*public ViewPagerAdapter(@NonNull FragmentActivity fragment) {
            super(fragment);
        }*/

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (preferences.getMatchModel().getSportId().equals("1") &&
                    preferences.getMatchModel().getShowScorecard().equalsIgnoreCase("yes")){
                if (position == 0) {
                    return  MatchMyContestFragment.newInstance();
                } else if (position == 1) {
                    return MatchMyTeamsFragment.newInstance();
                }else if (position == 2) {
                    return  CommentaryFragment.newInstance();
                }else if (position == 3) {
                    return  ScorecardFragment.newInstance();
                } else if (position == 4) {
                    return  MatchPlayerStatsFragment.newInstance();
                }
            }else {
                if (position == 0) {
                    return   MatchMyContestFragment.newInstance();
                } else if (position == 1) {
                    return  MatchMyTeamsFragment.newInstance();
                } else if (position == 2) {
                    return  MatchPlayerStatsFragment.newInstance();
                }
            }

            return null;
        }

        @Override
        public int getItemCount() {
            if (preferences.getMatchModel().getSportId().equals("1") &&
                    preferences.getMatchModel().getShowScorecard().equalsIgnoreCase("yes")){
                return 5;
            }else {
                return 3;
            }

        }

    }
}