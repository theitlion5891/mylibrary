package com.fantafeat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fantafeat.Adapter.LeaderBoardListAdapter;
import com.fantafeat.Adapter.MatchRankAdapter;
import com.fantafeat.Adapter.PromotorLeaderAdapter;
import com.fantafeat.Adapter.PromotorPoolAdapter;
import com.fantafeat.Model.PromotorsUserModel;
import com.fantafeat.Model.UserLeaderBoardType;
import com.fantafeat.Model.UserLeaderboardList;
import com.fantafeat.Model.UserLeaderboardSubType;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PromotorLeaderboardActivity extends BaseActivity implements View.OnClickListener {

    private TextView txtTabPrise,txtTabLeader,txtTotalPool,txtPtizeLbl;
    private View viewPrise,viewLeader;
    private LinearLayout layLeader,layPrise,layNoData,layNoDataPrise,laySubType,layData;
    private SwipeRefreshLayout swipe;
    private Spinner spinType;
    private ImageView toolbar_back,imgWinGadgets;
    private RecyclerView recyclerList,recyclerPrisePool;
    private String seriesMasterTypeId="",selectedSeriesTitle="",selectedSeriesId="",lType="";
    private String winningSeriesTree="";
    private String distributeSeries="";
    private String distributeAmt="";
    private String winningTree="";
    private String winning_dec="",leaderboard_id="";
    private int offset=0,limit=50;
    private ArrayList<PromotorsUserModel> list;
    private PromotorLeaderAdapter adapter;
    private PromotorPoolAdapter matchRank;
    private boolean isGetData, isApiCall;
    private JSONArray jsonArray;

    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        setContentView(R.layout.activity_promotor_leaderboard);
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
        laySubType=findViewById(R.id.laySubType);
        layData=findViewById(R.id.layData);
        imgWinGadgets=findViewById(R.id.imgWinGadgets);

        txtTabPrise.setOnClickListener(this);
        txtTabLeader.setOnClickListener(this);
        toolbar_back.setOnClickListener(this);

        LinearLayoutManager manager=new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        recyclerList.setLayoutManager(manager);
        adapter=new PromotorLeaderAdapter(mContext,list, position -> {
            if (MyApp.getClickStatus()){
              /*  selectedSeriesId=seriesMasterTypeId;
                startActivity(new Intent(mContext, UserDetailActivity.class)
                        .putExtra("leaderboard_item",list.get(position))
                        .putExtra("selectedSeriesTitle",selectedSeriesTitle)
                        .putExtra("selectedSeriesId",selectedSeriesId)
                );*/
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

        getType();
    }

    private void getType() {

        HttpRestClient.postJSON(this, true, ApiManager.infLeaderboardList, new JSONObject(), new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                try {
                    LogUtil.e("TAG", "getType: " + responseBody.toString() );
                    //UserLeaderBoardType type = gson.fromJson(responseBody.toString(), UserLeaderBoardType.class);
                    if(responseBody.optBoolean("status")){

                        List<String> names=new ArrayList<>();
                        List<String> ids=new ArrayList<>();
                        List<String> type=new ArrayList<>();

                        JSONArray data=responseBody.optJSONArray("data");
                        int selPos=0;
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj=data.optJSONObject(i);
                            if (obj.optString("is_default").equalsIgnoreCase("yes")){
                                selPos=i;
                                lType=obj.optString("is_usr_cm");
                            }
                            names.add(obj.optString("type"));
                            ids.add(obj.optString("ID"));
                            type.add(obj.optString("is_usr_cm"));
                        }

                        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(mContext, R.layout.spinner_text,names);
                        spinType.setAdapter(adapter1);

                        spinType.setSelection(selPos);
                        spinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                selectedSeriesTitle=names.get(position);
                                seriesMasterTypeId=ids.get(position);
                                lType=type.get(position);
                                getSeries();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    } else {
                        CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void getSeries() {
        JSONObject param=new JSONObject();
        try {

            param.put("inf_lb_master_id",seriesMasterTypeId);
        }catch (Exception e){
            e.printStackTrace();
        }
        LogUtil.e("TAG", "getSeries param: " + param.toString() );
        HttpRestClient.postJSON(this, true, ApiManager.infLeaderboardType, param, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                try {
                    LogUtil.e("TAG", "getSeries: " + responseBody.toString() );

                    UserLeaderboardSubType type = gson.fromJson(responseBody.toString(), UserLeaderboardSubType.class);
                    if(type.getStatus()){

                        if (type.getData().size()>0) {
                            displayType(type);
                        }else {
                            laySubType.removeAllViews();
                            layNoData.setVisibility(View.VISIBLE);
                            layData.setVisibility(View.GONE);
                            //btnLeaderboard.setVisibility(View.GONE);
                        }
                    }else{
                        CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                    }

                    winningSeriesTree=type.getData().get(0).getWinningTree();
                    distributeAmt=type.getData().get(0).getDistributeAmount();
                    if (!TextUtils.isEmpty(distributeAmt) && !TextUtils.isEmpty(type.getData().get(0).getAsset_url())){
                        txtTotalPool.setVisibility(View.VISIBLE);
                        txtTotalPool.setText(CustomUtil.displayAmount(getApplicationContext(),distributeAmt)+" / ");
                        imgWinGadgets.setVisibility(View.VISIBLE);
                        CustomUtil.loadImageWithGlide(mContext,imgWinGadgets,ApiManager.PROFILE_IMG,type.getData().get(0).getAsset_url(),R.drawable.ic_team1_placeholder);
                    }else if (!TextUtils.isEmpty(distributeAmt) && TextUtils.isEmpty(type.getData().get(0).getAsset_url())){
                        txtTotalPool.setVisibility(View.VISIBLE);
                        txtTotalPool.setText(CustomUtil.displayAmount(getApplicationContext(),distributeAmt));
                        imgWinGadgets.setVisibility(View.GONE);
                    }
                    else if (TextUtils.isEmpty(distributeAmt) && !TextUtils.isEmpty(type.getData().get(0).getAsset_url())){
                        txtTotalPool.setVisibility(View.GONE);
                        CustomUtil.loadImageWithGlide(mContext,imgWinGadgets,ApiManager.PROFILE_IMG,type.getData().get(0).getAsset_url(),R.drawable.ic_team1_placeholder);
                        imgWinGadgets.setVisibility(View.VISIBLE);
                    }
                   /* else {
                        txtTotalPool.setText(CustomUtil.displayAmount(getApplicationContext(),distributeAmt));
                        imgWinGadgets.setVisibility(View.GONE);
                    }*/


                    if (!TextUtils.isEmpty(winningSeriesTree)){
                        layNoDataPrise.setVisibility(View.GONE);
                        recyclerPrisePool.setVisibility(View.VISIBLE);
                        txtPtizeLbl.setVisibility(View.VISIBLE);
                        //txtTotalPool.setVisibility(View.VISIBLE);

                        jsonArray=new JSONArray(winningSeriesTree);
                        matchRank = new PromotorPoolAdapter(mContext, jsonArray);
                        recyclerPrisePool.setAdapter(matchRank);

                    }
                    else {
                        layNoDataPrise.setVisibility(View.VISIBLE);
                        recyclerPrisePool.setVisibility(View.GONE);
                        txtPtizeLbl.setVisibility(View.GONE);
                        txtTotalPool.setVisibility(View.GONE);
                    }

                   /* UserLeaderBoardType type = gson.fromJson(responseBody.toString(), UserLeaderBoardType.class);
                    if(type.getStatus()){

*/

                        /*List<String> names=new ArrayList<>();
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
                            txtTotalPool.setText(CustomUtil.displayAmount(getApplicationContext(),distributeAmt)+" / ");

                            if (!TextUtils.isEmpty(winningSeriesTree)){
                                layNoDataPrise.setVisibility(View.GONE);
                                recyclerPrisePool.setVisibility(View.VISIBLE);
                                txtPtizeLbl.setVisibility(View.VISIBLE);
                                txtTotalPool.setVisibility(View.VISIBLE);

                                jsonArray=new JSONArray(winningSeriesTree);
                                matchRank = new PromotorPoolAdapter(mContext, jsonArray);
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
                                        txtTotalPool.setText(CustomUtil.displayAmount(getApplicationContext(),distributeAmt)+" / ");
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
                                            matchRank = new PromotorPoolAdapter(mContext, jsonArray);
                                            recyclerPrisePool.setAdapter(matchRank);
                                            txtTotalPool.setText(CustomUtil.displayAmount(getApplicationContext(),distributeAmt)+" / ");
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
                        }*/

                   /* } else {
                        CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void displayType(UserLeaderboardSubType type) {
        try {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            laySubType.removeAllViews();
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

                if (k==0){
                    txtTitle.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                    // txtDate.setTextColor(mContext.getResources().getColor(R.color.white));
                    view.setBackgroundResource(R.drawable.bg_light_blue);
                    isApiCall = true;
                    isGetData = false;
                    list.clear();
                    adapter.notifyDataSetChanged();
                    seriesMasterTypeId=item.getId();
                    winningTree=item.getWinningTree();
                    winningSeriesTree=item.getWinningTree();
                    distributeAmt=item.getDistributeAmount();
                    winning_dec=item.getWinningDec();

                    getList();

                    if (!TextUtils.isEmpty(distributeAmt) && !TextUtils.isEmpty(type.getData().get(0).getAsset_url())){
                        txtTotalPool.setVisibility(View.VISIBLE);
                        txtTotalPool.setText(CustomUtil.displayAmount(getApplicationContext(),distributeAmt)+" / ");
                        imgWinGadgets.setVisibility(View.VISIBLE);
                        CustomUtil.loadImageWithGlide(mContext,imgWinGadgets,ApiManager.PROFILE_IMG,type.getData().get(0).getAsset_url(),R.drawable.ic_team1_placeholder);
                    }else if (!TextUtils.isEmpty(distributeAmt) && TextUtils.isEmpty(type.getData().get(0).getAsset_url())){
                        txtTotalPool.setVisibility(View.VISIBLE);
                        txtTotalPool.setText(CustomUtil.displayAmount(getApplicationContext(),distributeAmt));
                        imgWinGadgets.setVisibility(View.GONE);
                    }
                    else if (TextUtils.isEmpty(distributeAmt) && !TextUtils.isEmpty(type.getData().get(0).getAsset_url())){
                        txtTotalPool.setVisibility(View.GONE);
                        CustomUtil.loadImageWithGlide(mContext,imgWinGadgets,ApiManager.PROFILE_IMG,type.getData().get(0).getAsset_url(),R.drawable.ic_team1_placeholder);
                        imgWinGadgets.setVisibility(View.VISIBLE);
                    }
                    /*else {
                        txtTotalPool.setText(CustomUtil.displayAmount(getApplicationContext(),distributeAmt));
                        imgWinGadgets.setVisibility(View.GONE);
                    }*/

                    if (!TextUtils.isEmpty(winningSeriesTree)){
                        layNoDataPrise.setVisibility(View.GONE);
                        recyclerPrisePool.setVisibility(View.VISIBLE);
                        txtPtizeLbl.setVisibility(View.VISIBLE);
                        //txtTotalPool.setVisibility(View.VISIBLE);

                        jsonArray=new JSONArray(winningSeriesTree);
                        matchRank = new PromotorPoolAdapter(mContext, jsonArray);
                        recyclerPrisePool.setAdapter(matchRank);

                    }
                    else {
                        layNoDataPrise.setVisibility(View.VISIBLE);
                        recyclerPrisePool.setVisibility(View.GONE);
                        txtPtizeLbl.setVisibility(View.GONE);
                       // txtTotalPool.setVisibility(View.GONE);
                    }
                }

                laySubType.addView(view);

                view.setOnClickListener(v->{
                    try {
                        for (int i=0;i<laySubType.getChildCount();i++){
                            View otherView=laySubType.getChildAt(i);
                            TextView tit=otherView.findViewById(R.id.txtTitle);
                            //TextView dt=otherView.findViewById(R.id.txtDate);
                            tit.setTextColor(mContext.getResources().getColor(R.color.black_pure));
                            //dt.setTextColor(mContext.getResources().getColor(R.color.black));
                            otherView.setBackgroundResource(R.drawable.btn_border);
                        }
                        TextView tit=v.findViewById(R.id.txtTitle);
                        //TextView dt=v.findViewById(R.id.txtDate);

                        tit.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                        //dt.setTextColor(mContext.getResources().getColor(R.color.white));

                        view.setBackgroundResource(R.drawable.bg_light_blue);

                        isApiCall = true;
                        isGetData = false;

                        list.clear();
                        adapter.notifyDataSetChanged();
                        seriesMasterTypeId=item.getId();
                        winningTree=item.getWinningTree();
                        winningSeriesTree=item.getWinningTree();
                        distributeAmt=item.getDistributeAmount();
                        winning_dec=item.getWinningDec();

                        offset=0;limit=50;
                        recyclerList.scrollToPosition(0);
                        getList();

                        if (!TextUtils.isEmpty(distributeAmt) && !TextUtils.isEmpty(type.getData().get(0).getAsset_url())){
                            txtTotalPool.setVisibility(View.VISIBLE);
                            txtTotalPool.setText(CustomUtil.displayAmount(getApplicationContext(),distributeAmt)+" / ");
                            imgWinGadgets.setVisibility(View.VISIBLE);
                            CustomUtil.loadImageWithGlide(mContext,imgWinGadgets,ApiManager.PROFILE_IMG,type.getData().get(0).getAsset_url(),R.drawable.ic_team1_placeholder);
                        }else if (!TextUtils.isEmpty(distributeAmt) && TextUtils.isEmpty(type.getData().get(0).getAsset_url())){
                            txtTotalPool.setVisibility(View.VISIBLE);
                            txtTotalPool.setText(CustomUtil.displayAmount(getApplicationContext(),distributeAmt));
                            imgWinGadgets.setVisibility(View.GONE);
                        }
                        else if (TextUtils.isEmpty(distributeAmt) && !TextUtils.isEmpty(type.getData().get(0).getAsset_url())){
                            txtTotalPool.setVisibility(View.GONE);
                            CustomUtil.loadImageWithGlide(mContext,imgWinGadgets,ApiManager.PROFILE_IMG,type.getData().get(0).getAsset_url(),R.drawable.ic_team1_placeholder);
                            imgWinGadgets.setVisibility(View.VISIBLE);
                        }
                       /* else {
                            txtTotalPool.setText(CustomUtil.displayAmount(getApplicationContext(),distributeAmt));
                            imgWinGadgets.setVisibility(View.GONE);
                        }*/

                        if (!TextUtils.isEmpty(winningSeriesTree)){
                            layNoDataPrise.setVisibility(View.GONE);
                            recyclerPrisePool.setVisibility(View.VISIBLE);
                            txtPtizeLbl.setVisibility(View.VISIBLE);
                            //txtTotalPool.setVisibility(View.VISIBLE);

                            jsonArray=new JSONArray(winningSeriesTree);
                            matchRank = new PromotorPoolAdapter(mContext, jsonArray);
                            recyclerPrisePool.setAdapter(matchRank);

                        }
                        else {
                            layNoDataPrise.setVisibility(View.VISIBLE);
                            recyclerPrisePool.setVisibility(View.GONE);
                            txtPtizeLbl.setVisibility(View.GONE);
                            //txtTotalPool.setVisibility(View.GONE);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }


                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getList() {

        try {
            JSONObject param=new JSONObject();
            param.put("inf_lb_master_type_id",seriesMasterTypeId);
            param.put("user_id",preferences.getUserModel().getId());
            param.put("offset",offset);
            param.put("limit",limit);

            boolean isProgress=true;
            if (swipe.isRefreshing()){
                isProgress=false;
            }
            //isApiCall = false;

            LogUtil.e("TAG", "param: " + param.toString() );
            HttpRestClient.postJSON(mContext, isProgress, ApiManager.infLeaderboardListDtls,param, new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {
                    swipe.setRefreshing(false);
                    LogUtil.e("TAG", "onSuccessResult: " + responseBody.toString() );
                   // UserLeaderboardList lists = gson.fromJson(responseBody.toString(), UserLeaderboardList.class);


                    if(responseBody.optBoolean("status")){
                        //isApiCall = true;
                        JSONArray data=responseBody.optJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj=data.optJSONObject(i);
                            PromotorsUserModel promotorsUserModel=gson.fromJson(obj.toString(),PromotorsUserModel.class);
                            promotorsUserModel.setlType(lType);
                            promotorsUserModel.setWinning_dec(winning_dec);
                            list.add(promotorsUserModel);
                        }

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

            txtTabLeader.setTypeface(null, Typeface.NORMAL);
            txtTabPrise.setTypeface(null, Typeface.BOLD);
            viewPrise.setBackgroundResource(R.color.colorPrimary);
            viewLeader.setBackgroundResource(R.color.white_pure);
            layLeader.setVisibility(View.GONE);
            layPrise.setVisibility(View.VISIBLE);
        }else if (v.getId()==R.id.txtTabLeader){
            txtTabPrise.setTypeface(null, Typeface.NORMAL);
            txtTabLeader.setTypeface(null, Typeface.BOLD);

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