package com.fantafeat.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantafeat.Adapter.AffiliationDetailAdapter;
import com.fantafeat.Model.AffiliationDetailModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AffiliationDetailsActivity extends BaseActivity {

    RecyclerView affil_detail_list;
    ImageView toolbar_back;
    TextView txtNoData,toolbar_title;
    SwipeRefreshLayout affil_refresh;
    String start_date,end_date,match_id,match_name;
    List<AffiliationDetailModel> affiliationDetailModelList = new ArrayList<>();
    AffiliationDetailAdapter adapter;
    int offset, limit;
    LinearLayoutManager mLinearLayoutManager;
    boolean isGetData, isApiCall;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        setContentView(R.layout.activity_affiliation_details);

        Intent intent = getIntent();
        start_date = intent.getStringExtra("start_date");
        end_date = intent.getStringExtra("end_date");
        match_id = intent.getStringExtra("match_id");
        match_name = intent.getStringExtra("match_name");

        limit = 10;
        offset = 0;
        isGetData = true;
        isApiCall = true;
        mLinearLayoutManager = new LinearLayoutManager(mContext);

        affil_detail_list = findViewById(R.id.affil_detail_list);
        affil_refresh = findViewById(R.id.affil_refresh);
        toolbar_back = findViewById(R.id.toolbar_back);
        txtNoData = findViewById(R.id.txtNoData);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(match_name);
        initClick();
        getData();
    }

    private void getData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",preferences.getUserModel().getId());
            jsonObject.put("match_id",match_id);
            jsonObject.put("offset",offset);
            jsonObject.put("limit",limit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        isApiCall = false;

        boolean showProgress = true;
        if (affil_refresh != null && affil_refresh.isRefreshing()) {
            showProgress = false;
        }
        LogUtil.e(TAG,jsonObject.toString());
        HttpRestClient.postJSON(mContext, showProgress, ApiManager.MATCH_WISE_AFFILIATION, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (affil_refresh != null && affil_refresh.isRefreshing()) {
                    affil_refresh.setRefreshing(false);
                }
                isApiCall = true;
                LogUtil.e(TAG, "onSuccessResult: "+responseBody.toString() );

                if(responseBody.optBoolean("status")){

                    JSONArray array = responseBody.optJSONArray("data");

                    if (array.length() < limit) {
                        isGetData = false;
                        offset = 0;
                    }else{
                        offset += limit;
                    }

                    if(array.length()==0 && adapter==null){
                       // CustomUtil.showTopSneakError(mContext,"No details to show");
                        txtNoData.setVisibility(View.GONE);
                        affil_detail_list.setVisibility(View.VISIBLE);
                    }else{
                        for (int j = 0;j < array.length(); j++) {
                            JSONObject main = array.optJSONObject(j);
                            AffiliationDetailModel affiliationDetailModel =  gson.fromJson(main.toString(), AffiliationDetailModel.class);
                            try {
                                String bb11_commision = String.valueOf(main.getDouble("bb11_commision"));
                                affiliationDetailModel.setBb11Commision(bb11_commision);
                            }catch (Exception e){
                                String bb11_commision = "0";
                                LogUtil.e(TAG, String.valueOf(e));
                            }
                            affiliationDetailModelList.add(affiliationDetailModel);
                        }

                        if (limit >= array.length() && adapter != null) {
                            if (adapter != null) {
                                adapter.updateData(affiliationDetailModelList);
                            } else {
                                adapter = new AffiliationDetailAdapter(mContext, affiliationDetailModelList);
                                affil_detail_list.setLayoutManager(mLinearLayoutManager);
                                affil_detail_list.setAdapter(adapter);
                            }
                        } else {
                            adapter = new AffiliationDetailAdapter(mContext, affiliationDetailModelList);
                            affil_detail_list.setLayoutManager(mLinearLayoutManager);
                            affil_detail_list.setAdapter(adapter);
                        }

                    }
                }else {
                    txtNoData.setVisibility(View.VISIBLE);
                    affil_detail_list.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });

    }

    @Override
    public void initClick() {
        affil_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        toolbar_back.setOnClickListener(v->{
            finish();
        });

        affil_detail_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (mLinearLayoutManager != null &&
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition() == affiliationDetailModelList.size() - 1
                        && isGetData && isApiCall) {
                    //Load more online data
                    LogUtil.e(TAG, "on Scroll");
                    if (affiliationDetailModelList.size() > 0) {
                        //affil_refresh.setRefreshing(true);
                        getData();
                    }
                }
            }
        });
    }
}