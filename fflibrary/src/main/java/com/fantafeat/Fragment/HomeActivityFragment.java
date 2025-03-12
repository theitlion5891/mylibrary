package com.fantafeat.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
//import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;


import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.Adapter.ViewPagerAdapter;
import com.fantafeat.Model.SportsModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.PrefConstant;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;


public class HomeActivityFragment extends BaseFragment {

    private TabLayout tabs;
    private static ViewPager2 viewPager;
    private Toolbar toolbar;
    public ImageView mToolbarMenu;
    public List<SportsModel> sportList;
    //private ImageView notification;

    public String sportsId="1";

    public HomeActivityFragment(){

    }

    public static HomeActivityFragment newInstance() {
        HomeActivityFragment myFragment=new HomeActivityFragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        setStatusBarRed();
    }


    @Override
    public void initControl(View view) {
        toolbar = view.findViewById(R.id.home_actionbar);
        ((HomeActivity)getActivity()).setSupportActionBar(mToolbar);

        tabs = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.viewpager);
        mToolbarMenu = view.findViewById(R.id.drawer_image);
        //notification = view.findViewById(R.id.notification);
        sportList = preferences.getSports();

        viewPager.setAdapter(createAdapter());


        String spId="";

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

        if (selTab==-1){
            selTab=0;
        }

        viewPager.setCurrentItem(selTab,false);
        viewPager.setUserInputEnabled(false);
        ConstantUtil.reduceDragSensitivity(viewPager);

    }

    @Override
    public void initClick() {

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
                    tabs.getTabAt(i).setText(sportList.get(i).getSportName()).setIcon(res);
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

    private ViewPagerAdapter createAdapter() {
       return new ViewPagerAdapter(this,preferences.getSports());
    }


}