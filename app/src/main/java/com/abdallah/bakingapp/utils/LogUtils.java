package com.abdallah.bakingapp.utils;

import android.util.Log;

import com.abdallah.bakingapp.BuildConfig;

/**
 * Utility class provides additional features on top of normal Log class.
 *
 * This class will disable DEBUG & VERBOSE logs on release builds.
 * For other log levels, it's safe to use the normal Log class methods.
 * @author Abdallah Abdelazim
 */
public final class LogUtils {

    private LogUtils() {/* Prevent instantiation*/}

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg, tr);
        }
    }

    public static void d(String tag, Object obj) {
        d(tag, obj.toString());
    }

    public static void d(String tag, Object obj, Throwable tr) {
        d(tag, obj.toString(), tr);
    }


    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, msg, tr);
        }
    }

    public static void v(String tag, Object obj) {
        v(tag, obj.toString());
    }

    public static void v(String tag, Object obj, Throwable tr) {
        v(tag, obj.toString(), tr);
    }

}
