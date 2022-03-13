package com.example.twitter;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private SharedPreferences shr;
    private final String Sharedpref = "sharedpref";

    public static final String EMAIL = "text";
    public static final String NAME = "text1";
    public static final String Switch = "click";

    private static SharedPref instance;

    public static SharedPref getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPref(context.getApplicationContext());
        }
        return instance;
    }

    private SharedPref(Context context) {
        shr = context.getSharedPreferences(Sharedpref, Context.MODE_PRIVATE);
    }

    public void saveString(String savedVariableName, String value) {
        SharedPreferences.Editor editor = shr.edit();
        editor.putString(savedVariableName, value);
        editor.apply();
    }

    public void saveBoolean(String savedVariableName, boolean value) {
        SharedPreferences.Editor editor = shr.edit();
        editor.putBoolean(savedVariableName, value);
        editor.apply();
    }

    public void clearData() {
        SharedPreferences.Editor editor = shr.edit();
        editor.clear();
        editor.apply();
    }

    public String loadStringData(String savedVariableName) {
        return shr.getString(savedVariableName, null);
    }

    public boolean loadBooleanData(String savedVariableName) {
        return shr.getBoolean(savedVariableName, false);
    }


}
