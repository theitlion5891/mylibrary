package com.fantafeat.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cashfree.pg.api.CFPaymentGatewayService;
import com.cashfree.pg.core.api.CFCorePaymentGatewayService;
import com.cashfree.pg.core.api.CFSession;
import com.cashfree.pg.core.api.CFTheme;
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback;
import com.cashfree.pg.core.api.exception.CFException;
import com.cashfree.pg.core.api.upi.CFUPI;
import com.cashfree.pg.core.api.upi.CFUPIPayment;
import com.cashfree.pg.core.api.utils.CFErrorResponse;
import com.cashfree.pg.core.api.utils.CFUPIApp;
import com.cashfree.pg.core.api.utils.CFUPIUtil;
import com.cashfree.pg.ui.api.CFDropCheckoutPayment;
import com.cashfree.pg.ui.api.CFPaymentComponent;
import com.fantafeat.Adapter.PaymentSettingAdapter;
import com.fantafeat.Model.PaymentSettingModel;
import com.fantafeat.Model.UpdateModel;
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
import com.phonepe.intent.sdk.api.B2BPGRequest;
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder;
import com.phonepe.intent.sdk.api.PhonePe;
import com.phonepe.intent.sdk.api.PhonePeInitException;
import com.phonepe.intent.sdk.api.UPIApplicationInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PaymentActivity extends BaseActivity implements CFCheckoutResponseCallback {

    float amount = 0;
    private String orderId;

    public native String getMID();

    public native String getAPPID();
    public native String getPaytmHost();
    public String ORDERID;
    public String MID = getMID();
    public String APP_ID = getAPPID();
    String stage = "PROD";
    private String host = getPaytmHost();
    private String CHECKSUM = "";
    private Integer ActivityRequestCode = 2;
    private String status, bankName, txnAmount, txnId, paymrntMode, bankId, currency, gatewayName;
    private ImageView back_btn;
    LinearLayout layUpi;
    //private CardView cardEasebuzz,cardCF;
    private String cashFreeMethod = "",couponCodeId="";
    private String cashFreeToken = "";
    private boolean isDirectJoin=false;
    private RecyclerView recyclerPayment;
    private PaymentSettingModel upiModel;
    private Dialog statusDialog;
    private boolean isApiCall=true;

    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        this.runOnUiThread(() -> {
            initData();
            amount = getIntent().getFloatExtra("amount",0);//CustomUtil.convertFloat(getIntent().getStringExtra("amount"));
            if (getIntent().hasExtra("isDirectJoin")){
                isDirectJoin=getIntent().getBooleanExtra("isDirectJoin",false);
            }
            couponCodeId=getIntent().getStringExtra("couponCodeId");
            LogUtil.e(TAG, "onClick: " +amount);
            clickEvents();
            initStatusDialog();
            //initUpiApps();

            try {
                // If you are using a fragment then you need to add this line inside onCreate() of your Fragment
                CFPaymentGatewayService.getInstance().setCheckoutCallback(this);
            } catch (CFException e) {
                e.printStackTrace();
            }

        });

        /*new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 2 seconds
                LogUtil.d("timeru","call");

            }
        }, 100);*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        addPaymentSetting();
    }

    private void initUpiApps() {
        RecyclerView recyclerUpiApps=findViewById(R.id.recyclerUpiApps);
        TextView txtNoData=findViewById(R.id.txtNoData);

        ArrayList<CFUPIApp> list=new ArrayList<>();

        CFUPIUtil.getInstalledUPIApps(this, upiAppsList -> {
            list.addAll(upiAppsList);

            runOnUiThread(() -> {
                if (list.size()>0){
                    txtNoData.setVisibility(View.GONE);
                    recyclerUpiApps.setVisibility(View.VISIBLE);

                    /*UpiAppsCashFreeAdapter adapter=new UpiAppsCashFreeAdapter(list, mContext, cfupiApp -> {
                        if (upiModel!=null)
                            getCashfreeToken2("upi",cfupiApp,null);
                    });
                    recyclerUpiApps.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
                    recyclerUpiApps.setAdapter(adapter);*/
                   /* UpiAppAdapter adapter=new UpiAppAdapter(list, mContext, cfupiApp -> {
                        if (upiModel!=null)
                            getCashfreeToken2("upi",cfupiApp,null);

                    });
                    recyclerUpiApps.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
                    recyclerUpiApps.setAdapter(adapter);*/
                }else {
                    txtNoData.setVisibility(View.VISIBLE);
                    recyclerUpiApps.setVisibility(View.GONE);
                }
            });

        });
    }

    private void initPhonePeUpiApps(){

        RecyclerView recyclerUpiApps=findViewById(R.id.recyclerUpiApps);
        TextView txtNoData=findViewById(R.id.txtNoData);

        try {

            List<UPIApplicationInfo> upiApps=new ArrayList<>(PhonePe.getUpiApps());

            ArrayList<UPIApplicationInfo> list=new ArrayList<>();

            for (int i = 0; i < upiApps.size(); i++) {
                UPIApplicationInfo model=upiApps.get(i);
                //LogUtil.e("resp","Name: "+model.getApplicationName()+"   Id:"+model.getPackageName());
                if (model.getApplicationName().contains("GPay") ||
                        model.getApplicationName().contains("Paytm") ||
                        model.getApplicationName().contains("PhonePe") ||
                        model.getApplicationName().contains("BHIM") ||
                        model.getApplicationName().contains("amazon")
                ) {
                    //if (!model.getApplicationName().contains("PhonePe Simulator"))
                        list.add(model);
                }
            }

            runOnUiThread(() -> {
                if (list.size()>0){
                    txtNoData.setVisibility(View.GONE);
                    recyclerUpiApps.setVisibility(View.VISIBLE);

                    /*UpiAppAdapter adapter=new UpiAppAdapter(list, mContext, model -> {
                        generateCheckSum("UPI_INTENT",model.getPackageName());
                    });
                    recyclerUpiApps.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
                    recyclerUpiApps.setAdapter(adapter);*/
                }else {
                    txtNoData.setVisibility(View.VISIBLE);
                    recyclerUpiApps.setVisibility(View.GONE);
                }
            });

        } catch (PhonePeInitException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateCheckSum(String upi_intent, String packageName) {
        JSONObject options = new JSONObject();
        //trans_id="EZY_"+System.currentTimeMillis()+"_"+preferences.getUserModel().getId();
        ORDERID=generateOrderId("pf");
        try {
            options.put("deviceOS", "ANDROID");
            options.put("targetApp", packageName);
            options.put("paymentInstrumentType", upi_intent);
            options.put("trans_id", ORDERID);
            options.put("amount", (amount * 100));
            options.put("user_id", preferences.getUserModel().getId());
            options.put("mobile_number", preferences.getUserModel().getPhoneNo());
            options.put("coupon_id", couponCodeId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.e("resp","generateCheckSum:"+options);

        HttpRestClient.postJSON(mContext, true,  ApiManager.PHONEPE_CHECKSUM, options, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("resp","generateCheckSum:"+responseBody);
                if (responseBody.optBoolean("status")){
                    String base64=responseBody.optString("base64");
                    String checksum=responseBody.optString("checksum");

                    makePayment(base64,checksum,packageName);
                }
                else {
                    CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void makePayment(String base64, String checksum,String packageName) {

        String apiEndPoint = "/pg/v1/pay";
        B2BPGRequest b2BPGRequest = new B2BPGRequestBuilder()
                .setData(base64)
                .setChecksum(checksum)
                .setUrl(apiEndPoint)
                .build();

        try {
            someActivityResultLauncher.launch(PhonePe.getImplicitIntent(this, b2BPGRequest,packageName));
            isApiCall=false;
            checkStatus(false);
        } catch(PhonePeInitException e){
            e.printStackTrace();
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                LogUtil.e("resp","getResultCode:"+result.getResultCode());
                //if (result.getResultCode() == Activity.RESULT_OK) {
                isApiCall=true;
                checkStatus(true);
                //}
            });

    private void checkStatus(boolean isShow){

        JSONObject options = new JSONObject();

        try {
            options.put("trans_id", ORDERID);
            options.put("user_id", preferences.getUserModel().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e("resp","checkStatus:"+options);
        HttpRestClient.postJSON(mContext, false,  ApiManager.PHONEPE_CHECK_STATUS, options, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("resp","checkStatus:"+responseBody);

                updateDialog(responseBody.optString("code"),isShow);

            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void updateDialog(String tag,boolean isShow){

        if (statusDialog!=null && !statusDialog.isShowing() && isShow){
            showDialog();
        }

        ImageView imgStatus=statusDialog.findViewById(R.id.imgStatus);
        TextView txtTitle=statusDialog.findViewById(R.id.txtTitle);
        TextView txtTransId=statusDialog.findViewById(R.id.txtTransId);
        Button btnClose=statusDialog.findViewById(R.id.btnClose);

        if (tag.equalsIgnoreCase("PAYMENT_SUCCESS")){
            imgStatus.setImageResource(R.drawable.transaction_success_icon);
            txtTitle.setText("Transaction Success");
            txtTransId.setVisibility(View.GONE);
        }
        else if (tag.equalsIgnoreCase("PAYMENT_ERROR")){
            imgStatus.setImageResource(R.drawable.transaction_fail_icon);
            txtTitle.setText("Transaction Failed");
            txtTransId.setVisibility(View.GONE);
        }
        else if (tag.equalsIgnoreCase("PAYMENT_PENDING") || tag.equalsIgnoreCase("RES_BLANK")){
            imgStatus.setImageResource(R.drawable.transaction_pending_icon);
            txtTitle.setText("Transaction Pending");
            txtTransId.setVisibility(View.VISIBLE);
        }
        else {
            imgStatus.setImageResource(R.drawable.transaction_fail_icon);
            txtTitle.setText("Transaction Failed");
            txtTransId.setVisibility(View.GONE);
        }

        txtTransId.setText("If your transaction is success, we will automatic update in your wallet.");

        btnClose.setOnClickListener(v -> {
            isApiCall=false;
            statusDialog.dismiss();
            finish();
        });

        if (tag.equalsIgnoreCase("PAYMENT_PENDING") ||
                tag.equalsIgnoreCase("RES_BLANK")){
            if (isApiCall)
                checkStatus(isShow);
        }
    }

    private void initStatusDialog(){
        statusDialog=new Dialog(mContext);
        statusDialog.setContentView(R.layout.payment_status_dialog);
    }

    private void showDialog(){
        if (isApiCall)
            statusDialog.show();

        Window window = statusDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void clickEvents() {
        back_btn.setOnClickListener(view -> onBackPressed());

        /*cashFreeWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getCashfreeToken();
                cashFreeMethod = "web";
                getCashfreeToken2("web",null);
            }
        });

        layEaseBuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preferences.getUpdateMaster().getDisplay_deposit_easebuzz()==null || !preferences.getUpdateMaster().getDisplay_deposit_easebuzz().equalsIgnoreCase("Yes")) {
                    CustomUtil.showTopSneakWarning(mContext, "Deposit with Easebuzz gateway is currently unavailable. Please try after some time");
                } else {
                    getEasebuzzToken();
                }
            }
        });*/
    }

    private void initData() {

        back_btn = findViewById(R.id.back_btn);
        layUpi = findViewById(R.id.layUpi);
        recyclerPayment = findViewById(R.id.recyclerPayment);

        LinearLayoutManager layoutManager=new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        recyclerPayment.setLayoutManager(layoutManager);

    }

    private void addPaymentSetting() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());

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
                        ArrayList<PaymentSettingModel> list=new ArrayList<>();
                        boolean isUpi=false;

                       /* if (ConstantUtil.is_game_test){
                            JSONObject obj0=payment_settings.optJSONObject(0);
                            JSONObject obj1=payment_settings.optJSONObject(1);
                            obj0.put("is_enable_android","yes");
                            obj0.put("gateway_type","phonepe");

                           JSONArray payment_type=obj1.optJSONArray("payment_type");
                           payment_type.optJSONObject(payment_type.length()-1).put("is_enable_android","yes");
                           payment_type.optJSONObject(payment_type.length()-1).put("gateway_type","phonepe");
                           payment_type.optJSONObject(payment_type.length()-1).put("tag_android","phonepe");
                           payment_type.optJSONObject(payment_type.length()-1).put("title","Pay with PhonePe");

                        }*/

                        for (int i = 0; i < payment_settings.length(); i++) {
                            JSONObject object=payment_settings.optJSONObject(i);
                            PaymentSettingModel model=gson.fromJson(object.toString(),PaymentSettingModel.class);

                            if (model.getTag().equalsIgnoreCase("upi")){
                                if (model.getIs_enable_android().equalsIgnoreCase("yes")) {
                                    isUpi = true;
                                    upiModel=model;
                                    if (ConstantUtil.is_game_test){
                                        upiModel.setGatewayType("phonepe");
                                    }
                                }
                            }else {
                                list.add(model);
                            }
                        }

                        PaymentSettingAdapter adapter=new PaymentSettingAdapter(mContext,list);
                        recyclerPayment.setAdapter(adapter);

                        if (isUpi){
                            layUpi.setVisibility(View.VISIBLE);
                            if (upiModel.getGatewayType().equalsIgnoreCase("phonepe"))
                                initPhonePeUpiApps();
                            else
                                initUpiApps();
                            //initUpiApps();
                        }else {
                            layUpi.setVisibility(View.GONE);
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

    private void getCashfreeToken2(String tag, CFUPIApp cfupiApp,PaymentSettingModel.PaymentType paymentType) {
        //SimpleDateFormat format = MyApp.changedFormat("yyyy_MM_dd-HH_mm_ss");
        //String date = format.format(new Date());
        //long tsLong = System.currentTimeMillis()/1000;
        ORDERID = generateOrderId("FFCF");//"FFCF."+ tsLong+ "." + preferences.getUserModel().getId();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", preferences.getUserModel().getId());
            jsonObject.put("customer_name", preferences.getUserModel().getUserTeamName());
            jsonObject.put("customer_phone", preferences.getUserModel().getPhoneNo());
            jsonObject.put("customer_email", preferences.getUserModel().getEmailId());
            jsonObject.put("orderCurrency", "INR");
            jsonObject.put("orderAmount", CustomUtil.getFormater("0.00").format(amount));
            jsonObject.put("orderId", ORDERID);
            jsonObject.put("coupon_id", couponCodeId);
            if (paymentType!=null)
                jsonObject.put("payment_method",paymentType.getTag());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url="";
        if (paymentType==null)
            url=upiModel.getTokenGenerateUrl();
        else
            url=paymentType.getTokenGenerateUrl();

        LogUtil.e("resp",jsonObject.toString()+"\n"+url);
        HttpRestClient.postJSON(mContext, true, url, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                   /* if (tag.equalsIgnoreCase("web")){
                        sendCashFree2(responseBody.optJSONObject("data"));
                    }else if (tag.equalsIgnoreCase("upi")){
                        sendCashFree2Upi(responseBody.optJSONObject("data"),cfupiApp);
                    }*/
                    if (paymentType==null){
                        sendCashFree2Upi(responseBody.optJSONObject("data"),cfupiApp);
                    }
                    else {
                        if (paymentType.getIs_web_view_android().equalsIgnoreCase("yes")){
                            JSONObject data=responseBody.optJSONObject("data");
                            String web_view_url=data.optString("web_view_url");
                            if (paymentType.getTag().equalsIgnoreCase("cashfree")){
                                web_view_url=web_view_url+"&displayMode=All";
                            }
                            else if (paymentType.getTag().equalsIgnoreCase("wallet")){
                                web_view_url=web_view_url+"&displayMode=app";
                            }
                            else {
                                web_view_url=web_view_url+"&displayMode="+paymentType.getTag();
                            }
                            startActivity(new Intent(mContext,PaymentWebActivity.class).putExtra("payment_url",web_view_url)
                                    .putExtra("order_id",ORDERID)
                                    .putExtra("success_url",paymentType.getTrans_success_url())
                                    .putExtra("fail_url",paymentType.getTrans_fail_url())
                            );
                            finish();
                        }
                        else {
                            if (tag.equalsIgnoreCase("upi")){
                                if (paymentType==null)
                                    sendCashFree2Upi(responseBody.optJSONObject("data"),cfupiApp);
                                else
                                    sendCashFree2(responseBody.optJSONObject("data"),tag);
                            }
                            else {
                                sendCashFree2(responseBody.optJSONObject("data"),tag);
                            }
                        }
                    }
                }else {
                    CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });

    }

    public void choosePaymentOption(PaymentSettingModel.PaymentType paymentType){
        if (paymentType.getTag().equalsIgnoreCase("easebuzz")){
            getEasebuzzToken();
        }else {
            if (paymentType.getGatewayType().equalsIgnoreCase("phonepe")){
                generateCheckSum("PAY_PAGE","");
            }else
                getCashfreeToken2(paymentType.getTag(), null, paymentType);

            //getCashfreeToken2(paymentType.getTag(), null, paymentType);
        }
    }

    private void getEasebuzzToken() {
        //SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss",Locale.US);
        //String date = format.format(new Date());
        ORDERID = generateOrderId("EB");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("email", preferences.getUserModel().getEmailId());
            jsonObject.put("firstname", preferences.getUserModel().getUserTeamName());
            jsonObject.put("phone", preferences.getUserModel().getPhoneNo());
           /* jsonObject.put("key", "INR");
            jsonObject.put("hash", "INR");*/
            jsonObject.put("amount", CustomUtil.getFormater("0.00").format(amount));
            jsonObject.put("txnid", ORDERID);
            jsonObject.put("api_name", "initiate_payment");
            jsonObject.put("address1", "");
            jsonObject.put("address2", "");
            jsonObject.put("city", "");
            jsonObject.put("country", "");
            jsonObject.put("state", "");
            jsonObject.put("udf1", preferences.getUserModel().getId());
            jsonObject.put("udf2", "");
            jsonObject.put("udf3", "");
            jsonObject.put("udf4", "");
            jsonObject.put("udf5", "");
            jsonObject.put("udf6", "");
            jsonObject.put("udf7", "");
            jsonObject.put("zipcode", "");
            jsonObject.put("productinfo", "Fantafeat");
            jsonObject.put("furl", ApiManager.BASE+"easebuzz_fail.php");
            jsonObject.put("surl", ApiManager.BASE+"easebuzz_success.php");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "getEasebuzzToken: " + jsonObject.toString());
        HttpRestClient.postJSONNormal(mContext, true, ApiManager.easebuzz, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                try {
                    LogUtil.e(TAG, "getEasebuzzToken: " + responseBody.toString());
                    LogUtil.e(TAG, "getEasebuzzToken: " + responseBody.optJSONObject("data").optString("access_key"));
                    if (responseBody.optBoolean("status")) {
                        /*Intent intentProceed = new Intent(PaymentActivity.this, PWECouponsActivity.class);
                        intentProceed.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // This is mandatory flag
                        intentProceed.putExtra("access_key",responseBody.optJSONObject("data").optString("access_key"));
                        intentProceed.putExtra("pay_mode","production");//This will either be “test” or “production
                        startActivityForResult(intentProceed, PWEStaticDataModel.PWE_REQUEST_CODE);*/
                        //easeBuzzPayLauncher.launch(intentProceed);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == PWEStaticDataModel.PWE_REQUEST_CODE){
            String result = data.getStringExtra("result");
            String payment_response = data.getStringExtra("payment_response");
            if (result.equalsIgnoreCase("payment_successfull")){
                Toast.makeText(mContext,"Payment successfully added to your wallet.",Toast.LENGTH_LONG).show();
                finish();
            }else if (result.equalsIgnoreCase("payment_failed")){
                CustomUtil.showTopSneakError(mContext,"Payment failed, Please try again.");
            }else if (result.equalsIgnoreCase("user_cancelled")){
                CustomUtil.showTopSneakWarning(mContext,"User cancel payment request");
            }else {
                CustomUtil.showTopSneakWarning(mContext,"Something went wrong.");
            }

            LogUtil.e(TAG, "onSuccessResult: " + result+"    "+payment_response);
        }*/
    }

    private void sendCashFree2(JSONObject cfObject,String tag) {
        try {
            CFSession cfSession = new CFSession.CFSessionBuilder()
                    .setEnvironment(CFSession.Environment.PRODUCTION)
                    .setPaymentSessionID(cfObject.optString("payment_session_id"))
                    .setOrderId(cfObject.optString("order_id"))
                    .build();

            JSONArray allowdata=cfObject.optJSONArray("allowdata");
            ArrayList<String> allowList=new ArrayList<>();
            for (int i=0;i<allowdata.length();i++){
                allowList.add(allowdata.optString(i));
            }

            CFPaymentComponent.CFPaymentComponentBuilder cfPaymentComponent = new CFPaymentComponent.CFPaymentComponentBuilder();

            if (tag.equalsIgnoreCase("card"))
                cfPaymentComponent.add(CFPaymentComponent.CFPaymentModes.CARD);
            else if (tag.equalsIgnoreCase("netbanking"))
                cfPaymentComponent.add(CFPaymentComponent.CFPaymentModes.NB);
            else if (tag.equalsIgnoreCase("wallet"))
                cfPaymentComponent.add(CFPaymentComponent.CFPaymentModes.WALLET);
            else if (tag.equalsIgnoreCase("cashfree")){
                if (allowList.contains("card")){
                    cfPaymentComponent.add(CFPaymentComponent.CFPaymentModes.CARD);
                }
                if (allowList.contains("paylater")){
                    cfPaymentComponent.add(CFPaymentComponent.CFPaymentModes.PAY_LATER);
                }
                if (allowList.contains("wallet")){
                    cfPaymentComponent.add(CFPaymentComponent.CFPaymentModes.WALLET);
                }
                if (allowList.contains("emi")){
                    cfPaymentComponent.add(CFPaymentComponent.CFPaymentModes.EMI);
                }
                if (allowList.contains("netbanking")){
                    cfPaymentComponent.add(CFPaymentComponent.CFPaymentModes.NB);
                }
                if (allowList.contains("upi")){
                    cfPaymentComponent.add(CFPaymentComponent.CFPaymentModes.UPI);
                }
            }

            CFTheme cfTheme = new CFTheme.CFThemeBuilder()
                    .setNavigationBarBackgroundColor("#B20017")
                    .setNavigationBarTextColor("#ffffff")
                    .setButtonBackgroundColor("#B20017")
                    .setButtonTextColor("#ffffff")
                    .setPrimaryTextColor("#000000")
                    .setSecondaryTextColor("#000000")
                    .build();
            CFDropCheckoutPayment cfDropCheckoutPayment = new CFDropCheckoutPayment.CFDropCheckoutPaymentBuilder()
                    .setSession(cfSession)
                    .setCFUIPaymentModes(cfPaymentComponent.build())
                    .setCFNativeCheckoutUITheme(cfTheme)
                    .build();
            CFPaymentGatewayService gatewayService = CFPaymentGatewayService.getInstance();
            gatewayService.doPayment(PaymentActivity.this, cfDropCheckoutPayment);
        } catch (CFException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendCashFree2Upi(JSONObject cfObject,CFUPIApp cfupiApp) {
        try {
            CFSession cfSession = new CFSession.CFSessionBuilder()
                    .setEnvironment(CFSession.Environment.PRODUCTION)
                    .setPaymentSessionID(cfObject.optString("payment_session_id"))
                    .setOrderId(cfObject.optString("order_id"))
                    .build();

            CFUPI cfupi = new CFUPI.CFUPIBuilder()
                    .setMode(CFUPI.Mode.INTENT)
                    .setUPIID(cfupiApp.getAppId()) //getUPIID using CFUPIUtil.getInstalledUPIApps()
                    .build();

            CFUPIPayment cfupiPayment = new CFUPIPayment.CFUPIPaymentBuilder()
                    .setSession(cfSession)
                    .setCfUPI(cfupi)
                    .build();

            CFCorePaymentGatewayService.getInstance().doPayment(PaymentActivity.this, cfupiPayment);

        } catch (CFException e) {
            e.printStackTrace();
        }
    }

    private String generateOrderId(String gateway){
        long tsLong = System.currentTimeMillis()/1000;
        return gateway.toUpperCase()+"_"+ tsLong+ "_" + preferences.getUserModel().getId();
    }

    private void sendRequest() {
        JSONObject addMoney = new JSONObject();
        try {
            addMoney.put("paytmChecksum", CHECKSUM);
            addMoney.put("amount", String.valueOf(amount));
            addMoney.put("user_id", preferences.getUserModel().getId());
            addMoney.put("bankName", bankName);
            addMoney.put("orderId", orderId);
            addMoney.put("paymentMode", paymrntMode);
            addMoney.put("bankId", bankId);
            addMoney.put("coupon_id", couponCodeId);
            addMoney.put("currency", currency);
            addMoney.put("txnId", txnId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, true, ApiManager.ADD_AMOUNT, addMoney, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject jsonObject, int code) {
                try {
                    if (jsonObject.optBoolean("status")) {
                        couponCodeId="";
                        MyApp.getMyPreferences().setPref(PrefConstant.PAYMENT_SUCCESS,true);
                        JSONObject jo = null;

                        jo = jsonObject.getJSONObject("data");

                        float bonus = CustomUtil.convertFloat(jo.optString("bonus"));
                        float deposit = CustomUtil.convertFloat(jo.optString("deposit"));

                        float currentBal = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
                        float currentBonus = CustomUtil.convertFloat(preferences.getUserModel().getBonusBal());

                        UserModel user = preferences.getUserModel();
                        user.setDepositBal(String.valueOf(deposit + currentBal));


                        float total;
                        if (ConstantUtil.is_bonus_show){
                            user.setBonusBal(String.valueOf(currentBonus + bonus));
                            total =  CustomUtil.convertFloat(user.getDepositBal()) + CustomUtil.convertFloat(user.getWinBal()) +
                                    CustomUtil.convertFloat(user.getFf_coin()) + CustomUtil.convertFloat(user.getBonusBal());

                        }else {
                            total =   CustomUtil.convertFloat(user.getDepositBal()) + CustomUtil.convertFloat(user.getWinBal()) +
                                    CustomUtil.convertFloat(user.getFf_coin());

                        }
                        /*float total = CustomUtil.convertFloat(user.getDepositBal()) + CustomUtil.convertFloat(user.getWinBal()) +
                                CustomUtil.convertFloat(user.getFf_coin()) + CustomUtil.convertFloat(user.getBonusBal());*/

                        user.setTotal_balance(total);
                        preferences.setUserModel(user);
                        if (isDirectJoin){
                            Intent intent=new Intent();
                            intent.putExtra("isDirectJoin", isDirectJoin);
                            setResult(Activity.RESULT_OK,intent);
                        }
                        finish();


                    } else {
                        Toast.makeText(mContext, "Something Wrong Please try again", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                Toast.makeText(mContext
                        , "Somthing went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequestCode && data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    LogUtil.e(TAG, "onActivityResult: " + key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
                }
            }
         //   LogUtil.e(TAG, "onActivityResult: " + data.getStringExtra("nativeSdkForMerchantMessage") + data.getStringExtra("response"));
        }
        if (requestCode == 1007 && data != null) {
            try {
                Bundle inResponse = data.getExtras();
                LogUtil.e(TAG, "onActivityResult: " + inResponse);
                for (String key : inResponse.keySet()) {
                    LogUtil.e(TAG, "onActivityResult: " + key + " : " + (inResponse.get(key) != null ? inResponse.get(key) : "NULL"));
                }
                if (inResponse != null) {
                    String response=data.getStringExtra("response");
                    JSONObject object=new JSONObject(response);
                    status = object.getString("STATUS");
                    if (status.equals("TXN_SUCCESS")) {
                        bankName = object.getString("BANKNAME");
                        orderId = object.getString("ORDERID");
                        txnAmount = object.getString("TXNAMOUNT");
                        paymrntMode = object.getString("PAYMENTMODE");
                        bankId = object.getString("BANKTXNID");
                        currency = object.getString("CURRENCY");
                        gatewayName = object.getString("GATEWAYNAME");
                        txnId = object.getString("TXNID");
                        sendRequest();
                        //getState();
                    } else {
                        if (inResponse.containsKey("RESPCODE")) {
                            // failResponse(inResponse.getString("RESPCODE"), inResponse.toString());
                            CustomUtil.showTopSneakWarning(PaymentActivity.this,"Something went wrong Try after Some Time");
                        } else {
                            // failResponse("", inResponse.toString());
                            CustomUtil.showTopSneakWarning(PaymentActivity.this,"Something went wrong Try after Some Time");
                        }

                        Toast.makeText(mContext, "Something went wrong Try after Some Time", Toast.LENGTH_LONG).show();
                    }
                }
                LogUtil.e(TAG, "onActivityResult: " + data.getStringExtra("nativeSdkForMerchantMessage") + data.getStringExtra("response"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (data != null) {
            Bundle bundle = data.getExtras();
            //LogUtil.e(TAG, "onActivityResult: " + bundle.getString("txStatus", "")+"  " + bundle.getString("type")+" "+cashFreeMethod);
            if (bundle != null)
                if (bundle.getString("type", "").equalsIgnoreCase("CashFreeResponse")) {
                    if (bundle.getString("txStatus", "").equalsIgnoreCase("CANCELLED")) {
                        //failResponse("" + CFPaymentService.REQ_CODE, bundle.getString("txMsg", "Payment Cancel"));
                    } else if (bundle.getString("txStatus", "").equalsIgnoreCase("FAILED")) {
                        //failResponse("" + CFPaymentService.REQ_CODE, bundle.getString("txMsg", ""));
                        if (!cashFreeMethod.equals("web")) {
                          */
    /*  Map<String, String> params = new HashMap<>();

                            params.put(PARAM_APP_ID, APP_ID);
                            params.put(PARAM_ORDER_ID, ORDERID);
                            params.put(PARAM_ORDER_AMOUNT, String.valueOf(amount));
                            params.put(PARAM_ORDER_NOTE, "Desposit");
                            params.put(PARAM_CUSTOMER_NAME, preferences.getUserModel().getDisplayName());
                            params.put(PARAM_CUSTOMER_PHONE, preferences.getUserModel().getPhoneNo());
                            params.put(PARAM_CUSTOMER_EMAIL, preferences.getUserModel().getEmailId());
                            params.put(PARAM_ORDER_CURRENCY, "INR");

                            CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
                            cfPaymentService.setOrientation(0);
                            cfPaymentService.doPayment((Activity) mContext, params, cashFreeToken, stage, "#01579B", "#FFFFFF", true);*/
    /*
                        } else {
                            CustomUtil.showTopSneakWarning(PaymentActivity.this,"Transaction Cancelled");
                        }
                    } else if (bundle.getString("txStatus", "").equalsIgnoreCase("SUCCESS")) {
                        JSONObject paymentData = new JSONObject();
                        try {
                            paymentData.put("paymentMode", bundle.getString("paymentMode", ""));
                            paymentData.put("orderId", bundle.getString("orderId", ""));
                            paymentData.put("txTime", bundle.getString("txTime", ""));
                            paymentData.put("referenceId", bundle.getString("referenceId", ""));
                            paymentData.put("signature", bundle.getString("signature", ""));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        addCashFreeAmount(paymentData);
                    }
                }
        }
    }*/

    private void addCashFreeAmount(JSONObject paymentData) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("orderId", ORDERID);
            jsonObject.put("amount", CustomUtil.getFormater("0.00").format(amount));
            jsonObject.put("coupon_id", couponCodeId);
            jsonObject.put("data", paymentData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.ADD_CASH_CASHFREE, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    couponCodeId="";
                    JSONObject jo = null;
                    try {
                        jo = responseBody.getJSONObject("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    float bonus = CustomUtil.convertFloat(jo.optString("bonus"));
                    float deposit = CustomUtil.convertFloat(jo.optString("deposit_amt"));

                    float currentBal = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
                    float currentBonus = CustomUtil.convertFloat(preferences.getUserModel().getBonusBal());
                    UserModel user = preferences.getUserModel();
                    user.setDepositBal(String.valueOf(deposit + currentBal));
                    user.setBonusBal(String.valueOf(currentBonus + bonus));
                    float total = CustomUtil.convertFloat(user.getDepositBal()) + CustomUtil.convertFloat(user.getWinBal()) +
                            CustomUtil.convertFloat(user.getFf_coin()) + CustomUtil.convertFloat(user.getBonusBal());
                    user.setTotal_balance(total);
                    MyApp.getMyPreferences().setUserModel(user);
                    MyApp.getMyPreferences().setPref(PrefConstant.PAYMENT_SUCCESS,true);
                    if (isDirectJoin){
                        Intent intent=new Intent();
                        intent.putExtra("isDirectJoin", isDirectJoin);
                        setResult(Activity.RESULT_OK,intent);
                    }
                    finish();
                } else {
                    Toast.makeText(mContext, "Something Wrong Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void checkCashFreeAmount(String orderID) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("orderId", orderID);
            jsonObject.put("amount", CustomUtil.getFormater("0.00").format(amount));
            jsonObject.put("coupon_id", couponCodeId);
            jsonObject.put("gateway", "cashfree");
            // jsonObject.put("data", paymentData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.e("param","checkCashFreeAmount: "+jsonObject);
        HttpRestClient.postJSON(mContext, true, ApiManager.ADD_CASH_CASHFREE, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "checkCashFreeAmount: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    Toast.makeText(mContext, "Payment successfully added to your wallet.", Toast.LENGTH_LONG).show();
                    getUserDetails();

                } else {
                    CustomUtil.showTopSneakWarning(mContext,responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void getUserDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("token_no", preferences.getUserModel().getTokenNo());
            Log.e(TAG, "getUserDetails: " + jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, false, ApiManager.AUTHV3GetUserDetails, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                Log.e(TAG, "USER onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    if (responseBody.optString("code").equals("200")) {
                        JSONObject data = responseBody.optJSONObject("data");
                        UserModel userModel = gson.fromJson(data.toString(), UserModel.class);
                        preferences.setUserModel(userModel);
                        if (isDirectJoin){
                            Intent intent=new Intent();
                            intent.putExtra("isDirectJoin", isDirectJoin);
                            setResult(Activity.RESULT_OK,intent);
                        }
                        finish();
                    }
                } else {
                    preferences.setUserModel(new UserModel());
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                Log.e(TAG, "onFailureResult: " );
            }
        });
    }

    @Override
    public void onPaymentVerify(String orderID) {
        LogUtil.e(TAG, "onPaymentVerify: " + orderID);
        checkCashFreeAmount(orderID);
    }

    @Override
    public void onPaymentFailure(CFErrorResponse cfErrorResponse, String orderID) {
        LogUtil.e(TAG, "onPaymentFailure: " + cfErrorResponse.getMessage()+"    "+orderID);
        CustomUtil.showTopSneakError(mContext,cfErrorResponse.getMessage());
    }
}
