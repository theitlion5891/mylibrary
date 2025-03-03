package com.fantafeat.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantafeat.Activity.ContestListActivity;
import com.fantafeat.Activity.CricketSelectPlayerActivity;
import com.fantafeat.Adapter.MyTeamListAdapter;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.PlayerListModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.PrefConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyTeamFragment extends BaseFragment {

    SwipeRefreshLayout my_team_contest_refresh;
    private RecyclerView myTeamList;
    MyTeamListAdapter adapter;
    ContestListActivity contestListActivity;
    LinearLayout layNoData,create_team_btn;
    ImageView imgPlace;
    LinearLayout create_team;
    List<PlayerListModel> playerListModels ;

    public MyTeamFragment() {
       // super();
        //this.contestListActivity = contestListActivity;
    }

    public static MyTeamFragment newInstance() {
        return new MyTeamFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        ConstantUtil.isTimeOverShow=false;

        if (playerListModels!=null){
            playerListModels.clear();
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        getTempTeamData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_team, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initControl(View view) {
        myTeamList = view.findViewById(R.id.my_team_list);
        my_team_contest_refresh = view.findViewById(R.id.my_team_contest_refresh);
        layNoData = view.findViewById(R.id.layNoData);
        imgPlace = view.findViewById(R.id.imgPlace);
        create_team = view.findViewById(R.id.create_team);
        create_team_btn = view.findViewById(R.id.create_team_btn);
    }

    @Override
    public void initClick() {
        my_team_contest_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTempTeamData();
            }
        });

        create_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CricketSelectPlayerActivity.class);
                intent.putExtra(PrefConstant.TEAMCREATETYPE, 0);
                startActivity(intent);
            }
        });

        create_team_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CricketSelectPlayerActivity.class);
                intent.putExtra(PrefConstant.TEAMCREATETYPE, 0);
                startActivity(intent);
            }
        });
    }

    public void getTempTeamData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("con_display_type", preferences.getMatchModel().getConDisplayType());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean isShow = true;
        if (my_team_contest_refresh != null && my_team_contest_refresh.isRefreshing()) {
           // my_team_contest_refresh.setRefreshing(false);
            isShow = false;
        }
        HttpRestClient.postJSON(mContext, isShow, ApiManager.TEMP_TEAM_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                if (my_team_contest_refresh != null && my_team_contest_refresh.isRefreshing()) {
                    my_team_contest_refresh.setRefreshing(false);
                }
                LogUtil.e(TAG, "onSuccessResult: TEMP_TEAM_LIST " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONArray array = responseBody.optJSONArray("data");
                    if (array.length() > 0) {
                       // nodata.setVisibility(View.GONE);
                        create_team_btn.setVisibility(View.VISIBLE);
                        myTeamList.setVisibility(View.VISIBLE);
                        getTempTeamDetailData(responseBody.optJSONArray("data"));
                    }
                    else{
                       // nodata.setVisibility(View.VISIBLE);
                        myTeamList.setVisibility(View.GONE);
                        create_team_btn.setVisibility(View.GONE);
                        switch (preferences.getMatchModel().getSportId()+""){
                            case "1":
                                myTeamList.setVisibility(View.GONE);
                                layNoData.setVisibility(View.VISIBLE);
                                imgPlace.setImageResource(R.drawable.cricket_placeholder);
                                break;
                            case "2":
                                myTeamList.setVisibility(View.GONE);
                                layNoData.setVisibility(View.VISIBLE);
                                imgPlace.setImageResource(R.drawable.football_placeholder);
                                break;
                            case "3":
                                myTeamList.setVisibility(View.GONE);
                                layNoData.setVisibility(View.VISIBLE);
                                imgPlace.setImageResource(R.drawable.baseball_placeholder);
                                break;
                            case "4":
                                myTeamList.setVisibility(View.GONE);
                                layNoData.setVisibility(View.VISIBLE);
                                imgPlace.setImageResource(R.drawable.basketball_placeholder);
                                break;
                            case "5":
                                myTeamList.setVisibility(View.GONE);
                                layNoData.setVisibility(View.VISIBLE);
                                imgPlace.setImageResource(R.drawable.vollyball_placeholder);
                                break;
                            case "6"://6
                                myTeamList.setVisibility(View.GONE);
                                layNoData.setVisibility(View.VISIBLE);
                                imgPlace.setImageResource(R.drawable.handball_placeholder);
                                break;
                            case "7"://6
                                myTeamList.setVisibility(View.GONE);
                                layNoData.setVisibility(View.VISIBLE);
                                imgPlace.setImageResource(R.drawable.kabaddi_placeholder);
                                break;
                        }
                    }
                }

            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                if (my_team_contest_refresh != null && my_team_contest_refresh.isRefreshing()) {
                    my_team_contest_refresh.setRefreshing(false);
                }
            }
        });
    }

    private void getTempTeamDetailData(final JSONArray data) {
         playerListModels = new ArrayList<>();
        ((ContestListActivity)mContext).playerListModels = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("con_display_type", preferences.getMatchModel().getConDisplayType());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, false, ApiManager.TEMP_TEAM_DETAIL_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (my_team_contest_refresh != null && my_team_contest_refresh.isRefreshing()) {
                    my_team_contest_refresh.setRefreshing(false);
                }
                LogUtil.e(TAG, "onSuccessResult: My team" + responseBody.toString());
                if (responseBody.optBoolean("status")) {

                    int i = 0, j = 0;
                    JSONArray array = responseBody.optJSONArray("data");
                    MatchModel matchModel=preferences.getMatchModel();
                    for (j = 0; j < data.length(); j++) {
                        int wk = 0, bat = 0, ar = 0, bowl = 0, team1 = 0, team2 = 0, lineup_count = 0,cr=0;
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

                            playerListModel.setOther_text(playerDetail.getOther_text());
                            playerListModel.setOther_text2(playerDetail.getOther_text2());

                            if (playerDetail.getTempTeamId().equals(playerListModel.getId())) {

                                credit_point += CustomUtil.convertFloat(playerDetail.getPlayerRank());
                                if (matchModel.getSportId().equals("1")){
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
                                if (matchModel.getSportId().equals("2")){
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
                                if (matchModel.getSportId().equals("7")){
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
                                if (matchModel.getSportId().equals("4")){
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
                                if (matchModel.getSportId().equals("3")){
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
                                if (playerDetail.getTeamId().equals(matchModel.getTeam1())) {
                                    team1 += 1;
                                }
                                if (playerDetail.getTeamId().equals(matchModel.getTeam2())) {
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
                                    playerListModel.setVCTeam_id(playerDetail.getTeamId());//main.optString("team_id")
                                }

                                if (preferences.getPlayerList() != null) {
                                    for (PlayerModel playerModel : preferences.getPlayerList()) {
                                        if (playerModel.getPlayerId().equalsIgnoreCase(playerDetail.getPlayerId())) {
                                            playerDetail.setPlayingXi(playerModel.getPlayingXi());
                                            playerDetail.setOther_text(playerModel.getOther_text());
                                            playerDetail.setOther_text2(playerModel.getOther_text2());
                                            break;
                                        }
                                    }
                                }

                                if (matchModel.getTeamAXi().toLowerCase().equals("yes") ||
                                        matchModel.getTeamBXi().toLowerCase().equals("yes")) {
                                    //LogUtil.e(TAG, "onSuccessResult: " + playerDetail.getPlayingXi());

                                    if (playerDetail.getPlayingXi().toLowerCase().equals("no") &&
                                            !playerDetail.getOther_text().equalsIgnoreCase("substitute")) {
                                        lineup_count++;
                                       // LogUtil.e(TAG, "onSuccessResult: ABC " + playerDetail.getPlayerName());
                                    }
                                }

                               // LogUtil.e(TAG, "onSuccessResult: set Team");

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
                        //LogUtil.e(TAG, "onSuccessResult: " + lineup_count);
                        playerListModel.setLineup_count("" + lineup_count);
                        playerListModel.setTeam1_sname(matchModel.getTeam1Sname());
                        playerListModel.setTeam2_sname(matchModel.getTeam2Sname());
                        playerListModels.add(playerListModel);

                    }
                    preferences.setPlayerModel(playerListModels);

                    ((ContestListActivity)mContext).playerListModels.addAll(playerListModels);

                    //LogUtil.d("colDara","   "+((ContestListActivity)mContext).playerListModels.size());
                    if (playerListModels.size() > 0) {
                        ((ContestListActivity)mContext).tabLayout.getTabAt(2).setText("My Teams (" + playerListModels.size() + ")");
                    } else {
                        ((ContestListActivity)mContext).tabLayout.getTabAt(2).setText("My Team");
                    }

                    if (adapter != null) {
                        adapter.updateData(playerListModels);
                    } else {
                        adapter = new MyTeamListAdapter(mContext, playerListModels, 0);
                        myTeamList.setLayoutManager(new LinearLayoutManager(mContext));
                        myTeamList.setAdapter(adapter);
                    }
                }
                //if (adapter!=null){
                    checkData();
                //}

            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void checkData(){
        //LogUtil.d("selSports",preferences.getMatchModel().getSportId()+" ");
        switch (preferences.getMatchModel().getSportId()+""){
            case "1":
                if (playerListModels.size()>0){
                    myTeamList.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    myTeamList.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.cricket_placeholder);
                    //txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
            case "2":
                if (playerListModels.size()>0){
                    myTeamList.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    myTeamList.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.football_placeholder);
                  //  txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
            case "3":
                if (playerListModels.size()>0){
                    myTeamList.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    myTeamList.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.baseball_placeholder);
                }
                break;
            case "4":
                if (playerListModels.size()>0){
                    myTeamList.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    myTeamList.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.basketball_placeholder);
                }
                break;
            case "5":
                if (playerListModels.size()>0){
                    myTeamList.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    myTeamList.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.vollyball_placeholder);
                }
                break;
            case "6"://6
                if (playerListModels.size()>0){
                    myTeamList.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    myTeamList.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.handball_placeholder);
                }
                break;
            case "7"://6
                if (playerListModels.size()>0){
                    myTeamList.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    myTeamList.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.kabaddi_placeholder);
                }
                break;

        }

    }

}