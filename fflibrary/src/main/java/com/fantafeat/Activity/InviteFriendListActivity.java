package com.fantafeat.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantafeat.Adapter.ReferLevelAdapter;
import com.fantafeat.Model.UserRefLevelModel;
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

public class InviteFriendListActivity extends BaseActivity {

    private TextView mToolbarTitle;
    private ImageView mToolbarBack;
    private TextView txtNoData;
    private RecyclerView referral_list;
    private ReferLevelAdapter adapter;
    private SwipeRefreshLayout referral_refresh;
    int offset, limit, mLevel;
    boolean isGetData, isApiCall;
    ArrayList<UserRefLevelModel> arrayList;

    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend_list);

        initToolbar();
        initView();
    }

    private void getReferalCount() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("referCode", preferences.getUserModel().getRefNo());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, false, ApiManager.GET_REF_COUNT, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                if (responseBody.optBoolean("status")) {
                    JSONObject data = responseBody.optJSONObject("data");
                    int level1 = CustomUtil.convertInt(data.optString("level_1"));
                   /* int level2 = CustomUtil.convertInt(data.optString("level_2"));
                    int level3 = CustomUtil.convertInt(data.optString("level_3"));
                    int totalLevel = level1 + level2 + level3;

                    if (!preferences.getUserModel().getIsPromoter().equalsIgnoreCase("Yes")) {

                        referrals_tab.getTabAt(0).setText("Level 1 (" + level1 + ")");
                        referrals_tab.getTabAt(1).setText("Level 2 (" + level2 + ")");
                        referrals_tab.getTabAt(2).setText("Level 3 (" + level3 + ")");
                        referral_total_points.setText("Referral : " + totalLevel);
                    } else {
                        referral_total_points.setText("Referral : " + level1);
                    }*/
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });

    }

    private void getData() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("referCode", preferences.getUserModel().getRefNo());
            jsonObject.put("level", mLevel);
            jsonObject.put("offset", offset);
            jsonObject.put("limit", limit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "getData: " + offset + "   " + limit);
        boolean showProgress = true;
        if (referral_refresh != null && referral_refresh.isRefreshing()) {
            showProgress = false;
        }
        isApiCall = false;

        HttpRestClient.postJSON(mContext, showProgress, ApiManager.GET_REF_USER_DETAILS, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("resp",responseBody+" ");
                isApiCall = true;
               // progressBar.setVisibility(View.GONE);
                if (referral_refresh != null && referral_refresh.isRefreshing()) {
                    referral_refresh.setRefreshing(false);
                }
                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    try {
                        JSONArray jsonArray = responseBody.getJSONArray("data");

                        if (jsonArray.length() < limit) {
                            isGetData = false;
                            offset = 0;
                        } else {
                            offset += limit;
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.optJSONObject(i);
                            UserRefLevelModel userRefLevelModel = gson.fromJson(object.toString(), UserRefLevelModel.class);
                            arrayList.add(userRefLevelModel);
                        }
                        if (limit >= jsonArray.length() && adapter != null) {
                            adapter.updateData(arrayList);
                        } else {
                            adapter = new ReferLevelAdapter(mContext, arrayList);
                            referral_list.setAdapter(adapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                checkData();
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
               // progressBar.setVisibility(View.GONE);
                if (referral_refresh != null && referral_refresh.isRefreshing()) {
                    referral_refresh.setRefreshing(false);
                }
            }
        });
    }

    private void checkData(){
        if (arrayList.size()==0){
            txtNoData.setVisibility(View.VISIBLE);
            referral_list.setVisibility(View.GONE);
        }else {
            txtNoData.setVisibility(View.GONE);
            referral_list.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        limit = 20;
        offset = 0;
        mLevel = 1;
        isGetData = true;
        isApiCall = true;
        arrayList = new ArrayList<>();

        referral_refresh=findViewById(R.id.referral_refresh);
        referral_list=findViewById(R.id.referral_list);
        txtNoData=findViewById(R.id.txtNoData);

        LinearLayoutManager manager=new LinearLayoutManager(InviteFriendListActivity.this,LinearLayoutManager.VERTICAL,false);
        referral_list.setLayoutManager(manager);

        getData();

        referral_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null &&
                        manager.findLastCompletelyVisibleItemPosition() == arrayList.size() - 1
                        && isGetData && isApiCall) {
                    if (arrayList.size() > 0) {
                       // progressBar.setVisibility(View.VISIBLE);
                        getData();
                    }
                }
            }
        });

        referral_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList = new ArrayList<>();
                limit = 20;
                offset = 0;
                getData();
            }
        });
       // getReferalCount();
        //recyclerFrds.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

    }

    private void initToolbar() {
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mToolbarBack = findViewById(R.id.toolbar_back);
        mToolbarBack.setOnClickListener(v -> finish());
        mToolbarTitle.setText("Invited Friends");
    }
}