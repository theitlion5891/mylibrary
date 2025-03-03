package com.fantafeat.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class PaytmVerifyActivity extends BaseActivity {

   // private OtpView otp_view;
    private Button otp_btn;
    private TextView phone_number, edit_number;
    private ImageView paytm_verify_close;
    private TextView paytm_resent_otp;

    private String trans_id, amount;

    private CountDownTimer countDownTimer;

    @Override
    public void initClick() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_paytm_verify);

        if (getIntent().hasExtra(ConstantUtil.AMOUNT)){
            amount=getIntent().getStringExtra(ConstantUtil.AMOUNT);
        }
        if (getIntent().hasExtra(ConstantUtil.TRANS_ID)){
            trans_id=getIntent().getStringExtra(ConstantUtil.TRANS_ID);
        }
        initData();
        initClic();
    }

    private void initClic() {
        otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    if (validate()) {
                        checkOtp();
                    }
                }
            }
        });

       /* otp_view.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                hideKeyboard((Activity) mContext);
            }
        });*/
        paytm_verify_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        paytm_resent_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resentOtp();
            }
        });
    }

    private void initData() {
        //otp_view = findViewById(R.id.otp_view);
        otp_btn = findViewById(R.id.otp_btn);
        phone_number = findViewById(R.id.phone_number);
        edit_number = findViewById(R.id.edit_number);
        paytm_verify_close = findViewById(R.id.paytm_verify_close);
        paytm_resent_otp = findViewById(R.id.paytm_resent_otp);

        phone_number.setText("+91 " + preferences.getUserModel().getPhoneNo());

        long interval = 1000;
        long startTime = 45 * 1000;
        countDownTimer = new MyCountDownTimer(startTime, interval);
        countDownTimer.start();

    }

    private void resentOtp() {

        //otp_view.setText("");
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("trans_id", trans_id);
            jsonObject.put("user_id", preferences.getUserModel().getId());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.PAYTM_WITHDRAW_RESEND_OTP, jsonObject, new GetApiResult() {

            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    long interval = 1000;
                    long startTime = 45 * 1000;
                    countDownTimer = new MyCountDownTimer(startTime, interval);
                    countDownTimer.start();
                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {}

        });

    }

    private void checkOtp() {
        JSONObject jsonObject = new JSONObject();
        try {
            //jsonObject.put("otp", otp_view.getText().toString());
            jsonObject.put("trans_id", trans_id);
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.PAYTM_WITHDRAW_SUBMIT, jsonObject, new GetApiResult() {

            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("TAG", "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    MyApp.getMyPreferences().setPref(PrefConstant.PAYMENT_SUCCESS,true);
                    CustomUtil.showTopSneakSuccess(mContext, responseBody.optString("msg"));
                    finish();
                } else {
                    if (responseBody.has("trans_id") && TextUtils.isEmpty(responseBody.optString("trans_id"))){
                        trans_id=responseBody.optString("trans_id");
                    }
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {}

        });
    }

    private boolean validate() {
       /* if (otp_view.getText().toString().length() != 6) {
            CustomUtil.showTopSneakError(mContext, "Please Enter Valid OTP");
            return false;
        }*/
        return true;
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            paytm_resent_otp.setText("Resend OTP");
            paytm_resent_otp.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            long min = ((millisUntilFinished / 1000) / 60);
            long sec = ((millisUntilFinished / 1000) % 60);
            paytm_resent_otp.setText("" + new DecimalFormat("00").format(min) + " : " + new DecimalFormat("00").format(sec));

            paytm_resent_otp.setClickable(false);
        }
    }

}