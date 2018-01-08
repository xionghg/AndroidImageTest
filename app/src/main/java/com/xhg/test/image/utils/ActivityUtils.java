package com.xhg.test.image.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.Objects;


/**
 * @author xionghg
 * @created 17-7-20.
 */

public class ActivityUtils {

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        Objects.requireNonNull(fragmentManager);
        Objects.requireNonNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addNonUIFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                                  @NonNull Fragment nonUIFragment, String fragmentTag) {
        Objects.requireNonNull(fragmentManager);
        Objects.requireNonNull(nonUIFragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(nonUIFragment, fragmentTag);
        transaction.commit();
    }
}
