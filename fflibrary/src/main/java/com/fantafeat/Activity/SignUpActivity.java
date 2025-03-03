package com.fantafeat.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.fantafeat.BuildConfig;
import com.fantafeat.Model.StateModal;
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
import com.fantafeat.util.PrefConstant;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public void initClick() {
    }

    private TextView txtResendCnt/*, txtUserReferralCode*/,txtTnc;//txtLogin,
    private Button btnSignup;

    private LinearLayout check,ll_checkbox;
    private EditText edtPhone, edtName, edtEmail, edtPassword, edtTeamName, edtReferralCode;

    //private CheckBox userReferralCode;
    private CountDownTimer timerr;
//    private OtpView otp_numbers;

    private String mEmail, mName, mTeam, mRef, mPhone, mPass;
    private Dialog otpDialog;
    private ImageView toolbar_back;

    private boolean referSelected = false;
    File appForRefer;
    private boolean isPermission = false;
    private boolean isSettingOpen = false;
    private boolean isResume = false;

    private CheckBox checkBox;

    private static final int REQ_USER_CONSENT = 200;
    // SmsBroadcastReceiver smsBroadcastReceiver;
  //  private Socket mSocket = null;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        setContentView(R.layout.activity_sign_up);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
//        edtReferralCode=findViewById(R.id.edtReferralCode);
        initData();
//        getStateData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isResume) {
            isResume = true;
            Dexter.withActivity((Activity) mContext)
                    .withPermissions(p)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                isPermission = false;
                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                    // if (!Environment.isExternalStorageManager()){
                                    if (!Environment.isExternalStorageManager() && !isSettingOpen) {
                                        isSettingOpen = true;
                                        LogUtil.e("resp", "R");
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                        intent.setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                                        startActivityForResult(intent, 1234);
                                    } else {
                                        checkAutoRefer();
                                    }
                                } else {
                                    checkAutoRefer();
                                }

                            }

                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                LogUtil.e(TAG, "onPermissionsChecked: ");
                                //showSettingsDialog();
                                // permission is denied permenantly, navigate user to app settings
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .onSameThread()
                    .check();
        }


    }



    private void checkAutoRefer() {
        try {
            String path = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DOWNLOADS;

            File file = new File(path);
            //mRootPath = file.getAbsoluteFile().getPath();

            ArrayList<String> mFileNames = new ArrayList<String>();

            File filesInDirectory[] = file.listFiles();

            if (filesInDirectory != null) {
                boolean referralCodeFound = false;

                for (int i = 0; i < filesInDirectory.length; i++) {
                    mFileNames.add(filesInDirectory[i].getName());
                    String fileName = filesInDirectory[i].getName();
                    LogUtil.e("resp", "Fantafeat file size: " + fileName);

                    if (fileName.contains("fantafeat-") && fileName.length() > 11) {
                        try {
                            LogUtil.e(TAG, "Fantafeat file get: " + filesInDirectory[i].getName());

                            String[] parts = fileName.split("-");

                            if (parts.length > 1) {
                                String possibleReferralCode = parts[1];

                                if (possibleReferralCode.endsWith(".apk")) {
                                    possibleReferralCode = possibleReferralCode.replace(".apk", "");
                                }
                                LogUtil.e("resp", "possibleReferralCode: " + possibleReferralCode);
                                if (possibleReferralCode.contains("(")){
                                    String[] arr=possibleReferralCode.split("\\(");
                                    possibleReferralCode=arr[0].trim();
                                }
                                LogUtil.e("resp", "possibleReferralCode: " + possibleReferralCode);
                                edtReferralCode.setText(possibleReferralCode);
                                referralCodeFound = true;
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtil.e(TAG, "Error: " + e.toString());
                        }
                    }
                }


                if (referralCodeFound) {
                    //txtUserReferralCode.setVisibility(View.GONE);
                    //userReferralCode.setVisibility(View.GONE);
                    edtReferralCode.setVisibility(View.GONE);
                } else {
                    edtReferralCode.setVisibility(View.VISIBLE);
                   // txtUserReferralCode.setVisibility(View.VISIBLE);
                   // userReferralCode.setVisibility(View.VISIBLE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        // txtLogin=findViewById(R.id.txtLogin);
        btnSignup = findViewById(R.id.btnSignup);
        edtPhone = findViewById(R.id.edtPhone);
        //userReferralCode = findViewById(R.id.userReferralCode);
        edtReferralCode = findViewById(R.id.edtReferralCode);
        txtTnc = findViewById(R.id.txtTnc);
      //  txtUserReferralCode = findViewById(R.id.txtUserReferralCode);
        //check = findViewById(R.id.check);
        edtPhone.setLongClickable(false);
        edtPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10), ConstantUtil.filter, ConstantUtil.EMOJI_FILTER});
        toolbar_back = findViewById(R.id.toolbar_back);
        ll_checkbox = findViewById(R.id.ll_checkbox);
        checkBox = findViewById(R.id.checkAge);
        btnSignup.setOnClickListener(this);
        toolbar_back.setOnClickListener(this);
        txtTnc.setOnClickListener(this);

        edtPhone.setLongClickable(false);
        edtPhone.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });

       /* userReferralCode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtReferralCode.setVisibility(View.VISIBLE);
                } else {
                    edtReferralCode.setVisibility(View.GONE);
                }
            }
        });*/
        if (BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE)){
            ll_checkbox.setVisibility(View.VISIBLE);
        }else {
            ll_checkbox.setVisibility(View.GONE);
        }
    }

    private boolean Validate() {
        mPhone = edtPhone.getText().toString().trim();

        if (mPhone.equals("")) {
            CustomUtil.showTopSneakWarning(mContext, "Please Enter Your Phone Number");
            return false;
        } else if (mPhone.length() != 10) {
            CustomUtil.showTopSneakWarning(mContext, "Please Enter Valid Phone Number");
            return false;
        }else if (!checkBox.isChecked() && BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE)){
            CustomUtil.showTopSneakWarning(mContext, "Confirm you are above 18 years.");
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerr != null) {
            timerr.cancel();
            timerr = null;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();/* case R.id.txtLogin:{
                finish();
                break;
            }*/
        if (id == R.id.btnSignup) {
            if (MyApp.getClickStatus()) {
                if (Validate()) {
                    mRef = edtReferralCode.getText().toString();
                    makeApiCall();
                }
            }
        } else if (id == R.id.toolbar_back) {
            if (MyApp.getClickStatus()) {
                finish();
            }
        } else if (id == R.id.txtTnc) {
            startActivity(new Intent(mContext, WebViewActivity.class)
                    .putExtra(ConstantUtil.WEB_TITLE, "Terms & Condition")
                    .putExtra(ConstantUtil.WEB_URL, ApiManager.term_condition_url));
        }
    }

    private void makeApiCall() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone_no", mPhone);
            jsonObject.put("ref_by", mRef);
            jsonObject.put("type", "register");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e("param", jsonObject + " ");
        HttpRestClient.postJSON(mContext, true, ApiManager.REGISTER, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.d("logSucc", responseBody + " ");
                if (responseBody.optBoolean("status")) {
                    JSONObject data = responseBody.optJSONObject("data");

                    MyApp.USER_ID = data.optString("id");
                    MyApp.APP_KEY = data.optString("token_no");

                    initSocket("register", data.optString("id"));

                    showOtp();

                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void initSocket(String title, String user_id) {
        /*mSocket = MyApp.getInstance().getSocketInstance();
        if (mSocket != null) {
            if (!mSocket.connected())
                mSocket.connect();
            try {
                JSONObject obj = new JSONObject();
                obj.put("user_id", user_id);
                obj.put("title", title);
                mSocket.emit("connect_user", obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
    }

    private void showOtp() {
        //startSmsUserConsent();

        View view = getLayoutInflater().inflate(R.layout.fragment_otp, null);
        otpDialog = new BottomSheetDialog(SignUpActivity.this);
        otpDialog.setCancelable(false);
        otpDialog.setContentView(view);
        otpDialog.show();
       // otp_numbers = view.findViewById(R.id.otp_numbers);
        txtResendCnt = view.findViewById(R.id.txtResendCnt);
        startTimer();
        ImageView imgClose = view.findViewById(R.id.imgClose);
        Button otp_btn = view.findViewById(R.id.otp_btn);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otpDialog.dismiss();
            }
        });
        txtResendCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtResendCnt.getText().toString().equals("Resend")) {
                    if (MyApp.getClickStatus()) {
                        if (timerr != null) {
                            timerr.cancel();
                            timerr = null;
                        }
                        //showResendDialog();
                        if (preferences.getUpdateMaster() != null && !TextUtils.isEmpty(preferences.getUpdateMaster().getIs_whatsapp_enable()) &&
                                preferences.getUpdateMaster().getIs_whatsapp_enable().equalsIgnoreCase("yes")) {
                            showResendDialog();
                        } else {
                            resendOtp();
                        }
                    }
                }
            }
        });

        otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    /*if (!TextUtils.isEmpty(otp_numbers.getText())) {
                        checkOtp();
                    }*/
                }
            }
        });
    }

    private void checkOtp() {
        JSONObject jsonObject = new JSONObject();
        try {
            //jsonObject.put("otp", otp_numbers.getText().toString());
            jsonObject.put("user_id", MyApp.USER_ID);
            jsonObject.put("token_no", MyApp.tokenNo);

            LogUtil.e("otppp", jsonObject + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.AUTHV3OTP_VERIFY, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("TAG", "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {

                    if (otpDialog != null && otpDialog.isShowing()) {
                        otpDialog.dismiss();
                    }

                    JSONObject data = responseBody.optJSONObject("data");
                    UserModel userModel = preferences.getUserModel();
                    MyApp.USER_ID = userModel.getId();
                    MyApp.APP_KEY = userModel.getTokenNo();

                    preferences.setUserModel(userModel);

                    initSocket("otpverify", userModel.getId());

                    UpdateUserDetail();

                    /*if (preferences.getUserModel().getUserTeamName() != null && !preferences.getUserModel().getUserTeamName().equals("") &&
                            preferences.getUserModel().getEmailId() != null && !preferences.getUserModel().getEmailId().equals("") &&
                            preferences.getUserModel().getDisplayName() != null && !preferences.getUserModel().getDisplayName().equals("")) {
                        //new password*/
                    /*Intent intent = new Intent(mContext, SplashActivity.class);
                    startActivity(intent);
                    ((LoginActivity) mContext).finish();*/
                    /*  } else {
                        new FragmentUtil().addFragment((FragmentActivity) mContext,
                                R.id.login_fragment_container,
                                new BasicDetailFragment(),
                                fragmentTag(6),
                                FragmentUtil.ANIMATION_TYPE.SLIDE_LEFT_TO_RIGHT);
                    }*/
                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                LogUtil.e("TAG", "onFailureResult: " + responseBody.toString());
            }
        });
    }

    private void UpdateUserDetail() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MyApp.USER_ID);
            jsonObject.put("ref_by", mRef);
           /* jsonObject.put("email_id", mEmail);
            jsonObject.put("user_team_name", mTeam);
            jsonObject.put("display_name",mName);*/
//            jsonObject.put("password", mPass);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, false, ApiManager.UPDATE_USER_DETAILS, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    /*UserModel userModel = preferences.getUserModel();
                    userModel.setEmailId(Objects.requireNonNull(mDetailEmail));
                    userModel.setUserTeamName(mDetailUserName);
                    userModel.setDisplayName(mDetailFullName);*/
                    UserModel userModel = preferences.getUserModel();
                  /*  userModel.setEmailId(Objects.requireNonNull(edtEmail.getText()).toString().trim());
                    userModel.setUserTeamName(edtTeamName.getText().toString().trim());
                    userModel.setDisplayName(edtName.getText().toString().trim());*/
                    preferences.setUserModel(userModel);
                    //  preferences.setUserModel(userModel);
                    preferences.setPref(PrefConstant.APP_IS_LOGIN, true);
                    if (isPermission) {
                        deleteStoredApks();
                    }
                    Intent intent = new Intent(mContext, HomeActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void deleteStoredApks() {
        try {
            String downPath = Environment.getExternalStorageDirectory() + "/Download/";
            LogUtil.e("downPath", downPath + " ");
            File downFile = new File(downPath);
            File[] allFiles = downFile.listFiles();
            LogUtil.e("downPath", allFiles.length + " ");
            // List<String> pathList=new ArrayList<>();

            for (File file : allFiles) {
                // LogUtil.e("downPath",file.getName()+" name");
                // File file=new File(path);
                if (file.exists() && !file.isDirectory()) {
                    LogUtil.e("downPath", file.getName() + " ");
                    if (file.getName().toLowerCase().contains("fantafeat")) {
                        //pathList.add(path);
                        // boolean isdelete=deleteFile(file.getAbsolutePath());
                        boolean isdelete = file.getCanonicalFile().delete();
                        LogUtil.e("downPath", " isdelete " + isdelete);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void resendOtp() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MyApp.USER_ID);
            jsonObject.put("token_no", MyApp.APP_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.AUTHV3_Resend_OTP_VERIFY, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("resp", "resendOtp: " + responseBody);
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
                LogUtil.e("onFailureResult", responseBody.toString());
            }
        });
    }

    private void resendWPOtp() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MyApp.USER_ID);
            jsonObject.put("token_no", MyApp.APP_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.LOGIN_OTP_WP_RESEND, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
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

    private void showResendDialog() {
        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.otp_medium);

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout layWp = dialog.findViewById(R.id.layWp);
        LinearLayout layText = dialog.findViewById(R.id.layText);
        TextView btnCancel = dialog.findViewById(R.id.btnCancel);

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

    private void startTimer() {
        if (timerr != null) {
            timerr.cancel();
            timerr = null;
        }
        timerr = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished / 1000;
                txtResendCnt.setText("00:" + millis);
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
                LogUtil.e("resp", "On Success");
                //   Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                LogUtil.e("resp", "On OnFailure");
                //   Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
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
        } else if (requestCode == 1234 && resultCode == Activity.RESULT_OK) {
            checkAutoRefer();
        }
    }

    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            /*if (otp_numbers != null && matcher.group(0) != null) {
                otp_numbers.setText(matcher.group(0));
            }*/
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
        //unregisterReceiver(smsBroadcastReceiver);
    }

}