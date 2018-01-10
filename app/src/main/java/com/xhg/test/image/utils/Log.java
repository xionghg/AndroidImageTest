package com.xhg.test.image.utils;

import android.text.TextUtils;

/**
 * @author xionghg
 * @created 18-1-10.
 */

public class Log {
    private static final String APP_TAG = "ImageTest";

    private static final int VERBOSE = android.util.Log.VERBOSE;
    private static final int DEBUG   = android.util.Log.DEBUG;
    private static final int INFO    = android.util.Log.INFO;
    private static final int WARN    = android.util.Log.WARN;
    private static final int ERROR   = android.util.Log.ERROR;

    public static final int LOG_LEVEL = DEBUG;

    public static int v(String tag, String msg) {
        if (LOG_LEVEL <= VERBOSE && !TextUtils.isEmpty(msg)) {
            return android.util.Log.v(APP_TAG, "[" + tag + "]:" + msg);
        }
        return 0;
    }

    public static int v(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL <= VERBOSE && !TextUtils.isEmpty(msg)) {
            return android.util.Log.v(APP_TAG, "[" + tag + "]:" + msg, tr);
        }
        return 0;
    }

    public static int d(String tag, String msg) {
        if (LOG_LEVEL <= DEBUG && !TextUtils.isEmpty(msg)) {
            return android.util.Log.d(APP_TAG, "[" + tag + "]:" + msg);
        }
        return 0;
    }

    public static int d(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL <= DEBUG && !TextUtils.isEmpty(msg)) {
            return android.util.Log.d(APP_TAG, "[" + tag + "]:" + msg, tr);
        }
        return 0;
    }

    public static int i(String tag, String msg) {
        if (LOG_LEVEL <= INFO && !TextUtils.isEmpty(msg)) {
            return android.util.Log.i(APP_TAG, "[" + tag + "]:" + msg);
        }
        return 0;
    }

    public static int i(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL <= INFO && !TextUtils.isEmpty(msg)) {
            return android.util.Log.i(APP_TAG, "[" + tag + "]:" + msg, tr);
        }
        return 0;
    }

    public static int w(String tag, String msg) {
        if (LOG_LEVEL <= WARN && !TextUtils.isEmpty(msg)) {
            return android.util.Log.w(APP_TAG, "[" + tag + "]:" + msg);
        }
        return 0;
    }

    public static int w(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL <= WARN && !TextUtils.isEmpty(msg)) {
            return android.util.Log.w(APP_TAG, "[" + tag + "]:" + msg, tr);
        }
        return 0;
    }

    public static int e(String tag, String msg) {
        if (LOG_LEVEL <= ERROR && !TextUtils.isEmpty(msg)) {
            return android.util.Log.e(APP_TAG, "[" + tag + "]:" + msg);
        }
        return 0;
    }

    public static int e(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL <= ERROR && !TextUtils.isEmpty(msg)) {
            return android.util.Log.e(APP_TAG, "[" + tag + "]:" + msg, tr);
        }
        return 0;
    }
}
