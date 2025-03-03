package com.fantafeat.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fantafeat.Adapter.PromoteFieldAdapter;
import com.fantafeat.Model.PromoteChanelItem;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PromoteAppFormActivity extends BaseActivity {

    private ImageView toolbar_back;
    private TextView txtAdd;
    private Spinner spinState;
    private EditText edtName,edtEmail,edtPhone, edtCity;
    private RecyclerView recyclerChanel;
    private PromoteFieldAdapter adapter;
    private ArrayList<PromoteChanelItem> list;
    private ArrayList<String> cityName;
//    private ArrayList<String> cityId;
    private Button btnSubmit;
    private String selectedState="Select state";

    @Override
    public void initClick() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        setContentView(R.layout.activity_promote_app_form);
        initData();
        iniClick();
        getStateData();
    }

    private void getStateData() {
        HttpRestClient.postData(this, true, ApiManager.STATE_LIST, new JSONObject(), new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                try {
                    LogUtil.e(TAG, "onSuccessResult: " + responseBody);
                    cityName=new ArrayList<String>();
                    cityName.add("Select state");
                  /*  cityId=new ArrayList<String>();
                    cityId.add("0");*/
                    if(responseBody.optBoolean("status")){

                        JSONArray jsonArray = responseBody.getJSONArray("data");
                        for (int i =0;i<jsonArray.length();i++){
                            JSONObject obj=jsonArray.getJSONObject(i);
                            String name = obj.optString("name");
                            /*String id = obj.optString("id");
                            cityId.add(id);*/
                            cityName.add(name);
                        }
                    }
                    ArrayAdapter<String> stateAdapter=new ArrayAdapter<String>(PromoteAppFormActivity.this,R.layout.spinner_text,cityName);
                    spinState.setAdapter(stateAdapter);
                    spinState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                            selectedState=cityName.get(pos);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    protected void setStatusBarDark() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blackSecondary));
        }
    }

    private void iniClick() {
        toolbar_back.setOnClickListener(view->{
            finish();
        });
        txtAdd.setOnClickListener(view->{
            if (MyApp.getClickStatus()){
                addMoreFields();
            }
        });
    }

    private void addMoreFields() {
        if (list.size()!=5){
            list.add(new PromoteChanelItem(list.size()+1));
            adapter.notifyDataSetChanged();
        }
    }

    private Boolean isValid(){
        String name=edtName.getText().toString().trim();
        String email=edtEmail.getText().toString().trim();
        String phone=edtPhone.getText().toString().trim();
        String city=edtCity.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            CustomUtil.showTopSneakError(this,"Enter Name");
            return false;
        }
        if (TextUtils.isEmpty(email)){
            CustomUtil.showTopSneakError(this,"Enter Email");
            return false;
        }
        if (TextUtils.isEmpty(phone)){
            CustomUtil.showTopSneakError(this,"Enter Phone Number");
            return false;
        }
        if (phone.length()!=10){
            CustomUtil.showTopSneakError(this,"Enter valid Phone Number");
            return false;
        }
        if (selectedState.equals("Select state")){
            CustomUtil.showTopSneakError(this,"Select State");
            return false;
        }
        if (TextUtils.isEmpty(city)){
            CustomUtil.showTopSneakError(this,"Enter City");
            return false;
        }

        for (int i = 0;i<list.size();i++){
            PromoteChanelItem item=list.get(i);
            if (item.getCh_type().equals("Select Chanel Type")){
                CustomUtil.showTopSneakError(this,"Select Chanel Type");
                return false;
            }
            if (TextUtils.isEmpty(item.getCh_name())){
                CustomUtil.showTopSneakError(this,"Enter Chanel Name");
                return  false;
            }
            if (item.getCh_type().equals("Other")){
                if (TextUtils.isEmpty(item.getCh_url())){
                    CustomUtil.showTopSneakError(this,"Enter Chanel Url");
                    return false;
                }
            }
        }
        return true;
    }

    private void initData() {
        list=new ArrayList<PromoteChanelItem>();
        toolbar_back=findViewById(R.id.toolbar_back);
        recyclerChanel=findViewById(R.id.recyclerChanel);
        spinState=findViewById(R.id.spinState);
        txtAdd=findViewById(R.id.txtAdd);
        edtName=findViewById(R.id.edtName);
        edtEmail=findViewById(R.id.edtEmail);
        edtPhone=findViewById(R.id.edtPhone);
        edtCity=findViewById(R.id.edtCity);
        btnSubmit=findViewById(R.id.btnSubmit);

        RecyclerView.LayoutManager manager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerChanel.setLayoutManager(manager);

        list.add(new PromoteChanelItem(list.size()+1));

        adapter=new PromoteFieldAdapter(this, list, position -> {
            list.remove(position);
            adapter.notifyDataSetChanged();
        });
        recyclerChanel.setAdapter(adapter);

        btnSubmit.setOnClickListener(v->{
            if (MyApp.getClickStatus()){
                if (isValid()){
                    try {
                        JSONObject main=new JSONObject();
                        String name=edtName.getText().toString().trim();
                        String email=edtEmail.getText().toString().trim();
                        String phone=edtPhone.getText().toString().trim();
                        String city=edtCity.getText().toString().trim();

                        main.put("display_name",name);
                        main.put("email_id",email);
                        main.put("phone_no",phone);
                        main.put("state",selectedState);
                        main.put("city",city);
                        JSONArray array=new JSONArray();
                        for (int i = 0;i<list.size();i++){
                            PromoteChanelItem item=list.get(i);
                            if (item.getCh_type().equals("Select Chanel Type")){
                                CustomUtil.showTopSneakError(this,"Select Chanel Type");
                                return;
                            }
                            if (TextUtils.isEmpty(item.getCh_name())){
                                CustomUtil.showTopSneakError(this,"Enter Chanel Name");
                                return;
                            }
                            if (item.getCh_type().equals("Other")){
                                if (TextUtils.isEmpty(item.getCh_url())){
                                    CustomUtil.showTopSneakError(this,"Enter Chanel Url");
                                    return;
                                }
                            }
                            String matchModel = gson.toJson(item);
                            JSONObject obj=new JSONObject(matchModel);
                            array.put(obj);
                        }
                        main.put("channel_data",array);

                        LogUtil.e(TAG, "onSuccessResult: " + main);

                        submitData(main);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void submitData(JSONObject main) {
        HttpRestClient.postJSON(this, true, ApiManager.app_promotion, main, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult: " + responseBody);
                if(responseBody.optBoolean("status")){
                    CustomUtil.showTopSneakSuccess(PromoteAppFormActivity.this,responseBody.optString("msg"));
                    finish();
                }else {
                    CustomUtil.showTopSneakError(PromoteAppFormActivity.this,responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }
}