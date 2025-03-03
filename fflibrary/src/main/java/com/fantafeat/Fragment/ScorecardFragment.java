package com.fantafeat.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fantafeat.Activity.AfterMatchActivity;
import com.fantafeat.Adapter.ScoreCardAdapter;
import com.fantafeat.Adapter.StateTeamFilterAdapter;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.PlayerListModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.Model.ScoreModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.OnFragmentInteractionListener;
import com.google.gson.JsonSyntaxException;
import com.itextpdf.text.pdf.parser.Line;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScorecardFragment extends BaseFragment {

    private RecyclerView recyclerScore,recyclerTeamFilter;
    private LinearLayout  layScore;
    private ScoreCardAdapter adapter;
    private SwipeRefreshLayout swipScore;
    private AfterMatchActivity afterMatchActivity;
    private LinearLayout layNoData;
    private MatchModel matchModel;
    private String selectedTeam="";
    private StateTeamFilterAdapter filterAdapter;
    private OnFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    public ScorecardFragment() {

    }

    public static ScorecardFragment newInstance() {

        return new ScorecardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_scorecard, container, false);
        initFragment(view);
        //setScoreData();
        return view;
    }

    @Override
    public void initControl(View view) {


        recyclerScore=view.findViewById(R.id.recyclerScore);
        swipScore=view.findViewById(R.id.swipScore);

        layNoData=view.findViewById(R.id.layNoData);
        layScore=view.findViewById(R.id.layScore);
        recyclerTeamFilter=view.findViewById(R.id.recyclerTeamFilter);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.matchModel=preferences.getMatchModel();
        //getTeamData();
        setScoreData();
    }

    public void setScoreData(){
        if (!preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("Cancelled")) {
            if (ConstantUtil.score_list!=null && ConstantUtil.score_list.size()>0){

                getTeamData();
                //filterList(playerListModels);

                checkData(true);

            }else {
                getScoreCard();
            }
        }else {
            checkData(false);
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

                    if (array.length()>0){
                        LogUtil.e(TAG, "getTempTeamDetailData: " + data.length());

                        List<PlayerListModel> playerListModels=new ArrayList<>();
                        MatchModel prefMatch=preferences.getMatchModel();

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
                                    if (prefMatch.getSportId().equals("1")){
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
                                    else if (prefMatch.getSportId().equals("2")){
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
                                    else if (prefMatch.getSportId().equals("3")){
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
                                    else if (prefMatch.getSportId().equals("6")){
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
                                    else if (prefMatch.getSportId().equals("7")){
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
                                    else if (prefMatch.getSportId().equals("4")){
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

                                    if (playerDetail.getTeamId().equals(prefMatch.getTeam1())) {
                                        team1 += 1;
                                    }
                                    if (playerDetail.getTeamId().equals(prefMatch.getTeam2())) {
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

                                    if(prefMatch.getTeamAXi().toLowerCase().equals("yes") &&
                                            prefMatch.getTeamBXi().toLowerCase().equals("yes")){

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

                                /*if (ConstantUtil.score_list!=null){
                                    for (ScoreModel sm : ConstantUtil.score_list) {
                                        for (ScoreModel.Batsman batsman: sm.getBatsmen()) {
                                            if (batsman.getBatsmanId().equalsIgnoreCase(playerDetail.getId())){
                                                batsman.setPlayerInTeam(true);
                                            }
                                        }
                                        for (ScoreModel.Bowler batsman: sm.getBowlers()) {
                                            if (batsman.getBowlerId().equalsIgnoreCase(playerDetail.getId())){
                                                batsman.setPlayerInTeam(true);
                                            }
                                        }
                                    }
                                }*/
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
                            playerListModel.setTeam1_sname(prefMatch.getTeam1Sname());
                            playerListModel.setTeam2_sname(prefMatch.getTeam2Sname());

                            if (TextUtils.isEmpty(selectedTeam)){
                                if (j==0){
                                    selectedTeam=playerListModel.getId();
                                    playerListModel.setChecked(true);
                                    filterList(playerListModel.getPlayerDetailList());
                                    //getData(playerListModel.getPlayerDetailList());
                                }else
                                    playerListModel.setChecked(false);
                            }
                            else {
                                if (selectedTeam.equalsIgnoreCase(playerListModel.getId())){
                                    selectedTeam=playerListModel.getId();
                                    playerListModel.setChecked(true);
                                    filterList(playerListModel.getPlayerDetailList());
                                    //getData(playerListModel.getPlayerDetailList());
                                }else
                                    playerListModel.setChecked(false);
                            }

                            playerListModels.add(playerListModel);
                        }

                        filterAdapter=new StateTeamFilterAdapter(mContext, playerListModels, model -> {
                            selectedTeam=model.getId();
                            filterList(model.getPlayerDetailList());
                        });
                        recyclerTeamFilter.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
                        recyclerTeamFilter.setAdapter(filterAdapter);
                    }else {
                        filterList(new ArrayList<>());
                    }

                    //filterList(playerListModels);

                    //setScoreData();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void filterList(List<PlayerModel> playerListModels) {
        /*for (PlayerListModel plm:playerListModels) {
            if (plm.isChecked()){

            }
        }*/

        for (ScoreModel sm : ConstantUtil.score_list) {
            for (ScoreModel.Batsman batsman: sm.getBatsmen()) {
                batsman.setPlayerInTeam(false);
                batsman.setCorvc("");
            }
            for (ScoreModel.Bowler batsman: sm.getBowlers()) {
                batsman.setPlayerInTeam(false);
                batsman.setCorvc("");
            }
            /*for (ScoreModel.Fow fow: sm.getFows()) {
                fow.setPlayerInTeam(false);
            }*/
        }

        for (PlayerModel pm : playerListModels) {
            if (ConstantUtil.score_list!=null){
                for (ScoreModel sm : ConstantUtil.score_list) {
                    for (ScoreModel.Batsman batsman: sm.getBatsmen()) {
                        //LogUtil.e("resp","Batsman: "+batsman.getBatsmanId()+"  "+pm.getId());
                        if (batsman.getBatsmanId().equalsIgnoreCase(pm.getPlayerId())){
                            batsman.setPlayerInTeam(true);
                            if (pm.getIsCaptain().equalsIgnoreCase("yes")){
                                batsman.setCorvc("c");
                            }else if (pm.getIsWiseCaptain().equalsIgnoreCase("yes")){
                                batsman.setCorvc("vc");
                            }
                            //LogUtil.e("resp","Batsman: "+batsman.getName());
                        }
                    }
                    for (ScoreModel.Bowler bowler: sm.getBowlers()) {
                        //LogUtil.e("resp","Bowler: "+batsman.getBowlerId()+"  "+pm.getId());
                        if (bowler.getBowlerId().equalsIgnoreCase(pm.getPlayerId())){
                            bowler.setPlayerInTeam(true);
                            if (pm.getIsCaptain().equalsIgnoreCase("yes")){
                                bowler.setCorvc("c");
                            }
                            else if (pm.getIsWiseCaptain().equalsIgnoreCase("yes")){
                                bowler.setCorvc("vc");
                            }
                            //LogUtil.e("resp","Bowler: "+batsman.getName());
                        }
                    }
                    /*for (ScoreModel.Fow fow: sm.getFows()) {
                        if (fow.getBowlerId().equalsIgnoreCase(pm.getPlayerId())
                                || fow.getBatsmanId().equalsIgnoreCase(pm.getPlayerId())){
                            fow.setPlayerInTeam(true);
                        }
                    }*/
                }
            }
        }

        LinearLayoutManager manager=new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        recyclerScore.setLayoutManager(manager);
        adapter=new ScoreCardAdapter(mContext,ConstantUtil.score_list,matchModel);
        recyclerScore.setAdapter(adapter);
        //adapter.notifyInnerAdapters(ConstantUtil.score_list);
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

    public void getScoreCard() {
        ConstantUtil.score_list=new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id",preferences.getMatchModel().getId());
            jsonObject.put("user_id",preferences.getUserModel().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.e(TAG, "onSuccessResult param: " + jsonObject.toString() );

        boolean isLoader=true;
        if (swipScore.isRefreshing()){
            isLoader=false;
        }

        HttpRestClient.postJSONWithParam(mContext, isLoader, ApiManager.SCORE_CARD, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                try {
                    swipScore.setRefreshing(false);
                    LogUtil.e(TAG, "onSuccessResult Score: " + responseBody.toString() );
                    if(responseBody.optBoolean("status")){

                        JSONObject data=responseBody.optJSONObject("data");
                        if (data!=null && data.has("innings")){
                            JSONArray innings=data.optJSONArray("innings");
                            if (innings!=null && innings.length()>0){
                                ConstantUtil.score_list.clear();
                                for (int i=0;i<innings.length();i++){
                                    ScoreModel modal= gson.fromJson(innings.optJSONObject(i).toString(),ScoreModel.class);
                                    ConstantUtil.score_list.add(modal);
                                }

                                //afterMatchActivity.updateMainScore(ConstantUtil.score_list);
                                if (mListener!=null){
                                    mListener.onFragmentAction(ConstantUtil.score_list);
                                }
                                //Collections.reverse(scoreModelList);
                                setScoreData();
                                filterList(new ArrayList<>());
                                checkData(true);

                            }else {
                                checkData(false);
                            }
                        }else {
                            checkData(false);
                        }
                    }else {
                        checkData(false);
                    }
                } catch (JsonSyntaxException e) {
                    checkData(false);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                swipScore.setRefreshing(false);
            }
        });
    }

    private void checkData(boolean isData){
        if (isData) {
            layScore.setVisibility(View.VISIBLE);
            layNoData.setVisibility(View.GONE);

        }else {
            layScore.setVisibility(View.GONE);
            layNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initClick() {
        swipScore.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (preferences.getUpdateMaster().getIs_score_card_refresh().equalsIgnoreCase("no")){
                    ConstantUtil.score_list=new ArrayList<>();
                    getScoreCard();
                }else {
                    swipScore.setRefreshing(false);
                }

               /* */
            }
        });
    }
}