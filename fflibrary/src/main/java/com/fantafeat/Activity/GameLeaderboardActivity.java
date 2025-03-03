package com.fantafeat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fantafeat.Adapter.GameLeaderboardAdapter;
import com.fantafeat.Adapter.LeaderBoardListAdapter;
import com.fantafeat.Adapter.MatchRankAdapter;
import com.fantafeat.Model.GameLeaderboardModel;
import com.fantafeat.Model.Games;
import com.fantafeat.Model.UserLeaderBoardType;
import com.fantafeat.Model.UserLeaderboardList;
import com.fantafeat.Model.UserLeaderboardSubType;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameLeaderboardActivity extends BaseActivity implements View.OnClickListener  {

    @Override
    public void initClick() {

    }

    private TextView txtTabPrise,txtTabLeader,txtTotalPool,txtPtizeLbl,toolbar_title;
    private View viewPrise,viewLeader;
    private LinearLayout layLeader,layPrise,layNoData,layNoDataPrise,laySubType;
    private SwipeRefreshLayout swipe;
    private Spinner spinType;
    private ImageView toolbar_back,toolbar_info;
    private RecyclerView recyclerList,recyclerPrisePool;
    private String seriesMasterTypeId="",selectedSeriesTitle="",selectedSeriesId="";
    private String winningSeriesTree="";
    private String distributeSeries="";
    private HorizontalScrollView scrollType;
    private String distributeAmt="";
    private String winningTree="";
    private String winning_dec="",leaderboard_id="",leaderboard_title="Leaderboard",selectedGameId="",selectedGameName="";
    private String lb_sub_id,lb_master_id,week_id,mParam1="";
    private int offset=0,limit=50;
    private List<GameLeaderboardModel> list;
    private GameLeaderboardAdapter adapter;
    MatchRankAdapter matchRank;
    private boolean isGetData, isApiCall;
    private JSONArray jsonArray;
    private Typeface tfn,tfb;

    private List<String> gameNameList;
    private List<String> gameIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_leaderboard);
        lb_sub_id=getIntent().getStringExtra("leaderboard_id");
        leaderboard_title=getIntent().getStringExtra("leaderboard_title");

        tfn= ResourcesCompat.getFont(mContext, R.font.roboto_regular);
        tfb= ResourcesCompat.getFont(mContext, R.font.roboto_semi_bold);

        initData();
    }

    private void initData() {
        list=new ArrayList<>();
        jsonArray=new JSONArray();

        isGetData = true;
        isApiCall = true;

        txtTabPrise=findViewById(R.id.txtTabPrise);
        txtTabLeader=findViewById(R.id.txtTabLeader);
        toolbar_back=findViewById(R.id.toolbar_back);
        viewPrise=findViewById(R.id.viewPrise);
        viewLeader=findViewById(R.id.viewLeader);
        layLeader=findViewById(R.id.layLeader);
        layPrise=findViewById(R.id.layPrise);
        swipe=findViewById(R.id.swipe);
        recyclerList=findViewById(R.id.recyclerList);
        recyclerPrisePool=findViewById(R.id.recyclerPrisePool);
        spinType=findViewById(R.id.spinType);
        layNoData=findViewById(R.id.layNoData);
        layNoDataPrise=findViewById(R.id.layNoDataPrise);
        txtTotalPool=findViewById(R.id.txtTotalPool);
        txtPtizeLbl=findViewById(R.id.txtPtizeLbl);
        toolbar_info=findViewById(R.id.toolbar_info);
        laySubType=findViewById(R.id.laySubType);
        scrollType=findViewById(R.id.scrollType);
        toolbar_title=findViewById(R.id.toolbar_title);

        toolbar_title.setText(leaderboard_title);

        txtTabPrise.setOnClickListener(this);
        txtTabLeader.setOnClickListener(this);
        toolbar_back.setOnClickListener(this);

        LinearLayoutManager manager=new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        recyclerList.setLayoutManager(manager);
        adapter=new GameLeaderboardAdapter(mContext,list, preferences,winning_dec);
        recyclerList.setAdapter(adapter);

        recyclerPrisePool.setLayoutManager(new LinearLayoutManager(mContext));
        matchRank = new MatchRankAdapter(mContext, jsonArray);
        recyclerPrisePool.setAdapter(matchRank);

        recyclerList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (manager != null && manager.findLastCompletelyVisibleItemPosition() == list.size() - 1
                        && isGetData && isApiCall) {
                    getList();
                }
            }
        });

        swipe.setOnRefreshListener(() -> {
            list.clear();
            adapter.notifyDataSetChanged();
            isGetData = false;
            offset=0;
            getList();
        });

        toolbar_info.setOnClickListener(v -> {
            if(MyApp.getClickStatus()) {
                getTNCData();
            }
        });

        getGameList();
    }

    public void getTNCData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("game_id", selectedGameId);
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG,jsonObject.toString());

        HttpRestClient.postJSONNormal(mContext, true, ApiManager.gamesTncGetFF,
                jsonObject, new GetApiResult() {
                    @Override
                    public void onSuccessResult(JSONObject responseBody, int code) {
                        LogUtil.e("TAG", "gamesTncGetFF: " + responseBody.toString());
                        if (responseBody.optBoolean("status")){
                            JSONObject data=responseBody.optJSONObject("data");

                            if (data.optString("game_tnc_type").equalsIgnoreCase("Text")){
                                View view = LayoutInflater.from(mContext).inflate(R.layout.ludo_tnc_dialog, null);
                                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
                                bottomSheetDialog.setContentView(view);
                                bottomSheetDialog.setCancelable(false);
                                ((View) view.getParent()).setBackgroundResource(android.R.color.white);

                                TextView txtTitle = view.findViewById(R.id.txtTitle);
                                TextView txtTnc = view.findViewById(R.id.txtTnc);
                                ImageView imgClose = view.findViewById(R.id.imgClose);

                                txtTitle.setText(data.optString("game_name")+" Rules");

                                imgClose.setOnClickListener(vi->{
                                    bottomSheetDialog.dismiss();
                                });

                                if (!TextUtils.isEmpty(data.optString("games_tnc"))){

                                    txtTnc.setText(data.optString("games_tnc"));

                                    bottomSheetDialog.show();
                                }
                            }else {
                                startActivity(new Intent(mContext, WebViewActivity.class)
                                        .putExtra(ConstantUtil.WEB_TITLE,data.optString("game_name")+" Rules")
                                        .putExtra(ConstantUtil.WEB_URL,data.optString("games_tnc")));
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), responseBody.optString("msg"), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailureResult(String responseBody, int code) {

                    }
                });

    }

    private void getGameList() {
        try {
            JSONObject param=new JSONObject();
            param.put("user_id",preferences.getUserModel().getId());
            param.put("used_for","leaderboard");

            HttpRestClient.postJSONNormal(mContext, true, ApiManager.gameList,param , new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {
                    LogUtil.e("GameData", "getData: " + responseBody.toString());
                    if (responseBody.optInt("response_code")==200) {

                        gameNameList=new ArrayList<>();
                        gameIdList=new ArrayList<>();

                        JSONArray data=responseBody.optJSONArray("data");
                        for (int i=0;i<data.length();i++){
                            JSONObject obj=data.optJSONObject(i);
                            Games game=gson.fromJson(obj.toString(),Games.class);

                            gameIdList.add(game.getGameCode());
                            gameNameList.add(game.getGameName());
                        }

                        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(mContext, R.layout.spinner_text,gameNameList);
                        spinType.setAdapter(adapter1);

                        if (!TextUtils.isEmpty(selectedGameId)){
                            for (int i=0;i<gameIdList.size();i++){
                                if (gameIdList.get(i).equalsIgnoreCase(selectedGameId)){
                                    spinType.setSelection(i);
                                    selectedGameId=gameIdList.get(i);
                                    selectedGameName=gameNameList.get(i);
                                }
                            }
                        }else {
                            spinType.setSelection(0);
                        }

                        spinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                ((TextView) parent.getChildAt(0)).setTextColor(mContext.getResources().getColor(R.color.blackPrimary));
                                selectedGameId=gameIdList.get(position);
                                selectedGameName=gameNameList.get(position);
                                getLBList();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }
                }

                @Override
                public void onFailureResult(String responseBody, int code) {}
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void getLBList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("limit",1000);
            jsonObject.put("offset", 0);
            jsonObject.put("game_type", selectedGameId);
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.e(TAG, "getLBList: " + jsonObject.toString()+" url : "+ ApiManager.leaderboardList);

        HttpRestClient.postJSONNormal(mContext, false, ApiManager.leaderboardList, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e(TAG, "getLBList: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    UserLeaderboardSubType type = gson.fromJson(responseBody.toString(), UserLeaderboardSubType.class);
                    if (type.getData().size()>0) {
                        layNoData.setVisibility(View.GONE);
                        recyclerList.setVisibility(View.VISIBLE);
                        scrollType.setVisibility(View.VISIBLE);

                        layNoDataPrise.setVisibility(View.GONE);
                        recyclerPrisePool.setVisibility(View.VISIBLE);
                        txtPtizeLbl.setVisibility(View.VISIBLE);
                        txtTotalPool.setVisibility(View.VISIBLE);
                        //layPool.setVisibility(View.VISIBLE);

                        displayType(type);
                    }else {

                        lb_master_id="";
                        week_id="";

                        laySubType.removeAllViews();

                        layNoData.setVisibility(View.VISIBLE);
                        recyclerList.setVisibility(View.GONE);
                        scrollType.setVisibility(View.GONE);

                        layNoDataPrise.setVisibility(View.VISIBLE);
                        recyclerPrisePool.setVisibility(View.GONE);
                        txtPtizeLbl.setVisibility(View.GONE);
                        txtTotalPool.setVisibility(View.GONE);

                        matchRank.updateArray(new JSONArray());
                        txtTotalPool.setText( mContext.getResources().getString(R.string.rs)+" 00");
                        //layPool.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
            }
        });

    }

    private void displayType(UserLeaderboardSubType type) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        laySubType.removeAllViews();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;

        for (int k =0;k<type.getData().size();k++){

            UserLeaderboardSubType.Datum item=type.getData().get(k);

            View view = inflater.inflate(R.layout.user_board_type, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0,0,40,0);
            view.setLayoutParams(params);
            TextView txtTitle=view.findViewById(R.id.txtTitle);
            TextView txtDate=view.findViewById(R.id.txtDate);

            txtTitle.setText(item.getMasterTypeDesc());
            txtDate.setText(CustomUtil.dateConvert(item.getFromDt())+"-"+CustomUtil.dateConvert(item.getToDt()));
            txtDate.setTextSize(10f);

            txtTitle.setTextColor(mContext.getResources().getColor(R.color.blackPrimary));
            txtDate.setTextColor(mContext.getResources().getColor(R.color.blackPrimary));
            view.setBackgroundResource(R.drawable.btn_border);
            boolean isScroll=false;
            if (!TextUtils.isEmpty(lb_sub_id)){
                if (lb_sub_id.equalsIgnoreCase(item.getId())) {
                    txtTitle.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                    txtDate.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                    view.setBackgroundResource(R.drawable.bg_light_blue);

                    isApiCall = true;
                    isGetData = false;
                    list.clear();
                    offset=0;
                    adapter.notifyDataSetChanged();
                    lb_master_id=item.getLb_master_id();//getLbMasterId();
                    week_id=item.getId();
                    winningTree=item.getWinningTree();
                    distributeAmt=item.getDistributeAmount();
                    winning_dec=item.getWinningDec();
                    getList();

                    isScroll=true;

                   // scrollType.smoothScrollTo(0,k);

                    txtTotalPool.setText(CustomUtil.displayAmountFloat(mContext,distributeAmt));
                }
            }else {
                if (k==0){
                    txtTitle.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                    txtDate.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));

                    view.setBackgroundResource(R.drawable.bg_light_blue);
                    isApiCall = true;
                    isGetData = false;
                    list.clear();
                    offset=0;
                    adapter.notifyDataSetChanged();
                    lb_master_id=item.getLb_master_id();
                    week_id=item.getId();
                    winningTree=item.getWinningTree();
                    distributeAmt=item.getDistributeAmount();

                    txtTotalPool.setText(CustomUtil.displayAmountFloat(mContext,distributeAmt));

                    winning_dec=item.getWinningDec();
                    getList();
                }
            }
            laySubType.addView(view);

            if (isScroll){
                scrollType.post(new Runnable() {
                    public void run() {
                        scrollType.smoothScrollTo(view.getLeft(),0);
                    }
                });
            }

            //int finalK = k;
            view.setOnClickListener(v->{
                for (int i=0;i<laySubType.getChildCount();i++){
                    View otherView=laySubType.getChildAt(i);
                    TextView tit=otherView.findViewById(R.id.txtTitle);
                    TextView dt=otherView.findViewById(R.id.txtDate);
                    tit.setTextColor(mContext.getResources().getColor(R.color.blackPrimary));
                    dt.setTextColor(mContext.getResources().getColor(R.color.blackPrimary));
                    otherView.setBackgroundResource(R.drawable.btn_border);
                }
                TextView tit=v.findViewById(R.id.txtTitle);
                TextView dt=v.findViewById(R.id.txtDate);

                tit.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                dt.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));

                view.setBackgroundResource(R.drawable.bg_light_blue);

                isApiCall = true;
                isGetData = false;

                list.clear();
                offset=0;
                adapter.notifyDataSetChanged();
                lb_master_id=item.getLb_master_id();
                week_id=item.getId();
                winning_dec=item.getWinningDec();
                winningTree=item.getWinningTree();
                distributeAmt=item.getDistributeAmount();

                txtTotalPool.setText(CustomUtil.displayAmountFloat(mContext,distributeAmt));

                try {

                    if (!TextUtils.isEmpty(winningTree)){
                        layNoDataPrise.setVisibility(View.GONE);
                        recyclerPrisePool.setVisibility(View.VISIBLE);
                        txtPtizeLbl.setVisibility(View.VISIBLE);
                        txtTotalPool.setVisibility(View.VISIBLE);

                        // JSONArray js=new JSONArray(winningTree);

                        matchRank.updateArray(new JSONArray(winningTree));

                        //txtTotalPool.setText(CustomUtil.displayAmount(getApplicationContext(),distributeAmt));
                    }
                    else {
                        layNoDataPrise.setVisibility(View.VISIBLE);
                        recyclerPrisePool.setVisibility(View.GONE);
                        txtPtizeLbl.setVisibility(View.GONE);
                        txtTotalPool.setVisibility(View.GONE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                offset=0;limit=50;
                recyclerList.scrollToPosition(0);

                int center = (width - view.getWidth())/2;
                scrollType.scrollTo(view.getLeft() - center, view.getTop());
                //scrollType.scrollTo(finalK,0);

                getList();

            });

            try {

                if (!TextUtils.isEmpty(winningTree)){
                    layNoDataPrise.setVisibility(View.GONE);
                    recyclerPrisePool.setVisibility(View.VISIBLE);
                    txtPtizeLbl.setVisibility(View.VISIBLE);
                    txtTotalPool.setVisibility(View.VISIBLE);

                    // JSONArray js=new JSONArray(winningTree);

                    matchRank.updateArray(new JSONArray(winningTree));

                    //txtTotalPool.setText(CustomUtil.displayAmount(getApplicationContext(),distributeAmt));
                }
                else {
                    layNoDataPrise.setVisibility(View.VISIBLE);
                    recyclerPrisePool.setVisibility(View.GONE);
                    txtPtizeLbl.setVisibility(View.GONE);
                    txtTotalPool.setVisibility(View.GONE);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private void getList(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("limit",limit);
            jsonObject.put("offset", offset);
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("game_type", lb_master_id);
            jsonObject.put("lb_id", week_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.e(TAG, "getLBList: " + jsonObject.toString()+" url : "+ ApiManager.leaderboardRankList);
        boolean isProgress=true;
        if (swipe.isRefreshing()){
            isProgress=false;
        }
        HttpRestClient.postJSONNormal(mContext, isProgress, ApiManager.leaderboardRankList, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                swipe.setRefreshing(false);
                LogUtil.e(TAG, "getLBList: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {

                    JSONArray data=responseBody.optJSONArray("data");

                    if (offset==0){
                        JSONArray mydata=responseBody.optJSONArray("mydata");
                        for (int i = 0; i < mydata.length(); i++) {
                            JSONObject object=mydata.optJSONObject(i);
                            GameLeaderboardModel model=gson.fromJson(object.toString(),GameLeaderboardModel.class);
                            list.add(model);
                        }
                    }

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object=data.optJSONObject(i);
                        GameLeaderboardModel model=gson.fromJson(object.toString(),GameLeaderboardModel.class);
                        list.add(model);
                    }

                    adapter.notifyDataSetChanged();
                    adapter.updateWin(winning_dec);

                   // displayRank();

                    if (list.size() < limit) {
                        isGetData = false;
                        offset = 0;
                    }
                    else {
                        isGetData = true;
                        offset += limit;
                    }

                    if (list.size()>0){
                        /*nodata.setVisibility(View.GONE);
                        layData.setVisibility(View.VISIBLE);
                        layPool.setVisibility(View.VISIBLE);
                        layTopRank.setVisibility(View.VISIBLE);*/
                        layNoData.setVisibility(View.GONE);
                        recyclerList.setVisibility(View.VISIBLE);

                        //layPool.setVisibility(View.VISIBLE);
                    }
                    else {

                        layNoData.setVisibility(View.VISIBLE);
                        recyclerList.setVisibility(View.GONE);

                       // layPool.setVisibility(View.GONE);
                    }

                    if (TextUtils.isEmpty(winningTree)){
                        layNoDataPrise.setVisibility(View.VISIBLE);
                        recyclerPrisePool.setVisibility(View.GONE);
                        txtPtizeLbl.setVisibility(View.GONE);
                        txtTotalPool.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                swipe.setRefreshing(false);
                //preferences.setUserModel(new UserModel());

            }
        });
    }
/*
    private void displayRank() {
        if (list.size()>0){
            GameLeaderboardModel model0=list.get(0);//Loginuser
            GameLeaderboardModel model1=list.get(1);
            GameLeaderboardModel model2=list.get(2);
            GameLeaderboardModel model3=list.get(3);

            if (model0.getTotalRank().equalsIgnoreCase("1")){
                model1=list.get(0);
                model2=list.get(1);
                model3=list.get(2);
            }
            else if (model0.getTotalRank().equalsIgnoreCase("2")){
                model1=list.get(1);
                model2=list.get(0);
                model3=list.get(2);
            }
            else if (model0.getTotalRank().equalsIgnoreCase("3")){
                model1=list.get(1);
                model2=list.get(2);
                model3=list.get(0);
            }
            else {
                model1=list.get(1);
                model2=list.get(2);
                model3=list.get(3);
            }

            //1

            txtOneTeam.setText("@"+model1.getDisplayName());
            imgRankOne.setImageResource(R.drawable.rank_one_text);

            if (TextUtils.isEmpty(model1.getTotalPoint())){
                txtOnePoint.setText("-");
            }else {
                txtOnePoint.setText(model1.getTotalPoint());
            }

            if (winning_dec.equalsIgnoreCase("yes")){
                txtOneWin.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(model1.getTotalWinAmount())){
                    txtOneWin.setVisibility(View.GONE);
                }
                else {
                    if (model1.getTotalWinAmount().equalsIgnoreCase("0")){
                        txtOneWin.setVisibility(View.GONE);
                    }
                    else {
                        if (model1.getUserId().equals(preferences.getUserModel().getId())) {
                            txtOneWin.setText("You Won: "+CustomUtil.displayAmountFloat( mContext, model1.getTotalWinAmount()));
                        }else {
                            txtOneWin.setText("Won: "+CustomUtil.displayAmountFloat( mContext, model1.getTotalWinAmount()));
                        }
                    }
                }
            }else {
                txtOneWin.setVisibility(View.GONE);
            }

            //end
            //2

            txtTwoTeam.setText("@"+model2.getDisplayName());

            if (TextUtils.isEmpty(model2.getTotalPoint())){
                txtTwoPoint.setText("-");
            }else {
                txtTwoPoint.setText(model2.getTotalPoint());
            }

            if (winning_dec.equalsIgnoreCase("yes")){
                txtTwoWin.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(model2.getTotalWinAmount())){
                    txtTwoWin.setVisibility(View.GONE);
                }
                else {
                    if (model2.getTotalWinAmount().equalsIgnoreCase("0")){
                        txtTwoWin.setVisibility(View.GONE);
                    }
                    else {
                        if (model2.getUserId().equals(preferences.getUserModel().getId())) {
                            txtTwoWin.setText("You Won: "+CustomUtil.displayAmountFloat( mContext, model2.getTotalWinAmount()));
                        }else {
                            txtTwoWin.setText("Won: "+CustomUtil.displayAmountFloat( mContext, model2.getTotalWinAmount()));
                        }
                    }
                }
            }else {
                txtTwoWin.setVisibility(View.GONE);
            }

            //end
            //3

            txtThreeTeam.setText("@"+model3.getDisplayName());

            if (TextUtils.isEmpty(model3.getTotalPoint())){
                txtThreePoint.setText("-");
            }else {
                txtThreePoint.setText(model3.getTotalPoint());
            }

            if (winning_dec.equalsIgnoreCase("yes")){
                txtThreeWin.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(model3.getTotalWinAmount())){
                    txtThreeWin.setVisibility(View.GONE);
                }
                else {
                    if (model3.getTotalWinAmount().equalsIgnoreCase("0")){
                        txtThreeWin.setVisibility(View.GONE);
                    }
                    else {
                        if (model3.getUserId().equals(preferences.getUserModel().getId())) {
                            txtThreeWin.setText("You Won: "+CustomUtil.displayAmountFloat( mContext, model3.getTotalWinAmount()));
                        }else {
                            txtThreeWin.setText("Won: "+CustomUtil.displayAmountFloat( mContext, model3.getTotalWinAmount()));
                        }
                    }
                }
            }else {
                txtThreeWin.setVisibility(View.GONE);
            }

            //end

            if (model2.getTotalRank().equalsIgnoreCase("1")){
                imgRankTwo.setImageResource(R.drawable.rank_one_text);
            }
            else if (model2.getTotalRank().equalsIgnoreCase("2")){
                imgRankTwo.setImageResource(R.drawable.rank_two_text);
            }
            else if (model2.getTotalRank().equalsIgnoreCase("3")){
                imgRankTwo.setImageResource(R.drawable.rank_three_text);
            }

            if (model3.getTotalRank().equalsIgnoreCase("1")){
                imgRankThree.setImageResource(R.drawable.rank_one_text);
            }
            else if (model3.getTotalRank().equalsIgnoreCase("2")){
                imgRankThree.setImageResource(R.drawable.rank_two_text);
            }
            else if (model3.getTotalRank().equalsIgnoreCase("3")){
                imgRankThree.setImageResource(R.drawable.rank_three_text);
            }

        }

    }*/

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.txtTabPrise){
            txtTabPrise.setTextColor(getResources().getColor(R.color.colorPrimary));
            txtTabLeader.setTextColor(getResources().getColor(R.color.colorBlack));

            txtTabLeader.setTypeface(tfn, Typeface.NORMAL);
            txtTabPrise.setTypeface(tfb, Typeface.BOLD);
            viewPrise.setBackgroundResource(R.color.colorPrimary);
            viewLeader.setBackgroundResource(R.color.white_pure);
            layLeader.setVisibility(View.GONE);
            layPrise.setVisibility(View.VISIBLE);
        }
        else if (v.getId()==R.id.txtTabLeader){
            txtTabPrise.setTypeface(tfn, Typeface.NORMAL);
            txtTabLeader.setTypeface(tfb, Typeface.BOLD);

            txtTabLeader.setTextColor(getResources().getColor(R.color.colorPrimary));
            txtTabPrise.setTextColor(getResources().getColor(R.color.colorBlack));

            viewLeader.setBackgroundResource(R.color.colorPrimary);
            viewPrise.setBackgroundResource(R.color.white_pure);
            layLeader.setVisibility(View.VISIBLE);
            layPrise.setVisibility(View.GONE);
        }else if (v.getId()==R.id.toolbar_back){
            finish();
        }
    }

}