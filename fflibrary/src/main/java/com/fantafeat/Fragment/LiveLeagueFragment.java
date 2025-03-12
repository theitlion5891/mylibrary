package com.fantafeat.Fragment;

import android.os.Bundle;
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

import com.fantafeat.Adapter.MatchAfterAdapter;
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


public class LiveLeagueFragment extends BaseFragment {

    RecyclerView match_live_list;
    List<MatchModel> matchAfterModelList;
    MatchAfterAdapter adapter;
    SwipeRefreshLayout pull_match_live;

    LinearLayout layNoData;
    ImageView imgPlace;
    TextView txtPlace;
    String sportId="";
    private boolean isDataLoaded=false;

    public LiveLeagueFragment() {
      //  this.sportId = sportId;
    }

    public static LiveLeagueFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        LiveLeagueFragment f = new LiveLeagueFragment();
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
        View view = inflater.inflate(R.layout.fragment_live_league, container, false);
        initFragment(view);
        //getData();
        return view;
    }

    @Override
    public void initControl(View view) {
        match_live_list = view.findViewById(R.id.match_live_list);
        pull_match_live = view.findViewById(R.id.pull_match_live);
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
        pull_match_live.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    protected void getData() {
        matchAfterModelList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("sports_id", sportId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean showProgress = true;
        if (pull_match_live != null && pull_match_live.isRefreshing()) {
            showProgress = false;
        }

        HttpRestClient.postJSON(mContext, showProgress, ApiManager.MATCH_LIST_LIVE, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (pull_match_live != null && pull_match_live.isRefreshing()) {
                    pull_match_live.setRefreshing(false);
                }
                LogUtil.e(TAG, "onSuccessResult: "+responseBody.toString() );
                if (responseBody.optBoolean("status")) {
                    JSONArray jsonArray = responseBody.optJSONArray("data");
                    if(jsonArray.length()!=0) {


                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                MatchModel matchAfterModel = gson.fromJson(jsonArray.getJSONObject(i).toString(), MatchModel.class);
                                matchAfterModelList.add(matchAfterModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (adapter != null) {
                            adapter.updateData(matchAfterModelList);
                        } else {
                            adapter = new MatchAfterAdapter(mContext, matchAfterModelList);
                            match_live_list.setLayoutManager(new LinearLayoutManager(mContext));
                            match_live_list.setAdapter(adapter);
                        }
                    }
                }
                checkData();
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                if (pull_match_live != null && pull_match_live.isRefreshing()) {
                    pull_match_live.setRefreshing(false);
                }
            }
        });
    }

    private void checkData(){
        LogUtil.d("selSports",sportId);
        switch (sportId){
            case "1":
                if (matchAfterModelList.size()>0){
                    match_live_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_live_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.cricket_placeholder);
                    txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
            case "2":
                if (matchAfterModelList.size()>0){
                    match_live_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_live_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.football_placeholder);
                    txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
            case "3":
                if (matchAfterModelList.size()>0){
                    match_live_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_live_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.baseball_placeholder);
                    txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
            case "4":
                if (matchAfterModelList.size()>0){
                    match_live_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_live_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.basketball_placeholder);
                    txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
            case "5":
                if (matchAfterModelList.size()>0){
                    match_live_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_live_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.vollyball_placeholder);
                }
                break;
            case "7"://6
                if (matchAfterModelList.size()>0){
                    match_live_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_live_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.kabaddi_placeholder);
                    txtPlace.setText(getResources().getString(R.string.no_record_found));
                }
                break;
        }

    }
}