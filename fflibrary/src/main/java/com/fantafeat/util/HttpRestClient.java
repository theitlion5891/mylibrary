package com.fantafeat.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class HttpRestClient {

    private static String TAG = "HttpRestClient";
    private static MCrypt mCrypt = new MCrypt();
    private static Dialog dialog = null;
    private static int NETWORK_ERROR = 1111;
    private static int EXCEPTION_ERROR = 2222;
    private static  BottomSheetDialog bottomSheetDialog = null;
    private static Boolean isNetworkDialog=false;
    private static Boolean isLogoutDialog=false;
    private static CountDownTimer timerr;

    private static void showDialog(Context mContext, boolean showProgress) {
        if (showProgress) {
            dialog = new Dialog(mContext, R.style.Theme_Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.setContentView(R.layout.dialog_loading);
            if (!(mContext instanceof Activity && ((Activity) mContext).isFinishing())) {
                dialog.show();
            }
        }
    }

    private static void hideDialog(boolean showProgress) {
        try {
            if (dialog != null && showProgress && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private static void logoutUser(final Context mContext){

        Button btn_ok;
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_session_out, null);
        btn_ok = view.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                // MyApp.getMyPreferences().logout();
                MyApp.getMyPreferences().setPref(PrefConstant.APP_IS_LOGIN,false);
                ((Activity)mContext).finishAffinity();
            }
        });
        bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);
        if (!isLogoutDialog){
            if (!(mContext instanceof Activity && ((Activity) mContext).isFinishing())) {
                isLogoutDialog=true;
                bottomSheetDialog.show();
            }
        }


    }

    private static JSONObject decryptData(String data) {
        //LogUtil.e(TAG, "decryptData: " + data);
        try {

            return new JSONObject(CustomUtil.replaceNull(mCrypt.Decrypt(data)));
        } catch (JSONException e) {
            e.printStackTrace();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("status", false);
                jsonObject.put("message", "Data not Formatted");
                return jsonObject;
            } catch (JSONException je) {
                LogUtil.e(TAG, "decryptData: " + je.getLocalizedMessage());
            }
            return new JSONObject();
        }
    }

    public static void showNetworkDialog(Context mContext,InternetConnection connection){
        /*NetworkDialogSheet bottomSheetTeam = new NetworkDialogSheet(mContext);
        bottomSheetTeam.setCancelable(false);
        bottomSheetTeam.show(((FragmentActivity)mContext).getSupportFragmentManager(),NetworkDialogSheet.TAG);*/
        final ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
            /*connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED*/
            connection.onSuccess();
        } else {

            Button btn_ok;
            View view = LayoutInflater.from(mContext).inflate(R.layout.network_dialog, null);
            btn_ok = view.findViewById(R.id.btnClose);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
            btn_ok.setOnClickListener(view1 -> {
                bottomSheetDialog.dismiss();
                isNetworkDialog=false;
                showNetworkDialog(mContext, connection);
            });
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.setContentView(view);
            ((View) view.getParent()).setBackgroundResource(android.R.color.white);
            if (!((Activity)mContext).isFinishing())
            if (!isNetworkDialog) {
                if (!(mContext instanceof Activity && ((Activity) mContext).isFinishing())) {
                    isNetworkDialog=true;
                    bottomSheetDialog.show();
                }
            }
        }

    }

    private static void maintanceDialog(Context mContext, JSONObject data){
        Button btn_ok;
        View view = LayoutInflater.from(mContext).inflate(R.layout.maintance_dialog, null);
        btn_ok = view.findViewById(R.id.btnClose);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        txtTitle.setText("Maintenance");
        TextView txtSubTitle = view.findViewById(R.id.txtSubTitle);
        txtSubTitle.setText("App Under maintenance.\nWe will come back soon.");
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        btn_ok.setOnClickListener(view1 -> {
            bottomSheetDialog.dismiss();
            ((Activity) mContext).finishAffinity();
            System.exit(0);
        });
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);
        if (!((Activity)mContext).isFinishing())
            if (!(mContext instanceof Activity && ((Activity) mContext).isFinishing())) {
                bottomSheetDialog.show();
            }
    }

    public static void postJSONNormal(final Context mContext, final boolean showProgress, final String url, final JSONObject params,final GetApiResult getApiResult) {

        // Log.e("resp","Url : "+url);
        //final Dialog dialog = new Dialog(mContext, R.style.Theme_Dialog);

        hideDialog(showProgress);
        showDialog(mContext, showProgress);
        showNetworkDialog(mContext, () -> new Thread(() -> {

            final JSONObject responseObject;
           // LogUtil.e(TAG, "run: "+ params.toString()+" url: "+url ); //to check what I send to server
            responseObject = JSONParser.doPostNormal(params.toString(), url);
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideDialog(showProgress);

                    //LogUtil.e("response",responseObject+" responseObject");
                    //LogUtil.e(TAG, "output: "+ data); //to check what I get from server
                    if (responseObject.optBoolean("status")) {
                        JSONObject data=responseObject;
                        if(data.optString("code").equalsIgnoreCase("1000")){

                            logoutUser(mContext);
                        }
                        else if (data.optString("code").equalsIgnoreCase("1005")){//Force Update
                            //appUpdateDialog(mContext,data);
                            getApiResult.onSuccessResult(data, responseObject.optInt("response_code"));
                        }
                        else {
                            if ((responseObject.optBoolean("isSuccessful") || responseObject.optBoolean("isRedirect"))) {
                                getApiResult.onSuccessResult(data, responseObject.optInt("response_code"));
                            } else {
                                String message = mContext.getResources().getString(R.string.unexpected_error);
                                if (responseObject.optInt("response_code") >= 400 && responseObject.optInt("response_code") < 500) {
                                    message = mContext.getResources().getString(R.string.client_error);
                                } else if (responseObject.optInt("response_code") >= 500 && responseObject.optInt("response_code") < 600) {
                                    message = mContext.getResources().getString(R.string.server_error);
                                }
                                getApiResult.onFailureResult(message, responseObject.optInt("response_code"));
                            }
                        }
                    } else {
                        getApiResult.onSuccessResult(responseObject, responseObject.optInt("response_code"));
                    }
                }
            });

        }).start());


        /*showDialog(mContext, showProgress);
        CustomUtil.checkInternet(mContext, () -> new Thread(() -> {
            final JSONObject responseObject;

            responseObject = JSONParser.doPostNormal(params.toString(), url);
            ((Activity) mContext).runOnUiThread(() -> {
                hideDialog(showProgress);
                if (responseObject.optBoolean("status")) {
                    JSONObject data=responseObject;
                    //LogUtil.e(TAG, "data123:  " + data.optJSONObject("Data"));
                    if (data.optString("code").equalsIgnoreCase("1000")){
                        logoutUser(mContext);
                    }else if (data.optString("code").equalsIgnoreCase("1001")){
                        logoutUser(mContext,"User Saftey first","We have update security in our App. So, You have to login again");
                    }else if (data.optString("code").equalsIgnoreCase("1002")){
                        //logoutUser(mContext,"User Saftey first","We have update security in our App. So, You have to login again");
                        showOtpScreen(mContext,data);
                    }
                    else if (data.optString("code").equalsIgnoreCase("2000")){
                        maintanceDialog(mContext,data);
                    }else {
                        //getApiResult.onSuccessResult(data, CustomUtil.convertInt(data.optString("code")));
                        if ((responseObject.optBoolean("isSuccessful") || responseObject.optBoolean("isRedirect"))) {
                            getApiResult.onSuccessResult(data, responseObject.optInt("response_code"));
                        } else {
                            String message = mContext.getResources().getString(R.string.unexpected_error);
                            if (responseObject.optInt("response_code") >= 400 && responseObject.optInt("response_code") < 500) {
                                message = mContext.getResources().getString(R.string.client_error);
                            } else if (responseObject.optInt("response_code") >= 500 && responseObject.optInt("response_code") < 600) {
                                message = mContext.getResources().getString(R.string.server_error);
                            }
                            getApiResult.onFailureResult(message, responseObject.optInt("response_code"));
                        }
                    }
                }else {
                    getApiResult.onFailureResult(responseObject.optString("message"), EXCEPTION_ERROR);
                }

            });

        }).start());*/
    }


    public static void postJSON(final Context mContext, final boolean showProgress, final String url, final JSONObject params,
                                final GetApiResult getApiResult) {
        hideDialog(showProgress);
        showDialog(mContext, showProgress);
        showNetworkDialog(mContext, () -> new Thread(() -> {

        final JSONObject responseObject;
       // LogUtil.e(TAG, "run: "+ params.toString()+" url: "+url ); //to check what I send to server
        responseObject = JSONParser.doPost(mCrypt.Encrypt(params.toString()), url);
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideDialog(showProgress);
                JSONObject data=decryptData(responseObject.optString("Data"));
                //LogUtil.e(TAG, "output: "+ data); //to check what I get from server
                if (responseObject.optBoolean("status")) {
                    if(data.optString("code").equalsIgnoreCase("1000")){

                        logoutUser(mContext);
                    }
                    else {
                        if ((responseObject.optBoolean("isSuccessful") || responseObject.optBoolean("isRedirect"))) {
                            getApiResult.onSuccessResult(data, responseObject.optInt("response_code"));
                        } else {
                            String message = mContext.getResources().getString(R.string.unexpected_error);
                            if (responseObject.optInt("response_code") >= 400 && responseObject.optInt("response_code") < 500) {
                                message = mContext.getResources().getString(R.string.client_error);
                            } else if (responseObject.optInt("response_code") >= 500 && responseObject.optInt("response_code") < 600) {
                                message = mContext.getResources().getString(R.string.server_error);
                            }
                            getApiResult.onFailureResult(responseObject.optString("message")/*message*/, responseObject.optInt("response_code"));
                        }
                    }
                } else {
                    getApiResult.onFailureResult(responseObject.optString("message"), EXCEPTION_ERROR);
                    //getApiResult.onSuccessResult(responseObject, responseObject.optInt("response_code"));
                }
            }
        });

        }).start());

       /*
        if (!CustomUtil.checkConnection(mContext)) {
           // hideDialog(showProgress);
            showNetworkDialog(mContext, new InternetConnection() {
                @Override
                public void onSuccess() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final JSONObject responseObject;
                            //Log.e(TAG, "run: "+ params.toString() ); //to check what I send to server
                            responseObject = JSONParser.doPost(mCrypt.Encrypt(params.toString()), url);
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideDialog(showProgress);
                                    //Log.e(TAG, "output: "+ decryptData(responseObject.optString("Data"))); //to check what I get from server
                                    if (responseObject.optBoolean("status")) {
                                        if(decryptData(responseObject.optString("Data")).optString("code").equalsIgnoreCase("1000")){
                                            logoutUser(mContext);
                                        }else {
                                            if ((responseObject.optBoolean("isSuccessful") || responseObject.optBoolean("isRedirect"))) {
                                                getApiResult.onSuccessResult(decryptData(responseObject.optString("Data")), responseObject.optInt("response_code"));
                                            } else {
                                                String message = mContext.getResources().getString(R.string.unexpected_error);
                                                if (responseObject.optInt("response_code") >= 400 && responseObject.optInt("response_code") < 500) {
                                                    message = mContext.getResources().getString(R.string.client_error);
                                                } else if (responseObject.optInt("response_code") >= 500 && responseObject.optInt("response_code") < 600) {
                                                    message = mContext.getResources().getString(R.string.server_error);
                                                }
                                                getApiResult.onFailureResult(message, responseObject.optInt("response_code"));
                                            }
                                        }
                                    } else {
                                        getApiResult.onFailureResult(responseObject.optString("message"), EXCEPTION_ERROR);
                                    }
                                }
                            });
                        }
                    }).start();
                }
            });
            getApiResult.onFailureResult(mContext.getResources().getString(R.string.internet_error), NETWORK_ERROR);

        }*/
    }

    public static void postJSONWithParam(final Context mContext, final boolean showProgress, final String url, final JSONObject params,
                                final GetApiResult getApiResult) {
        hideDialog(showProgress);
        showDialog(mContext, showProgress);
        showNetworkDialog(mContext, () -> new Thread(() -> {

            final JSONObject responseObject;
           // LogUtil.e(TAG, "run: "+ params.toString()+" url: "+url ); //to check what I send to server
            responseObject = JSONParser.doPostNormal(mCrypt.Encrypt(params.toString()), url);
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideDialog(showProgress);
                    //JSONObject data=responseObject.optString("Data");//decryptData(responseObject.optString("Data"));
                    //LogUtil.e(TAG, "output: "+ data); //to check what I get from server
                    JSONObject data=responseObject;
                    if (responseObject.optBoolean("status")) {
                        if(data.optString("code").equalsIgnoreCase("1000")){
                            logoutUser(mContext);
                        }
                        else {
                            if ((responseObject.optBoolean("isSuccessful") || responseObject.optBoolean("isRedirect"))) {
                                getApiResult.onSuccessResult(data, responseObject.optInt("response_code"));
                            } else {
                                String message = mContext.getResources().getString(R.string.unexpected_error);
                                if (responseObject.optInt("response_code") >= 400 && responseObject.optInt("response_code") < 500) {
                                    message = mContext.getResources().getString(R.string.client_error);
                                } else if (responseObject.optInt("response_code") >= 500 && responseObject.optInt("response_code") < 600) {
                                    message = mContext.getResources().getString(R.string.server_error);
                                }
                                getApiResult.onFailureResult(message, responseObject.optInt("response_code"));
                            }
                        }
                    } else {
                        //getApiResult.onFailureResult(responseObject.optString("message"), EXCEPTION_ERROR);
                        getApiResult.onSuccessResult(responseObject, responseObject.optInt("response_code"));
                    }
                }
            });
        }).start());
    }

    public static void postDataFileWithVideo(final Context mContext, final boolean showProgress, final String url, final HashMap<String, String> params,
                                             final File image, final String image_name, final GetApiResult getApiResult) {
        final Dialog dialog = new Dialog(mContext, R.style.Theme_Dialog);
        showDialog(mContext, showProgress);

        if (!CustomUtil.checkConnection(mContext)) {
            hideDialog(showProgress);
            getApiResult.onFailureResult(mContext.getResources().getString(R.string.internet_error), NETWORK_ERROR);
        }
        else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final JSONObject responseObject;
                    responseObject = JSONParser.doPostRequestWithFileWithVideo(params, url, image, image_name);
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideDialog(showProgress);

                            if (responseObject.optBoolean("status")) {
                                //JSONObject data=decryptData(responseObject.optString("Data"));
                                JSONObject data=decryptData(responseObject.optString("Data"));
                                LogUtil.e(TAG, "data123:  " + data+"   url:"+url);
                                if (data.optString("code").equalsIgnoreCase("1000")){

                                    logoutUser(mContext);
                                }
                                else {
                                    //getApiResult.onSuccessResult(data, CustomUtil.convertInt(data.optString("code")));
                                    if ((responseObject.optBoolean("isSuccessful") || responseObject.optBoolean("isRedirect"))) {
                                        getApiResult.onSuccessResult(data, responseObject.optInt("response_code"));
                                    } else {
                                        String message = mContext.getResources().getString(R.string.unexpected_error);
                                        if (responseObject.optInt("response_code") >= 400 && responseObject.optInt("response_code") < 500) {
                                            message = mContext.getResources().getString(R.string.client_error);
                                        } else if (responseObject.optInt("response_code") >= 500 && responseObject.optInt("response_code") < 600) {
                                            message = mContext.getResources().getString(R.string.server_error);
                                        }
                                        getApiResult.onFailureResult(message, responseObject.optInt("response_code"));
                                    }
                                }
                            }else {
                                getApiResult.onFailureResult(responseObject.optString("message"), EXCEPTION_ERROR);
                            }
                        }
                    });
                }
            }).start();
        }
    }

    public static void postData(final Context mContext, final boolean showProgress, final String url, final JSONObject params,
                                final GetApiResult getApiResult) {

        hideDialog(showProgress);
        showDialog(mContext, showProgress);
        showNetworkDialog(mContext, () -> new Thread(() -> {

            final JSONObject responseObject;
           // LogUtil.e(TAG, "run: "+ params.toString()+" url: "+url );
            responseObject = JSONParser.doPostData(params, url);
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideDialog(showProgress);
                    JSONObject data=decryptData(responseObject.optString("Data"));
                    if (responseObject.optBoolean("status")) {
                        if(data.optString("code").equalsIgnoreCase("1000")){
                            logoutUser(mContext);
                        }
                        else {
                            if ((responseObject.optBoolean("isSuccessful") || responseObject.optBoolean("isRedirect"))) {
                                getApiResult.onSuccessResult(data, responseObject.optInt("response_code"));
                            }else {
                                String message = mContext.getResources().getString(R.string.unexpected_error);
                                if (responseObject.optInt("response_code") >= 400 && responseObject.optInt("response_code") < 500) {
                                    message = mContext.getResources().getString(R.string.client_error);
                                } else if (responseObject.optInt("response_code") >= 500 && responseObject.optInt("response_code") < 600) {
                                    message = mContext.getResources().getString(R.string.server_error);
                                }
                                getApiResult.onFailureResult(message, responseObject.optInt("response_code"));
                            }
                        }
                    } else {
                        //getApiResult.onFailureResult(responseObject.optString("message"), EXCEPTION_ERROR);
                        getApiResult.onSuccessResult(responseObject, responseObject.optInt("response_code"));
                    }
                }
            });
        }).start());
    }

    public static void postDataFile(final Context mContext, final boolean showProgress, final String url, final HashMap<String, String> params,
                                    final File image, final String image_name, final GetApiResult getApiResult) {

        hideDialog(showProgress);
        showDialog(mContext, showProgress);
        showNetworkDialog(mContext, () -> new Thread(() -> {

            final JSONObject responseObject;
            //LogUtil.e(TAG, "run: "+ params.toString()+" url: "+url );
            responseObject = JSONParser.doPostRequestWithFile(params, url, image, image_name);
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideDialog(showProgress);
                    JSONObject data=decryptData(responseObject.optString("Data"));
                    if (responseObject.optBoolean("status")) {
                        if(data.optString("code").equalsIgnoreCase("1000")){
                            logoutUser(mContext);
                        }
                        else {
                            if ((responseObject.optBoolean("isSuccessful") || responseObject.optBoolean("isRedirect"))) {
                                //if (responseObject.has("Data")){
                                // LogUtil.e(TAG, "data: " + responseObject);
                                getApiResult.onSuccessResult(data, responseObject.optInt("response_code"));
                                //  }

                            } else {
                                String message = mContext.getResources().getString(R.string.unexpected_error);
                                if (responseObject.optInt("response_code") >= 400 && responseObject.optInt("response_code") < 500) {
                                    message = mContext.getResources().getString(R.string.client_error);
                                } else if (responseObject.optInt("response_code") >= 500 && responseObject.optInt("response_code") < 600) {
                                    message = mContext.getResources().getString(R.string.server_error);
                                }
                                getApiResult.onFailureResult(message, responseObject.optInt("response_code"));
                            }
                        }

                    }
                    else {
                        //getApiResult.onFailureResult(responseObject.optString("message"), EXCEPTION_ERROR);
                        getApiResult.onSuccessResult(responseObject, responseObject.optInt("response_code"));
                    }
                }
            });
        }).start());
    }


}