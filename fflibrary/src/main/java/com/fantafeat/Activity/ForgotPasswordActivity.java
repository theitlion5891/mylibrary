package com.fantafeat.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordActivity extends BaseActivity {

    @Override
    public void initClick() {

    }

    private TextView txtResendCnt;
    private ImageView imgBack;
    private Button btnNext;
    private EditText edtPhone;
    private String number="";
//    private OtpView otp_numbers;
    private CountDownTimer timerr;
    private BottomSheetDialog dialog=null;

    private static final int REQ_USER_CONSENT = 200;
   // SmsBroadcastReceiver smsBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        setContentView(R.layout.activity_forgot_password);

        initData();
    }

    private void initData() {
        btnNext=findViewById(R.id.btnNext);
        edtPhone=findViewById(R.id.edtPhone);
       // edtPhone.setFilters(new InputFilter[] {ConstantUtil.filter,ConstantUtil.EMOJI_FILTER });
        edtPhone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10), ConstantUtil.filter,ConstantUtil.EMOJI_FILTER });
        edtPhone.requestFocus();
        edtPhone.setLongClickable(false);
        imgBack = findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()){
                    number=getEditText(edtPhone);
                    if (TextUtils.isEmpty(number)){
                        CustomUtil.showTopSneakWarning(mContext,"Please Enter Valid Number");
                        return;
                    }
                    if(number.length()!= 10){
                        CustomUtil.showTopSneakWarning(mContext,"Please Enter Valid Number");
                        return ;
                    }
                    submitData();
                }
            }
        });

        edtPhone.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_DONE){
                number=getEditText(edtPhone);
                if (TextUtils.isEmpty(number)){
                    CustomUtil.showTopSneakWarning(mContext,"Please Enter Valid Number");
                    return false;
                }
                if(number.length()!= 10){
                    CustomUtil.showTopSneakWarning(mContext,"Please Enter Valid Number");
                    return false;
                }
                submitData();
                return true;
            }
            return false;
        });

    }

    private void submitData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_name",number);
            jsonObject.put("forgot_password","Yes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, true, ApiManager.LOGIN, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("TAG", "onSuccessResult: " + responseBody.toString() );
                if(responseBody.optBoolean("status")){
                    JSONObject data = responseBody.optJSONObject("data");
                    MyApp.USER_ID = data.optString("id");
                    MyApp.APP_KEY = data.optString("token_no");
                    showOtp();
                }else{
                    CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void showOtp() {
        //startSmsUserConsent();

        View view = getLayoutInflater().inflate(R.layout.fragment_otp, null);
        dialog = new BottomSheetDialog(ForgotPasswordActivity.this);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        dialog.show();
        //otp_numbers=view.findViewById(R.id.otp_numbers);
        txtResendCnt=view.findViewById(R.id.txtResendCnt);
        startTimer();
        ImageView imgClose=view.findViewById(R.id.imgClose);
        Button otp_btn=view.findViewById(R.id.otp_btn);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        txtResendCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtResendCnt.getText().toString().equals("Resend")){
                    if (MyApp.getClickStatus()){
                        if (timerr!=null){
                            timerr.cancel();
                            timerr=null;
                        }
                        //
                        if (preferences.getUpdateMaster()!=null && !TextUtils.isEmpty(preferences.getUpdateMaster().getIs_whatsapp_enable()) &&
                                preferences.getUpdateMaster().getIs_whatsapp_enable().equalsIgnoreCase("yes")){
                            showResendDialog();
                        }else {
                            resendOtp();
                        }

                    }
                }
            }
        });

        otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()){
                    /*if (!TextUtils.isEmpty(otp_numbers.getText())){
                        checkOtp();
                    }*/
                }
            }
        });
    }

    private void resendWPOtp() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MyApp.USER_ID);
            jsonObject.put("token_no",MyApp.APP_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.LOGIN_OTP_WP_RESEND, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("resp","resendOtp: "+responseBody);
                if (responseBody.optBoolean("status")) {
                    CustomUtil.showTopSneakSuccess(mContext, responseBody.optString("msg"));
                    JSONObject data = responseBody.optJSONObject("data");
                    MyApp.USER_ID = data.optString("id");
                    MyApp.APP_KEY = data.optString("token_no");
                    startTimer();
                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void showResendDialog(){
        Dialog dialog=new Dialog(mContext);
        dialog.setContentView(R.layout.otp_medium);

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout layWp=dialog.findViewById(R.id.layWp);
        LinearLayout layText=dialog.findViewById(R.id.layText);
        TextView btnCancel=dialog.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        layWp.setOnClickListener(v -> {
            if (MyApp.getClickStatus()) {
                dialog.dismiss();
                resendWPOtp();
            }
        });

        layText.setOnClickListener(v -> {
            if (MyApp.getClickStatus()) {
                dialog.dismiss();
                resendOtp();
            }
        });

        dialog.show();
    }

    private void checkOtp() {
        JSONObject jsonObject = new JSONObject();
      /*  try {
           // jsonObject.put("otp", otp_numbers.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        HttpRestClient.postJSON(mContext, true, ApiManager.OTP_VERIFY, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code){
                LogUtil.e("TAG", "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONObject data = responseBody.optJSONObject("data");
                    UserModel userModel = gson.fromJson(data.toString(), UserModel.class);
                    MyApp.USER_ID = userModel.getId();
                    MyApp.APP_KEY = userModel.getTokenNo();
                    LogUtil.e(TAG, "onSuccessResult: " + gson.toJson(userModel));
                    //float total = CustomUtil.convertFloat(userModel.getDepositBal()) + CustomUtil.convertFloat(userModel.getWinBal())
                    //        + CustomUtil.convertFloat(userModel.getTransferBal()) + CustomUtil.convertFloat(userModel.getBonusBal());
                    //userModel.setTotal_balance(total);
                    preferences.setUserModel(userModel);

                    if (dialog!=null && dialog.isShowing()){
                        dialog.dismiss();
                    }

                    startActivity(new Intent(ForgotPasswordActivity.this,NewPasswordActivity.class)
                    .putExtra("phoneNumber",number));

                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }

        });
    }

    private void resendOtp() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MyApp.USER_ID);
            jsonObject.put("token_no",MyApp.APP_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.LOGIN_OTP_RESEND, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("resp","resendOtp: "+responseBody);
                if (responseBody.optBoolean("status")) {
                    CustomUtil.showTopSneakSuccess(mContext, responseBody.optString("msg"));
                    JSONObject data = responseBody.optJSONObject("data");
                    MyApp.USER_ID = data.optString("id");
                    MyApp.APP_KEY = data.optString("token_no");
                    startTimer();
                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void startTimer() {
        if (timerr!=null){
            timerr.cancel();
            timerr=null;
        }
        timerr=new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished / 1000;
                txtResendCnt.setText("00:"+ millis) ;
            }

            @Override
            public void onFinish() {
                txtResendCnt.setText("Resend");
            }
        };

        timerr.start();
    }

    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               // Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);

                getOtpFromMessage(message);
            }
        }
    }

    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
        //    otp_numbers.setText(matcher.group(0));
        }
    }

    private void registerBroadcastReceiver() {
        /*smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
                    }
                    @Override
                    public void onFailure() {
                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        //registerBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
       // unregisterReceiver(smsBroadcastReceiver);
    }

}