package com.fantafeat.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fantafeat.Activity.FiferContestActivity;
import com.fantafeat.Adapter.FiferContestAdapter;
import com.fantafeat.Model.GroupContestModel;
import com.fantafeat.Model.GroupModel;
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

public class FiferMyContestsFragment extends BaseFragment {


    private static final String ARG_PARAM1 = "groupModel";
    private static final String ARG_PARAM2 = "contest_from";

    private String tag="";
    private GroupModel groupModel;

    RecyclerView contest_my_list;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<GroupContestModel.ContestDatum> contestMyModelList;
    private LinearLayout layNoDataDuo;
    private JSONArray join_count;

    public FiferMyContestsFragment() { }

    public static FiferMyContestsFragment newInstance(GroupModel param1,String tag) {
        FiferMyContestsFragment fragment = new FiferMyContestsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, tag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupModel = (GroupModel) getArguments().getSerializable(ARG_PARAM1);
            tag = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fifer_my_contests, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initControl(View view) {
        swipeRefreshLayout=view.findViewById(R.id.contest_refresh);
        contest_my_list=view.findViewById(R.id.contest_my_list);
        layNoDataDuo=view.findViewById(R.id.layNoDataDuo);
        contest_my_list.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public void initClick() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            ConstantUtil.isTimeOverShow=false;
            getMyContests();
        });

        contest_my_list.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getMyContests();
    }

    public void getMyContests() {
        contestMyModelList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            //Log.e(TAG, "getContests: "+preferences.getMatchData().getId()+"   " +preferences.getUserModel().getId());
            jsonObject.put("match_id", preferences.getMatchModel().getId());//preferences.getMatchModel().getId()
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("grp_id", groupModel.getId());
            jsonObject.put("display_type", groupModel.getDisplayType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "onSuccessResult getContests: " + jsonObject);
        boolean show_dialog = true;
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            show_dialog = false;
        }

        HttpRestClient.postJSON(mContext, show_dialog, ApiManager.MATCH_WISE_JOINED_CONTEST_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult getContests: " + responseBody);

                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                if (responseBody.optBoolean("status")) {
                    JSONArray array = responseBody.optJSONArray("data");
                    join_count = responseBody.optJSONArray("join_count");
                    try {
                        int i = 0;
                        contestMyModelList.clear();
                        for (i = 0; i < array.length(); i++) {
                            JSONObject data = array.getJSONObject(i);
                            GroupContestModel.ContestDatum contestModel = gson.fromJson(data.toString(), GroupContestModel.ContestDatum.class);
                            contestMyModelList.add(contestModel);
                        }

                        for (GroupContestModel.ContestDatum contestDatum : contestMyModelList) {
                            if (!TextUtils.isEmpty(contestDatum.getPlayerSpotWiseData())){
                                List<GroupModel.PlayersDatum> playersData=contestDatum.getPlayers();
                                JSONArray selectedArray=new JSONArray(contestDatum.getPlayerSpotWiseData());
                                //String[] arr=contestDatum.getCon_win_player_list().split(",");
                                for (GroupModel.PlayersDatum player : playersData) {
                                    for (int j = 0; j < selectedArray.length(); j++) {
                                        JSONObject obj=selectedArray.optJSONObject(j);
                                        if (obj.optString("player_id").equalsIgnoreCase(player.getPlayerId())){
                                            player.setJoiningCnt(obj.optString("cnt_no"));
                                            player.setApx_win_amt(obj.optString("apx_win_amt"));
                                            //player.setJoined_spot(obj.optString("cnt_no"));
                                        }
                                        /*for (int k=0;k<arr.length;k++){
                                            if (!obj.optString("player_id").equalsIgnoreCase(arr[k].trim())){
                                                contestDatum.setCon_win_amount_per_spot("0.0");
                                            }
                                        }*/
                                    }
                                    for (int j=0;j<join_count.length();j++){
                                        JSONObject obj=join_count.optJSONObject(j);
                                        if (obj.optString("player_id").equalsIgnoreCase(player.getPlayerId())){
                                            player.setJoined_spot(obj.optString("joined_spot"));
                                        }
                                    }
                                }
                            }
                        }

                        /*if (contestListAdapter != null) {
                            contestListAdapter.updateList(contestMyModelList);
                        } else {
                            FiferContestAdapter contestListAdapter = new FiferContestAdapter(mContext,contestMyModelList, gson,0,true);
                            contest_list.setItemAnimator(null);
                            contest_list.setAdapter(contestListAdapter);
                        }*/

                        FiferContestAdapter contestListAdapter = new FiferContestAdapter(mContext,contestMyModelList, gson,0,true,
                                ((FiferContestActivity)mContext).is_match_after);
                        contest_my_list.setItemAnimator(null);
                        contest_my_list.setAdapter(contestListAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //checkDuoData();
                    if (contestMyModelList.size()>0){
                        contest_my_list.setVisibility(View.VISIBLE);
                        layNoDataDuo.setVisibility(View.GONE);
                    }else {
                        contest_my_list.setVisibility(View.GONE);
                        layNoDataDuo.setVisibility(View.VISIBLE);
                    }
                    // setData();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }
}