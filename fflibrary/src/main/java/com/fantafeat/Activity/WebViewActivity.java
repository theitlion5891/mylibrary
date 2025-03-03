package com.fantafeat.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.JavaScriptInterface;
import com.fantafeat.util.LogUtil;

public class WebViewActivity extends BaseActivity {

    String title,url;
    WebView webView;
    ProgressBar webView_progressBar;
    ImageView toolbar_back;
    TextView toolbar_title;

    @Override
    public void initClick() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        setContentView(R.layout.fragment_web_view);

        toolbar_title=findViewById(R.id.toolbar_title);
        toolbar_back=findViewById(R.id.toolbar_back);

        if (getIntent().hasExtra(ConstantUtil.WEB_TITLE)){
            title=getIntent().getStringExtra(ConstantUtil.WEB_TITLE);
            toolbar_title.setText(title);
            if (TextUtils.isEmpty(title)){
                findViewById(R.id.toolbar).setVisibility(View.GONE);
            }
        }
        if (getIntent().hasExtra(ConstantUtil.WEB_URL)){
            url=getIntent().getStringExtra(ConstantUtil.WEB_URL);
        }

        initData();
    }

    private void initData() {
        webView = findViewById(R.id.webView);
        webView_progressBar = findViewById(R.id.webView_progressBar);
        if (url!=null && url.equals("")) {
            webView.setVisibility(View.GONE);
            webView_progressBar.setVisibility(View.GONE);
        }else{
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webView.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                    webView.loadUrl(JavaScriptInterface.getBase64StringFromBlobUrl(url));
                    LogUtil.e(TAG, "onDownloadStart: " + JavaScriptInterface.getBase64StringFromBlobUrl(url) );
                }
            });
            webView.loadUrl(url);

            webView.setWebViewClient(new CustomWebViewClient());
          //  webSettings.setAppCachePath(mContext.getApplicationContext().getCacheDir().getAbsolutePath());
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            webSettings.setDatabaseEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
            webSettings.setPluginState(WebSettings.PluginState.ON);
        }

        toolbar_back.setOnClickListener(v->{
            finish();
        });
    }

    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {
            webview.setVisibility(webview.INVISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            webView_progressBar.setVisibility(View.GONE);

            view.setVisibility(webView.VISIBLE);
            super.onPageFinished(view, url);

        }
    }
}