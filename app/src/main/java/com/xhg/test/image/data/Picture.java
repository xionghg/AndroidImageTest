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
    private String mTitle;

    private ColorStrategy mStrategy;

    private Bitmap mBitmap;

    public Picture(String id, ColorStrategy colorStrategy) {
        mId = id;
        mStrategy = colorStrategy;
    }

    public Picture(String id, String title ) {
        mId = id;
        mTitle = title;
    }

    public void recycle() {
        mBitmap = null;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    public void setId(@NonNull String id) {
        mId = id;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(@Nullable String title) {
        mTitle = title;
    }

    public ColorStrategy getStrategy() {
        return mStrategy;
    }

    public void setStrategy(ColorStrategy strategy) {
        mStrategy = strategy;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }
}
