package com.fantafeat.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fantafeat.Adapter.AffiliationDetailAdapter;
import com.fantafeat.Model.AffiliationDetailModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
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

public class AffiliationDetailFragment extends BaseFragment {

    RecyclerView affil_detail_list;
    SwipeRefreshLayout affil_refresh;
    String start_date,end_date;
    List<AffiliationDetailModel> affiliationDetailModelList = new ArrayList<>();
    AffiliationDetailAdapter adapter;
    int offset, limit;
    LinearLayoutManager mLinearLayoutManager;
    boolean isGetData, isApiCall;

    public AffiliationDetailFragment(String start_date, String end_date) {
        this.start_date = start_date;
        this.end_date = end_date;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_affiliation_detail, container, false);
        initFragment(view);
        initToolBar(view, "Affiliation Details", true);
        return view;
    }

    @Override
    public void initControl(View view) {
        limit = 10;
        offset = 0;
        isGetData = true;
        isApiCall = true;
        mLinearLayoutManager = new LinearLayoutManager(mContext);


        affil_detail_list = view.findViewById(R.id.affil_detail_list);
        affil_refresh = view.findViewById(R.id.affil_refresh);
        getData();
    }

    private void getData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ref_code",preferences.getUserModel().getRefNo());
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
        HttpRestClient.postJSON(mContext, showProgress, ApiManager.AFFILIATION_DETAILS, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (affil_refresh != null && affil_refresh.isRefreshing()) {
                    affil_refresh.setRefreshing(false);
                }
                isApiCall = true;
                if(responseBody.optBoolean("status")){
                    LogUtil.e(TAG, "onSuccessResult: "+responseBody.toString() );
                    JSONArray array = responseBody.optJSONArray("data");

                    if (array.length() < limit) {
                        isGetData = false;
                        offset = 0;
                    }else{
                        offset += limit;
                    }

                    if(array.length()==0 && adapter==null){
                        CustomUtil.showTopSneakError(mContext,"No details to show");
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