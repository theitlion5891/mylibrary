package com.fantafeat.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fantafeat.Activity.CompareTeamActivity;
import com.fantafeat.Activity.LeaderBoardActivity;
import com.fantafeat.Activity.SwipeTeamActivity;
import com.fantafeat.Adapter.LeaderBoardAdapter;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.Model.LeaderBoardModel;
import com.fantafeat.Model.PlayerListModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.BottomSheetTeam;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.google.gson.JsonSyntaxException;
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
import java.util.Date;
import java.util.List;

public class LeaderBoardFragment extends BaseFragment {

    RecyclerView match_leader_board_list;
    public ContestModel.ContestDatum contestData=new ContestModel.ContestDatum();
    List<LeaderBoardModel> leaderBoardModelList;
    int offset, limit;
    LeaderBoardAdapter adapter;
    LinearLayoutManager mLinearLayoutManager;
    private SwipeRefreshLayout pull_leaderboard;
    private RelativeLayout scroll_loading;
    private LinearLayout layNoData;
    boolean isApiCall, isGetData;
    private ImageView back_img, excel_download,imgPlace;
    private TextView leader_board_join, contest_left_total_team,txtPlace;

    private ImageView compare;
    public boolean compare_on = false;
    private boolean is_same_team_cancel = false;
    public int compare_count = 0;
    public String sportId = "1";
    public ArrayList<LeaderBoardModel> compare_list = new ArrayList<>();

    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    String[] p;

    public LeaderBoardFragment() {
    /*    contestData = model;
        sportId=contestData.getSportId();
        this.is_same_team_cancel=is_same_team_cancel;*/
    }

    public static LeaderBoardFragment newInstance(ContestModel.ContestDatum model,Boolean is_same_team_cancel) {
        LeaderBoardFragment fragment = new LeaderBoardFragment();
        Bundle args = new Bundle();
        args.putSerializable("contest_model", model);
        args.putSerializable("is_same_team_cancel", is_same_team_cancel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            contestData=(ContestModel.ContestDatum) getArguments().getSerializable("contest_model");
            is_same_team_cancel=getArguments().getBoolean("is_same_team_cancel");
        }
        if (contestData!=null){
            sportId=contestData.getSportId();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);
        initFragment(view);
        //getData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        leaderBoardModelList = new ArrayList<>();
        offset = 0;
        isGetData = false;
        compare_on = false;
        compare.setBackgroundResource(R.drawable.black_circle);
        if (!preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("Pending")){
            compare.setVisibility(View.VISIBLE);
            excel_download.setVisibility(View.VISIBLE);
        }else {
            compare.setVisibility(View.GONE);
            excel_download.setVisibility(View.GONE);
        }
        //compare.setImageResource(R.drawable.compare);
        compare_count = 0;
        compare_list = new ArrayList<>();

        getData();
        if(preferences.getPrefBoolean("get_player_data")){
            Log.e(TAG, "onResume: " );
            preferences.setPref("get_player_data",false);
            getTempTeamData();
        }
    }

    public void switchTeam(int pos, Date finalMatchDate){
        if (finalMatchDate.after(MyApp.getCurrentDate())) {
            Intent intent = new Intent(mContext, SwipeTeamActivity.class);
            intent.putExtra("team_id",leaderBoardModelList.get(pos).getTempTeamId());
            intent.putExtra("contest_id",leaderBoardModelList.get(pos).getContestId());
            intent.putExtra("user_join_team_id",leaderBoardModelList.get(pos).getId());
            intent.putExtra("joined_temp_team_id",contestData.getJoinedTeamTempTeamId());
            intent.putExtra("sport_id",contestData.getSportId());
            startActivity(intent);
        } else {
            CustomUtil.showTopSneakError(mContext, "Match Started!");
        }
    }

    @Override
    public void initControl(View view) {
        leaderBoardModelList = new ArrayList<>();
        offset = 0;
        limit = 100;
        isApiCall = true;
        isGetData = true;
        mLinearLayoutManager = new LinearLayoutManager(mContext);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }

        pull_leaderboard = view.findViewById(R.id.pull_leaderboard);
        match_leader_board_list = view.findViewById(R.id.match_leader_board_list);
        compare = view.findViewById(R.id.compare);
        excel_download = view.findViewById(R.id.excel_download);
        layNoData = view.findViewById(R.id.layNoData);
        imgPlace = view.findViewById(R.id.imgPlace);
        txtPlace = view.findViewById(R.id.txtPlace);

        adapter = new LeaderBoardAdapter(mContext, leaderBoardModelList, LeaderBoardFragment.this,is_same_team_cancel);
        match_leader_board_list.setLayoutManager(mLinearLayoutManager);
        match_leader_board_list.setAdapter(adapter);
    }

    public void onCompare(){
        //CustomUtil.showSnackBarLong(mContext,"Compare");
        startActivity(new Intent(mContext, CompareTeamActivity.class)
                .putExtra("compareList",compare_list)
                .putExtra("sportsId",preferences.getMatchModel().getSportId()));

    }

    @Override
    public void initClick() {
        pull_leaderboard.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                leaderBoardModelList = new ArrayList<>();
                offset = 0;
                isGetData = true;
                compare_on = false;
                //compare.setImageResource(R.drawable.compare);
                compare_count = 0;
                compare_list = new ArrayList<>();
                //compare_btn.setVisibility(View.GONE);
                if(preferences.getMatchModel() != null &&
                        !preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("pending")){
                    //updateScore();
                }
                getData();
            }
        });

        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MyApp.getClickStatus()){

                    if (compare_on){
                        compare_on=false;
                        compare.setBackgroundResource(R.drawable.black_circle);

                    }else {
                        for(int i=0;i<leaderBoardModelList.size();i++){
                            leaderBoardModelList.get(i).setSelected(false);
                        }
                        compare_on=true;
                        compare.setBackgroundResource(R.drawable.round_green_fill);
                    }
                    adapter.notifyDataSetChanged();
                   /* new FragmentUtil().addFragment((FragmentActivity) mContext,
                            R.id.fragment_container,
                            new CompareTeamFragment(compare_list),
                            ((HomeActivity) mContext).fragmentTag(46),
                            FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
                }
            }
        });

        excel_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadExcel(contestData.getId());
            }
        });

        match_leader_board_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mLinearLayoutManager != null &&
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition() == leaderBoardModelList.size() - 1
                        && isGetData && isApiCall) {

                    //scroll_loading.setVisibility(View.VISIBLE);
                    getData();
                }
            }
        });
    }

    private void getData() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("con_display_type", preferences.getMatchModel().getConDisplayType());
            jsonObject.put("contest_id", contestData.getId());
            jsonObject.put("offset", offset);
            jsonObject.put("limit", limit);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        boolean showProgress = true;
        if (pull_leaderboard != null && pull_leaderboard.isRefreshing()) {
            showProgress = false;
        }
        isApiCall = false;


        HttpRestClient.postJSON(mContext, showProgress, ApiManager.CONTEST_LEADER_BOARD, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                try {
                    checkPull();
                     LogUtil.e(TAG, "onSuccessResult: leader" + responseBody.toString());
                    if (responseBody.optBoolean("status")) {
                        JSONArray array = responseBody.optJSONArray("data");
                        int i = 0;

                        isApiCall = true;

                        for (i = 0; i < array.length(); i++) {
                            JSONObject object = array.optJSONObject(i);
                            LeaderBoardModel leaderBoardModel = gson.fromJson(object.toString(), LeaderBoardModel.class);
                            leaderBoardModelList.add(leaderBoardModel);
                        }
                        /*if (offset == 0) {


                        } else {
                            if (adapter != null) {
                                adapter.updateData(leaderBoardModelList);
                            } else {
                                adapter = new LeaderBoardAdapter(mContext, leaderBoardModelList, LeaderBoardFragment.this,is_same_team_cancel);
                                match_leader_board_list.setLayoutManager(mLinearLayoutManager);
                                match_leader_board_list.setAdapter(adapter);
                            }
                        }*/
                        Log.e(TAG, "onSuccessResult: size: " + leaderBoardModelList.size()+"   "+array.length());
                        adapter.updateData(leaderBoardModelList);

                        //offset += array.length();
                        if (limit > array.length()) {
                            isGetData = false;
                        }else{
                            if (array.length() > 0 && array.length()==limit){
                                offset += limit;
                                isGetData = true;
                            }
                        }
                    }
                    checkData();
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {}
        });
    }

    private void checkPull() {
        if (pull_leaderboard != null && pull_leaderboard.isRefreshing()) {
            pull_leaderboard.setRefreshing(false);
        }
        isApiCall = false;
        //scroll_loading.setVisibility(View.GONE);
    }

    private void downloadExcel(String excel_name) {
        final String excelFile = excel_name + ".xlsx";
        final String csvFile = excel_name + ".csv";

        Dexter.withActivity((Activity) mContext)
                .withPermissions(p)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        //LogUtil.e(TAG, "onPermissionsChecked: " + ApiManager.EXCEL_DOWNLOAD + excelFile);

                       // if (!exists(ApiManager.EXCEL_DOWNLOAD + excelFile)) {
                           if (report.areAllPermissionsGranted()){
                               if (!exists(ApiManager.EXCEL_DOWNLOAD + csvFile)) {
                                   CustomUtil.showTopSneakError(mContext, "File not Available Currently try after Some time");
                               }
                               else {
                                   CustomUtil.showTopSneakSuccess(mContext, "Download Start in your Download Manager");
                                   DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);

                                   Uri Download_Uri = Uri.parse(ApiManager.EXCEL_DOWNLOAD + csvFile);
                                   LogUtil.e(TAG, "onPermissionsChecked: " + Download_Uri);

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
                           }
                       /* } else {
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

    public void getTeamDetail(String temp_team_id, final String tempTeamName) {
        final List<PlayerModel> playerModelList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("temp_team_id", temp_team_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.TEMP_TEMP_DETAIL_BY_ID, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONArray data = responseBody.optJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.optJSONObject(i);
                        PlayerModel playerModel = gson.fromJson(object.toString(), PlayerModel.class);
                        playerModelList.add(playerModel);
                    }
                    new Handler().post(() -> {
                        BottomSheetTeam bottomSheetTeam = new BottomSheetTeam(mContext, tempTeamName,temp_team_id, playerModelList,
                                true);
                        bottomSheetTeam.show(((LeaderBoardActivity) mContext).getSupportFragmentManager(),
                                BottomSheetTeam.TAG);
                    });

                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void getTempTeamData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("con_display_type", preferences.getMatchModel().getConDisplayType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, false, ApiManager.TEMP_TEAM_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    getTempTeamDetailData(responseBody.optJSONArray("data"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void getTempTeamDetailData(final JSONArray data) {
        final List<PlayerListModel> playerListModels = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("con_display_type", preferences.getMatchModel().getConDisplayType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, false, ApiManager.TEMP_TEAM_DETAIL_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {

                    int i = 0, j = 0;
                    JSONArray array = responseBody.optJSONArray("data");
                    for (j = 0; j < data.length(); j++) {
                        int wk = 0, bat = 0, ar = 0, bowl = 0, team1 = 0, team2 = 0,lineup_count = 0,cr=0;
                        float credit_point = 0;
                        JSONObject main = data.optJSONObject(j);

                        PlayerListModel playerListModel = new PlayerListModel();
                        playerListModel.setId(main.optString("id"));
                        playerListModel.setTempTeamName(main.optString("temp_team_name"));
                        playerListModel.setMatchId(main.optString("match_id"));
                        playerListModel.setContestDisplayTypeId(main.optString("contest_display_type_id"));
                        playerListModel.setUserId(main.optString("user_id"));
                        playerListModel.setTotalPoint(main.optString("total_point"));

                        List<PlayerModel> playerDetails = new ArrayList<>();
                        for (i = 0; i < array.length(); i++) {
                            JSONObject object = array.optJSONObject(i);

                            PlayerModel playerDetail = gson.fromJson(object.toString(), PlayerModel.class);

                            if (playerDetail.getTempTeamId().equals(playerListModel.getId())) {

                                credit_point += CustomUtil.convertFloat(playerDetail.getPlayerRank());
                                if (preferences.getMatchModel().getSportId().equals("1")){
                                    switch (playerDetail.getPlayerType().toLowerCase()) {
                                        case "wk":
                                            wk += 1;
                                            break;
                                        case "bat":
                                            bat += 1;
                                            break;
                                        case "ar":
                                            ar += 1;
                                            break;
                                        case "bowl":
                                            bowl += 1;
                                            break;
                                    }
                                }
                                if (preferences.getMatchModel().getSportId().equals("2")){
                                    switch (playerDetail.getPlayerType().toLowerCase()) {
                                        case "gk":
                                            wk += 1;
                                            break;
                                        case "def":
                                            bat += 1;
                                            break;
                                        case "mid":
                                            ar += 1;
                                            break;
                                        case "for":
                                            bowl += 1;
                                            break;
                                    }
                                }
                                if (preferences.getMatchModel().getSportId().equals("3")){
                                    switch (playerDetail.getPlayerType().toLowerCase()) {
                                        case "of":
                                            wk += 1;
                                            break;
                                        case "if":
                                            bat += 1;
                                            break;
                                        case "pit":
                                            ar += 1;
                                            break;
                                        case "cat":
                                            bowl += 1;
                                            break;
                                    }
                                }
                                if (preferences.getMatchModel().getSportId().equals("6")){
                                    switch (playerDetail.getPlayerType().toLowerCase()) {
                                        case "gk":
                                            wk += 1;
                                            break;
                                        case "def":
                                            bat += 1;
                                            break;
                                        case "fwd":
                                            ar += 1;
                                            break;
                                    }
                                }
                                if (preferences.getMatchModel().getSportId().equals("7")){
                                    switch (playerDetail.getPlayerType().toLowerCase()) {
                                        case "def":
                                            wk += 1;
                                            break;
                                        case "ar":
                                            bat += 1;
                                            break;
                                        case "raid":
                                            ar += 1;
                                            break;
                                    }
                                }
                                if (preferences.getMatchModel().getSportId().equals("4")){
                                    switch (playerDetail.getPlayerType().toLowerCase()) {
                                        case "pg":
                                            wk += 1;
                                            break;
                                        case "sg":
                                            bat += 1;
                                            break;
                                        case "sf":
                                            ar += 1;
                                            break;
                                        case "pf":
                                            bowl += 1;
                                            break;
                                        case "cr":
                                            cr += 1;
                                            break;
                                    }
                                }

                                if (playerDetail.getTeamId().equals(preferences.getMatchModel().getTeam1())) {
                                    team1 += 1;
                                }
                                if (playerDetail.getTeamId().equals(preferences.getMatchModel().getTeam2())) {
                                    team2 += 1;
                                }
                                if (playerDetail.getIsCaptain().equalsIgnoreCase("yes")) {
                                    playerListModel.setC_player_name(playerDetail.getPlayerName());
                                    playerListModel.setC_player_sname(playerDetail.getPlayerSname());
                                    playerListModel.setC_player_avg_point(playerDetail.getPlayerAvgPoint());
                                    playerListModel.setC_player_rank(playerDetail.getPlayerRank());
                                    playerListModel.setC_player_img(playerDetail.getPlayerImage());
                                    playerListModel.setC_player_xi(playerDetail.getPlayingXi());
                                    playerListModel.setC_player_type(playerDetail.getPlayerType());
                                    playerListModel.setCTeam_id(playerDetail.getTeamId());
                                }
                                if (playerDetail.getIsWiseCaptain().equalsIgnoreCase("yes")) {
                                    playerListModel.setVc_player_name(playerDetail.getPlayerName());
                                    playerListModel.setVc_player_sname(playerDetail.getPlayerSname());
                                    playerListModel.setVc_player_avg_point(playerDetail.getPlayerAvgPoint());
                                    playerListModel.setVc_player_rank(playerDetail.getPlayerRank());
                                    playerListModel.setVc_player_img(playerDetail.getPlayerImage());
                                    playerListModel.setVc_player_xi(playerDetail.getPlayingXi());
                                    playerListModel.setVc_player_type(playerDetail.getPlayerType());
                                    playerListModel.setVCTeam_id(playerDetail.getTeamId());
                                }

                                if(preferences.getPlayerList() != null){
                                    for(PlayerModel playerModel : preferences.getPlayerList()){
                                        if(playerModel.getPlayerId().equalsIgnoreCase(playerDetail.getPlayerId())){
                                            playerDetail.setPlayingXi(playerModel.getPlayingXi());
                                            playerDetail.setOther_text(playerModel.getOther_text());
                                            playerDetail.setOther_text2(playerModel.getOther_text2());
                                            break;
                                        }
                                    }
                                }

                                if(preferences.getMatchModel().getTeamAXi().toLowerCase().equals("yes") || preferences.getMatchModel().getTeamBXi().toLowerCase().equals("yes")){

                                    if(playerDetail.getPlayingXi().toLowerCase().equals("no") &&
                                            !playerDetail.getOther_text().equalsIgnoreCase("substitute")){
                                        lineup_count++;
                                        LogUtil.e(TAG, "onSuccessResult: ABC "+playerDetail.getPlayerName() );
                                    }
                                }
                                playerDetails.add(playerDetail);
                            }
                        }

                        playerListModel.setPlayerDetailList(playerDetails);
                        playerListModel.setTeam1_count("" + team1);
                        playerListModel.setTeam2_count("" + team2);
                        playerListModel.setCreditTotal("" + credit_point);
                        playerListModel.setWk_count("" + wk);
                        playerListModel.setBat_count("" + bat);
                        playerListModel.setAr_count("" + ar);
                        playerListModel.setBowl_count("" + bowl);
                        playerListModel.setIsJoined("No");
                        playerListModel.setIsSelected("No");
                        LogUtil.e(TAG, "onSuccessResult: "+lineup_count );
                        playerListModel.setLineup_count(""+ lineup_count);
                        playerListModel.setTeam1_sname(preferences.getMatchModel().getTeam1Sname());
                        playerListModel.setTeam2_sname(preferences.getMatchModel().getTeam2Sname());
                        playerListModels.add(playerListModel);
                    }
                    preferences.setPlayerModel(playerListModels);
                    //joinDirectContest();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void checkData(){
        LogUtil.d("selSports",preferences.getMatchModel().getSportId()+" ");
        switch (preferences.getMatchModel().getSportId()+""){
            case "1":
                if (leaderBoardModelList.size()>0){
                    match_leader_board_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_leader_board_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.cricket_placeholder);
                    txtPlace.setText(mContext.getResources().getString(R.string.no_record_found));
                }
                break;
            case "2":
                if (leaderBoardModelList.size()>0){
                    match_leader_board_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_leader_board_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.football_placeholder);
                    txtPlace.setText(mContext.getResources().getString(R.string.no_record_found));
                }
                break;
            case "3":
                if (leaderBoardModelList.size()>0){
                    match_leader_board_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_leader_board_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.baseball_placeholder);
                }
                break;
            case "4":
                if (leaderBoardModelList.size()>0){
                    match_leader_board_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_leader_board_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.basketball_placeholder);
                }
                break;
            case "5":
                if (leaderBoardModelList.size()>0){
                    match_leader_board_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_leader_board_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.vollyball_placeholder);
                }
                break;
            case "6"://6
                if (leaderBoardModelList.size()>0){
                    match_leader_board_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_leader_board_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.handball_placeholder);
                    txtPlace.setText(mContext.getResources().getString(R.string.no_record_found));
                }
                break;
            case "7"://6
                if (leaderBoardModelList.size()>0){
                    match_leader_board_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    match_leader_board_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.kabaddi_placeholder);
                    txtPlace.setText(mContext.getResources().getString(R.string.no_record_found));
                }
                break;
        }

    }
}