package com.fantafeat.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fantafeat.BuildConfig;
import com.fantafeat.Model.Games;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.LogUtil;

import java.util.Set;

public class WebViewGameActivity extends BaseActivity {

    private String webUrl="",con_id="";
    private Games game;
    private WebView webGame;

    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        setContentView(R.layout.activity_web_view_game);
        initData();
    }

    private void initData() {

        webGame = findViewById(R.id.webGame);

        //setStrictMode();

        if (getIntent().hasExtra("con_id")){
            con_id=getIntent().getStringExtra("con_id");
        }

        if (getIntent().hasExtra("gameData")){
            game = (Games) getIntent().getSerializableExtra("gameData");

            if (ConstantUtil.is_game_test){
                webUrl=game.getAndroidAssetUrlTest()+"?name="+preferences.getUserModel().getUserTeamName()+"&con_id="+con_id+"&user_id="+
                        preferences.getUserModel().getId()+"&nocache";
                //webUrl="https://stackoverflow.com/";
            }else {
                webUrl=game.getAndroidAssetUrl()+"?name="+preferences.getUserModel().getUserTeamName()+"&con_id="+con_id+"&user_id="+
                        preferences.getUserModel().getId()+"&nocache";
            }
        }

        /*webGame.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                LogUtil.d("WebView", "your current url when webpage loading.." + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                LogUtil.d("WebView", "your current url when webpage loading.. finish: " + url);

                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onLoadResource(view, url);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.e("WebView","when you click on any interlink on webview that time you got url :-" + url);
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });*/

        // webGame.setMixedContentAllowed(false);
        webGame.getSettings().setUseWideViewPort(true);
        webGame.setVerticalScrollBarEnabled(false);
        webGame.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

       // webGame.getSettings().setAppCacheEnabled(false);

        webGame.getSettings().setJavaScriptEnabled(true);
        webGame.getSettings().setDomStorageEnabled(true);
        webGame.getSettings().setPluginState(WebSettings.PluginState.ON);
        webGame.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webGame.getSettings().setAllowFileAccess(true);
        webGame.getSettings().setLoadWithOverviewMode(true);
        webGame.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webGame.getSettings().setAllowContentAccess(true);
        webGame.getSettings().setDomStorageEnabled(true);
        webGame.setWebChromeClient(new WebChromeClient());

        webGame.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtil.e("resp",url);
                if (url.contains("result.html")){
                    Uri uri = Uri.parse(url);
                    String server = uri.getAuthority();
                    String path = uri.getPath();
                    String protocol = uri.getScheme();
                    Set<String> args = uri.getQueryParameterNames();

                    boolean is_win = uri.getBooleanQueryParameter("is_win",false);

                    showFinishDialog(is_win);
                    // finish();
                }
            }
        });

        // webGame.clearCache(true);
        // webGame.clearHistory();
        webGame.addJavascriptInterface(new WebViewJavaScriptInterface(this), "app");

        webGame.postDelayed(new Runnable() {

            @Override
            public void run() {
                webGame.loadUrl(webUrl);
            }
        }, 100);

    }

    private void showFinishDialog(boolean is_win){

        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_webview_result);

        Button imgYes = dialog.findViewById(R.id.btnYes);
        ImageView imgResult = dialog.findViewById(R.id.imgResult);

        if (is_win){
            imgResult.setImageResource(R.drawable.youwin);
        }
        else {
            imgResult.setImageResource(R.drawable.youloss);
        }

        imgYes.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent=getIntent();
            intent.putExtra("is_webview","yes");
            intent.putExtra("con_id",con_id);
            setResult(RESULT_OK,intent);
            finish();
        });

        dialog.show();

       /* String title="";
        DialogType dialogType;

        if (is_win){
            title="You Won this game";
            dialogType=DialogType.SUCCESS;
        }
        else {
            title="You Loss this game";
            dialogType=DialogType.ERROR;
        }

        new AestheticDialog.Builder(this, DialogStyle.FLAT, dialogType)
                .setTitle(title)
                .setMessage("")
                .setCancelable(false)
                .setDarkMode(true)
                .setGravity(Gravity.CENTER)
                .setAnimation(DialogAnimation.CARD)
                .setOnClickListener(builder -> {
                    builder.dismiss();
                    finish();
                })
                .show();*/
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        webGame.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        webGame.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        webGame.destroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // webGame.onActivityResult(requestCode, resultCode, intent);
    }
    public class WebViewJavaScriptInterface{

        private Context context;

        /*
         * Need a reference to the context in order to sent a post message
         */
        public WebViewJavaScriptInterface(Context context){
            this.context = context;
        }

        /*
         * This method can be called from Android. @JavascriptInterface
         * required after SDK version 17.
         */
        @JavascriptInterface
        public void makeToast(String message, boolean lengthLong){
            Toast.makeText(context, message, (lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)).show();
        }
    }

    private void exitGameDialog(){
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setMessage("Are you sure, You want to exit? You may lose your contest amount!");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent=getIntent();
                intent.putExtra("is_webview","yes");
                intent.putExtra("con_id",con_id);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        exitGameDialog();
    }

    private void setStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()   // detectDiskReads, detectDiskWrites, detectNetwork
                    .penaltyLog()
                    .build());

            StrictMode.setVmPolicy(getStrictModeBuilder().build());
        }
    }

    private StrictMode.VmPolicy.Builder getStrictModeBuilder() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();

        builder.detectLeakedSqlLiteObjects();

        final int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.HONEYCOMB) {
            builder.detectActivityLeaks();
            builder.detectLeakedClosableObjects();
        }

        if (sdkInt >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.detectLeakedRegistrationObjects();
        }

        if (sdkInt >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        if (sdkInt >= Build.VERSION_CODES.O) {
            builder.detectContentUriWithoutPermission();
        }

        builder.penaltyLog().penaltyDeath();
        return builder;
    }

}