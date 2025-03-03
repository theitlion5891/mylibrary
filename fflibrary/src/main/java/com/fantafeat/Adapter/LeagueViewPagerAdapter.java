package com.fantafeat.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.fantafeat.Fragment.MyLeagueInnerTabFragment;
import com.fantafeat.Model.SportsModel;

import java.util.List;


public class LeagueViewPagerAdapter extends FragmentStateAdapter {

    private List<SportsModel> allSports;


    public LeagueViewPagerAdapter(@NonNull Fragment fragment, List<SportsModel> sports) {
        super(fragment);
        allSports = sports;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        SportsModel sportsModel = allSports.get(position);
        return MyLeagueInnerTabFragment.newInstance(sportsModel.getId());
        //  LogUtil.d("resp",sportsModel.getId()+"   "+sportsModel.getSportName()+"  "+position);
       /* if(Integer.parseInt(sportsModel.getId()) == 1) {
            return new MyLeagueInnerTabFragment();
        }else if(Integer.parseInt(sportsModel.getId()) == 2) {
            return new MyLeagueInnerTabFragment();
        }else if(Integer.parseInt(sportsModel.getId()) == 3) {
            return new MyLeagueInnerTabFragment();
        }else if(Integer.parseInt(sportsModel.getId()) == 4) {
            return new MyLeagueInnerTabFragment();
        }else if(Integer.parseInt(sportsModel.getId()) == 5) {
            return new MyLeagueInnerTabFragment();
        }
        return null;*/
    }

    @Override
    public int getItemCount() {
        return  allSports.size();
    }
}
