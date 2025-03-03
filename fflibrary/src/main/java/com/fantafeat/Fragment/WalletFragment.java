package com.fantafeat.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fantafeat.Activity.AddDepositActivity;
import com.fantafeat.Activity.AffiliationActivity;
import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.Activity.InstantKycActivity;
import com.fantafeat.Activity.RequestStatementActivity;
import com.fantafeat.Activity.TransactionListActivity;
import com.fantafeat.Activity.UpdateKYCActivity;
import com.fantafeat.Activity.WithdrawActivity;
import com.fantafeat.BuildConfig;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.Model.StateModal;
import com.fantafeat.Model.UpdateModel;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.FragmentUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.PrefConstant;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class WalletFragment extends BaseFragment {

    private TextView total_balance,user_bal_winning,user_bal_deposit,user_bal_rewards,txtAddAmt,btnDeposit,txtWithdraw,amtTool,txtFantaGems,
            toolWinning,toolBonus,txtWinDetail,btnRefer,txtFooter,txtFooterLinkWeb/*,user_bal_coin*/;
    float available_bal = 0, deposit_bal = 0, winning_bal = 0, /*borrowed_bal = 0,*/ reward_bal = 0, donation_bal = 0,coin_bal=0;
    LinearLayout user_bal_deposit_btn, user_bal_winning_btn, user_bal_borrowed_btn,
            user_bal_rewards_btn, user_bal_donation_btn,transaction_view_more,kyc_detail,layBonus,
            update_kyc,statement_request,send_money,my_referral,transfer_btn,affiliation;
    //private ImageView toolbar_menu;
    private ImageView drawer_image;
    private View viewafl;
    private SwipeRefreshLayout swipeRefresh;
    //private Socket mSocket= null;



    public static WalletFragment newInstance() {
        return new WalletFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_wallet, container, false);
        initFragment(view);
        //getUserDetails(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setStatusBarDark();
        Fragment current = FragmentUtil.fragmentStack.peek();
        if (current instanceof WalletFragment) {
            //new Handler().postDelayed(() -> UpdatePayment(),300);
            getUserDetails(true);
            /*if(MyApp.getMyPreferences().getPrefBoolean(PrefConstant.PAYMENT_SUCCESS)){
                MyApp.getMyPreferences().setPref(PrefConstant.PAYMENT_SUCCESS,false);
                getUserDetails(true);
            }else {
                UpdatePayment();
            }*/
        }

        /*mSocket = MyApp.getInstance().getSocketInstance();
        if (mSocket!=null){
            if (!mSocket.connected())
                mSocket.connect();
            try {
                JSONObject obj=new JSONObject();
                if (preferences.getUserModel()!=null){
                    obj.put("user_id",preferences.getUserModel().getId());
                }
                obj.put("title","wallet");
                mSocket.emit("connect_user",obj);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }*/
    }

    private void getUserDetails(boolean isProgress) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("token_no", preferences.getUserModel().getTokenNo());
            //Log.e(TAG, "getUserDetails: " + jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, isProgress, ApiManager.AUTHV3GetUserDetails, jsonObject, new GetApiResult() {
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
                UpdatePayment();
              //  mUserDetail = true;
            }
            @Override
            public void onFailureResult(String responseBody, int code) {
                Log.e(TAG, "onFailureResult: " );
                UpdatePayment();
               // mUserDetail = true;
              // preferences.setUserModel(new UserModel());

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
       // setStatusBarRed();
    }

    private void UpdatePayment() {
        if (swipeRefresh!=null && swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
        preferences = MyApp.getMyPreferences();
        deposit_bal = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        winning_bal = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
        //borrowed_bal = CustomUtil.convertFloat(preferences.getUserModel().getTransferBal());
        reward_bal = CustomUtil.convertFloat(preferences.getUserModel().getBonusBal());
        donation_bal = CustomUtil.convertFloat(preferences.getUserModel().getDonationBal());
        coin_bal = CustomUtil.convertFloat(preferences.getUserModel().getFf_coin());
       // float old_coin_bal = CustomUtil.convertFloat(preferences.getUserModel().getOld_ff_coins_bal());

        if (ConstantUtil.is_bonus_show){
            available_bal = deposit_bal + winning_bal /*+ borrowed_bal*/ + reward_bal + coin_bal;
        }else {
            available_bal = deposit_bal + winning_bal /*+ borrowed_bal*/ + coin_bal;
        }

        preferences.getUserModel().setTotal_balance(available_bal);

        total_balance.setText(mContext.getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((available_bal/*+old_coin_bal*/)));
        user_bal_deposit.setText(mContext.getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((deposit_bal+coin_bal)));
        user_bal_winning.setText(mContext.getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(winning_bal));
       // txtFantaGems.setText(mContext.getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(old_coin_bal));
        //user_bal_borrowed.setText(mContext.getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(borrowed_bal));
        user_bal_rewards.setText(mContext.getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(reward_bal));


        //user_bal_coin.setText(mContext.getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(coin_bal));
        //user_bal_donation.setText(mContext.getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(donation_bal));

        if (preferences.getUserModel()!=null){
            if (preferences.getUserModel().getPanStatus().toLowerCase().equals("approve") &&
                    preferences.getUserModel().getBankAccStatus().toLowerCase().equals("approve")) {
                txtWithdraw.setText("Withdraw");
                //kyc_detail.setVisibility(View.VISIBLE);
                txtWinDetail.setText("Withdraw your winnings.");
                txtWithdraw.setBackgroundResource(R.drawable.btn_green);
            }else if (preferences.getUserModel().getPanStatus().toLowerCase().equals("inreview") ||
                    preferences.getUserModel().getBankAccStatus().toLowerCase().equals("inreview")) {
                txtWithdraw.setText("Inreview");
                //kyc_detail.setVisibility(View.VISIBLE);
                txtWinDetail.setText("Your KYC is Inreview.");
                txtWithdraw.setBackgroundResource(R.drawable.btn_orange);
            }else if (preferences.getUserModel().getPanStatus().toLowerCase().equals("reject") ||
                    preferences.getUserModel().getBankAccStatus().toLowerCase().equals("reject")) {
                txtWithdraw.setText("Rejected");
                txtWithdraw.setBackgroundResource(R.drawable.btn_primary);
                //kyc_detail.setVisibility(View.VISIBLE);
                txtWinDetail.setText("Your KYC is Rejected, Please re-upload your documents.");
            }else {
                txtWithdraw.setBackgroundResource(R.drawable.btn_green);
                txtWithdraw.setText("Verify Now");
               // kyc_detail.setVisibility(View.GONE);
                txtWinDetail.setText("Verify your account to withdraw.");
            }

            if(preferences.getUserModel().getIsPromoter().equalsIgnoreCase("yes")){
                affiliation.setVisibility(View.VISIBLE);
                viewafl.setVisibility(View.VISIBLE);
            }else{
                affiliation.setVisibility(View.GONE);
                viewafl.setVisibility(View.GONE);
            }
          /*  if (ConstantUtil.is_game_test){
                affiliation.setVisibility(View.VISIBLE);
                viewafl.setVisibility(View.VISIBLE);
            }else {

            }*/
        }
    }

    protected void setStatusBarDark() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.blackSecondary));
        }
    }

    protected void setStatusBarRed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void initControl(View view) {
        total_balance=view.findViewById(R.id.txtTotalBal);
        user_bal_winning=view.findViewById(R.id.user_bal_winning);
        user_bal_deposit=view.findViewById(R.id.user_bal_deposit);
        user_bal_rewards=view.findViewById(R.id.user_bal_rewards);
        txtWinDetail=view.findViewById(R.id.txtWinDetail);
        txtFooter=view.findViewById(R.id.txtFooter);
        txtFooterLinkWeb=view.findViewById(R.id.txtFooterLinkWeb);
        amtTool=view.findViewById(R.id.amtTool);
        toolWinning=view.findViewById(R.id.toolWinning);
        toolBonus=view.findViewById(R.id.toolBonus);
        kyc_detail=view.findViewById(R.id.kyc_detail);
        drawer_image=view.findViewById(R.id.drawer_image);
        txtAddAmt=view.findViewById(R.id.txtAddAmt);
        btnDeposit=view.findViewById(R.id.btnDeposit);
        txtWithdraw=view.findViewById(R.id.txtWithdraw);
        btnRefer=view.findViewById(R.id.btnRefer);
        swipeRefresh=view.findViewById(R.id.swipeRefresh);
        affiliation=view.findViewById(R.id.affiliation);
        viewafl=view.findViewById(R.id.view);
        txtFantaGems=view.findViewById(R.id.txtFantaGems);
        layBonus=view.findViewById(R.id.layBonus);
        //user_bal_coin=view.findViewById(R.id.user_bal_coin);

        txtWinDetail.setText("Withdraw your winnings.");

        if (BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE)){
            txtFooter.setVisibility(View.VISIBLE);
            txtFooterLinkWeb.setVisibility(View.VISIBLE);
            txtFooterLinkWeb.setMovementMethod(LinkMovementMethod.getInstance());
            String url = "https://www.fantafeat.com/play-responsibly.html";
            SpannableString spannableString = new SpannableString(url);
            spannableString.setSpan(new UnderlineSpan(), 0, url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtFooterLinkWeb.setText(spannableString);
            txtFooterLinkWeb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Try to open in Chrome first
                    intent.setPackage("com.android.chrome");

                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        // Chrome is not installed; open with any available browser
                        intent.setPackage(null);
                        startActivity(intent);
                    }
                }
            });
        }else{
            txtFooter.setVisibility(View.GONE);
            txtFooterLinkWeb.setVisibility(View.GONE);
        }

        UpdateModel updateModel=preferences.getUpdateMaster();
        ApiManager.isInstant = updateModel.getIs_cf_instant_withdraw().equalsIgnoreCase("yes");
        ApiManager.isPayTm = updateModel.getDisplayDepositPaytmInstant().equalsIgnoreCase("yes");

        if (preferences.getUserModel()!=null){
            if (preferences.getUserModel().getPanStatus().toLowerCase().equals("approve") &&
                    preferences.getUserModel().getBankAccStatus().toLowerCase().equals("approve")) {
                //txtWithdraw.setText("Withdraw");
                //kyc_detail.setVisibility(View.VISIBLE);
               // txtWinDetail.setText("Withdraw your winnings.");
            }else {
               // txtWithdraw.setText("Verify Now");
               // kyc_detail.setVisibility(View.GONE);
               // txtWinDetail.setText("Verify your account to withdraw.");
            }

            if(preferences.getUserModel().getIsPromoter().equalsIgnoreCase("yes")){
                affiliation.setVisibility(View.VISIBLE);
                viewafl.setVisibility(View.VISIBLE);
            }else{
                affiliation.setVisibility(View.GONE);
                viewafl.setVisibility(View.GONE);
            }

            /*if (ConstantUtil.is_game_test){
                affiliation.setVisibility(View.VISIBLE);
                viewafl.setVisibility(View.VISIBLE);
            }else {

            }*/
        }

     /*   if (preferences.getUserModel()!=null && !TextUtils.isEmpty(preferences.getUserModel().getUserImg())){
            Glide.with(mContext)
                    .load(ApiManager.PROFILE_IMG +preferences.getUserModel().getUserImg())
                    .placeholder(R.drawable.user_image)
                    .into(drawer_image);
        }*/

        update_kyc=view.findViewById(R.id.update_kyc);
        statement_request=view.findViewById(R.id.statement_request);
        send_money=view.findViewById(R.id.send_money);
        my_referral=view.findViewById(R.id.my_referral);
        transfer_btn=view.findViewById(R.id.transfer_btn);
        transaction_view_more=view.findViewById(R.id.transaction_view_more);

        swipeRefresh.setOnRefreshListener(() -> getUserDetails(false));

        if (ConstantUtil.is_bonus_show){
            layBonus.setVisibility(View.VISIBLE);
        }else {
            layBonus.setVisibility(View.GONE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void initClick() {

        drawer_image.setOnClickListener(view -> OpenNavDrawer());

        amtTool.setOnClickListener(v->{
            /*ViewTooltip
                    .on((Activity) mContext, amtTool)
                    .autoHide(true, 1000)
                    .align(CENTER)
                    .position(LEFT)
                    .text("Add amount from your bank account")
                    .textColor(Color.WHITE)
                    .color(mContext.getResources().getColor(R.color.colorBlack))
                    .show();*/
        });

        toolWinning.setOnClickListener(v->{
            /*if (preferences.getUserModel().getPanStatus().toLowerCase().equals("approve") &&
                    preferences.getUserModel().getBankAccStatus().toLowerCase().equals("approve")) {
                ViewTooltip
                        .on((Activity) mContext, toolWinning)
                        .autoHide(true, 1000)
                        .align(CENTER)
                        .position(LEFT)
                        .text("Your account is verified you can withdraw anytime")
                        .textColor(Color.WHITE)
                        .color(mContext.getResources().getColor(R.color.colorBlack))
                        .show();
            }else {
                ViewTooltip
                        .on((Activity) mContext, toolWinning)
                        .autoHide(true, 1000)
                        .align(CENTER)
                        .position(LEFT)
                        .text("Verify your PAN & BANK details")
                        .textColor(Color.WHITE)
                        .color(mContext.getResources().getColor(R.color.colorBlack))
                        .show();
            }*/

        });

        toolBonus.setOnClickListener(v->{
            /*ViewTooltip
                    .on((Activity) mContext, toolBonus)
                    .autoHide(true, 1000)
                    .align(CENTER)
                    .position(LEFT)
                    .text("Money that you can use to join any Contests in which % of Bonus mentioned.")
                    .textColor(Color.WHITE)
                    .color(mContext.getResources().getColor(R.color.colorBlack))
                    .show();*/
        });

        update_kyc.setOnClickListener(view -> {
            if(MyApp.getClickStatus()) {
                if (!ConstantUtil.is_instant_kyc)
                    startActivity(new Intent(mContext, UpdateKYCActivity.class));
                else
                    startActivity(new Intent(mContext, InstantKycActivity.class));
           /*     new FragmentUtil().addFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new UpdateKYCFragment(),
                        ((HomeActivity) mContext)
                                .fragmentTag(22),
                        FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
            }
        });

        statement_request.setOnClickListener(view -> {
            if(MyApp.getClickStatus()) {
                startActivity(new Intent(mContext, RequestStatementActivity.class));
                /*new FragmentUtil().addFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new RequestStatementFragment(),
                        ((HomeActivity) mContext).fragmentTag(23),
                        FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
            }
        });

        btnRefer.setOnClickListener(v->{
            new FragmentUtil().addFragment(((HomeActivity)mContext),
                    R.id.home_fragment_container,
                    new InviteFriendsFragment(),
                    fragmentTag(32),
                    FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
        });

        send_money.setOnClickListener(view -> {
            if(MyApp.getClickStatus()) {
                if (ApiManager.isInstant){
                    showConfirm();
                }else {
                    startActivity(new Intent(mContext, WithdrawActivity.class)
                            .putExtra("is_instant_amount",false));
                }
                //showConfirm();
               /* startActivity(new Intent(mContext, WithdrawActivity.class)
                .putExtra("is_instant_amount",false));*/
              /*  new FragmentUtil().addFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new WithdrawFragment(),
                        ((HomeActivity) mContext).fragmentTag(24),
                        FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
            }
        });

        my_referral.setOnClickListener(view -> {
            if(MyApp.getClickStatus()) {
                new FragmentUtil().addFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new MyReferralsFragment(),
                        ((HomeActivity) mContext).fragmentTag(26),
                        FragmentUtil.ANIMATION_TYPE.CUSTOM);
            }
        });

        transfer_btn.setOnClickListener(view -> {
            if(MyApp.getClickStatus()) {
                new FragmentUtil().addFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new TransferFragment(),
                        ((HomeActivity) mContext).fragmentTag(27),
                        FragmentUtil.ANIMATION_TYPE.CUSTOM);
            }
        });

        transaction_view_more.setOnClickListener(view -> {
            if(MyApp.getClickStatus()) {
                startActivity(new Intent(mContext, TransactionListActivity.class));
               /* new FragmentUtil().addFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new TransactionListFragment(),
                        ((HomeActivity) mContext).fragmentTag(33),
                        FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
            }
        });

        txtAddAmt.setOnClickListener(view -> {
            if(MyApp.getClickStatus()) {
                if (BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE)) {
                    if (preferences.getUserModel().getPanStatus().toLowerCase().equals("approve") &&
                            preferences.getUserModel().getBankAccStatus().toLowerCase().equals("approve") &&
                            preferences.getUserModel().getAddr_prf_status().toLowerCase().equals("approve")) {
                        Intent intent = new Intent(mContext, AddDepositActivity.class);
                        intent.putExtra("isJoin", false);
                        intent.putExtra("depositAmt", "");
                        startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "Please Update KYC first", Toast.LENGTH_LONG).show();
                        if (!ConstantUtil.is_instant_kyc)
                            startActivity(new Intent(mContext, UpdateKYCActivity.class));
                        else
                            startActivity(new Intent(mContext, InstantKycActivity.class));
                    }
                }else {
                    Intent intent = new Intent(mContext, AddDepositActivity.class);
                    intent.putExtra("isJoin", false);
                    intent.putExtra("depositAmt", "");
                    startActivity(intent);
                }
            }
        });

        btnDeposit.setOnClickListener(view -> {
            if(MyApp.getClickStatus()) {
                if (BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE)) {
                    if (preferences.getUserModel().getPanStatus().toLowerCase().equals("approve") &&
                            preferences.getUserModel().getBankAccStatus().toLowerCase().equals("approve") &&
                            preferences.getUserModel().getAddr_prf_status().toLowerCase().equals("approve")) {
                        Intent intent = new Intent(mContext, AddDepositActivity.class);
                        intent.putExtra("isJoin",false);
                        intent.putExtra("depositAmt","");
                        startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "Please Update KYC first", Toast.LENGTH_LONG).show();
                        if (!ConstantUtil.is_instant_kyc)
                            startActivity(new Intent(mContext, UpdateKYCActivity.class));
                        else
                            startActivity(new Intent(mContext, InstantKycActivity.class));
                    }
                }else {
                    Intent intent = new Intent(mContext, AddDepositActivity.class);
                    intent.putExtra("isJoin",false);
                    intent.putExtra("depositAmt","");
                    startActivity(intent);
                }

            }
        });

        txtWithdraw.setOnClickListener(view -> {
            if(MyApp.getClickStatus()) {
                if (txtWithdraw.getText().toString().equals("Withdraw")){
                    //ApiManager.isInstant=false;
                    //ApiManager.isPayTm=true;
                   // showConfirm();
                    if (ApiManager.isInstant){
                        showConfirm();
                    }else {
                        startActivity(new Intent(mContext, WithdrawActivity.class)
                                .putExtra("is_instant_amount",false));
                    }
                }else {
                    if (!ConstantUtil.is_instant_kyc)
                        startActivity(new Intent(mContext, UpdateKYCActivity.class));
                    else
                        startActivity(new Intent(mContext, InstantKycActivity.class));
                    //showConfirm();
                }
            }
        });

        kyc_detail.setOnClickListener(v->{
            if (!ConstantUtil.is_instant_kyc)
                startActivity(new Intent(mContext, UpdateKYCActivity.class));
            else
                startActivity(new Intent(mContext, InstantKycActivity.class));
         /*   new FragmentUtil().addFragment((FragmentActivity) mContext,
                    R.id.home_fragment_container,
                    new UpdateKYCFragment(),
                    ((HomeActivity) mContext)
                            .fragmentTag(22),
                    FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
        });

        affiliation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MyApp.getClickStatus()) {
                    Intent intent = new Intent(mContext, AffiliationActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void showConfirm() {

        View view = LayoutInflater.from(mContext).inflate(R.layout.withdraw_dialog, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);

        TextView txtWinBalance = view.findViewById(R.id.txtWinBalance);
        Button txtNormal = view.findViewById(R.id.txtNormal);
        Button txtInstant = view.findViewById(R.id.txtInstant);
        Button txtPaytm = view.findViewById(R.id.txtPaytm);
       // Button txtPaytm1 = view.findViewById(R.id.txtPaytm1);

        txtWinBalance.setText(mContext.getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(winning_bal));

        txtNormal.setOnClickListener(v->{
            bottomSheetDialog.dismiss();
            if (preferences.getUserModel().getPanStatus().toLowerCase().equals("approve") &&
                    preferences.getUserModel().getBankAccStatus().toLowerCase().equals("approve")) {

                startActivity(new Intent(mContext, WithdrawActivity.class)
                        .putExtra("is_instant_amount",false));
            }else {
                //Toast.makeText(mContext,"Please update your KYC details. We need your KYC detail for Bank withdraw",Toast.LENGTH_LONG).show();
                CustomUtil.showTopSneakWarning(mContext,"Please update your KYC details. We need your KYC detail for Bank withdraw");
                //startActivity(new Intent(mContext, UpdateKYCActivity.class));
            }


        });

        txtPaytm.setOnClickListener(v->{
            if (!ApiManager.isPayTm){
                CustomUtil.showTopSneakWarning(mContext,"Temporary unavailable Paytm Withdraw.");
            }else {
                bottomSheetDialog.dismiss();
                startActivity(new Intent(mContext, WithdrawActivity.class)
                        .putExtra("is_instant_amount",false)
                        .putExtra("isPaytm","yes"));
            }
        });

    /*    txtPaytm1.setOnClickListener(v->{
            bottomSheetDialog.dismiss();
            startActivity(new Intent(mContext, WithdrawActivity.class)
                    .putExtra("is_instant_amount",false)
                    .putExtra("isPaytm","yes"));
        });*/

        txtInstant.setOnClickListener(v->{
            bottomSheetDialog.dismiss();
            if (preferences.getUserModel().getPanStatus().toLowerCase().equals("approve") &&
                    preferences.getUserModel().getBankAccStatus().toLowerCase().equals("approve")) {
                if (ApiManager.isInstant){
                    bottomSheetDialog.dismiss();
                    startActivity(new Intent(mContext, WithdrawActivity.class)
                            .putExtra("is_instant_amount",true));
                }else {
                    CustomUtil.showTopSneakWarning(mContext,"Temporary unavailable Instant Bank Withdraw, You can use normal withdraw instead.");
                }
            }else {
                //startActivity(new Intent(mContext, UpdateKYCActivity.class));
                //Toast.makeText(mContext,"Please update your KYC details. We need your KYC detail for Instant Bank withdraw",Toast.LENGTH_LONG).show();
                CustomUtil.showTopSneakWarning(mContext,"Please update your KYC details. We need your KYC detail for Instant Bank withdraw");
            }
        });

        bottomSheetDialog.show();
    }

    public String fragmentTag(int position) {
        String[] drawer_tag = getResources().getStringArray(R.array.stack);
        return drawer_tag[position];
    }

}