package com.xhg.test.image.utils;

import android.text.TextUtils;

import java.util.Collection;

/**
 * @author xionghg
 * @created 2017-07-22.
 */

public class CheckUtils {

    private CheckUtils() {
        // Utility class.
    }

//    public static <T> T requireNonNull(T arg) {
//        Objects.requireNonNull(arg);
//        return requireNonNull(arg, "Argument must not be null");
//    }

//    public static <T> T requireNonNull(T arg, String message) {
//        if (arg == null) {
//            throw new NullPointerException(message);
//        }
//        return arg;
//    }

    public static String checkNotEmpty(String string) {
        if (TextUtils.isEmpty(string)) {
            throw new IllegalArgumentException("Must not be null or empty");
        }
        return string;
    }

    public static <T extends Collection<Y>, Y> T checkNotEmpty(T collection) {
        if (collection.isEmpty()) {
            throw new IllegalArgumentException("Must not be empty.");
        }
        return collection;
    }
}
