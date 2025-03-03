package com.fantafeat.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fantafeat.BuildConfig;
import com.fantafeat.Fragment.AffiliationFragment;
import com.fantafeat.Fragment.CallBackFragment;
import com.fantafeat.Fragment.ContestListFragment;
import com.fantafeat.Fragment.CricketSelectPlayerFragment;
import com.fantafeat.Fragment.HomeActivityFragment;
import com.fantafeat.Fragment.InviteFriendsFragment;
import com.fantafeat.Fragment.MoreFragment;
import com.fantafeat.Fragment.MyLeagueFragment;
import com.fantafeat.Fragment.MyReferralsFragment;
import com.fantafeat.Fragment.NotificationFragment;
import com.fantafeat.Fragment.ProfileFragment;
import com.fantafeat.Fragment.RequestStatementFragment;
import com.fantafeat.Fragment.TransactionListFragment;
import com.fantafeat.Fragment.TransferFragment;
import com.fantafeat.Fragment.WalletFragment;
import com.fantafeat.Fragment.WithdrawFragment;
import com.fantafeat.Model.Games;
import com.fantafeat.Model.MenuModel;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomBottomNavigationView;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.FragmentUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.GetGroupDetail;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.PrefConstant;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainActivity";
    CustomBottomNavigationView bottomNavigationView;
    public static TextView profile_name, profile_tag;

    EditText display_name, email_id;
    FrameLayout frameLayout;
    private static MyPreferences myPreferences;
    private static Button btnSubmit;

    private Dialog basicDetails;
    private String selectedTab = "home";
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    RelativeLayout mNavigationView;
    //private Socket mSocket = null;
    ListView mNavList;
    LinearLayout layGuru;
    public static CircleImageView profile_image;

    LinearLayout profile_details;
    ImageView imgGames;
    BottomSheetDialog dialog;
    public String linkData;
    private List<Games> gameList;

    FusedLocationProviderClient fusedLocationClient;
    AlertDialog.Builder builder;
    private boolean isDialog = false;
    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.inflateMenu(R.menu.bottom_nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setItemIconTintList(null);

        frameLayout = findViewById(R.id.home_fragment_container);
        mNavigationView = findViewById(R.id.home_navigation);
        mNavList = findViewById(R.id.nav_list);
        profile_name = findViewById(R.id.profile_name);
        profile_tag = findViewById(R.id.profile_tag);
        profile_image = findViewById(R.id.profile_image);
        profile_details = findViewById(R.id.profile_details);
        layGuru = findViewById(R.id.layGuru);
        imgGames = findViewById(R.id.imgGames);
        gameList=new ArrayList<>();
        preferences.setPref(PrefConstant.IS_BANNER_CLICK,false);

       // mSocket = MyApp.getInstance().getSocketInstance();

        mToggle = new ActionBarDrawerToggle((Activity) mContext, mDrawerLayout, R.string.open_drawer, R.string.close_drawer);
        mToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white_font));
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

//        showBasicDetails();
        myPreferences = new MyPreferences(this.getApplicationContext());
        Log.d("MyApp", "Before isNewUser check");
       /* if (myPreferences.isNewUser()) {
            Log.d("MyApp", "isNewUser is true, showing dialog for new user");
            showBasicDetails();
        } else {
            Log.d("MyApp", "isNewUser is false, user is not new");
            // User is not new, handle accordingly
        }
        Log.d("MyApp", "After isNewUser check");*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withContext(mContext)
                    .withPermission(
                            Manifest.permission.POST_NOTIFICATIONS)
                    .withListener(new PermissionListener() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            LogUtil.e("TAG", "onPermissionsChecked: ");

                        }
                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                            LogUtil.e("TAG", "onPermissionsChecked: ");
                            //displayPromptForPermission(true);
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    })
                    .check();
        }
        else {

        }


        Glide.with(mContext).load(R.drawable.games_stick).into(imgGames);

        Uri link = getIntent().getData();

        if (link == null)
            linkData = "";
        else {
            linkData = link.toString();
        }
        // linkdata= https://www.fantafeat.com/fantasy/39354/12002197 m-c
        LogUtil.d("linkdata", linkData + " uri data");

        createMenu();

        new FragmentUtil().replaceFragment((FragmentActivity) mContext,
                R.id.home_fragment_container,
                HomeActivityFragment.newInstance(linkData),
                ((HomeActivity) mContext).fragmentTag(0),
                FragmentUtil.ANIMATION_TYPE.NONE);

        profile_details.setOnClickListener(view -> {
            if (MyApp.getClickStatus()) {
                CloseNavDrawer();
                //bottomNavigationView.setSelectedItemId(R.id.profile);
                //startActivity(new Intent(this,ProfileActivity.class));
                new Handler().postDelayed(() -> new FragmentUtil().addFragment(HomeActivity.this,
                        R.id.home_fragment_container,
                        new ProfileFragment(),
                        fragmentTag(29),
                        FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT), 200);
            }
        });

        layGuru.setOnClickListener(view -> {
            //if (ApiManager.play_store_app){
                if (BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE)) {
                    getRummyData();
                    Log.e("FlavorCheck34", "Running playstore version");
                } else {
                    Log.e("FlavorCheck3", "Running Website version");
                    startActivity(new Intent(mContext, GameListActivity.class));
                }
//            } else {
//                Log.e("FlavorCheck234", "Running playstore version");
//                startActivity(new Intent(mContext, GameListActivity.class));
//            }

        });

        getUserDetails();

     /*   runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Glide.get(HomeActivity.this).clearMemory();
                // Glide.get(HomeActivity.this).clearDiskCache();
                try {
                    trimCache(HomeActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/

    }


    public void getRummyData(){
        try {
            JSONObject object=new JSONObject();
            object.put("user_id",preferences.getUserModel().getId());

            LogUtil.e(TAG,object.toString());
            HttpRestClient.postJSONNormal(mContext, true, ApiManager.gameList, object, new GetApiResult() {
                        @Override
                        public void onSuccessResult(JSONObject responseBody, int code) {
                            LogUtil.e("TAG", "getGameListHomeActivity: " + responseBody.toString());
                            JSONArray data=responseBody.optJSONArray("data");
                            if (data!=null && data.length()>0){
                                gameList.clear();
                                for (int i=0;i<data.length();i++){
                                    JSONObject obj=data.optJSONObject(i);
                                    Games games=gson.fromJson(obj.toString(),Games.class);
                                    if (games.getGameName().equalsIgnoreCase("Rummy")){
                                        startActivity(new Intent(mContext,RummyContestActivity.class).putExtra("gameData",games));
                                        break;
                                    }

                                }
                            }
                        }

                        @Override
                        public void onFailureResult(String responseBody, int code) {

                        }
                    }
            );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }

    private void getUserDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("token_no", preferences.getUserModel().getTokenNo());

            Log.e(TAG, "getUserDetails: " + jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, false, ApiManager.AUTHV3GetUserDetails, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "USER onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    if (responseBody.optString("code").equals("200")) {
                        JSONObject data = responseBody.optJSONObject("data");
                        UserModel userModel = gson.fromJson(data.toString(), UserModel.class);

                        preferences.setUserModel(userModel);

                        if (userModel.getIsPromoter().equalsIgnoreCase("yes")) {
                            boolean isDataAvailable = false;
                            if (!TextUtils.isEmpty(userModel.getFacebook_id()) ||
                                    !TextUtils.isEmpty(userModel.getInsta_id()) ||
                                    !TextUtils.isEmpty(userModel.getTelegram_id()) ||
                                    !TextUtils.isEmpty(userModel.getTwitter_id()) ||
                                    !TextUtils.isEmpty(userModel.getYoutube_id())
                            ) {
                                isDataAvailable = true;
                            }

                            if (!isDataAvailable) {
                                showPromotorsLinkDialog();
                            }
                        }

                        /*if (TextUtils.isEmpty(userModel.getDisplayName())) {
                            LogUtil.e("DataCheck", userModel.toString());

                            showBasicDetails();
                        }*/
                    }
                } else {
                    preferences.setUserModel(new UserModel());
                }
                // mUserDetail = true;
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                Log.e(TAG, "onFailureResult: ");
                // mUserDetail = true;
                preferences.setUserModel(new UserModel());

            }
        });
    }

    /*private void submitApi() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("email_id", email_id.getText().toString());
            jsonObject.put("display_name", display_name.getText().toString());


            Log.e(TAG, "submitApi: " + jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, false, ApiManager.AUTHV3UpdateUserDetails, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "USER onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    if (responseBody.optString("code").equals("200")) {
                        UserModel userModel = preferences.getUserModel();
                        userModel.setDisplayName(display_name.getText().toString());
                        userModel.setEmailId(email_id.getText().toString());
                        preferences.setUserModel(userModel);

                        if (basicDetails != null && basicDetails.isShowing()) {
                            basicDetails.dismiss();
                        }

                        updateUIWithUserDetails();

                    }
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                Log.e(TAG, "onFailureResult: ");


            }
        });
    }*/

    private void showPromotorsLinkDialog() {
        dialog = new BottomSheetDialog(this);
        View bottomSheet = getLayoutInflater().inflate(R.layout.promotors_link_detail, null);
        dialog.setCancelable(false);

        EditText edtInstagram = bottomSheet.findViewById(R.id.edtInstagram);
        EditText edtFacebook = bottomSheet.findViewById(R.id.edtFacebook);
        EditText edtTelegram = bottomSheet.findViewById(R.id.edtTelegram);
        EditText edtYoutube = bottomSheet.findViewById(R.id.edtYoutube);
        EditText edtOther = bottomSheet.findViewById(R.id.edtOther);
        TextView btnSubmit = bottomSheet.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
            String insta = edtInstagram.getText().toString().trim();
            String fb = edtFacebook.getText().toString().trim();
            String tele = edtTelegram.getText().toString().trim();
            String yt = edtYoutube.getText().toString().trim();
            String other = edtOther.getText().toString().trim();

            boolean isDataAvailable = false;

            if (!TextUtils.isEmpty(fb) ||
                    !TextUtils.isEmpty(insta) ||
                    !TextUtils.isEmpty(tele) ||
                    !TextUtils.isEmpty(other) ||
                    !TextUtils.isEmpty(yt)
            ) {
                isDataAvailable = true;
            }

            if (!isDataAvailable) {
                Toast.makeText(mContext, "We need at least one of your social details", Toast.LENGTH_LONG).show();
                return;
            }

            getUpdatePromotorDetails(insta, fb, tele, yt, other);

        });

        dialog.setContentView(bottomSheet);
        dialog.show();
    }

    private void getUpdatePromotorDetails(String insta, String fb, String tele, String yt, String other) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("insta_id", insta);
            jsonObject.put("facebook_id", fb);
            jsonObject.put("telegram_id", tele);
            jsonObject.put("youtube_id", yt);
            jsonObject.put("twitter_id", other);
            Log.e(TAG, "getUserDetails: " + jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, false, ApiManager.updateUserVerifyDetails, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                Log.e(TAG, "USER onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    if (responseBody.optString("code").equals("200")) {

                        CustomUtil.showTopSneakSuccess(mContext, "Detail updated successfully");

                        UserModel userModel = preferences.getUserModel();
                        userModel.setInsta_id(insta);
                        userModel.setFacebook_id(fb);
                        userModel.setTelegram_id(tele);
                        userModel.setYoutube_id(yt);
                        userModel.setTwitter_id(other);
                        preferences.setUserModel(userModel);

                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }

                    }
                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
                // mUserDetail = true;
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                Log.e(TAG, "onFailureResult: ");
                // mUserDetail = true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (mSocket != null && mSocket.connected()) {
            mSocket.disconnect();
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConstantUtil.isTimeOverShow = false;

        /*if (mSocket != null) {
            if (!mSocket.connected())
                mSocket.connect();
            try {
                JSONObject obj = new JSONObject();
                if (preferences.getUserModel() != null) {
                    obj.put("user_id", preferences.getUserModel().getId());
                }
                obj.put("title", "matchlist");
                mSocket.emit("connect_user", obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }*/
        UserModel userModel=preferences.getUserModel();

      if (userModel!=null){
          profile_name.setText(preferences.getUserModel().getDisplayName());
          profile_tag.setText("@" + preferences.getUserModel().getUserTeamName());

          if (userModel.getGender() != null) {

              int age = 18;
              if (!TextUtils.isEmpty(userModel.getDob()) && !userModel.getDob().equals("0000-00-00")) {
                  age = CustomUtil.getAge(userModel.getDob());
              }
              if (TextUtils.isEmpty(userModel.getUserImg())) {

                  if (userModel.getGender().equals("Male")) {
                      if (age >= 18 && age <= 25) {
                          profile_image.setImageResource(R.drawable.male_18_below);
                      } else if (age > 25 && age <= 40) {
                          profile_image.setImageResource(R.drawable.male_25_above);
                      } else {
                          profile_image.setImageResource(R.drawable.male_40_above);
                      }
                  } else {
                      if (age >= 18 && age <= 25) {
                          profile_image.setImageResource(R.drawable.female_18_below);
                      } else if (age > 25 && age <= 40) {
                          profile_image.setImageResource(R.drawable.female_25_above);
                      } else {
                          profile_image.setImageResource(R.drawable.female_40_above);
                      }
                  }
              }
              else {

                  LogUtil.e(TAG, "onCreate: " + ApiManager.PROFILE_IMG + userModel.getUserImg());
                  if (userModel.getGender().equals("Male")) {

                      if (age >= 18 && age <= 25) {
                          CustomUtil.loadImageWithGlide(mContext, profile_image, ApiManager.PROFILE_IMG, userModel.getUserImg(), R.drawable.male_18_below);
                      /*  Glide.with(mContext)
                                .load(ApiManager.PROFILE_IMG + userModel.getUserImg())
                                .error(R.drawable.male_18_below)
                                .placeholder(R.drawable.male_18_below)
                                .into(profile_image);*/
                      } else if (age > 25 && age <= 40) {
                          CustomUtil.loadImageWithGlide(mContext, profile_image, ApiManager.PROFILE_IMG, userModel.getUserImg(), R.drawable.male_25_above);
                       /* Glide.with(mContext)
                                .load(ApiManager.PROFILE_IMG + userModel.getUserImg())
                                .error(R.drawable.male_25_above)
                                .placeholder(R.drawable.male_25_above)
                                .into(profile_image);*/
                      } else {
                          CustomUtil.loadImageWithGlide(mContext, profile_image, ApiManager.PROFILE_IMG, userModel.getUserImg(), R.drawable.male_40_above);
                     /*   Glide.with(mContext)
                                .load(ApiManager.PROFILE_IMG + userModel.getUserImg())
                                .error(R.drawable.male_40_above)
                                .placeholder(R.drawable.male_40_above)
                                .into(profile_image);*/
                      }
                  } else {
                      if (age >= 18 && age <= 25) {
                          CustomUtil.loadImageWithGlide(mContext, profile_image, ApiManager.PROFILE_IMG, userModel.getUserImg(), R.drawable.female_18_below);
                       /* Glide.with(mContext)
                                .load(ApiManager.PROFILE_IMG + userModel.getUserImg())
                                .error(R.drawable.female_18_below)
                                .placeholder(R.drawable.female_18_below)
                                .into(profile_image);*/
                      } else if (age > 25 && age <= 40) {
                          CustomUtil.loadImageWithGlide(mContext, profile_image, ApiManager.PROFILE_IMG, userModel.getUserImg(), R.drawable.female_25_above);
                       /* Glide.with(mContext)
                                .load(ApiManager.PROFILE_IMG + userModel.getUserImg())
                                .error(R.drawable.female_25_above)
                                .placeholder(R.drawable.female_25_above)
                                .into(profile_image);*/
                      } else {
                          CustomUtil.loadImageWithGlide(mContext, profile_image, ApiManager.PROFILE_IMG, userModel.getUserImg(), R.drawable.female_40_above);
                       /* Glide.with(mContext)
                                .load(ApiManager.PROFILE_IMG + userModel.getUserImg())
                                .error(R.drawable.female_40_above)
                                .placeholder(R.drawable.female_40_above)
                                .into(profile_image);*/
                      }
                  }

              }
          }
      }



    }

    private void createMenu() {
        NavigationDrawerAdapter mNavigationDrawerAdapter = new NavigationDrawerAdapter(preferences.getMenu());
        mNavList.setAdapter(mNavigationDrawerAdapter);
        mNavList.setOnItemClickListener(HomeActivity.this);
        mNavigationDrawerAdapter.notifyDataSetChanged();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        int itemId = item.getItemId();
        if (itemId == R.id.home) {
            if (!selectedTab.equals("home")) {
                selectedTab = "home";
                new FragmentUtil().replaceFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        HomeActivityFragment.newInstance(linkData.toString()),
                        ((HomeActivity) mContext).fragmentTag(0),
                        FragmentUtil.ANIMATION_TYPE.NONE);
            }
        } else if (itemId == R.id.my_league) {
            if (!selectedTab.equals("league")) {
                selectedTab = "league";
                new FragmentUtil().replaceFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        MyLeagueFragment.newInstance(),
                        ((HomeActivity) mContext).fragmentTag(1),
                        FragmentUtil.ANIMATION_TYPE.NONE);
            }

                    /*case R.id.profile:
                        if (!selectedTab.equals("profile")) {
                            selectedTab = "profile";
                            new Handler().postDelayed(() -> new FragmentUtil().addFragment(HomeActivity.this,
                                    R.id.home_fragment_container,
                                    ProfileFragment.newInstance(),
                                    fragmentTag(29),
                                    FragmentUtil.ANIMATION_TYPE.NONE), 200);
                            */
                    /*new FragmentUtil().replaceFragment((FragmentActivity) mContext,
                                    R.id.home_fragment_container,
                                    new MoreFragment(),
                                    ((HomeActivity) mContext).fragmentTag(16),
                                    FragmentUtil.ANIMATION_TYPE.NONE);*/
                    /*
                        }
                        break;*/
        } else if (itemId == R.id.more) {
            if (!selectedTab.equals("more")) {
                selectedTab = "more";
                new FragmentUtil().replaceFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        MoreFragment.newInstance(),
                        ((HomeActivity) mContext).fragmentTag(16),
                        FragmentUtil.ANIMATION_TYPE.NONE);
            }
        } else if (itemId == R.id.wallet) {
            if (!selectedTab.equals("wallet")) {
                selectedTab = "wallet";
                new FragmentUtil().replaceFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        WalletFragment.newInstance(),
                        ((HomeActivity) mContext).fragmentTag(3),
                        FragmentUtil.ANIMATION_TYPE.NONE);
            }
        }
        return true;
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HandelBackPress();
    }

    private void HandelBackPress() {
        Log.e(TAG, "HandelBackPress: ");
        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.home_fragment_container);
        if (currentFrag == null) {
            Log.e(TAG, "HandelBackPress: " + "here");
            return;
        }
        Log.e(TAG, "HandelBackPress:" + currentFrag.getTag());

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (currentFrag.getTag().equals(fragmentTag(12))) {
            ((ContestListFragment) currentFrag).RemoveFragment();
        } else if (currentFrag.getTag().equals(fragmentTag(13))) {
            ((CricketSelectPlayerFragment) currentFrag).RemoveFragment();
        } else if (currentFrag.getTag().equals(fragmentTag(23))) {
            ((RequestStatementFragment) currentFrag).RemoveFragment();
        } else if (currentFrag.getTag().equals(fragmentTag(24))) {
            ((WithdrawFragment) currentFrag).RemoveFragment();
        } else if (currentFrag.getTag().equals(fragmentTag(26))) {
            ((MyReferralsFragment) currentFrag).RemoveFragment();
        } else if (currentFrag.getTag().equals(fragmentTag(27))) {
            ((TransferFragment) currentFrag).RemoveFragment();
        } else if (currentFrag.getTag().equals(fragmentTag(33))) {
            ((TransactionListFragment) currentFrag).RemoveFragment();
        } else if (currentFrag.getTag().equals(fragmentTag(32))) {
            ((InviteFriendsFragment) currentFrag).RemoveFragment();
        }
        /*else if (currentFrag.getTag().equals(fragmentTag(16))) {
            ((MoreFragment) currentFrag).RemoveFragment();
        }*/
       /* else if (currentFrag.getTag().equals(fragmentTag(22))) {
            ((UpdateKYCFragment) currentFrag).RemoveFragment();
        }*/
     /*   else if (currentFrag.getTag().equals(fragmentTag(17))) {
            ((WebViewFragment) currentFrag).RemoveFragment();
        }*/
        else if (currentFrag.getTag().equals(fragmentTag(18))) {
            ((AffiliationFragment) currentFrag).RemoveFragment();
        } else if (currentFrag.getTag().equals(fragmentTag(20))) {
            ((CallBackFragment) currentFrag).RemoveFragment();
        } else if (currentFrag.getTag().equals(fragmentTag(29))) {
            ((ProfileFragment) currentFrag).RemoveFragment();
        } else if (currentFrag.getTag().equals(fragmentTag(31))) {
            ((NotificationFragment) currentFrag).RemoveFragment();
        } else {
            quitApp(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void quitApp(boolean isExit) {

        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.exit_dialog, null);
        mBottomSheetDialog.setContentView(sheetView);

        Button txtYes = sheetView.findViewById(R.id.txtYes);
        Button txtNo = sheetView.findViewById(R.id.txtNo);

        if (isExit) {
            txtYes.setText("Exit From App");
        } else {
            txtYes.setText("Logout");
        }

        TextView txtTitle = sheetView.findViewById(R.id.txtTitle);
        if (isExit) {
            txtTitle.setText("Want to Go Back?");
        } else {
            txtTitle.setText("Logout");
        }
        TextView txtMsg = sheetView.findViewById(R.id.txtMsg);
        if (isExit) {
            txtMsg.setText("Are you sure you want to exit from app?");
        } else {
            txtMsg.setText("Are you sure you want to logout from this app?");
        }

        txtNo.setOnClickListener(v -> {
            mBottomSheetDialog.dismiss();
        });
        txtYes.setOnClickListener(v -> {
            mBottomSheetDialog.dismiss();
            if (isExit) {
                preferences.setPref(PrefConstant.MATCH_SELECTED_TAB, 0);
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                finishAffinity();
            } else {
                preferences.setPref(PrefConstant.APP_IS_LOGIN, false);

                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                finish();
            }

        });

        mBottomSheetDialog.show();

        /*AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setMessage("" + getResources().getString(R.string.quite_app));
        builder.setNegativeButton("" + getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("" + getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preferences.setPref(PrefConstant.MATCH_SELECTED_TAB,0);
                Intent startMain = new  Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                finishAffinity();
            }
        });

        AlertDialog alert = builder.create();
        alert.setCancelable(true);
        alert.show();*/
    }

    public String fragmentTag(int position) {
        String[] drawer_tag = getResources().getStringArray(R.array.stack);
        return drawer_tag[position];
    }

    public void OpenNavDrawer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.openDrawer(mNavigationView);
            }
        }, 100);
    }

    public void CloseNavDrawer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawer(mNavigationView);
            }
        }, 100);
    }

    public void LockCloseDrawerState() {
        if (mDrawerLayout != null) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    public void LockOpenDrawerState() {
        if (mDrawerLayout != null) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    public void HideBottomNavigationBar() {
        bottomNavigationView.setVisibility(View.GONE);
        layGuru.setVisibility(View.GONE);
    }

    public void ShowBottomNavigationBar() {
        bottomNavigationView.setVisibility(View.VISIBLE);
        layGuru.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private void SwitchFragment(MenuModel menuData) {
        switch (menuData.getMenuKey()) {
            case "find_people":
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new FragmentUtil().addFragment(HomeActivity.this,
                                R.id.fragment_container,
                                new FindPeopleFragment(),
                                fragmentTag(25),
                                FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                    }
                }, 200);*/
                break;
            case "my_profile":
                //bottomNavigationView.setSelectedItemId(R.id.profile);
                //startActivity(new Intent(this,ProfileActivity.class));
                new FragmentUtil().addFragment(HomeActivity.this,
                        R.id.home_fragment_container,
                        new ProfileFragment(),
                        fragmentTag(29),
                        FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                break;
            case "menu_leaderboard":
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(HomeActivity.this, LeaderboardListActivity.class)
                                .putExtra("leaderboard_id", ""));
                       /* startActivity(new Intent(HomeActivity.this, UserLeaderboardActivity.class)
                                .putExtra("leaderboard_id", ""));*/
                    }
                }, 200);
                break;
            case "view_winner":
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new FragmentUtil().addFragment(HomeActivity.this,
                                R.id.fragment_container,
                                new ViewWinnersFragment(),
                                fragmentTag(27),
                                FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                    }
                }, 200);*/
                break;
            case "my_balance":
                bottomNavigationView.setSelectedItemId(R.id.wallet);
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new FragmentUtil().addFragment(HomeActivity.this,
                                R.id.fragment_container,
                                StatsFragment.newInstance(preferences.getUserModel().getData().getId(), true),
                                fragmentTag(3),
                                FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                    }
                }, 200);*/
                break;
            case "my_offers_coupons":
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new FragmentUtil().addFragment(HomeActivity.this,
                                R.id.fragment_container,
                                new MyOfferCouponsFragment(),
                                fragmentTag(28),
                                FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                    }
                }, 200);*/
                break;
            case "invite_friends":
                new FragmentUtil().addFragment(HomeActivity.this,
                        R.id.home_fragment_container,
                        new InviteFriendsFragment(),
                        fragmentTag(32),
                        FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new FragmentUtil().addFragment(HomeActivity.this,
                                R.id.fragment_container,
                                new InviteFriendsFragment(),
                                fragmentTag(31),
                                FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                    }
                }, 200);*/
                break;
            case "my_setting":
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new FragmentUtil().addFragment(HomeActivity.this,
                                R.id.fragment_container,
                                new MyInfoAndSettingFragment(),
                                fragmentTag(29),
                                FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                    }
                }, 200);*/
                break;

            case "point_system":
                startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra(ConstantUtil.WEB_TITLE, "Fantasy Points System")
                        .putExtra(ConstantUtil.WEB_URL, ApiManager.point_system_url));
               /* new FragmentUtil().addFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new WebViewFragment("Fantasy Points System", "https://www.batball11.com/fantasy_point.html"),
                        ((HomeActivity) mContext).fragmentTag(17),
                        FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);*/
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new FragmentUtil().addFragment((FragmentActivity) mContext,
                                R.id.fragment_container,
                                new WebViewFragment("Fantasy Points System", "https://www.batball11.com/fantasy_point.html"),
                                ((HomeActivity) mContext).fragmentTag(24),
                                FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                    }
                }, 200);*/
                break;
            case "more":
                bottomNavigationView.setSelectedItemId(R.id.more);
              /*  new FragmentUtil().replaceFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new MoreFragment(),
                        ((HomeActivity) mContext).fragmentTag(16),
                        FragmentUtil.ANIMATION_TYPE.NONE);*/
                break;
            case "log_out":
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // preferences.logout();
                        quitApp(false);
                       /* preferences.setPref(PrefConstant.APP_IS_LOGIN,false);
                        FCMHandler.disableFCM();
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                        finish();*/
                    }
                }, 200);
                break;
            default:
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new FragmentUtil().addFragment(HomeActivity.this,
                                R.id.fragment_container,
                                MenuWebFragment.newInstance(menuData.getUrl(), menuData.getName()),
                                fragmentTag(4),
                                FragmentUtil.ANIMATION_TYPE.SLIDE_LEFT_TO_RIGHT);
                    }
                }, 300);*/
                break;
        }

        CloseNavDrawer();

    }
    private class NavigationDrawerAdapter extends BaseAdapter {

        private List<MenuModel> menuData;
        private NavDrawHolder holder;

        NavigationDrawerAdapter(List<MenuModel> menuData) {
            this.menuData = menuData;
        }

        @Override
        public int getCount() {
            return menuData.size();
        }

        @Override
        public Object getItem(int position) {
            return menuData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.row_menu, parent, false);
                holder = new NavDrawHolder(view);
                LogUtil.e(TAG, "getView: here");
                view.setTag(holder);
            } else {
                holder = (NavDrawHolder) view.getTag();
            }
            if (menuData.get(position).getDisplaySeparator().equalsIgnoreCase("Yes")) {
                holder.menu_separator.setVisibility(View.VISIBLE);
            } else {
                holder.menu_separator.setVisibility(View.GONE);
            }

            holder.menu_title.setText(menuData.get(position).getMenuName());
            int res = getResources().getIdentifier(menuData.get(position).getMenuKey(), "drawable", getPackageName());
            LogUtil.e(TAG, "getView: " + res + "->" + position + "->" + menuData.get(position).getMenuKey());
            if (res == 0 && !menuData.get(position).getMenuImg().trim().equals("")) {
                LogUtil.e(TAG, "getView: " + menuData.get(position).getMenuImg());
                LogUtil.e(TAG, "getView: " + menuData.get(position).getMenuName());
                CustomUtil.loadImageWithGlide(mContext, holder.menu_img, "", menuData.get(position).getMenuImg(), R.drawable.team1);
             /*   Glide.with(mContext)
                        .load(menuData.get(position).getMenuImg())
                        .error(R.drawable.team1)
                        .placeholder(R.drawable.team1)
                        .into(holder.menu_img);*/

            } else {
                Glide.with(mContext)
                        .load(res)
                        .error(R.drawable.team1)
                        .placeholder(R.drawable.team1)
                        .into(holder.menu_img);
                //holder.menu_img.setImageResource(res);
            }

            LogUtil.e(TAG, "getView: Here1");
            holder.menu_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MyApp.getClickStatus()) {
                        SwitchFragment(menuData.get(position));
                    }
                }
            });
            return view;
        }

        private class NavDrawHolder {
            LinearLayout menu_layout;
            TextView menu_title;
            ImageView menu_img;
            View menu_separator;

            public NavDrawHolder(View view) {
                menu_layout = view.findViewById(R.id.menu_layout);
                menu_title = view.findViewById(R.id.menu_title);
                menu_img = view.findViewById(R.id.menu_img);
                menu_separator = view.findViewById(R.id.menu_separator);

            }
        }
    }

    private boolean Validate() {

        if (getEditText(display_name).equals("")) {
            CustomUtil.showTopSneakWarning(mContext, "Please Enter Your Display Name");
            return false;

        } else if (getEditText(email_id).equals("")) {
            CustomUtil.showTopSneakError(mContext, "Please Enter Email.");
            return false;

        } else if (!isValidEmail(getEditText(email_id))) {
            CustomUtil.showTopSneakError(mContext, "Please Enter Valid Email.");
            return false;
        }
        return true;
    }

    private void showBasicDetails() {
        View view = getLayoutInflater().inflate(R.layout.fragment_basic_details, null);
        basicDetails = new BottomSheetDialog(HomeActivity.this);
        basicDetails.setCancelable(false);
        basicDetails.setContentView(view);
        basicDetails.show();

        display_name = view.findViewById(R.id.display_name);
        email_id = view.findViewById(R.id.email_id);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validate()) {
                    //submitApi();

                }
            }
        });

    }

    private void updateUIWithUserDetails() {
        UserModel userModel = preferences.getUserModel();
        if (userModel != null) {
            profile_name.setText(userModel.getDisplayName());
        }
        // Other UI updates as needed
    }

}