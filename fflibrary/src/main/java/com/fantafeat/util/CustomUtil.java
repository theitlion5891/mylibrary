package com.fantafeat.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.tapadoo.alerter.Alerter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class CustomUtil extends BaseActivity {

    public static void showSnackBarLong(Context context, String message) {
        Snackbar.make(((Activity) context).getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG).show();
       /* Sneaker.with((Activity)context)
                .setIcon(R.drawable.ic_error, R.color.colorWhite, false)
                .setMessage(message, R.color.colorBlack)
                .sneak(ResourcesCompat.getColor(context.getResources(), R.color.yellow_dark, null));*/
    }

    public static void showSnackBarShort(Context context, String message) {
        Snackbar.make(((Activity) context).getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT)
                .show();
    }

    public static String dateConvertWithFormat(String date,String outFormat){
        //2021-02-12 11:11:58
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
        SimpleDateFormat output = new SimpleDateFormat(outFormat,Locale.getDefault());
        Date dt;
        try {
            dt=input.parse(date);
            return output.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String dateConvertWithFormat(String date,String outFormat,String inputFormat){
        //2021-02-12 11:11:58
        SimpleDateFormat input = new SimpleDateFormat(inputFormat,Locale.getDefault());
        SimpleDateFormat output = new SimpleDateFormat(outFormat,Locale.getDefault());
        Date dt;
        try {
            dt=input.parse(date);
            return output.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isEventEnd(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
        Date strDate = null;
        try {
            strDate = sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return System.currentTimeMillis() > strDate.getTime();
    }
    public static String coolFormat(Context mContext, String sAmount) {
        float number=CustomUtil.convertFloat(sAmount);
        DecimalFormat df = new DecimalFormat("0.00");
        // df.setRoundingMode(RoundingMode.DOWN);
        if (number >= 10000000) // 1 crore
            return df.format(number / 10000000)+" Cr";//String.format("%.2f Cr", number / 10000000);
        else if (number >= 100000) // 1 lakh
            return df.format(number / 100000)+" L";//String.format("%.2f L", number / 100000);
    /*    else if (number >= 1000) // 1 thousand
            return df.format(number / 1000)+" K";//String.format("%.2f K", number / 1000);*/
        else
            return sAmount;
    }

    public static String getAppDirectory(Context context){
        return context.getFilesDir().getAbsolutePath()+"/";
        /*PackageManager m = context.getPackageManager();
        String s = context.getPackageName();

        try {
            PackageInfo p = m.getPackageInfo(s, 0);
            s = p.applicationInfo.dataDir+"/games/";
           // Log.e("appDir", "Path: "+s);
            return s;
        } catch (PackageManager.NameNotFoundException e) {
            //Log.w("yourtag", "Error Package name not found ", e);
            return "";
        }*/

    }

    public static String printDifferenceAgo(String start) {

        Date startDate, endDate = new  Date();
        String diff = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            startDate = simpleDateFormat.parse(start);

            long different = endDate.getTime() - startDate.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;


            if (elapsedDays > 0) {
                if (elapsedDays<=2) {
                    if (elapsedDays==1)
                        diff = String.valueOf(elapsedDays) + " day ago ";
                    else
                        diff = String.valueOf(elapsedDays) + " days ago ";
                }
                else {
                    diff=new SimpleDateFormat("MMM dd, yy hh:mm:ss").format(startDate);
                }
            } else {
                if (elapsedHours > 0) {
                    diff = String.valueOf(elapsedHours) + " hours ago ";
                }else {
                    if (elapsedMinutes > 0) {
                        diff = String.valueOf(elapsedMinutes) + " minutes ago ";
                    }else {
                        if (elapsedSeconds > 0) {
                            diff = String.valueOf(elapsedSeconds) + " seconds ago ";
                        }else {
                            diff =  "just now";
                        }
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return diff;
    }
    public static DecimalFormat getFormater(String pattern){
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat format = (DecimalFormat) nf;
        format.applyPattern(pattern);
        return format;
    }

    public static String dateConvert(String date){
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.US);
        SimpleDateFormat output = new SimpleDateFormat("dd MMM,yy hh:mm a",Locale.getDefault());
        Date dt;
        try {
            dt=input.parse(date);
            return output.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String dateTimeConvert(String date){
        //2021-02-12 11:11:58
        SimpleDateFormat input = MyApp.changedFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat output = MyApp.changedFormat("dd MMM, yy hh:mm a");
        Date dt;
        try {
            dt=input.parse(date);
            return output.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static int getAge(String dobString){
       // Date date = null;
        SimpleDateFormat sdf = MyApp.changedFormat("yyyy-MM-dd");
        int age = 0;
        try {
            Date date1 = sdf.parse(dobString);
            Calendar now = Calendar.getInstance();
            Calendar dob = Calendar.getInstance();
            dob.setTime(date1);
            if (dob.after(now)) {
                throw new IllegalArgumentException("Can't be born in the future");
            }
            int year1 = now.get(Calendar.YEAR);
            int year2 = dob.get(Calendar.YEAR);
            age = year1 - year2;
            int month1 = now.get(Calendar.MONTH);
            int month2 = dob.get(Calendar.MONTH);
            if (month2 > month1) {
                age--;
            } else if (month1 == month2) {
                int day1 = now.get(Calendar.DAY_OF_MONTH);
                int day2 = dob.get(Calendar.DAY_OF_MONTH);
                if (day2 > day1) {
                    age--;
                }
            }
        } catch (ParseException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return age ;
    }

    public static <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
        return iterable == null ? Collections.<T>emptyList() : iterable;
    }

    public static void showTopSneakError(Context mContext, String message) {

        Alerter.create(((Activity) mContext))
                .setText(message)
                .setBackgroundColorRes(R.color.red)
                .hideIcon()
                .show();
        /*Sneaker.with((Activity) mContext)
                .setMessage(message)
                .sneakError();*/
    }

    public static void showTopSneakWarning(Context mContext, String message) {


        Alerter.create(((Activity) mContext))
                //.setTitle(title)
                .setText(message)
                .setBackgroundColorRes(R.color.yellow_dark)
                .hideIcon()
                .show();
       /* Sneaker.with((Activity) mContext)
                .setMessage(message)
                .sneakWarning();*/
    }

    public static void loadImageWithGlide(Context context, CircleImageView view, String imageBase, String imageName, int placeholder){
        // imageName="";
        //LogUtil.e("loadedimage",imageBase+""+imageName);
        MatchModel matchModel=MyApp.getMyPreferences().getMatchModel();

        if (matchModel!=null && !TextUtils.isEmpty(matchModel.getIs_male()) && matchModel.getIs_male().equalsIgnoreCase("no")){
            if (placeholder==R.drawable.ic_team1_tshirt)
                placeholder=R.drawable.ic_team1_tshirt_female;
            if (placeholder==R.drawable.ic_team2_tshirt)
                placeholder=R.drawable.ic_team2_tshirt_female;

            if (placeholder==R.drawable.football_player1)
                placeholder=R.drawable.football_player1_female;
            if (placeholder==R.drawable.football_player2)
                placeholder=R.drawable.football_player2_female;

            if (placeholder==R.drawable.basketball_team1)
                placeholder=R.drawable.basketball_team1_female;
            if (placeholder==R.drawable.basketball_team2)
                placeholder=R.drawable.basketball_team2_female;

            if (placeholder==R.drawable.baseball_player1)
                placeholder=R.drawable.baseball_player1_female;
            if (placeholder==R.drawable.baseball_player2)
                placeholder=R.drawable.baseball_player2_female;

            if (placeholder==R.drawable.kabaddi_player1)
                placeholder=R.drawable.kabaddi_player1_female;
            if (placeholder==R.drawable.kabaddi_player2)
                placeholder=R.drawable.kabaddi_player2_female;

            if (placeholder==R.drawable.handball_player1)
                placeholder=R.drawable.kabaddi_player1_female;
            if (placeholder==R.drawable.handball_player2)
                placeholder=R.drawable.kabaddi_player2_female;
        }

        try {
            if (imageName != null && !imageName.isEmpty() && !imageName.equals("null")){
                if (URLUtil.isValidUrl(imageName.trim())){//Patterns.WEB_URL.matcher(imageName).matches()
                    //LogUtil.e("loadedimage",imageName);
                    Glide.with(context)
                            .load(imageName.trim())
                            .placeholder(placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(placeholder)
                            .into(view);
                }
                else {
                    if (URLUtil.isValidUrl((imageBase+imageName).trim())){
                        //LogUtil.e("loadedimage",imageBase+""+imageName);
                        Glide.with(context)
                                .load((imageBase+imageName).trim())
                                .placeholder(placeholder)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .error(placeholder)
                                .into(view);
                    }else {
                        view.setImageResource(placeholder);
                    }
                }
            }else {
             //   LogUtil.e("loadedimage",imageBase+""+imageName+" else");
                view.setImageResource(placeholder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImageWithGlide(Context context, ImageView view, String imageBase, String imageName, int placeholder){
        // imageName="";

        MatchModel matchModel=MyApp.getMyPreferences().getMatchModel();

        if (matchModel!=null && !TextUtils.isEmpty(matchModel.getIs_male()) && matchModel.getIs_male().equalsIgnoreCase("no")){
            //LogUtil.e("loadedimage",matchModel.getIs_male());
            if (placeholder==R.drawable.ic_team1_tshirt)
                placeholder=R.drawable.ic_team1_tshirt_female;
            if (placeholder==R.drawable.ic_team2_tshirt)
                placeholder=R.drawable.ic_team2_tshirt_female;

            if (placeholder==R.drawable.football_player1)
                placeholder=R.drawable.football_player1_female;
            if (placeholder==R.drawable.football_player2)
                placeholder=R.drawable.football_player2_female;

            if (placeholder==R.drawable.basketball_team1)
                placeholder=R.drawable.basketball_team1_female;
            if (placeholder==R.drawable.basketball_team2)
                placeholder=R.drawable.basketball_team2_female;

            if (placeholder==R.drawable.baseball_player1)
                placeholder=R.drawable.baseball_player1_female;
            if (placeholder==R.drawable.baseball_player2)
                placeholder=R.drawable.baseball_player2_female;

            if (placeholder==R.drawable.kabaddi_player1)
                placeholder=R.drawable.kabaddi_player1_female;
            if (placeholder==R.drawable.kabaddi_player2)
                placeholder=R.drawable.kabaddi_player2_female;

            if (placeholder==R.drawable.handball_player1)
                placeholder=R.drawable.kabaddi_player1_female;
            if (placeholder==R.drawable.handball_player2)
                placeholder=R.drawable.kabaddi_player2_female;
        }

        try {
            if (imageName != null && !imageName.isEmpty() && !imageName.equals("null")){
              //  LogUtil.e("loadedimage",imageBase+""+imageName+" if");
                if (URLUtil.isValidUrl(imageName.trim())){//Patterns.WEB_URL.matcher(imageName).matches()
                    //LogUtil.e("loadedimage",imageName);
                    Glide.with(context)
                            .load(imageName.trim())
                            .placeholder(placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(placeholder)
                            .into(view);
                }
                else {
                    if (URLUtil.isValidUrl((imageBase+imageName).trim())){
                        //LogUtil.e("loadedimage",imageBase+""+imageName);
                        Glide.with(context)
                                .load((imageBase+imageName).trim())
                                .placeholder(placeholder)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .error(placeholder)
                                .into(view);
                    }else {
                        view.setImageResource(placeholder);
                    }
                }
            }else {
               // LogUtil.e("loadedimage",imageBase+""+imageName+" else");
                view.setImageResource(placeholder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String cleanUpCommas(String string) {
        Splitter splitter = Splitter.on(',').omitEmptyStrings().trimResults();
        Joiner joiner = Joiner.on(',').skipNulls();
        return joiner.join(splitter.split(string));
    }



    public static byte[] getByteImageFromURL(String url) {
        LogUtil.e("nikhil_image_check",url);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            is = new URL(url).openStream ();
            byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
            int n;

            while ( (n = is.read(byteChunk)) > 0 ) {
                baos.write(byteChunk, 0, n);
            }
            return baos.toByteArray();
        }
        catch (IOException e) {
            //System.err.printf ("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
            e.printStackTrace ();
            // Perform any other exception handling that's appropriate.
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        /*Bitmap bmp = loadBitmap(url);//w w w.  ja v a 2s.c o m
        if (bmp != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            bmp.recycle();
            return stream.toByteArray();
        }*/
        return null;
    }

    public static void showTopSneakSuccess(Context mContext, String message) {
        Alerter.create(((Activity) mContext))
                .setText(message)
                .setBackgroundColorRes(R.color.green_pure)
                .hideIcon()
                .show();
       /* Sneaker.with((Activity) mContext)
                .setMessage(message)
                .sneakSuccess();*/
    }

    public static float convertFloat(String value){

        if(value != null && !value.equals("null") && !value.equals("")){
            try {
                //LogUtil.e("resval",value+"   "+Float.parseFloat(value));
                return Float.parseFloat(value);
            }catch (Exception e) {
                return (float) 0;
            }
        }else {
            return (float) 0;
        }
    }

    public static int convertInt(String value){
        if(value != null && !value.equals("null") && !value.equals("")){
            try {
                return Integer.parseInt(value);
            }catch (Exception e) {
                return 0;
            }
        }else {
            return 0;
        }
    }

    public static double convertDouble(String value){
        if(value != null && !value.equals("null") && !value.equals("")){
            try {
                return Double.parseDouble(value);
            }catch (Exception e) {
                return 0;
            }
        }else {
            return 0;
        }
    }

    public static boolean stringEquals(String compareWith, String compareTo){
        if(compareWith != null && compareTo !=null){
            try {
                return compareWith.equals(compareTo);
            }catch (Exception e){
                return  false;
            }
        }else {
            return false;
        }
    }

    public static boolean stringEqualsIgnore(String compareWith, String compareTo){
        if(compareWith != null && compareTo !=null){
            try {
                return compareWith.equalsIgnoreCase(compareTo);
            }catch (Exception e){
                return  false;
            }
        }else {
            return false;
        }
    }

    public static void checkInternet(final Context mContext, final InternetConnection connection) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            connection.onSuccess();
        }
       /* final ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connection.onSuccess();
        } else {*/
           /* LogUtil.e("", "checkInternet: -> false");
            Snackbar.make(((Activity) mContext).getWindow().getDecorView().getRootView(), "Please Check Internet!", Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkInternet(mContext, connection);
                }
            }).show();*/
//            connection.onSuccess(false);
       // }
    }

    public static boolean checkConnection(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            return true;
        }
        return false;
        /*ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }*/
    }

    public static String replaceNull(String data){
        return data.replace(":null", ":\"\"");
    }

    public static String printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String diff = "";
        if (elapsedDays > 0) {
            diff = String.valueOf(elapsedDays) + "d " + String.valueOf(elapsedHours) + "h Left";
        } else if (elapsedHours > 0) {
            diff = String.valueOf(elapsedHours) + "h " + String.valueOf(elapsedMinutes) + "m";
        } else {
            diff = String.valueOf(elapsedMinutes) + "m " + String.valueOf(elapsedSeconds) + "s";
        }

        return diff;
    }

    public static String displayAmount(Context mContext, String sAmount) {
        if (isString(sAmount)){
            return sAmount;
        }
        int amount = CustomUtil.convertInt(sAmount);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        String displayAmount = "";
        try {
            if (amount < 100000) {
                displayAmount = mContext.getResources().getString(R.string.rs) + new DecimalFormat("#,##,##,###",symbols).format(amount);
            } else if (amount % 10000000 == 0) {
                int a = amount / 10000000;
                displayAmount = mContext.getResources().getString(R.string.rs) + a + " Crore";
            } else if (amount % 100000 == 0 && amount / 100000 == 1) {
                int a = amount / 100000;
                displayAmount = mContext.getResources().getString(R.string.rs) + a + " Lakh";
            } else if (amount % 100000 == 0) {
                int a = amount / 100000;
                displayAmount = mContext.getResources().getString(R.string.rs) + a + " Lakhs";
            } else {
                if (amount % 10000 != 0) {
                    float a = (float) (amount / 100000.0);
                    displayAmount = mContext.getResources().getString(R.string.rs) + new DecimalFormat("0.00",symbols).format(a) + " Lakhs";
                } else {
                    float a = (float) (amount / 100000.0);
                    displayAmount = mContext.getResources().getString(R.string.rs) + new DecimalFormat("0.0",symbols).format(a) + " Lakhs";
                }
            }
        } catch (Exception e) {
            displayAmount = mContext.getResources().getString(R.string.rs) + sAmount;
        }
        return displayAmount;
    }

    public static String displayAmountFloat(Context mContext, String sAmount) {
       // Log.e("resp","Pool1: "+sAmount);
        if (isString(sAmount)){
            return sAmount;
        }
        float amount = CustomUtil.convertFloat(sAmount);
        //Log.e("resp","Pool2: "+amount);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        String displayAmount = "";
        try {
            if (amount < 100000) {
                if (String.valueOf(amount).contains(".")){
                    displayAmount = mContext.getResources().getString(R.string.rs) + new DecimalFormat("#,##,##,###.##",symbols).format(amount);
                }else {
                    displayAmount = mContext.getResources().getString(R.string.rs) + new DecimalFormat("#,##,##,###",symbols).format(amount);
                }
            } else if (amount % 10000000 == 0) {
                int a = (int) (amount / 10000000);
                displayAmount = mContext.getResources().getString(R.string.rs) + a + " Crore";
            } else if (amount % 100000 == 0 && amount / 100000 == 1) {
                int a = (int) (amount / 100000);
                displayAmount = mContext.getResources().getString(R.string.rs) + a + " Lakh";
            } else if (amount % 100000 == 0) {
                int a = (int) (amount / 100000);
                displayAmount = mContext.getResources().getString(R.string.rs) + a + " Lakhs";
            } else {
                if (amount % 10000 != 0) {
                    float a = (float) (amount / 100000.0);
                    displayAmount = mContext.getResources().getString(R.string.rs) + new DecimalFormat("0.00",symbols).format(a) + " Lakhs";
                } else {
                    float a = (float) (amount / 100000.0);
                    displayAmount = mContext.getResources().getString(R.string.rs) + new DecimalFormat("0.0",symbols).format(a) + " Lakhs";
                }
            }
            //Log.e("resp","Pool3: "+displayAmount);

        } catch (Exception e) {
            displayAmount = mContext.getResources().getString(R.string.rs) + sAmount;
            //Log.e("resp","Pool4: "+displayAmount);
        }
        return displayAmount;
    }

    public static boolean isString(String string){
        if(string.matches("\\d+(?:\\.\\d+)?")) {
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void initClick() {

    }
}