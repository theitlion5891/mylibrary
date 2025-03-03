package com.fantafeat.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CallBackFragment extends BaseFragment {

    LinearLayout callView;
    EditText call_msg;
    ImageView btnCall,btnEmail;
    Spinner call_sub;
    TextView call_request;
    String[] call_subject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_back, container, false);
        initFragment(view);
        initToolBar(view, "Call back", true);
        return view;
    }

    @Override
    public void initControl(View view) {
        callView = view.findViewById(R.id.call_view);
        call_msg = view.findViewById(R.id.call_msg);
        call_sub = view.findViewById(R.id.call_sub);
        call_request = view.findViewById(R.id.call_request);
        btnEmail = view.findViewById(R.id.btnEmail);
        btnCall = view.findViewById(R.id.btnCall);

        getData();
    }

    private void getData() {
        HttpRestClient.postJSON(mContext, true, ApiManager.CALLBACK_SUB_TYPE, new JSONObject(), new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                    JSONArray jsonArray = responseBody.optJSONArray("data");
                    call_subject = new String[jsonArray.length() + 1];
                    call_subject[0] = "Select Call Back Subject";
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            call_subject[i + 1] = jsonArray.getString(i);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, R.layout.spinner_text, call_subject);
                    call_sub.setAdapter(adapter);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    @Override
    public void initClick() {
        call_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    callAPI();
                }
            }
        });

        btnCall.setOnClickListener(view -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+ApiManager.support_phone));//"tel:+919625449625"
            startActivity(callIntent);
        });

        btnEmail.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto",ApiManager.support_email, null));
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        });
    }

    private void callAPI() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",preferences.getUserModel().getId());
            jsonObject.put("name",preferences.getUserModel().getDisplayName());
            jsonObject.put("phone_no",preferences.getUserModel().getPhoneNo());
            jsonObject.put("subject",call_sub.getSelectedItem().toString());
            jsonObject.put("message",call_msg.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.CALL_BACK_SUBMIT, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if(responseBody.optBoolean("status")){
                    LogUtil.e(TAG, "onSuccessResult: "+responseBody.toString() );
                    CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                    RemoveFragment();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private boolean validate() {
        if(call_sub.getSelectedItem().toString().toLowerCase().equals("select call back subject")){
            LogUtil.e(TAG, "validate: 1" );
            CustomUtil.showTopSneakError(mContext,"Please select subject");
            return false;
        } else if(call_msg.getText().toString().trim().length()==0){
            LogUtil.e(TAG, "validate: 2" );
            CustomUtil.showTopSneakError(mContext,"Please write the description");
            return false;
        }
        return true;
    }
}