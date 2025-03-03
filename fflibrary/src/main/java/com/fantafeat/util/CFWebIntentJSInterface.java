package com.fantafeat.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class CFWebIntentJSInterface {
    private final CFWebIntentInterface cfWebIntentInterface;

    public CFWebIntentJSInterface(CFWebIntentInterface cfWebIntentInterface) {
        this.cfWebIntentInterface = cfWebIntentInterface;
    }

    public interface CFWebIntentInterface {
        List<ResolveInfo> getAppList(String link);

        String getAppName(ApplicationInfo pkg);

        void openApp(String appPkg, String url);

        void enableDisableCancelButton(Boolean flag);

        void getResult(String result);
    }


    @android.webkit.JavascriptInterface
    public void getResult(String result){
        LogUtil.e("openApp","openApp: "+result);
        cfWebIntentInterface.getResult(result);
    }

    @android.webkit.JavascriptInterface
    public void enableCancelButton() {
        this.cfWebIntentInterface.enableDisableCancelButton(true);
    }

    @android.webkit.JavascriptInterface
    public void disableCancelButton() {
        this.cfWebIntentInterface.enableDisableCancelButton(false);
    }

    @android.webkit.JavascriptInterface
    public String getAppList(String name) {
        final List<ResolveInfo> resInfo = cfWebIntentInterface.getAppList(name);
        JSONArray packageNames = new JSONArray();
        try {
            for (ResolveInfo info : resInfo) {
                JSONObject appInfo = new JSONObject();
                appInfo.put("appName", cfWebIntentInterface.getAppName(info.activityInfo.applicationInfo));
                appInfo.put("appPackage", info.activityInfo.packageName);
                packageNames.put(appInfo);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return packageNames.toString();
    }

    @android.webkit.JavascriptInterface
    public boolean openApp(String upiClientPackage, String upiURL) {
        cfWebIntentInterface.openApp(upiClientPackage, upiURL);
        return true;
    }

}
