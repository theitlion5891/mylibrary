package com.fantafeat.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fantafeat.Adapter.AvatarAdapter;
import com.fantafeat.Adapter.GameListAdapter;
import com.fantafeat.Adapter.GameOfferAdapter;
import com.fantafeat.Adapter.OfferBannerAdapter;
import com.fantafeat.BuildConfig;
import com.fantafeat.Fragment.CricketHomeFragment;
import com.fantafeat.Model.AvatarModal;
import com.fantafeat.Model.GameMainModal;
import com.fantafeat.Model.Games;
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
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class GameListActivity extends BaseActivity {

    private ImageView toolbar_wallet,imgBack;
    private PopupWindow popupWindow;
    private LinearLayout layBanner;
    private RelativeLayout bac_dim_layout;
    private CircleImageView imgProfile;
    private CardView mainWallet;
    private RecyclerView recyclerGames;
    private Animation slideUp;
    private Animation slideDown;
    private ViewPager2 mHomeOffer;
    private Timer timerTopAds;
    private int pageTopAds=0;
    private GameOfferAdapter gameOfferAdapter;
    private GameListAdapter gameListAdapter;
    private ArrayList<Games> gameList;
    private List<Games> gameOfferList;
    private String game_id="";
    //private Socket mSocket= null;
    private  String selectedAvatar="avatar1";
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
            float available_bal;
            if (ConstantUtil.is_bonus_show){
                available_bal = deposit_bal + winning_bal /*+ borrowed_bal*/ + reward_bal + coin_bal;
                layBonus.setVisibility(View.VISIBLE);
            }else {
                available_bal = deposit_bal + winning_bal /*+ borrowed_bal*/ + coin_bal;
                layBonus.setVisibility(View.GONE);
            }
            //float available_bal = deposit_bal + winning_bal  + reward_bal+coin_bal;

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

        imgBack.setOnClickListener(v -> {
            if (MyApp.getClickStatus()){
               // clickEffect();
                onBackPressed();
            }
        });

        imgProfile.setOnClickListener(v -> {
            if (MyApp.getClickStatus()){
                /*clickEffect();
                showPopup(v);*/

                showNameDialog();
            }

        });


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTransparent();
        setContentView(R.layout.activity_game_list);

        slideDown = AnimationUtils.loadAnimation(mContext,
                R.anim.slide_down_wallet);
        slideUp = AnimationUtils.loadAnimation(mContext,
                R.anim.slide_up);

        if (getIntent().hasExtra("game_id")){
            game_id=getIntent().getStringExtra("game_id");
        }

        initData();
        initClick();
    }
    @Override
    protected void onResume() {
        super.onResume();
        /*mSocket = MyApp.getInstance().getSocketInstance();
        if (mSocket!=null){
            if (!mSocket.connected())
                mSocket.connect();
            try {
                JSONObject obj=new JSONObject();
                if (preferences.getUserModel()!=null){
                    obj.put("user_id",preferences.getUserModel().getId());
                }
                obj.put("title","gamezone");
                mSocket.emit("connect_user",obj);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }*/
    }

    private void initData() {
        imgBack = findViewById(R.id.toolbar_back);
        toolbar_wallet = findViewById(R.id.toolbar_wallet);
        bac_dim_layout = findViewById(R.id.bac_dim_layout);
        mHomeOffer = findViewById(R.id.home_offer);
        recyclerGames = findViewById(R.id.recyclerGames);
        layBanner = findViewById(R.id.layBanner);
        imgProfile = findViewById(R.id.imgProfile);

        gameList=new ArrayList<>();

        gameOfferList=new ArrayList<>();


        if (TextUtils.isEmpty(preferences.getUserModel().getLevel3Id())){
            //preferences.setPref(PrefConstant.LUDO_AVATAR_KEY,selectedAvatar);
            showNameDialog();
        }
        else {
            selectedAvatar=preferences.getUserModel().getLevel3Id();
            CustomUtil.setAvatar(selectedAvatar,imgProfile);
        }

        getGameList();

    }

    private void showNameDialog() {

        ArrayList<AvatarModal> listAvatar  = CustomUtil.getAvatarList();

        for (int i=0;i<listAvatar.size();i++) {
            if (listAvatar.get(i).getId().equals(selectedAvatar)) {
                listAvatar.get(i).setCheck(true);
            }
        }
        Dialog searchingDialog = new Dialog(this);
        searchingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        searchingDialog.setCancelable(false);
        searchingDialog.setContentView(R.layout.avatar_dialog);
        //val edtName = searchingDialog.findViewById<EditText>(R.id.activity_sign_up_name_et)
        Button submit = searchingDialog.findViewById(R.id.btnChoose);
        ImageView imgClose = searchingDialog.findViewById(R.id.imgClose);
        AvatarAdapter adapter = new AvatarAdapter(this, listAvatar);
        RecyclerView recyclerAvatar = searchingDialog.findViewById(R.id.recyclerAvatar);
        recyclerAvatar.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerAvatar.setAdapter(adapter);
        submit.setOnClickListener(  v ->{
            if (MyApp.getClickStatus()){
                for (int i=0;i<listAvatar.size();i++) {
                    if (listAvatar.get(i).isCheck()) {
                        selectedAvatar = listAvatar.get(i).getId();
                    }
                }

                updateProfile(selectedAvatar);
                //preferences.setPref(PrefConstant.LUDO_AVATAR_KEY,selectedAvatar);

                CustomUtil.setAvatar(selectedAvatar,imgProfile);

                searchingDialog.dismiss();

            }
        });

        imgClose.setOnClickListener(v -> {
            if (MyApp.getClickStatus()){
                searchingDialog.dismiss();
            }

        });

        // Making background transparent
        if (searchingDialog.getWindow() != null) {
            searchingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            searchingDialog.getWindow().setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
            );
        }
        searchingDialog.show();
    }

    private void updateProfile(String avatar) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("level_3_id", avatar);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.AUTHV3UpdateUserDetails, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());

                    UserModel userModel = preferences.getUserModel();
                    userModel.setLevel3Id(avatar);
                    preferences.setUserModel(userModel);

                    //CustomUtil.showTopSneakSuccess(mContext, responseBody.optString("msg"));

                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void checkGameStart(Games game){
        if (!ConstantUtil.is_game_test){
            if (game.getIsMaintanance().equalsIgnoreCase("yes")){
                CustomUtil.showTopSneakWarning(this,game.getMaintananceMsg());
                return;
            }
            if (game.getAndroidUpdateType().equalsIgnoreCase("upcoming")){
                CustomUtil.showTopSneakWarning(this,game.getAndroidUpdateMsg());
            }
            else {
                if (!game.getGameCode().equalsIgnoreCase("121")){
                    if (game.getGameCode().equalsIgnoreCase("104")){
                        startActivity(new Intent(mContext,LudoTournamentActivity.class)
                                .putExtra("gameData",game)
                                .putExtra("allGamesList",gameList));
                    }else {
                        startActivity(new Intent(this,LudoContestListActivity.class)
                                .putExtra("gameData",game)
                                .putExtra("allGamesList",gameList));
                    }
                }
                else {
                    startActivity(new Intent(this,RummyContestActivity.class)//SubGameTypeActivity
                            .putExtra("gameData",game)
                            .putExtra("allGamesList",gameList));
                }
            }
        }
        else {
            if (!game.getGameCode().equalsIgnoreCase("121")){
                if (game.getGameCode().equalsIgnoreCase("104")){
                    startActivity(new Intent(mContext,LudoTournamentActivity.class)
                            .putExtra("gameData",game)
                            .putExtra("allGamesList",gameList));
                }else {
                    startActivity(new Intent(this,LudoContestListActivity.class)
                            .putExtra("gameData",game)
                            .putExtra("allGamesList",gameList));
                }
            }
            else {
                LogUtil.e("qwerty","Rummy=>"+game.getGameName());
                startActivity(new Intent(this,RummyContestActivity.class)//SubGameTypeActivity
                        .putExtra("gameData",game)
                        .putExtra("allGamesList",gameList));
            }
        }
    }
    public void pageSwitcher() {
        timerTopAds = new Timer();
        timerTopAds.scheduleAtFixedRate(new TopAdsAutoScroll(), 0, 5 * 1000);
    }
    public void getGameList() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("user_id",preferences.getUserModel().getId());
            jsonObject.put("comp_id",ConstantUtil.COMPANY_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG,jsonObject.toString());

        HttpRestClient.postJSONNormal(mContext, true, ApiManager.gameList, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("TAG", "getGameList: " + responseBody.toString());
                if (responseBody.optBoolean("status")){
                    JSONArray data=responseBody.optJSONArray("data");
                    if (data!=null && data.length()>0){
                        gameList.clear();
                        for (int i=0;i<data.length();i++){
                            JSONObject obj=data.optJSONObject(i);
                            Games games=gson.fromJson(obj.toString(),Games.class);
                           /* if (games.getGameName().equalsIgnoreCase("Speed Ludo")){
                                games.setNo_token_win(1);
                            }else {
                                games.setNo_token_win(0);
                            }*/
                            gameList.add(games);
                        }
                        Collections.sort(gameList,new prizePoolUp());

                        recyclerGames.setLayoutManager(new GridLayoutManager(mContext,2));
                        gameListAdapter=new GameListAdapter(mContext, gameList, game -> {
                            if (MyApp.getClickStatus())
                                checkGameStart(game);
                        });
                        recyclerGames.setAdapter(gameListAdapter);

                        if (!TextUtils.isEmpty(game_id)){
                            for (int i = 0; i < gameList.size(); i++) {
                                Games games=gameList.get(i);
                                if (games.getId().equalsIgnoreCase(game_id)){
                                    game_id="";
                                    checkGameStart(games);
                                }
                            }
                        }
                    }

                    JSONArray banner=responseBody.optJSONArray("banner");
                    if (banner!=null && banner.length()>0){
                        layBanner.setVisibility(View.VISIBLE);
                        gameOfferList.clear();
                        for (int i=0;i<banner.length();i++){
                            JSONObject obj=banner.optJSONObject(i);
                            Games games=gson.fromJson(obj.toString(),Games.class);
                            gameOfferList.add(games);
                        }
                        //gameOfferAdapter.notifyDataSetChanged();
                        gameOfferAdapter = new GameOfferAdapter(mContext, gameOfferList, game -> checkGameStart(game));
                        mHomeOffer.setAdapter(gameOfferAdapter);
                        mHomeOffer.setClipChildren(false);
                        mHomeOffer.setOffscreenPageLimit(2);
                        mHomeOffer.setPageTransformer(new MarginPageTransformer(20));

                        pageSwitcher();

                    }else {
                        layBanner.setVisibility(View.GONE);
                    }
                }

            }
            @Override
            public void onFailureResult(String responseBody, int code) {}
        });

    }
    public class prizePoolUp implements Comparator<Games> {
        @Override
        public int compare(Games o1, Games o2) {
            return Float.compare(CustomUtil.convertFloat(o1.getOrderId()), CustomUtil.convertFloat(o2.getOrderId()));
        }
    }
    private class TopAdsAutoScroll extends TimerTask {
        @Override
        public void run() {
            ((Activity) mContext).runOnUiThread(() -> {
                if (pageTopAds > gameOfferList.size() - 1) {
                    pageTopAds = 0;
                } else {
                    mHomeOffer.setCurrentItem(pageTopAds++);
                }
            });
        }
    }
}