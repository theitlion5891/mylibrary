package com.fantafeat.Fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Activity.PaytmVerifyActivity;
import com.fantafeat.Activity.TransactionListActivity;
import com.fantafeat.Activity.WithdrawActivity;
import com.fantafeat.Adapter.WinningTransferOfferAdapter;
import com.fantafeat.Model.UpdateModel;
import com.fantafeat.Model.UserModel;
import com.fantafeat.Model.WinningTransferOfferModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.PrefConstant;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BankWithdrawFragment extends BaseFragment {

    private Button bank_withdraw_btn;
    private EditText withdraw_amount_edit;
    private TextView bank_account_number,bank_ifsc_code,bank_kyc_error,txtAmtDetail,total_winning_bal,toolbar_title,txtAmt,txtCharge,
            txtTotal,txtTerms,/*txtTdsMainTitle,txtTdsSubTitle,txtTaxableAmt,txtWithdrawAmt,*/txtConvert,txtEntry1,txtEntry2,txtLblWin2,
            /*txtLblTaxGovt,txtTaxAmt,*/txtAnnounce,txtTransFee,txtEnteredAmt,txtDepositAmt,txtDiscount;
    private LinearLayout bank_kyc_details/*,layTrans*/;
    private CardView /*cardTdsDetail,*/cardCal;

    private TextView tnc_text;
    private boolean is_instant_amount=false;
    private Boolean is_paytm=false;
    LinearLayout layChart,layXi;
    private static final String ARG_instant="instant";
    private static final String ARG_paytm="paytm";
    private ArrayList<WinningTransferOfferModel> list=new ArrayList<>();
    private String rs;
    private double tds_per,non_tds_amount;
    private DecimalFormat format =CustomUtil.getFormater("00.00");
    private float winning_bal=0;
    private BottomSheetDialog dialog=null;

    public static BankWithdrawFragment newInstance(boolean is_instant_amount,boolean is_paytm) {
        BankWithdrawFragment fragment = new BankWithdrawFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_instant, is_instant_amount);
        args.putBoolean(ARG_paytm, is_paytm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            is_instant_amount = getArguments().getBoolean(ARG_instant);
            is_paytm = getArguments().getBoolean(ARG_paytm);
        }
    }

    public BankWithdrawFragment() {//boolean is_instant_amount,boolean is_paytm
       // this.is_instant_amount = is_instant_amount;
        //this.is_paytm = is_paytm;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bank_withdraw, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initControl(View view) {
        rs=mContext.getResources().getString(R.string.rs);
        winning_bal=CustomUtil.convertFloat(preferences.getUserModel().getWinBal());

        bank_withdraw_btn = view.findViewById(R.id.bank_withdraw_btn);
        withdraw_amount_edit = view.findViewById(R.id.withdraw_amount_edit);

        txtAmt = view.findViewById(R.id.txtAmt);
        txtCharge = view.findViewById(R.id.txtCharge);
        txtTotal = view.findViewById(R.id.txtTotal);
        toolbar_title = view.findViewById(R.id.toolbar_title);
        total_winning_bal = view.findViewById(R.id.total_winning_bal);
        txtTerms = view.findViewById(R.id.txtTerms);

        cardCal = view.findViewById(R.id.cardCal);
        layChart = view.findViewById(R.id.layChart);
        txtEntry1 = view.findViewById(R.id.txtEntry1);
        txtEntry2 = view.findViewById(R.id.txtEntry2);
        txtLblWin2 = view.findViewById(R.id.txtLblWin2);
       // txtAmtDetail = view.findViewById(R.id.txtAmtDetail);
        bank_account_number = view.findViewById(R.id.bank_account_number);
        bank_ifsc_code = view.findViewById(R.id.bank_ifsc_code);
        bank_kyc_details = view.findViewById(R.id.bank_kyc_details);
        bank_kyc_error = view.findViewById(R.id.bank_kyc_error);
        //txtWithdrawAmt = view.findViewById(R.id.txtWithdrawAmt);

        //txtTdsMainTitle = view.findViewById(R.id.txtTdsMainTitle);
        //txtTdsSubTitle = view.findViewById(R.id.txtTdsSubTitle);
        //txtTaxableAmt = view.findViewById(R.id.txtTaxableAmt);
        //txtLblTaxGovt = view.findViewById(R.id.txtLblTaxGovt);
        //txtTaxAmt = view.findViewById(R.id.txtTaxAmt);
        txtAnnounce = view.findViewById(R.id.txtAnnounce);
        //cardTdsDetail = view.findViewById(R.id.cardTdsDetail);
        txtTransFee = view.findViewById(R.id.txtTransFee);
        //layTrans = view.findViewById(R.id.layTrans);
        cardCal = view.findViewById(R.id.cardCal);
        txtEnteredAmt = view.findViewById(R.id.txtEnteredAmt);
        txtDepositAmt = view.findViewById(R.id.txtDepositAmt);
        txtConvert = view.findViewById(R.id.txtConvert);
        txtDiscount = view.findViewById(R.id.txtDiscount);
        layXi = view.findViewById(R.id.layXi);

        if(preferences.getUserModel()!=null && preferences.getUserModel().getBankAccStatus().equalsIgnoreCase("approve")) {
          //  bank_kyc_error.setVisibility(View.GONE);
            if (!preferences.getUserModel().getBankIfscNo().equalsIgnoreCase("")) {
                bank_ifsc_code.setText(preferences.getUserModel().getBankIfscNo());
            }
            if (!preferences.getUserModel().getBankAccNo().equalsIgnoreCase("")) {
                bank_account_number.setText(preferences.getUserModel().getBankAccNo());
            }
        }
        else{
           // bank_kyc_details.setVisibility(View.GONE);
           // bank_kyc_error.setVisibility(View.VISIBLE);
        }

        if (is_instant_amount){
            //layCalc.setVisibility(View.VISIBLE);
            layChart.setVisibility(View.VISIBLE);
          //  layTrans.setVisibility(View.VISIBLE);
        }
        else {
            //layCalc.setVisibility(View.GONE);
            layChart.setVisibility(View.GONE);
           // layTrans.setVisibility(View.GONE);
        }

        withdraw_amount_edit.setFilters(new InputFilter[]{new MinMaxFilter(0, winning_bal)});

        withdraw_amount_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                calculate(s.toString());

                /*if (is_instant_amount){
                    if (TextUtils.isEmpty(s)){
                        txtAmt.setText(getResources().getString(R.string.rs)+"0.0");
                        txtCharge.setText(getResources().getString(R.string.rs)+"0");
                        txtTotal.setText(getResources().getString(R.string.rs)+"0.0");
                        return;
                    }
                    float amount=CustomUtil.convertFloat(s.toString());
                    txtAmt.setText(getResources().getString(R.string.rs)+amount);
                    if (amount>0){
                        int charge=0;
                        if (amount<=1000){
                            charge=2;
                        }else if (amount<=10000){
                            charge=5;
                        }else if (amount>=10001){
                            charge=10;
                        }
                        float total=amount-charge;

                        txtCharge.setText("-"+getResources().getString(R.string.rs)+charge);
                        if (total<0){
                            txtTotal.setText("-"+getResources().getString(R.string.rs)+String.valueOf(total).replace("-",""));
                        }else {
                            txtTotal.setText(getResources().getString(R.string.rs)+total);
                        }

                    }
                    else {
                        txtAmt.setText(getResources().getString(R.string.rs)+"0.0");
                        txtCharge.setText(getResources().getString(R.string.rs)+"0");
                        txtTotal.setText(getResources().getString(R.string.rs)+"0.0");
                    }
                }*/

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //txtAmtDetail.setText("*You can Withdraw between " +mContext.getResources().getString(R.string.rs)+ preferences.getUpdateMaster().getMinWithdrawAmount() + " to " +
       //         mContext.getResources().getString(R.string.rs)+ preferences.getUpdateMaster().getMaxWithdrawAmount());

        getTdsDetails();
    }

    private void calculate(String s){
        if (!TextUtils.isEmpty(s)){
           // cardTdsDetail.setVisibility(View.VISIBLE);

            // double tdsCountAmount=winning_bal-non_tds_amount;

            //double tdsCountAmount=0,payTdsAmount=0;
            float enterAmt=CustomUtil.convertFloat(s.toString());

           /* if (enterAmt > non_tds_amount){
                tdsCountAmount=enterAmt-non_tds_amount;
                payTdsAmount=tdsCountAmount*tds_per/100;
            }*/

            /*txtTaxableAmt.setText(rs+format.format(tdsCountAmount));
            txtTaxAmt.setText(rs+format.format(payTdsAmount));
            txtWithdrawAmt.setText(rs+format.format(enterAmt));*/

            //txtAnnounce.setText("You Can Withdraw "+rs+format.format(non_tds_amount)+" without any TDS(tax)");
            int charge=0;

            if (is_instant_amount){
                if (enterAmt<=1000){
                    charge=2;
                }
                else if (enterAmt<=10000){
                    charge=5;
                }
                else if (enterAmt>=10001){
                    charge=10;
                }
            }

            //txtLblTaxGovt.setText("TDS (Tax to govt. @"+tds_per+"%)");
            txtTransFee.setText(rs+charge);

            //double cashAmt=enterAmt-payTdsAmount;

            if (non_tds_amount<=0){
                txtEntry2.setVisibility(View.GONE);
                txtLblWin2.setVisibility(View.GONE);

                if (enterAmt>=charge){
                    txtEntry1.setText(rs+format.format(enterAmt-charge));
                    txtTotal.setText(rs+format.format((enterAmt-charge)));
                }else {
                    txtTotal.setText(rs+"00.00");
                    txtEntry1.setText(rs +"00.00");//+ format.format(enterAmt)
                }

                txtEntry2.setText("");
            }
            else if (enterAmt > non_tds_amount){
                txtEntry2.setVisibility(View.VISIBLE);
                txtLblWin2.setVisibility(View.VISIBLE);

                double win2Left=enterAmt-non_tds_amount;

                if (win2Left < charge){
                    txtEntry1.setText(rs+format.format(non_tds_amount-charge));
                    txtEntry2.setText(rs+format.format(win2Left));
                    if (win2Left<=0){
                        txtEntry2.setVisibility(View.GONE);
                        txtLblWin2.setVisibility(View.GONE);
                    }
                }else {
                    txtEntry1.setText(rs+format.format(non_tds_amount));
                    txtEntry2.setText(rs+format.format(win2Left-charge));
                    if ((win2Left-charge)<=0){
                        txtEntry2.setVisibility(View.GONE);
                        txtLblWin2.setVisibility(View.GONE);
                    }
                }
                txtTotal.setText(rs+format.format((enterAmt-charge)));
            }
            else {
                txtEntry2.setVisibility(View.GONE);
                txtLblWin2.setVisibility(View.GONE);

                if (enterAmt>=charge){
                    txtEntry1.setText(rs+format.format(enterAmt-charge));
                    txtTotal.setText(rs+format.format((enterAmt-charge)));
                }else {
                    txtTotal.setText(rs+"00.00");
                    txtEntry1.setText(rs +"00.00");//+ format.format(enterAmt)
                }

                txtEntry2.setText("");
            }
           // txtTdsSubTitle.setText("Tax to govt. "+rs+format.format(payTdsAmount));

            if (list.size()>0){
                float discount=0;
                for (int i = 0; i < list.size(); i++) {
                    WinningTransferOfferModel model=list.get(i);
                    float startAmt=CustomUtil.convertFloat(model.getStartAmt());
                    float endAmt=CustomUtil.convertFloat(model.getEndAmt());

                    if (enterAmt>=startAmt && enterAmt<=endAmt){
                        discount=CustomUtil.convertFloat(model.getCreditPercentage());

                    }
                }

                txtEnteredAmt.setText(rs+format.format(enterAmt));

                float depositAmt=(enterAmt*discount/100)+enterAmt;

                txtDepositAmt.setText(rs+format.format(depositAmt));
                txtDiscount.setText("Get "+discount+"% Extra");
                //float depositAmt=(enterAmt*discount/100)+enterAmt;

                cardCal.setVisibility(View.VISIBLE);
                txtDiscount.setVisibility(View.VISIBLE);

            }
            else {
                cardCal.setVisibility(View.GONE);
            }

        }
        else {
            txtEntry2.setText(rs+"00.00");
            txtEntry1.setText(rs+"00.00");
            txtTotal.setText(rs+"00.00");
            txtTransFee.setText(rs+"0");
          //  cardTdsDetail.setVisibility(View.GONE);
            //txtTdsMainTitle.setText("Enter Amount to calculate");
            //txtTdsSubTitle.setText("Withdrawal Amount after Tax");
        }
    }

    private void getTdsDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("amount",winning_bal);
            jsonObject.put("phone_no",preferences.getUserModel().getPhoneNo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "getTdsDetails: " + jsonObject.toString() );
        HttpRestClient.postJSON(mContext, true, ApiManager.getTdsAmountAndPercentage, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "getTdsDetails: " + responseBody.toString() );
                if(responseBody.optBoolean("status")){
                    JSONObject data=responseBody.optJSONObject("data");

                    tds_per=data.optInt("tds_per");

                    //double tdsCountAmount=data.optDouble("tdsCountAmount");
                    //double payTdsAmount=data.optDouble("payTdsAmount");
                    //double Total_wd_bal=data.optDouble("Total_wd_bal");
                    non_tds_amount=data.optDouble("non_tds_amount");

                    // double taxableAmt=Total_wd_bal-non_tds_amount;

                    //txtTaxableAmt.setText(rs+format.format(tdsCountAmount));
                    //txtTaxAmt.setText(rs+format.format(payTdsAmount));

                 /*   if (non_tds_amount<=0)
                        layXi.setVisibility(View.GONE);*/

                    //txtAnnounce.setText("You Can Withdraw "+rs+format.format(non_tds_amount)+" without any TDS(tax)");
                    //txtLblTaxGovt.setText("Tax to govt. @"+tds_per+"%");

                }
                getWinTransferList();
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void getWinTransferList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "getWinTransferList: " + jsonObject.toString() );
        HttpRestClient.postJSON(mContext, false, ApiManager.getWinningTranferOfferList, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "getWinTransferList: " + responseBody.toString() );
                if(responseBody.optBoolean("status")){

                    JSONArray data=responseBody.optJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj=data.optJSONObject(i);
                        WinningTransferOfferModel model=gson
                                .fromJson(obj.toString(),WinningTransferOfferModel.class);
                        list.add(model);
                    }

                    if (list.size()>0) {
                        hideKeyboard(((Activity) mContext));
                        cardCal.setVisibility(View.VISIBLE);
                        if (!preferences.getPrefBoolean(PrefConstant.is_skip_withdraw))
                            showTransferSheet(list);
                    }else {
                        cardCal.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void showTransferSheet(ArrayList<WinningTransferOfferModel> list) {
        dialog=new BottomSheetDialog(mContext);
        dialog.setContentView(R.layout.winning_transfer_sheet);
        dialog.setCancelable(false);

        TextView txtSkip=dialog.findViewById(R.id.txtSkip);
        ImageView imgBack=dialog.findViewById(R.id.imgBack);
        RecyclerView recyclerList=dialog.findViewById(R.id.recyclerList);
        TextView btnConvert=dialog.findViewById(R.id.btnConvert);
        TextView txtEnteredAmt=dialog.findViewById(R.id.txtEnteredAmt);
        TextView txtDepositAmt=dialog.findViewById(R.id.txtDepositAmt);
        EditText edtAmt=dialog.findViewById(R.id.edtAmt);
        TextView txtWinBal=dialog.findViewById(R.id.txtWinBal);
        txtWinBal.setText("Winning- "+rs+winning_bal);

        recyclerList.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        WinningTransferOfferAdapter adapter=new WinningTransferOfferAdapter(mContext,list);
        recyclerList.setAdapter(adapter);

        txtEnteredAmt.setText(rs+"0");
        txtDepositAmt.setText(rs+"0");

        txtSkip.setOnClickListener(v -> {
            preferences.setPref(PrefConstant.is_skip_withdraw,true);
            dialog.dismiss();
        });

        /*txtDifAmt.setOnClickListener(v -> {
            layOffer.setVisibility(View.GONE);
            layAmt.setVisibility(View.VISIBLE);
        });*/

        imgBack.setOnClickListener(v -> {
            dialog.dismiss();
            /*if (layOffer.getVisibility()==View.VISIBLE){
                dialog.dismiss();
            }else {
                layOffer.setVisibility(View.VISIBLE);
                layAmt.setVisibility(View.GONE);
            }*/
        });

        edtAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)){
                    float enterAmt=CustomUtil.convertFloat(s.toString());
                    float discount=0;
                    for (int i = 0; i < list.size(); i++) {
                        WinningTransferOfferModel model=list.get(i);
                        float startAmt=CustomUtil.convertFloat(model.getStartAmt());
                        float endAmt=CustomUtil.convertFloat(model.getEndAmt());
                        model.setSelected(false);
                        if (enterAmt>=startAmt && enterAmt<=endAmt){
                            discount=CustomUtil.convertFloat(model.getCreditPercentage());
                            model.setSelected(true);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    txtEnteredAmt.setText(rs+format.format(enterAmt));

                    float depositAmt=(enterAmt*discount/100)+enterAmt;

                    txtDepositAmt.setText(rs+format.format(depositAmt));


                }else {
                    txtEnteredAmt.setText(rs+"0");
                    txtDepositAmt.setText(rs+"0");

                }
            }
        });

        btnConvert.setOnClickListener(v -> {
            String amt=edtAmt.getText().toString().trim();

            if (TextUtils.isEmpty(amt)){
                CustomUtil.showToast(mContext,"Enter Amount");
                return;
            }

            if(CustomUtil.convertFloat(amt) >
                    CustomUtil.convertFloat(preferences.getUserModel().getWinBal())){
                CustomUtil.showToast(mContext,"Insufficient Balance");
                return ;
            }

            submitTransfer(amt);
        });

        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            setupFullHeight(bottomSheetDialog);
        });

        dialog.show();
    }

    private void submitTransfer(String amt) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("amount", amt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "getWinTransferList: " + jsonObject.toString() );
        HttpRestClient.postJSON(mContext, true, ApiManager.winningTransferToDepositCash, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "getWinTransferList: " + responseBody.toString() );
                if(responseBody.optBoolean("status")){
                    if (dialog!=null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                    //JSONObject data=responseBody.optJSONObject("data");
                    ((Activity)mContext).finish();
                    CustomUtil.showTopSneakSuccess(mContext,responseBody.optString("msg"));

                   /* Dialog dialog=new Dialog(mContext,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                    dialog.setContentView(R.layout.transfer_result_dialog);

                    TextView txtTransBal=dialog.findViewById(R.id.txtTransBal);
                    txtTransBal.setText(data.optString("total_credit_amount"));

                    dialog.setOnDismissListener(dialog1 -> RemoveFragment());

                    dialog.show();*/

                }else {
                    CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        /*FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
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
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Override
    public void initClick() {
        bank_kyc_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bank_kyc_error.setEnabled(false);
                if (MyApp.getClickStatus()) {
                    startActivity(new Intent(mContext, TransactionListActivity.class));
                   /* new FragmentUtil().addFragment((FragmentActivity) mContext,
                            R.id.home_fragment_container,
                            new UpdateKYCFragment(),
                            ((HomeActivity) mContext).fragmentTag(20),
                            FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
                }
            }
        });

        txtTerms.setOnClickListener(v->{
            if (MyApp.getClickStatus()){
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
                 //Button btn_ok;
                View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_terms_condition, null);
               // btn_ok =  view.findViewById(R.id.btn_ok);
                tnc_text = view.findViewById(R.id.tnc_text);

                if (is_instant_amount){
                    tnc_text.setText(preferences.getUpdateMaster().getCf_instant_withdraw_tnc());
                }else {
                    if (is_paytm){
                        tnc_text.setText(preferences.getUpdateMaster().getPaytmInstantTnc());
                    }else {
                        tnc_text.setText(preferences.getUpdateMaster().getBankWithdrawTnc());
                    }
                }
                tnc_text.setMovementMethod(LinkMovementMethod.getInstance());

              /*  btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        Intent intent = new Intent(mContext,HomeActivity.class);
                        startActivity(intent);
                    }
                });*/

                //bottomSheetDialog.setCancelable(false);
                bottomSheetDialog.setContentView(view);
                ((View) view.getParent()).setBackgroundResource(android.R.color.white);

                if (!bottomSheetDialog.isShowing()){
                    bottomSheetDialog.show();
                }
            }
        });

        txtConvert.setOnClickListener(v -> {
            String amt=withdraw_amount_edit.getText().toString().trim();

            if (TextUtils.isEmpty(amt)){
                CustomUtil.showToast(mContext,"Enter Amount");
                return;
            }

            if(CustomUtil.convertFloat(amt) > winning_bal){
                CustomUtil.showToast(mContext,"Insufficient Balance");
                return ;
            }
            submitTransfer(amt);
        });

        bank_withdraw_btn.setOnClickListener(view -> {
            bank_withdraw_btn.setEnabled(false);
            if(isValid()){
                if (is_paytm){
                    paytmRequest();
                }else {
                    showConfirm();
                }

                /*if (is_instant_amount){
                    bank_withdraw_btn.setEnabled(true);
                    CustomUtil.showTopSneakWarning(mContext,"Coming Soon");
                }  else {
                showConfirm();
                // submitWithdrawRequest();
                }*/
            }else {
                bank_withdraw_btn.setEnabled(true);
            }

        });
    }

    private void paytmRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("amount", getEditText(withdraw_amount_edit));
            jsonObject.put("phone_no", preferences.getUserModel().getPhoneNo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, true, ApiManager.PAYTM_WITHDRAW_REQUEST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                bank_withdraw_btn.setEnabled(true);
                if (responseBody.optBoolean("status")) {
                    JSONObject data = responseBody.optJSONObject("data");
                    String trans_id = data.optString("trans_id");
                    startActivity(new Intent(mContext, PaytmVerifyActivity.class)
                            .putExtra(ConstantUtil.TRANS_ID,trans_id)
                            .putExtra(ConstantUtil.AMOUNT,getEditText(withdraw_amount_edit)));
                  /*  new FragmentUtil().addFragment((FragmentActivity) mContext,
                            R.id.fragment_container,
                            new PaytmVerifyFragment(trans_id, getEditText(withdraw_amount_edit)),
                            fragmentTag(41),
                            FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
                } else {
                    CustomUtil.showTopSneakWarning(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                bank_withdraw_btn.setEnabled(true);
            }
        });
    }
    private void showConfirm() {
        Button btn_ok;
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_withdraw, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);

        btn_ok = view.findViewById(R.id.btn_ok);
        TextView txtNote = view.findViewById(R.id.txtNote);

        ObjectAnimator anim = ObjectAnimator.ofFloat(txtNote, "alpha", 0.5f, 1.0f);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(300);
        anim.start();

        TextView txtSattleTime = view.findViewById(R.id.txtSattleTime);
        TextView txtMsg = view.findViewById(R.id.txtMsg);
        TextView txtBankDetails = view.findViewById(R.id.txtBankDetails);
        String msg="Are you sure you want to withdraw <font color=#39AD5E><b>"+mContext.getResources().getString(R.string.rs)+getEditText(withdraw_amount_edit)+"</b></font>?";
        txtMsg.setText(Html.fromHtml(msg));
        UserModel userModel=preferences.getUserModel();
        String bank="◉ NAME: "+userModel.getPanName()+"\n"+
                "◉ BANK A/C: "+userModel.getBankAccNo()+"\n"+
                "◉ IFSC: "+userModel.getBankIfscNo()+"\n"+
                "◉ BANK NAME: "+userModel.getBankName()+"\n"+
                "◉ PAN: "+userModel.getPanNo();
        txtBankDetails.setText(bank);

        if (is_instant_amount){
            txtSattleTime.setVisibility(View.GONE);
        }else {
            UpdateModel updateModel=preferences.getUpdateMaster();
            if (TextUtils.isEmpty(updateModel.getIs_wd_expected_date())){
                txtSattleTime.setVisibility(View.GONE);
            }
            else {
                txtSattleTime.setVisibility(View.VISIBLE);
                txtSattleTime.setText("Expected Settlement Date "+CustomUtil.dateConvertWithFormat(updateModel.getIs_wd_expected_date(),
                        "MMM dd, yyyy hh:mm a","yyyy-MM-dd HH:mm:ss"));
            }
        }

        ImageView btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(view1 -> {
            bottomSheetDialog.dismiss();
            if (is_instant_amount){
                submitInstantWithdrawRequest();
            }else {
                submitWithdrawRequest();
            }
        });

        btn_cancel.setOnClickListener(v->{
            bottomSheetDialog.dismiss();
            bank_withdraw_btn.setEnabled(true);
        });

        bottomSheetDialog.show();
    }
    private void submitWithdrawRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("amount",getEditText(withdraw_amount_edit));
            jsonObject.put("phone_no",preferences.getUserModel().getPhoneNo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e("resp","param: "+jsonObject+" url:"+ApiManager.BANK_WITHDRAW_REQUEST);
        HttpRestClient.postJSON(mContext, true, ApiManager.BANK_WITHDRAW_REQUEST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                bank_withdraw_btn.setEnabled(true);
                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString() );
                if(responseBody.optBoolean("status")){
                    CustomUtil.showTopSneakSuccess(mContext, responseBody.optString("msg"));
                    getUserDetails();
                }else{
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                bank_withdraw_btn.setEnabled(true);

            }
        });
    }
    private void submitInstantWithdrawRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("amount",getEditText(withdraw_amount_edit));
            jsonObject.put("phone_no",preferences.getUserModel().getPhoneNo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "onSuccessResult: " + jsonObject.toString() );
        HttpRestClient.postJSON(mContext, true, ApiManager.BANK_INSTANT_WITHDRAW_REQUEST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString() );
                bank_withdraw_btn.setEnabled(true);
               // LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString() );
                if(responseBody.optBoolean("status")){
                    CustomUtil.showTopSneakSuccess(mContext, responseBody.optString("msg"));
                    getUserDetails();
                }else{
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
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
            Log.e(TAG, "getUserDetails: " + jsonObject.toString());


            HttpRestClient.postJSON(mContext, false, ApiManager.AUTHV3GetUserDetails, jsonObject, new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {
                    Log.e(TAG, "USER onSuccessResult: " + responseBody.toString());
                    if (responseBody.optBoolean("status")) {
                        if (responseBody.optString("code").equals("200")) {
                            MyApp.getMyPreferences().setPref(PrefConstant.PAYMENT_SUCCESS,true);
                            JSONObject data = responseBody.optJSONObject("data");
                            UserModel userModel = gson.fromJson(data.toString(), UserModel.class);
                            preferences.setUserModel(userModel);
                            ((WithdrawActivity)mContext).winning_bal=CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
                            ((WithdrawActivity)mContext).total_winning_bal.setText(mContext.getResources().getString(R.string.rs) + preferences.getUserModel().getWinBal());
                            ((WithdrawActivity) mContext).finish();
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

    @Override
    public void onResume() {
        super.onResume();
        withdraw_amount_edit.setText("");
    }

    private boolean isValid() {
        if (is_instant_amount){

            if(CustomUtil.convertInt(getEditText(withdraw_amount_edit)) <
                    CustomUtil.convertFloat(preferences.getUpdateMaster().getCf_instant_withdraw_limit())){

                CustomUtil.showTopSneakError(mContext,"Minimum Rs." + preferences.getUpdateMaster().getCf_instant_withdraw_limit() +" can be withdrawn" );
                bank_withdraw_btn.setEnabled(true);
                return  false;
            }
            if(CustomUtil.convertInt(getEditText(withdraw_amount_edit)) >
                    CustomUtil.convertFloat(preferences.getUpdateMaster().getCf_instant_withdraw_maxlimit())){
                bank_withdraw_btn.setEnabled(true);
                CustomUtil.showTopSneakError(mContext,"Maximum Rs." + preferences.getUpdateMaster().getCf_instant_withdraw_maxlimit() +" can be withdrawn" );
                return  false;
            }

        }
        else {
            if (is_paytm){
                LogUtil.e("ptmMax",preferences.getUpdateMaster().getPaytmWalletWithdrawAmountMax());
                if (!preferences.getUpdateMaster().getDisplayDepositPaytmInstant().equalsIgnoreCase("Yes")) {
                    CustomUtil.showTopSneakError(mContext, "Paytm withdraw is currently unavailable. Please try after some time");
                    return false;
                }
                if (CustomUtil.convertInt(getEditText(withdraw_amount_edit)) <
                        CustomUtil.convertFloat(preferences.getUpdateMaster().getMinWithdrawAmount())) {

                    CustomUtil.showTopSneakError(mContext, "Minimum Rs." + preferences.getUpdateMaster().getMinWithdrawAmount() + " can be withdraw");
                    return false;
                }
                if (CustomUtil.convertInt(getEditText(withdraw_amount_edit)) >
                        CustomUtil.convertFloat(preferences.getUpdateMaster().getPaytmWalletWithdrawAmountMax())) {

                    CustomUtil.showTopSneakError(mContext, "Maximum Rs." + preferences.getUpdateMaster().getPaytmWalletWithdrawAmountMax() + " can be withdraw");
                    return false;
                }

            }else {
                if (CustomUtil.convertInt(getEditText(withdraw_amount_edit)) <
                        CustomUtil.convertFloat(preferences.getUpdateMaster().getMinWithdrawAmount())) {

                    CustomUtil.showTopSneakError(mContext, "Minimum Rs." + preferences.getUpdateMaster().getMinWithdrawAmount() + " can be withdrawn");
                    bank_withdraw_btn.setEnabled(true);
                    return false;
                }
                if (CustomUtil.convertInt(getEditText(withdraw_amount_edit)) >
                        CustomUtil.convertFloat(preferences.getUpdateMaster().getMaxWithdrawAmount())) {
                    bank_withdraw_btn.setEnabled(true);
                    CustomUtil.showTopSneakError(mContext, "Maximum Rs." + preferences.getUpdateMaster().getMinWithdrawAmount() + " can be withdrawn");
                    return false;
                }
            }

        }

        if(CustomUtil.convertInt(getEditText(withdraw_amount_edit)) >
                CustomUtil.convertFloat(preferences.getUserModel().getWinBal())){
            CustomUtil.showTopSneakError(mContext,"Insufficient Balance");
            bank_withdraw_btn.setEnabled(true);
            return  false;
        }
        if (!is_paytm){
            if(!(preferences.getUserModel().getBankAccStatus().equalsIgnoreCase("Approve") &&
                    preferences.getUserModel().getPanStatus().equalsIgnoreCase("Approve"))){
                bank_withdraw_btn.setEnabled(true);
                CustomUtil.showTopSneakError(mContext,"Please verify your KYC and try again.");
                return  false;
            }
        }

        return true;
    }

    public class MinMaxFilter implements InputFilter {
        private float intMin = 0;
        private float intMax = 0;

        // Initialized
        public MinMaxFilter(float minValue, float maxValue) {
            this.intMin = minValue;
            this.intMax = maxValue;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(intMin, intMax, input)) {
                    return null;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return "";
        }

        private boolean isInRange(float a, float b, int c) {
            return (b > a) ? (c >= a && c <= b) : (c >= b && c <= a);
        }
    }
}