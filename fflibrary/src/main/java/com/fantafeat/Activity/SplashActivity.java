package com.fantafeat.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fantafeat.BuildConfig;
import com.fantafeat.Model.BannerModel;
import com.fantafeat.Model.MenuModel;
import com.fantafeat.Model.SportsDetailModel;
import com.fantafeat.Model.SportsModel;
import com.fantafeat.Model.UpdateModel;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.DBHelper;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.PrefConstant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends BaseActivity {

    ImageView logo,imgSplash;
    private boolean mSportList, mSportDetailList, mMenu, mUserDetail, mUpdate = false, mMaintance = false;
    private Handler mHandler;
    private Runnable mRunnable;
    private Button btnPlayNow;
    private ImageView imgBottom;
    private File file;
    /*private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;*/
    private ProgressBar progress;
    private Boolean isRedirect = false;
    static long kilo = 1024;
    DBHelper dbHelper;
    private JSONObject currentData;
    private BottomSheetDialog bottomSheetDialog;
    private boolean updateDialogShown = false;
    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    String[] p;

    @Override
    public void initClick() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTransparent();
        //getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.pureWhite)); //status bar or the time bar at the top
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }

       // logo = findViewById(R.id.splash_logo);
        btnPlayNow = findViewById(R.id.btnPlayNow);
        imgBottom = findViewById(R.id.imgBottom);
        imgSplash = findViewById(R.id.imgSplash);
        ImageView imgPlayer = findViewById(R.id.imgPlayer);
        mSportList = mSportDetailList = mMenu = mUserDetail = false;

        dbHelper = new DBHelper(this);
        currentData = new JSONObject();

       /* CustomUtil.checkInternet(mContext, new InternetConnection() {
            @Override
            public void onSuccess() {
                checkApiCall();
            }
        });*/

        /*FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {return;}
            String token = task.getResult().getToken();
            MyApp.tokenNo = token;

        });*/

       /* Glide.with(this).asGif()
                .load(R.raw.splash_video)
                .addListener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        resource.setLoopCount(1); // Place your loop count here.
                        return false;
                    }
                })
                .into(imgSplash);*/

        Glide.with(this)
                .load(R.drawable.splash_players)
                .override(600, 800) // Adjust size to prevent crashes
                .into(imgPlayer);

        /*FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    return;
                }
                String token = task.getResult();
                LogUtil.e(TAG, "Refreshed token: " + token);
                MyApp.tokenNo = token;
            }
        });*/

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                /*HttpRestClient.showNetworkDialog(mContext, () -> new Thread(() -> {
                    getAppDataList();
                }).start());*/
                checkLogin(false);
            }
        },3000);

        btnPlayNow.setOnClickListener(v->{
            preferences.setPref(PrefConstant.APP_IS_WELCOME,true);
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            finish();
        });


        //  checkApiCall();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (updateDialogShown) {
            if (mContext != null && !this.isFinishing() && bottomSheetDialog.isShowing()) {
                bottomSheetDialog.dismiss();
            }
            //bottomSheetDialog.dismiss();
           // getAppDataList(); // Call the API only if update dialog wasn't shown in this session
        }
    }

    private void getAppDataList() {
        try {
            JSONObject param = new JSONObject();
            if (preferences.getUserModel() != null && !TextUtils.isEmpty(preferences.getUserModel().getId())) {
                param.put("user_id", preferences.getUserModel().getId());
                param.put("token_no", preferences.getUserModel().getTokenNo());
            }
            LogUtil.e(TAG, "getAppDataList Param: " + param.toString());
            HttpRestClient.postJSON(mContext, false, ApiManager.appDataList, param, new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {
                    LogUtil.e(TAG, "getAppDataList: " + responseBody.toString());
                    if (responseBody.optBoolean("status")) {
                        currentData = responseBody;

                        if (currentData.has("check_app"))
                            MyApp.getInstance().setApp_url(currentData.optJSONObject("check_app").optString("app_url"));

                        if (isUpdateAvailable()) {
                            return;
                        }

                        setOtherData();
                    }
                }

                @Override
                public void onFailureResult(String responseBody, int code) {
                    if (vpn()){
                        checkVPN();
                    }
                    /*else {
                        AlertDialog.Builder alert=new AlertDialog.Builder(mContext);
                        alert.setMessage(": "+code);
                        alert.setMessage(responseBody);
                        alert.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishAffinity();
                            }
                        });
                        alert.setCancelable(false);
                        alert.show();
                        //Toast.makeText(mContext,"Not Connected",Toast.LENGTH_LONG).show();
                    }*/
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkVPN() {
        //LogUtil.e("resp","Vpn status: "+vpn());
        AlertDialog.Builder alert=new AlertDialog.Builder(mContext);
        alert.setMessage("VPN detected, Please turn off your VPN and Re-Open the app.");
        alert.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity)mContext).finishAffinity();
            }
        });
        alert.setCancelable(false);
        alert.show();
    }

    public boolean vpn() {
        String iface = "";
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    iface = networkInterface.getName();
                Log.d("DEBUG", "IFACE NAME: " + iface);
                if ( iface.contains("tun") || iface.contains("ppp") || iface.contains("pptp")) {
                    return true;
                }
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }

        return false;
    }

    private boolean isUpdateAvailable() {
        try {
            if (currentData != null) {
                JSONObject check_app = currentData.optJSONObject("check_app");

               /* LogUtil.e("VERSION_CHK", "vverr=>" + check_app.toString());
                check_app.put("v", "1010");
                check_app.put("update_type", "normal_update");

                check_app.put("is_play_store_download", "no");
                LogUtil.e("after_VERSION_CHK", "aftervverr=>" + check_app.toString());
                LogUtil.e("Urrl", check_app.optString("app_url"));*/

            /*     if (ConstantUtil.is_game_test){
                    try {
                        check_app.put("update_type","normal_update");
                        check_app.put("v",1017);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }*/

                if (check_app != null) {
                    LogUtil.e("VERsionnme", "vverr=>" + check_app.optString("v"));
                    if (CustomUtil.convertFloat(check_app.optString("v")) > BuildConfig.VERSION_CODE) {
                        showAppUpdate(check_app);
                        return true;
                    }
                    else if (check_app.optString("maintenance").equalsIgnoreCase("yes")) {
                        showAppMaintance(check_app);
                        return true;
                    }

                /*if (check_app.optString("update_type").equalsIgnoreCase("force_update")) {
                    if (CustomUtil.convertInt(check_app.optString("v")) > BuildConfig.VERSION_CODE) {
                        showAppUpdate(check_app);
                        return true;
                    }
                }
                if (check_app.optString("update_type").equalsIgnoreCase("normal_update")){
                    if (CustomUtil.convertInt(check_app.optString("v")) > BuildConfig.VERSION_CODE) {
                        showAppUpdate(check_app);
                        return true;
                    }
                }
                if (check_app.optString("update_type").equalsIgnoreCase("maintenance")){
                    showAppUpdate(check_app);
                    return true;
                }*/
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private void setOtherData() {
        if (currentData != null) {
            JSONObject user_data = currentData.optJSONObject("user_data");
            LogUtil.e(TAG, "getAppDataList  user_data: " + user_data.toString());
            JSONObject dynamic_key = currentData.optJSONObject("dynamic_key");
            LogUtil.e(TAG, "getAppDataList  dynamic_key: " + dynamic_key.toString());
            JSONArray menu_list = currentData.optJSONArray("menu_list");
            JSONArray sport_list = currentData.optJSONArray("sport_list");

            LogUtil.e(TAG, "getAppDataList  sport_list: " + sport_list.toString());

            if (user_data != null) {
                UserModel userModel = gson.fromJson(user_data.toString(), UserModel.class);
                float total;
                if (ConstantUtil.is_bonus_show) {
                    total = CustomUtil.convertFloat(userModel.getDepositBal()) + CustomUtil.convertFloat(userModel.getWinBal())
                            + CustomUtil.convertFloat(userModel.getFf_coin()) + CustomUtil.convertFloat(userModel.getBonusBal());

                } else {
                    total = CustomUtil.convertFloat(userModel.getDepositBal()) + CustomUtil.convertFloat(userModel.getWinBal())
                            + CustomUtil.convertFloat(userModel.getFf_coin());

                }

                /*float total = CustomUtil.convertFloat(userModel.getDepositBal()) + CustomUtil.convertFloat(userModel.getWinBal())
                        + CustomUtil.convertFloat(userModel.getFf_coin()) + CustomUtil.convertFloat(userModel.getBonusBal());*/
                userModel.setTotal_balance(total);
                preferences.setUserModel(userModel);
            } else {
                preferences.setUserModel(new UserModel());
            }

            if (dynamic_key != null) {
                UpdateModel updateModel = gson.fromJson(dynamic_key.toString(), UpdateModel.class);

                ApiManager.play_store_app = updateModel.getIs_play_store().equalsIgnoreCase("yes");
                ApiManager.isInstant = updateModel.getIs_cf_instant_withdraw().equalsIgnoreCase("yes");
                ApiManager.IS_SQLITE_ENABLE = updateModel.getSql_lite_enable().equalsIgnoreCase("yes");
                ApiManager.isPayTm = updateModel.getDisplayDepositPaytmInstant().equalsIgnoreCase("yes");
                MyApp.user_header_key = updateModel.getUser_header_key();
                preferences.setPref(PrefConstant.user_header_key, MyApp.user_header_key);

                if (TextUtils.isEmpty(updateModel.getImage_base_path_url())) {
                    MyApp.imageBase = ApiManager.IMAGEBASE;
                    preferences.setPref(PrefConstant.IMAGE_BASE, MyApp.imageBase);
                } else {
                    MyApp.imageBase = updateModel.getImage_base_path_url();
                    preferences.setPref(PrefConstant.IMAGE_BASE, MyApp.imageBase);
                }
                LogUtil.e(TAG, "getAppDataList  dynamic_key: " + preferences.getPrefString(PrefConstant.IMAGE_BASE));
                /*if (ApiManager.IS_SQLITE_ENABLE){
                    getDatabaseSize();
                }*/
                preferences.setUpdateMaster(updateModel);
            }

            if (menu_list != null) {
                final List<MenuModel> menuModals = new ArrayList<>();
                for (int i = 0; i < menu_list.length(); i++) {
                    JSONObject object = menu_list.optJSONObject(i);
                    if (!object.optString("key").equals("menu_dashboard")) {
                        MenuModel menuModel = gson.fromJson(CustomUtil.replaceNull(object.toString()), MenuModel.class);
                        menuModals.add(menuModel);
                    }
                }
                preferences.setMenu(menuModals);
            }

            final List<SportsModel> sportsModels = new ArrayList<>();
            for (int i = 0; i < sport_list.length(); i++) {
                JSONObject data = sport_list.optJSONObject(i);
                SportsModel sportsModel = gson.fromJson(data.toString(), SportsModel.class);
                /*if (ConstantUtil.is_game_test){
                    if (sportsModel.getId().equalsIgnoreCase("7")){
                        sportsModel.setTab_1_min("1");
                        sportsModel.setTab_1_max("5");

                        sportsModel.setTab_2_min("1");
                        sportsModel.setTab_2_max("5");

                        sportsModel.setTab_3_min("1");
                        sportsModel.setTab_3_max("5");

                        sportsModel.setTab_4_min("1");
                        sportsModel.setTab_4_max("5");

                        sportsModel.setTeam_1_max_player("6");
                    }
                }*/
                sportsModels.add(sportsModel);
            }
            preferences.setSports(sportsModels);
        }

        Intent intent = new Intent(mContext, HomeActivity.class);
        startActivity(intent);
        finish();
       // checkLogin();
    }

    private void checkApiCall() {
        getUpdateMaster();
        getHomeBanner();
        checkAppUpdate();

        mHandler = new Handler();
        mHandler.postDelayed(mRunnable = new Runnable() {
            @Override
            public void run() {
                if (mSportList && mSportDetailList && mMenu && !mMaintance && !mUpdate) {//&& mUserDetail
                    mHandler.removeCallbacks(mRunnable);
                    //LogUtil.e(TAG, "run: true");
                    if (!isRedirect) {
                        isRedirect = true;
                        preferences.setPref(PrefConstant.isFestivalShow, false);
                        checkLogin(false);
                    }

                } else {
                    mHandler.postDelayed(mRunnable, 200);
                }
            }
        }, 200);

    }

    private void checkAppUpdate() {
        HttpRestClient.postJSON(mContext, false, ApiManager.CHECK_APP, new JSONObject(), new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "checkAppUpdate: " + responseBody.toString() + "    " + ApiManager.CHECK_APP);
                if (responseBody.optBoolean("status")) {
                    JSONObject data = responseBody.optJSONObject("data");

                    if (CustomUtil.convertFloat(data.optString("v")) > BuildConfig.VERSION_CODE) {
                        mUpdate = true;
                        showAppUpdate(data);
                    } else if (data.optString("maintenance").equalsIgnoreCase("yes")) {
                        mMaintance = true;
                        showAppMaintance(data);
                    } /*else {
                        mAppUpdate = true;
                    }*/
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                checkAppUpdateServer();
            }

        });
    }

    private void showAppMaintance(JSONObject data) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.maintance_dialog, null);
        Button btn_ok = view.findViewById(R.id.btnClose);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        txtTitle.setText(data.optString("maintenance_title"));
        TextView txtSubTitle = view.findViewById(R.id.txtSubTitle);
        txtSubTitle.setText(data.optString("maintenance_msg"));
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        btn_ok.setOnClickListener(view1 -> {
            bottomSheetDialog.dismiss();
            finish();
        });
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);
        if (!bottomSheetDialog.isShowing()) {
            bottomSheetDialog.show();
        }
    }

    private void showAppUpdate(JSONObject data) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.app_update_dialog, null);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView app_update_msg = view.findViewById(R.id.app_update_msg);
        TextView txtSubTitle = view.findViewById(R.id.txtSubTitle);
        ImageView imageView3 = view.findViewById(R.id.imageView3);
        ImageView imgClose = view.findViewById(R.id.imgClose);
        progress = view.findViewById(R.id.progress);

        if (data.optString("update_type").equalsIgnoreCase("force_update")) {
            imgClose.setVisibility(View.GONE);
        } else {
            imgClose.setVisibility(View.VISIBLE);
        }

       bottomSheetDialog = new BottomSheetDialog(mContext);
        btnUpdate.setOnClickListener(view1 -> {
            updateDialogShown = true;
            progress.setVisibility(View.VISIBLE);
            app_update_msg.setVisibility(View.VISIBLE);
            btnUpdate.setVisibility(View.GONE);
            imageView3.setVisibility(View.GONE);
            txtTitle.setText("What’s new in Update");
            txtSubTitle.setText("There is new version is available for download! Please update the app by visiting www.fantafeat.com");
            app_update_msg.setText(Html.fromHtml(data.optString("update_message")));

            if (data.has("is_play_store_download") && data.optString("is_play_store_download").equalsIgnoreCase("yes")) {
                if (BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE)) {
                    txtTitle.setVisibility(View.GONE);
                    txtSubTitle.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                    app_update_msg.setVisibility(View.GONE);
                    bottomSheetDialog.dismiss();
                    final String appPackageName = BuildConfig.APPLICATION_ID;
                    LogUtil.e("appplays_tore", appPackageName);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(
                            "https://play.google.com/store/apps/details?id=" + appPackageName));
                    intent.setPackage("com.android.vending");
                    startActivity(intent);
                } else {
                    Dexter.withActivity((Activity) mContext)
                            .withPermissions(p)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    // check if all permissions are granted
                                    if (report.areAllPermissionsGranted()) {
                                        LogUtil.e("resp", "Update App Url:" + MyApp.getInstance().getApp_url());
                                        new DownloadAsync().execute(MyApp.getInstance().getApp_url());
                                    }

                                    // check for permanent denial of any permission
                                    if (report.isAnyPermissionPermanentlyDenied()) {
                                        LogUtil.e(TAG, "onPermissionsChecked: ");
                                        //showSettingsDialog();
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            })
                            .onSameThread()
                            .check();
                }
            } else {
                Dexter.withActivity((Activity) mContext)
                        .withPermissions(p)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    LogUtil.e("resp", "Update App Url:" + MyApp.getInstance().getApp_url());
                                    new DownloadAsync().execute(MyApp.getInstance().getApp_url());
                                }

                                // check for permanent denial of any permission
                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    LogUtil.e(TAG, "onPermissionsChecked: ");
                                    //showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        })
                        .onSameThread()
                        .check();

            }

        });
        imgClose.setOnClickListener(v -> {
            mUpdate = false;
            bottomSheetDialog.dismiss();
            if (data.optString("update_type").equalsIgnoreCase("force_update")) {
                LogUtil.e("FForce","updateForce");
                getAppDataList();
            }
            else if (data.optString("update_type").equalsIgnoreCase("normal_update")){
                LogUtil.e("NNormal","updateNormal");
                UserModel userModel=preferences.getUserModel();
                if (userModel != null && TextUtils.isEmpty(userModel.getId())) {
                    setOtherData();
                } else {
                    checkLogin(true);
                }
            }
        });
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);
        if (mContext != null && !this.isFinishing() && !bottomSheetDialog.isShowing()) {
            bottomSheetDialog.show();
        }
    }

    private void checkAppUpdateServer() {
        HttpRestClient.postJSON(mContext, false, ApiManager.CHECK_APP_SERVER, new JSONObject(), new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "checkAppUpdateServer: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONObject data = responseBody.optJSONObject("data");
                    if (CustomUtil.convertInt(data.optString("v")) > MyApp.current_version) {
                        mUpdate = true;
                        showAppUpdate(data);
                    } else if (data.optString("maintenance").equalsIgnoreCase("yes")) {
                        mMaintance = true;
                        showAppMaintance(data);
                    } /*else {
                        mAppUpdate = true;
                    }*/
                }

            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void getUserDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("token_no", preferences.getUserModel().getTokenNo());
            LogUtil.e(TAG, "getUserDetails: " + jsonObject.toString());

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

                    }
                } else {
                    preferences.setUserModel(new UserModel());
                }
                mUserDetail = true;
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                LogUtil.e(TAG, "onFailureResult: ");
                mUserDetail = true;
                preferences.setUserModel(new UserModel());

            }
        });
    }

    private void getUpdateMaster() {

        HttpRestClient.postJSON(mContext, false, ApiManager.UPDATE_MASTER, new JSONObject(), new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "UPDATE_MASTER: " + responseBody.optJSONObject("data").toString());
                if (responseBody.optBoolean("status")) {
                    UpdateModel updateModel = gson.fromJson(responseBody.optJSONObject("data").toString(), UpdateModel.class);

                    //ApiManager.IS_MULTIJOIN = updateModel.getIs_multijoin().equalsIgnoreCase("yes");
                    ApiManager.isInstant = updateModel.getIs_cf_instant_withdraw().equalsIgnoreCase("yes");
                    ApiManager.IS_SQLITE_ENABLE = updateModel.getSql_lite_enable().equalsIgnoreCase("yes");
                    ApiManager.isPayTm = updateModel.getDisplayDepositPaytmInstant().equalsIgnoreCase("yes");
                    MyApp.user_header_key = updateModel.getUser_header_key();
                    preferences.setPref(PrefConstant.user_header_key, MyApp.user_header_key);
                    //ApiManager.ISCDN = updateModel.getIs_image_enable_cdn().equalsIgnoreCase("yes");

                    //preferences.setPref(PrefConstant.IMAGE_BASE,updateModel.getImage_base_path_url());

                    if (TextUtils.isEmpty(updateModel.getImage_base_path_url())) {
                        MyApp.imageBase = ApiManager.IMAGEBASE;
                    } else {
                        MyApp.imageBase = updateModel.getImage_base_path_url();
                    }
                    preferences.setPref(PrefConstant.IMAGE_BASE, MyApp.imageBase);

                    LogUtil.e(TAG, "UPDATE_MASTER: " + MyApp.imageBase + "   " + updateModel.getImage_base_path_url());

                    getMenuData();

                    if (ApiManager.IS_SQLITE_ENABLE) {
                        getDatabaseSize();
                    }

                   /* if (preferences.getUpdateMaster() == null) {
                        getMenuData();
                        getSportsList();
                        getSportsSubList();
                    } else {
                        if (!preferences.getUpdateMaster().getMenu().equals(updateModel.getMenu())) {
                            LogUtil.e(TAG, "onSuccessResult: " + "menu");
                            getMenuData();
                        } else {
                            mMenu = true;
                        }
                        if (!preferences.getUpdateMaster().getSports().equals(updateModel.getSports())) {
                            LogUtil.e(TAG, "onSuccessResult: sports list");
                            getSportsList();
                        } else {
                            mSportList = true;
                        }
                        if (!preferences.getUpdateMaster().getSportDetails().equals(updateModel.getSportDetails())) {
                            LogUtil.e(TAG, "onSuccessResult: sub list");
                            getSportsSubList();
                        } else {
                            mSportDetailList = true;
                        }
                    }*/

                    preferences.setUpdateMaster(updateModel);

                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                LogUtil.e(TAG, "onFailureResult: ");
            }
        });
    }

    private void getDatabaseSize() {
        try {
            float dbSize = Float.parseFloat(bytesToMeg(this.getDatabasePath(DBHelper.DATABASE_NAME).length()));//Float.parseFloat(getFileSize(dbFile.length()));
            if (dbSize > ApiManager.MAX_SQLITE_SIZE) {
                dbHelper.deleteAllImages();
            }
            LogUtil.e("resp", dbSize + "MB Size");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static String bytesToMeg(long size) {
        // return bytes / MEGABYTE ;
        String s = "";
        double kb = (double) size / kilo;
        double mb = kb / kilo;
        return String.format("%.2f", mb);
    }

    private void getHomeBanner() {
        final List<BannerModel> bannerModels = new ArrayList<>();
        HttpRestClient.postJSON(mContext, false, ApiManager.HOME_BANNER, new JSONObject(), new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    JSONArray array = responseBody.optJSONArray("data");
                    int i = 0;
                    for (i = 0; i < array.length(); i++) {
                        try {
                            JSONObject data = array.getJSONObject(i);
                            BannerModel bannerModel = gson.fromJson(data.toString(), BannerModel.class);
                            bannerModels.add(bannerModel);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    preferences.setHomeBanner(bannerModels);
                }
                mMenu = true;
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void getSportsSubList() {
        final List<SportsDetailModel> sportsDetailModels = new ArrayList<>();
        HttpRestClient.postJSON(mContext, false, ApiManager.SPORT_SUB_LIST, new JSONObject(), new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONArray array = responseBody.optJSONArray("data");
                    int i;
                    for (i = 0; i < array.length(); i++) {
                        JSONObject data = array.optJSONObject(i);
                        SportsDetailModel sportsDetailModel = gson.fromJson(data.toString(), SportsDetailModel.class);
                        sportsDetailModels.add(sportsDetailModel);
                    }
                    preferences.setSportDetails(sportsDetailModels);
                }
                mSportDetailList = true;
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void getSportsList() {
        final List<SportsModel> sportsModels = new ArrayList<>();
        HttpRestClient.postJSON(mContext, false, ApiManager.SPORTS_LIST, new JSONObject(), new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "getSportsList: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONArray array = responseBody.optJSONArray("data");
                    int i;
                    for (i = 0; i < array.length(); i++) {
                        JSONObject data = array.optJSONObject(i);
                        SportsModel sportsModel = gson.fromJson(data.toString(), SportsModel.class);
                       /* if (ConstantUtil.is_game_test){
                            if (sportsModel.getId().equalsIgnoreCase("7")){
                                sportsModel.setTab_1_min("1");
                                sportsModel.setTab_1_max("5");

                                sportsModel.setTab_2_min("1");
                                sportsModel.setTab_2_max("5");

                                sportsModel.setTab_3_min("1");
                                sportsModel.setTab_3_max("5");

                                sportsModel.setTab_4_min("1");
                                sportsModel.setTab_4_max("5");

                                sportsModel.setTeam_1_max_player("6");
                            }
                        }*/
                        sportsModels.add(sportsModel);
                    }
                    preferences.setSports(sportsModels);
                    getSportsSubList();
                }
                mSportList = true;
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }
    private void continueToHome() {
        Intent intent = new Intent(mContext, HomeActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Ensure HomeActivity starts fresh
        startActivity(intent);
        finish();
    }
    private void checkLogin(boolean goHome) {
        if (!preferences.getPrefBoolean(PrefConstant.APP_IS_WELCOME)) {

            /*Intent intent = new Intent(mContext, WelcomeActivity.class);
            startActivity(intent);
            finish();*/

            btnPlayNow.setVisibility(View.VISIBLE);
            imgBottom.setVisibility(View.GONE);
        } else {
            if (preferences.getPrefBoolean(PrefConstant.APP_IS_LOGIN)) {//preferences.getUserModel().getId() != null && !preferences.getUserModel().getId().equals("")
                // getUserDetails();
                if (goHome){
                    Intent intent = new Intent(mContext, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    getAppDataList();
                }

                /**/
            } else {
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

    private void getMenuData() {
        HttpRestClient.postJSON(mContext, false, ApiManager.MENU_LIST, new JSONObject(), new GetApiResult() {

            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    JSONArray array = responseBody.optJSONArray("data");
                    LogUtil.e(TAG, "getMenuData: " + responseBody.toString());
                    int i = 0;
                    List<MenuModel> menuModels = new ArrayList<>();

                    for (i = 0; i < array.length(); i++) {
                        JSONObject object = array.optJSONObject(i);
                        if (!object.optString("key").equals("menu_dashboard")) {
                            MenuModel menuModel = gson.fromJson(CustomUtil.replaceNull(object.toString()), MenuModel.class);
                            menuModels.add(menuModel);
                        }
                    }
                    preferences.setMenu(menuModels);
                    mMenu = true;
                    getSportsList();

                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }

        });
    }

    private class DownloadAsync extends AsyncTask<String, Long, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*builder = new AlertDialog.Builder(mContext);
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Connecting to server...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();*/
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String URL = params[0];
            if (URL == null || TextUtils.isEmpty(URL)) {
                URL = "https://ffcdn.fantafeat.com/apk/fantafeat.apk";
            }
            OkHttpClient httpClient = new OkHttpClient();
            Call call = httpClient.newCall(new Request.Builder().url(URL).get().build());
            try {
                Response response = call.execute();
                if (response.code() == 200) {
                    InputStream inputStream = null;
                    try {
                        inputStream = response.body().byteStream();
                        byte[] buff = new byte[12451982];
                        long downloaded = 0;
                        long target = response.body().contentLength();

                        //String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";

                        final File mainFile = new File(Environment.getExternalStorageDirectory() + "/Download/");//

                        if (!mainFile.exists()) {
                            if (mainFile.mkdir()) {
                                LogUtil.e(TAG, "download: ");
                            }
                        }
                        file = new File(mainFile, "Fantafeat.apk");
                        if (file.exists()) {
                            file.delete();
                        }
                        OutputStream output = new FileOutputStream(file);

                        publishProgress(0L, target);
                        int count;
                        while ((count = inputStream.read(buff)) != -1) {
                            //int readed = inputStream.read(buff);
                        /*if(readed == -1){
                            break;
                        }*/
                            //write buff
                            downloaded += count;
                            publishProgress(downloaded, target);
                            if (isCancelled()) {
                                return false;
                            }
                            output.write(buff, 0, count);
                        }

                        output.flush();
                        output.close();

                        return downloaded == target;
                    } catch (IOException ignore) {
                        return false;
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    }
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
            progress.setMax(values[1].intValue());
            progress.setProgress(values[0].intValue());

            //float downloading =  CustomUtil.convertFloat(String.format("%d / %d", values[0], values[1]));
            //   long progress = (values[0] * 100) / values[1];
            // progressDialog.setMessage(progress + "% Download..");
            //textViewProgress.setText(String.format("%d / %d", values[0], values[1]));
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            /*if (progressDialog != null) {
                progressDialog.dismiss();
            }*/
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                //preferences.clearData();
                if (Build.VERSION.SDK_INT >= 24) {
                    LogUtil.e(TAG, "onPostExecute: " + file);
                    intent.setDataAndType(FileProvider.getUriForFile(mContext,
                                    BuildConfig.APPLICATION_ID + ".provider", file),
                            "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                }
                // intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                // intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                startActivity(intent);
            } catch (Exception e) {
                LogUtil.e(TAG, "onPostExecute: " + e.toString());
            }
        }
    }

}
