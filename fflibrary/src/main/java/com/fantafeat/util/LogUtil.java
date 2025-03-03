package com.fantafeat.util;

import android.util.Log;

import org.json.JSONObject;

public class LogUtil {

    public static boolean isLog = false;

    public static void e(String tag, String message){
        try {
            if (isLog)
                Log.e(tag, message);
        } catch (Exception e){
            e.printStackTrace();
            Log.e(tag, "e: " + e );
        }
    }

    public static void e(String tag, Boolean message){
        try {
            if (isLog)
                Log.e(tag, "" + message);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void e(String tag, JSONObject message){
        try {
            if (isLog)
                Log.e(tag, "" + message.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void e(String tag, Integer message){
        try {
            if (isLog)
                Log.e(tag, "" + message);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

    public static void d(String tag, String message){
        try {
            if (isLog)
                Log.d(tag, message);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

    public static void i(String tag, String message){
        try {
            if (isLog)
                Log.i(tag, message);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

    public static void v(String tag, String message){
        try {
            if (isLog)
                Log.v(tag, message);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }
}
