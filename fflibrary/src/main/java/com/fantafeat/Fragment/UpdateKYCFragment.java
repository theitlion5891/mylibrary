package com.fantafeat.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class UpdateKYCFragment extends BaseFragment {

    ViewPager2 kyc_viewpager;
    TabLayout kyc_tabs;
    private final List<String> mFragmentTitleList = new ArrayList<>();
    Fragment currentFrg;
    private static UpdateKYCFragment instance = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_k_y_c, container, false);
        instance = this;
        initFragment(view);
        initToolBar(view, "KYC Details", true);
        return view;
    }

    public static UpdateKYCFragment getInstance() {
        return instance;
    }

    @Override
    public void initControl(View view) {
        kyc_tabs = view.findViewById(R.id.kyc_tabs);
        kyc_viewpager = view.findViewById(R.id.kyc_viewpager);

        mFragmentTitleList.add("PAN");
        mFragmentTitleList.add("Bank Account");
        KYCViewPager adapter = new KYCViewPager(UpdateKYCFragment.this);

        kyc_viewpager.setAdapter(adapter);

        new TabLayoutMediator(kyc_tabs, kyc_viewpager,
                (tab, position) -> tab.setText(mFragmentTitleList.get(position))).attach();

        kyc_viewpager.setUserInputEnabled(false);

    }

    public Fragment getCurrentFrg() {
        return currentFrg;
    }

    public void setCurrentFrg(Fragment currentFrg) {
        this.currentFrg = currentFrg;
    }

    @Override
    public void initClick() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class KYCViewPager extends FragmentStateAdapter {

        Fragment fragment;

        public KYCViewPager(@NonNull Fragment fragment) {
            super(fragment);
            this.fragment = fragment;

        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            /*if (position == 0) {
                setCurrentFrg(new PanDetailsFragment());
                return new PanDetailsFragment();
            } else if (position == 1) {
                setCurrentFrg(new BankAccountDetailsFragment());
                return new BankAccountDetailsFragment();
            }*/
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

}