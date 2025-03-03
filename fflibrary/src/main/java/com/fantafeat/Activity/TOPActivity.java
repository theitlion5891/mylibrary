package com.fantafeat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fantafeat.Adapter.EventFilterAdapter;
import com.fantafeat.Adapter.OpinionAdapter;
import com.fantafeat.Adapter.PollAdapter;
import com.fantafeat.Adapter.StocksAdapter;
import com.fantafeat.Adapter.TradeBoardAdapter;
import com.fantafeat.Adapter.TradingListAdapter;
import com.fantafeat.Fragment.ClosedEventFragment;
import com.fantafeat.Fragment.LiveEventFragment;
import com.fantafeat.Model.AvailableQtyModel;
import com.fantafeat.Model.EventIntroModel;
import com.fantafeat.Model.EventModel;
import com.fantafeat.Model.OpinionFilterTags;
import com.fantafeat.Model.ScoreModel;
import com.fantafeat.Model.StocksModel;
import com.fantafeat.Model.TradeBoard;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CenterLayoutManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.MusicManager;
import com.fantafeat.util.PrefConstant;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class TOPActivity extends BaseActivity implements View.OnClickListener  {

    private LinearLayout layPlay,layEvents,/*layFilter1,layPFilter1,layFilter2,layPFilter2,*/layTrading,layOpinion,layPlayDetail,layEventDetail,nodata,layPoll,layTabs,
            layBowl,layMatchDetail,layLiveScore,layLiveBg,layStocks;
    private ImageView imgPlay,imgMute;
    private CardView layScore;
    private ImageView imgEvent;
    private TextView txtPlay,txtEvent,btnTrading,btnOpinion,btnPoll,txtTitle,
            team1_name,team2_name,team1_inn1_score,team1_inn2_score,team2_inn1_score,
            team2_inn2_score,txt_match_commentry,txt_batsman1,txt_batsman1_score,txt_batsman2,txt_batsman2_score,txtBowlerName,txtBowlerDesc,btnTradeX,btnStocks;
    private TabLayout tabEvent;
    private ViewPager2 pagerEvent;
    private SwipeRefreshLayout swipe;
    private RecyclerView recyclerOpinion;
    private RecyclerView recyclerPoll;
    private RecyclerView recyclerFilter;
    private RecyclerView recyclerStocks;
    private ArrayList<OpinionFilterTags> filterList;
    private ArrayList<EventModel> list;
    private ArrayList<EventModel> opinionArrayList;
    private ArrayList<EventModel> pollArrayList;
    private String selectedTag="1",selectedOpinionTime=""; // 1=Trades, 2=Opinion, 3=poll
    /*private float  use_deposit = 0;
    private float  use_transfer =  0;
    private float  use_winning =  0;*/
    private boolean isEventTabLoad=false,isEventTab=false,isEmit=false;
    private Fragment currentFragment;
    private LiveEventFragment liveEventFragment;
    private ClosedEventFragment closedEventFragment;
    private EventFilterAdapter filterAdapter;
    private TradingListAdapter tradingAdapter;
    private TradeBoardAdapter tradeBoardAdapter;
    private OpinionAdapter opinionAdapter;
    private PollAdapter pollAdapter;

    private String contest_id="",conType="",selectedFilterId="";
    private EventModel selectedEventModel,confirmDialogModel,dialogEventModel;

    private TextView[] dots;

   // private TextToSpeech t1;
    private boolean isSpeech=true;
    RecyclerView recyclerTrading;

   // private Socket mSocket= null;
    private OpinionFilterTags.OtherData selectedFilterOtherModel=null;

    private StocksAdapter adapter;

    private long diff;
    private boolean isFirst=false;
    private ArrayList<EventModel> confirmTradeList;
    private TextView txtConfirmContestTime,txtConfirnConTrades,txtConfirmOption1,txtConfirmOption2,join_confirm_contest_btn,txtTrades,
            txtMaxQtyAtPrice,txtQtyAtPrice,txtContestTime;
    private LinearLayout layoutTeam1,layoutTeam2;
    private SeekBar seekConfirmQty;
    private BottomSheetDialog confirmDialog,dialog;
    private int confirmQty=0;
    private float confirmPrise=0f;
    private ArrayList<AvailableQtyModel> yesList=new ArrayList<>();
    private ArrayList<AvailableQtyModel> noList=new ArrayList<>();
    private ArrayList<AvailableQtyModel> qtyList=new ArrayList<>();
    private String tradingDialogId="";
    private  CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topactivity);

       /* try {
            IO.Options opts =new IO.Options();
            //opts.query = "auth_token=$authToken"
            mSocket = IO.socket("http://52.66.235.207:2020", opts);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }*/

       // mSocket=MyApp.getInstance().getSocketInstance();

      /*  t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });*/

        filterList=new ArrayList<>();


        initData();

       /* if (!preferences.getPrefBoolean(PrefConstant.is_trade_intro)){
            showIntroDialog();
        }*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        //getTradingData();
        LogUtil.e(TAG,"SelectTaG=>"+selectedTag);
        /*if (mSocket!=null){
            if (TextUtils.isEmpty(conType) || conType.equalsIgnoreCase(ConstantUtil.LINK_PREFIX_TRADING)){
                if (!mSocket.connected())
                    mSocket.connect();
                try {
                    //if (!isEmit) {
                        //isEmit=true;
                        JSONObject obj = new JSONObject();
                        obj.put("en", "tradingData");
                        mSocket.emit("req", obj);
                        listener();
                    //}
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }*/
    }
    @Override
    public void initClick() {
       /* btnTradeX.setOnClickListener(v -> {
            selectedTag="1";
            btnTradeX.setBackgroundResource(R.drawable.primary_fill_border);
            btnTradeX.setTextColor(getResources().getColor(R.color.white));
           *//* btnDuo.setBackgroundResource(R.drawable.transparent_view);
            btnDuo.setTextColor(getResources().getColor(R.color.black));*//*
            btnStocks.setBackgroundResource(R.drawable.transparent_view);
            btnStocks.setTextColor(getResources().getColor(R.color.black));

            layPlayDetail.setVisibility(View.VISIBLE);
            layStocks.setVisibility(View.GONE);
            //getTradingData();
        });*/

       /* btnStocks.setOnClickListener(v -> {
            selectedTag="4";
            btnTradeX.setBackgroundResource(R.drawable.transparent_view);
            btnTradeX.setTextColor(getResources().getColor(R.color.black));

           *//* btnDuo.setBackgroundResource(R.drawable.transparent_view);
            btnDuo.setTextColor(getResources().getColor(R.color.black));*//*
            btnStocks.setBackgroundResource(R.drawable.primary_fill_border);
            btnStocks.setTextColor(getResources().getColor(R.color.white));

            layPlayDetail.setVisibility(View.GONE);
            layStocks.setVisibility(View.VISIBLE);
            getStocksData();
        });*/
    }
    private void initData() {
        layPlay=findViewById(R.id.layPlay);
        layPlay.setOnClickListener(this);
        layEvents=findViewById(R.id.layEvents);
        layEvents.setOnClickListener(this);
        ImageView back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);
        imgPlay=findViewById(R.id.imgPlay);
        imgEvent=findViewById(R.id.imgEvent);
        txtEvent=findViewById(R.id.txtEvent);
        txtPlay=findViewById(R.id.txtPlay);
        recyclerFilter=findViewById(R.id.recyclerFilter);
        recyclerStocks=findViewById(R.id.recyclerStocks);
        layTabs=findViewById(R.id.layTabs);
        txtTitle=findViewById(R.id.txtTitle);
        // layFilter1=findViewById(R.id.layFilter1);
        //layFilter2=findViewById(R.id.layFilter2);

        // layPFilter1=findViewById(R.id.layPFilter1);
        //layPFilter2=findViewById(R.id.layPFilter2);

        recyclerTrading = findViewById(R.id.recyclerTrading);
        btnOpinion=findViewById(R.id.btnOpinion);
        btnOpinion.setOnClickListener(this);
        btnTrading=findViewById(R.id.btnTrading);
        btnTrading.setOnClickListener(this);
        btnPoll=findViewById(R.id.btnPoll);
        btnPoll.setOnClickListener(this);
        layTrading=findViewById(R.id.layTrading);
        layOpinion=findViewById(R.id.layOpinion);
        recyclerOpinion=findViewById(R.id.recyclerOpinion);
        recyclerPoll=findViewById(R.id.recyclerPoll);
        layPlayDetail=findViewById(R.id.layPlayDetail);
        layEventDetail=findViewById(R.id.layEventDetail);
        tabEvent=findViewById(R.id.tabEvent);
        pagerEvent=findViewById(R.id.pagerEvent);
        nodata=findViewById(R.id.nodata);
        swipe=findViewById(R.id.swipe);
        layPoll=findViewById(R.id.layPoll);
        ImageView imgInfo = findViewById(R.id.imgInfo);
        imgInfo.setOnClickListener(this);

        layMatchDetail=findViewById(R.id.layMatchDetail);
        layLiveScore=findViewById(R.id.layLiveScore);
        layStocks=findViewById(R.id.layStocks);
        team1_name=findViewById(R.id.team1_name);
        team2_name=findViewById(R.id.team2_name);
        team1_inn1_score=findViewById(R.id.team1_inn1_score);
        team1_inn2_score=findViewById(R.id.team1_inn2_score);
        team2_inn1_score=findViewById(R.id.team2_inn1_score);
        team2_inn2_score=findViewById(R.id.team2_inn2_score);
        txt_match_commentry=findViewById(R.id.txt_match_commentry);
        txt_match_commentry.setSelected(true);
        txt_batsman1=findViewById(R.id.txt_batsman1);
        txt_batsman1_score=findViewById(R.id.txt_batsman1_score);
        txt_batsman2=findViewById(R.id.txt_batsman2);
        txt_batsman2_score=findViewById(R.id.txt_batsman2_score);
        txtBowlerName=findViewById(R.id.txtBowlerName);
        txtBowlerDesc=findViewById(R.id.txtBowlerDesc);
        layBowl=findViewById(R.id.layBowl);
        layScore=findViewById(R.id.layScore);
        layLiveBg=findViewById(R.id.layLiveBg);
        imgMute=findViewById(R.id.imgMute);

        btnTradeX=findViewById(R.id.btnTradeX);
        btnTradeX.setOnClickListener(this);
        btnStocks=findViewById(R.id.btnStocks);
        btnStocks.setOnClickListener(this);

        if (getIntent().hasExtra("contest_id")){
            contest_id=getIntent().getStringExtra("contest_id");
        }
        if (getIntent().hasExtra("conType")){
            conType=getIntent().getStringExtra("conType");
        }

        list=new ArrayList<>();
        filterList=new ArrayList<>();

        //StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerFilter.setLayoutManager(new CenterLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        filterAdapter=new EventFilterAdapter(getApplicationContext(), filterList, (model,position) -> {
            recyclerFilter.smoothScrollToPosition(position);
            filterRecyclerList(model);
        });
        recyclerFilter.setAdapter(filterAdapter);

        recyclerOpinion.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        recyclerPoll.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        recyclerTrading.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        tradingAdapter=new TradingListAdapter(list, mContext, item -> {
            if (item.getType()==1){
                getOpinionCnt(item);
            }else {
                showConfirmTradeBoardDialog(item);
            }
        });
        recyclerTrading.setAdapter(tradingAdapter);

        if (TextUtils.isEmpty(conType)){
            getTradingData();
            LogUtil.e(TAG,"TextUTilEmpty=>"+selectedTag);
        }else {
            if (conType.equalsIgnoreCase(ConstantUtil.LINK_PREFIX_TRADING)){
                btnTrading.performClick();
            }else if (conType.equalsIgnoreCase(ConstantUtil.LINK_PREFIX_OPINION)){
                btnOpinion.performClick();
            }else if (conType.equalsIgnoreCase(ConstantUtil.LINK_PREFIX_POLL)){
                btnPoll.performClick();
            }
        }

        swipe.setOnRefreshListener(()->{
            //if (mSocket!=null && !mSocket.connected()){
            if (!isEventTab){
                recyclerPoll.getRecycledViewPool().clear();
                tradingAdapter.notifyDataSetChanged();
                //swipe.setEnabled(false);
                if (selectedTag.equalsIgnoreCase("1")){
                    getTradingData();
                    LogUtil.e(TAG,"swipe=>"+selectedTag);
                } else if (selectedTag.equalsIgnoreCase("2")){
                    getOpinionData();
                }else if (selectedTag.equalsIgnoreCase("3")){
                    getPollData();
                }
                /*else if (selectedTag.equalsIgnoreCase("4")) {
                    getStocksData();
                }*/
            }
//            }else {
//                swipe.setRefreshing(false);
//            }

        });

        imgMute.setOnClickListener(v -> {
            isSpeech=!isSpeech;
            if (isSpeech){
                imgMute.setImageResource(R.drawable.ic_volume_white);
            }else {
                stopSpeech();
                imgMute.setImageResource(R.drawable.ic_mute_white);
            }
        });

        if (ConstantUtil.isTop){
            layTabs.setVisibility(View.VISIBLE);
            txtTitle.setVisibility(View.GONE);
        }else {
            layTabs.setVisibility(View.GONE);
            txtTitle.setVisibility(View.VISIBLE);
        }

    }

    private void initEventTabs() {
        isEventTabLoad=true;
        ArrayList<String> mFragmentTitleList=new ArrayList<>();

        mFragmentTitleList.add("Live Events");
        mFragmentTitleList.add("Closed Events");

        final EventAdapter adapter = new EventAdapter(this,mContext);
        pagerEvent.setAdapter(adapter);

        new TabLayoutMediator(tabEvent, pagerEvent,
                (tab, position) -> {
                    tab.setText(mFragmentTitleList.get(position));
                    pagerEvent.setCurrentItem(tab.getPosition(),false);
                }).attach();

        pagerEvent.setUserInputEnabled(false);
        pagerEvent.setOffscreenPageLimit(2);

        tabEvent.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition()==0){
                    currentFragment=liveEventFragment;
                    ((LiveEventFragment) currentFragment).changeTags(selectedTag);
                }else if (tab.getPosition()==1){
                    currentFragment=closedEventFragment;
                    ((ClosedEventFragment) currentFragment).changeTags(selectedTag);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void getOpinionData() {
        opinionArrayList=new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        boolean isLoader= swipe == null || !swipe.isRefreshing();
        HttpRestClient.postJSONNormal(mContext, isLoader, ApiManager.openionContestList, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (swipe!=null && swipe.isRefreshing())
                    swipe.setRefreshing(false);

                LogUtil.e(TAG, "getPredictionData : " + responseBody.toString());

                if (responseBody.optBoolean("status")) {
                    JSONArray data=responseBody.optJSONArray("data");
                    JSONArray tags=responseBody.optJSONArray("tags");

                    if (data!=null && data.length()>0){
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj=data.optJSONObject(i);
                            EventModel model=gson.fromJson(obj.toString(),EventModel.class);
                            if (!TextUtils.isEmpty(contest_id)){
                                if (model.getId().equalsIgnoreCase(contest_id)){
                                    selectedEventModel=model;
                                }
                            }
                            opinionArrayList.add(model);
                        }

                    }
                    filterList.clear();
                    ArrayList<EventModel> listOp=new ArrayList<>();
                    for (int i = 0; i < tags.length(); i++) {
                        JSONObject object=tags.optJSONObject(i);
                        OpinionFilterTags model=gson.fromJson(object.toString(),OpinionFilterTags.class);

                        ArrayList<EventModel> listOpinion=new ArrayList<>();

                        if (model.getId().equalsIgnoreCase("0")){
                            listOpinion.addAll(opinionArrayList);
                            //model.setSelected(true);
                            //filterRecyclerList(model,i);
                        }
                        else {
                            for (int j = 0; j < opinionArrayList.size(); j++) {
                                if (TextUtils.isEmpty(opinionArrayList.get(j).getFilter_tags_name())){
                                    opinionArrayList.get(j).setFilter_tags_name("0");
                                }
                                ArrayList<String> tagList = new ArrayList<>(Arrays.asList(opinionArrayList.get(j).getFilter_tags_name().split(",")));
                                if (tagList.contains(model.getId().trim())) {
                                    listOpinion.add(opinionArrayList.get(j));
                                }
                            }
                        }
                        model.setListOpinion(listOpinion);
                        filterList.add(model);
                    }
                    filterAdapter=new EventFilterAdapter(getApplicationContext(), filterList, (model,position) -> {
                        filterRecyclerList(model);
                    });
                    recyclerFilter.setAdapter(filterAdapter);

                    for (int i = 0; i < filterList.size(); i++) {
                        OpinionFilterTags model=filterList.get(i);
                        if (model.getId().equalsIgnoreCase(selectedFilterId)){
                            model.setSelected(true);
                            listOp.addAll(model.getListOpinion());
                        }else {
                            model.setSelected(false);
                        }
                    }

                    opinionAdapter=new OpinionAdapter(listOp,mContext,(model -> {
                        showConfirmOpinionContestDialog(model);
                    }));
                    recyclerOpinion.setAdapter(opinionAdapter);
                }

                if (selectedEventModel!=null){
                    showConfirmOpinionContestDialog(selectedEventModel);
                }

                if (opinionArrayList.size()<=0){
                    nodata.setVisibility(View.VISIBLE);
                    layOpinion.setVisibility(View.GONE);
                }else {
                    nodata.setVisibility(View.GONE);
                    layOpinion.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                if (swipe!=null && swipe.isRefreshing())
                    swipe.setRefreshing(false);
            }
        });
    }

    private void getTradingData() {
        list.clear();
        confirmTradeList=new ArrayList<>();
        filterList.clear();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e("resp","Param: "+jsonObject+"\n"+ApiManager.TRADING_CONTEST_LIST);
        boolean isLoader= swipe == null || !swipe.isRefreshing();

        HttpRestClient.postJSONNormal(mContext, isLoader, ApiManager.TRADING_CONTEST_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (swipe!=null && swipe.isRefreshing()) {
                    swipe.setRefreshing(false);
                    swipe.setEnabled(true);
                }
                LogUtil.e(TAG, "getOpinionData : " + responseBody.toString());

                list.clear();
                filterList.clear();
                confirmTradeList.clear();
                //LogUtil.e(TAG, "getOpinionData : " + responseBody);
                if (responseBody.optBoolean("status")) {
                    JSONArray data=responseBody.optJSONArray("data");
                    JSONArray tags=responseBody.optJSONArray("tags");
                    JSONArray tradeBoard=responseBody.optJSONArray("trade_board");
                    //LogUtil.e(TAG, "getOpinionData data : " + data);
                    //LogUtil.e(TAG, "getOpinionData data : " + data);
                    //LogUtil.e(TAG, "getOpinionData tags : " + tags);
                    LogUtil.e(TAG, "getOpinionData trade_board : " + tradeBoard);

                    if (tradeBoard!=null){
                        for (int i = 0; i < tradeBoard.length(); i++) {
                            JSONObject obj=tradeBoard.optJSONObject(i);
                            EventModel model=gson.fromJson(obj.toString(),EventModel.class);
                            model.setType(0);
                            if (TextUtils.isEmpty(model.getTotal_join_cnt()))
                                model.setTotal_join_cnt("0");
                            confirmTradeList.add(model);
                        }
                    }

                    setTradingData(data,tags,isEmit);
                }
                else {
                    if (list.size()<=0){
                        nodata.setVisibility(View.VISIBLE);
                        layTrading.setVisibility(View.GONE);
                    }else {
                        nodata.setVisibility(View.GONE);
                        layTrading.setVisibility(View.VISIBLE);
                    }
                }
                /*if (responseBody.optBoolean("status")) {
                    JSONArray data=responseBody.optJSONArray("data");
                    JSONArray tags=responseBody.optJSONArray("tags");

                    if (data!=null && data.length()>0){
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj=data.optJSONObject(i);
                            EventModel model=gson.fromJson(obj.toString(),EventModel.class);
                            if (!TextUtils.isEmpty(contest_id)){
                                if (model.getId().equalsIgnoreCase(contest_id)){
                                    selectedEventModel=model;
                                }
                            }
                            list.add(model);
                        }

                    }

                    filterList.clear();
                    ArrayList<EventModel> listTrading=new ArrayList<>();

                    for (int i = 0; i < tags.length(); i++) {
                        JSONObject object=tags.optJSONObject(i);
                        OpinionFilterTags model=gson.fromJson(object.toString(),OpinionFilterTags.class);

                        ArrayList<EventModel> listOpinion=new ArrayList<>();

                        if (model.getId().equalsIgnoreCase("0")){
                            listOpinion.addAll(list);
                            // model.setSelected(true);
                            //filterRecyclerList(model,i);
                        }else {

                            for (int j = 0; j < list.size(); j++) {
                                if (TextUtils.isEmpty(list.get(j).getFilter_tags_name())){
                                    list.get(j).setFilter_tags_name("0");
                                }

                                ArrayList<String> tagList = new ArrayList<>(Arrays.asList(list.get(j).getFilter_tags_name().split(",")));
                                if (tagList.contains(model.getId().trim())) {
                                    listOpinion.add(list.get(j));
                                }
                            }
                        }
                        model.setListOpinion(listOpinion);
                        filterList.add(model);
                    }

                    filterAdapter=new EventFilterAdapter(getApplicationContext(), filterList, (model,position) -> {
                        filterRecyclerList(model);
                    });
                    recyclerFilter.setAdapter(filterAdapter);

                    for (int i = 0; i < filterList.size(); i++) {
                        OpinionFilterTags model=filterList.get(i);
                        if (model.getId().equalsIgnoreCase(selectedFilterId)){
                            model.setSelected(true);
                            listTrading.addAll(model.getListOpinion());
                        }else {
                            model.setSelected(false);
                        }
                    }

                    tradingAdapter.updateData(listTrading);


                    if (selectedEventModel!=null){
                        if (TextUtils.isEmpty(selectedEventModel.getOptionValue())) {
                            selectedEventModel.setOptionValue("Yes");
                            selectedEventModel.setOptionEntryFee(selectedEventModel.getOption1Price());
                        }

                        getOpinionCnt(selectedEventModel);
                    }

                }
                if (list.size()<=0){
                    nodata.setVisibility(View.VISIBLE);
                    layTrading.setVisibility(View.GONE);
                }else {
                    nodata.setVisibility(View.GONE);
                    layTrading.setVisibility(View.VISIBLE);
                }*/
                //setPFilterData();
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                if (swipe!=null && swipe.isRefreshing()) {
                    swipe.setRefreshing(false);
                    swipe.setEnabled(true);
                }
            }
        });
    }

    private void getPollData() {
        pollArrayList=new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        boolean isLoader= swipe == null || !swipe.isRefreshing();
        HttpRestClient.postJSONNormal(mContext, isLoader, ApiManager.pollContestList, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (swipe!=null && swipe.isRefreshing())
                    swipe.setRefreshing(false);

                LogUtil.e(TAG, "getPollData : " + responseBody.toString());

                if (responseBody.optBoolean("status")) {
                    JSONArray data=responseBody.optJSONArray("data");
                    JSONArray tags=responseBody.optJSONArray("tags");

                    if (data!=null && data.length()>0){
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj=data.optJSONObject(i);
                            EventModel model=gson.fromJson(obj.toString(),EventModel.class);
                            if (!TextUtils.isEmpty(contest_id)){
                                if (model.getId().equalsIgnoreCase(contest_id)){
                                    selectedEventModel=model;
                                }
                            }
                            pollArrayList.add(model);
                        }
                    }
                    filterList.clear();
                    ArrayList<EventModel> listPoll=new ArrayList<>();

                    for (int i = 0; i < tags.length(); i++) {
                        JSONObject object=tags.optJSONObject(i);
                        OpinionFilterTags model=gson.fromJson(object.toString(),OpinionFilterTags.class);

                        ArrayList<EventModel> listOpinion=new ArrayList<>();

                        if (model.getId().equalsIgnoreCase("0")){
                            listOpinion.addAll(pollArrayList);
                        }
                        else {
                            for (int j = 0; j < pollArrayList.size(); j++) {
                                if (TextUtils.isEmpty(pollArrayList.get(j).getFilter_tags_name())){
                                    pollArrayList.get(j).setFilter_tags_name("0");
                                }
                                ArrayList<String> tagList = new ArrayList<>(Arrays.asList(pollArrayList.get(j).getFilter_tags_name().split(",")));
                                if (tagList.contains(model.getId().trim())) {
                                    listOpinion.add(pollArrayList.get(j));
                                }
                            }
                        }
                        model.setListOpinion(listOpinion);
                        filterList.add(model);
                    }

                    for (int i = 0; i < filterList.size(); i++) {
                        OpinionFilterTags model=filterList.get(i);
                        if (model.getId().equalsIgnoreCase(selectedFilterId)){
                            model.setSelected(true);
                            listPoll.addAll(model.getListOpinion());
                        }else {
                            model.setSelected(false);
                        }
                    }
                    filterAdapter=new EventFilterAdapter(getApplicationContext(), filterList, (model,position) -> {
                        filterRecyclerList(model);
                    });
                    recyclerFilter.setAdapter(filterAdapter);

                    pollAdapter=new PollAdapter(listPoll,mContext,(model -> {
                        showConfirmPollContestDialog(model);
                    }));
                    recyclerPoll.setAdapter(pollAdapter);

                    if (selectedEventModel!=null){
                        selectedEventModel.setOptionValue(selectedEventModel.getOption1());
                        //selectedEventModel.setOptionEntryFee(selectedEventModel.getOption1Price());
                        showConfirmPollContestDialog(selectedEventModel);
                    }
                }

                if (pollArrayList.size()<=0){
                    nodata.setVisibility(View.VISIBLE);
                    layPoll.setVisibility(View.GONE);
                }else {
                    nodata.setVisibility(View.GONE);
                    layPoll.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                if (swipe!=null && swipe.isRefreshing())
                    swipe.setRefreshing(false);
            }
        });
    }

    private void getStocksData(){
        StocksModel[] stockList = new StocksModel[]{
            new StocksModel("Virat Kohli"),
            new StocksModel("Rohit Sharma")
        };

        adapter = new StocksAdapter(mContext,stockList);
        recyclerStocks.setHasFixedSize(true);
        recyclerStocks.setLayoutManager(new LinearLayoutManager(this));
        recyclerStocks.setAdapter(adapter);

    }

    private void setTradingData(JSONArray data,JSONArray tags,boolean isEmit){
        list.clear();
        if (data!=null && data.length()>0){
            for (int i = 0; i < data.length(); i++) {
                JSONObject obj=data.optJSONObject(i);
                EventModel model=gson.fromJson(obj.toString(),EventModel.class);

                if (!TextUtils.isEmpty(contest_id) && model.getId().equalsIgnoreCase(contest_id)){
                    selectedEventModel=model;
                }
                list.add(model);

            }
        }

        filterList.clear();
        ArrayList<EventModel> listTrading=new ArrayList<>();

        if (TextUtils.isEmpty(selectedFilterId) && tags.length()>0){
            JSONObject object=tags.optJSONObject(0);
            OpinionFilterTags model=gson.fromJson(object.toString(),OpinionFilterTags.class);
            model.setSelected(true);
            selectedFilterId=model.getId();
        }

        for (int i = 0; i < tags.length(); i++) {
            JSONObject object=tags.optJSONObject(i);
            OpinionFilterTags model=gson.fromJson(object.toString(),OpinionFilterTags.class);

            ArrayList<EventModel> listOpinion=new ArrayList<>();

            if (TextUtils.isEmpty(selectedFilterId) && !isFirst) {
                isFirst = true;
                model.setSelected(true);
                selectedFilterId=model.getId();
            }

            for (int j = 0; j < list.size(); j++) {
                if (TextUtils.isEmpty(list.get(j).getFilter_tags_name())){
                    list.get(j).setFilter_tags_name("0");
                }

                ArrayList<String> tagList = new ArrayList<>(Arrays.asList(list.get(j).getFilter_tags_name().split(",")));
                if (tagList.contains(model.getId().trim())) {
                    list.get(j).setType(1);
                    listOpinion.add(list.get(j));
                }
            }
            /*if (model.getId().equalsIgnoreCase("0")){
                listOpinion.addAll(list);
            }else {

            }*/
            model.setListOpinion(listOpinion);
            filterList.add(model);
        }

      /*  ArrayList<EventModel> listTradeBoard=new ArrayList<>();
       // LogUtil.e("lenthtradeboard",tradeBoard.length());
        for (int i =0;i<tradeBoard.length();i++){
            JSONObject object = tradeBoard.optJSONObject(i);
            EventModel model=gson.fromJson(object.toString(),EventModel.class);
            listTradeBoard.add(model);
        }*/

        filterAdapter.notifyDataSetChanged();

        /*StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerFilter.setLayoutManager(gridLayoutManager);
        filterAdapter=new EventFilterAdapter(getApplicationContext(), filterList, (model,position) -> {
            filterRecyclerList(model,isEmit);
        });
        recyclerFilter.setAdapter(filterAdapter);*/

        selectedFilterOtherModel=null;
        for (int i = 0; i < filterList.size(); i++) {
            OpinionFilterTags model=filterList.get(i);

            if (model.getId().equalsIgnoreCase(selectedFilterId)){
                model.setSelected(true);

                listTrading.addAll(model.getListOpinion());

                if (selectedTag.equalsIgnoreCase("1")){
                    if (model.getOther_data()!=null && !TextUtils.isEmpty(model.getOther_data().getId()) &&
                            model.getOther_data().getMatchStatus().equalsIgnoreCase("playing")){
                        selectedFilterOtherModel=model.getOther_data();
                        if (!TextUtils.isEmpty(selectedFilterOtherModel.getIs_full_scorecard()) &&
                                selectedFilterOtherModel.getIs_full_scorecard().equalsIgnoreCase("yes")){
                            initSocket(isEmit);
                        } else {
                           /* if (mSocket!=null)
                                mSocket.off("res");*/
                            layLiveScore.setVisibility(View.GONE);
                        }
                        setLiveData(selectedFilterOtherModel);
                    }else {
                        /*if (mSocket!=null)
                            mSocket.off("res");*/
                        layScore.setVisibility(View.GONE);
                        //layMatchDetail.setVisibility(View.GONE);
                    }

                }
                else {
                    /*if (mSocket!=null)
                        mSocket.off("res");*/
                    layScore.setVisibility(View.GONE);
                    //layMatchDetail.setVisibility(View.GONE);
                }
                /*if (selectedTag.equalsIgnoreCase("1") && !TextUtils.isEmpty(model.getOther_data().getId()) &&
                        model.getOther_data().getMatchStatus().equalsIgnoreCase("playing")) {
                    if (isEmit) {
                        selectedFilterOtherModel=model.getOther_data();
                        initSocket(isEmit);
                    }
                }
                else {
                    if (mSocket!=null)
                        mSocket.off("res");
                    layLiveScore.setVisibility(View.GONE);
                }*/
            }else {
                model.setSelected(false);
            }
        }

  /*      EventModel eventModel = new EventModel();
        eventModel.setConfirmTradeList(listTradeBoard);
        eventModel.setType(0);
        listTrading.add(0,eventModel);*/

        if (confirmTradeList!=null && confirmTradeList.size()>0){
            EventModel model=new EventModel();
            model.setType(0);
            model.setConDesc("Confirm Trade");
            model.setConfirmTradeList(confirmTradeList);
            listTrading.add(0,model);
        }

        //LogUtil.e("resp","confirmTradeList: "+listTrading.size());

        if (listTrading.size()>0){
            nodata.setVisibility(View.GONE);
            layTrading.setVisibility(View.VISIBLE);
        }else {
            nodata.setVisibility(View.VISIBLE);
            layTrading.setVisibility(View.GONE);
        }

        tradingAdapter.updateData(listTrading);

        if (selectedEventModel!=null){
            if (TextUtils.isEmpty(selectedEventModel.getOptionValue())) {
                selectedEventModel.setOptionValue("Yes");
                selectedEventModel.setOptionEntryFee(selectedEventModel.getOption1Price());
            }
            getOpinionCnt(selectedEventModel);
        }

      /*  if (list.size()<=0){
            nodata.setVisibility(View.VISIBLE);
            layTrading.setVisibility(View.GONE);
        }
        else {
            nodata.setVisibility(View.GONE);
            layTrading.setVisibility(View.VISIBLE);
        }*/
    }

    private void initSocket(boolean isEmit){
        /*if (mSocket!=null &&  isEmit){

            try {
                JSONObject obj=new JSONObject();
                JSONObject data=new JSONObject();
                obj.put("en","GetScoreCard");
                data.put("match_id",selectedFilterOtherModel.getUniqueId());//eventModel.getMatchId()
                //selectedMatchId=selectedFilterOtherModel.getUniqueId();
                obj.put("data",data);
                Log.e("resp","LiveScore Emit: "+obj);
                //mSocket.emit("req",obj);
            }catch (JSONException e){
                e.printStackTrace();
            }
            listener();
        }*/
    }

    private void setLiveData(OpinionFilterTags.OtherData selectedFilterOtherModel) {

        if (selectedFilterOtherModel!=null){
            layScore.setVisibility(View.VISIBLE);
            layMatchDetail.setVisibility(View.VISIBLE);
            if (selectedFilterOtherModel.getMatchType().equalsIgnoreCase("Test")){
                team1_inn2_score.setVisibility(View.VISIBLE);
                team2_inn2_score.setVisibility(View.VISIBLE);

                team1_inn2_score.setText(selectedFilterOtherModel.getTeam1Inn2Score());
                team2_inn2_score.setText(selectedFilterOtherModel.getTeam2Inn2Score());

            }
            else {
                team1_inn2_score.setVisibility(View.GONE);
                team2_inn2_score.setVisibility(View.GONE);
            }

            team1_name.setText(selectedFilterOtherModel.getTeam1Name());
            team2_name.setText(selectedFilterOtherModel.getTeam2Name());
            team1_inn1_score.setText(selectedFilterOtherModel.getTeam1Inn1Score());
            team2_inn1_score.setText(selectedFilterOtherModel.getTeam2Inn1Score());
        }else {
            layMatchDetail.setVisibility(View.GONE);
        }
       // checkBg();
    }

    private void listener(){
        /*if (mSocket!=null){
            mSocket.off("res");
            mSocket.on("res", args -> {
                // Log.e("resp","LiveScore: "+args[0]);
                if (args!=null){
                    //Log.e("resp","LiveScore: "+args[0]);

                    try {
                        JSONObject object=new JSONObject(args[0].toString());
                        LogUtil.e("resp",object.optString("en"));
                        if (object.optString("en").equalsIgnoreCase("tradingData") && !isEventTab){

                            LogUtil.e("resp",object);

                            JSONArray data=object.optJSONArray("data");
                            JSONArray tags=object.optJSONArray("tags");
                            JSONArray trade_board=object.optJSONArray("trade_board");

                            //LogUtil.e(TAG, "LiveScore data : " + data);
                            // LogUtil.e(TAG, "LiveScore tags : " + tags);

                            if (data!=null && tags!=null){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (trade_board!=null){
                                            confirmTradeList.clear();
                                            for (int i = 0; i < trade_board.length(); i++) {
                                                JSONObject obj = trade_board.optJSONObject(i);
                                                EventModel model = gson.fromJson(obj.toString(), EventModel.class);
                                                model.setType(0);
                                                if (TextUtils.isEmpty(model.getTotal_join_cnt()))
                                                    model.setTotal_join_cnt("0");
                                                if (confirmDialog != null && confirmDialog.isShowing()){
                                                    if (confirmDialogModel != null && confirmDialogModel.getId().equalsIgnoreCase(model.getId())) {
                                                        if (!CustomUtil.isEventEnd(model.getConEndTime())){
                                                            if (confirmDialogModel.getOption1().equalsIgnoreCase(confirmDialogModel.getOptionValue())){
                                                                model.setOptionValue(confirmDialogModel.getOption1());
                                                                model.setOptionPrice(confirmDialogModel.getOption1Price());
                                                            }else {
                                                                model.setOptionValue(confirmDialogModel.getOption2());
                                                                model.setOptionPrice(confirmDialogModel.getOption2Price());
                                                            }
                                                            model.setCon_join_qty(confirmQty+"");

                                                            confirmDialogModel=model;

                                                            if (model.getCon_status().equalsIgnoreCase("hold")){
                                                                //join_confirm_contest_btn.setEnabled(false);
                                                                join_confirm_contest_btn.setBackgroundResource(R.drawable.red_bg_radius);
                                                                join_confirm_contest_btn.setText("Please wait for a while");
                                                            }else {
                                                                //join_confirm_contest_btn.setEnabled(true);
                                                                join_confirm_contest_btn.setBackgroundResource(R.drawable.btn_green);
                                                                join_confirm_contest_btn.setText("Confirm");
                                                            }
                                                            txtConfirmOption1.setText(model.getOption1Price());
                                                            txtConfirmOption2.setText(model.getOption2Price());

                                                           // txtConfirmContestTime.setText("Ends on "+CustomUtil.dateConvertWithFormat(model.getConEndTime(),"dd-MMM-yy hh:mm a"));
                                                            txtConfirnConTrades.setText(CustomUtil.coolFormat(mContext,model.getTotalTrades())+" People are already joined");
                                                            //seekConfirmQty.setProgress(confirmQty);
                                                            if (model.getOption1().equalsIgnoreCase(model.getOptionValue())){
                                                                layoutTeam1.performClick();
                                                            }
                                                            else {
                                                                layoutTeam2.performClick();
                                                            }
                                                        }else {
                                                            join_confirm_contest_btn.setBackgroundResource(R.drawable.red_bg_radius);
                                                            join_confirm_contest_btn.setText("Event is closed");
                                                            model.setCon_status("hold");
                                                        }
                                                    }
                                                }
                                                confirmTradeList.add(model);
                                            }
                                        }
                                        setTradingData(data,tags,false);
                                    }
                                });
                            }
                        }
                        else if (object.optString("en").equalsIgnoreCase("trade_unmatch_count")){
                            //Log.e("resp","trade_unmatch_count: "+args[0]);
                            if (!TextUtils.isEmpty(tradingDialogId) && dialogEventModel!=null && dialog!=null && dialog.isShowing()){
                                JSONArray data=object.optJSONArray("data");
                                String rs=mContext.getResources().getString(R.string.rs);
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject obj=data.optJSONObject(i);
                                    String contest_id=obj.optString("contest_id");
                                    if (contest_id.equalsIgnoreCase(tradingDialogId)){
                                        JSONArray yesCount=obj.optJSONArray("yesCount");
                                        JSONArray noCount=obj.optJSONArray("noCount");
                                        yesList.clear();
                                        noList.clear();
                                        for (int j = 0; j < yesCount.length(); j++) {
                                            JSONObject yesObj=yesCount.optJSONObject(j);
                                            AvailableQtyModel model1=gson.fromJson(yesObj.toString(),AvailableQtyModel.class);
                                            yesList.add(model1);
                                        }

                                        for (int j = 0; j < noCount.length(); j++) {
                                            JSONObject noObj=noCount.optJSONObject(j);
                                            AvailableQtyModel model1=gson.fromJson(noObj.toString(),AvailableQtyModel.class);
                                            noList.add(model1);
                                        }

                                        if (dialogEventModel.getOptionValue().equalsIgnoreCase("yes")){
                                            if (txtMaxQtyAtPrice!=null){
                                                if (yesList.size()>0){
                                                    txtMaxQtyAtPrice.setText(yesList.get(0).getTotalJoinCnt()+" quantities available at "+
                                                            rs+String.format("%.2f",CustomUtil.convertFloat(yesList.get(0).getJnAmount())));
                                                }else {
                                                    txtMaxQtyAtPrice.setText("0 quantities available at "+rs+"1");
                                                }
                                            }

                                        }
                                        else {
                                            if (txtMaxQtyAtPrice!=null) {
                                                if (noList.size()>0){
                                                    txtMaxQtyAtPrice.setText(noList.get(0).getTotalJoinCnt()+" quantities available at "+
                                                            rs+String.format("%.2f",CustomUtil.convertFloat(noList.get(0).getJnAmount())));
                                                }else {
                                                    txtMaxQtyAtPrice.setText("0 quantities available at "+rs+"1");
                                                }
                                            }
                                        }
                                        if (txtQtyAtPrice!=null)
                                            setOpinionValue(dialogEventModel.getOptionValue(),dialogEventModel.getSelectedPrice()
                                                ,obj,txtQtyAtPrice);
                                        *//*Collections.sort(yesList, new prizePoolUp());
                                        Collections.sort(noList, new prizePoolUp());*//*

                                       // qtyList.clear();
                                      *//*  if (dialogEventModel.getOptionValue().equalsIgnoreCase("yes")){
                                            qtyList.addAll(yesList);
                                        }else {
                                            qtyList.addAll(noList);
                                        }*//*

                                        runOnUiThread(() -> {
                                            *//*if (qtyList.size()>0){
                                                txtNoQty.setVisibility(View.GONE);
                                                recyclerQty.setVisibility(View.VISIBLE);
                                            }else {
                                                txtNoQty.setVisibility(View.VISIBLE);
                                                recyclerQty.setVisibility(View.GONE);
                                            }
                                            if (adapter!=null && recyclerQty!=null){
                                                adapter.updateList(qtyList,dialogEventModel.getOptionValue(),recyclerQty);
                                            }*//*
                                            if (txtTrades!=null)
                                                txtTrades.setText(CustomUtil.coolFormat(mContext,obj.optJSONObject("contest_data").optString("total_trades"))+" Trades");

                                            if (tradingAdapter!=null){
                                                ArrayList<EventModel> arrayList=tradingAdapter.getList();
                                                for (int j = 0; j < arrayList.size(); j++) {
                                                    EventModel model=arrayList.get(j);
                                                    if (model.getType()==1 || model.getType()==2){
                                                        if (model.getId().equalsIgnoreCase(contest_id)){
                                                            model.setTotalTrades(obj.optJSONObject("contest_data").optString("total_trades"));
                                                            tradingAdapter.updateData(arrayList);
                                                        }
                                                    }

                                                }
                                            }
                                        });

                                    }
                                }
                            }
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (selectedFilterOtherModel!=null && selectedFilterOtherModel.getMatchStatus().equalsIgnoreCase("playing")){
                                        if (selectedTag.equalsIgnoreCase("1") && !isEventTab &&
                                                object.has("data") && object.optJSONObject("data")!=null &&
                                                selectedFilterOtherModel.getUniqueId().equalsIgnoreCase(object.optString("match_id"))
                                        ) {
                                            layLiveScore.setVisibility(View.VISIBLE);
                                            displayScorCardData(object.optJSONObject("data"));
                                        }
                                    }else {
                                       *//* if (mSocket!=null)
                                            mSocket.off("res");*//*
                                        layLiveScore.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }*/
    }

    private void displayScorCardData(JSONObject data) {
        try {
            runOnUiThread(() -> {

                JSONArray innings=data.optJSONArray("innings");

                if (innings.length()>0){
                    JSONObject currentInning=innings.optJSONObject(0);

                    //LogUtil.e("resp",currentInning.toString());

                    if (selectedFilterOtherModel!=null){
                        if (currentInning.optString("number").equalsIgnoreCase("1") ||
                                currentInning.optString("number").equalsIgnoreCase("2")){
                            if (selectedFilterOtherModel.getMatchTeam1Win().equalsIgnoreCase(currentInning.optString("batting_team_id"))){
                                team1_inn1_score.setText(currentInning.optString("scores_full"));
                            }else if (selectedFilterOtherModel.getMatchTeam2Win().equalsIgnoreCase(currentInning.optString("batting_team_id"))){
                                team2_inn1_score.setText(currentInning.optString("scores_full"));
                            }
                        }else if (currentInning.optString("number").equalsIgnoreCase("3") ||
                                currentInning.optString("number").equalsIgnoreCase("4")){
                            if (selectedFilterOtherModel.getMatchTeam1Win().equalsIgnoreCase(currentInning.optString("batting_team_id"))){
                                team1_inn2_score.setText(currentInning.optString("scores_full"));
                            }else if (selectedFilterOtherModel.getMatchTeam2Win().equalsIgnoreCase(currentInning.optString("batting_team_id"))){
                                team2_inn2_score.setText(currentInning.optString("scores_full"));
                            }
                        }
                    }

                    JSONArray bowlers=currentInning.optJSONArray("bowlers");
                    JSONArray batsmen=currentInning.optJSONArray("batsmen");
                    ArrayList<ScoreModel.Batsman> batsmanArrayList=new ArrayList<>();
                    ArrayList<ScoreModel.Bowler> bowlerArrayList=new ArrayList<>();

                    for (int i = 0; i < batsmen.length(); i++) {
                        JSONObject bat=batsmen.optJSONObject(i);
                        if (bat.optString("how_out").equalsIgnoreCase("Not out")){
                            ScoreModel.Batsman batsman=gson.fromJson(bat.toString(),ScoreModel.Batsman.class);
                            batsmanArrayList.add(batsman);
                        }
                    }

                    if (batsmanArrayList.size()>0){
                        if (batsmanArrayList.get(0).getPosition().equalsIgnoreCase("striker"))
                            txt_batsman1.setText(batsmanArrayList.get(0).getName()+" *");
                        else
                            txt_batsman1.setText(batsmanArrayList.get(0).getName());

                        txt_batsman1_score.setText(batsmanArrayList.get(0).getRuns()+" ("+batsmanArrayList.get(0).getBallsFaced()+")");

                        if (batsmanArrayList.size()>1){
                            if (batsmanArrayList.get(1).getPosition().equalsIgnoreCase("striker"))
                                txt_batsman2.setText(batsmanArrayList.get(1).getName()+" *");
                            else
                                txt_batsman2.setText(batsmanArrayList.get(1).getName());
                            txt_batsman2_score.setText(batsmanArrayList.get(1).getRuns()+" ("+batsmanArrayList.get(1).getBallsFaced()+")");
                        }else {
                            txt_batsman1_score.setText("-");
                            txt_batsman2_score.setText("-");
                        }
                    }else {
                        txt_batsman1.setText("-");
                        txt_batsman2.setText("-");
                        txt_batsman1_score.setText("-");
                        txt_batsman2_score.setText("-");
                    }

                    for (int i = 0; i < bowlers.length(); i++) {
                        JSONObject bowl=bowlers.optJSONObject(i);
                        if (bowl.optString("bowling").equalsIgnoreCase("true")){
                            ScoreModel.Bowler bowler=gson.fromJson(bowl.toString(),ScoreModel.Bowler.class);
                            bowlerArrayList.add(bowler);
                        }
                    }

                    if (bowlerArrayList.size()>0){
                        txtBowlerName.setText(bowlerArrayList.get(0).getName());
                        txtBowlerDesc.setText(bowlerArrayList.get(0).getOvers()+"-"+bowlerArrayList.get(0).getMaidens()+"-"+
                                bowlerArrayList.get(0).getRunsConceded()+"-"+bowlerArrayList.get(0).getWickets());
                    }else {
                        txtBowlerName.setText("-");
                        txtBowlerDesc.setText("-");
                    }

                    // LogUtil.e("resp","commentaries: "+data.optJSONArray("commentaries"));

                    Type type=new TypeToken<ArrayList<ScoreModel.Commentary>>() {}.getType();;
                    List<ScoreModel.Commentary> commentaries=gson.fromJson(data.optJSONArray("commentaries").toString(),type);
                    Collections.reverse(commentaries);
                    if (commentaries.size()>0 && !TextUtils.isEmpty(commentaries.get(0).getCommentary())){
                        txt_match_commentry.setVisibility(View.VISIBLE);
                        String comentry=commentaries.get(commentaries.size()-1).getCommentary();
                        if (!TextUtils.isEmpty(comentry)){
                            txt_match_commentry.setText(comentry);
                            /*if (isSpeech)
                                t1.speak(comentry, TextToSpeech.QUEUE_FLUSH, null);
                            else
                                stopSpeech();*/
                        }/*else
                            stopSpeech();*/

                    }else {
                        txt_match_commentry.setVisibility(View.GONE);
                    }
                    displayBowl(commentaries);

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onPause(){
        stopSpeech();
        super.onPause();
    }

    private void stopSpeech(){
       /* if(t1 !=null){
            t1.stop();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       /* if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }*/
        /*if (mSocket!=null){
            mSocket.off("res");
            //mSocket.disconnect();
        }*/
    }

    private void displayBowl( List<ScoreModel.Commentary> bowlCntModel) {
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(50,50);
        params.setMargins(5,0,5,5);

        layBowl.removeAllViews();

        for (int i=0;i<bowlCntModel.size();i++){

            ScoreModel.Commentary ball = bowlCntModel.get(i);

            TextView tv=new TextView(mContext);
            tv.setLayoutParams(params);

            tv.setText(ball.getScore());

            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(mContext.getResources().getColor(R.color.white_pure));
            tv.setTextSize(10f);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setBackgroundResource(ConstantUtil.getColorCode(ball.getScore()));
           // tv.setBackgroundResource(R.drawable.white_fill_circul);

            layBowl.addView(tv);

            //if (bowlCntModel.size()<=)
        }
    }

    private void filterRecyclerList(OpinionFilterTags model) {
        stopSpeech();
        ArrayList<EventModel> arrayList=new ArrayList<>();

        selectedFilterId=model.getId();

        /*if (mSocket!=null)
            mSocket.off("res");*/
        layLiveScore.setVisibility(View.GONE);
        selectedFilterOtherModel=null;
        for (int i = 0; i < filterList.size(); i++) {
            //filterList.get(i).setSelected(false);
            if (filterList.get(i)!=null && filterList.get(i).isSelected()){
                arrayList.addAll(filterList.get(i).getListOpinion());
                if (selectedTag.equalsIgnoreCase("1")){
                    if (model.getOther_data()!=null &&
                            !TextUtils.isEmpty(model.getOther_data().getId()) &&
                            model.getOther_data().getMatchStatus().equalsIgnoreCase("playing")){
                        selectedFilterOtherModel=model.getOther_data();
                        if (!TextUtils.isEmpty(selectedFilterOtherModel.getIs_full_scorecard()) &&
                                selectedFilterOtherModel.getIs_full_scorecard().equalsIgnoreCase("yes")){
                            initSocket(true);
                        } else {
                           /* if (mSocket!=null)
                                mSocket.off("res");*/
                            layLiveScore.setVisibility(View.GONE);
                        }
                        setLiveData(selectedFilterOtherModel);
                    }else {
                        /*if (mSocket!=null)
                            mSocket.off("res");*/
                        layScore.setVisibility(View.GONE);
                        //layMatchDetail.setVisibility(View.GONE);
                    }

                } else {
                    /*if (mSocket!=null)
                        mSocket.off("res");*/
                    layScore.setVisibility(View.GONE);
                    //layMatchDetail.setVisibility(View.GONE);
                }
            }
        }
        //model.setSelected(true);
        filterAdapter.notifyDataSetChanged();

        //recyclerFilter.smoothScrollToPosition(position);

        /*if (model.getId().equalsIgnoreCase("0")){
            arrayList.addAll(list);
        }else {
            for (int i = 0; i < list.size(); i++) {
                OpinionModel opinionModel = list.get(i);
                ArrayList<String> tagList = new ArrayList<>(Arrays.asList(opinionModel.getFilter_tags_name().split(",")));
                if (tagList.contains(model.getId().trim())) {
                    arrayList.add(opinionModel);
                }
            }
        }*/

        if (selectedTag.equalsIgnoreCase("1")) {

            if (confirmTradeList != null && confirmTradeList.size() > 0) {
                EventModel model1 = new EventModel();
                model1.setType(0);
                model1.setConDesc("Confirm Trade");
                model1.setConfirmTradeList(confirmTradeList);
                arrayList.add(0, model1);
            }

            tradingAdapter.updateData(arrayList);
        }
        else if (selectedTag.equalsIgnoreCase("2"))
            opinionAdapter.updateData(arrayList);
        else if (selectedTag.equalsIgnoreCase("3"))
            pollAdapter.updateData(arrayList);

    }

    private void filterRecyclerList(OpinionFilterTags model,int position) {

        ArrayList<EventModel> arrayList=new ArrayList<>();

        selectedFilterId=model.getId();

        for (int i = 0; i < filterList.size(); i++) {
            //filterList.get(i).setSelected(false);
            if (filterList.get(i).isSelected()){
                arrayList.addAll(filterList.get(i).getListOpinion());
            }
        }
        //model.setSelected(true);
        filterAdapter.notifyDataSetChanged();
        //recyclerFilter.smoothScrollToPosition(position);

        /*if (model.getId().equalsIgnoreCase("0")){
            arrayList.addAll(list);
        }else {
            for (int i = 0; i < list.size(); i++) {
                OpinionModel opinionModel = list.get(i);
                ArrayList<String> tagList = new ArrayList<>(Arrays.asList(opinionModel.getFilter_tags_name().split(",")));
                if (tagList.contains(model.getId().trim())) {
                    arrayList.add(opinionModel);
                }
            }
        }*/

        if (selectedTag.equalsIgnoreCase("1"))
            tradingAdapter.updateData(arrayList);
        else if (selectedTag.equalsIgnoreCase("2"))
            opinionAdapter.updateData(arrayList);
        else if (selectedTag.equalsIgnoreCase("3"))
            pollAdapter.updateData(arrayList);

    }

    private void getOpinionCnt(EventModel model){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("contest_id", model.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "getOpinionCnt : " + jsonObject);
        HttpRestClient.postJSONNormal(mContext, true, ApiManager.tradesContestListDetailsCount, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e(TAG, "getOpinionCnt : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONObject data=responseBody.optJSONObject("data");
                    showConfirmContestDialog(model,data);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                //preferences.setUserModel(new UserModel());
            }
        });

    }

    private void showConfirmOpinionContestDialog(EventModel model) {

        selectedEventModel=null;
        contest_id="";
        conType="";

        //use_deposit=use_transfer=use_winning=0;

        final float[] entryFee = {CustomUtil.convertFloat(model.getEntryFee())};
        final float deposit = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        final float winning = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
        final float transfer_bal = CustomUtil.convertFloat(preferences.getUserModel().getTransferBal());

        /*if ((entryFee[0] - deposit) < 0) {
            use_deposit = entryFee[0];
        }
        else if ((entryFee[0] - (deposit + transfer_bal)) < 0) {
            use_deposit = deposit;
            use_transfer = entryFee[0] - deposit;
        }
        else {
            use_deposit = deposit;
            use_transfer = transfer_bal;
            use_winning = entryFee[0] - (deposit + transfer_bal);
        }*/

        float calBalance=deposit + transfer_bal + winning;

        if (calBalance < entryFee[0]) {
            CustomUtil.showToast(mContext,"Insufficient Balance");
            double amt=Math.ceil(entryFee[0] -calBalance);
            if (amt<1){
                amt=amt+1;
            }
            int intamt=(int) amt;
            if (intamt<amt){
                intamt+=1;
            }
            String payableAmt=String.valueOf(intamt);
            startActivity(new Intent(mContext,AddDepositActivity.class)
                    .putExtra("depositAmt",payableAmt));
        }
        else {
            DecimalFormat format = CustomUtil.getFormater("00.00");
            String rs=mContext.getResources().getString(R.string.rs);

            UserModel userModel=preferences.getUserModel();
            float totalWalletBalance=CustomUtil.convertFloat(userModel.getDepositBal())+
                    CustomUtil.convertFloat(userModel.getWinBal())+CustomUtil.convertFloat(userModel.getTransferBal());

            Dialog dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.opinion_confirm_dialog);

            TextView txtEndDate=dialog.findViewById(R.id.txtEndDate);
            TextView txtTrades=dialog.findViewById(R.id.txtTrades);
            TextView txtConTitle=dialog.findViewById(R.id.txtConTitle);
            TextView txtDesc=dialog.findViewById(R.id.txtDesc);
            TextView join_contest_btn=dialog.findViewById(R.id.join_contest_btn);

            TextView txtIntegerEntry=dialog.findViewById(R.id.txtIntegerEntry);
            EditText edtInteger=dialog.findViewById(R.id.edtInteger);

            LinearLayout layInteger=dialog.findViewById(R.id.layInteger);

            //TextView txtInvest=dialog.findViewById(R.id.txtInvest);
            TextView txtTimeEntry=dialog.findViewById(R.id.txtTimeEntry);
            EditText edtTime=dialog.findViewById(R.id.edtTime);
            LinearLayout layTime=dialog.findViewById(R.id.layTime);

            txtEndDate.setText("Ends on "+CustomUtil.dateConvertWithFormat(model.getConEndTime(),"MMM dd hh:mm aa"));
            txtConTitle.setText(model.getConDesc());
            txtDesc.setText(model.getConSubDesc());
            txtTrades.setText(model.getTotalTrades()+" Trades");

            if (model.getCon_type().equalsIgnoreCase("Time")){
                layTime.setVisibility(View.VISIBLE);
                layInteger.setVisibility(View.GONE);

                txtTimeEntry.setText(rs+model.getEntryFee());

                edtTime.setOnClickListener(view -> {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    int mHour = c.get(Calendar.HOUR_OF_DAY);
                    int mMinute = c.get(Calendar.MINUTE);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                            (view12, year, monthOfYear, dayOfMonth) -> {
                                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                                        (view1, hourOfDay, minute) -> {

                                            Calendar cal = Calendar.getInstance();
                                            cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                            cal.set(Calendar.MINUTE,minute);

                                            Format formatter,submitFormat;
                                            formatter = new SimpleDateFormat("h:mm a");
                                            edtTime.setText( year+ "-" + (monthOfYear + 1) + "-" +dayOfMonth+" "+formatter.format(cal.getTime()));

                                            submitFormat=new SimpleDateFormat("HH:mm");
                                            selectedOpinionTime=year+ "-" + (monthOfYear + 1) + "-" +dayOfMonth+" "+submitFormat.format(cal.getTime())+":00";

                                        }, mHour, mMinute, false);
                                timePickerDialog.show();

                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();

                });

                /*txtSubmitTime.setOnClickListener(view -> {
                    String value=edtTime.getText().toString().trim();
                    if (TextUtils.isEmpty(value)){
                        CustomUtil.showTopSneakWarning(mContext,"Enter Date and Time");
                        return;
                    }
                    model.setOptionValue(value);

                });*/

            }
            else if (model.getCon_type().equalsIgnoreCase("Integer")){
                layTime.setVisibility(View.GONE);
                layInteger.setVisibility(View.VISIBLE);

                txtIntegerEntry.setText(rs+model.getEntryFee());

               /* txtSubmitInteger.setOnClickListener(view -> {
                    String value=edtInteger.getText().toString().trim();
                    if (TextUtils.isEmpty(value)){
                        CustomUtil.showTopSneakWarning(context,"Enter Value");
                        return;
                    }
                    model.setOptionValue(value);
                    listener.onItemClick(model);
                });*/

            }

            if (totalWalletBalance<(entryFee[0])){
                join_contest_btn.setBackgroundResource(R.drawable.red_bg_radius);
                join_contest_btn.setText("Insufficient balance | Add Cash");
            }
            else {
                join_contest_btn.setBackgroundResource(R.drawable.btn_green);
                join_contest_btn.setText("Confirm");
            }

            model.setSelectedPrice(entryFee[0]+"");
            //txtInvest.setText(entryFee[0]+"");

            join_contest_btn.setOnClickListener(view -> {
                if (join_contest_btn.getText().toString().trim().equalsIgnoreCase("Confirm")){
                    if (model.getCon_type().equalsIgnoreCase("Time")){
                        //String time=edtTime.getText().toString().trim();
                        if (TextUtils.isEmpty(selectedOpinionTime)){
                            CustomUtil.showTopSneakWarning(mContext,"Select Time");
                            return;
                        }
                        dialog.dismiss();
                        model.setOptionValue(selectedOpinionTime);
                        model.setCon_join_qty("1");
                        joinPollContest(model);

                    }
                    else if (model.getCon_type().equalsIgnoreCase("Integer")){
                        String integer=edtInteger.getText().toString().trim();
                        if (TextUtils.isEmpty(integer)){
                            CustomUtil.showTopSneakWarning(mContext,"Enter Value");
                            return;
                        }
                        dialog.dismiss();
                        model.setCon_join_qty("1");
                        model.setOptionValue(integer);
                        joinPollContest(model);
                    }
                }else {

                    double amt=Math.ceil(entryFee[0] - totalWalletBalance);
                    if (amt<1){
                        amt=amt+1;
                    }
                    int intamt=(int) amt;
                    if (intamt<amt){
                        intamt+=1;
                    }
                    String payableAmt=String.valueOf(intamt);

                    startActivity(new Intent(mContext,AddDepositActivity.class)
                            .putExtra("depositAmt",payableAmt));

                   /* float leftAmt=(totalWalletBalance-entryFee[0])+1;
                    startActivity(new Intent(mContext,AddDepositActivity.class)
                            .putExtra("depositAmt",((int)leftAmt)+""));*/
                }

            });

            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
        }
    }

    private void showConfirmContestDialog(EventModel model,JSONObject data) {

        selectedEventModel=null;
        contest_id="";
        conType="";

        //use_deposit=use_transfer=use_winning=0;

        final float[] entryFee = {CustomUtil.convertFloat(model.getOptionEntryFee())};
        final float deposit = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        final float winning = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
        final float transfer_bal = CustomUtil.convertFloat(preferences.getUserModel().getTransferBal());
        final float ff_coin = CustomUtil.convertFloat(preferences.getUserModel().getFf_coin());

        /*if ((entryFee[0] - deposit) < 0) {
            use_deposit = entryFee[0];
        }
        else if ((entryFee[0] - (deposit + transfer_bal)) < 0) {
            use_deposit = deposit;
            use_transfer = entryFee[0] - deposit;
        }
        else {
            use_deposit = deposit;
            use_transfer = transfer_bal;
            use_winning = entryFee[0] - (deposit + transfer_bal);
        }*/

        float calBalance=deposit + transfer_bal + winning + ff_coin;

        if (calBalance < entryFee[0]) {
            CustomUtil.showToast(mContext,"Insufficient Balance");
            double amt=Math.ceil(entryFee[0] -calBalance);
            if (amt<1){
                amt=amt+1;
            }
            int intamt=(int) amt;
            if (intamt<amt){
                intamt+=1;
            }
            String payableAmt=String.valueOf(intamt);
            startActivity(new Intent(mContext,AddDepositActivity.class)
                    .putExtra("depositAmt",payableAmt));
        }
        else {
            if (dialog!=null && dialog.isShowing()){
                return;
            }

            DecimalFormat format = CustomUtil.getFormater("00.00");
            String rs=mContext.getResources().getString(R.string.rs);
            UserModel userModel=preferences.getUserModel();

            float totalWalletBalance=CustomUtil.convertFloat(userModel.getDepositBal())+
                    CustomUtil.convertFloat(userModel.getFf_coin())+
                    CustomUtil.convertFloat(userModel.getWinBal())+CustomUtil.convertFloat(userModel.getTransferBal());


            //View view = LayoutInflater.from(mContext).inflate(R.layout.opinion_confirm_dialog, null);

            tradingDialogId=model.getId();
            dialogEventModel=model;

            dialog=new BottomSheetDialog(mContext);
            dialog.setContentView(R.layout.trading_confirm_dialog);

            final int[] qty = {0};
            final int[] price = {0};

            yesList.clear();
            noList.clear();
            qtyList.clear();

            JSONArray yesCount=data.optJSONArray("yesCount");
            JSONArray noCount=data.optJSONArray("noCount");

            for (int i = 0; i < yesCount.length(); i++) {
                JSONObject object=yesCount.optJSONObject(i);
                AvailableQtyModel model1=gson.fromJson(object.toString(),AvailableQtyModel.class);
                if (CustomUtil.convertFloat(model1.getTotalJoinCnt()) > 0)
                    yesList.add(model1);
            }

            for (int i = 0; i < noCount.length(); i++) {
                JSONObject object=noCount.optJSONObject(i);
                AvailableQtyModel model1=gson.fromJson(object.toString(),AvailableQtyModel.class);
                if (CustomUtil.convertFloat(model1.getTotalJoinCnt()) > 0)
                    noList.add(model1);
            }

            EditText edtQty=dialog.findViewById(R.id.edtQty);
            edtQty.setText("1");
            TextView txtPrice=dialog.findViewById(R.id.txtPrice);
            txtPrice.setText(rs+"1");
            TextView txtOptionYes=dialog.findViewById(R.id.txtOptionYes);
            TextView txtOptionNo=dialog.findViewById(R.id.txtOptionNo);
            txtQtyAtPrice=dialog.findViewById(R.id.txtQtyAtPrice);
            txtMaxQtyAtPrice=dialog.findViewById(R.id.txtMaxQtyAtPrice);
            TextView txtSelect=dialog.findViewById(R.id.txtSelect);

            TextView txtEndDate=dialog.findViewById(R.id.txtEndDate);
            ImageView imgConImage=dialog.findViewById(R.id.imgConImage);
            TextView txtConTitle=dialog.findViewById(R.id.txtConTitle);
            TextView txtDesc=dialog.findViewById(R.id.txtDesc);
            LinearLayout layDesc=dialog.findViewById(R.id.layDesc);
            txtTrades=dialog.findViewById(R.id.txtTrades);
            TextView join_contest_btn=dialog.findViewById(R.id.join_contest_btn);
            TextView txtGet=dialog.findViewById(R.id.txtGet);
            TextView txtInvest=dialog.findViewById(R.id.txtInvest);
            TextView txtNewBadge=dialog.findViewById(R.id.txtNewBadge);
            TextView txtLblCommision=dialog.findViewById(R.id.txtLblCommision);
            TextView txtBtn10=dialog.findViewById(R.id.txtBtn10);
            TextView txtBtn50=dialog.findViewById(R.id.txtBtn50);
            TextView txtBtn100=dialog.findViewById(R.id.txtBtn100);
            TextView txtBtn500=dialog.findViewById(R.id.txtBtn500);
            TextView txtBtn750=dialog.findViewById(R.id.txtBtn750);

            txtLblCommision.setText("Fee of "+dialogEventModel.getCommission()+"% on credit amount, 0% fee on loss.");

            txtEndDate.setText("Ends on "+CustomUtil.dateConvertWithFormat(dialogEventModel.getConEndTime(),"MMM dd hh:mm a"));

            if (dialogEventModel.isNewBadge()){
                txtNewBadge.setVisibility(View.VISIBLE);
            }else {
                txtNewBadge.setVisibility(View.GONE);
            }

            txtConTitle.setText(dialogEventModel.getConDesc());
            if (TextUtils.isEmpty(dialogEventModel.getConSubDesc())){
                layDesc.setVisibility(View.GONE);
            }else {
                layDesc.setVisibility(View.VISIBLE);
                txtDesc.setText(dialogEventModel.getConSubDesc());
            }

            if (!TextUtils.isEmpty(dialogEventModel.getConImage())){
                CustomUtil.loadImageWithGlide(mContext,imgConImage, MyApp.imageBase + MyApp.document,dialogEventModel.getConImage(),R.drawable.event_placeholder);
            }

            txtTrades.setText(dialogEventModel.getTotalTrades()+" Trades");

            SeekBar seekBarPrice = dialog.findViewById(R.id.seekPrice);

            txtOptionYes.setOnClickListener(v->{
                dialogEventModel.setOptionValue("Yes");
                txtOptionYes.setBackgroundResource(R.drawable.opinio_yes);
                txtOptionNo.setBackgroundResource(R.drawable.transparent_view);
                txtOptionNo.setTextColor(mContext.getResources().getColor(R.color.black_pure));
                txtOptionYes.setTextColor(mContext.getResources().getColor(R.color.green_pure));

                setOpinionValue(dialogEventModel.getOptionValue(),price[0]+"",data,txtQtyAtPrice);

                if (yesList.size()>0){
                    txtMaxQtyAtPrice.setText(yesList.get(0).getTotalJoinCnt()+" quantities available at "+
                            rs+String.format("%.2f",CustomUtil.convertFloat(yesList.get(0).getJnAmount())));
                }else {
                    txtMaxQtyAtPrice.setText("0 quantities available at "+rs+format.format(price[0]));
                }

            });

            txtOptionNo.setOnClickListener(v->{
                dialogEventModel.setOptionValue("No");
                txtOptionYes.setBackgroundResource(R.drawable.transparent_view);
                txtOptionNo.setBackgroundResource(R.drawable.opinio_no);
                txtOptionNo.setTextColor(mContext.getResources().getColor(R.color.red));
                txtOptionYes.setTextColor(mContext.getResources().getColor(R.color.black_pure));

                setOpinionValue(dialogEventModel.getOptionValue(),price[0]+"",data,txtQtyAtPrice);

                if (noList.size()>0){
                    txtMaxQtyAtPrice.setText(noList.get(0).getTotalJoinCnt()+" quantities available at "+
                            rs+String.format("%.2f",CustomUtil.convertFloat(noList.get(0).getJnAmount())));
                }else {
                    txtMaxQtyAtPrice.setText("0 quantities available at "+rs+format.format(price[0]));
                }

            });

            seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    price[0]=progress;

                    dialogEventModel.setSelectedPrice(price[0]+"");

                    txtPrice.setText(rs+price[0]);

                    float investValue=price[0]*qty[0];
                    txtInvest.setText(rs+investValue+"");

                    setOpinionValue(dialogEventModel.getOptionValue(),price[0]+"",data,txtQtyAtPrice);

                    if (totalWalletBalance<(qty[0]*price[0])){
                        join_contest_btn.setBackgroundResource(R.drawable.red_bg_radius);
                        join_contest_btn.setText("Insufficient balance | Add Cash");
                    }else {
                        join_contest_btn.setBackgroundResource(R.drawable.btn_green);
                        join_contest_btn.setText("Confirm");
                    }

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            if (dialogEventModel.getOptionValue().equalsIgnoreCase("yes")){
                txtOptionYes.setBackgroundResource(R.drawable.opinio_yes);
                txtOptionNo.setBackgroundResource(R.drawable.transparent_view);
                txtOptionNo.setTextColor(mContext.getResources().getColor(R.color.black_pure));
                txtOptionYes.setTextColor(mContext.getResources().getColor(R.color.green_pure));

                if (TextUtils.isEmpty(dialogEventModel.getOption1Price())){
                    price[0]=1;
                    dialogEventModel.setSelectedPrice("1");
                }else {
                    price[0]= (int)CustomUtil.convertFloat(dialogEventModel.getOption1Price());
                    dialogEventModel.setSelectedPrice(dialogEventModel.getOption1Price());
                }

                if (yesList.size()>0){
                    txtMaxQtyAtPrice.setText(yesList.get(0).getTotalJoinCnt()+" quantities available at "+
                            rs+String.format("%.2f",CustomUtil.convertFloat(yesList.get(0).getJnAmount())));
                }else {
                    txtMaxQtyAtPrice.setText("0 quantities available at "+rs+"1");
                }
            }
            else {
                txtOptionYes.setBackgroundResource(R.drawable.transparent_view);
                txtOptionNo.setBackgroundResource(R.drawable.opinio_no);
                txtOptionNo.setTextColor(mContext.getResources().getColor(R.color.red));
                txtOptionYes.setTextColor(mContext.getResources().getColor(R.color.black_pure));

                if (noList.size()>0){
                    txtMaxQtyAtPrice.setText(noList.get(0).getTotalJoinCnt()+" quantities available at "+
                            rs+String.format("%.2f",CustomUtil.convertFloat(noList.get(0).getJnAmount())));
                }else {
                    txtMaxQtyAtPrice.setText("0 quantities available at "+rs+"1");
                }

                if (TextUtils.isEmpty(dialogEventModel.getOption2Price())){
                    price[0]=1;
                    dialogEventModel.setSelectedPrice("1");
                }else {
                    price[0]= (int)CustomUtil.convertFloat(dialogEventModel.getOption2Price());
                    dialogEventModel.setSelectedPrice(dialogEventModel.getOption2Price());
                }
            }

            seekBarPrice.setProgress((int) price[0]);

            setOpinionValue(dialogEventModel.getOptionValue(),price[0]+"",data,txtQtyAtPrice);

            SeekBar seekBarQty = dialog.findViewById(R.id.seekQty);

            seekBarQty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    qty[0] =progress;

                    edtQty.setText(qty[0]+"");
                    dialogEventModel.setCon_join_qty(qty[0]+"");

                    float getValue=qty[0]*10;
                    txtGet.setText(rs+getValue+"");

                    float investValue=price[0]*qty[0];
                    txtInvest.setText(rs+investValue+"");
                    LogUtil.e("0price0",price[0]);
                    if (edtQty.hasFocus()){
                        edtQty.setSelection(String.valueOf(progress).length());
                    }

                    if (qty[0]==10){
                        txtBtn10.performClick();
                    }
                    else if (qty[0]==50){
                        txtBtn50.performClick();
                    }
                    else if (qty[0]==100){
                        txtBtn100.performClick();
                    }
                    else if (qty[0]==500){
                        txtBtn500.performClick();
                    }
                    else if (qty[0]==750){
                        txtBtn750.performClick();
                    }
                    else{
                        txtBtn10.setBackgroundResource(R.drawable.border_match_green);
                        txtBtn50.setBackgroundResource(R.drawable.border_match_green);
                        txtBtn100.setBackgroundResource(R.drawable.border_match_green);
                        txtBtn500.setBackgroundResource(R.drawable.border_match_green);
                        txtBtn750.setBackgroundResource(R.drawable.border_match_green);
                    }

                    if (totalWalletBalance<(qty[0]*price[0])){
                        join_contest_btn.setBackgroundResource(R.drawable.red_bg_radius);
                        join_contest_btn.setText("Insufficient balance | Add Cash");
                    }else {
                        join_contest_btn.setBackgroundResource(R.drawable.btn_green);
                        join_contest_btn.setText("Confirm");
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            edtQty.setOnEditorActionListener((v, actionId, event) -> {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    hideKeyboard((TOPActivity) mContext);
                    if (TextUtils.isEmpty(edtQty.getText().toString().trim()) ||
                            CustomUtil.convertInt(edtQty.getText().toString().trim())<=0){
                        edtQty.setText("1");
                        edtQty.setSelection(1);
                    }
                    return false;
                }
                return false;
            });

            edtQty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    /*if (TextUtils.isEmpty(charSequence)){
                        qty[0] = 1;
                    }else {
                        qty[0] = CustomUtil.convertInt(charSequence.toString());
                    }*/
                    if (TextUtils.isEmpty(charSequence)){
                        qty[0] = 1;
                        //edtQty.setText("1");
                        //edtQty.setSelection(1);
                    }else {
                        qty[0] = CustomUtil.convertInt(charSequence.toString());
                        edtQty.setSelection(charSequence.length());
                    }

                    if (qty[0]<=0){
                        qty[0]=1;
                    }
                    else if (qty[0]>1000){
                        qty[0]=1000;
                    }
                  /*  if (edtQty.hasFocus())
                        edtQty.setSelection(charSequence.length());*/

                    seekBarQty.setProgress(qty[0]);
                }

                @Override
                public void afterTextChanged(Editable editable) {}

            });

            txtSelect.setOnClickListener(view1 -> {
                if (dialogEventModel.getOptionValue().equalsIgnoreCase("yes")){
                    if (yesList.size()>0){
                        price[0]=CustomUtil.convertInt(yesList.get(0).getJnAmount());
                    }else {
                        price[0]=1;
                    }
                    seekBarPrice.setProgress((int)price[0]);
                }else {
                    if (noList.size()>0){
                        price[0]=CustomUtil.convertInt(noList.get(0).getJnAmount());
                    }else {
                        price[0]=1;
                    }
                    seekBarPrice.setProgress((int)price[0]);
                }
            });

            txtBtn10.setOnClickListener(view1 -> {
                edtQty.setText("10");

                txtBtn10.setBackgroundResource(R.drawable.opinio_yes);
                txtBtn50.setBackgroundResource(R.drawable.border_match_green);
                txtBtn100.setBackgroundResource(R.drawable.border_match_green);
                txtBtn500.setBackgroundResource(R.drawable.border_match_green);
                txtBtn750.setBackgroundResource(R.drawable.border_match_green);
            });

            txtBtn50.setOnClickListener(view1 -> {
                edtQty.setText("50");

                txtBtn50.setBackgroundResource(R.drawable.opinio_yes);
                txtBtn10.setBackgroundResource(R.drawable.border_match_green);
                txtBtn100.setBackgroundResource(R.drawable.border_match_green);
                txtBtn500.setBackgroundResource(R.drawable.border_match_green);
                txtBtn750.setBackgroundResource(R.drawable.border_match_green);
            });

            txtBtn100.setOnClickListener(view1 -> {
                edtQty.setText("100");

                txtBtn100.setBackgroundResource(R.drawable.opinio_yes);
                txtBtn50.setBackgroundResource(R.drawable.border_match_green);
                txtBtn10.setBackgroundResource(R.drawable.border_match_green);
                txtBtn500.setBackgroundResource(R.drawable.border_match_green);
                txtBtn750.setBackgroundResource(R.drawable.border_match_green);
            });

            txtBtn500.setOnClickListener(view1 -> {
                edtQty.setText("500");

                txtBtn500.setBackgroundResource(R.drawable.opinio_yes);
                txtBtn50.setBackgroundResource(R.drawable.border_match_green);
                txtBtn100.setBackgroundResource(R.drawable.border_match_green);
                txtBtn10.setBackgroundResource(R.drawable.border_match_green);
                txtBtn750.setBackgroundResource(R.drawable.border_match_green);
            });

            txtBtn750.setOnClickListener(view1 -> {
                edtQty.setText("750");

                txtBtn750.setBackgroundResource(R.drawable.opinio_yes);
                txtBtn50.setBackgroundResource(R.drawable.border_match_green);
                txtBtn100.setBackgroundResource(R.drawable.border_match_green);
                txtBtn500.setBackgroundResource(R.drawable.border_match_green);
                txtBtn10.setBackgroundResource(R.drawable.border_match_green);
            });

            seekBarQty.setProgress(10);

            join_contest_btn.setOnClickListener(v -> {
                if (join_contest_btn.getText().toString().trim().equalsIgnoreCase("Confirm")){
                    if (TextUtils.isEmpty(dialogEventModel.getCon_join_qty()) ){
                        CustomUtil.showTopSneakWarning(mContext,"Select contest quantity");
                        return;
                    }
                    if (CustomUtil.convertInt(dialogEventModel.getCon_join_qty())<=0){
                        CustomUtil.showTopSneakWarning(mContext,"Select contest quantity");
                        return;
                    }
                    dialog.dismiss();
                    joinOpinionContest(dialogEventModel);
                }
                else {
                    if (totalWalletBalance<(qty[0]*price[0])){
                        double amt=Math.ceil((qty[0]*price[0]) - totalWalletBalance);
                        if (amt<1){
                            amt=amt+1;
                        }
                        int intamt=(int) amt;
                        if (intamt<amt){
                            intamt+=1;
                        }
                        String payableAmt=String.valueOf(intamt);

                        startActivity(new Intent(mContext,AddDepositActivity.class)
                                .putExtra("depositAmt",payableAmt));
                    }
                }
            });

            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);

            dialog.show();
        }
    }

    private void showConfirmTradeBoardDialog(EventModel model){

        final float[] entryFee = {CustomUtil.convertFloat(model.getEntryFee())};
        final float deposit = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        final float winning = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
        final float transfer_bal = CustomUtil.convertFloat(preferences.getUserModel().getTransferBal());
        final float ff_coin = CustomUtil.convertFloat(preferences.getUserModel().getFf_coin());

        float calBalance=deposit + transfer_bal + winning + ff_coin;

        if (calBalance < entryFee[0]) {
            CustomUtil.showToast(mContext,"Insufficient Balance");
            double amt=Math.ceil(entryFee[0] -calBalance);
            if (amt<1){
                amt=amt+1;
            }
            int intamt=(int) amt;
            if (intamt<amt){
                intamt+=1;
            }
            String payableAmt=String.valueOf(intamt);
            startActivity(new Intent(mContext,AddDepositActivity.class)
                    .putExtra("depositAmt",payableAmt));
        }
        else{
            confirmDialogModel=model;

            confirmDialog=new BottomSheetDialog(mContext);
            confirmDialog.setContentView(R.layout.dialog_confirm_trading);

            DecimalFormat format = CustomUtil.getFormater("00.00");

            txtConfirmContestTime = confirmDialog.findViewById(R.id.txtEndDate);
            txtConfirnConTrades = confirmDialog.findViewById(R.id.txtTrades);
            TextView txtConDesc = confirmDialog.findViewById(R.id.txtConDesc);
            TextView team1TradeBoard = confirmDialog.findViewById(R.id.team1TradeBoard);
            TextView team2TradeBoard = confirmDialog.findViewById(R.id.team2TradeBoard);
            txtConfirmOption1 = confirmDialog.findViewById(R.id.txtRateTeam1);
            txtConfirmOption2 = confirmDialog.findViewById(R.id.txtRateTeam2);
            layoutTeam1 = confirmDialog.findViewById(R.id.layoutTeam1);
            layoutTeam2 = confirmDialog.findViewById(R.id.layoutTeam2);
            LinearLayout layoutRateTeam1 = confirmDialog.findViewById(R.id.layoutRateTeam1);
            LinearLayout layoutRateTeam2 = confirmDialog.findViewById(R.id.layoutRateTeam2);
            EditText edtQty = confirmDialog.findViewById(R.id.edtQty);
            edtQty.setText("10");
            seekConfirmQty = confirmDialog.findViewById(R.id.seekbarQty);
            TextView txtBtn10=confirmDialog.findViewById(R.id.txtBtn10);
            TextView txtBtn100=confirmDialog.findViewById(R.id.txtBtn100);
            TextView txtBtn500=confirmDialog.findViewById(R.id.txtBtn500);
            TextView txtBtn1000=confirmDialog.findViewById(R.id.txtBtn1000);
            TextView txtBtn10000=confirmDialog.findViewById(R.id.txtBtn10000);
            TextView txtInvest=confirmDialog.findViewById(R.id.txtInvest);
            txtContestTime=confirmDialog.findViewById(R.id.txtContestTime);
            TextView txtContestDate=confirmDialog.findViewById(R.id.txtContestDate);
            TextView txtNewWinning=confirmDialog.findViewById(R.id.txtNewWinning);
            TextView txtEntryFee=confirmDialog.findViewById(R.id.entryFee);
            CircleImageView imgContest1=confirmDialog.findViewById(R.id.imgContest1);
            CircleImageView imgContest2=confirmDialog.findViewById(R.id.imgContest2);
            join_confirm_contest_btn=confirmDialog.findViewById(R.id.join_contest_btn);
            String rs=mContext.getResources().getString(R.string.rs);

            txtConfirmContestTime.setText(model.getConSubDesc());//"ENDS ON "+CustomUtil.dateConvertWithFormat(model.getConEndTime(),"dd-MMM-yy hh:mm a")
            txtConDesc.setText(model.getConDesc());
            team1TradeBoard.setText(model.getOption1());
            team2TradeBoard.setText(model.getOption2());
            txtConfirmOption1.setText(model.getOption1Price());
            txtConfirmOption2.setText(model.getOption2Price());
            txtConfirnConTrades.setText(CustomUtil.coolFormat(mContext,model.getTotalTrades())+" joined");
            txtEntryFee.setText("Confirm Trade "+"("+mContext.getResources().getString(R.string.rs)+model.getEntryFee()+" = 1 Slot)");

            confirmPrise=entryFee[0];
            confirmQty=0;

            startTradeBoarTimer(model.getConEndTime());

            UserModel userModel=preferences.getUserModel();

            float totalWalletBalance=CustomUtil.convertFloat(userModel.getDepositBal())+
                    CustomUtil.convertFloat(userModel.getFf_coin())+
                    CustomUtil.convertFloat(userModel.getWinBal())+CustomUtil.convertFloat(userModel.getTransferBal());

            CustomUtil.loadImageWithGlide(mContext,imgContest1, ApiManager.TEAM_IMG,model.getOptionImg1(),R.drawable.event_placeholder);
            CustomUtil.loadImageWithGlide(mContext,imgContest2, ApiManager.TEAM_IMG,model.getOptionImg2(),R.drawable.event_placeholder);

            layoutTeam1.setOnClickListener(v -> {

                confirmDialogModel.setOptionValue(confirmDialogModel.getOption1());
                confirmDialogModel.setOptionPrice(confirmDialogModel.getOption1Price());

                layoutRateTeam1.setBackgroundResource(R.drawable.tradeboard_rate_bg_selected);
                layoutRateTeam2.setBackgroundResource(R.drawable.tradeboard_rate_bg_unselected);
                txtConfirmOption1.setTextColor(getResources().getColor(R.color.green_pure));
                txtConfirmOption2.setTextColor(getResources().getColor(R.color.black_pure));

                float investValue=confirmPrise*confirmQty;
                txtInvest.setText(rs + investValue + "");

                float winning1=investValue * Float.parseFloat(confirmDialogModel.getOption1Price());
                txtNewWinning.setText(rs + winning1 + "");

            });

            layoutTeam2.setOnClickListener(v -> {

                confirmDialogModel.setOptionValue(confirmDialogModel.getOption2());
                confirmDialogModel.setOptionPrice(confirmDialogModel.getOption2Price());

                layoutRateTeam1.setBackgroundResource(R.drawable.tradeboard_rate_bg_unselected);
                layoutRateTeam2.setBackgroundResource(R.drawable.tradeboard_rate_bg_selected);
                txtConfirmOption1.setTextColor(getResources().getColor(R.color.black_pure));
                txtConfirmOption2.setTextColor(getResources().getColor(R.color.green_pure));

                float investValue=confirmPrise*confirmQty;
                txtInvest.setText(rs + investValue + "");

                float winning2=investValue * Float.parseFloat(confirmDialogModel.getOption2Price());
                txtNewWinning.setText(rs + winning2 + "");
            });

            if (confirmDialogModel.getOption1().equalsIgnoreCase(confirmDialogModel.getOptionValue())){
                layoutTeam1.performClick();
            }
            else {
                layoutTeam2.performClick();
            }

            Date date = null;
            String confirmTradeDate = "";
            SimpleDateFormat format1 = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat matchFormate = MyApp.changedFormat("dd-MMM-yy");
            try {
                date = format1.parse(model.getConEndTime());
                confirmTradeDate = matchFormate.format(date);
            }
            catch (ParseException e) {
                //LogUtil.e(TAG, "error onBindViewHolder: " + e.toString());
                e.printStackTrace();
            }

            txtContestDate.setText(confirmTradeDate);

            seekConfirmQty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /*qty[0] =progress;
                edtQty.setText(qty[0]+"");
                model.setCon_join_qty(qty[0]+"");

                float winning=qty[0]*CustomUtil.convertFloat(model.getOptionPrice());
                txtNewWinning.setText(rs+winning+"");
                LogUtil.e("winning",String.valueOf(winning));

                float investValue=qty[0];
                txtInvest.setText(rs+investValue+"");
                LogUtil.e("Invest_value",String.valueOf(qty[0]));*/

                    confirmQty = progress;
                    edtQty.setText(String.valueOf(confirmQty));
                    model.setCon_join_qty(String.valueOf(confirmQty));

                    /*float winning1 =confirmQty * CustomUtil.convertFloat(model.getOptionPrice());
                    float winning2 =confirmQty * CustomUtil.convertFloat(model.getOption2Price());

                    if (model.isItemSelected()){
                        txtNewWinning.setText(rs + winning1);
                        float investValue =confirmQty;
                        txtInvest.setText(rs + investValue);
                    }else{
                        txtNewWinning.setText(rs + winning2 + "");
                        float investValue =confirmQty;
                        txtInvest.setText(rs + investValue + "");
                    }*/
                    float investValue=confirmPrise*confirmQty;
                    txtInvest.setText(rs+format.format(investValue)+"");

                    float getValue;
                    if (confirmDialogModel.getOption1Price().equalsIgnoreCase(confirmDialogModel.getOptionPrice())){
                        getValue = investValue * Float.parseFloat(confirmDialogModel.getOption1Price());
                    }else {
                        getValue = investValue * Float.parseFloat(confirmDialogModel.getOption2Price());
                    }
                    txtNewWinning.setText(rs+format.format(getValue)+"");

                    if (edtQty.hasFocus()){
                        edtQty.setSelection(String.valueOf(progress).length());
                    }

                    if (confirmQty==10){
                        txtBtn10.performClick();
                    }
                    else if (confirmQty==100){
                        txtBtn100.performClick();
                    }
                    else if (confirmQty==500){
                        txtBtn500.performClick();
                    }
                    else if (confirmQty==1000){
                        txtBtn1000.performClick();
                    }
                    else if (confirmQty==10000){
                        txtBtn10000.performClick();
                    }
                    else{
                        txtBtn10.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                        txtBtn100.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                        txtBtn500.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                        txtBtn1000.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                        txtBtn10000.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                    }

                    if (totalWalletBalance<investValue){

                        join_confirm_contest_btn.setBackgroundResource(R.drawable.red_bg_radius);
                        join_confirm_contest_btn.setText("Insufficient balance | Add Cash");
                    }else {
                        join_confirm_contest_btn.setBackgroundResource(R.drawable.btn_green);
                        join_confirm_contest_btn.setText("Confirm");
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            edtQty.setOnEditorActionListener((v, actionId, event) -> {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    hideKeyboard((TOPActivity) mContext);
                    if (TextUtils.isEmpty(edtQty.getText().toString().trim()) ||
                            CustomUtil.convertInt(edtQty.getText().toString().trim())<=0){
                        edtQty.setText("1");
                        edtQty.setSelection(1);
                    }
                    return false;
                }
                return false;
            });

            edtQty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (TextUtils.isEmpty(charSequence)){
                       confirmQty = 1;
                    /*edtQty.setText("10");
                    edtQty.setSelection(1);*/
                    }else {
                       confirmQty = CustomUtil.convertInt(charSequence.toString());
                        edtQty.setSelection(charSequence.length());
                    }

                    if (confirmQty<=0){
                       confirmQty=1;
                    }
                    else if (confirmQty>10000){
                       confirmQty=10000;
                    }
                    if (edtQty.hasFocus())
                        edtQty.setSelection(charSequence.length());

                    seekConfirmQty.setProgress(confirmQty);
                }

                @Override
                public void afterTextChanged(Editable editable) {}

            });

            txtBtn10.setOnClickListener(view1 -> {
                edtQty.setText("10");

                txtBtn10.setBackgroundResource(R.drawable.tradeboard_rate_bg_selected);
                txtBtn100.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                txtBtn500.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                txtBtn1000.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                txtBtn10000.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
            });

            txtBtn100.setOnClickListener(view1 -> {
                edtQty.setText("100");

                txtBtn10.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                txtBtn100.setBackgroundResource(R.drawable.tradeboard_rate_bg_selected);
                txtBtn500.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                txtBtn1000.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                txtBtn10000.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
            });

            txtBtn500.setOnClickListener(view1 -> {
                edtQty.setText("500");

                txtBtn10.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                txtBtn100.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                txtBtn500.setBackgroundResource(R.drawable.tradeboard_rate_bg_selected);
                txtBtn1000.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                txtBtn10000.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
            });

            txtBtn1000.setOnClickListener(view1 -> {
                edtQty.setText("1000");

                txtBtn10.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                txtBtn100.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                txtBtn500.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                txtBtn1000.setBackgroundResource(R.drawable.tradeboard_rate_bg_selected);
                txtBtn10000.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
            });

            txtBtn10000.setOnClickListener(view1 -> {
                edtQty.setText("10000");

                txtBtn10.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                txtBtn100.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                txtBtn500.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                txtBtn1000.setBackgroundResource(R.drawable.tradeboard_rate_bg_not_fill);
                txtBtn10000.setBackgroundResource(R.drawable.tradeboard_rate_bg_selected);
            });
            seekConfirmQty.setProgress(10);

            join_confirm_contest_btn.setOnClickListener(v -> {
                if (join_confirm_contest_btn.getText().toString().trim().equalsIgnoreCase("Confirm")){
                    LogUtil.e("JoinQTYyy",model.getCon_join_qty());
                    if (TextUtils.isEmpty(model.getCon_join_qty()) ){
                        CustomUtil.showTopSneakWarning(mContext,"Select contest quantity");
                        return;
                    }
                    if (CustomUtil.convertInt(model.getCon_join_qty())<=0){
                        CustomUtil.showTopSneakWarning(mContext,"Select contest quantity");
                        return;
                    }

                    LogUtil.e("JoinYes","yes");
                    confirmDialog.dismiss();
                    joinTradeBoardContest(model);
                }
                else {
                    LogUtil.e("Joinno","no");
                    if (totalWalletBalance<(confirmQty*confirmPrise)){
                        double amt=Math.ceil((confirmQty*confirmPrise) - totalWalletBalance);
                        if (amt<1){
                            amt=amt+1;
                        }
                        int intamt=(int) amt;
                        if (intamt<amt){
                            intamt+=1;
                        }
                        String payableAmt=String.valueOf(intamt);

                        startActivity(new Intent(mContext,AddDepositActivity.class)
                                .putExtra("depositAmt",payableAmt));
                    }
                }
            });
            Window window = confirmDialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);

            confirmDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (timer != null) {
                        timer.cancel();
                    }
                }
            });

            confirmDialog.show();
        }


    }

    private void startTradeBoarTimer(String endTime){
        if (timer != null) {
            timer.cancel();
        }

        Date date = null;
        String confirmTradeDate = "";
        SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
        //SimpleDateFormat matchFormate = MyApp.changedFormat("dd-MMM-yy");
        try {
            date = format.parse(endTime);
            //confirmTradeDate = matchFormate.format(date);
            diff = date.getTime() - MyApp.getCurrentDate().getTime();
            //LogUtil.e("Difghgfff", String.valueOf(diff));
            //Log.e(TAG, "onBindViewHolder: " + diff);
        }
        catch (ParseException e) {
            //LogUtil.e(TAG, "error onBindViewHolder: " + e.toString());
            e.printStackTrace();
        }
        timer = new CountDownTimer(diff, 1000) {

            public void onTick(long millisUntilFinished) {

                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;

                long elapsedDays = millisUntilFinished / daysInMilli;
                millisUntilFinished = millisUntilFinished % daysInMilli;

                long elapsedHours = millisUntilFinished / hoursInMilli;
                millisUntilFinished = millisUntilFinished % hoursInMilli;

                long elapsedMinutes = millisUntilFinished / minutesInMilli;
                millisUntilFinished = millisUntilFinished % minutesInMilli;

                long elapsedSeconds = millisUntilFinished / secondsInMilli;



                String diff = "";
                if (elapsedDays > 0) {
                    diff = CustomUtil.getFormater("00").format(elapsedDays) + "D " +": "+ CustomUtil.getFormater("00").format(elapsedHours) + "H";
                }
                else if (elapsedHours > 0) {
                    diff = CustomUtil.getFormater("00").format(elapsedHours) + "H "+": " + CustomUtil.getFormater("00").format(elapsedMinutes) + "M";
                }
                else {
                    diff = CustomUtil.getFormater("00").format(elapsedMinutes) + "M "+": " + CustomUtil.getFormater("00").format(elapsedSeconds) + "S";
                }

                txtContestTime.setText(diff);
            }

            @Override
            public void onFinish() {
                txtContestTime.setText("Event Ended");
            }

        }.start();
    }

    private void showConfirmPollContestDialog(EventModel model) {

        selectedEventModel=null;
        contest_id="";
        conType="";

        //use_deposit=use_transfer=use_winning=0;

        final float[] entryFee = {CustomUtil.convertFloat(model.getEntryFee())};
        final float deposit = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        final float winning = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
        final float transfer_bal = CustomUtil.convertFloat(preferences.getUserModel().getTransferBal());

        /*if ((entryFee[0] - deposit) < 0) {
            use_deposit = entryFee[0];
        }
        else if ((entryFee[0] - (deposit + transfer_bal)) < 0) {
            use_deposit = deposit;
            use_transfer = entryFee[0] - deposit;
        }
        else {
            use_deposit = deposit;
            use_transfer = transfer_bal;
            use_winning = entryFee[0] - (deposit + transfer_bal);
        }*/

        float calBalance=deposit + transfer_bal + winning;

        if (calBalance < entryFee[0]) {
            CustomUtil.showToast(mContext,"Insufficient Balance");
            double amt=Math.ceil(entryFee[0] -calBalance);
            if (amt<1){
                amt=amt+1;
            }
            int intamt=(int) amt;
            if (intamt<amt){
                intamt+=1;
            }
            String payableAmt=String.valueOf(intamt);
            startActivity(new Intent(mContext,AddDepositActivity.class)
                    .putExtra("depositAmt",payableAmt));
        }
        else {
            DecimalFormat format = CustomUtil.getFormater("#.##");
            String rs=mContext.getResources().getString(R.string.rs);
            UserModel userModel=preferences.getUserModel();

            float totalWalletBalance=CustomUtil.convertFloat(userModel.getDepositBal())+
                    CustomUtil.convertFloat(userModel.getWinBal())+CustomUtil.convertFloat(userModel.getTransferBal());

            //View view = LayoutInflater.from(mContext).inflate(R.layout.opinion_confirm_dialog, null);

            Dialog dialog=new Dialog(mContext);
            dialog.setContentView(R.layout.poll_join_dialog);

            final int[] qty = {0};
            final int[] price = {0};

            TextView txtEndDate=dialog.findViewById(R.id.txtEndDate);

            TextView txtOp1=dialog.findViewById(R.id.txtOp1);
            TextView txtOp2=dialog.findViewById(R.id.txtOp2);
            TextView txtOp3=dialog.findViewById(R.id.txtOp3);
            TextView txtOp4=dialog.findViewById(R.id.txtOp4);
            TextView txtOp5=dialog.findViewById(R.id.txtOp5);

            RelativeLayout layOp1=dialog.findViewById(R.id.layOp1);
            RelativeLayout layOp2=dialog.findViewById(R.id.layOp2);
            RelativeLayout layOp3=dialog.findViewById(R.id.layOp3);
            RelativeLayout layOp4=dialog.findViewById(R.id.layOp4);
            RelativeLayout layOp5=dialog.findViewById(R.id.layOp5);

            ProgressBar progressOp1=dialog.findViewById(R.id.progressOp1);
            ProgressBar progressOp2=dialog.findViewById(R.id.progressOp2);
            ProgressBar progressOp3=dialog.findViewById(R.id.progressOp3);
            ProgressBar progressOp4=dialog.findViewById(R.id.progressOp4);
            ProgressBar progressOp5=dialog.findViewById(R.id.progressOp5);
            //TextView txtPrice=dialog.findViewById(R.id.txtPrice);
            TextView txtConDesc=dialog.findViewById(R.id.txtConDesc);
            SeekBar seekBarQty=dialog.findViewById(R.id.seekQty);

            TextView txtBtn10=dialog.findViewById(R.id.txtBtn10);
            TextView txtBtn50=dialog.findViewById(R.id.txtBtn50);
            TextView txtBtn100=dialog.findViewById(R.id.txtBtn100);
            TextView txtBtn500=dialog.findViewById(R.id.txtBtn500);
            TextView txtBtn750=dialog.findViewById(R.id.txtBtn750);

            TextView txtTrades=dialog.findViewById(R.id.txtTrades);
            TextView txtConTitle=dialog.findViewById(R.id.txtConTitle);
            TextView txtInvest=dialog.findViewById(R.id.txtInvest);
            TextView txtGet=dialog.findViewById(R.id.txtGet);
            EditText edtQty=dialog.findViewById(R.id.edtQty);
            TextView join_contest_btn=dialog.findViewById(R.id.join_contest_btn);
            ImageView imgConImage=dialog.findViewById(R.id.imgConImage);

            txtEndDate.setText("Ends on "+ CustomUtil.dateConvertWithFormat(model.getConEndTime(),"MMM dd hh:mm aa"));
            txtConTitle.setText(model.getConDesc());
            txtConDesc.setText(model.getConSubDesc());

            if (TextUtils.isEmpty(model.getTotalTrades()) || model.getTotalTrades().equalsIgnoreCase("null"))
                txtTrades.setText("0 Vote");
            else
                txtTrades.setText(model.getTotalTrades()+" Votes");

            if (!TextUtils.isEmpty(model.getConImage())){
                CustomUtil.loadImageWithGlide(mContext,imgConImage, MyApp.imageBase + MyApp.document,model.getConImage(),R.drawable.event_placeholder);
            }

            if (TextUtils.isEmpty(model.getOption1())){
                layOp1.setVisibility(View.GONE);
            }else {
                layOp1.setVisibility(View.VISIBLE);
                txtOp1.setText(model.getOption1());// + " | "+rs+model.getEntryFee()
            }

            if (TextUtils.isEmpty(model.getOption2())){
                layOp2.setVisibility(View.GONE);
            }else {
                layOp2.setVisibility(View.VISIBLE);
                txtOp2.setText(model.getOption2());// + " | "+rs+model.getEntryFee()
            }

            if (TextUtils.isEmpty(model.getOption3())){
                layOp3.setVisibility(View.GONE);
            }else {
                layOp3.setVisibility(View.VISIBLE);
                txtOp3.setText(model.getOption3());// + " | "+rs+model.getEntryFee()
            }

            if (TextUtils.isEmpty(model.getOption4())){
                layOp4.setVisibility(View.GONE);
            }else {
                layOp4.setVisibility(View.VISIBLE);
                txtOp4.setText(model.getOption4());// + " | "+rs+model.getEntryFee()
            }

            if (TextUtils.isEmpty(model.getOption5())){
                layOp5.setVisibility(View.GONE);
            }else {
                layOp5.setVisibility(View.VISIBLE);
                txtOp5.setText(model.getOption5());// + " | "+rs+model.getEntryFee()
            }

            layOp1.setOnClickListener(view -> {
                model.setOptionValue(model.getOption1());
                progressOp1.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_dark));
                progressOp2.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp3.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp4.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp5.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));

                CalculatePollPercentage(model,qty[0],dialog);
            });

            layOp2.setOnClickListener(view -> {
                model.setOptionValue(model.getOption2());
                progressOp1.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp2.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_dark));
                progressOp3.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp4.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp5.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));

                CalculatePollPercentage(model,qty[0],dialog);
            });

            layOp3.setOnClickListener(view -> {
                model.setOptionValue(model.getOption3());
                progressOp1.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp2.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp3.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_dark));
                progressOp4.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp5.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));

                CalculatePollPercentage(model,qty[0],dialog);
            });

            layOp4.setOnClickListener(view -> {
                model.setOptionValue(model.getOption4());
                progressOp1.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp2.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp3.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp4.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_dark));
                progressOp5.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));

                CalculatePollPercentage(model,qty[0],dialog);
            });

            layOp5.setOnClickListener(view -> {
                model.setOptionValue(model.getOption5());
                progressOp1.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp2.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp3.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp4.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp5.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_dark));

                CalculatePollPercentage(model,qty[0],dialog);
            });

            if (model.getOptionValue().equalsIgnoreCase(model.getOption1())){
                layOp1.performClick();
            }
            else if (model.getOptionValue().equalsIgnoreCase(model.getOption2())){
                layOp2.performClick();
            }
            else if (model.getOptionValue().equalsIgnoreCase(model.getOption3())){
                layOp3.performClick();
            }
            else if (model.getOptionValue().equalsIgnoreCase(model.getOption4())){
                layOp4.performClick();
            }
            else if (model.getOptionValue().equalsIgnoreCase(model.getOption5())){
                layOp5.performClick();
            }

            qty[0]=10;
            price[0]= (int) entryFee[0];

            CalculatePollPercentage(model,qty[0],dialog);

            //txtPrice.setText(rs+price[0]);
            txtInvest.setText(rs+price[0]);

            model.setCon_join_qty(qty[0]+"");
            model.setSelectedPrice(price[0]+"");

            seekBarQty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    qty[0] = progress;

                    edtQty.setText(qty[0]+"");
                    model.setCon_join_qty(qty[0]+"");

                   /* float getValue=qty[0]*10;
                    txtGet.setText(getValue+"");*/

                    float investValue=price[0]*qty[0];
                    txtInvest.setText(rs+investValue+"");

                    if (edtQty.hasFocus()){
                        edtQty.setSelection(String.valueOf(progress).length());
                    }

                    if (qty[0]==10){
                        txtBtn10.performClick();
                    }
                    else if (qty[0]==50){
                        txtBtn50.performClick();
                    }
                    else if (qty[0]==100){
                        txtBtn100.performClick();
                    }
                    else if (qty[0]==500){
                        txtBtn500.performClick();
                    }
                    else if (qty[0]==750){
                        txtBtn750.performClick();
                    }
                    else{
                        txtBtn10.setBackgroundResource(R.drawable.border_match_green);
                        txtBtn50.setBackgroundResource(R.drawable.border_match_green);
                        txtBtn100.setBackgroundResource(R.drawable.border_match_green);
                        txtBtn500.setBackgroundResource(R.drawable.border_match_green);
                        txtBtn750.setBackgroundResource(R.drawable.border_match_green);
                    }

                    if (totalWalletBalance<(qty[0]*price[0])){
                        join_contest_btn.setBackgroundResource(R.drawable.red_bg_radius);
                        join_contest_btn.setText("Insufficient balance | Add Cash");
                    }else {
                        join_contest_btn.setBackgroundResource(R.drawable.btn_green);
                        join_contest_btn.setText("Confirm");
                    }

                    CalculatePollPercentage(model,qty[0],dialog);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            edtQty.setOnEditorActionListener((v, actionId, event) -> {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    hideKeyboard((TOPActivity) mContext);
                    if (TextUtils.isEmpty(edtQty.getText().toString().trim()) ||
                            CustomUtil.convertInt(edtQty.getText().toString().trim())<=0){
                        edtQty.setText("1");
                        edtQty.setSelection(1);
                    }
                    return false;
                }
                return false;
            });

            edtQty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (TextUtils.isEmpty(charSequence)){
                        qty[0] = 1;
                        //edtQty.setText("1");
                        //edtQty.setSelection(1);
                    }else {
                        qty[0] = CustomUtil.convertInt(charSequence.toString());
                        edtQty.setSelection(charSequence.length());
                    }

                    if (qty[0]<=0){
                        qty[0]=1;
                    }else if (qty[0]>1000){
                        qty[0]=1000;
                    }

                    seekBarQty.setProgress(qty[0]);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            txtBtn10.setOnClickListener(view1 -> {
                edtQty.setText("10");

                txtBtn10.setBackgroundResource(R.drawable.opinio_yes);
                txtBtn50.setBackgroundResource(R.drawable.border_match_green);
                txtBtn100.setBackgroundResource(R.drawable.border_match_green);
                txtBtn500.setBackgroundResource(R.drawable.border_match_green);
                txtBtn750.setBackgroundResource(R.drawable.border_match_green);
            });

            txtBtn50.setOnClickListener(view1 -> {
                edtQty.setText("50");

                txtBtn50.setBackgroundResource(R.drawable.opinio_yes);
                txtBtn10.setBackgroundResource(R.drawable.border_match_green);
                txtBtn100.setBackgroundResource(R.drawable.border_match_green);
                txtBtn500.setBackgroundResource(R.drawable.border_match_green);
                txtBtn750.setBackgroundResource(R.drawable.border_match_green);
            });

            txtBtn100.setOnClickListener(view1 -> {
                edtQty.setText("100");

                txtBtn100.setBackgroundResource(R.drawable.opinio_yes);
                txtBtn50.setBackgroundResource(R.drawable.border_match_green);
                txtBtn10.setBackgroundResource(R.drawable.border_match_green);
                txtBtn500.setBackgroundResource(R.drawable.border_match_green);
                txtBtn750.setBackgroundResource(R.drawable.border_match_green);
            });

            txtBtn500.setOnClickListener(view1 -> {
                edtQty.setText("500");

                txtBtn500.setBackgroundResource(R.drawable.opinio_yes);
                txtBtn50.setBackgroundResource(R.drawable.border_match_green);
                txtBtn100.setBackgroundResource(R.drawable.border_match_green);
                txtBtn10.setBackgroundResource(R.drawable.border_match_green);
                txtBtn750.setBackgroundResource(R.drawable.border_match_green);
            });

            txtBtn750.setOnClickListener(view1 -> {
                edtQty.setText("750");

                txtBtn750.setBackgroundResource(R.drawable.opinio_yes);
                txtBtn50.setBackgroundResource(R.drawable.border_match_green);
                txtBtn100.setBackgroundResource(R.drawable.border_match_green);
                txtBtn500.setBackgroundResource(R.drawable.border_match_green);
                txtBtn10.setBackgroundResource(R.drawable.border_match_green);
            });

            seekBarQty.setProgress(qty[0]);
            edtQty.setText(qty[0]+"");

            join_contest_btn.setOnClickListener(view -> {
                dialog.dismiss();
                if (join_contest_btn.getText().toString().trim().equalsIgnoreCase("Confirm")){
                    if (TextUtils.isEmpty(model.getCon_join_qty()) ){
                        CustomUtil.showTopSneakWarning(mContext,"Select contest quantity");
                        return;
                    }
                    if (CustomUtil.convertInt(model.getCon_join_qty())<=0){
                        CustomUtil.showTopSneakWarning(mContext,"Select contest quantity");
                        return;
                    }
                    dialog.dismiss();
                    joinPollContest(model);
                }
                else {

                    double amt=Math.ceil((price[0]*qty[0])-totalWalletBalance);
                    if (amt<1){
                        amt=amt+1;
                    }
                    int intamt=(int) amt;
                    if (intamt<amt){
                        intamt+=1;
                    }
                    String payableAmt=String.valueOf(intamt);

                    startActivity(new Intent(mContext,AddDepositActivity.class)
                            .putExtra("depositAmt",payableAmt));

                    /*float leftAmt=(totalWalletBalance-price[0])+1;
                    startActivity(new Intent(mContext,AddDepositActivity.class)
                            .putExtra("depositAmt",((int)leftAmt)+""));*/
                }

            });

            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);

            dialog.show();
        }
    }

    private void CalculatePollPercentage(EventModel model, int qty,Dialog dialog){
        if (qty==0){
            return;
        }
        DecimalFormat format = CustomUtil.getFormater("#.##");
        String rs=mContext.getResources().getString(R.string.rs);

        float entryFee = CustomUtil.convertFloat(model.getEntryFee());

        TextView txtOp1=dialog.findViewById(R.id.txtOp1Per);
        TextView txtOp2=dialog.findViewById(R.id.txtOp2Per);
        TextView txtOp3=dialog.findViewById(R.id.txtOp3Per);
        TextView txtOp4=dialog.findViewById(R.id.txtOp4Per);
        TextView txtOp5=dialog.findViewById(R.id.txtOp5Per);

        ProgressBar progressOp1=dialog.findViewById(R.id.progressOp1);
        ProgressBar progressOp2=dialog.findViewById(R.id.progressOp2);
        ProgressBar progressOp3=dialog.findViewById(R.id.progressOp3);
        ProgressBar progressOp4=dialog.findViewById(R.id.progressOp4);
        ProgressBar progressOp5=dialog.findViewById(R.id.progressOp5);

        TextView txtGet=dialog.findViewById(R.id.txtGet);

        float op1cnt=CustomUtil.convertFloat(model.getOption_1_cnt());
        float op2cnt=CustomUtil.convertFloat(model.getOption_2_cnt());
        float op3cnt=CustomUtil.convertFloat(model.getOption_3_cnt());
        float op4cnt=CustomUtil.convertFloat(model.getOption_4_cnt());
        float op5cnt=CustomUtil.convertFloat(model.getOption_5_cnt());
        float totalTrade=CustomUtil.convertFloat(model.getTotalTrades())+qty;

        float op1per=(op1cnt*100)/totalTrade;
        float op2per=(op2cnt*100)/totalTrade;
        float op3per=(op3cnt*100)/totalTrade;
        float op4per=(op4cnt*100)/totalTrade;
        float op5per=(op5cnt*100)/totalTrade;

        float entryBeforeCommission=0;
        float winnercnt=0;

        if (model.getOptionValue().equalsIgnoreCase(model.getOption1())){
            op1per=((op1cnt+qty)*100)/totalTrade;

            winnercnt=op1cnt+qty;
        }
        else if (model.getOptionValue().equalsIgnoreCase(model.getOption2())){
            op2per=((op2cnt+qty)*100)/totalTrade;

            winnercnt=op2cnt+qty;
        }
        else if (model.getOptionValue().equalsIgnoreCase(model.getOption3())){
            op3per=((op3cnt+qty)*100)/totalTrade;

            winnercnt=op3cnt+qty;
        }
        else if (model.getOptionValue().equalsIgnoreCase(model.getOption4())){
            op4per=((op4cnt+qty)*100)/totalTrade;

            winnercnt=op4cnt+qty;
        }
        else if (model.getOptionValue().equalsIgnoreCase(model.getOption5())){
            op5per=((op5cnt+qty)*100)/totalTrade;

            winnercnt=op5cnt+qty;
        }
        float totalEEntryFee=entryFee*totalTrade;
        // LogUtil.e("resp","totalEEntryFee: "+totalEEntryFee);

        entryBeforeCommission=totalEEntryFee-(winnercnt*entryFee);

        // LogUtil.e("resp","entryBeforeCommission: "+entryBeforeCommission);

        float entryAfterCommission=entryBeforeCommission-(entryBeforeCommission*CustomUtil.convertFloat(model.getCommission())/100);

        //LogUtil.e("resp","entryAfterCommission: "+entryAfterCommission);

        float winningAmt=entryAfterCommission+(winnercnt*entryFee);
        float winningAmtPerQty=winningAmt/qty;

        float winningPerQtySelected=winningAmt/winnercnt;
        float myAproxWinAmt=winningPerQtySelected*qty;

        // LogUtil.e("resp","winningAmt: "+winningAmt);
        //LogUtil.e("resp","winningAmtPerQty: "+winningAmtPerQty);

        progressOp1.setProgress((int)op1per);
        progressOp2.setProgress((int)op2per);
        progressOp3.setProgress((int)op3per);
        progressOp4.setProgress((int)op4per);
        progressOp5.setProgress((int)op5per);

        txtGet.setText(rs+format.format(myAproxWinAmt));

        txtOp1.setText(format.format(op1per)+"%");
        txtOp2.setText(format.format(op2per)+"%");
        txtOp3.setText(format.format(op3per)+"%");
        txtOp4.setText(format.format(op4per)+"%");
        txtOp5.setText(format.format(op5per)+"%");
    }

    private void setOpinionValue(String option,String price,JSONObject data,TextView txtQtyAtPrice){

        //JSONArray yesCount=data.optJSONArray("yesCount");
        //JSONArray noCount=data.optJSONArray("noCount");

        txtQtyAtPrice.setText("0 quantities available at ₹"+price);

        if (option.equalsIgnoreCase("yes")){
            for (int i = 0; i < yesList.size(); i++) {
                AvailableQtyModel model=yesList.get(i);
                if (model.getJnAmount().equalsIgnoreCase(String.valueOf(price))){
                    txtQtyAtPrice.setText(model.getTotalJoinCnt()+" quantities available at ₹"+price);
                }
            }
        }
        else {
            for (int i = 0; i < noList.size(); i++) {
                AvailableQtyModel model=noList.get(i);
                if (model.getJnAmount().equalsIgnoreCase(String.valueOf(price))){
                    txtQtyAtPrice.setText(model.getTotalJoinCnt()+" quantities available at ₹"+price);
                }
                /*JSONObject yesObj=noCount.optJSONObject(i);
                if (yesObj.optString("jn_amount").equalsIgnoreCase(String.valueOf(price))){
                    txtQtyAtPrice.setText(yesObj.optString("total_join_cnt")+" quantities available at ₹"+price);
                }*/
            }
        }
    }

    private void joinPollContest(EventModel model) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("user_team_name", preferences.getUserModel().getUserTeamName());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("contest_id", model.getId());
            jsonObject.put("entry_fee", model.getEntryFee());
            jsonObject.put("option_value", model.getOptionValue());
            jsonObject.put("con_join_qty", model.getCon_join_qty());
            LogUtil.e("resp","param: "+jsonObject+" \n url: "+ApiManager.joinPollContest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSONNormal(mContext, true, ApiManager.joinPollContest, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e(TAG, "joinPollContest : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    // CustomUtil.showTopSneakSuccess(mContext,responseBody.optString("msg"));
                    selectedOpinionTime="";
                    getUserDetails(model);
                }else {
                    CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

                //preferences.setUserModel(new UserModel());

            }
        });
    }

    private void getUserDetails(EventModel model) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("token_no", preferences.getUserModel().getTokenNo());
            //Log.e(TAG, "getUserDetails: " + jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, true, ApiManager.AUTHV3GetUserDetails, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "USER onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONObject data = responseBody.optJSONObject("data");
                    UserModel userModel = gson.fromJson(data.toString(), UserModel.class);
                    preferences.setUserModel(userModel);
                    //if (model.getType()==1){
                        showOrderSuccessDialog(model);
                        LogUtil.e("tradeYess","TREADYED");
                    //}

                }

            }
            @Override
            public void onFailureResult(String responseBody, int code) {}
        });
    }

    private void showOrderSuccessTradeBoardDialog(EventModel model){
        Dialog dialog=new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_order_success);

        String rs=mContext.getResources().getString(R.string.rs);

        TextView txtSlot=dialog.findViewById(R.id.txtSlot);
        TextView txtRate=dialog.findViewById(R.id.txtRate);
        TextView txtOrderTotal=dialog.findViewById(R.id.txtOrderTotal);
        Button btnClose=dialog.findViewById(R.id.btnClose);
        Button btnMore=dialog.findViewById(R.id.btnMore);
        ImageView imgCorrect=dialog.findViewById(R.id.imgCorrect);

        txtSlot.setText(model.getCon_join_qty());
        txtRate.setText(model.getOptionPrice());
        float total = CustomUtil.convertFloat(model.getCon_join_qty()) * CustomUtil.convertFloat(model.getOptionPrice());
        txtOrderTotal.setText(rs+total+"");
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnMore.setOnClickListener(view -> {
            dialog.dismiss();
           getTradingData();
        });

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    private void joinTradeBoardContest(EventModel model){
        JSONObject param = new JSONObject();
        try {
            param.put("comp_id",ConstantUtil.COMPANY_ID);
            param.put("con_join_qty",model.getCon_join_qty());
            param.put("contest_id",model.getId());
            param.put("entry_fee",model.getEntryFee());
            param.put("option_price",model.getOptionPrice());
            param.put("option_value",model.getOptionValue());
            param.put("user_id",preferences.getUserModel().getId());
            LogUtil.e("resp","param: "+param+" \n url: "+ApiManager.joinTrdeBoardContest);
        }catch (JSONException e){
            e.printStackTrace();
        }

        HttpRestClient.postJSONNormal(mContext, false, ApiManager.joinTrdeBoardContest, param, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "joinTrdeBoardContest : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    // CustomUtil.showTopSneakSuccess(mContext,responseBody.optString("msg"));
                    LogUtil.e("joinTrdeBoardContestPasssed","001234");
                    //showOrderSuccessTradeBoardDialog(model);
                    showOrderSuccessDialog(model);
                }else {
                    CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                LogUtil.e("joinTrdeBoardContest Failed",responseBody);
            }
        });
    }

    private void joinOpinionContest(EventModel model) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("contest_id", model.getId());
            jsonObject.put("entry_fee", model.getSelectedPrice());
            jsonObject.put("option_value", model.getOptionValue());
            jsonObject.put("con_join_qty", model.getCon_join_qty());
            LogUtil.e("resp","param: "+jsonObject+" \n url: "+ApiManager.joinTradesContest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSONNormal(mContext, true, ApiManager.joinTradesContest, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e(TAG, "joinContest : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    // CustomUtil.showTopSneakSuccess(mContext,responseBody.optString("msg"));
                    getUserDetails(model);
                }
                else {
                    CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

                //preferences.setUserModel(new UserModel());

            }
        });
    }

    private void showOrderSuccessDialog(EventModel model) {
        //View view = LayoutInflater.from(mContext).inflate(R.layout.order_success_dialog, null);

        Dialog dialog=new Dialog(mContext);
        dialog.setContentView(R.layout.order_success_dialog);

        String rs=mContext.getResources().getString(R.string.rs);

        TextView txtOrderQty=dialog.findViewById(R.id.txtOrderQty);
        TextView txtOrderPrice=dialog.findViewById(R.id.txtOrderPrice);
        TextView txtOrderTotal=dialog.findViewById(R.id.txtOrderTotal);
        TextView lblPrise=dialog.findViewById(R.id.lblPrise);
        TextView txtTitle=dialog.findViewById(R.id.txtTitle);
        TextView lblQty=dialog.findViewById(R.id.lblQty);
        Button btnShare=dialog.findViewById(R.id.btnShare);
        Button btnMore=dialog.findViewById(R.id.btnMore);
        ImageView imgCorrect=dialog.findViewById(R.id.imgCorrect);

        if (selectedTag.equalsIgnoreCase("1")){
            txtTitle.setText("Trade Order placed!");
        }else if (selectedTag.equalsIgnoreCase("2")){
            txtTitle.setText("Opinion Order placed!");
        }else if (selectedTag.equalsIgnoreCase("3")){
            txtTitle.setText("Poll Order placed!");
        }

//        Glide.with(mContext).asGif().load(R.raw.ic_correct).into(imgCorrect);
        MusicManager.getInstance().playEventSuccess(mContext);

        if (model.getType()==0){
            txtOrderQty.setText(model.getCon_join_qty());
            txtOrderPrice.setText(model.getOptionPrice());

            LogUtil.e("resp","getCon_join_qty: "+model.getCon_join_qty());
            float total = CustomUtil.convertFloat(model.getEntryFee()) * CustomUtil.convertFloat(model.getCon_join_qty());
            txtOrderTotal.setText(rs+total+"");

            btnShare.setText("Close");
            lblPrise.setText("Rate");
            lblQty.setText("Slots");
        }else {
            txtOrderQty.setText(model.getCon_join_qty());
            txtOrderPrice.setText(rs+model.getSelectedPrice());
            float total = CustomUtil.convertFloat(model.getSelectedPrice()) * CustomUtil.convertFloat(model.getCon_join_qty());
            txtOrderTotal.setText(rs+total+"");
            lblPrise.setText("Price");
            lblQty.setText("Quantity");
        }

       /* txtOrderQty.setText(model.getCon_join_qty());
        txtOrderPrice.setText(rs+model.getSelectedPrice());
        float total = CustomUtil.convertFloat(model.getSelectedPrice()) * CustomUtil.convertFloat(model.getCon_join_qty());
        txtOrderTotal.setText(rs+total+"");*/

        btnMore.setOnClickListener(view -> {
            dialog.dismiss();
            if (selectedTag.equalsIgnoreCase("2")){
                getOpinionData();
            }else if (selectedTag.equalsIgnoreCase("1")){
                getTradingData();
                LogUtil.e(TAG,"btnMore=>"+selectedTag);
            }else if (selectedTag.equalsIgnoreCase("3")){
                getPollData();
            }
        });

        btnShare.setOnClickListener(view -> {

            dialog.dismiss();
            if (!btnShare.getText().toString().equalsIgnoreCase("Close"))
                shareContest(model);
        });

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        dialog.show();
    }

    private void shareContest(EventModel eventModel) {
        String url= "";
        if (selectedTag.equalsIgnoreCase("1")){
            url= ConstantUtil.LINK_TRADING_URL+eventModel.getId();
        }else  if (selectedTag.equalsIgnoreCase("2")){
            url= ConstantUtil.LINK_OPINION_URL+eventModel.getId();
        }else  if (selectedTag.equalsIgnoreCase("3")){
            url= ConstantUtil.LINK_POLL_URL+eventModel.getId();
        }

        /*FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(url))
                .setDomainUriPrefix(ConstantUtil.LINK_PREFIX_URL)
                .setIosParameters(new DynamicLink.IosParameters.Builder(ConstantUtil.FF_IOS_APP_BUNDLE).setAppStoreId(ConstantUtil.FF_IOS_APP_ID)
                        .setFallbackUrl(Uri.parse(ConstantUtil.FALL_BACK_LINK)).build())
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder()
                        .setFallbackUrl(Uri.parse(ConstantUtil.FALL_BACK_LINK))
                        .build())
                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                .addOnCompleteListener((Activity) mContext, task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().getShortLink()!=null)
                            shareShortLink(task.getResult().getShortLink(),eventModel);
                        else {
                            CustomUtil.showTopSneakWarning(mContext,"Can't share this");
                        }
                    } else {
                        CustomUtil.showTopSneakWarning(mContext,"Can't share this");
                    }
                });*/
    }

    private void shareShortLink(Uri link,EventModel eventModel) {
        String content=//ConstantUtil.LINK_URL+"\n\n"+
                "*"+eventModel.getConDesc()+"* \uD83C\uDFC6"+
                        "\n\nwhat do you think will happen ❓\n\n"+
                        "\uD83D\uDCC8Trade on *Fantafeat*.\n\n"+
                        "\uD83D\uDCB8Earning is simple here.\n\n"+
                        "Deadline⏳ : "+CustomUtil.dateConvertWithFormat(eventModel.getConEndTime(),"dd MMM, yyyy hh:mm a")+
                        "\n\nTap below link for join:\uD83D\uDCF2" +
                        "\n"+link.toString().trim()/*+MyApp.getMyPreferences().getMatchModel().getId()+"/"+list.getId()*/;

        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, content);
            sendIntent.setPackage("com.whatsapp");
            sendIntent.setType("text/html");
            startActivity(sendIntent);
        }catch (ActivityNotFoundException e){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, content);
            sendIntent.setType("text/html");
            startActivity(sendIntent);
        }
    }

    private void showIntroDialog(){


        ArrayList<EventIntroModel> eventIntroModels=new ArrayList<>();

        //View view = LayoutInflater.from(mContext).inflate(R.layout.event_intro_dialog, null);

        Dialog dialog=new Dialog(mContext);
        dialog.setContentView(R.layout.event_intro_dialog);

        ViewPager viewPager=dialog.findViewById(R.id.view_pager);

        LinearLayout dotsLayout=dialog.findViewById(R.id.layoutDots);
        TextView txtSkip=dialog.findViewById(R.id.txtSkip);
        TextView txtNext=dialog.findViewById(R.id.txtNext);

        addBottomDots(0,dotsLayout);

        if (selectedTag.equalsIgnoreCase("1")){
            preferences.setPref(PrefConstant.is_trade_intro,true);
            EventIntroModel model=new EventIntroModel();
            model.setTitle("Choose");
            model.setSubtitle("Trading Event");
            model.setDescription("Select an Trading event that belongs to you");
            model.setImage(R.drawable.event_intro1);
            eventIntroModels.add(model);

            model=new EventIntroModel();
            model.setTitle("Choose");
            model.setSubtitle("Yes or No");
            model.setDescription("Buy Yes if you think the event will happen and No if you don't");
            model.setImage(R.drawable.event_intro2);
            eventIntroModels.add(model);

            model=new EventIntroModel();
            model.setTitle("Set");
            model.setSubtitle("Prize and Qty");
            model.setDescription("Increase quantity to earn more, choose  a prize closer to rate shown on screen for instant match");
            model.setImage(R.drawable.event_intro3);
            eventIntroModels.add(model);

            model=new EventIntroModel();
            model.setTitle("Event Ends");
            model.setSubtitle("Collect Profits");
            model.setDescription("When the outcome of the event becomes clear, you earn rs10 for every correct position you own.");
            model.setImage(R.drawable.event_intro4);
            eventIntroModels.add(model);
        }
        else if (selectedTag.equalsIgnoreCase("2")){
            preferences.setPref(PrefConstant.is_opinion_intro,true);

            EventIntroModel model=new EventIntroModel();
            model.setTitle("Choose");
            model.setSubtitle("Opinion Event");
            model.setDescription("Select an Opinion event that belongs to you");
            model.setImage(R.drawable.event_intro_21);
            eventIntroModels.add(model);

            model=new EventIntroModel();
            model.setTitle("Enter");
            model.setSubtitle("Forecast Value");
            model.setDescription("Forecast as per you think the event will happen near by your forecast value");
            model.setImage(R.drawable.event_intro_22);
            eventIntroModels.add(model);

            model=new EventIntroModel();
            model.setTitle("Check");
            model.setSubtitle("Your Rank");
            model.setDescription("Check your Rank in Forecast Leaderboard. Nearest Forecast value high in rank");
            model.setImage(R.drawable.event_intro_23);
            eventIntroModels.add(model);

            model=new EventIntroModel();
            model.setTitle("Event Ends");
            model.setSubtitle("Collect Profits");
            model.setDescription("When the outcome of the event becomes clear, you earn as per mention Prize Pool.");
            model.setImage(R.drawable.event_intro4);
            eventIntroModels.add(model);
        }
        else if (selectedTag.equalsIgnoreCase("3")){
            preferences.setPref(PrefConstant.is_poll_intro,true);

            EventIntroModel model=new EventIntroModel();
            model.setTitle("Choose");
            model.setSubtitle("Poll Event");
            model.setDescription("Select an Poll event that belongs to you");
            model.setImage(R.drawable.event_intro_31);
            eventIntroModels.add(model);

            model=new EventIntroModel();
            model.setTitle("Choose");
            model.setSubtitle("Option & Investment");
            model.setDescription("Select an option as per you think the event will happen with and set your investment");
            model.setImage(R.drawable.event_intro_32);
            eventIntroModels.add(model);

            model=new EventIntroModel();
            model.setTitle("Check");
            model.setSubtitle("Poll Detail");
            model.setDescription("Check your poll and total poll with other option.");
            model.setImage(R.drawable.event_intro_33);
            eventIntroModels.add(model);

            model=new EventIntroModel();
            model.setTitle("Event Ends");
            model.setSubtitle("Collect Profits");
            model.setDescription("You will win if your poll is matched with the event happen");
            model.setImage(R.drawable.event_intro4);
            eventIntroModels.add(model);
        }

        txtSkip.setOnClickListener(view -> {
            dialog.dismiss();
        });

        txtNext.setOnClickListener(view -> {
            int current = viewPager.getCurrentItem() + 1;
            if (current < eventIntroModels.size()) {
                viewPager.setCurrentItem(current);
            } else {
                dialog.dismiss();
            }
        });

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(eventIntroModels);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                dotsLayout.removeAllViews();
                addBottomDots(position,dotsLayout);

                if (position == eventIntroModels.size() - 1) {
                    txtNext.setText("Start");
                } else {
                    txtNext.setText("Next");
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }

        });

        viewPager.setPageTransformer(false, (v, position) -> {
            v.setTranslationX(v.getWidth() * -position);

            if(position <= -1.0F || position >= 1.0F) {
                v.setAlpha(0.0F);
            } else if( position == 0.0F ) {
                v.setAlpha(1.0F);
            } else {
                // position is between -1.0F & 0.0F OR 0.0F & 1.0F
                v.setAlpha(1.0F - Math.abs(position));
            }
        });

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        dialog.show();
    }

    private void addBottomDots(int currentPage,LinearLayout dotsLayout) {

        dots = new TextView[4];

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorWhiteLight));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.white_pure));
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.back_img){
            finish();
        }
        if (view.getId()==R.id.btnOpinion){
            selectedTag="2";
            selectedFilterId="0";

           /* if (mSocket!=null)
                mSocket.off("res");*/

            btnOpinion.setBackgroundResource(R.drawable.btn_opinio_selected);
            btnOpinion.setTextColor(getResources().getColor(R.color.white_pure));
            btnTrading.setBackgroundResource(R.drawable.transparent_view);
            btnTrading.setTextColor(getResources().getColor(R.color.black_pure));
            btnPoll.setBackgroundResource(R.drawable.transparent_view);
            btnPoll.setTextColor(getResources().getColor(R.color.black_pure));

            layTrading.setVisibility(View.GONE);
            layOpinion.setVisibility(View.VISIBLE);
            layPoll.setVisibility(View.GONE);
            if (!isEventTab){
                getOpinionData();
            }
            if (currentFragment!=null){
                if (currentFragment instanceof LiveEventFragment){
                    ((LiveEventFragment) currentFragment).changeTags(selectedTag);
                }
                if (currentFragment instanceof ClosedEventFragment){
                    ((ClosedEventFragment) currentFragment).changeTags(selectedTag);
                }
            }

           /* if (!preferences.getPrefBoolean(PrefConstant.is_opinion_intro)){
                showIntroDialog();
            }*/
        }
        if (view.getId()==R.id.btnPoll){
            selectedTag="3";
            selectedFilterId="0";
            btnPoll.setBackgroundResource(R.drawable.btn_opinio_selected);
            btnPoll.setTextColor(getResources().getColor(R.color.white_pure));
            btnOpinion.setBackgroundResource(R.drawable.transparent_view);
            btnOpinion.setTextColor(getResources().getColor(R.color.black_pure));
            btnTrading.setBackgroundResource(R.drawable.transparent_view);
            btnTrading.setTextColor(getResources().getColor(R.color.black_pure));
            layPoll.setVisibility(View.VISIBLE);
            layTrading.setVisibility(View.GONE);
            layOpinion.setVisibility(View.GONE);
            if (!isEventTab){
                getPollData();
            }
            if (currentFragment!=null){
                if (currentFragment instanceof LiveEventFragment){
                    ((LiveEventFragment) currentFragment).changeTags(selectedTag);
                }
                if (currentFragment instanceof ClosedEventFragment){
                    ((ClosedEventFragment) currentFragment).changeTags(selectedTag);
                }
            }

          /*  if (mSocket!=null)
                mSocket.off("res");*/
            /*if (!preferences.getPrefBoolean(PrefConstant.is_poll_intro)){
                showIntroDialog();
            }*/
        }
        if (view.getId()==R.id.btnTrading){
            selectedTag="1";
            selectedFilterId="0";
            btnOpinion.setBackgroundResource(R.drawable.transparent_view);
            btnOpinion.setTextColor(getResources().getColor(R.color.black_pure));
            btnTrading.setBackgroundResource(R.drawable.btn_opinio_selected);
            btnTrading.setTextColor(getResources().getColor(R.color.white_pure));
            btnPoll.setBackgroundResource(R.drawable.transparent_view);
            btnPoll.setTextColor(getResources().getColor(R.color.black_pure));

            layPoll.setVisibility(View.GONE);
            layTrading.setVisibility(View.VISIBLE);
            layOpinion.setVisibility(View.GONE);

            if (!isEventTab){
                getTradingData();
            }

            if (currentFragment!=null){
                if (currentFragment instanceof LiveEventFragment){
                    ((LiveEventFragment) currentFragment).changeTags(selectedTag);
                }
                if (currentFragment instanceof ClosedEventFragment){
                    ((ClosedEventFragment) currentFragment).changeTags(selectedTag);
                }
            }

            if (selectedTag.equalsIgnoreCase("1")){
                listener();
            }

        }

        if (view.getId()==R.id.btnTradeX){
            selectedTag="1";
            selectedFilterId="0";
            btnTradeX.setBackgroundResource(R.drawable.primary_fill_border);
            btnTradeX.setTextColor(getResources().getColor(R.color.white_pure));
            btnStocks.setBackgroundResource(R.drawable.transparent_view);
            btnStocks.setTextColor(getResources().getColor(R.color.black_pure));

            layPlayDetail.setVisibility(View.VISIBLE);

            layStocks.setVisibility(View.GONE);
            if (!isEventTab){
                getTradingData();
            }


            if (currentFragment!=null){
                if (currentFragment instanceof LiveEventFragment){
                    ((LiveEventFragment) currentFragment).changeTags(selectedTag);
                }
                if (currentFragment instanceof ClosedEventFragment){
                    ((ClosedEventFragment) currentFragment).changeTags(selectedTag);
                    if (selectedTag.equalsIgnoreCase("1")){
                        layPlayDetail.setVisibility(View.GONE);
                        layTrading.setVisibility(View.GONE);
                    }else {
                        layPlayDetail.setVisibility(View.VISIBLE);
                        layTrading.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        /*if (view.getId()==R.id.btnStocks){
            selectedTag="4";
            btnTradeX.setBackgroundResource(R.drawable.transparent_view);
            btnTradeX.setTextColor(getResources().getColor(R.color.black));
            btnStocks.setBackgroundResource(R.drawable.primary_fill_border);
            btnStocks.setTextColor(getResources().getColor(R.color.white));

            layPlayDetail.setVisibility(View.GONE);
            // layTrading.setVisibility(View.GONE);


            if (!isEventTab){
                getStocksData();
                layStocks.setVisibility(View.VISIBLE);

            }

            if (currentFragment!=null){
                if (currentFragment instanceof LiveEventFragment){
                    ((LiveEventFragment) currentFragment).changeTags(selectedTag);
                }
                if (currentFragment instanceof ClosedEventFragment){
                    ((ClosedEventFragment) currentFragment).changeTags(selectedTag);
                }
            }
        }*/

        if (view.getId()==R.id.layPlay){
            isEventTab=false;

            layPlay.setBackgroundColor(mContext.getResources().getColor(R.color.green_pure));;
            layEvents.setBackgroundColor(mContext.getResources().getColor(R.color.blackPrimary));

            layPlayDetail.setVisibility(View.VISIBLE);
            layEventDetail.setVisibility(View.GONE);

            if (selectedTag.equalsIgnoreCase("1")){
                getTradingData();

            }
            else if (selectedTag.equalsIgnoreCase("2")){
                getOpinionData();
            }
            else if (selectedTag.equalsIgnoreCase("3")){
                getPollData();
            }
            /*else if (selectedTag.equalsIgnoreCase("4")){
                getStocksData();
                layPlayDetail.setVisibility(View.GONE);
                layStocks.setVisibility(View.VISIBLE);
            }*/

        }
        if (view.getId()==R.id.layEvents){
            isEventTab=true;

            layEvents.setBackgroundColor(mContext.getResources().getColor(R.color.green_pure));
            layPlay.setBackgroundColor(mContext.getResources().getColor(R.color.blackPrimary));

            layPlayDetail.setVisibility(View.GONE);
            layEventDetail.setVisibility(View.VISIBLE);
            layStocks.setVisibility(View.GONE);

            if (selectedTag.equalsIgnoreCase("1")){
                layPlayDetail.setVisibility(View.GONE);
                layStocks.setVisibility(View.GONE);

            }/*else if (selectedTag.equalsIgnoreCase("4")){
                layStocks.setVisibility(View.GONE);
                layEventDetail.setVisibility(View.VISIBLE);
            }*/
           /* if (mSocket!=null)
                mSocket.off("res");*/
            stopSpeech();
            layScore.setVisibility(View.GONE);

            if (!isEventTabLoad){
                initEventTabs();
            }else {
                /*if (currentFragment!=null){
                    if (currentFragment instanceof LiveEventFragment){
                        ((LiveEventFragment) currentFragment).changeTags(selectedTag);
                    }
                    if (currentFragment instanceof ClosedEventFragment){
                        ((ClosedEventFragment) currentFragment).changeTags(selectedTag);
                    }
                }*/
            }
        }
        if (view.getId()==R.id.imgInfo){
            //showIntroDialog();
            getTNCData();
        }
    }

    public void getTNCData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("game_id", "11");
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

    private class EventAdapter extends FragmentStateAdapter {
        private final Context myContext;
        int totalTabs;

        public EventAdapter(@NonNull FragmentActivity fragmentActivity, Context mContext) {
            super(fragmentActivity);
            this.myContext = mContext;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    liveEventFragment=LiveEventFragment.newInstance(selectedTag);
                    currentFragment=liveEventFragment;
                    return liveEventFragment;
                case 1:
                    closedEventFragment=ClosedEventFragment.newInstance(selectedTag);
                    return closedEventFragment;

                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }

    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        private final ArrayList<EventIntroModel> eventIntroModels;
        MyViewPagerAdapter(ArrayList<EventIntroModel> eventIntroModels) {
            this.eventIntroModels=eventIntroModels;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {


            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            View view = layoutInflater.inflate(R.layout.event_slider_1, container, false);//layouts[position]

            ImageView imgIntro=view.findViewById(R.id.imgIntro);
            TextView txtSubTitle=view.findViewById(R.id.txtSubTitle);
            TextView txtDesc=view.findViewById(R.id.txtDesc);
            TextView txtTitle=view.findViewById(R.id.txtTitle);

            EventIntroModel model=eventIntroModels.get(position);
            txtTitle.setText(model.getTitle());
            txtSubTitle.setText(model.getSubtitle());
            txtDesc.setText(model.getDescription());
            imgIntro.setImageResource(model.getImage());
            //ImageView slide_img = (ImageView) view.findViewById(R.id.slide_img);

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return eventIntroModels.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

}