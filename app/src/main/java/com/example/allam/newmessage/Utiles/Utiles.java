package com.example.allam.newmessage.Utiles;

import android.content.Context;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;

import java.text.SimpleDateFormat;


public class Utiles {

    public static final String LOG_TAG = "LogTagTest";
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("EEE - dd MMM  h:mm a");
    public static final String NOTIFICATION_ID = "is_on";
    public static final String NUMBER_ID = "number";
    public static final String USERNAME = "username";

    public final static int SMS_PERMESSION_REQUEST_CODE = 1;
    public static boolean checkPermession(Context context, String permission){
        return ContextCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean IsNotificationOn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(NOTIFICATION_ID, true);
    }
    public static void setNotificationOn(Context context, boolean isOn) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(NOTIFICATION_ID, isOn)
                .apply();
    }

    public static String getPhoneNumber(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(NUMBER_ID, null);
    }
    public static void setPhoneNumber(Context context, String  number) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(NUMBER_ID, number)
                .apply();
    }

    public static String getUserName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(USERNAME, getPhoneNumber(context));
    }
    public static void setUserName(Context context, String  number) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(USERNAME, number)
                .apply();
    }
}

