package com.fantafeat.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class CallbackActivity extends BaseActivity {

    LinearLayout callView;
    EditText call_msg;
    ImageView toolbar_back;
    TextView call_sub;
    TextView call_request;
    String[] call_subject;
    String selectedQuery="";

    @Override
    public void initClick() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        setContentView(R.layout.fragment_call_back);

        if (getIntent().hasExtra("selectedQuery")){
            selectedQuery=getIntent().getStringExtra("selectedQuery");
        }

        initData();
        initClic();
    }

    private void initData() {
        callView = findViewById(R.id.call_view);
        call_msg = findViewById(R.id.call_msg);
        call_sub = findViewById(R.id.call_sub);
        call_sub.setText(selectedQuery);
        call_request = findViewById(R.id.call_request);
        toolbar_back = findViewById(R.id.toolbar_back);

        toolbar_back.setOnClickListener(v->{
            finish();
        });

       // getData();
    }

   /* private void getData() {
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
    }*/

    public void initClic() {
        call_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    callAPI();
                }
            }
        });


    }

    private void callAPI() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",preferences.getUserModel().getId());
            jsonObject.put("name",preferences.getUserModel().getDisplayName());
            jsonObject.put("phone_no",preferences.getUserModel().getPhoneNo());
            jsonObject.put("subject",selectedQuery);
            jsonObject.put("message",call_msg.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.CALL_BACK_SUBMIT, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if(responseBody.optBoolean("status")){
                    LogUtil.e(TAG, "onSuccessResult: "+responseBody.toString() );
                    CustomUtil.showToast(mContext,responseBody.optString("msg"));
                    finish();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private boolean validate() {
        /*if(call_sub.getSelectedItem().toString().toLowerCase().equals("select call back subject")){
            LogUtil.e(TAG, "validate: 1" );
            CustomUtil.showTopSneakWarning(mContext,"Please select subject");
            return false;
        } else*/ if(call_msg.getText().toString().trim().length()==0){
            LogUtil.e(TAG, "validate: 2" );
            CustomUtil.showTopSneakError(mContext,"Please write the description");
            return false;
        }
        return true;
    }
}