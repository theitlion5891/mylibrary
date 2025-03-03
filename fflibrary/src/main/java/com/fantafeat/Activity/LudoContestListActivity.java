package com.fantafeat.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fantafeat.Adapter.LudoContestAdapter;
import com.fantafeat.Adapter.PokerTableAdapter;

import com.fantafeat.BuildConfig;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.Model.GameContestModel;
import com.fantafeat.Model.GameMainModal;
import com.fantafeat.Model.Games;
import com.fantafeat.Model.LudoContestModal;
import com.fantafeat.Model.LudoWaitingModal;
import com.fantafeat.Model.NewOfferModal;
import com.fantafeat.Model.PokerTableModel;
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
import com.fantafeat.util.MCrypt;
import com.fantafeat.util.MusicManager;
import com.fantafeat.util.PrefConstant;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.itextpdf.text.pdf.parser.Line;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
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
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LudoContestListActivity extends BaseActivity implements LudoContestAdapter.ItemClickListener {

    private RecyclerView recyclerContest ,recyclerTable;
  /*  private ImageView backgroundTwo;
    private ImageView backgroundOne;*/
    private ArrayList<GameContestModel> mDataList;
    private LudoContestAdapter mAdapter;

    private ImageView imgShowBal,toolbar_friend,toolbar_wallet,toolbar_info,imgBack/*imgWallet*/ ;
    private LinearLayout layWallet,layWalletMain,btnMore,layBal;
    private EditText edtCode,edtAmount;
    private Button btnSubmitTable;
    private TextView txtTabContest,txtTabFriend,txtSpot2,txtSpot3,txtSpot4,txtTabJoin,txtTabCreate,txtTest,txtTitle,txtOnlineCnt;
    private TextView txtMoreLbl;
    private LinearLayout layContest,layCreate,layJoin;
    private RelativeLayout layFriend;
    private Animation slideUp;
    private Animation slideDown;
    private SoundPool sound;
    private int tune;
    private boolean isMusic=false,isSound=true,isVibrate=true,isPerDialog=false;
    private int retryCnt=0;
    private int noSpots=2;
    private String is_ludo_maintanance="No";
    private String ludo_maintenance_msg="";
    private String tnc="";
    private String selectedAvatar="avatar1",socket_url="", asset_url_android="",assetType="",token_generate_url="";
    private Socket mSocket= null;
    private Boolean isSettingOpen = false;
    private JSONObject appUpdateData=new JSONObject();
    private ArrayList<Games> allGamesList=new ArrayList<>();
    private Dialog updateDialog;
    //private GameMainModal.Datum.Game game;
    private int retryPermission=0,no_token_win=0;
    private ProgressBar progress;
    private RelativeLayout bac_dim_layout;
    private TextView txtProgress ,txtNoData;
    private LinearLayout layBtn,layProgress;
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
    private PopupWindow popupWindow;
    private CardView mainWallet;
    private Games games;
    private boolean isRejoinGame=true,isLogout=false,is_contest_waiting=false,is_auto_game_start=false;
    private PokerTableAdapter pokerTableAdapter;
    private ArrayList<PokerTableModel> pokerTableModelArrayList;
    private BottomSheetDialog tableListDialog;
    private Dialog dialog;

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode()==Activity.RESULT_OK){
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
                                .putExtra("depositAmt","1000"));
                    }
                    else {
                        isRejoinGame=true;
                    }
                }else {
                    isRejoinGame=true;
                }
            });

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTransparent();
        setContentView(R.layout.activity_ludo_contest_list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }

        txtTest = findViewById(R.id.txtTest);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attribute =new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            sound=new SoundPool.Builder()
                    .setMaxStreams(7)
                    .setAudioAttributes(attribute)
                    .build();
        }
        else {
            sound=new SoundPool(7, AudioManager.STREAM_MUSIC, 0);
        }

        //tune = sound.load(this, R.raw.click_sound, 1);

        if (getIntent().hasExtra("gameData")){
            games= (Games) getIntent().getSerializableExtra("gameData");

            if (ConstantUtil.is_game_test){
                /*if(games.getAssetType().equalsIgnoreCase("ludo"))
                    asset_url_android="https://drive.usercontent.google.com/u/0/uc?id=1SRkpUDvWdBL_em6vlUmvDyHK2Y_KT_Pu&export=download";//games.getAndroidAssetUrlTest();
                else {
                    //asset_url_android = games.getAndroidAssetUrlTest();

                    if (games.getAssetType().equalsIgnoreCase("Win3Patti")){
                        asset_url_android="https://drive.usercontent.google.com/u/0/uc?id=1J4RgGHu2VZzf_kRbCr2H0AB1klm-90Tq&export=download";
                        games.setSocket_url_test("https://280f-152-58-60-72.ngrok-free.app");
                    }
                    else
                        asset_url_android=games.getAndroidAssetUrlTest();
                }*/

                asset_url_android=games.getAndroidAssetUrlTest();//getAndroidAssetUrlTest();
                socket_url=/*"http://43.204.74.148:4020";*/games.getSocket_url_test();//"http://192.168.100.150:3001/"
                token_generate_url=games.getToken_create_url_test();
                txtTest.setText("TEST MODE : Activated \nSocket url:"+socket_url+"\nAsset Url:"+asset_url_android+"\nGame Type:"+games.getType());
                txtTest.setVisibility(View.VISIBLE);
            }
            else {
                asset_url_android=games.getAndroidAssetUrl();
                socket_url=games.getSocketUrl();
                token_generate_url=games.getToken_create_url();
                txtTest.setVisibility(View.GONE);
            }

            no_token_win = Integer.parseInt(games.getNo_token_win());
            assetType=games.getAssetType()+"/";
            is_auto_game_start= games.getIs_auto_game_start().equalsIgnoreCase("yes");
            is_contest_waiting= games.getIs_contest_waiting().equalsIgnoreCase("yes");
        }

        if (getIntent().hasExtra("allGamesList")){
            allGamesList= (ArrayList<Games>) getIntent().getSerializableExtra("allGamesList");
        }

        slideDown = AnimationUtils.loadAnimation(mContext, R.anim.slide_down_wallet);
        slideUp = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);

        initData();
        initClick();

    }

    private void checkGameData(String asset_url_android){
        //if (getIntent().hasExtra("gameData")){
            try {
                //game = (GameMainModal.Datum.Game) getIntent().getSerializableExtra("gameData");

                String path =
                        CustomUtil.getAppDirectory(this) + assetType;
                String downloadUrl=path+ ConstantUtil.GAME_ASSETS_NAME;

                File filePath=new File(path);
                File file=new File(downloadUrl);

                boolean isCompletes=preferences.getPrefBoolean(games.getAssetType()+ PrefConstant.DOWNLOAD_STATUS);
                if (!isCompletes){
                    if (file.exists()){
                        file.delete();
                    }
                }

                Log.e("appDir", "Path: "+path+"\n"+downloadUrl);

                if (!filePath.exists()){
                    filePath.mkdirs();
                }

                if (!file.exists()){
                    preferences.setPref(games.getAssetType()+PrefConstant.DOWNLOAD_STATUS,false);

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
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                            // if (!Environment.isExternalStorageManager()){
                                            if (!Environment.isExternalStorageManager() && !isSettingOpen) {

                                                isSettingOpen = true;
                                                Intent intent = new Intent();
                                                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                                intent.setData(Uri.parse("package:com.fantafeat"));
                                                startActivityForResult(intent, 2242);
                                            } else {

                                            }
                                        }else {

                                        }
                                    }

                                    if (report.isAnyPermissionPermanentlyDenied() && !isPerDialog){
                                        showSettingsDialog();
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> list, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }

                            }).check();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

       // }
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
                preferences.setPref(games.getAssetType()+PrefConstant.DOWNLOAD_STATUS,false);
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

            preferences.setPref(games.getAssetType()+PrefConstant.DOWNLOAD_STATUS,true);

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

    @SuppressLint("MissingPermission")
    public void clickEffect(){

        if (isSound){
            sound.play(tune, 1f, 1f, 1, 0, 1f);
        }

        if (isVibrate){
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(100);
            }
        }
    }

    private void initData() {
       // backgroundTwo = findViewById(R.id.background_two);
        //backgroundOne = findViewById(R.id.background_one);
        imgBack = findViewById(R.id.toolbar_back);
        toolbar_info = findViewById(R.id.toolbar_info);
        toolbar_wallet = findViewById(R.id.toolbar_wallet);
        bac_dim_layout = findViewById(R.id.bac_dim_layout);
        toolbar_friend = findViewById(R.id.toolbar_friend);
        txtTitle = findViewById(R.id.txtTitle);
        //imgWallet = findViewById(R.id.imgWallet);
        //imgProfile = findViewById(R.id.imgProfile);
        txtTabContest = findViewById(R.id.txtTabContest);
        txtTabFriend = findViewById(R.id.txtTabFriend);
        layContest = findViewById(R.id.layContest);
        layFriend = findViewById(R.id.layFriend);
        txtSpot2 = findViewById(R.id.txtSpot2);
        txtSpot3 = findViewById(R.id.txtSpot3);
        txtSpot4 = findViewById(R.id.txtSpot4);
        txtTabCreate = findViewById(R.id.txtTabCreate);
        txtTabJoin = findViewById(R.id.txtTabJoin);
        layJoin = findViewById(R.id.layJoin);
        layCreate = findViewById(R.id.layCreate);
        edtCode = findViewById(R.id.edtCode);
        edtAmount = findViewById(R.id.edtAmount);
        btnSubmitTable = findViewById(R.id.btnSubmitTable);
        txtNoData = findViewById(R.id.txtNoData);

        recyclerContest = findViewById(R.id.recyclerContest);

        txtTitle.setText(games.getGameName());

        if (getIntent().hasExtra("ludoCode") && !TextUtils.isEmpty(getIntent().getStringExtra("ludoCode"))){
            String code=getIntent().getStringExtra("ludoCode");
            layContest.setVisibility(View.GONE);
            layFriend.setVisibility(View.VISIBLE);
            txtTabContest.setBackgroundResource(R.drawable.transparent_view);
           // txtTabFriend.setBackgroundResource(R.drawable.selected_ludo_tab);
            txtTabFriend.setTextColor(getResources().getColor(R.color.black_pure));
            txtTabContest.setTextColor(getResources().getColor(R.color.white_pure));
            getJoinTable(code);
            edtCode.setText(code);
        }

        mDataList = new ArrayList();

        recyclerContest.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        ));
        mAdapter = new LudoContestAdapter(mDataList,this);
        recyclerContest.setAdapter(mAdapter);

        isSound = preferences.getPrefBoolean(PrefConstant.LUDO_SOUND_KEY);
        isVibrate = preferences.getPrefBoolean(PrefConstant.LUDO_VIBRATE_KEY);

        isMusic=preferences.getPrefBoolean(PrefConstant.LUDO_MUSIC_KEY);

        /*if (isMusic){
            MusicManager.getInstance().play(this);
        }*/

    }

    private void listenWaiting(){
        if (mSocket!=null){

            mSocket.off(ConstantUtil.WAITING_LUDO_USER);
            mSocket.off(ConstantUtil.GET_PLAYER_DETAIL);
            mSocket.off(ConstantUtil.RES);

            mSocket.on(ConstantUtil.WAITING_LUDO_USER, args -> {
                LogUtil.e(ConstantUtil.WAITING_LUDO_USER,"Response:- "+args[0]);
                if (args[0]!=null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject data=new JSONObject(args[0].toString());
                                //JSONObject data=obj.optJSONObject("data");

                                /*if (data !=null && data.has("playerInfo")){
                                    JSONArray playerInfo=data.optJSONArray("playerInfo");
                                    if (playerInfo!=null){
                                        if (mDataList!=null){
                                            for (int k=0;k<mDataList.size();k++){
                                                GameContestModel modal = mDataList.get(k);
                                                if (modal.getType()==1){
                                                    if (modal.getId().equalsIgnoreCase(data.optString("con_id"))){
                                                        if (data.optInt("activePlayer")>0){
                                                            ArrayList<LudoWaitingModal> waitingModals=new ArrayList<LudoWaitingModal>();
                                                            for (int i=0;i<playerInfo.length();i++){
                                                                JSONObject playerObj=playerInfo.optJSONObject(i);
                                                                if (playerObj.has("userInf")){
                                                                    JSONObject userInf=playerObj.optJSONObject("userInf");
                                                                    LudoWaitingModal waitingModal = new LudoWaitingModal();
                                                                    waitingModal.setUser_avatar(userInf.optString("profilePic"));
                                                                    waitingModal.setUser_team_name(" Player");//userInf.optString("user_team_name")
                                                                    waitingModal.setUser_name(userInf.optString("userName"));
                                                                    waitingModal.setUser_id(userInf.optString("user_id"));
                                                                    waitingModals.add(waitingModal);
                                                                }
                                                            }
                                                            modal.setWaitCount(data.optInt("activePlayer"));//waitingModals.size()
                                                            modal.setWaitingModals(waitingModals);

                                                            if (data.optString("activePlayer").equalsIgnoreCase(modal.getNo_spot())){
                                                                final Handler handler = new Handler(Looper.getMainLooper());
                                                                handler.postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        modal.setWaitCount(0);
                                                                        modal.setWaitingModals(new ArrayList<LudoWaitingModal>());
                                                                        mAdapter.notifyDataSetChanged();
                                                                    }
                                                                }, 700);
                                                            }

                                                        }else {
                                                            modal.setWaitCount(0);
                                                            modal.setWaitingModals(new ArrayList<LudoWaitingModal>());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }*/

                                if (games.getAssetType().equalsIgnoreCase("ludo")){
                                    if (data.optString("isPublic").equalsIgnoreCase("yes")) {
                                        if (data.optString("gameId").equalsIgnoreCase(games.getGameCode())) {
                                            if (mDataList!=null){
                                                for (int k=0;k<mDataList.size();k++){
                                                    GameContestModel modal = mDataList.get(k);
                                                    if (modal.getType()==1){
                                                        if (modal.getId().equalsIgnoreCase(data.optString("lobbyId"))){
                                                            if (data.optInt("numberOfPlayersJoined")>0){
                                                                ArrayList<LudoWaitingModal> waitingModals=new ArrayList<LudoWaitingModal>();
                                                                JSONArray userDetail=data.optJSONArray("userDetail");
                                                                for (int i=0;i<userDetail.length();i++){
                                                                    JSONObject playerObj=userDetail.optJSONObject(i);
                                                                    if (!TextUtils.isEmpty(playerObj.optString("userId"))){
                                                                        LudoWaitingModal waitingModal = new LudoWaitingModal();
                                                                        waitingModal.setUser_avatar(playerObj.optString("profilePic"));
                                                                        waitingModal.setUser_team_name(playerObj.optString("user_team_name"));
                                                                        waitingModal.setUser_id(playerObj.optString("userId"));
                                                                        waitingModals.add(waitingModal);
                                                                    }
                                                                }
                                                                modal.setWaitCount(waitingModals.size());
                                                                modal.setWaitingModals(waitingModals);

                                                                if (data.optString("numberOfPlayersJoined").equalsIgnoreCase(modal.getNo_spot())){
                                                                    final Handler handler = new Handler(Looper.getMainLooper());
                                                                    handler.postDelayed(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            modal.setWaitCount(0);
                                                                            modal.setWaitingModals(new ArrayList<LudoWaitingModal>());
                                                                            mAdapter.notifyDataSetChanged();
                                                                        }
                                                                    }, 300);
                                                                }

                                                            }else {
                                                                modal.setWaitCount(0);
                                                                modal.setWaitingModals(new ArrayList<LudoWaitingModal>());
                                                            }
                                                        }
                                                    }

                                                }
                                            }
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                                else if (data!=null && data.has("seats")){
                                    JSONArray seats=data.optJSONArray("seats");
                                    if (mDataList!=null){
                                        for (int k=0;k<mDataList.size();k++){
                                            GameContestModel modal1 = mDataList.get(k);
                                            if (modal1.getType()==1){
                                                if (modal1.getId().equalsIgnoreCase(data.optString("con_id"))){
                                                    if (data.optInt("totalPlayers")>0){
                                                        ArrayList<LudoWaitingModal> waitingModals=new ArrayList<LudoWaitingModal>();
                                                        for (int i=0;i<seats.length();i++){
                                                            JSONObject playerObj=seats.optJSONObject(i);
                                                            if (playerObj!=null && playerObj.has("profileURL")){
                                                                LudoWaitingModal waitingModal = new LudoWaitingModal();
                                                                waitingModal.setUser_avatar(playerObj.optString("profileURL"));
                                                                waitingModal.setUser_team_name(playerObj.optString("team_name"));
                                                                waitingModal.setUser_id(playerObj.optString("userId"));
                                                                waitingModals.add(waitingModal);
                                                            }
                                                        }
                                                        modal1.setWaitCount(waitingModals.size());
                                                        modal1.setWaitingModals(waitingModals);

                                                        if (data.optString("totalPlayers").equalsIgnoreCase(modal1.getNo_spot())){
                                                            final Handler handler = new Handler(Looper.getMainLooper());
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    modal1.setWaitCount(0);
                                                                    modal1.setWaitingModals(new ArrayList<LudoWaitingModal>());
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
                                else if (data.has("data")){
                                    JSONArray array = data.optJSONArray("data");
                                    for (int k=0;k<mDataList.size();k++) {
                                        GameContestModel modal1 = mDataList.get(k);
                                        if (modal1.getType()==1){
                                            modal1.setWaitCount(0);
                                            modal1.setWaitingModals(new ArrayList<LudoWaitingModal>());
                                        }
                                    }
                                    if (array != null && array.length()>0){
                                        for (int m=0;m<array.length();m++){
                                            JSONObject data1=array.optJSONObject(m);
                                            if ( data1.has("waiting_user")){
                                                //JSONArray seats=data.optJSONArray("active_player");
                                                if (mDataList!=null){
                                                    for (int k=0;k<mDataList.size();k++){
                                                        GameContestModel modal1 = mDataList.get(k);
                                                        if (modal1.getType()==1){
                                                            if (modal1.getId().equalsIgnoreCase(data1.optString("con_id"))){
                                                                JSONArray active_player=data1.optJSONArray("waiting_user");
                                                                if (active_player.length()>0){
                                                                    ArrayList<LudoWaitingModal> waitingModals=new ArrayList<LudoWaitingModal>();
                                                                    for (int i=0;i<active_player.length();i++){
                                                                        JSONObject playerObj=active_player.optJSONObject(i);
                                                                        if (playerObj!=null && playerObj.has("user_team_name")){
                                                                            LudoWaitingModal waitingModal = new LudoWaitingModal();
                                                                            waitingModal.setUser_avatar(playerObj.optString("user_avatar"));
                                                                            waitingModal.setUser_team_name(playerObj.optString("user_team_name"));
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

                                                                } else {
                                                                    modal1.setWaitCount(0);
                                                                    modal1.setWaitingModals(new ArrayList<LudoWaitingModal>());
                                                                }
                                                            }
                                                        }

                                                    }
                                                }
                                                // mAdapter.notifyDataSetChanged();
                                            }
                                        }
                                     }

                                    mAdapter.notifyDataSetChanged();
                                }

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

            mSocket.on(ConstantUtil.GET_PLAYER_DETAIL,args -> {
                  Log.e(ConstantUtil.GET_PLAYER_DETAIL,"GET_PLAYER_DETAIL:- "+args[0]);//{"con_id":0,"utoken":""}
                runOnUiThread(() -> {
                    try {
                        if (isRejoinGame && !isLogout){
                            JSONObject obj=new JSONObject(args[0].toString());

                            if (obj.has("utoken")){
                                if (!TextUtils.isEmpty(obj.optString("utoken"))){
                                    if (games!=null && games.getAssetType().equalsIgnoreCase("ludo")){
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

                                        if (!games.getAssetType().equalsIgnoreCase("webview"))
                                            checkAndDownloadAssets();

                                        dialog.setOnDismissListener(dialog1 -> {


                                        });

                                        imgNo.setOnClickListener(v ->  {
                                            if (MyApp.getClickStatus()){
                                                clickEffect();
                                                dialog.dismiss();
                                            }
                                        });

                                        imgYes.setOnClickListener(v -> {
                                            if (MyApp.getClickStatus()){
                                                dialog.dismiss();
                                                clickEffect();

                                                try {
                                                    // UserModel user=preferences.getUserModel();

                                                    JSONObject object=new JSONObject();

                                                    object.put("socketURL",socket_url);//"http://192.168.100.185:3000"
                                                    object.put("utoken",obj.optString("utoken"));
                                                    object.put("asset_path",CustomUtil.getAppDirectory(this) + assetType + getFileNameFromUrl(asset_url_android));
                                                    object.put("comp_id",ConstantUtil.COMPANY_ID);
                                                    object.put("token_generate_url",token_generate_url);
                                                    object.put("game_type_name",obj.optString("gameName"));//gameType
                                                    //object.put("header",getHeaderArray());
                                                    object.put("game_mode","");
                                                    //object.put("winning_amount",finalContest.getDisAmt());
                                                    object.put("play_with_friend","No");

                                                    if (games.getType().equalsIgnoreCase("power")){
                                                        object.put("game_type_name","Classic");
                                                        object.put("game_mode",games.getType());
                                                    }

                                                    String ludoType = "";
                                                    if (!TextUtils.isEmpty(games.getGame_mode()) &&
                                                            games.getGame_mode().equalsIgnoreCase("speedPointLudo")){
                                                        ludoType = games.getGame_mode();
                                                    }
                                                    object.put("ludoType",ludoType);

                                                    isRejoinGame=true;

                                                    Log.e("userData",object.toString());
                                                    launchUnity(object);

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        if (!isLogout)
                                            dialog.show();
                                    }
                                    else {
                                        GameContestModel contest=null;
                                        if (mDataList!=null) {
                                            for (int k = 0; k < mDataList.size(); k++) {
                                                GameContestModel modal1 = mDataList.get(k);
                                                contest=modal1;
                                            }
                                        }
                                        if (contest!=null){
                                            dialog = new Dialog(this);
                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                            dialog.setContentView(R.layout.rejoin_dialog);

                                            TextView imgYes = dialog.findViewById(R.id.btnYes);
                                            ImageView imgNo = dialog.findViewById(R.id.imgClose) ;
                                            layBtn = dialog.findViewById(R.id.layBtn);
                                            layProgress = dialog.findViewById(R.id.layProgress);
                                            txtProgress = dialog.findViewById(R.id.txtProgress);
                                            progress = dialog.findViewById(R.id.progress);

                                            if (!games.getAssetType().equalsIgnoreCase("webview")){
                                                checkAndDownloadAssets();
                                            }

                                            dialog.setOnDismissListener(dialog1 -> {


                                            });

                                            imgNo.setOnClickListener(v ->  {
                                                if (MyApp.getClickStatus()){
                                                    clickEffect();
                                                    dialog.dismiss();
                                                }
                                            });

                                            GameContestModel finalContest = contest;
                                            imgYes.setOnClickListener(v -> {
                                                if (MyApp.getClickStatus()){

                                                    dialog.dismiss();
                                                    clickEffect();

                                                    if (games.getAssetType().equalsIgnoreCase("webview")){
                                                        startActivity(new Intent(mContext,WebViewGameActivity.class) .putExtra("gameData",games)
                                                                .putExtra("con_id", finalContest.getId()));
                                                    } else {
                                                        try {
                                                            //UserModel user=preferences.getUserModel();

                                                            JSONObject object=new JSONObject();
                                                            /*object.put("con_id",finalContest.getId());
                                                            object.put("comp_id",ConstantUtil.COMPANY_ID);
                                                            object.put("winning_amount",finalContest.getDisAmt());
                                                            object.put("user_avatar",getAgeWiseUrl());//ApiManager.PROFILE_IMG + user.getUserImg()
                                                            object.put("no_of_spot",finalContest.getNo_spot());
                                                            object.put("user_id",user.getId());
                                                            object.put("user_name",user.getDisplayName());
                                                            object.put("user_team_name",user.getUserTeamName());
                                                            object.put("play_with_friend","No");
                                                            object.put("is_creator","No");
                                                            object.put("game_play_code","");
                                                            object.put("game_mode","");
                                                            object.put("utoken",obj.optString("utoken"));
                                                            object.put("entry_fee",finalContest.getEntryFee());
                                                            object.put("socketURL",socket_url);////"http://15.206.22.216:5000"
                                                            object.put("header",getHeaderArray());
                                                            object.put("token_generate_url",token_generate_url);
                                                            //object.put("game_type_name",games.getType());//"Classic"
                                                            object.put("game_type_name",obj.optString("gameName"));//gameType
                                                            object.put("no_token_win",no_token_win);
                                                            object.put("contest_timer",finalContest.getGame_time());
                                                            object.put("total_round",finalContest.getGame_round());
                                                            object.put("asset_path",
                                                                    CustomUtil.getAppDirectory(this) + assetType + getFileNameFromUrl(asset_url_android));//ConstantUtil.GAME_ASSETS_NAME
                                                            String ludoType = "";
                                                            if (!TextUtils.isEmpty(games.getGameLabel()) && games.getGameLabel().equalsIgnoreCase("speedPointLudo")){
                                                                ludoType = games.getGameLabel();
                                                            }
                                                            object.put("ludoType",ludoType);*/

                                                            object.put("socketURL",socket_url.replaceAll("\\\\",""));//"http://192.168.100.185:3000"
                                                            object.put("utoken",obj.optString("utoken"));
                                                            object.put("asset_path",CustomUtil.getAppDirectory(this) + assetType + getFileNameFromUrl(asset_url_android));
                                                            object.put("comp_id",ConstantUtil.COMPANY_ID);
                                                            object.put("token_generate_url",token_generate_url);
                                                            object.put("game_type_name",obj.optString("gameName"));//obj.optString("gameName")
                                                            //object.put("header",getHeaderArray());
                                                            object.put("game_mode","");
                                                            if (games.getType().equalsIgnoreCase("power")){
                                                                object.put("game_type_name","Classic");
                                                                object.put("game_mode",games.getType());
                                                            }
                                                            object.put("winning_amount",finalContest.getDisAmt());
                                                            object.put("play_with_friend","No");

                                                            String ludoType = "";
                                                            if (!TextUtils.isEmpty(games.getGame_mode()) && games.getGame_mode().equalsIgnoreCase("speedPointLudo")){
                                                                ludoType = games.getGame_mode();
                                                            }
                                                            object.put("ludoType",ludoType);

                                                            //ConstantUtil.GAME_ASSETS_NAME);
                                                            isRejoinGame=true;

                                                            Log.e("userData",object.toString());
                                                            launchUnity(object);

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
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            });

            mSocket.on(ConstantUtil.RES,args -> {
                LogUtil.e("resp",ConstantUtil.RES+": "+args[0]);
                runOnUiThread(()->{
                    try {
                        if (args[0]!=null){

                            JSONObject object=new JSONObject(args[0].toString());
                            if (object!=null){
                                String en=object.optString("en");
                                if (en.equalsIgnoreCase(ConstantUtil.CONTAST_PLAYER_COUNT)){
                                    JSONObject data=object.optJSONObject("data");
                                    if (data!=null){
                                        JSONArray selectTable=data.optJSONArray("selectTable");
                                        //GameContestModel.Datum contest=null;
                                        for (int i = 0; i < selectTable.length(); i++) {
                                            JSONObject table=selectTable.optJSONObject(i);

                                            if (mDataList!=null) {
                                                for (int k = 0; k < mDataList.size(); k++) {
                                                    GameContestModel modal1 = mDataList.get(k);
                                                    if (modal1.getType()==1) {
                                                        if (modal1.getId().equalsIgnoreCase(table.optString("con_id"))) {
                                                            modal1.setPokerNoOfPlaying(table.optString("player_count"));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }
                                else if (en.equalsIgnoreCase("getPlayerDetails")){
                                    JSONObject selectTable=object.optJSONObject("data").optJSONObject("selectTable");
                                    String con_id=selectTable.optString("con_id");
                                    if (selectTable!=null){
                                        boolean isAbleToRejoin=selectTable.optBoolean("isAbleToRejoin");
                                        if (isAbleToRejoin){

                                            if (dialog!=null && dialog.isShowing()){
                                                dialog.dismiss();
                                            }

                                            dialog = new Dialog(this);
                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                            dialog.setContentView(R.layout.rejoin_dialog);

                                            txtYes = dialog.findViewById(R.id.btnYes);
                                            ImageView imgNo = dialog.findViewById(R.id.imgClose) ;
                                            imgNo.setVisibility(View.GONE);
                                            layBtn = dialog.findViewById(R.id.layBtn);
                                            LinearLayout layAsset = dialog.findViewById(R.id.layProgress);
                                            txtProgress = dialog.findViewById(R.id.txtProgress);
                                            progress = dialog.findViewById(R.id.progress);

                                            if (!games.getAssetType().equalsIgnoreCase("webview")){
                                                try {
                                                    //game = (GameMainModal.Datum.Game) getIntent().getSerializableExtra("gameData");

                                                    String path = CustomUtil.getAppDirectory(this) + assetType;
                                                    String downloadUrl=path+ getFileNameFromUrl(asset_url_android);//ConstantUtil.GAME_ASSETS_NAME;

                                                    File filePath=new File(path);
                                                    File file=new File(downloadUrl);

                                                    boolean isCompletes=preferences.getPrefBoolean(games.getAssetType()+ PrefConstant.DOWNLOAD_STATUS);
                                                    if (!isCompletes){
                                                        if (file.exists()){
                                                            file.delete();
                                                        }
                                                    }

                                                    Log.e("appDir", "Path: "+path+"\n"+downloadUrl);

                                                    if (!filePath.exists()){
                                                        filePath.mkdirs();
                                                    }

                                                    if (!file.exists()){

                                                        layAsset.setVisibility(View.VISIBLE);
                                                        txtYes.setVisibility(View.GONE);
                                                        dialog.setCancelable(false);

                                                        preferences.setPref(games.getAssetType()+PrefConstant.DOWNLOAD_STATUS,false);

                                                        Dexter.withContext(this)
                                                                .withPermissions(p).withListener(new MultiplePermissionsListener() {
                                                                    @Override
                                                                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                                                                        if (report.areAllPermissionsGranted()) {

                                                                            if (filePath.isDirectory()) {
                                                                                String[] children = filePath.list();
                                                                                if (children != null) {
                                                                                    for (int i = 0; i < children.length; i++) {
                                                                                        if (!children[i].contains(getFileNameFromUrl(asset_url_android))){
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
                                                                                    new DownloadFileFromURL(downloadUrl,txtProgress,progress,dialog,
                                                                                            layBtn,layProgress).execute(asset_url_android);
                                                                                    //showAssetsDownload(downloadUrl,asset_url_android);
                                                                                }
                                                                            }else {
                                                                                new DownloadFileFromURL(downloadUrl,txtProgress,progress,dialog,
                                                                                        layBtn,layProgress).execute(asset_url_android);
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

                                                    }
                                                    else {
                                                        dialog.setCancelable(true);
                                                        layAsset.setVisibility(View.GONE);
                                                        txtYes.setVisibility(View.VISIBLE);
                                                    }

                                                }
                                                catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            else {
                                                dialog.setCancelable(true);
                                                layAsset.setVisibility(View.GONE);
                                                txtYes.setVisibility(View.VISIBLE);
                                            }

                                            dialog.setOnDismissListener(dialog1 -> {


                                            });

                                            imgNo.setOnClickListener(v ->  {
                                                if (MyApp.getClickStatus()){
                                                    clickEffect();
                                                    dialog.dismiss();
                                                }
                                            });

                                            txtYes.setOnClickListener(v -> {
                                                if (MyApp.getClickStatus()){
                                                    dialog.dismiss();
                                                    clickEffect();
                                                    GameContestModel gameConModel=null;
                                                    if (mDataList!=null) {

                                                        for (int k = 0; k < mDataList.size(); k++) {
                                                            GameContestModel modal1 = mDataList.get(k);
                                                            if (modal1.getType()==1) {
                                                                if (modal1.getId().equalsIgnoreCase(con_id)) {
                                                                    gameConModel = modal1;
                                                                }
                                                            }
                                                        }
                                                    }

                                                    try {

                                                        JSONObject object1=new JSONObject();

                                                        JSONObject pokerObj=new JSONObject();

                                                        UserModel userModel=preferences.getUserModel();
                                                        float totalBal=CustomUtil.convertFloat(userModel.getWinBal())+
                                                                CustomUtil.convertFloat(userModel.getDepositBal())+
                                                                CustomUtil.convertFloat(userModel.getTransferBal())+
                                                                CustomUtil.convertFloat(userModel.getFf_coin());

                                                        if (gameConModel!=null){
                                                            float nospot=CustomUtil.convertFloat(gameConModel.getNo_spot());
                                                            float gameround=CustomUtil.convertFloat(gameConModel.getGame_round());

                                                            pokerObj.put("con_id",con_id);//
                                                            pokerObj.put("joinTable_amount",0);//totalBal+

                                                            pokerObj.put("stakes_amount",nospot/gameround);//contest.getPokerMinBuyIn()

                                                            pokerObj.put("current_balance",CustomUtil.convertDouble(totalBal+""));

                                                            pokerObj.put("minBuyIn",CustomUtil.convertInt(gameConModel.getEntryFee()));

                                                            pokerObj.put("userId",userModel.getId());
                                                            //pokerObj.put("user_avatar",getAgeWiseUrl());//userModel.getLevel3Id()
                                                            pokerObj.put("user_avatar", userModel.getLevel3Id());
                                                            pokerObj.put("user_team_name",userModel.getUserTeamName());
                                                            pokerObj.put("user_Id","");
                                                            pokerObj.put("tableId","");
                                                            pokerObj.put("commission",CustomUtil.convertInt(gameConModel.getGame_profit_amt()));
                                                            pokerObj.put("small_blind",CustomUtil.convertInt(gameConModel.getNo_spot()));
                                                            pokerObj.put("big_blind",CustomUtil.convertInt(gameConModel.getGame_round()));
                                                            pokerObj.put("isplayerrejoin",true);
                                                            pokerObj.put("comp_id",ConstantUtil.COMPANY_ID);
                                                            object1.put("poker_temporary_object",pokerObj);
                                                            object1.put("header",getHeaderArray());
                                                            //pokerObj.put("commission",CustomUtil.convertInt(contest.getCommission()));

                                                            object1.put("socketURL",socket_url.replaceAll("\\\\",""));//"http://192.168.100.185:3000"
                                                            object1.put("utoken","");
                                                            object1.put("asset_path",CustomUtil.getAppDirectory(this) + assetType + getFileNameFromUrl(asset_url_android));
                                                            object1.put("game_type_name",games.getType());
                                                            object1.put("comp_id",ConstantUtil.COMPANY_ID);
                                                            object1.put("ludoType","");
                                                            if (games.getType().equalsIgnoreCase("power")){
                                                                object1.put("game_type_name","Classic");
                                                                object1.put("game_mode",games.getType());
                                                            }

                                                            isRejoinGame=true;

                                                            Log.e("userData",object1.toString());
                                                            launchUnity(object1);
                                                           /* Intent intent=new Intent(this, UnityPlayerActivity.class)
                                                                    .putExtra("userData",object1.toString());
                                                            someActivityResultLauncher.launch(intent);

                                                            offEmmiter();*/
                                                        }


                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });

                                            dialog.show();
                                        }
                                    }
                                }
                                else {
                                    if (tableListDialog!=null && tableListDialog.isShowing() && pokerTableAdapter!=null){
                                        JSONObject data=object.optJSONObject("data");
                                        if (data!=null){
                                            JSONArray selectTable=data.optJSONArray("selectTable");
                                            JSONObject userData=data.optJSONObject("userData");
                                            pokerTableModelArrayList.clear();
                                            //int onlineCnt=0;
                                            for (int i = 0; i < selectTable.length(); i++) {
                                                JSONObject obj=selectTable.optJSONObject(i);
                                                PokerTableModel model=new PokerTableModel();
                                                model.setStakes_amount(obj.optString("stakes_amount"));
                                                model.setAvgStack(obj.optString("avgStakes"));
                                                model.setUser_id(userData.optString("_id"));
                                                model.setMin_buy_in(obj.optString("minBuyIn"));
                                                model.setTable_id(obj.optString("_id"));
                                                model.setTotal_player(obj.optString("maxSeat"));
                                                model.setWaiting_player(obj.optString("totalPlayers"));
                                                pokerTableModelArrayList.add(model);
                                                //onlineCnt+=CustomUtil.convertInt(model.getWaiting_player());
                                            }

                                            //int finalOnlineCnt = onlineCnt;
                                            runOnUiThread(() -> {
                                                pokerTableAdapter.notifyDataSetChanged();
                                                /*if (txtOnlineCnt!=null){
                                                    txtOnlineCnt.setText(finalOnlineCnt +" Playing");
                                                }*/
                                            });
                                        }
                                    }

                                }
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            });

        }
        // LogUtil.e("getContestList","listenWaiting");
    }

    private void checkAndDownloadAssets(){
        try {
            //game = (GameMainModal.Datum.Game) getIntent().getSerializableExtra("gameData");

            String path =
                    CustomUtil.getAppDirectory(this) + assetType;
            String downloadUrl=path+ getFileNameFromUrl(asset_url_android);//ConstantUtil.GAME_ASSETS_NAME;

            File filePath=new File(path);
            File file=new File(downloadUrl);

            boolean isCompletes=preferences.getPrefBoolean(games.getAssetType()+ PrefConstant.DOWNLOAD_STATUS);
            if (!isCompletes){
                if (file.exists()){
                    file.delete();
                }
            }

            //Log.e("appDir", "Path: "+path+"\n"+downloadUrl);

            if (!filePath.exists()){
                filePath.mkdirs();
            }

            if (!file.exists()){

                layProgress.setVisibility(View.VISIBLE);
                layBtn.setVisibility(View.GONE);
                dialog.setCancelable(false);

                preferences.setPref(games.getAssetType()+PrefConstant.DOWNLOAD_STATUS,false);
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
                                                if (!children[i].contains(getFileNameFromUrl(asset_url_android))){
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
                                            new DownloadFileFromURL(downloadUrl,txtProgress,progress,dialog,
                                                    layBtn,layProgress).execute(asset_url_android);
                                            //showAssetsDownload(downloadUrl,asset_url_android);
                                        }
                                    }else {
                                        new DownloadFileFromURL(downloadUrl,txtProgress,progress,dialog,layBtn,layProgress)
                                                .execute(asset_url_android);
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

            }
            else {
                if (dialog!=null)
                    dialog.setCancelable(true);
                if (layAsset!=null)
                    layAsset.setVisibility(View.GONE);
                if (txtYes!=null)
                    txtYes.setVisibility(View.VISIBLE);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONObject getHeaderArray(){

        TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
        Calendar c = Calendar.getInstance(tz);
        long current=c.getTimeInMillis();
        //MCrypt crypt=new MCrypt();

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

    private void launchUnity(JSONObject object){
        /*Intent intent=new Intent(this, UnityPlayerActivity.class)
                .putExtra("userData",object.toString());
        someActivityResultLauncher.launch(intent);*/

        offEmmiter();
    }

    public void initClick() {

        toolbar_wallet.setOnClickListener(v -> {
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

            //float available_bal = deposit_bal + winning_bal  + reward_bal+coin_bal;
            float available_bal;
            if (ConstantUtil.is_bonus_show){
                available_bal = deposit_bal + winning_bal /*+ borrowed_bal*/ + reward_bal + coin_bal;
                layBonus.setVisibility(View.VISIBLE);
            }else {
                available_bal = deposit_bal + winning_bal /*+ borrowed_bal*/ + coin_bal;
                layBonus.setVisibility(View.GONE);
            }

            txtTotalBal.setText(mContext.getResources().getString(R.string.rs) +
                    CustomUtil.getFormater("00.00").format((available_bal/*+old_coin_bal*/)));
            txtDepositBal.setText(mContext.getResources().getString(R.string.rs) +
                    CustomUtil.getFormater("00.00").format((deposit_bal+coin_bal)));
            txtWinningBal.setText(mContext.getResources().getString(R.string.rs) +
                    CustomUtil.getFormater("00.00").format(winning_bal));
            txtBonusBal.setText(mContext.getResources().getString(R.string.rs) +
                    CustomUtil.getFormater("00.00").format(reward_bal));
            /*txtCoinBal.setText(mContext.getResources().getString(R.string.rs) +
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
            }
        });

        txtTabCreate.setOnClickListener(v -> {
            /*if (MyApp.getClickStatus()){
                hideKeyBoard();
                clickEffect();
                txtTabCreate.setBackgroundResource(R.drawable.bg_edt_ludo);
                txtTabJoin.setBackgroundResource(R.drawable.transparent_view);
                txtTabCreate.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                txtTabJoin.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                // txtTabJoin.setTextColor(getResources().getColor(R.color.black));
                //txtTabCreate.setTextColor(getResources().getColor(R.color.white));

                layCreate.setVisibility(View.VISIBLE);
                layJoin.setVisibility(View.GONE);

                btnSubmitTable.setText("Create Table");
            }*/

        });

        txtTabJoin.setOnClickListener(v -> {
            /*if (MyApp.getClickStatus()){
                hideKeyBoard();
                clickEffect();
                txtTabJoin.setBackgroundResource(R.drawable.bg_edt_ludo);
                txtTabCreate.setBackgroundResource(R.drawable.transparent_view);

                txtTabCreate.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                txtTabJoin.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));


                layCreate.setVisibility(View.GONE);
                layJoin.setVisibility(View.VISIBLE);

                btnSubmitTable.setText("Join Table");
            }*/

        });

        imgBack.setOnClickListener(v -> {
            if (MyApp.getClickStatus()){
                clickEffect();
                onBackPressed();
            }

        });

        txtTabContest.setOnClickListener(v -> {
            /*if (MyApp.getClickStatus()){
                hideKeyBoard();
                clickEffect();
                layContest.setVisibility(View.VISIBLE);
                layFriend.setVisibility(View.GONE);
                txtTabFriend.setBackgroundResource(R.drawable.transparent_view);
                txtTabContest.setBackgroundResource(R.drawable.selected_ludo_tab);
                txtTabFriend.setTextColor(getResources().getColor(R.color.white));
                txtTabContest.setTextColor(getResources().getColor(R.color.black));
            }*/


        });

       /* txtTabFriend.setOnClickListener(v -> {
            if (MyApp.getClickStatus()){
                hideKeyBoard();
                clickEffect();

                edtCode.setText("");
                edtAmount.setText("");

                layContest.setVisibility(View.GONE);
                layFriend.setVisibility(View.VISIBLE);
                txtTabContest.setBackgroundResource(R.drawable.transparent_view);
                txtTabFriend.setBackgroundResource(R.drawable.selected_ludo_tab);
                txtTabFriend.setTextColor(getResources().getColor(R.color.black));
                txtTabContest.setTextColor(getResources().getColor(R.color.white));
            }

        });

        txtSpot2.setOnClickListener(v -> {
            if (MyApp.getClickStatus()){
                noSpots=2;
                clickEffect();
                txtSpot2.setBackgroundResource(R.drawable.bg_spot_selected);
                txtSpot3.setBackgroundResource(R.drawable.bg_spot_ludo);
                txtSpot4.setBackgroundResource(R.drawable.bg_spot_ludo);
            }

        });

        txtSpot3.setOnClickListener(v -> {
            if (MyApp.getClickStatus()){
                noSpots=3;
                clickEffect();
                txtSpot3.setBackgroundResource(R.drawable.bg_spot_selected);
                txtSpot2.setBackgroundResource(R.drawable.bg_spot_ludo);
                txtSpot4.setBackgroundResource(R.drawable.bg_spot_ludo);
            }

        });

        txtSpot4.setOnClickListener(v -> {
            if (MyApp.getClickStatus()){
                noSpots=4;
                clickEffect();
                txtSpot4.setBackgroundResource(R.drawable.bg_spot_selected);
                txtSpot3.setBackgroundResource(R.drawable.bg_spot_ludo);
                txtSpot2.setBackgroundResource(R.drawable.bg_spot_ludo);
            }

        });*/

        btnSubmitTable.setOnClickListener(v -> {
            if (MyApp.getClickStatus()){
                clickEffect();
                if (btnSubmitTable.getText().toString().trim().equalsIgnoreCase("Create Table")){
                    String amt=edtAmount.getText().toString().trim();
                    if (TextUtils.isEmpty(amt)){
                        Toast.makeText(this, "Enter Amount", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (Long.parseLong(amt)<=0){
                        Toast.makeText(this, "Enter Valid Amount", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getCreateTable(noSpots,amt);
                }
                else if (btnSubmitTable.getText().toString().trim().equalsIgnoreCase("Join Table")){
                    String code=edtCode.getText().toString().trim();
                    if (TextUtils.isEmpty(code)){
                        Toast.makeText(this, "Enter Code", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getJoinTable(code);
                }
            }
        });

        /*btnJoinTable.setOnClickListener(view -> {
            String code=edtCode.getText().toString().trim();
            if (TextUtils.isEmpty(code)){
                Toast.makeText(this, "Enter Code", Toast.LENGTH_SHORT).show();
                return;
            }
            getJoinTable(code);
        });

        btnCreateTable.setOnClickListener(view -> {
            String amt=edtAmount.getText().toString().trim();
            if (TextUtils.isEmpty(amt)){
                Toast.makeText(this, "Enter Amount", Toast.LENGTH_SHORT).show();
                return;
            }

            getCreateTable(noSpots,amt);
        });*/

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

    private void hideKeyBoard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*if (preferences.getUpdateMaster().getUsb_enable().equalsIgnoreCase("yes")) {
            if (CustomUtil.isUSBDebugging(this)) {
                CustomUtil.showDebuggingDialog(this);
                return;
            }

            if (CustomUtil.checkGGOnDevice(this)) {
                CustomUtil.showGGDialog(this);
                return;
            }
            CustomUtil.isAppCloning(this);
        }*/

        isMusic=preferences.getPrefBoolean(PrefConstant.LUDO_MUSIC_KEY);
        if (isMusic){
            MusicManager.getInstance().resumeMusic(this);
        }

        getUserDetail();
        getContestList();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSocket!=null) {
            mSocket.off(ConstantUtil.WAITING_LUDO_USER);
            mSocket.off(ConstantUtil.GET_PLAYER_DETAIL);
            mSocket.off(ConstantUtil.RES);
        }

        isMusic=preferences.getPrefBoolean(PrefConstant.LUDO_MUSIC_KEY);
        if (isMusic){
            MusicManager.getInstance().pauseMusic();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("getContestList","onDestroy");

        isMusic=preferences.getPrefBoolean(PrefConstant.LUDO_MUSIC_KEY);
        if (isMusic){
            MusicManager.getInstance().stopMusic();
        }

        if (mSocket!=null){
            //mSocket.off(ConstantUtil.WAITING_LUDO_USER);
            offEmmiter();
            mSocket.disconnect();
            mSocket=null;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mSocket!=null){
            mSocket.off(ConstantUtil.WAITING_LUDO_USER);
            mSocket.off(ConstantUtil.GET_PLAYER_DETAIL);
        }
        if (mSocket!=null && mSocket.connected()){
            mSocket.disconnect();
        }

        finish();
    }

    public void getTNCData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("game_id", games.getGameCode());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG,jsonObject.toString());

        HttpRestClient.postJSONNormal(LudoContestListActivity.this, true, ApiManager.gamesTncGetFF,
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
                                //   HttpRestClient.logoutUser(LudoContestListActivity.this);
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

    public void getJoinTable(String code) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("con_code",code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG,jsonObject.toString());

        HttpRestClient.postJSONNormal(LudoContestListActivity.this, false, ApiManager.verifyLudoContest,
                jsonObject, new GetApiResult() {
                    @Override
                    public void onSuccessResult(JSONObject responseBody, int code) {
                        LogUtil.e("TAG", "getContestList: " + responseBody.toString());
                        if (responseBody.optBoolean("status")){
                            JSONObject data=responseBody.optJSONObject("data");
                            //startGame(data,"No");
                            showConfirmDialog(data,"No");
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

    private void getCreateTable(int noSpots, String amt) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("spot",noSpots+"");
            jsonObject.put("entry_fee",amt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG,jsonObject.toString());

        HttpRestClient.postJSONNormal(LudoContestListActivity.this, false, ApiManager.createLudoContest,
                jsonObject, new GetApiResult() {
                    @Override
                    public void onSuccessResult(JSONObject responseBody, int code) {
                        LogUtil.e("TAG", "getContestList: " + responseBody.toString());
                        if (responseBody.optBoolean("status")){
                            JSONObject data=responseBody.optJSONObject("data");
                            showCodeDialog(data);
                        }else {
                            Toast.makeText(getApplicationContext(), responseBody.optString("msg"), Toast.LENGTH_SHORT).show();
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

    public void getContestList() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("user_id",preferences.getUserModel().getId());
            jsonObject.put("game_id",games.getGameCode());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG,jsonObject.toString());
        HttpRestClient.postJSONNormal(LudoContestListActivity.this, false, ApiManager.ludoContestList, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("TAG", "getContestList: " + responseBody.toString());

                if (responseBody.optBoolean("status")){
                    tnc=responseBody.optString("tnc");
                    if (ConstantUtil.is_game_test){
                        setContestData(responseBody);
                    }
                    else {
                        JSONObject ludo_update_check_android = responseBody.optJSONObject("ludo_update_check_android");

                        if (ludo_update_check_android.optString("update_type").
                                equalsIgnoreCase("force_update")) {
                            if (CustomUtil.convertInt(ludo_update_check_android.optString("v")) > BuildConfig.VERSION_CODE) {
                                showAppUpdate(ludo_update_check_android);
                            } else {
                                setContestData(responseBody);
                            }
                        } else if (ludo_update_check_android.optString("update_type").
                                equalsIgnoreCase("normal_update")) {
                            if (CustomUtil.convertInt(ludo_update_check_android.optString("v")) > BuildConfig.VERSION_CODE) {
                                showAppUpdate(ludo_update_check_android);
                            }
                            setContestData(responseBody);
                        } else if (ludo_update_check_android.optString("update_type")
                                .equalsIgnoreCase("maintenance")) {
                            showAppMaintance(ludo_update_check_android);
                        } else {
                            setContestData(responseBody);
                        }
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

    private void showAppMaintance(JSONObject data) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.maintance_dialog, null);
        Button btn_ok = view.findViewById(R.id.btnClose);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        txtTitle.setText("Ludo is under maintenance");
        TextView txtSubTitle = view.findViewById(R.id.txtSubTitle);
        txtSubTitle.setText(data.optString("update_message"));
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
        TextView btnBackToHome = view.findViewById(R.id.btnBackToHome);
        TextView app_update_msg = view.findViewById(R.id.app_update_msg);
        TextView txtSubTitle = view.findViewById(R.id.txtSubTitle);
        ImageView imageView3 = view.findViewById(R.id.imageView3);
        ImageView imgClose = view.findViewById(R.id.imgClose);
        progress = view.findViewById(R.id.progress);
        btnBackToHome.setVisibility(View.VISIBLE);

        if (data.optString("update_type").equalsIgnoreCase("force_update")) {
            imgClose.setVisibility(View.GONE);
        }else {
            imgClose.setVisibility(View.VISIBLE);
        }

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        btnUpdate.setOnClickListener(view1 -> {
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

    public void connectSocket(){
        if (mSocket!=null && !mSocket.connected()){
            mSocket.connect();
            /*mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e(ConstantUtil.WAITING_LUDO_USER,"Response:- Connected");
                }
            });
            mSocket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e(ConstantUtil.WAITING_LUDO_USER,"Response:- Error: "+args[0]);
                }
            });*/
        }
    }

    public void getUserToken(View view, GameContestModel contest){
        JSONObject jsonObject = new JSONObject();
        try {
            UserModel userModel=preferences.getUserModel();
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("user_id", userModel.getId());
            jsonObject.put("entry_fee", contest.getEntryFee());
            jsonObject.put("no_of_spot", contest.getNo_spot());
            jsonObject.put("con_id", contest.getId());
            jsonObject.put("dist_amt", contest.getDisAmt());
            jsonObject.put("commission", "");
            jsonObject.put("is_public", "Yes");
            jsonObject.put("header",getHeaderArray());
            jsonObject.put("game_code", games.getGameCode());
            jsonObject.put("game_mode_sub_type", contest.getGame_round());
            jsonObject.put("phone_u_id", MyApp.deviceId);
            jsonObject.put("fcm_id", MyApp.tokenNo);
            jsonObject.put("mobile_os", MyApp.deviceType);
            jsonObject.put("mobile_model_name", MyApp.deviceName);
            jsonObject.put("game_mode", games.getType());
            if (!TextUtils.isEmpty(contest.getUser_token_position()))
                jsonObject.put("other_detials", new JSONArray(contest.getUser_token_position()));
            jsonObject.put("contest_timer", contest.getGame_time());
            //jsonObject.put("user_image_url", getAgeWiseUrl());
            //jsonObject.put("user_avatar",getAgeWiseUrl());//userModel.getLevel3Id()
            jsonObject.put("user_avatar", userModel.getLevel3Id());
            jsonObject.put("game_type", games.getGameCode());
            jsonObject.put("display_name", userModel.getDisplayName());
            jsonObject.put("team_name", userModel.getUserTeamName());
            jsonObject.put("is_auto_game_start",is_auto_game_start);
            jsonObject.put("is_contest_waiting",is_contest_waiting);

            jsonObject.put("high_number", contest.getHigh_number());
            jsonObject.put("low_number", contest.getLow_number());
            jsonObject.put("odd_number", contest.getOdd_number());
            jsonObject.put("even_number", contest.getEven_number());
            jsonObject.put("bomb", contest.getBomb());
            jsonObject.put("rocket", contest.getRocket());
            jsonObject.put("extra_turn", contest.getExtra_turn());
            jsonObject.put("shield", contest.getShield());

            /*jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("entry_fee", contest.getEntryFee());
            jsonObject.put("no_of_spot", contest.getNo_spot());
            jsonObject.put("con_id", contest.getId());
            jsonObject.put("dist_amt", contest.getDisAmt());
            jsonObject.put("is_public", "Yes");
            jsonObject.put("game_code", "0000");
            jsonObject.put("game_mode_sub_type", contest.getGame_round());
            jsonObject.put("phone_u_id", MyApp.deviceId);
            jsonObject.put("fcm_id", MyApp.tokenNo);
            jsonObject.put("mobile_os", MyApp.deviceType);
            jsonObject.put("mobile_model_name", MyApp.deviceName);
            jsonObject.put("game_mode", "Classic");
            jsonObject.put("user_avatar",getAgeWiseUrl());// preferences.getUserModel().getLevel3Id()
            jsonObject.put("game_type", games.getGameCode());
            jsonObject.put("display_name", preferences.getUserModel().getDisplayName());
            jsonObject.put("team_name", preferences.getUserModel().getUserTeamName());
            jsonObject.put("total_round",contest.getGame_round());
            jsonObject.put("raise_detail",new JSONObject("{\"raise\":\"0.01\",\"max\":\"0.1\",\"min\":\"0.05\"}"));*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "getUserToken param: " + jsonObject.toString()+"   "+games.getToken_create_url());
        String url="";
        if (ConstantUtil.is_game_test){
            url=games.getToken_create_url_test();
        }
        else {
            url=games.getToken_create_url();
        }

        if (!TextUtils.isEmpty(url)){
            HttpRestClient.postJSONNormal(LudoContestListActivity.this, true, url, jsonObject, new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {

                    LogUtil.e(TAG, "getUserToken onSuccessResult: " + responseBody.toString());
                    if (responseBody.optInt("code")==1005){
                        otherGamePlayingDialog(responseBody.optJSONObject("data"));
                    }
                    else {
                        if (responseBody.optBoolean("status")) {

                            String utoken = responseBody.optJSONObject("data").optString("utoken");

                            showConfirmDialog(contest, view, utoken);
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
                    view.setEnabled(true);
                }

                @Override
                public void onFailureResult(String responseBody, int code) {
                    view.setEnabled(true);
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

    public void getUserDetail(View view, GameContestModel contest){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("token_no", preferences.getUserModel().getTokenNo());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(LudoContestListActivity.this, true, ApiManager.AUTHV3GetUserDetails, jsonObject, new GetApiResult() {
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
                                + CustomUtil.convertFloat(userModel.getFf_coin()) + CustomUtil.convertFloat(userModel.getBonusBal());*/
                        userModel.setTotal_balance(total);
                        preferences.setUserModel(userModel);
                    }

                    if (games.getAssetType().equalsIgnoreCase("webview") ||
                        games.getGameName().equalsIgnoreCase("Carrom") ||
                            games.getGameName().equalsIgnoreCase("poker") ||
                            games.getType().equalsIgnoreCase("Win3Patti")){ //games.getAssetType().equalsIgnoreCase("ludo") ||
                        view.setEnabled(true);
                        showConfirmDialog(contest,view,"");
                    }
                    else {
                        getUserToken(view,contest);
                    }
                   // showConfirmDialog(contest,view,"");
                }else {
                    if (code==1000){
                        isRejoinGame=false;
                        isLogout=true;
                        if (dialog!=null && dialog.isShowing()){
                            dialog.dismiss();
                        }
                    }
                }
                view.setEnabled(true);
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                view.setEnabled(true);
                //preferences.setUserModel(new UserModel());

            }
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

        HttpRestClient.postJSON(LudoContestListActivity.this, true, ApiManager.AUTHV3GetUserDetails, jsonObject, new GetApiResult() {
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

                       /* float total = CustomUtil.convertFloat(userModel.getDepositBal()) + CustomUtil.convertFloat(userModel.getWinBal())
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

    private void updateApp(JSONObject data){
        /*if (updateDialog!=null && updateDialog.isShowing()){
            return;
        }
        updateDialog=new Dialog(this);
        updateDialog.setContentView(R.layout.ludo_app_update_dialog);
        updateDialog.setCancelable(false);
        TextView txtTitle=updateDialog.findViewById(R.id.txtTitle);

        TextView txtMsg=updateDialog.findViewById(R.id.txtMsg);
        txtMsg.setText(Html.fromHtml(data.optString("update_message")));

        ImageView imgClose=updateDialog.findViewById(R.id.imgClose);

        Button txtDownload=updateDialog.findViewById(R.id.btnDownload);


        if (data.optString("update_type").equalsIgnoreCase("force_update")) {
            txtTitle.setText("App Update");

            imgClose.setVisibility(View.GONE);

            txtDownload.setText("Download");

        } else if (data.optString("update_type").equalsIgnoreCase("normal_update")){
            txtTitle.setText("App Update");

            imgClose.setVisibility(View.VISIBLE);

            txtDownload.setText("Download");

        } else if (data.optString("update_type").equalsIgnoreCase("maintenance")){
            txtTitle.setText("Under Maintenance");

            imgClose.setVisibility(View.GONE);

            txtDownload.setText("Exit");
        }

        txtDownload.setOnClickListener(v -> {
            if (txtDownload.getText().toString().trim().equalsIgnoreCase("Download")){
                Dexter.withActivity((Activity) mContext)
                        .withPermissions(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                        // if (!Environment.isExternalStorageManager()){
                                        if (!Environment.isExternalStorageManager() && !isSettingOpen) {
                                            appUpdateData=data;
                                            isSettingOpen = true;
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                            intent.setData(Uri.parse("package:com.batball11"));
                                            startActivityForResult(intent, 1234);
                                        } else {
                                            AsyncDownload asyncDownload = new AsyncDownload(mContext);
                                            asyncDownload.execute(data.optString("app_url"));
                                        }
                                    }else {
                                        AsyncDownload asyncDownload = new AsyncDownload(mContext);
                                        asyncDownload.execute(data.optString("app_url"));
                                    }
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
            }else {
                updateDialog.dismiss();
                onBackPressed();
            }

        });

        imgClose.setOnClickListener(v -> {
            clickEffect();
            updateDialog.dismiss();
        });

        Window window = updateDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        updateDialog.show();*/
    }

    private void setContestData(JSONObject data){
        JSONArray array = data.optJSONArray("data");
        ArrayList<GameContestModel> list = new ArrayList<GameContestModel>();
        StringBuilder conIds= new StringBuilder();

        JSONArray arrConId=new JSONArray();

        for (int i = 0;i<array.length();i++ ) {
            JSONObject mainObject = array.optJSONObject(i);
            if (mainObject.has("data")){
                JSONArray contestArray = mainObject.optJSONArray("data");
                String title = mainObject.optString("title");
                String subTitle = mainObject.optString("sub_title");
                GameContestModel mainModal = new GameContestModel();
                mainModal.setType(0);
                mainModal.setTitle(title);
                mainModal.setSubTitle(subTitle);
                /*if (ConstantUtil.is_game_test)
                    mainModal.setLb_id("26");
                            jsonObject.put("high_number", contest.getHigh_number());
            jsonObject.put("low_number", contest.getLow_number());
            jsonObject.put("odd_number", contest.getOdd_number());
            jsonObject.put("even_number", contest.getEven_number());
            jsonObject.put("bomb", contest.getBomb());
            jsonObject.put("rocket", contest.getRocket());
            jsonObject.put("extra_turn", contest.getExtra_turn());
            jsonObject.put("shield", contest.getShield());

                    */
                list.add(mainModal);
                for (int k = 0;k<contestArray.length();k++ ) {
                    JSONObject obj = contestArray.optJSONObject(k);
                    GameContestModel modal = new GameContestModel();
                    conIds.append(obj.optString("id")).append(",");

                    modal.setType(1);
                    String entry_fee="",dis_amt="";
                    entry_fee = obj.optString("entry_fee");
                    dis_amt = obj.optString("dis_amt");

                    String no_spot = obj.optString("no_spot");
                    String bonus = obj.optString("bonus");
                    String id = obj.optString("id");
                    String game_time = obj.optString("game_time");
                    String game_round = obj.optString("game_round");
                    String game_use_coin = obj.optString("game_use_coin");

                    if (ConstantUtil.is_game_test){
                        if (games.getAssetType().equalsIgnoreCase("callbreak")){
                            if (id.equalsIgnoreCase("199") || id.equalsIgnoreCase("200")){
                                game_round="4";
                            }
                        }
                    }

                    modal.setHigh_number(obj.optString("high_number"));
                    modal.setShield(obj.optString("shield"));
                    modal.setBomb(obj.optString("bomb"));
                    modal.setRocket(obj.optString("rocket"));
                    modal.setExtra_turn(obj.optString("extra_turn"));
                    modal.setEven_number(obj.optString("even_number"));
                    modal.setOdd_number(obj.optString("odd_number"));
                    modal.setLow_number(obj.optString("low_number"));

                    modal.setPokerNoOfPlaying("0");
                    modal.setEntryFee(entry_fee);
                    modal.setNo_spot(no_spot);
                    modal.setDisAmt(dis_amt);
                    modal.setBonus(bonus);
                    modal.setGame_round(game_round);
                    modal.setGame_use_coin(game_use_coin);
                    modal.setGame_time(game_time);
                    modal.setAsset_type(assetType);
                    //LogUtil.e("resp",assetType);
                   // modal.setIs_pass(is_pass);
                    modal.setId(id);
                    list.add(modal);
                    arrConId.put(id);
                }
            }
        }

        LogUtil.e("conId","ConId: "+conIds);

        if (list.size()>0){
            txtNoData.setVisibility(View.GONE);
            recyclerContest.setVisibility(View.VISIBLE);

            mDataList.clear();
            mDataList.addAll(list);
            mAdapter.notifyDataSetChanged();

        }
        else {
            txtNoData.setVisibility(View.VISIBLE);
            recyclerContest.setVisibility(View.GONE);
        }
        UserModel userModel=preferences.getUserModel();
        if (!TextUtils.isEmpty(socket_url)){
            try {
                IO.Options opts =new IO.Options();
                mSocket = IO.socket(socket_url, opts);

                connectSocket();

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            mSocket.emit(ConstantUtil.WAITING_LUDO_USER,"");

            JSONObject usrId=new JSONObject();
            try {
                usrId.put("user_id",userModel.getId());
                usrId.put("token_no",userModel.getTokenNo());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            isRejoinGame=true;
            mSocket.emit(ConstantUtil.GET_PLAYER_DETAIL,usrId.toString());

            if (games.getGameName().equalsIgnoreCase("poker") || games.getType().equalsIgnoreCase("Win3Patti")){
                try {
                    JSONObject data1=new JSONObject();
                    JSONObject main=new JSONObject();
                    data1.put("userId",userModel.getId());
                    data1.put("user_id",userModel.getId());
                    data1.put("token_no",userModel.getTokenNo());
                    data1.put("con_id",arrConId);
                    data1.put("comp_id",ConstantUtil.COMPANY_ID);
                    main.put("data",data1);
                    main.put("comp_id",ConstantUtil.COMPANY_ID);
                    main.put("en",ConstantUtil.CONTAST_PLAYER_COUNT);

                    LogUtil.e("resp","PokerEmitData: "+main);

                    mSocket.emit(ConstantUtil.REQ,main);

                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(() -> {
                        try {
                            main.put("en","getPlayerDetails");
                            mSocket.emit(ConstantUtil.REQ,main);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }, 500);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            listenWaiting();
        }
    }

    private void showCodeDialog(JSONObject data) {
        /*Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.code_dialog);
        dialog.setCancelable(false);
        TextView txtCode=dialog.findViewById(R.id.txtCode);
        txtCode.setText(data.optString("game_con_code"));
        Button txtShare=dialog.findViewById(R.id.txtShare);
        Button txtPlay=dialog.findViewById(R.id.txtPlay);
        TextView txtEntry=dialog.findViewById(R.id.txtEntry);
        TextView txtWin=dialog.findViewById(R.id.txtWin);
        TextView txtSpot=dialog.findViewById(R.id.txtSpot);
        ImageView imgClose=dialog.findViewById(R.id.imgClose);

        txtEntry.setText(getResources().getString(R.string.rs)+data.optString("game_price"));
        txtWin.setText(getResources().getString(R.string.rs)+data.optString("game_win_amount"));
        txtSpot.setText(data.optString("game_no_spot"));

        txtPlay.setOnClickListener(view -> {
            if (MyApp.getClickStatus()){
                dialog.dismiss();
                clickEffect();
                startGame(data,"Yes");
                //showConfirmDialog(data,"Yes");
            }
        });

        txtShare.setOnClickListener(view -> {
            if (MyApp.getClickStatus()){
                clickEffect();
                String content="https://www.batball11.com\n\n"+
                        "I have challenged you on the BB11 Ludo game.\n\n"+
                        "*Code* ⏳ : *"+data.optString("game_con_code")+"*"+
                        "\n\n*Prize Pool*\uD83D\uDCB0 : "+getResources().getString(R.string.rs)+data.optString("game_win_amount")+"\n"+
                        "*Entry Fee*\uD83D\uDCB6 : "+getResources().getString(R.string.rs)+ data.optString("game_price")+"\n"+
                        "\nTap below link for join:" +
                        "\n\n*For Android*\uD83D\uDCF2"+
                        "\nhttps://batball11.com/ludo/"+data.optString("game_con_code")+
                        "\n\n*For IPhone*\uD83D\uDCF2" +
                        "\nbb11ludo://ludo/"+data.optString("game_con_code");


                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,content
                    *//*"Hey your friend invited to play ludo\nCode:"+data.optString("game_con_code")+
                            "\nNumber of Spot:"+data.optString("game_no_spot")+
                            "\nPrize Pool:"+data.optString("game_win_amount")+"\nEntry Fees:"+
                            data.optString("game_price")*//*);

                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }

        });

        imgClose.setOnClickListener(v -> {
            if (MyApp.getClickStatus()){
                clickEffect();
                dialog.dismiss();
            }

        });

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();*/

        /*{"status":true,"data":{"user_id":"05ebfeb231c69ce5","game_con_code":4522,"game_no_spot":"2","game_price":"26","game_use_bonus":"0","game_profit_amt":11,"game_win_amount":41,"game_total_winner":"1","game_status":"Pending","create_at":"2021-12-11 09:50:52","id":56},"msg":"Contest code created successfully.","code":"200"}*/
    }

    private void showConfirmDialog(JSONObject data,String is_creator){
        /*Dialog dialog = new Dialog(this);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true);
        //dialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.confirm_dialog);

        Button imgYes = dialog.findViewById(R.id.btnYes);
        Button imgNo = dialog.findViewById(R.id.btnNo) ;
        TextView txtEntry = dialog.findViewById(R.id.txtEntry) ;
        TextView txtWin = dialog.findViewById(R.id.txtWin);

        TextView txtCondition = dialog.findViewById(R.id.txtCondition);
        txtCondition.setText(tnc);//"◈ "+

        txtEntry.setText(getResources().getString(R.string.rs)+data.optString("game_price"));
        txtWin.setText(getResources().getString(R.string.rs)+data.optString("game_win_amount"));

        dialog.setOnDismissListener(dialog1 -> {
        });

        imgNo.setOnClickListener(v ->  {
            if (MyApp.getClickStatus()){
                clickEffect();
                dialog.dismiss();
            }
        });

        imgYes.setOnClickListener(v -> {
            if (MyApp.getClickStatus()){
                if (!isWalletValid(data.optString("game_price"))){
                    return;
                }
                dialog.dismiss();
                clickEffect();
                startGame(data,is_creator);
            }


        });
        dialog.show();*/
    }

    private void showConfirmDialog(GameContestModel contest,View view,String utoken){

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

            if (!games.getAssetType().equalsIgnoreCase("webview")){
                try {
                    //game = (GameMainModal.Datum.Game) getIntent().getSerializableExtra("gameData");

                    String path =
                            CustomUtil.getAppDirectory(this) + assetType;
                    String downloadUrl=path + getFileNameFromUrl(asset_url_android);//ConstantUtil.GAME_ASSETS_NAME;

                    Log.e("appDir", "downloadUrl: "+downloadUrl+"\nasset_url_android: "+asset_url_android);
                    File filePath=new File(path);
                    File file=new File(downloadUrl);

                    boolean isCompletes=preferences.getPrefBoolean(games.getAssetType()+ PrefConstant.DOWNLOAD_STATUS);
                    if (!isCompletes){
                        if (file.exists()){
                            file.delete();
                        }
                    }

                    Log.e("appDir", "Path: "+path+"\n"+downloadUrl);

                    if (!filePath.exists()){
                        filePath.mkdirs();
                    }

                    if (!file.exists()){

                        layAsset.setVisibility(View.VISIBLE);
                        txtYes.setVisibility(View.GONE);
                        mBottomSheetDialog.setCancelable(false);

                        preferences.setPref(games.getAssetType()+PrefConstant.DOWNLOAD_STATUS,false);

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
                                                        if (!children[i].contains(getFileNameFromUrl(asset_url_android))){
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
                                                    new DownloadFileFromURL(downloadUrl,txtProgress,progress,mBottomSheetDialog,
                                                            txtYes,layAsset)
                                                            .execute(asset_url_android);
                                                    //showAssetsDownload(downloadUrl,asset_url_android);
                                                }
                                            }else {
                                                new DownloadFileFromURL(downloadUrl,txtProgress,progress,mBottomSheetDialog,txtYes,layAsset)
                                                        .execute(asset_url_android);
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
            }
            else {
                mBottomSheetDialog.setCancelable(true);
                layAsset.setVisibility(View.GONE);
                txtYes.setVisibility(View.VISIBLE);
            }

            txtYes.setOnClickListener(v -> {
                mBottomSheetDialog.dismiss();
                try {
                    UserModel user=preferences.getUserModel();

                    if (games.getAssetType().equalsIgnoreCase("webview")) {
                        JSONObject object=new JSONObject();
                        object.put("con_id",contest.getId());
                        object.put("socketURL",socket_url.replaceAll("\\\\",""));//MyApp.getInstance().Ludo_Socket.replaceAll("\\\\","")
                        object.put("asset_path",CustomUtil.getAppDirectory(this) + assetType + getFileNameFromUrl(asset_url_android));
                        object.put("game_type_name",games.getType());
                        object.put("rules",tnc);
                        object.put("utoken",utoken);
                        object.put("game_name",games.getGameName());
                        object.put("winning_amount",contest.getDisAmt());
                        //object.put("user_avatar",getAgeWiseUrl());//user.getLevel3Id()
                        object.put("user_avatar", user.getLevel3Id());
                        object.put("no_of_spot",contest.getNo_spot());
                        object.put("user_id",user.getId());
                        object.put("user_name",user.getUserTeamName());
                        object.put("user_team_name",user.getDisplayName());
                        object.put("play_with_friend","No");
                        object.put("is_creator","No");
                        object.put("game_play_code","");
                        object.put("entry_fee",contest.getEntryFee());
                        object.put("contest_timer",contest.getGame_time());
                        object.put("comp_id",ConstantUtil.COMPANY_ID);

                        object.put("total_round",contest.getGame_round());

                        Log.e("userData",object.toString()+"  userdata");

                        if (mSocket!=null)
                            mSocket.emit(ConstantUtil.WAITING_LUDO_USER,object);

                        Intent intent=new Intent(mContext,WebViewGameActivity.class) .putExtra("gameData",games)
                                .putExtra("con_id",contest.getId());

                        someActivityResultLauncher.launch(intent);

                        offEmmiter();

                    }
                    else {
                        JSONObject object=new JSONObject();

                        if (games.getGameName().equalsIgnoreCase("poker")  || games.getType().equalsIgnoreCase("Win3Patti")){
                            JSONObject pokerObj=new JSONObject();

                            UserModel userModel=preferences.getUserModel();
                            float totalBal=CustomUtil.convertFloat(userModel.getWinBal())+CustomUtil.convertFloat(userModel.getDepositBal())
                                    +CustomUtil.convertFloat(userModel.getTransferBal()) +CustomUtil.convertFloat(userModel.getFf_coin());

                            float nospot=CustomUtil.convertFloat(contest.getNo_spot());
                            float gameround=CustomUtil.convertFloat(contest.getGame_round());

                            pokerObj.put("con_id",contest.getId());//
                            pokerObj.put("joinTable_amount",0);//totalBal+

                         /*   int entry=CustomUtil.convertInt(contest.getEntryFee());
                            if (totalBal>entry*10){
                                total=String.valueOf(entry*10);
                            }
                            else {
                                total=String.valueOf(totalBal);
                            }*/

                            pokerObj.put("current_balance",CustomUtil.convertDouble(totalBal+""));
                            pokerObj.put("stakes_amount",nospot/gameround);//contest.getPokerMinBuyIn()
                            pokerObj.put("minBuyIn",CustomUtil.convertInt(contest.getPokerMinBuyIn()));
                            pokerObj.put("userId",userModel.getId());
                            //pokerObj.put("user_avatar",getAgeWiseUrl());//userModel.getLevel3Id()
                            pokerObj.put("user_avatar", userModel.getLevel3Id());
                            pokerObj.put("user_team_name",userModel.getUserTeamName());
                            pokerObj.put("user_Id",contest.getPokerUserId());
                            pokerObj.put("tableId",contest.getPokerTableId());
                            pokerObj.put("commission",CustomUtil.convertInt(contest.getGame_profit_amt()));
                            pokerObj.put("small_blind",CustomUtil.convertInt(contest.getNo_spot()));
                            pokerObj.put("big_blind",CustomUtil.convertInt(contest.getGame_round()));
                            pokerObj.put("isplayerrejoin",false);
                            pokerObj.put("comp_id",ConstantUtil.COMPANY_ID);
                            object.put("poker_temporary_object",pokerObj);
                            object.put("header",getHeaderArray());

                        }

                        object.put("comp_id",ConstantUtil.COMPANY_ID);
                        object.put("winning_amount",contest.getDisAmt());
                        object.put("token_generate_url",token_generate_url);
                        //object.put("header",getHeaderArray());
                        object.put("asset_path",CustomUtil.getAppDirectory(this) + assetType + getFileNameFromUrl(asset_url_android));
                        object.put("game_type_name",games.getType());
                        object.put("game_mode","");
                        if (games.getType().equalsIgnoreCase("power")){
                            object.put("game_type_name","Classic");
                            object.put("game_mode",games.getType());
                        }

                        if (ConstantUtil.is_game_test){
                            if (!TextUtils.isEmpty(edtSocketUrl.getText().toString())){
                                object.put("socketURL",edtSocketUrl.getText().toString());
                            }else {
                                object.put("socketURL",socket_url.replaceAll("\\\\",""));
                            }
                        }else {
                            object.put("socketURL",socket_url.replaceAll("\\\\",""));
                        }

                        object.put("play_with_friend","No");
                        object.put("utoken",utoken);

                        String ludoType = "";
                        if (!TextUtils.isEmpty(games.getGameLabel()) && games.getGameLabel().equalsIgnoreCase("speedPointLudo")){
                            ludoType = games.getGameLabel();
                        }
                        object.put("ludoType",ludoType);

                        Log.e("userData",object.toString()+"  userdata");
                        launchUnity(object);
                        /*offEmmiter();

                        startActivity(new Intent(this, UnityPlayerActivity.class)
                                .putExtra("userData",object.toString()));*/
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            if (mBottomSheetDialog != null && !mBottomSheetDialog.isShowing())
                mBottomSheetDialog.show();
        }
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

    private void offEmmiter(){
        if (mSocket!=null)
            mSocket.off(ConstantUtil.WAITING_LUDO_USER);
        mSocket.off(ConstantUtil.GET_PLAYER_DETAIL);

     /*   mSocket.off(Socket.EVENT_CONNECT);
        mSocket.off(Socket.EVENT_CONNECT_ERROR);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data1) {
        super.onActivityResult(requestCode, resultCode, data1);
       /* if (requestCode==1234){
            if (appUpdateData.has("app_url")){
                AsyncDownload asyncDownload = new AsyncDownload(mContext);
                asyncDownload.execute(appUpdateData.optString("app_url"));
            }
        }*/
        if (requestCode==2242){
            if (checkWriteExternalPermission()){
                String path = CustomUtil.getAppDirectory(this) + assetType;
                String downloadUrl=path+getFileNameFromUrl(asset_url_android);//ConstantUtil.GAME_ASSETS_NAME;
                new DownloadFileFromURL(downloadUrl,txtProgress,progress,mBottomSheetDialog,
                        txtYes,layAsset)
                        .execute(asset_url_android);
            }else {
                onBackPressed();
            }
        }
    }

    private boolean checkWriteExternalPermission() {
        String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = mContext.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    @Override
    public void onItemClick(View view, GameContestModel contest) {
        if (!assetType.equalsIgnoreCase("poker/") ){
            if (!assetType.equalsIgnoreCase("Win3Patti/"))
                getUserDetail(view,contest);
            else {
                view.setEnabled(true);
                submitPokerData(contest,view);
            }
        }else {
            view.setEnabled(true);
            submitPokerData(contest,view);
        }

        /*if (MyApp.getClickStatus()){
            CustomUtil.checkInternet(mContext,()->new Thread(() ->{
                runOnUiThread(()->{
                    clickEffect();
                    *//*if (!isWalletValid(contest.getEntry_fee())){
                        view.setEnabled(true);
                        return;
                    }*//*

                    //showConfirmDialog(contest,view);
                });
            }).start());
        }*/
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

    private void submitPokerData(GameContestModel contest,View view) {
        if (mSocket!=null && mSocket.connected()){
            try {
                UserModel userModel=preferences.getUserModel();

                float totalBal=CustomUtil.convertFloat(userModel.getWinBal())+CustomUtil.convertFloat(userModel.getDepositBal());

                float nospot=CustomUtil.convertFloat(contest.getNo_spot());
                float gameround=CustomUtil.convertFloat(contest.getGame_round());

                JSONObject main=new JSONObject();
                JSONObject data=new JSONObject();
                data.put("userId",userModel.getId());
                data.put("con_id",contest.getId());
                data.put("userName",userModel.getUserTeamName());
                data.put("profileURL",userModel.getLevel3Id());//getAgeWiseUrl()
                data.put("user_avatar", userModel.getLevel3Id());
                data.put("small_blind",contest.getNo_spot());
                data.put("big_blind",contest.getGame_round());
                data.put("comp_id",ConstantUtil.COMPANY_ID);
                data.put("commission",CustomUtil.convertInt(contest.getGame_profit_amt()));

                data.put("comp_id",ConstantUtil.COMPANY_ID);
                //data.put("reconnection",false);
                data.put("stakes_amount",nospot/gameround);//contest.getEntryFee()
                //data.put("playerChipsAtJoinTable",totalBal);
                main.put("en",ConstantUtil.SIGNUP);
                main.put("comp_id",ConstantUtil.COMPANY_ID);
                main.put("data",data);
                LogUtil.e("resp",main+"  ");
                mSocket.emit(ConstantUtil.REQ,main.toString());

                showPokerTableList(contest,view);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showPokerTableList(GameContestModel contest,View btn){
        pokerTableModelArrayList = new ArrayList();

        View view = LayoutInflater.from(mContext).inflate(R.layout.poker_table_dialog, null);

        TextView txtTitle=view.findViewById(R.id.txtTitle);
        txtTitle.setText(games.getGameName()+" Live Tables");
        //txtWinAmt.setText(contest.getNo_spot()+"/"+contest.getGame_round());
        //TextView btnEntry=view.findViewById(R.id.btnEntry);
        //btnEntry.setText(contest.getEntryFee());
        //txtOnlineCnt=view.findViewById(R.id.txtOnlineCnt);
        ImageView imgClose=view.findViewById(R.id.imgClose);
        //txtOnlineCnt.setText(contest.getPokerNoOfPlaying()+" Playing");
        recyclerTable=view.findViewById(R.id.recyclerTable);

        recyclerTable.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        pokerTableAdapter=new PokerTableAdapter(mContext, pokerTableModelArrayList, new PokerTableAdapter.onTableClick() {
            @Override
            public void onClick(PokerTableModel model) {
                tableListDialog.dismiss();
                contest.setPokerTableId(model.getTable_id());
                contest.setPokerUserId(model.getUser_id());
                contest.setPokerStackAmt(model.getAvgStack());
                contest.setPokerMinBuyIn(model.getMin_buy_in());
                getUserDetail(btn,contest);
            }
        });
        recyclerTable.setAdapter(pokerTableAdapter);

        tableListDialog = new BottomSheetDialog(mContext);
        tableListDialog.setContentView(view);
        //tableListDialog.setCancelable(false);

        imgClose.setOnClickListener(view1 -> {
            tableListDialog.dismiss();
        });

        ((View) view.getParent()).setBackgroundResource(android.R.color.white);
        if (!((Activity) mContext).isFinishing()){
            tableListDialog.show();
        }

        setupFullHeight(tableListDialog);

        /*mSocket.off(ConstantUtil.RES);
        mSocket.on(ConstantUtil.RES,args -> {
            LogUtil.e("resp",args[0]+"  ");
            if (args[0] != null){
                try {
                    JSONObject object=new JSONObject(args[0].toString());
                    if (object.has("data")){
                        JSONObject data=object.optJSONObject("data");
                        if (data!=null){
                            JSONArray selectTable=data.optJSONArray("selectTable");
                            JSONObject userData=data.optJSONObject("userData");

                            for (int i = 0; i < selectTable.length(); i++) {
                                JSONObject obj=selectTable.optJSONObject(i);
                                PokerTableModel model=new PokerTableModel();
                                model.setStakes_amount(obj.optString("stakes_amount"));
                                model.setAvgStack(obj.optString("avgStakes"));
                                model.setUser_id(userData.optString("_id"));
                                model.setMin_buy_in(obj.optString("minBuyIn"));
                                model.setTable_id(obj.optString("_id"));
                                model.setTotal_player(obj.optString("maxSeat"));
                                model.setWaiting_player(obj.optString("totalPlayers"));
                                pokerTableModelArrayList.add(model);
                            }

                            runOnUiThread(() -> {
                                if (tableListDialog!=null && tableListDialog.isShowing() && pokerTableAdapter!=null){
                                    pokerTableAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });*/

    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
       /* FrameLayout bottomSheet =  bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);*/
    }

    private int getWindowHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /*
     android:process=":unity"
     android:exported="true"

        public void callJavaMethod(String tag) {
            if (tag.equalsIgnoreCase("closeUnity")){
                Intent intent=getIntent();
                intent.putExtra("isRejoinGame","no");
                setResult(RESULT_OK,intent);
                finish();
                //mUnityPlayer.quit();
            }else if (tag.equalsIgnoreCase("addcash")){
                Intent intent=getIntent();
                intent.putExtra("isDepositAdd","yes");
                setResult(RESULT_OK,intent);
                finish();
            }
        }

     public void callJavaMethod(String tag) {

        if (tag.equalsIgnoreCase("closeUnity")){
            Intent intent=getIntent();
            intent.putExtra("isRejoinGame","no");
            setResult(RESULT_OK,intent);
            finish();
        }
     }

    public void onBackPressed()
    {
        // instead of calling UnityPlayerActivity.onBackPressed() we just ignore the back button event
        // super.onBackPressed();
    }
    */

}