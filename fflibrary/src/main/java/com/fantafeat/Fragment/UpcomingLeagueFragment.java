package com.fantafeat.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fantafeat.Adapter.MatchListAdapter;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class UpcomingLeagueFragment extends BaseFragment {

    List<MatchModel> matchModelList;
    MatchListAdapter adapter;
    RecyclerView match_fixer_list;
    SwipeRefreshLayout pull_match_upcoming;
    LinearLayout layNoData;
    ImageView imgPlace;
    TextView txtPlace;
    String sportId="";
    private boolean isDataLoaded=false;

    public UpcomingLeagueFragment() {
       // this.sportId = sportId;
    }

    public static UpcomingLeagueFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        UpcomingLeagueFragment f = new UpcomingLeagueFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_league, container, false);
        initFragment(view);
        //getData();
        return view;
    }

    @Override
    public void initControl(View view) {
        match_fixer_list = view.findViewById(R.id.match_fixer_list);
        pull_match_upcoming = view.findViewById(R.id.pull_match_upcoming);
        layNoData = view.findViewById(R.id.layNoData);
        imgPlace = view.findViewById(R.id.imgPlace);
        txtPlace = view.findViewById(R.id.txtPlace);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible() && !isDataLoaded) {
            isDataLoaded=true;
            getData();
        }
    }

    @Override
    public void initClick() {
        pull_match_upcoming.setOnRefreshListener(() -> getData());
    }

    protected void getData() {
        matchModelList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("sports_id", sportId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean showProgress = true;
        if (pull_match_upcoming != null && pull_match_upcoming.isRefreshing()) {
            showProgress = false;
        }
        HttpRestClient.postJSON(mContext, showProgress, ApiManager.MATCH_LIST_FIXTURE, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (pull_match_upcoming != null && pull_match_upcoming.isRefreshing()) {
                    pull_match_upcoming.setRefreshing(false);
                }
                if (responseBody.optBoolean("status")) {
                    Log.e(TAG, "onSuccessResult: "+responseBody.toString());
                    JSONArray jsonArray = responseBody.optJSONArray("data");
                    if(jsonArray.length()!=0) {

                        //pull_match_upcoming.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                MatchModel matchModel = gson.fromJson(jsonArray.getJSONObject(i).toString(), MatchModel.class);
                                matchModelList.add(matchModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (adapter != null) {
                            adapter.updateList(matchModelList);
                        } else {
                            adapter = new MatchListAdapter(mContext, matchModelList);
                            match_fixer_list.setLayoutManager(new LinearLayoutManager(mContext));
                            match_fixer_list.setAdapter(adapter);
                        }
                    }
                }
                checkData();
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                if (pull_match_upcoming != null && pull_match_upcoming.isRefreshing()) {
                    pull_match_upcoming.setRefreshing(false);
                }
            }
        });
    }

    private void checkData(){
        //LogUtil.d("selSports",sportId);
        switch (sportId){
            case "1":
                if (matchModelList.size()>0){
                    match_fixer_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_fixer_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.cricket_placeholder);
                    txtPlace.setText(mContext.getResources().getString(R.string.no_record_found));
                }
                break;
            case "2":
                if (matchModelList.size()>0){
                    match_fixer_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_fixer_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.football_placeholder);
                    txtPlace.setText(mContext.getResources().getString(R.string.no_record_found));
                }
                break;
            case "3":
                if (matchModelList.size()>0){
                    match_fixer_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_fixer_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.baseball_placeholder);
                    txtPlace.setText(mContext.getResources().getString(R.string.no_record_found));
                }
                break;
            case "4":
                if (matchModelList.size()>0){
                    match_fixer_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_fixer_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.basketball_placeholder);
                    txtPlace.setText(mContext.getResources().getString(R.string.no_record_found));
                }
                break;
            case "5":
                if (matchModelList.size()>0){
                    match_fixer_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_fixer_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.vollyball_placeholder);
                }
                break;
            case "7"://6
                if (matchModelList.size()>0){
                    match_fixer_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_fixer_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.kabaddi_placeholder);
                    txtPlace.setText(mContext.getResources().getString(R.string.no_record_found));
                }
                break;
        }

    }


}