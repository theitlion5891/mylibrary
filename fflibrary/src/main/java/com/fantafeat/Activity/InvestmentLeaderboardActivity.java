package com.fantafeat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fantafeat.Adapter.LeaderBoardListAdapter;
import com.fantafeat.Adapter.MatchRankAdapter;
import com.fantafeat.Model.InvestmentLBTypeModel;
import com.fantafeat.Model.UserLeaderboardList;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.BottomSheetTeam;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.WinningTreeSheet;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InvestmentLeaderboardActivity extends BaseActivity {

    private String title;
    private ImageView toolbar_back,imgInfo;
    private TextView toolbar_title;
    private RecyclerView recyclerList,recyclerPrisePool;
    private LinearLayout laySubType,nodata,layData,layMain,/*layPool,*/layPrise,layLeader,layNoDataPrise;
    private HorizontalScrollView scrollType;
    private SwipeRefreshLayout swipe;
    //private NestedScrollView scroll;
   // private ConstraintLayout layTopRank;
   // private ImageView imgRankOne,imgRankTwo,imgRankThree;

    private TextView /*txtPool,*/btnLeaderboard;
    private int offset=0,limit=50;
    private List<UserLeaderboardList.Datum> list;
    private LeaderBoardListAdapter adapter;
    private boolean isGetData, isApiCall;
    private String masterTypeId="";

    private String distributeAmt="";
    private String winningTree="";
    private String winning_dec="";
    private boolean isPoolshow=false;
    private String lb_id="",lb_sub_id="";
    private TextView txtTabPrise,txtTabLeader,txtTotalPool,txtPtizeLbl;
    private View viewPrise,viewLeader;
    private JSONArray jsonArray;
    MatchRankAdapter matchRank;

    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_leaderboard);

        title=getIntent().getStringExtra("title");

        imgInfo=findViewById(R.id.imgInfo);
        toolbar_back=findViewById(R.id.toolbar_back);
        toolbar_title=findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);

        list=new ArrayList<>();

        isGetData = true;
        isApiCall = true;


        scrollType=findViewById(R.id.scrollType);
        recyclerList=findViewById(R.id.recyclerList);
        nodata=findViewById(R.id.nodata);
        layData=findViewById(R.id.layData);
        layMain=findViewById(R.id.layMain);
        swipe=findViewById(R.id.swipe);

        laySubType=findViewById(R.id.laySubType);
        btnLeaderboard=findViewById(R.id.btnLeaderboard);
      /*  txtPool=findViewById(R.id.txtPool);
        layPool=findViewById(R.id.layPool);*/
        layPrise=findViewById(R.id.layPrise);
        layLeader=findViewById(R.id.layLeader);
        txtTabPrise=findViewById(R.id.txtTabPrise);
        txtTabLeader=findViewById(R.id.txtTabLeader);
        txtTotalPool=findViewById(R.id.txtTotalPool);
        txtPtizeLbl=findViewById(R.id.txtPtizeLbl);
        viewPrise=findViewById(R.id.viewPrise);
        viewLeader=findViewById(R.id.viewLeader);
        recyclerPrisePool=findViewById(R.id.recyclerPrisePool);
        layNoDataPrise=findViewById(R.id.layNoDataPrise);

        toolbar_back.setOnClickListener(v->{
            finish();
        });

        LinearLayoutManager manager=new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        recyclerList.setLayoutManager(manager);
        adapter=new LeaderBoardListAdapter(mContext,list, position -> {
            if (MyApp.getClickStatus()){
                // selectedSeriesId=masterTypeId;
                if (position!=-1){

                    startActivity(new Intent(mContext,InvesterDetailsActivity.class)
                            .putExtra("userModal",list.get(position))
                            .putExtra("selectedSeriesId",masterTypeId)
                    );
                }

            }
           /* startActivity(new Intent(mContext, UserDetailActivity.class)
                    .putExtra("leaderboard_item",list.get(position))
                    .putExtra("selectedSeriesTitle",selectedSeriesTitle)
                    .putExtra("selectedSeriesId",selectedSeriesId)
            );*/
        },winning_dec);
        recyclerList.setAdapter(adapter);
        //recyclerList.setNestedScrollingEnabled(false);
        //recyclerList.setHasFixedSize(false);

        jsonArray=new JSONArray();

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

     /*   layPool.setOnClickListener(v->{
            showLeaderboard();
        });*/

        swipe.setOnRefreshListener(() -> {
            list.clear();
            adapter.notifyDataSetChanged();
            isGetData = false;
            offset=0;
            getList();
        });

        imgInfo.setOnClickListener(v -> {
            getTNCData();
        });

        Typeface tfn= ResourcesCompat.getFont(mContext, R.font.roboto_regular);
        Typeface tfb= ResourcesCompat.getFont(mContext, R.font.roboto_bold);

        txtTabPrise.setOnClickListener(v -> {
            txtTabPrise.setTextColor(getResources().getColor(R.color.colorPrimary));
            txtTabLeader.setTextColor(getResources().getColor(R.color.colorBlack));

            txtTabLeader.setTypeface(tfn, Typeface.NORMAL);
            txtTabPrise.setTypeface(tfb, Typeface.BOLD);
            viewPrise.setBackgroundResource(R.color.colorPrimary);
            viewLeader.setBackgroundResource(R.color.white_pure);
            layLeader.setVisibility(View.GONE);
            layPrise.setVisibility(View.VISIBLE);
        });

        txtTabLeader.setOnClickListener(v -> {
            txtTabPrise.setTypeface(tfn, Typeface.NORMAL);
            txtTabLeader.setTypeface(tfb, Typeface.BOLD);

            txtTabLeader.setTextColor(getResources().getColor(R.color.colorPrimary));
            txtTabPrise.setTextColor(getResources().getColor(R.color.colorBlack));

            viewLeader.setBackgroundResource(R.color.colorPrimary);
            viewPrise.setBackgroundResource(R.color.white_pure);
            layLeader.setVisibility(View.VISIBLE);
            layPrise.setVisibility(View.GONE);
        });

        getSeries();
    }


    public void getTNCData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("game_id", "4");

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
                        }
                    }
                    @Override
                    public void onFailureResult(String responseBody, int code) {

                    }
                });

    }

    private void showLeaderboard() {

        WinningTreeSheet bottomSheetTeam = new WinningTreeSheet(mContext, distributeAmt,winningTree
                ,distributeAmt);
        bottomSheetTeam.show(((InvestmentLeaderboardActivity) mContext).getSupportFragmentManager(),
                BottomSheetTeam.TAG);

     /*   WinningTreeSheet bottomSheetTeam = new WinningTreeSheet( mContext, amt,tree );
        bottomSheetTeam.show(((InvestmentLeaderboardActivity)  mContext).getSupportFragmentManager(),
                BottomSheetTeam.TAG);*/

    }

    private void getSeries() {
        HttpRestClient.postJSON(mContext, true, ApiManager.investmentLeaderBoardList, new JSONObject(), new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("TAG", "onSuccessResult getSeries: " + responseBody.toString() );
                JSONArray data=responseBody.optJSONArray("data");
                if (responseBody.optBoolean("status")){
                    if (data.length()>0) {
                        displayType(data);
                    }else {
                        laySubType.removeAllViews();
                        nodata.setVisibility(View.VISIBLE);
                        layData.setVisibility(View.GONE);
                        //layPool.setVisibility(View.GONE);
                    }
                }
                else{
                    CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void getList() {

        try {
            JSONObject param=new JSONObject();
            param.put("lb_master_type_id",masterTypeId);
            param.put("user_id",preferences.getUserModel().getId());
            param.put("offset",offset);
            param.put("limit",limit);

            boolean isProgress=true;
            if (swipe.isRefreshing()){
                isProgress=false;
            }

            LogUtil.e("TAG", "param: " + param.toString() );
            HttpRestClient.postJSON(mContext, isProgress, ApiManager.investmentLeaderboardRankList,param, new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {
                    swipe.setRefreshing(false);
                    LogUtil.e("TAG", "onSuccessResult: " + responseBody.toString() );

                    if(responseBody.optBoolean("status")){

                        UserLeaderboardList lists = gson.fromJson(responseBody.toString(), UserLeaderboardList.class);
                        list.addAll(lists.getData());

                        adapter.notifyDataSetChanged();
                        adapter.updateWin(winning_dec);

                        if (list.size() < limit) {
                            isGetData = false;
                            offset = 0;
                        }
                        else {
                            isGetData = true;
                            offset += limit;
                        }

                        if (list.size()>0){
                            nodata.setVisibility(View.GONE);
                            layData.setVisibility(View.VISIBLE);
                           // layPool.setVisibility(View.VISIBLE);
                            //layTopRank.setVisibility(View.VISIBLE);
                        }
                        else {
                            nodata.setVisibility(View.VISIBLE);
                            layData.setVisibility(View.GONE);
                           // layPool.setVisibility(View.GONE);
                            // layTopRank.setVisibility(View.GONE);
                        }

                        //list.get(2).setTotalRank("1");
                        //list.get(3).setTotalRank("2");

                        //displayRank();
                    }
                    else{
                        nodata.setVisibility(View.VISIBLE);
                        layData.setVisibility(View.GONE);
                        //layPool.setVisibility(View.GONE);
                       // layTopRank.setVisibility(View.GONE);
                        CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                    }
                }

                @Override
                public void onFailureResult(String responseBody, int code) {
                    swipe.setRefreshing(false);
                }

            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayType(JSONArray  data) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        laySubType.removeAllViews();
        for (int k =0;k<data.length();k++){

            InvestmentLBTypeModel item=gson.fromJson(data.optJSONObject(k).toString(),InvestmentLBTypeModel.class);

            View view = inflater.inflate(R.layout.user_board_type, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0,0,40,0);
            view.setLayoutParams(params);
            TextView txtTitle=view.findViewById(R.id.txtTitle);
            TextView txtDate=view.findViewById(R.id.txtDate);

            txtTitle.setText(item.getMasterTypeDesc());
            txtDate.setText(CustomUtil.dateConvert(item.getFromDt())+"-"+CustomUtil.dateConvert(item.getToDt()));

            if (!TextUtils.isEmpty(lb_sub_id)){
                if (lb_sub_id.equalsIgnoreCase(item.getId())) {
                    txtTitle.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                    // txtDate.setTextColor(mContext.getResources().getColor(R.color.white));
                    view.setBackgroundResource(R.drawable.bg_light_blue);
                    isApiCall = true;
                    isGetData = false;
                    list.clear();
                    adapter.notifyDataSetChanged();
                    masterTypeId=item.getId();
                    winningTree=item.getWinningTree();
                    distributeAmt=item.getDistributeAmount();
                    winning_dec=item.getWinningDec();
                    getList();

                    txtTotalPool.setText(CustomUtil.displayAmountFloat(getApplicationContext(),distributeAmt));
                    //txtPool.setText(CustomUtil.displayAmountFloat(mContext,distributeAmt));
                }
            }else {
                if (k==0){
                    txtTitle.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                    // txtDate.setTextColor(mContext.getResources().getColor(R.color.white));
                    view.setBackgroundResource(R.drawable.bg_light_blue);
                    isApiCall = true;
                    isGetData = false;
                    list.clear();
                    adapter.notifyDataSetChanged();
                    masterTypeId=item.getId();
                    winningTree=item.getWinningTree();
                    distributeAmt=item.getDistributeAmount();

                    txtTotalPool.setText(CustomUtil.displayAmountFloat(getApplicationContext(),distributeAmt));
                    //txtPool.setText(CustomUtil.displayAmountFloat(mContext,distributeAmt));

                    winning_dec=item.getWinningDec();
                    getList();
                }
            }
            laySubType.addView(view);

            view.setOnClickListener(v->{
                for (int i=0;i<laySubType.getChildCount();i++){
                    View otherView=laySubType.getChildAt(i);
                    TextView tit=otherView.findViewById(R.id.txtTitle);
                    TextView dt=otherView.findViewById(R.id.txtDate);
                    tit.setTextColor(mContext.getResources().getColor(R.color.black_pure));
                    //dt.setTextColor(mContext.getResources().getColor(R.color.black));
                    otherView.setBackgroundResource(R.drawable.btn_border);
                }
                TextView tit=v.findViewById(R.id.txtTitle);
                TextView dt=v.findViewById(R.id.txtDate);

                tit.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                //dt.setTextColor(mContext.getResources().getColor(R.color.white));

                view.setBackgroundResource(R.drawable.bg_light_blue);

                isApiCall = true;
                isGetData = false;

                list.clear();
                adapter.notifyDataSetChanged();
                masterTypeId=item.getId();

                winningTree=item.getWinningTree();
                distributeAmt=item.getDistributeAmount();
                winning_dec=item.getWinningDec();

                txtTotalPool.setText(CustomUtil.displayAmountFloat(getApplicationContext(),distributeAmt));

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
                //txtPool.setText(CustomUtil.displayAmountFloat(mContext,distributeAmt));

                offset=0;limit=50;
                recyclerList.scrollToPosition(0);

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

}