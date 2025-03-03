package com.fantafeat.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantafeat.Adapter.UserDetailTeamAdapter;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.Model.UserLeaderboardList;
import com.fantafeat.Model.UserTeamModal;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.BottomSheetTeam;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailActivity extends BaseActivity implements View.OnClickListener  {

    private ImageView toolbar_back;
    private CircleImageView imgUser;
    private RecyclerView recyclerMatch;
    private LinearLayout layNoData;
    private TextView toolbar_title,txtUserName,txtUserTeamName,txtTotalMatch,txtPoint,txtRankCnt;
    private UserLeaderboardList.Datum item;
    private String selectedSeriesTitle="",selectedSeriesId="";
    private List<UserTeamModal> list;

    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        initData();
    }

    private void initData() {
        toolbar_back=findViewById(R.id.toolbar_back);
        toolbar_title=findViewById(R.id.toolbar_title);
        txtUserName=findViewById(R.id.txtUserName);
        txtUserTeamName=findViewById(R.id.txtUserTeamName);
        txtTotalMatch=findViewById(R.id.txtTotalMatch);
        txtRankCnt=findViewById(R.id.txtRankCnt);
        recyclerMatch=findViewById(R.id.recyclerMatch);
        layNoData=findViewById(R.id.layNoData);
        txtPoint=findViewById(R.id.txtPoint);
        imgUser=findViewById(R.id.imgUser);
        toolbar_title.setSelected(true);
        toolbar_back.setOnClickListener(this);

        if (getIntent().hasExtra("leaderboard_item")){
            item= (UserLeaderboardList.Datum) getIntent().getSerializableExtra("leaderboard_item");
            txtUserName.setText(item.getDisplayName());
            txtUserTeamName.setText(item.getUserTeamName());
            if (TextUtils.isEmpty(item.getTotalRank())){
                txtRankCnt.setText("-");
            }else {
                txtRankCnt.setText("#"+item.getTotalRank());
            }
            if (TextUtils.isEmpty(item.getTotalPoint())){
                txtPoint.setText("-");
            }else {
                txtPoint.setText(item.getTotalPoint());
            }

            CustomUtil.loadImageWithGlide(mContext,imgUser, MyApp.imageBase + MyApp.user_img,item.getUserImg(),
                    R.drawable.user_image);
        }
        if (getIntent().hasExtra("selectedSeriesTitle")){
            selectedSeriesTitle= getIntent().getStringExtra("selectedSeriesTitle");
            toolbar_title.setText(selectedSeriesTitle);
        }
        if (getIntent().hasExtra("selectedSeriesId")){
            selectedSeriesId= getIntent().getStringExtra("selectedSeriesId");
        }
        getData();
    }

    private void getData() {
        list=new ArrayList<>();
        try {
            JSONObject param=new JSONObject();
            param.put("lb_master_type_id",selectedSeriesId);
            param.put("user_id",item.getUserId());

            HttpRestClient.postJSON(mContext, true, ApiManager.userLeaderboardDetailslist, param, new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {
                    LogUtil.e("TAG", "onSuccessResult Detail: " + responseBody.toString() );
                    if (responseBody.optBoolean("status")){
                        JSONArray data=responseBody.optJSONArray("data");
                        if (data!=null && data.length()>0){
                            recyclerMatch.setVisibility(View.VISIBLE);
                            layNoData.setVisibility(View.GONE);
                            txtTotalMatch.setText(""+data.length());
                            for (int i=0;i<data.length();i++){
                                JSONObject object=data.optJSONObject(i);
                                UserTeamModal teamModal=gson.fromJson(object.toString(), UserTeamModal.class);
                                list.add(teamModal);
                            }
                            recyclerMatch.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
                            UserDetailTeamAdapter adapter=new UserDetailTeamAdapter(mContext,list, position -> {
                                getTeamDetail(position);
                            },false);
                            recyclerMatch.setAdapter(adapter);
                        }else {
                            recyclerMatch.setVisibility(View.GONE);
                            layNoData.setVisibility(View.VISIBLE);
                        }
                    }else {
                        recyclerMatch.setVisibility(View.GONE);
                        layNoData.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailureResult(String responseBody, int code) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getTeamDetail(int position) {
        final List<PlayerModel> playerModelList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        UserTeamModal item=list.get(position);
        try {
            jsonObject.put("user_id",item.getUserId());
            jsonObject.put("match_id", item.getMatchId());
            jsonObject.put("temp_team_id", item.getTempTeamId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e("resp","param: "+jsonObject);
        HttpRestClient.postJSON(mContext, true, ApiManager.TEMP_TEMP_DETAIL_BY_ID, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult Team: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONArray data = responseBody.optJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.optJSONObject(i);
                        PlayerModel playerModel = gson.fromJson(object.toString(), PlayerModel.class);
                        playerModelList.add(playerModel);
                    }

                    MatchModel matchModel=new MatchModel();
                    matchModel.setTeam1(item.getTeam1());
                    matchModel.setTeam2(item.getTeam2());
                    matchModel.setLeagueId(item.getLeagueId());
                    matchModel.setTeamAXi("Yes");
                    matchModel.setTeamBXi("Yes");
                    matchModel.setTeam1Sname(item.getTeam1Sname());
                    matchModel.setTeam2Sname(item.getTeam2Sname());
                    matchModel.setTeam1Name(item.getTeam1Name());
                    matchModel.setTeam2Name(item.getTeam2Name());
                    matchModel.setId(item.getMatchId());
                    matchModel.setConDisplayType("1");
                    matchModel.setMatchType(item.getMatchType());
                    matchModel.setSportId(item.getSport_id());
                    matchModel.setRegularMatchStartTime(item.getRegularMatchStartTime());
                    matchModel.setMatchStatus("completed");

                    //preferences.setMatchModel(matchModel);

                    BottomSheetTeam bottomSheetTeam = new BottomSheetTeam(mContext, item.getTempTeamName(), item.getTempTeamId(),
                            playerModelList,false,matchModel);
                    bottomSheetTeam.show(((UserDetailActivity)mContext).getSupportFragmentManager(),
                            BottomSheetTeam.TAG);
                } else {
                    CustomUtil.showTopSneakWarning(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.toolbar_back){
            finish();
        }
    }
}