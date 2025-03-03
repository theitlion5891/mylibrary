package com.fantafeat.Activity;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantafeat.Adapter.LeagueJoinAdapter;
import com.fantafeat.Adapter.SwitchTeamAdapter;
import com.fantafeat.Model.PlayerListModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SwipeTeamActivity extends BaseActivity {

    private RecyclerView switch_team_list;
    private LinearLayout switch_team_confirm_btn;
    public String team_id;
    public String contest_id,user_join_team_id,joined_temp_team_id;
    public int selectPosition = -1;
    private SwitchTeamAdapter adapter;
    private ImageView back_img;
    private List<PlayerListModel> playerListModelList;
    private long diff;
    private CountDownTimer countDownTimer;
    private BottomSheetDialog bottomSheetDialog = null;
    private String sport_id = "1";


    public void initClick() {
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        switch_team_confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MyApp.getClickStatus()) {
                    if (selectPosition == -1 || team_id.equals(playerListModelList.get(selectPosition).getId())) {
                        CustomUtil.showTopSneakError(mContext, "Please select another team to switch");
                    } else {
                        submitData();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        selectPosition = -1;
        countDownTimer.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_team);
        Window window = SwipeTeamActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.blackPrimary));

        Intent intent = getIntent();
        team_id = intent.getStringExtra("team_id");
        contest_id = intent.getStringExtra("contest_id");
        user_join_team_id = intent.getStringExtra("user_join_team_id");
        joined_temp_team_id = intent.getStringExtra("joined_temp_team_id");

        sport_id = preferences.getMatchModel().getSportId();//intent.getStringExtra("sport_id");
        setTimer();
        switch_team_list = findViewById(R.id.switch_team_list);
        switch_team_confirm_btn = findViewById(R.id.switch_team_confirm_btn);
        back_img = findViewById(R.id.toolbar_back);

        initClick();
        getTempTeamData();

        /*int i =0;
        String[] selectedTeams;
        selectedTeams = joined_temp_team_id.split(",");

        playerListModelList = new ArrayList<>();

        for(PlayerListModel plm: CustomUtil.emptyIfNull(preferences.getPlayerModel())){
            if(plm.getId().equalsIgnoreCase(team_id)){
                selectPosition = i;
            }
            if (selectedTeams != null) {
                for (int j = 0; j < selectedTeams.length; j++) {
                    if (!plm.getId().equalsIgnoreCase(team_id) && plm.getId().equals(selectedTeams[j])) {
                        plm.setIsSelected("Yes");
                    }
                }
            }
            playerListModelList.add(plm);
            i += 1;
        }*/

       /* adapter = new SwitchTeamAdapter(mContext,playerListModelList,SwipeTeamActivity.this,sport_id);
        switch_team_list.setLayoutManager(new LinearLayoutManager(mContext));
        switch_team_list.setAdapter(adapter);*/
    }

    private void getTempTeamData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("con_display_type", preferences.getMatchModel().getConDisplayType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, false, ApiManager.TEMP_TEAM_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "TEMP_TEAM_LIST: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    getTempTeamDetailData(responseBody.optJSONArray("data"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void getTempTeamDetailData(final JSONArray data) {
        final List<PlayerListModel> playerListModels = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("con_display_type", preferences.getMatchModel().getConDisplayType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, true, ApiManager.TEMP_TEAM_DETAIL_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "TEMP_TEAM_LIST: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {

                    int i = 0, j = 0;
                    JSONArray array = responseBody.optJSONArray("data");
                    for (j = 0; j < data.length(); j++) {
                        int wk = 0, bat = 0, ar = 0, bowl = 0, team1 = 0, team2 = 0,lineup_count = 0,cr=0;
                        float credit_point = 0;
                        JSONObject main = data.optJSONObject(j);

                        PlayerListModel playerListModel = new PlayerListModel();
                        playerListModel.setId(main.optString("id"));
                        playerListModel.setTempTeamName(main.optString("temp_team_name"));
                        playerListModel.setMatchId(main.optString("match_id"));
                        playerListModel.setContestDisplayTypeId(main.optString("contest_display_type_id"));
                        playerListModel.setUserId(main.optString("user_id"));
                        playerListModel.setTotalPoint(main.optString("total_point"));

                        List<PlayerModel> playerDetails = new ArrayList<>();
                        for (i = 0; i < array.length(); i++) {
                            JSONObject object = array.optJSONObject(i);

                            PlayerModel playerDetail = gson.fromJson(object.toString(), PlayerModel.class);

                            if (playerDetail.getTempTeamId().equals(playerListModel.getId())) {

                                credit_point += CustomUtil.convertFloat(playerDetail.getPlayerRank());
                                if (preferences.getMatchModel().getSportId().equals("1")){
                                    switch (playerDetail.getPlayerType().toLowerCase()) {
                                        case "wk":
                                            wk += 1;
                                            break;
                                        case "bat":
                                            bat += 1;
                                            break;
                                        case "ar":
                                            ar += 1;
                                            break;
                                        case "bowl":
                                            bowl += 1;
                                            break;
                                    }
                                }
                                if (preferences.getMatchModel().getSportId().equals("2")){
                                    switch (playerDetail.getPlayerType().toLowerCase()) {
                                        case "gk":
                                            wk += 1;
                                            break;
                                        case "def":
                                            bat += 1;
                                            break;
                                        case "mid":
                                            ar += 1;
                                            break;
                                        case "for":
                                            bowl += 1;
                                            break;
                                    }
                                }
                                if (preferences.getMatchModel().getSportId().equals("3")){
                                    switch (playerDetail.getPlayerType().toLowerCase()) {
                                        case "of":
                                            wk += 1;
                                            break;
                                        case "if":
                                            bat += 1;
                                            break;
                                        case "pit":
                                            ar += 1;
                                            break;
                                        case "cat":
                                            bowl += 1;
                                            break;
                                    }
                                }
                                if (preferences.getMatchModel().getSportId().equals("6")){
                                    switch (playerDetail.getPlayerType().toLowerCase()) {
                                        case "gk":
                                            wk += 1;
                                            break;
                                        case "def":
                                            bat += 1;
                                            break;
                                        case "fwd":
                                            ar += 1;
                                            break;
                                    }
                                }
                                if (preferences.getMatchModel().getSportId().equals("7")){
                                    switch (playerDetail.getPlayerType().toLowerCase()) {
                                        case "def":
                                            wk += 1;
                                            break;
                                        case "ar":
                                            bat += 1;
                                            break;
                                        case "raid":
                                            ar += 1;
                                            break;
                                    }
                                }
                                if (preferences.getMatchModel().getSportId().equals("4")){
                                    switch (playerDetail.getPlayerType().toLowerCase()) {
                                        case "pg":
                                            wk += 1;
                                            break;
                                        case "sg":
                                            bat += 1;
                                            break;
                                        case "sf":
                                            ar += 1;
                                            break;
                                        case "pf":
                                            bowl += 1;
                                            break;
                                        case "cr":
                                            cr += 1;
                                            break;
                                    }
                                }

                                if (playerDetail.getTeamId().equals(preferences.getMatchModel().getTeam1())) {
                                    team1 += 1;
                                }
                                if (playerDetail.getTeamId().equals(preferences.getMatchModel().getTeam2())) {
                                    team2 += 1;
                                }
                                if (playerDetail.getIsCaptain().equalsIgnoreCase("yes")) {
                                    playerListModel.setC_player_name(playerDetail.getPlayerName());
                                    playerListModel.setC_player_sname(playerDetail.getPlayerSname());
                                    playerListModel.setC_player_avg_point(playerDetail.getPlayerAvgPoint());
                                    playerListModel.setC_player_rank(playerDetail.getPlayerRank());
                                    playerListModel.setC_player_img(playerDetail.getPlayerImage());
                                    playerListModel.setC_player_xi(playerDetail.getPlayingXi());
                                    playerListModel.setC_player_type(playerDetail.getPlayerType());
                                    playerListModel.setCTeam_id(playerDetail.getTeamId());
                                }
                                if (playerDetail.getIsWiseCaptain().equalsIgnoreCase("yes")) {
                                    playerListModel.setVc_player_name(playerDetail.getPlayerName());
                                    playerListModel.setVc_player_sname(playerDetail.getPlayerSname());
                                    playerListModel.setVc_player_avg_point(playerDetail.getPlayerAvgPoint());
                                    playerListModel.setVc_player_rank(playerDetail.getPlayerRank());
                                    playerListModel.setVc_player_img(playerDetail.getPlayerImage());
                                    playerListModel.setVc_player_xi(playerDetail.getPlayingXi());
                                    playerListModel.setVc_player_type(playerDetail.getPlayerType());
                                    playerListModel.setVCTeam_id(playerDetail.getTeamId());
                                }

                                if(preferences.getPlayerList() != null){
                                    for(PlayerModel playerModel : preferences.getPlayerList()){
                                        if(playerModel.getPlayerId().equalsIgnoreCase(playerDetail.getPlayerId())){
                                            playerDetail.setPlayingXi(playerModel.getPlayingXi());
                                            playerDetail.setOther_text(playerModel.getOther_text());
                                            playerDetail.setOther_text2(playerModel.getOther_text2());
                                            break;
                                        }
                                    }
                                }
                                if(preferences.getMatchModel().getTeamAXi().toLowerCase().equals("yes") || preferences.getMatchModel().getTeamBXi().toLowerCase().equals("yes")){

                                    if(playerDetail.getPlayingXi().toLowerCase().equals("no") &&
                                            !playerDetail.getOther_text().equalsIgnoreCase("substitute")){
                                        lineup_count++;
                                        LogUtil.e(TAG, "onSuccessResult: ABC "+playerDetail.getPlayerName() );
                                    }
                                }
                                playerDetails.add(playerDetail);
                            }
                        }
                        playerListModel.setPlayerDetailList(playerDetails);
                        playerListModel.setTeam1_count("" + team1);
                        playerListModel.setTeam2_count("" + team2);
                        playerListModel.setCreditTotal("" + credit_point);
                        playerListModel.setWk_count("" + wk);
                        playerListModel.setBat_count("" + bat);
                        playerListModel.setAr_count("" + ar);
                        playerListModel.setBowl_count("" + bowl);
                        playerListModel.setCr_count("" + cr);
                        playerListModel.setIsJoined("No");
                        playerListModel.setIsSelected("No");
                        LogUtil.e(TAG, "onSuccessResult: "+lineup_count );
                        playerListModel.setLineup_count(""+ lineup_count);
                        playerListModel.setTeam1_sname(preferences.getMatchModel().getTeam1Sname());
                        playerListModel.setTeam2_sname(preferences.getMatchModel().getTeam2Sname());
                        playerListModels.add(playerListModel);
                    }
                    preferences.setPlayerModel(playerListModels);
                    //joinDirectContest();
                    setData();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void setData() {

        String[] selectedTeams;
        selectedTeams = joined_temp_team_id.split(",");

        playerListModelList = new ArrayList<>();
        Map<String, List<PlayerListModel>> filterJoining = new HashMap<>();

        for (PlayerListModel list : CustomUtil.emptyIfNull(preferences.getPlayerModel())) {
            if (selectedTeams != null) {
                for (int j = 0; j < selectedTeams.length; j++) {
                    if (list.getId().equals(selectedTeams[j])) {
                        LogUtil.e(TAG, "initControl: selected list");
                        list.setIsSelected("Yes");
                    }
                }
            }
            if (filterJoining.containsKey(list.getIsSelected())) {
                List<PlayerListModel> tempList = filterJoining.get(list.getIsSelected());
                tempList.add(list);
                filterJoining.put(list.getIsSelected(), tempList);
            } else {
                List<PlayerListModel> tempList = new ArrayList<>();
                tempList.add(list);
                filterJoining.put(list.getIsSelected(), tempList);
            }
            playerListModelList.add(list);
        }

        if (filterJoining.containsKey("Yes") || filterJoining.containsKey("No")) {
            playerListModelList = new ArrayList<>();
            if (filterJoining.get("No") != null) {
                playerListModelList.addAll(Objects.requireNonNull(filterJoining.get("No")));
            }
            if (filterJoining.get("Yes") != null) {
                playerListModelList.addAll(Objects.requireNonNull(filterJoining.get("Yes")));
            }

        }

        adapter = new SwitchTeamAdapter(mContext,playerListModelList,SwipeTeamActivity.this,sport_id);
        switch_team_list.setLayoutManager(new LinearLayoutManager(mContext));
        switch_team_list.setAdapter(adapter);

    }

    private void submitData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("temp_team_id",playerListModelList.get(selectPosition).getId());
            jsonObject.put("match_id",preferences.getMatchModel().getId());
            jsonObject.put("con_display_type",preferences.getMatchModel().getConDisplayType());
            jsonObject.put("user_id",preferences.getUserModel().getId());
            jsonObject.put("contest_id",contest_id);
            jsonObject.put("team_name",playerListModelList.get(selectPosition).getTempTeamName());
            jsonObject.put("user_join_team_id",user_join_team_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, true, ApiManager.JOINED_CONTEST_EDIT, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult: " + responseBody);
                if(responseBody.optBoolean("status")){
                    CustomUtil.showTopSneakSuccess(mContext,responseBody.optString("msg"));
                    preferences.setPref("get_player_data",true);
                    //preferences.setPref(PrefConstant.CONTEST_REFRESH,true);
                    finish();
                }else{
                    CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void setTimer() {
        Date date = null;
        String matchDate = "";

        SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat matchFormate = MyApp.changedFormat("dd MMM yyyy");
        try {
            date = format.parse(preferences.getMatchModel().getRegularMatchStartTime());
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
                    diff = new DecimalFormat("00").format(elapsedDays) + "d " + new DecimalFormat("00").format(elapsedHours) + "h Left";
                } else if (elapsedHours > 0) {
                    diff = new DecimalFormat("00").format(elapsedHours) + "h " + new DecimalFormat("00").format(elapsedMinutes) + "m";
                } else {
                    diff = new DecimalFormat("00").format(elapsedMinutes) + "m " + new DecimalFormat("00").format(elapsedSeconds) + "s";
                }
                //timer.setText(diff);
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

    private void timesOver(){
        TextView btn_ok;
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_events, null);
        btn_ok = (TextView) view.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(mContext,HomeActivity.class);
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
        bottomSheetDialog.show();
    }

}