package com.fantafeat.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;


import com.bumptech.glide.Glide;
import com.fantafeat.Activity.TOPActivity;
import com.fantafeat.Adapter.LeagueViewPagerAdapter;
import com.fantafeat.BuildConfig;
import com.fantafeat.Model.SportsModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.GetGroupDetail;
import com.fantafeat.util.PrefConstant;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;


public class MyLeagueFragment extends BaseFragment {


    private TabLayout league_tabs;
    private ViewPager2 league_viewpager;

    private Toolbar toolbar;
    public ImageView mToolbarMenu,imgTrade;
    private final List<String> mFragmentTitleList = new ArrayList<>();
    public List<SportsModel> sportList;

    GetGroupDetail mCallback;
    private int[] tabIcons = {
            R.drawable.tab_cricket,
            R.drawable.tab_football,
            R.drawable.tab_vollyball,
            R.drawable.ic_american_football,
    };
    public String sportsId="1";



    public static MyLeagueFragment newInstance() {
        return new MyLeagueFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setStatusBarDark();
        View view =  inflater.inflate(R.layout.fragment_my_league, container, false);
        initFragment(view);

        return view;
    }

    private LeagueViewPagerAdapter createAdapter() {
        LeagueViewPagerAdapter adapter = new LeagueViewPagerAdapter(this,preferences.getSports());
        return adapter;
    }

    @SuppressLint("ResourceType")
    private void setupTabIcons() {
        league_tabs.getTabAt(0).setIcon(tabIcons[0]);
        league_tabs.getTabAt(1).setIcon(tabIcons[1]);
        league_tabs.getTabAt(2).setIcon(tabIcons[2]);
        league_tabs.getTabAt(3).setIcon(tabIcons[3]);
        league_tabs.getTabAt(4).setIcon(tabIcons[4]);
        league_tabs.getTabAt(5).setIcon(tabIcons[5]);
        league_tabs.getTabAt(6).setIcon(tabIcons[6]);

    }

    @Override
    public void initControl(View view) {
        mToolbarMenu = view.findViewById(R.id.drawer_image);
        league_tabs = view.findViewById(R.id.my_league_tabs);
        league_viewpager = view.findViewById(R.id.my_league_viewpager);
        imgTrade = view.findViewById(R.id.imgTrade);

        /*toolbar = view.findViewById(R.id.league_toolbar);
        ((HomeActivity)getActivity()).setSupportActionBar(toolbar);*/
        sportList = preferences.getSports();

        if (preferences.getUpdateMaster().getIs_top_enable()!=null &&
                preferences.getUpdateMaster().getIs_top_enable().equalsIgnoreCase("yes") && !BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE)){
            imgTrade.setVisibility(View.VISIBLE);
//            Glide.with(mContext).asGif().load(R.raw.trade_x_launcher).into(imgTrade);
        }else {
            imgTrade.setVisibility(View.GONE);
        }



        /*mFragmentTitleList.add("Cricket");
        mFragmentTitleList.add("Football");
        mFragmentTitleList.add("Basketball");
        mFragmentTitleList.add("Rugby");
        mFragmentTitleList.add("F-1");
        mFragmentTitleList.add("Golf");
        mFragmentTitleList.add("Horse Racing");

        if (preferences.getUserModel()!=null && !TextUtils.isEmpty(preferences.getUserModel().getUserImg())){
            Glide.with(mContext)
                    .load(ApiManager.PROFILE_IMG +preferences.getUserModel().getUserImg())
                    .placeholder(R.drawable.user_image)
                    .into(mToolbarMenu);
        }*/

        league_viewpager.setAdapter(createAdapter());

        //get Tab from HomeActivityFragment and set here
       /* int current_tab = preferences.getPrefInt(PrefConstant.CURRENT_TAB);
        if( current_tab != -1){
            league_viewpager.setCurrentItem(current_tab,false);
        }else
        {
            league_viewpager.setCurrentItem(0,false);
        }*/

        new TabLayoutMediator(league_tabs, league_viewpager,
                (tab, position) -> {
                    SportsModel sportsModel=sportList.get(position);
                    int res = getResources().getIdentifier(sportsModel.getSportImg(), "drawable", mContext.getPackageName());
                    if(res != 0) {
                        tab.setText(sportsModel.getSportName()).setIcon(res);
                    }else{
                        tab.setText(sportsModel.getSportName());
                    }
        }).attach();

        int selTab=preferences.getPrefInt(PrefConstant.MATCH_SELECTED_TAB);//LEAGUE_SELECTED_TAB

        if (selTab==-1){
            selTab=0;
        }

        league_viewpager.setCurrentItem(selTab,false);
        league_viewpager.setUserInputEnabled(ApiManager.isPagerSwipe);
        ConstantUtil.reduceDragSensitivity(league_viewpager);

       /* league_tabs.getTabAt(0).setIcon(R.drawable.ic_selected_cricket);
        league_viewpager.setUserInputEnabled(false);*/
        //league_viewpager.setOffscreenPageLimit(5);

    }

    @Override
    public void initClick() {

        imgTrade.setOnClickListener(v -> {
            startActivity(new Intent(mContext, TOPActivity.class));
        });

        mToolbarMenu.setOnClickListener(view -> OpenNavDrawer());

        league_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                league_viewpager.setCurrentItem(tab.getPosition(),false);
                preferences.setPref(PrefConstant.CURRENT_TAB,tab.getPosition());
               // preferences.setPref(PrefConstant.LEAGUE_SELECTED_TAB,tab.getPosition());
                preferences.setPref(PrefConstant.MATCH_SELECTED_TAB,tab.getPosition());

                Log.e(TAG, "onTabSelected: "+ tab.getPosition() );

                for (int i =0;i<sportList.size();i++){
                    Log.e("tabImg",sportList.get(i).getSportImg());
                    int res = getResources().getIdentifier(sportList.get(i).getSportImg(), "drawable", mContext.getPackageName());
                   /* if (sportList.get(i).getId().equalsIgnoreCase("5")){
                        league_tabs.getTabAt(i).setText("Handball").setIcon(R.drawable.tab_handball);
                    }else {
                        league_tabs.getTabAt(i).setText(sportList.get(i).getSportName()).setIcon(res);
                    }*/
                    league_tabs.getTabAt(i).setText(sportList.get(i).getSportName()).setIcon(res);
                }

                /*if (tab.getText().toString().equals("Cricket")){
                    tab.setIcon(R.drawable.ic_selected_cricket);
                }
                if (tab.getText().toString().equals("Football")){
                    tab.setIcon(R.drawable.ic_selected_football);
                }
                if (tab.getText().toString().equals("Baseball")){
                    tab.setIcon(R.drawable.ic_selected_baseball);
                }
                if (tab.getText().toString().equals("Basketball")){
                    tab.setIcon(R.drawable.ic_selected_basketball);
                }
                if (tab.getText().toString().equals("Volleyball")){
                    tab.setIcon(R.drawable.ic_selected_nba);
                }*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}