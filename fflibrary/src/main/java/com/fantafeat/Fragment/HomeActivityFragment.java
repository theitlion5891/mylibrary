package com.fantafeat.Fragment;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentActivity;
//import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fantafeat.Activity.AddDepositActivity;
import com.fantafeat.Activity.CommongSoonActivity;
import com.fantafeat.Activity.ContestListActivity;
import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.Activity.LudoContestListActivity;
import com.fantafeat.Activity.TOPActivity;
import com.fantafeat.Adapter.ViewPagerAdapter;
import com.fantafeat.BuildConfig;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.PopUpBannerModel;
import com.fantafeat.Model.SportsModel;
import com.fantafeat.Model.UpdateModel;
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
import com.fantafeat.util.PrefConstant;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.itextpdf.text.pdf.parser.Line;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class HomeActivityFragment extends BaseFragment {

    private TabLayout tabs;
    private static ViewPager2 viewPager;
    //private ViewPager2 mHomeOffer;
    private TextView tab1;//,tab2,tab3,tab4,tab5;
    private Toolbar toolbar;
   // private OfferBannerAdapter adapter;
    public ImageView mToolbarMenu,imgTrade,imgBg;
    public List<SportsModel> sportList;
    private ImageView notification,imgSanta;
    private Timer timerTopAds;
    private int pageTopAds=0;
    private boolean show = false;
   // private RelativeLayout layLudoSoon;
  //  private CoordinatorLayout main;

   // private boolean isBannerShow = false;
    public MatchViewModal viewModel;
    public String sportsId="1";
   // public String linkData="";
   // public LinearLayout layGuru;
   // public TextView txtGames;
   // private String[] arrGame=new String[]{"G","A","M","E","S"};
   // public CardView circleImageView;
   // public ImageView circleLogo;

    //private Socket mSocket= null;
    public HomeActivityFragment(){

    }

    public static HomeActivityFragment newInstance(String linkData) {
        HomeActivityFragment myFragment=new HomeActivityFragment();
        Bundle args = new Bundle();
        args.putString("linkData", linkData);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (getArguments() != null) {
            linkData = getArguments().getString("linkData");
            LogUtil.d("linkdata",linkData+" uri data");
        }*/
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
       // viewModel = ViewModelProviders.of(getActivity()).get(MatchViewModal.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_activity, container, false);

        initFragment(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        isShowBg(false);
        setStatusBarRed();
        displayGameAnimation();
        /*mSocket = MyApp.getInstance().getSocketInstance();
        if (mSocket!=null && !mSocket.connected()){
            mSocket.connect();
            try {
                JSONObject obj=new JSONObject();
                if (preferences.getUserModel()!=null){
                    obj.put("user_id",preferences.getUserModel().getId());
                }
                obj.put("title","matchlist");

                mSocket.emit("connect_user",obj);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }*/
    }

    public void isShowBg(boolean isShow){
        if (isShow)
            imgBg.setVisibility(View.VISIBLE);
        else
            imgBg.setVisibility(View.GONE);
    }

    @Override
    public void initControl(View view) {
        toolbar = view.findViewById(R.id.home_actionbar);
        ((HomeActivity)getActivity()).setSupportActionBar(mToolbar);

        tabs = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.viewpager);
        mToolbarMenu = view.findViewById(R.id.drawer_image);
        notification = view.findViewById(R.id.notification);
        imgSanta = view.findViewById(R.id.imgSanta);
        imgTrade = view.findViewById(R.id.imgTrade);
        //txtGames = view.findViewById(R.id.txtGames);
       // mHomeOffer = view.findViewById(R.id.home_offer);
        tab1 = view.findViewById(R.id.tab1);
        imgBg = view.findViewById(R.id.imgBg);
       // layLudoSoon = view.findViewById(R.id.layLudoSoon);
       // main = view.findViewById(R.id.main);
       // layGuru = view.findViewById(R.id.layGuru);
        /*circleImageView = view.findViewById(R.id.circleImageView);
        circleLogo = view.findViewById(R.id.circleLogo);*/
        // tab2 = view.findViewById(R.id.tab2);
        // tab3 = view.findViewById(R.id.tab3);
        //tab4 = view.findViewById(R.id.tab4);
        //tab5 = view.findViewById(R.id.tab5);
        sportList = preferences.getSports();

        viewPager.setAdapter(createAdapter());

        UpdateModel updateModel=preferences.getUpdateMaster();
        if (updateModel.getIs_top_enable()!=null &&
                updateModel.getIs_top_enable().equalsIgnoreCase("yes") && !BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE)){
            imgTrade.setVisibility(View.VISIBLE);
            //Glide.with(mContext).asGif().load(R.raw.trade_x_launcher).into(imgTrade);
        }else {
            if (ConstantUtil.is_game_test)
                imgTrade.setVisibility(View.VISIBLE);
            else
                imgTrade.setVisibility(View.GONE);
        }

        String spId="";

        if (((HomeActivity)mContext).linkData!=null && !TextUtils.isEmpty(((HomeActivity)mContext).linkData)) {
           // LogUtil.e("resp","LinkData: "+linkData);
            String data=((HomeActivity)mContext).linkData.replaceAll(ConstantUtil.LINK_URL,"");
            String[] arr=data.split("/");
            if (!TextUtils.isEmpty(arr[0].trim())){
                if (arr[0].trim().equalsIgnoreCase(ConstantUtil.LINK_PREFIX_FANTASY)){
                    if (!TextUtils.isEmpty(arr[1])){// sports id
                        spId=arr[1];
                        LogUtil.e("resp","LinkData spId: "+spId);
                    }
                }
                else if (arr[0].trim().equalsIgnoreCase(ConstantUtil.LINK_PREFIX_DEPOSIT)){
                    String amt=arr[1];
                    if (TextUtils.isEmpty(amt)){
                        amt="";
                    }

                    ((HomeActivity)mContext).linkData=null;

                    startActivity(new Intent(mContext,AddDepositActivity.class)
                            .putExtra("depositAmt",amt));

                }
                else if (arr[0].trim().equalsIgnoreCase(ConstantUtil.LINK_PREFIX_TRADING) ||
                        arr[0].trim().equalsIgnoreCase(ConstantUtil.LINK_PREFIX_POLL) ||
                        arr[0].trim().equalsIgnoreCase(ConstantUtil.LINK_PREFIX_OPINION)){
                    String conType=arr[0];
                    String conId=arr[1];
                    ((HomeActivity)mContext).linkData=null;
                    LogUtil.e("resp","TradData Home: "+arr[0]+" : "+conId+" : "+conType);
                    //startActivity(new Intent(mContext, TOPActivity.class));
                    startActivity(new Intent(mContext, TOPActivity.class)
                            .putExtra("contest_id",conId)
                            .putExtra("conType",conType)
                    );
                }
            }
        }

        new TabLayoutMediator(tabs, viewPager,
                (tab, position) -> {
                    SportsModel sportsModel=sportList.get(position);
                    int res = getResources().getIdentifier(sportsModel.getSportImg(), "drawable", mContext.getPackageName());
                    if(res != 0) {
                        tab.setText(sportsModel.getSportName()).setIcon(res);
                    }else{
                        tab.setText(sportsModel.getSportName());
                    }
                    //preferences.setSports(sportList);
                }).attach();

        viewPager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);

        int selTab=preferences.getPrefInt(PrefConstant.MATCH_SELECTED_TAB);
     //   int selTab=0;
        if (!ApiManager.isTabLoad){
            ApiManager.isTabLoad=true;
            List<SportsModel> sportsModelList=preferences.getSports();
            if (sportsModelList!=null){
                for (int i=0;i<sportsModelList.size();i++){
                    SportsModel model=sportsModelList.get(i);
                    if (TextUtils.isEmpty(spId)){
                        if (model.getSportDefaultDisplay().equalsIgnoreCase("yes")){
                            selTab=i;
                            preferences.setPref(PrefConstant.CURRENT_TAB,i);
                            preferences.setPref(PrefConstant.MATCH_SELECTED_TAB,i);
                            sportsId=model.getId();
                        }
                    }
                    else {
                        if (spId.trim().equalsIgnoreCase(model.getId().trim())){
                            selTab=i;
                            preferences.setPref(PrefConstant.CURRENT_TAB,i);
                            preferences.setPref(PrefConstant.MATCH_SELECTED_TAB,i);
                            sportsId=model.getId();
                        }
                    }
                }
            }
        }

        /*if (selTab!=-1){
            int icon=0;
            if (selTab==0){
                icon=R.drawable.ic_selected_cricket;
            }
            if (selTab==1){
                icon=R.drawable.ic_selected_football;
            }
            if (selTab==2){
                icon=R.drawable.ic_selected_basketball;
            }
            if (selTab==3){
                icon=R.drawable.ic_selected_baseball;
            }
            if (selTab==4){
                icon=R.drawable.ic_selected_nba;
            }
            tabs.getTabAt(selTab).setIcon(icon);
            viewPager.setCurrentItem(selTab,false);
        }else {
            tabs.getTabAt(0).setIcon(R.drawable.ic_selected_cricket);
        }*/

        if (selTab==-1){
            selTab=0;
        }

        viewPager.setCurrentItem(selTab,false);
        viewPager.setUserInputEnabled(false);
        ConstantUtil.reduceDragSensitivity(viewPager);

        /*try {
            getPopupBanner(sportsId);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        if (!preferences.getPrefBoolean(PrefConstant.isFestivalShow)){
            if (updateModel!=null && !TextUtils.isEmpty(updateModel.getHome_christmas_image())){
                imgSanta.setVisibility(View.VISIBLE);
                MyApp.isFestivalShow=true;
                preferences.setPref(PrefConstant.isFestivalShow,true);
                Glide.with(this).asGif()
                        .load(updateModel.getHome_christmas_image())
                        .into(imgSanta);
                new CountDownTimer(10000, 1000) {
                    public void onTick(long millisUntilFinished) {}
                    public void onFinish() {
                        imgSanta.setVisibility(View.GONE);
                    }
                }.start();
            }
            else {
                imgSanta.setVisibility(View.GONE);
            }
        }
        else {
            imgSanta.setVisibility(View.GONE);
        }

        /*if (!preferences.getPrefBoolean(PrefConstant.POPUP_SHOWN)) {
            try {
                getPopupBanner();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

    }

    private void displayGameAnimation(){
        final String[] game = {""};
        final int[] pos = {0};

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                       /* try {
                            if (pos[0] !=arrGame.length){
                                game[0] = game[0] +arrGame[pos[0]];
                                txtGames.setText(game[0]);
                                pos[0]++;
                            }else {
                                timer.cancel();
                            }

                        } catch (Exception e) {
                        }*/
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 500);
    }

    public void changeSpotsTab(String spotId,String matchId,String contest_id){
        String spId=sportList.get(viewPager.getCurrentItem()).getId();

        if (!spotId.equalsIgnoreCase(spId)){
            int selTab=-1;
            for (int i=0;i<sportList.size();i++){
                SportsModel sportsModel=sportList.get(i);
                if (spotId.equalsIgnoreCase(sportsModel.getId())){
                    selTab = i;
                }
            }

            preferences.setPref(PrefConstant.IS_BANNER_CLICK,true);
            preferences.setPref(PrefConstant.BANNER_CLICK_MATCH_ID,matchId);
            preferences.setPref(PrefConstant.BANNER_CLICK_CONTEST_ID,contest_id);

            //home_tab.getTabAt(selTab).setIcon(icon);
            viewPager.setCurrentItem(selTab,false);
        }
    }

    private void getPopupBanner(String spId) throws JSONException {
        JSONObject param=new JSONObject();
        param.put("sports_id",spId);//preferences.getSports().get(tabs.getSelectedTabPosition())
        LogUtil.d("resp",param+" ");
        HttpRestClient.postJSON(mContext, false, ApiManager.POPUP_BANNER, param, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e("popresp", "popup" + responseBody.toString());
                preferences.setPref(PrefConstant.POPUP_SHOWN, true);
                if (responseBody.optBoolean("status")) {

                    //JSONObject jsonObject = responseBody.optJSONObject("data");
                    //LogUtil.e("popresp", "onSuccessResult: " + responseBody.optString("data").length());
                    if (responseBody.optString("data").length() != 2){
                        PopUpBannerModel popUpBannerModel = gson.fromJson(responseBody.optString("data"), PopUpBannerModel.class);
                        int count = CustomUtil.convertInt(popUpBannerModel.getDisplayBannerCount());
                       // LogUtil.e("popresp", "onSuccessResult: " + count+"   "+preferences.getPrefInt(PrefConstant.POPUP_BANNER_COUNT));
                        if (!preferences.getPrefString(PrefConstant.POPUP_BANNER_ID).equals(popUpBannerModel.getId())) {
                            LogUtil.e("popresp", "onSuccessResult: cnt");
                            preferences.setPref(PrefConstant.POPUP_BANNER_COUNT, 0);
                        }

                        if (count!=preferences.getPrefInt(PrefConstant.POPUP_BANNER_COUNT)){
                            showPopupBanner(popUpBannerModel,spId);
                        }
                    }
                /*    if (responseBody.optString("data").length() != 2) {

                        PopUpBannerModel popUpBannerModel = gson.fromJson(responseBody.optString("data"), PopUpBannerModel.class);

                        LogUtil.e("popresp", "onSuccessResult: "+preferences.getPrefString(PrefConstant.POPUP_BANNER_ID)+
                                "  "+popUpBannerModel.getId());

                        if (!preferences.getPrefString(PrefConstant.POPUP_BANNER_ID).equals(popUpBannerModel.getId())) {
                            */
                    /*if(preferences.getPrefInt(PrefConstant.POPUP_BANNER_COUNT) <= CustomUtil.convertInt(popUpBannerModel.getDisplayBannerCount())){
                                int count = preferences.getPrefInt(PrefConstant.POPUP_BANNER_COUNT);
                                count--;
                            }*/
                    /*
                            LogUtil.e("popresp", "onSuccessResult: 1");
                            preferences.setPref(PrefConstant.POPUP_BANNER_ID, popUpBannerModel.getId());

                            int count = CustomUtil.convertInt(popUpBannerModel.getDisplayBannerCount());
                            count--;
                            preferences.setPref(PrefConstant.POPUP_BANNER_COUNT, count);
                            show = true;
                        }
                        else {
                            LogUtil.e("popresp", "onSuccessResult: else" + preferences.getPrefInt(PrefConstant.POPUP_BANNER_COUNT));

                            int count = preferences.getPrefInt(PrefConstant.POPUP_BANNER_COUNT);
                            if (count > 0) {
                                show = true;
                                count--;
                                preferences.setPref(PrefConstant.POPUP_BANNER_COUNT, count);
                            } else {
                                show = false;
                            }

                            if (show) {
                                showPopupBanner(popUpBannerModel,spId);
                            }
                        }
                    }*/
                }

            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                /*if (match_pull != null && match_pull.isRefreshing()) {
                    match_pull.setRefreshing(false);
                }*/
            }
        });
    }

    public void showPopupBanner(PopUpBannerModel popUpBannerModel,String spId){
        List<SportsModel> sportsModelList=preferences.getSports();
        List<SportsModel> updatedList=new ArrayList<>();

        for (int i=0;i<sportsModelList.size();i++){
            SportsModel model=sportsModelList.get(i);
            if (model.getId().equalsIgnoreCase(spId)){
                model.setPopupShow(true);
            }
            updatedList.add(model);
        }
        preferences.setSports(updatedList);

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

        if (popUpBannerModel.getBannerAction().equals("webview")){
            webView.setVisibility(View.VISIBLE);
            banner_img.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(popUpBannerModel.getBannerWebUrl())){
                webView.loadUrl(popUpBannerModel.getBannerWebUrl());
            }
        }else {
            webView.setVisibility(View.GONE);
            banner_img.setVisibility(View.VISIBLE);
            banner_img.setMinimumHeight((int) heightPixels);
            banner_img.setMinimumWidth((int) widthPixels);

            CustomUtil.loadImageWithGlide(mContext,banner_img,ApiManager.POPUP_BANNER_IMG,popUpBannerModel.getBannerImg(),R.drawable.popup_placeholder);
           /* Glide.with(mContext)
                    .load(ApiManager.POPUP_BANNER_IMG + popUpBannerModel.getBannerImg())
                    .placeholder(R.drawable.popup_placeholder)
                    .error(R.drawable.popup_placeholder)
                    .into(banner_img);*/
            if (popUpBannerModel.getBannerAction().equals("maintenance")){
                close.setVisibility(View.GONE);
            }else {
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
            preferences.setPref(PrefConstant.POPUP_BANNER_COUNT, preferences.getPrefInt(PrefConstant.POPUP_BANNER_COUNT)+1);

        });

        banner_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*preferences.setPref(PrefConstant.POPUP_BANNER_ID, popUpBannerModel.getId());
                preferences.setPref(PrefConstant.POPUP_BANNER_COUNT, preferences.getPrefString(PrefConstant.POPUP_BANNER_ID)+1);*/
                if (popUpBannerModel.getBannerAction().equalsIgnoreCase("deposit")) {
                    dialog.dismiss();

                    Intent intent = new Intent(mContext, AddDepositActivity.class);
                    intent.putExtra("isJoin",false);
                    intent.putExtra("depositAmt",popUpBannerModel.getMatchId());
                    startActivity(intent);
                }
                else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("Contest")) {
                    for (MatchModel matchModel : CustomUtil.emptyIfNull((viewModel.getMatchModelList()))) {
                        if (popUpBannerModel.getMatchId().equalsIgnoreCase(matchModel.getId())) {
                            preferences.setMatchModel(matchModel);
                            Intent intent = new Intent(mContext, ContestListActivity.class);
                            mContext.startActivity(intent);
                            break;
                        }
                    }
                }else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("whatsapp")){
                    if (MyApp.getClickStatus()){
                        dialog.dismiss();
                        String url = ApiManager.whatsapp_url;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                }
                else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("twitter")){
                    if (MyApp.getClickStatus()){
                        dialog.dismiss();
                        String url = ApiManager.tweet_url;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                }
                else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("facebook")){
                    if (MyApp.getClickStatus()){
                        dialog.dismiss();
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApiManager.fb_page_id));
                            startActivity(intent);
                        } catch(Exception e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ApiManager.fb_page_url)));
                        }
                    }
                }
                else if (popUpBannerModel.getBannerAction().equalsIgnoreCase("telegram")){
                    if (MyApp.getClickStatus()){
                        dialog.dismiss();
                        String url = ApiManager.telegram_id;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                }
            }
        });
    }

    /*public void pageSwitcher() {
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
                    mHomeOffer.setCurrentItem(pageTopAds++);
                }
            });
        }
    }*/

    @Override
    public void initClick() {

        /*layGuru.setOnClickListener(view -> {
            startActivity(new Intent(mContext, LudoContestListActivity.class));//CommongSoonActivity
        });*/

        mToolbarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenNavDrawer();
            }
        });

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e(TAG, "onTabSelected: "+ tab.getPosition() );
                viewPager.setCurrentItem(tab.getPosition(),false);
                preferences.setPref(PrefConstant.CURRENT_TAB,tab.getPosition());
                preferences.setPref(PrefConstant.MATCH_SELECTED_TAB,tab.getPosition());

                for (int i =0;i<sportList.size();i++){
                    Log.e("tabImg",sportList.get(i).getSportImg());
                    int res = getResources().getIdentifier(sportList.get(i).getSportImg(), "drawable", mContext.getPackageName());
                    /*if (sportList.get(i).getId().equalsIgnoreCase("5")){
                        tabs.getTabAt(i).setText("Handball").setIcon(R.drawable.tab_handball);
                    }else {
                        tabs.getTabAt(i).setText(sportList.get(i).getSportName()).setIcon(res);
                    }*/
                    tabs.getTabAt(i).setText(sportList.get(i).getSportName()).setIcon(res);
                }

                tab1.setText(tab.getText());
               /* Log.e("tabImg",tab.getText()+" ");
                String spId=sportList.get(tab.getPosition()).getId();
                if (spId.equalsIgnoreCase("1")){
                    try {
                        if (!preferences.getSports().get(tab.getPosition()).isPopupShow())
                            getPopupBanner("1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (spId.equalsIgnoreCase("2")){
                    try {
                        if (!preferences.getSports().get(tab.getPosition()).isPopupShow())
                            getPopupBanner("2");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (spId.equalsIgnoreCase("4")){
                    try {
                        if (!preferences.getSports().get(tab.getPosition()).isPopupShow())
                            getPopupBanner("4");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (spId.equalsIgnoreCase("3")){
                    try {
                        if (!preferences.getSports().get(tab.getPosition()).isPopupShow())
                            getPopupBanner("3");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (spId.equalsIgnoreCase("5")){
                    try {
                        if (!preferences.getSports().get(tab.getPosition()).isPopupShow())
                            getPopupBanner("5");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (spId.equalsIgnoreCase("6")){
                    try {
                        if (!preferences.getSports().get(tab.getPosition()).isPopupShow())
                            getPopupBanner("6");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //int tabIconColor = ContextCompat.getColor(mContext, R.color.tab_unselected);
               // tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.MULTIPLY);
                /*int tabIconColor = ContextCompat.getColor(mContext, R.color.tab_unselected);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);*/
                //tabs.getTabAt(tab.getPosition()).(getResources().getColor(R.color.tab_unselected));

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    String url = ApiManager.telegram_id;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    /*new FragmentUtil().addFragment((FragmentActivity) mContext,
                            R.id.home_fragment_container,
                            new NotificationFragment(),
                            ((HomeActivity) mContext).fragmentTag(31),
                            FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
                }
            }
        });

        imgTrade.setOnClickListener(v -> {
            startActivity(new Intent(mContext, TOPActivity.class));
        });

    }

    private ViewPagerAdapter createAdapter() {
       return new ViewPagerAdapter(this,preferences.getSports(),((HomeActivity)mContext).linkData);
    }

  /*   public class OfferBannerAdapter extends RecyclerView.Adapter<OfferBannerAdapter.OfferBannerHolder> {

        private Context mContext;
        private int lastHolder = -1;
        private List<BannerModel> bannerModelList;

        public OfferBannerAdapter(Context mContext, List<BannerModel> bannerModelList) {
            this.mContext = mContext;
            this.bannerModelList = bannerModelList;
        }

        @NonNull
        @Override
        public OfferBannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new OfferBannerHolder(LayoutInflater.from(mContext).inflate(R.layout.row_home_offer, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull OfferBannerHolder holder, int position) {
            final BannerModel bannerModel = bannerModelList.get(position);

            LogUtil.e(TAG, "onBindViewHolder: " + gson.toJson(bannerModel));

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((HomeActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            float widthPixels = displayMetrics.widthPixels;
            float heightPixels = (float) (widthPixels * 0.2);
            holder.level_image.setMinimumHeight((int) heightPixels);

            Glide.with(mContext)
                    .load(ApiManager.BANNER_IMAGES + bannerModel.getBannerImage())
                    .placeholder(R.drawable.placeholder_banner)
                    .error(R.drawable.placeholder_banner)
                    .into(holder.level_image);

            holder.level_image.setOnClickListener(view -> {

               // bannerModel.setBannerAction("Contest");
               // LogUtil.e("banAct",bannerModel.getBannerAction()+"   "+viewModel.getMatchModelList().size());
                if (bannerModel.getBannerAction().equalsIgnoreCase("deposit")) {
                    Intent intent = new Intent(mContext, AddDepositActivity.class);
                    intent.putExtra("isJoin",false);
                    intent.putExtra("depositAmt",bannerModel.getBannerMatchId());
                    startActivity(intent);
                }
                else if (bannerModel.getBannerAction().equalsIgnoreCase("Contest")) {
                    for (MatchModel matchModel : CustomUtil.emptyIfNull((viewModel.getMatchModelList()))) {
                        if (bannerModel.getBannerMatchId().equalsIgnoreCase(matchModel.getId())) {
                            preferences.setMatchModel(matchModel);
                            Intent intent = new Intent(mContext, ContestListActivity.class);
                            mContext.startActivity(intent);
                            break;
                        }
                    }
                }
                else if (bannerModel.getBannerAction().equalsIgnoreCase("web_view")) {
                    startActivity(new Intent(mContext, WebViewActivity.class)
                            .putExtra(ConstantUtil.WEB_TITLE,"")
                            .putExtra(ConstantUtil.WEB_URL,bannerModel.getBannerWebViewUrl()));
                }
                else if (bannerModel.getBannerAction().equalsIgnoreCase("fullBanner")) {
                    new FragmentUtil().addFragment((FragmentActivity) mContext,
                            R.id.home_fragment_container,
                            new FullImageFragment(ApiManager.BANNER_IMAGES + bannerModel.getBannerPopupImage()),
                            ((HomeActivity) mContext).fragmentTag(21),
                            FragmentUtil.ANIMATION_TYPE.CUSTOM);
                }else if (bannerModel.getBannerAction().equalsIgnoreCase("whatsapp")){
                    if (MyApp.getClickStatus()){
                        String url = ApiManager.whatsapp_url;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                }
                else if (bannerModel.getBannerAction().equalsIgnoreCase("twitter")){
                    if (MyApp.getClickStatus()){
                        String url = ApiManager.tweet_url;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                }
                else if (bannerModel.getBannerAction().equalsIgnoreCase("facebook")){
                    if (MyApp.getClickStatus()){
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApiManager.fb_page_id));
                            startActivity(intent);
                        } catch(Exception e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ApiManager.fb_page_url)));
                        }
                    }
                }
                else if (bannerModel.getBannerAction().equalsIgnoreCase("telegram")){
                    if (MyApp.getClickStatus()){
                        String url = ApiManager.telegram_id;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return bannerModelList.size();
        }


        public class OfferBannerHolder extends RecyclerView.ViewHolder {
            ImageView level_image;

            public OfferBannerHolder(@NonNull View itemView) {
                super(itemView);
                level_image = itemView.findViewById(R.id.offer_slider_img);
            }
        }
    }*/

    public static int getCurrentViewpager(){
        return viewPager.getCurrentItem();
    }

    public void enterAnim(){
        /*Animation rightSwipe = AnimationUtils.loadAnimation(mContext, R.anim.anim_right);
        layLudoSoon.startAnimation(rightSwipe);*/
       /* TranslateAnimation animate;
        if (layLudoSoon.getHeight() == 0) {
            main.getHeight(); // parent layout
            animate = new TranslateAnimation(main.getWidth()/2,
                    0, 0, 0);
        } else {
            animate = new TranslateAnimation(layLudoSoon.getWidth(),0, 0, 0); // View for animation
        }

        animate.setDuration(500);
        animate.setFillAfter(true);
        layLudoSoon.startAnimation(animate);
        layLudoSoon.setVisibility(View.VISIBLE);*/
    }

    public void leaveAnim(){
  /*      Animation rightSwipe = AnimationUtils.loadAnimation(mContext, R.anim.anim_left);
        layLudoSoon.startAnimation(rightSwipe);*/
        /*TranslateAnimation animate;
        if (layLudoSoon.getHeight() == 0) {
            animate = new TranslateAnimation(layLudoSoon.getWidth(),0, 0, 0); // View for animation
        } else {
            main.getHeight(); // parent layout
            animate = new TranslateAnimation(main.getWidth()/2,
                    0, 0, 0);
        }

        animate.setDuration(500);
        animate.setFillAfter(true);
        layLudoSoon.startAnimation(animate);
        layLudoSoon.setVisibility(View.VISIBLE);*/
    }

}