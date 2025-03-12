package com.fantafeat.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.fantafeat.Fragment.CricketHomeFragment;
import com.fantafeat.Model.SportsModel;

import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
/*private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();*/
    private List<SportsModel> allSports;
    //private Fragment fragment;


    /*public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }*/

    public ViewPagerAdapter(@NonNull Fragment fragment, List<SportsModel> sports) {
        super(fragment);
        this.allSports = sports;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        SportsModel sportsModel = allSports.get(position);
      //  LogUtil.d("resp",sportsModel.getId()+"   "+sportsModel.getSportName()+"  "+position);
        return CricketHomeFragment.newInstance(sportsModel.getId());//fragment

       /* if(Integer.parseInt(sportsModel.getId()) == 1) {
            return CricketHomeFragment.newInstance(sportsModel.getId(),linkData*//*,fragment*//*);
        }else if(Integer.parseInt(sportsModel.getId()) == 2) {
            return FootballHomeFragment.newInstance(sportsModel.getId(),linkData*//*,fragment*//*);
        }else if(Integer.parseInt(sportsModel.getId()) == 3) {
            return BaseballHomeFragment.newInstance(sportsModel.getId(),linkData*//*,fragment*//*);
        }else if(Integer.parseInt(sportsModel.getId()) == 4) {
            return BasketballHomeFragment.newInstance(sportsModel.getId(),linkData*//*,fragment*//*);
        }else if(Integer.parseInt(sportsModel.getId()) == 5) {
            return VollyballHomeFragment.newInstance(sportsModel.getId(),linkData*//*,fragment*//*);
        }else if(Integer.parseInt(sportsModel.getId()) == 7) {
            return KabaddiFragment.newInstance(sportsModel.getId(),linkData*//*,fragment*//*);
        }else {
            return KabaddiFragment.newInstance(sportsModel.getId(),linkData*//*,fragment*//*);
        }*/
        /*else if(position==4) {
            return new F1HomeFragment();
        }else if(position==5) {
            return new GolfHomeFragment();
        }else if(position==6) {
            return new HorseRacingHomeFragment();
        }*/
        //return null;

    }

    @Override
    public int getItemCount() {
        return allSports.size();
    }

}

