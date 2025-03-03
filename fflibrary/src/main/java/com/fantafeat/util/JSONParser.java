package com.fantafeat.util;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.fantafeat.BuildConfig;
import com.fantafeat.Session.MyApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JSONParser {

    public static JSONObject doGet(HashMap<String, String> param, String url) {
        JSONObject result = null;
        String response;
        Set keys = param.keySet();

        TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
        Calendar c = Calendar.getInstance(tz);
        long current=c.getTimeInMillis();

        int count = 0;
        for (Iterator i = keys.iterator(); i.hasNext(); ) {
            count++;
            String key = (String) i.next();
            String value = (String) param.get(key);
            if (count == param.size()) {
                LogUtil.e("Key", key + "");
                LogUtil.e("Value", value + "");
                url += key + "=" + URLEncoder.encode(value);

            } else {
                LogUtil.e("Key", key + "");
                LogUtil.e("Value", value + "");

                url += key + "=" + URLEncoder.encode(value) + "&";
            }
        }
        LogUtil.e("URL", url);
        OkHttpClient client = new OkHttpClient();

        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("mobile_type", MyApp.deviceType);
        headersMap.put("mobile_os",MyApp.deviceVersion);
        headersMap.put("phone_uid",MyApp.deviceId);
        headersMap.put("mobile_hardware",MyApp.deviceHardware);
        headersMap.put("mobile_name",MyApp.deviceName);
        headersMap.put("app_ver", String.valueOf(MyApp.current_version));
        headersMap.put("user_id", MyApp.USER_ID);
        headersMap.put("uenc_id", getEncryptedUserId(current));
        headersMap.put("request_time", current+"");
        headersMap.put("token_no", MyApp.APP_KEY);
        headersMap.put("fcm_id", MyApp.tokenNo);
        headersMap.put("comp_id", ConstantUtil.COMPANY_ID);
        LogUtil.e("headers",headersMap+" Data");
        Headers header = Headers.of(headersMap);
        Request request;
        try {
            request = new Request.Builder()
                    .headers(header)
                    .url(url)
                    .build();

        } catch (IllegalArgumentException e) {
            JSONObject jsonObject = new JSONObject();
            return jsonObject;
        }

        Response responseClient = null;
        try {
            responseClient = client.newCall(request).execute();
            response = responseClient.body().string();

            result = new JSONObject(response);
            LogUtil.e("response", response + "==============");
        } catch (Exception e) {
            JSONObject jsonObject = new JSONObject();

            return jsonObject;
        }
        return result;
    }

    public static String getEncryptedUserId(long current){

        MCrypt crypt=new MCrypt();
        String user_id="";
        user_id=crypt.Encrypt(MyApp.USER_ID,current+"");
        return user_id;
    }

    public static JSONObject doPost(String data, String url) {


        //LogUtil.e("URL", url+" ------- "+data.trim());
        try {
            MediaType JSON = MediaType.parse("text/plain; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, data.trim());

            TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
            Calendar c = Calendar.getInstance(tz);
            long current=c.getTimeInMillis();

            Map<String, String> headersMap = new HashMap<>();
            headersMap.put("mobile_type", MyApp.deviceType);
            headersMap.put("mobile_os",MyApp.deviceVersion);
            headersMap.put("phone_uid",MyApp.deviceId);
            headersMap.put("mobile_hardware",MyApp.deviceHardware);
            headersMap.put("mobile_name",MyApp.deviceName);
            headersMap.put("app_ver", String.valueOf(MyApp.current_version));
            headersMap.put("user_id", MyApp.USER_ID);
            headersMap.put("token_no", MyApp.APP_KEY);
            headersMap.put("uenc_id", getEncryptedUserId(current));
            headersMap.put("request_time", current+"");
            headersMap.put("fcm_id",MyApp.tokenNo);
            headersMap.put("user_header_key",MyApp.user_header_key);
            headersMap.put("bundle_id", BuildConfig.APPLICATION_ID);
            headersMap.put("lat",MyApp.lat);
            headersMap.put("lng",MyApp.lng);
            headersMap.put("comp_id", ConstantUtil.COMPANY_ID);
            //headersMap.put("abc",String.valueOf(System.currentTimeMillis()));

            LogUtil.e("headers",headersMap+" Data");

            Headers header = Headers.of(headersMap);
            Request request = new Request.Builder()
                    .url(url)
                    .headers(header)
                    .post(body)
                    .build();
            OkHttpClient client = new OkHttpClient();
            /*OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(10, TimeUnit.MINUTES)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .build();*/

            Response response = client.newCall(request).execute();

            String data1 = response.body().string();

            LogUtil.e("response",data1+" Data123 "+url);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", true);
            jsonObject.put("response_code", response.code());
            jsonObject.put("isSuccessful",response.isSuccessful());
            jsonObject.put("isRedirect",response.isRedirect());
            jsonObject.put("Data", data1);

            return jsonObject;

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("status", false);
                jsonObject.put("message", e.getLocalizedMessage()/*"Something went wrong, Please try after sometime."*/);
                jsonObject.put("msg", "Something went wrong, Please try after sometime.");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            LogUtil.e("", "Error: " + jsonObject.toString() + "\n" + e.getLocalizedMessage());
            return jsonObject;
        } catch (Exception e) {

            // e.printStackTrace();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("status", false);
                jsonObject.put("message", e.getLocalizedMessage()/*"Something went wrong, Please try after sometime."*/);
                jsonObject.put("msg", "Something went wrong, Please try after sometime.");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            LogUtil.e("", "Other Error: " + jsonObject.toString() + "\n" + e.getLocalizedMessage());
            return jsonObject;
        }
    }

    static JSONObject doPostRequestWithFileWithVideo(HashMap<String, String> data,
                                                     String url,
                                                     File image,
                                                     String fileParamName) {

        try {


            //final File imageFile = new File(uriToFilename(uri));
            //Uri uris = Uri.fromFile(image);
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(image.getPath());
            String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
            String imageName = image.getName();

            // String mime=getMimeType(image);
            //final MediaType MEDIA_TYPE_PNG = MediaType.parse(mime);//"image/jpg"

            LogUtil.e("Method", "======= mime: "+mime+"\nfileExtension: "+fileExtension);

            MultipartBody.Builder mBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            for (String key : data.keySet()) {
                String value = data.get(key);
                LogUtil.e("Key Values", key + "-----------------" + value);

                mBuilder.addFormDataPart(key, value);
            }
            if (image.exists()) {
                LogUtil.e("File Exits", "==================="+imageName);
                //MultipartBody.Part vFile = MultipartBody.Part.createFormData(fileParamName, image.getName(),  RequestBody.create(MediaType.parse(mime), image));
                mBuilder.addFormDataPart(fileParamName, imageName,
                        RequestBody.create(MediaType.parse(mime), image));

            }

            RequestBody requestBody = mBuilder.build();

            TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
            Calendar c = Calendar.getInstance(tz);
            long current=c.getTimeInMillis();

            Map<String, String> headersMap = new HashMap<>();
            headersMap.put("mobile_type", MyApp.deviceType);
            headersMap.put("mobile_os",MyApp.deviceVersion);
            headersMap.put("phone_uid",MyApp.deviceId);
            headersMap.put("mobile_hardware",MyApp.deviceHardware);
            headersMap.put("mobile_name",MyApp.deviceName);
            headersMap.put("app_ver", String.valueOf(MyApp.current_version));
            headersMap.put("user_id", MyApp.USER_ID);
            headersMap.put("token_no", MyApp.APP_KEY);
            headersMap.put("uenc_id", getEncryptedUserId(current));
            headersMap.put("request_time", current+"");
            headersMap.put("fcm_id",MyApp.tokenNo);
            headersMap.put("user_header_key",MyApp.user_header_key);
            headersMap.put("bundle_id", BuildConfig.APPLICATION_ID);
            headersMap.put("lat",MyApp.lat);
            headersMap.put("lng",MyApp.lng);
            headersMap.put("comp_id", ConstantUtil.COMPANY_ID);
            //headersMap.put("abc",String.valueOf(System.currentTimeMillis()));

            LogUtil.e("headers",headersMap+" Data");

            Headers header = Headers.of(headersMap);

            Request request = new Request.Builder()
                    .url(url)
                    .headers(header)
                    .post(requestBody)
                    .build();

            /*OkHttpClient client = new OkHttpClient().newBuilder()
                    .readTimeout(60, TimeUnit.MINUTES)
                    .writeTimeout(60, TimeUnit.MINUTES)
                    .build();*/
            //OkHttpClient client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.MINUTES)
                    .readTimeout(15, TimeUnit.MINUTES)
                    .writeTimeout(60, TimeUnit.MINUTES)
                    .build();

            Response response = client.newCall(request).execute();

            assert response.body() != null;
            String result = response.body().string();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", true);
            jsonObject.put("response_code", response.code());
            jsonObject.put("isSuccessful",response.isSuccessful());
            jsonObject.put("isRedirect",response.isRedirect());
            jsonObject.put("Data", result);

            //LogUtil.e("File Exits", "===================>"+jsonObject);
            return jsonObject;

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            LogUtil.e("TAG", "Error: " + e.getLocalizedMessage());
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("status", false);
                jsonObject.put("message", "Something went wrong, Please try after sometime.");
                jsonObject.put("is_string",false);
                return jsonObject;

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            LogUtil.e("TAG", "Other Error: " + e.getMessage());
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("status", false);
                jsonObject.put("message", "Something went wrong, Please try after sometime.");
                jsonObject.put("is_string",false);
                return jsonObject;
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public static JSONObject doPostNormal(String data, String url) {


       // LogUtil.e("URL", url);
        try {
            MediaType JSON = MediaType.parse("text/plain; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, data.trim());

            //LogUtil.e("USER", "doPost: " + data.trim());
            TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
            Calendar c = Calendar.getInstance(tz);
            long current=c.getTimeInMillis();

            Map<String, String> headersMap = new HashMap<>();
            headersMap.put("mobile_type", MyApp.deviceType);
            headersMap.put("mobile_os",MyApp.deviceVersion);
            headersMap.put("phone_uid",MyApp.deviceId);
            headersMap.put("mobile_hardware",MyApp.deviceHardware);
            headersMap.put("mobile_name",MyApp.deviceName);
            headersMap.put("app_ver", String.valueOf(MyApp.current_version));
            headersMap.put("user_id", MyApp.USER_ID);
            headersMap.put("uenc_id", getEncryptedUserId(current));
            headersMap.put("request_time", current+"");
            headersMap.put("token_no", MyApp.APP_KEY);
            headersMap.put("fcm_id",MyApp.tokenNo);
            headersMap.put("user_header_key",MyApp.user_header_key);
            headersMap.put("lat",MyApp.lat);
            headersMap.put("lng",MyApp.lng);
            headersMap.put("comp_id", ConstantUtil.COMPANY_ID);
            headersMap.put("bundle_id", BuildConfig.APPLICATION_ID);
            //headersMap.put("abc",String.valueOf(System.currentTimeMillis()));

            LogUtil.e("headers",headersMap+" Data");

            Headers header = Headers.of(headersMap);
            Request request = new Request.Builder()
                    .url(url)
                    .headers(header)
                    .post(body)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(10, TimeUnit.MINUTES)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();

            String data1 = response.body().string();



            JSONObject jsonObject = new JSONObject(data1.trim());

            jsonObject.put("response_code", response.code());
            jsonObject.put("isSuccessful",response.isSuccessful());
            jsonObject.put("isRedirect",response.isRedirect());

            LogUtil.e("response",jsonObject+" Data");

            return jsonObject;

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("status", false);
                jsonObject.put("message", "Something went wrong, Please try after sometime.");
                jsonObject.put("msg", "Something went wrong, Please try after sometime.");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            LogUtil.e("", "Error: " + jsonObject.toString() + "\n" + e.getLocalizedMessage());
            return jsonObject;
        } catch (Exception e) {

            // e.printStackTrace();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("status", false);
                jsonObject.put("message", "Something went wrong, Please try after sometime.");
                jsonObject.put("msg", "Something went wrong, Please try after sometime.");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            LogUtil.e("", "Other Error: " + jsonObject.toString() + "\n" + e.getLocalizedMessage());
            return jsonObject;
        }
    }

    public static JSONObject doPostData(JSONObject data, String url) {
        LogUtil.e("URL", url);
        try {
            //MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            //RequestBody body = RequestBody.create(JSON, data);
            Iterator<String> temp = data.keys();
            FormBody.Builder formBody = new FormBody.Builder();

            while (temp.hasNext()) {
                String key = temp.next();
                String value = data.optString(key);
                formBody.add(key, value);
                LogUtil.e("Data", "" + key + " -> " + value);
            }
            RequestBody body = formBody.build();

            TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
            Calendar c = Calendar.getInstance(tz);
            long current=c.getTimeInMillis();

            Map<String, String> headersMap = new HashMap<>();
            headersMap.put("mobile_type", MyApp.deviceType);
            headersMap.put("mobile_os",MyApp.deviceVersion);
            headersMap.put("phone_uid",MyApp.deviceId);
            headersMap.put("mobile_hardware",MyApp.deviceHardware);
            headersMap.put("mobile_name",MyApp.deviceName);
            headersMap.put("app_ver", String.valueOf(MyApp.current_version));
            headersMap.put("user_id", MyApp.USER_ID);
            headersMap.put("uenc_id", getEncryptedUserId(current));
            headersMap.put("request_time", current+"");
            headersMap.put("token_no", MyApp.APP_KEY);
            headersMap.put("fcm_id",MyApp.tokenNo);
            headersMap.put("user_header_key",MyApp.user_header_key);
            headersMap.put("lat",MyApp.lat);
            headersMap.put("lng",MyApp.lng);
            headersMap.put("comp_id", ConstantUtil.COMPANY_ID);
            headersMap.put("bundle_id", BuildConfig.APPLICATION_ID);
           // headersMap.put("abc",String.valueOf(System.currentTimeMillis()));

            LogUtil.e("headers",headersMap+" Data");
            Headers header = Headers.of(headersMap);
            Request request = new Request.Builder()
                    .url(url)
                    .headers(header)
                    .post(body)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(10, TimeUnit.MINUTES)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();


            String data1 = response.body().string();

           // LogUtil.e("Data", "" + data1);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", true);
            jsonObject.put("response_code", response.code());
            jsonObject.put("isSuccessful",response.isSuccessful());
            jsonObject.put("isRedirect",response.isRedirect());
            jsonObject.put("Data", data1);
            return jsonObject;

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("status", false);
                jsonObject.put("message", "Something went wrong, Please try after sometime.");
                jsonObject.put("msg", "Something went wrong, Please try after sometime.");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            LogUtil.e("", "Error: " + jsonObject.toString() + "\n" + e.getLocalizedMessage());
            return jsonObject;
        } catch (Exception e) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("status", false);
                jsonObject.put("message", "Something went wrong, Please try after sometime.");
                jsonObject.put("msg", "Something went wrong, Please try after sometime.");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            LogUtil.e("", "Other Error: " + jsonObject.toString() + "\n" + e.getLocalizedMessage());
            return jsonObject;
        }
    }

    static JSONObject doPostRequestWithFile(HashMap<String, String> data,
                                            String url,
                                            File image,
                                            String fileParamName) {
        LogUtil.e("URL", url);
        try {
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");

            LogUtil.e("Method", "=======");
            RequestBody requestBody;

            MultipartBody.Builder mBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            for (String key : data.keySet()) {
                String value = data.get(key);
                LogUtil.e("Key Values", key + "-----------------" + value);

                mBuilder.addFormDataPart(key, value);
            }
            if (image.exists()) {
                //image.getPath().substring(image.getPath().lastIndexOf(".") + 1)
                LogUtil.e("File Exits", "===================");
                mBuilder.addFormDataPart(fileParamName, image.getName(), RequestBody.create(MEDIA_TYPE_PNG, image));
            }

            requestBody = mBuilder.build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(10, TimeUnit.MINUTES)
                    .build();

            Response response = client.newCall(request).execute();

            String result = response.body().string();

            //LogUtil.e("response",result+" Data");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", true);
            jsonObject.put("response_code", response.code());
            jsonObject.put("isSuccessful",response.isSuccessful());
            jsonObject.put("isRedirect",response.isRedirect());
            jsonObject.put("Data", result);

            return jsonObject;

        }
        catch (UnknownHostException | UnsupportedEncodingException e) {
            LogUtil.e("TAG", "Error: " + e.getLocalizedMessage());
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("status", false);
                jsonObject.put("message", "Something went wrong, Please try after sometime.");
                jsonObject.put("msg", "Something went wrong, Please try after sometime.");
                return jsonObject;

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        catch (Exception e) {
            LogUtil.e("TAG", "Other Error: " + e.getMessage());
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("status", false);
                jsonObject.put("message", "Something went wrong, Please try after sometime.");
                jsonObject.put("msg", "Something went wrong, Please try after sometime.");
                return jsonObject;
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public static File saveBitmapToFile(File file) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }

}