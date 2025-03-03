package com.fantafeat.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONObject;

public class CallbackSelectQueryActivity extends BaseActivity {

    private RelativeLayout layQuery;
    private ChipGroup chipsPrograms;
    int prevTextViewId = 0;
    ImageView btnCall,btnEmail,toolbar_back,imgTeleMsg;
    TextView call_request;
    String selectedQuery="";

    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        setContentView(R.layout.activity_callback_select_query);
        initData();
    }

    private void initData() {
       // layQuery=findViewById(R.id.layQuery);
        chipsPrograms=findViewById(R.id.chipsPrograms);
        btnEmail = findViewById(R.id.btnEmail);
        imgTeleMsg = findViewById(R.id.imgTeleMsg);
        btnCall = findViewById(R.id.btnCall);
        call_request = findViewById(R.id.call_request);
        toolbar_back = findViewById(R.id.toolbar_back);

        toolbar_back.setOnClickListener(v->{
            finish();
        });

        call_request.setOnClickListener(v->{
            selectedQuery="";
            for (int i=0;i<chipsPrograms.getChildCount();i++){
                Chip chip= (Chip) chipsPrograms.getChildAt(i);
                if (chip.isChecked()){
                    selectedQuery=chip.getText().toString().trim();
                }
            }
            if (TextUtils.isEmpty(selectedQuery)){
                CustomUtil.showTopSneakError(this,"Select your query");
                return;
            }
            startActivity(new Intent(mContext, CallbackActivity.class)
            .putExtra("selectedQuery",selectedQuery));
        });

        btnCall.setOnClickListener(view -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+ApiManager.support_phone));
            startActivity(callIntent);
        });

        imgTeleMsg.setOnClickListener(view -> {
            try {
                String url = ApiManager.telegram_id;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

                /*String url = "https://t.me/"+ApiManager.support_username;//2122548406
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setPackage("org.telegram.messenger");
                i.setData(Uri.parse(url));
                startActivity(i);*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        btnEmail.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto",ApiManager.support_email, null));
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        });

        getData();
    }

    private void getData() {
        HttpRestClient.postJSON(this, true, ApiManager.CALLBACK_SUB_TYPE, new JSONObject(), new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                    JSONArray jsonArray = responseBody.optJSONArray("data");
                    displayData(jsonArray);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void displayData(JSONArray jsonArray) {

        for (int i =0 ;i<jsonArray.length();i++){
            String query=jsonArray.optString(i);

            /*Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.item_chip_category, null, false);
            mChip.setText(query);
            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10,
                    getResources().getDisplayMetrics()
            );
            mChip.setPadding(paddingDp, 0, paddingDp, 0);
                mChip.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (b){
                        selectedQuery=mChip.getText().toString().trim();
                        LogUtil.e("selectedQuery",selectedQuery+" ");
                    }
            });

            //mChip.setOnClickListener();
            chipsPrograms.addView(mChip);*/

        }
    }

}