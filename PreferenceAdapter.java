package com.example.appdevynmustard;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceAdapter {
    private static final String PREF_FILE_NAME = "permission_value";

    // Save the boolean value
    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    // Retrieve the bool value
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, defaultValue);
    }
}