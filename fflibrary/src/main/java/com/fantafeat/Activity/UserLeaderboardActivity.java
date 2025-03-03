package com.fantafeat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fantafeat.Adapter.LeaderBoardListAdapter;
import com.fantafeat.Adapter.MatchRankAdapter;
import com.fantafeat.Model.UserLeaderBoardType;
import com.fantafeat.Model.UserLeaderboardList;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserLeaderboardActivity extends BaseActivity implements View.OnClickListener {

    private TextView txtTabPrise,txtTabLeader,txtTotalPool,txtPtizeLbl,toolbar_title;
    private View viewPrise,viewLeader;
    private LinearLayout layLeader,layPrise,layNoData,layNoDataPrise;
    private SwipeRefreshLayout swipe;
    private Spinner spinType;
    private ImageView toolbar_back,toolbar_info;
    private RecyclerView recyclerList,recyclerPrisePool;
    private String seriesMasterTypeId="",selectedSeriesTitle="",selectedSeriesId="";
    private String winningSeriesTree="";
    private String distributeSeries="";
    private String distributeAmt="";
    private String winningTree="";
    private String winning_dec="",leaderboard_id="",leaderboard_title="Leaderboard";
    private int offset=0,limit=50;
    private List<UserLeaderboardList.Datum> list;
    private LeaderBoardListAdapter adapter;
    MatchRankAdapter matchRank;
    private boolean isGetData, isApiCall;
    private JSONArray jsonArray;
    private Typeface tfn,tfb;

    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_leaderboard);

        leaderboard_id=getIntent().getStringExtra("leaderboard_id");
        leaderboard_title=getIntent().getStringExtra("leaderboard_title");

        tfn= ResourcesCompat.getFont(mContext, R.font.roboto_regular);
        tfb= ResourcesCompat.getFont(mContext, R.font.roboto_bold);

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
        toolbar_title=findViewById(R.id.toolbar_title);

        toolbar_title.setText(leaderboard_title);

        txtTabPrise.setOnClickListener(this);
        txtTabLeader.setOnClickListener(this);
        toolbar_back.setOnClickListener(this);

        LinearLayoutManager manager=new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        recyclerList.setLayoutManager(manager);
        adapter=new LeaderBoardListAdapter(mContext,list, position -> {
            if (MyApp.getClickStatus()){
                selectedSeriesId=seriesMasterTypeId;
                /*if (tabType.equalsIgnoreCase("series")){
                    selectedSeriesId=seriesMasterTypeId;
                }else {
                    selectedSeriesId=masterTypeId;
                }
                if (position!=-1){
                    new FragmentUtil().addFragment((FragmentActivity) mContext,
                            R.id.fragment_container,
                            UserDetailActivity.newInstance(list.get(position),selectedSeriesTitle,selectedSeriesId),
                            ((HomeActivity) mContext).fragmentTag(51),
                            FragmentUtil.ANIMATION_TYPE.CUSTOM);
                }*/
                startActivity(new Intent(mContext, UserDetailActivity.class)
                    .putExtra("leaderboard_item",list.get(position))
                    .putExtra("selectedSeriesTitle",selectedSeriesTitle)
                    .putExtra("selectedSeriesId",selectedSeriesId)
                );
            }

        },winning_dec);
        recyclerList.setAdapter(adapter);

        recyclerPrisePool.setLayoutManager(new LinearLayoutManager(mContext));

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
                View view = LayoutInflater.from(mContext).inflate(R.layout.ludo_tnc_dialog, null);
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.setCancelable(false);
                ((View) view.getParent()).setBackgroundResource(android.R.color.white);

                TextView txtTitle = view.findViewById(R.id.txtTitle);
                TextView txtTnc = view.findViewById(R.id.txtTnc);
                ImageView imgClose = view.findViewById(R.id.imgClose);

                txtTitle.setText("Terms & Conditions");

                imgClose.setOnClickListener(vi->{
                    bottomSheetDialog.dismiss();
                });

                if (!TextUtils.isEmpty(preferences.getUpdateMaster().getLb_tnc())){

                    txtTnc.setText(preferences.getUpdateMaster().getLb_tnc());

                    bottomSheetDialog.show();
                }
            }
        });

        getType();
    }

    private void getType() {

        HttpRestClient.postJSON(this, true, ApiManager.userLeaderboardType, new JSONObject(), new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                try {
                    LogUtil.e("TAG", "onSuccessResult: " + responseBody.toString() );
                    UserLeaderBoardType type = gson.fromJson(responseBody.toString(), UserLeaderBoardType.class);
                    if(type.getStatus()){

                        List<String> names=new ArrayList<>();
                        List<String> ids=new ArrayList<>();
                        List<String> winDesc=new ArrayList<>();

                        if (type.getData().size()>0){
                            for (int i=0;i<type.getData().size();i++){
                                UserLeaderBoardType.Datum item=type.getData().get(i);
                                names.add(item.getMasterTypeDesc());
                                ids.add(item.getId());
                                winDesc.add(item.getWinningDec());
                            }

                            seriesMasterTypeId=ids.get(0);
                            winningSeriesTree=type.getData().get(0).getWinning_tree();
                            distributeAmt=type.getData().get(0).getDistribute_amount();
                            txtTotalPool.setText(CustomUtil.displayAmountFloat(getApplicationContext(),distributeAmt));

                            if (!TextUtils.isEmpty(winningSeriesTree)){
                                layNoDataPrise.setVisibility(View.GONE);
                                recyclerPrisePool.setVisibility(View.VISIBLE);
                                txtPtizeLbl.setVisibility(View.VISIBLE);
                                txtTotalPool.setVisibility(View.VISIBLE);

                                jsonArray=new JSONArray(winningSeriesTree);
                                matchRank = new MatchRankAdapter(mContext, jsonArray);
                                recyclerPrisePool.setAdapter(matchRank);

                            }
                            else {
                                layNoDataPrise.setVisibility(View.VISIBLE);
                                recyclerPrisePool.setVisibility(View.GONE);
                                txtPtizeLbl.setVisibility(View.GONE);
                                txtTotalPool.setVisibility(View.GONE);
                            }

                            ArrayAdapter<String> adapter1=new ArrayAdapter<String>(mContext, R.layout.spinner_text,names);
                            spinType.setAdapter(adapter1);

                            distributeSeries=type.getData().get(0).getDistribute_amount();
                            if (leaderboard_id.equalsIgnoreCase("") && leaderboard_id.equalsIgnoreCase("0")){
                                selectedSeriesTitle=names.get(0);
                            }else {
                                for (int i=0;i<ids.size();i++){
                                    if (leaderboard_id.equalsIgnoreCase(ids.get(i))){
                                        selectedSeriesTitle=names.get(i);
                                        spinType.setSelection(i);
                                        seriesMasterTypeId=ids.get(i);
                                        winningSeriesTree=type.getData().get(i).getWinning_tree();
                                        distributeAmt=type.getData().get(i).getDistribute_amount();
                                        txtTotalPool.setText(CustomUtil.displayAmountFloat(getApplicationContext(),distributeAmt));
                                    }
                                }
                            }
                            spinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    selectedSeriesTitle=names.get(position);
                                    seriesMasterTypeId=ids.get(position);
                                    winning_dec=winDesc.get(position);
                                    winningSeriesTree=type.getData().get(position).getWinning_tree();
                                    distributeAmt=type.getData().get(position).getDistribute_amount();
                                    distributeSeries=type.getData().get(position).getDistribute_amount();

                                    list.clear();
                                    adapter.notifyDataSetChanged();
                                    isGetData = false;
                                    offset=0;

                                    getList();

                                    if (!TextUtils.isEmpty(winningSeriesTree)){
                                        try {
                                            layNoDataPrise.setVisibility(View.GONE);
                                            recyclerPrisePool.setVisibility(View.VISIBLE);
                                            txtPtizeLbl.setVisibility(View.VISIBLE);
                                            txtTotalPool.setVisibility(View.VISIBLE);

                                            jsonArray=new JSONArray(winningSeriesTree);
                                            matchRank = new MatchRankAdapter(mContext, jsonArray);
                                            recyclerPrisePool.setAdapter(matchRank);
                                            txtTotalPool.setText(CustomUtil.displayAmountFloat(getApplicationContext(),distributeAmt));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }else {
                                        layNoDataPrise.setVisibility(View.VISIBLE);
                                        recyclerPrisePool.setVisibility(View.GONE);

                                        txtPtizeLbl.setVisibility(View.GONE);
                                        txtTotalPool.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }else {
                            layNoData.setVisibility(View.VISIBLE);
                            swipe.setVisibility(View.GONE);
                        }

                    } else {
                        CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
            param.put("lb_master_type_id",seriesMasterTypeId);
            param.put("user_id",preferences.getUserModel().getId());
            param.put("offset",offset);
            param.put("limit",limit);

            boolean isProgress=true;
            if (swipe.isRefreshing()){
                isProgress=false;
            }
            //isApiCall = false;

            LogUtil.e("TAG", "param: " + param.toString() );
            HttpRestClient.postJSON(mContext, isProgress, ApiManager.userLeaderboardlist,param, new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {
                    swipe.setRefreshing(false);
                    LogUtil.e("TAG", "onSuccessResult: " + responseBody.toString() );
                    UserLeaderboardList lists = gson.fromJson(responseBody.toString(), UserLeaderboardList.class);

                    if(lists.getStatus()){
                        //isApiCall = true;

                        list.addAll(lists.getData());
                        adapter.notifyDataSetChanged();
                        adapter.updateWin(winning_dec);

                        if (list.size() < limit) {
                            isGetData = false;
                            offset = 0;
                        } else {
                            isGetData = true;
                            offset += limit;
                        }

                        if (list.size()>0){
                            layNoData.setVisibility(View.GONE);
                            swipe.setVisibility(View.VISIBLE);
                        }else {
                            layNoData.setVisibility(View.VISIBLE);
                            swipe.setVisibility(View.GONE);
                        }
                    }
                    else{
                        layNoData.setVisibility(View.VISIBLE);
                        swipe.setVisibility(View.GONE);
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
        }else if (v.getId()==R.id.txtTabLeader){
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