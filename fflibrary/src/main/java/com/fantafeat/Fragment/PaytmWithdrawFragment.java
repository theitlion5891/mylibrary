package com.fantafeat.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fantafeat.Activity.PaytmVerifyActivity;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class PaytmWithdrawFragment extends BaseFragment {

    private Button paytm_withdraw_btn;
    private EditText withdraw_amount_edit;
    private TextView tnc_text, withdraw_amount_after_commission, withdraw_paytm_commission_per,
            pan_user_number,txtAmtDetail;
    Float paytmCharges, paytmChargesGst;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_paytm_withdraw, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initControl(View view) {

        paytmCharges = CustomUtil.convertFloat(preferences.getUpdateMaster().getPaytmCharges());
        paytmChargesGst = CustomUtil.convertFloat(preferences.getUpdateMaster().getPaytmChargesGst());


        paytm_withdraw_btn = view.findViewById(R.id.paytm_withdraw_btn);
        withdraw_amount_edit = view.findViewById(R.id.paytm_withdraw_amount_edit);
        txtAmtDetail = view.findViewById(R.id.txtAmtDetail);
        tnc_text = view.findViewById(R.id.tnc_text);
        withdraw_amount_after_commission = view.findViewById(R.id.withdraw_amount_after_commission);
        withdraw_paytm_commission_per = view.findViewById(R.id.withdraw_paytm_commission_per);
        pan_user_number = view.findViewById(R.id.pan_user_number);

        tnc_text.setText(Html.fromHtml(preferences.getUpdateMaster().getPaytmInstantTnc()));
        tnc_text.setMovementMethod(LinkMovementMethod.getInstance());

        txtAmtDetail.setText("*You can Withdraw to Paytm between " +mContext.getResources().getString(R.string.rs)+ preferences.getUpdateMaster().getMinWithdrawAmount() + " to " +
                mContext.getResources().getString(R.string.rs)+ preferences.getUpdateMaster().getPaytmWalletWithdrawAmountMax());

        pan_user_number.setText(preferences.getUserModel().getPhoneNo());

    }

    @Override
    public void initClick() {
        withdraw_amount_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Float getValue = CustomUtil.convertFloat(String.valueOf(charSequence));
                Float paytmCommission = (getValue * paytmCharges) / 100;
                Float PaytmFinalCommission = (paytmCommission * paytmChargesGst) / 100;
                Float finalAmount = getValue - (PaytmFinalCommission + paytmCommission);
                withdraw_amount_after_commission.setText(new DecimalFormat("00.00").format(finalAmount));
                withdraw_paytm_commission_per.setText("Gateway charges " + preferences.getUpdateMaster().getPaytmCharges() + "% + "
                        + preferences.getUpdateMaster().getPaytmChargesGst() + "% GST = " +
                        new DecimalFormat("00.00").format((PaytmFinalCommission + paytmCommission)));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        paytm_withdraw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paytm_withdraw_btn.setEnabled(false);
                if (isValid()) {
                    showConfirm();
                }
            }
        });

    }

    private void submitPaytmWithdrawRequest() {
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
                paytm_withdraw_btn.setEnabled(true);
                if (responseBody.optBoolean("status")) {
                    JSONObject data = responseBody.optJSONObject("data");
                    String trans_id = data.optString("trans_id");
                    startActivity(new Intent(mContext, PaytmVerifyActivity.class)
                            .putExtra(ConstantUtil.TRANS_ID,trans_id)
                            .putExtra(ConstantUtil.AMOUNT,getEditText(withdraw_amount_edit)));
                   /* new FragmentUtil().addFragment((FragmentActivity) mContext,
                            R.id.home_fragment_container,
                            new PaytmVerifyFragment(trans_id, getEditText(withdraw_amount_edit)),
                            fragmentTag(25),
                            FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                paytm_withdraw_btn.setEnabled(true);
            }
        });

    }

    private boolean isValid() {

        if (!preferences.getUpdateMaster().getDisplayDepositPaytmInstant().equalsIgnoreCase("Yes")) {
            CustomUtil.showTopSneakError(mContext, "Paytm withdraw is currently unavailable. Please try after some time");
            paytm_withdraw_btn.setEnabled(true);
            return false;
        }
        if (CustomUtil.convertInt(getEditText(withdraw_amount_edit)) <
                CustomUtil.convertFloat(preferences.getUpdateMaster().getMinWithdrawAmount())) {
            paytm_withdraw_btn.setEnabled(true);
            CustomUtil.showTopSneakError(mContext, "Minimum Rs." + preferences.getUpdateMaster().getMinWithdrawAmount() + " can be withdraw");
            return false;
        }
        if (CustomUtil.convertInt(getEditText(withdraw_amount_edit)) >
                CustomUtil.convertFloat(preferences.getUpdateMaster().getPaytmWalletWithdrawAmountMax())) {
            paytm_withdraw_btn.setEnabled(true);
            CustomUtil.showTopSneakError(mContext, "Maximum Rs." + preferences.getUpdateMaster().getMinWithdrawAmount() + " can be withdraw");
            return false;
        }
        if (CustomUtil.convertInt(getEditText(withdraw_amount_edit)) >
                CustomUtil.convertFloat(preferences.getUserModel().getWinBal())) {
            paytm_withdraw_btn.setEnabled(true);
            CustomUtil.showTopSneakError(mContext, "Insufficient Balance");
            return false;
        }
//        if(!(preferences.getUserModel().getBankAccStatus().equalsIgnoreCase("Approve") &&
//                preferences.getUserModel().getPanStatus().equalsIgnoreCase("Approve"))){
//
//            CustomUtil.showTopSneakError(mContext,"Please Verify Your KYC and try Again.");
//            return  false;
//        }
        return true;
    }

    private void showConfirm() {
        Button btn_ok;
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_withdraw, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);

        btn_ok = view.findViewById(R.id.btn_ok);
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
        TextView btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(view1 -> {
            bottomSheetDialog.dismiss();
            submitPaytmWithdrawRequest();

        });
        btn_cancel.setOnClickListener(v->{
            bottomSheetDialog.dismiss();
            paytm_withdraw_btn.setEnabled(true);
        });

        bottomSheetDialog.show();
    }

}