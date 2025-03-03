package com.fantafeat.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
//import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.fantafeat.Activity.AddDepositActivity;
import com.fantafeat.Activity.ContestListActivity;
import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.Activity.TOPActivity;
import com.fantafeat.Adapter.MatchListAdapter;
import com.fantafeat.Adapter.OfferBannerAdapter;
import com.fantafeat.Model.BannerModel;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.PopUpBannerModel;
import com.fantafeat.Model.SportsModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.PrefConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class BasketballHomeFragment extends BaseFragment {

    private String sport_id="",linkData="";
    private RecyclerView basketball_list;
    private MatchListAdapter matchListAdapter;
    private List<MatchModel> matchModelList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout layNoData;
    private TextView txtNoLbl;
    private boolean show_dialog;
    public MatchViewModal viewModel;
    private Timer timerTopAds;
    private int pageTopAds=0;
    private ViewPager2 mHomeOffer;
    private OfferBannerAdapter adapter;
   // private Fragment fragment;
    private NestedScrollView scroll;

    public BasketballHomeFragment( ) {

    }

    public static BasketballHomeFragment newInstance(String id,String linkData) {
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("linkData", linkData);
        BasketballHomeFragment f = new BasketballHomeFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sport_id = getArguments().getString("id");
            linkData = getArguments().getString("linkData");
        }

        //viewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(MatchViewModal.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basketball_home, container, false);
        initFragment(view);
        getData();
        return view;
    }

    @Override
    public void initControl(View view) {
        matchModelList=new ArrayList<>();
        layNoData = view.findViewById(R.id.layNoData);
        basketball_list = view.findViewById(R.id.basketball_list);
        txtNoLbl = view.findViewById(R.id.txtNoLbl);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_football);
        scroll = view.findViewById(R.id.scroll);
        //swipeRefreshLayout.setEnabled(false);
        //checkData();
        swipeRefreshLayout.setOnRefreshListener(()->{
            matchModelList.clear();
            getData();
        });

        mHomeOffer = view.findViewById(R.id.home_offer);

        if (preferences.getHomeBanner() != null && preferences.getHomeBanner().size() > 0) {
            adapter = new OfferBannerAdapter(getContext(),viewModel,preferences);
            mHomeOffer.setAdapter(adapter);
            mHomeOffer.setClipChildren(false);
            mHomeOffer.setOffscreenPageLimit(2);
            mHomeOffer.setPageTransformer(new MarginPageTransformer(40));
            pageSwitcher();
        }
        else {
            mHomeOffer.setVisibility(View.GONE);
        }

        scroll.setOnTouchListener(new View.OnTouchListener() {
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
        });
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
                    mHomeOffer.setCurrentItem(pageTopAds++);
                }
            });
        }
    }

    @Override
    public void initClick() {}

    private void getData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",preferences.getUserModel().getId());
            jsonObject.put("sports_id",sport_id);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        boolean isLoading=true;
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            isLoading=false;
        }

        HttpRestClient.postJSON(mContext, isLoading, ApiManager.MATCH_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult: "+ responseBody);
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                /*if (matchModelList!=null){
                    matchModelList.clear();
                } else {
                    matchModelList=new ArrayList<>();
                }*/
                if(responseBody.optBoolean("status")){
                    JSONArray array = responseBody.optJSONArray("data");
                    for(int i=0;i < array.length(); i++){
                        try {
                            JSONObject data = array.getJSONObject(i);
                            MatchModel matchModel = gson.fromJson(data.toString(),MatchModel.class);
                            matchModelList.add(matchModel);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    viewModel.setMatchModelList(matchModelList);
                    show_dialog = false;

                    JSONArray top_banner_data=responseBody.optJSONArray("top_banner_data");
                    if (top_banner_data!=null){
                        final List<BannerModel> bannerModels = new ArrayList<>();
                        for (int i = 0; i < top_banner_data.length(); i++) {
                            try {
                                JSONObject data = top_banner_data.getJSONObject(i);
                                BannerModel bannerModel = gson.fromJson(data.toString(), BannerModel.class);
                                bannerModels.add(bannerModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        preferences.setHomeBanner(bannerModels);
                        // displayOffer();
                    }

                    JSONObject popup_banner_data=responseBody.optJSONObject("popup_banner_data");
                    LogUtil.e(TAG, "popup_banner_data: " + popup_banner_data);
                    if (popup_banner_data!=null && !ConstantUtil.IS_POPUP_BANNER_SHOW){
                        PopUpBannerModel popUpBannerModel = gson.fromJson(popup_banner_data.toString(), PopUpBannerModel.class);
                        int count = CustomUtil.convertInt(popUpBannerModel.getDisplayBannerCount());
                        // LogUtil.e("popresp", "onSuccessResult: " + count+"   "+preferences.getPrefInt(PrefConstant.POPUP_BANNER_COUNT));
                        if (!preferences.getPrefString(PrefConstant.POPUP_BANNER_ID).equals(popUpBannerModel.getId())) {
                            LogUtil.e("popresp", "onSuccessResult: cnt");
                            preferences.setPref(PrefConstant.POPUP_BANNER_COUNT, 0);
                        }

                        if (count!=preferences.getPrefInt(PrefConstant.POPUP_BANNER_COUNT)){
                            ConstantUtil.IS_POPUP_BANNER_SHOW=true;
                            showPopupBanner(popUpBannerModel,sport_id);
                        }
                    }

                    setData();
                    //matchListAdapter.notifyDataSetChanged();
                }
                checkData();
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
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
    private void checkData(){
        if (matchModelList.size()>0){
            layNoData.setVisibility(View.GONE);
            basketball_list.setVisibility(View.VISIBLE);
        }else {
            layNoData.setVisibility(View.VISIBLE);
            txtNoLbl.setText(getString(R.string.no_record_found));//arrive_soon
            basketball_list.setVisibility(View.GONE);
        }
    }

    private void setData() {
        if(matchListAdapter!=null){
            matchListAdapter.updateList(matchModelList);
        }else {
            matchListAdapter = new MatchListAdapter(mContext, matchModelList);
            basketball_list.setLayoutManager(new LinearLayoutManager(mContext));
            basketball_list.setAdapter(matchListAdapter);
        }

        if (linkData != null) {
            String data=linkData.replaceAll(ConstantUtil.LINK_URL,"");
            String[] arr=data.split("/");
            if (!TextUtils.isEmpty(arr[0].trim())){
                if (arr[0].trim().equalsIgnoreCase(ConstantUtil.LINK_PREFIX_FANTASY)){
                    for (MatchModel matchModel : CustomUtil.emptyIfNull(matchModelList)) {
                        if (!TextUtils.isEmpty(arr[2].trim())){
                            if (arr[2].trim().equalsIgnoreCase(matchModel.getId())) {
                                if (arr[3]!=null){
                                    linkData=null;
                                    preferences.setMatchModel(matchModel);

                                    Intent intent = new Intent(mContext, ContestListActivity.class);
                                    intent.putExtra("link_contest_id",arr[3]);
                                    mContext.startActivity(intent);

                                    break;
                                }
                            }
                        }
                    }
                }
                else if (arr[0].trim().equalsIgnoreCase(ConstantUtil.LINK_PREFIX_DEPOSIT)){
                    String amt=arr[1];
                    if (TextUtils.isEmpty(amt)){
                        amt="";
                    }
                    linkData=null;

                    Intent intent = new Intent(mContext, AddDepositActivity.class);
                    intent.putExtra("isJoin",false);
                    intent.putExtra("depositAmt",amt);
                    startActivity(intent);
                }
                else if (arr[0].trim().equalsIgnoreCase(ConstantUtil.LINK_PREFIX_TRADING) ||
                        arr[0].trim().equalsIgnoreCase(ConstantUtil.LINK_PREFIX_POLL) ||
                        arr[0].trim().equalsIgnoreCase(ConstantUtil.LINK_PREFIX_OPINION)){
                    String conType=arr[0];
                    String conId=arr[1];
                    linkData=null;
                    LogUtil.e("resp","TradData BasketBall: "+arr[0]+" : "+conId+" : "+conType);
                    //startActivity(new Intent(mContext, TOPActivity.class));
                    startActivity(new Intent(mContext, TOPActivity.class)
                            .putExtra("contest_id",conId)
                            .putExtra("conType",conType)
                    );
                }
            }
        }
    }

}