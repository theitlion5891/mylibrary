package com.fantafeat.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class PaytmVerifyFragment extends BaseFragment {

  //  private OtpView otp_view;
    private Button otp_btn;
    private TextView phone_number, edit_number;
    private ImageView paytm_verify_close;
    private TextView paytm_resent_otp;

    private String trans_id, amount;

    private CountDownTimer countDownTimer;

    public PaytmVerifyFragment(String trans_id, String amount) {
        this.trans_id = trans_id;
        this.amount = amount;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_paytm_verify, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initControl(View view) {
       // otp_view = view.findViewById(R.id.otp_view);
        otp_btn = view.findViewById(R.id.otp_btn);
        phone_number = view.findViewById(R.id.phone_number);
        edit_number = view.findViewById(R.id.edit_number);
        paytm_verify_close = view.findViewById(R.id.paytm_verify_close);
        paytm_resent_otp = view.findViewById(R.id.paytm_resent_otp);

        phone_number.setText("+91 " + preferences.getUserModel().getPhoneNo());

        long interval = 1000;
        long startTime = 45 * 1000;
        countDownTimer = new MyCountDownTimer(startTime, interval);
        countDownTimer.start();

    }

    @Override
    public void initClick() {
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

        /*otp_view.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                hideKeyboard((Activity) mContext);
            }
        });*/
        paytm_verify_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveFragment();
            }
        });

        paytm_resent_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resentOtp();
            }
        });

        /*edit_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveFragment();
            }
        });*/

    }

    private void resentOtp() {
        //otp_view.setText("");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("trans_id", trans_id);
            jsonObject.put("user_id", preferences.getUserModel().getId());
        } catch (JSONException e) {
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
            public void onFailureResult(String responseBody, int code) {

            }
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
                    CustomUtil.showTopSneakSuccess(mContext, responseBody.optString("msg"));
                    RemoveFragment();
                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private boolean validate() {
        /*if (otp_view.getText().toString().length() != 6) {
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