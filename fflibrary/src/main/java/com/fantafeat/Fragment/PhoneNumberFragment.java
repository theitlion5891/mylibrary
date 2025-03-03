package com.fantafeat.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.Activity.LoginActivity;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseLoginFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;

import org.json.JSONException;
import org.json.JSONObject;

public class PhoneNumberFragment extends BaseLoginFragment {

    EditText phone_num,password;
    Button login_button;
    TextView welcom_text,extra_text,forgot_passwrd,new_user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_phone_number, container, false);
        initFragment(view);

        return view;
    }

    private boolean checkNumber() {
        /*if(phone_num.getText().toString().trim().equals("")){
            Sneaker.with((Activity) mContext)
                    .setMessage("Please Enter Valid Number")
                    .sneakError();
            return false;
        }else if(phone_num.getText().toString().trim().length()!= 10){
            Sneaker.with((Activity) mContext)
                    .setMessage("Please Enter Valid Number")
                    .sneakError();
            return false;
        }else if(password.getText().toString().trim().equals("")){
            Sneaker.with((Activity) mContext)
                    .setMessage("Please Enter Password")
                    .sneakError();
            return false;
        }*/
        return true;
    }

    private void makeRequest(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_name",phone_num.getText().toString().trim());
            jsonObject.put("password",password.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "makeRequest: "+ jsonObject.toString() );
        HttpRestClient.postJSON(mContext, true, ApiManager.LOGIN, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if(responseBody.optBoolean("status")){
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody.optString("data"));
                        UserModel userModel = gson.fromJson(jsonObject.toString(), UserModel.class);
                        MyApp.USER_ID = jsonObject.optString("id");
                        MyApp.APP_KEY = jsonObject.optString("token_no");
                        preferences.setUserModel(userModel);

                        Intent intent = new Intent(mContext, HomeActivity.class);
                        startActivity(intent);
                        ((LoginActivity)mContext).finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    CustomUtil.showSnackBarLong(mContext,responseBody.optString("data"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }


    @Override
    public void initControl(View view) {
        phone_num = view.findViewById(R.id.phone_number);
        login_button = view.findViewById(R.id.login_button);
        welcom_text = view.findViewById(R.id.welcome_back);
        extra_text = view.findViewById(R.id.welcome_detail_text);
        forgot_passwrd = view.findViewById(R.id.forgot_passwrd);
        new_user = view.findViewById(R.id.new_user);
        password = view.findViewById(R.id.password);
    }

    @Override
    public void initClick() {
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String phone_number = phone_num.getText().toString();
                if(checkNumber()){
                    makeRequest();
                }
            }
        });

        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        forgot_passwrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}