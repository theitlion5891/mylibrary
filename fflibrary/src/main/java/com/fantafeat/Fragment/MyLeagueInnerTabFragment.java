package com.fantafeat.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.GetGroupDetail;
import com.fantafeat.util.LogUtil;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MyLeagueInnerTabFragment extends BaseFragment {


    private TabLayout league_inner_tabs;
    private ViewPager2 league_inner_viewpager;
    private final List<String> mFragmentTitleList = new ArrayList<>();
    UpcomingLeagueFragment upcomingLeagueFragment;
    LiveLeagueFragment liveLeagueFragment;
    CompletedLeagueFragment completedLeagueFragment;
    private String sportId="";
    //private Socket mSocket= null;
    public MyLeagueInnerTabFragment(){
        //this.sportId=id;
      //  LogUtil.d("selSports",sportId);
    }
    public static MyLeagueInnerTabFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        MyLeagueInnerTabFragment f = new MyLeagueInnerTabFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sportId = getArguments().getString("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_league_inner_tab, container, false);
        initFragment(view);

        mFragmentTitleList.add("Upcoming");
        mFragmentTitleList.add("Live");
        mFragmentTitleList.add("Completed");
        league_inner_viewpager.setAdapter(createAdapter());
        new TabLayoutMediator(league_inner_tabs, league_inner_viewpager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(mFragmentTitleList.get(position));
                    }
                }).attach();

        league_inner_viewpager.setOffscreenPageLimit(3);
        league_inner_viewpager.setCurrentItem(0);
        league_inner_viewpager.setUserInputEnabled(ApiManager.isPagerSwipe);
        ConstantUtil.reduceDragSensitivity(league_inner_viewpager);

        return view;
    }

    private LeagueInnerViewPaggerAdapter createAdapter() {
        LeagueInnerViewPaggerAdapter adapter = new LeagueInnerViewPaggerAdapter((HomeActivity)mContext);
        return adapter;
    }

    @Override
    public void initControl(View view) {
        league_inner_tabs = view.findViewById(R.id.my_league_inner_tabs);
        league_inner_viewpager = view.findViewById(R.id.my_league_inner_viewpager);
    }

    @Override
    public void initClick() {

        league_inner_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    if(upcomingLeagueFragment != null) {
                        upcomingLeagueFragment.getData();
                    }
                }else if(tab.getPosition() == 1){
                    if(liveLeagueFragment != null) {
                        liveLeagueFragment.getData();
                        initSocket("livetab");
                    }
                }else if(tab.getPosition() == 2) {
                    if(completedLeagueFragment != null) {
                        completedLeagueFragment.offset = 0;
                        completedLeagueFragment.getData();
                        completedLeagueFragment.matchAfterModelList = new ArrayList<>();
                        initSocket("completedtab");
                    }
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

    private void initSocket(String title){
       /* mSocket = MyApp.getInstance().getSocketInstance();
        if (mSocket!=null){
            if (!mSocket.connected())
                mSocket.connect();
            try {
                JSONObject obj=new JSONObject();
                if (preferences.getUserModel()!=null){
                    obj.put("user_id",preferences.getUserModel().getId());
                }
                obj.put("title",title);
                mSocket.emit("connect_user",obj);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }*/
    }

    public class LeagueInnerViewPaggerAdapter extends FragmentStateAdapter {

        public LeagueInnerViewPaggerAdapter(FragmentActivity fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            switch (position){
                case 0:
                    return upcomingLeagueFragment = UpcomingLeagueFragment.newInstance(sportId);
                case 1:
                    return liveLeagueFragment = LiveLeagueFragment.newInstance(sportId);
                case 2:
                    return completedLeagueFragment = CompletedLeagueFragment.newInstance(sportId);
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

}