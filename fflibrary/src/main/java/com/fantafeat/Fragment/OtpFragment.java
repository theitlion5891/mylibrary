package com.fantafeat.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.Activity.LoginActivity;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseLoginFragment;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;

import org.json.JSONException;
import org.json.JSONObject;


public class OtpFragment extends BaseLoginFragment {

    private Button otp_button;
    //private OtpView otpView;
    private String id, token_no, otp_num;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_otp, container, false);
        initFragment(view);

        return view;
    }

    @Override
    public void initControl(View view) {
        otp_button = view.findViewById(R.id.otp_btn);
       // otpView = view.findViewById(R.id.otp_numbers);
    }

    @Override
    public void initClick() {
        /*otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                otp_num = otp;
                hideKeyboard((Activity) mContext);
            }
        });*/

        otp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequest();
            }
        });
    }

    private void makeRequest() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("otp", otp_num);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, false, ApiManager.OTP, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {

                    UserModel userModel = gson.fromJson(responseBody.optString("data"), UserModel.class);
                    preferences.setUserModel(userModel);
                    Log.e(TAG, "onSuccessResult: Successful" + responseBody.toString());
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(responseBody.optString("data"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (jsonObj.optString("display_name").equals("") || jsonObj.optString("display_name").equals("null") ||
                            jsonObj.optString("email_id").equals("") || jsonObj.optString("email_id").equals("null") ||
                            jsonObj.optString("user_team_name").equals("") || jsonObj.optString("user_team_name").equals("null")) {
                       /* new FragmentUtil().replaceFragment(getActivity(),
                                R.id.login_fragment,
                                new BasicDetailFragment(),
                                fragmentTag(6),
                                FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);*/
                    }else{

                        Intent intent = new Intent(mContext, HomeActivity.class);
                        startActivity(intent);
                        ((LoginActivity)mContext).finish();
                    }
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }
}