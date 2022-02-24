package com.jk.codez;

import android.app.Activity;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class Settings {
    private static SharedPreferences prefs;

    public static void load(@NonNull Activity a) {
        prefs = a.getPreferences(0);
        if (!prefs.contains("LAST_CONNECTION")) {
            prefs.edit().putString("LAST_CONNECTION", "none").apply();
        }
        if (!prefs.contains("DBTYPE")) {
            prefs.edit().putString("DBTYPE", "local").apply();
        }
    }

    public static String getLastConnection() {
        return prefs.getString("LAST_CONNECTION", "none");
    }

    public static void setLastConnection(String conn) {
        prefs.edit().putString("LAST_CONNECTION", conn).apply();
    }

    public static String getDbType() {
        return prefs.getString("DBTYPE", "local");
    }

    public static void setDbType(String dbType) {
        prefs.edit().putString("DBTYPE", dbType).apply();
    }
}
