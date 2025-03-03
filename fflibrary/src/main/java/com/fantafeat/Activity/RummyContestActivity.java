package com.fantafeat.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fantafeat.Adapter.LudoContestAdapter;

import com.fantafeat.BuildConfig;
import com.fantafeat.Model.GameContestModel;
import com.fantafeat.Model.GameMainModal;
import com.fantafeat.Model.GameSubTypeModel;
import com.fantafeat.Model.Games;
import com.fantafeat.Model.LudoWaitingModal;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.JSONParser;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.PrefConstant;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RummyContestActivity extends BaseActivity implements LudoContestAdapter.ItemClickListener {

    private GameSubTypeModel gameModel;
    private Games game;
    private RecyclerView recyclerContest;
    private Dialog updateDialog;
    private Boolean isSettingOpen = false,isAllPermission=false;
    private JSONObject appUpdateData = new JSONObject();
    private ArrayList<GameContestModel> mDataList;
    private Socket mSocket= null;
    private LudoContestAdapter mAdapter;
    private String socket_url;
    private boolean isPlayWithCode=false,isPerDialog=false;;

    private ImageView imgShowBal,imgBack,imgWallet,toolbar_info;//,imgGameLogo ;
    private TextView txtTitle,txtMoreLbl,txtTest,txtAnounce;
    private LinearLayout layWallet,layWalletMain,btnMore,layBal;
    private Animation slideUp;
    private Animation slideDown;
    private String gameType="",asset_type="",game_id="",assets_path="",android_asset_url="",ios_asset_url="",tnc="",selectedGameMode="",token_generate_url="";

    private Dialog dialog;
    private boolean isRejoinGame=true,isLogout=false,isRejoinEmit=false,is_auto_game_start=false,is_contest_waiting=false;
    private String lastSelectedTab="";

    LinearLayout layBtn,layProgress,layTabs;
    TextView txtProgress;
    ProgressBar progress;

    private TextView txtYes;
    private RelativeLayout layAsset,bac_dim_layout;

    float  use_deposit = 0;
    float  use_transfer =  0;
    float  use_winning =  0;
    float  use_donation_deposit = 0;
    float  useBonus = 0;
    float  useCoin = 0;
    float  amtToAdd = 0;
    private PopupWindow popupWindow;
    private CardView mainWallet;

    private ArrayList<Games> allGamesList=new ArrayList<>();

    private static String[] storge_permissions = {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTransparent();
        setContentView(R.layout.activity_rummy_contest);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }

        slideDown = AnimationUtils.loadAnimation(mContext,
                R.anim.slide_down_wallet);
        slideUp = AnimationUtils.loadAnimation(mContext,
                R.anim.slide_up);

        initData();
        initClick();

    }

    public void initClick() {
        imgWallet.setOnClickListener(v -> {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View popupView = layoutInflater.inflate(R.layout.popup_wallet, null);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            popupWindow = new PopupWindow(
                    popupView,
                    displayMetrics.widthPixels,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            popupWindow.showAsDropDown(v, 0, 60);

            TextView txtTotalBal=popupView.findViewById(R.id.txtTotalBal);
            TextView txtDepositBal=popupView.findViewById(R.id.txtDepositBal);
            TextView txtWinningBal=popupView.findViewById(R.id.txtWinningBal);
            TextView txtBonusBal=popupView.findViewById(R.id.txtBonusBal);
            CardView layBonus=popupView.findViewById(R.id.layBonus);
            //TextView txtCoinBal=popupView.findViewById(R.id.txtCoinBal);
            mainWallet = popupView.findViewById(R.id.mainWallet);

            float deposit_bal = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
            float winning_bal = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
            float reward_bal = CustomUtil.convertFloat(preferences.getUserModel().getBonusBal());
            float coin_bal = CustomUtil.convertFloat(preferences.getUserModel().getFf_coin());
            //float old_coin_bal = CustomUtil.convertFloat(preferences.getUserModel().getOld_ff_coins_bal());

            float available_bal;
            if (ConstantUtil.is_bonus_show){
                available_bal = deposit_bal + winning_bal /*+ borrowed_bal*/ + reward_bal + coin_bal;
                layBonus.setVisibility(View.VISIBLE);
            }else {
                available_bal = deposit_bal + winning_bal /*+ borrowed_bal*/ + coin_bal;
                layBonus.setVisibility(View.GONE);
            }
            //float available_bal = deposit_bal + winning_bal  + reward_bal + coin_bal;

            txtTotalBal.setText(mContext.getResources().getString(R.string.rs) +
                    CustomUtil.getFormater("00.00").format((available_bal/*+old_coin_bal*/)));
            txtDepositBal.setText(mContext.getResources().getString(R.string.rs) +
                    CustomUtil.getFormater("00.00").format((deposit_bal+coin_bal)));
            txtWinningBal.setText(mContext.getResources().getString(R.string.rs) +
                    CustomUtil.getFormater("00.00").format(winning_bal));
            txtBonusBal.setText(mContext.getResources().getString(R.string.rs) +
                    CustomUtil.getFormater("00.00").format(reward_bal));
           /* txtCoinBal.setText(mContext.getResources().getString(R.string.rs) +
                    CustomUtil.getFormater("00.00").format(coin_bal));*/

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                }
            });

            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    bac_dim_layout.setVisibility(View.GONE);
                    mainWallet.startAnimation(slideUp);
                }
            });

            bac_dim_layout.setVisibility(View.VISIBLE);
            mainWallet.startAnimation(slideDown);

            popupWindow.showAsDropDown(v);
        });

        bac_dim_layout.setOnClickListener(v -> {
            if (popupWindow!=null && popupWindow.isShowing()){
                popupWindow.dismiss();
            }
            if (mainWallet!=null){
                mainWallet.startAnimation(slideUp);
            }
            bac_dim_layout.setVisibility(View.GONE);

        });

        toolbar_info.setOnClickListener(v -> {
            if(MyApp.getClickStatus()) {
                getTNCData();
                /*View view = LayoutInflater.from(mContext).inflate(R.layout.ludo_tnc_dialog, null);
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.setCancelable(false);
                ((View) view.getParent()).setBackgroundResource(android.R.color.white);

                TextView txtTitle = view.findViewById(R.id.txtTitle);
                TextView txtTnc = view.findViewById(R.id.txtTnc);
                ImageView imgClose = view.findViewById(R.id.imgClose);

                txtTitle.setText(game.getGameName()+" Rules");

                imgClose.setOnClickListener(vi->{
                    bottomSheetDialog.dismiss();
                });

                if (!TextUtils.isEmpty(game.getGamesTnc())){
                    txtTnc.setText(game.getGamesTnc());
                    bottomSheetDialog.show();
                }*/
            }
        });
    }

    private void initData() {

        imgBack = findViewById(R.id.toolbar_back);
        imgWallet = findViewById(R.id.toolbar_wallet);
        txtTitle = findViewById(R.id.txtTitle);
        txtTest = findViewById(R.id.txtTest);
        recyclerContest = findViewById(R.id.recyclerContest);
        layTabs = findViewById(R.id.layTabs);
        txtAnounce = findViewById(R.id.txtAnounce);
        txtAnounce.setSelected(true);
        bac_dim_layout = findViewById(R.id.bac_dim_layout);
        toolbar_info = findViewById(R.id.toolbar_info);

        mDataList = new ArrayList();

        /*if (getIntent().hasExtra("rummy_game")){
            gameModel= (GameSubTypeModel) getIntent().getSerializableExtra("rummy_game");
            txtTitle.setText(gameModel.getModeName());
        }*/
          if (getIntent().hasExtra("allGamesList")){
            allGamesList= (ArrayList<Games>) getIntent().getSerializableExtra("allGamesList");
        }

        if (getIntent().hasExtra("gameData")){
            game= (Games) getIntent().getSerializableExtra("gameData");
            if (ConstantUtil.is_game_test){
                android_asset_url=game.getAndroidAssetUrlTest();
                socket_url=game.getSocket_url_test();//"https://rummy.ezypocket.com:8034";
                // socket_url=game.getSocket_url_test();
                txtTest.setText("TEST MODE : Activated \nSocket url:"+socket_url+"\nAsset Url:"+android_asset_url+"\nGame Type:"+game.getType());
                token_generate_url=game.getToken_create_url_test();
                txtTest.setVisibility(View.VISIBLE);
            }
            else {
                socket_url=game.getSocketUrl();
                android_asset_url=game.getAndroidAssetUrl();
                token_generate_url=game.getToken_create_url();
                txtTest.setVisibility(View.GONE);
            }
            gameType = game.getType();
            //tnc=game.getGames_tnc();

            game_id=game.getGameCode()+"";
            asset_type=game.getAssetType()+"/";
            ios_asset_url=game.getIosAssetUrl();
            txtTitle.setText(game.getGameName());
            is_auto_game_start= game.getIs_auto_game_start().equalsIgnoreCase("yes");
            is_contest_waiting= game.getIs_contest_waiting().equalsIgnoreCase("yes");
        }

        imgBack.setOnClickListener(view -> {
            finish();
        });

        LinearLayoutManager manager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerContest.setLayoutManager(manager);
        mAdapter = new LudoContestAdapter(mDataList,this);
        recyclerContest.setAdapter(mAdapter);

        // getRummyContestList();

    }

    @Override
    protected void onResume() {
        super.onResume();

        getUserDetail();
        initTabs();

    }

    private void initTabs(){
        try {
            layTabs.removeAllViews();
            JSONArray array=new JSONArray(game.getGame_mode());

            for (int i = 0;i<array.length();i++){
                JSONObject obj=array.optJSONObject(i);
                GameSubTypeModel gameSubTypeModel=gson.fromJson(obj.toString(),GameSubTypeModel.class);

                if (gameSubTypeModel.getIsActive().equalsIgnoreCase("yes")){
                    TextView textView=new TextView(mContext);
                    LinearLayout.LayoutParams paramsView=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,8);
                    LinearLayout.LayoutParams paramsTv=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1f);
                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1f);
                    params.setMargins(15,0,15,0);
                    LinearLayout innerLayout=new LinearLayout(mContext);
                    View line=new View(mContext);
                    line.setLayoutParams(paramsView);
                    line.setBackgroundColor(mContext.getResources().getColor(R.color.white_pure));
                    innerLayout.setOrientation(LinearLayout.VERTICAL);

                    innerLayout.setLayoutParams(params);
                    textView.setLayoutParams(paramsTv);

                    textView.setText(gameSubTypeModel.getModeName());
                    textView.setGravity(Gravity.CENTER);

                    if (TextUtils.isEmpty(selectedGameMode)){
                        if (gameSubTypeModel.getIs_default().equalsIgnoreCase("yes")){
                            selectedGameMode=gameSubTypeModel.getModeName();
                            gameSubTypeModel.setSelected(true);

                        }
                    }
                    else if (selectedGameMode.equalsIgnoreCase(gameSubTypeModel.getModeName())){
                        selectedGameMode=gameSubTypeModel.getModeName();
                        gameSubTypeModel.setSelected(true);
                    }
                    // textView.setTag(gameSubTypeModel.isSelected());

                    if (gameSubTypeModel.isSelected()){
                        textView.setTextSize(16f);
                        textView.setTypeface(null, Typeface.BOLD);
                        textView.setTextColor(mContext.getResources().getColor(R.color.white_pure));

                        gameModel= gameSubTypeModel;
                        line.setVisibility(View.VISIBLE);

                        selectedGameMode=gameSubTypeModel.getModeName();

                        getRummyContestList();
                    }
                    else {
                        textView.setTextSize(14f);
                        textView.setTypeface(null, Typeface.NORMAL);
                        textView.setTextColor(mContext.getResources().getColor(R.color.white_pure));
                        line.setVisibility(View.GONE);
                    }

                    textView.setOnClickListener(view -> {
                        for (int j = 0;j<layTabs.getChildCount();j++) {
                            LinearLayout layout= (LinearLayout) layTabs.getChildAt(j);
                            for (int k = 0; k <layout.getChildCount(); k++) {
                                if (layout.getChildAt(k) instanceof TextView){
                                    TextView tv= (TextView) layout.getChildAt(k);
                                    // tv.setTag(false);
                                    tv.setTextSize(14f);
                                    tv.setTypeface(null, Typeface.NORMAL);
                                    tv.setTextColor(mContext.getResources().getColor(R.color.white_pure));

                                }
                                else if (layout.getChildAt(k) instanceof View){
                                    layout.getChildAt(k).setVisibility(View.GONE);
                                }
                            }
                        }
                        // textView.setTag(true);
                        gameSubTypeModel.setSelected(true);
                        layTabs.invalidate();
                        textView.setTextSize(16f);
                        textView.setTypeface(null, Typeface.BOLD);
                        textView.setTextColor(mContext.getResources().getColor(R.color.white_pure));
                        line.setVisibility(View.VISIBLE);

                        gameModel= gameSubTypeModel;
                        selectedGameMode=gameSubTypeModel.getModeName();

                        getRummyContestList();
                    });

                    innerLayout.addView(textView);
                    innerLayout.addView(line);

                    layTabs.addView(innerLayout);

                }
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getTNCData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("game_id", game.getGameCode());
            jsonObject.put("comp_id", "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG,jsonObject.toString());

        HttpRestClient.postJSONNormal(RummyContestActivity.this, true, ApiManager.gamesTncGetFF,
                jsonObject, new GetApiResult() {
                    @Override
                    public void onSuccessResult(JSONObject responseBody, int code) {
                        LogUtil.e("TAG", "gamesTncGetFF: " + responseBody.toString());
                        if (responseBody.optBoolean("status")){
                            JSONObject data=responseBody.optJSONObject("data");

                            if (data.optString("game_tnc_type").equalsIgnoreCase("Text")){
                                View view = LayoutInflater.from(mContext).inflate(R.layout.ludo_tnc_dialog, null);
                                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
                                bottomSheetDialog.setContentView(view);
                                bottomSheetDialog.setCancelable(false);
                                ((View) view.getParent()).setBackgroundResource(android.R.color.white);

                                TextView txtTitle = view.findViewById(R.id.txtTitle);
                                TextView txtTnc = view.findViewById(R.id.txtTnc);
                                ImageView imgClose = view.findViewById(R.id.imgClose);

                                txtTitle.setText(data.optString("game_name")+" Rules");

                                imgClose.setOnClickListener(vi->{
                                    bottomSheetDialog.dismiss();
                                });

                                if (!TextUtils.isEmpty(data.optString("games_tnc"))){

                                    txtTnc.setText(data.optString("games_tnc"));

                                    bottomSheetDialog.show();
                                }
                            }else {
                                startActivity(new Intent(mContext, WebViewActivity.class)
                                        .putExtra(ConstantUtil.WEB_TITLE,data.optString("game_name")+" Rules")
                                        .putExtra(ConstantUtil.WEB_URL,data.optString("games_tnc")));
                            }


                        }else {
                            Toast.makeText(getApplicationContext(), responseBody.optString("msg"), Toast.LENGTH_LONG).show();
                            if (code==1000){
                                isRejoinGame=false;
                                isLogout=true;
                                if (dialog!=null && dialog.isShowing()){
                                    dialog.dismiss();
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailureResult(String responseBody, int code) {

                    }
                });

    }

    public void getRummyContestList() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("user_id",preferences.getUserModel().getId());
            jsonObject.put("game_id",game.getGameCode());
            jsonObject.put("game_mode",gameModel.getModeName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "getContestList: "+jsonObject.toString());

        HttpRestClient.postJSONNormal(RummyContestActivity.this, true, ApiManager.RUMMY_CONTEST,
                jsonObject, new GetApiResult() {
                    @Override
                    public void onSuccessResult(JSONObject responseBody, int code) {
                        LogUtil.e("TAG", "getRummyContestList: " + responseBody.toString());

                        if (responseBody.optBoolean("status")){

                            tnc=responseBody.optString("tnc");
                            if (TextUtils.isEmpty(tnc) || tnc.equalsIgnoreCase("null")){
                                txtAnounce.setVisibility(View.GONE);
                            }
                            else {
                                txtAnounce.setVisibility(View.VISIBLE);
                                txtAnounce.setText(tnc);
                            }
                            JSONObject ludo_update_check_android=responseBody.optJSONObject("ludo_update_check_android");

                            /*try {
                                ludo_update_check_android.put("update_type","force_update");
                                ludo_update_check_android.put("v","1002");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }*/

                            LogUtil.e("TAG", "ludo_update_check_android: " + ludo_update_check_android.toString());

                            /*  if (ConstantUtil.is_game_test){
                                setContestData(responseBody);
                            }else {

                            }*/
                            if (ludo_update_check_android.optString("update_type").equalsIgnoreCase("force_update")) {
                                if (CustomUtil.convertInt(ludo_update_check_android.optString("v")) > BuildConfig.VERSION_CODE) {
                                    updateApp(ludo_update_check_android);
                                }else {
                                    setContestData(responseBody);
                                }
                            }
                            else if (ludo_update_check_android.optString("update_type").equalsIgnoreCase("normal_update")){
                                if (CustomUtil.convertInt(ludo_update_check_android.optString("v")) > BuildConfig.VERSION_CODE) {
                                    updateApp(ludo_update_check_android);
                                }
                                setContestData(responseBody);
                            }
                            else if (ludo_update_check_android.optString("update_type").equalsIgnoreCase("maintenance")){
                                updateApp(ludo_update_check_android);
                            }
                            else {
                                setContestData(responseBody);
                            }

                        }else {
                            if (code==1000){
                                isRejoinGame=false;
                                isLogout=true;
                                if (dialog!=null && dialog.isShowing()){
                                    dialog.dismiss();
                                }
                            }
                        }

                    }
                    @Override
                    public void onFailureResult(String responseBody, int code) {}
                });

    }

    public void getUserDetail(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("token_no", preferences.getUserModel().getTokenNo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "PARAM onSuccessResult: " + jsonObject.toString());
        HttpRestClient.postJSON(RummyContestActivity.this, false, ApiManager.AUTHV3GetUserDetails, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e(TAG, "USER onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    if (responseBody.optString("code").equals("200")) {
                        JSONObject data = responseBody.optJSONObject("data");
                        UserModel userModel = gson.fromJson(data.toString(), UserModel.class);
                        float total;
                        if (ConstantUtil.is_bonus_show){
                            total = CustomUtil.convertFloat(userModel.getDepositBal()) + CustomUtil.convertFloat(userModel.getWinBal())
                                    + CustomUtil.convertFloat(userModel.getFf_coin()) + CustomUtil.convertFloat(userModel.getBonusBal());

                        }else {
                            total =  CustomUtil.convertFloat(userModel.getDepositBal()) + CustomUtil.convertFloat(userModel.getWinBal())
                                    + CustomUtil.convertFloat(userModel.getFf_coin());

                        }
                        /*float total = CustomUtil.convertFloat(userModel.getDepositBal()) + CustomUtil.convertFloat(userModel.getWinBal())
                                + CustomUtil.convertFloat(userModel.getFf_coin()) + CustomUtil.convertFloat(userModel.getBonusBal());*/
                        userModel.setTotal_balance(total);
                        preferences.setUserModel(userModel);
                    }
                }else {
                    if (code==1000){
                        isRejoinGame=false;
                        isLogout=true;
                        if (dialog!=null && dialog.isShowing()){
                            dialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

                //preferences.setUserModel(new UserModel());

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        offSocket();
    }

    private void updateApp(JSONObject data){
        if (updateDialog!=null && updateDialog.isShowing()){
            return;
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.app_update_dialog, null);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView btnBackToHome = view.findViewById(R.id.btnBackToHome);
        TextView app_update_msg = view.findViewById(R.id.app_update_msg);
        TextView txtSubTitle = view.findViewById(R.id.txtSubTitle);
        ImageView imageView3 = view.findViewById(R.id.imageView3);
        ImageView imgClose = view.findViewById(R.id.imgClose);
        progress = view.findViewById(R.id.progress);
        btnBackToHome.setVisibility(View.VISIBLE);

        if (data.optString("update_type").equalsIgnoreCase("force_update")) {
            imgClose.setVisibility(View.GONE);
            txtTitle.setText("App Update");
            txtSubTitle.setText("New Version Available.Download now.");
        }else if (data.optString("update_type").equalsIgnoreCase("maintenance")){
            imgClose.setVisibility(View.GONE);
            txtTitle.setText("Maintenance");
            txtSubTitle.setText("App is Under Maintenance");
            app_update_msg.setVisibility(View.VISIBLE);
            btnBackToHome.setVisibility(View.GONE);
            app_update_msg.setText(Html.fromHtml(data.optString("update_message")));

            btnUpdate.setText("Back To Home");
        }
        else {
            imgClose.setVisibility(View.VISIBLE);
            txtTitle.setText("App Update");
            txtSubTitle.setText("New Version Available.Download now.");
        }

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        btnUpdate.setOnClickListener(view1 -> {
            if (data.optString("update_type").equalsIgnoreCase("maintenance")){
                bottomSheetDialog.dismiss();
                finish();
            }else {
                progress.setVisibility(View.VISIBLE);
                app_update_msg.setVisibility(View.VISIBLE);
                btnUpdate.setVisibility(View.GONE);
                imageView3.setVisibility(View.GONE);
                btnBackToHome.setVisibility(View.GONE);
                txtTitle.setText("What’s new in Update");
                txtSubTitle.setText("There is new version is available for download! Please update the app by visiting www.fantafeat.com");
                app_update_msg.setText(Html.fromHtml(data.optString("update_message")));
           /* String[] p;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                p = storge_permissions_33;
            } else {
                p = storge_permissions;
            }*/
                Dexter.withActivity((Activity) mContext)
                        .withPermissions(p)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    new DownloadAsync().execute(MyApp.getInstance().getApp_url());
                                }

                                // check for permanent denial of any permission
                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    LogUtil.e(TAG, "onPermissionsChecked: ");
                                    //showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.
                                    PermissionRequest> list, PermissionToken token) {
                                token.continuePermissionRequest();
                            }


                        })
                        .onSameThread()
                        .check();
            }


        });
        imgClose.setOnClickListener(v->{
            bottomSheetDialog.dismiss();
        });

        btnBackToHome.setOnClickListener(v -> {
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

    private void setContestData(JSONObject data){
        mDataList.clear();
        JSONArray array = data.optJSONArray("data");
        ArrayList<GameContestModel> list = new ArrayList<GameContestModel>();
        StringBuilder conIds= new StringBuilder();


        for (int i = 0;i<array.length();i++ ) {
            JSONObject mainObject = array.optJSONObject(i);
            if (mainObject.has("data")) {
                JSONArray contestArray = mainObject.optJSONArray("data");
                String title = mainObject.optString("title");
                String subTitle = mainObject.optString("sub_title");
                GameContestModel mainModal = new GameContestModel();
                mainModal.setType(0);
                mainModal.setTitle(title);
                mainModal.setSubTitle(subTitle);
                list.add(mainModal);

                for (int j = 0; j < contestArray.length(); j++) {
                    JSONObject obj = contestArray.optJSONObject(j);
                    GameContestModel modal = new GameContestModel();

                    conIds.append(obj.optString("id")).append(",");
                    modal.setType(1);
                    String entry_fee="",dis_amt="";

                   /* if (ConstantUtil.is_game_test){
                        if (obj.optString("entry_fee").equalsIgnoreCase("9")){
                            entry_fee = "0";
                            dis_amt = "0";
                        } else {
                            entry_fee = obj.optString("entry_fee");
                            dis_amt = obj.optString("dis_amt");
                        }
                    }else {
                        entry_fee = obj.optString("entry_fee");
                        dis_amt = obj.optString("dis_amt");
                    }*/
                    entry_fee = obj.optString("entry_fee");
                    dis_amt = obj.optString("dis_amt");

                    String no_spot = obj.optString("game_play_mode_1");
                    String bonus = obj.optString("bonus");
                    String id = obj.optString("id");
                    String game_time = obj.optString("game_time");
                    String game_round = obj.optString("game_round");
                    //String is_pass = obj.optString("is_pass");
                    modal.setGameId(obj.optString("game_id"));
                    modal.setGameMode(obj.optString("game_mode"));
                    modal.setGameSubMode(obj.optString("game_sub_mode"));
                    modal.setCommission(obj.optString("commission"));
                    modal.setGamePlayMode1(obj.optString("game_play_mode_1"));
                    modal.setGamePlayMode2(obj.optString("game_play_mode_2"));
                    modal.setGamePlayMode3(obj.optString("game_play_mode_3"));
                    modal.setGamePlayMode4(obj.optString("game_play_mode_4"));
                    modal.setGamePlayMode5(obj.optString("game_play_mode_5"));
                    modal.setIsActive(obj.optString("is_active"));
                    modal.setGameConTypeId(obj.optString("game_con_type_id"));
                    modal.setBonusText(obj.optString("bonus_text"));
                    modal.setBonus(obj.optString("bonus"));
                    modal.setGame_use_coin(obj.optString("game_use_coin"));

                    modal.setEntryFee(entry_fee);
                    modal.setNo_spot(no_spot);
                    modal.setDisAmt(dis_amt);
                    modal.setBonus(bonus);
                    modal.setGame_round(game_round);
                    modal.setGame_time(game_time);
                    modal.setAsset_type(asset_type);
                    // modal.setIs_pass(is_pass);
                    modal.setId(id);//entry_fee + "_" + no_spot
                   /* if (modal.getIs_pass()!=null && modal.getIs_pass().equalsIgnoreCase("yes")){
                        JSONArray my_pass_data=obj.optJSONArray("my_pass_data");
                        if (my_pass_data!=null && my_pass_data.length()>0){
                            for (int j = 0; j < my_pass_data.length(); j++) {
                                JSONObject passObj=my_pass_data.optJSONObject(j);
                                GameContestModel.PassDatam passDatam = new GameContestModel.PassDatam();
                                passDatam.setCreate_at(passObj.optString("create_at"));
                            }
                        }
                    }*/
                    list.add(modal);

                }
            }


        }

        mDataList.addAll(list);

        mAdapter.notifyDataSetChanged();

        try {
            IO.Options opts =new IO.Options();
            mSocket = IO.socket(socket_url, opts);
            connectSocket();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        listenWaiting();

        try {

            JSONObject data1=new JSONObject();
            JSONObject obj=new JSONObject();

            obj.put("rummy_mode",gameModel.getModeName());
            data1.put("data",obj);
            data1.put("en",ConstantUtil.WAITING_LUDO_USER);

            LogUtil.e("resp",ConstantUtil.WAITING_LUDO_USER+"    "+data1);
            mSocket.emit(ConstantUtil.WAITING_LUDO_USER,data1.toString());

            if (!isRejoinEmit){
                isRejoinEmit=true;
                emitRejoin();
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    private void emitRejoin(){
        JSONObject main=new JSONObject();
        JSONObject usr=new JSONObject();
        try {
            usr.put("userId",preferences.getUserModel().getId());
            usr.put("token_no",preferences.getUserModel().getTokenNo());
            main.put("data",usr);
            main.put("en",ConstantUtil.GET_PLAYER_DETAIL);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mSocket.emit(ConstantUtil.GET_PLAYER_DETAIL,main.toString());
    }

    private void offSocket(){
        if (!isPlayWithCode && mSocket!=null){
            mSocket.off(ConstantUtil.WAITING_LUDO_USER);
            mSocket.off(ConstantUtil.GET_PLAYER_DETAIL);
            // gameSocket.off(ConstantUtil.ALL_GAMES_DATA);
            /*mSocket.disconnect();
            gameSocket.disconnect();*/
        }
    }

    private void connectSocket(){
        mSocket.connect();
        //gameSocket.connect();
    }

    private void listenWaiting(){
        //mSocketClassic.off(ConstantUtil.WAITING_LUDO_USER);

        offSocket();

        mSocket.on(ConstantUtil.WAITING_LUDO_USER, args -> {
             LogUtil.e(ConstantUtil.WAITING_LUDO_USER,"Rummy Response:- "+args[0]);
            if (args[0]!=null){
                runOnUiThread(() -> {
                    try {
                        JSONObject object=new JSONObject(args[0].toString());
                        JSONArray array = object.optJSONArray("data");
                        // JSONObject data = object.optJSONObject("data");
                        if (array != null && array.length()>0){
                            for (int m=0;m<array.length();m++){
                                JSONObject data=array.optJSONObject(m);
                                if (data.has("game_mode") && data.optString("game_mode").equalsIgnoreCase(selectedGameMode)){
                                    if ( data.has("active_player")){
                                        //JSONArray seats=data.optJSONArray("active_player");
                                        if (mDataList!=null){
                                            for (int k=0;k<mDataList.size();k++){
                                                GameContestModel modal1 = mDataList.get(k);
                                                if (modal1.getType()==1){
                                                    if (modal1.getId().equalsIgnoreCase(data.optString("con_id"))){
                                                        JSONArray active_player=data.optJSONArray("active_player");
                                                        if (active_player.length()>0){
                                                            ArrayList<LudoWaitingModal> waitingModals=new ArrayList<LudoWaitingModal>();
                                                            for (int i=0;i<active_player.length();i++){
                                                                JSONObject playerObj=active_player.optJSONObject(i);
                                                                if (playerObj!=null && playerObj.has("user_avatar")){
                                                                    LudoWaitingModal waitingModal = new LudoWaitingModal();
                                                                    waitingModal.setUser_avatar(playerObj.optString("user_avatar"));
                                                                    waitingModal.setUser_team_name(" Player");//playerObj.optString("team_name")
                                                                    waitingModal.setUser_id(playerObj.optString("user_id"));
                                                                    waitingModals.add(waitingModal);
                                                                }
                                                            }

                                                            modal1.setWaitCount(waitingModals.size());
                                                            modal1.setWaitingModals(waitingModals);

                                                            if (active_player.length()==Integer.parseInt(modal1.getNo_spot())){
                                                                final Handler handler = new Handler(Looper.getMainLooper());
                                                                handler.postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        modal1.setWaitCount(0);
                                                                        modal1.setWaitingModals(new ArrayList<>());
                                                                        mAdapter.notifyDataSetChanged();
                                                                    }
                                                                }, 300);
                                                            }

                                                        }else {
                                                            modal1.setWaitCount(0);
                                                            modal1.setWaitingModals(new ArrayList<LudoWaitingModal>());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }
        });

        mSocket.on(ConstantUtil.GET_PLAYER_DETAIL,args -> {
             Log.e(ConstantUtil.GET_PLAYER_DETAIL,"GET_PLAYER_DETAIL:- "+args[0]);//{"con_id":0,"utoken":""}
            runOnUiThread(() -> {
                try {
                    if (isRejoinGame && !isLogout){
                        JSONObject main=new JSONObject(args[0].toString());
                        JSONObject obj=main.optJSONObject("data");

                        if (obj.has("utoken")){
                            if (!TextUtils.isEmpty(obj.optString("utoken"))){
                                if (obj.has("game_mode") && !obj.optString("game_mode").equalsIgnoreCase(selectedGameMode)){
                                    selectedGameMode=obj.optString("game_mode");
                                    initTabs();
                                    if (game.getAssetType().equalsIgnoreCase("callbreak")){
                                        if (obj.has("con_id") && !obj.optString("con_id").equalsIgnoreCase("0")){
                                            final Handler handler = new Handler(Looper.getMainLooper());
                                            handler.postDelayed(() -> checkRejoinGame(obj), 1000);
                                        }
                                    }else {
                                        final Handler handler = new Handler(Looper.getMainLooper());
                                        handler.postDelayed(() -> checkRejoinGame(obj), 1000);
                                    }
                                }else {
                                    checkRejoinGame(obj);
                                }
                            }
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        });
        // LogUtil.e("getContestList","listenWaiting");
    }

    private void checkRejoinGame(JSONObject obj){
        String utoken=obj.optString("utoken");

        GameContestModel contest=null;
        if (mDataList!=null) {
            for (int k = 0; k < mDataList.size(); k++) {
                GameContestModel modal1 = mDataList.get(k);
                if (modal1!=null && modal1.getType()==1) {
                    if (modal1.getId().equalsIgnoreCase(obj.optString("con_id"))){
                        contest=modal1;
                    }
                }
            }
        }
        if (contest!=null){

            if (dialog!=null && dialog.isShowing()){
                dialog.dismiss();
            }

            dialog = new Dialog(this);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.setContentView(R.layout.rejoin_dialog);
            dialog.setCancelable(false);

            TextView imgYes = dialog.findViewById(R.id.btnYes);
            ImageView imgNo = dialog.findViewById(R.id.imgClose) ;
            imgNo.setVisibility(View.GONE);
            layBtn = dialog.findViewById(R.id.layBtn);
            layProgress = dialog.findViewById(R.id.layProgress);
            txtProgress = dialog.findViewById(R.id.txtProgress);
            progress = dialog.findViewById(R.id.progress);

            if (!game.getAssetType().equalsIgnoreCase("webview"))
                checkAndDownloadAssets();

            dialog.setOnDismissListener(dialog1 -> {


            });

            imgNo.setOnClickListener(v ->  {
                if (MyApp.getClickStatus()){
                    dialog.dismiss();
                }
            });

            GameContestModel finalContest = contest;

            imgYes.setOnClickListener(v -> {
                if (MyApp.getClickStatus()){
                    dialog.dismiss();

                    if (game.getAssetType().equalsIgnoreCase("webview")){
                        startActivity(new Intent(mContext,WebViewGameActivity.class) .putExtra("gameData",game)
                                .putExtra("con_id", finalContest.getId()));
                    } else {
                        try {
                            UserModel user=preferences.getUserModel();

                            JSONObject object=new JSONObject();
                            object.put("con_id", finalContest.getId());
                            object.put("socketURL",socket_url.replaceAll("\\\\",""));//MyApp.getInstance().Ludo_Socket.replaceAll("\\\\","")
                            object.put("asset_path",assets_path);
                            object.put("game_type_name",gameType);
                            object.put("game_mode", finalContest.getGameMode());
                            object.put("game_mode_sub_type", finalContest.getGameSubMode());
                            object.put("game_type_name",game.getType());
                            object.put("other_game_data",game.getGame_mode());
                            object.put("user_avatar",user.getLevel3Id());
                            object.put("user_id",user.getId());
                            object.put("user_name",user.getUserTeamName());
                            object.put("user_team_name",user.getDisplayName());
                            object.put("utoken",utoken);
                            object.put("winning_amount", finalContest.getDisAmt());
                            object.put("comp_id",ConstantUtil.COMPANY_ID);
                            object.put("token_generate_url",token_generate_url);

                            String ludoType = "";
                            if (!TextUtils.isEmpty(game.getGameLabel()) && game.getGameLabel().equalsIgnoreCase("speedPointLudo")) {
                                ludoType = game.getGameLabel();
                            }
                            object.put("ludoType", ludoType);
                            /*if (mSocket!=null && finalContest.getWaitCount()>0){
                                try {
                                    JSONObject data1=new JSONObject();
                                    JSONObject obj1=new JSONObject();

                                    obj1.put("wait_user_id",finalContest.getWaitingModals().get(0).getUser_id());
                                    obj1.put("con_id",finalContest.getId());
                                    obj1.put("comp_id",ConstantUtil.COMPANY_ID);
                                    obj1.put("utoken",utoken);
                                    if (finalContest.getWaitingModals()!=null && finalContest.getWaitingModals().size()>0)
                                        obj1.put("team_name",finalContest.getWaitingModals().get(0).getUser_team_name());

                                    data1.put("data",obj1);
                                    data1.put("en","display_oppo_user_name");

                                    LogUtil.e("resp",ConstantUtil.REQ+":  display_oppo_user_name :  "+data1);
                                    mSocket.emit(ConstantUtil.REQ,data1.toString());

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }*/


                            /*object.put("socketURL",socket_url.replaceAll("\\\\",""));//"http://192.168.100.185:3000"
                            object.put("utoken",utoken);
                            object.put("asset_path",assets_path);
                            object.put("game_type_name",gameType);*/

                            //ConstantUtil.GAME_ASSETS_NAME);

                            Log.e("userData",object.toString());

                            isRejoinGame=true;

                            /*Intent intent=new Intent(this, UnityPlayerActivity.class)
                                    .putExtra("userData",object.toString());
                            someActivityResultLauncher.launch(intent);*/

                            offSocket();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            if (!isLogout)
                dialog.show();
        }
    }

    private String DecryptOPENSSL(String data, String AES_API_KEY) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(AES_API_KEY.getBytes(), "AES");
            byte[] finalIvs = new byte[16];
            IvParameterSpec ivps = new IvParameterSpec(finalIvs);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivps);
            return new String(cipher.doFinal(Base64.decode(data, Base64.DEFAULT)));
        } catch (Exception e) {
            // Decrypt(data, MyApp.mapp_key);
            LogUtil.e(TAG, "ERROR1 : " + e.toString());
            //e.printStackTrace();
            return data;
        }

    }

    public void getUserDetail(GameContestModel contest){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("token_no", preferences.getUserModel().getTokenNo());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(RummyContestActivity.this, true, ApiManager.AUTHV3GetUserDetails, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e(TAG, "USER onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    if (responseBody.optString("code").equals("200")) {
                        JSONObject data = responseBody.optJSONObject("data");
                        UserModel userModel = gson.fromJson(data.toString(), UserModel.class);
                        float total;
                        if (ConstantUtil.is_bonus_show){
                            total =  CustomUtil.convertFloat(userModel.getDepositBal()) + CustomUtil.convertFloat(userModel.getWinBal())
                                    + CustomUtil.convertFloat(userModel.getFf_coin()) + CustomUtil.convertFloat(userModel.getBonusBal());

                        }else {
                            total =  CustomUtil.convertFloat(userModel.getDepositBal()) + CustomUtil.convertFloat(userModel.getWinBal())
                                    + CustomUtil.convertFloat(userModel.getFf_coin());

                        }
                        /*float total = CustomUtil.convertFloat(userModel.getDepositBal()) + CustomUtil.convertFloat(userModel.getWinBal())
                                + CustomUtil.convertFloat(userModel.getFf_coin()) + CustomUtil.convertFloat(userModel.getBonusBal());*/
                        userModel.setTotal_balance(total);
                        preferences.setUserModel(userModel);
                    }
                    //showConfirmDialog(contest,view);
                    if (game.getAssetType().equalsIgnoreCase("ludo") || game.getAssetType().equalsIgnoreCase("webview")){

                        showConfirmDialog(contest,"");
                    }else {
                        getUserToken(contest);
                    }

                }else {
                    if (code==1000){
                        isRejoinGame=false;
                        isLogout=true;
                        if (dialog!=null && dialog.isShowing()){
                            dialog.dismiss();
                        }
                    }
                }
                //view.setEnabled(true);
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

                //preferences.setUserModel(new UserModel());

            }
        });
    }

    public void getUserToken(GameContestModel contest){
        JSONObject jsonObject = new JSONObject();
        try {
            UserModel userModel=preferences.getUserModel();
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("user_id", userModel.getId());
            jsonObject.put("entry_fee", contest.getEntryFee());
            jsonObject.put("no_of_spot", contest.getNo_spot());
            jsonObject.put("con_id", contest.getId());
            jsonObject.put("dist_amt", contest.getDisAmt());
            jsonObject.put("header",getHeaderArray());
            jsonObject.put("commission", contest.getCommission());
            jsonObject.put("is_public", "Yes");
            jsonObject.put("game_code", game.getGameCode());
            jsonObject.put("phone_u_id", MyApp.deviceId);
            jsonObject.put("fcm_id", MyApp.tokenNo);
            jsonObject.put("mobile_os", MyApp.deviceType);
            jsonObject.put("mobile_model_name", MyApp.deviceName);
            //jsonObject.put("contest_timer", "");
            //jsonObject.put("contest_timer", game_time);
            if (contest.getGameMode().equalsIgnoreCase("Raise")) {
                JSONObject raise_detail = new JSONObject();
                raise_detail.put("max", "0.1");
                raise_detail.put("raise", "0.01");
                raise_detail.put("min", "0.05");
                jsonObject.put("raise_detail", raise_detail);
            }
            /*if (contest.getGameMode().equalsIgnoreCase("Raise")){

            }*/
            jsonObject.put("game_mode_sub_type", contest.getGameSubMode());
            jsonObject.put("game_mode", contest.getGameMode());
            //jsonObject.put("user_avatar", userModel.getLevel3Id());
            //jsonObject.put("user_image_url", getAgeWiseUrl());
            //jsonObject.put("user_avatar", getAgeWiseUrl());
            jsonObject.put("user_avatar", userModel.getLevel3Id());
            jsonObject.put("game_type", game.getGameCode());
            jsonObject.put("game_type_name", game.getType());
            jsonObject.put("display_name", userModel.getUserTeamName());
            jsonObject.put("team_name", userModel.getUserTeamName());
            jsonObject.put("is_auto_game_start",is_auto_game_start);
            jsonObject.put("is_contest_waiting",is_contest_waiting);

            /*jsonObject.put("user_id", userModel.getId());
            jsonObject.put("game_mode_sub_type", contest.getGameSubMode());
            jsonObject.put("is_public", "Yes");
            jsonObject.put("mobile_os", "Android");
            JSONObject raise_detail=new JSONObject();
            raise_detail.put("max", "0.1");
            raise_detail.put("raise", "0.01");
            raise_detail.put("min", "0.05");
            jsonObject.put("raise_detail",raise_detail);
            jsonObject.put("team_name", userModel.getDisplayName());
            jsonObject.put("mobile_model_name", MyApp.deviceHardware);
            jsonObject.put("game_type", game.getGameCode());
            jsonObject.put("game_type_name", game.getType());
            jsonObject.put("user_id", userModel.getId());
            jsonObject.put("entry_fee", contest.getEntryFee());
            jsonObject.put("game_mode", contest.getGameMode());
            jsonObject.put("phone_u_id", MyApp.deviceId);
            jsonObject.put("con_id", contest.getId());
            jsonObject.put("display_name", userModel.getUserTeamName());
            jsonObject.put("user_avatar", preferences.getUserModel().getLevel3Id());
            jsonObject.put("no_of_spot", contest.getNo_spot());
            jsonObject.put("dist_amt", contest.getDisAmt());
            jsonObject.put("game_code", "0000");
            jsonObject.put("fcm_id", MyApp.tokenNo);*/


        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url="";
        if (ConstantUtil.is_game_test){
            url=game.getToken_create_url_test();
        }else {
            url=game.getToken_create_url();
        }
        LogUtil.e(TAG, "getUserToken param: " + jsonObject.toString()+"   "+url);
        if (!TextUtils.isEmpty(url)){
            HttpRestClient.postJSONNormal(RummyContestActivity.this, true, url, jsonObject, new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {

                    LogUtil.e(TAG, "getUserToken onSuccessResult: " + responseBody.toString());
                    if (responseBody.optInt("code")==1005){
                        otherGamePlayingDialog(responseBody.optJSONObject("data"));
                    }
                    else {
                        if (responseBody.optBoolean("status")) {

                            String utoken = responseBody.optJSONObject("data").optString("utoken");

                            showConfirmDialog(contest, utoken);
                        }else {
                            CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                            if (code==1000){
                                isRejoinGame=false;
                                isLogout=true;
                                if (dialog!=null && dialog.isShowing()){
                                    dialog.dismiss();
                                }
                            }
                        }
                    }

                }

                @Override
                public void onFailureResult(String responseBody, int code) {
                    //preferences.setUserModel(new UserModel());
                }
            });
        }

    }

    private void otherGamePlayingDialog(JSONObject data) {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.rejoin_dialog);
        dialog.setCancelable(false);
        TextView imgYes = dialog.findViewById(R.id.btnYes);
        ImageView imgNo = dialog.findViewById(R.id.imgClose) ;
        //TextView winnerNameTV = dialog.findViewById(R.id.winnerNameTV);
        TextView txtCondition = dialog.findViewById(R.id.txtCondition);
        //winnerNameTV.setText("Game in-Progress");
        if (allGamesList!=null && allGamesList.size()>0){
            String gameName="";
            for (Games game :
                    allGamesList) {
                if (game.getGameCode().equalsIgnoreCase(data.optString("game_type"))){
                    gameName=game.getGameName();
                }
            }

            String msg="Your last "+gameName+" game is in-progress. Press OK and Go into "+gameName+" Game to re-join.";
            txtCondition.setText(msg);

        }else {
            return;
        }

        imgNo.setOnClickListener(v ->  {
            if (MyApp.getClickStatus()){
                dialog.dismiss();
            }
        });

        imgYes.setOnClickListener(v -> {
            if (MyApp.getClickStatus()){
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
    }

    private String getAgeWiseUrl(){
        int age=18;
        UserModel userModel=preferences.getUserModel();
        if (userModel!=null){
            if (!TextUtils.isEmpty(userModel.getDob()) && !userModel.getDob().equals("0000-00-00")){
                age=CustomUtil.getAge(userModel.getDob());
            }
            // LogUtil.d("agereap",age+" ");
            if (TextUtils.isEmpty(userModel.getUserImg())) {
                if (userModel.getGender().equals("Male")){
                    if (age>=18 && age <= 25){
                        return ApiManager.PROFILE_IMG+"18to25Male.png";
                    }else if (age>25 && age<=40){
                        return ApiManager.PROFILE_IMG+"40plusMale.png";
                    }else {
                        return ApiManager.PROFILE_IMG+"25to40Male.png";
                    }
                }else {
                    if (age>=18 && age <= 25){
                        return ApiManager.PROFILE_IMG+"18to25Female.png";
                    }else if (age>25 && age<=40){
                        return ApiManager.PROFILE_IMG+"25to40Female.png";
                    }else {
                        return ApiManager.PROFILE_IMG+"40plusFemale.png";
                    }
                }
            }
            else {
                return ApiManager.PROFILE_IMG+userModel.getUserImg();
                /*LogUtil.e(TAG, "onCreate: " + ApiManager.PROFILE_IMG + userModel.getUserImg());
                if (userModel.getGender().equals("Male")){
                    if (age>=18 && age <= 25){
                        CustomUtil.loadImageWithGlide(mContext,profile_img,ApiManager.PROFILE_IMG,userModel.getUserImg(),R.drawable.male_18_below);

                    }else if (age>25 && age<=40){
                        CustomUtil.loadImageWithGlide(mContext,profile_img,ApiManager.PROFILE_IMG,userModel.getUserImg(),R.drawable.male_25_above);

                    }else {
                        CustomUtil.loadImageWithGlide(mContext,profile_img,ApiManager.PROFILE_IMG,userModel.getUserImg(),R.drawable.male_40_above);

                    }
                }else {
                    if (age>=18 && age <= 25){
                        CustomUtil.loadImageWithGlide(mContext,profile_img,ApiManager.PROFILE_IMG,userModel.getUserImg(),R.drawable.female_18_below);

                    }else if (age>25 && age<=40){
                        CustomUtil.loadImageWithGlide(mContext,profile_img,ApiManager.PROFILE_IMG,userModel.getUserImg(),R.drawable.female_25_above);

                    }else {
                        CustomUtil.loadImageWithGlide(mContext,profile_img,ApiManager.PROFILE_IMG,userModel.getUserImg(),R.drawable.female_40_above);
                    }
                }*/
            }
        }
        return "";

    }

    private void showConfirmDialog(GameContestModel contest,String utoken){

        /*use_deposit = use_transfer = use_winning = use_donation_deposit = 0;//useBonus = 0;

        float Contest_fee = CustomUtil.convertFloat(contest.getEntryFee());
        float orgContestEntry = CustomUtil.convertFloat(contest.getEntryFee());
        final float deposit = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        float bonus = 0;

        final float winning = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
        //final float transfer_bal = CustomUtil.convertFloat(preferences.getUserModel().getTransferBal());
        float usableBonus = 0;

        useBonus = ((Contest_fee * usableBonus) / 100);

        if (useBonus > bonus) {
            useBonus = bonus;
        }

        if (Contest_fee - useBonus >= 0) {// (Contest_fee - useBonus >= 0)
            Contest_fee = Contest_fee - useBonus;
        }

        if ((Contest_fee - deposit) < 0) {
            use_deposit = Contest_fee;
        }
        else {
            use_deposit = deposit;
            //use_transfer = transfer_bal;
            use_winning = Contest_fee - deposit;
        }*/

        if (!isValidForJoin(contest)) {//((deposit +  winning) - Contest_fee) < 0
            Toast.makeText(this,"Not Enough Balance, Please deposit and try again.",Toast.LENGTH_LONG).show();
            //float amt=Contest_fee-(deposit + winning);
            double amt=Math.ceil(amtToAdd);

            if (amt<1){
                amt=1;
            }

            String patableAmt=CustomUtil.getFormater("0.00").format(amt);
            MyApp.getMyPreferences().setPref(PrefConstant.PAYMENT_SUCCESS,false);
            Intent intent = new Intent(mContext,AddDepositActivity.class);
            intent.putExtra("isJoin",false);
            intent.putExtra("depositAmt",patableAmt);
            startActivity(intent);
        }
        else {

            TextView join_contest_fee, join_use_deposit,
                    join_use_winning, join_user_pay,txtUseBonus;

            View confirmView = LayoutInflater.from(mContext).inflate(R.layout.games_contest_confirm_dialog, null);

            txtYes = confirmView.findViewById(R.id.join_contest_btn);
            join_contest_fee = confirmView.findViewById(R.id.join_contest_fee);
            join_use_deposit = confirmView.findViewById(R.id.join_use_deposit);
            join_use_winning = confirmView.findViewById(R.id.join_use_winning);
            join_user_pay = confirmView.findViewById(R.id.join_user_pay);

            txtUseBonus = confirmView.findViewById(R.id.txtUseBonus);
            txtProgress = confirmView.findViewById(R.id.txtProgress);
            progress = confirmView.findViewById(R.id.progress);
            layAsset = confirmView.findViewById(R.id.layProgress);
            EditText edtSocketUrl = confirmView.findViewById(R.id.edtSocketUrl);
            LinearLayout layBonus = confirmView.findViewById(R.id.layBonus);
            if (ConstantUtil.is_bonus_show){
                layBonus.setVisibility(View.VISIBLE);
            }else {
                layBonus.setVisibility(View.GONE);
            }

            if (ConstantUtil.is_game_test){
                edtSocketUrl.setVisibility(View.VISIBLE);
                edtSocketUrl.setText(socket_url.replaceAll("\\\\",""));
            }else {
                edtSocketUrl.setVisibility(View.GONE);
            }

            join_use_deposit.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit+useCoin)));
            join_use_winning.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(use_winning));
            txtUseBonus.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(useBonus));
            join_contest_fee.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(CustomUtil.convertFloat(contest.getEntryFee())));// Contest_fee+ useBonus
            join_user_pay.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit+use_winning+useBonus+useCoin)));

            BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(mContext);

            mBottomSheetDialog.setContentView(confirmView);

            if (!game.getAssetType().equalsIgnoreCase("webview")) {
                try {
                    //game = (GameMainModal.Datum.Game) getIntent().getSerializableExtra("gameData");

                    String path = CustomUtil.getAppDirectory(this) + asset_type;
                    String downloadUrl = path + getFileNameFromUrl(android_asset_url);//ConstantUtil.GAME_ASSETS_NAME;

                    File filePath = new File(path);
                    File file = new File(downloadUrl);

                    boolean isCompletes = preferences.getPrefBoolean(game.getAssetType() + PrefConstant.DOWNLOAD_STATUS);
                    if (!isCompletes) {
                        if (file.exists()) {
                            file.delete();
                        }
                    }

                    Log.e("appDir", "Path: " + path + "\n" + downloadUrl);

                    if (!filePath.exists()) {
                        filePath.mkdirs();
                    }

                    if (!file.exists()) {

                        layAsset.setVisibility(View.VISIBLE);
                        txtYes.setVisibility(View.GONE);
                        mBottomSheetDialog.setCancelable(false);

                        preferences.setPref(game.getAssetType() + PrefConstant.DOWNLOAD_STATUS, false);

                       /* String[] p;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            p = storge_permissions_33;
                        } else {
                            p = storge_permissions;
                        }*/
                        Dexter.withContext(this)
                                .withPermissions(p).withListener(new MultiplePermissionsListener() {
                                    @Override
                                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                                        if (report.areAllPermissionsGranted()) {

                                            if (filePath.isDirectory()) {
                                                String[] children = filePath.list();
                                                if (children != null) {
                                                    for (int i = 0; i < children.length; i++) {
                                                        if (!children[i].contains(getFileNameFromUrl(android_asset_url))) {
                                                            new File(filePath, children[i]).delete();
                                                        }
                                                    }
                                                }
                                            }
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                                // if (
                                                // !Environment.isExternalStorageManager()){
                                                if (!Environment.isExternalStorageManager() && !isSettingOpen) {

                                                    isSettingOpen = true;
                                                    Intent intent = new Intent();
                                                    intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                                    intent.setData(Uri.parse("package:com.fantafeat"));
                                                    startActivityForResult(intent, 2242);
                                                } else {
                                                    new DownloadFileFromURL(downloadUrl, txtProgress, progress, mBottomSheetDialog,
                                                            txtYes, layAsset)
                                                            .execute(android_asset_url);
                                                    //showAssetsDownload(downloadUrl,asset_url_android);
                                                }
                                            } else {
                                                new DownloadFileFromURL(downloadUrl, txtProgress, progress, mBottomSheetDialog, txtYes, layAsset)
                                                        .execute(android_asset_url);
                                                // showAssetsDownload(downloadUrl,asset_url_android);
                                            }
                                        }

                                        if (report.isAnyPermissionPermanentlyDenied() && !isPerDialog) {
                                            showSettingsDialog();
                                        }
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.
                                            PermissionRequest> list, PermissionToken token) {
                                        token.continuePermissionRequest();
                                    }


                                }).check();

                    } else {
                        mBottomSheetDialog.setCancelable(true);
                        layAsset.setVisibility(View.GONE);
                        txtYes.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                mBottomSheetDialog.setCancelable(true);
                layAsset.setVisibility(View.GONE);
                txtYes.setVisibility(View.VISIBLE);
            }

            txtYes.setOnClickListener(v -> {
                mBottomSheetDialog.dismiss();
                try {
                    UserModel user = preferences.getUserModel();

                    if (game.getAssetType().equalsIgnoreCase("webview")) {
                        JSONObject object = new JSONObject();
                        object.put("con_id", contest.getId());
                        object.put("socketURL", socket_url.replaceAll("\\\\", ""));//MyApp.getInstance().Ludo_Socket.replaceAll("\\\\","")
                        object.put("asset_path", CustomUtil.getAppDirectory(this) + asset_type + getFileNameFromUrl(android_asset_url));
                        object.put("game_type_name", game.getType());
                        object.put("rules", tnc);
                        object.put("utoken", utoken);
                        object.put("game_name", game.getGameName());
                        object.put("winning_amount", contest.getDisAmt());
                        object.put("user_avatar", user.getLevel3Id());
                        object.put("no_of_spot", contest.getNo_spot());
                        object.put("user_id", user.getId());
                        object.put("user_name", user.getUserTeamName());
                        object.put("user_team_name", user.getDisplayName());
                        object.put("play_with_friend", "No");
                        object.put("is_creator", "No");
                        object.put("game_play_code", "");
                        object.put("entry_fee", contest.getEntryFee());
                        object.put("contest_timer", contest.getGame_time());
                        object.put("comp_id",ConstantUtil.COMPANY_ID);

                        object.put("total_round", contest.getGame_round());

                        Log.e("userData", object.toString() + "  userdata");

                        if (mSocket != null)
                            mSocket.emit(ConstantUtil.WAITING_LUDO_USER, object);

                        Intent intent = new Intent(mContext, WebViewGameActivity.class).putExtra("gameData", game)
                                .putExtra("con_id", contest.getId());

                        someActivityResultLauncher.launch(intent);

                        offSocket();

                    }
                    else {

                        JSONObject object = new JSONObject();

                        object.put("con_id", contest.getId());
                        object.put("entry_fee", contest.getEntryFee());
                        object.put("game_mode", contest.getGameMode());
                        object.put("game_mode_sub_type", contest.getGameSubMode());
                        object.put("game_play_code", "");
                        object.put("game_type_name", game.getType());
                        object.put("other_game_data", game.getGame_mode());
                        object.put("play_with_friend", "no");
                        //object.put("user_avatar", getAgeWiseUrl());
                        object.put("user_avatar", user.getLevel3Id());
                        object.put("user_id",user.getId());
                        //object.put("header",getHeaderArray());
                        object.put("token_generate_url",token_generate_url);
                        object.put("user_team_name", user.getUserTeamName());
                        object.put("winning_amount", contest.getDisAmt());
                        object.put("comp_id",ConstantUtil.COMPANY_ID);

                        object.put("asset_path", CustomUtil.getAppDirectory(this) + asset_type + getFileNameFromUrl(android_asset_url));
                        //object.put("socketURL", socket_url.replaceAll("\\\\", ""));

                        if (ConstantUtil.is_game_test){
                            if (!TextUtils.isEmpty(edtSocketUrl.getText().toString())){
                                object.put("socketURL",edtSocketUrl.getText().toString());
                            }
                            else {
                                object.put("socketURL",socket_url.replaceAll("\\\\",""));
                            }
                        }
                        else {
                            object.put("socketURL",socket_url.replaceAll("\\\\",""));
                        }

                        object.put("utoken", utoken);

                        String ludoType = "";
                        if (!TextUtils.isEmpty(game.getGameLabel()) && game.getGameLabel().equalsIgnoreCase("speedPointLudo")) {
                            ludoType = game.getGameLabel();
                        }
                        object.put("ludoType", ludoType);

                        Log.e("userData", object.toString() + "  userdata");


                        if (mSocket!=null && contest.getWaitCount()>0){
                            try {
                                JSONObject data1=new JSONObject();
                                JSONObject obj=new JSONObject();


                                obj.put("con_id",contest.getId());
                                obj.put("comp_id",ConstantUtil.COMPANY_ID);
                                obj.put("utoken",utoken);
                                if (contest.getWaitingModals()!=null && contest.getWaitingModals().size()>0) {
                                    obj.put("team_name", contest.getWaitingModals().get(0).getUser_team_name());
                                    obj.put("wait_user_id",contest.getWaitingModals().get(0).getUser_id());
                                }

                                data1.put("data",obj);
                                data1.put("en","display_oppo_user_name");

                                LogUtil.e("resp",ConstantUtil.REQ+":  display_oppo_user_name :  "+data1);
                                mSocket.emit(ConstantUtil.REQ,data1.toString());

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        offSocket();

                        /*startActivity(new Intent(this, UnityPlayerActivity.class)
                                .putExtra("userData", object.toString()));*/
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            if (mBottomSheetDialog != null && !mBottomSheetDialog.isShowing())
                mBottomSheetDialog.show();
        }
    }

    private JSONObject getHeaderArray(){

        TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
        Calendar c = Calendar.getInstance(tz);
        long current=c.getTimeInMillis();
       // MCrypt crypt=new MCrypt();

        HashMap<String, String> headersMap = new HashMap<>();
        headersMap.put("mobile_type", MyApp.deviceType);
        headersMap.put("mobile_os",MyApp.deviceVersion);
        headersMap.put("phone_uid",MyApp.deviceId);
        headersMap.put("mobile_hardware",MyApp.deviceHardware);
        headersMap.put("mobile_name",MyApp.deviceName);
        headersMap.put("app_ver", String.valueOf(MyApp.current_version));
        headersMap.put("user_id", MyApp.USER_ID);
        headersMap.put("uenc_id", JSONParser.getEncryptedUserId(current).replace("\n", "").replace("\r", ""));
        headersMap.put("request_time", current+"");
        headersMap.put("token_no", MyApp.APP_KEY);
        headersMap.put("fcm_id",MyApp.tokenNo);
        headersMap.put("user_header_key",MyApp.user_header_key);
        headersMap.put("lat",MyApp.lat);
        headersMap.put("lng",MyApp.lng);
        headersMap.put("comp_id", ConstantUtil.COMPANY_ID);
        headersMap.put("bundle_id", com.fantafeat.BuildConfig.APPLICATION_ID);

        return new JSONObject(headersMap);
    }

    private boolean isValidForJoin(GameContestModel contestData){
        DecimalFormat format = CustomUtil.getFormater("00.00");

        use_deposit  = use_winning = use_donation_deposit = useBonus = useCoin = 0;

        float finalEntryFee = CustomUtil.convertFloat(contestData.getEntryFee());

        final float deposit = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        float bonus = CustomUtil.convertFloat(preferences.getUserModel().getBonusBal());
        final float winning = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
        final float bb_coin = CustomUtil.convertFloat(preferences.getUserModel().getFf_coin());

        float usableBonus1 , usableBBCoins = 0;

       /* if (!TextUtils.isEmpty(contestData.getOffer_date_text())){
            if (contestData.getNewOfferRemovedList().size()>0){
                NewOfferModal modal=contestData.getNewOfferRemovedList().get(0);
                if (modal.getDiscount_entry_fee().equalsIgnoreCase("")){
                    finalEntryFee =CustomUtil.convertFloat(contestData.getEntryFee());
                }else {
                    finalEntryFee =CustomUtil.convertFloat(modal.getDiscount_entry_fee());
                }
                usableBonus=CustomUtil.convertFloat(modal.getUsed_bonus());
            }
            else {
                usableBonus=CustomUtil.convertFloat(contestData.getDefaultBonus());
                finalEntryFee =CustomUtil.convertFloat(contestData.getEntryFee());
            }
        }else {
            usableBonus=CustomUtil.convertFloat(contestData.getDefaultBonus());
            finalEntryFee =CustomUtil.convertFloat(contestData.getEntryFee());
        }*/

        usableBBCoins=CustomUtil.convertFloat(contestData.getGame_use_coin());
        usableBonus1=CustomUtil.convertFloat(contestData.getBonus());

        useBonus = ((finalEntryFee * usableBonus1) / 100);
        useCoin = ((finalEntryFee * usableBBCoins) / 100);

        if (useBonus > bonus) {
            useBonus = bonus;
        }

        if (useCoin > bb_coin) {
            useCoin = bb_coin;
        }

        if (finalEntryFee - useBonus >= 0) {
            finalEntryFee = finalEntryFee - useBonus;
        }

        if (useCoin>=useBonus)
            useCoin=useCoin-useBonus;

        if (finalEntryFee - useCoin >= 0) {
            finalEntryFee = finalEntryFee - useCoin;
        }

        if ((finalEntryFee - deposit) < 0) {
            use_deposit = finalEntryFee;
        }
        else {
            use_deposit = deposit;
            use_winning = finalEntryFee - deposit;
        }

        float calBalance1=deposit + winning+bb_coin;

        amtToAdd = finalEntryFee - calBalance1;

        if (calBalance1 < finalEntryFee) {
            return false;
        }
        return true;
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode()==Activity.RESULT_OK){
                    if (result.getData()!=null && result.getData().hasExtra("isRejoinGame")){
                        isRejoinGame = !result.getData().getStringExtra("isRejoinGame").equalsIgnoreCase("no");
                    }else {
                        isRejoinGame=true;
                    }
                }else {
                    isRejoinGame=true;
                }
            });

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        TextView txtProgress;
        ProgressBar progress;
        BottomSheetDialog dialog;
        Dialog dialog1;
        private String downloadUrl="";
        TextView txtYes;
        LinearLayout layBtn;
        LinearLayout layProgress;
        RelativeLayout layAsset;

        private boolean isAlert=false;

        public DownloadFileFromURL(String downloadUrl,TextView txtProgress,ProgressBar progress,
                                   BottomSheetDialog dialog,TextView txtYes,RelativeLayout layAsset) {
            this.downloadUrl = downloadUrl;
            this.txtProgress = txtProgress;
            this.progress = progress;
            this.dialog = dialog;
            this.txtYes = txtYes;
            this.layAsset = layAsset;
            isAlert=false;

        }

        public DownloadFileFromURL(String downloadUrl, TextView txtProgress, ProgressBar progress,
                                   Dialog dialog, LinearLayout layBtn, LinearLayout layProgress) {
            this.downloadUrl = downloadUrl;
            this.txtProgress = txtProgress;
            this.progress = progress;
            this.dialog1 = dialog;
            this.layBtn = layBtn;
            this.layProgress = layProgress;
            isAlert=true;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {

                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream


                OutputStream output = new FileOutputStream(downloadUrl);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                preferences.setPref(game.getAssetType()+PrefConstant.DOWNLOAD_STATUS,false);
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }


        protected void onProgressUpdate(String... progressCnt) {

            if (progress!=null){
                progress.setProgress(Integer.parseInt(progressCnt[0]));
            }
            if (txtProgress!=null){
                txtProgress.setText(Integer.parseInt(progressCnt[0])+" %");
            }

            //pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {

            preferences.setPref(game.getAssetType()+PrefConstant.DOWNLOAD_STATUS,true);

            if (dialog!=null){
                dialog.setCancelable(true);
            }

            if (!isAlert){
                if (layAsset!=null){
                    layAsset.setVisibility(View.GONE);
                }

                if (txtYes!=null){
                    txtYes.setVisibility(View.VISIBLE);
                }
            }
            else {
                if (layProgress!=null){
                    layProgress.setVisibility(View.GONE);
                }

                if (layBtn!=null){
                    layBtn.setVisibility(View.VISIBLE);
                }
            }
            /* dialog.dismiss();*/

        }

    }

    private void checkAndDownloadAssets(){
        String path = CustomUtil.getAppDirectory(this) + asset_type;
        assets_path=path+getFileNameFromUrl(android_asset_url);//ConstantUtil.GAME_ASSETS_NAME;
        LogUtil.e("resp","assets_path: "+assets_path+"\nandroid_asset_url: "+android_asset_url);

        File filePath=new File(path);
        File file=new File(assets_path);

        boolean isCompletes=preferences.getPrefBoolean(game.getAssetType()+ PrefConstant.DOWNLOAD_STATUS);
        if (!isCompletes){
            if (file.exists()){
                file.delete();
            }
        }

        LogUtil.e("appDir", "Path: "+path+"\n"+assets_path);

        if (!filePath.exists()){
            filePath.mkdirs();
        }

        if (!file.exists()){
            layProgress.setVisibility(View.VISIBLE);
            layBtn.setVisibility(View.GONE);
            dialog.setCancelable(false);
            preferences.setPref(game.getAssetType()+PrefConstant.DOWNLOAD_STATUS,false);
            /*String[] p;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                p = storge_permissions_33;
            } else {
                p = storge_permissions;
            }*/
            Dexter.withContext(this)
                    .withPermissions(p).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {

                                if (filePath.isDirectory()) {
                                    String[] children = filePath.list();
                                    if (children != null) {
                                        for (int i = 0; i < children.length; i++) {
                                            if (!children[i].contains(getFileNameFromUrl(android_asset_url))){
                                                new File(filePath, children[i]).delete();
                                            }
                                        }
                                    }
                                }
                                new DownloadFileFromURL(assets_path,txtProgress,progress,dialog,
                                        layBtn,layProgress)
                                        .execute(android_asset_url);
                            }

                            if (report.isAnyPermissionPermanentlyDenied() && !isPerDialog){
                                showSettingsDialog();
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }else {
            dialog.setCancelable(true);
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder alert=new AlertDialog.Builder(mContext);
        alert.setTitle("Permission Denied");
        alert.setMessage("Without internal storage access permission the app is unable play this game.");
        alert.setPositiveButton("Access", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isPerDialog=false;
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 2242);
            }
        });
        alert.setCancelable(false);
        alert.show();
        isPerDialog=true;
    }

    private class DownloadAsync extends AsyncTask<String, Long, Boolean>{

        private File file;
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
                                LogUtil.e(TAG, "download: " );
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
                   /* intent.setDataAndType(FileProvider.getUriForFile(mContext,
                                    BuildConfig.APPLICATION_ID + ".provider", file),
                            "application/vnd.android.package-archive");*/
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

    private boolean isAssetAvailable(){
        String path = CustomUtil.getAppDirectory(this) + asset_type;
        assets_path=path+getFileNameFromUrl(android_asset_url);//ConstantUtil.GAME_ASSETS_NAME;

        File filePath=new File(path);
        File file=new File(assets_path);

        boolean isCompletes=preferences.getPrefBoolean(asset_type+PrefConstant.DOWNLOAD_STATUS);
        if (!isCompletes){
            if (file.exists()){
                file.delete();
            }
        }

        if (!filePath.exists()){
            filePath.mkdirs();
        }

        return file.exists();
    }

    private String getFileNameFromUrl(String url){
        return url.substring(url.lastIndexOf('/') + 1);
    }

    private boolean isWalletValid(String amt){

        if (TextUtils.isEmpty(amt)){
            Toast.makeText(this,"Amount must not empty, Please try again.",Toast.LENGTH_LONG).show();
            return false;
        }

        UserModel user=preferences.getUserModel();

        float deposit = Float.parseFloat(user.getDepositBal());
        float winning = Float.parseFloat(user.getWinBal());
        float transferBal = Float.parseFloat(user.getTransferBal());
        float contestFee = Float.parseFloat(amt);

        float calBalance = deposit + transferBal + winning;

        if (calBalance < contestFee) {
            Toast.makeText(this,"Not Enough Balance, Please deposit and try again.",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onItemClick(View view, GameContestModel contest) {

        getUserDetail(contest);
        /*if (MyApp.getClickStatus()){
            CustomUtil.checkInternet(mContext,()->new Thread(() ->{
                runOnUiThread(()->{

                    if (!isWalletValid(contest.getEntryFee())){

                        return;
                    }

                    //game_time=contest.getGame_time();

                    getUserDetail(contest);
                    //showConfirmDialog(contest,view);
                });
            }).start());
        }*/
    }

}