package com.fantafeat.Session;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.fantafeat.BuildConfig;
import com.fantafeat.util.PrefConstant;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import kotlin.jvm.Synchronized;


public class MyApp extends Application {

    static {
        System.loadLibrary("native-lib");
    }

    private static Context appContext;
    private static Toast toast;
    public native String getSocketServer();
    public native String getImageBase();
    public native String getPhonePeMID();
    //static Socket mSocket;
    //public static String URL = "http://13.127.129.218:8090/";
    //public static String URL = "http://192.168.0.117:8090/";

    static long oldSecond = -1;

    private static String spName = "DFCM";
    private static int spMode = 0;
    static SharedPreferences sp = null;
    static SharedPreferences.Editor editor = null;

    private static final String TAG = "MyApp";
    public static SocketIo socketIo;
    private  String URL = getSocketServer();
    private  String IMAGEURL = getImageBase();

    private static MyPreferences myPreferences;

    public static String tokenNo = "", deviceName, deviceBrand, deviceHardware, appCode, deviceVersion,imageBase,
            document="documents/",banner="banner/",user_img="user_img/",lat="",lng="",user_header_key="",
            noti_img="notification_img/",excel_img="Excel/",callback_support="callback_support/",
            deviceId, deviceType;
    public static int current_version;
    public static String USER_ID="",APP_KEY="";
   // private Socket iSocket = null;
    public static MyApp mInstance;
    public static boolean isFestivalShow=false;

    public String app_url="";
  //  private HttpProxyCacheServer proxy;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        appContext = this;
        myPreferences = new MyPreferences(this);

        current_version = BuildConfig.VERSION_CODE;
        appCode = String.valueOf(BuildConfig.VERSION_CODE);
        deviceName = Build.MANUFACTURER + " " + Build.MODEL;
        deviceBrand = Build.BRAND;
        deviceHardware = Build.HARDWARE;

        //PhonePe.init(this, PhonePeEnvironment.RELEASE, getPhonePeMID(), "");//44b0b243823c4222bbe886ac2dd1717d

        //socketIo = new SocketIo();
        //socketIo.connect(getApplicationContext());

       /* if (BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.TEST_APP)){
            ConstantUtil.is_game_test=true;
            ApiManager.BASE=getDashBASEURL();
            ApiManager.V3BASE=getV3DASHBASEURL();
            ApiManager.OPINIOBASEURL=getOpinioDashBaseUrl();
            LogUtil.isLog=true;
        }else {
            ConstantUtil.is_game_test=false;
            ApiManager.BASE=getBASEURL();
            ApiManager.V3BASE=getV3BASEURL();
            ApiManager.OPINIOBASEURL=getOpinioBaseUrl();
            LogUtil.isLog=false;
        }*/

       /* try {
            IO.Options opts =new IO.Options();
            //opts.query = "auth_token=$authToken"
             *//*if (ConstantUtil.is_game_test)
                 iSocket = IO.socket("http://65.1.2.61:2021/", opts);
            else*//*
                iSocket = IO.socket(URL, opts);

            iSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }*/

        if(myPreferences == null){
            Log.e(TAG, "onCreate: preferance");
        }

        if (myPreferences.getUserModel().getId() != null &&
                !myPreferences.getUserModel().getId().equals("")){
            USER_ID = myPreferences.getUserModel().getId();
        }if(myPreferences.getUserModel().getTokenNo() != null &&
                !myPreferences.getUserModel().getTokenNo().equals("")) {
            APP_KEY = myPreferences.getUserModel().getTokenNo();
        }

        deviceVersion = Build.VERSION.RELEASE;
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceType = "Android";
        if (TextUtils.isEmpty(myPreferences.getPrefString(PrefConstant.IMAGE_BASE))){
            imageBase=IMAGEURL;
            myPreferences.setPref(PrefConstant.IMAGE_BASE,IMAGEURL);
            //LogUtil.e("myappbase",imageBase+"  ");
        }else {
            imageBase=myPreferences.getPrefString(PrefConstant.IMAGE_BASE);
        }
        //imageBase=myPreferences.getPrefString(PrefConstant.IMAGE_BASE);
        //LogUtil.e("Serial",deviceId);
       /* if (TextUtils.isEmpty(myPreferences.getPrefString(PrefConstant.IMAGE_BASE))){
            ApiManager.IMGURL=ApiManager.BASE+"myAppImages/";
            myPreferences.setPref(PrefConstant.IMAGE_BASE,ApiManager.IMGURL);
        }else {
            ApiManager.IMGURL=myPreferences.getPrefString(PrefConstant.IMAGE_BASE);
        }*/
       // ApiManager.IMGURL=myPreferences.getPrefString(PrefConstant.IMAGE_BASE);

        //getFCMToken();
        /*FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            LogUtil.d("token","  "+instanceIdResult.getToken());
            if (instanceIdResult!=null && TextUtils.isEmpty(instanceIdResult.getToken())){
                tokenNo=instanceIdResult.getToken();
            }

        });
*/


    }

   /* public static HttpProxyCacheServer getProxy(Context context) {
        MyApp app = (MyApp) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        //return new HttpProxyCacheServer(this);
        return new HttpProxyCacheServer.Builder(this)
                .cacheDirectory(new File(getExternalCacheDir(), "video-cache"))
                .maxCacheFilesCount(40)
                .maxCacheSize(1024 * 1024 * 1024)
                .build();
    }*/

    public static String getImageBaseUrl(){
        if (TextUtils.isEmpty(imageBase))
            return myPreferences.getPrefString(PrefConstant.IMAGE_BASE);
        else
            return imageBase;
    }

    public  String getApp_url() {
        if (TextUtils.isEmpty(app_url) || app_url.equalsIgnoreCase("null")){
            app_url="https://ffcdn.fantafeat.com/apk/fantafeat.apk";
        }
        return app_url;
    }

    public void setApp_url(String app_url) {
        this.app_url = app_url;
    }

    public static void setImageBaseUrl(){
        imageBase=myPreferences.getPrefString(PrefConstant.IMAGE_BASE);
    }

    private void getFCMToken(){
/*        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(tokenNo->{
            if (!TextUtils.isEmpty(tokenNo)) {
                MyApp.tokenNo=tokenNo;
                Log.d(TAG, "retrieve token successful : " + tokenNo);
            } else{
                Log.w(TAG, "token should not be null...");
            }
        });*/
    }

    public static SimpleDateFormat changedFormat(String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        return format;
    }

   /* public Socket getSocketInstance() {
        return iSocket;
    }*/

    @Synchronized
    public static MyApp getInstance() {
        return mInstance;
    }

    public static MyPreferences getMyPreferences() {
        return myPreferences;
    }

    public static Context getContext() {
        return appContext;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }



    public static void toast(CharSequence text, int duration) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        if (appContext != null) {
            toast = Toast.makeText(appContext, text, duration);
            toast.show();
        }
    }

    public static boolean getClickStatus() {

        long time = System.currentTimeMillis();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time);

        if (oldSecond == seconds) {
            return false;
        } else {
            oldSecond = seconds;

            return true;
        }
    }

    public static void setSharedPreferences(String KEY, String VALUES) {
        sp = appContext.getSharedPreferences("" + spName, spMode);
        editor = sp.edit();
        editor.putString("" + KEY, "" + VALUES);
        editor.commit();
    }

    //    public static String getSharedPreferences(Activity activity, String KEY) {
    public static String getSharedPreferences(String KEY) {
        sp = appContext.getSharedPreferences("" + spName, spMode);
        return sp.getString("" + KEY, "");
    }

    // get update server updated time
    public static Date getCurrentDate(){
        Calendar now=Calendar.getInstance();
        //now.setTimeInMillis(SystemClock.elapsedRealtime());
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));

        return now.getTime();
    }



}
