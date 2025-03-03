package com.fantafeat.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.Activity.LoginActivity;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseLoginFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.fantafeat.Session.BaseFragment.isValidEmail;

public class BasicDetailFragment extends BaseLoginFragment {

    EditText phone_number_register,full_name,user_team_name,referal_code,password,email;
    Button submit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basic_detail, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initControl(View view) {
        phone_number_register = view.findViewById(R.id.phone_number_register);
        full_name = view.findViewById(R.id.detail_full_name);
        email = view.findViewById(R.id.detail_email);
        user_team_name = view.findViewById(R.id.detail_user_name);
        referal_code = view.findViewById(R.id.detail_refer_no);
        password = view.findViewById(R.id.password);
        submit = view.findViewById(R.id.basic_detail_btn);
    }

    @Override
    public void initClick() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    hideKeyboard((Activity)mContext);
                    if(Validate()){
                        UpdateUserDetail();
                    }
                }
            }
        });
    }

    private void UpdateUserDetail() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("email_id", email.getText().toString().trim());
            jsonObject.put("user_team_name", user_team_name.getText().toString().trim());
            jsonObject.put("display_name", full_name.getText().toString().trim());
            /*jsonObject.put("display_name", full_name.getText().toString().trim());
            jsonObject.put("display_name", full_name.getText().toString().trim());*/
            jsonObject.put("ref_by", referal_code.getText().toString().trim().toUpperCase());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.UPDATE_USER_DETAILS, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    UserModel userModel = preferences.getUserModel();
                    userModel.setEmailId(Objects.requireNonNull(email.getText()).toString().trim());
                    userModel.setUserTeamName(user_team_name.getText().toString().trim());
                    userModel.setDisplayName(full_name.getText().toString().trim());
                    preferences.setUserModel(userModel);

                    Intent intent = new Intent(mContext, HomeActivity.class);
                    startActivity(intent);
                    ((LoginActivity)mContext).finish();
                } else {
                    CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private boolean Validate() {
        if (phone_number_register.getText().toString().trim().equals("")) {
            CustomUtil.showTopSneakError(mContext,"Please Enter Your Phone Number");
            return false;
        }
        else if (full_name.getText().toString().trim().equals("")) {
            CustomUtil.showTopSneakError(mContext,"Please Enter Your Name");
            return false;
        }
        else if (!full_name.getText().toString().trim().matches(ConstantUtil.Pattern_NAME)) {
            CustomUtil.showTopSneakError(mContext,"Special characters not allowed in Name");
            return false;
        }
        else if (user_team_name.getText().toString().trim().equals("")) {
            CustomUtil.showTopSneakError(mContext,"Please Enter Team Name");
            return false;
        }
        else if (user_team_name.getText().toString().trim().contains(" ")) {
            CustomUtil.showTopSneakError(mContext,"Space not Allow in Team Name");
            return false;
        }
        else if (!user_team_name.getText().toString().trim().matches(ConstantUtil.Pattern_NAME)) {
            CustomUtil.showTopSneakError(mContext,"Special characters not allowed in User Name");
            return false;
        }
        else if (!isValidEmail(email.getText().toString().trim())) {
            CustomUtil.showTopSneakError(mContext,"Please Enter Valid Email");
            return false;
        }
        else if (TextUtils.isEmpty(password.getText().toString().trim())) {
            CustomUtil.showTopSneakError(mContext,"Please Enter Password");
            return false;
        }
        else if (!referal_code.getText().toString().trim().equals("") &&
                !referal_code.getText().toString().trim().matches(ConstantUtil.Pattern_NAME)) {
            CustomUtil.showTopSneakError(mContext,"Please Enter Valid Refer Code");
            return false;
        }
        else if (!referal_code.getText().toString().trim().equals("") &&
                !referal_code.getText().toString().trim().contains("")) {
            CustomUtil.showTopSneakError(mContext,"Please Enter Valid Refer Code");
            return false;
        }
        return true;
    }
}