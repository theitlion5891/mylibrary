package com.fantafeat.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fantafeat.Adapter.ReferLevelAdapter;
import com.fantafeat.Model.UserRefLevelModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.PrefConstant;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyReferralsFragment extends BaseFragment {

    private SwipeRefreshLayout referral_refresh;
    private RecyclerView referral_list;
    private TextView total_earning, referral_total_points;
    private TextView buzz;
    private TabLayout referrals_tab;
    private LinearLayout bottom_buzz;

    LinearLayoutManager linearLayoutManager;

    RelativeLayout progressBar;
    ArrayList<UserRefLevelModel> arrayList;

    ReferLevelAdapter adapter;

    int offset, limit, mLevel;
    boolean isGetData, isApiCall;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_referrals, container, false);
        initFragment(view);
        initToolBar(view, "My Referrals", true);
        return view;
    }

    @Override
    public void initControl(View view) {

        limit = 20;
        offset = 0;
        mLevel = 1;
        isGetData = true;
        isApiCall = true;
        arrayList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(mContext);
        getData();
        getReferalCount();


        referrals_tab = view.findViewById(R.id.referrals_tab);
        buzz = view.findViewById(R.id.buzz);
        total_earning = view.findViewById(R.id.total_earning);
        referral_list = view.findViewById(R.id.referral_list);
        referral_refresh = view.findViewById(R.id.referral_refresh);
        progressBar = view.findViewById(R.id.referral_progress);
        bottom_buzz = view.findViewById(R.id.bottom_buzz);
        referral_total_points = view.findViewById(R.id.referral_total_points);

        referrals_tab.addTab(referrals_tab.newTab().setText("Level 1"));
        referrals_tab.addTab(referrals_tab.newTab().setText("Level 2"));
        referrals_tab.addTab(referrals_tab.newTab().setText("Level 3"));

        if (preferences.getUserModel().getIsPromoter().equalsIgnoreCase("Yes")) {
            referrals_tab.setVisibility(View.GONE);
        } else {
            referrals_tab.setVisibility(View.VISIBLE);
        }

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
                    int level2 = CustomUtil.convertInt(data.optString("level_2"));
                    int level3 = CustomUtil.convertInt(data.optString("level_3"));
                    int totalLevel = level1 + level2 + level3;

                    if (!preferences.getUserModel().getIsPromoter().equalsIgnoreCase("Yes")) {

                        referrals_tab.getTabAt(0).setText("Level 1 (" + level1 + ")");
                        referrals_tab.getTabAt(1).setText("Level 2 (" + level2 + ")");
                        referrals_tab.getTabAt(2).setText("Level 3 (" + level3 + ")");
                        referral_total_points.setText("Referral : " + totalLevel);
                    } else {
                        referral_total_points.setText("Referral : " + level1);
                    }
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
                isApiCall = true;
                progressBar.setVisibility(View.GONE);
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
                            referral_list.setLayoutManager(linearLayoutManager);
                            referral_list.setAdapter(adapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                progressBar.setVisibility(View.GONE);
                if (referral_refresh != null && referral_refresh.isRefreshing()) {
                    referral_refresh.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void initClick() {
        buzz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valid_buzz()) {
                    CustomUtil.showTopSneakError(mContext, "You have already buzzed your friends!");
                } else {
                    call_buzz_api();
                }
            }
        });

        referral_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList = new ArrayList<>();
                getData();
            }
        });

        referral_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager != null &&
                        linearLayoutManager.findLastCompletelyVisibleItemPosition() == arrayList.size() - 1
                        && isGetData && isApiCall) {
                    //Load more online data
                    if (arrayList.size() > 0) {
                        progressBar.setVisibility(View.VISIBLE);
                        getData();
                    }
                }
            }
        });
        referrals_tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isApiCall = true;
                isGetData = true;
                offset = 0;
                arrayList = new ArrayList<>();
                mLevel = tab.getPosition() + 1;
                if (tab.getPosition() == 1 || tab.getPosition() == 2) {
                    bottom_buzz.setVisibility(View.GONE);
                } else {
                    bottom_buzz.setVisibility(View.VISIBLE);
                }
                getData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private boolean valid_buzz() {
        if (preferences.getPrefString(PrefConstant.BUZZED_TODAY).equals((String) android.text.format.DateFormat.format("yyyy-MM-dd", MyApp.getCurrentDate()))) {
            return true;
        } else {
            return false;
        }
    }

    private void call_buzz_api() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("ref_no", preferences.getUserModel().getRefNo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, true, ApiManager.BUZZ, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                    preferences.setPref(PrefConstant.BUZZED_TODAY, (String) android.text.format.DateFormat.format("yyyy-MM-dd", MyApp.getCurrentDate()));
                    CustomUtil.showTopSneakSuccess(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }
}