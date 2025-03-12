package com.fantafeat.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager2.widget.ViewPager2;


import com.fantafeat.Adapter.LeagueViewPagerAdapter;
import com.fantafeat.Model.SportsModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.PrefConstant;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;


public class MyLeagueFragment extends BaseFragment {
    private TabLayout league_tabs;
    private ViewPager2 league_viewpager;
    public ImageView mToolbarMenu;
    public List<SportsModel> sportList;

    public static MyLeagueFragment newInstance() {
        return new MyLeagueFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setStatusBarDark();
        View view =  inflater.inflate(R.layout.fragment_my_league, container, false);
        initFragment(view);
        return view;
    }

    private LeagueViewPagerAdapter createAdapter() {
        LeagueViewPagerAdapter adapter = new LeagueViewPagerAdapter(this,preferences.getSports());
        return adapter;
    }


    @Override
    public void initControl(View view) {
        mToolbarMenu = view.findViewById(R.id.drawer_image);
        league_tabs = view.findViewById(R.id.my_league_tabs);
        league_viewpager = view.findViewById(R.id.my_league_viewpager);

        sportList = preferences.getSports();

        league_viewpager.setAdapter(createAdapter());

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

    }

    @Override
    public void initClick() {

        league_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                league_viewpager.setCurrentItem(tab.getPosition(),false);
                preferences.setPref(PrefConstant.CURRENT_TAB,tab.getPosition());
                preferences.setPref(PrefConstant.MATCH_SELECTED_TAB,tab.getPosition());

                for (int i =0;i<sportList.size();i++){
                    Log.e("tabImg",sportList.get(i).getSportImg());
                    int res = getResources().getIdentifier(sportList.get(i).getSportImg(), "drawable", mContext.getPackageName());
                    league_tabs.getTabAt(i).setText(sportList.get(i).getSportName()).setIcon(res);
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
}