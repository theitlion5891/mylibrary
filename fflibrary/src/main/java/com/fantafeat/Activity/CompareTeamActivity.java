package com.fantafeat.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantafeat.Adapter.TeamCompareAdapter;
import com.fantafeat.Model.LeaderBoardModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompareTeamActivity extends BaseActivity {

    ArrayList<LeaderBoardModel> compare_list;
    List<PlayerModel> team1 = new ArrayList<>();
    List<PlayerModel> team2 = new ArrayList<>();
    List<PlayerModel> team1_diff = new ArrayList<>();
    List<PlayerModel> team2_diff = new ArrayList<>();
    Map<Integer, PlayerModel> team1_map = new HashMap<>();
    Map<Integer, PlayerModel> team2_map = new HashMap<>();

    LinearLayout difference_text_linear;
    TextView player1_total_points, player2_total_points, player1_id, player1_name, player2_id, player2_name,
            lead_by_text, c_vc_text, diff_player_text, same_player_text,toolbar_title;
    CircleImageView player1_img, player2_img;
    RecyclerView c_vc_recyclerCompare, diff_recyclerCompare, same_recyclerCompare;
    List<PlayerModel> left_c_vc = new ArrayList<>();
    List<PlayerModel> right_c_vc = new ArrayList<>();
    List<PlayerModel> left_common = new ArrayList<>();
    List<PlayerModel> right_common = new ArrayList<>();
    List<PlayerModel> left_diff = new ArrayList<>();
    List<PlayerModel> right_diff = new ArrayList<>();
    ImageView toolbar_back;
    TeamCompareAdapter adapter;
    float left_cv_c, right_cv_c, common_player_point, left_diff_point, right_diff_point;
    String sportsId="";

    @Override
    public void initClick() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        setContentView(R.layout.fragment_compare_team);

        if (getIntent().hasExtra("compareList")){
            compare_list= (ArrayList<LeaderBoardModel>) getIntent().getSerializableExtra("compareList");
            sportsId=getIntent().getStringExtra("sportsId");
        }

        initData();
        initClic();
        getTeamDetail();

    }

    public void getTeamDetail() {
        //final List<PlayerModel> playerModelList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("match_id", compare_list.get(0).getMatchId());
            jsonObject.put("temp_team_id", compare_list.get(0).getTempTeamId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, false, ApiManager.TEMP_TEMP_DETAIL_BY_ID, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONArray data = responseBody.optJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.optJSONObject(i);
                        PlayerModel playerModel = gson.fromJson(object.toString(), PlayerModel.class);
                        team1.add(playerModel);
                        team1_map.put(i, playerModel);
                    }
                    getTeam2Detail();
                    /*BottomSheetTeam bottomSheetTeam = new BottomSheetTeam(mContext, tempTeamName, playerModelList);
                    bottomSheetTeam.show(((HomeActivity) mContext).getSupportFragmentManager(),
                            BottomSheetTeam.TAG);*/
                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });

    }

    public void getTeam2Detail() {
        //final List<PlayerModel> playerModelList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("match_id", compare_list.get(1).getMatchId());
            jsonObject.put("temp_team_id", compare_list.get(1).getTempTeamId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.TEMP_TEMP_DETAIL_BY_ID, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONArray data = responseBody.optJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.optJSONObject(i);
                        PlayerModel playerModel = gson.fromJson(object.toString(), PlayerModel.class);
                        team2.add(playerModel);
                        team2_map.put(i, playerModel);

                    }

                    compareDisplay();
                    /*BottomSheetTeam bottomSheetTeam = new BottomSheetTeam(mContext, tempTeamName, playerModelList);
                    bottomSheetTeam.show(((HomeActivity) mContext).getSupportFragmentManager(),
                            BottomSheetTeam.TAG);*/
                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });

    }

    private void compareDisplay() {
        left_cv_c = right_cv_c = common_player_point = left_diff_point = right_diff_point = 0;


        player1_total_points.setText(compare_list.get(0).getTotalPoint());
        player2_total_points.setText(compare_list.get(1).getTotalPoint());

        player1_id.setText("#" + compare_list.get(0).getTotalRank());
        player2_id.setText("#" + compare_list.get(1).getTotalRank());

        player1_name.setText(compare_list.get(0).getTempTeamName());
        player2_name.setText(compare_list.get(1).getTempTeamName());

        //CustomUtil.loadImageWithGlide(mContext,player1_img, ApiManager.PROFILE_IMG,compare_list.get(0).getUser_img(),R.drawable.ic_team1_placeholder);
       // CustomUtil.loadImageWithGlide(mContext,player2_img, ApiManager.PROFILE_IMG,compare_list.get(1).getUser_img(),R.drawable.ic_team1_placeholder);

        float player1_score = CustomUtil.convertFloat(compare_list.get(0).getTotalPoint());
        float player2_score = CustomUtil.convertFloat(compare_list.get(1).getTotalPoint());

        int age=18,age1=18;
        if (!TextUtils.isEmpty(compare_list.get(0).getDob()) && !compare_list.get(0).getDob().equals("0000-00-00")){
            age=CustomUtil.getAge(compare_list.get(0).getDob());
        }

        if (!TextUtils.isEmpty(compare_list.get(1).getDob()) && !compare_list.get(1).getDob().equals("0000-00-00")){
            age1=CustomUtil.getAge(compare_list.get(1).getDob());
        }

        if (TextUtils.isEmpty(compare_list.get(0).getUser_img())) {
            if (compare_list.get(0).getGender().equals("Male")){
                if (age>=18 && age <= 25){
                    player1_img.setImageResource(R.drawable.male_18_below);
                }else if (age>25 && age<=40){
                    player1_img.setImageResource(R.drawable.male_25_above);
                }else {
                    player1_img.setImageResource(R.drawable.male_40_above);
                }
            }else {
                if (age>=18 && age <= 25){
                    player1_img.setImageResource(R.drawable.female_18_below);
                }else if (age>25 && age<=40){
                    player1_img.setImageResource(R.drawable.female_25_above);
                }else {
                    player1_img.setImageResource(R.drawable.female_40_above);
                }
            }
        }
        else {
            if (compare_list.get(0).getGender().equals("Male")){
                if (age>=18 && age <= 25){
                    CustomUtil.loadImageWithGlide(mContext,player1_img,ApiManager.PROFILE_IMG,compare_list.get(0).getUser_img(),R.drawable.male_18_below);
                }else if (age>25 && age<=40){
                    CustomUtil.loadImageWithGlide(mContext,player1_img,ApiManager.PROFILE_IMG,compare_list.get(0).getUser_img(),R.drawable.male_25_above);
                }else {
                    CustomUtil.loadImageWithGlide(mContext,player1_img,ApiManager.PROFILE_IMG,compare_list.get(0).getUser_img(),R.drawable.male_40_above);
                }
            }else {
                if (age>=18 && age <= 25){
                    CustomUtil.loadImageWithGlide(mContext,player1_img,ApiManager.PROFILE_IMG,compare_list.get(0).getUser_img(),R.drawable.female_18_below);
                }else if (age>25 && age<=40){
                    CustomUtil.loadImageWithGlide(mContext,player1_img,ApiManager.PROFILE_IMG,compare_list.get(0).getUser_img(),R.drawable.female_25_above);
                }else {
                    CustomUtil.loadImageWithGlide(mContext,player1_img,ApiManager.PROFILE_IMG,compare_list.get(0).getUser_img(),R.drawable.female_40_above);
                }
            }
        }

        if (TextUtils.isEmpty(compare_list.get(1).getUser_img())) {
            if (compare_list.get(1).getGender().equals("Male")){
                if (age1>=18 && age1 <= 25){
                    player2_img.setImageResource(R.drawable.male_18_below);
                }else if (age1>25 && age1<=40){
                    player2_img.setImageResource(R.drawable.male_25_above);
                }else {
                    player2_img.setImageResource(R.drawable.male_40_above);
                }
            }else {
                if (age1>=18 && age1 <= 25){
                    player2_img.setImageResource(R.drawable.female_18_below);
                }else if (age1>25 && age1<=40){
                    player2_img.setImageResource(R.drawable.female_25_above);
                }else {
                    player2_img.setImageResource(R.drawable.female_40_above);
                }
            }
        }
        else {
            if (compare_list.get(1).getGender().equals("Male")){
                if (age1>=18 && age1 <= 25){
                    CustomUtil.loadImageWithGlide(mContext,player2_img,ApiManager.PROFILE_IMG,compare_list.get(1).getUser_img(),R.drawable.male_18_below);
                }else if (age1>25 && age1<=40){
                    CustomUtil.loadImageWithGlide(mContext,player2_img,ApiManager.PROFILE_IMG,compare_list.get(1).getUser_img(),R.drawable.male_25_above);
                }else {
                    CustomUtil.loadImageWithGlide(mContext,player2_img,ApiManager.PROFILE_IMG,compare_list.get(1).getUser_img(),R.drawable.male_40_above);
                }
            }else {
                if (age1>=18 && age1 <= 25){
                    CustomUtil.loadImageWithGlide(mContext,player2_img,ApiManager.PROFILE_IMG,compare_list.get(1).getUser_img(),R.drawable.female_18_below);
                }else if (age1>25 && age1<=40){
                    CustomUtil.loadImageWithGlide(mContext,player2_img,ApiManager.PROFILE_IMG,compare_list.get(1).getUser_img(),R.drawable.female_25_above);
                }else {
                    CustomUtil.loadImageWithGlide(mContext,player2_img,ApiManager.PROFILE_IMG,compare_list.get(1).getUser_img(),R.drawable.female_40_above);
                }
            }
        }

        if (player1_score - player2_score > 0) {
            lead_by_text.setText(compare_list.get(0).getTempTeamName() + " leaded by " + (player1_score - player2_score) + " points.");
        } else {
            lead_by_text.setText(compare_list.get(1).getTempTeamName() + " leaded by " + (player2_score - player1_score) + " points.");
        }

        int maxTeam=11;
        if (sportsId.equals("4")){
            maxTeam=8;
        }else if (sportsId.equals("3")){
            maxTeam=9;
        }else if (sportsId.equals("7")){
            maxTeam=7;
        }

        for (int i = 0; i < maxTeam; i++) {
            //Log.e(TAG, "compareDisplay: "+i+" "+team1.get(i).getIsCaptain()+" "+team1.get(i).getIsWiseCaptain() );
            if (team1.get(i).getIsCaptain().equals("Yes")) {
                left_c_vc.add(team1.get(i));
                left_cv_c += CustomUtil.convertFloat(team1.get(i).getTotalPoints());
                team1_map.put(i, null);
                LogUtil.e(TAG, "compareDisplay: "+team1.get(i).getPlayerName() );
            }

            if (team2.get(i).getIsCaptain().equals("Yes")) {
                right_c_vc.add(team2.get(i));
                right_cv_c += CustomUtil.convertFloat(team2.get(i).getTotalPoints());
                team2_map.put(i,null);
                LogUtil.e(TAG, "compareDisplay: "+team2.get(i).getPlayerName() );
            }
        }
        for (int i = 0; i < maxTeam; i++) {
            //Log.e(TAG, "compareDisplay: "+i+" "+team2.get(i).getIsCaptain()+" "+team2.get(i).getIsWiseCaptain() );
            if (team1.get(i).getIsWiseCaptain().equals("Yes")) {
                left_c_vc.add(team1.get(i));
                left_cv_c += CustomUtil.convertFloat(team1.get(i).getTotalPoints());
                team1_map.put(i, null);
                LogUtil.e(TAG, "compareDisplay: "+team1.get(i).getPlayerName() );
            }

            if (team2.get(i).getIsWiseCaptain().equals("Yes")) {
                right_c_vc.add(team2.get(i));
                right_cv_c += CustomUtil.convertFloat(team2.get(i).getTotalPoints());
                team2_map.put(i,null);
                LogUtil.e(TAG, "compareDisplay: "+team2.get(i).getPlayerName() );
            }

        }

        if (left_cv_c - right_cv_c == 0) {
            c_vc_text.setText("C & CV of both teams have same "+left_cv_c+" Points.");
        }else if (left_cv_c - right_cv_c > 0) {
            c_vc_text.setText(compare_list.get(0).getTempTeamName() + "'s C & CV lead by " + (left_cv_c - right_cv_c) + " Points.");
        } else {
            c_vc_text.setText(compare_list.get(1).getTempTeamName() + "'s C & CV lead by " + (right_cv_c - left_cv_c) + " Points.");
        }

        adapter = new TeamCompareAdapter(mContext, left_c_vc, right_c_vc);
        c_vc_recyclerCompare.setAdapter(adapter);
        c_vc_recyclerCompare.setLayoutManager(new LinearLayoutManager(mContext));

        //===========common===========
        for (int i = 0; i < maxTeam; i++) {
            for (int j = 0; j < maxTeam; j++) {
                if (team1.get(i).getPlayerId().equals(team2.get(j).getPlayerId())
                        && !team1.get(i).getIsCaptain().equals("Yes")
                        && !team2.get(j).getIsCaptain().equals("Yes")
                        && !team1.get(i).getIsWiseCaptain().equals("Yes")
                        && !team2.get(j).getIsWiseCaptain().equals("Yes")) {
                    common_player_point += CustomUtil.convertFloat(team1.get(i).getTotalPoints());
                    left_common.add(team1.get(i));
                    team1_map.put(i, null);
                    right_common.add(team2.get(j));
                    team2_map.put(j, null);
                }
            }
        }

        same_player_text.setText("Common Player " + common_player_point + " Points.");

        Collections.sort(left_common, new PointsUp());
        Collections.sort(right_common, new PointsUp());

        adapter = new TeamCompareAdapter(mContext, left_common, right_common);
        same_recyclerCompare.setAdapter(adapter);
        same_recyclerCompare.setLayoutManager(new LinearLayoutManager(mContext));


//===========difference===========
        for (Map.Entry<Integer, PlayerModel> entry : team1_map.entrySet()) {
            if (entry.getValue() != null) {
                team1_diff.add(entry.getValue());
                LogUtil.e(TAG, "compareDisplay: "+entry.getValue().getPlayerName() );
                left_diff_point += CustomUtil.convertFloat(entry.getValue().getTotalPoints());
            }
        }
        for (Map.Entry<Integer, PlayerModel> entry : team2_map.entrySet()) {
            if (entry.getValue() != null) {
                LogUtil.e(TAG, "compareDisplay: "+entry.getValue().getPlayerName() );
                team2_diff.add(entry.getValue());
                right_diff_point += CustomUtil.convertFloat(entry.getValue().getTotalPoints());
            }
        }

        if(left_diff_point - right_diff_point == 0){
            diff_player_text.setText(compare_list.get(0).getTempTeamName() + "'s & "+compare_list.get(1).getTempTeamName()+ "'s Players have same "+left_diff_point +" Points.");
        }else if (left_diff_point - right_diff_point > 0) {
            diff_player_text.setText(compare_list.get(0).getTempTeamName() + "'s Players lead by " + (left_diff_point - right_diff_point) + " Points.");
        } else {
            diff_player_text.setText(compare_list.get(1).getTempTeamName() + "'s Players lead by " + (right_diff_point - left_diff_point) + " Points.");
        }

        Collections.sort(team1_diff, new PointsUp());
        Collections.sort(team2_diff, new PointsUp());

        if (team1_diff.size() == 0 && team2_diff.size() == 0) {
            difference_text_linear.setVisibility(View.GONE);
            diff_recyclerCompare.setVisibility(View.GONE);
        } else {
            adapter = new TeamCompareAdapter(mContext, team1_diff, team2_diff);
            diff_recyclerCompare.setAdapter(adapter);
            diff_recyclerCompare.setLayoutManager(new LinearLayoutManager(mContext));
        }
    }

    private void initClic() {
        toolbar_back.setOnClickListener(v->{
            finish();
        });
    }

    private void initData() {
        toolbar_back=findViewById(R.id.toolbar_back);
        toolbar_title=findViewById(R.id.toolbar_title);
        toolbar_title.setText("Teams Compare");

        player1_total_points = findViewById(R.id.player1_total_points);
        player2_total_points = findViewById(R.id.player2_total_points);
        player1_id = findViewById(R.id.player1_id);
        player1_name = findViewById(R.id.player1_name);
        player2_id = findViewById(R.id.player2_id);
        player2_name = findViewById(R.id.player2_name);
        lead_by_text = findViewById(R.id.lead_by_text);
        c_vc_text = findViewById(R.id.c_vc_text);
        player1_img = findViewById(R.id.player1_img);
        player2_img = findViewById(R.id.player2_img);
        diff_player_text = findViewById(R.id.diff_player_text);
        same_player_text = findViewById(R.id.same_player_text);
        c_vc_recyclerCompare = findViewById(R.id.c_vc_recyclerCompare);
        diff_recyclerCompare = findViewById(R.id.diff_recyclerCompare);
        same_recyclerCompare = findViewById(R.id.same_recyclerCompare);
        difference_text_linear = findViewById(R.id.difference_text_linear);
    }

    public class PointsUp implements Comparator<PlayerModel> {
        @Override
        public int compare(PlayerModel o1, PlayerModel o2) {
            return Float.compare(CustomUtil.convertFloat(o2.getTotalPoints()), CustomUtil.convertFloat(o1.getTotalPoints()));
        }
    }

}