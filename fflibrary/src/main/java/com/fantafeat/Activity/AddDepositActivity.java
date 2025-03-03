package com.fantafeat.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fantafeat.Adapter.NewOfferAdapter;
import com.fantafeat.Model.BannerModel;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.Model.NewOfferModel;
import com.fantafeat.Model.PaymentSettingModel;
import com.fantafeat.Model.StateModal;
import com.fantafeat.Model.UpdateModel;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CirclePagerIndicatorDecoration;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.PrefConstant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddDepositActivity extends BaseActivity {

    private TextView total_balance_add_cash, third_topup, second_topup, first_topup,toolbar_title,txtLbl,txtCodeDesc,txtDepositAmt,txtDepositToAccount,
            txtGst,txtDepositToGems,txtGstLbl,txtDepositTotal;
    private EditText add_amount_edit,edtCode;
    private Button add_cash_button,btnApply;
    private float amount = 0;
    private ImageView toolbar_back;
    private LinearLayout layChart;
    private String depositAmt = "";
    private String orderId,couponCode="", couponCodeId="",ORDERID="";
    private ContestModel.ContestDatum contestData;
    private boolean isJoin = false,isDirectJoin=false;
    private RecyclerView relOffer;
    private OfferBannerAdapter adapter;
    private float available_bal = 0, deposit_bal = 0, winning_bal = 0, /*borrowed_bal = 0,*/ reward_bal = 0, donation_bal = 0,coin_bal=0;
    private String selectedState="";
    private Spinner spinState;
    private ArrayList<String> cityName,cityId;
    private Calendar myCalendar;
    private DatePickerDialog date;
    private String selectedGender="Select Gender";
    private float gst=0;
    private UpdateModel updateModel;
    private boolean isReverseCal=false;
    FusedLocationProviderClient fusedLocationClient;
    AlertDialog.Builder builder;
    private boolean isDialog = false;
    private ArrayList<NewOfferModel> offerList;
    private NewOfferAdapter newOfferAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetails();
        /*if(MyApp.getMyPreferences().getPrefBoolean(PrefConstant.PAYMENT_SUCCESS)){
            //MyApp.getMyPreferences().setPref(PrefConstant.PAYMENT_SUCCESS,false);
            finish();
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deposit);
        Window window = AddDepositActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.blackPrimary));

        Intent intent = getIntent();
        isJoin = intent.getBooleanExtra("isJoin",false);

        if (intent.hasExtra("isDirectJoin")){
            isDirectJoin=intent.getBooleanExtra("isDirectJoin",false);
        }

        if(isJoin){
            String model = intent.getStringExtra("contestData");
            contestData = gson.fromJson(model,ContestModel.ContestDatum.class);
            depositAmt = intent.getStringExtra("depositAmt");
        }
        else{
            depositAmt = intent.getStringExtra("depositAmt");
        }

        if (!TextUtils.isEmpty(depositAmt)){
            isReverseCal=true;
        }

        total_balance_add_cash = findViewById(R.id.total_balance_add_cash);
        third_topup = findViewById(R.id.third_topup);
        second_topup = findViewById(R.id.second_topup);
        first_topup = findViewById(R.id.first_topup);
        add_amount_edit = findViewById(R.id.add_amount_edit);
        toolbar_back = findViewById(R.id.toolbar_back);
        toolbar_title = findViewById(R.id.toolbar_title);
        relOffer = findViewById(R.id.relOffer);
        txtLbl = findViewById(R.id.txtLbl);
        btnApply = findViewById(R.id.btnApply);
        edtCode = findViewById(R.id.edtCode);
        txtCodeDesc = findViewById(R.id.txtCodeDesc);
        txtDepositAmt = findViewById(R.id.txtDepositAmt);
        txtGst = findViewById(R.id.txtGst);
        txtDepositToAccount = findViewById(R.id.txtDepositToAccount);
        layChart = findViewById(R.id.layChart);
        txtDepositToGems = findViewById(R.id.txtDepositToGems);
        txtGstLbl = findViewById(R.id.txtGstLbl);
        txtDepositTotal = findViewById(R.id.txtDepositTotal);

        toolbar_title.setText("Add Deposit");
        add_cash_button = findViewById(R.id.add_cash_button);

       // float donation_bal = CustomUtil.convertFloat(preferences.getUserModel().getDonationBal());
        txtGst.setText(mContext.getResources().getString(R.string.rs)+"0.00");// ("+gst+"%)
        txtDepositToAccount.setText(mContext.getResources().getString(R.string.rs)+"0.00");

        updateModel=preferences.getUpdateMaster();

       /* if (ConstantUtil.is_game_test){
            updateModel.setIs_display_gst("yes");
            updateModel.setGst_per("28");
        }*/

       /* if (!TextUtils.isEmpty(updateModel.getGst_per())){
            gst=Float.parseFloat(updateModel.getGst_per());
        }

        if (!TextUtils.isEmpty(updateModel.getIs_display_gst()) && updateModel.getIs_display_gst().equalsIgnoreCase("yes")){
            layChart.setVisibility(View.VISIBLE);
            txtGstLbl.setText("Applicable GST ("+gst+"%)");
        }
        else {

        }*/
        layChart.setVisibility(View.GONE);

        offerList=new ArrayList<>();
        relOffer.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        newOfferAdapter=new NewOfferAdapter(mContext, offerList, model -> {
            amount=CustomUtil.convertFloat(add_amount_edit.getText().toString().trim());
            if (amount<=0){
                CustomUtil.showTopSneakWarning(mContext,"Enter Amount");
                return;
            }
      /*      for (int i = 0; i < offerList.size(); i++) {
                offerList.get(i).setApply(false);
            }
            model.setApply(true);*/

            edtCode.setText(model.getCouponCode());
            couponCode=model.getCouponCode();

           // newOfferAdapter.notifyDataSetChanged();

            checkCode();
            //submitCode(model.getCouponCode());
        });
        relOffer.setAdapter(newOfferAdapter);
        relOffer.hasFixedSize();
        relOffer.addItemDecoration(new CirclePagerIndicatorDecoration());
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(relOffer);

        setDefaultBal();

        /*if (preferences.getHomeBanner() != null && preferences.getHomeBanner().size() > 0) {
            relOffer.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            adapter = new OfferBannerAdapter(this, preferences.getHomeBanner());
            relOffer.setAdapter(adapter);
        } else {
            relOffer.setVisibility(View.GONE);
            txtLbl.setVisibility(View.GONE);
        }*/

        initClick();


        add_amount_edit.setText(depositAmt);

        if (TextUtils.isEmpty(preferences.getUserModel().getEmailId())){
            showBasicDetailDialog();
            return;
        }

        getOfferData();
    }

    private void getOfferData(){
        try {
            JSONObject param=new JSONObject();
            param.put("user_id", preferences.getUserModel().getId());
            HttpRestClient.postJSON(mContext, true, ApiManager.getDepositOfferList, param, new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {
                    LogUtil.e("resp","getOfferData: "+responseBody);
                    if (responseBody.optBoolean("status")){
                        JSONArray data=responseBody.optJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj=data.optJSONObject(i);
                            NewOfferModel model=gson.fromJson(obj.toString(),NewOfferModel.class);
                            offerList.add(model);
                        }
                        newOfferAdapter.notifyDataSetChanged();
                    }
                   /* if (offerList.size()>0){
                        layBestOffer.setVisibility(View.VISIBLE);
                    }else {
                        layBestOffer.setVisibility(View.GONE);
                    }*/
                }

                @Override
                public void onFailureResult(String responseBody, int code) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private void showBasicDetailDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_profile, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(false);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);

        UserModel user=preferences.getUserModel();

        RelativeLayout layImage = view.findViewById(R.id.layImage);
        layImage.setVisibility(View.GONE);
        LinearLayout layName = view.findViewById(R.id.layName);
        layName.setVisibility(View.GONE);
        ImageView toolbar_back = view.findViewById(R.id.toolbar_back);
        //toolbar_back.setVisibility(View.GONE);
        toolbar_back.setImageResource(R.drawable.ic_close_otp);
        TextView toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Profile");

        EditText name_as_aadhar=view.findViewById(R.id.name_as_aadhar);
        EditText team_name=view.findViewById(R.id.edtTeamName);
        EditText mobile_number=view.findViewById(R.id.mobile_number);
        EditText email=view.findViewById(R.id.email);
        EditText dob=view.findViewById(R.id.dob);
        Spinner spinGender=view.findViewById(R.id.spinGender);
        Button confirm=view.findViewById(R.id.confirm);
        spinState=view.findViewById(R.id.spinState);

        name_as_aadhar.setText(user.getDisplayName());
        team_name.setText(user.getUserTeamName());
        mobile_number.setText(user.getPhoneNo());
        email.setText(user.getEmailId());
        dob.setText(user.getDob());

        selectedState=user.getStateId();

        /*if (TextUtils.isEmpty(user.getEmailId())){
            email.setEnabled(true);
        }else {
            email.setEnabled(false);
        }*/

        team_name.setEnabled(user.getTeam_name_change_allow().equalsIgnoreCase("yes"));

        ArrayList<String> genderList=new ArrayList<String>();
        genderList.add("Select Gender");
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Other");

        ArrayAdapter<String> genderAdapter=new ArrayAdapter(mContext,R.layout.spinner_text,genderList);
        spinGender.setAdapter(genderAdapter);

        toolbar_back.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });
        spinGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                selectedGender=genderList.get(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        myCalendar = Calendar.getInstance();
        myCalendar.add(Calendar.YEAR, -18);

        date = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(dob);
            }
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        date.getDatePicker().setMaxDate(System.currentTimeMillis() - 568025136000L);

        dob.setOnClickListener(view1 -> date.show());

        confirm.setOnClickListener(view1 -> {
            String db=dob.getText().toString().trim();
            String strEmail=getEditText(email);
            String strName=getEditText(name_as_aadhar);
            String strTeam=getEditText(team_name);

            if (TextUtils.isEmpty(strName)) {
                CustomUtil.showToast(mContext, "Please Enter Name as on Aadhar Card.");
            }
            else if (TextUtils.isEmpty(strTeam)) {
                CustomUtil.showToast(mContext, "Please Enter Team Name.");
            }
            else if (strTeam.length()>11) {
                CustomUtil.showToast(mContext, "Team name should be less than or equals to 11 characters.");
            }
            else if (TextUtils.isEmpty(strEmail)) {
                CustomUtil.showToast(mContext, "Please Enter Email.");
            }
            else if (!isValidEmail(strEmail)) {
                CustomUtil.showToast(mContext, "Please Enter Valid Email.");
            }
            else if (selectedState.equalsIgnoreCase("0")) {
                CustomUtil.showToast(mContext, "Please Select State.");
            }
            else if (TextUtils.isEmpty(db)) {
                CustomUtil.showToast(mContext, "Please Enter Date of Birth.");
            }
            else {
                callFirstApi(strName,strEmail,strTeam,db,bottomSheetDialog);
            }

        });

        switch (user.getGender()) {
            case "Male":
                spinGender.setSelection(1);
                break;
            case "Female":
                spinGender.setSelection(2);
                break;
            case "Other":
                spinGender.setSelection(3);
                break;
            default:
                spinGender.setSelection(0);
                break;
        }

        getStateData();

        bottomSheetDialog.show();
    }

    private void callFirstApi(String name,String email,String team,String dob,BottomSheetDialog bottomSheetDialog) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("email_id", email);
            jsonObject.put("display_name", name);
            jsonObject.put("state_id", selectedState);
            jsonObject.put("gender", selectedGender);
            jsonObject.put("dob", dob);
            jsonObject.put("user_team_name", team);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.AUTHV3UpdateUserDetails, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());

                    UserModel userModel = preferences.getUserModel();
                    userModel.setEmailId(email);
                    userModel.setDisplayName(name);
                    userModel.setStateId(selectedState);
                    userModel.setGender(selectedGender);
                    userModel.setDob(dob);
                    userModel.setUserTeamName(team);
                    preferences.setUserModel(userModel);
                    MyApp.getMyPreferences().setUserModel(userModel);

                    bottomSheetDialog.dismiss();

                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void updateLabel( EditText dob) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = MyApp.changedFormat(myFormat);
        dob.setText(sdf.format(myCalendar.getTime()));
    }

    private void getStateData() {
        HttpRestClient.postData(mContext, true, ApiManager.STATE_LIST, new JSONObject(), new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                try {
                    LogUtil.e(TAG, "onSuccessResult: " + responseBody);
                    cityName=new ArrayList<String>();
                    cityName.add("Select state");
                    cityId=new ArrayList<String>();
                    cityId.add("0");
                    ArrayList<StateModal> stateModals=new ArrayList<>();
                    int pos=0;
                    if(responseBody.optBoolean("status")){
                        JSONArray jsonArray = responseBody.getJSONArray("data");

                        for (int i =0;i<jsonArray.length();i++){
                            JSONObject obj=jsonArray.getJSONObject(i);
                            String name = obj.optString("name");
                            String id = obj.optString("id");
                            if (selectedState.equals(id)){
                                pos=i+1;
                            }
                            cityId.add(id);
                            cityName.add(name);
                            StateModal stateModal = gson.fromJson(obj.toString(), StateModal.class);
                            stateModals.add(stateModal);
                        }
                        preferences.setStateList(stateModals);
                    }
                    ArrayAdapter<String> stateAdapter=new ArrayAdapter<String>(mContext,R.layout.spinner_text,cityName);
                    spinState.setAdapter(stateAdapter);
                    spinState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                            selectedState=cityId.get(pos);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    spinState.setSelection(pos);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {}

        });
    }

    private void setDefaultBal(){
        preferences = MyApp.getMyPreferences();
        deposit_bal = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        winning_bal = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
        //borrowed_bal = CustomUtil.convertFloat(preferences.getUserModel().getTransferBal());
        reward_bal = CustomUtil.convertFloat(preferences.getUserModel().getBonusBal());
        coin_bal = CustomUtil.convertFloat(preferences.getUserModel().getFf_coin());
        //float old_coin_bal = CustomUtil.convertFloat(preferences.getUserModel().getOld_ff_coins_bal());

        if (ConstantUtil.is_bonus_show){
            available_bal = deposit_bal + winning_bal /*+ borrowed_bal*/ + reward_bal + coin_bal;
        }else {
            available_bal = deposit_bal + winning_bal /*+ borrowed_bal*/ + coin_bal;
        }
        //available_bal = deposit_bal + winning_bal /*+ borrowed_bal*/ + reward_bal+coin_bal;

        preferences.getUserModel().setTotal_balance(available_bal);

        //total_balance_add_cash.setText("₹" + preferences.getUserModel().getTotal_balance());
        total_balance_add_cash.setText(mContext.getResources().getString(R.string.rs) +
                CustomUtil.getFormater("00.00").format((available_bal/*+old_coin_bal*/)));
    }

    private void getUserDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("token_no", preferences.getUserModel().getTokenNo());
            //Log.e(TAG, "getUserDetails: " + jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, true, ApiManager.AUTHV3GetUserDetails, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                //Log.e(TAG, "USER onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    if (responseBody.optString("code").equals("200")) {
                        JSONObject data = responseBody.optJSONObject("data");
                        UserModel userModel = gson.fromJson(data.toString(), UserModel.class);
                        preferences.setUserModel(userModel);

                        if (offerList.size()>0) {
                            for (int i = 0; i < offerList.size(); i++) {
                                offerList.get(i).setApply(false);
                            }
                            newOfferAdapter.notifyDataSetChanged();
                        }

                        edtCode.setEnabled(true);
                        btnApply.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        btnApply.setText("Apply");
                        couponCodeId="";
                        edtCode.setText("");
                        couponCode="";
                        txtCodeDesc.setVisibility(View.GONE);
                        edtCode.clearFocus();

                        setDefaultBal();
                    }
                }

                //  mUserDetail = true;
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                Log.e(TAG, "onFailureResult: " );
                setDefaultBal();

            }
        });
    }
    public void initClick() {

        first_topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_amount_edit.setText("100");
                first_topup.setBackgroundResource(R.drawable.btn_primary);
                first_topup.setTextColor(getResources().getColor(R.color.white_pure));

                second_topup.setBackgroundResource(R.drawable.curv_black_border);
                second_topup.setTextColor(getResources().getColor(R.color.textPrimary));

                third_topup.setBackgroundResource(R.drawable.curv_black_border);
                third_topup.setTextColor(getResources().getColor(R.color.textPrimary));

                amount = 100;
            }
        });

        second_topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_amount_edit.setText("200");
                amount = 200;

                first_topup.setBackgroundResource(R.drawable.curv_black_border);
                first_topup.setTextColor(getResources().getColor(R.color.textPrimary));

                second_topup.setBackgroundResource(R.drawable.btn_primary);
                second_topup.setTextColor(getResources().getColor(R.color.white_pure));

                third_topup.setBackgroundResource(R.drawable.curv_black_border);
                third_topup.setTextColor(getResources().getColor(R.color.textPrimary));
            }
        });

        third_topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_amount_edit.setText("500");
                amount = 500;
                first_topup.setBackgroundResource(R.drawable.curv_black_border);
                first_topup.setTextColor(getResources().getColor(R.color.textPrimary));

                second_topup.setBackgroundResource(R.drawable.curv_black_border);
                second_topup.setTextColor(getResources().getColor(R.color.textPrimary));

                third_topup.setBackgroundResource(R.drawable.btn_primary);
                third_topup.setTextColor(getResources().getColor(R.color.white_pure));
            }
        });

        add_cash_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (add_amount_edit.getText().toString().trim().equals("0") ||
                            add_amount_edit.getText().toString().trim().equals("")) {
                        CustomUtil.showTopSneakError(mContext, "Please enter the amount");
                    }else if (Float.parseFloat(add_amount_edit.getText().toString().trim())<=0) {
                        CustomUtil.showTopSneakError(mContext, "Please enter valid amount");
                    } else {
                        amount = CustomUtil.convertFloat(getEditText(add_amount_edit).trim());
                        ORDERID=generateOrderId("pu");
                        LogUtil.e(TAG, "onClick: " +amount);
                        //submitAmount();

                        checkPayuData();
                        //LogUtil.e(TAG, "onClick: " + add_amount_edit.getText().toString());
                    }
                }catch (NumberFormatException e){
                    CustomUtil.showTopSneakWarning(mContext,"Enter Valid Amount");
                    add_amount_edit.setText("");
                }
            }
        });

        toolbar_back.setOnClickListener(view -> finish());

        btnApply.setOnClickListener(v->{
            if (btnApply.getText().toString().trim().equals("Apply")){
                couponCode=edtCode.getText().toString().trim();
                amount = CustomUtil.convertFloat(getEditText(add_amount_edit).trim());

                if (TextUtils.isEmpty(couponCode)){
                    CustomUtil.showTopSneakError(this,"Please enter coupon code");
                    return;
                }
                if (amount<=0 ||
                        add_amount_edit.getText().toString().trim().equals("")) {
                    CustomUtil.showTopSneakError(mContext, "Please enter the amount");
                    return;
                }
                checkCode();
            }
            else {
                for (int i = 0; i < offerList.size(); i++) {
                    offerList.get(i).setApply(false);
                }
                newOfferAdapter.notifyDataSetChanged();

                edtCode.setEnabled(true);
                btnApply.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnApply.setText("Apply");
                couponCodeId="";
                edtCode.setText("");
                couponCode="";
                txtCodeDesc.setVisibility(View.GONE);
                edtCode.clearFocus();
            }

        });

        add_amount_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    for (int i = 0; i < offerList.size(); i++) {
                        offerList.get(i).setApply(false);
                    }
                    newOfferAdapter.notifyDataSetChanged();

                    edtCode.setText("");
                    couponCode="";
                    couponCodeId="";
                    txtCodeDesc.setVisibility(View.GONE);
                    edtCode.clearFocus();
                    edtCode.setEnabled(true);
                    btnApply.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnApply.setText("Apply");
                   /* if (!TextUtils.isEmpty(updateModel.getIs_display_gst()) &&
                            updateModel.getIs_display_gst().equalsIgnoreCase("yes")){
                        calculateGst(s);
                    }*/
                }catch (NumberFormatException e){
                    CustomUtil.showTopSneakWarning(mContext,"Enter Valid Amount");
                    add_amount_edit.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void checkPayuData() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("amount", amount);
            LogUtil.e("resp",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.appPaymentSetting, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                try {
                    LogUtil.e(TAG, "getPaymentData: " + responseBody.toString());
                    if (responseBody.optBoolean("status")) {
                        JSONArray payment_settings=responseBody.optJSONArray("payment_settings");

                        PaymentSettingModel payUType=null;

                        if (payment_settings!=null){
                            for (int i = 0; i < payment_settings.length(); i++) {
                                JSONObject upiObj = payment_settings.optJSONObject(i);
                                PaymentSettingModel model = gson.fromJson(upiObj.toString(), PaymentSettingModel.class);
                                if (model.getIs_enable_android().equalsIgnoreCase("yes") &&
                                        model.getGatewayType().equalsIgnoreCase("payu")) {
                                    payUType=model;
                                    break;
                                }
                            }
                        }

                        if (payUType!=null){
                            UserModel userModel=preferences.getUserModel();
                            String web_view_url=payUType.getTokenGenerateUrl()+"?user_id="+userModel.getId()+"&amount="+amount+
                                    "&phone_no="+userModel.getPhoneNo()+"&email_id="+userModel.getEmailId()+"&coupon_id="+couponCodeId;

                            if(isDirectJoin){
                                startActivityForResult(new Intent(mContext,PaymentWebActivity.class).putExtra("payment_url",web_view_url)
                                        .putExtra("order_id",ORDERID)
                                        .putExtra("success_url",payUType.getTrans_success_url())
                                        .putExtra("fail_url",payUType.getTrans_fail_url()),1002
                                );
                            }else {
                                startActivity(new Intent(mContext,PaymentWebActivity.class).putExtra("payment_url",web_view_url)
                                        .putExtra("order_id",ORDERID)
                                        .putExtra("success_url",payUType.getTrans_success_url())
                                        .putExtra("fail_url",payUType.getTrans_fail_url())
                                );
                            }

                        }else {
                            Intent intent = new Intent(mContext, PaymentActivity.class);
                            intent.putExtra("amount", amount);
                            intent.putExtra("couponCodeId",couponCodeId);
                            if(isDirectJoin){
                                intent.putExtra("isDirectJoin", isDirectJoin);
                                startActivityForResult(intent,1002);
                            }else {
                                startActivity(intent);
                            }
                        }

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });

    }

    private String generateOrderId(String gateway){
        long tsLong = System.currentTimeMillis()/1000;
        return gateway.toUpperCase()+"_"+ tsLong+ "_" + preferences.getUserModel().getId();
    }

    private void calculateGst(CharSequence s) {
        try {
            String rs=mContext.getResources().getString(R.string.rs);
            if (!TextUtils.isEmpty(s) && Float.parseFloat(s.toString())>0){
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat format=new DecimalFormat("0.00",symbols);

                float depositAmt=Float.parseFloat(add_amount_edit.getText().toString().trim());

                if (isReverseCal) {
                    isReverseCal = false;

                    //let GSTAmount = ((Double(strAddAmount) ?? 0)*(Double(Constant.Common.strGSTper) ?? 0))/100
                    //                let NetPrice = (Double(strAddAmount) ?? 0) + GSTAmount

                    float gstAmt=(depositAmt*gst)/100;
                    depositAmt=depositAmt+gstAmt+1;

                    txtGst.setText(rs+format.format(gstAmt));//+" ("+gst+"%)"
                    LogUtil.e("resp","calculateGst depositAmt if: "+depositAmt);

                    add_amount_edit.setText(format.format(depositAmt)+"");

                    //txtDepositToAccount.setText(rs+format.format(depositAmt));

                    //txtDepositAmt.setText(rs+format.format(depositAmt)+" (Inclusive GST)");

                }
                else {
                    float amtToAdd=(depositAmt/(100+gst))*100;

                    float gstVal=depositAmt-amtToAdd;

                    txtGst.setText(rs+format.format(gstVal));//+" ("+gst+"%)"

                    txtDepositToAccount.setText(rs+format.format(amtToAdd));

                    txtDepositToGems.setText(rs+format.format(gstVal));

                    txtDepositAmt.setText(rs+format.format(depositAmt));//+" (Inclusive GST)"
                    txtDepositTotal.setText(rs+format.format(depositAmt));

                    LogUtil.e("resp","calculateGst amtToAdd else: "+amtToAdd);
                    LogUtil.e("resp","calculateGst depositAmt else: "+depositAmt);
                }

               /* if (isReverseCal){
                    isReverseCal=false;

                    //LogUtil.e("resp","calculateGst: "+depositAmt);

                    float addedAmt=(depositAmt*100)/(100-gst);

                    //LogUtil.e("resp","calculateGst: "+addedAmt);

                    float gstNew=(addedAmt*gst)/100;

                    //LogUtil.e("resp","calculateGst: "+gstNew);

                    txtGst.setText(rs+format.format(gstNew)+" ("+gst+"%)");

                    float fAmt=addedAmt-gstNew;

                    //LogUtil.e("resp","calculateGst: "+fAmt);

                    add_amount_edit.setText(format.format(addedAmt)+"");

                    txtDepositAmt.setText(rs+format.format(addedAmt));

                    txtDepositToAccount.setText(rs+format.format(fAmt));
                    //txtDepositToAccount.setText(rs+format.format(finalAmt));
                }
                else {

                    txtDepositAmt.setText(rs+add_amount_edit.getText().toString().trim());

                    float gstAmt=(depositAmt*gst)/100;

                    txtGst.setText(rs+format.format(gstAmt)+" ("+gst+"%)");

                    float finalAmt=depositAmt-gstAmt;

                    txtDepositToAccount.setText(rs+format.format(finalAmt));
                }*/
            }
            else {
                txtDepositAmt.setText(rs+"0.00");// (Inclusive GST)
                txtGst.setText(rs+"0.00");// ("+gst+"%)
                txtDepositToAccount.setText(rs+"0.00");
                txtDepositToGems.setText(rs+"0.00");
                txtDepositTotal.setText(rs+"0.00");
            }
        }catch (NumberFormatException e){
            add_amount_edit.setText("");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==Activity.RESULT_OK && requestCode==1002){
            Intent intent=new Intent();
            intent.putExtra("isDirectJoin", isDirectJoin);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    }

    private void checkCode(){
        try {
            JSONObject param=new JSONObject();
            param.put("code",couponCode);
            param.put("amount", amount);
            param.put("user_id", preferences.getUserModel().getId());

            LogUtil.d("resp","param: "+param);

            HttpRestClient.postJSON(this, true, ApiManager.CHECK_CODE, param, new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {
                    LogUtil.e("resp","Success: "+responseBody);
                    if (responseBody.optBoolean("status")){
                        JSONObject data=responseBody.optJSONObject("data");
                        couponCodeId=data.optString("id");
                        txtCodeDesc.setVisibility(View.VISIBLE);
                        txtCodeDesc.setTextColor(getResources().getColor(R.color.green_pure));
                        txtCodeDesc.setText(responseBody.optString("msg"));
                        edtCode.setEnabled(false);
                        edtCode.clearFocus();
                        btnApply.setBackgroundColor(getResources().getColor(R.color.green_pure));
                        btnApply.setText("Remove");

                        if (offerList.size() > 0) {
                            for (int i = 0; i < offerList.size(); i++) {
                                offerList.get(i).setApply(couponCode.equalsIgnoreCase(offerList.get(i).getCouponCode()));
                            }
                            newOfferAdapter.notifyDataSetChanged();
                        }
                    }else {
                        couponCodeId="";
                        txtCodeDesc.setVisibility(View.VISIBLE);
                        txtCodeDesc.setTextColor(getResources().getColor(R.color.darkRed));
                        btnApply.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        edtCode.setEnabled(true);
                        btnApply.setText("Apply");
                        txtCodeDesc.setText(responseBody.optString("msg"));
                        edtCode.clearFocus();

                        if (offerList.size() > 0) {
                            for (int i = 0; i < offerList.size(); i++) {
                                offerList.get(i).setApply(false);
                            }
                            newOfferAdapter.notifyDataSetChanged();
                        }
                    }
                }
                @Override
                public void onFailureResult(String responseBody, int code) {}
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class OfferBannerAdapter extends RecyclerView.Adapter<OfferBannerAdapter.OfferBannerHolder> {

        private Context mContext;
        private int lastHolder = -1;
        private List<BannerModel> bannerModelList;

        public OfferBannerAdapter(Context mContext, List<BannerModel> bannerModelList) {
            this.mContext = mContext;
            this.bannerModelList = bannerModelList;
        }

        @NonNull
        @Override
        public OfferBannerAdapter.OfferBannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new OfferBannerAdapter.OfferBannerHolder(LayoutInflater.from(mContext).inflate(R.layout.deposit_offer_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull OfferBannerAdapter.OfferBannerHolder holder, int position) {
            final BannerModel bannerModel = bannerModelList.get(position);

            LogUtil.e(TAG, "onBindViewHolder: " + gson.toJson(bannerModel));

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            float widthPixels = displayMetrics.widthPixels;
            float heightPixels = (float) ((widthPixels-20) * 0.2);
            holder.level_image.setMinimumHeight((int) heightPixels);

            CustomUtil.loadImageWithGlide(mContext,holder.level_image,ApiManager.BANNER_IMAGES,bannerModel.getBannerImage(),R.drawable.placeholder_banner);
        /*    Glide.with(mContext)
                    .load(ApiManager.BANNER_IMAGES + bannerModel.getBannerImage())
                    .placeholder(R.drawable.placeholder_banner)
                    .error(R.drawable.placeholder_banner)
                    .into(holder.level_image);*/

            /* holder.level_image.setOnClickListener(view -> {

                bannerModel.setBannerAction("Contest");
              //  LogUtil.e("banAct",bannerModel.getBannerAction()+"   "+viewModel.getMatchModelList().size());
                if (bannerModel.getBannerAction().equalsIgnoreCase("deposit")) {
                    Intent intent = new Intent(mContext, AddDepositActivity.class);
                    intent.putExtra("isJoin",false);
                    intent.putExtra("depositAmt",bannerModel.getBannerMatchId());
                    startActivity(intent);
                  */

            /*
                }
                else if (bannerModel.getBannerAction().equalsIgnoreCase("Contest")) {
                    for (MatchModel matchModel : CustomUtil.emptyIfNull((viewModel.getMatchModelList()))) {
                        if (bannerModel.getBannerMatchId().equalsIgnoreCase(matchModel.getId())) {
                            preferences.setMatchModel(matchModel);
                            Intent intent = new Intent(mContext, ContestListActivity.class);
                            mContext.startActivity(intent);
                            */
            /*new FragmentUtil().addFragment((FragmentActivity) mContext,
                                    R.id.home_fragment_container,
                                    new MatchContestFragment(),
                                    ((HomeActivity) mContext).fragmentTag(7),
                                    FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
            /*
                            break;
                        }
                    }
                }
                else if (bannerModel.getBannerAction().equalsIgnoreCase("web_view")) {
                    startActivity(new Intent(mContext, WebViewActivity.class)
                            .putExtra(ConstantUtil.WEB_TITLE,"")
                            .putExtra(ConstantUtil.WEB_URL,bannerModel.getBannerWebViewUrl()));
                    */
            /*new FragmentUtil().addFragment((FragmentActivity) mContext,
                            R.id.home_fragment_container,
                            new WebViewFragment("", bannerModel.getBannerWebViewUrl()),
                            ((HomeActivity) mContext).fragmentTag(24),
                            FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
            /*
                }
                else if (bannerModel.getBannerAction().equalsIgnoreCase("fullBanner")) {
                    new FragmentUtil().addFragment((FragmentActivity) mContext,
                            R.id.home_fragment_container,
                            new FullImageFragment(ApiManager.BANNER_IMAGES + bannerModel.getBannerPopupImage()),
                            ((HomeActivity) mContext).fragmentTag(21),
                            FragmentUtil.ANIMATION_TYPE.CUSTOM);
                }
            });*/
        }

        @Override
        public int getItemCount() {
            return bannerModelList.size();
        }

        public class OfferBannerHolder extends RecyclerView.ViewHolder {
            ImageView level_image;

            public OfferBannerHolder(@NonNull View itemView) {
                super(itemView);
                level_image = itemView.findViewById(R.id.offer_slider_img);
            }
        }
    }

  /*  private void submitAmount() {
        getRazorPayChecksum();
        startPayment();
    }*/

   /* private void getRazorPayChecksum() {
        JSONObject options = new JSONObject();

        try {
            options.put("name", getResources().getString(R.string.app_name));
            options.put("description", "Deposit");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", ApiManager.payment_image_url);
            options.put("currency", "INR");
            options.put("amount", (amount * 100));
            options.put("user_id", preferences.getUserModel().getId());

            JSONObject preFill = new JSONObject();
            preFill.put("email", preferences.getUserModel().getEmailId());
            preFill.put("contact", preferences.getUserModel().getPhoneNo());
            options.put("prefill", preFill);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.RAZOR_CHECKSUM, options, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    public void startPayment() {
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.app_name));
            options.put("description", "Deposit");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", ApiManager.payment_image_url);
            options.put("currency", "INR");
            options.put("amount", (amount * 100));

            JSONObject preFill = new JSONObject();
            preFill.put("email", preferences.getUserModel().getEmailId());
            preFill.put("contact", preferences.getUserModel().getPhoneNo());
            options.put("prefill", preFill);

            co.open((Activity) mContext, options);

        } catch (Exception e) {
            Toast.makeText(mContext, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            LogUtil.e(TAG, "startPayment: " + e.toString() );
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        try {
            sendRequestRozorpay(s);
            LogUtil.e("ID_RESPONCE: ", "-> " + s);
            //Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            LogUtil.e(TAG, "onPaymentSuccess: " + e.toString() );
            LogUtil.e(TAG, "Exception in onPaymentSuccess : " + e.toString());
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            failResponse(String.valueOf(i), s);
            LogUtil.e("Payment failed: " + i, " => " + s);
        } catch (Exception e) {
            LogUtil.e(TAG, "Exception in onPaymentError");
        }
    }

    private void sendRequestRozorpay(final String txnId) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("txnId", txnId);
            jsonObject.put("amount", amount);
            jsonObject.put("user_id", preferences.getUserModel().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.ADD_AMOUNT_RAZORPAY, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    JSONObject jo = null;
                    try {
                        jo = responseBody.getJSONObject("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    float bonus = CustomUtil.convertFloat(jo.optString("bonus"));
                    float deposit = CustomUtil.convertFloat(jo.optString("deposit"));

                    float currentBal = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
                    float currentBonus = CustomUtil.convertFloat(preferences.getUserModel().getBonusBal());
                    UserModel user = preferences.getUserModel();
                    user.setDepositBal(String.valueOf(deposit + currentBal));
                    user.setBonusBal(String.valueOf(currentBonus + bonus));
                    float total = CustomUtil.convertFloat(user.getDepositBal()) + CustomUtil.convertFloat(user.getWinBal()) + CustomUtil.convertFloat(user.getTransferBal()) + CustomUtil.convertFloat(user.getBonusBal());
                    //user.setTotal_balance(String.valueOf(total));
                    preferences.setUserModel(user);
                } else {
                    Toast.makeText(mContext, "Something Wrong Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });

    }

    private void failResponse(final String ResponseCode, final String response) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject();
            jsonObject.put("amount", String.valueOf(amount));
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("orderId", orderId);
            jsonObject.put("code", ResponseCode);
            jsonObject.put("paytmResponse", response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, false, ApiManager.TRANSACTION_FAIL, jsonObject, new GetApiResult() {


            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }*/
}
