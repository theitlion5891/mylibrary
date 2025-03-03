package com.fantafeat.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.fantafeat.Adapter.LeaderboardsListAdapter;
import com.fantafeat.Model.LeaderboardListModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LeaderboardListActivity extends BaseActivity {

    private ImageView imgBack;
    private RecyclerView recyclerList;
    private ArrayList<LeaderboardListModel> list;
    private LeaderboardsListAdapter adapter;

    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_list);

        imgBack=findViewById(R.id.imgBack);
        recyclerList=findViewById(R.id.recyclerList);

        list=new ArrayList<>();

        recyclerList.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        getLeaderboardList();

        imgBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void getLeaderboardList() {
        try {
            JSONObject param=new JSONObject();
            param.put("user_id",preferences.getUserModel().getId());

            HttpRestClient.postJSON(mContext, true, ApiManager.leaderboardCategoryList,param , new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {
                    LogUtil.e("GameData", "getLeaderboardList: " + responseBody.toString());
                    if (responseBody.optBoolean("status")) {

                        JSONArray data=responseBody.optJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj=data.optJSONObject(i);
                            LeaderboardListModel model=gson.fromJson(obj.toString(),LeaderboardListModel.class);
                            if (model.getId().equalsIgnoreCase("3")){
                                if (preferences.getUserModel().getIsPromoter().equalsIgnoreCase("yes")){
                                    list.add(model);
                                }
                            }else
                                list.add(model);
                        }

                        adapter = new LeaderboardsListAdapter(list, mContext, model1 -> {
                            if (model1.getId().equalsIgnoreCase("1")){
                                startActivity(new Intent(LeaderboardListActivity.this, UserLeaderboardActivity.class)
                                        .putExtra("leaderboard_id", "")
                                        .putExtra("leaderboard_title", model1.getLbListTitle())
                                );
                            }
                            else if (model1.getId().equalsIgnoreCase("2")) {
                                startActivity(new Intent(LeaderboardListActivity.this, GameLeaderboardActivity.class)
                                        .putExtra("leaderboard_id", "")
                                        .putExtra("leaderboard_title", model1.getLbListTitle())
                                );
                            }
                            else if (model1.getId().equalsIgnoreCase("3")) {}
                            else if (model1.getId().equalsIgnoreCase("4")) {
                                startActivity(new Intent(mContext, InvestmentLeaderboardActivity.class)
                                        .putExtra("title",model1.getLbListTitle()));
                            }
                        });
                        recyclerList.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailureResult(String responseBody, int code) {}
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}