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
import com.fantafeat.Activity.SinglesContestActivity;
import com.fantafeat.Adapter.ContestHeaderAdapter;
import com.fantafeat.Adapter.FiferContestAdapter;
import com.fantafeat.Adapter.GroupContestListAdapter;
import com.fantafeat.Adapter.GroupHeaderAdapter;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.Model.GroupContestModel;
import com.fantafeat.Model.GroupModel;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class FiferContestListFragment extends BaseFragment {


    private static final String ARG_PARAM1 = "groupModel";
    private static final String ARG_PARAM2 = "contest_from";
    private String tag="";
    private GroupModel groupModel;
    private List<GroupContestModel.ContestDatum> contestModelList;

    private SwipeRefreshLayout swipeRefreshLayout;
    private JSONArray join_count;
    private LinearLayout layNoDataDuo;
    private GroupContestListAdapter contestListAdapter;
    RecyclerView contest_list;
    public FiferContestAdapter adapter;

    public FiferContestListFragment() {}

    public static FiferContestListFragment newInstance(GroupModel param1,String tag) {
        FiferContestListFragment fragment = new FiferContestListFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fifer_contest_list, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initControl(View view) {
        swipeRefreshLayout=view.findViewById(R.id.contest_refresh);
        contest_list=view.findViewById(R.id.contest_main_list);

        layNoDataDuo=view.findViewById(R.id.layNoDataDuo);

        contest_list.setLayoutManager(new LinearLayoutManager(mContext));



    }

    @Override
    public void initClick() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            ConstantUtil.isTimeOverShow=false;
            contest_list.getRecycledViewPool().clear();
            getContests();
        });

        contest_list.addOnScrollListener(new RecyclerView.OnScrollListener(){
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
        getContests();

    }

    public void getContests() {
         contestModelList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            //Log.e(TAG, "getContests: "+preferences.getMatchData().getId()+"   " +preferences.getUserModel().getId());
            jsonObject.put("match_id", preferences.getMatchModel().getId());//preferences.getMatchModel().getId()
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("grp_id", groupModel.getId());
            jsonObject.put("display_type", groupModel.getDisplayType());

            LogUtil.e(TAG, "onSuccessResult getContests: " + jsonObject);
            boolean show_dialog = true;
            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                show_dialog = false;
            }

            HttpRestClient.postJSON(mContext, show_dialog, ApiManager.MATCH_WISE_GROUP_CONTEST_LIST, jsonObject, new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {
                    LogUtil.e(TAG, "onSuccessResult getContests: " + responseBody);

                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    if (responseBody.optBoolean("status")) {
                        JSONArray array = responseBody.optJSONArray("data");
                        join_count = responseBody.optJSONArray("join_count");
                        int i = 0;
                        try {
                            contestModelList.clear();
                            for (i = 0; i < array.length(); i++) {
                                JSONObject data = array.getJSONObject(i);
                                for (int j=0;j<data.optJSONArray("contest_data").length();j++){
                                    JSONObject obj=data.optJSONArray("contest_data").optJSONObject(j);
                                    GroupContestModel.ContestDatum contestModel = gson.fromJson(obj.toString(), GroupContestModel.ContestDatum.class);
                                    contestModelList.add(contestModel);
                                }
                            }

                            JSONObject matchJSON = responseBody.optJSONObject("match_data");

                            if (matchJSON.length()>0){
                                MatchModel matchModel = preferences.getMatchModel();
                                matchModel.setMatchStartDate(matchJSON.optString("match_start_date"));
                                matchModel.setSafeMatchStartTime(matchJSON.optString("safe_match_start_time"));
                                matchModel.setRegularMatchStartTime(matchJSON.optString("regular_match_start_time"));
                                matchModel.setMatchType(matchJSON.optString("match_type"));
                                matchModel.setTeamAXi(matchJSON.optString("team_a_xi"));
                                matchModel.setTeamBXi(matchJSON.optString("team_b_xi"));
                                matchModel.setMatchDesc(matchJSON.optString("match_desc"));

                                MyApp.getMyPreferences().setMatchModel(matchModel);
                                preferences = MyApp.getMyPreferences();
                                ((FiferContestActivity)mContext).setTimer();
                            }

                            //for (GroupContestModel groupContestModel:contestModelList) {
                            for (GroupContestModel.ContestDatum contestDatum : contestModelList) {
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
                                            }
                                            /*for (int k=0;k<arr.length;k++){
                                                if (!obj.optString("player_id").equalsIgnoreCase(arr[k].trim())){
                                                    contestDatum.setCon_win_amount_per_spot("0.0");
                                                }
                                            }*/
                                        }
                                    }
                                }
                            }
                            //}

                            FiferContestAdapter adapter = new FiferContestAdapter(mContext, contestModelList,  gson,0,false,
                                    ((FiferContestActivity)mContext).is_match_after);
                            contest_list.setAdapter(adapter);

                           /* if (join_count != null && join_count.length() > 0) {
                                if (!join_count.optJSONObject(0).optString("join_contest_count").equals("")) {
                                    ((FiferContestActivity) mContext).join_count=join_count;
                                    ((FiferContestActivity) mContext).tabLayout.getTabAt(1).setText("My Contests (" + join_count.optJSONObject(0).optString("join_contest_count") + ")");
                                }
                            }*/

                            /* adapter = new FiferContestAdapter(mContext, contestModelList,  gson,0,false);
                            contest_list.setItemAnimator(null);
                            contest_list.setAdapter(adapter);*/

                            /*if (adapter != null) {
                                adapter.updateList(contestModelList);
                            } else {
                                adapter = new GroupHeaderAdapter(mContext, contestModelList, FiferContestListFragment.this, gson);
                                contest_list.setLayoutManager(new LinearLayoutManager(mContext));
                                contest_list.setAdapter(adapter);
                            }*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (contestModelList.size()>0){
                            contest_list.setVisibility(View.VISIBLE);
                            layNoDataDuo.setVisibility(View.GONE);
                        }else {
                            contest_list.setVisibility(View.GONE);
                            layNoDataDuo.setVisibility(View.VISIBLE);
                        }
                        //setData();
                    }
                }

                @Override
                public void onFailureResult(String responseBody, int code) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setData() {

        try {

            if (tag.equalsIgnoreCase("my")){

            }
            else {


            }


            if (tag.equalsIgnoreCase("my")){

            }else {


            }

            //checkDuoData();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void checkDuoData(){
        if (tag.equalsIgnoreCase("my")){

        }else {

        }

    }

}