package com.fantafeat.Activity;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantafeat.Adapter.AfterMatchPlayerStatesAdapter;
import com.fantafeat.Model.AfterMatchPlayerStatesModel;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.MatchPlayerStateModel;
import com.fantafeat.Model.PlayerModel;
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

public class AfterMatchPlayerStatesActivity extends BaseActivity {

    MatchPlayerStateModel matchPlayerStateModel;
    PlayerModel playerModel;
    ImageView imgUSerProfile,toolbar_back;
    TextView txtPlayerName, type_player, player_points, player_credit,txtSelBy,txtLblSelBy;
    List<AfterMatchPlayerStatesModel> list = new ArrayList<>();
    AfterMatchPlayerStatesAdapter adapter;
    RecyclerView player_list;
    int type = 0;

    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_match_player_states);
        Window window = AfterMatchPlayerStatesActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.blackPrimary));

        Intent intent = getIntent();
        type = intent.getIntExtra("type",1);
        if(type==1){
            matchPlayerStateModel = gson.fromJson(intent.getStringExtra("model"),MatchPlayerStateModel.class);
        }else{
            playerModel = gson.fromJson(intent.getStringExtra("model"),PlayerModel.class);
        }

        imgUSerProfile = findViewById(R.id.imgUSerProfile);
        txtPlayerName = findViewById(R.id.txtPlayerName);
        type_player = findViewById(R.id.type_player);
        player_points = findViewById(R.id.player_points);
        player_credit = findViewById(R.id.player_credit);
        player_list = findViewById(R.id.player_list);
        toolbar_back = findViewById(R.id.toolbar_back);
        txtSelBy = findViewById(R.id.txtSelBy);
        txtLblSelBy = findViewById(R.id.txtLblSelBy);

        MatchModel matchModel=preferences.getMatchModel();



        if(type==1){
            if (matchModel.getSportId().equals("1")){
                if (matchPlayerStateModel.getPlayerImage().equals("")) {
                    if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam1())){
                        imgUSerProfile.setImageResource(R.drawable.ic_team1_tshirt);
                       /* Glide.with(mContext)
                                .load(R.drawable.ic_team1_tshirt)
                                .placeholder(R.drawable.ic_team1_tshirt)
                                .error(R.drawable.ic_team1_tshirt)
                                .into(imgUSerProfile);*/
                    }else if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam2())){
                        imgUSerProfile.setImageResource(R.drawable.ic_team2_tshirt);
                        /*Glide.with(mContext)
                                .load(R.drawable.ic_team2_tshirt)
                                .placeholder(R.drawable.ic_team2_tshirt)
                                .error(R.drawable.ic_team2_tshirt)
                                .into(imgUSerProfile);*/
                    }
                }
                else {
                    if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam1())){
                        CustomUtil.loadImageWithGlide(mContext,imgUSerProfile,ApiManager.DOCUMENTS,matchPlayerStateModel.getPlayerImage(),R.drawable.ic_team1_tshirt);
                        /*Glide.with(mContext)
                                .load(ApiManager.DOCUMENTS + matchPlayerStateModel.getPlayerImage())
                                .error(R.drawable.ic_team1_tshirt)
                                .placeholder(R.drawable.ic_team1_tshirt)
                                .into(imgUSerProfile);*/
                    }else if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam2())){
                        CustomUtil.loadImageWithGlide(mContext,imgUSerProfile,ApiManager.DOCUMENTS,matchPlayerStateModel.getPlayerImage(),R.drawable.ic_team2_tshirt);
                       /* Glide.with(mContext)
                                .load(ApiManager.DOCUMENTS + matchPlayerStateModel.getPlayerImage())
                                .error(R.drawable.ic_team2_tshirt)
                                .placeholder(R.drawable.ic_team2_tshirt)
                                .into(imgUSerProfile);*/
                    }
                }
            }
            else if (matchModel.getSportId().equals("2")){
                if (matchPlayerStateModel.getPlayerImage().equals("")) {
                    if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam1())){
                        imgUSerProfile.setImageResource(R.drawable.football_player1);
                       /* Glide.with(mContext)
                                .load(R.drawable.ic_team1_tshirt)
                                .placeholder(R.drawable.ic_team1_tshirt)
                                .error(R.drawable.ic_team1_tshirt)
                                .into(imgUSerProfile);*/
                    }else if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam2())){
                        imgUSerProfile.setImageResource(R.drawable.football_player2);
                        /*Glide.with(mContext)
                                .load(R.drawable.ic_team2_tshirt)
                                .placeholder(R.drawable.ic_team2_tshirt)
                                .error(R.drawable.ic_team2_tshirt)
                                .into(imgUSerProfile);*/
                    }
                } else {
                    if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam1())){
                        CustomUtil.loadImageWithGlide(mContext,imgUSerProfile,ApiManager.DOCUMENTS,matchPlayerStateModel.getPlayerImage(),R.drawable.football_player1);
                    /*    Glide.with(mContext)
                                .load(ApiManager.DOCUMENTS + matchPlayerStateModel.getPlayerImage())
                                .error(R.drawable.football_player1)
                                .placeholder(R.drawable.football_player1)
                                .into(imgUSerProfile);*/
                    }else if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam2())){
                        CustomUtil.loadImageWithGlide(mContext,imgUSerProfile,ApiManager.DOCUMENTS,matchPlayerStateModel.getPlayerImage(),R.drawable.football_player2);
                        /*Glide.with(mContext)
                                .load(ApiManager.DOCUMENTS + matchPlayerStateModel.getPlayerImage())
                                .error(R.drawable.football_player2)
                                .placeholder(R.drawable.football_player2)
                                .into(imgUSerProfile);*/
                    }
                }
            }
            else if (matchModel.getSportId().equals("3")){
                if (matchPlayerStateModel.getPlayerImage().equals("")) {
                    if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam1())){
                        imgUSerProfile.setImageResource(R.drawable.baseball_player1);
                       /* Glide.with(mContext)
                                .load(R.drawable.ic_team1_tshirt)
                                .placeholder(R.drawable.ic_team1_tshirt)
                                .error(R.drawable.ic_team1_tshirt)
                                .into(imgUSerProfile);*/
                    }else if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam2())){
                        imgUSerProfile.setImageResource(R.drawable.baseball_player2);
                        /*Glide.with(mContext)
                                .load(R.drawable.ic_team2_tshirt)
                                .placeholder(R.drawable.ic_team2_tshirt)
                                .error(R.drawable.ic_team2_tshirt)
                                .into(imgUSerProfile);*/
                    }
                } else {
                    if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam1())){
                        CustomUtil.loadImageWithGlide(mContext,imgUSerProfile,ApiManager.DOCUMENTS,matchPlayerStateModel.getPlayerImage(),R.drawable.baseball_player1);
                    /*    Glide.with(mContext)
                                .load(ApiManager.DOCUMENTS + matchPlayerStateModel.getPlayerImage())
                                .error(R.drawable.football_player1)
                                .placeholder(R.drawable.football_player1)
                                .into(imgUSerProfile);*/
                    }else if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam2())){
                        CustomUtil.loadImageWithGlide(mContext,imgUSerProfile,ApiManager.DOCUMENTS,matchPlayerStateModel.getPlayerImage(),R.drawable.baseball_player2);
                        /*Glide.with(mContext)
                                .load(ApiManager.DOCUMENTS + matchPlayerStateModel.getPlayerImage())
                                .error(R.drawable.football_player2)
                                .placeholder(R.drawable.football_player2)
                                .into(imgUSerProfile);*/
                    }
                }
            }
            else if (matchModel.getSportId().equals("4")){
                if (matchPlayerStateModel.getPlayerImage().equals("")) {
                    if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam1())){
                        imgUSerProfile.setImageResource(R.drawable.basketball_team1);
                       /* Glide.with(mContext)
                                .load(R.drawable.ic_team1_tshirt)
                                .placeholder(R.drawable.ic_team1_tshirt)
                                .error(R.drawable.ic_team1_tshirt)
                                .into(imgUSerProfile);*/
                    }else if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam2())){
                        imgUSerProfile.setImageResource(R.drawable.basketball_team2);
                        /*Glide.with(mContext)
                                .load(R.drawable.ic_team2_tshirt)
                                .placeholder(R.drawable.ic_team2_tshirt)
                                .error(R.drawable.ic_team2_tshirt)
                                .into(imgUSerProfile);*/
                    }
                } else {
                    if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam1())){
                        CustomUtil.loadImageWithGlide(mContext,imgUSerProfile,ApiManager.DOCUMENTS,matchPlayerStateModel.getPlayerImage(),R.drawable.basketball_team1);
                      /*  Glide.with(mContext)
                                .load(ApiManager.DOCUMENTS + matchPlayerStateModel.getPlayerImage())
                                .error(R.drawable.basketball_team1)
                                .placeholder(R.drawable.basketball_team1)
                                .into(imgUSerProfile);*/
                    }else if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam2())){
                        CustomUtil.loadImageWithGlide(mContext,imgUSerProfile,ApiManager.DOCUMENTS,matchPlayerStateModel.getPlayerImage(),R.drawable.basketball_team2);
                        /*Glide.with(mContext)
                                .load(ApiManager.DOCUMENTS + matchPlayerStateModel.getPlayerImage())
                                .error(R.drawable.basketball_team2)
                                .placeholder(R.drawable.basketball_team2)
                                .into(imgUSerProfile);*/
                    }
                }
            }
            else if (matchModel.getSportId().equals("6")){
                if (matchPlayerStateModel.getPlayerImage().equals("")) {
                    if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam1())){
                        imgUSerProfile.setImageResource(R.drawable.handball_player1);
                       /* Glide.with(mContext)
                                .load(R.drawable.ic_team1_tshirt)
                                .placeholder(R.drawable.ic_team1_tshirt)
                                .error(R.drawable.ic_team1_tshirt)
                                .into(imgUSerProfile);*/
                    }else if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam2())){
                        imgUSerProfile.setImageResource(R.drawable.handball_player2);
                        /*Glide.with(mContext)
                                .load(R.drawable.ic_team2_tshirt)
                                .placeholder(R.drawable.ic_team2_tshirt)
                                .error(R.drawable.ic_team2_tshirt)
                                .into(imgUSerProfile);*/
                    }
                } else {
                    if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam1())){
                        CustomUtil.loadImageWithGlide(mContext,imgUSerProfile,ApiManager.DOCUMENTS,matchPlayerStateModel.getPlayerImage(),R.drawable.handball_player1);
                      /*  Glide.with(mContext)
                                .load(ApiManager.DOCUMENTS + matchPlayerStateModel.getPlayerImage())
                                .error(R.drawable.basketball_team1)
                                .placeholder(R.drawable.basketball_team1)
                                .into(imgUSerProfile);*/
                    }else if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam2())){
                        CustomUtil.loadImageWithGlide(mContext,imgUSerProfile,ApiManager.DOCUMENTS,matchPlayerStateModel.getPlayerImage(),R.drawable.handball_player2);
                        /*Glide.with(mContext)
                                .load(ApiManager.DOCUMENTS + matchPlayerStateModel.getPlayerImage())
                                .error(R.drawable.basketball_team2)
                                .placeholder(R.drawable.basketball_team2)
                                .into(imgUSerProfile);*/
                    }
                }
            }
            else if (matchModel.getSportId().equals("7")){
                if (matchPlayerStateModel.getPlayerImage().equals("")) {
                    if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam1())){
                        imgUSerProfile.setImageResource(R.drawable.kabaddi_player1);
                       /* Glide.with(mContext)
                                .load(R.drawable.ic_team1_tshirt)
                                .placeholder(R.drawable.ic_team1_tshirt)
                                .error(R.drawable.ic_team1_tshirt)
                                .into(imgUSerProfile);*/
                    }else if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam2())){
                        imgUSerProfile.setImageResource(R.drawable.kabaddi_player2);
                        /*Glide.with(mContext)
                                .load(R.drawable.ic_team2_tshirt)
                                .placeholder(R.drawable.ic_team2_tshirt)
                                .error(R.drawable.ic_team2_tshirt)
                                .into(imgUSerProfile);*/
                    }
                } else {
                    if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam1())){
                        CustomUtil.loadImageWithGlide(mContext,imgUSerProfile,ApiManager.DOCUMENTS,matchPlayerStateModel.getPlayerImage(),R.drawable.kabaddi_player1);
                      /*  Glide.with(mContext)
                                .load(ApiManager.DOCUMENTS + matchPlayerStateModel.getPlayerImage())
                                .error(R.drawable.basketball_team1)
                                .placeholder(R.drawable.basketball_team1)
                                .into(imgUSerProfile);*/
                    }else if (matchPlayerStateModel.getTeamId().equals(matchModel.getTeam2())){
                        CustomUtil.loadImageWithGlide(mContext,imgUSerProfile,ApiManager.DOCUMENTS,matchPlayerStateModel.getPlayerImage(),R.drawable.kabaddi_player2);
                        /*Glide.with(mContext)
                                .load(ApiManager.DOCUMENTS + matchPlayerStateModel.getPlayerImage())
                                .error(R.drawable.basketball_team2)
                                .placeholder(R.drawable.basketball_team2)
                                .into(imgUSerProfile);*/
                    }
                }
            }


            txtPlayerName.setText(matchPlayerStateModel.getPlayerName());
            if (getIntent().hasExtra("joining")){
                txtLblSelBy.setText("Joining");
                txtSelBy.setText(getIntent().getStringExtra("joining"));
            }
            else {
                if (TextUtils.isEmpty(matchPlayerStateModel.getPlayer_percent())){
                    txtSelBy.setText("-");
                }else {
                    txtSelBy.setText(matchPlayerStateModel.getPlayer_percent()+"%");
                }
            }


            String team = (matchModel.getTeam1().equals(matchPlayerStateModel.getTeamId())) ? matchModel.getTeam1Name() : matchModel.getTeam2Name();
            type_player.setText(team + " - " + matchPlayerStateModel.getPlayerType());
            player_points.setText(matchPlayerStateModel.getTotalPoint());
            player_credit.setText(matchPlayerStateModel.getPlayerRank());

        }
        else if(type==2){

            if (matchModel.getSportId().equals("1")){
                if (playerModel.getPlayerImage().equals("")) {
                    if (playerModel.getTeamId().equals(matchModel.getTeam1())){
                        imgUSerProfile.setImageResource(R.drawable.ic_team1_tshirt);
                       /* Glide.with(mContext)
                                .load(R.drawable.ic_team1_tshirt)
                                .placeholder(R.drawable.ic_team1_tshirt)
                                .error(R.drawable.ic_team1_tshirt)
                                .into(imgUSerProfile);*/
                    }else if (playerModel.getTeamId().equals(matchModel.getTeam2())){
                        imgUSerProfile.setImageResource(R.drawable.ic_team2_tshirt);
                       /* Glide.with(mContext)
                                .load(R.drawable.ic_team2_tshirt)
                                .placeholder(R.drawable.ic_team2_tshirt)
                                .error(R.drawable.ic_team2_tshirt)
                                .into(imgUSerProfile);*/
                    }
                } else {
                    if (playerModel.getTeamId().equals(matchModel.getTeam1())){
                        CustomUtil.loadImageWithGlide(mContext,imgUSerProfile,ApiManager.DOCUMENTS,playerModel.getPlayerImage(),R.drawable.ic_team1_tshirt);
                        /*Glide.with(mContext)
                                .load(ApiManager.DOCUMENTS + playerModel.getPlayerImage())
                                .error(R.drawable.ic_team1_tshirt)
                                .placeholder(R.drawable.ic_team1_tshirt)
                                .into(imgUSerProfile);*/
                    }else if (playerModel.getTeamId().equals(matchModel.getTeam2())){
                        CustomUtil.loadImageWithGlide(mContext,imgUSerProfile,ApiManager.DOCUMENTS,playerModel.getPlayerImage(),R.drawable.ic_team2_tshirt);
                        /*Glide.with(mContext)
                                .load(ApiManager.DOCUMENTS + playerModel.getPlayerImage())
                                .error(R.drawable.ic_team2_tshirt)
                                .placeholder(R.drawable.ic_team2_tshirt)
                                .into(imgUSerProfile);*/
                    }

                }
            }
            else if (matchModel.getSportId().equals("2")){
                if (playerModel.getPlayerImage().equals("")) {
                    if (playerModel.getTeamId().equals(matchModel.getTeam1())){
                        imgUSerProfile.setImageResource(R.drawable.football_player1);
                       /* Glide.with(mContext)
                                .load(R.drawable.ic_team1_tshirt)
                                .placeholder(R.drawable.ic_team1_tshirt)
                                .error(R.drawable.ic_team1_tshirt)
                                .into(imgUSerProfile);*/
                    }else if (playerModel.getTeamId().equals(matchModel.getTeam2())){
                        imgUSerProfile.setImageResource(R.drawable.football_player2);
                        /*Glide.with(mContext)
                                .load(R.drawable.ic_team2_tshirt)
                                .placeholder(R.drawable.ic_team2_tshirt)
                                .error(R.drawable.ic_team2_tshirt)
                                .into(imgUSerProfile);*/
                    }
                } else {
                    if (playerModel.getTeamId().equals(matchModel.getTeam1())){
                        CustomUtil.loadImageWithGlide(mContext,imgUSerProfile,ApiManager.DOCUMENTS,playerModel.getPlayerImage(),R.drawable.football_player1);
                        /*Glide.with(mContext)
                                .load(ApiManager.DOCUMENTS + playerModel.getPlayerImage())
                                .error(R.drawable.football_player1)
                                .placeholder(R.drawable.football_player1)
                                .into(imgUSerProfile);*/
                    }else if (playerModel.getTeamId().equals(matchModel.getTeam2())){
                        CustomUtil.loadImageWithGlide(mContext,imgUSerProfile,ApiManager.DOCUMENTS,playerModel.getPlayerImage(),R.drawable.football_player2);
                        /*Glide.with(mContext)
                                .load(ApiManager.DOCUMENTS + playerModel.getPlayerImage())
                                .error(R.drawable.football_player2)
                                .placeholder(R.drawable.football_player2)
                                .into(imgUSerProfile);*/
                    }
                }
            }
            else if (matchModel.getSportId().equals("3")){
                if (playerModel.getPlayerImage().equals("")) {
                    if (playerModel.getTeamId().equals(matchModel.getTeam1())){
                        imgUSerProfile.setImageResource(R.drawable.baseball_player1);
                       /* Glide.with(mContext)
                                .load(R.drawable.ic_team1_tshirt)
                                .placeholder(R.drawable.ic_team1_tshirt)
                                .error(R.drawable.ic_team1_tshirt)
                                .into(imgUSerProfile);*/
                    }else if (playerModel.getTeamId().equals(matchModel.getTeam2())){
                        imgUSerProfile.setImageResource(R.drawable.baseball_player2);
                        /*Glide.with(mContext)
                                .load(R.drawable.ic_team2_tshirt)
                                .placeholder(R.drawable.ic_team2_tshirt)
                                .error(R.drawable.ic_team2_tshirt)
                                .into(imgUSerProfile);*/
                    }
                } else {
                    if (playerModel.getTeamId().equals(matchModel.getTeam1())){
                        CustomUtil.loadImageWithGlide(mContext,imgUSerProfile,ApiManager.DOCUMENTS,playerModel.getPlayerImage(),R.drawable.baseball_player1);
                        /*Glide.with(mContext)
                                .load(ApiManager.DOCUMENTS + playerModel.getPlayerImage())
                                .error(R.drawable.football_player1)
                                .placeholder(R.drawable.football_player1)
                                .into(imgUSerProfile);*/
                    }else if (playerModel.getTeamId().equals(matchModel.getTeam2())){
                        CustomUtil.loadImageWithGlide(mContext,imgUSerProfile,ApiManager.DOCUMENTS,playerModel.getPlayerImage(),R.drawable.baseball_player2);
                        /*Glide.with(mContext)
                                .load(ApiManager.DOCUMENTS + playerModel.getPlayerImage())
                                .error(R.drawable.football_player2)
                                .placeholder(R.drawable.football_player2)
                                .into(imgUSerProfile);*/
                    }
                }
            }
            else if (matchModel.getSportId().equals("4")){
                if (playerModel.getPlayerImage().equals("")) {
                    if (playerModel.getTeamId().equals(matchModel.getTeam1())){
                        imgUSerProfile.setImageResource(R.drawable.basketball_team1);
                       /* Glide.with(mContext)
                                .load(R.drawable.ic_team1_tshirt)
                                .placeholder(R.drawable.ic_team1_tshirt)
                                .error(R.drawable.ic_team1_tshirt)
                                .into(imgUSerProfile);*/
                    }else if (playerModel.getTeamId().equals(matchModel.getTeam2())){
                        imgUSerProfile.setImageResource(R.drawable.basketball_team2);
                        /*Glide.with(mContext)
                                .load(R.drawable.ic_team2_tshirt)
                                .placeholder(R.drawable.ic_team2_tshirt)
                                .error(R.drawable.ic_team2_tshirt)
                                .into(imgUSerProfile);*/
                    }
                } else {
                    if (playerModel.getTeamId().equals(matchModel.getTeam1())){
                        CustomUtil.loadImageWithGlide(mContext,imgUSerProfile,ApiManager.DOCUMENTS,playerModel.getPlayerImage(),R.drawable.basketball_team1);
                       /* Glide.with(mContext)
                                .load(ApiManager.DOCUMENTS + playerModel.getPlayerImage())
                                .error(R.drawable.basketball_team1)
                                .placeholder(R.drawable.basketball_team1)
                                .into(imgUSerProfile);*/
                    }else if (playerModel.getTeamId().equals(matchModel.getTeam2())){
                        CustomUtil.loadImageWithGlide(mContext,imgUSerProfile,ApiManager.DOCUMENTS,playerModel.getPlayerImage(),R.drawable.basketball_team2);
                       /* Glide.with(mContext)
                                .load(ApiManager.DOCUMENTS + playerModel.getPlayerImage())
                                .error(R.drawable.basketball_team2)
                                .placeholder(R.drawable.basketball_team2)
                                .into(imgUSerProfile);*/
                    }
                }
            }

            txtPlayerName.setText(playerModel.getPlayerName());
            if (getIntent().hasExtra("joining")){
                txtLblSelBy.setText("Joining");
                txtSelBy.setText(getIntent().getStringExtra("joining"));
            }
            else {
                if (TextUtils.isEmpty(playerModel.getPlayer_percent())){
                    txtSelBy.setText("-");
                }else {
                    txtSelBy.setText(playerModel.getPlayer_percent()+"%");
                }
            }

            String team = (matchModel.getTeam1().equals(playerModel.getTeamId())) ? matchModel.getTeam1Name() : preferences.getMatchModel().getTeam2Name();
            type_player.setText(team + " - " + playerModel.getPlayerType());
            player_points.setText(playerModel.getTotalPoints());
            player_credit.setText(playerModel.getPlayerRank());
        }

        toolbar_back.setOnClickListener(v->{
            finish();
        });

        getData();
    }

    private void getData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("match_type", preferences.getMatchModel().getMatchType());
            if(type==1) {
                jsonObject.put("player_id", matchPlayerStateModel.getPlayerId());
            }else if(type==2){
                jsonObject.put("player_id", playerModel.getPlayerId());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, jsonObject.toString());
        HttpRestClient.postJSON(mContext, true, ApiManager.AFTER_MATCH_PLAYER_STATE, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("TAG", "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONArray array = responseBody.optJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        try {
                            JSONObject data = array.getJSONObject(i);
                            AfterMatchPlayerStatesModel afterMatchPlayerStatesModel = gson.fromJson(data.toString(), AfterMatchPlayerStatesModel.class);
                            list.add(afterMatchPlayerStatesModel);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter = new AfterMatchPlayerStatesAdapter(mContext,list);
                    player_list.setAdapter(adapter);
                    player_list.setLayoutManager(new LinearLayoutManager(mContext));

                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {}

        });
    }

}