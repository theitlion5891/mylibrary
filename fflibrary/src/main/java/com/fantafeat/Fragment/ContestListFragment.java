package com.fantafeat.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.LogUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContestListFragment extends BaseFragment {

    private TabLayout tabLayout;
    private Toolbar toolbar;
    public ImageView mToolBarBack;
    private static ViewPager2 viewPager;
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private TextView match_title, timer;
    private MatchModel selected_match;
    private long diff;
    private CountDownTimer countDownTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contest_list, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initControl(View view) {
        toolbar = view.findViewById(R.id.contest_list_toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(mToolbar);
        match_title = view.findViewById(R.id.match_title);
        timer = view.findViewById(R.id.timer);
        mToolBarBack = view.findViewById(R.id.toolbar_back);

        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.viewpager);

        if(preferences.getMatchModel()!=null){
            selected_match = preferences.getMatchModel();
        }

        if(selected_match.getMatchTitle()!=null){
            match_title.setText(selected_match.getMatchTitle());
        }
        setTimer();

        mFragmentTitleList.add("Matches");
        mFragmentTitleList.add("My Matches");
        mFragmentTitleList.add("My Team");

        //viewPager.setAdapter(createContestAdapter());

        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(mFragmentTitleList.get(position));
                    }
                }).attach();

        // Disable swipe changes and allow to change only by pressing on tab
        viewPager.setUserInputEnabled(false);
    }

    private void setTimer() {
        Date date = null;
        String matchDate = "";

        SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat matchFormate = MyApp.changedFormat("dd MMM yyyy");
        try {
            date = format.parse(selected_match.getRegularMatchStartTime());
            matchDate = matchFormate.format(date);

            diff = date.getTime() - MyApp.getCurrentDate().getTime();

            Log.e(TAG, "onBindViewHolder: " + diff);
        } catch (ParseException e) {
            LogUtil.e(TAG, "onBindViewHolder: " + e.toString());
            e.printStackTrace();
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        /*if (matchModel.getMatchStatus().equals("Pending")) {*/
        countDownTimer = new CountDownTimer(diff, 1000) {
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
                    diff = new DecimalFormat("00").format(elapsedDays) + "d " + new DecimalFormat("00").format(elapsedHours) + "h Left";
                } else if (elapsedHours > 0) {
                    diff = new DecimalFormat("00").format(elapsedHours) + "h " + new DecimalFormat("00").format(elapsedMinutes) + "m";
                } else {
                    diff = new DecimalFormat("00").format(elapsedMinutes) + "m " + new DecimalFormat("00").format(elapsedSeconds) + "s";
                }
                timer.setText(diff);
            }

            public void onFinish() {

                // Do something on finish
            }
        }.start();
        /*} else {
            holder.match_remain_time.setTextSize(14);
            holder.match_remain_time.setText(List.getMatchStatus());
        }*/
    }

    /*private RecyclerView.Adapter createContestAdapter() {
        ContestListViewPagerAdapter adapter = new ContestListViewPagerAdapter( preferences.getMatchData());
        return adapter;
    }*/

    @Override
    public void initClick() {
        mToolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveFragment();
            }
        });

    }

    public void setMyTeamCount(String count){
        tabLayout.getTabAt(2).setText("My Team("+count+")");
    }

}