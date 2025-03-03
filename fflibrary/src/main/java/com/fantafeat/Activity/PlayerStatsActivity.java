package com.fantafeat.Activity;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantafeat.Adapter.PlayerStatsAdapter;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.Model.PlayerStatsModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlayerStatsActivity extends BaseActivity {

    RecyclerView player_stat_list;
    TextView player_name, player_spec, credits, total_points;
    ImageView player_img;
    PlayerModel playerModel;
    String league_id;
    List<PlayerStatsModel> playerStatsModelList = new ArrayList<>();
    PlayerStatsAdapter playerStatsAdapter;
    ImageView toolbar_back,imgPlace,imgClose;
    TextView toolbar_title,txtPlace,txtSelBy;
    LinearLayout layNoData;

    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_stats);
        Window window = PlayerStatsActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.blackPrimary));
//fantafeat@123
        Intent intent = getIntent();
        league_id = intent.getStringExtra("league_id");
        String model = intent.getStringExtra("playerModel");
        playerModel = gson.fromJson(model,PlayerModel.class);

        player_stat_list = findViewById(R.id.player_stat_list);
        player_name = findViewById(R.id.player_name);
        player_spec = findViewById(R.id.player_spec);
        credits = findViewById(R.id.credits);
        total_points = findViewById(R.id.total_points);
        player_img = findViewById(R.id.player_img);
        imgClose = findViewById(R.id.imgClose);
        txtSelBy = findViewById(R.id.txtSelBy);
       // toolbar_back = findViewById(R.id.toolbar_back);
       // toolbar_title = findViewById(R.id.toolbar_title);
        layNoData = findViewById(R.id.layNoData);
        imgPlace = findViewById(R.id.imgPlace);
        txtPlace = findViewById(R.id.txtPlace);

        /*toolbar_title.setText("Player Stats");
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

        imgClose.setOnClickListener(v->{
            finish();
        });

        String sportId=preferences.getMatchModel().getSportId();

        if (sportId.equals("1")){
            if (playerModel.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                player_spec.setText(preferences.getMatchModel().getTeam1Sname()+" | "+playerModel.getPlayerType());
                CustomUtil.loadImageWithGlide(mContext,player_img,ApiManager.DOCUMENTS,playerModel.getPlayerImage(),R.drawable.ic_team1_tshirt);
            }else if (playerModel.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                player_spec.setText(preferences.getMatchModel().getTeam2Sname()+" | "+playerModel.getPlayerType());
                CustomUtil.loadImageWithGlide(mContext,player_img,ApiManager.DOCUMENTS,playerModel.getPlayerImage(),R.drawable.ic_team2_tshirt);
            }
        }
        else if (sportId.equals("2")){
            if (playerModel.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                player_spec.setText(preferences.getMatchModel().getTeam1Sname()+" | "+playerModel.getPlayerType());
                CustomUtil.loadImageWithGlide(mContext,player_img,ApiManager.DOCUMENTS,playerModel.getPlayerImage(),R.drawable.football_player1);
            }else if (playerModel.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                player_spec.setText(preferences.getMatchModel().getTeam2Sname()+" | "+playerModel.getPlayerType());
                CustomUtil.loadImageWithGlide(mContext,player_img,ApiManager.DOCUMENTS,playerModel.getPlayerImage(),R.drawable.football_player2);
            }
        }
        else if (sportId.equals("3")){
            if (playerModel.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                player_spec.setText(preferences.getMatchModel().getTeam1Sname()+" | "+playerModel.getPlayerType());
                CustomUtil.loadImageWithGlide(mContext,player_img,ApiManager.DOCUMENTS,playerModel.getPlayerImage(),R.drawable.baseball_player1);
            }else if (playerModel.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                player_spec.setText(preferences.getMatchModel().getTeam2Sname()+" | "+playerModel.getPlayerType());
                CustomUtil.loadImageWithGlide(mContext,player_img,ApiManager.DOCUMENTS,playerModel.getPlayerImage(),R.drawable.baseball_player2);
            }
        }
        else if (sportId.equals("4")){
            if (playerModel.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                player_spec.setText(preferences.getMatchModel().getTeam1Sname()+" | "+playerModel.getPlayerType());
                CustomUtil.loadImageWithGlide(mContext,player_img,ApiManager.DOCUMENTS,playerModel.getPlayerImage(),R.drawable.basketball_team1);
            }else if (playerModel.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                player_spec.setText(preferences.getMatchModel().getTeam2Sname()+" | "+playerModel.getPlayerType());
                CustomUtil.loadImageWithGlide(mContext,player_img,ApiManager.DOCUMENTS,playerModel.getPlayerImage(),R.drawable.basketball_team2);
            }
        }

        player_name.setText(playerModel.getPlayerName());
        if (playerModel.getPlayer_percent()==null){
            txtSelBy.setText("-");
        }else {
            txtSelBy.setText(playerModel.getPlayer_percent()+"%");
        }

      // player_spec.setText(playerModel.getPlayerType());
        credits.setText(playerModel.getPlayerRank());
        total_points.setText(playerModel.getPlayerAvgPoint());
        getData();

    }

    private void getData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",preferences.getUserModel().getId());
            jsonObject.put("league_id", league_id);
            jsonObject.put("player_id", playerModel.getPlayerId());
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("offset",5);
            //jsonObject.put("limit",5);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, true, ApiManager.PLAYER_STATS, jsonObject, new GetApiResult() {

            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG,responseBody.toString());
                if (responseBody.optBoolean("status")) {

                    JSONArray array = responseBody.optJSONArray("data");
                    int i = 0;
                    if(array.length()>0) {

                        for (i = 0; i < array.length(); i++) {
                            try {
                                JSONObject data = array.getJSONObject(i);
                                PlayerStatsModel playerStatsModel = gson.fromJson(data.toString(), PlayerStatsModel.class);
                                playerStatsModelList.add(playerStatsModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        setData();
                    }
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void setData() {
        playerStatsAdapter = new PlayerStatsAdapter(mContext, playerStatsModelList);
        player_stat_list.setAdapter(playerStatsAdapter);
        player_stat_list.setLayoutManager(new LinearLayoutManager(mContext));
        checkData();
    }

    private void checkData(){
        LogUtil.d("selSports",preferences.getMatchModel().getSportId()+" ");
        switch (preferences.getMatchModel().getSportId()+""){
            case "1":
                if (playerStatsModelList.size()>0){
                    player_stat_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    player_stat_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.cricket_placeholder);
                    txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
            case "2":
                if (playerStatsModelList.size()>0){
                    player_stat_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    player_stat_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.football_placeholder);
                    txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
            case "3":
                if (playerStatsModelList.size()>0){
                    player_stat_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    player_stat_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.baseball_placeholder);
                    txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
            case "4":
                if (playerStatsModelList.size()>0){
                    player_stat_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    player_stat_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.basketball_placeholder);
                    txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
            case "5":
                if (playerStatsModelList.size()>0){
                    player_stat_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    player_stat_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.vollyball_placeholder);
                }
                break;
             case "6"://6
                if (playerStatsModelList.size()>0){
                    player_stat_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    player_stat_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.handball_placeholder);
                    txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
        }

    }

}