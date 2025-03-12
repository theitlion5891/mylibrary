package com.fantafeat.Fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fantafeat.Activity.AfterMatchActivity;
import com.fantafeat.Adapter.MyTeamListAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class  MatchMyTeamsFragment extends BaseFragment {

    private RecyclerView myTeamList;
    MyTeamListAdapter adapter;
    LinearLayout layNoData;
    SwipeRefreshLayout refresh_my_team;
    private long lastUpdateTime=0;
    private boolean isDataLoaded=false;
   // AfterMatchActivity afterMatchActivity;

    public MatchMyTeamsFragment() {
     //   this.afterMatchActivity = afterMatchActivity;
    }

    public static MatchMyTeamsFragment newInstance(){
        return new MatchMyTeamsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match_my_teams, container, false);
        initFragment(view);

        return view;
    }

    @Override
    public void initControl(View view) {
        myTeamList = view.findViewById(R.id.my_team_list_match);
        refresh_my_team = view.findViewById(R.id.refresh_my_team);
        layNoData = view.findViewById(R.id.layNoData);
        //getTempTeamData();
    }

    @Override
    public void initClick() {
        refresh_my_team.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if ((System.currentTimeMillis()-lastUpdateTime)>= ConstantUtil.Refresh_delay) {
                    getTempTeamData();
                }
                else {
                    refresh_my_team.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onResume() {
        if (isVisible() && !isDataLoaded) {
            isDataLoaded=true;
            getTempTeamData();
        }
        super.onResume();
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
        if (refresh_my_team != null && refresh_my_team.isRefreshing()) {
            isShow = false;
        }
        HttpRestClient.postJSON(mContext, isShow, ApiManager.TEMP_TEAM_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (refresh_my_team != null && refresh_my_team.isRefreshing()) {
                    refresh_my_team.setRefreshing(false);
                }
                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
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
        HttpRestClient.postJSON(mContext, false, ApiManager.TEMP_TEAM_DETAIL_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "getTempTeamDetailData: " + responseBody.toString());
                lastUpdateTime=System.currentTimeMillis();
                if (responseBody.optBoolean("status")) {

                    int i = 0, j = 0;
                    JSONArray array = responseBody.optJSONArray("data");

                    LogUtil.e(TAG, "getTempTeamDetailData: " + array.length());
                    LogUtil.e(TAG, "getTempTeamDetailData: " + data.length());

                    for (j = 0; j < data.length(); j++) {
                        int wk = 0, bat = 0, ar = 0, bowl = 0, team1 = 0, team2 = 0, lineup_count = 0,cr=0;
                        float credit_point = 0, totalPoints = 0;
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
                                playerDetails.add(playerDetail);
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
                                }else if (preferences.getMatchModel().getSportId().equals("2")){
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
                                }else if (preferences.getMatchModel().getSportId().equals("3")){
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
                                }else if (preferences.getMatchModel().getSportId().equals("6")){
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
                                else if (preferences.getMatchModel().getSportId().equals("7")){
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
                                else if (preferences.getMatchModel().getSportId().equals("4")){
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

                                if(preferences.getMatchModel().getTeamAXi().toLowerCase().equals("yes") &&
                                        preferences.getMatchModel().getTeamBXi().toLowerCase().equals("yes")){

                                    if(!playerDetail.getPlayingXi().equalsIgnoreCase("Yes") &&
                                            !playerDetail.getOther_text().equalsIgnoreCase("substitute")){
                                        lineup_count++;
                                        LogUtil.e(TAG, "onSuccessResult: ABC "+playerDetail.getPlayerName()+"  "+playerDetail.getPlayingXi() );
                                    }
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

                                totalPoints+= CustomUtil.convertFloat(playerDetail.getTotalPoints());
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
                        playerListModel.setTotal_points(totalPoints);

                        LogUtil.e(TAG, "onSuccessResult: "+lineup_count );
                        playerListModel.setLineup_count(""+ lineup_count);
                        playerListModel.setTeam1_sname(preferences.getMatchModel().getTeam1Sname());
                        playerListModel.setTeam2_sname(preferences.getMatchModel().getTeam2Sname());
                        playerListModels.add(playerListModel);
                    }

                    adapter = new MyTeamListAdapter(mContext, playerListModels,1);
                    myTeamList.setLayoutManager(new LinearLayoutManager(mContext));
                    myTeamList.setAdapter(adapter);

                    if (playerListModels.size()>0){
                        layNoData.setVisibility(View.GONE);
                        myTeamList.setVisibility(View.VISIBLE);
                    }else {
                        layNoData.setVisibility(View.VISIBLE);
                        myTeamList.setVisibility(View.GONE);
                    }

                    if (playerListModels != null && playerListModels.size()>0) {
                        ((AfterMatchActivity) mContext).joined_team_tab.getTabAt(1).setText("My Teams (" + playerListModels.size() + ")");
                    } else {
                        ((AfterMatchActivity) mContext).joined_team_tab.getTabAt(1).setText("My Teams");
                    }
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }


}