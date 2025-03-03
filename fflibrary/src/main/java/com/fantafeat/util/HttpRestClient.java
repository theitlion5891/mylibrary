package com.fantafeat.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.Activity.LoginActivity;
import com.fantafeat.Activity.LudoContestListActivity;
import com.fantafeat.Activity.LudoTournamentActivity;
import com.fantafeat.Activity.RummyContestActivity;
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
                //FCMHandler.disableFCM();
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
                ((Activity)mContext).finish();
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

    private static void logoutUser(final Context mContext,String title,String message){

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        TextView btn_ok;
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_session_out, null);
        btn_ok = (TextView) view.findViewById(R.id.btn_ok);
        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        txtTitle.setText(title);
        TextView txtMsg = (TextView) view.findViewById(R.id.txtMsg);
        txtMsg.setText(message);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                // MyApp.getMyPreferences().logout();
                MyApp.getMyPreferences().setPref(PrefConstant.APP_IS_LOGIN,false);
                //FCMHandler.disableFCM();
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
                ((Activity)mContext).finish();

            }
        });

        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);
        if (!bottomSheetDialog.isShowing()){
            bottomSheetDialog.show();
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
            LogUtil.e(TAG, "run: "+ params.toString()+" url: "+url ); //to check what I send to server
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
                            if (mContext instanceof LudoContestListActivity
                                    || mContext instanceof LudoTournamentActivity
                                    || mContext instanceof RummyContestActivity)
                                getApiResult.onSuccessResult(data, data.optInt("code"));
                            isLogoutDialog=false;
                            logoutUser(mContext);
                        }else if (data.optString("code").equalsIgnoreCase("1002")){
                            //logoutUser(mContext,"User Saftey first","We have update security in our App. So, You have to login again");
                            showOtpScreen(mContext,data);
                        }else if (data.optString("code").equalsIgnoreCase("2000")){
                            maintanceDialog(mContext,data);
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
        LogUtil.e(TAG, "run: "+ params.toString()+" url: "+url ); //to check what I send to server
        responseObject = JSONParser.doPost(mCrypt.Encrypt(params.toString()), url);
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideDialog(showProgress);
                JSONObject data=decryptData(responseObject.optString("Data"));
                //LogUtil.e(TAG, "output: "+ data); //to check what I get from server
                if (responseObject.optBoolean("status")) {
                    if(data.optString("code").equalsIgnoreCase("1000")){
                        if (mContext instanceof LudoContestListActivity
                                || mContext instanceof LudoTournamentActivity
                                || mContext instanceof RummyContestActivity)
                            getApiResult.onSuccessResult(data, data.optInt("code"));
                        isLogoutDialog=false;
                        logoutUser(mContext);
                    }else if (data.optString("code").equalsIgnoreCase("1002")){
                        //logoutUser(mContext,"User Saftey first","We have update security in our App. So, You have to login again");
                        showOtpScreen(mContext,data);
                    }else if (data.optString("code").equalsIgnoreCase("2000")){
                        maintanceDialog(mContext,data);
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
            LogUtil.e(TAG, "run: "+ params.toString()+" url: "+url ); //to check what I send to server
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
                            if (mContext instanceof LudoContestListActivity
                                    || mContext instanceof LudoTournamentActivity
                                    || mContext instanceof RummyContestActivity)
                                getApiResult.onSuccessResult(data, data.optInt("code"));
                            isLogoutDialog=false;
                            logoutUser(mContext);
                        }else if (data.optString("code").equalsIgnoreCase("1002")){
                            //logoutUser(mContext,"User Saftey first","We have update security in our App. So, You have to login again");
                            showOtpScreen(mContext,data);
                        }else if (data.optString("code").equalsIgnoreCase("2000")){
                            maintanceDialog(mContext,data);
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
                                    if (mContext instanceof LudoContestListActivity
                                            || mContext instanceof LudoTournamentActivity
                                            || mContext instanceof RummyContestActivity)
                                        getApiResult.onSuccessResult(data, data.optInt("code"));
                                    isLogoutDialog=false;
                                    logoutUser(mContext);
                                }
                               /* else if (data.optString("code").equalsIgnoreCase("1001")){
                                    logoutUser(mContext,"User Saftey first","We have update security in our App. So, You have to login again");
                                }*/
                                else if (data.optString("code").equalsIgnoreCase("1002")){
                                    //logoutUser(mContext,"User Saftey first","We have update security in our App. So, You have to login again");
                                    showOtpScreen(mContext,data);
                                }
                                else if (data.optString("code").equalsIgnoreCase("2000")){
                                    maintanceDialog(mContext,data);
                                }
                               /* else if (data.optString("code").equalsIgnoreCase("1003")){//Force Update
                                    appUpdateDialog(mContext,data);
                                }*/
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
            LogUtil.e(TAG, "run: "+ params.toString()+" url: "+url );
            responseObject = JSONParser.doPostData(params, url);
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideDialog(showProgress);
                    JSONObject data=decryptData(responseObject.optString("Data"));
                    if (responseObject.optBoolean("status")) {
                        if (data.optString("code").equalsIgnoreCase("1002")){
                            showOtpScreen(mContext,data);
                        }
                        else if(data.optString("code").equalsIgnoreCase("1000")){
                            if (mContext instanceof LudoContestListActivity
                                    || mContext instanceof LudoTournamentActivity
                                    || mContext instanceof RummyContestActivity)
                                getApiResult.onSuccessResult(data, data.optInt("code"));
                            isLogoutDialog=false;
                            logoutUser(mContext);
                        }else if (data.optString("code").equalsIgnoreCase("2000")){
                            maintanceDialog(mContext,data);
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

        /*showDialog(mContext, showProgress);
        if (!CustomUtil.checkConnection(mContext)) {
            hideDialog(showProgress);
            getApiResult.onFailureResult(mContext.getResources().getString(R.string.internet_error), NETWORK_ERROR);
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final JSONObject responseObject;
                    responseObject = JSONParser.doPostData(params, url);
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideDialog(showProgress);
                            JSONObject data=decryptData(responseObject.optString("Data"));
                            if (responseObject.optBoolean("status")) {
                                if (data.optString("code").equalsIgnoreCase("1002")){
                                    showOtpScreen(mContext,data);
                                }
                                else if(data.optString("code").equalsIgnoreCase("1000")){
                                    logoutUser(mContext);
                                }else if (data.optString("code").equalsIgnoreCase("2000")){
                                    maintanceDialog(mContext,data);
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
                                getApiResult.onFailureResult(responseObject.optString("message"), EXCEPTION_ERROR);
                            }
                        }
                    });
                }
            }).start();
        }*/
    }

    public static void postDataFile(final Context mContext, final boolean showProgress, final String url, final HashMap<String, String> params,
                                    final File image, final String image_name, final GetApiResult getApiResult) {

        hideDialog(showProgress);
        showDialog(mContext, showProgress);
        showNetworkDialog(mContext, () -> new Thread(() -> {

            final JSONObject responseObject;
            LogUtil.e(TAG, "run: "+ params.toString()+" url: "+url );
            responseObject = JSONParser.doPostRequestWithFile(params, url, image, image_name);
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideDialog(showProgress);
                    JSONObject data=decryptData(responseObject.optString("Data"));
                    if (responseObject.optBoolean("status")) {
                        if (data.optString("code").equalsIgnoreCase("1002")){
                            showOtpScreen(mContext,data);
                        }
                        else if(data.optString("code").equalsIgnoreCase("1000")){
                            if (mContext instanceof LudoContestListActivity
                                    || mContext instanceof LudoTournamentActivity
                                    || mContext instanceof RummyContestActivity)
                                getApiResult.onSuccessResult(data, data.optInt("code"));
                            isLogoutDialog=false;
                            logoutUser(mContext);
                        }
                        else if (data.optString("code").equalsIgnoreCase("2000")){
                            maintanceDialog(mContext,data);
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

    private static void showOtpScreen(Context mContext, JSONObject data) {
        //{"status":true,"data":{"id":"122","token_no":"OVNiS1lnVmNFbHVzeE9qV0U1TzhLNGdHL0hYMzA5b1BzZG1CZXhmakhmST0=","phone_no":"8758879558"},"msg":"","code":"1002"}
        LogUtil.e("logresp",data.toString()+"    ");
        MyApp.USER_ID = data.optJSONObject("data").optString("id");
        MyApp.APP_KEY = data.optJSONObject("data").optString("token_no");


        View view = ((Activity)mContext).getLayoutInflater().inflate(R.layout.fragment_otp, null);
        BottomSheetDialog otpDialog = new BottomSheetDialog(mContext);
        otpDialog.setCancelable(false);
        otpDialog.setContentView(view);
        otpDialog.show();
        //OtpView otp_numbers=view.findViewById(R.id.otp_numbers);
        TextView txtResendCnt=view.findViewById(R.id.txtResendCnt);
        startTimer(txtResendCnt);
        ImageView imgClose=view.findViewById(R.id.imgClose);
        Button otp_btn=view.findViewById(R.id.otp_btn);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otpDialog.dismiss();
            }
        });
        txtResendCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtResendCnt.getText().toString().equals("Resend")){
                    if (MyApp.getClickStatus()){
                        if (timerr!=null){
                            timerr.cancel();
                            timerr=null;
                        }
                       // resendOtp(mContext,txtResendCnt);
                        //showResendDialog(mContext,txtResendCnt);
                        if (MyApp.getMyPreferences().getUpdateMaster()!=null && !TextUtils.isEmpty(MyApp.getMyPreferences().getUpdateMaster().getIs_whatsapp_enable()) &&
                                MyApp.getMyPreferences().getUpdateMaster().getIs_whatsapp_enable().equalsIgnoreCase("yes")){
                            showResendDialog(mContext,txtResendCnt);
                        }else {
                            resendOtp(mContext,txtResendCnt);
                        }
                    }
                }
            }
        });

        otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()){
                    /*if (!TextUtils.isEmpty(otp_numbers.getText())){
                        checkOtp(mContext,otp_numbers.getText().toString(),otpDialog);
                    }*/
                }
            }
        });
    }

    private static void showResendDialog(Context mContext, TextView txtResendCnt){
        Dialog dialog=new Dialog(mContext);
        dialog.setContentView(R.layout.otp_medium);

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout layWp=dialog.findViewById(R.id.layWp);
        LinearLayout layText=dialog.findViewById(R.id.layText);
        TextView btnCancel=dialog.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        layWp.setOnClickListener(v -> {
            if (MyApp.getClickStatus()) {
                dialog.dismiss();
                resendWPOtp(mContext,txtResendCnt);
            }
        });

        layText.setOnClickListener(v -> {
            if (MyApp.getClickStatus()) {
                dialog.dismiss();
                resendOtp(mContext,txtResendCnt);
            }
        });

        dialog.show();
    }

    private static void checkOtp(Context mContext, String otp, BottomSheetDialog otpDialog) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("otp", otp);
            jsonObject.put("user_id", MyApp.USER_ID);
            jsonObject.put("token_no", MyApp.APP_KEY);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e("TAG", "Param: " + jsonObject.toString());
        HttpRestClient.postJSON(mContext, true, ApiManager.AUTHV3OTP_VERIFY, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("TAG", "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {

                    if (otpDialog!=null && otpDialog.isShowing()){
                        otpDialog.dismiss();
                    }

                    Gson gson = new Gson();

                    JSONObject data = responseBody.optJSONObject("data");
                    UserModel userModel = gson.fromJson(data.toString(), UserModel.class);
                    MyApp.USER_ID = userModel.getId();
                    MyApp.APP_KEY = userModel.getTokenNo();
                    MyApp.getMyPreferences().setUserModel(userModel);
                    MyApp.getMyPreferences().setPref(PrefConstant.APP_IS_LOGIN,true);

                    Intent intent = new Intent(mContext, HomeActivity.class);
                    mContext.startActivity(intent);
                    ((Activity)mContext).finish();

                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private static void resendOtp(Context mContext, TextView txtResendCnt) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MyApp.USER_ID);
            jsonObject.put("token_no",MyApp.APP_KEY);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.AUTHV3_Resend_OTP_VERIFY, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    CustomUtil.showTopSneakSuccess(mContext, responseBody.optString("msg"));
                    JSONObject data = responseBody.optJSONObject("data");
                    MyApp.USER_ID = data.optString("id");
                    MyApp.APP_KEY = data.optString("token_no");
                    startTimer(txtResendCnt);
                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private static void resendWPOtp(Context mContext, TextView txtResendCnt) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MyApp.USER_ID);
            jsonObject.put("token_no",MyApp.APP_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.LOGIN_OTP_WP_RESEND, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    CustomUtil.showTopSneakSuccess(mContext, responseBody.optString("msg"));
                    JSONObject data = responseBody.optJSONObject("data");
                    MyApp.USER_ID = data.optString("id");
                    MyApp.APP_KEY = data.optString("token_no");
                    startTimer(txtResendCnt);
                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private static void startTimer(TextView txtResendCnt) {

        if (timerr!=null){
            timerr.cancel();
            timerr=null;
        }
        timerr=new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished / 1000;
                txtResendCnt.setText("00:"+ millis) ;
            }

            @Override
            public void onFinish() {
                txtResendCnt.setText("Resend");
            }
        };

        timerr.start();
    }


}