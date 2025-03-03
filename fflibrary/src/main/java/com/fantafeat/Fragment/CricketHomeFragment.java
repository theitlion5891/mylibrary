package com.fantafeat.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.fantafeat.Activity.AddDepositActivity;
import com.fantafeat.Activity.ContestListActivity;
import com.fantafeat.Activity.FullImageActivity;
import com.fantafeat.Activity.GameListActivity;
import com.fantafeat.Activity.HelpDeskActivity;
import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.Activity.RummyContestActivity;
import com.fantafeat.Activity.TOPActivity;
import com.fantafeat.Activity.WebViewActivity;
import com.fantafeat.Adapter.MatchFullListAdapter;
import com.fantafeat.Adapter.MatchListAdapter;
import com.fantafeat.Adapter.OfferBannerAdapter;
import com.fantafeat.BuildConfig;
import com.fantafeat.Model.AvailableQtyModel;
import com.fantafeat.Model.BannerModel;
import com.fantafeat.Model.EventModel;
import com.fantafeat.Model.Games;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.PopUpBannerModel;
import com.fantafeat.Model.SportsModel;
import com.fantafeat.Model.StatusUserListModel;
import com.fantafeat.Model.UpdateModel;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.FragmentUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.MusicManager;
import com.fantafeat.util.PrefConstant;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;


public class CricketHomeFragment extends BaseFragment {

    private RecyclerView cricket_list;
    //private ViewPager2 mHomeOffer;
    private OfferBannerAdapter adapter;
    // private LinearLayout layNoData;
    // private MatchListAdapter matchListAdapter;
    List<MatchModel> matchModelList;
    private ArrayList<MatchModel> matchModelListSafe;
    private ArrayList<MatchModel> tournamentMatchList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MatchFullListAdapter adapter1;
    private BottomSheetDialog dialog;
    private String sport_id = ""/*, linkData = ""*/;
    public MatchViewModal viewModel;
    private Timer timerTopAds;
    private int pageTopAds = 0;
  //  private Fragment fragment;
    //private NestedScrollView scroll;
    private ArrayList<EventModel> list;
    private boolean isBanner = false;
    private UpdateModel updateModel;
   // private Socket mSocket= null;

    private List<Games> gameList;
    private String tradingDialogId="";
    private EventModel dialogEventModel;
    private TextView txtMaxQtyAtPrice,txtQtyAtPrice,txtTrades;
    private ArrayList<AvailableQtyModel> yesList=new ArrayList<>();
    private ArrayList<AvailableQtyModel> noList=new ArrayList<>();
    private String tradeTitle="Live Trades";
    private long lastUpdateTime=0;

    public CricketHomeFragment() {
    }

   /* public CricketHomeFragment(Fragment fragment) {
       // this.sport_id = id;
       // this.fragment=fragment;
    }*/

    public static CricketHomeFragment newInstance(String id, String linkData) {//, Fragment fragment
        CricketHomeFragment myFragment = new CricketHomeFragment();//fragment
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("linkData", linkData);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sport_id = getArguments().getString("id");
            //linkData = getArguments().getString("linkData");
        }

        //viewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(MatchViewModal.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cricket_home, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        isBg(false);

        boolean isApiCall=false;

        if (BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE)){
            if (!ConstantUtil.play_store_app) {
                isApiCall=true;
            }
        }
        else {
            isApiCall=true;
        }
        /*if (isApiCall){
            if (mSocket!=null){
                if (!mSocket.connected())
                    mSocket.connect();
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("en", "tradingData");
                    LogUtil.e("resp","tradingData: "+obj);
                    mSocket.emit("req", obj);
                    listener();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }*/
        //LogUtil.e("resp","Call");
    }

    private void listener() {
        /*if (mSocket!=null){
            mSocket.off("res");
            mSocket.on("res", args -> {
                //Log.e("resp","LiveScore: "+args[0]);
                if (args!=null){
                    //Log.e("resp","LiveScore: "+args[0]);
                    try {
                        JSONObject object=new JSONObject(args[0].toString());
                        if (object!=null){
                            if (object.optString("en").equalsIgnoreCase("trade_unmatch_count")){
                               // Log.e("resp","trade_unmatch_count: "+args[0]);
                                if (!TextUtils.isEmpty(tradingDialogId) && dialogEventModel!=null && dialog!=null && dialog.isShowing()){
                                    JSONArray data=object.optJSONArray("data");
                                    if (data!=null){
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

                                                //Collections.sort(yesList, new TradingUp());
                                                //Collections.sort(noList, new TradingUp());

                                                ((Activity)mContext).runOnUiThread(() -> {
                                                    if (txtTrades!=null)
                                                        txtTrades.setText(CustomUtil.coolFormat(mContext,obj.optJSONObject("contest_data").optString("total_trades"))+" Trades");

                                                    if (adapter1!=null && matchModelList!=null){
                                                        //ArrayList<EventModel> arrayList=tradingAdapter.getList();
                                                        for (int j = 0; j < matchModelList.size(); j++) {
                                                            MatchModel matchModel=matchModelList.get(j);
                                                            if (matchModel.getMatchDisplayType()==4){
                                                                for (int k = 0; k < matchModel.getTradingList().size(); k++) {
                                                                    EventModel model=matchModel.getTradingList().get(k);
                                                                    if (!TextUtils.isEmpty(model.getId())){
                                                                        if (model.getId().equalsIgnoreCase(contest_id)){
                                                                            model.setTotalTrades(obj.optJSONObject("contest_data").optString("total_trades"));
                                                                            adapter1.notifyDataSetChanged();
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                });

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }*/
    }

    @Override
    public void initControl(View view) {

       // mSocket=MyApp.getInstance().getSocketInstance();

        matchModelList = new ArrayList<>();
        matchModelListSafe = new ArrayList<>();
        tournamentMatchList = new ArrayList<>();

        cricket_list = view.findViewById(R.id.cricket_list);
        //layNoData = view.findViewById(R.id.layNoData);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_cricket);

        updateModel = preferences.getUpdateMaster();
        gameList=new ArrayList<>();
//        scroll = view.findViewById(R.id.scroll);

        // mHomeOffer = view.findViewById(R.id.home_offer);

       /* scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch( View v, MotionEvent event ) {
                switch ( event.getAction( ) ) {
                    case MotionEvent.ACTION_SCROLL:
                    case MotionEvent.ACTION_MOVE:
                        Log.e( "SCROLL", "ACTION_SCROLL" );
                       // ((HomeActivityFragment)fragment).enterAnim();
                        break;
                    case MotionEvent.ACTION_DOWN:
                        Log.e( "SCROLL", "ACTION_DOWN" );
                        //((HomeActivityFragment)fragment).enterAnim();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        Log.e( "SCROLL", "SCROLL_STOP" );
                       // ((HomeActivityFragment)fragment).leaveAnim();
                        break;
                }
                return false;
            }
        });*/

        adapter1 = new MatchFullListAdapter(mContext, matchModelList, new MatchFullListAdapter.MatchListener() {
            /*StorySheet dialog = new StorySheet(mContext, position, list, CricketHomeFragment.this);
                dialog.show(getChildFragmentManager(), StorySheet.TAG);
*/@Override
            public void onStatusClick(ArrayList<StatusUserListModel> list, int position) {

                isBg(true);
            }

            @Override
            public void onBannerClick(BannerModel bannerModel) {
                bannerAction(bannerModel);
            }

            @Override
            public void onMatchClick(MatchModel matchModel) {

            }

            @Override
            public void onTradeClick(EventModel eventModel) {
                getOpinionCnt(eventModel);
            }
        });
        cricket_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        cricket_list.setHasFixedSize(true);
        cricket_list.setAdapter(adapter1);
        ViewCompat.setNestedScrollingEnabled(cricket_list, true);


        /*if (!TextUtils.isEmpty(updateModel.getIs_story_avaialble()) && updateModel.getIs_story_avaialble().equalsIgnoreCase("yes") &&
                !BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE))
            getStatusUserList(true);
        else if (!TextUtils.isEmpty(updateModel.getIs_trade_available()) && updateModel.getIs_trade_available().equalsIgnoreCase("yes") &&
                !BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE))
            getTradingData();
        else
            getData();*/

        getData();

    }

    private void getStatusUserList(boolean isCall) {
        // mainMatchList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("sports_id", sport_id);//preferences.getSports().get(0).getId()
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean showLoader = swipeRefreshLayout == null || !swipeRefreshLayout.isRefreshing();
        HttpRestClient.postJSON(mContext, false, ApiManager.STORY_USER_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "getStatusUserList: " + responseBody);
                if (responseBody.optBoolean("status")) {

                    JSONArray data = responseBody.optJSONArray("data");
                    ArrayList<StatusUserListModel> statusUserListModels = new ArrayList<>();

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.optJSONObject(i);
                        StatusUserListModel model = gson.fromJson(obj.toString(), StatusUserListModel.class);
                        statusUserListModels.add(model);
                    }

                    boolean isData=false;

                    for (int i = 0; i < matchModelList.size(); i++) {
                        MatchModel matchModel=matchModelList.get(i);
                        if (matchModel.getMatchDisplayType()==5){
                            isData=true;
                        }
                    }

                    if (isData){
                        matchModelList.remove(0);
                        //mainMatchList.remove(2);
                    }

                    MatchModel matchModel = new MatchModel();
                    matchModel.setMatchDisplayType(5);
                    // Collections.sort(statusUserListModels, new UnreadCountUp());
                    matchModel.setStatusUserListModels(statusUserListModels);
                    matchModelList.add(0, matchModel);//

                    adapter1.notifyDataSetChanged();

                }
                if (!TextUtils.isEmpty(updateModel.getIs_trade_available()) && updateModel.getIs_trade_available().equalsIgnoreCase("yes") &&
                        !BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE))
                    getTradingData();
               /* if (!TextUtils.isEmpty(updateModel.getIs_trade_available()) && updateModel.getIs_trade_available().equalsIgnoreCase("yes"))
                    getTradingData();
                else
                    getData();*/
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                if (!TextUtils.isEmpty(updateModel.getIs_trade_available()) && updateModel.getIs_trade_available().equalsIgnoreCase("yes") &&
                        !BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE))
                    getTradingData();
                /*if (isCall) {
                    if (!TextUtils.isEmpty(updateModel.getIs_trade_available()) && updateModel.getIs_trade_available().equalsIgnoreCase("yes"))
                        getTradingData();
                    else
                        getData();
                }*/
            }
        });
    }

    private void getData() {
        // matchModelList.clear();
        matchModelListSafe = new ArrayList<>();
        tournamentMatchList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject();
        try {
            // Log.e(TAG, "onSuccessResult: "+ preferences.getUserModel().getId()+"   "+sport_id);

            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("sports_id", sport_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean show_dialog = true;
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            show_dialog = false;
        }
        HttpRestClient.postJSON(mContext, show_dialog, ApiManager.MATCH_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                //Log.e(TAG, "onSuccessResult: "+responseBody.toString() );
                lastUpdateTime=System.currentTimeMillis();
                matchModelList.clear();
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                /*if (matchModelList!=null){
                    matchModelList.clear();
                }else {
                    matchModelList=new ArrayList<>();
                }*/
                if (responseBody.optBoolean("status")) {

                    //Log.e(TAG, "onSuccessResult: "+ responseBody.optJSONArray("data") );
                    JSONArray array = responseBody.optJSONArray("data");

                    Log.e(TAG, "onSuccessResult: " + array);

                    JSONArray top_banner_data = responseBody.optJSONArray("top_banner_data");
                    //LogUtil.e(TAG, "onSuccessResultopbnerrt: " + top_banner_data.toString());
                    if (top_banner_data != null && !TextUtils.isEmpty(updateModel.getIs_rectangle_banner_avaialble()) &&
                            updateModel.getIs_rectangle_banner_avaialble().equalsIgnoreCase("yes")) {
                        final ArrayList<BannerModel> bannerModels = new ArrayList<>();
                        for (int i = 0; i < top_banner_data.length(); i++) {
                            try {
                                JSONObject data = top_banner_data.getJSONObject(i);
                                BannerModel bannerModel = gson.fromJson(data.toString(), BannerModel.class);
                                bannerModels.add(bannerModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        isBanner = true;
                        MatchModel matchModel = new MatchModel();
                        matchModel.setMatchDisplayType(1);
                        matchModel.setBannerModels(bannerModels);
                        matchModelList.add(0, matchModel);//0,

                        //adapter1.notifyItemChanged(0);
                        // preferences.setHomeBanner(bannerModels);
                        //displayOffer();
                    }

                    if (array != null && array.length() > 0) {

                        for (int i = 0; i < array.length(); i++) {
                            try {
                                JSONObject data = array.getJSONObject(i);
                                MatchModel matchModel = gson.fromJson(data.toString(), MatchModel.class);
                                matchModel.setMatchDisplayType(5);
                                if (!TextUtils.isEmpty(updateModel.getSeries_id_banner()) &&
                                        matchModel.getLeagueId().equalsIgnoreCase(updateModel.getSeries_id_banner())) {
                                    if (matchModel.getDisplayIsSafe().equalsIgnoreCase("Yes")) {
                                        tournamentMatchList.add(matchModel);
                                    }
                                } else {
                                    if (matchModel.getDisplayIsSafe().equalsIgnoreCase("Yes")) {
                                        matchModel.setMatchDisplayType(7);
                                        matchModelListSafe.add(matchModel);
                                        //mainMatchList.add(matchModel);
                                    }
                                }
                                // matchModelList.add(matchModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter1.notifyDataSetChanged();
                        viewModel.setMatchModelList(matchModelList);
                    }
                    /*else {
                        MatchModel matchModel=new MatchModel();
                        matchModel.setMatchDisplayType(3);
                        matchModelList.add(matchModel);
                        adapter1.notifyDataSetChanged();
                    }*/


                    JSONObject popup_banner_data = responseBody.optJSONObject("popup_banner_data");

                   /* if (ConstantUtil.is_game_test){
                        try {
                            popup_banner_data.put("banner_action","deposit");
                            popup_banner_data.put("match_id","1000");
                            popup_banner_data.put("id","1000");
                            popup_banner_data.put("display_banner_count","10");
                            popup_banner_data.put("banner_img","https://dash.ezybetting.com/batball11new/myAppImages/documents/home_banner_image_20241129_172939.jpg");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }*/

                    LogUtil.e(TAG, "popup_banner_data: " + popup_banner_data);
                    if (popup_banner_data != null && !ConstantUtil.IS_POPUP_BANNER_SHOW) {
                        PopUpBannerModel popUpBannerModel = gson.fromJson(popup_banner_data.toString(), PopUpBannerModel.class);
                        int count = CustomUtil.convertInt(popUpBannerModel.getDisplayBannerCount());
                        // LogUtil.e("popresp", "onSuccessResult: " + count+"   "+preferences.getPrefInt(PrefConstant.POPUP_BANNER_COUNT));
                        if (!preferences.getPrefString(PrefConstant.POPUP_BANNER_ID).equals(popUpBannerModel.getId())) {
                            LogUtil.e("popresp", "onSuccessResult: cnt");
                            preferences.setPref(PrefConstant.POPUP_BANNER_COUNT, 0);
                        }

                        if (count != preferences.getPrefInt(PrefConstant.POPUP_BANNER_COUNT)) {
                            ConstantUtil.IS_POPUP_BANNER_SHOW = true;
                            showPopupBanner(popUpBannerModel, sport_id);
                        }
                    }


                    //matchListAdapter.notifyDataSetChanged();
                }

                setData();
                //checkData();
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                setData();
            }

        });
    }

    public void getRummyData(){
        try {
            JSONObject object=new JSONObject();
            object.put("user_id",preferences.getUserModel().getId());

            LogUtil.e(TAG,object.toString());
            HttpRestClient.postJSONNormal(mContext, true, ApiManager.gameList, object, new GetApiResult() {
                        @Override
                        public void onSuccessResult(JSONObject responseBody, int code) {
                            LogUtil.e("TAG", "getGameListHomeActivity: " + responseBody.toString());
                            JSONArray data=responseBody.optJSONArray("data");
                            if (data!=null && data.length()>0){
                                gameList.clear();
                                for (int i=0;i<data.length();i++){
                                    JSONObject obj=data.optJSONObject(i);
                                    Games games=gson.fromJson(obj.toString(),Games.class);
                                    if (games.getGameName().equalsIgnoreCase("Rummy")){
                                        startActivity(new Intent(mContext, RummyContestActivity.class).putExtra("gameData",games));
                                        break;
                                    }

                                }
                            }
                        }

                        @Override
                        public void onFailureResult(String responseBody, int code) {

                        }
                    }
            );
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void getTradingData() {
        list = new ArrayList<>();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSONNormal(mContext, false, ApiManager.TRADING_CONTEST_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e(TAG, "getOpinionData : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONArray data = responseBody.optJSONArray("data");
                    int maxCnt = 0;
                    if (data != null && data.length() > 0) {

                       /* int posT=-1;
                        for (int i = 0; i <matchModelList.size(); i++) {
                            if (matchModelList.get(i).getMatchDisplayType()==2 &&
                                    matchModelList.get(i).getMatchTitle().equalsIgnoreCase("Trending Trades")){
                                posT=i;
                            }
                        }

                        if (posT!=-1){
                            matchModelList.remove(posT);
                            matchModelList.remove(posT);
                        }*/

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.optJSONObject(i);
                            EventModel model = gson.fromJson(obj.toString(), EventModel.class);
                            if (maxCnt < 5)
                                list.add(model);
                            maxCnt++;
                        }

                        boolean isData=false;

                        for (int i = 0; i < matchModelList.size(); i++) {
                            MatchModel matchModel=matchModelList.get(i);
                            if (matchModel.getMatchDisplayType()==4){
                                isData=true;
                            }
                        }

                        if (isData){
                            matchModelList.remove(1);
                            //mainMatchList.remove(2);
                        }

                     /*   MatchModel matchModel1 = new MatchModel();
                        matchModel1.setMatchDisplayType(2);
                        matchModel1.setMatchTitle(tradeTitle);
                        matchModelList.add(matchModel1);//pos,*/

                        MatchModel matchModel = new MatchModel();
                        matchModel.setMatchDisplayType(4);
                        matchModel.setMatchTitle(tradeTitle);
                        matchModel.setTradingList(list);
                        matchModelList.add(1,matchModel);//(pos+1),

                        adapter1.notifyDataSetChanged();

                       /* adapter1.notifyItemChanged(pos);
                        adapter1.notifyItemChanged(pos+1);*/
                    }
                }
                //getData();

            }

            @Override
            public void onFailureResult(String responseBody, int code) {
            }
        });
    }

    private void displayOffer() {

        //MyApp.imageBase=preferences.getPrefString(PrefConstant.IMAGE_BASE);
        // LogUtil.e("TAG", "onBindViewHolder  dynamic_key 2: " + MyApp.imageBase);
        MyApp.setImageBaseUrl();

        if (preferences.getHomeBanner() != null && preferences.getHomeBanner().size() > 0) {
            adapter = new OfferBannerAdapter(getContext(), viewModel, preferences);
           /* mHomeOffer.setAdapter(adapter);
            mHomeOffer.setClipChildren(false);
            mHomeOffer.setOffscreenPageLimit(2);
            mHomeOffer.setPageTransformer(new MarginPageTransformer(40));*/
            //pageSwitcher();
        } else {
            //  mHomeOffer.setVisibility(View.GONE);
        }
    }

    public void showPopupBanner(PopUpBannerModel popUpBannerModel, String spId) {
        List<SportsModel> sportsModelList = preferences.getSports();
        List<SportsModel> updatedList = new ArrayList<>();

        for (int i = 0; i < sportsModelList.size(); i++) {
            SportsModel sportsModel = sportsModelList.get(i);
            /*if (ConstantUtil.is_game_test){
                if (sportsModel.getId().equalsIgnoreCase("7")){
                    sportsModel.setTab_1_min("1");
                    sportsModel.setTab_1_max("5");

                    sportsModel.setTab_2_min("1");
                    sportsModel.setTab_2_max("5");

                    sportsModel.setTab_3_min("1");
                    sportsModel.setTab_3_max("5");

                    sportsModel.setTab_4_min("1");
                    sportsModel.setTab_4_max("5");

                    sportsModel.setTeam_1_max_player("6");
                }
            }*/
            if (sportsModel.getId().equalsIgnoreCase(spId)) {
                sportsModel.setPopupShow(true);
            }
            updatedList.add(sportsModel);
        }
        preferences.setSports(updatedList);
        boolean showBanner = !BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE) ||
                (BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE) &&
                        popUpBannerModel.getMatchId().equalsIgnoreCase("6"));

        if (showBanner) {
            final Dialog dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.dialog_popup_banner);
            ImageView banner_img = (ImageView) dialog.findViewById(R.id.banner_img);
            ImageView close = (ImageView) dialog.findViewById(R.id.close);
            WebView webView = (WebView) dialog.findViewById(R.id.webView);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((HomeActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            float widthPixels = (float) (displayMetrics.widthPixels * 0.8);
            float heightPixels = (float) (widthPixels * 1.9);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = (int) widthPixels;
            lp.height = (int) heightPixels;

            if (popUpBannerModel.getBannerAction().equals("webview")) {
                webView.setVisibility(View.VISIBLE);
                banner_img.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(popUpBannerModel.getBannerWebUrl())) {
                    webView.loadUrl(popUpBannerModel.getBannerWebUrl());
                }
            } else {
                webView.setVisibility(View.GONE);
                banner_img.setVisibility(View.VISIBLE);
                banner_img.setMinimumHeight((int) heightPixels);
                banner_img.setMinimumWidth((int) widthPixels);
                CustomUtil.loadImageWithGlide(mContext, banner_img, ApiManager.POPUP_BANNER_IMG, popUpBannerModel.getBannerImg(), R.drawable.popup_placeholder);
           /* Glide.with(mContext)
                    .load(ApiManager.POPUP_BANNER_IMG + popUpBannerModel.getBannerImg())
                    .placeholder(R.drawable.popup_placeholder)
                    .error(R.drawable.popup_placeholder)
                    .into(banner_img);*/
                if (popUpBannerModel.getBannerAction().equals("maintenance")) {
                    close.setVisibility(View.GONE);
                } else {
                    close.setVisibility(View.VISIBLE);
                }
            }


            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.show();
            dialog.getWindow().setAttributes(lp);

            close.setOnClickListener(view -> {
                dialog.dismiss();
                preferences.setPref(PrefConstant.POPUP_BANNER_ID, popUpBannerModel.getId());
                preferences.setPref(PrefConstant.POPUP_BANNER_COUNT, preferences.getPrefInt(PrefConstant.POPUP_BANNER_COUNT) + 1);

            });

            banner_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                /*preferences.setPref(PrefConstant.POPUP_BANNER_ID, popUpBannerModel.getId());
                preferences.setPref(PrefConstant.POPUP_BANNER_COUNT, preferences.getPrefString(PrefConstant.POPUP_BANNER_ID)+1);*/
                    popupBannerClick(popUpBannerModel, dialog);
                }
            });
        }
    }

    private void popupBannerClick(PopUpBannerModel popUpBannerModel, Dialog dialog) {
        dialog.dismiss();
        if (popUpBannerModel.getBannerAction().equalsIgnoreCase("deposit")) {
            Intent intent = new Intent(mContext, AddDepositActivity.class);
            intent.putExtra("isJoin", false);
            intent.putExtra("depositAmt", popUpBannerModel.getMatchId());
            startActivity(intent);
        }
        else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("Contest")) {
            /*if (!popUpBannerModel.getSport_id().equalsIgnoreCase(sport_id)) {
                if (getParentFragment() != null && fragment instanceof HomeActivityFragment) {
                    ((HomeActivityFragment) fragment).changeSpotsTab(popUpBannerModel.getSport_id(),
                            popUpBannerModel.getMatchId(), popUpBannerModel.getContestId());
                }
            }
            else {*/
                boolean isContestFind = false;
                for (MatchModel matchModel : CustomUtil.emptyIfNull(matchModelListSafe)) {
                    //LogUtil.d("matchId","contestId Home :  "+bannerModel.getContestId());
                    if (popUpBannerModel.getMatchId().equalsIgnoreCase(matchModel.getId())) {
                        matchModel.setConDisplayType("1");
                        preferences.setMatchModel(matchModel);
                        isContestFind = true;
                        Intent intent = new Intent(mContext, ContestListActivity.class);
                        intent.putExtra("link_contest_id", popUpBannerModel.getContestId());
                        mContext.startActivity(intent);
                        break;
                    }
                }
                if (!isContestFind) {
                    for (MatchModel matchModel : CustomUtil.emptyIfNull(tournamentMatchList)) {
                        // LogUtil.d("matchId","contestId Home :  "+bannerModel.getContestId() +"  "+bannerModel.getMatchId());
                        if (popUpBannerModel.getMatchId().equalsIgnoreCase(matchModel.getId())) {
                            //LogUtil.d("matchId","contestId Home :  "+bannerModel.getContestId() +"  "+bannerModel.getMatchId());
                            matchModel.setConDisplayType("1");
                            preferences.setMatchModel(matchModel);
                            Intent intent = new Intent(mContext, ContestListActivity.class);
                            intent.putExtra("link_contest_id", popUpBannerModel.getContestId());
                            mContext.startActivity(intent);

                            break;
                        }
                    }
                }
                /*for (MatchModel matchModel : CustomUtil.emptyIfNull(matchModelListSafe)) {
                    if (bannerModel.getMatchId().equalsIgnoreCase(matchModel.getId())) {
                        matchModel.setConDisplayType("1");
                        preferences.setMatchModel(matchModel);
                        new FragmentUtil().addFragment((FragmentActivity) mContext,
                                R.id.fragment_container,
                                new MatchContestFragment(0),
                                ((HomeActivity) mContext).fragmentTag(7),
                                FragmentUtil.ANIMATION_TYPE.CUSTOM);
                        break;
                    }
                }*/
            //}
            /*for (MatchModel matchModel : CustomUtil.emptyIfNull((viewModel.getMatchModelList()))) {
                if (popUpBannerModel.getMatchId().equalsIgnoreCase(matchModel.getId())) {
                    preferences.setMatchModel(matchModel);
                    Intent intent = new Intent(mContext, ContestListActivity.class);
                    mContext.startActivity(intent);
                    break;
                }
            }*/
        }
        else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("web_view")) {

            startActivity(new Intent(mContext, WebViewActivity.class)
                    .putExtra(ConstantUtil.WEB_TITLE, "")
                    .putExtra(ConstantUtil.WEB_URL, popUpBannerModel.getMatchId()));
        }
        else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("fullBanner")) {
            startActivity(new Intent(mContext, FullImageActivity.class)
                    .putExtra(ConstantUtil.FULL_IMAGE_PATH, popUpBannerModel.getMatchId()));
        }
        else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("whatsapp")) {
            String url = "https://api.whatsapp.com/send?phone=" + popUpBannerModel.getMatchId();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
        else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("twitter")) {
            String url = ApiManager.tweet_url + popUpBannerModel.getMatchId();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
        else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("facebook")) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + popUpBannerModel.getMatchId()));//2216938415251012
                startActivity(intent);
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/" + popUpBannerModel.getContestId())));//batball11
            }
        }
        else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("telegram")) {
            String url = "https://t.me/" + popUpBannerModel.getMatchId();//batballeleven
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
        else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("telegram_support")) {
            try {
                String url = ApiManager.support_username + popUpBannerModel.getMatchId();//2122548406
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setPackage("org.telegram.messenger");
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("instagram")) {
            try {
                String url = ApiManager.instagram_id + popUpBannerModel.getMatchId();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("game")) {
            if (BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE)){
                getRummyData();
            }else{
                startActivity(new Intent(mContext, GameListActivity.class).putExtra("game_id", popUpBannerModel.getMatchId()));
            }
        }
        else if (popUpBannerModel.getBannerAction().equalsIgnoreCase(ConstantUtil.LINK_PREFIX_TRADING)) {
            startActivity(new Intent(mContext, TOPActivity.class)
                    .putExtra("contest_id", popUpBannerModel.getMatchId())
                    .putExtra("conType", ConstantUtil.LINK_PREFIX_TRADING)
            );
        }
    }

    public void bannerAction(BannerModel popUpBannerModel) {
        if (popUpBannerModel.getBannerAction().equalsIgnoreCase("deposit")) {
            Intent intent = new Intent(mContext, AddDepositActivity.class);
            intent.putExtra("isJoin", false);
            intent.putExtra("depositAmt", popUpBannerModel.getBannerMatchId());
            startActivity(intent);
        } else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("Contest")) {
            /*if (!popUpBannerModel.getSport_id().equalsIgnoreCase(sport_id)) {
                if (getParentFragment() != null && fragment instanceof HomeActivityFragment) {
                    ((HomeActivityFragment) fragment).changeSpotsTab(popUpBannerModel.getSport_id(),
                            popUpBannerModel.getBannerMatchId(), popUpBannerModel.getBanner_contest_id());
                }
            } else {*/
                boolean isContestFind = false;
                for (MatchModel matchModel : CustomUtil.emptyIfNull(matchModelListSafe)) {

                    if (popUpBannerModel.getBannerMatchId().equalsIgnoreCase(matchModel.getId())) {
                        //  LogUtil.d("matchId","contestId Home :  "+matchModel.getId());
                        matchModel.setConDisplayType("1");
                        preferences.setMatchModel(matchModel);
                        isContestFind = true;
                        Intent intent = new Intent(mContext, ContestListActivity.class);
                        intent.putExtra("link_contest_id", popUpBannerModel.getBanner_contest_id());
                        mContext.startActivity(intent);
                        break;
                    }
                }
                if (!isContestFind) {
                    for (MatchModel matchModel : CustomUtil.emptyIfNull(tournamentMatchList)) {
                        // LogUtil.d("matchId","contestId Home :  "+bannerModel.getContestId() +"  "+bannerModel.getMatchId());
                        if (popUpBannerModel.getBannerMatchId().equalsIgnoreCase(matchModel.getId())) {
                            //LogUtil.d("matchId","contestId Home :  "+popUpBannerModel.getBanner_contest_id());
                            //LogUtil.d("matchId","contestId Home :  "+bannerModel.getContestId() +"  "+bannerModel.getBannerMatchId());
                            matchModel.setConDisplayType("1");
                            preferences.setMatchModel(matchModel);
                            Intent intent = new Intent(mContext, ContestListActivity.class);
                            intent.putExtra("link_contest_id", popUpBannerModel.getBanner_contest_id());
                            mContext.startActivity(intent);

                            break;
                        }
                    }
                }
                /*for (MatchModel matchModel : CustomUtil.emptyIfNull(matchModelListSafe)) {
                    if (bannerModel.getBannerMatchId().equalsIgnoreCase(matchModel.getId())) {
                        matchModel.setConDisplayType("1");
                        preferences.setMatchModel(matchModel);
                        new FragmentUtil().addFragment((FragmentActivity) mContext,
                                R.id.fragment_container,
                                new MatchContestFragment(0),
                                ((HomeActivity) mContext).fragmentTag(7),
                                FragmentUtil.ANIMATION_TYPE.CUSTOM);
                        break;
                    }
                }*/
          //  }
            /*for (MatchModel matchModel : CustomUtil.emptyIfNull((viewModel.getMatchModelList()))) {
                if (popUpBannerModel.getBannerMatchId().equalsIgnoreCase(matchModel.getId())) {
                    preferences.setMatchModel(matchModel);
                    Intent intent = new Intent(mContext, ContestListActivity.class);
                    mContext.startActivity(intent);
                    break;
                }
            }*/
        } else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("web_view")) {

            startActivity(new Intent(mContext, WebViewActivity.class)
                    .putExtra(ConstantUtil.WEB_TITLE, "")
                    .putExtra(ConstantUtil.WEB_URL, popUpBannerModel.getBannerMatchId()));
        } else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("fullBanner")) {
            startActivity(new Intent(mContext, FullImageActivity.class)
                    .putExtra(ConstantUtil.FULL_IMAGE_PATH, popUpBannerModel.getBannerMatchId()));
        } else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("whatsapp")) {
            String url = "https://api.whatsapp.com/send?phone=" + popUpBannerModel.getBannerMatchId();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("twitter")) {
            String url = ApiManager.tweet_url + popUpBannerModel.getBannerMatchId();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("facebook")) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + popUpBannerModel.getBannerMatchId()));//2216938415251012
                startActivity(intent);
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/" + popUpBannerModel.getBanner_contest_id())));//batball11
            }
        } else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("telegram")) {
            String url = "https://t.me/" + popUpBannerModel.getBannerMatchId();//batballeleven
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("telegram_support")) {
            try {
                String url = ApiManager.support_username + popUpBannerModel.getBannerMatchId();//2122548406
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setPackage("org.telegram.messenger");
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("instagram")) {
            try {
                String url = ApiManager.instagram_id + popUpBannerModel.getBannerMatchId();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("game")) {
            startActivity(new Intent(mContext, GameListActivity.class).putExtra("game_id", popUpBannerModel.getBannerMatchId()));
        } else if (popUpBannerModel.getBannerAction().equalsIgnoreCase(ConstantUtil.LINK_PREFIX_TRADING)) {
            startActivity(new Intent(mContext, TOPActivity.class)
                    .putExtra("contest_id", popUpBannerModel.getBannerMatchId())
                    .putExtra("conType", ConstantUtil.LINK_PREFIX_TRADING)
            );
        }

    }

    private void checkData() {
    /*    if (matchModelList.size()>0){
            layNoData.setVisibility(View.GONE);
            cricket_list.setVisibility(View.VISIBLE);
        }else {
            layNoData.setVisibility(View.VISIBLE);
            cricket_list.setVisibility(View.GONE);
        }*/
    }

    private void setData() {
        /*if(matchListAdapter!=null){
             matchListAdapter.updateList(matchModelList);
        }
        else{
            matchListAdapter = new MatchListAdapter(mContext,matchModelList);
            LinearLayoutManager layoutManage=new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
            cricket_list.setLayoutManager(layoutManage);
            cricket_list.setAdapter(matchListAdapter);
        }*/

        if (tournamentMatchList.size() > 0) {
           /* int pos=1;
            if (list.size()>0){
                pos=3;
            }*/

           /* MatchModel matchModel = new MatchModel();
            matchModel.setMatchDisplayType(2);
            matchModel.setMatchTitle(updateModel.getSeries_title_banner());
            matchModelList.add(matchModel);//pos,*/

            MatchModel matchModel = new MatchModel();
            matchModel.setMatchDisplayType(6);
            matchModel.setMatchTitle(updateModel.getSeries_title_banner());
            matchModel.setOtherMatchList(tournamentMatchList);
            matchModelList.add(matchModel);//(pos+1),
        }

        if (matchModelListSafe.size() > 0) {
            MatchModel matchModel1 = new MatchModel();
            matchModel1.setMatchDisplayType(2);
            matchModel1.setMatchTitle("Upcoming Matches");
            matchModelList.add(matchModel1);
            matchModelList.addAll(matchModelListSafe);
        }
        else {
            MatchModel matchModel1 = new MatchModel();
            matchModel1.setMatchDisplayType(3);
            matchModelList.add(matchModel1);
        }
        adapter1.notifyDataSetChanged();

        if (preferences.getPrefBoolean(PrefConstant.IS_BANNER_CLICK)) {
            preferences.setPref(PrefConstant.IS_BANNER_CLICK, false);

            String matchId = preferences.getPrefString(PrefConstant.BANNER_CLICK_MATCH_ID);
            String contestId = preferences.getPrefString(PrefConstant.BANNER_CLICK_CONTEST_ID);

            if (!TextUtils.isEmpty(matchId) && !TextUtils.isEmpty(contestId)) {
                boolean isContestFind = false;
                for (MatchModel matchModel : CustomUtil.emptyIfNull(matchModelListSafe)) {
                    //LogUtil.d("matchId","contestId Home :  "+bannerModel.getContestId());
                    if (matchId.equalsIgnoreCase(matchModel.getId())) {
                        matchModel.setConDisplayType("1");
                        preferences.setMatchModel(matchModel);
                        isContestFind = true;

                        Intent intent = new Intent(mContext, ContestListActivity.class);
                        intent.putExtra("link_contest_id", contestId);
                        mContext.startActivity(intent);

                        break;
                    }
                }
                if (!isContestFind) {
                    for (MatchModel matchModel : CustomUtil.emptyIfNull(tournamentMatchList)) {
                        // LogUtil.d("matchId","contestId Home :  "+bannerModel.getContestId() +"  "+bannerModel.getMatchId());
                        if (matchId.equalsIgnoreCase(matchModel.getId())) {
                            //LogUtil.d("matchId","contestId Home :  "+bannerModel.getContestId() +"  "+bannerModel.getBannerMatchId());
                            matchModel.setConDisplayType("1");
                            preferences.setMatchModel(matchModel);

                            Intent intent = new Intent(mContext, ContestListActivity.class);
                            intent.putExtra("link_contest_id", contestId);
                            mContext.startActivity(intent);

                            break;
                        }
                    }
                }
            }

        }

        /*if (fragment!=null && fragment instanceof HomeActivityFragment){
            linkData=((HomeActivityFragment)fragment).linkData;
        }*/

        if (((HomeActivity)mContext).linkData != null) {
            String data = ((HomeActivity)mContext).linkData.replaceAll(ConstantUtil.LINK_URL, "");
            String[] arr = data.split("/");
            if (!TextUtils.isEmpty(arr[0].trim())) {
                if (arr[0].trim().equalsIgnoreCase(ConstantUtil.LINK_PREFIX_FANTASY)) {
                    boolean isContestFind = false;

                    for (MatchModel matchModel : CustomUtil.emptyIfNull(matchModelList)) {
                        if (!TextUtils.isEmpty(arr[2].trim())) {
                            if (arr[2].trim().equalsIgnoreCase(matchModel.getId())) {
                                if (!TextUtils.isEmpty(arr[3].trim())) {
                                    ((HomeActivity)mContext).linkData = null;
                                   /* if (fragment!=null && fragment instanceof HomeActivityFragment){
                                        ((HomeActivityFragment)fragment).linkData=null;
                                    }*/
                                    preferences.setMatchModel(matchModel);
                                    Intent intent = new Intent(mContext, ContestListActivity.class);
                                    intent.putExtra("link_contest_id", arr[3]);
                                    mContext.startActivity(intent);
                                    break;
                                }
                            }
                        }
                    }

                    if (!isContestFind) {
                        for (MatchModel matchModel : CustomUtil.emptyIfNull(tournamentMatchList)) {
                            if (!TextUtils.isEmpty(arr[2].trim())) {
                                if (arr[2].trim().equalsIgnoreCase(matchModel.getId())) {
                                    if (!TextUtils.isEmpty(arr[3].trim())) {
                                        ((HomeActivity)mContext).linkData = null;
                                       /* if (fragment!=null && fragment instanceof HomeActivityFragment){
                                            ((HomeActivityFragment)fragment).linkData=null;
                                        }*/
                                        preferences.setMatchModel(matchModel);
                                        Intent intent = new Intent(mContext, ContestListActivity.class);
                                        intent.putExtra("link_contest_id", arr[3]);
                                        mContext.startActivity(intent);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                }
                else if (arr[0].trim().equalsIgnoreCase(ConstantUtil.LINK_PREFIX_HELPDESK)){
                    ((HomeActivity)mContext).linkData = null;
                    /*if (fragment!=null && fragment instanceof HomeActivityFragment){
                        ((HomeActivityFragment)fragment).linkData=null;
                    }*/
                    startActivity(new Intent(mContext, HelpDeskActivity.class));
                }else if (arr[0].trim().equalsIgnoreCase(ConstantUtil.LINK_PREFIX_GAME)){
                    ((HomeActivity)mContext).linkData = null;
                    /*if (fragment!=null && fragment instanceof HomeActivityFragment){
                        ((HomeActivityFragment)fragment).linkData=null;
                    }*/
                    startActivity(new Intent(mContext, GameListActivity.class));
                }
                else if (arr[0].trim().equalsIgnoreCase(ConstantUtil.LINK_PREFIX_DEPOSIT)) {
                    String amt = arr[1];
                    if (TextUtils.isEmpty(amt)) {
                        amt = "";
                    }
                    ((HomeActivity)mContext). linkData = null;
                    /*if (fragment!=null && fragment instanceof HomeActivityFragment){
                        ((HomeActivityFragment)fragment).linkData=null;
                    }*/
                    Intent intent = new Intent(mContext, AddDepositActivity.class);
                    intent.putExtra("isJoin", false);
                    intent.putExtra("depositAmt", amt);
                    startActivity(intent);
                }
                else if (arr[0].trim().equalsIgnoreCase(ConstantUtil.LINK_PREFIX_TRADING) ||
                        arr[0].trim().equalsIgnoreCase(ConstantUtil.LINK_PREFIX_POLL) ||
                        arr[0].trim().equalsIgnoreCase(ConstantUtil.LINK_PREFIX_OPINION)) {
                    String conType = arr[0];
                    String conId = "";
                    ((HomeActivity)mContext).linkData = null;
                    /*if (fragment!=null && fragment instanceof HomeActivityFragment){
                        ((HomeActivityFragment)fragment).linkData=null;
                    }*/

                    if (arr.length>=2){
                        conId = arr[1];
                    }
                    //startActivity(new Intent(mContext, TOPActivity.class));
                    LogUtil.e("resp","TradData Cricket: "+arr[0]+" : "+conId+" : "+conType);
                    startActivity(new Intent(mContext, TOPActivity.class)
                            .putExtra("contest_id", conId)
                            .putExtra("conType", conType)
                    );
                }
            }
        }

        if (!TextUtils.isEmpty(updateModel.getIs_story_avaialble()) && updateModel.getIs_story_avaialble().equalsIgnoreCase("yes") &&
                !BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE))
            getStatusUserList(true);

        /*cricket_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d("-----","end");
                    ((HomeActivityFragment)fragment).leaveAnim();
                }else if (newState==RecyclerView.SCROLL_STATE_DRAGGING){
                    ((HomeActivityFragment)fragment).enterAnim();
                }
            }
        });*/
    }

    public void isBg(boolean show) {
       // ((HomeActivityFragment) fragment).isShowBg(show);
    }

    public void StoryDismiss() {
        isBg(false);
        //mainMatchList = new ArrayList<>();

        if (!TextUtils.isEmpty(preferences.getUpdateMaster().getIs_story_avaialble()) &&
                preferences.getUpdateMaster().getIs_story_avaialble().equalsIgnoreCase("yes"))
            getStatusUserList(false);
    }

    private void getOpinionCnt(EventModel model) {

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
                    JSONObject data = responseBody.optJSONObject("data");
                    showConfirmContestDialog(model, data);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                //preferences.setUserModel(new UserModel());
            }
        });

    }

    private void showConfirmContestDialog(EventModel model, JSONObject data) {


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

        float calBalance = deposit + transfer_bal + winning + ff_coin;

        if (calBalance < entryFee[0]) {
            CustomUtil.showToast(mContext, "Insufficient Balance");
            double amt = Math.ceil(entryFee[0] - calBalance);
            if (amt < 1) {
                amt = amt + 1;
            }
            int intamt = (int) amt;
            if (intamt < amt) {
                intamt += 1;
            }
            String payableAmt = String.valueOf(intamt);
            startActivity(new Intent(mContext, AddDepositActivity.class)
                    .putExtra("depositAmt", payableAmt));
        }
        else {

            tradingDialogId=model.getId();
            dialogEventModel=model;

            DecimalFormat format = CustomUtil.getFormater("00.00");
            String rs = mContext.getResources().getString(R.string.rs);
            UserModel userModel = preferences.getUserModel();

            float totalWalletBalance = CustomUtil.convertFloat(userModel.getDepositBal()) +
                    CustomUtil.convertFloat(userModel.getFf_coin()) +
                    CustomUtil.convertFloat(userModel.getWinBal()) + CustomUtil.convertFloat(userModel.getTransferBal());

            //View view = LayoutInflater.from(mContext).inflate(R.layout.opinion_confirm_dialog, null);

            dialog = new BottomSheetDialog(mContext);
            dialog.setContentView(R.layout.trading_confirm_dialog);

            final int[] qty = {0};
            final int[] price = {0};

            yesList.clear();
            noList.clear();

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

            EditText edtQty = dialog.findViewById(R.id.edtQty);
            edtQty.setText("1");
            TextView txtPrice = dialog.findViewById(R.id.txtPrice);
            txtPrice.setText(rs + "1");
            TextView txtOptionYes = dialog.findViewById(R.id.txtOptionYes);
            TextView txtOptionNo = dialog.findViewById(R.id.txtOptionNo);
            txtQtyAtPrice = dialog.findViewById(R.id.txtQtyAtPrice);
            txtMaxQtyAtPrice = dialog.findViewById(R.id.txtMaxQtyAtPrice);
            TextView txtSelect = dialog.findViewById(R.id.txtSelect);

            TextView txtEndDate = dialog.findViewById(R.id.txtEndDate);
            ImageView imgConImage = dialog.findViewById(R.id.imgConImage);
            TextView txtConTitle = dialog.findViewById(R.id.txtConTitle);
            TextView txtDesc = dialog.findViewById(R.id.txtDesc);
            LinearLayout layDesc = dialog.findViewById(R.id.layDesc);
            txtTrades = dialog.findViewById(R.id.txtTrades);
            TextView join_contest_btn = dialog.findViewById(R.id.join_contest_btn);
            TextView txtGet = dialog.findViewById(R.id.txtGet);
            TextView txtInvest = dialog.findViewById(R.id.txtInvest);
            TextView txtNewBadge = dialog.findViewById(R.id.txtNewBadge);
            TextView txtLblCommision = dialog.findViewById(R.id.txtLblCommision);
            TextView txtBtn10 = dialog.findViewById(R.id.txtBtn10);
            TextView txtBtn50 = dialog.findViewById(R.id.txtBtn50);
            TextView txtBtn100 = dialog.findViewById(R.id.txtBtn100);
            TextView txtBtn500 = dialog.findViewById(R.id.txtBtn500);
            TextView txtBtn750 = dialog.findViewById(R.id.txtBtn750);

            txtLblCommision.setText("Fee of " + model.getCommission() + "% on credit amount, 0% fee on loss.");

            txtEndDate.setText("Ends on " + CustomUtil.dateConvertWithFormat(model.getConEndTime(), "MMM dd hh:mm a"));

            if (model.isNewBadge()) {
                txtNewBadge.setVisibility(View.VISIBLE);
            } else {
                txtNewBadge.setVisibility(View.GONE);
            }

            txtConTitle.setText(model.getConDesc());
            if (TextUtils.isEmpty(model.getConSubDesc())) {
                layDesc.setVisibility(View.GONE);
            } else {
                layDesc.setVisibility(View.VISIBLE);
                txtDesc.setText(model.getConSubDesc());
            }

            if (!TextUtils.isEmpty(model.getConImage())) {
                CustomUtil.loadImageWithGlide(mContext, imgConImage, MyApp.imageBase + MyApp.document, model.getConImage(), R.drawable.event_placeholder);
            }

            txtTrades.setText(model.getTotalTrades() + " Trades");

            SeekBar seekBarPrice = dialog.findViewById(R.id.seekPrice);

            txtOptionYes.setOnClickListener(v -> {
                model.setOptionValue("Yes");
                txtOptionYes.setBackgroundResource(R.drawable.opinio_yes);
                txtOptionNo.setBackgroundResource(R.drawable.transparent_view);
                txtOptionNo.setTextColor(mContext.getResources().getColor(R.color.black_pure));
                txtOptionYes.setTextColor(mContext.getResources().getColor(R.color.green_pure));

                setOpinionValue(model.getOptionValue(), price[0] + "", data, txtQtyAtPrice);

                if (yesList.size()>0){
                    txtMaxQtyAtPrice.setText(yesList.get(0).getTotalJoinCnt()+" quantities available at "+
                            rs+String.format("%.2f",CustomUtil.convertFloat(yesList.get(0).getJnAmount())));
                }else {
                    txtMaxQtyAtPrice.setText("0 quantities available at "+rs+format.format(price[0]));
                }

            });

            txtOptionNo.setOnClickListener(v -> {
                model.setOptionValue("No");
                txtOptionYes.setBackgroundResource(R.drawable.transparent_view);
                txtOptionNo.setBackgroundResource(R.drawable.opinio_no);
                txtOptionNo.setTextColor(mContext.getResources().getColor(R.color.red));
                txtOptionYes.setTextColor(mContext.getResources().getColor(R.color.black_pure));

                setOpinionValue(model.getOptionValue(), price[0] + "", data, txtQtyAtPrice);
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
                    price[0] = progress;

                    model.setSelectedPrice(price[0] + "");

                    txtPrice.setText(rs + price[0]);

                    float investValue = price[0] * qty[0];
                    txtInvest.setText(rs + investValue + "");

                    setOpinionValue(model.getOptionValue(), price[0] + "", data, txtQtyAtPrice);

                    if (totalWalletBalance < (qty[0] * price[0])) {
                        join_contest_btn.setBackgroundResource(R.drawable.red_bg_radius);
                        join_contest_btn.setText("Insufficient balance | Add Cash");
                    } else {
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

            if (model.getOptionValue().equalsIgnoreCase("yes")) {
                txtOptionYes.setBackgroundResource(R.drawable.opinio_yes);
                txtOptionNo.setBackgroundResource(R.drawable.transparent_view);
                txtOptionNo.setTextColor(mContext.getResources().getColor(R.color.black_pure));
                txtOptionYes.setTextColor(mContext.getResources().getColor(R.color.green_pure));

                if (TextUtils.isEmpty(model.getOption1Price())) {
                    price[0] = 1;
                    model.setSelectedPrice("1");
                } else {
                    price[0] = (int) CustomUtil.convertFloat(model.getOption1Price());
                    model.setSelectedPrice(model.getOption1Price());
                }

                if (yesList.size()>0){
                    txtMaxQtyAtPrice.setText(yesList.get(0).getTotalJoinCnt()+" quantities available at "+
                            rs+String.format("%.2f",CustomUtil.convertFloat(yesList.get(0).getJnAmount())));
                }else {
                    txtMaxQtyAtPrice.setText("0 quantities available at "+rs+"1");
                }
            } else {
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

                if (TextUtils.isEmpty(model.getOption2Price())) {
                    price[0] = 1;
                    model.setSelectedPrice("1");
                } else {
                    price[0] = (int) CustomUtil.convertFloat(model.getOption2Price());
                    model.setSelectedPrice(model.getOption2Price());
                }
            }

            seekBarPrice.setProgress(price[0]);

            setOpinionValue(model.getOptionValue(), price[0] + "", data, txtQtyAtPrice);

            SeekBar seekBarQty = dialog.findViewById(R.id.seekQty);

            seekBarQty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    qty[0] = progress;

                    edtQty.setText(qty[0] + "");
                    model.setCon_join_qty(qty[0] + "");

                    float getValue = qty[0] * 10;
                    txtGet.setText(rs + getValue + "");

                    float investValue = price[0] * qty[0];
                    txtInvest.setText(rs + investValue + "");

                    if (edtQty.hasFocus()) {
                        edtQty.setSelection(String.valueOf(progress).length());
                    }

                    if (qty[0] == 10) {
                        txtBtn10.performClick();
                    } else if (qty[0] == 50) {
                        txtBtn50.performClick();
                    } else if (qty[0] == 100) {
                        txtBtn100.performClick();
                    } else if (qty[0] == 500) {
                        txtBtn500.performClick();
                    } else if (qty[0] == 750) {
                        txtBtn750.performClick();
                    } else {
                        txtBtn10.setBackgroundResource(R.drawable.border_match_green);
                        txtBtn50.setBackgroundResource(R.drawable.border_match_green);
                        txtBtn100.setBackgroundResource(R.drawable.border_match_green);
                        txtBtn500.setBackgroundResource(R.drawable.border_match_green);
                        txtBtn750.setBackgroundResource(R.drawable.border_match_green);
                    }

                    if (totalWalletBalance < (qty[0] * price[0])) {
                        join_contest_btn.setBackgroundResource(R.drawable.red_bg_radius);
                        join_contest_btn.setText("Insufficient balance | Add Cash");
                    } else {
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
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard((Activity) mContext);
                    if (TextUtils.isEmpty(edtQty.getText().toString().trim()) ||
                            CustomUtil.convertInt(edtQty.getText().toString().trim()) <= 0) {
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
                    if (TextUtils.isEmpty(charSequence)) {
                        qty[0] = 1;
                        //edtQty.setText("1");
                        //edtQty.setSelection(1);
                    } else {
                        qty[0] = CustomUtil.convertInt(charSequence.toString());
                        edtQty.setSelection(charSequence.length());
                    }

                    if (qty[0] <= 0) {
                        qty[0] = 1;
                    } else if (qty[0] > 1000) {
                        qty[0] = 1000;
                    }
                  /*  if (edtQty.hasFocus())
                        edtQty.setSelection(charSequence.length());*/

                    seekBarQty.setProgress(qty[0]);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }

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
                if (join_contest_btn.getText().toString().trim().equalsIgnoreCase("Confirm")) {
                    if (TextUtils.isEmpty(model.getCon_join_qty())) {
                        CustomUtil.showTopSneakWarning(mContext, "Select contest quantity");
                        return;
                    }
                    if (CustomUtil.convertInt(model.getCon_join_qty()) <= 0) {
                        CustomUtil.showTopSneakWarning(mContext, "Select contest quantity");
                        return;
                    }
                    dialog.dismiss();
                    joinOpinionContest(model);
                } else {
                    if (totalWalletBalance < (qty[0] * price[0])) {
                        double amt = Math.ceil((qty[0] * price[0]) - totalWalletBalance);
                        if (amt < 1) {
                            amt = amt + 1;
                        }
                        int intamt = (int) amt;
                        if (intamt < amt) {
                            intamt += 1;
                        }
                        String payableAmt = String.valueOf(intamt);

                        startActivity(new Intent(mContext, AddDepositActivity.class)
                                .putExtra("depositAmt", payableAmt));
                    }
                }
            });

            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);

            dialog.show();
        }
    }

    private void setOpinionValue(String option, String price, JSONObject data, TextView txtQtyAtPrice) {

        //JSONArray yesCount = data.optJSONArray("yesCount");
        //JSONArray noCount = data.optJSONArray("noCount");

        txtQtyAtPrice.setText("0 quantities available at ₹" + price);

        if (option.equalsIgnoreCase("yes")) {
            for (int i = 0; i < yesList.size(); i++) {
                //JSONObject yesObj = yesCount.optJSONObject(i);
                AvailableQtyModel yesObj=yesList.get(i);
                if (yesObj.getJnAmount().equalsIgnoreCase(String.valueOf(price))) {
                    txtQtyAtPrice.setText(yesObj.getTotalJoinCnt() + " quantities available at ₹" + price);
                }
            }
        } else {
            for (int i = 0; i < noList.size(); i++) {
                //JSONObject yesObj = noCount.optJSONObject(i);
                AvailableQtyModel yesObj=noList.get(i);
                if (yesObj.getJnAmount().equalsIgnoreCase(String.valueOf(price))) {
                    txtQtyAtPrice.setText(yesObj.getTotalJoinCnt() + " quantities available at ₹" + price);
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        /*if (mSocket!=null) {
            mSocket.off("res");
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       /* if (mSocket!=null) {
            mSocket.off("res");
        }*/
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
            LogUtil.e("resp", "param: " + jsonObject + " \n url: " + ApiManager.joinTradesContest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSONNormal(mContext, true, ApiManager.joinTradesContest, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e(TAG, "joinContest : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    // CustomUtil.showTopSneakSuccess(mContext,responseBody.optString("msg"));
                    getUserData(model);
                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

                //preferences.setUserModel(new UserModel());

            }
        });
    }

    private void getUserData(EventModel model) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("token_no", preferences.getUserModel().getTokenNo());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.USER_DETAIL, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e(TAG, "USER USER_DETAIL: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    //if (responseBody.optString("code").equals("200")) {
                    JSONObject data = responseBody.optJSONObject("data");
                    UserModel userModel = gson.fromJson(data.toString(), UserModel.class);

                    float total;
                    if (ConstantUtil.is_bonus_show) {
                        total = CustomUtil.convertFloat(userModel.getDepositBal()) + CustomUtil.convertFloat(userModel.getWinBal())
                                + CustomUtil.convertFloat(userModel.getTransferBal()) + CustomUtil.convertFloat(userModel.getBonusBal());

                    } else {
                        total = CustomUtil.convertFloat(userModel.getDepositBal()) + CustomUtil.convertFloat(userModel.getWinBal())
                                + CustomUtil.convertFloat(userModel.getTransferBal());

                    }

                    /*float total = CustomUtil.convertFloat(userModel.getDepositBal()) + CustomUtil.convertFloat(userModel.getWinBal())
                            + CustomUtil.convertFloat(userModel.getTransferBal()) + CustomUtil.convertFloat(userModel.getBonusBal());*/
                    userModel.setTotal_balance(total);

                    preferences.setUserModel(userModel);

                    showOrderSuccessDialog(model);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
            }
        });
    }

    private void showOrderSuccessDialog(EventModel model) {
        //View view = LayoutInflater.from(mContext).inflate(R.layout.order_success_dialog, null);

        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.order_success_dialog);

        String rs = mContext.getResources().getString(R.string.rs);

        TextView txtOrderQty = dialog.findViewById(R.id.txtOrderQty);
        TextView txtOrderPrice = dialog.findViewById(R.id.txtOrderPrice);
        TextView txtOrderTotal = dialog.findViewById(R.id.txtOrderTotal);
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        Button btnShare = dialog.findViewById(R.id.btnShare);
        Button btnMore = dialog.findViewById(R.id.btnMore);
        ImageView imgCorrect = dialog.findViewById(R.id.imgCorrect);

        txtTitle.setText("Trade Order placed!");

        //Glide.with(mContext).asGif().load(R.raw.ic_correct).into(imgCorrect);

        txtOrderQty.setText(model.getCon_join_qty());
        txtOrderPrice.setText(rs + model.getSelectedPrice());
        float total = CustomUtil.convertFloat(model.getSelectedPrice()) * CustomUtil.convertFloat(model.getCon_join_qty());
        txtOrderTotal.setText(rs + total + "");

        btnMore.setOnClickListener(view -> {
            dialog.dismiss();
            getTradingData();
            //getData();
        });

        btnShare.setOnClickListener(view -> {
            dialog.dismiss();

            shareContest(model);
        });

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        MusicManager.getInstance().playEventSuccess(mContext);

        dialog.show();
    }

    private void shareContest(EventModel eventModel) {
        String url = ConstantUtil.LINK_TRADING_URL + eventModel.getId();
        ;

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
                        if (task.getResult().getShortLink() != null)
                            shareShortLink(task.getResult().getShortLink(), eventModel);
                        else {
                            CustomUtil.showTopSneakWarning(mContext, "Can't share this");
                        }
                    } else {
                        CustomUtil.showTopSneakWarning(mContext, "Can't share this");
                    }
                });*/
    }

    private void shareShortLink(Uri link, EventModel eventModel) {
        String content =//ConstantUtil.LINK_URL+"\n\n"+
                "*" + eventModel.getConDesc() + "* \uD83C\uDFC6" +
                        "\n\nwhat do you think will happen ❓\n\n" +
                        "\uD83D\uDCC8Trade on *Fantafeat*.\n\n" +
                        "\uD83D\uDCB8Earning is simple here.\n\n" +
                        "Deadline⏳ : " + CustomUtil.dateConvertWithFormat(eventModel.getConEndTime(), "dd MMM, yyyy hh:mm a") +
                        "\n\nTap below link for join:\uD83D\uDCF2" +
                        "\n" + link.toString().trim()/*+MyApp.getMyPreferences().getMatchModel().getId()+"/"+list.getId()*/;

        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, content);
            sendIntent.setPackage("com.whatsapp");
            sendIntent.setType("text/html");
            startActivity(sendIntent);
        } catch (ActivityNotFoundException e) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, content);
            sendIntent.setType("text/html");
            startActivity(sendIntent);
        }
    }

    public void pageSwitcher() {
        timerTopAds = new Timer();
        timerTopAds.scheduleAtFixedRate(new TopAdsAutoScroll(), 0, 5 * 1000);
    }

    private class TopAdsAutoScroll extends TimerTask {
        @Override
        public void run() {
            ((Activity) mContext).runOnUiThread(() -> {
                if (pageTopAds > preferences.getHomeBanner().size() - 1) {
                    pageTopAds = 0;
                } else {
                    // mHomeOffer.setCurrentItem(pageTopAds++);
                }
            });
        }
    }

    @Override
    public void initClick() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
           /* matchModelList.clear();
            cricket_list.getRecycledViewPool().clear();
            adapter1.notifyDataSetChanged();*/

            /*if (!TextUtils.isEmpty(updateModel.getIs_story_avaialble()) && updateModel.getIs_story_avaialble().equalsIgnoreCase("yes") &&
                    !BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE))
                getStatusUserList(true);
            else if (!TextUtils.isEmpty(updateModel.getIs_trade_available()) && updateModel.getIs_trade_available().equalsIgnoreCase("yes") &&
                    !BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE))
                getTradingData();
            else*/
                getData();
            /*if ((System.currentTimeMillis()-lastUpdateTime)>=ConstantUtil.Refresh_delay) {

            }else {
                swipeRefreshLayout.setRefreshing(false);
            }*/
        });
    }


}