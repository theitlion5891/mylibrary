package com.fantafeat.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fantafeat.Adapter.MatchAfterAdapter;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CompletedLeagueFragment extends BaseFragment {
    RecyclerView match_result_list;
    List<MatchModel> matchAfterModelList;
    MatchAfterAdapter adapter;
    SwipeRefreshLayout pull_match_result;
    LinearLayout layNoData;
    ImageView imgPlace;
    TextView txtPlace;

    int offset, limit, mLevel;
    boolean isGetData, isApiCall;
    LinearLayoutManager linearLayoutManager;
    String sportId="";
    private long lastUpdateTime=0;

    public CompletedLeagueFragment() {
       // this.sportId = sportId;
    }

    public static CompletedLeagueFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        CompletedLeagueFragment f = new CompletedLeagueFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sportId = getArguments().getString("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_league, container, false);
        initFragment(view);
       // getData();
        return view;
    }

    @Override
    public void initControl(View view){
        limit = 10;
        offset = 0;
        isGetData = false;
        isApiCall = false;
        matchAfterModelList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(mContext);

        match_result_list = view.findViewById(R.id.match_result_list);
        pull_match_result = view.findViewById(R.id.pull_match_result);
        layNoData = view.findViewById(R.id.layNoData);
        imgPlace = view.findViewById(R.id.imgPlace);
        txtPlace = view.findViewById(R.id.txtPlace);
    }

    @Override
    public void initClick() {
        pull_match_result.setOnRefreshListener(() -> {
            if ((System.currentTimeMillis()-lastUpdateTime)>= ConstantUtil.Refresh_delay) {
                limit = 10;
                offset = 0;
                isGetData = false;
                isApiCall = false;
                matchAfterModelList = new ArrayList<>();
                getData();
            }else {
                pull_match_result.setRefreshing(false);
            }
        });

        match_result_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager != null &&
                        linearLayoutManager.findLastCompletelyVisibleItemPosition() == matchAfterModelList.size() - 1
                        && isGetData && isApiCall) {
                    //Load more online data
                    if (matchAfterModelList.size() > 0) {
                        //match_result_progress.setVisibility(View.VISIBLE);
                        getData();
                    }
                }
            }
        });
    }

    protected void getData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("sports_id", sportId);
            jsonObject.put("offset", offset);
            jsonObject.put("limit", limit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "completed Param: "+jsonObject.toString());
        isApiCall = false;
        Boolean showProgress = true;
        if (pull_match_result != null && pull_match_result.isRefreshing()) {
            showProgress = false;
        }
        HttpRestClient.postJSON(mContext, showProgress, ApiManager.MATCH_LIST_COMPLETED, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                lastUpdateTime=System.currentTimeMillis();
                isApiCall = true;
                //match_result_progress.setVisibility(View.GONE);
                if (pull_match_result != null && pull_match_result.isRefreshing()) {
                    pull_match_result.setRefreshing(false);
                }
                if (responseBody.optBoolean("status")) {
                    LogUtil.e(TAG,responseBody.toString());
                    JSONArray jsonArray = responseBody.optJSONArray("data");
                    if (jsonArray.length() != 0 || matchAfterModelList.size() != 0) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                MatchModel matchAfterModel = gson.fromJson(jsonArray.getJSONObject(i).toString(), MatchModel.class);
                                matchAfterModelList.add(matchAfterModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (limit >= jsonArray.length() && adapter != null) {
                            adapter.updateData(matchAfterModelList);
                        } else {
                            adapter = new MatchAfterAdapter(mContext, matchAfterModelList);
                            match_result_list.setLayoutManager(linearLayoutManager);
                            match_result_list.setAdapter(adapter);
                        }

                        if (jsonArray.length() < limit) {
                            isGetData = false;
                            offset = 0;
                        } else {
                            offset += jsonArray.length();
                            isGetData = true;
                        }

                    }
                }
                checkData();
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                //match_result_progress.setVisibility(View.GONE);
                if (pull_match_result != null && pull_match_result.isRefreshing()) {
                    pull_match_result.setRefreshing(false);
                }
            }
        });
    }

    private void checkData(){
        LogUtil.d("selSports",sportId);
        switch (sportId){
            case "1":
                if (matchAfterModelList.size()>0){
                    match_result_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_result_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.cricket_placeholder);
                    txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
            case "2":
                if (matchAfterModelList.size()>0){
                    match_result_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_result_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.football_placeholder);
                    txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
            case "3":
                if (matchAfterModelList.size()>0){
                    match_result_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_result_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.baseball_placeholder);
                    txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
            case "4":
                if (matchAfterModelList.size()>0){
                    match_result_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_result_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.basketball_placeholder);
                    txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
            case "5":
                if (matchAfterModelList.size()>0){
                    match_result_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_result_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.vollyball_placeholder);
                }
                break;
            case "7"://6
                if (matchAfterModelList.size()>0){
                    match_result_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_result_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.kabaddi_placeholder);
                    txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
        }

    }

}