package com.fantafeat.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fantafeat.Adapter.AffiliationDetailAdapter;
import com.fantafeat.Adapter.AffiliationMatchAdapter;
import com.fantafeat.Model.AffiliationDetailModel;
import com.fantafeat.Model.AffiliationMatchModal;
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

public class AffilationMatchList extends BaseActivity {

    String start_date,end_date,totalBal;
    int offset, limit;
    boolean isGetData, isApiCall;
    private TextView txtTotal,txtNoData,txtDates;
    private RecyclerView recyclerMatch;
    private ImageView toolbar_back;
    private AffiliationMatchAdapter adapter;
    private SwipeRefreshLayout affil_refresh;
    LinearLayoutManager mLinearLayoutManager;
    List<AffiliationMatchModal> affiliationDetailModelList = new ArrayList<>();

    @Override
    public void initClick() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affilation_match_list);

        Intent intent = getIntent();
        start_date = intent.getStringExtra("start_date");
        end_date = intent.getStringExtra("end_date");
        totalBal = intent.getStringExtra("totalBal");


        toolbar_back=findViewById(R.id.toolbar_back);
        txtDates=findViewById(R.id.txtDates);
        txtDates.setText("Affiliation from date "+start_date+" to "+end_date);
        txtTotal=findViewById(R.id.txtTotal);
        txtTotal.setText(totalBal);
        recyclerMatch=findViewById(R.id.recyclerMatch);
        txtNoData=findViewById(R.id.txtNoData);
        affil_refresh=findViewById(R.id.affil_refresh);

        limit = 10;
        offset = 0;
        isGetData = true;
        isApiCall = true;

        mLinearLayoutManager = new LinearLayoutManager(mContext);

        getData();

        affil_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        toolbar_back.setOnClickListener(v -> {
            finish();
        });

        recyclerMatch.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    public void matchItemClick(AffiliationMatchModal modal){
        Intent intent = new Intent(mContext, AffiliationDetailsActivity.class);
        intent.putExtra("start_date",start_date);
        intent.putExtra("end_date",end_date);
        intent.putExtra("totalBal",totalBal);
        intent.putExtra("match_id",modal.getId());
        intent.putExtra("match_name",modal.getTeam1Sname()+" VS "+modal.getTeam2Sname());
        startActivity(intent);
    }

    private void getData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",preferences.getUserModel().getId());
            jsonObject.put("from_date",start_date);
            jsonObject.put("to_date",end_date);
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
        HttpRestClient.postJSON(mContext, showProgress, ApiManager.MATCH_LIST_AFFILIATION, jsonObject, new GetApiResult() {
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
                        recyclerMatch.setVisibility(View.VISIBLE);
                    }else{
                        for (int j = 0;j < array.length(); j++) {
                            JSONObject main = array.optJSONObject(j);
                            AffiliationMatchModal affiliationDetailModel =  gson.fromJson(main.toString(), AffiliationMatchModal.class);

                            affiliationDetailModelList.add(affiliationDetailModel);
                        }

                        if (limit >= array.length() && adapter != null) {
                            if (adapter != null) {
                                adapter.updateData(affiliationDetailModelList);
                            } else {
                                adapter = new AffiliationMatchAdapter(mContext, affiliationDetailModelList);
                                recyclerMatch.setLayoutManager(mLinearLayoutManager);
                                recyclerMatch.setAdapter(adapter);
                            }
                        } else {
                            adapter = new AffiliationMatchAdapter(mContext, affiliationDetailModelList);
                            recyclerMatch.setLayoutManager(mLinearLayoutManager);
                            recyclerMatch.setAdapter(adapter);
                        }

                    }
                }else {
                    txtNoData.setVisibility(View.VISIBLE);
                    recyclerMatch.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

}