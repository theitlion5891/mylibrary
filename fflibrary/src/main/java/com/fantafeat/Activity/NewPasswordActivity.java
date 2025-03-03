package com.fantafeat.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.PrefConstant;

import org.json.JSONException;
import org.json.JSONObject;

public class NewPasswordActivity extends BaseActivity {

    @Override
    public void initClick() {

    }

    private TextView txtNumber,txtEdit;
    private EditText edtPassword;
    private Button btnNext;
    private String phoneNumber="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        setContentView(R.layout.activity_new_password);

        initData();
    }

    private void initData() {
        txtNumber=findViewById(R.id.txtNumber);
        txtEdit=findViewById(R.id.txtEdit);
        edtPassword=findViewById(R.id.edtPassword);
        edtPassword.setLongClickable(false);
        btnNext=findViewById(R.id.btnNext);

        if (getIntent().hasExtra("phoneNumber")){
            phoneNumber=getIntent().getStringExtra("phoneNumber");
            txtNumber.setText(phoneNumber);
        }

        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()){
                    String pwd=getEditText(edtPassword);

                    if (TextUtils.isEmpty(pwd)){
                        CustomUtil.showTopSneakWarning(mContext,"Enter Password");
                        return;
                    }
                    if (pwd.length()<6){
                        CustomUtil.showTopSneakWarning(mContext,"Enter atlease 6 characters of password");
                        return;
                    }
                    else if (pwd.length()>16) {
                        CustomUtil.showTopSneakWarning(mContext,"Enter maximum 16 characters password");
                        return ;
                    }
                    submitPwd();
                }
            }
        });

        edtPassword.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_DONE){
                String pwd=getEditText(edtPassword);

                if (TextUtils.isEmpty(pwd)){
                    CustomUtil.showTopSneakWarning(mContext,"Enter Password");
                    return false;
                }
                if (pwd.length()<6){
                    CustomUtil.showTopSneakWarning(mContext,"Enter atlease 6 characters of password");
                    return false;
                }
                else if (pwd.length()>16) {
                    CustomUtil.showTopSneakWarning(mContext,"Enter maximum 16 characters password");
                    return false;
                }
                submitPwd();
                return true;
            }
            return false;
        });
    }

    private void submitPwd() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("password", getEditText(edtPassword));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.UPDATE_USER_DETAILS, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {

                    /*new FragmentUtil().addFragment((FragmentActivity) mContext,
                            R.id.login_fragment_container,
                            new SuccessFragment(),
                            fragmentTag(35),
                            FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);*/
                    CustomUtil.showTopSneakSuccess(mContext, "Password changed successfully.");
                    preferences.setPref(PrefConstant.APP_IS_LOGIN,true);

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
}