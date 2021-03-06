package fr.codazzi.smsonline;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tools {

    /*
        Get the Android_id
     */
    static public String getDeviceID(Context c) {
        String deviceId;
        deviceId = Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID);
        return deviceId;
    }

    /*
        Convert a bitmap image to string (For web view for example)
     */
    public static String bitmapToString64(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=null;
        try{
            System.gc();
            temp=Base64.encodeToString(b, Base64.DEFAULT);
        }catch(Exception e){
            e.printStackTrace();
        }catch(OutOfMemoryError e){
            baos=new  ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,50, baos);
            b=baos.toByteArray();
            temp=Base64.encodeToString(b, Base64.DEFAULT);
            Log.e("EWN", "Out of memory error catched");
        }
        return temp;
    }

    /*
        Return true if the permission is granted, ask the permission on
            Android 6+
     */
    static void getPermission (Activity ac, String permission) {

        if (Build.VERSION.SDK_INT > 22) {
            if (ac.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                ac.requestPermissions(new String[]{permission}, 0);
            }
        }
    }

    static public boolean checkPermission (Context c, String permission) {

        if (Build.VERSION.SDK_INT > 22) {
            if (c.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    static private void logDebug(int type, String tag, String data) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        switch (type) {
            case 0:
                Log.i(tag, data);
                break;
            case 1:
                Log.d(tag, data);
                break;
            case 2:
                Log.w(tag, data);
                break;
            case 3:
                Log.wtf(tag, data);
                break;
            case 4:
                Log.e(tag, data);
                break;
            default:
                Log.e(tag, data);
                break;
        }
    }

//    static public void logDebug(int type, String data) {
//        Tools.logDebug(type, "SWB", data);
//    }

    static public void logDebug(String data) {
        Tools.logDebug(0, "SWB", data);
    }

    static public void storeLog(Context context, String logs) {
        Tools.logDebug(logs);
        SharedPreferences settings = context.getSharedPreferences("swb_logs", 0);
        String old_logs = settings.getString("logs", "");
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        logs = simpleDate.format(new Date()) + ": " + logs + "\n" + old_logs;
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("logs", logs);
        editor.apply();
    }

    static void resetLogs(Context context) {
        SharedPreferences settings = context.getSharedPreferences("swb_logs", 0);
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String logs = simpleDate.format(new Date()) + ": Reset logs \n";
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("logs", logs);
        editor.apply();
    }

    static String getStoreLog(Context context) {
        SharedPreferences settings = context.getSharedPreferences("swb_logs", 0);
        return settings.getString("logs", "");
    }

    static public boolean isInJSONArray(JSONArray arr, int value) {
        try {
            for (int i = 0; i < arr.length(); i++) {
                if (arr.getInt(i) == value) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    static public String getRandomString(int size) {
        String alpha = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(size);
        SecureRandom rnd = new SecureRandom();

        for( int i = 0; i < size; i++ ) {
            sb.append(alpha.charAt(rnd.nextInt(alpha.length())));
        }
        return sb.toString();
    }

    static public int getApiError(int error) {
        switch (error) {
            case 1:
                return R.string.sta_bad_method;
            case 2:
                return R.string.sta_missing_arg;
            case 3:
                return R.string.sta_invalid_mail;
            case 4:
                return R.string.sta_short_password;
            case 6:
                return R.string.sta_bad_credential;
            default:
                return R.string.sta_unknow;
        }
    }
}
