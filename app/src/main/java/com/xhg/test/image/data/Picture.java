package com.xhg.test.image.data;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xhg.test.image.strategies.ColorStrategy;

/**
 * @author xionghg
 * @email xiong9394@gmail.com
 * @created 2017-07-19.
 */

public class Picture {

    @NonNull
    private String mId;

    @Nullable
    private final String mTitle;

    @Nullable
    private String mDescription;

    private ColorStrategy mStrategy;

    private Bitmap mBitmap;

    public Picture(String title, String description) {
        mTitle = title;
        mDescription = description;
    }

    public Picture(String title, String description, String id) {
        mTitle = title;
        mDescription = description;
        mId = id;
    }

    public void recyvle() {
        mBitmap = null;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public ColorStrategy getStrategy() {
        return mStrategy;
    }
}
