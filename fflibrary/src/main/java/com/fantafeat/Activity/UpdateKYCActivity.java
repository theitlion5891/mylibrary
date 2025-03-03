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

import com.fantafeat.BuildConfig;
import com.fantafeat.Fragment.AddressVerificationFragment;
import com.fantafeat.Fragment.BankAccountDetailsFragment;
import com.fantafeat.Fragment.PanDetailsFragment;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.LogUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UpdateKYCActivity extends BaseActivity {

    ViewPager2 kyc_viewpager;
    TabLayout kyc_tabs;
    private final List<String> mFragmentTitleList = new ArrayList<>();
    public static Fragment currentFrg;
    TextView toolbar_title;
    ImageView toolbar_back;
    PanDetailsFragment panDetailsFragment;
    BankAccountDetailsFragment bankAccountDetailsFragment;
    AddressVerificationFragment addressVerificationFragment;
    FusedLocationProviderClient fusedLocationClient;
    AlertDialog.Builder builder;
    private boolean isDialog = false;
    private String verify_tag="";
    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        setContentView(R.layout.fragment_update_k_y_c);

        initData();
    }

    private void initData() {
        kyc_tabs = findViewById(R.id.kyc_tabs);
        kyc_viewpager = findViewById(R.id.kyc_viewpager);
        toolbar_back = findViewById(R.id.toolbar_back);
        toolbar_title = findViewById(R.id.toolbar_title);

        toolbar_title.setText("KYC Details");

        toolbar_back.setOnClickListener(v->{
            finish();
        });

        mFragmentTitleList.add("PAN");
        mFragmentTitleList.add("Bank Account");
        mFragmentTitleList.add("Address Verification");
        KYCViewPager adapter = new KYCViewPager(this);

        if (getIntent().hasExtra("verify_tag")) {
            verify_tag = getIntent().getStringExtra("verify_tag");
            kyc_tabs.setVisibility(View.GONE);
        }
        else {
            kyc_tabs.setVisibility(View.VISIBLE);
        }

        kyc_viewpager.setAdapter(adapter);

        new TabLayoutMediator(kyc_tabs, kyc_viewpager, new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(mFragmentTitleList.get(position));
                    }
                }).attach();

        kyc_viewpager.setUserInputEnabled(false);

        if (verify_tag.equalsIgnoreCase("pan")){
            kyc_viewpager.setCurrentItem(0);
        }
        else if (verify_tag.equalsIgnoreCase("bank")){
            kyc_viewpager.setCurrentItem(1);
        }

    }



   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            currentFrg.onActivityResult(requestCode, resultCode, data);
        //}

    }*/

    public void setCurrentFrg(Fragment currentFrg) {
        this.currentFrg = currentFrg;
    }

    public static Fragment getCurrentFrg(){
        return currentFrg;
    }

    public class KYCViewPager extends FragmentStateAdapter {
        //Fragment fragment;
        public KYCViewPager(@NonNull FragmentActivity fragment) {
            super(fragment);
            //this.fragment = fragment;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                panDetailsFragment=new PanDetailsFragment();
                setCurrentFrg(panDetailsFragment);
                return panDetailsFragment;
            } else if (position == 1)                                                                 {
               bankAccountDetailsFragment=new BankAccountDetailsFragment();
                setCurrentFrg(bankAccountDetailsFragment);
                return bankAccountDetailsFragment;
            } else if (position==2) {
                addressVerificationFragment=new AddressVerificationFragment();
                setCurrentFrg(addressVerificationFragment);
                return addressVerificationFragment;
            }
            return null;
        }

        @Override
        public int getItemCount() {
            if (BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE)){
                return 3;
            }else {
                return 2;
            }
        }
    }
}