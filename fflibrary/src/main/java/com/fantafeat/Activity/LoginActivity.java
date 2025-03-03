package com.fantafeat.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.fantafeat.BuildConfig;
import com.fantafeat.Model.MenuModel;
import com.fantafeat.Model.SportsModel;
import com.fantafeat.Model.UpdateModel;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.PrefConstant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

   // FrameLayout login_fragment;
    Context mContext;
    private EditText mPhone;
    private TextView txtForgot,txtSignup,txtTnc;
    private Button btnLogin;
    private boolean isPermission=false;
    private boolean isSettingOpen = false;
    private boolean isResume = false;
    private String refferCode="";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    FusedLocationProviderClient fusedLocationClient;
    AlertDialog.Builder builder;
    private boolean isDialog = false;
    private boolean updateDialogShown = false;

    private static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    String[] p;

    private JSONObject currentData;
    private BottomSheetDialog bottomSheetDialog;
    private ProgressBar progress;
    private File file;

    @Override
    public void initClick(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }

        mContext=this;

        initData();

        getAppDataList();

    }

    public void HideKeyboardFormUser(){
        View view = getCurrentFocus();
        InputMethodManager hideKeyboard  = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        hideKeyboard.hideSoftInputFromWindow( view.getWindowToken(), 0);
    }

   /* login_fragment = findViewById(R.id.login_fragment);
    getSupportFragmentManager().beginTransaction().add(R.id.login_fragment,
            new PhoneNumberFragment()).commit();*/

    private void checkAutoRefer() {
        try {
            String path = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DOWNLOADS;

            File file = new File(path);
            //mRootPath = file.getAbsoluteFile().getPath();

            ArrayList<String> mFileNames = new ArrayList<String>();

            File filesInDirectory[] = file.listFiles();

            if (filesInDirectory != null) {
                boolean referralCodeFound = false;

                for (int i = 0; i < filesInDirectory.length; i++) {
                    mFileNames.add(filesInDirectory[i].getName());
                    String fileName = filesInDirectory[i].getName();
                    LogUtil.e("resp", "Fantafeat file size: " + fileName);

                    if (fileName.contains("fantafeat-") && fileName.length() > 11) {
                        try {
                            LogUtil.e(TAG, "Fantafeat file get: " + filesInDirectory[i].getName());

                            String[] parts = fileName.split("-");

                            if (parts.length > 1) {
                                String possibleReferralCode = parts[1];

                                if (possibleReferralCode.endsWith(".apk")) {
                                    possibleReferralCode = possibleReferralCode.replace(".apk", "");
                                }
                                LogUtil.e("resp", "possibleReferralCode: " + possibleReferralCode);
                                if (possibleReferralCode.contains("(")){
                                    String[] arr=possibleReferralCode.split("\\(");
                                    possibleReferralCode=arr[0].trim();
                                }
                                LogUtil.e("resp", "possibleReferralCode: " + possibleReferralCode);
                                refferCode=possibleReferralCode;
                                referralCodeFound = true;
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtil.e(TAG, "Error: " + e.toString());
                        }
                    }
                }

                if (!referralCodeFound) {
                    refferCode="";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isResume) {
            isResume = true;
            Dexter.withActivity((Activity) mContext)
                    .withPermissions(p)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                checkAutoRefer();
                                /*isPermission = true;
                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                    // if (!Environment.isExternalStorageManager()){
                                    if (!Environment.isExternalStorageManager() && !isSettingOpen) {
                                        isSettingOpen = true;
                                        LogUtil.e("resp", "R");
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                        intent.setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                                        //startActivityForResult(intent, 1234);
                                        someActivityResultLauncher.launch(intent);
                                    }else {
                                        checkAutoRefer();
                                    }
                                } else {
                                    checkAutoRefer();
                                }*/
                            }
                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                LogUtil.e(TAG, "onPermissionsChecked: ");
                                //showSettingsDialog();
                                // permission is denied permenantly, navigate user to app settings
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
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                //if (result.getResultCode()==Activity.RESULT_OK){
                    checkAutoRefer();
                //}
            });
/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == Activity.RESULT_OK) {
            checkAutoRefer();
        }
    }*/


    private void getAppDataList() {
        try {
            JSONObject param = new JSONObject();
            if (preferences.getUserModel() != null && !TextUtils.isEmpty(preferences.getUserModel().getId())) {
                param.put("user_id", preferences.getUserModel().getId());
                param.put("token_no", preferences.getUserModel().getTokenNo());
            }
            LogUtil.e(TAG, "getAppDataList Param: " + param.toString());
            HttpRestClient.postJSON(mContext, true, ApiManager.appDataList, param, new GetApiResult() {
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

               /* if (ConstantUtil.is_game_test){
                    try {
                        check_app.put("update_type","normal_update");
                        check_app.put("v",1017);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }*/

               /* LogUtil.e("VERSION_CHK", "vverr=>" + check_app.toString());
                check_app.put("v", "1010");
                check_app.put("update_type", "normal_update");

                check_app.put("is_play_store_download", "no");
                LogUtil.e("after_VERSION_CHK", "aftervverr=>" + check_app.toString());
                LogUtil.e("Urrl", check_app.optString("app_url"));*/

                if (check_app != null) {
                    LogUtil.e("VERsionnme", "vverr=>" + check_app.optString("v"));
                    if (CustomUtil.convertFloat(check_app.optString("v")) > BuildConfig.VERSION_CODE) {
                        showAppUpdate(check_app);
                        return true;
                    } else if (check_app.optString("maintenance").equalsIgnoreCase("yes")) {
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

            if (sport_list != null) {
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
        }

        //checkLogin();
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
            //mUpdate = false;
            bottomSheetDialog.dismiss();
            if (data.optString("update_type").equalsIgnoreCase("force_update")) {
                LogUtil.e("FForce","updateForce");
                getAppDataList();
            }else if (data.optString("update_type").equalsIgnoreCase("normal_update")){
                LogUtil.e("NNormal","updateNormal");
                if (preferences.getUserModel() != null && TextUtils.isEmpty(preferences.getUserModel().getId())) {
                    setOtherData();
                } /*else {
                    checkLogin();
                }*/
            }


        });
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);
        if (mContext != null && !this.isFinishing() && !bottomSheetDialog.isShowing()) {
            bottomSheetDialog.show();
        }
    }

    private void initData() {

        mPhone=findViewById(R.id.mPhone);
        mPhone.setFilters(new InputFilter[] {ConstantUtil.filter,ConstantUtil.EMOJI_FILTER });
        mPhone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10), ConstantUtil.filter,ConstantUtil.EMOJI_FILTER });
        mPhone.requestFocus();
        mPhone.setLongClickable(false);

        txtSignup=findViewById(R.id.txtSignup);
        btnLogin=findViewById(R.id.btnLogin);
        txtTnc=findViewById(R.id.txtTnc);

        txtSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        txtTnc.setOnClickListener(this);

        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 10){
                    HideKeyboardFormUser();
                }
            }

        });

    }


    private boolean checkNumber() {
        if(mPhone.getText().toString().trim().equals("")){
            CustomUtil.showTopSneakWarning(this,"Please Enter Valid Number");
            return false;
        }
        else if(mPhone.getText().toString().trim().length()!= 10){
            CustomUtil.showTopSneakWarning(this,"Please Enter Valid Number");
            return false;
        }
        return true;
    }

    /* public String fragmentTag(int position) {
        String[] drawer_tag = getResources().getStringArray(R.array.stack);
        return drawer_tag[position];
    }*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //quitApp();
        finish();
    }

    private void quitApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                finishAffinity();
            }
        });

        AlertDialog alert = builder.create();
        alert.setCancelable(true);
        alert.show();
    }

    private void makeRequest(){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone_no", mPhone.getText().toString());
            jsonObject.put("ref_by", refferCode);
            jsonObject.put("type", "login");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "makeRequest: "+ jsonObject.toString() );
        HttpRestClient.postJSON(mContext, true, ApiManager.REGISTER, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.d("loginSucc",responseBody+" ");
                if(responseBody.optBoolean("status")){
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody.optString("data"));
                        UserModel userModel = gson.fromJson(jsonObject.toString(), UserModel.class);
                        MyApp.USER_ID = jsonObject.optString("id");
                        MyApp.APP_KEY = jsonObject.optString("token_no");
                        preferences.setUserModel(userModel);
                        preferences.setPref(PrefConstant.APP_IS_LOGIN,true);
                        if (isPermission){
                            deleteStoredApks();
                        }
                        Intent intent = new Intent(mContext, HomeActivity.class);
                        startActivity(intent);
                        ((LoginActivity)mContext).finish();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
            }
        });
    }

    private void makeRequestUser(){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone_no", mPhone.getText().toString());
            jsonObject.put("ref_by", refferCode);
            jsonObject.put("type", "login");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "makeRequest: "+ jsonObject.toString() );
        HttpRestClient.postJSON(mContext, true, ApiManager.adminUserDetailsCheck, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.d("loginSucc",responseBody+" ");
                if(responseBody.optBoolean("status")){
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody.optString("data"));
                        UserModel userModel = gson.fromJson(jsonObject.toString(), UserModel.class);
                        MyApp.USER_ID = jsonObject.optString("id");
                        MyApp.APP_KEY = jsonObject.optString("token_no");
                        preferences.setUserModel(userModel);
                        preferences.setPref(PrefConstant.APP_IS_LOGIN,true);
                        if (isPermission){
                            deleteStoredApks();
                        }
                        Intent intent = new Intent(mContext, HomeActivity.class);
                        startActivity(intent);
                        ((LoginActivity)mContext).finish();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
            }
        });
    }

    private void deleteStoredApks(){
        try {
            String downPath=Environment.getExternalStorageDirectory() + "/Download/";
            LogUtil.e("downPath",downPath+" ");
            File downFile=new File(downPath);
            File[] allFiles=downFile.listFiles();
            LogUtil.e("downPath",allFiles.length+" ");
            // List<String> pathList=new ArrayList<>();
            for (File file:allFiles){
               // LogUtil.e("downPath",file.getName()+" name");
               // File file=new File(path);
                if (file.exists() && !file.isDirectory()){
                    LogUtil.e("downPath",file.getName()+" ");
                    if (file.getName().toLowerCase().contains("fantafeat")){
                        //pathList.add(path);
                       // boolean isdelete=deleteFile(file.getAbsolutePath());
                        boolean isdelete=file.getCanonicalFile().delete();
                        LogUtil.e("downPath"," isdelete "+isdelete);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnLogin) {
            if (MyApp.getClickStatus()) {
                if (checkNumber()) {
                    makeRequest();
                }
            }
        } else if (id == R.id.txtSignup) {
            startActivity(new Intent(this, SignUpActivity.class));
        } else if (id == R.id.txtTnc) {
            startActivity(new Intent(mContext, WebViewActivity.class)
                    .putExtra(ConstantUtil.WEB_TITLE, "Terms & Condition")
                    .putExtra(ConstantUtil.WEB_URL, ApiManager.term_condition_url));
         /*   case R.id.txtForgot:{
                startActivity(new Intent(this,ForgotPasswordActivity.class));
                break;
            }*/
        }
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
                        file = new File(mainFile,  "Fantafeat.apk");
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