package com.fantafeat.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.util.CustomUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class WithdrawFragment extends BaseFragment {

    TabLayout withdraw_tablayout;
    ViewPager2 withdraw_pager;
    Button withdraw_btn;
    TextView total_winning_bal;
    float winning_bal = 0;
    private final List<String> mFragmentTitleList = new ArrayList<>();
    WithdrawPagerAdapter withdrawPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_withdraw, container, false);
        initFragment(view);
        initToolBar(view, "Withdraw", true);
        return view;
    }

    @Override
    public void initControl(View view) {
        withdraw_tablayout = view.findViewById(R.id.withdraw_tablayout);
        withdraw_pager = view.findViewById(R.id.withdraw_pager);
        //withdraw_btn = view.findViewById(R.id.withdraw_btn);
        total_winning_bal = view.findViewById(R.id.total_winning_bal);
        mFragmentTitleList.add("Bank Transfer");
        mFragmentTitleList.add("Paytm Wallet");
        withdrawPagerAdapter = new WithdrawPagerAdapter(this);
        withdraw_pager.setAdapter(withdrawPagerAdapter);
        new TabLayoutMediator(withdraw_tablayout, withdraw_pager,
                (tab, position) -> tab.setText(mFragmentTitleList.get(position))).attach();


        withdraw_pager.setUserInputEnabled(false);
        winning_bal = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());

        total_winning_bal.setText(mContext.getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(winning_bal));

    }

    @Override
    public void initClick() {

    }

    public class WithdrawPagerAdapter extends FragmentStateAdapter {

        public WithdrawPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return  BankWithdrawFragment.newInstance(false,false);
            } else if (position == 1) {
                return BankWithdrawFragment.newInstance(false,false);//PaytmWithdrawFragment();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }


}