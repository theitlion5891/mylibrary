package com.fantafeat.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantafeat.Activity.AfterMatchActivity;
import com.fantafeat.Activity.FiferContestActivity;
import com.fantafeat.Activity.SinglesContestActivity;
import com.fantafeat.Adapter.GroupListAdapter;
import com.fantafeat.Adapter.JoinLeagueAdapter;
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
import com.fantafeat.util.MyRecyclerScroll;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MatchMyContestFragment extends BaseFragment {

    private RecyclerView joined_team_contest_list;
    SwipeRefreshLayout refresh_my_contest;
    CardView cardMvp;
    JoinLeagueAdapter adapter;
    public List<ContestModel.ContestDatum> contestModelList;
    private TextView btnClassic,/*btnDuo,*/btnQuinto;
    private LinearLayout layTabs,layNoDataDuo;
    public String selectedTag="1";
    private ArrayList<GroupModel> duoList;
    private boolean isFirstTime=false;

    private int offset, limit;
    private boolean isApiCall, isGetData;
    private long lastUpdateTime=0;

    public MatchMyContestFragment() {
        //this.afterMatchActivity = afterMatchActivity;
    }

    public static MatchMyContestFragment newInstance(){
        return new MatchMyContestFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match_my_contest, container, false);
        initFragment(view);

        return view;
    }

    @Override
    public void initControl(View view) {
        joined_team_contest_list = view.findViewById(R.id.after_joined_team_contest_list);
        refresh_my_contest = view.findViewById(R.id.refresh_my_contest);
        layTabs = view.findViewById(R.id.layTabs);
        btnClassic = view.findViewById(R.id.btnClassic);
      //  btnDuo = view.findViewById(R.id.btnDuo);
        btnQuinto = view.findViewById(R.id.btnQuinto);
        layNoDataDuo = view.findViewById(R.id.layNoDataDuo);
        cardMvp = view.findViewById(R.id.cardMvp);

        offset = 0;
        limit = 100;
        isApiCall = true;
        isGetData = true;

        contestModelList = new ArrayList<>();


        LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        joined_team_contest_list.setLayoutManager(mLinearLayoutManager);

        adapter = new JoinLeagueAdapter(mContext, contestModelList, position -> {
            if (contestModelList.size()>0){
                downloadExcel(contestModelList.get(position).getId());
            }else {
                CustomUtil.showTopSneakError(mContext,"Unable to download");
            }
        });
        joined_team_contest_list.setAdapter(adapter);
        joined_team_contest_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mLinearLayoutManager != null &&
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition() == contestModelList.size() - 1
                        && isGetData && isApiCall) {
                    if (selectedTag.equalsIgnoreCase("1")){
                        getData();
                    }
                }
            }
        });

        if (preferences.getMatchModel().getIs_fifer().equalsIgnoreCase("yes")){
            layTabs.setVisibility(View.VISIBLE);
        }else {
            layTabs.setVisibility(View.GONE);
        }

        if (preferences.getMatchModel().getIs_single().equalsIgnoreCase("yes")){
            cardMvp.setVisibility(View.VISIBLE);
        }else {
            cardMvp.setVisibility(View.GONE);
        }

        if (selectedTag.equalsIgnoreCase("1")){
            getData();
        }else {
            getSinglesData();
        }

        //getData();
    }

    @Override
    public void initClick() {

        joined_team_contest_list.addOnScrollListener(new MyRecyclerScroll() {
            @Override
            public void show() {
                cardMvp.animate().translationX(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide() {
                int fabMargin=getResources().getDimensionPixelSize(R.dimen.top_bottom_margin);
                cardMvp.animate().translationX(cardMvp.getHeight() + fabMargin).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });

        refresh_my_contest.setOnRefreshListener(() -> {
            if ((System.currentTimeMillis()-lastUpdateTime)>= ConstantUtil.Refresh_delay) {
                if (selectedTag.equalsIgnoreCase("1")) {
                    isGetData = false;
                    offset = 0;
                    contestModelList.clear();
                    adapter.updateData(contestModelList);

                    getData();
                } else {
                    getSinglesData();
                }

                if (preferences.getMatchModel() != null &&
                        !preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("pending")) {
                    //updateScore();
                }
            }else {
                refresh_my_contest.setRefreshing(false);
            }
        });

        btnClassic.setOnClickListener(view -> {
            isGetData = false;
            offset=0;

            joined_team_contest_list.scrollToPosition(0);

            duoList=new ArrayList<>();
            contestModelList.clear();
            adapter = new JoinLeagueAdapter(mContext, contestModelList, position -> {
                if (contestModelList.size()>0){
                    downloadExcel(contestModelList.get(position).getId());
                }else {
                    CustomUtil.showTopSneakError(mContext,"Unable to download");
                }
            });
            joined_team_contest_list.setAdapter(adapter);
            adapter.updateData(contestModelList);

            selectedTag="1";
            btnClassic.setBackgroundResource(R.drawable.primary_fill_border);
            btnClassic.setTextColor(getResources().getColor(R.color.white_pure));

            /*btnDuo.setBackgroundResource(R.drawable.transparent_view);
            btnDuo.setTextColor(getResources().getColor(R.color.black));*/
            btnQuinto.setBackgroundResource(R.drawable.transparent_view);
            btnQuinto.setTextColor(getResources().getColor(R.color.black_pure));

            getData();

          /*  layClassic.setVisibility(View.VISIBLE);
            layDuo.setVisibility(View.GONE);
            layQuinto.setVisibility(View.GONE);*/

        });

        cardMvp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMVPData();
            }
        });

        /*btnDuo.setOnClickListener(view -> {

            duoList=new ArrayList<>();
            contestModelList = new ArrayList<>();

            selectedTag="2";
            btnClassic.setBackgroundResource(R.drawable.transparent_view);
            btnClassic.setTextColor(getResources().getColor(R.color.black));

            btnDuo.setBackgroundResource(R.drawable.primary_fill_border);
            btnDuo.setTextColor(getResources().getColor(R.color.white));
            btnQuinto.setBackgroundResource(R.drawable.transparent_view);
            btnQuinto.setTextColor(getResources().getColor(R.color.black));

            getSinglesData();

        });*/

        btnQuinto.setOnClickListener(view -> {
            selectedTag="3";

            duoList=new ArrayList<>();
            contestModelList = new ArrayList<>();

            btnClassic.setBackgroundResource(R.drawable.transparent_view);
            btnClassic.setTextColor(getResources().getColor(R.color.black_pure));

           /* btnDuo.setBackgroundResource(R.drawable.transparent_view);
            btnDuo.setTextColor(getResources().getColor(R.color.black));*/
            btnQuinto.setBackgroundResource(R.drawable.primary_fill_border);
            btnQuinto.setTextColor(getResources().getColor(R.color.white_pure));

            getSinglesData();

        });
    }

    public void changeTabs(String id){

    }

    private void updateScore() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id",preferences.getMatchModel().getId());
            jsonObject.put("user_id",preferences.getUserModel().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, false, ApiManager.GET_MATCH_SCORE, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "matchScore: " + responseBody.toString() );
                if(responseBody.optBoolean("status")){
                    MatchModel matchModel = preferences.getMatchModel();
                    JSONObject data = responseBody.optJSONObject("data");
                    matchModel.setTeam1Inn1Score(data.optString("team_1_inn_1_score"));
                    matchModel.setTeam1Inn2Score(data.optString("team_1_inn_2_score"));
                    matchModel.setTeam2Inn1Score(data.optString("team_2_inn_1_score"));
                    matchModel.setTeam2Inn2Score(data.optString("team_2_inn_2_score"));
                    MyApp.getMyPreferences().setMatchModel(matchModel);
                    ((AfterMatchActivity)mContext).updateScore();

                   /* if (preferences.getMatchModel().getMatchType().equalsIgnoreCase("test")) {
                        inning2_score_team1.setVisibility(View.VISIBLE);
                        inning2_score_team2.setVisibility(View.VISIBLE);

                        inning1_score_team1.setText(preferences.getMatchModel().getTeam1Inn1Score());
                        inning2_score_team1.setText(preferences.getMatchModel().getTeam1Inn2Score());
                        inning1_score_team2.setText(preferences.getMatchModel().getTeam2Inn1Score());
                        inning2_score_team2.setText(preferences.getMatchModel().getTeam2Inn2Score());
                    } else {
                        inning1_score_team1.setText(preferences.getMatchModel().getTeam1Inn1Score());
                        inning2_score_team1.setText(preferences.getMatchModel().getTeam1Inn2Score());
                        inning1_score_team2.setText(preferences.getMatchModel().getTeam2Inn1Score());
                        inning2_score_team2.setText(preferences.getMatchModel().getTeam2Inn2Score());

                        inning2_score_layout.setVisibility(View.GONE);
                        inning4_score_layout.setVisibility(View.GONE);
                    }*/
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    public void getData() {
        //

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("con_display_type", preferences.getMatchModel().getConDisplayType());
            jsonObject.put("offset", offset);
            jsonObject.put("limit", limit);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.e("resp",jsonObject.toString());

     //   boolean isShow = refresh_my_contest == null || !refresh_my_contest.isRefreshing();
        boolean isShow=true;
        if (refresh_my_contest.isRefreshing()){
            isShow=false;
        }
        HttpRestClient.postJSON(mContext, isShow, ApiManager.JOIN_CONTEST_LIST_BY_ID, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                lastUpdateTime=System.currentTimeMillis();
                if (refresh_my_contest != null && refresh_my_contest.isRefreshing()) {
                    refresh_my_contest.setRefreshing(false);
                }
                LogUtil.e(TAG, "JOIN_CONTEST_LIST_BY_ID: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONArray array = responseBody.optJSONArray("data");
                    isApiCall = true;
                    int i = 0;
                    for (i = 0; i < array.length(); i++) {
                        JSONObject object = array.optJSONObject(i);
                        ContestModel.ContestDatum contestModel = gson.fromJson(object.toString(), ContestModel.ContestDatum.class);
                        contestModelList.add(contestModel);
                    }
                    adapter.updateData(contestModelList);

                    /*if (responseBody.has("my_join_cnt ") && !TextUtils.isEmpty(responseBody.optString("my_join_cnt"))) {
                        ((AfterMatchActivity) mContext).joined_team_tab.getTabAt(0).setText("My Contests (" + responseBody.optString("my_join_cnt") + ")");
                    } else {
                        ((AfterMatchActivity) mContext).joined_team_tab.getTabAt(0).setText("My Contest");
                    }*/

                    if(array.length()>0)
                        ((AfterMatchActivity) mContext).joined_team_tab.getTabAt(0).setText("My Contests (" + responseBody.optString("my_join_cnt") + ")");

                    /*if (limit > array.length()) {
                        isGetData = false;
                    }else{
                        if (array.length() > 0 && array.length()==limit){
                            offset += limit;
                            isGetData = true;
                        }
                    }*/
                    if (contestModelList.size() < limit) {
                        isGetData = false;
                        offset = 0;
                    }
                    else {

                        if(array.length()>0) {
                            isGetData = true;
                            offset += limit;
                        }else {
                            isGetData = false;
                        }
                    }
                }

                if (contestModelList.size()>0){
                    layNoDataDuo.setVisibility(View.GONE);
                    joined_team_contest_list.setVisibility(View.VISIBLE);
                }else {
                    layNoDataDuo.setVisibility(View.VISIBLE);
                    joined_team_contest_list.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {refresh_my_contest.setRefreshing(false);}

        });
    }

    public void getSinglesData() {
        duoList=new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id", preferences.getMatchModel().getId());//ConstantUtil.testMatchId
            jsonObject.put("display_type", selectedTag);
            jsonObject.put("user_id", preferences.getUserModel().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        boolean isShow = refresh_my_contest == null || !refresh_my_contest.isRefreshing();
        HttpRestClient.postJSON(mContext, isShow, ApiManager.MATCH_WISE_JOIN_GROUP_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                lastUpdateTime=System.currentTimeMillis();
                if (refresh_my_contest != null && refresh_my_contest.isRefreshing()) {
                    refresh_my_contest.setRefreshing(false);
                }
                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {

                    JSONArray data = responseBody.optJSONArray("data");
                    if (data!=null && data.length()>0){
                        duoList.clear();
                        for (int i = 0;i<data.length();i++){
                            JSONObject obj=data.optJSONObject(i);
                            GroupModel groupModel=gson.fromJson(obj.toString(),GroupModel.class);
                            duoList.add(groupModel);
                        }
                        GroupListAdapter adapter=new GroupListAdapter(mContext, duoList, preferences.getMatchModel(), model -> {
                            if (preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("Cancelled")) {
                                CustomUtil.showTopSneakWarning(mContext,"This match is cancelled.");
                            }else {
                                if (selectedTag.equalsIgnoreCase("2")){
                                    startActivity(new Intent(mContext,SinglesContestActivity.class)
                                            .putExtra("group_model",model)
                                            .putExtra("is_match_after",true)
                                    );
                                }else {
                                    startActivity(new Intent(mContext, FiferContestActivity.class)
                                            .putExtra("group_model",model)
                                            .putExtra("is_match_after",true)
                                    );
                                }
                            }


                        });
                        joined_team_contest_list.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
                        joined_team_contest_list.setAdapter(adapter);
                    }

                }
                if (duoList.size()>0){
                    layNoDataDuo.setVisibility(View.GONE);
                    joined_team_contest_list.setVisibility(View.VISIBLE);
                }else {
                    layNoDataDuo.setVisibility(View.VISIBLE);
                    joined_team_contest_list.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    public void getMVPData() {
        ArrayList<GroupModel> duoList=new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id", preferences.getMatchModel().getId());//ConstantUtil.testMatchId
            jsonObject.put("display_type", "2");
            jsonObject.put("user_id", preferences.getUserModel().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.MATCH_WISE_JOIN_GROUP_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {

                    JSONArray data = responseBody.optJSONArray("data");
                    if (data!=null && data.length()>0){
                        duoList.clear();
                        for (int i = 0;i<data.length();i++){
                            JSONObject obj=data.optJSONObject(i);
                            GroupModel groupModel=gson.fromJson(obj.toString(),GroupModel.class);
                            duoList.add(groupModel);
                        }

                        if (duoList.size()>0){
                            startActivity(new Intent(mContext,SinglesContestActivity.class)
                                    .putExtra("group_model",duoList.get(0))
                                    .putExtra("is_match_after",true)
                            );
                        }

                    }

                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    /*public void getSinglesData() {

        contestModelList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            //Log.e(TAG, "getContests: "+preferences.getMatchData().getId()+"   " +preferences.getUserModel().getId());
            jsonObject.put("match_id",preferences.getMatchModel().getId() );//preferences.getMatchModel().getId()
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("grp_id", ((AfterMatchActivity)mContext).selectedTag);
            jsonObject.put("display_type", groupModel.getDisplayType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "onSuccessResult getContests: " + jsonObject);
        boolean show_dialog = true;
        if (refresh_my_contest != null && refresh_my_contest.isRefreshing()) {
            show_dialog = false;
        }

        String url="";

        if (tag.equalsIgnoreCase("my")){
            url=ApiManager.MATCH_WISE_JOINED_CONTEST_LIST;
        }else {
            url=ApiManager.MATCH_WISE_GROUP_CONTEST_LIST;
        }
        HttpRestClient.postJSON(mContext, show_dialog, url, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult getContests: " + responseBody);

                if (refresh_my_contest != null && refresh_my_contest.isRefreshing()) {
                    refresh_my_contest.setRefreshing(false);
                }

                if (responseBody.optBoolean("status")) {
                    JSONArray array = responseBody.optJSONArray("data");
                    int i = 0;
                    if (tag.equalsIgnoreCase("my")){
                        for (i = 0; i < array.length(); i++) {
                            try {
                                JSONObject data = array.getJSONObject(i);
                                GroupContestModel.ContestDatum contestModel = gson.fromJson(data.toString(), GroupContestModel.ContestDatum.class);

                                contestMyModelList.add(contestModel);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }else {
                        for (i = 0; i < array.length(); i++) {
                            try {
                                JSONObject data = array.getJSONObject(i);
                                GroupContestModel contestModel = gson.fromJson(data.toString(), GroupContestModel.class);

                                contestModelList.add(contestModel);

                            } catch (JSONException e) {
                                e.printStackTrace();
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
                            ((SinglesContestActivity)mContext).setTimer();
                        }

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
    }*/

    private void downloadExcel(String excel_name) {
        final String excelFile = excel_name + ".xlsx";
        final String csvFile = excel_name + ".csv";

        Dexter.withActivity((Activity) mContext)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        LogUtil.e(TAG, "onPermissionsChecked: " + ApiManager.EXCEL_DOWNLOAD + excelFile);
                        LogUtil.e(TAG, "onPermissionsChecked: " + ApiManager.EXCEL_DOWNLOAD + csvFile);
                        //if (!exists(ApiManager.EXCEL_DOWNLOAD + excelFile)) {
                            if (!exists(ApiManager.EXCEL_DOWNLOAD + csvFile)) {
                                CustomUtil.showTopSneakError(mContext, "File not Available Currently try after Some time");
                            } else {
                                CustomUtil.showTopSneakSuccess(mContext, "Download Start in your Download Manager");
                                DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);

                                Uri Download_Uri = Uri.parse(ApiManager.EXCEL_DOWNLOAD + csvFile);

                                DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                                request.setAllowedOverRoaming(false);
                                request.setTitle("League Report Downloading " + csvFile);
                                request.setDescription("Downloading " + csvFile);
                                request.setVisibleInDownloadsUi(true);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Fantafeat/" + "/" + csvFile);

                                Long refid = downloadManager.enqueue(request);
                            }
                        /*} else {
                            Toast.makeText(mContext, "Download Start in your Download Manager", Toast.LENGTH_LONG).show();
                            DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);

                            Uri Download_Uri = Uri.parse(ApiManager.EXCEL_DOWNLOAD + excelFile);

                            DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                            request.setAllowedOverRoaming(false);
                            request.setTitle("League Report Downloading " + excelFile);
                            request.setDescription("Downloading " + excelFile);
                            request.setVisibleInDownloadsUi(true);
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Fantafeat/" + "/" + excelFile);

                            Long refid = downloadManager.enqueue(request);
                        }*/
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    public static boolean exists(String URLName) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}