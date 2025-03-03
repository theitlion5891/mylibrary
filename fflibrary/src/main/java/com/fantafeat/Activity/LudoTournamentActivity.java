package com.fantafeat.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fantafeat.Adapter.LudoTournamentAdapter;
import com.fantafeat.Adapter.LudoTournamentUserListAdapter;
import com.fantafeat.Adapter.MatchRankAdapter;
import com.fantafeat.Adapter.TournamentMatchRankAdapter;
import com.fantafeat.Model.GameContestModel;
import com.fantafeat.Model.GameMainModal;
import com.fantafeat.Model.Games;
import com.fantafeat.Model.LudoTournamentModel;
import com.fantafeat.Model.LudoTournamentUserModel;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.BottomSheetTeam;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.JSONParser;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.MCrypt;
import com.fantafeat.util.PrefConstant;
import com.fantafeat.util.WinningTreeSheet;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class LudoTournamentActivity extends BaseActivity {

    private ImageView toolbar_back,toolbar_info;
    private TextView toolbar_title;
    private SwipeRefreshLayout swipe;
    private RecyclerView recyclerList;
    private ArrayList<LudoTournamentModel> list;
    private ArrayList<Games> allGamesList=new ArrayList<>();
    private LudoTournamentModel selectedModel;
    private LudoTournamentAdapter adapter;
    private LudoTournamentUserListAdapter userListAdapter;
    private String winning_dec="";
    private Games gameModel;
    private Dialog dialog;
    private LinearLayout layBtn,layProgress;
    private TextView txtProgress;
    private ProgressBar progress;
    private boolean isPerDialog=false,is_auto_game_start=false;
    private String socket_url="",android_asset_url="",gameType="",tnc="",game_id="",asset_type="",game_code="",ios_asset_url=""
            ,token_generate_url="",assets_path="";

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

    private TextView txtNoData;
    private RelativeLayout layAsset;
    private BottomSheetDialog mBottomSheetDialog;
    private TextView txtYes;

    float  use_deposit = 0;
    float  use_transfer =  0;
    float  use_winning =  0;
    float  use_donation_deposit = 0;
    float  useBonus = 0;
    float  useCoin = 0;
    float  amtToAdd = 0;

    private Boolean isSettingOpen = false,is_contest_waiting=false;
    private Socket mSocket = null;
    private boolean isRejoinGame=true,isLogout=false;
    private long lastUpdateTime=0;

    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ludo_tournament);

        toolbar_back=findViewById(R.id.toolbar_back);
        toolbar_info=findViewById(R.id.toolbar_info);
        toolbar_info.setVisibility(View.VISIBLE);
        toolbar_title=findViewById(R.id.toolbar_title);

        swipe=findViewById(R.id.swipe);
        recyclerList=findViewById(R.id.recyclerList);

        gameModel=(Games) getIntent().getSerializableExtra("gameData");

        if (getIntent().hasExtra("allGamesList")){
            allGamesList= (ArrayList<Games>) getIntent().getSerializableExtra("allGamesList");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }

        if (ConstantUtil.is_game_test){
            android_asset_url=gameModel.getAndroidAssetUrlTest();
            socket_url=gameModel.getSocket_url_test();
            token_generate_url=gameModel.getToken_create_url_test();
        }
        else {
            socket_url=gameModel.getSocketUrl();
            android_asset_url=gameModel.getAndroidAssetUrl();
            token_generate_url=gameModel.getToken_create_url();
        }

        gameType = gameModel.getType();
        tnc=gameModel.getGamesTnc();
        game_id=gameModel.getGameCode()+"";
        asset_type=gameModel.getAssetType()+"/";
        game_code=gameModel.getGameCode();
        ios_asset_url=gameModel.getIosAssetUrl();

        is_auto_game_start= gameModel.getIs_auto_game_start().equalsIgnoreCase("yes");
        is_contest_waiting= gameModel.getIs_contest_waiting().equalsIgnoreCase("yes");

        toolbar_title.setText(gameModel.getGameName());

        list=new ArrayList<>();

        recyclerList.setLayoutManager(new LinearLayoutManager(this));
        adapter=new LudoTournamentAdapter(mContext, list, new LudoTournamentAdapter.TournamentListener() {
            @Override
            public void onItemClick(LudoTournamentModel model) {
                getDetailData(model,true);
            }

            @Override
            public void onTreeShow(LudoTournamentModel contest) {
                if (!TextUtils.isEmpty(contest.getWinning_tree())){
                    WinningTreeSheet bottomSheetTeam = new WinningTreeSheet(mContext, contest.getDistAmt(),contest.getWinning_tree()
                            ,contest.getDistAmt());
                    bottomSheetTeam.show(((LudoTournamentActivity) mContext).getSupportFragmentManager(),
                            BottomSheetTeam.TAG);

                /*    WinningTreeSheet bottomSheetTeam = new WinningTreeSheet( mContext,
                            contest.getDistAmt(),contest.getWinning_tree(),true );
                    bottomSheetTeam.show(getSupportFragmentManager(), BottomSheetTeam.TAG);*/
                }
            }

            @Override
            public void onJoin(LudoTournamentModel contest) {
                CustomUtil.checkInternet(mContext,()->new Thread(() ->{
                    runOnUiThread(()->{
                        if (!isWalletValid(contest.getEntryFee())){
                            return;
                        }
                        getUserDetail(contest);
                    });
                }).start());
            }
        });
        recyclerList.setAdapter(adapter);

        initClicks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        getData();
    }

    private boolean isWalletValid(String amt1){

        if (TextUtils.isEmpty(amt1)){
            Toast.makeText(this,"Amount must not empty, Please try again.",Toast.LENGTH_LONG).show();
            return false;
        }

        UserModel user=preferences.getUserModel();

        float deposit = Float.parseFloat(user.getDepositBal());
        float winning = Float.parseFloat(user.getWinBal());
        float transferBal = Float.parseFloat(user.getTransferBal());
        //float bb_coin = CustomUtil.convertFloat(user.getBb_coin());
        float contestFee = Float.parseFloat(amt1);

        float calBalance = deposit + transferBal + winning/*+bb_coin*/;

        if (calBalance < contestFee) {
            //Toast.makeText(this,"Not Enough Balance, Please deposit and try again.",Toast.LENGTH_LONG).show();
            CustomUtil.showToast(mContext,"Insufficient Balance");
            double amt=Math.ceil(contestFee - calBalance);
            LogUtil.e("resp","Remain Balance: "+amt);
            if (amt<1){
                amt=amt+1;
            }
            int intamt=(int) amt;
            if (intamt<amt){
                intamt+=1;
            }
            String payableAmt=String.valueOf(intamt);
            startActivity(new Intent(mContext,AddDepositActivity.class)
                    .putExtra("depositAmt",payableAmt));
            return false;
        }
        return true;
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

    public void getUserDetail(LudoTournamentModel contest){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("token_no", preferences.getUserModel().getTokenNo());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(LudoTournamentActivity.this, true, ApiManager.USER_DETAIL, jsonObject, new GetApiResult() {
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
                            total = CustomUtil.convertFloat(userModel.getDepositBal()) + CustomUtil.convertFloat(userModel.getWinBal())
                                    + CustomUtil.convertFloat(userModel.getFf_coin());

                        }
                        /*float total = CustomUtil.convertFloat(userModel.getDepositBal()) + CustomUtil.convertFloat(userModel.getWinBal())
                                + CustomUtil.convertFloat(userModel.getTransferBal()) + CustomUtil.convertFloat(userModel.getBonusBal());*/
                        userModel.setTotal_balance(total);
                        preferences.setUserModel(userModel);
                    }

                    getUserToken(contest);

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

    public void getUserToken(LudoTournamentModel contest){
        JSONObject jsonObject = new JSONObject();
        try {
            UserModel userModel=preferences.getUserModel();
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("user_id", userModel.getId());
            jsonObject.put("entry_fee", contest.getEntryFee());
            jsonObject.put("no_of_spot", contest.getNoOfSpot());
            jsonObject.put("con_id", contest.getId());
            jsonObject.put("dist_amt", contest.getDistAmt());
            jsonObject.put("header",getHeaderArray());
            jsonObject.put("commission", "");
            jsonObject.put("is_public", "Yes");
            jsonObject.put("game_code", gameModel.getGameCode());
            jsonObject.put("game_mode_sub_type", "");
            jsonObject.put("phone_u_id", MyApp.deviceId);
            jsonObject.put("fcm_id", MyApp.tokenNo);
            jsonObject.put("mobile_os", MyApp.deviceType);
            jsonObject.put("mobile_model_name", MyApp.deviceName);
            jsonObject.put("game_mode", gameModel.getType());
            jsonObject.put("other_detials", new JSONArray(contest.getUser_token_position()));
            jsonObject.put("contest_timer", contest.getGame_time());
            //jsonObject.put("user_image_url", getAgeWiseUrl());
            //jsonObject.put("user_avatar",getAgeWiseUrl());//userModel.getLevel3Id()
            jsonObject.put("user_avatar", userModel.getLevel3Id());
            jsonObject.put("game_type", gameModel.getGameCode());
            jsonObject.put("display_name", userModel.getDisplayName());
            jsonObject.put("team_name", userModel.getUserTeamName());
            jsonObject.put("is_auto_game_start",is_auto_game_start);
            jsonObject.put("is_contest_waiting",is_contest_waiting);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        String url="";
        if (ConstantUtil.is_game_test){
            url=gameModel.getToken_create_url_test();
        }
        else {
            url=gameModel.getToken_create_url();
        }
        LogUtil.e(TAG, "getUserToken param: " + jsonObject.toString()+"   "+url);
        if (!TextUtils.isEmpty(url)){
            HttpRestClient.postJSONNormal(LudoTournamentActivity.this, true, url, jsonObject, new GetApiResult() {
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
                public void onFailureResult(String responseBody, int code) {}
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
        TextView winnerNameTV = dialog.findViewById(R.id.winnerNameTV);
        TextView txtCondition = dialog.findViewById(R.id.txtCondition);
        winnerNameTV.setText("Game in-Progress");
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

    private void showConfirmDialog(LudoTournamentModel contest,String utoken){

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
            //  txtUseGems = confirmView.findViewById(R.id.txtUseGems);
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
            // txtUseGems.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(useCoin));
            txtUseBonus.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(useBonus));
            join_contest_fee.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(CustomUtil.convertFloat(contest.getEntryFee())));// Contest_fee+ useBonus
            join_user_pay.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit+use_winning+useBonus+useCoin)));

            mBottomSheetDialog = new BottomSheetDialog(mContext);

            mBottomSheetDialog.setContentView(confirmView);

            try {
                //game = (GameMainModal.Datum.Game) getIntent().getSerializableExtra("gameData");

                String path = CustomUtil.getAppDirectory(this) + asset_type;
                assets_path=path+ getFileNameFromUrl(android_asset_url);//ConstantUtil.GAME_ASSETS_NAME;

                File filePath=new File(path);
                File file=new File(assets_path);

                //boolean isCompletes=preferences.getPrefBoolean(asset_type+ PrefConstant.DOWNLOAD_STATUS);
                boolean isCompletes=preferences.getPrefBoolean(gameModel.getAssetType()+ PrefConstant.DOWNLOAD_STATUS);
                if (!isCompletes){
                    if (file.exists()){
                        file.delete();
                    }
                }

                Log.e("appDir", "Path: "+path+"\n"+assets_path);

                if (!filePath.exists()){
                    filePath.mkdirs();
                }

                if (!file.exists()){

                    layAsset.setVisibility(View.VISIBLE);
                    txtYes.setVisibility(View.GONE);
                    mBottomSheetDialog.setCancelable(false);

                    preferences.setPref(gameModel.getAssetType()+PrefConstant.DOWNLOAD_STATUS,false);

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
                                                new DownloadFileFromURL(assets_path,txtProgress,progress,mBottomSheetDialog,
                                                        txtYes,layAsset)
                                                        .execute(android_asset_url);
                                                //showAssetsDownload(downloadUrl,asset_url_android);
                                            }
                                        }else {
                                            new DownloadFileFromURL(assets_path,txtProgress,progress,mBottomSheetDialog,txtYes,layAsset)
                                                    .execute(android_asset_url);
                                            // showAssetsDownload(downloadUrl,asset_url_android);
                                        }
                                    }

                                    if (report.isAnyPermissionPermanentlyDenied() && !isPerDialog){
                                        showSettingsDialog();
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.
                                        PermissionRequest> list, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }


                            }).check();

                }else {
                    mBottomSheetDialog.setCancelable(true);
                    layAsset.setVisibility(View.GONE);
                    txtYes.setVisibility(View.VISIBLE);
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }
          /*  if (!gameModel.getAssetType().equalsIgnoreCase("webview")){

            }
            else {
                mBottomSheetDialog.setCancelable(true);
                layAsset.setVisibility(View.GONE);
                txtYes.setVisibility(View.VISIBLE);
            }*/

            txtYes.setOnClickListener(v -> {
                mBottomSheetDialog.dismiss();
                try {
                    //UserModel user=preferences.getUserModel();

                    JSONObject object=new JSONObject();
                    object.put("token_generate_url",token_generate_url);
                    object.put("comp_id",ConstantUtil.COMPANY_ID);
                    object.put("asset_path",assets_path);
                    object.put("game_type_name",gameType);
                    object.put("entry_fee",contest.getEntryFee());
                    object.put("game_mode","");
                    object.put("game_mode_sub_type","");
                    object.put("game_play_code","");
                    object.put("winning_amount",contest.getDistAmt());
                    if (ConstantUtil.is_game_test){
                        if (!TextUtils.isEmpty(edtSocketUrl.getText().toString().trim())){
                            object.put("socketURL",edtSocketUrl.getText().toString().trim());//"http://192.168.100.185:3000"
                        }else
                            object.put("socketURL",socket_url.replaceAll("\\\\",""));//"http://192.168.100.185:3000"
                    }else
                        object.put("socketURL",socket_url.replaceAll("\\\\",""));//"http://192.168.100.185:3000"

                    object.put("utoken",utoken);
                    object.put("play_with_friend","No");

                    object.put("ludoType","tournamentLudo");
                    //object.put("header",getHeaderArray());
                    object.put("winning_amount",contest.getDistAmt());
                    // object.put("isautogamestart",is_auto_game_start);
                    LogUtil.e("userData","userData: "+object.toString());

                    launchUnity(object);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            if (mBottomSheetDialog != null && !mBottomSheetDialog.isShowing())
                mBottomSheetDialog.show();
        }
    }

    private boolean isValidForJoin(LudoTournamentModel contestData){
        DecimalFormat format = CustomUtil.getFormater("00.00");

        use_deposit  = use_winning = use_donation_deposit = useBonus = useCoin = 0;

        float finalEntryFee = CustomUtil.convertFloat(contestData.getEntryFee());

        final float deposit = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        float bonus = CustomUtil.convertFloat(preferences.getUserModel().getBonusBal());
        final float winning = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
        final float bb_coin = CustomUtil.convertFloat(preferences.getUserModel().getFf_coin());

        float usableBonus1=0 , usableBBCoins = 0;

       /* usableBBCoins=CustomUtil.convertFloat(contestData.getGame_use_coin());
        usableBonus1=CustomUtil.convertFloat(contestData.getBonus());*/

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

    private void listenWaiting() {
        offSocket();
        mSocket.on(ConstantUtil.GET_PLAYER_DETAIL,args -> {
            Log.e(ConstantUtil.GET_PLAYER_DETAIL,"GET_PLAYER_DETAIL:- "+args[0]);//{"con_id":0,"utoken":""}
            runOnUiThread(() -> {
                try {
                    if (isRejoinGame && !isLogout){
                        JSONObject obj=new JSONObject(args[0].toString());
                        //if (!TextUtils.isEmpty(obj.optString("gameName")) && obj.optString("gameName").equalsIgnoreCase(gameModel.getType())){
                        if (obj.has("utoken")){
                            if (!TextUtils.isEmpty(obj.optString("utoken"))){
                                if (gameModel!=null && gameModel.getAssetType().equalsIgnoreCase("ludo")){
                                    dialog = new Dialog(this);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                    dialog.setContentView(R.layout.rejoin_dialog);
                                    dialog.setCancelable(false);
                                    TextView imgYes = dialog.findViewById(R.id.btnYes);
                                    ImageView imgNo = dialog.findViewById(R.id.imgClose) ;
                                    layBtn = dialog.findViewById(R.id.layBtn);
                                    layProgress = dialog.findViewById(R.id.layProgress);
                                    txtProgress = dialog.findViewById(R.id.txtProgress);
                                    progress = dialog.findViewById(R.id.progress);

                                    if (!gameModel.getAssetType().equalsIgnoreCase("webview"))
                                        checkAndDownloadAssets();

                                    dialog.setOnDismissListener(dialog1 -> {


                                    });

                                    imgNo.setOnClickListener(v ->  {
                                        if (MyApp.getClickStatus()){
                                            dialog.dismiss();
                                        }
                                    });

                                    imgYes.setOnClickListener(v -> {
                                        if (MyApp.getClickStatus()){
                                            dialog.dismiss();

                                            try {
                                                // UserModel user=preferences.getUserModel();

                                                JSONObject object=new JSONObject();

                                                object.put("socketURL",socket_url.replaceAll("\\\\",""));//"http://192.168.100.185:3000"
                                                object.put("utoken",obj.optString("utoken"));
                                                object.put("asset_path",assets_path);
                                                object.put("comp_id",ConstantUtil.COMPANY_ID);
                                                object.put("token_generate_url",token_generate_url);
                                                object.put("game_type_name",obj.optString("gameName"));//gameType
                                                //object.put("header",getHeaderArray());
                                                object.put("game_mode","");
                                                //object.put("winning_amount",finalContest.getDisAmt());
                                                object.put("play_with_friend","No");

                                                String ludoType = "";
                                                if (!TextUtils.isEmpty(gameModel.getGame_mode()) && gameModel.getGame_mode().equalsIgnoreCase("speedPointLudo")){
                                                    ludoType = gameModel.getGame_mode();
                                                }
                                                object.put("ludoType",ludoType);

                                                isRejoinGame=true;

                                                Log.e("userData",object.toString());
                                                launchUnity(object);
                                                offSocket();

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    if (!isLogout)
                                        dialog.show();
                                }
                                else {
                                    LudoTournamentModel contest = null;
                                    if (list != null) {
                                        for (int k = 0; k < list.size(); k++) {
                                            LudoTournamentModel modal1 = list.get(k);
                                            if (modal1 != null) {
                                                if (modal1.getId().equalsIgnoreCase(obj.optString("con_id"))) {
                                                    contest = modal1;
                                                }
                                            }
                                        }
                                    }
                                    if (contest != null) {
                                        dialog = new Dialog(this);
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                        dialog.setContentView(R.layout.rejoin_dialog);
                                        dialog.setCancelable(false);
                                        TextView imgYes = dialog.findViewById(R.id.btnYes);
                                        ImageView imgNo = dialog.findViewById(R.id.imgClose);
                                        layBtn = dialog.findViewById(R.id.layBtn);
                                        layProgress = dialog.findViewById(R.id.layProgress);
                                        txtProgress = dialog.findViewById(R.id.txtProgress);
                                        progress = dialog.findViewById(R.id.progress);

                                        if (!gameModel.getAssetType().equalsIgnoreCase("webview"))
                                            checkAndDownloadAssets();

                                        dialog.setOnDismissListener(dialog1 -> {


                                        });

                                        imgNo.setOnClickListener(v -> {
                                            if (MyApp.getClickStatus()) {
                                                dialog.dismiss();
                                            }
                                        });

                                        LudoTournamentModel finalContest = contest;

                                        imgYes.setOnClickListener(v -> {
                                            if (MyApp.getClickStatus()) {
                                                dialog.dismiss();

                                                if (gameModel.getAssetType().equalsIgnoreCase("webview")) {
                                                    startActivity(new Intent(mContext, WebViewGameActivity.class).putExtra("gameData", gameModel)
                                                            .putExtra("con_id", finalContest.getId()));
                                                } else {
                                                    try {
                                                        // UserModel user=preferences.getUserModel();

                                                        JSONObject object = new JSONObject();

                                                        object.put("socketURL", socket_url.replaceAll("\\\\", ""));//"http://192.168.100.185:3000"
                                                        object.put("utoken", obj.optString("utoken"));
                                                        object.put("asset_path", assets_path);
                                                        object.put("game_type_name", gameType);
                                                        object.put("comp_id", ConstantUtil.COMPANY_ID);
                                                        object.put("token_generate_url", token_generate_url);
                                                        object.put("game_type_name", gameType);
                                                        //object.put("header", getHeaderArray());
                                                        object.put("game_mode", "");
                                                        object.put("winning_amount", finalContest.getDistAmt());
                                                        object.put("play_with_friend", "No");

                                                        String ludoType = "";
                                                        if (!TextUtils.isEmpty(gameModel.getGame_mode()) && gameModel.getGame_mode().equalsIgnoreCase("speedPointLudo")) {
                                                            ludoType = gameModel.getGame_mode();
                                                        }
                                                        object.put("ludoType", ludoType);

                                                        //ConstantUtil.GAME_ASSETS_NAME);
                                                        isRejoinGame = true;

                                                        Log.e("userData", object.toString());
                                                        launchUnity(object);
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
                            }
                        }
                        //}

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        });
    }

    private void offSocket(){
        if (mSocket!=null) {
            mSocket.off(ConstantUtil.GET_PLAYER_DETAIL);
        }
    }

    private void launchUnity(JSONObject object){
        //isRejoinGame=true;

     /*   Intent intent=new Intent(this, UnityPlayerActivity.class)
                .putExtra("userData",object.toString());
        someActivityResultLauncher.launch(intent);*/

        // offSocket();
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                /*if (result.getResultCode()== Activity.RESULT_OK){
                    if (result.getData()!=null && result.getData().hasExtra("isRejoinGame")){
                        isRejoinGame = !result.getData().getStringExtra("isRejoinGame").equalsIgnoreCase("no");
                    }
                    else if (result.getData()!=null && result.getData().hasExtra("is_webview")){
                        if (mSocket!=null){
                            try {
                                JSONObject object=new JSONObject();
                                object.put("con_id",result.getData().getStringExtra("con_id"));
                                object.put("user_id",preferences.getUserModel().getId());
                                mSocket.emit("exit_contest",object);

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    else if (result.getData()!=null && result.getData().hasExtra("isDepositAdd")){
                        startActivity(new Intent(this,AddDepositActivity.class)
                                .putExtra("depositAmt",""));
                    }
                    else {
                        isRejoinGame=true;
                    }
                }else {
                    isRejoinGame=true;
                }*/
            });

    private void checkAndDownloadAssets(){
        String path = CustomUtil.getAppDirectory(this) + asset_type;
        assets_path=path+getFileNameFromUrl(android_asset_url);//ConstantUtil.GAME_ASSETS_NAME;
        LogUtil.e("resp",assets_path);// /data/user/0/com.batball11/files/ludo/bb11_ludo_android_2023_04_10   /data/user/0/com.batball11/files/ludo/ludo_android_28_02_2023_01

        File filePath=new File(path);
        File file=new File(assets_path);

        boolean isCompletes=preferences.getPrefBoolean(gameModel.getAssetType()+ PrefConstant.DOWNLOAD_STATUS);
        //boolean isCompletes=preferences.getPrefBoolean(asset_type+ PrefConstant.DOWNLOAD_STATUS);
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
            preferences.setPref(asset_type+PrefConstant.DOWNLOAD_STATUS,false);

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
                preferences.setPref(gameModel.getAssetType()+PrefConstant.DOWNLOAD_STATUS,false);
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

            preferences.setPref(gameModel.getAssetType()+PrefConstant.DOWNLOAD_STATUS,true);

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

    private String getFileNameFromUrl(String url){
        return url.substring(url.lastIndexOf('/') + 1);
    }

    private JSONObject getHeaderArray(){

        TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
        Calendar c = Calendar.getInstance(tz);
        long current=c.getTimeInMillis();

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

       /* TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
        Calendar c = Calendar.getInstance(tz);
        long current=c.getTimeInMillis();
        MCrypt crypt=new MCrypt();

        HashMap<String, String> headersMap = new HashMap<>();
        headersMap.put("mobile_type", MyApp.deviceType);
        headersMap.put("mobile_os",MyApp.deviceVersion);
        headersMap.put("phone_uid",MyApp.deviceId);
        headersMap.put("mobile_hardware",MyApp.deviceHardware);
        headersMap.put("mobile_name",MyApp.deviceName);

        if (TextUtils.isEmpty(MyApp.USER_ID)){
            headersMap.put("user_id", "");
        }else {
            headersMap.put("user_id", crypt.Encrypt(MyApp.USER_ID));
        }
        headersMap.put("request_time", current+"");
        headersMap.put("token_no", MyApp.APP_KEY);
        headersMap.put("fcm_id",MyApp.tokenNo);
        headersMap.put("user_header_key",MyApp.user_header_key);
        headersMap.put("lat",MyApp.lat);
        headersMap.put("lng",MyApp.lng);
        headersMap.put("user_id1",MyApp.USER_ID);
        headersMap.put("comp_id", "1");*/

        return new JSONObject(headersMap);
    }

    private void getData() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("user_id", preferences.getUserModel().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // LogUtil.e(TAG, "getData: " + jsonObject.toString()+" \nurl : "+ ApiManager.getTournamentList);
        boolean isProgress=true;
        if (swipe.isRefreshing()){
            isProgress=false;
        }
        HttpRestClient.postJSONNormal(LudoTournamentActivity.this, isProgress, ApiManager.getTournamentList, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                lastUpdateTime=System.currentTimeMillis();
                swipe.setRefreshing(false);
                LogUtil.e(TAG, "getData: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {

                    JSONArray data=responseBody.optJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj=data.optJSONObject(i);
                        LudoTournamentModel model=gson.fromJson(obj.toString(),LudoTournamentModel.class);
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();

                    try {
                        IO.Options opts =new IO.Options();
                        mSocket = IO.socket(socket_url, opts);
                        mSocket.connect();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    JSONObject usrId=new JSONObject();
                    try {
                        usrId.put("user_id",preferences.getUserModel().getId());
                        usrId.put("token_no",preferences.getUserModel().getTokenNo());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    mSocket.emit(ConstantUtil.GET_PLAYER_DETAIL,usrId.toString());
                    isRejoinGame=true;
                    listenWaiting();
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
                swipe.setRefreshing(false);
                //preferences.setUserModel(new UserModel());

            }
        });
    }

    private void getDetailData(LudoTournamentModel model,boolean isdDialogShow) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("con_id", model.getId());
            jsonObject.put("user_id", preferences.getUserModel().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.e(TAG, "getDetailData: " + jsonObject.toString()+" \nurl : "+ ApiManager.getTournamentDetailsList);
        boolean isProgress=true;
        if (swipe.isRefreshing()){
            isProgress=false;
        }
        HttpRestClient.postJSONNormal(LudoTournamentActivity.this, isProgress, ApiManager.getTournamentDetailsList, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                swipe.setRefreshing(false);
                LogUtil.e(TAG, "getDetailData: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {

                    JSONObject data=responseBody.optJSONObject("data");
                    if (isdDialogShow){
                        winning_dec=model.getIsWinCredited();
                        selectedModel=model;
                        showLeaderboardList(data);
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
                swipe.setRefreshing(false);
                //preferences.setUserModel(new UserModel());
            }
        });
    }

    private void showLeaderboardList(JSONObject data) {


        BottomSheetDialog dialog=new BottomSheetDialog(mContext);

        dialog.setCancelable(false);

        dialog.setContentView(R.layout.tournamment_detail_dialog);

        dialog.setOnShowListener(dialog1 -> {
            /*FrameLayout bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);

            if (null != bottomSheet) {
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setDraggable(false);
            }
            setupRatio(dialog);*/
        });

        dialog.show();

        /*dialog.findViewById(R.id.toolbar).setVisibility(View.GONE);
        dialog.findViewById(R.id.spinType).setVisibility(View.GONE);
        dialog.findViewById(R.id.scrollType).setVisibility(View.GONE);
        dialog.findViewById(R.id.view_2).setVisibility(View.GONE);
        dialog.findViewById(R.id.view_1).setVisibility(View.GONE);*/

        TextView txtTabLeader=dialog.findViewById(R.id.txtTabLeader);
        TextView txtTabPrise=dialog.findViewById(R.id.txtTabPrise);
        View viewLeader=dialog.findViewById(R.id.viewLeader);
        View viewPrise=dialog.findViewById(R.id.viewPrise);
        LinearLayout layLeader=dialog.findViewById(R.id.layLeader);
        LinearLayout layPrise=dialog.findViewById(R.id.layPrise);

        Typeface tfn,tfb;
        tfn= ResourcesCompat.getFont(mContext, R.font.roboto_regular);
        tfb= ResourcesCompat.getFont(mContext, R.font.roboto_semi_bold);

        txtTabPrise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTabPrise.setTextColor(getResources().getColor(R.color.white_pure));
                txtTabLeader.setTextColor(getResources().getColor(R.color.btnGrey));

                txtTabLeader.setTypeface(tfn, Typeface.NORMAL);
                txtTabPrise.setTypeface(tfb, Typeface.BOLD);
                viewPrise.setBackgroundResource(R.color.white_pure);
                viewLeader.setBackgroundResource(R.color.transparent);
                layLeader.setVisibility(View.GONE);
                layPrise.setVisibility(View.VISIBLE);
            }
        });

        txtTabLeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTabPrise.setTypeface(tfn, Typeface.NORMAL);
                txtTabLeader.setTypeface(tfb, Typeface.BOLD);

                txtTabLeader.setTextColor(getResources().getColor(R.color.white_pure));
                txtTabPrise.setTextColor(getResources().getColor(R.color.btnGrey));

                viewLeader.setBackgroundResource(R.color.white_pure);
                viewPrise.setBackgroundResource(R.color.transparent);
                layLeader.setVisibility(View.VISIBLE);
                layPrise.setVisibility(View.GONE);
            }
        });


        ProgressBar progress = dialog.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() ->   progress.setVisibility(View.GONE), 800);

        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> setDetailsData(dialog,data), 100);

    }

    private void setupRatio(BottomSheetDialog bottomSheetDialog) {
        /*FrameLayout bottomSheet = (FrameLayout)
                bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        layoutParams.height = getWindowHeight();
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);*/
    }

    private int getWindowHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private void setDetailsData(BottomSheetDialog dialog,JSONObject data){

        SwipeRefreshLayout swipe=dialog.findViewById(R.id.swipe);
        RecyclerView recyclerList=dialog.findViewById(R.id.recyclerList);
        RecyclerView recyclerPrisePool=dialog.findViewById(R.id.recyclerPrisePool);
        LinearLayout layNoData=dialog.findViewById(R.id.layNoData);
        LinearLayout layNoDataPrise=dialog.findViewById(R.id.layNoDataPrise);
        TextView txtTotalPool=dialog.findViewById(R.id.txtTotalPool);
        TextView txtTournamentEnd=dialog.findViewById(R.id.txtTournamentEnd);
        ImageView imgClose=dialog.findViewById(R.id.imgClose);

        JSONArray all_join_data=data.optJSONArray("all_join_data");
        JSONArray my_join_data=data.optJSONArray("my_join_data");
       // JSONArray top_rank_list=data.optJSONArray("1_3_rank_list");

        ArrayList<LudoTournamentUserModel> userList=new ArrayList<>();
        //ArrayList<LudoTournamentUserModel> topList=new ArrayList<>();

       /* if (top_rank_list!=null && top_rank_list.length()>0){
            for (int i = 0; i < top_rank_list.length(); i++) {
                JSONObject obj=top_rank_list.optJSONObject(i);
                LudoTournamentUserModel model=gson.fromJson(obj.toString(),LudoTournamentUserModel.class);
                topList.add(model);
            }
        }*/

        if (my_join_data!=null && my_join_data.length()>0){
            for (int i = 0; i < my_join_data.length(); i++) {
                JSONObject obj=my_join_data.optJSONObject(i);
                LudoTournamentUserModel model=gson.fromJson(obj.toString(),LudoTournamentUserModel.class);
                userList.add(model);
            }
        }

        for (int i = 0; i < all_join_data.length(); i++) {
            JSONObject obj=all_join_data.optJSONObject(i);
            LudoTournamentUserModel model=gson.fromJson(obj.toString(),LudoTournamentUserModel.class);
            userList.add(model);
        }

        if (userList.size()>0){
            layNoData.setVisibility(View.GONE);
            recyclerList.setVisibility(View.VISIBLE);

            //recyclerList.setNestedScrollingEnabled(false);

            userListAdapter=new LudoTournamentUserListAdapter(mContext,userList,winning_dec);
            recyclerList.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerList.setAdapter(userListAdapter);
        }else {
            layNoData.setVisibility(View.VISIBLE);
            recyclerList.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(selectedModel.getWinning_tree())){

            layNoDataPrise.setVisibility(View.GONE);
            recyclerPrisePool.setVisibility(View.VISIBLE);

            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray = new JSONArray(selectedModel.getWinning_tree());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            txtTotalPool.setText(CustomUtil.displayAmountFloat(mContext,selectedModel.getDistAmt()));

            TournamentMatchRankAdapter matchRank = new TournamentMatchRankAdapter(mContext, jsonArray);
            recyclerPrisePool.setHasFixedSize(true);
            recyclerPrisePool.setAdapter(matchRank);
            recyclerPrisePool.setLayoutManager(new LinearLayoutManager(mContext));
        }
        else {
            layNoDataPrise.setVisibility(View.VISIBLE);
            recyclerPrisePool.setVisibility(View.GONE);

            txtTotalPool.setText( mContext.getResources().getString(R.string.rs)+" 00");
        }

       txtTournamentEnd.setText("Tournament Ends on "+CustomUtil.dateConvertWithFormat(selectedModel.getEndTime(),"dd MMM, yy hh:mm aa"));

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerList.getRecycledViewPool().clear();
                userListAdapter.notifyDataSetChanged();

                getDetailData(selectedModel,false);
                swipe.setRefreshing(false);
            }
        });

        imgClose.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    public void getTNCData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("game_id", gameModel.getGameCode());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("game_type", "games");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG,jsonObject.toString());

        HttpRestClient.postJSONNormal(LudoTournamentActivity.this, true, ApiManager.gamesTncGetFF,
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

    private void initClicks() {
        toolbar_back.setOnClickListener(v -> {
            finish();
        });

        swipe.setOnRefreshListener(() -> {
            if ((System.currentTimeMillis()-lastUpdateTime)>=ConstantUtil.Refresh_delay) {
                list.clear();
                recyclerList.getRecycledViewPool().clear();
                adapter.notifyDataSetChanged();
                getData();
            }else {
                swipe.setRefreshing(false);
            }
        });

        toolbar_info.setOnClickListener(v -> {
            getTNCData();
        });

       /* imgInfo.setOnClickListener(v -> {
            getTNCData();
        });*/
    }

}