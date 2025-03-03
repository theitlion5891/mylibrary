package com.fantafeat.util;

import android.util.Base64;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MCrypt {
    private static final String TAG = "MCrypt";
    private String IVKey;
    private String secretKey;
    private boolean changeData = true;
    public MCrypt(){
        String type=getType();
        String type2=getType2();
        String bb=getBB();
        String var=getVar();
        String key = stringFromJNIKey();
        String key1 = key + bb;
        String key2 = key1 + var;

        secretKey = SHAConvert(key1,type).substring(0,16);
        IVKey = SHAConvert(key2,type2).substring(0,16);
    }

    public String Encrypt(String data){
        String encrypt= "";
        try {
            if(changeData) {
                String openSSL = EncryptOPENSSL(data, secretKey, IVKey);
                encrypt = EncryptBASE64(openSSL);
            }else{
                return  data;
            }
        }catch (Exception e){
            LogUtil.e(TAG,"ERROR : " + e.toString());
        }
        return encrypt;

    }

    public String Encrypt(String data,String key) {
        String encrypt = "";
        try {
            if (changeData) {
                secretKey = SHAConvert(key+getBB(), getType()).substring(0, 16);
                IVKey = SHAConvert(key+getBB()+getVar(), getType2()).substring(0, 16);

                String openSSL = EncryptOPENSSL(data, secretKey, IVKey);
                encrypt = EncryptBASE64(openSSL);
                return encrypt;
            } else {
                return data;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "ERROR : " + e.toString());
            return data;
        }

    }

    public String Decrypt(String data){
        String decrypt="";
        try {
            if(changeData) {
                String DEBASE64 = DecryptBASE64(data);
                decrypt = DecryptOPENSSL(DEBASE64, secretKey, IVKey);
            }else{
                return data;
            }
        }catch (Exception e){
            LogUtil.e(TAG,"ERROR : " + e.toString());
        }
        return decrypt;

    }

    private String SHAConvert(String data, String format){
        MessageDigest md = null;
        StringBuilder sb = null;
        try {
            md = MessageDigest.getInstance(format);
            byte[] digest = md.digest(data.getBytes());
            sb = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG,"ERROR : " + e.toString());
        }
        return String.valueOf(sb);
    }

    private String EncryptOPENSSL(String data, String AES_API_KEY, String AES_API_IV) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(AES_API_KEY.getBytes(), "AES");
            byte[] finalIvs = new byte[16];
            int len = AES_API_IV.getBytes().length > 16 ? 16 : AES_API_IV.getBytes().length;
            System.arraycopy(AES_API_IV.getBytes(), 0, finalIvs, 0, len);
            IvParameterSpec ivps = new IvParameterSpec(finalIvs);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivps);
            return Base64.encodeToString(cipher.doFinal(data.getBytes()), Base64.DEFAULT).trim().replaceAll("\n", "").replaceAll("\r", "");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, "encrypt: " + e.toString());
        }
        return "";
    }

    private String EncryptBASE64(String data){
        String base = "";
        try {
            base = new String(Base64.encode(data.getBytes(), 0));
        }catch (Exception e){
            LogUtil.e(TAG,"ERROR : " + e.toString());
        }
        return base;
    }

    private String DecryptOPENSSL(String data, String AES_API_KEY, String AES_API_IV){
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(AES_API_KEY.getBytes(), "AES");
            byte[] finalIvs = new byte[16];
            int len = AES_API_IV.getBytes().length > 16 ? 16 : AES_API_IV.getBytes().length;
            System.arraycopy(AES_API_IV.getBytes(), 0, finalIvs, 0, len);
            IvParameterSpec ivps = new IvParameterSpec(finalIvs);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivps);
            return new String(cipher.doFinal(Base64.decode(data, Base64.DEFAULT)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private String DecryptBASE64(String data){
        String base = "";
        try {
            base = new String(Base64.decode(data, 0));
        }catch (Exception e){
            LogUtil.e(TAG,"ERROR : " + e.toString());
        }
        return  base;

    }

    public native String stringFromJNIKey();
    private native String getType();
    private native String getType2();
    private native String getBB();
    private native String getVar();
}
