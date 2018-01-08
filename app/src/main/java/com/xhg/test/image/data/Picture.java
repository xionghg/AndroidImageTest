package com.xhg.test.image.data;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xhg.test.image.strategies.ColorStrategy;

/**
 * @author xionghg
 * @created 2017-07-19.
 */

public class Picture {

    @NonNull
    private final int mId;

    @Nullable
    private final String mTitle;

    private final ColorStrategy mStrategy;

    private Bitmap mBitmap;

    public Picture(int id, ColorStrategy colorStrategy) {
        mId = id;
        mTitle = "Picture" + id;
        mStrategy = colorStrategy;
    }

    public void recycle() {
        mBitmap = null;
    }

    @NonNull
    public int getId() {
        return mId;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    public ColorStrategy getStrategy() {
        return mStrategy;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }
}
