package com.fantafeat.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.fantafeat.R;
import com.fantafeat.util.CFWebIntentJSInterface;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PaymentWebActivity extends AppCompatActivity implements CFWebIntentJSInterface.CFWebIntentInterface{

    private WebView webPayment;
    private String url="",order_id="",checkOrderUrl="",success_url="",fail_url="";
    private boolean backEnabled = true;
    private static final int REQ_CODE_UPI = 9901;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web);

        webPayment=findViewById(R.id.webPayment);

        url=getIntent().getStringExtra("payment_url");
        order_id=getIntent().getStringExtra("order_id");
        success_url=getIntent().getStringExtra("success_url");
        fail_url=getIntent().getStringExtra("fail_url");
        LogUtil.e("resp",order_id);

        checkOrderUrl="https://fantafeat.com/checkCfOrederStatusAndroid.php?order_id="+order_id;

        /*webPayment.getSettings().setUseWideViewPort(true);
        webPayment.setVerticalScrollBarEnabled(false);
        webPayment.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);*/

        webPayment.setWebViewClient(new WebViewClient(){

            /*public String getMimeType(String url) {
                String type = null;
                String extension = MimeTypeMap.getFileExtensionFromUrl(url);
                if (extension != null) {
                    if (extension.equals("js")) {
                        return "text/javascript";
                    }
                    else if(extension.equals("html")) {
                        return "text/html";
                    }
                    else if (extension.equals("woff")) {
                        return "application/font-woff";
                    }
                    else if (extension.equals("woff2")) {
                        return "application/font-woff2";
                    }
                    else if (extension.equals("ttf")) {
                        return "application/x-font-ttf";
                    }
                    else if (extension.equals("eot")) {
                        return "application/vnd.ms-fontobject";
                    }
                    else if (extension.equals("svg")) {
                        return "image/svg+xml";
                    }
                    type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                }
                return type;
            }

            @SuppressLint("NewApi")
            @Override
            public WebResourceResponse shouldInterceptRequest(final WebView view, String url) {
                if(url.contains("https://www.fantafeat.com/")) {
                    url = url.replace("https://www.fantafeat.com/", "");

                    try {
                        return new WebResourceResponse(getMimeType(url), "UTF-8", getAssets().open(url));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }*/
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                LogUtil.e("resp",url);
                if (url.contains(success_url)){
                    CustomUtil.showToast(getApplicationContext(),"Deposit added Successfully");
                    finish();
                }
                else if (url.contains(fail_url)){
                    CustomUtil.showToast(getApplicationContext(),"Transaction failed, Try again with other payment method.");
                    finish();
                }
            }

           /* @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }*/
        });

        // webPayment.setWebViewClient(new WebViewClient());
        webPayment.setWebChromeClient(new WebChromeClient());

        webPayment.getSettings().setJavaScriptEnabled(true);
        webPayment.getSettings().setDomStorageEnabled(true);

        //webPayment.getSettings().setSupportMultipleWindows(false);
        //webPayment.getSettings().setAppCacheEnabled(true);

        CFWebIntentJSInterface wbInInterface = new CFWebIntentJSInterface(this);
        webPayment.addJavascriptInterface(wbInInterface, "Android");

        webPayment.getSettings().setPluginState(WebSettings.PluginState.ON);
        webPayment.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webPayment.getSettings().setAllowFileAccess(true);
        webPayment.getSettings().setLoadWithOverviewMode(true);
        webPayment.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webPayment.getSettings().setAllowContentAccess(true);
        webPayment.getSettings().setDomStorageEnabled(true);
        //webPayment.setWebChromeClient(new WebChromeClient());

        webPayment.loadUrl(url);
    }

    @Override
    public List<ResolveInfo> getAppList(String link) {
        ArrayList<ResolveInfo> resolveInfos = new ArrayList<>();
        Intent shareIntent = new Intent(Intent.ACTION_VIEW);
        shareIntent.setData(Uri.parse(link));
        final List<ResolveInfo> resInfos = getPackageManager().queryIntentActivities(shareIntent, 0);
        if (resInfos != null) {
            resolveInfos = new ArrayList<>(resInfos);
        }
        return resolveInfos;
    }

    @Override
    public String getAppName(ApplicationInfo pkg) {
        return (String) getPackageManager().getApplicationLabel(pkg);
    }

    @Override
    public void openApp(String appPkg, String url) {
        LogUtil.e("openApp","openApp"+url);
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        runOnUiThread(() -> {
            if ("others.upiapp".equals(appPkg)) {
                Intent chooser = Intent.createChooser(intent, "Pay with");
                someActivityResultLauncher.launch(chooser);
            } else {
                intent.setPackage(appPkg);
                someActivityResultLauncher.launch(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode()==RESULT_OK){
                    //CustomUtil.showToast(getApplicationContext(),"Deposit added Successfully");
                    //finish();
                    //webPayment.loadUrl(checkOrderUrl);
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            checkOrder();
                        }
                    }, 2000);

                }
                else if (result.getResultCode()==RESULT_CANCELED){
                    CustomUtil.showToast(getApplicationContext(),"User Cancelled Request");
                    finish();
                }else {
                    CustomUtil.showToast(getApplicationContext(),"Transaction failed, Try again with other payment method.");
                    finish();
                }
                LogUtil.e("openApp","openApp"+result.getResultCode()+"  "+result.getData());
                /*if (result.getResultCode() == Activity.RESULT_OK) {
                    webPayment.evaluateJavascript("window.showVerifyUI()", (ValueCallback<String>) s -> {});
                }*/
            });

    private void checkOrder() {
        LogUtil.e("resp","checkOrderUrl: "+checkOrderUrl);
        HttpRestClient.postJSONNormal(this, true, checkOrderUrl, new JSONObject(), new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("TAG", "checkOrder: " + responseBody.toString());
                CustomUtil.showToast(getApplicationContext(),responseBody.optString("msg"));
                finish();
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    @Override
    public void enableDisableCancelButton(Boolean flag) {
        backEnabled = flag;
    }

    @Override
    public void getResult(String result) {
        LogUtil.e("openApp","openApp123: "+result);
        runOnUiThread(() -> {
            if (result.equalsIgnoreCase("success")){
                //CustomUtil.showToast(PaymentWebActivity.this,"Deposit added Successfully");
                Toast.makeText(getApplicationContext(),"Deposit added Successfully",Toast.LENGTH_LONG).show();
                // CustomUtil.showTopSneakSuccess();
                finish();
            }
            else if (result.equalsIgnoreCase("fail")){
                //CustomUtil.showToast(PaymentWebActivity.this,"Deposit added Failed");
                Toast.makeText(getApplicationContext(),"Transaction failed, Try again with other payment method.",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (backEnabled) {
            //super.onBackPressed();
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Are you sure you want to exit from this page?");
            alert.setPositiveButton("Yes", (dialogInterface, i) -> finish());
            alert.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
            alert.show();
            /*if (webPayment.canGoBack()){
                webPayment.goBack();
            }else
                super.onBackPressed();*/
        }
    }

}