package com.fantafeat.Fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantafeat.Adapter.MatchPlayerStatsAdapter;
import com.fantafeat.Adapter.StateTeamFilterAdapter;
import com.fantafeat.Model.MatchPlayerStateModel;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MatchPlayerStatsFragment extends BaseFragment {

    //AfterMatchActivity afterMatchActivity;
    List<MatchPlayerStateModel> matchPlayerStateModelList = new ArrayList<>();
    MatchPlayerStatsAdapter adapter;
    StateTeamFilterAdapter filterAdapter;
    RecyclerView player_stats_list,recyclerTeamFilter;
    SwipeRefreshLayout pull_player_stats;
    private LinearLayout layNoData;
    private ImageView imgPlace;
    private TextView txtPlace,txtSelBy;
    private String selectedTeam="";
    private long lastUpdateTime=0;
    private boolean isDataLoaded=false;

    public MatchPlayerStatsFragment() {
        //this.afterMatchActivity = afterMatchActivity;
    }

    public static MatchPlayerStatsFragment newInstance(){
        return new MatchPlayerStatsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match_player_stats, container, false);
        initFragment(view);

        return view;
    }

    @Override
    public void initControl(View view) {
        player_stats_list = view.findViewById(R.id.player_stats_list);
        pull_player_stats = view.findViewById(R.id.pull_player_stats);
        layNoData = view.findViewById(R.id.layNoData);
        imgPlace = view.findViewById(R.id.imgPlace);
        txtPlace = view.findViewById(R.id.txtPlace);
        txtSelBy = view.findViewById(R.id.txtSelBy);
        recyclerTeamFilter = view.findViewById(R.id.recyclerTeamFilter);

        matchPlayerStateModelList = new ArrayList<>();

        adapter = new MatchPlayerStatsAdapter(mContext, matchPlayerStateModelList, preferences);
        player_stats_list.setLayoutManager(new LinearLayoutManager(mContext));
        player_stats_list.setAdapter(adapter);
        //getData();
        //getTeamData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible() && !isDataLoaded) {
            isDataLoaded = true;
            getTeamData();
        }
    }

    @Override
    public void initClick() {
        pull_player_stats.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if ((System.currentTimeMillis()-lastUpdateTime) >= ConstantUtil.Refresh_delay) {
                    getTeamData();
                }else {
                    pull_player_stats.setRefreshing(false);
                }
            }
        });
    }

    private void getData(List<PlayerModel> compareList) {
        matchPlayerStateModelList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("match_id", preferences.getMatchModel().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean showProgress = true;
        if (pull_player_stats != null && pull_player_stats.isRefreshing()) {
            showProgress = false;
        }

        HttpRestClient.postJSON(mContext, showProgress, ApiManager.PLAYER_LEADERBOARD, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                lastUpdateTime=System.currentTimeMillis();
                if (pull_player_stats != null && pull_player_stats.isRefreshing()) {
                    pull_player_stats.setRefreshing(false);
                }
                if (responseBody.optBoolean("status")) {
                    LogUtil.e(TAG, "getData: " + responseBody.toString());
                    JSONArray array = responseBody.optJSONArray("data");
                    LogUtil.e(TAG, "getData: " + array.length());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject data = array.optJSONObject(i);
                        MatchPlayerStateModel matchPlayerStateModel = gson.fromJson(data.toString(), MatchPlayerStateModel.class);
                        matchPlayerStateModelList.add(matchPlayerStateModel);
                    }
                    Collections.sort(matchPlayerStateModelList, new PointUp());

                    filterList(compareList);
                   //getTeamData();
                    //getTempTeamDetailData();
                }
                checkData();
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });

    }

    private void checkData(){
        LogUtil.d("selSports",preferences.getMatchModel().getSportId()+" ");
        switch (preferences.getMatchModel().getSportId()+""){
            case "1":
                if (matchPlayerStateModelList.size()>0){
                    player_stats_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    player_stats_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.cricket_placeholder);
                    txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
            case "2":
                if (matchPlayerStateModelList.size()>0){
                    player_stats_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    player_stats_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.football_placeholder);
                    txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
            case "3":
                if (matchPlayerStateModelList.size()>0){
                    player_stats_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    player_stats_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.baseball_placeholder);
                }
                break;
            case "4":
                if (matchPlayerStateModelList.size()>0){
                    player_stats_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    player_stats_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.basketball_placeholder);
                }
                break;
            case "5":
                if (matchPlayerStateModelList.size()>0){
                    player_stats_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    player_stats_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.vollyball_placeholder);
                }
                break;
            case "6"://6
                if (matchPlayerStateModelList.size()>0){
                    player_stats_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    player_stats_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.handball_placeholder);
                    txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
            case "7"://6
                if (matchPlayerStateModelList.size()>0){
                    player_stats_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    player_stats_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.kabaddi_placeholder);
                    txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
        }

    }

    private void getTempTeamDetailData(JSONArray data) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("con_display_type", preferences.getMatchModel().getConDisplayType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "getTempTeamDetailData: " + jsonObject.toString());
        HttpRestClient.postJSON(mContext, false, ApiManager.TEMP_TEAM_DETAIL_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "getTempTeamDetailData: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {

                    int i = 0, j = 0;
                    JSONArray array = responseBody.optJSONArray("data");

                    LogUtil.e(TAG, "getTempTeamDetailData: " + data.length());

                    List<PlayerListModel> playerListModels=new ArrayList<>();

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
                                }
                                else if (preferences.getMatchModel().getSportId().equals("2")){
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
                                else if (preferences.getMatchModel().getSportId().equals("3")){
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
                                else if (preferences.getMatchModel().getSportId().equals("6")){
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

                                    if (playerDetail.getOther_text()==null && TextUtils.isEmpty(playerDetail.getOther_text())){
                                        playerDetail.setOther_text("");
                                    }

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

                        //LogUtil.e(TAG, "onSuccessResult: "+lineup_count );
                        playerListModel.setLineup_count(""+ lineup_count);
                        playerListModel.setTeam1_sname(preferences.getMatchModel().getTeam1Sname());
                        playerListModel.setTeam2_sname(preferences.getMatchModel().getTeam2Sname());

                        if (TextUtils.isEmpty(selectedTeam)){
                            if (j==0){
                                selectedTeam=playerListModel.getId();
                                playerListModel.setChecked(true);
                                getData(playerListModel.getPlayerDetailList());
                            }else
                                playerListModel.setChecked(false);
                        }else {
                            if (selectedTeam.equalsIgnoreCase(playerListModel.getId())){
                                selectedTeam=playerListModel.getId();
                                playerListModel.setChecked(true);
                                getData(playerListModel.getPlayerDetailList());
                            }else
                                playerListModel.setChecked(false);
                        }

                        playerListModels.add(playerListModel);
                    }

                    if (playerListModels.size()==0){
                        getData(new ArrayList<>());
                    }

                    filterAdapter=new StateTeamFilterAdapter(mContext, playerListModels, model -> {
                        //filterList(model.getPlayerDetailList());
                        selectedTeam=model.getId();

                        getData(model.getPlayerDetailList());
                    });
                    recyclerTeamFilter.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
                    recyclerTeamFilter.setAdapter(filterAdapter);

                    //filterList(playerListModels);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    public void getTeamData() {
        //final ArrayList<PlayerModel> playerModels = new ArrayList<>();
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

                if (responseBody.optBoolean("status")) {

                    JSONArray array = responseBody.optJSONArray("data");

                    LogUtil.e(TAG, "TEMP_TEAM_LIST: " + array);
                    /*int i = 0;
                    for (i = 0; i < array.length(); i++) {
                        try {
                            PlayerModel playerModel = gson.fromJson(array.getJSONObject(i).toString(), PlayerModel.class);
                            playerModels.add(playerModel);
                            preferences.setPlayerList(playerModels);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }*/
                    getTempTeamDetailData(array);
                }

            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void filterList(List<PlayerModel> compareList) {
        /*List<PlayerModel> filtered=new ArrayList();
        LogUtil.d("resp",compareList.size()+" ");
        filtered.addAll(compareList);*/
        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            List<PlayerModel> listDistinct = compareList.stream().distinct().collect(Collectors.toList());
            LogUtil.d("resp","hash "+listDistinct.size()+" ");
        }

        filtered=new ArrayList(new LinkedHashSet(compareList));*/

        /*Object[] st = compareList.toArray();
        for (Object s : st) {
            if (filtered.indexOf(s) != filtered.lastIndexOf(s)) {
                filtered.remove(filtered.lastIndexOf(s));
            }
        }*/

        for (MatchPlayerStateModel modal:matchPlayerStateModelList){
            for (PlayerModel playerModel:compareList){
                if (modal.getPlayerId().equalsIgnoreCase(playerModel.getPlayerId())){
                    modal.setCheck(true);
                    if (playerModel.getIsCaptain().equalsIgnoreCase("yes")){
                        modal.setCorvc("c");
                    }else if (playerModel.getIsWiseCaptain().equalsIgnoreCase("yes")){
                        modal.setCorvc("vc");
                    }else {
                        modal.setCorvc("");
                    }
                }
            }
        }
        adapter.updateData(matchPlayerStateModelList);
       // LogUtil.d("resp",filtered.size()+" ");
    }

    public class PointUp implements Comparator<MatchPlayerStateModel> {
        @Override
        public int compare(MatchPlayerStateModel o1, MatchPlayerStateModel o2) {
            return Float.compare( CustomUtil.convertFloat(o2.getTotalPoint()),CustomUtil.convertFloat(o1.getTotalPoint()));
        }

    }
}