package com.fantafeat.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.fantafeat.Fragment.ContestListFragment;
import com.fantafeat.Fragment.CricketSelectPlayerFragment;
import com.fantafeat.Fragment.HomeActivityFragment;
import com.fantafeat.Fragment.MyLeagueFragment;
import com.fantafeat.Model.MenuModel;
import com.fantafeat.Model.SportsModel;
import com.fantafeat.Model.UpdateModel;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.FragmentUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.PrefConstant;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainActivity";
    BottomNavigationView bottomNavigationView;

    FrameLayout frameLayout;

    private String selectedTab = "home",user_id="";

    private JSONObject currentData;

    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.inflateMenu(R.menu.bottom_nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setItemIconTintList(null);

        frameLayout = findViewById(R.id.home_fragment_container);

        preferences.setPref(PrefConstant.IS_BANNER_CLICK,false);

        user_id=getIntent().getStringExtra("user_id");

        getUserDetails();


    }

    private void getUserDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", user_id);
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, true, ApiManager.getUserDetailsOtherCompanyLogin, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "getUserDetails: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    if (responseBody.optString("code").equals("200")) {
                        JSONObject data = responseBody.optJSONObject("data");
                        UserModel userModel = gson.fromJson(data.toString(), UserModel.class);
                        MyApp.USER_ID = userModel.getId();
                        MyApp.APP_KEY = userModel.getTokenNo();

                        preferences.setUserModel(userModel);

                        getAppDataList();

                    }
                } else {
                    preferences.setUserModel(new UserModel());
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                Log.e(TAG, "onFailureResult: ");
                // mUserDetail = true;
                preferences.setUserModel(new UserModel());

            }
        });
    }

    private void getAppDataList() {
        try {
            JSONObject param = new JSONObject();
            if (preferences.getUserModel() != null && !TextUtils.isEmpty(preferences.getUserModel().getId())) {
                param.put("user_id", preferences.getUserModel().getId());
                param.put("token_no", preferences.getUserModel().getTokenNo());
            }
            LogUtil.e(TAG, "getAppDataList Param: " + param.toString());
            HttpRestClient.postJSON(mContext, true, ApiManager.appDataList, param, new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {
                    LogUtil.e(TAG, "getAppDataList: " + responseBody.toString());
                    if (responseBody.optBoolean("status")) {
                        currentData = responseBody;

                        setOtherData();

                        new FragmentUtil().replaceFragment((FragmentActivity) mContext,
                                R.id.home_fragment_container,
                                HomeActivityFragment.newInstance(),
                                ((HomeActivity) mContext).fragmentTag(0),
                                FragmentUtil.ANIMATION_TYPE.NONE);
                    }
                }

                @Override
                public void onFailureResult(String responseBody, int code) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setOtherData() {
        if (currentData != null) {
            JSONObject dynamic_key = currentData.optJSONObject("dynamic_key");
            JSONArray menu_list = currentData.optJSONArray("menu_list");
            JSONArray sport_list = currentData.optJSONArray("sport_list");

            if (dynamic_key != null) {
                UpdateModel updateModel = gson.fromJson(dynamic_key.toString(), UpdateModel.class);

                ApiManager.play_store_app = updateModel.getIs_play_store().equalsIgnoreCase("yes");
                ApiManager.isInstant = updateModel.getIs_cf_instant_withdraw().equalsIgnoreCase("yes");
                ApiManager.IS_SQLITE_ENABLE = updateModel.getSql_lite_enable().equalsIgnoreCase("yes");
                ApiManager.isPayTm = updateModel.getDisplayDepositPaytmInstant().equalsIgnoreCase("yes");
                MyApp.user_header_key = updateModel.getUser_header_key();
                preferences.setPref(PrefConstant.user_header_key, MyApp.user_header_key);

                if (TextUtils.isEmpty(updateModel.getImage_base_path_url())) {
                    MyApp.imageBase = ApiManager.IMAGEBASE;
                    preferences.setPref(PrefConstant.IMAGE_BASE, MyApp.imageBase);
                } else {
                    MyApp.imageBase = updateModel.getImage_base_path_url();
                    preferences.setPref(PrefConstant.IMAGE_BASE, MyApp.imageBase);
                }

                preferences.setUpdateMaster(updateModel);
            }

            if (menu_list != null) {
                final List<MenuModel> menuModals = new ArrayList<>();
                for (int i = 0; i < menu_list.length(); i++) {
                    JSONObject object = menu_list.optJSONObject(i);
                    if (!object.optString("key").equals("menu_dashboard")) {
                        MenuModel menuModel = gson.fromJson(CustomUtil.replaceNull(object.toString()), MenuModel.class);
                        menuModals.add(menuModel);
                    }
                }
                preferences.setMenu(menuModals);
            }

            if (sport_list != null) {
                final List<SportsModel> sportsModels = new ArrayList<>();
                for (int i = 0; i < sport_list.length(); i++) {
                    JSONObject data = sport_list.optJSONObject(i);
                    SportsModel sportsModel = gson.fromJson(data.toString(), SportsModel.class);
                    sportsModels.add(sportsModel);
                }
                preferences.setSports(sportsModels);
            }
        }

        //checkLogin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (mSocket != null && mSocket.connected()) {
            mSocket.disconnect();
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        int itemId = item.getItemId();
        if (itemId == R.id.home) {
            if (!selectedTab.equals("home")) {
                selectedTab = "home";
                new FragmentUtil().replaceFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        HomeActivityFragment.newInstance(),
                        ((HomeActivity) mContext).fragmentTag(0),
                        FragmentUtil.ANIMATION_TYPE.NONE);
            }
        }
        else if (itemId == R.id.my_league) {
            if (!selectedTab.equals("league")) {
                selectedTab = "league";
                new FragmentUtil().replaceFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        MyLeagueFragment.newInstance(),
                        ((HomeActivity) mContext).fragmentTag(1),
                        FragmentUtil.ANIMATION_TYPE.NONE);
            }
        }

        return true;
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HandelBackPress();
    }

    private void HandelBackPress() {
        Log.e(TAG, "HandelBackPress: ");
        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.home_fragment_container);
        if (currentFrag == null) {
            Log.e(TAG, "HandelBackPress: " + "here");
            return;
        }
        Log.e(TAG, "HandelBackPress:" + currentFrag.getTag());

        if (currentFrag.getTag().equals(fragmentTag(12))) {
            ((ContestListFragment) currentFrag).RemoveFragment();
        } else if (currentFrag.getTag().equals(fragmentTag(13))) {
            ((CricketSelectPlayerFragment) currentFrag).RemoveFragment();
        }  else {
            finish();
            //quitApp(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String fragmentTag(int position) {
        String[] drawer_tag = getResources().getStringArray(R.array.stack);
        return drawer_tag[position];
    }

    public void HideBottomNavigationBar() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    public void ShowBottomNavigationBar() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private void SwitchFragment(MenuModel menuData) {
        switch (menuData.getMenuKey()) {
            case "find_people":
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new FragmentUtil().addFragment(HomeActivity.this,
                                R.id.fragment_container,
                                new FindPeopleFragment(),
                                fragmentTag(25),
                                FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                    }
                }, 200);*/
                break;
            case "my_profile":

                break;
            case "menu_leaderboard":

                break;
            case "view_winner":
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new FragmentUtil().addFragment(HomeActivity.this,
                                R.id.fragment_container,
                                new ViewWinnersFragment(),
                                fragmentTag(27),
                                FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                    }
                }, 200);*/
                break;
            case "my_balance":
                bottomNavigationView.setSelectedItemId(R.id.wallet);
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new FragmentUtil().addFragment(HomeActivity.this,
                                R.id.fragment_container,
                                StatsFragment.newInstance(preferences.getUserModel().getData().getId(), true),
                                fragmentTag(3),
                                FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                    }
                }, 200);*/
                break;
            case "my_offers_coupons":
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new FragmentUtil().addFragment(HomeActivity.this,
                                R.id.fragment_container,
                                new MyOfferCouponsFragment(),
                                fragmentTag(28),
                                FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                    }
                }, 200);*/
                break;
            case "invite_friends":

                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new FragmentUtil().addFragment(HomeActivity.this,
                                R.id.fragment_container,
                                new InviteFriendsFragment(),
                                fragmentTag(31),
                                FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                    }
                }, 200);*/
                break;
            case "my_setting":
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new FragmentUtil().addFragment(HomeActivity.this,
                                R.id.fragment_container,
                                new MyInfoAndSettingFragment(),
                                fragmentTag(29),
                                FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                    }
                }, 200);*/
                break;

            case "point_system":

               /* new FragmentUtil().addFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new WebViewFragment("Fantasy Points System", "https://www.batball11.com/fantasy_point.html"),
                        ((HomeActivity) mContext).fragmentTag(17),
                        FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);*/
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new FragmentUtil().addFragment((FragmentActivity) mContext,
                                R.id.fragment_container,
                                new WebViewFragment("Fantasy Points System", "https://www.batball11.com/fantasy_point.html"),
                                ((HomeActivity) mContext).fragmentTag(24),
                                FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                    }
                }, 200);*/
                break;
            case "more":
                bottomNavigationView.setSelectedItemId(R.id.more);
              /*  new FragmentUtil().replaceFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new MoreFragment(),
                        ((HomeActivity) mContext).fragmentTag(16),
                        FragmentUtil.ANIMATION_TYPE.NONE);*/
                break;
            case "log_out":
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // preferences.logout();
                        //quitApp(false);
                       /* preferences.setPref(PrefConstant.APP_IS_LOGIN,false);
                        FCMHandler.disableFCM();
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                        finish();*/
                    }
                }, 200);
                break;
            default:
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new FragmentUtil().addFragment(HomeActivity.this,
                                R.id.fragment_container,
                                MenuWebFragment.newInstance(menuData.getUrl(), menuData.getName()),
                                fragmentTag(4),
                                FragmentUtil.ANIMATION_TYPE.SLIDE_LEFT_TO_RIGHT);
                    }
                }, 300);*/
                break;
        }

    }

    private class NavigationDrawerAdapter extends BaseAdapter {

        private List<MenuModel> menuData;
        private NavDrawHolder holder;

        NavigationDrawerAdapter(List<MenuModel> menuData) {
            this.menuData = menuData;
        }

        @Override
        public int getCount() {
            return menuData.size();
        }

        @Override
        public Object getItem(int position) {
            return menuData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.row_menu, parent, false);
                holder = new NavDrawHolder(view);
                LogUtil.e(TAG, "getView: here");
                view.setTag(holder);
            } else {
                holder = (NavDrawHolder) view.getTag();
            }
            if (menuData.get(position).getDisplaySeparator().equalsIgnoreCase("Yes")) {
                holder.menu_separator.setVisibility(View.VISIBLE);
            } else {
                holder.menu_separator.setVisibility(View.GONE);
            }

            holder.menu_title.setText(menuData.get(position).getMenuName());
            int res = getResources().getIdentifier(menuData.get(position).getMenuKey(), "drawable", getPackageName());
            LogUtil.e(TAG, "getView: " + res + "->" + position + "->" + menuData.get(position).getMenuKey());
            if (res == 0 && !menuData.get(position).getMenuImg().trim().equals("")) {
                LogUtil.e(TAG, "getView: " + menuData.get(position).getMenuImg());
                LogUtil.e(TAG, "getView: " + menuData.get(position).getMenuName());
                CustomUtil.loadImageWithGlide(mContext, holder.menu_img, "", menuData.get(position).getMenuImg(), R.drawable.team1);
             /*   Glide.with(mContext)
                        .load(menuData.get(position).getMenuImg())
                        .error(R.drawable.team1)
                        .placeholder(R.drawable.team1)
                        .into(holder.menu_img);*/

            } else {
                Glide.with(mContext)
                        .load(res)
                        .error(R.drawable.team1)
                        .placeholder(R.drawable.team1)
                        .into(holder.menu_img);
                //holder.menu_img.setImageResource(res);
            }

            LogUtil.e(TAG, "getView: Here1");
            holder.menu_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MyApp.getClickStatus()) {
                        SwitchFragment(menuData.get(position));
                    }
                }
            });
            return view;
        }

        private class NavDrawHolder {
            LinearLayout menu_layout;
            TextView menu_title;
            ImageView menu_img;
            View menu_separator;

            public NavDrawHolder(View view) {
                menu_layout = view.findViewById(R.id.menu_layout);
                menu_title = view.findViewById(R.id.menu_title);
                menu_img = view.findViewById(R.id.menu_img);
                menu_separator = view.findViewById(R.id.menu_separator);

            }
        }
    }



}