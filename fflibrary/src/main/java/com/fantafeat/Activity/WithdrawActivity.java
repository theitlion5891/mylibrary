package com.fantafeat.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantafeat.Fragment.BankWithdrawFragment;
import com.fantafeat.Model.UpdateModel;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WithdrawActivity extends BaseActivity {

    TabLayout withdraw_tablayout;
    ViewPager2 withdraw_pager;
    ImageView toolbar_back;
    public TextView total_winning_bal,toolbar_title;
    public float winning_bal = 0;
    private final List<String> mFragmentTitleList = new ArrayList<>();
    WithdrawPagerAdapter withdrawPagerAdapter;

   // private Boolean is_instant=false;
    private Boolean is_instant_amount=false;
    private Boolean is_paytm=false;
    FusedLocationProviderClient fusedLocationClient;
    AlertDialog.Builder builder;
    private boolean isDialog = false;

    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        setContentView(R.layout.fragment_withdraw);

        if (getIntent().hasExtra("is_instant_amount")){
            is_instant_amount=getIntent().getBooleanExtra("is_instant_amount",false);
        }

        if (getIntent().hasExtra("isPaytm")){
            if (getIntent().getStringExtra("isPaytm").equalsIgnoreCase("yes")){
                is_paytm=true;
            }
        }

        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        UpdateModel updateModel=preferences.getUpdateMaster();
        ApiManager.isInstant = updateModel.getIs_cf_instant_withdraw().equalsIgnoreCase("yes");
        ApiManager.isPayTm = updateModel.getDisplayDepositPaytmInstant().equalsIgnoreCase("yes");

        getUserDetails();
    }

    private void initData() {
        withdraw_tablayout = findViewById(R.id.withdraw_tablayout);
        withdraw_tablayout.setVisibility(View.GONE);
        withdraw_pager = findViewById(R.id.withdraw_pager);
       //
        //withdraw_btn = findViewById(R.id.withdraw_btn);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_back = findViewById(R.id.toolbar_back);
        total_winning_bal = findViewById(R.id.total_winning_bal);
        mFragmentTitleList.add("Bank Transfer");
        mFragmentTitleList.add("Paytm Wallet");
        withdrawPagerAdapter = new WithdrawPagerAdapter(this);
        withdraw_pager.setAdapter(withdrawPagerAdapter);
        new TabLayoutMediator(withdraw_tablayout, withdraw_pager,
                (tab, position) -> tab.setText(mFragmentTitleList.get(position))).attach();


        if (is_instant_amount){
            toolbar_title.setText("Instant Bank Withdraw");
        }else {
            if (is_paytm){
                toolbar_title.setText("Paytm Instant Withdraw");
            }else {
                toolbar_title.setText("Withdraw");
            }

        }

        withdraw_pager.setUserInputEnabled(false);
        winning_bal = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());

        total_winning_bal.setText(mContext.getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(winning_bal));

        toolbar_back.setOnClickListener(v->{
            finish();
        });

    }


    private void getUserDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("token_no", preferences.getUserModel().getTokenNo());
            Log.e(TAG, "getUserDetails: " + jsonObject.toString());


            HttpRestClient.postJSON(mContext, false, ApiManager.AUTHV3GetUserDetails, jsonObject, new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {
                    Log.e(TAG, "USER onSuccessResult: " + responseBody.toString());
                    if (responseBody.optBoolean("status")) {
                        if (responseBody.optString("code").equals("200")) {
                            JSONObject data = responseBody.optJSONObject("data");
                            UserModel userModel = gson.fromJson(data.toString(), UserModel.class);
                            preferences.setUserModel(userModel);
                            winning_bal=CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
                            total_winning_bal.setText(mContext.getResources().getString(R.string.rs) + preferences.getUserModel().getWinBal());
                        }
                    } /*else {
                        preferences.setUserModel(new UserModel());
                    }*/
                    // UpdatePayment();
                    //  mUserDetail = true;
                }

                @Override
                public void onFailureResult(String responseBody, int code) {
                    Log.e(TAG, "onFailureResult: " );
                    // UpdatePayment();
                    // mUserDetail = true;
                    // preferences.setUserModel(new UserModel());

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class WithdrawPagerAdapter extends FragmentStateAdapter {
        public WithdrawPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        // public WithdrawPagerAdapter() {
           // super(fragmentManager);
       // }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return  BankWithdrawFragment.newInstance(is_instant_amount,is_paytm);
            } else if (position == 1) {
                return BankWithdrawFragment.newInstance(is_instant_amount,is_paytm);//PaytmWithdrawFragment
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

}