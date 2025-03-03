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

import com.fantafeat.Activity.ContestListActivity;
import com.fantafeat.Activity.SinglesContestActivity;
import com.fantafeat.Adapter.ContestHeaderAdapter;
import com.fantafeat.Adapter.GroupContestListAdapter;
import com.fantafeat.Adapter.GroupHeaderAdapter;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.Model.GroupContestModel;
import com.fantafeat.Model.GroupModel;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.OfferModel;
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
import java.util.List;

public class GroupContestListFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "groupModel";
    private static final String ARG_PARAM2 = "contest_from";
    private GroupModel groupModel;
    private LinearLayout layNoDataDuo;
    RecyclerView contest_list;
    private List<GroupContestModel> contestModelList;
    private List<GroupContestModel.ContestDatum> contestMyModelList;
    private List<OfferModel> offerModelList;
    public GroupHeaderAdapter adapter;
    private GroupContestListAdapter contestListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String tag="";
    private JSONObject join_count;
    public GroupContestListFragment() {}
    public static GroupContestListFragment newInstance(GroupModel param1,String tag) {
        GroupContestListFragment fragment = new GroupContestListFragment();
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
        View view=inflater.inflate(R.layout.fragment_group_contest_list, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initControl(View view) {
        contest_list=view.findViewById(R.id.contest_main_list);
        swipeRefreshLayout=view.findViewById(R.id.contest_refresh);
        layNoDataDuo=view.findViewById(R.id.layNoDataDuo);
    }

    @Override
    public void initClick() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ConstantUtil.isTimeOverShow=false;
                getContests();
            }
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
        offerModelList = new ArrayList<>();
        contestMyModelList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            //Log.e(TAG, "getContests: "+preferences.getMatchData().getId()+"   " +preferences.getUserModel().getId());
            jsonObject.put("match_id",preferences.getMatchModel().getId() );//preferences.getMatchModel().getId()
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


        HttpRestClient.postJSON(mContext, show_dialog, ApiManager.MATCH_WISE_GROUP_CONTEST_LIST_v3, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult getContests: " + responseBody);

                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                if (responseBody.optBoolean("status")) {
                    JSONArray array = responseBody.optJSONArray("data");
                    int i = 0;

                        for (i = 0; i < array.length(); i++) {
                            try {
                                JSONObject data = array.getJSONObject(i);
                                GroupContestModel contestModel = gson.fromJson(data.toString(), GroupContestModel.class);

                                contestModelList.add(contestModel);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (responseBody.has("match_data")){
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
                                ((SinglesContestActivity)mContext).setTimer();
                            }
                        }

                        if (responseBody.has("join_count")){
                            try {
                                JSONArray join_count_temp = responseBody.optJSONArray("join_count");
                                if (join_count_temp != null && join_count_temp.length() > 0) {
                                    join_count = join_count_temp.getJSONObject(0);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    setData();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void setData() {

        try {

            String userId=preferences.getUserModel().getId();

            for (int l=0;l<contestModelList.size();l++) {
                GroupContestModel groupContestModel=contestModelList.get(l);
                for (int k=0;k<groupContestModel.getContestData().size();k++) {
                    GroupContestModel.ContestDatum contestDatum=groupContestModel.getContestData().get(k);
                    List<GroupContestModel.ContestDatum.PlayersDatum> playersData=new ArrayList<>();
                    for (int i = 0; i < Integer.parseInt(contestDatum.getNoTeamJoin()); i++) {
                        GroupContestModel.ContestDatum.PlayersDatum player= new GroupContestModel.ContestDatum.PlayersDatum();
                        player.setPlayerName("Choose Player");
                        player.setDisable(false);
                        player.setJoinMe(false);
                        player.setPlayingXi("");
                        playersData.add(player);
                    }
                    contestDatum.setPlayersContest(playersData);

                    if (!TextUtils.isEmpty(contestDatum.getPlayerSpotWiseData())){

                        JSONArray selectedArray=new JSONArray(contestDatum.getPlayerSpotWiseData());

                        for (int i = 0; i < contestDatum.getAll_players().size(); i++) {
                            GroupContestModel.ContestDatum.PlayersDatum player=contestDatum.getAll_players().get(i);
                            for (int j = 0; j < selectedArray.length(); j++) {
                                JSONObject obj = selectedArray.optJSONObject(j);
                                if (obj.optString("player_id").equalsIgnoreCase(player.getPlayerId())){
                                    playersData.remove(playersData.size()-1);

                                    playersData.add(0, player);
                                    playersData.get(0).setDisable(true);

                                    if (userId.equalsIgnoreCase(obj.optString("user_id"))){
                                        playersData.get(0).setJoinMe(true);

                                        contestDatum.setDisable(true);
                                    }
                                }
                            }
                        }
                    }

                }
            }

            /*for (GroupContestModel groupContestModel:contestModelList) {
                for (GroupContestModel.ContestDatum contestDatum : groupContestModel.getContestData()) {
                    if (!TextUtils.isEmpty(contestDatum.getPlayerSpotWiseData())){
                        List<GroupModel.PlayersDatum> playersData=contestDatum.getPlayers();
                        JSONArray selectedArray=new JSONArray(contestDatum.getPlayerSpotWiseData());
                       // String[] arr=contestDatum.getCon_win_player_list().split(",");
                        for (GroupModel.PlayersDatum player : playersData) {
                            for (int i = 0; i < selectedArray.length(); i++) {
                                JSONObject obj=selectedArray.optJSONObject(i);
                                if (obj.optString("player_id").equalsIgnoreCase(player.getPlayerId())){
                                    player.setDisable(true);
                                    if (preferences.getUserModel().getId().equalsIgnoreCase(obj.optString("user_id"))){
                                        player.setJoinMe(true);
                                        contestDatum.setDisable(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }*/

            if (join_count != null && !join_count.optString("join_contest_count").equals("")) {
                ((SinglesContestActivity) mContext).join_count=join_count;
                ((SinglesContestActivity) mContext).tabLayout.getTabAt(1).setText("My Contests (" + join_count.optString("join_contest_count") + ")");
            }


            if (adapter != null) {
                adapter.updateList(contestModelList);
            } else {
                adapter = new GroupHeaderAdapter(mContext, contestModelList, GroupContestListFragment.this, gson,
                        ((SinglesContestActivity) mContext).is_match_after);
                contest_list.setLayoutManager(new LinearLayoutManager(mContext));
                contest_list.setAdapter(adapter);
            }

            checkDuoData();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void checkDuoData(){
        if (contestModelList.size()>0){
            contest_list.setVisibility(View.VISIBLE);
            layNoDataDuo.setVisibility(View.GONE);
        }else {
            contest_list.setVisibility(View.GONE);
            layNoDataDuo.setVisibility(View.VISIBLE);
        }

    }

}